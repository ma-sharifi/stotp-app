package ir.htsc.log;

/**
 * Created by m-asvadi on 11/19/2017.
 */
public enum TargetType {
    DB,
    SRV;

    @Override
    public String toString() {
        switch (this) {
            case DB:
                return "DB__";

            case SRV:
                return "SRV_";
            default:  return "";
        }
    }
}
