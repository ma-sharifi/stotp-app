package ir.htsc.enums;

/**
 * @author MSH on 6/20/2019 at 02:36 PM.
 */
public enum OTPUsedFor {
    CARD_FIRST_PASS(1, 4,30),
    CARD_SECOND_PASSWORD(2, 6,30);

    public final Integer value;
    public final Integer lengthOfOTP;
    public final Integer step;

    OTPUsedFor(Integer value, Integer lengthOfOTP,Integer step) {
        this.value = value;
        this.lengthOfOTP = lengthOfOTP;
        this.step = step;
    }

    public static OTPUsedFor getEnum(Integer value) {
        for (OTPUsedFor type : OTPUsedFor.values()) {
            if (type.value.equals( value)) {
                return type;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }
}
