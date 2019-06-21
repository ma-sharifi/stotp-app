package ir.htsc.enums;

public enum OTPChannelType {
    MOBILE_APP(0),
    SMS(1),
    PWA(2),
    USSD(3);

    private int value;

    private OTPChannelType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static OTPChannelType valueOf(byte v) {
        for(OTPChannelType o : OTPChannelType.values()) {
            if(o.getValue() == v)
                return o;
        }
        return null;
    }
}
