package com.mushiny;

import com.mushiny.constant.Template;

import java.util.Map;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2018/2/10.
 */
public interface Paser {
    void parserTemplate(Template template, Map<String, String> param);
}
