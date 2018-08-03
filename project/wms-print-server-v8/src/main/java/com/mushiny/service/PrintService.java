package com.mushiny.service;

import com.mushiny.Paser;
import com.mushiny.PrinterClient;
import com.mushiny.TemplateHandle;
import com.mushiny.constant.Template;
import com.mushiny.constant.TemplateType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2018/2/10.
 */
@Service
public class PrintService {
    @Autowired
    private TemplateHandle templateHandle;

    public void print(Map<String, Object> data) {
        String ip = (String) data.get("ip");
        TemplateType templateType = TemplateType.forTypeName((String) data.get("type"));
        Paser paser = templateHandle.getPaser(templateType);
        Template template = templateHandle.getTemplate(templateType);
        PrinterClient printerClient = templateHandle.getPrinterClient(ip);
        List<Map<String, String>> details = (List<Map<String, String>>) data.get("data");
        for (Map<String, String> temp : details) {
            Template tp = new Template();
            BeanUtils.copyProperties(template, tp);

            paser.parserTemplate(tp, temp);
            boolean result = printerClient.print(tp.getTemplate());

        }


    }


}
