package com.mushiny.service.impl;

import com.mushiny.model.SuningOutBound;
import com.mushiny.model.SuningOutboundPosition;
import com.mushiny.service.OutBoundService;
import com.mushiny.web.dto.CustomerShipmentDTO;
import com.mushiny.web.dto.CustomerShipmentPositionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by 123 on 2018/5/2.
 */
@Service
@Transactional
public class OutBoundServiceImpl implements OutBoundService {
    private final Logger logger = LoggerFactory.getLogger(OutBoundServiceImpl.class);

    private final EntityManager manager;

    @Autowired
    public OutBoundServiceImpl(EntityManager manager){
        this.manager = manager;
    }

    @Override
    public void createOutbound(CustomerShipmentDTO dto) {

        logger.info("开始创建OutBound 数据。。。");

        SuningOutBound outBound = new SuningOutBound();
        outBound.setId(UUID.randomUUID().toString());
        outBound.setCreateTime(LocalDateTime.now());
        outBound.setDeliveryTime(dto.getDeliveryDate());
        outBound.setOrderCode(dto.getOrderNo());
        outBound.setZtbpri(dto.getZtbpri());

        manager.persist(outBound);

        for (CustomerShipmentPositionDTO d:dto.getPositions()) {
            SuningOutboundPosition position = new SuningOutboundPosition();
            position.setId(UUID.randomUUID().toString());
            position.setAmount(String.valueOf(d.getAmount()));
            position.setClientNo(d.getClientNo());
            position.setItemNo(d.getItemNo());
            position.setLineNo(d.getItem());
            position.setLot(d.getEndDate());
            position.setOrderCode(dto.getOrderNo());
            position.setStockState(d.getStockState());
            manager.persist(position);
        }

    }
}
