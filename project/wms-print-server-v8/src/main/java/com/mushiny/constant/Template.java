package com.mushiny.constant;


import java.util.HashMap;
import java.util.Map;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2018/2/8.
 */
public class Template {
    private TemplateType templateType;
    private String template = "";
    private Map<String, String> templateDetail = new HashMap<>();

    public Template() {


    }

    public TemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Map<String, String> getTemplateDetail() {
        return templateDetail;
    }

    public void setTemplateDetail(Map<String, String> templateDetail) {
        this.templateDetail.putAll(templateDetail);
    }

    public void addTemplateDetail(String key, String value) {
        templateDetail.put(key, value);
    }
}
