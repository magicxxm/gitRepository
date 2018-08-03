package com.mushiny;


import com.mushiny.constant.Template;
import com.mushiny.constant.TemplateType;
import com.mushiny.exception.TemplateUndefinedException;

import java.util.Map;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2018/2/9.
 */
public class ShunFengTemplateParser extends AbstractTemplatePaser {

    @Override
    public void parserTemplate(Template template, Map<String, String> param) {

        if (template.getTemplateType().equals(TemplateType.SHUN_FENG)) {
            reset();
            String[] keys = new String[param.size()];
            String[] values = new String[param.size()];
            parser(template, param.keySet().toArray(keys), param.values().toArray(values));

            zplContent.append("^CFP,18,10");
            zplContent.append("^FO16,30^GB560,426,3^FS");

            zplContent.append(template.getTemplateDetail().get("motherSingleNumber"));
            zplContent.append("^FO16,135^GB560,0,3^FS");
            zplContent.append("^FO16,216^GB560,0,3^FS");
            zplContent.append("^FO16,297^GB560,0,3^FS");
            zplContent.append("^FO104,135^GB0,162,3^FS");
            setText("母单号 ", 92, 92, 60,
                    60, 30, 1, 1, 24);
            setText("收件人", 24, 167, 60,
                    30, 15, 1, 1, 24);

            setText(template.getTemplateDetail().get("receiver"), 112, 143, 60,
                    30, 15, 1, 1, 24);
            setChar(template.getTemplateDetail().get("receiverPhone"), 216, 143, 18, 10);
            setText(template.getTemplateDetail().get("receiverAddress"), 112, 175, 60,
                    30, 15, 1, 1, 24);
            setText("寄件人", 24, 248, 60,
                    30, 15, 1, 1, 24);
            setText(template.getTemplateDetail().get("sender"), 112, 224, 60,
                    30, 15, 1, 1, 24);
            setChar(template.getTemplateDetail().get("senderPhone"), 216, 224, 18, 10);
            setText(template.getTemplateDetail().get("senderAddress"), 112, 272, 32,
                    25, 15, 1, 1, 24);
            //付款方式
            setText(template.getTemplateDetail().get("payment"), 24, 313, 18,
                    10, 12, 1, 1, 24);
            //计费重量
            setText(template.getTemplateDetail().get("chargedWeight"), 24 + 263, 313, 18,
                    10, 12, 1, 1, 24);
            //月结账号
            setText(template.getTemplateDetail().get("monthlyAccountNumber"), 24, 345, 18,
                    10, 12, 1, 1, 24);
            //实际重量
            setText(template.getTemplateDetail().get("actualWeight"), 24 + 263, 345, 18,
                    10, 12, 1, 1, 24);
            //第三方地区
            setText(template.getTemplateDetail().get("thirdPartyArea"), 24, 377, 18,
                    10, 12, 1, 1, 24);
            //运费
            setText(template.getTemplateDetail().get("freight"), 24 + 263, 377, 18,
                    10, 12, 1, 1, 24);
            //费用合计
            setText(template.getTemplateDetail().get("totalCost"), 24, 409, 18,
                    10, 12, 1, 1, 24);


            template.setTemplate(buildTemplate());

        } else {
            throw new TemplateUndefinedException("没有type=" + param.get("type") + "模板");
        }


    }


}
