package com.mushiny.wms.tot.report.bussiness;

import com.mushiny.wms.tot.report.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/8/7.
 */
@Component
public class UserBussness {
    @Autowired
    private SysUserService service;
    public Object getWareHouseAndClient(String param)
    {
        Map<String,String> params=new HashMap<>(1);
        params.put("userCode",param);
        Map<Object,Object> result=new HashMap<>();
        result.put("wareHouse",service.getWareHouse(params));
        result.put("client",service.getClient(params));
        return result;
    }
}
