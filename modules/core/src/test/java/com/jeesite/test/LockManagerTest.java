/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test;

import com.jeesite.common.cache.RedisUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lock.LockManager;
import com.jeesite.common.lock.impl.LocalLockManager;
import com.jeesite.common.lock.impl.RedisLockManager;
import com.jeesite.common.tests.BaseSpringContextTests;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 锁管理器测试：本地锁、Redis 分布式锁
 * @author ThinkGem
 * @version 2026-11-21
 */
@ActiveProfiles({"unit_test", "redis"})
@SpringBootTest(classes = ApplicationTest.class)
public class LockManagerTest extends BaseSpringContextTests {

	/**
	 * 生成全局唯一的锁键，避免用例之间互相影响
	 */
	private static String newKey(String prefix) {
		return prefix + ":lock:test:" + UUID.randomUUID();
	}

	/**
	 * 工厂方法选择实现：集群模式走 Redis 分布式锁，否则本地锁
	 */
	@Test
	public void testCreateLockManager() {
		LockManager manager = LockManager.createLockManager();
		boolean clusterMode = "true".equals(Global.getProperty("spring.cache.isClusterMode", "false"));
		if (clusterMode) {
			Assertions.assertTrue(manager instanceof RedisLockManager, "集群模式应使用 Redis 分布式锁");
		} else {
			Assertions.assertTrue(manager instanceof LocalLockManager, "非集群模式应使用本地锁");
		}
		Assertions.assertTrue(LockManager.createLocalLockManager() instanceof LocalLockManager);
		Assertions.assertTrue(LockManager.createRedisLockManager() instanceof RedisLockManager);
	}

	/**
	 * 本地锁测试
	 */
	@Nested
	class LocalLockTest {

		private final LockManager lockManager = new LocalLockManager();

		@Test
		public void testLockAndUnlock() {
			String key = newKey("local");
			lockManager.lock(key);
			Assertions.assertTrue(lockManager.exists(key), "加锁后应存在锁");
			lockManager.unlock(key);
			Assertions.assertFalse(lockManager.exists(key), "解锁后锁应被移除");
		}

		@Test
		public void testMutualExclusion() throws Exception {
			String key = newKey("local");
			ExecutorService pool = Executors.newSingleThreadExecutor();
			try {
				lockManager.lock(key);
				Assertions.assertFalse(pool.submit(() -> lockManager.tryLock(key, 200L)).get(2, TimeUnit.SECONDS),
						"持有期间其他线程不能获取");
				lockManager.unlock(key);
				Assertions.assertTrue(pool.submit(() -> lockManager.tryLock(key, 2000L)).get(3, TimeUnit.SECONDS),
						"释放后其他线程可以获取");
				pool.submit(() -> lockManager.unlock(key)).get(1, TimeUnit.SECONDS);
				Assertions.assertFalse(lockManager.exists(key), "全部释放后锁应被移除");
			} finally {
				pool.shutdownNow();
			}
		}

		@Test
		public void testTryLockTimeout() throws Exception {
			String key = newKey("local");
			ExecutorService pool = Executors.newSingleThreadExecutor();
			try {
				lockManager.lock(key);
				long start = System.currentTimeMillis();
				Assertions.assertFalse(pool.submit(() -> lockManager.tryLock(key, 200L)).get(2, TimeUnit.SECONDS),
						"等待超时应返回 false");
				long cost = System.currentTimeMillis() - start;
				Assertions.assertTrue(cost >= 180 && cost < 2000, "等待时间应约为 200ms，实际：" + cost);
			} finally {
				lockManager.unlock(key);
				pool.shutdownNow();
			}
		}

		@Test
		public void testReentrant() throws Exception {
			String key = newKey("local");
			ExecutorService pool = Executors.newSingleThreadExecutor();
			try {
				lockManager.lock(key);
				lockManager.lock(key);
				Assertions.assertFalse(pool.submit(() -> lockManager.tryLock(key, 200L)).get(2, TimeUnit.SECONDS),
						"重入未完全释放时其他线程不能获取");
				lockManager.unlock(key);
				Assertions.assertFalse(pool.submit(() -> lockManager.tryLock(key, 200L)).get(2, TimeUnit.SECONDS),
						"仍有重入次数时其他线程不能获取");
				lockManager.unlock(key);
				Assertions.assertTrue(pool.submit(() -> lockManager.tryLock(key, 2000L)).get(3, TimeUnit.SECONDS),
						"完全释放后其他线程可以获取");
				pool.submit(() -> lockManager.unlock(key)).get(1, TimeUnit.SECONDS);
				Assertions.assertFalse(lockManager.exists(key));
			} finally {
				pool.shutdownNow();
			}
		}

		@Test
		public void testLeaseTimeoutTakeover() throws Exception {
			String key = newKey("local");
			ExecutorService pool = Executors.newSingleThreadExecutor();
			try {
				// 模拟持有者卡死：加锁后不释放
				lockManager.lock(key, 300L);
				long start = System.currentTimeMillis();
				Assertions.assertTrue(pool.submit(() -> lockManager.tryLock(key, 3000L)).get(4, TimeUnit.SECONDS),
						"超过租期后应自动解锁并可被其他线程获取");
				long cost = System.currentTimeMillis() - start;
				Assertions.assertTrue(cost >= 250, "接管耗时应不小于租期，实际：" + cost);
				pool.submit(() -> lockManager.unlock(key)).get(1, TimeUnit.SECONDS);
				Assertions.assertFalse(lockManager.exists(key));
			} finally {
				pool.shutdownNow();
			}
		}

		@Test
		public void testLeaseTakeoverThenOldHolderUnlock() throws Exception {
			String key = newKey("local");
			ExecutorService poolA = Executors.newSingleThreadExecutor();
			ExecutorService poolB = Executors.newSingleThreadExecutor();
			try {
				poolA.submit(() -> {
					lockManager.lock(key, 300L);
					return null;
				}).get(1, TimeUnit.SECONDS);
				Assertions.assertTrue(lockManager.exists(key));
				Assertions.assertTrue(poolB.submit(() -> lockManager.tryLock(key, 3000L)).get(4, TimeUnit.SECONDS),
						"超过租期后 B 应接管锁");
				poolA.submit(() -> {
					lockManager.unlock(key);
					return null;
				}).get(1, TimeUnit.SECONDS);
				Assertions.assertTrue(lockManager.exists(key), "旧持有者解锁不应释放被接管的锁");
				poolB.submit(() -> {
					lockManager.unlock(key);
					return null;
				}).get(1, TimeUnit.SECONDS);
				Assertions.assertFalse(lockManager.exists(key), "新持有者解锁后锁应被移除");
			} finally {
				poolA.shutdownNow();
				poolB.shutdownNow();
			}
		}

		@Test
		public void testLockUninterruptible() throws Exception {
			String key = newKey("local");
			lockManager.lock(key);
			AtomicBoolean acquired = new AtomicBoolean(false);
			AtomicBoolean interrupted = new AtomicBoolean(false);
			CountDownLatch done = new CountDownLatch(1);
			Thread thread = new Thread(() -> {
				lockManager.lock(key);
				acquired.set(true);
				interrupted.set(Thread.currentThread().isInterrupted());
				lockManager.unlock(key);
				done.countDown();
			});
			try {
				thread.start();
				Thread.sleep(100);
				thread.interrupt();
				Thread.sleep(200);
				Assertions.assertFalse(acquired.get(), "lock 不响应中断，中断后应继续等待");
				lockManager.unlock(key);
				Assertions.assertTrue(done.await(2, TimeUnit.SECONDS), "锁释放后应能获取");
				Assertions.assertTrue(interrupted.get(), "获取锁后应保留中断标志");
			} finally {
				thread.interrupt();
				thread.join(1000);
			}
		}

		@Test
		public void testTryLockInterruptible() throws Exception {
			String key = newKey("local");
			lockManager.lock(key);
			AtomicReference<Boolean> result = new AtomicReference<>(Boolean.TRUE);
			AtomicBoolean interrupted = new AtomicBoolean(false);
			CountDownLatch done = new CountDownLatch(1);
			Thread thread = new Thread(() -> {
				result.set(lockManager.tryLock(key, 5000L));
				interrupted.set(Thread.currentThread().isInterrupted());
				done.countDown();
			});
			try {
				thread.start();
				Thread.sleep(100);
				long start = System.currentTimeMillis();
				thread.interrupt();
				Assertions.assertTrue(done.await(2, TimeUnit.SECONDS), "tryLock 应响应中断立即返回");
				Assertions.assertTrue(System.currentTimeMillis() - start < 2000, "中断后应尽快返回");
				Assertions.assertFalse(result.get(), "中断后 tryLock 应返回 false");
				Assertions.assertTrue(interrupted.get(), "应恢复中断标志");
			} finally {
				lockManager.unlock(key);
			}
		}

		@Test
		public void testPermanentLockNoAutoRelease() throws Exception {
			String key = newKey("local");
			ExecutorService pool = Executors.newSingleThreadExecutor();
			try {
				// 未指定租期，永久锁
				lockManager.lock(key);
				Thread.sleep(300);
				Assertions.assertTrue(lockManager.exists(key), "未指定租期应永久持有");
				Assertions.assertFalse(pool.submit(() -> lockManager.tryLock(key, 200L)).get(2, TimeUnit.SECONDS),
						"永久锁不应自动解锁");
			} finally {
				lockManager.unlock(key);
				pool.shutdownNow();
			}
		}

		@Test
		public void testConcurrentMutualExclusion() throws Exception {
			String key = newKey("local");
			int threads = 3;
			int rounds = 5;
			ExecutorService pool = Executors.newFixedThreadPool(threads);
			AtomicInteger inCritical = new AtomicInteger(0);
			AtomicInteger maxInCritical = new AtomicInteger(0);
			AtomicInteger acquiredNum = new AtomicInteger(0);
			CountDownLatch start = new CountDownLatch(1);
			try {
				List<Future<?>> futures = ListUtils.newArrayList();
				for (int i = 0; i < threads; i++) {
					futures.add(pool.submit(() -> {
						start.await();
						for (int j = 0; j < rounds; j++) {
							lockManager.lock(key);
							try {
								int current = inCritical.incrementAndGet();
								maxInCritical.accumulateAndGet(current, Math::max);
								acquiredNum.incrementAndGet();
								Thread.sleep(2);
							} finally {
								inCritical.decrementAndGet();
								lockManager.unlock(key);
							}
						}
						return null;
					}));
				}
				start.countDown();
				for (Future<?> future : futures) {
					future.get(30, TimeUnit.SECONDS);
				}
				Assertions.assertEquals(1, maxInCritical.get(), "同一时刻只允许一个线程进入临界区");
				Assertions.assertEquals(threads * rounds, acquiredNum.get(), "所有加锁请求都应成功");
				Assertions.assertFalse(lockManager.exists(key), "全部释放后不应残留锁对象");
			} finally {
				pool.shutdownNow();
			}
		}

		@Test
		public void testMultipleWaiters() throws Exception {
			String key = newKey("local");
			int waiters = 3;
			ExecutorService pool = Executors.newFixedThreadPool(waiters);
			CountDownLatch ready = new CountDownLatch(waiters);
			CountDownLatch acquiredLatch = new CountDownLatch(waiters);
			AtomicInteger inCritical = new AtomicInteger(0);
			AtomicInteger maxInCritical = new AtomicInteger(0);
			try {
				lockManager.lock(key);
				List<Future<?>> futures = ListUtils.newArrayList();
				for (int i = 0; i < waiters; i++) {
					futures.add(pool.submit(() -> {
						ready.countDown();
						lockManager.lock(key);
						try {
							maxInCritical.accumulateAndGet(inCritical.incrementAndGet(), Math::max);
							Thread.sleep(20);
							acquiredLatch.countDown();
						} finally {
							inCritical.decrementAndGet();
							lockManager.unlock(key);
						}
						return null;
					}));
				}
				Assertions.assertTrue(ready.await(1, TimeUnit.SECONDS), "等待线程应启动完成");
				Thread.sleep(200);
				lockManager.unlock(key);
				Assertions.assertTrue(acquiredLatch.await(5, TimeUnit.SECONDS), "所有等待者都应依次获取到锁");
				Assertions.assertEquals(1, maxInCritical.get(), "等待者之间不应并发进入临界区");
				for (Future<?> future : futures) {
					future.get(5, TimeUnit.SECONDS);
				}
				Assertions.assertFalse(lockManager.exists(key));
			} finally {
				pool.shutdownNow();
			}
		}

		@Test
		public void testTryLockFailureCleanup() throws Exception {
			String key = newKey("local");
			ExecutorService pool = Executors.newFixedThreadPool(3);
			try {
				lockManager.lock(key);
				List<Future<Boolean>> futures = ListUtils.newArrayList();
				for (int i = 0; i < 3; i++) {
					futures.add(pool.submit(() -> lockManager.tryLock(key, 100L)));
				}
				for (Future<Boolean> future : futures) {
					Assertions.assertFalse(future.get(3, TimeUnit.SECONDS), "持有期间 tryLock 应失败");
				}
				lockManager.unlock(key);
				Assertions.assertFalse(lockManager.exists(key), "失败等待的锁对象应被清理");
			} finally {
				pool.shutdownNow();
			}
		}

		@Test
		public void testRepeatedUnlock() {
			String key = newKey("local");
			lockManager.lock(key);
			lockManager.unlock(key);
			Assertions.assertFalse(lockManager.exists(key));
			Assertions.assertDoesNotThrow(() -> lockManager.unlock(key), "重复解锁不应抛异常");
			Assertions.assertFalse(lockManager.exists(key));
		}
	}

	/**
	 * Redis 分布式锁测试（Redis 不可用时自动跳过）
	 */
	@Nested
	class RedisLockTest {

		private LockManager lockManager;

		@BeforeEach
		public void checkRedis() {
			Assumptions.assumeTrue(isRedisAvailable(), "Redis 不可用，跳过 Redis 锁测试");
			lockManager = new RedisLockManager();
		}

		private boolean isRedisAvailable() {
			try {
				RedisUtils.getRedisTemplate().hasKey("lockManager:test:available");
				return true;
			} catch (Exception e) {
				logger.warn("Redis 不可用，跳过 Redis 锁测试：{}", e.getMessage());
				return false;
			}
		}

		@Test
		public void testLockAndUnlock() {
			String key = newKey("redis");
			lockManager.lock(key);
			try {
				Assertions.assertTrue(lockManager.exists(key), "加锁后 Redis 中应存在锁");
			} finally {
				lockManager.unlock(key);
			}
			Assertions.assertFalse(lockManager.exists(key), "解锁后 Redis 中锁应被删除");
		}

		@Test
		public void testMutualExclusion() throws Exception {
			String key = newKey("redis");
			ExecutorService pool = Executors.newSingleThreadExecutor();
			try {
				lockManager.lock(key);
				Assertions.assertFalse(pool.submit(() -> lockManager.tryLock(key, 300L)).get(3, TimeUnit.SECONDS),
						"持有期间其他线程不能获取");
				lockManager.unlock(key);
				Assertions.assertTrue(pool.submit(() -> lockManager.tryLock(key, 2000L)).get(3, TimeUnit.SECONDS),
						"释放后其他线程可以获取");
				pool.submit(() -> lockManager.unlock(key)).get(2, TimeUnit.SECONDS);
				Assertions.assertFalse(lockManager.exists(key));
			} finally {
				pool.shutdownNow();
			}
		}

		@Test
		public void testLeaseTimeoutTakeover() throws Exception {
			String key = newKey("redis");
			ExecutorService pool = Executors.newSingleThreadExecutor();
			try {
				// 模拟持有者卡死：加锁后不释放，依赖 Redis 过期时间自动解锁
				lockManager.lock(key, 300L);
				long start = System.currentTimeMillis();
				Assertions.assertTrue(pool.submit(() -> lockManager.tryLock(key, 3000L)).get(4, TimeUnit.SECONDS),
						"超过租期后 Redis 锁应自动过期并可被获取");
				long cost = System.currentTimeMillis() - start;
				Assertions.assertTrue(cost >= 250, "接管耗时应不小于租期，实际：" + cost);
				pool.submit(() -> lockManager.unlock(key)).get(2, TimeUnit.SECONDS);
				Assertions.assertFalse(lockManager.exists(key));
			} finally {
				pool.shutdownNow();
			}
		}

		@Test
		public void testExpiredLockNotDeletedByOldHolder() throws Exception {
			String key = newKey("redis");
			ExecutorService pool = Executors.newSingleThreadExecutor();
			try {
				// 主线程加锁，租期 200ms 后自动过期
				lockManager.lock(key, 200L);
				Thread.sleep(400);
				Assertions.assertTrue(pool.submit(() -> lockManager.tryLock(key, 2000L)).get(3, TimeUnit.SECONDS),
						"租期到期后其他线程应可获取");
				// 旧持有者再解锁，不能删除新持有者的锁
				lockManager.unlock(key);
				Assertions.assertTrue(lockManager.exists(key), "旧持有者解锁不应删除他人获取的锁");
				pool.submit(() -> lockManager.unlock(key)).get(2, TimeUnit.SECONDS);
				Assertions.assertFalse(lockManager.exists(key), "新持有者解锁后锁应被删除");
			} finally {
				pool.shutdownNow();
			}
		}

		@Test
		public void testTryLockInterruptible() throws Exception {
			String key = newKey("redis");
			lockManager.lock(key);
			AtomicReference<Boolean> result = new AtomicReference<>(Boolean.TRUE);
			CountDownLatch done = new CountDownLatch(1);
			Thread thread = new Thread(() -> {
				result.set(lockManager.tryLock(key, 1500L));
				done.countDown();
			});
			try {
				thread.start();
				Thread.sleep(200);
				thread.interrupt();
				Assertions.assertTrue(done.await(4, TimeUnit.SECONDS), "tryLock 应中断返回");
				Assertions.assertFalse(result.get(), "中断后 tryLock 应返回 false");
			} finally {
				lockManager.unlock(key);
			}
		}

		@Test
		public void testPermanentLockNoAutoRelease() throws Exception {
			String key = newKey("redis");
			ExecutorService pool = Executors.newSingleThreadExecutor();
			try {
				// 未指定租期，永久锁
				lockManager.lock(key);
				Thread.sleep(300);
				Assertions.assertTrue(lockManager.exists(key), "未指定租期应永久持有");
				Assertions.assertFalse(pool.submit(() -> lockManager.tryLock(key, 200L)).get(3, TimeUnit.SECONDS),
						"永久锁不应自动解锁");
			} finally {
				lockManager.unlock(key);
				pool.shutdownNow();
			}
		}

		@Test
		public void testConcurrentMutualExclusion() throws Exception {
			String key = newKey("redis");
			int threads = 3;
			int rounds = 5;
			ExecutorService pool = Executors.newFixedThreadPool(threads);
			AtomicInteger inCritical = new AtomicInteger(0);
			AtomicInteger maxInCritical = new AtomicInteger(0);
			AtomicInteger acquiredNum = new AtomicInteger(0);
			CountDownLatch start = new CountDownLatch(1);
			try {
				List<Future<?>> futures = ListUtils.newArrayList();
				for (int i = 0; i < threads; i++) {
					futures.add(pool.submit(() -> {
						start.await();
						for (int j = 0; j < rounds; j++) {
							lockManager.lock(key);
							try {
								int current = inCritical.incrementAndGet();
								maxInCritical.accumulateAndGet(current, Math::max);
								acquiredNum.incrementAndGet();
								Thread.sleep(2);
							} finally {
								inCritical.decrementAndGet();
								lockManager.unlock(key);
							}
						}
						return null;
					}));
				}
				start.countDown();
				for (Future<?> future : futures) {
					future.get(60, TimeUnit.SECONDS);
				}
				Assertions.assertEquals(1, maxInCritical.get(), "同一时刻只允许一个节点/线程进入临界区");
				Assertions.assertEquals(threads * rounds, acquiredNum.get(), "所有加锁请求都应成功");
				Assertions.assertFalse(lockManager.exists(key));
			} finally {
				pool.shutdownNow();
			}
		}

		@Test
		public void testMultipleWaiters() throws Exception {
			String key = newKey("redis");
			int waiters = 3;
			ExecutorService pool = Executors.newFixedThreadPool(waiters);
			CountDownLatch ready = new CountDownLatch(waiters);
			CountDownLatch acquiredLatch = new CountDownLatch(waiters);
			AtomicInteger inCritical = new AtomicInteger(0);
			AtomicInteger maxInCritical = new AtomicInteger(0);
			try {
				lockManager.lock(key);
				List<Future<?>> futures = ListUtils.newArrayList();
				for (int i = 0; i < waiters; i++) {
					futures.add(pool.submit(() -> {
						ready.countDown();
						lockManager.lock(key);
						try {
							maxInCritical.accumulateAndGet(inCritical.incrementAndGet(), Math::max);
							Thread.sleep(20);
							acquiredLatch.countDown();
						} finally {
							inCritical.decrementAndGet();
							lockManager.unlock(key);
						}
						return null;
					}));
				}
				Assertions.assertTrue(ready.await(1, TimeUnit.SECONDS));
				Thread.sleep(300);
				lockManager.unlock(key);
				Assertions.assertTrue(acquiredLatch.await(10, TimeUnit.SECONDS), "所有等待者都应依次获取到锁");
				Assertions.assertEquals(1, maxInCritical.get(), "等待者之间不应并发进入临界区");
				for (Future<?> future : futures) {
					future.get(5, TimeUnit.SECONDS);
				}
				Assertions.assertFalse(lockManager.exists(key));
			} finally {
				pool.shutdownNow();
			}
		}

		@Test
		public void testUnlockFromOtherThread() throws Exception {
			String key = newKey("redis");
			ExecutorService pool = Executors.newSingleThreadExecutor();
			try {
				lockManager.lock(key);
				pool.submit(() -> {
					lockManager.unlock(key);
					return null;
				}).get(2, TimeUnit.SECONDS);
				Assertions.assertTrue(lockManager.exists(key), "非加锁线程解锁不应删除锁");
			} finally {
				lockManager.unlock(key);
				pool.shutdownNow();
			}
			Assertions.assertFalse(lockManager.exists(key), "加锁线程解锁后锁应被删除");
		}

		@Test
		public void testRepeatedUnlock() {
			String key = newKey("redis");
			lockManager.lock(key);
			lockManager.unlock(key);
			Assertions.assertFalse(lockManager.exists(key));
			Assertions.assertDoesNotThrow(() -> lockManager.unlock(key), "重复解锁不应抛异常");
			Assertions.assertFalse(lockManager.exists(key));
		}
	}
}
