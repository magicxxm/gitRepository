package com.mushiny.wms.test.web;

import org.junit.*;
import redis.clients.jedis.Jedis;

/**
 * Created by Laptop-9 on 2018/8/15.
 */
public class TestRedis {
    private Jedis jedis;

    @Before
    public void setJedis() {
        //连接redis服务器(在这里是连接本地的)
        jedis = new Jedis("127.0.0.1", 6379);
        //权限认证
        //jedis.auth("123456");
        System.out.println("连接服务成功");
    }
    /**
     * Redis操作字符串
     */
    @org.junit.Test
    public void testString() {
        //添加数据
        jedis.set("name", "小学生"); //key为name放入value值为chx
        System.out.println("name:" + jedis.get("name"));//读取key为name的值
    }
}
