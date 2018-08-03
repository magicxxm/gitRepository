package com.mingchun.mu.mushiny.common;

/**
 * 错误故障码常量类
 *
 * Created by Laptop-6 on 2017/9/25.
 */
public class ConstantErrorCode {

    public static final int POD_UNMATCH_ERROR_CODE = 0x200B; // 货架码不匹配
    public static final int POD_UNFIND_ERROR_CODE = 0x2008; // 货架码扫不到

    public static int getConvertErrorCode(int errorCode){
        switch (errorCode){
            case POD_UNMATCH_ERROR_CODE:
                return 1;
            case POD_UNFIND_ERROR_CODE:
                return 2;
        }
        return 0;
    }



}
