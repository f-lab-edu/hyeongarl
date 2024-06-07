package com.hyeongarl.config;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
public class PasswordEncoder {
    // PBKDF2 알고리즘을 사용하여 비밀번호를 암호화
    public static String encrypt(String userEmail, String password) {
        System.err.println("PasswordEncoder");
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), getSalt(userEmail), 85319, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] getSalt(String userEmail)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] keyBytes = userEmail.getBytes("UTF-8");

        return digest.digest(keyBytes);
    }
}
