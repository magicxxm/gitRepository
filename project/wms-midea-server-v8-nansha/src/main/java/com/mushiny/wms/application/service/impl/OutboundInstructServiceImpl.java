package com.mushiny.wms.application.service.impl;

import com.mushiny.wms.application.business.common.OutboundInstructBusiness;

import com.mushiny.wms.application.domain.OutboundInstruct;
import com.mushiny.wms.application.domain.WmsInstructOutPosition;
import com.mushiny.wms.application.service.OutboundInstructService;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.common.utils.JSONUtil;
import org.apache.commons.lang3.ObjectUtils;
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
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/6.
 */
@Service
@Transactional
public class OutboundInstructServiceImpl implements OutboundInstructService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OutboundInstructServiceImpl.class);

    private final OutboundInstructBusiness outboundInstructBusiness;

    @Autowired
    private EntityManager entityManager;
     @Autowired
    public OutboundInstructServiceImpl(OutboundInstructBusiness outboundInstructBusiness) {
        this.outboundInstructBusiness = outboundInstructBusiness;
     }


    @Override
    public Integer saveOnboundInstruct(String outboundInstruct) {
         int result=0;
        List<Map> param=JSONUtil.jsonToList(outboundInstruct);

        if(!CollectionUtils.isEmpty(param))
         {

             for(Map temp:param)
             {
                 OutboundInstruct oi= JSONUtil.mapToBean(temp,new  OutboundInstruct(),"ID","MES_ID");
                 result=outboundInstructBusiness.saveOutboundInstruct2(oi);
                 if(LOGGER.isDebugEnabled())
                 {
                     LOGGER.debug("处理入库指令返回{} --{} ",result,JSONUtil.toJSon(oi));
                 }
             }


         }

        return result;
    }

    @Override
    public Page<OutboundInstruct> getByOutboundLabelNo(String startTime, String endTime, String labelNo, Pageable pageable) {
        String hql="SELECT i FROM "+OutboundInstruct.class.getSimpleName()+
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
        Query query=entityManager.createQuery(hql,OutboundInstruct.class);
        Query query2=entityManager.createQuery(hql,OutboundInstruct.class);
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
        List<OutboundInstruct> list = query.getResultList();
        List<OutboundInstruct> list2 = query2.getResultList();
        long count=list2.size();
        return new PageImpl<OutboundInstruct>(list,pageable,count);
    }

    @Override
    public List<WmsInstructOutPosition> getOutboundByLabelNo(String labelNo) {
        String hql="SELECT i FROM "+WmsInstructOutPosition.class.getSimpleName()+
                " i WHERE i.outboundInstruct.LABEL_NO=:labelNo";
        Query query = entityManager.createQuery(hql,WmsInstructOutPosition.class);
        query.setParameter("labelNo",labelNo);
        List<WmsInstructOutPosition> list = query.getResultList();
        return list;
    }
}
