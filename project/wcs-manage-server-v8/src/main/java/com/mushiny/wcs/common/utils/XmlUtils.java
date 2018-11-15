package com.mushiny.wcs.common.utils;

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.lang.reflect.Method;

/**
 * @author:
 * @Description: Created by wangjianwei on 2017/12/14.
 */
@Component
public class XmlUtils {
    private ResourceLoader resourceLoader = new DefaultResourceLoader();
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlUtils.class);
    private static final String DEFAULTPATH = "classpath:release.xml";

    public <T> T load(File file, Class<T> root) {
        T person = null;
        try {
            SAXReader reader = new SAXReader();
            reader.setEncoding("utf-8");
            //创建一个BeanReader实例，相当于转换器
            BeanReader beanReader = new BeanReader();

            //配置BeanReader实例
            beanReader.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(false);
            beanReader.getBindingConfiguration().setMapIDs(false); //不自动生成ID
            //注册要转换对象的类，并指定根节点名称

            beanReader.registerBeanClass(root.getSimpleName(), root);
            //将XML解析Java Object
            person = (T) beanReader.parse(new FileInputStream(file));

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return person;
    }

    public <T> boolean java2xmlFile(String releaseFile, T root) {

        try {
            boolean reslutXml;

            //创建一个输出流，将用来输出Java转换的XML文件
            FileWriter outputWriter = null;
            outputWriter = new FileWriter(releaseFile);
            //输出XML的文件头
            outputWriter.write("<?xml version='1.0' encoding='UTF-8'?>");
            //创建一个BeanWriter实例，并将BeanWriter的输出重定向到指定的输出流
            BeanWriter beanWriter = new BeanWriter(outputWriter);

            //配置BeanWriter对象
            beanWriter.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(false);
            beanWriter.getBindingConfiguration().setMapIDs(false); //不自动生成ID
            beanWriter.setWriteEmptyElements(true);         //输出空元素
            beanWriter.enablePrettyPrint();         //格式化输出

            //构建要转换的对象
            T rootElement = BeanUtils.instantiate((Class<T>) root.getClass());
            Method[] rootMethods = ReflectionUtils.getAllDeclaredMethods(root.getClass());
            for (Method m : rootMethods) {
                String methodName = m.getName();

                if (methodName.startsWith("set")) {
                    String fileName = methodName.substring(3, methodName.length());
                    String getName = "get" + StringUtils.capitalize(fileName);
                    Method getMethod = ReflectionUtils.findMethod(root.getClass(), getName);
                    ReflectionUtils.invokeMethod(m, rootElement, ReflectionUtils.invokeMethod(getMethod, root));
                }
            }

            beanWriter.write(rootElement);
            outputWriter.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }


        return true;
    }
   /* public static void main(String[] args) throws Exception {
        XmlUtils util=new XmlUtils();
        util.load(DEFAULTPATH);
     *//*   Mushiny mushiny=new Mushiny();
        List<Module>  modules=new ArrayList<>();
        Module module=new Module();
        module.setName("wcs-stowpodselection-server-v8");
        module.setLogDir("/home/mushiny/logs:/home/log");
        module.setPath("/home/mslab/wms_v8/wcs-stowpodselection-server-v8");
        module.setPort("12004");
        module.setVersion("1.0.0");
        mushiny.setName("牧星wms");
        mushiny.setVersion("1.0.0");
        modules.add(module);
        mushiny.setModules(modules);
        System.out.println(util.java2XML(mushiny));*//*

    }*/
}
