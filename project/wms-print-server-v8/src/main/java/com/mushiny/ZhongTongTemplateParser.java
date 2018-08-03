package com.mushiny;


import com.mushiny.constant.Template;
import com.mushiny.constant.TemplateType;
import com.mushiny.exception.TemplateUndefinedException;

import java.util.Map;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2018/2/9.
 */
public class ZhongTongTemplateParser extends AbstractTemplatePaser {

    @Override
    public void parserTemplate(Template template, Map<String, String> param) {

        if (template.getTemplateType().equals(TemplateType.ZHONG_TONG)) {
            reset();
            String[] keys = new String[param.size()];
            String[] values = new String[param.size()];
            parser(template, param.keySet().toArray(keys), param.values().toArray(values));
            int x=24;
            int y=30;
            int y2=636;
            int width=800;
            int numHe=18;
            int numWidth=10;
            int numSpace=12;
           // zplContent.append("^CFP,18,10");
            zplContent.append("^FO"+x+","+(y+56)+"^GB"+width+",0,3^FS");
            zplContent.append("^FO"+x+","+(y+168)+"^GB"+width+",0,3^FS");
            zplContent.append("^FO"+x+","+(y+320)+"^GB"+width+",0,3^FS");
            zplContent.append("^FO"+(x+464)+","+(y+56)+"^GB0,112,3^FS");
            zplContent.append("^FO"+x+","+(y+56+y2)+"^GB"+width+",0,3^FS");
            zplContent.append("^FO"+x+","+(y+168+y2)+"^GB"+width+",0,3^FS");
            zplContent.append("^FO"+x+","+(y+320+y2)+"^GB"+width+",0,3^FS");
            zplContent.append("^FO"+(x+464)+","+(y+56+y2)+"^GB0,112,3^FS");

            String shipMentId = "^FO"+(x+520)+","+(y+56)+",0^BQ,2,4 ^FDD03048F,LM,N"+template.getTemplateDetail().get("shipMentId")+"^FS"
                    + "^FO"+(x+40)+","+(y+96)+"^ADN,18,10^FD"+template.getTemplateDetail().get("shipMentId")+"^FS";
            zplContent.append(shipMentId);
            zplContent.append(template.getTemplateDetail().get("waybillNumber"));
            zplContent.append("^FO"+(x+90)+","+(y+456)+"^BY2^B3N,N,50,Y,N,N^FD"+template.getTemplateDetail().get("waybillNumber")+"^FS");

            setText(template.getTemplateDetail().get("sender"), x+8, (y+168+8), numHe,
                    numWidth, numSpace, 1, 1, 24);
            setText(template.getTemplateDetail().get("senderPhone"), x+240, (y+168+8), numHe,
                    numWidth, numSpace, 1, 1, 24);
            setText(template.getTemplateDetail().get("senderAddress"), x+8, (y+168+56), numHe,
                    numWidth, numSpace, 1, 1, 24);
            setText(template.getTemplateDetail().get("sendDate"), x+8, (y+168+96), numHe,
                    numWidth, numSpace, 1, 1, 24);
            setText(template.getTemplateDetail().get("receiver"), x+8, (y+320+8), numHe,
                    numWidth, numSpace, 1, 1, 24);

            setText(template.getTemplateDetail().get("receiverPhone"), x+240, (y+320+8), numHe,
                    numWidth, numSpace, 1, 1, 24);
            setText(template.getTemplateDetail().get("receiverAddress"), x+8, (y+320+56), numHe, numWidth, numSpace, 1, 1, 24);
            setText(template.getTemplateDetail().get("commodityName"), x+8, (y+320+96), numHe, numWidth, numSpace, 1, 1, 24);
            setText(template.getTemplateDetail().get("deliveryNetwork"), 360+x, y+16, numHe,
                    numWidth, numSpace, 1, 1, 24);


            String shipMentId2 = "^FO"+(x+520)+","+(y+56+y2)+",0^BQ,2,4 ^FDD03048F,LM,N"+template.getTemplateDetail().get("shipMentId")+"^FS"
                    + "^FO"+(x+40)+","+(y+96+y2)+"^ADN,18,10^FD"+template.getTemplateDetail().get("shipMentId")+"^FS";
            zplContent.append(shipMentId2);
            zplContent.append(template.getTemplateDetail().get("waybillNumber"));
            zplContent.append("^FO"+(x+90)+","+(y+456+y2)+"^BY2^B3N,N,50,Y,N,N^FD"+template.getTemplateDetail().get("waybillNumber")+"^FS");

            setText(template.getTemplateDetail().get("sender"), x+8, (y+168+8+y2), numHe,
                    numWidth, numSpace, 1, 1, 24);
            setText(template.getTemplateDetail().get("senderPhone"), x+240, (y+168+8+y2), numHe,
                    numWidth, numSpace, 1, 1, 24);
            setText(template.getTemplateDetail().get("senderAddress"), x+8, (y+168+56+y2), numHe,
                    numWidth, numSpace, 1, 1, 24);
            setText(template.getTemplateDetail().get("sendDate"), x+8, (y+168+96+y2), numHe,
                    numWidth, numSpace, 1, 1, 24);
            setText(template.getTemplateDetail().get("receiver"), x+8, (y+320+8+y2), numHe,
                    numWidth, numSpace, 1, 1, 24);

            setText(template.getTemplateDetail().get("receiverPhone"), x+240, (y+320+8+y2), numHe,
                    numWidth, numSpace, 1, 1, 24);
            setText(template.getTemplateDetail().get("receiverAddress"), x+8, (y+320+56+y2), numHe, numWidth, numSpace, 1, 1, 24);
            setText(template.getTemplateDetail().get("commodityName"), x+8, (y+320+96+y2), numHe, numWidth, numSpace, 1, 1, 24);
           /* setText(template.getTemplateDetail().get("deliveryNetwork"), 360+x, y+16+y2, numHe,
                    numWidth, numSpace, 1, 1, 24);*/



            template.setTemplate(buildTemplate());

        } else {
            throw new TemplateUndefinedException("没有type=" + param.get("type") + "模板");
        }


    }


}
