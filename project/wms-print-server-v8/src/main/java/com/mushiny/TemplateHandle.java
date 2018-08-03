package com.mushiny;

import com.mushiny.constant.Template;
import com.mushiny.constant.TemplateType;
import com.mushiny.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2018/2/10.
 */
public class TemplateHandle {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateHandle.class);
    private final Pattern printPattern = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+):(.*)");
    private TemplateFactory templateFactory = new TemplateFactory();
    private Map<TemplateType, Template> templates = new HashMap<>();
    private Map<TemplateType, Paser> parsers = new HashMap<>();
    private Map<String, PrinterClient> printers = new HashMap<>();

    @Scheduled(fixedRate=1000*10)
    public void intPrinters() {

        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        if (services != null && services.length > 0) {
            for (PrintService service : services) {
                Matcher matcher = printPattern.matcher(service.getName());
                if (matcher.matches()) {
                    PrinterClient printerClient = new PrinterClient();
                    printerClient.setIp(matcher.group(1));
                    printerClient.setPrinterName(matcher.group(2));
                    printerClient.setPrintService(service);
                    printers.put(matcher.group(1), printerClient);
                }

            }
        }
        LOGGER.info("查找到的{}个斑马打印机如下{}",printers.size(), JSONUtil.toJSon(printers));
    }

    public void initTemplates() {
        CommonTemplateBuilder commonTemplateBuilder = new CommonTemplateBuilder();
        ShunFengTemplateBuilder ShunFengTemplateBuilder = new ShunFengTemplateBuilder();
        ZhongTongTemplateBuilder zhongTongTemplateBuilder=new ZhongTongTemplateBuilder();
        registerTemplates(TemplateType.TOOLS, commonTemplateBuilder);
        registerTemplates(TemplateType.ZHONG_TONG, zhongTongTemplateBuilder);

        registerTemplates(TemplateType.SHUN_FENG, ShunFengTemplateBuilder);


    }

    public void initParsers() {
        Paser commonTemplateParser = new CommonTemplateParser();
        ZhongTongTemplateParser zhongTongTemplateParser = new ZhongTongTemplateParser();
        ShunFengTemplateParser ShunFengTemplateParser = new ShunFengTemplateParser();
        registerTemplatePaser(TemplateType.TOOLS, commonTemplateParser);

        registerTemplatePaser(TemplateType.ZHONG_TONG, zhongTongTemplateParser);
        registerTemplatePaser(TemplateType.SHUN_FENG, ShunFengTemplateParser);


    }

    public void registerTemplates(TemplateType templateType, TemplateBuilder templateBuilder) {
        templateFactory.setBuilder(templateBuilder);
        this.registerTemplate(templateType, templateFactory.factoryTemplate());
    }


    public void registerTemplate(TemplateType templateType, Template template) {
        templates.put(templateType, template);

    }

    public void registerTemplatePaser(TemplateType templateType, Paser paser) {
        parsers.put(templateType, paser);

    }

    public Template getTemplate(TemplateType templateType) {
        return templates.get(templateType);
    }

    public Paser getPaser(TemplateType templateType) {
        return parsers.get(templateType);
    }

    public PrinterClient getPrinterClient(String ip) {
        return printers.get(ip);
    }
}
