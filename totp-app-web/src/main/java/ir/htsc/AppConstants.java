package ir.htsc;

public class AppConstants {

    public static final String VERSION_NO = "1.0.0";
    public static final String BUILD_DATE = "2019-06-13";

    public static volatile int OTP_STEP_INTERVAL_SECOND = 30;

    public static String host_Address;
    public static String host_Name;

    public static void print(String msg){
        System.out.println("##->"+msg);
    }
    public static void printErr(String msg){
        System.err.println("##->"+msg);
    }
    public static boolean checkLuhnCheckDigit(String cardNo) {
        int sum = 0;
        for(int i= cardNo.length()-1; i>=0; i--) {
            if(i%2 == 0)
            {
                int d = (cardNo.charAt(i) - 48) * 2;
                if(d > 9) d = d - 9;
                sum += d;
            }
            else
                sum += (cardNo.charAt(i)-48);
        }
        return (sum % 10 == 0);
    }
    public static String maskCardAccountNo(String source){
        String masked="";
        if(source!=null && source.length()>3){
            if(source.length()>15){
                masked=source.substring(0,6)+"******"+source.substring(12);//6+6+4
            }else
                masked="***"+source.substring(4);
        }
        return masked;
    }
    public static String skipMaskCardAccountNo(String source){
        String masked="";
        if(source!=null && source.length()>3){
            if(source.length()>15){
                masked=source.substring(0,6)+source.substring(12);
            }else
                masked=source.substring(4);
        }
        return masked;
    }
}
