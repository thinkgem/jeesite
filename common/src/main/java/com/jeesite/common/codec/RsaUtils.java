/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.codec;

import com.jeesite.common.io.PropertiesUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA 加密解密工具类，非对称加密
 * @author ThinkGem
 */
public class RsaUtils {
	
	private static final String RSA = "RSA";
	private static final String algorithm = "SHA256withRSA";
	private static final boolean STORE_BASE64 = PropertiesUtils.getInstance()
			.getPropertyToBoolean("encrypt.storeBase64", "false");

	/**
	 * 生成 RSA 秘钥对
	 */
	public static String[] genKeys() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
			keyPairGenerator.initialize(1024);
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
	 * @author ThinkGem
	 */
	public static byte[] encode(byte[] input, PublicKey publicKey) {
		try {
			Cipher encryptCipher = Cipher.getInstance(RSA);
			encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return encryptCipher.doFinal(input);
		} catch (InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException |
				 NoSuchAlgorithmException | BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 公钥加密
	 * @author ThinkGem
	 */
	public static String encode(String input, PublicKey publicKey) {
		if (STORE_BASE64) {
			return EncodeUtils.encodeBase64(encode(input.getBytes(StandardCharsets.UTF_8), publicKey));
		}
		return EncodeUtils.encodeHex(encode(input.getBytes(StandardCharsets.UTF_8), publicKey));
	}

	/**
	 * 私钥解密
	 * @author ThinkGem
	 */
	public static byte[] decode(byte[] input, PrivateKey privateKey) {
		return decodeImpl(input, privateKey);
	}

	private static byte[] decodeImpl(byte[] input, PrivateKey privateKey) {
		try {
			Cipher decryptCipher = Cipher.getInstance(RSA);
			decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
			return decryptCipher.doFinal(input);
		} catch (InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException |
				 NoSuchAlgorithmException | BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 私钥解密
	 * @author ThinkGem
	 */
	public static String decode(String input, PrivateKey privateKey) {
		if (STORE_BASE64) {
			return new String(decode(EncodeUtils.decodeBase64(input), privateKey), StandardCharsets.UTF_8);
		}
		return new String(decode(EncodeUtils.decodeHex(input), privateKey), StandardCharsets.UTF_8);
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
		if (STORE_BASE64) {
			return EncodeUtils.encodeBase64(sign(input.getBytes(StandardCharsets.UTF_8), privateKey));
		}
		return EncodeUtils.encodeHex(sign(input.getBytes(StandardCharsets.UTF_8), privateKey));
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
		if (STORE_BASE64) {
			return verify(input.getBytes(StandardCharsets.UTF_8), publicKey, EncodeUtils.decodeBase64(signature));
		}
		return verify(input.getBytes(StandardCharsets.UTF_8), publicKey, EncodeUtils.decodeHex(signature));
	}
	
}