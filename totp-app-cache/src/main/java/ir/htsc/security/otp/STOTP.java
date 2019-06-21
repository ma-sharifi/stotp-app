package ir.htsc.security.otp;

import java.security.GeneralSecurityException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author me-sharifi on 6/11/2019 at 4:23 PM.
 */
public class STOTP implements IOTP {
    private final int length;
    private final String algorithm;

    public STOTP(int length) {
        this.length = length;
        this.algorithm = "HmacSHA256";
    }

    public STOTP(int length, String algorithm) {
        this.length = length;
        this.algorithm = algorithm;
    }

    @Override
    public byte[] generateKey() {
        byte[] key = new byte[length];
        ThreadLocalRandom.current().nextBytes(key);
        return key;
    }

    @Override
    public String generateToken(byte[] key) throws GeneralSecurityException {//HmacSHA256
        ir.htsc.security.otp.PasscodeGenerator passcodeGenerator = new ir.htsc.security.otp.PasscodeGenerator(new MACGenerator(key, algorithm));
        return passcodeGenerator.generateResponseCode(getTimeSequence());
    }

    @Override
    public boolean validateToken(byte[] key, String otp) throws GeneralSecurityException {
        ir.htsc.security.otp.PasscodeGenerator passcodeGenerator = new ir.htsc.security.otp.PasscodeGenerator(new MACGenerator(key, algorithm));
        long timeSequence = getTimeSequence();
        boolean isValid = passcodeGenerator.verifyResponseCode(timeSequence, otp);
        System.out.println("Expected otp is: " + passcodeGenerator.generateResponseCode(timeSequence, null)
                + " Client sent: " + otp + " ,validate: " + isValid + " ,interval: " + timeSequence);
        if (!isValid) {
            isValid = passcodeGenerator.verifyTimeoutCode(timeSequence, otp);
            System.out.println("Try time window " + isValid);
        }
        return isValid;
    }
}
