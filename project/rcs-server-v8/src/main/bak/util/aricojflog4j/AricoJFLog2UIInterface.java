/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.util.aricojflog4j;

/**
 *此接口是log4j所输出日志到UI所必须实现的，
 * 如果用户想打印日志到UI,则所在类必须实现此接口。
 * @author AricoChen <陈庆余 AricoChen@tom.com>
 */
public interface AricoJFLog2UIInterface {
    public void setLoggerMsg(String msg,String logType);
}
