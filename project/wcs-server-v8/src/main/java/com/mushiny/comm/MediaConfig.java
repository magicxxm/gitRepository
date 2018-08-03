package com.mushiny.comm;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by zf on 2017/11/17.
 */
@Component
public class MediaConfig {
    private Properties properties = new Properties();
    private String defaultFilePath = "/mediaPods.properties";

    public Properties getProperties() {
        if (properties == null || properties.isEmpty()){
            init(defaultFilePath);
        }
        return properties;
    }

    public MediaConfig() {
        init(defaultFilePath);
    }

    private void init(String filePath){
        //读取属性文件a.properties
        try {
            if (CommonUtils.isEmpty(filePath)){
                filePath = defaultFilePath;
            }
            InputStream in = MediaConfig.class.getResourceAsStream(filePath);
            properties.load(in);     ///加载属性列表
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public String getProperty(String key){
        if (properties==null || properties.isEmpty()){
            return "AGVConfigParameters配置文件为空！";
        }
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value){
        if (properties == null){
            init(defaultFilePath);
        }
        properties.setProperty(key, value);
    }

    ///保存属性到b.properties文件
    public void addProperty(String key, String value){
        System.out.println("addProperty:"+key+" "+value);
        if (properties == null || properties.isEmpty()){
            init(defaultFilePath);
        }
        try {
            String filePath = AGVConfigParameters.class.getResource(defaultFilePath).toString();
            filePath = filePath.substring(6);
            File file = new File(filePath);
            if (!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file, true);
            properties.setProperty(key, value);
            properties.store(fos, "add new properties!");
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AGVConfigParameters t = new AGVConfigParameters();
        t.addProperty("b", "3");
    }*/

}
