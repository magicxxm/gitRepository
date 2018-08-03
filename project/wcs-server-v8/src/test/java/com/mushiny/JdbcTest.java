package com.mushiny;

import com.mushiny.jdbc.repositories.JdbcRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Tank.li on 2017/6/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WcsApplication.class)
public class JdbcTest {
    @Autowired
    private JdbcRepository jdbcRepository;
    @Test
    @Transactional
    public void testInsert(){
        /*Map user = new LinkedHashMap();
        user.put("id",1111);
        user.put("name","黎庆剑");
        user.put("email","csslisi@163.com");
        jdbcRepository.insertRecord("USER",user);
        Map user2 = new LinkedHashMap();
        user2.put("id",1234);
        user2.put("name","牧星智能");
        user2.put("email","mushiny@mushiny.com");
        jdbcRepository.insertRecord("USER",user2);*/
        List params = new ArrayList();
        params.add(123);
        List list = jdbcRepository.queryBySql("select * from user where id=?",params);

        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            System.out.println(o);
        }
        Map condition = new HashMap();
        condition.put("ID",123);
        int count = jdbcRepository.deleteRecords("USER",condition);
        System.out.println(count);
    }
}
