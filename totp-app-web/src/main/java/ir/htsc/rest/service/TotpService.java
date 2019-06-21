package ir.htsc.rest.service;

import com.tangosol.net.NamedCache;
import ir.htsc.AppConstants;
import ir.htsc.dao.ProvisionDao;
import ir.htsc.dao.OtpKeyStoreDao;
import ir.htsc.entity.OTPEmbedded;
import ir.htsc.entity.OTPKeyStore;
import ir.htsc.entity.Provision;
import ir.htsc.enums.OTPUsedFor;
import ir.htsc.log.Loggable;
import ir.htsc.model.CreditCard;
import ir.htsc.model.OTPKeyStoreCache;
import ir.htsc.processor.GenerateSTOTPProcessor;
import ir.htsc.processor.ValidateSTOTPProcessor;
import ir.htsc.security.AES;
import ir.htsc.security.PasswordUtils;
import ir.htsc.security.UUIDGenerator;
import ir.htsc.security.otp.IOTP;
import ir.htsc.security.otp.STOTP;
import ir.htsc.util.CacheConstants;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by MSH on 5/27/2019.
 */
@Stateless
@Loggable
public class TotpService {

    private final static Logger LOGGER = Logger.getLogger(TotpService.class);
    @Inject
    private OtpKeyStoreDao totpKeyDao;
    @Inject
    private ProvisionDao provisionDao;

    @Resource(mappedName = "totp_key_cache")//Correct
    private NamedCache cacheCardMobile;

    public Provision provision(String cardNo, String mobileNo, String otpForStr, String otpStaticPassword) throws Exception {
        Provision provision = getProvision(cardNo, mobileNo, otpForStr, otpStaticPassword);
        provisionDao.edit(provision);
        System.out.println("#provision: " + provision);
        //TODO send  cardNo.skiped:otpForSt:provision.getOtpKeyPart1 to mobileNo with spc
        return provision;
    }

    public Provision active(String cardNo, String mobileNo, String otpForStr, String activationCode) throws Exception {
        System.err.println("cardNo = [" + cardNo + "], mobileNo = [" + mobileNo + "], otpForStr = [" + otpForStr + "], activationCode = [" + activationCode + "]");
        OTPKeyStore keyStore;
        BigDecimal id = new BigDecimal(mobileNo + cardNo + otpForStr);
        Provision provision = provisionDao.findById(id);//TODO can get from cache :-)
        AppConstants.print("Read From db id: " + id + " entity: " + provision);
        if (provision != null) {
            if (activationCode.equals(provision.getActivationCode())) {
                AppConstants.printErr("SEND BY SMS: " + provision.getOtpEncryptedKeyPart2());//iHL76In2C9YsXb3c3JAa5pvkYKZC5ku7eGPdPYPD
                provision.setStatus(1);
                provision.setUpdateAt(new Date());
                provision.setEnabled(false);
                provisionDao.edit(provision);
                keyStore = getOtpKeyStore(provision);
                AppConstants.print("provision keyStore = " + keyStore);
                register(cardNo, mobileNo, otpForStr, keyStore);
            } else {
                System.err.println("#Acivation Code is incorrect! activation code: " + activationCode);
            }
        } else {
            AppConstants.printErr("id not found in db: " + id);
        }
        return provision;
    }

    private OTPKeyStore getOtpKeyStore(Provision provision) {
        OTPKeyStore keyStore;
        keyStore = new OTPKeyStore();
        keyStore.setOtpEmbedded(provision.getOtpEmbedded());
        keyStore.setId(provision.getId());
        keyStore.setOtpEmbedded(provision.getOtpEmbedded());
        byte[] otpSecretKey = AES.decrypt(provision.getAesKeyPart1() + provision.getAesKeyPart2()
                , Base64.getDecoder().decode((provision.getOtpEncryptedKeyPart1() + provision.getOtpEncryptedKeyPart2())));
        AppConstants.printErr(" OTP KEY: " + Base64.getEncoder().encodeToString(otpSecretKey));
        keyStore.setKey(otpSecretKey);
        keyStore.setTryCount((byte) 0);
        return keyStore;
    }

    public boolean validate(BigDecimal id, String otp, String staticPassword, boolean checkLastOTP) {
        boolean result = false;
        try {
            OTPKeyStoreCache storeCache = (OTPKeyStoreCache) cacheCardMobile.get(id);
            fetchFromDbPutKeyStoreToCache(id, storeCache);
            result = (Boolean) cacheCardMobile.invoke(id, new ValidateSTOTPProcessor(otp, staticPassword, checkLastOTP));//process in cache
        } catch (Exception e) {
            LOGGER.error("Exception in handling otp Validation Request. reqStr:" + id, e);
        }
        return result;
    }

    public String generate(BigDecimal id) {
        String result = "";
        try {
            OTPKeyStoreCache storeCache = (OTPKeyStoreCache) cacheCardMobile.get(id);//TODO if expired? what we do?
            System.out.println("storeCache = " + storeCache);
            fetchFromDbPutKeyStoreToCache(id, storeCache);
            result = (String) cacheCardMobile.invoke(id, new GenerateSTOTPProcessor(1));//process in cache
        } catch (Exception e) {
            LOGGER.error("Exception in handling otp generate Request. reqStr:" + id, e);
        }
        return result;
    }

    private void fetchFromDbPutKeyStoreToCache(BigDecimal id, OTPKeyStoreCache storeCache) {
        OTPKeyStore keyStore;
        if (storeCache == null) {
            keyStore = totpKeyDao.findById(id);
            System.err.println("#validate read DB keyStore: " + keyStore);
            putKeyStoreToCache(keyStore);
        } else {
            System.err.println("#cache hit validate: " + id + " -> " + storeCache);
        }
    }

    private void putKeyStoreToCache(OTPKeyStore keyStore) {
        OTPKeyStoreCache storeCache;
        if (keyStore != null) {//put to cache
            storeCache = OTPKeyStoreCache.builder().key(keyStore.getKey())
                    .password(keyStore.getOtpEmbedded().getOtpStaticPassword())
                    .tryCount(keyStore.getTryCount())
                    .expireAt(keyStore.getOtpEmbedded().getExpireAt())
                    .timeOffset(keyStore.getOtpEmbedded().getTimeOffset())
                    .otpLength(keyStore.getOtpEmbedded().getOtpLength())
                    .lastOTP(keyStore.getLastOTP())
                    .step(keyStore.getOtpEmbedded().getStep())
                    .lastUsedAt(keyStore.getLastUsedAt())
                    .build();
            cacheCardMobile.put(keyStore.getId(), storeCache);
        }
    }

    private Provision getProvision(String cardNo, String mobileNo, String otpForStr, String otpStaticPassword) {
        OTPUsedFor otpUsedFor = OTPUsedFor.getEnum(Integer.valueOf(otpForStr));
        BigDecimal id = new BigDecimal(mobileNo + cardNo + otpForStr);
        mobileNo = CacheConstants.correctMobileNo(mobileNo);
        IOTP iotp = new STOTP(32, "HmacSHA256");
        Provision provision = new Provision();
        byte[] otpSecretKey = iotp.generateKey();//A9+dPkVbWWXoXskg3zPqG8eD0fgOR9WKEVgC9/onVvKAuXYxAVF5+073MyxRB3ZE
        AppConstants.printErr("OTP secretKey: " + Base64.getEncoder().encodeToString(otpSecretKey));
        provision.setId(id);
        OTPEmbedded otpEmbedded = new OTPEmbedded();
        CreditCard creditCard = new CreditCard(cardNo);
        otpEmbedded.setCardNo(creditCard);
        otpEmbedded.setCardNoSkipMasked(creditCard.getSkipMaskedNumber());
        otpEmbedded.setUsedFor(otpUsedFor);
        otpEmbedded.setStep(AppConstants.OTP_STEP_INTERVAL_SECOND);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1); // to get previous year add -1
        Date nextYear = cal.getTime();
        otpEmbedded.setExpireAt(nextYear);// TODO set End of month
        otpEmbedded.setAlgorithm("HmacSHA256");
        otpEmbedded.setUuid(UUIDGenerator.generateOTPClientToken());
        otpEmbedded.setOtpLength(otpUsedFor.lengthOfOTP);
        otpEmbedded.setMobileNo(mobileNo);
        otpEmbedded.setOtpStaticPassword(PasswordUtils.digestPassword(otpStaticPassword));
        otpEmbedded.setCardNo(new CreditCard(cardNo));
        provision.setOtpEmbedded(otpEmbedded);
        provision.setEnabled(true);
        provision.setAesKeyPart1(RandomStringUtils.randomNumeric(6));//keep in server then send to app
        provision.setAesKeyPart2(RandomStringUtils.randomAscii(10));//set to atm
        String aesKeyForOTPKey = provision.getAesKeyPart1() + provision.getAesKeyPart2();
        String OTPKeyEncryptedBase64 = AES.encrypt(aesKeyForOTPKey, otpSecretKey);//A9+dPkVbWWXoXskg3zPqG8eD0fgOR9WKEVgC9/onVvKAuXYxAVF5+073MyxRB3ZE
        System.out.println("OTPKeyEncryptedBase64: " + OTPKeyEncryptedBase64);
        provision.setOtpEncryptedKeyPart1(OTPKeyEncryptedBase64.substring(0, 30));//sample encrypted key: A9+dPkVbWWXoXskg3zPqG8eD0fgOR9
        provision.setOtpEncryptedKeyPart2(OTPKeyEncryptedBase64.substring(30));//sample 9WKEVgC9/onVvKAuXYxAVF5+073MyxRB3ZE
        return provision;
    }

    public OTPKeyStore register(String cardNo, String mobileNo, String otpForStr, OTPKeyStore keyStore) {
        System.out.println("cardNo = [" + cardNo + "], mobileNo = [" + mobileNo + "], otpForStr = [" + otpForStr + "], otpKeyStore = [" + keyStore + "]");
        OTPUsedFor otpUsedFor = OTPUsedFor.getEnum(Integer.valueOf(otpForStr));
        try {
            keyStore.setEnabled(true);
            OTPKeyStore keyStoreReadFromDB = totpKeyDao.edit(keyStore);
            putKeyStoreToCache(keyStore);
            System.err.println("#result keyStore: " + keyStore);
        } catch (Exception e) {
//            result = ResultCodesEnum.UnknownError;
            LOGGER.error("Exception in handling otp Register Request. reqStr:" + keyStore, e);
        } finally {
        }

        return keyStore;
    }
}
