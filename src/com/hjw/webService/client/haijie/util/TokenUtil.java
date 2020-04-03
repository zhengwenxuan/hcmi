package com.hjw.webService.client.haijie.util;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TokenUtil {

    private static final DateTimeFormatter DEFAULT_FORMATTER_LONG = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");

    private static String desEncrypt(String data, String key)
            throws GeneralSecurityException {
        byte[] dataBytes = decodeData(data);
        dataBytes = completionArray(dataBytes);

        byte[] keyBytes = decodeData(key);
        keyBytes = completionArray(keyBytes);

        byte[] bt = desEncrypt(dataBytes, keyBytes);
        return Base64.encodeBase64String(bt);
    }

    private static byte[] desEncrypt(byte[] data, byte[] key)
            throws GeneralSecurityException {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretkey = keyFactory.generateSecret(dks);

        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretkey, sr);

        return cipher.doFinal(data);
    }

    private static byte[] decodeData(String data) {
        return data.getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] completionArray(byte[] srcArray) {
        int multiple = srcArray.length / 16 + (srcArray.length % 16 > 0 ? 1 : 0);
        int length = 16 * multiple;
        byte[] resultArray = new byte[length];

        System.arraycopy(srcArray, 0, resultArray, 0, srcArray.length);

        for (int i = srcArray.length; i < length; i++) {
            resultArray[i] = (byte) 1;
        }

        return resultArray;
    }

    private static String aesEncrypt(String encryptStr, String decryptKey)
            throws GeneralSecurityException {
        byte[] dataBytes = decodeData(encryptStr);
        dataBytes = completionArray(dataBytes);
        byte[] resultBytes = aesEncrypt(dataBytes, decryptKey);

        return Base64.encodeBase64String(resultBytes);
    }

    private static byte[] aesEncrypt(byte[] dataBytes, String key)
            throws GeneralSecurityException {

        SecretKey secretKey = getSecretKey(key);
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(dataBytes);
    }

    private static SecretKey getSecretKey(String strKey)
            throws GeneralSecurityException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(strKey.getBytes());
        generator.init(128, secureRandom);
        return generator.generateKey();
    }

    private static String encrypt(String key, String data)
            throws GeneralSecurityException {
        String newKey = desEncrypt(key, key);

        return aesEncrypt(data, newKey);
    }

    public static String createToken(String userCode, String userIp) {
        try {
            String tokenValue = LocalDateTime.now().format(DEFAULT_FORMATTER_LONG);
            return encrypt(userCode + "_" + userIp, tokenValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) throws UnknownHostException {
    	System.out.println(createToken("admin", InetAddress.getLocalHost().getHostAddress()));
    	System.out.println(InetAddress.getLocalHost().getHostAddress());
	}
}
