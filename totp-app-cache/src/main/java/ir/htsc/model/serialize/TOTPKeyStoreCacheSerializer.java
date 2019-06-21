package ir.htsc.model.serialize;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofSerializer;
import com.tangosol.io.pof.PofWriter;
import ir.htsc.model.OTPKeyStoreCache;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Date;

/**
 * Created by MSH on 5/27/2019.
 */
public class TOTPKeyStoreCacheSerializer implements PofSerializer {
    private static final int KEY = 0;
    private static final int PASSWORD = 1;
    private static final int TRY_COUNT = 2;
    private static final int EXPIRE_AT = 3;
    private static final int TIME_OFFSET = 4;
    private static final int OTP_LENGTH = 5;
    private static final int LAST_OTP = 6;
    private static final int STEP = 7;//30,60
    private static final int LAST_USED_AT = 8;//30,60

    private final Logger LOGGER_NODE = Logger.getLogger("node");

    public void serialize(PofWriter writer, Object o) throws IOException {
        OTPKeyStoreCache object = (OTPKeyStoreCache) o;
        writer.writeByteArray(KEY, object.getKey());
        writer.writeString(PASSWORD, object.getPassword());
        writer.writeByte(TRY_COUNT, object.getTryCount());
        writer.writeDate(EXPIRE_AT, object.getExpireAt());
        writer.writeInt(TIME_OFFSET, object.getTimeOffset());
        writer.writeInt(OTP_LENGTH, object.getOtpLength());
        writer.writeString(LAST_OTP, object.getLastOTP());
        writer.writeInt(STEP, object.getStep());
        writer.writeDate(LAST_USED_AT, object.getLastUsedAt());

        writer.writeRemainder(null);
    }

    public Object deserialize(PofReader reader) throws IOException {
        OTPKeyStoreCache object = OTPKeyStoreCache.builder().key(reader.readByteArray(KEY))
                .password(reader.readString(PASSWORD))
                .tryCount(reader.readByte(TRY_COUNT))
                .expireAt( reader.readDate(EXPIRE_AT))
                .timeOffset( reader.readInt(TIME_OFFSET))
                .otpLength(reader.readInt(OTP_LENGTH))
                .lastOTP(reader.readString(LAST_OTP))
                .step( reader.readInt(STEP))
                .lastUsedAt( reader.readDate(LAST_USED_AT))
                .build();
        reader.readRemainder();
        return object;
    }
}
