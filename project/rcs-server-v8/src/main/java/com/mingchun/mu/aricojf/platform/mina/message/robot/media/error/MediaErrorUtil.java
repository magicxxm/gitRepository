package com.mingchun.mu.aricojf.platform.mina.message.robot.media.error;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Laptop-6 on 2017/11/15.
 */
public class MediaErrorUtil {
    public static int getErrorId(int bitPos){
        StringBuilder temp = new StringBuilder();
        for(int i = 0; i < 16; i++){
            temp.append("0");
        }
        temp.replace(bitPos, bitPos + 1, "1");
        temp.reverse();
        return Integer.valueOf(temp.toString(), 2);
    }

    public static StringBuilder int2BitString(int errorId, int num) { //获取byte的二进制信息
        StringBuilder buf = new StringBuilder(Integer.toBinaryString(errorId));
        while (buf.length() < num) {
            buf.insert(0, "0");
        }
        return buf;
    }
    public static String int2BitString(int errorId) { //获取byte的二进制信息
        StringBuilder res = int2BitString(errorId, 16);
        res.reverse();
        return res.toString();
    }

    public static String int4BitString(int errorId) { //获取byte的二进制信息
        StringBuilder res = int2BitString(errorId, 32);
        res.reverse();
        return res.toString();
    }

    public static List<Map<String, Object>> getGeneralError(int generalErrorId){
        return operError(generalErrorId, new ErrorTemplate() {
            @Override
            public void addError2List(List<Map<String, Object>> res, int i) {
                GeneralError generalError = GeneralError.getEnumObject(i);
                if(generalError != null){
                    Map<String, Object> tempMap = new HashMap<>();
                    tempMap.put("generalErrorID", generalError.getErrorId());
                    tempMap.put("bitPosition", generalError.getIndex());
                    tempMap.put("description", generalError.getName());
                    res.add(tempMap);
                }
            }
        });
    }
    public static List<Map<String, Object>> getCommonError(int commonErrorId){
        return operError(commonErrorId, new ErrorTemplate() {
            @Override
            public void addError2List(List<Map<String, Object>> res, int i) {
                CommonError commonError = CommonError.getEnumObject(i);
                if(commonError != null){
                    Map<String, Object> tempMap = new HashMap<>();
                    tempMap.put("commonErrorID", commonError.getErrorId());
                    tempMap.put("bitPosition", commonError.getIndex());
                    tempMap.put("description", commonError.getName());
                    res.add(tempMap);
                }
            }
        });
    }
    public static List<Map<String, Object>> getSeriousError(int seriousErrorId){
        return operError2(seriousErrorId, new ErrorTemplate() {
            @Override
            public void addError2List(List<Map<String, Object>> res, int i) {
                SeriousError seriousError = SeriousError.getEnumObject(i);
                if(seriousError != null){
                    Map<String, Object> tempMap = new HashMap<>();
                    tempMap.put("seriousErrorID", seriousError.getErrorId());
                    tempMap.put("bitPosition", seriousError.getIndex());
                    tempMap.put("description", seriousError.getName());
                    res.add(tempMap);
                }
            }
        });
    }
    public static List<Map<String, Object>> getLogicError(int logicErrorId){
        return operError(logicErrorId, new ErrorTemplate() {
            @Override
            public void addError2List(List<Map<String, Object>> res, int i) {
                LogicError logicError = LogicError.getEnumObject(i);
                if(logicError != null){
                    Map<String, Object> tempMap = new HashMap<>();
                    tempMap.put("logicErrorID", logicError.getErrorId());
                    tempMap.put("bitPosition", logicError.getIndex());
                    tempMap.put("description", logicError.getName());
                    res.add(tempMap);
                }
            }
        });
    }
    public static List<Map<String, Object>> operError(int errorId, ErrorTemplate errorTemplate){
        List<Map<String, Object>> res = new ArrayList<>();
        String bitStr = int2BitString(errorId);
        String[] bits = bitStr.split("");
        for(int i = 0, len = bits.length; i < len; i++){
            String temp = bits[i];
            if("1".equals(temp)){
                errorTemplate.addError2List(res, i);
            }
        }
        return res;
    }
    public static List<Map<String, Object>> operError2(int errorId, ErrorTemplate errorTemplate){
        List<Map<String, Object>> res = new ArrayList<>();
        String bitStr = int4BitString(errorId);
        String[] bits = bitStr.split("");
        for(int i = 0, len = bits.length; i < len; i++){
            String temp = bits[i];
            if("1".equals(temp)){
                errorTemplate.addError2List(res, i);
            }
        }
        return res;
    }
    interface ErrorTemplate{
        void addError2List(List<Map<String, Object>> res, int i);
    }

}


