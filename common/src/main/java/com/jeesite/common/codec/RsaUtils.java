/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.codec;

import com.jeesite.common.io.PropertiesUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA 加密解密工具类，非对称加密
 * <p>加密统一采用安全的 OAEP 填充方式（SHA-256 摘要 + MGF1），不再使用默认的 PKCS#1 v1.5 填充。</p>
 * <p>为兼容历史数据，解密时仍支持旧版本 PKCS#1 v1.5 填充的密文（见 {@link #decodeLegacy}）；
 * 旧版本填充方式的加密方法 {@link #encodeLegacy(byte[], PublicKey)} 仅保留给单元测试
 * 及历史数据兼容性验证使用，不用于业务数据加密，历史数据迁移完成后可一并删除。</p>
 * @author ThinkGem
 * @version 2026-09-22
 */
public class RsaUtils {
	
	private static final String RSA = "RSA";
	private static final String algorithm = "SHA256withRSA";
	private static final String RSA_OAEP = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";	// 安全填充方式，加密使用
	private static final String RSA_LEGACY = "RSA/ECB/PKCS1Padding";				// 旧版本填充方式，仅用于解密兼容
	private static final int DEFAULT_KEY_SIZE = 2048;	// RSA 密钥长度，密码指南建议至少为 2048 位
	private static final OAEPParameterSpec OAEP_PARAM_SPEC = new OAEPParameterSpec(
			"SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);

	private static final SecureRandom RANDOM = new SecureRandom();

	private static final boolean STORE_BASE64 = PropertiesUtils.getInstance()
			.getPropertyToBoolean("encrypt.storeBase64", "false");

	/**
	 * 生成 RSA 秘钥对，密钥长度默认 2048 位（密码指南建议 RSA 密钥长度至少为 2048 位）
	 */
	public static String[] genKeys() {
		return genKeys(DEFAULT_KEY_SIZE);
	}

	/**
	 * 生成 RSA 秘钥对
	 * @param keySize 密钥长度，建议不小于 2048 位
	 */
	public static String[] genKeys(int keySize) {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
			keyPairGenerator.initialize(keySize, RANDOM);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			PublicKey publicKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();
			return new String[]{
					EncodeUtils.encodeBase64(publicKey.getEncoded()),
					EncodeUtils.encodeBase64(privateKey.getEncoded()),
			};
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将 Base64 公钥串，转化为公钥对象
	 * @author ThinkGem
	 */
	public static PublicKey toPublicKey(String publicKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(EncodeUtils.decodeBase64(publicKey));
			return keyFactory.generatePublic(publicKeySpec);
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将 Base64 私钥串，转化为私钥对象
	 * @author ThinkGem
	 */
	public static PrivateKey toPrivateKey(String privateKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(EncodeUtils.decodeBase64(privateKey));
			return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 公钥加密
	 * <p>使用安全的 OAEP 填充方式（SHA-256 摘要 + MGF1），单次可加密的明文长度受密钥长度限制，
	 * 2048 位密钥约为 190 字节，超出请改用对称加密或分段加密。</p>
	 * @author ThinkGem
	 */
	public static byte[] encode(byte[] input, PublicKey publicKey) {
		try {
			Cipher encryptCipher = Cipher.getInstance(RSA_OAEP);
			encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey, OAEP_PARAM_SPEC);
			return encryptCipher.doFinal(input);
		} catch (InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException |
				 NoSuchAlgorithmException | InvalidAlgorithmParameterException | BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 公钥加密
	 * @author ThinkGem
	 */
	public static String encode(String input, PublicKey publicKey) {
		return encodeToStore(encode(input.getBytes(StandardCharsets.UTF_8), publicKey));
	}

	/**
	 * 私钥解密
	 * @author ThinkGem
	 */
	public static byte[] decode(byte[] input, PrivateKey privateKey) {
		return decodeImpl(input, privateKey);
	}

	private static byte[] decodeImpl(byte[] input, PrivateKey privateKey) {
		RuntimeException cause;
		try {
			Cipher decryptCipher = Cipher.getInstance(RSA_OAEP);
			decryptCipher.init(Cipher.DECRYPT_MODE, privateKey, OAEP_PARAM_SPEC);
			return decryptCipher.doFinal(input);
		} catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException |
				 InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			cause = new RuntimeException(e);
		}
		// RSA 密文长度必然等于密钥模长，长度不符说明不是有效的 RSA 密文，不必再尝试旧版填充方式
		if (privateKey instanceof RSAPrivateKey) {
			int modulusLength = (((RSAPrivateKey) privateKey).getModulus().bitLength() + 7) / 8;
			if (input == null || input.length != modulusLength) {
				throw cause;
			}
		}
		// 兼容旧版本 PKCS#1 v1.5 填充方式加密的历史数据，两种填充方式都失败时抛出主异常的原始原因
		try {
			return decodeLegacy(input, privateKey);
		} catch (RuntimeException e) {
			cause.addSuppressed(e);
			throw cause;
		}
	}

	/**
	 * 兼容旧版本数据：使用 PKCS#1 v1.5 填充方式解密（旧版本所使用的填充方式，
	 * 可能存在填充预言攻击的风险）
	 * <p>该分支仅用于解密历史遗留数据，不再用于加密；历史数据全部迁移完成后，可删除该方法及
	 * {@link #RSA_LEGACY} 常量。</p>
	 * @param input 旧版本 PKCS#1 v1.5 填充方式加密的字节数组
	 * @param privateKey 符合要求的私钥
	 * @author ThinkGem
	 */
	private static byte[] decodeLegacy(byte[] input, PrivateKey privateKey) {
		try {
			Cipher decryptCipher = Cipher.getInstance(RSA_LEGACY);
			decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
			return decryptCipher.doFinal(input);
		} catch (InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException |
				 NoSuchAlgorithmException | BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 兼容旧版本数据：使用 PKCS#1 v1.5 填充方式加密（旧版本所使用的填充方式）
	 * <p>仅供单元测试及历史数据兼容性验证使用（验证旧版密文能否正常解密），
	 * 不再用于业务数据加密，新数据加密请使用 {@link #encode(byte[], PublicKey)} 方法（OAEP 填充方式）。</p>
	 * @param input 原始输入字符数组
	 * @param publicKey 符合要求的公钥
	 * @author ThinkGem
	 */
	public static byte[] encodeLegacy(byte[] input, PublicKey publicKey) {
		try {
			Cipher encryptCipher = Cipher.getInstance(RSA_LEGACY);
			encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return encryptCipher.doFinal(input);
		} catch (InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException |
				 NoSuchAlgorithmException | BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 兼容旧版本数据：使用 PKCS#1 v1.5 填充方式加密（旧版本所使用的填充方式）
	 * <p>仅供单元测试及历史数据兼容性验证使用，新数据加密请使用 {@link #encode(String, PublicKey)} 方法。</p>
	 * @param input 原始输入字符串
	 * @param publicKey 符合要求的公钥
	 * @author ThinkGem
	 */
	public static String encodeLegacy(String input, PublicKey publicKey) {
		return encodeToStore(encodeLegacy(input.getBytes(StandardCharsets.UTF_8), publicKey));
	}

	/**
	 * 私钥解密
	 * @author ThinkGem
	 */
	public static String decode(String input, PrivateKey privateKey) {
		return new String(decode(decodeFromStore(input), privateKey), StandardCharsets.UTF_8);
	}

	/**
	 * 私钥签名
	 * @author ThinkGem
	 */
	public static byte[] sign(byte[] input, PrivateKey privateKey) {
		try {
			Signature sig = Signature.getInstance(algorithm);
			sig.initSign(privateKey);
			sig.update(input);
			return sig.sign();
		} catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 私钥签名
	 * @author ThinkGem
	 */
	public static String sign(String input, PrivateKey privateKey) {
		return encodeToStore(sign(input.getBytes(StandardCharsets.UTF_8), privateKey));
	}

	/**
	 * 公钥验签
	 * @author ThinkGem
	 */
	public static boolean verify(byte[] input, PublicKey publicKey, byte[] signature) {
		try {
			Signature sig = Signature.getInstance(algorithm);
			sig.initVerify(publicKey);
			sig.update(input);
			return sig.verify(signature);
		} catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 公钥验签
	 * @author ThinkGem
	 */
	public static boolean verify(String input, PublicKey publicKey, String signature) {
		return verify(input.getBytes(StandardCharsets.UTF_8), publicKey, decodeFromStore(signature));
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
