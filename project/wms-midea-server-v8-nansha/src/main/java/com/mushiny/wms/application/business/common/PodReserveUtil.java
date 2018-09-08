package com.mushiny.wms.application.business.common;

import com.mushiny.wms.application.domain.Pod;
import com.mushiny.wms.application.domain.enums.PodStateEnum;
import com.mushiny.wms.application.repository.PodRepository;
import com.mushiny.wms.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2018/7/7.
 */
@Component
@Transactional
public class PodReserveUtil   {
    private static final Logger LOGGER = LoggerFactory.getLogger(PodReserveUtil.class);
    private final PodRepository podRepository;
    @Autowired
    public PodReserveUtil(PodRepository podRepository) {
        this.podRepository = podRepository;
    }


    public   synchronized boolean  reservePod(Pod pod) {
        boolean result=false;
        if(pod.getState().equalsIgnoreCase(PodStateEnum.AVAILABLE.getName())) {
            pod.setState(PodStateEnum.RESERVED.getName());
            pod=podRepository.saveAndFlush(pod);
            result=true;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("reserved pod {} 成功\n{}", pod.getId(), JSONUtil.toJSon(pod));
            }
        }
        return  result;
    }
}
