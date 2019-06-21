package ir.htsc.model;

import lombok.*;

/**
 * @author me-sharifi on 5/28/2019 at 4:06 PM.
 */
@Setter
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TOTPUuidCache {
    private String mobileNo;
    private String cardNo;
    @ToString.Exclude
    private byte[] key;
    private String password;
    private byte tryCount;//ToDO Change byte
    private int timeOffset;
}
