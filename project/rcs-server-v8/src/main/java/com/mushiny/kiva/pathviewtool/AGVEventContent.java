/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.pathviewtool;

import java.awt.Color;
import java.awt.Font;

/**
 * AGV事件内容 实体类
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class AGVEventContent {

    private String eventContent;
    private Color contentColor;
    private Font contentFont;

    public AGVEventContent() {
        contentColor = Color.WHITE;
        contentFont = new Font("宋体", 18, 5);
    }

    public AGVEventContent(String eventContent, Color contentColor, Font contentFont) {
        this.eventContent = eventContent;
        this.contentColor = contentColor;
        this.contentFont = contentFont;
    }
    public AGVEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    /**
     * @return the eventContent
     */
    public String getEventContent() {
        return eventContent;
    }

    /**
     * @param eventContent the eventContent to set
     */
    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    /**
     * @return the contentColor
     */
    public Color getContentColor() {
        return contentColor;
    }

    /**
     * @param contentColor the contentColor to set
     */
    public void setContentColor(Color contentColor) {
        this.contentColor = contentColor;
    }

    /**
     * @return the contentFont
     */
    public Font getContentFont() {
        return contentFont;
    }

    /**
     * @param contentFont the contentFont to set
     */
    public void setContentFont(Font contentFont) {
        this.contentFont = contentFont;
    }
    
}
