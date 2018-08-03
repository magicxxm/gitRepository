package com.mushiny.wms.tot.attendance.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.tot.attendance.crud.dto.AttendanceDTO;
import com.mushiny.wms.tot.attendance.crud.mapper.AttendanceMapper;
import com.mushiny.wms.tot.attendance.domain.Attendance;
import com.mushiny.wms.tot.attendance.service.ClockTimeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/8/1.
 */
@Service
@Transactional
public class ClockTimeServiceImpl implements ClockTimeService {
    private final Log  LOG= LogFactory.getLog(ClockTimeServiceImpl.class);
    private final AttendanceMapper clockMapper;
    private final ApplicationContext applicationContext;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    public ClockTimeServiceImpl(AttendanceMapper mapper,ApplicationContext applicationContext) {
        this.clockMapper = mapper;
        this.applicationContext = applicationContext;
    }
    @Override
    public int addClockTime(Attendance params) {
        int result=0;
        if(!ObjectUtils.isEmpty(params))
        {
            try
            {
                entityManager.persist(params);
                result=1;
            }catch (Exception e)
            {
                result=-1;
                LOG.error(e.getMessage(),e);
            }
        }

        return result;
    }

    @Override
    public int updateClockTime(Attendance params) {
        int result=0;
        if(!ObjectUtils.isEmpty(params))
        {
            try
            {
                entityManager.persist(params);
                result=1;
            }catch (Exception e)
            {
                result=-1;
                LOG.error(e.getMessage(),e);
            }
        }

        return result;
    }

    @Override
    public int deleteClockTime(String id) {
        int result=-1;
        Map<String,String> param=new HashMap<>();
        param.put("keyId",id);
        String sql="delete from Attendance att where att.id=:keyId";
        result=executeUpdate(sql,param);
        return result;
    }

    @Override
    public List<Attendance> getClockTimes(String emCode,String begainTime,String endTime) {
        List<Attendance> result=null;
        String currentWarehouseId = applicationContext.getCurrentWarehouse();
//        String warehouseSql ="SELECT NAME from SYS_WAREHOUSE WHERE ID = :currentWarehouseId";
//        Query query = entityManager.createNativeQuery(warehouseSql);
//        query.setParameter("currentWarehouseId", currentWarehouseId);
//        List<String> warehouseEntity = query.getResultList();
//        String currentWarehouseName = warehouseEntity.get(0);
        String sql="select att from  Attendance att where att.employeeCode=:emCode  and att.clockTime between :begainTime and :endTime" +
                " and att.warehouseId = :warehouse order by att.clockTime asc";
        Map<String,String> param=new HashMap<>();
        param.put("emCode",emCode);
        param.put("begainTime",begainTime);
        param.put("endTime",endTime);
        param.put("warehouse",currentWarehouseId);
        result= execute(sql,param,Attendance.class);
        for (Attendance attendance : result) {
            if (attendance.getClockTime()!=null && attendance.getClockTime().length()>=19)
                attendance.setClockTime(attendance.getClockTime().substring(0,19));
        }
        return result;
    }
    private List  execute(String sql,Map<String, String> params,Class type)
    {
        Query query= entityManager.createQuery(sql,type);
        if(!CollectionUtils.isEmpty(params))
        {
            Set<Map.Entry<String,String>> paramsTemp= params.entrySet();
            for(Map.Entry<String,String> temp:paramsTemp)
            {
                query.setParameter(temp.getKey(),temp.getValue());
            }
        }
        List temp= query.getResultList();

        return temp;
    }
    private int  executeUpdate(String sql,Map<String, String> params)
    {
        int result=-1;
        Query query= entityManager.createQuery(sql);
        if(!CollectionUtils.isEmpty(params))
        {
            Set<Map.Entry<String,String>> paramsTemp= params.entrySet();
            for(Map.Entry<String,String> temp:paramsTemp)
            {
                query.setParameter(temp.getKey(),temp.getValue());
            }
        }
         result=query.executeUpdate();

        return result;
    }
}
