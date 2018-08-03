/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class ConfigParser
{

    private static final String FILE_SEPARATOR = "/";

    private static String value = "";

    /**
     * 解析特定配置文件的特定节点值
     * 参数为String类型的文件路径和节点路径，从根目录开始 "根节点/子节点/子节点"
     * ep: "config/conn/user"
     * @return
     */
    public static String parseConfigParmByPath(String configFilePath, String nodes)
    {
        value = "";
        FileInputStream fs = null;
        File file = new File(configFilePath);
        if (file.exists())
        {
            file = null;
            try {
                fs = new FileInputStream(configFilePath);
                SAXReader reader = new SAXReader();
                org.dom4j.Document document = reader.read(fs);
                org.dom4j.Element element = document.getRootElement();
                String[] nod = nodes.trim().split(FILE_SEPARATOR);
                for (int i = 1; i < nod.length ; i++)
                {
                    List<Element> elements = element.elements(nod[i]);
                    /**
                     * 以该算法获取节点，每个节点只能有一个同名子节点
                     * 即：配置文件xml里兄弟节点不可同名
                     */
                    for (Element ele : elements)
                    {
                        element = ele;
                        value = ele.getText();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                closeConn(fs);
            }
        }
        return null == value ? "" : value;
    }

    /**
     * 关闭文件流
     * @param fs
     */
    public static void closeConn(InputStream fs) {
        if (null != fs)
        {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
