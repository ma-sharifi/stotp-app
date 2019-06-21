package ir.htsc.rest.dto;

import ir.htsc.enums.OTPUsedFor;
import ir.htsc.serializer.GsonModel;
import lombok.Setter;

import java.util.Date;

/**
 * @author MSH on 6/21/2019 at 01:38 AM.
 */
@Setter
public class OTPResponseDto extends GsonModel{
    private String algorithm;
    private String activationKey1;
    private Date expireAt;
    private String mobileNo;
    private String cardNo;
    private String key;
    private OTPUsedFor usedFor;
    private int length;
    private int step;
}
