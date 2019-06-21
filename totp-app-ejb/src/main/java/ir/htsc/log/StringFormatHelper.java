package ir.htsc.log;

/**
 * Created by me-sharifi on 11/23/2016.
 */
public class StringFormatHelper {
    public static String getFormatForStackTrace(String clazz,String method){
        return " ,Class(" + String.format("%-25.25s", clazz) + ")" +
                " ,method(" + String.format("%-20.20s", method) + ")";
    }
    public static String getFormatForStatisticsTiming(String clazz,String method){
        return clazz + "_" + method;
    }
}
