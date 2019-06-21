package ir.htsc.rest;

import ir.htsc.AppConstants;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by MSH on 6/13/2019.
 */
public class MultiTest {
    @Test
    public void testAMsk() {
        System.out.println(AppConstants.maskCardAccountNo("6037701012345678"));
    }

    @Test
    public void generateUUID() {
        byte[] key = new byte[32];
        ThreadLocalRandom.current().nextBytes(key);
        System.out.println("key: " + Base64.getEncoder().encodeToString(key));
    }

    @Test
    public void stringRandom() {
        System.out.println(RandomStringUtils.randomGraph(10));
        System.out.println(RandomStringUtils.randomAscii(10));
        System.out.println(RandomStringUtils.randomPrint(10));
        System.out.println(RandomStringUtils.random(10));
    }

    @Test
    public void testNextYear() {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        System.out.println(today);
        cal.add(Calendar.YEAR, 1); // to get previous year add -1
        Date nextYear = cal.getTime();
        System.out.println(nextYear);
    }

    @Test
    public void givenUsingPlainJava_whenGeneratingRandomStringUnbounded_thenCorrect() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = Base64.getEncoder().encodeToString(array);

        System.out.println(generatedString);
    }
}
