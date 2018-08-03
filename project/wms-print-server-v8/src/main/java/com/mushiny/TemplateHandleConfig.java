package com.mushiny;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2018/2/10.
 */
@Configuration
public class TemplateHandleConfig {

    @Bean
    public TemplateHandle createTemplateHandle() {
        TemplateHandle templateHandle = new TemplateHandle();
        templateHandle.initParsers();
        templateHandle.initTemplates();
        templateHandle.intPrinters();
        return templateHandle;

    }

}
