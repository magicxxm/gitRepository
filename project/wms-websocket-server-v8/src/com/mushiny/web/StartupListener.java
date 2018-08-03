package com.mushiny.web;
/**
 * Created by Tank.li on 2017/6/16.
 */

import com.mushiny.Application;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
       // Application.main(new String[]{});
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
