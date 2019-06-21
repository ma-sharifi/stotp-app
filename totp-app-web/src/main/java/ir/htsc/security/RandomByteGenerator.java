package ir.htsc.security;

import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author me-sharifi on 6/11/2019 at 4:02 PM.
 */
public class RandomByteGenerator {

    private final int length;

    public RandomByteGenerator(int length) {
        this.length = length;
    }

    public byte[] generate() {
        byte[] key = new byte[length];
        ThreadLocalRandom.current().nextBytes(key);
        return key;
    }

    public String generateStr() {
        return Base64.getEncoder().encodeToString(generate());
    }

//    public static void main(String[] args) {
//        RandomByteGenerator randomByteGenerator=new RandomByteGenerator(32);
//        System.out.println(randomByteGenerator.generateStr());
//    }
}
