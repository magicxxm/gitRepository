/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.map;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class ScrollablePanel extends JPanel implements Scrollable{
    private int maxUnitIncrement = 10;//单位像素
     public ScrollablePanel() {
        super();
        setOpaque(true);
        //Let the user scroll by dragging to outside the window.
        setAutoscrolls(true); //enable synthetic drag events
    }

    //返回视图组件视口的首选大小
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    /*
     显示逻辑行或列的组件应计算滚动增量，它将根据方向的值完全公开一个新的行或列。
     理想情况下，组件应通过返回需要的距离来处理一个部分公开的行或列，以便完全公开此项。
     每当用户请求一个单位的滚动时，类似 JScrollPane 的滚动容器将使用此方法。
     参数：
     visibleRect - 视口内可见的视图区域
     orientation - SwingConstants.VERTICAL 或 SwingConstants.HORIZONTAL。
     direction - 小于 0 为向上/左滚动，大于 0 为向下/右滚动。 
     返回：
     沿指定方向滚动的“单位”增量。此值应该永远为正数。
     */
    public int getScrollableUnitIncrement(
            Rectangle visibleRect,
            int orientation,
            int direction) {
        //Get the current position.
        int currentPosition = 0;
        if (orientation == SwingConstants.HORIZONTAL) {
            currentPosition = visibleRect.x;
        } else {
            currentPosition = visibleRect.y;
        }
        if (direction < 0) {
            int newPosition = currentPosition
                    - (currentPosition / getMaxUnitIncrement())
                    * getMaxUnitIncrement();
            return (newPosition == 0) ? getMaxUnitIncrement() : newPosition;
        } else {
            return ((currentPosition / getMaxUnitIncrement()) + 1)
                    * getMaxUnitIncrement()
                    - currentPosition;
        }
    }
    /*
     显示逻辑行或列的组件应计算滚动增量，它将根据方向的值完全公开一个行块或列块。
     每当用户请求一个块的滚动时，类似 JScrollPane 的滚动容器将使用此方法。
     参数：
     visibleRect - 视口内可见的视图区域
     orientation - SwingConstants.VERTICAL 或 SwingConstants.HORIZONTAL。
     direction - 小于 0 为向上/左滚动，大于 0 为向下/右滚动。 
     返回：
     沿指定方向滚动的“块”增量。此值应该永远为正数。
     */

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width - getMaxUnitIncrement();
        } else {
            return visibleRect.height - getMaxUnitIncrement();
        }
    }
    /*
     如果视口总是强制此 Scrollable 的宽度与视口宽度匹配，则返回 true。
     例如，支持换行的正常文本显示在这里将返回 true，因为不希望让换行内容超出视口右边界而无法显示。注意，祖先为 JScrollPane 的 Scrollable 返回 true 可有效地禁用水平滚动。
     类似 JViewport 的滚动容器在每次进行验证时都使用此方法。
     返回：
     如果视口强制 Scrollable 宽度与其自身宽度匹配，则返回 True。
     */

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }
    /*   
     如果视口总是强制此 Scrollable 的高度与视口高度匹配，则返回 true。
     例如，纵行文本视图（按列从左向右流入文本）通过在此处返回 true 可有效地禁用垂直滚动。
     类似 JViewport 的滚动容器在每次进行验证时都使用此方法。
     返回：
     如果视口强制 Scrollable 高度与其自身高度匹配，则返回 True。
     */

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
     /**
     * @return the maxUnitIncrement
     */
    public int getMaxUnitIncrement() {
        return maxUnitIncrement;
    }

    /**
     * @param maxUnitIncrement the maxUnitIncrement to set
     */
    public void setMaxUnitIncrement(int maxUnitIncrement) {
        this.maxUnitIncrement = maxUnitIncrement;
    }

}
