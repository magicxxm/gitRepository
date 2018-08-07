package com.mushiny.service.impl;

import com.mushiny.common.crud.AccessDTO;
import com.mushiny.service.RebackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Created by 123 on 2018/3/15.
 */
@Service
@Transactional
public class RebackServiceImpl implements RebackService {

    private final Logger logger = LoggerFactory.getLogger(RebackServiceImpl.class);

    private final EntityManager manager;

    @Autowired
    public RebackServiceImpl(EntityManager manager){
        this.manager = manager;
    }


    @Override
    public AccessDTO updateCustomer(String orderNo) {
        AccessDTO accessDTO = new AccessDTO();
        Query query = manager.createNativeQuery("UPDATE SUNING_ZRFC_AGV_OUTBOUND SET SUNING_ZRFC_AGV_OUTBOUND.NEED_RESPONSE = 1 " +
                "WHERE SUNING_ZRFC_AGV_OUTBOUND.OUT_ORDER_CODE = '"+orderNo+"'");
        int amount = query.executeUpdate();

        if(amount <= 0){
            accessDTO.setMsg("出库单：" + orderNo +" 重新反馈失败");
        }
        return accessDTO;
    }

    @Override
    public AccessDTO updateInbound(String orderNo) {
        AccessDTO accessDTO = new AccessDTO();
        Query query = manager.createNativeQuery("UPDATE SUNING_ZRFC_AGV_INBOUND SET SUNING_ZRFC_AGV_INBOUND.NEED_RESPONSE = 1 " +
                "WHERE SUNING_ZRFC_AGV_INBOUND.RECEIPT_CODE = '"+orderNo+"'");
        int amount = query.executeUpdate();

        if(amount <= 0){
            accessDTO.setMsg("上架单：" + orderNo +" 重新反馈失败");
        }
        return accessDTO;
    }
}
