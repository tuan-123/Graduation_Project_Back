package com.zsc.springboot.util;

import javax.crypto.*;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * (Advanced Encryption Standard，高级加密标准）是一种对称加密算法
 *
 *@Author：黄港团
 *@Since：2021/4/4 9:09
 */

public class AESUtil {

    /**
     * 生成密钥对象
     */
    private static SecretKey generateKey(byte[] key) throws NoSuchAlgorithmException {
        // 根据指定的 RNG 算法, 创建安全随机数生成器
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        // 设置 密钥key的字节数组 作为安全随机数生成器的种子
        random.setSeed(key);

        // 创建 AES算法生成器
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        // 初始化算法生成器
        generator.init(128,random);

        // 生成 AES密钥对象, 也可以直接创建密钥对象: return new SecretKeySpec(key, ALGORITHM);
        return generator.generateKey();
    }

    /**
     * 数据加密: 明文 -> 密文
     */
    public static String encrypt(String content, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        byte[] contentByte = content.getBytes("utf-8");
        byte[] keyByte = key.getBytes("utf-8");

        // 生成密钥对象
        SecretKey secretKey = generateKey(keyByte);

        //获取AES密码器
        Cipher cipher = Cipher.getInstance("AES");
        //初始化密码器(加密模型)
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);

        //加密数据 得到密文
        byte[] cipherText  = cipher.doFinal(contentByte);
        return parseByte2HexStr(cipherText);
    }

    /**
     * 数据加密: 明文 -> 密文
     */
    /*
    public static byte[] encrypt(byte[] plainBytes, byte[] key) throws Exception {
        // 生成密钥对象
        SecretKey secKey = generateKey(key);

        // 获取 AES 密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化密码器（加密模型）
        cipher.init(Cipher.ENCRYPT_MODE, secKey);

        // 加密数据, 返回密文
        return cipher.doFinal(plainBytes);
    }
     */

    /**
     * 数据解密: 密文 -> 明文
     */
    public static String decrypt(String cipherText, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        //byte[] cipherTextByte = cipherText.getBytes("utf-8");
        byte[] cipherTextByte = parseHexStr2Byte(cipherText);
        byte[] keyByte = key.getBytes("utf-8");

        //生成密钥对象
        SecretKey secretKey = generateKey(keyByte);

        //获取AES密码器
        Cipher cipher = Cipher.getInstance("AES");
        //初始化好密码器（解密模型）
        cipher.init(Cipher.DECRYPT_MODE,secretKey);

        //解密数据  得到明文
        byte[] plainText = cipher.doFinal(cipherTextByte);
        return new String(plainText);
    }

    /**
     * 数据解密: 密文 -> 明文
     */
    /*
    public static byte[] decrypt(byte[] cipherBytes, byte[] key) throws Exception {
        // 生成密钥对象
        SecretKey secKey = generateKey(key);

        // 获取 AES 密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化密码器（解密模型）
        cipher.init(Cipher.DECRYPT_MODE, secKey);

        // 解密数据, 返回明文
        return cipher.doFinal(cipherBytes);
    }
     */
    /**将二进制转换成16进制
     * @param buf
     * @return
     */
    private static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**将16进制转换为二进制
     * @param hexStr
     * @return
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

}
