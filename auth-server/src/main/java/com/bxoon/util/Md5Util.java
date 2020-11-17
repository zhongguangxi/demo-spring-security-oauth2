package com.bxoon.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author 刘飞
 * @version 1.0.0
 * @comment
 * @date 2018/8/11
 * @Copyright: 东巴文信息技术有限公司
 */
@Slf4j
public class Md5Util {

    private final static String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 生成字符串的MD5编码的完整的32位
     *
     * @param orignal 原字符串
     * @return 源字符串的MD5编码32位
     */
    public static String str2Base32MD5(String orignal) {
        String resultString = null;
        try {
            resultString = orignal;
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes(StandardCharsets.UTF_8))).toLowerCase();
        } catch (Exception e) {
            log.error("", e);
        }
        return resultString;
    }

    /**
     * 转换字节数组为16进制字符串
     *
     * @param b 字节数组
     * @return 16进制字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        return HEX_DIGITS[n / 16] + HEX_DIGITS[n % 16];
    }

}
