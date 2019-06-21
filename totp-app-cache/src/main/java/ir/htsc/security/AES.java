package ir.htsc.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Created by MSH on 6/12/2019.
 */
//https://www.javacodegeeks.com/2018/03/aes-encryption-and-decryption-in-javacbc-mode.html
public class AES {
    private static final String initVector = "ML*%R6Bkd13lwf15";

    public static String encrypt(String key,byte[] plain) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(plain);
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public static String encrypt(String key,String plain) {
       return encrypt(key,plain.getBytes());
    }
    public static String decrypt(String key,String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decryptString(String key,byte[] encrypted) {
        return  new String(decrypt(key,encrypted));
    }
    public static String decryptBase64(String key,byte[] encrypted) {
        return  Base64.getEncoder().encodeToString(decrypt(key,encrypted));
    }
    public static byte[] decrypt(String key,byte[] encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(encrypted);
            return original;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String key = "j2p8UO@F12345678";
        String originalString = "6037701012345678";
        System.out.println("Original String to encrypt - " + originalString);
        String encryptedString = encrypt(key,originalString);
        System.out.println("Encrypted String - " + encryptedString);
        System.out.println("After decryption - " + new String(decrypt(key,Base64.getDecoder().decode(encryptedString))));
        System.out.println("After decryption2  - " + decryptString(key,Base64.getDecoder().decode(encryptedString)));
        System.out.println(System.currentTimeMillis() - start);
    }
}
