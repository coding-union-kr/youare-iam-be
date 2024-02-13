package com.letter.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Slf4j
@Component
public class CryptoHelper {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    private static String valueKey;

    @Value("${encryption.secret_key}")
    public void setValueKey(String valueKey) {
        CryptoHelper.valueKey = valueKey;
    }

    private static String iv;

    @Value("${encryption.iv}")
    public void setIv(String iv) {
        CryptoHelper.iv = iv;
    }



    /**
     * String 타입 암호화
     * @param plainText
     * @return
     */
    public String encryptToString(String plainText) {
        try {
            byte[] KEY = valueKey.getBytes();
            IvParameterSpec ivKey = new IvParameterSpec(iv.getBytes("UTF-8"));

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeySpec secretKey = new SecretKeySpec(KEY, ALGORITHM);
            cipher.init(cipher.ENCRYPT_MODE, secretKey,ivKey);

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            log.error("String 타입 암호화 중에 에러가 발생했습니다. {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * String 타입 복호화
     * @param encryptedText
     * @return
     */
    public String decryptToString(String encryptedText) {
        try{
            byte[] KEY = valueKey.getBytes();
            IvParameterSpec ivKey = new IvParameterSpec(iv.getBytes("UTF-8"));

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeySpec secretKey = new SecretKeySpec(KEY, ALGORITHM);
            cipher.init(cipher.DECRYPT_MODE, secretKey,ivKey);

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);

            return new String(decryptedBytes);

        }catch (Exception e){
            log.error("String 타입 복호화 중에 에러가 발생했습니다. {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
