/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.idgen;

import com.jeesite.common.collect.SetUtils;
import com.jeesite.common.idgen.IdWorker;

import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * snowflake的id生成测试类
 */
public class IdWorkerTest {

	public static void main(String[] args) throws Exception {
		final Set<Long> set = SetUtils.newHashSet();

		final IdWorker w1 = new IdWorker(-1, -1);
		final IdWorker w2 = new IdWorker(-1, -1);
		final CyclicBarrier cdl = new CyclicBarrier(100);

		for (int i = 0; i < 1000; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						cdl.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (BrokenBarrierException e) {
						e.printStackTrace();
					}

					// id
					Long id = w1.nextId();
					if (set.contains(id)){
						System.out.println(id + " exists");
					}
					set.add(id);
					System.out.println(id);

					// id2
					Long id2 = w2.nextId();
					if (set.contains(id2)){
						System.out.println(id2 + " exists");
					}
					set.add(id2);
					System.out.println(id2);
				}
			}).start();
		}
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}