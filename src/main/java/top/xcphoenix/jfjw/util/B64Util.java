package top.xcphoenix.jfjw.util;

import static java.lang.Integer.parseInt;

/**
 * 参考：https://blog.csdn.net/ldx19980108/article/details/81866351
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/4/15 下午5:42
 */
public class B64Util {

    public static String b64map = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    private static final char B64PAD = '=';

    /**
     * 获取对应16进制字符
     */
    private static char intToChar(int a) {
        String hexCode = "0123456789abcdef";
        return hexCode.charAt(a);
    }

    /**
     * Base64转16进制
     */
    public static String b64ToHex(String s) {
        StringBuilder ret = new StringBuilder();
        int k = 0;
        int slop = 0;
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == B64PAD) {
                break;
            }
            int v = b64map.indexOf(s.charAt(i));
            if (v < 0) {
                continue;
            }
            if (k == 0) {
                ret.append(intToChar(v >> 2));
                slop = v & 3;
                k = 1;
            } else if (k == 1) {
                ret.append(intToChar((slop << 2) | (v >> 4)));
                slop = v & 0xf;
                k = 2;
            } else if (k == 2) {
                ret.append(intToChar(slop));
                ret.append(intToChar(v >> 2));
                slop = v & 3;
                k = 3;
            } else {
                ret.append(intToChar((slop << 2) | (v >> 4)));
                ret.append(intToChar(v & 0xf));
                k = 0;
            }
        }
        if (k == 1) {
            ret.append(intToChar(slop << 2));
        }
        return ret.toString();
    }

    public static String hex2b64(String h) {
        int i, c;
        StringBuilder ret = new StringBuilder();
        for (i = 0; i + 3 <= h.length(); i += 3) {
            c = parseInt(h.substring(i, i + 3), 16);
            ret.append(b64map.charAt(c >> 6));
            ret.append(b64map.charAt(c & 63));
        }
        if (i + 1 == h.length()) {
            c = parseInt(h.substring(i, i + 1), 16);
            ret.append(b64map.charAt(c << 2));
        } else if (i + 2 == h.length()) {
            c = parseInt(h.substring(i, i + 2), 16);
            ret.append(b64map.charAt(c >> 2));
            ret.append(b64map.charAt((c & 3) << 4));
        }
        while ((ret.length() & 3) > 0) {
            ret.append(B64PAD);
        }
        return ret.toString();
    }
}