package ir.htsc.security;

/**
 * Created by me-sharifi on 7/12/2017.
 */
//https://tools.ietf.org/id/draft-mraihi-totp-timebased-06.html#RFC2104

import ir.htsc.AppConstants;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Base64;

/**
 * This an example implementation of the OATH TOTP algorithm.
 * Visit www.openauthentication.org for more information.
 *
 * @author Johan Rydell, PortWise, Inc.
 */
public class TOTP {
    public static final String SEED = "EFF27BFF5D3087891C3AD98AD7";
    public static final String SEED_BASE64 = "op2bwCHRV22a+OHuBUvrxLkq7ddnTzzmdQInKehNSyg=";
    private static final int DELAY_WINDOW = 2;
    private static final int RETURN_DIGIT_LENGTH = 6;

    private static final int[] DIGITS_POWER
            // 0   1   2    3     4       5       6        7         8
            = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};
    static long interval = 10 * 60;//5 minutes
    static int lengthOfOtp = 5;

    private TOTP() {
    }
    //-----------------------------------------------
    public static void main(String[] args) throws InterruptedException {
        long time=(System.currentTimeMillis() / 1000) / 30;
        for (int i = 0; i < 7; i++) {
            System.err.println("##" + generateTOTP256("rZxQsGhmzUghdZu6UztmDSZLH77fgkCJ7z+2UAPgdR0=", "" +time));
            Thread.sleep(10000);
        }


    }//    public static void main(String[] args) {
//
//        String key = "3132333435363738393031323334353637383930";
//        long T0 = 0;
//        long X = 30;
//        long testTime[] = {59, 1111111109, 1111111111,
//                1560248459, 2000000000};
//
//        String time = "0";
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            System.out.println(
//                    "+--------------+-----------------------+" +
//                            "------------------+--------+--------+");
//            System.out.println(
//                    "|  Time(sec)   |   Time (UTC format)   " +
//                            "| Value of T(Hex)  |  TOTP  | Mode   |");
//            System.out.println(
//                    "+--------------+-----------------------+" +
//                            "------------------+--------+--------+");
//
//            for (int i = 0; i < testTime.length; i++) {
//                long T = (testTime[i] - T0) / X;
//                time = Long.toHexString(T).toUpperCase();
//                while (time.length() < 16) time = "0" + time;
//                String fmtTime = String.format("%1$-10s", testTime[i]);
//                String utcTime = df.format(new Date(testTime[i] * 1000));
////                System.err.println(key + "->" + time);
//
//                System.out.print("|  " + fmtTime + "  |  " + utcTime + "  | " + time + " |");
//                System.out.println(generateTOTP(key, time, "8", "HmacSHA1") + "| SHA1   |");
//
//
//                System.out.print("|  " + fmtTime + "  |  " + utcTime + "  | " + time + " |");
//                System.out.println(generateTOTP(key, time, "8", "HmacSHA256") + "| SHA256 |");
//
//
//                System.err.print("|  " + fmtTime + "  |  " + utcTime + "  | " + time + " |");
//                System.err.println("##" + generateTOTP256("rZxQsGhmzUghdZu6UztmDSZLH77fgkCJ7z+2UAPgdR0=", "" +(System.currentTimeMillis() / 1000) / 30)  + "| SHA256 |");
//
//                System.out.print("|  " + fmtTime + "  |  " + utcTime + "  | " + time + " |");
//                System.out.println(generateTOTP(key, time, "8", "HmacSHA512") + "| SHA512 |");
//
//                System.out.println("+--------------+-----------------------+" + "------------------+--------+--------+");
//            }
//        } catch (final Exception e) {
//            System.out.println("Error : " + e);
//        }
//    }

    /**
     * This method uses the JCE to provide the crypto
     * algorithm.
     * HMAC computes a Hashed Message Authentication Code with the
     * crypto hash algorithm as a parameter.
     *
     * @param crypto   the crypto algorithm (HmacSHA1, HmacSHA256,
     *                 HmacSHA512)
     * @param keyBytes the bytes to use for the HMAC key
     * @param text     the message or text to be authenticated.
     */
    private static byte[] hmac_sha1(String crypto, byte[] keyBytes,
                                    byte[] text) {
        try {
            Mac hmac;
            hmac = Mac.getInstance(crypto);//Mac.getInstance("HmacSHA256")
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }


    /**
     * This method converts HEX string to Byte[]
     *
     * @param hex the HEX string
     * @return A byte array
     */
    private static byte[] hexStr2Bytes(String hex) {
        // Adding one byte to get the right conversion
        // values starting with "0" can be converted
        byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();

        // Copy all the REAL bytes, not the "first"
        byte[] ret = new byte[bArray.length - 1];
        for (int i = 0; i < ret.length; i++)
            ret[i] = bArray[i + 1];
        return ret;
    }

    /**
     * This method generates an TOTP value for the given
     * set of parameters.
     *
     * @param key          the shared secret, HEX encoded
     * @param time         a value that reflects a time
     * @param returnDigits number of digits to return
     * @return A numeric String in base 10 that includes
     * {@link truncationDigits} digits
     */
    public static String generateTOTP(String key,
                                      String time,
                                      String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA1");
    }

    /**
     * This method generates an TOTP value for the given
     * set of parameters.
     *
     * @param key          the shared secret, HEX encoded
     * @param time         a value that reflects a time
     * @param returnDigits number of digits to return
     * @return A numeric String in base 10 that includes
     * {@link truncationDigits} digits
     */
    public static String generateTOTP256(String key,
                                         String time,
                                         String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA256");
    }

    public static String generateTOTP256(String key,
                                         String time) {
        return generateTOTPBase64(key, time, RETURN_DIGIT_LENGTH + "", "HmacSHA256");
    }


    /**
     * This method generates an TOTP value for the given
     * set of parameters.
     *
     * @param key          the shared secret, HEX encoded
     * @param time         a value that reflects a time
     * @param returnDigits number of digits to return
     * @return A numeric String in base 10 that includes
     * {@link truncationDigits} digits
     */
    public static String generateTOTP512(String key,
                                         String time,
                                         String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA512");
    }

    /**
     * This method generates an TOTP value for the given
     * set of parameters.
     *
     * @param key          the shared secret, HEX encoded
     * @param time         a value that reflects a time
     * @param returnDigits number of digits to return
     * @param crypto       the crypto function to use
     * @return A numeric String in base 10 that includes
     * {@link truncationDigits} digits
     */
    public static String generateTOTP(String key,
                                      String time,
                                      String returnDigits,
                                      String crypto) {
//        int codeDigits = Integer.decode(returnDigits).intValue();
        key = key + SEED;
//        System.out.println("key: " + key);
        int codeDigits = Integer.parseInt(returnDigits);
        String result = null;
        byte[] hash;

        // Using the counter
        // First 8 bytes are for the movingFactor
        // Complaint with base RFC 4226 (HOTP)
        while (time.length() < 16)
            time = "0" + time;

        // Get the HEX in a Byte[]
        byte[] msg = hexStr2Bytes(time);
//        System.out.println("time: " + time);
        // Adding one byte to get the right conversion
        byte[] k = hexStr2Bytes(key);

        hash = hmac_sha1(crypto, k, msg);

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary =
                ((hash[offset] & 0x7f) << 24) |
                        ((hash[offset + 1] & 0xff) << 16) |
                        ((hash[offset + 2] & 0xff) << 8) |
                        (hash[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[codeDigits];

        result = Integer.toString(otp);
        while (result.length() < codeDigits) {
            result = "0" + result;
        }
        return result;
    }

    public static String generateTOTPBase64(String keyBase64,
                                            String time,
                                            String returnDigits,
                                            String crypto) {
//        int codeDigits = Integer.decode(returnDigits).intValue();

        String result = null;
        try {
//            keyBase64 = keyBase64 + SEED_BASE64;
            int codeDigits = Integer.parseInt(returnDigits);
            byte[] hash;

            // Using the counter
            // First 8 bytes are for the movingFactor
            // Complaint with base RFC 4226 (HOTP)
            while (time.length() < 16)
                time = "0" + time;

            // Get the HEX in a Byte[]
            byte[] msg = hexStr2Bytes(time);
//            System.out.println("time: " + time + " Time Byte: " + Base64.getEncoder().encodeToString(msg));
            // Adding one byte to get the right conversion
            byte[] k = Base64.getDecoder().decode(keyBase64);
            ;// Base64.getEncoder().encodeToString(generateOTPSecretKey());

            hash = hmac_sha1(crypto, k, msg);
            // put selected bytes into result int
            int offset = hash[hash.length - 1] & 0xf;

            int binary =
                    ((hash[offset] & 0x7f) << 24) |
                            ((hash[offset + 1] & 0xff) << 16) |
                            ((hash[offset + 2] & 0xff) << 8) |
                            (hash[offset + 3] & 0xff);

            int otp = binary % DIGITS_POWER[codeDigits];

            result = Integer.toString(otp);
            while (result.length() < codeDigits) {
                result = "0" + result;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;

    }

    private static long getTimeSequence(int timeOffset) {
        long seq = System.currentTimeMillis() / 1000;
        return seq / AppConstants.OTP_STEP_INTERVAL_SECOND;
    }


    public static boolean verify(String secret, long time, long startTime, long interval, String otp) {
        long code = Long.parseLong(otp);
        int pastResponse = Math.max(DELAY_WINDOW, 0);
        for (int i = 0; i <= pastResponse; i++) {
            int candidate = Integer.valueOf(generate(secret, time, startTime, interval - i, 5));
            if (candidate == code) {
                return true;
            }
            candidate = Integer.valueOf(generate(secret, time, startTime, interval + i + 1, 5));
            if (candidate == code) {
                return true;
            }
        }
        return false;
    }

    public static String generate(String secret, long startTime) {
        return generate(secret, startTime, 0, interval, lengthOfOtp);
    }

    public static String generate(String secret) {
        return generate(secret, System.currentTimeMillis() / 1000, 0, interval, lengthOfOtp);
    }

    public static boolean verify(String secret, String otp) {
        return verify(secret, System.currentTimeMillis() / 1000, 0, interval, otp);
    }

    public static String generate(String secret, long time, long startTime, long interval, int lengthOfOtp) {
        long T0 = startTime;
        long X = interval;
        String steps;
        long diff = (time - T0);
        long T = diff / X;
        steps = Long.toHexString(T).toUpperCase();
//        System.out.println("diff: " + diff + " ,T: " + T);
        while (steps.length() < 16) steps = "0" + steps;
        return TOTP.generateTOTP(secret, steps, lengthOfOtp + "", "HmacSHA512");
    }

}