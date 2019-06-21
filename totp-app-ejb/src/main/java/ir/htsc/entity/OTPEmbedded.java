package ir.htsc.entity;

import ir.htsc.entity.convertor.CreditCardConverterForPersist;
import ir.htsc.entity.convertor.OTPUsedForConvertor;
import ir.htsc.enums.OTPUsedFor;
import ir.htsc.model.CreditCard;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

/**
 * @author MSH on 6/20/2019 at 07:22 PM.
 */
@Embeddable
@Getter
@Setter
@ToString
public class OTPEmbedded {

//    @NotNull(message = "#mobileNo cant be null")
//    @Size(min = 11, max = 15, message = "#Mobile no Must be this format 989XXXXXXXXX for example 989133480144")
    @Column(name = "MOBILE_NO", length = 13, nullable = false)
    protected String mobileNo;

    @Column(name = "ALGORITHM", length = 10)
    protected String algorithm;

    //    @NotNull
    @Column(name = "USED_FOR")
    @Convert(converter = OTPUsedForConvertor.class)
    protected OTPUsedFor usedFor;

    @Column(name = "CARD_NO", length = 200)
    @Convert(converter = CreditCardConverterForPersist.class)
    protected CreditCard cardNo;// encrypted //6037705678

    @Column(name = "CARD_SKIP_MASKED", length = 20)
    protected String cardNoSkipMasked;//6037705678

    //    @NotNull
//    @Size(min = 20, max = 30)
    @Column(name = "UUID", length = 30)
    protected String uuid;
    //
//    @Basic(optional = false)
//    @NotNull
    @Column(name = "OTP_LENGTH")
    protected int otpLength;
    @Column(name = "TIME_OFFSET")
    protected int timeOffset;

    @Column(name = "STEP")
    protected int step;

    //    @Size(max = 250)
    @Column(name = "OTP_STATIC_PASSWORD", length = 250)
    private String otpStaticPassword;//for SMS and USSD method

    //    @Basic(optional = false)
    @Column(name = "EXPIRE_AT")
    @XmlElement(name = "expire_at")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date expireAt;

}
