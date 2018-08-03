package com.mushiny;


import com.mushiny.constant.Template;
import com.mushiny.constant.TemplateType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2018/2/9.
 */
public class ShunFengTemplateBuilder implements TemplateBuilder {

    @Override
    public Template factoryTemplate() {

//70 38^B3N,N,50,Y,N,N
        //~TA000^LT0^MNW^MTT^PON^PMN^LH0,0^JMA^PR2,2~SD15^JUS^LRN^CI0
        /*p.content="~TA000^LT0^MNW^MTT^PON^PMN^LH0,0^JMA^PR2,2~SD15^JUS^LRN^CI0"+*/
        // TA调整撕纸位置/向上或向下移动/
    /*            p.content=CONTROLHEAD+

                "~DG000.GRF," +result.length+str
               +
               "^FO56,135^XG000.GRF,1,1^FS"+
          "^CFP,18,10"+
                "^FO16,30^GB560,426,3^FS" +
                "^FO56,38^B3N,N,50,Y,N,N^FD123456789^FS";
        p.setText("母单号 ", 92, 92, 60,
                60, 30, 1, 1, 24);

        p.content+="^FO16,135^GB560,0,3^FS";
        p.content+="^FO16,216^GB560,0,3^FS";
        p.content+="^FO16,297^GB560,0,3^FS";
       // p.content+="^FO16,432^GB560,0,3^FS";
        p.content+="^FO104,135^GB0,162,3^FS";
       // p.content+="^FO208,432^GB0,64,3^FS";
       // p.content+="^FO396,432^GB0,64,3^FS";

        p.setText("收件人", 24, 167, 60,
                30, 15, 1, 1, 24);
        p.setText("熊大", 112, 143, 60,
                30, 15, 1, 1, 24);
        p.setChar("15798782345", 216, 143, 18, 10);
        p.setText("海南省海口市美兰区海秀东路望海国际", 112, 175, 60,
                30, 15, 1, 1, 24);
        p.setText("寄件人", 24, 248, 60,
                30, 15, 1, 1, 24);
        p.setText("光头强", 112, 224, 60,
                30, 15, 1, 1, 24);
        p.setChar("15998782375", 216, 224, 18, 10);
        p.setText("陕西省西安市雁塔区科技二路光电园", 112, 272, 32,
                25, 15, 1, 1, 24);
        p.setText("付款方式: 到付        计费重量:1.000", 24, 313, 18,
                10, 12, 1, 1, 24);
        p.setText("月结账号:             实际重量:1.000", 24, 345, 18,
                10, 12, 1, 1, 24);
        p.setText("第三方地区:           运费:23.00", 24, 377, 18,
                10, 12, 1, 1, 24);
        p.setText("费用合计:23.00", 24, 409, 18,
                10, 12, 1, 1, 24);
        String zpl2 = p.getZpl();

       String linebarCode="^XA^FO50,20^GB400,0,4,^FS" +
               "^FO20,70" +
               "^B8N,100,Y,N" +
               "^FD1234567^FS" +
               "^XZ";
        String barCode="^XA" +
                "^FO20,70" +
                "^B8N,100,Y,N" +
                "^FD1234567^FS" +
                "^XZ";
                 ^B3N,N,50,Y,N,N
                */


        String motherSingleNumber = "^FO56,38^BY2^B3N,N,50,Y,N,N^FD{motherSingleNumber}^FS";
        String sender = "{sender}";
        String senderPhone = "{senderPhone}";
        String senderAddress = "{senderAddress}";
        String receiver = "{receiver}";
        String receiverPhone = "{receiverPhone}";
        String receiverAddress = "{receiverAddress}";
        String payment = "付款方式:{payment}";
        String chargedWeight = "计费重量:{chargedWeight}";
        String monthlyAccountNumber = "月结账号:{monthlyAccountNumber}";
        String actualWeight = "实际重量:{actualWeight}";
        String thirdPartyArea = "第三方地区:{thirdPartyArea}";
        String freight = "运费:{freight}";
        String totalCost = "费用合计:{totalCost}";
        Map<String, String> templateDetails = new HashMap<>();
        templateDetails.put("motherSingleNumber", motherSingleNumber);
        templateDetails.put("sender", sender);
        templateDetails.put("senderPhone", senderPhone);
        templateDetails.put("senderAddress", senderAddress);
        templateDetails.put("receiver", receiver);
        templateDetails.put("receiverPhone", receiverPhone);
        templateDetails.put("receiverAddress", receiverAddress);
        templateDetails.put("payment", payment);
        templateDetails.put("chargedWeight", chargedWeight);
        templateDetails.put("monthlyAccountNumber", monthlyAccountNumber);
        templateDetails.put("actualWeight", actualWeight);
        templateDetails.put("thirdPartyArea", thirdPartyArea);
        templateDetails.put("freight", freight);
        templateDetails.put("totalCost", totalCost);
        Template tm = new Template();
        tm.setTemplateDetail(templateDetails);
        tm.setTemplateType(TemplateType.SHUN_FENG);
        return tm;
    }
}
