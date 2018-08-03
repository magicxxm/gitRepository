package com.mushiny.wms.masterdata.obbasics.common.service.util;


import org.apache.commons.lang.RandomStringUtils;

public final class RandomUtil {

    private static final int DEF_COUNT = 20;

    private RandomUtil() {
    }

    public static String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(DEF_COUNT);
    }

    public static String generateActivationKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }

    public static String generateResetKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }

    public static String generateClientNo() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }
}
