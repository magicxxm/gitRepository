package com.mushiny.zookeeper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Tank.li on 2017/6/11.
 */
@Component
public class ZookeeperConfig {
    //@Value("${spring.zookeeper.url}")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
