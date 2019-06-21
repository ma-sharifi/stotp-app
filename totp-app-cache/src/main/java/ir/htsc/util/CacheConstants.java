package ir.htsc.util;

/**
 * @author MSH on 6/20/2019 at 05:10 PM.
 */
public class CacheConstants {
    public static String correctMobileNo(String mobileNo) {
        mobileNo = mobileNo.trim();
        if (mobileNo.startsWith("+")) //+989133480144
            mobileNo = mobileNo.substring(1);
        if (isNumericLong(mobileNo)) {
            if (mobileNo.length() == 13 || (mobileNo.length() == 12 && mobileNo.substring(0, 2).equals("98"))) {//989133480144
            } else if (mobileNo.length() == 11 && mobileNo.substring(0, 1).equals("0")) {
                mobileNo = ("98" + mobileNo.substring(1));
            } else if (mobileNo.length() == 10 && mobileNo.substring(0, 1).equals("9")) {
                mobileNo = ("98" + mobileNo);
            }else
               throw  new RuntimeException("can't correct mobile No: "+mobileNo);
        }else throw new RuntimeException("mobile No is not numeric! mobile No:" +mobileNo);
        return mobileNo;
    }

    public static boolean isNumericLong(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println(correctMobileNo("09133480144"));
        System.out.println(correctMobileNo("9133480144"));
        System.out.println(correctMobileNo("+989133480144"));
        System.out.println(correctMobileNo("989133480144"));
        System.out.println(correctMobileNo("9799133480144"));
        System.out.println(correctMobileNo("89799133480144"));
        System.out.println(correctMobileNo("sa9133480144a"));
    }
}
