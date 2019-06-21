package ir.htsc.model.serialize;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofSerializer;
import com.tangosol.io.pof.PofWriter;
import ir.htsc.model.TOTPUuidCache;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by MSH on 5/27/2019.
 */
public class TOTPUuidCacheSerializer implements PofSerializer {
    public static final int MOBILE_NO = 0;
    public static final int CARD_NO = 1;
    public static final int KEY = 2;
    public static final int PASSWORD = 3;
    public static final int TIME_OFFSET = 4;
    public static final int TRY_COUNT = 5;
    private final Logger LOGGER_NODE = Logger.getLogger("node");

    public void serialize(PofWriter writer, Object o) throws IOException {
        TOTPUuidCache object = (TOTPUuidCache) o;
        writer.writeString(MOBILE_NO, object.getMobileNo());
        writer.writeString(CARD_NO, object.getCardNo());
        writer.writeByteArray(KEY, object.getKey());
        writer.writeString(PASSWORD, object.getPassword());
        writer.writeInt(TIME_OFFSET, object.getTimeOffset());
        writer.writeByte(TRY_COUNT, object.getTryCount());

        writer.writeRemainder(null);
    }

    public Object deserialize(PofReader reader) throws IOException {
        String mobileNo = reader.readString(MOBILE_NO);
        String cardNo = reader.readString(CARD_NO);
        byte[] key = reader.readByteArray(KEY);
        String password = reader.readString(PASSWORD);
        int timeOffset = reader.readInt(TIME_OFFSET);
        byte tryCount = reader.readByte(TRY_COUNT);
        reader.readRemainder();
        
        TOTPUuidCache object = new TOTPUuidCache(mobileNo, cardNo, key, password, tryCount, timeOffset);
        return object;
    }
}
