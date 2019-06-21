package ir.htsc.enums;

/**
 * @author me-sharifi on 6/11/2019 at 3:20 PM.
 */

/**
 * Types of secret keys.
 */
public enum OTPType {  // must be the same as client
    DISABLED(0),
    TOTP(1),  // time based
    HOTP(2);  // counter based

    public final Integer value;

    OTPType(Integer value) {
        this.value = value;
    }

    public static OTPType getEnum(Integer i) {
        for (OTPType type : OTPType.values()) {
            if (type.value.equals(i)) {
                return type;
            }
        }

        return null;
    }
}