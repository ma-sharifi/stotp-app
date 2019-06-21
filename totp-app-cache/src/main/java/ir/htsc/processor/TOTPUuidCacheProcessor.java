package ir.htsc.processor;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofSerializer;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.processor.AbstractProcessor;
import ir.htsc.model.OTPKeyStoreCache;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * validate a token by UUID
 * customer send token with UUID
 * key: UUID , value: OTPKeyStoreCache
 * web send Token to cache for validate;
 * <p>
 * <p>
 * Created by MSH on 5/27/2019.
 */
public class TOTPUuidCacheProcessor extends AbstractProcessor {//OTPKeyStoreCache=get(Token)
    private static final int TOKEN = 0;
    private static final int SERVER = 1;
    transient private final Logger LOGGER_NODE = Logger.getLogger("node");
    private String token;
    private String server;

    public TOTPUuidCacheProcessor(String token, String server) {
        this.token = token;
        this.server = server;
    }

    public Object process(InvocableMap.Entry entry) {
        OTPKeyStoreCache messageCacheRead = (OTPKeyStoreCache) entry.getValue();
        return messageCacheRead;
    }

    //------------------------------------------
    public static class Serializer implements PofSerializer {

        public void serialize(PofWriter writer, Object o) throws IOException {
            TOTPUuidCacheProcessor processor = (TOTPUuidCacheProcessor) o;
            writer.writeString(TOKEN, processor.token);
            writer.writeString(SERVER, processor.server);
            writer.writeRemainder(null);
        }

        public Object deserialize(PofReader reader) throws IOException {
//            System.out.println("********** deserialize: "+reader);
            TOTPUuidCacheProcessor processor = new TOTPUuidCacheProcessor(
                    reader.readString(TOKEN),
                    reader.readString(SERVER)
            );
            reader.readRemainder();
            return processor;
        }
    }
}