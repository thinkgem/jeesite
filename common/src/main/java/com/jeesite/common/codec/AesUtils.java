/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.codec;

import com.jeesite.common.io.PropertiesUtils;
import com.jeesite.common.lang.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * AES 加密解密工具类
 * <p>
 * 一、加密（增强）：使用 AES-GCM 认证加密算法，每次加密随机生成 12 字节初始向量，
 * 数据格式：[版本号 1 字节][初始向量 12 字节][密文 + 认证标签 16 字节]，共比旧格式多 29 字节，
 * 具备防篡改（完整性校验）能力，且避免了固定向量模式下的数据模式泄露问题。
 * <p>
 * 二、解密（兼容）：自动识别新旧格式（Hex 或 Base64 编码由 encrypt.storeBase64 参数决定），
 * 新格式（GCM）直接解密，数据格式：[版本号][初始向量][密文 + 认证标签]，
 * 由于认证标签已能唯一确认数据是否为 GCM 格式（破解概率 2^-128），故不保存魔术字，节省数据长度；
 * 版本号一致时先按 GCM 解密，认证标签校验失败再按旧格式解密；
 * 旧格式（CBC）历史格式为 AES/CBC/PKCS5Padding，支持下列情况（见 decodeLegacy）：
 * 1、有初始向量：密文的前 16 字节为初始向量，即数据格式：[初始向量 16 字节][密文]；
 * 2、无初始向量：初始向量为全 0 的 16 字节，或密钥的前 16 字节；
 * 无初始向量的密文，用"密文携带初始向量"的方式也能解密出缺少前 16 字节的明文，两种解释仅靠密文无法区分，
 * 因此文本数据先按无初始向量解密、并以解密结果必须是合法 UTF-8 文本作为校验，字节数据只按第 1 种格式解密；
 * 另外，指定初始向量的解密，请使用 decode(data, key, iv) 方法。
 * <p>
 * 三、注意：新格式密文比旧格式长约 29 字节，Hex 编码后约长 58 个字符，
 * 若使用加密存储的数据库字段，建议字段长度不小于 255。
 * @author ThinkGem
 * @version 2026-09-26
 */
public class AesUtils {

	private static final Logger logger = LoggerFactory.getLogger(AesUtils.class);

	private static final String AES = "AES";
	private static final String AES_CBC = "AES/CBC/PKCS5Padding"; 		// 历史加密格式, 带初始向量
	private static final String AES_GCM = "AES/GCM/NoPadding"; 			// 增强加密格式, 认证加密

	private static final int DEFAULT_KEY_SIZE = 128; 			// 生成AES密钥, 默认长度为128位(16字节).
	private static final int DEFAULT_IV_SIZE = 16; 				// 生成随机向量, 默认大小为cipher.getBlockSize(), 16字节
	private static final int BLOCK_SIZE = 16; 					// AES 分组长度, 16字节
	private static final int GCM_IV_SIZE = 12; 					// GCM 推荐的初始向量长度, 12字节
	private static final int GCM_TAG_BITS = 128; 				// GCM 认证标签长度, 128位
	private static final int GCM_TAG_SIZE = GCM_TAG_BITS / 8; 	// GCM 认证标签长度, 16字节
	private static final int GCM_VERSION = 0x01; 				// 新格式版本号, 占 1 字节
	private static final int GCM_VERSION_SIZE = 1; 				// 新格式版本号长度, 1字节

	/** 新格式数据最小长度：版本号 + 初始向量 + 认证标签 + 至少 1 字节密文 */
	private static final int GCM_MIN_SIZE = GCM_VERSION_SIZE + GCM_IV_SIZE + GCM_TAG_SIZE + 1;

	/** 旧格式 CBC 无初始向量时的默认初始向量：全 0 的 16 字节 */
	private static final byte[] CBC_DEFAULT_IV = new byte[BLOCK_SIZE];

	private static final SecureRandom RANDOM = new SecureRandom();

	private static final byte[] DEFAULT_KEY = EncodeUtils.decodeHex(PropertiesUtils.getInstance()
			.getProperty("encrypt.defaultKey", "9f58a20946b47e190003ec716c1c457d"));
	private static final boolean STORE_BASE64 = PropertiesUtils.getInstance()
			.getPropertyToBoolean("encrypt.storeBase64", "false");

	/**
	 * 生成 AES 密钥,返回字节数组, 默认长度为128位(16字节)
	 */
	public static byte[] genKey() {
		return genKey(DEFAULT_KEY_SIZE);
	}

	/**
	 * 生成 AES 密钥, 可选长度为128,192,256位
	 */
	public static byte[] genKey(int keySize) {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
			keyGenerator.init(keySize);
			SecretKey secretKey = keyGenerator.generateKey();
			return secretKey.getEncoded();
		} catch (GeneralSecurityException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 生成 AES 密钥, 返回字节数组, 默认长度为128位(16字节)
	 */
	public static String genKeyString() {
		return EncodeUtils.encodeHex(genKey());
	}

	/**
	 * 生成随机向量, 默认大小为cipher.getBlockSize(), 16字节
	 */
	public static byte[] genIV() {
		byte[] bytes = new byte[DEFAULT_IV_SIZE];
		RANDOM.nextBytes(bytes);
		return bytes;
	}

	/**
	 * 使用 AES 加密原始字符串
	 * @param input 原始输入字符串
	 * @author ThinkGem
	 */
	public static String encode(String input) {
		return encodeToStore(encode(input.getBytes(StandardCharsets.UTF_8), DEFAULT_KEY));
	}
	
	/**
	 * 使用 AES 加密原始字符串
	 * @param input 原始输入字符数组
	 * @param key 符合要求的密钥
	 * @author ThinkGem
	 */
	public static byte[] encode(byte[] input, byte[] key) {
		return encodeGcm(input, key);
	}

	/**
	 * 使用 AES 加密原始字符串
	 * @param input 原始输入字符串
	 * @param key 符合要求的密钥
	 * @author ThinkGem
	 */
	public static String encode(String input, String key) {
		return encodeToStore(encode(input.getBytes(StandardCharsets.UTF_8), EncodeUtils.decodeHex(key)));
	}

	/**
	 * 使用 AES 加密原始字符串
	 * @param input 原始输入字符数组
	 * @param key 符合要求的密钥
	 * @param iv 初始向量
	 * @author ThinkGem
	 */
	public static byte[] encode(byte[] input, byte[] key, byte[] iv) {
		return aes(input, key, AES_CBC, iv, Cipher.ENCRYPT_MODE);
	}

	/**
	 * 旧版加密：AES/CBC/PKCS5Padding，随机初始向量，初始向量存放在密文的前 16 字节
	 * <p>仅供单元测试及历史数据兼容性验证使用（验证旧版密文能否正常解密），
	 * 新数据加密请使用 {@link #encode(byte[], byte[])} 方法。</p>
	 * @param input 原始输入字符数组
	 * @param key 符合要求的密钥
	 * @author ThinkGem
	 */
	public static byte[] encodeLegacy(byte[] input, byte[] key) {
		byte[] iv = genIV();
		byte[] data = encode(input, key, iv);
		byte[] output = new byte[iv.length + data.length];
		System.arraycopy(iv, 0, output, 0, iv.length);
		System.arraycopy(data, 0, output, iv.length, data.length);
		return output;
	}

	/**
	 * 旧版加密：AES/CBC/PKCS5Padding，随机初始向量，初始向量存放在密文的前 16 字节
	 * <p>仅供单元测试及历史数据兼容性验证使用，新数据加密请使用 {@link #encode(String)} 方法。</p>
	 * @param input 原始输入字符串
	 * @author ThinkGem
	 */
	public static String encodeLegacy(String input) {
		return encodeToStore(encodeLegacy(input.getBytes(StandardCharsets.UTF_8), DEFAULT_KEY));
	}

	/**
	 * 旧版加密：AES/CBC/PKCS5Padding，随机初始向量，初始向量存放在密文的前 16 字节
	 * <p>仅供单元测试及历史数据兼容性验证使用，新数据加密请使用 {@link #encode(String, String)} 方法。</p>
	 * @param input 原始输入字符串
	 * @param key 符合要求的密钥
	 * @author ThinkGem
	 */
	public static String encodeLegacy(String input, String key) {
		return encodeToStore(encodeLegacy(input.getBytes(StandardCharsets.UTF_8), EncodeUtils.decodeHex(key)));
	}

	/**
	 * 旧版加密：AES/CBC/PKCS5Padding，指定初始向量，初始向量不存放在密文中
	 * <p>无初始向量的历史数据，初始向量可传入全 0 的 16 字节，或密钥的前 16 字节；
	 * 仅供单元测试及历史数据兼容性验证使用，新数据加密请使用 {@link #encode(byte[], byte[])} 方法。</p>
	 * @param input 原始输入字符数组
	 * @param key 符合要求的密钥
	 * @param iv 初始向量，长度必须为 16 字节
	 * @author ThinkGem
	 */
	public static byte[] encodeLegacy(byte[] input, byte[] key, byte[] iv) {
		return encode(input, key, iv);
	}

	/**
	 * 旧版加密：AES/CBC/PKCS5Padding，指定初始向量，初始向量不存放在密文中
	 * <p>无初始向量的历史数据，初始向量可传入全 0 的 16 字节，或密钥的前 16 字节；
	 * 仅供单元测试及历史数据兼容性验证使用，新数据加密请使用 {@link #encode(String)} 方法。</p>
	 * @param input 原始输入字符串
	 * @param iv 初始向量，长度必须为 16 字节
	 * @author ThinkGem
	 */
	public static String encodeLegacy(String input, byte[] iv) {
		return encodeToStore(encodeLegacy(input.getBytes(StandardCharsets.UTF_8), DEFAULT_KEY, iv));
	}

	/**
	 * 使用 AES 解密数据, 返回原始字符串
	 * @param input Hex 或 Base64 编码的加密字符串
	 * @author ThinkGem
	 */
	public static String decode(String input) {
		return new String(decodeImpl(decodeFromStore(input), DEFAULT_KEY, true), StandardCharsets.UTF_8);
	}
	
	/**
	 * 使用 AES 解密数据, 返回原始字符串
	 * <p>字节数据无法校验解密结果是否为文本，旧格式只按"密文的前 16 字节为初始向量"一种格式解密，
	 * 无初始向量的历史数据请使用 {@link #decode(String)} 方法解密。</p>
	 * @param input 加密输入字符数组
	 * @param key 符合要求的密钥
	 * @author ThinkGem
	 */
	public static byte[] decode(byte[] input, byte[] key) {
		return decodeImpl(input, key, false);
	}

	/**
	 * 使用 AES 解密数据, 返回原始字符串
	 * @param input Hex 或 Base64 编码的加密字符串
	 * @param key 符合要求的密钥
	 * @author ThinkGem
	 */
	public static String decode(String input, String key) {
		return new String(decodeImpl(decodeFromStore(input), EncodeUtils.decodeHex(key), true), StandardCharsets.UTF_8);
	}

	/**
	 * 使用 AES 解密数据, 返回原始字符串
	 * @param input Hex编码的加密字符串
	 * @param key 符合AES要求的密钥
	 * @param iv 初始向量
	 * @author ThinkGem
	 */
	public static byte[] decode(byte[] input, byte[] key, byte[] iv) {
		return aes(input, key, AES_CBC, iv, Cipher.DECRYPT_MODE);
	}

	/**
	 * 使用 AES 加密或解密无编码的原始字节数组, 返回无编码的字节数组结果
	 * @param input 原始字节数组
	 * @param key 符合AES要求的密钥
	 * @param transformation 加密算法, 如 AES/CBC/PKCS5Padding
	 * @param iv 初始向量, 为 null 时使用无向量模式
	 * @param mode Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
	 * @author ThinkGem
	 */
	private static byte[] aes(byte[] input, byte[] key, String transformation, byte[] iv, int mode) {
		try {
			Cipher cipher = Cipher.getInstance(transformation);
			SecretKey secretKey = new SecretKeySpec(key, AES);
			if (iv == null) {
				cipher.init(mode, secretKey);
			} else {
				cipher.init(mode, secretKey, new IvParameterSpec(iv));
			}
			return cipher.doFinal(input);
		} catch (GeneralSecurityException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 使用 AES-GCM 增强加密, 数据格式：[版本号][初始向量][密文+认证标签]
	 * @param input 原始字节数组
	 * @param key 符合AES要求的密钥
	 * @author ThinkGem
	 */
	private static byte[] encodeGcm(byte[] input, byte[] key) {
		try {
			byte[] iv = new byte[GCM_IV_SIZE];
			RANDOM.nextBytes(iv);
			Cipher cipher = Cipher.getInstance(AES_GCM);
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, AES), new GCMParameterSpec(GCM_TAG_BITS, iv));
			byte[] data = cipher.doFinal(input);
			// 数据格式：[版本号][初始向量][密文+认证标签]
			int offset = GCM_VERSION_SIZE + GCM_IV_SIZE;
			byte[] output = new byte[offset + data.length];
			output[0] = (byte) GCM_VERSION;
			System.arraycopy(iv, 0, output, GCM_VERSION_SIZE, GCM_IV_SIZE);
			System.arraycopy(data, 0, output, offset, data.length);
			return output;
		} catch (GeneralSecurityException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 使用 AES-GCM 解密, 认证标签校验失败时抛出异常
	 * @param input GCM 格式的加密字节数组
	 * @param key 符合AES要求的密钥
	 * @author ThinkGem
	 */
	private static byte[] decodeGcm(byte[] input, byte[] key) {
		try {
			byte[] iv = Arrays.copyOfRange(input, GCM_VERSION_SIZE, GCM_VERSION_SIZE + GCM_IV_SIZE);
			byte[] data = Arrays.copyOfRange(input, GCM_VERSION_SIZE + GCM_IV_SIZE, input.length);
			Cipher cipher = Cipher.getInstance(AES_GCM);
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, AES), new GCMParameterSpec(GCM_TAG_BITS, iv));
			return cipher.doFinal(data);
		} catch (GeneralSecurityException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 解密数据, 自动识别新格式（GCM）和旧格式（CBC）
	 * <p>新格式不保存固定的魔术字，直接按 GCM 解密，以认证标签校验（破解概率 2^-128）作为判别依据，
	 * 比固定字节的魔术字（2^-56）更严谨，也少占用字节；标签校验失败再按旧格式解密，
	 * 这样旧格式密文恰好与版本号相同的极端情况也能正常解密。</p>
	 * @param input 无编码的加密字节数组
	 * @param key 符合AES要求的密钥
	 * @param text 是否文本数据, 文本数据校验 UTF-8 编码, 避免旧格式解密时返回乱码
	 * @author ThinkGem
	 */
	private static byte[] decodeImpl(byte[] input, byte[] key, boolean text) {
		RuntimeException cause = null;
		// 新格式：数据长度和版本号满足时才尝试，认证标签校验通过才认为是新格式数据
		if (isGcmData(input)) {
			try {
				return decodeGcm(input, key);
			} catch (RuntimeException e) {
				cause = e;
			}
		}
		// 旧格式：CBC 密文长度必然是 16 的整数倍，长度不符说明不是旧格式密文，不再尝试
		if (input != null && key != null && input.length > 0 && input.length % BLOCK_SIZE == 0) {
			return decodeLegacy(input, key, text);
		}
		throw cause != null ? cause : ExceptionUtils.unchecked(new GeneralSecurityException("AES decrypt error"));
	}

	/**
	 * 旧格式解密, 兼容历史 CBC 格式的无初始向量和有初始向量两种情况
	 * <p>注意：CBC 解密后续分组只用前一个密文分组，所以"密文携带初始向量"的解释，
	 * 用在无初始向量的密文上时，仍能解密出缺少了前 16 字节的明文尾部（数据的前 16 字节被当成了初始向量），
	 * 反过来无初始向量的解释，用在"密文携带初始向量"的密文上时，第一个分组是乱码。
	 * 两种解释都会解密"成功"，仅靠密文长度无法区分，因此：</p>
	 * <p>1、文本数据（text=true）：先按无初始向量（零初始向量、密钥作为初始向量）解密，
	 * 解密结果必须是合法的 UTF-8 文本才认可，否则继续尝试下一种；</p>
	 * <p>2、字节数据（text=false）：无法通过文本校验区分，只按"密文的前 16 字节为初始向量"
	 * 一种格式解密，避免解密出错误数据。</p>
	 * @author ThinkGem
	 */
	private static byte[] decodeLegacy(byte[] input, byte[] key, boolean text) {
		RuntimeException cause = null;
		if (input != null && key != null) {
			// 1、文本数据：无初始向量，初始向量为全 0 的 16 字节
			if (text) {
				try {
					byte[] output = checkText(aes(input, key, AES_CBC, CBC_DEFAULT_IV, Cipher.DECRYPT_MODE), true);
					if (output != null) {
						logger.debug("AES 解密使用历史 CBC 格式（零初始向量），建议更新为 GCM 格式。");
						return output;
					}
				} catch (RuntimeException e) {
					cause = e;
				}
				// 2、文本数据：无初始向量，初始向量为密钥的前 16 字节
				if (key.length >= BLOCK_SIZE) {
					try {
						byte[] iv = Arrays.copyOfRange(key, 0, BLOCK_SIZE);
						byte[] output = checkText(aes(input, key, AES_CBC, iv, Cipher.DECRYPT_MODE), true);
						if (output != null) {
							logger.debug("AES 解密使用历史 CBC 格式（密钥作为初始向量），建议更新为 GCM 格式。");
							return output;
						}
					} catch (RuntimeException e) {
						cause = e;
					}
				}
			}
			// 3、有初始向量：密文的前 16 字节为初始向量
			if (input.length >= BLOCK_SIZE * 2) {
				byte[] iv = Arrays.copyOfRange(input, 0, BLOCK_SIZE);
				byte[] data = Arrays.copyOfRange(input, BLOCK_SIZE, input.length);
				try {
					byte[] output = checkText(aes(data, key, AES_CBC, iv, Cipher.DECRYPT_MODE), text);
					if (output != null) {
						logger.debug("AES 解密使用历史 CBC 格式（密文携带初始向量），建议更新为 GCM 格式。");
						return output;
					}
				} catch (RuntimeException e) {
					cause = e;
				}
			}
		}
		throw cause != null ? cause : ExceptionUtils.unchecked(new GeneralSecurityException("AES decrypt error"));
	}

	/**
	 * 是否为 GCM 新格式数据（只判断数据长度和版本号，最终以认证标签校验结果为准）
	 * @author ThinkGem
	 */
	private static boolean isGcmData(byte[] input) {
		// 数据长度至少为：版本号 + 初始向量 + 认证标签 + 1 字节密文
		if (input == null || input.length < GCM_MIN_SIZE) {
			return false;
		}
		// 版本号一致才按新格式解密，版本号不一致时直接按旧格式解密
		return input[0] == (byte) GCM_VERSION;
	}

	/**
	 * 文本数据校验 UTF-8 编码, 校验不通过时返回 null, 继续尝试其它旧格式
	 * @author ThinkGem
	 */
	private static byte[] checkText(byte[] input, boolean text) {
		if (text && !isValidUtf8(input)) {
			return null;
		}
		return input;
	}

	/**
	 * 是否为合法的 UTF-8 编码字节数组
	 * @author ThinkGem
	 */
	private static boolean isValidUtf8(byte[] input) {
		CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
				.onMalformedInput(CodingErrorAction.REPORT)
				.onUnmappableCharacter(CodingErrorAction.REPORT);
		try {
			decoder.decode(ByteBuffer.wrap(input));
			return true;
		} catch (CharacterCodingException e) {
			return false;
		}
	}

	/**
	 * 根据 encrypt.storeBase64 参数, 进行 Hex 或 Base64 编码存储
	 * @author ThinkGem
	 */
	private static String encodeToStore(byte[] input) {
		if (STORE_BASE64) {
			return EncodeUtils.encodeBase64(input);
		}
		return EncodeUtils.encodeHex(input);
	}

	/**
	 * 根据 encrypt.storeBase64 参数, 进行 Hex 或 Base64 解码
	 * @author ThinkGem
	 */
	private static byte[] decodeFromStore(String input) {
		if (STORE_BASE64) {
			return EncodeUtils.decodeBase64(input);
		}
		return EncodeUtils.decodeHex(input);
	}

}
