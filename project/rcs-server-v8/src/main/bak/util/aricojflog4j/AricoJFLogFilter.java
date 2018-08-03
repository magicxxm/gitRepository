/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.util.aricojflog4j;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * AricoJFLevel级别过滤类
 *
 * @author AricoChen <陈庆余 AricoChen@tom.com>
 */
public class AricoJFLogFilter extends Filter {  
     boolean acceptOnMatch = false;
     int levelMin;
     int levelMax;

     public int getLevelMin() {
     return levelMin;
     }

     public void setLevelMin(int levelMin) {
     this.levelMin = levelMin;
     }

     public int getLevelMax() {
     return levelMax;
     }

     public void setLevelMax(int levelMax) {
     this.levelMax = levelMax;
     }

     @Override
     public int decide(LoggingEvent lgEvent) {
     int inputLevel = lgEvent.getLevel().toInt();
     if (inputLevel >= levelMin && inputLevel <= levelMax) {
     return 0;
     }
     return -1;
     }

   
}
