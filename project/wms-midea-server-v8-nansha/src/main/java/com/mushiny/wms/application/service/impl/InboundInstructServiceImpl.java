package com.mushiny.wms.application.service.impl;

import com.mushiny.wms.application.business.common.InboundInstructBusiness;
import com.mushiny.wms.application.domain.InboundInstruct;
import com.mushiny.wms.application.domain.WmsInstructInPosition;
import com.mushiny.wms.application.service.InboundInstructService;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.common.utils.JSONUtil;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Administrator on 2018/7/6.
 */
@Service
@Transactional
public class InboundInstructServiceImpl implements InboundInstructService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InboundInstructServiceImpl.class);

    private final InboundInstructBusiness inboundInstructBusiness;

    @PersistenceContext
    private EntityManager entityManager;
     @Autowired
    public InboundInstructServiceImpl(InboundInstructBusiness inboundInstructBusiness) {
        this.inboundInstructBusiness = inboundInstructBusiness;
    }

    @Override
    public Integer saveInboundInstruct(String inboundInstruct) {
         int result=0;
         List<Map> param=JSONUtil.jsonToList(inboundInstruct);

         for(Map temp:param)
         {
             InboundInstruct iis= JSONUtil.mapToBean(temp, new InboundInstruct(),"ID","MES_ID");

             result =inboundInstructBusiness.saveInboundInstruct2(iis);
             if(LOGGER.isDebugEnabled())
             {
                 LOGGER.debug("处理入库指令返回{} --{} ",result,JSONUtil.toJSon(iis));
             }
         }

        return result ;
    }

    @Override
    public Page<InboundInstruct> getByInboundLabelNo(String startTime, String endTime, String labelNo, Pageable pageable) {
        String hql="SELECT i FROM "+InboundInstruct.class.getSimpleName()+
                " i WHERE 1=1";
        if(startTime!=null && !ObjectUtils.equals("",startTime)){
            hql= hql+" AND i.createdDate>:startDate";
        }
        if(endTime!=null && !ObjectUtils.equals("",endTime)){
            hql= hql+" AND i.modifiedDate<:endDate";
        }
        if(labelNo!=null && !ObjectUtils.equals("",labelNo) ){
            hql= hql+" AND i.LABEL_NO = :labelNo";
        }
        Query query=entityManager.createQuery(hql,InboundInstruct.class);
        Query query2=entityManager.createQuery(hql,InboundInstruct.class);
        if(startTime!=null && !ObjectUtils.equals("",startTime)){
            LocalDateTime start= DateTimeUtil.getLocalDatetime(startTime+" 00:00:00");
            query.setParameter("startDate",start);
            query2.setParameter("startDate",start);
        }
        if(endTime!=null && !ObjectUtils.equals("",endTime)){
            LocalDateTime end= DateTimeUtil.getLocalDatetime(endTime+" 23:59:59");
            query.setParameter("endDate",end);
            query2.setParameter("endDate",end);
        }
        if(labelNo!=null && !ObjectUtils.equals("",labelNo)){
            query.setParameter("labelNo",labelNo);
            query2.setParameter("labelNo",labelNo);
        }
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List<InboundInstruct> list = query.getResultList();
        List<InboundInstruct> list2 = query2.getResultList();
        long count=list2.size();
        return new PageImpl<InboundInstruct>(list,pageable,count);
    }

    @Override
    public List<WmsInstructInPosition> getInboundByLabelNo(String labelNo) {
        String hql="SELECT i FROM "+WmsInstructInPosition.class.getSimpleName()+
                " i WHERE i.inboundInstruct.LABEL_NO=:labelNo";
        Query query = entityManager.createQuery(hql,WmsInstructInPosition.class);
        query.setParameter("labelNo",labelNo);
        List<WmsInstructInPosition> list = query.getResultList();
        return list;
    }
}
