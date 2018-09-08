package com.mushiny.wms.application.service.impl;

import com.mushiny.wms.application.business.common.BuildEntityBusiness;
import com.mushiny.wms.application.business.common.PodReserveUtil;
import com.mushiny.wms.application.domain.Constance;
import com.mushiny.wms.application.domain.Pod;
import com.mushiny.wms.application.domain.Section;
import com.mushiny.wms.application.domain.WmsInvUnitload;
import com.mushiny.wms.application.domain.enums.StationType;
import com.mushiny.wms.application.domain.enums.TripType;
import com.mushiny.wms.application.repository.PodRepository;
import com.mushiny.wms.application.repository.SectionRepository;
import com.mushiny.wms.application.repository.WmsInvUnitLoadRepository;
import com.mushiny.wms.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */
@Service
@Transactional
public class PutBackServiceImpl implements PutBackService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PutBackServiceImpl.class);
    private final PodRepository podRepository;
    private final SectionRepository sectionRepository;
    private final PodReserveUtil podReserveUtil;
    private final BuildEntityBusiness buildEntityBusiness;
    private final WmsInvUnitLoadRepository wmsInvUnitLoadRepository;
@Autowired
    public PutBackServiceImpl(PodRepository podRepository, SectionRepository sectionRepository, PodReserveUtil podReserveUtil, BuildEntityBusiness buildEntityBusiness,WmsInvUnitLoadRepository wmsInvUnitLoadRepository) {
        this.podRepository = podRepository;
        this.sectionRepository = sectionRepository;
        this.podReserveUtil = podReserveUtil;
        this.buildEntityBusiness = buildEntityBusiness;
    this.wmsInvUnitLoadRepository=wmsInvUnitLoadRepository;
    }


    @Override
    public void execute() {


        List<Section> sections=sectionRepository.getAll();

            for(Section section:sections)
            {
                doExecute(section,StationType.PODCARRY,false);
            }
    }

    @Override
    public void test() {
        List<Section> sections=sectionRepository.getAll();

        for(Section section:sections)
        {
            doExecute(section,StationType.INBOUND,false);
        }
    }

    @Override
    public void testAll() {
        List<Section> sections=sectionRepository.getAll();

        for(Section section:sections)
        {
            doExecute(section,StationType.INBOUND,true);
        }
    }

    private  synchronized void doExecute(Section section,Integer stationType,boolean buildUn ){
        List<Pod> pods=podRepository.getPutBackPod(section.getId(),stationType);
        LOGGER.info("sction {} pod 放回区类型{} 查到货架\n{}",section.getName(),stationType, JSONUtil.toJSon(pods));
        for(Pod pod:pods)
        {
            podReserveUtil.reservePod(pod);
            buildEntityBusiness.buildTrip(pod,null,null, TripType.POD_RUN);
            if(buildUn)
            {
                WmsInvUnitload invLoad=new WmsInvUnitload();
                invLoad.setPodIndex(pod.getPodIndex());
                invLoad.setInboundInstructId("");
                //重量的获取
                invLoad.setStationName("system");
                invLoad.setEntityLock(Constance.avable);
                invLoad.setWarehouseId(pod.getWarehouseId());
                invLoad=wmsInvUnitLoadRepository.saveAndFlush(invLoad);
            }
        }

    }






}
