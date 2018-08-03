package com.mushiny;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import javax.print.*;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2018/2/10.
 */
public class PrinterClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrinterClient.class);
    private String ip;
    private String printerName;
    private PrintService printService;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public PrintService getPrintService() {
        return printService;
    }

    public void setPrintService(PrintService printService) {
        this.printService = printService;
    }

    public boolean print(String cmd) {
        boolean result = false;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("开始给打印机" + ip + printerName + "发送打印任务\n" + cmd);
        }

        if (!ObjectUtils.isEmpty(printService)) {
            DocPrintJob job = printService.createPrintJob();
            byte[] by = cmd.getBytes();
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(by, flavor, null);

            try {
                job.print(doc, null);

                result = true;

            } catch (PrintException e) {
                LOGGER.error("打印出错" + e.getMessage(), e);
                result = false;
            }
        }
        return result;

    }
}
