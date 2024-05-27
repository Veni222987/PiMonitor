package org.pi.server.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class PasswordUtil {
    // 将密码加密为哈希值
    @Nullable
    public static String encryptPassword(@NotNull String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 检验密码正确性
    public static boolean verifyPassword(String password, String hashedPassword) {
        String encryptedPassword = encryptPassword(password);
        return encryptedPassword.equals(hashedPassword);
    }
}
