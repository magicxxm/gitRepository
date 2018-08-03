/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.pathviewtool;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * 路径区域显示面板
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class PathView extends JPanel {

    private int DEFAULT_WIDTH = 750;
    private int DEFAULT_HEIGHT = 500;
    private FontMetrics fm;

    public PathView() {
        super();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        setFm(g2.getFontMetrics());
    }

    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Dimension getMaximumSize() {
        return new Dimension(DEFAULT_WIDTH* 10, DEFAULT_HEIGHT * 10);
    }

    public Dimension getMinimumSize() {
        return new Dimension((int)(DEFAULT_WIDTH*0.1), (int)(DEFAULT_HEIGHT*0.1));
    }

    // 界面组件校验和重画窗口
    protected void updateView() {
        validate();
        repaint();
    }

    /**
     * @return the fm
     */
    public FontMetrics getFm() {
        return fm;
    }

    /**
     * @param fm the fm to set
     */
    public void setFm(FontMetrics fm) {
        this.fm = fm;
    }

}
