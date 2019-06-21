package ir.htsc.model;

import lombok.*;

import java.util.Date;

/**
 * Created by MSH on 5/27/2019.
 */
@Setter
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class OTPKeyStoreCache {//this is a value that put into cache
    @ToString.Exclude
    private byte[] key;
    @ToString.Exclude
    private String password;
    private byte tryCount;
    private Date expireAt;
    private int timeOffset;
    private int otpLength;
    private String lastOTP;
    private int step;
    private Date lastUsedAt;
}
