package ir.htsc.security;

import org.apache.log4j.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Security;
import java.util.Base64;


public class SymmetricEncryption {

    static IvParameterSpec ivSpec = new IvParameterSpec(new byte[16]);

    static String ALGORITHM = "AES/CBC/PKCS7Padding";
    static String KEY_ALGORITHM = "AES";
    static int keySize = 128;

    public static void initialize() throws Exception{
//        try {
//            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//        } catch (Exception e) {
//            new APMSEncryptionException(ResultCodesEnum.ErrorInitializingSymmetricSecurity, e);
//        }
    }



    public static String encrypt(String plain, byte[] key, byte[] iv) throws Exception{
        try {
            if(key == null || key.length < (keySize/8)) {
                new Exception("Exception InvalidSecretKey");
            }

            SecretKeySpec keySpec = new SecretKeySpec(key, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec,new IvParameterSpec(iv));
            byte[] encrypted = cipher.doFinal(plain.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encrypted);//encodeBase64String

        } catch (Exception e) {
            throw new Exception("Exception PacketEncryptionFailed", e);
        }
    }




    public static String decrypt(String encrypted_base64, byte[] key, byte[] iv) throws Exception{
        try {
            if(key == null || key.length < (keySize/8)) {
                new Exception("Exception InvalidSecretKey");
            }

            //SecretKeySpec keySpec = new SecretKeySpec(key.substring(0,(keySize/8)).getBytes(), KEY_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
            cipher.init(Cipher.DECRYPT_MODE, keySpec,new IvParameterSpec(iv));
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted_base64));
            return new String(decrypted);

        } catch (Exception e) {
            //TODO : Remove Log
            Logger logger = Logger.getLogger(SymmetricEncryption.class);
            logger.warn("Exception in symmetric decryption. keySize:" + key.length + "   key: " + DatatypeConverter.printHexBinary(key)
                         + " strKey:" + new String(key)  +  "  \n encrypted request: " + encrypted_base64);
            throw new Exception(" Exception PacketEncryptionFailed", e);
        }
    }





//========================================================================================================================================================
//========================================================================================================================================================
//========================================================================================================================================================

    public static void main(String[] args) throws Exception{

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());


        String encrypted_base64 = "pJ1kpjL4E3b6BFNQckuZBA==";
        String key = "D0735F9E602247D3";
        String plain = decryp(encrypted_base64, key);
    }

    public static String decryp(String encrypted_base64, String key) throws Exception{
        SecretKeySpec keySpec = new SecretKeySpec(key.substring(0,(keySize/8)).getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, keySpec,ivSpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted_base64));
        return new String(decrypted);

    }

    public static void test2() throws Exception{
        SymmetricEncryption.initialize();

        String str = "AsanPardakht";
        byte[] key = "D0735F9E602247D3ZZZZZZZZ".getBytes();

        String res = SymmetricEncryption.encrypt(str, key, new byte[16]);
        System.out.println(res);
    }

    public static void test1() throws Exception{
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());


        String str = "AsanPardakht";
        String key = "D0735F9E602247D3";

        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivspec = new IvParameterSpec(new byte[16]);

        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
//        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec,ivspec);
        byte[] encrypted = cipher.doFinal(str.getBytes());

        System.out.println(Base64.getEncoder().encodeToString(encrypted));

        String text64 = "pJ1kpjL4E3b6BFNQckuZBA==";

        //keySpec = new SecretKeySpec(key.getBytes(), "AES");
        cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
//        cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, keySpec,ivspec);
//        byte[] decrypted = cipher.doFinal(encrypted);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(text64));

        System.out.println(new String(decrypted));
    }
}
