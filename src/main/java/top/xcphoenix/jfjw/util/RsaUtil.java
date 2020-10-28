package top.xcphoenix.jfjw.util;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Random;

/**
 * 参考：https://blog.csdn.net/ldx19980108/article/details/81866351
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/4/15 下午6:19
 */
public class RsaUtil {

    private BigInteger n;
    private BigInteger e;

    public RsaUtil(String n, String e) {
        this.n = new BigInteger(n, 16);
        this.e = new BigInteger(e, 16);
    }

    public String rsaEncrypt(String pwd) {
        BigInteger r = rsaDoPublic(
                Objects.requireNonNull(pkcs1pad2(pwd, (n.bitLength() + 7) >> 3))
        );
        String sp = r.toString(16);
        if ((sp.length() & 1) != 0) {
            sp = "0" + sp;
        }
        return sp;
    }

    private BigInteger rsaDoPublic(BigInteger x) {
        return x.modPow(e, n);
    }

    private static BigInteger pkcs1pad2(String s, int n) {
        if (n < s.length() + 11) {
            System.err.println("Message too long for RSAEncoder");
            return null;
        }
        byte[] ba = new byte[n];
        int i = s.length() - 1;
        while (i >= 0 && n > 0) {
            int c = s.codePointAt(i--);
            // encode using utf-8
            if (c < 128) {
                ba[--n] = new Byte(String.valueOf(c));
            } else {
                Byte aByte = new Byte(String.valueOf((c & 63) | 128));
                ba[--n] = aByte;
                if (c < 2048) {
                    ba[--n] = new Byte(String.valueOf((c >> 6) | 192));
                } else {
                    ba[--n] = new Byte(String.valueOf(((c >> 6) & 63) | 128));
                    ba[--n] = new Byte(String.valueOf((c >> 12) | 224));
                }
            }
        }
        ba[--n] = new Byte("0");
        byte[] temp = new byte[1];
        Random rdm = new Random(47L);
        // random non-zero pad
        while (n > 2) {
            temp[0] = new Byte("0");
            while (temp[0] == 0) {
                rdm.nextBytes(temp);
            }
            ba[--n] = temp[0];
        }
        ba[--n] = 2;
        ba[--n] = 0;
        return new BigInteger(ba);
    }

}
