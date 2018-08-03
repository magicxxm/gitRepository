package com.mushiny.wcs.common.utils;

import java.util.Random;

public class RandomUtil {
    private final static String NUM_CHAR = "0123456789";
    private static int charLen = NUM_CHAR.length();

    public static String getTripNo() {
        return getRandomNumber(20);
    }

    public static String getSortingNo() {
        return getRandomNumber(20);
    }

    public static String getPackNo() {
        return getRandomNumber(20);
    }

    public static String getUnitLoadLabel() {
        return getRandomNumber(20);
    }

    public static String getItemNo() {
        return getRandomNumber(15);
    }

    public static String getShipmentNo() {
        return getRandomNumber(15);
    }

    public static String getLotNo() {
        return getRandomNumber(20);
    }

    public static String getGrNo() {
        return getRandomNumber(15);
    }

    public static String getAdviceNo() {
        return getRandomNumber(15);
    }

    private static String getRandomNumber(int length) {
        StringBuilder builder = new StringBuilder();// 装载生成的随机数
        Random random = new Random(System.currentTimeMillis());// 生成系统时间随机数
        for (int i = 0; i < length; i++) {
            builder.append(NUM_CHAR.charAt(random.nextInt(charLen)));
        }
        return builder.toString();
    }
}
