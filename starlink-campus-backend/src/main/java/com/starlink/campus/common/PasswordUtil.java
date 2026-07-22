package com.starlink.campus.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 密码安全工具类
 * 使用 SHA-256 + 随机盐值进行密码哈希
 */
public class PasswordUtil {

    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;

    /**
     * 对明文密码进行加密（生成 盐:哈希 格式）
     */
    public static String encrypt(String rawPassword) {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        String saltStr = Base64.getEncoder().encodeToString(salt);
        String hash = sha256(saltStr + rawPassword);
        return saltStr + ":" + hash;
    }

    /**
     * 校验明文密码是否与加密密码匹配
     */
    public static boolean matches(String rawPassword, String encryptedPassword) {
        if (encryptedPassword == null || !encryptedPassword.contains(":")) {
            // 兼容旧版明文密码：直接对比
            return rawPassword.equals(encryptedPassword);
        }
        String[] parts = encryptedPassword.split(":", 2);
        String saltStr = parts[0];
        String storedHash = parts[1];
        String computedHash = sha256(saltStr + rawPassword);
        return storedHash.equals(computedHash);
    }

    private static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
