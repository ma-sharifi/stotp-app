package ir.htsc.processor;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofSerializer;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.processor.AbstractProcessor;
import ir.htsc.model.OTPKeyStoreCache;
import ir.htsc.security.PasswordUtils;
import ir.htsc.security.otp.IOTP;
import ir.htsc.security.otp.STOTP;
import org.apache.log4j.Logger;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;

/**
 * @author me-sharifi on 6/11/2019 at 6:10 PM.
 */

public class ValidateSTOTPProcessor extends AbstractProcessor {

    private static final int TOTP = 0;
    private static final int STATIC_PASSWORD = 1;
    private static final int CHECK_LAST_OTP = 2;
    private final transient Logger LOGGER_NODE = Logger.getLogger("node");
    // ======================================
    // =             Attributes             =
    // ======================================
    private final String otp;
    private final String staticPassword;
    private final boolean checkLastOTP;


    public ValidateSTOTPProcessor(String otp, String staticPassword, boolean checkLastOTP) {
        this.otp = otp;
        this.staticPassword = staticPassword;
        this.checkLastOTP = checkLastOTP;
    }

    @Override
    public Object process(InvocableMap.Entry entry) {
        boolean result = true;
        StopWatch stopWatch = new Log4JStopWatch();
        OTPKeyStoreCache otpKeyStoreCacheRead = (OTPKeyStoreCache) entry.getValue();
        System.out.println("##otp: " + otp + " ,otpKeyStoreCacheRead.getLastOTP(): " + otpKeyStoreCacheRead.getLastOTP());
        if (result && checkLastOTP && otp.equals(otpKeyStoreCacheRead.getLastOTP())) {//if last
            result = false;
            LOGGER_NODE.error(String.format("%-12s"," ")+entry.getKey() + " -> Last OTP check : " + " , " + entry.getValue());
            System.out.println("##otp: " + otp + " ,Last OTP check : " + otpKeyStoreCacheRead.getLastOTP());
        }
        try {
            if (result &&!otpKeyStoreCacheRead.getPassword().equals(PasswordUtils.digestPassword(staticPassword))) {
                result = false;
                LOGGER_NODE.error(String.format("%-12s", " ") + entry.getKey() + "-> Password is wrong: " + " , " + entry.getValue());
                System.out.println("##otp: " + otp + " ,Password is wrong:: " + result);
            }
        }catch (Exception ex){
            result = false;
            LOGGER_NODE.error(String.format("%-12s", " ") + entry.getKey() + "-> Exception in check password: " + ex.getMessage());
        }
        if (result) {
            try {
                IOTP otp = new STOTP(32);
                result = otp.validateToken(otpKeyStoreCacheRead.getKey(), this.otp);
                if (result) {
                    otpKeyStoreCacheRead.setLastOTP(this.otp);
                    otpKeyStoreCacheRead.setLastUsedAt(new Date());
                    entry.setValue(otpKeyStoreCacheRead);
                }
                System.out.println("##otp: " + otp + " ,validate otp:: " + result);
                LOGGER_NODE.info("[" + String.format("%-6d", stopWatch.getElapsedTime()) + " ms] "  +entry.getKey()+ "-> validate TOTP -> result: " + result);
            } catch (GeneralSecurityException e) {
                LOGGER_NODE.error(String.format("%-12s"," ")+entry.getKey() + " Exception in validating TOTP : " + entry.getValue()+" ,result: "+result);
            } finally {
                stopWatch.stop("cache_validate_totp");
            }
        }
        return result;
    }

    //------------------------------------------
    public static class Serializer implements PofSerializer {

        public void serialize(PofWriter writer, Object o) throws IOException {
            ValidateSTOTPProcessor processor = (ValidateSTOTPProcessor) o;
            writer.writeString(TOTP, processor.otp);
            writer.writeString(STATIC_PASSWORD, processor.staticPassword);
            writer.writeBoolean(CHECK_LAST_OTP, processor.checkLastOTP);

            writer.writeRemainder(null);
        }

        public Object deserialize(PofReader reader) throws IOException {
            ValidateSTOTPProcessor processor = new ValidateSTOTPProcessor(
                    reader.readString(TOTP),
                    reader.readString(STATIC_PASSWORD),
                    reader.readBoolean(CHECK_LAST_OTP)
            );
            reader.readRemainder();
            return processor;
        }
    }
}
