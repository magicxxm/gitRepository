package com.mushiny.wms.application.business.common;

import com.mushiny.wms.application.domain.OutboundInstruct;
import com.mushiny.wms.application.repository.OutboundInstructRepository;
import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2018/7/6.
 */
@Component
public class OutboundInstructBusiness {
    private Logger LOG = LoggerFactory.getLogger(OutboundInstructBusiness.class.getName());
    private final OutboundInstructRepository outboundInstructRepository;

    public OutboundInstructBusiness(OutboundInstructRepository outboundInstructRepository) {
        this.outboundInstructRepository = outboundInstructRepository;
    }


    public Integer saveOutboundInstruct(OutboundInstruct outboundInstruct)
    {
        //先查找在更新
        if(!CommonUtil.isPositiveInteger(outboundInstructRepository.countOutboundInstruct(outboundInstruct))){
            outboundInstructRepository.save(outboundInstruct);
            return Constant.SUCCESS_FLAG;
        }
        return Constant.FAIL_FLAG;
    }

    public Integer saveOutboundInstruct2(OutboundInstruct outboundInstruct)
    {
        Integer result=Constant.SUCCESS_FLAG;
        try{
            outboundInstructRepository.save(outboundInstruct);
        }catch (Exception e)
        {
            result=Constant.FAIL_FLAG;
            LOG.error(e.getMessage(),e);
        }

        return result;
    }
}
