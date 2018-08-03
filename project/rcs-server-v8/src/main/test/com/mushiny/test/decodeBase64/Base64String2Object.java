package com.mushiny.test.decodeBase64;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/6 0006.
 */
public class Base64String2Object {


    public static void main(String[] args) {
        String str = "rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAN3CAAAAAQAAAABdAAUYXZhaWxh\n" +
                "YmxlQWRkcmVzc0xpc3RzcgATamF2YS51dGlsLkFycmF5TGlzdHiB0h2Zx2GdAwABSQAEc2l6ZXhwAAAAAncEAAAAAnNyABFqYXZhLmxhbmcuSW50ZWdlchLi\n" +
                "oKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAGSc3EAfgAFAAABk3h4";


        System.out.println("Base64 编码后：" + base64String2Object(str));
    }

    public static Map<String, Object> base64String2Object(String base64String){
        Base64 base64 = new Base64();
        Map<String, Object> message = (Map<String, Object>) toObject(base64.decode(base64String));
        return message;
    }

    public static Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }
}
