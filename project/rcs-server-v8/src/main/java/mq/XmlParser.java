package mq;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {
    private static final String FILE_SEPARATOR = "/";
    private String configFilePath;
    private String nodesStr;
    private InputStream xmlStream;
    private SAXReader reader;
    private Document document;
    private Element rootElement;
    private String[] nodes;
    private String text;
    private List<Element> resList;
    public XmlParser() {
    }
    private void before(String configFilePath, String nodesStr){
        try {
            this.configFilePath = configFilePath;
            this.nodesStr = nodesStr;
            xmlStream = Object.class.getResourceAsStream(configFilePath);
            reader = new SAXReader();
            document = reader.read(new BufferedReader(new InputStreamReader(xmlStream, "utf-8")));
            rootElement = document.getRootElement();
            nodes = nodesStr.trim().split(FILE_SEPARATOR);
            text = "";
            resList = new ArrayList<Element>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 关闭文件流
     */
    private void after() {
        if (null != xmlStream){
            try {
                xmlStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param configFilePath eg: resource下单config路径下的agv.xml文件路径："/config/agv_test.xml"
     * @param nodesStr 参数为String类型的文件路径和节点路径，从根目录开始 "根节点/子节点/子节点"。eg: "config/conn/user"
     * @param template 模板 编写不同的模板实现节点获取形式
     */
    private void parseConfigParmByPath(String configFilePath, String nodesStr, XmlTemplate template){
        if(null == configFilePath || "".equals(configFilePath.trim())){
            return ;
        }
        if("".equals(nodesStr.trim())){
            return ;
        }
        before(configFilePath, nodesStr);
        template.parseXmlAction();
        after();
    }

    /**
     * 解析特定配置文件的特定节点值
     *
     * @param configFilePath eg: resource下单config路径下的agv.xml文件路径："/config/agv_test.xml"
     * @param nodesStr 参数为String类型的文件路径和节点路径，从根目录开始 "根节点/子节点/子节点"。eg: "config/conn/user"
     * 更改text的属性值
     */
    private void parseConfigParmByPath(String configFilePath, String nodesStr){
        parseConfigParmByPath(configFilePath, nodesStr, new XmlTemplate(){
            @Override
            public void parseXmlAction() {
                for (int i = 1; i < nodes.length ; i++){
                    List<Element> elements = rootElement.elements(nodes[i]);
                    /**
                     * 以该算法获取节点，每个节点只能有一个同名子节点
                     * 即：配置文件xml里兄弟节点不可同名
                     */
                    for (Element ele : elements){
                        rootElement = ele;
                        if(nodes[nodes.length - 1].equals(ele.getName())){
                            text = ele.getText();
                            break;
                        }
                    }
                }
            }
        });
    }

    /**
     * 解析特定配置文件的特定节点值
     *
     * @param configFilePath eg: resource下单config路径下的agv.xml文件路径："/config/agv_test.xml"
     * @param nodesStr 参数为String类型的文件路径和节点路径，从根目录开始 "根节点/子节点/子节点"。eg: "config/conn/user"
     * @return String text返回特定节点
     */
    public String getText(String configFilePath, String nodesStr) {
        parseConfigParmByPath(configFilePath, nodesStr);
        return text;
    }

    /**
     * 解析特定配置文件的指定同名节点的所有值
     *
     * @param configFilePath eg: resource下单config路径下的agv.xml文件路径："/config/agv_test.xml"
     * @param nodesStr 参数为String类型的文件路径和节点路径，从根目录开始 "根节点/子节点/子节点"。eg: "config/conn/user"
     * 更改resList的元素值
     */
    private void parseSameNameNodesByNodesStr(String configFilePath, String nodesStr){
        parseConfigParmByPath(configFilePath, nodesStr, new XmlTemplate(){
            @Override
            public void parseXmlAction() {
                for (int i = 1; i < nodes.length ; i++){
                    List<Element> elements = rootElement.elements(nodes[i]);
                    for (Element ele : elements){
                        rootElement = ele;
                        if(nodes[nodes.length - 1].equals(ele.getName())){
                            resList.add(ele);
                        }
                    }
                }
            }
        });
    }

    /**
     * 解析特定配置文件的特定节点值
     *
     * @param configFilePath eg: resource下单config路径下的agv.xml文件路径："/config/agv_test.xml"
     * @param nodesStr 参数为String类型的文件路径和节点路径，从根目录开始 "根节点/子节点/子节点"。eg: "config/conn/user"
     * @return List<Element> resList返回同名元素的集合
     */
    public List<Element> getResList(String configFilePath, String nodesStr) {
        parseSameNameNodesByNodesStr(configFilePath, nodesStr);
        return resList;
    }
    private class XmlTemplate{
        public void parseXmlAction(){}
    }

}

