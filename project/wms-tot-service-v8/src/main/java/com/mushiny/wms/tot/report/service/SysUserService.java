package com.mushiny.wms.tot.report.service;

import com.mushiny.wms.tot.general.domain.User;

import java.util.List;
import java.util.Map;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/8/7.
 */
public interface SysUserService {
     List<Object> getWareHouse(Map<String,String> userCode);
     List<Object> getClient(Map<String,String> userCode);

}
