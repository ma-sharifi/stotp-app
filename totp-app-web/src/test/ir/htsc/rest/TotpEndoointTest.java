package ir.htsc.rest;

import ir.htsc.rest.client.TotpClient;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;

/**
 * Created by MSH on 5/27/2019.
 */
public class TotpEndoointTest {

    @Test
    public void test12() throws InterruptedException {
        ExecutorService executor = Executors.newWorkStealingPool();
        List<Callable<Integer>> callables = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            callables.add(() -> {
                return ThreadLocalRandom.current().nextInt();
            });
        }
        executor.invokeAll(callables);
    }

    @Test
    public void register() throws IOException {
        TotpClient totpClient = new TotpClient();
        String response = totpClient.register();
        System.out.println("register->response = " + response);
    }

    @Test
    public void registerGenerateValidate() throws IOException {
        TotpClient totpClient = new TotpClient();
        String response = totpClient.register();
        String generateTotp = totpClient.generate();
        System.out.println("generateAndValidate->generate = " + generateTotp);
        String result = totpClient.validate(generateTotp);
        assertEquals("true", result);

    }

    @Test
    public void generateAndValidate() throws IOException {
        TotpClient totpClient = new TotpClient();
        String generateTotp = totpClient.generate();
        System.out.println("generateAndValidate->generate = " + generateTotp);
        String result = totpClient.validate(generateTotp);

        assertEquals(result, "true");

    }

    //---------------------------
//    @Test
    public void test() throws IOException {
        String str = null;
        if (!"".equals(str)) {
            System.out.println("!\"\".equals(str)");
        }
        if (str != null) {
            System.out.println("str!=null");
        }
        if (!"".equals(str) && str != null && str.length() > 3)
            System.out.println("!\"\".equals(str) && str.length()>3");
    }
}
