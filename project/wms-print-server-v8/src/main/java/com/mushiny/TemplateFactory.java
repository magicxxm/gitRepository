package com.mushiny;

import com.mushiny.constant.Template;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2018/2/9.
 */
public class TemplateFactory {
    private TemplateBuilder builder;

    public TemplateFactory() {

    }

    public TemplateBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(TemplateBuilder builder) {
        this.builder = builder;
    }

    public Template factoryTemplate() {
        return builder.factoryTemplate();
    }

}
