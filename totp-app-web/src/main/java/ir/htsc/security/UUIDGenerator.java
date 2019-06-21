package ir.htsc.security;

import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class UUIDGenerator {
    static Logger logger = Logger.getLogger(UUIDGenerator.class);

    private static SecureRandom randomGenerator;

    public static void initialize() {
        try {
            randomGenerator = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Exception in initializing STOTP TokenGenerator.", e);
        }
    }

    public static String generateOTPClientToken() {
        return new BigInteger(130, randomGenerator).toString(32);
    }


    public static void main(String[] args) {
        initialize();
        System.out.println(generateOTPClientToken());
    }

}
