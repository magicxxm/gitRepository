package com.mushiny;

import com.mushiny.constant.Template;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2018/2/10.
 */
public abstract class AbstractTemplatePaser extends ZplCmandGenage implements Paser {
    public void parser(Template template, String[] keys, String[] values) {
        int keysLen = keys.length;
        int valueLen = values.length;

        for (int k = 0; k < keysLen; k++) {
            Map<String, String> details = template.getTemplateDetail();
            String value = details.get(keys[k]);
            if (!StringUtils.isEmpty(value)) {

                details.put(keys[k], value.replaceAll("\\{" + keys[k] + "\\}", values[k]));
            }

        }


    }

}
