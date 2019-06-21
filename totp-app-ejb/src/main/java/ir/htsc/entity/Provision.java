package ir.htsc.entity;

import ir.htsc.serializer.GsonExcludeField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by MSH on 6/13/2019.
 */
@Entity
@Table(name = "T_PROVISION"
//        ,uniqueConstraints = {
//                @UniqueConstraint(name = "UNQ_OTP_KEY_UUID", columnNames = {"UUID"})}
//        , indexes = {
//        @Index(name = "IDX_OTP_KEY_MOBILE_NO", columnList = "MOBILE_NO"),
//        @Index(name = "IDX_OTP_KEY_CARD_NO", columnList = "CARD_NO")
//}
)
@XmlRootElement
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@Cacheable(false)
public class Provision extends BaseUpdatableEntity {

    @Id
    @NotNull
    @Column(name = "ID", length = 30)
//    @Convert(converter = CreditCardConverterForPersist.class)
    protected BigDecimal id;//989133480144603770123401

    @Embedded
    private OTPEmbedded otpEmbedded;

    //    @NotNull(message = "#otpEncryptedKeyPart1 cant be null")
    @Column(name = "OTP_ENCRYPTED_KEY_PART1", nullable = false, length = 200)
    @GsonExcludeField
    private String otpEncryptedKeyPart1;

    //    @NotNull(message = "#otpEncryptedKeyPart2 cant be null")
    @Column(name = "OTP_ENCRYPTED_KEY_PART2", nullable = false, length = 200)
    @GsonExcludeField
    private String otpEncryptedKeyPart2;

    //    @NotNull(message = "#aesKeyPart1 cant be null")
    @Column(name = "AES_KEY_PART1", nullable = false, length = 30)
    @GsonExcludeField
    private String aesKeyPart1;

    //    @NotNull(message = "#aesKeyPart2 cant be null")
    @Column(name = "AES_KEY_PART2", nullable = false, length = 200)
    private String aesKeyPart2;

    //    @NotNull(message = "#activationCode cant be null")
    @Column(name = "ACTIVIVATION_CODE", nullable = false, length = 10)
    private String activationCode;

    @PreUpdate
    private void onPreUpdate() {
        setUpdateAt(new Date());
        if (aesKeyPart1 != null && !"".equals(aesKeyPart1) && otpEncryptedKeyPart1 != null && !"".equals(otpEncryptedKeyPart1))
            activationCode = aesKeyPart1.substring(0, 2) + otpEncryptedKeyPart1.substring(0, 2);
    }

    @PrePersist
    private void onPrePersis() {
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        setCreateAt(now);
        if (aesKeyPart1 != null && !"".equals(aesKeyPart1) && otpEncryptedKeyPart1 != null && !"".equals(otpEncryptedKeyPart1))
            activationCode = aesKeyPart1.substring(0, 2) + otpEncryptedKeyPart1.substring(0, 2);
//        if (!"".equals(otpStaticPassword) && otpStaticPassword != null && otpStaticPassword.length() > 3) {
//            otpStaticPassword = PasswordUtils.digestPassword(otpStaticPassword);
//        }
//        if (cardNo != null && cardNo.isCardValid()) {
//            cardNoSkipMasked = cardNo.getSkipMaskedNumber();
//        }
    }

}
