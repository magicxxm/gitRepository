import com.mingchun.mu.aricojf.platform.mina.message.robot.media.error.GeneralError;

import java.util.*;

/**
 * Created by Laptop-6 on 2017/11/15.
 */
public class TestByte {

    public static void main(String[] args) {

//        System.out.println(int2BitString(32769));
//        System.out.println("errorId="+getErrorId(2));

        String temp = "101010111";
        String[] temps = temp.split("");
        for(String a: temps){
            System.out.println(" - - > "+a);
        }

    }

    public static String int2BitString(int errorId) { //获取byte的二进制信息
        StringBuilder buf = new StringBuilder(Integer.toBinaryString(errorId));
        while (buf.length() < 16) {
            buf.insert(0, "0");
        }
        return buf.toString();
    }

    public static List<Map<String, Object>> getGeneralError(int generalErrorId){
        List<Map<String, Object>> res = new ArrayList<>();
        String bitStr = int2BitString(generalErrorId);
        String[] bits = bitStr.split("");
        for(int i = 0, len = bits.length; i < len; i++){
            String temp = bits[i];
            if("1".equals(temp)){
                GeneralError generalError = GeneralError.getEnumObject(i);
                Map<String, Object> tempMap = new HashMap<>();
                tempMap.put("generalErrorID", generalError.getErrorId());
                tempMap.put("bitPosition", generalError.getIndex());
                tempMap.put("description", generalError.getName());
                res.add(tempMap);
            }
        }
        return res;
    }


    public static int getErrorId(int bitPos){
        StringBuilder temp = new StringBuilder();
        for(int i = 0; i < 16; i++){
            temp.append("0");
        }
        System.out.println("1-->"+temp);
        temp.replace(bitPos, bitPos + 1, "1");
        System.out.println("2-->"+temp);
        temp.reverse();
        System.out.println("3-->"+temp);
        return Integer.valueOf(temp.toString(), 2);
    }





}
