package com.mushiny.wms.application.service.impl;

import com.mushiny.wms.application.business.common.RfidBusiness;
import com.mushiny.wms.application.service.RfidService;
import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;

import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Created by Administrator on 2018/7/6.
 */
@Service
@Transactional
public class RfidServiceImpl implements RfidService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RfidServiceImpl.class);

    private final RfidBusiness rfidBusiness;
    @Autowired
    public RfidServiceImpl(RfidBusiness rfidBusiness) {
        this.rfidBusiness = rfidBusiness;
    }

    public Integer saveRfidInfo(String pods)
    {
        Integer result=0;
        Map pod= JSONUtil.jsonToMap(pods);
        if(!CollectionUtils.isEmpty(pod))
        {
            result=rfidBusiness.saveRfidInfo(pod);

        }

        return result;

    }

    /**
     * 按工作站呼叫pod
     * @param stationNameJson
     * @return
     */
    public Integer saveRfidInfoByStationName(String stationNameJson){
        Map stationName= JSONUtil.jsonToMap(stationNameJson);
        if(!CollectionUtils.isEmpty(stationName)){
            rfidBusiness.workStationCall((String)stationName.get("stationName"), NumberUtils.parseNumber((String)stationName.get("isCall"),Integer.class));

        }
        return Constant.SUCCESS_FLAG;

    }



}
