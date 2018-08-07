package com.mushiny.utils;

import com.mushiny.constants.ChangeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Created by 123 on 2018/2/22.
 */
public class StringUtil {

    private final static Logger logger = LoggerFactory.getLogger(StringUtil.class);
    /*
     *String 转 int
     */
    public static int stringToint(String number){
        int i = 0;
        try {

            i = Integer.valueOf(number).intValue();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return i;
    }

    /**
     * String to double
     * @param number
     * @return
     */
    public static BigDecimal stringToDouble(String number){
        Double i = 0.0;
        try {

             i = Double.valueOf(number).doubleValue();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return new BigDecimal(i);
    }

    public static int stringToInt(String ztbtri){

        if(ChangeType.PU_TONG.equalsIgnoreCase(ztbtri)){
            return 0;
        }

        if(ChangeType.JIN_JI.equalsIgnoreCase(ztbtri)){
            return 1;
        }
        if(ChangeType.JIA_JI.equalsIgnoreCase(ztbtri)){
            return 2;
        }
        return 0;
    }
}
