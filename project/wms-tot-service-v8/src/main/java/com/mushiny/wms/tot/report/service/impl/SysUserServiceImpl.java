package com.mushiny.wms.tot.report.service.impl;

import com.mushiny.wms.tot.general.domain.User;
import com.mushiny.wms.tot.report.service.SysUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/8/7.
 */
@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {
    private final Log LOG= LogFactory.getLog(SysUserServiceImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    public SysUserServiceImpl() {
    }

    @Override
    public List<Object> getClient(Map<String,String> userCode) {
        List<Object> result=null;
        String sql="select SYS_CLIENT.ID as clientId,SYS_CLIENT.CLIENT_NO as clientNo,SYS_CLIENT.NAME as clientName from SYS_CLIENT inner join SYS_USER on SYS_USER.CLIENT_ID=SYS_CLIENT.CLIENT_NO and SYS_USER.USERNAME=:userCode";
        result= execute(sql,userCode);
        return result;
    }
    @Override
    public List<Object> getWareHouse(Map<String,String> userCode) {
        List<Object> result=null;
        String sql="SELECT SYS_WAREHOUSE.ID AS wareHouseId,SYS_WAREHOUSE.WAREHOUSE_NO AS wareHouseNo,SYS_WAREHOUSE.NAME AS wareHouseName FROM SYS_WAREHOUSE  INNER JOIN SYS_USER_WAREHOUSE ON SYS_WAREHOUSE.ID= SYS_USER_WAREHOUSE.WAREHOUSE_ID AND SYS_USER_WAREHOUSE.USER_ID IN (SELECT SYS_USER.ID FROM SYS_USER WHERE SYS_USER.USERNAME=:userCode)";
        result= execute(sql,userCode);
        return result;
    }
    private List  execute(String sql, Map<String, String> params)
    {
        Query query= entityManager.createNativeQuery(sql);
        if(!CollectionUtils.isEmpty(params))
        {
            Set<Map.Entry<String,String>> paramsTemp= params.entrySet();
            for(Map.Entry<String,String> temp:paramsTemp)
            {
                query.setParameter(temp.getKey(),temp.getValue());
            }
        }

        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List rows = query.getResultList();


        return rows;
    }
}
