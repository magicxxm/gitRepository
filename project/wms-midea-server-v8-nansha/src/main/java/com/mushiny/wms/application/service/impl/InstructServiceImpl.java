package com.mushiny.wms.application.service.impl;

import com.mushiny.wms.application.domain.InboundInstruct;
import com.mushiny.wms.application.domain.OutboundInstruct;
import com.mushiny.wms.application.domain.WmsInstructInPosition;
import com.mushiny.wms.application.domain.WmsInstructOutPosition;
import com.mushiny.wms.application.domain.enums.InstructStatus;
import com.mushiny.wms.application.repository.InboundInstructRepository;
import com.mushiny.wms.application.repository.OutboundInstructRepository;
import com.mushiny.wms.application.repository.WmsInstructInPositionRepository;
import com.mushiny.wms.application.repository.WmsInstructOutPositionRepository;
import com.mushiny.wms.application.service.EmptyPodCarryService;
import com.mushiny.wms.application.service.InboundTripService;
import com.mushiny.wms.application.service.InstructService;
import com.mushiny.wms.application.service.OutboundTripService;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/10.
 */
@Service
@Transactional
public class InstructServiceImpl implements InstructService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstructServiceImpl.class);
    private OutboundTripService outboundTripService;
    private InboundTripService inboundTripService;
    private OutboundInstructRepository outboundInstructRepository;
    private InboundInstructRepository inboundInstructRepository;
    private WmsInstructInPositionRepository wmsInstructInPositionRepository;
    private WmsInstructOutPositionRepository wmsInstructOutPositionRepository;
    @Autowired
    public InstructServiceImpl(OutboundTripService outboundTripService, InboundTripService inboundTripService,OutboundInstructRepository outboundInstructRepository,
            InboundInstructRepository inboundInstructRepository,
 WmsInstructInPositionRepository wmsInstructInPositionRepository,WmsInstructOutPositionRepository wmsInstructOutPositionRepository) {
        this.outboundTripService = outboundTripService;
        this.inboundTripService = inboundTripService;
        this.outboundInstructRepository=outboundInstructRepository;
        this.inboundInstructRepository =inboundInstructRepository;
        this.wmsInstructInPositionRepository=wmsInstructInPositionRepository;
        this.wmsInstructOutPositionRepository=wmsInstructOutPositionRepository;
    }

    @Override
    public boolean cancelInstruct(String cancelRequest) {
        boolean result=false;
       List<Map>  params= JSONUtil.jsonToList(cancelRequest);


        if(!CollectionUtils.isEmpty(params))
        {
            Map param=params.get(0);
            String id= ""+ param.get("ID");
            String InOrOut= ""+param.get("InOrOut");
            String INV_ORG_ID= ""+param.get("INV_ORG_ID");
            String BILL_TYPE= ""+param.get("BILL_TYPE");
            String BILL_NO= ""+param.get("BILL_NO");
            String LABEL_NO= ""+param.get("LABEL_NO");
            String INV_CODE= ""+param.get("INV_CODE");
            String MO_NAME= ""+param.get("MO_NAME");
            if("In".equalsIgnoreCase(InOrOut))
            {
             /*   InboundInstruct inboundInstruct =inboundInstructRepository.getInstruByCondition(INV_ORG_ID,BILL_TYPE,BILL_NO,LABEL_NO,INV_CODE,MO_NAME);
*/
                InboundInstruct inboundInstruct =inboundInstructRepository.getInstruByMesId(id);
                if(!ObjectUtils.isEmpty(inboundInstruct))
                {
                    result=inboundTripService.cancelInstruct(inboundInstruct, InstructStatus.CANCEL.getStatus());
                }else{
                    LOGGER.error("未找到入库指令,参数{}",JSONUtil.toJSon(param));
                }

            }else{
               /* OutboundInstruct outboundInstruct =outboundInstructRepository.getInstruByCondition(INV_ORG_ID,BILL_TYPE,BILL_NO,INV_CODE,MO_NAME);*/
                OutboundInstruct outboundInstruct =outboundInstructRepository.getInstruById(id);
                if(!ObjectUtils.isEmpty(outboundInstruct))
                {
                    result=outboundTripService.cancelInstruct(outboundInstruct, InstructStatus.CANCEL.getStatus());
                }else{
                    LOGGER.error("未找到出库指令,参数{}",JSONUtil.toJSon(param));
                }
            }
        }else {
            LOGGER.error("转换指令{}出错",cancelRequest);
        }
        return result;
    }

    @Override
    public Object getInstruct(String param) {
        Object result=null;
        Map param2= JSONUtil.jsonToMap(param);
        String ID= (String) param2.get("ID");
        String InOrOut= (String) param2.get("InOrOut");
        String INV_ORG_ID= (String) param2.get("INV_ORG_ID");
        String BILL_TYPE= (String) param2.get("BILL_TYPE");
        String BILL_NO= (String) param2.get("BILL_NO");
        String INV_CODE= (String) param2.get("INV_CODE");
        String MO_NAME= (String) param2.get("MO_NAME");
        if("In".equalsIgnoreCase(InOrOut))
        {
           /* List<WmsInstructInPosition> inboundInstruct =wmsInstructInPositionRepository.getWmsInstructInPosition(INV_ORG_ID,BILL_TYPE,BILL_NO,INV_CODE,MO_NAME);*/
            List<WmsInstructInPosition> inboundInstruct =wmsInstructInPositionRepository.getWmsInstructInPosition(INV_ORG_ID,BILL_TYPE,ID);
            result=inboundInstruct;

        }else{
            List<WmsInstructOutPosition> outboundInstruct =wmsInstructOutPositionRepository.getInstructOutPosition(INV_ORG_ID,BILL_TYPE,ID);
            result=outboundInstruct;
        }
        return result;
    }

    @Override
    public Integer updateInstruct(String param) {
        Integer result=1;
        Map param2= JSONUtil.jsonToMap(param);
        String InOrOut= (String) param2.get("InOrOut");
        String INV_ORG_ID= (String) param2.get("INV_ORG_ID");
        String BILL_TYPE= (String) param2.get("BILL_TYPE");
        String BILL_NO= (String) param2.get("BILL_NO");
        String INV_CODE= (String) param2.get("INV_CODE");
        String MO_NAME= (String) param2.get("MO_NAME");
        if("In".equalsIgnoreCase(InOrOut))
        {
            InboundInstruct inboundInstruct =inboundInstructRepository.getInstruByCondition(INV_ORG_ID,BILL_TYPE,BILL_NO,INV_CODE,MO_NAME);

            if(!ObjectUtils.isEmpty(inboundInstruct))
            {

            }else{
                result=0;
            }


        }else{

            OutboundInstruct outboundInstruct=outboundInstructRepository.getInstruByCondition(INV_ORG_ID,BILL_TYPE,BILL_NO,INV_CODE,MO_NAME);
            if(!ObjectUtils.isEmpty(outboundInstruct))
            {
                outboundInstruct.setWORKCENTER_CODE((String) param2.get("WORKCENTER_CODE"));

                outboundInstructRepository.save(outboundInstruct);
            }else{
                result=0;
            }
        }
        return result;
    }
}
