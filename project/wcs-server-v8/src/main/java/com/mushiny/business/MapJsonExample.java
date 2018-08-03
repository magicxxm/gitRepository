package com.mushiny.business;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tank.li on 2017/7/26.
 */
public class MapJsonExample{

    public static void main1(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = "";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", "mkyong");
            map.put("age", 29);
            // convert map to JSON string
            json = mapper.writeValueAsString(map);
            System.out.println("===>"+json);
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
            // pretty print
            System.out.println(json);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = "{\"name\":\"mkyong\", \"age\":29}";
            Map<String, Object> map = new HashMap<String, Object>();
            // convert JSON string to Map
            map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});//比 Map<String, String> 准确
            System.out.println(map);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}