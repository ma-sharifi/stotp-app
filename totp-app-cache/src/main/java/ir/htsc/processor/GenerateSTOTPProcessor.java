package ir.htsc.processor;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofSerializer;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.processor.AbstractProcessor;
import ir.htsc.model.OTPKeyStoreCache;
import ir.htsc.security.otp.IOTP;
import ir.htsc.security.otp.STOTP;
import org.apache.log4j.Logger;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author me-sharifi on 6/11/2019 at 6:10 PM.
 * you dont need to use password for generate OTP but you need to password for validate OTP
 */
public class GenerateSTOTPProcessor extends AbstractProcessor {

    //
    public static final int OTP_CHANNEL_TYPE = 0;
    private transient final Logger LOGGER_NODE = Logger.getLogger("node");
    //    public static final int CARD_NO = 1;
//    public static final int PASSWORD = 2;
//
    int otpChannelType;

//    private String cardNo;
//    private String password;


    public GenerateSTOTPProcessor(int otpChannelType) {
        this.otpChannelType = otpChannelType;
    }

    @Override
    public Object process(InvocableMap.Entry entry) {
        String result = "";
        StopWatch stopWatch = new Log4JStopWatch();
        OTPKeyStoreCache read = (OTPKeyStoreCache) entry.getValue();
        IOTP otp = new STOTP(32);
        try {
            result = otp.generateToken(read.getKey());
        } catch (GeneralSecurityException e) {
            LOGGER_NODE.error("Exception in generate token : " + entry);
        } finally {
            LOGGER_NODE.info("[" + String.format("%-6d", stopWatch.getElapsedTime()) + " ms] "+ entry.getKey() + " -> generate otp result: " + result+ "->" + entry.getValue());
            stopWatch.stop("cache_generate_totp");
        }
        return result;
    }

    public static class Serializer implements PofSerializer {

        public void serialize(PofWriter writer, Object o) throws IOException {
            GenerateSTOTPProcessor processor = (GenerateSTOTPProcessor) o;
            writer.writeInt(OTP_CHANNEL_TYPE, processor.otpChannelType);
            writer.writeRemainder(null);
        }

        public Object deserialize(PofReader reader) throws IOException {
//            System.out.println("********** deserialize: "+reader);
            GenerateSTOTPProcessor processor = new GenerateSTOTPProcessor(
                    reader.readInt(OTP_CHANNEL_TYPE)
            );
            reader.readRemainder();
            return processor;
        }
    }
}
