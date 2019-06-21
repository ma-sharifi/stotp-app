package ir.htsc.security;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
public class SimpleKeyGenerator implements KeyGenerator {

    private final  String keyString = "WA2Rs#GytR4gFT1F%Q*kXGuerhteOG";

    // ======================================
    // =          Business methods          =
    // ======================================

    @Override
    public Key generateKey() {
        return new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
    }
}