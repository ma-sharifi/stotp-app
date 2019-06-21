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
public class TotpKeyCacheProcessor extends AbstractProcessor {
    private final Logger LOGGER_NODE = Logger.getLogger("node");

    public static final int MOBILE_NO = 0;
    public static final int CARD_NO = 1;
    public static final int KEY = 2;
    public static final int PASSWORD = 3;
    public static final int TIME_OFFSET = 4;
    public static final int SERVER = 5;

    transient Logger logger;

    private String mobileNo;
    private String cardNo;
    private byte[] key;
    private String password;
    private long timeOffset;

//    @Override
//    public Object process(InvocableMap.Entry entry) {
//        int lenSmsHeader = 2/*AppConstants.SMS_HEADER_CTM_LENGTH*/ + 2/*AppConstants.SMS_HEADER_PARTCOUNT_LENGTH*/;
//        MessageCache messageCacheRead = (MessageCache) entry.getValue();
//        try {
//            if (messageCacheRead == null) {
//                messageCacheRead = new MessageCache();
//            }
////        LOGGER_NODE.info("*****Read mapTranNo From MessageCache: " + messageCacheRead.getMapTranNo());
//
//            Map<Integer, String> mapMessagePartSet = messageCacheRead.getMapTranNo().get(tranNo);
////        LOGGER_NODE.info("**READ FROM CACHE mapMessagePartSet: " + mapMessagePartSet);
//            if (mapMessagePartSet == null) {
//                mapMessagePartSet = new TreeMap<>();
//            }
//            int currentMessagePart = ParseDataType.tryParsInt(messageBody.substring(2, lenSmsHeader));
//            mapMessagePartSet.put(currentMessagePart, messageBody.substring(lenSmsHeader));//13-15
//
//            messageCacheRead.getMapTranNo().put(tranNo, mapMessagePartSet);
//            messageCacheRead.setMobileNo(from);
//            messageCacheRead.setMaxPartNo(maxPart);
//            messageCacheRead.setMessageId(messageId);
//            messageCacheRead.setTranNo(tranNo);
//            messageCacheRead.setMapId(ParseDataType.tryParsLong(tranNo));
//            messageCacheRead.setDateOfInsert(new Date());
////        System.err.println("-------*********after put to messageCache: " + messageCache);
//            entry.setValue(messageCacheRead);
//            if (messageCacheRead.getMapTranNo() != null && (messageCacheRead.getMapTranNo().get(messageCacheRead.getTranNo()).size() == messageCacheRead.getMaxPartNo())) {
//                LOGGER_NODE.info(from + "," + messageId + "->" + "tranNo: " + messageCacheRead.getTranNo() +
//                        " .Last part To PROCESS (len:" + String.format("%3d",messageBody.length()) + ").Map size this tran is  :" +
//                        +mapMessagePartSet.size() + " -> part: " + partNo + " of " + maxPart + " is received. from server: "+server+
////                        " .message is: " + printPrivateString(messageBody));
//                        " .message is: " + messageBody);
//                return considerCacheForRequest(messageCacheRead);
//            } else {
//                LOGGER_NODE.info(from + "," + messageId + "->" + "tranNo: " + messageCacheRead.getTranNo() +
//                        " .Put Message To cache (len:" +  String.format("%3d",messageBody.length()) + ").cache size this tran is:" +
//                        +mapMessagePartSet.size() + " -> part: " + partNo + " of " + maxPart + " is received. from server: "+server+
////                        " .message is: " + printPrivateString(messageBody));
//                        " .message is: " + messageBody);
//            }
//        } catch (Exception ex) {
//            LOGGER_NODE.log(Level.ERROR, from + "," + messageId , ex);
//            return  null;
//        }
//        return messageCacheRead;
//    }
    private String server;
    public TotpKeyCacheProcessor() {
    }

    public TotpKeyCacheProcessor(String mobileNo, String cardNo, byte[] key, String password, long timeOffset, String server) {
        this.mobileNo = mobileNo;
        this.cardNo = cardNo;
        this.key = key;
        this.password = password;
        this.timeOffset = timeOffset;
        this.server = server;
    }

//    public String printPrivateString(String source) {
//        String destination = "";
//        if (source != null && source.length() > SMS_IN_BODY_LENGTH_FOR_SAVE_TO_DB)
//            destination = source.substring(0, SMS_IN_BODY_LENGTH_FOR_SAVE_TO_DB) + PRIVATE_MESSAGE +
//                    source.substring(source.length() - 3, source.length());
//
//        else
//            destination = source;
//        return destination;
//    }

    public Object process(InvocableMap.Entry entry) {
        OTPKeyStoreCache messageCacheRead = (OTPKeyStoreCache) entry.getValue();
        return messageCacheRead;
    }

    //------------------------------------------
    public static class Serializer implements PofSerializer {

        public void serialize(PofWriter writer, Object o) throws IOException {
//            System.out.println("********** Serializer: "+o);
            TotpKeyCacheProcessor processor = (TotpKeyCacheProcessor) o;
            writer.writeString(MOBILE_NO, processor.mobileNo);
            writer.writeString(CARD_NO, processor.cardNo);
            writer.writeByteArray(KEY, processor.key);
            writer.writeString(PASSWORD, processor.password);
            writer.writeLong(TIME_OFFSET, processor.timeOffset);
            writer.writeString(SERVER, processor.server);

            writer.writeRemainder(null);
        }

        public Object deserialize(PofReader reader) throws IOException {
//            System.out.println("********** deserialize: "+reader);
            TotpKeyCacheProcessor processor = new TotpKeyCacheProcessor(
                    reader.readString(MOBILE_NO),
                    reader.readString(CARD_NO),
                    reader.readByteArray(KEY),
                    reader.readString(PASSWORD),
                    reader.readLong(TIME_OFFSET),
                    reader.readString(SERVER)
            );
            reader.readRemainder();
            return processor;
        }
    }
}