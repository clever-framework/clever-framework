package io.github.toquery.framework.common.util;

/**
 * <a href="https://www.jianshu.com/p/ead5b08e9839">简书</a>
 * <p>
 * 身份证号码的编码规则
 * 身份证号码共18位，由17位本体码和1位校验码组成。
 * <p>
 * 前6位是地址码，表示登记户口时所在地的行政区划代码，依照《中华人民共和国行政区划代码》国家标准（GB/T2260）的规定执行；
 * 7到14位是出生年月日，采用YYYYMMDD格式；
 * 15到17位是顺序码，表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编订的顺序号，顺序码的奇数分配给男性，偶数分配给女性，即第17位奇数表示男性，偶数表示女性；
 * 第18位是校验码，采用ISO 7064:1983, MOD 11-2校验字符系统，计算规则下一章节说明。
 * 一代身份证与二代身份证的区别在于：
 * <p>
 * 一代身份证是15位，二代身份证是18位；
 * 一代身份证出生年月日采用YYMMDD格式，二代身份证出生年月日采用YYYYMMDD格式；
 * 一代身份证无校验码，二代身份证有校验码。
 */
public class AppChinaIdCardUtils {

    /**
     * 18位二代身份证号码的正则表达式
     */
    public static final String REGEX_ID_NO_18 = "^"
            + "\\d{6}" // 6位地区码
            + "(18|19|([23]\\d))\\d{2}" // 年YYYY
            + "((0[1-9])|(10|11|12))" // 月MM
            + "(([0-2][1-9])|10|20|30|31)" // 日DD
            + "\\d{3}" // 3位顺序码
            + "[0-9Xx]" // 校验码
            + "$";

    /**
     * 15位一代身份证号码的正则表达式
     */
    public static final String REGEX_ID_NO_15 = "^"
            + "\\d{6}" // 6位地区码
            + "\\d{2}" // 年YYYY
            + "((0[1-9])|(10|11|12))" // 月MM
            + "(([0-2][1-9])|10|20|30|31)" // 日DD
            + "\\d{3}"// 3位顺序码
            + "$";


    // 加权因子
    public static final int[] W = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};


    /**
     * 校验身份证号码
     *
     * <p>
     * 适用于18位的二代身份证号码
     * </p>
     *
     * @param IDNo18 身份证号码
     * @return  true - 校验通过 <br> false - 校验不通过
     * @throws IllegalArgumentException 如果身份证号码为空或长度不为18位或不满足身份证号码组成规则 <i>6位地址码+ 出生年月日YYYYMMDD+3位顺序码 +0~9或X(x)校验码</i>
     */
    public static boolean checkIDNo(String IDNo18) {
        // 校验身份证号码的长度
        if (!checkStrLength(IDNo18, 18)) {
            throw new IllegalArgumentException();
        }
        // 匹配身份证号码的正则表达式
        if (!regexMatch(IDNo18, REGEX_ID_NO_18)) {
            throw new IllegalArgumentException();
        }
        // 校验身份证号码的验证码
        return validateCheckNumber(IDNo18);
    }

    /**
     * 校验字符串长度
     *
     * @param inputString 字符串
     * @param len         预期长度
     * @return true - 校验通过 <br> false - 校验不通过
     */
    private static boolean checkStrLength(String inputString, int len) {
        if (inputString == null || inputString.length() != len) {
            return false;
        }
        return true;
    }

    /**
     * 匹配正则表达式
     *
     * @param inputString 字符串
     * @param regex       正则表达式
     * @return true - 校验通过 <br> false - 校验不通过
     */
    private static boolean regexMatch(String inputString, String regex) {
        return inputString.matches(regex);
    }

    /**
     * 校验码校验
     * <p>
     * 适用于18位的二代身份证号码
     * </p>
     *
     * @param IDNo18 身份证号码
     * @return true - 校验通过 <br> false - 校验不通过
     */
    private static boolean validateCheckNumber(String IDNo18) {
        char[] IDNoArray = IDNo18.toCharArray();
        int sum = 0;
        for (int i = 0; i < W.length; i++) {
            sum += Integer.parseInt(String.valueOf(IDNoArray[i])) * W[i];
        }
        // 校验位是X，则表示10
        if (IDNoArray[17] == 'X' || IDNoArray[17] == 'x') {
            sum += 10;
        } else {
            sum += Integer.parseInt(String.valueOf(IDNoArray[17]));
        }
        // 如果除11模1，则校验通过
        return sum % 11 == 1;
    }

    /**
     * 计算身份证号码的校验码
     * <p>
     * 适用于18位的二代身份证号码，身份证号码由17位本体码和1位校验码组成
     * </p>
     *
     * @param masterNumber 本体码
     * @return 身份证号码
     * @throws IllegalArgumentException 如果本体码为空或长度不为17位或不满足本体码组成规则 <i>6位地址码+ 出生年月日YYYYMMDD+3位顺序码</i>

    public static String computeIDNoCheckNumber(String masterNumber) {
    // 校验本体码的长度
    if (!checkStrLength(masterNumber, 17)) {
    throw new IllegalArgumentException();
    }
    // todo https://www.jianshu.com/p/ead5b08e9839
    // 匹配本体码的正则表达式
    if (!regexMatch(masterNumber, REGEX_MASTER_NUMBER)) {
    throw new IllegalArgumentException();
    }
    // 计算校验码
    String checkNumber = computeCheckNumber(masterNumber);
    // 返回本体码+校验码=完整的身份证号码
    return masterNumber + checkNumber;
    }
     */

    /**
     * 计算校验码
     * <p>
     * 适用于18位的二代身份证号码
     * </p>
     *
     * @param masterNumber 本体码
     * @return 校验码
     */
    private static String computeCheckNumber(String masterNumber) {
        char[] masterNumberArray = masterNumber.toCharArray();
        int sum = 0;
        for (int i = 0; i < W.length; i++) {
            sum += Integer.parseInt(String.valueOf(masterNumberArray[i])) * W[i];
        }
        // 根据同余定理得到的校验码数组
        String[] checkNumberArray = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        // 得到校验码 返回校验码
        return checkNumberArray[sum % 11];
    }

    /**
     * 15位一代身份证号码升级18位二代身份证号码
     * <p>
     * 为15位的一代身份证号码增加年份的前2位和最后1位校验码
     * </p>
     *
     * @param IDNo15 15位的一代身份证号码
     * @return 18位的二代身份证号码
     */
    public static String updateIDNo15to18(String IDNo15) {
        // 校验身份证号码的长度
        if (!checkStrLength(IDNo15, 15)) {
            throw new IllegalArgumentException();
        }
        // 匹配身份证号码的正则表达式
        if (!regexMatch(IDNo15, REGEX_ID_NO_15)) {
            throw new IllegalArgumentException();
        }
        // 得到本体码，因一代身份证皆为19XX年生人，年份中增加19，组成4位
        String masterNumber = IDNo15.substring(0, 6) + "19" + IDNo15.substring(6);
        // 计算校验码
        String checkNumber = computeCheckNumber(masterNumber);
        // 返回本体码+校验码=完整的身份证号码
        return masterNumber + checkNumber;
    }
}
