package ir.htsc.entity;

import ir.htsc.serializer.GsonExcludeField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * @author me-sharifi
 */
@Entity
@Table(name = "T_OTP_KEY_STORE",
        uniqueConstraints = {@UniqueConstraint(name = "UNQ_OTP_KEY_UUID", columnNames = {"UUID"})}
        , indexes = {
        @Index(name = "IDX_OTP_KEY_MOBILE_NO", columnList = "MOBILE_NO"),
        @Index(name = "IDX_OTP_KEY_CARD_NO", columnList = "CARD_NO")
}
)
@XmlRootElement
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@Cacheable(false)
@NamedQueries({
        @NamedQuery(name = "TotpKeyStore.findAll", query = "SELECT m FROM OTPKeyStore m")

//        , @NamedQuery(name = "OTPKeyStore.findByOtpid", query = "SELECT m FROM OTPKeyStore m WHERE m.otpid = :otpid")
//        , @NamedQuery(name = "OTPKeyStore.findByOTPStatus", query = "SELECT m FROM OTPKeyStore m WHERE m.oTPStatus = :oTPStatus")
//        , @NamedQuery(name = "OTPKeyStore.findByTimeOffset", query = "SELECT m FROM OTPKeyStore m WHERE m.timeOffset = :timeOffset")
        , @NamedQuery(name = "TotpKeyStore.findByCreatedAt", query = "SELECT m FROM OTPKeyStore m WHERE m.createAt = :createAt")})
public class OTPKeyStore extends BaseUpdatableEntity{

    public static final String FIND_BY_UUID = "OTPKeyStore.findByUuid";

    @Id
    @NotNull
    @Column(name = "ID", length = 30)
//    @Convert(converter = CreditCardConverterForPersist.class)
    protected BigDecimal id;//989133480144603770123401

    @Embedded
    private OTPEmbedded otpEmbedded;

    @Column(name = "LAST_OTP", length = 10)
    private String lastOTP;

    @Column(name = "LAST_USEED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastUsedAt;

    //    @NotNull
    @Lob
    @Column(name = "OTP_KEY", nullable = false)
    @GsonExcludeField
    private byte[] key;

    @Column(name = "TRY_COUNT")
    private byte tryCount;

    public OTPKeyStore() {
    }

    // ======================================
    // =     Lifecycle Callback Methods     =
    // ======================================

//    @PreUpdate
//    private void onPreUpdate() {
//        setUpdateAt(new Date());
//        if (!"".equals(otpStaticPassword) && otpStaticPassword != null && otpStaticPassword.length() > 3) {
//            otpStaticPassword = PasswordUtils.digestPassword(otpStaticPassword);
//        }
//        if (cardNo != null && cardNo.isCardValid()) {
//            cardNoSkipMasked = cardNo.getSkipMaskedNumber();
//        }
//    }

    @PrePersist
    private void onPrePersis() {
        setCreateAt(new Date());

    }
    // ======================================
    // =          Getters & Setters         =
    // ======================================
}
