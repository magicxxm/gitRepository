
package com.mushiny.wms.application.redis;

import com.mushiny.wms.application.domain.WmsWarehousePosition;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


/**
 * Created by Administrator on 2018/3/20.
 */

@Component
public class RedisUtil {


    @Autowired
    private RedisTemplate redisTemplate;
    public   void put(String route,String key,Object data)
    {

        redisTemplate.opsForHash().put(route,key,data);

    }
    public  Object get(String route,String key)
    {
        Object result=redisTemplate.opsForHash().get(route,key);
        return result;

    }

}

