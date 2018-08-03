package com.mushiny;


import com.mushiny.constant.FontStyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2018/2/8.
 */
public abstract class ZplCmandGenage {
    private static byte[] dotFont;

    static {

        File file = new File("D:/printServer/lib/ts24.lib");
        if (file.exists()) {
            FileInputStream fis;
            try {
                fis = new FileInputStream(file);
                dotFont = new byte[fis.available()];
                fis.read(dotFont);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("C://ts24.lib文件不存在");
        }
    }

   // String CONTROLHEAD = "~TA000^LT0^MNW^MTT^PON^PMN^LH0,0^JMA~SD15^LL480^PW632^JUS^LRN^CI0";
   String CONTROLHEAD = "~TA000^LT0^MNW^MTT^PON^PMN^LH0,0^JMA~SD15^LL800^PW1200^JUS^LRN^CI0";
    String BEGINZPL = "^XA";
    String chinessCode = "";
    String ENDZPL = "^XZ";
    StringBuffer zplContent = new StringBuffer();

    public ZplCmandGenage appContent(String content) {
        zplContent.append(content);
        return this;
    }

    public ZplCmandGenage reset() {
        BEGINZPL = "^XA";
        ENDZPL = "^XZ";
        chinessCode = "";
        zplContent.setLength(0);
        return this;
    }

    /**
     * 中文字符、英文字符(包含数字)混合
     *
     * @param str 中文、英文
     * @param x   x坐标
     * @param y   y坐标
     * @param eh  英文字体高度height
     * @param ew  英文字体宽度width
     * @param es  英文字体间距spacing
     * @param mx  中文x轴字体图形放大倍率。范围1-10，默认1
     * @param my  中文y轴字体图形放大倍率。范围1-10，默认1
     * @param ms  中文字体间距。24是个比较合适的值。
     */
  public ZplCmandGenage setText(String str, int x, int y, int eh, int ew, int es, int mx, int my, int ms) {
        byte[] ch = str2bytes(str);
        int xTemp=x;
        int yTemp=y;
        boolean newLine=false;
        for (int off = 0; off < ch.length; ) {
            if(off>64&&!newLine)
            {
                x=xTemp;
                y=yTemp+24;
                newLine=true;

            }
            if (((int) ch[off] & 0x00ff) >= 0xA0) {
                int qcode = ch[off] & 0xff;
                int wcode = ch[off + 1] & 0xff;
                zplContent.append(String.format("^FO%d,%d^XG0000%01X%01X,%d,%d^FS\n", x, y, qcode, wcode, mx, my));
                chinessCode += String.format("~DG0000%02X%02X,00072,003,\n", qcode, wcode);
                qcode = (qcode + 128 - 32) & 0x00ff;
                wcode = (wcode + 128 - 32) & 0x00ff;
                int offset = ((int) qcode - 16) * 94 * 72 + ((int) wcode - 1) * 72;
                for (int j = 0; j < 72; j += 3) {
                    qcode = (int) dotFont[j + offset] & 0x00ff;
                    wcode = (int) dotFont[j + offset + 1] & 0x00ff;
                    int qcode1 = (int) dotFont[j + offset + 2] & 0x00ff;
                    chinessCode += String.format("%02X%02X%02X\n", qcode, wcode, qcode1);
                }
                x = x + ms * mx;
                off = off + 2;
            } else if (((int) ch[off] & 0x00FF) < 0xA0) {
                zplContent.append(setChar(String.format("%c", ch[off]), x, y+4, eh, ew));
                x = x + es;
                off++;
            }
        }
        return this;
    }



    public ZplCmandGenage setText(FontStyle font) {
        return setText(font.getStr(), font.getX(), font.getY(), font.getEh(), font.getEw(), font.getEs(), font.getMx(), font.getMy(), font.getMs());
    }

    /**
     * 英文字符串(包含数字)
     *
     * @param str 英文字符串
     * @param x   x坐标
     * @param y   y坐标
     * @param h   高度 18<=h<=180
     * @param w   宽度 10<=w<=100
     */
    public String setChar(String str, int x, int y, int h, int w) {
       /* content += "^FO" + x + "," + y + "^A0," + h + "," + w + "^FD" + str + "^FS";*/
        String result = "^FO" + x + "," + y + "^ADN," + h + "," + w + "^FD" + str + "^FS";
        //String result = "^FB30,2,L^FO" + x + "," + y + "^ADN," + h + "," + w + "^FD" + str + "^FS";
        zplContent.append(result);
        return result;
    }

    public String setChar(FontStyle font) {
        return setChar(font.getStr(), font.getX(), font.getY(), font.getEh(), font.getEw());
    }

    /**
     * 字符串转byte[]
     *
     * @param s
     * @return
     */
    private byte[] str2bytes(String s) {
        if (null == s || "".equals(s)) {
            return null;
        }
        byte[] abytes = null;
        try {
            abytes = s.getBytes("gb2312");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return abytes;
    }

    public String buildTemplate() {
        StringBuilder sb = new StringBuilder();
        sb.append(BEGINZPL);
        sb.append(CONTROLHEAD);
        sb.append(chinessCode);
        sb.append(zplContent);
        sb.append(ENDZPL);
        return sb.toString();
    }
}
