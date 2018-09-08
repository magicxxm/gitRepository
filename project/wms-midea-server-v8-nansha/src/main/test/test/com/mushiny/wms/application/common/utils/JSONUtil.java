package test.com.mushiny.wms.application.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/8/20.
 */
public class JSONUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtil.class);
    private static ObjectMapper objectMapper = new ObjectMapper();
    private final static JsonParser parser = JsonParserFactory.getJsonParser();

    /**
     * 将Map转换成json
     *
     * @param data
     * @param data
     * @return
     */
    public static String mapToJSon(Map data) {
        String result = "";

        if ((data != null) && (!data.isEmpty())) {
            try {

                result = objectMapper.writeValueAsString(data);
            } catch (JsonProcessingException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return result;
    }

    public static String toJSon(Object object) {
        String result = "";
        if (!ObjectUtils.isEmpty(object)) {
            try {
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                result = objectMapper.writeValueAsString(object);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }

        }

        return result;
    }

    public static List jsonToList(String json) {
        return parser.parseList(json);
    }

    public static Map jsonToMap(String object) {

            Map result = null;

            try {

                result = parser.parseMap(object);
            } catch (Exception e) {

                LOGGER.error("解析"+object+"\n"+e.getMessage(), e);
            }


        return result;
    }

    public static <T> T jsonToJavaBean(String jsonString,Class<T> targetClass)
    {
        T result = null;
        if (!StringUtils.isEmpty(jsonString)) {
            try {

                result = objectMapper.readValue(jsonString,targetClass);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }

        }
        return result;

    }


   /* public static void main(String[] args) {
        try{

            ObjectMapper mapper = new ObjectMapper();
            String jsonString = "{\"name\":\"Mahesh\", \"age\":21}";



            //json字符串转为Map对象
            Map map=mapper.readValue(jsonString, Map.class);
            System.out.println(map);

        }catch(Exception e) {
            e.printStackTrace();
        }
    }
*/
}
