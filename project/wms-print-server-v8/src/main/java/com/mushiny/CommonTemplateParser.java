package com.mushiny;


import com.mushiny.constant.FontStyle;
import com.mushiny.constant.Template;
import com.mushiny.constant.TemplateType;
import com.mushiny.exception.TemplateUndefinedException;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2018/2/9.
 */
public class CommonTemplateParser extends AbstractTemplatePaser {

    @Override
    public void parserTemplate(Template template, Map<String, String> param) {

        if (template.getTemplateType().equals(TemplateType.TOOLS)) {
            reset();
            String[] keys = new String[param.size()];
            String[] values = new String[param.size()];
            parser(template, param.keySet().toArray(keys), param.values().toArray(values));
            zplContent.append(template.getTemplateDetail().get("goodsItemNo"));
            String descript = template.getTemplateDetail().get("goodsDescript");

            int descriptX = 8;
            int descriptY = 134;
            int len = descript.length();
            int start = 0;

            int end = 0;
            int temp = 0;
            int maxHunagLength = 30;
            int hang = 0;
            for (int k = 0; k < len; k++) {
                temp += 2;

                if (temp > maxHunagLength || k + 1 >= len) {
                    FontStyle f1 = new FontStyle();
                    f1.setX(descriptX);
                    f1.setY(descriptY + 32 * hang);
                    if (k + 1 >= len) {
                        end = len;
                    } else {
                        end = k - 1;
                    }
                    //行号加一
                    hang += 1;
                    temp = 0;
                    f1.setStr(descript.substring(start, end).trim());
                    start = end;
                    if (!StringUtils.isEmpty(f1.getStr())) {
                        setText(f1);
                    }
                }
            }
            template.setTemplate(buildTemplate());

        } else {
            throw new TemplateUndefinedException("没有type=" + param.get("type") + "模板");
        }


    }


}
