package com.mushiny.constant;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2018/2/8.
 * /**
 * 中文字符、英文字符(包含数字)混合
 * str 中文、英文
 * x x坐标
 * y y坐标
 * eh 英文字体高度height
 * ew 英文字体宽度width
 * es 英文字体间距spacing
 * mx 中文x轴字体图形放大倍率。范围1-10，默认1
 * my 中文y轴字体图形放大倍率。范围1-10，默认1
 * ms 中文字体间距。24是个比较合适的值。
 */
public class FontStyle {
    private int x;
    private int y;
    private int eh = 18;
    private int ew = 10;
    private int es = 12;
    private int mx = 1;
    private int my = 1;
    private int ms = 24;
    private String str;

    public int getMy() {
        return my;
    }

    public void setMy(int my) {
        this.my = my;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getEh() {
        return eh;
    }

    public void setEh(int eh) {
        this.eh = eh;
    }

    public int getEw() {
        return ew;
    }

    public void setEw(int ew) {
        this.ew = ew;
    }

    public int getEs() {
        return es;
    }

    public void setEs(int es) {
        this.es = es;
    }

    public int getMx() {
        return mx;
    }

    public void setMx(int mx) {
        this.mx = mx;
    }

    public int getMs() {
        return ms;
    }

    public void setMs(int ms) {
        this.ms = ms;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
