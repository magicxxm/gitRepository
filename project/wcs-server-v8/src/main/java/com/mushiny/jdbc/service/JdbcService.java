package com.mushiny.jdbc.service;

import com.mushiny.jdbc.domain.IParam;
import com.mushiny.jdbc.domain.InOutParam;
import com.mushiny.jdbc.domain.InParam;
import com.mushiny.jdbc.domain.OutParam;
import com.mushiny.jdbc.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试类 没用
 * Created by Tank.li on 2017/6/13.
 */
@Component
public class JdbcService {
    @Autowired
    private JdbcRepository jdbcRepository;
    @Transactional
    public void deleteUser(){
        /*List params = new ArrayList();
        params.add(111);
        jdbcRepository.queryByKey("selectuser",params);
        Map condition = new HashMap();
        condition.put("id",1);
        jdbcRepository.deleteRecords("USER",condition);*/
        List<IParam> params = new ArrayList();
        InParam ip1 = new InParam();
        ip1.setIndex(1);
        ip1.setValue(1111);
        InParam ip2 = new InParam();
        ip2.setIndex(2);
        ip2.setValue("剑雨浮生");
        InParam ip3 = new InParam();
        ip3.setIndex(3);
        ip3.setValue("csslisi@163.com");
        params.add(ip1);
        params.add(ip2);
        params.add(ip3);
        InOutParam inOutParam = new InOutParam();
        inOutParam.setValue("黎庆剑");
        inOutParam.setIndex(4);
        params.add(inOutParam);
        jdbcRepository.callProc("P_INSERT_RETURN",params);
        for (int i = 0; i < params.size(); i++) {
            IParam iParam = params.get(i);
            if(iParam instanceof OutParam || iParam instanceof InOutParam){
                System.out.println(iParam.getIndex());
                System.out.println(iParam.getValue());
            }
        }
     }
}
