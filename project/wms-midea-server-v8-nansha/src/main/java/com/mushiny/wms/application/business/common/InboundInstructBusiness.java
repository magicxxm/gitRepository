package com.mushiny.wms.application.business.common;

import com.mushiny.wms.application.domain.InboundInstruct;
import com.mushiny.wms.application.repository.InboundInstructRepository;
import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/7/6.
 */
@Component
public class InboundInstructBusiness {

    private Logger LOG = LoggerFactory.getLogger(InboundInstructBusiness.class.getName());

    private final InboundInstructRepository inboundInstructRepository;

    @Autowired
    public InboundInstructBusiness(InboundInstructRepository inboundInstructRepository) {
        this.inboundInstructRepository = inboundInstructRepository;
    }

    /**
     * 保存入库单
     * 修改人： mingchun.mu@mushiny.com
     * @param inboundInstruct
     * @return 0 失败 -- 未插入数据； 1 成功 -- 插入数据
     */
    public Integer saveInboundInstruct(InboundInstruct inboundInstruct){
        //先查找在更新
        if(!CommonUtil.isPositiveInteger(inboundInstructRepository.countInboundInstruct(inboundInstruct))){
            inboundInstructRepository.save(inboundInstruct);
            return Constant.SUCCESS_FLAG;
        }
        return Constant.FAIL_FLAG;
    }

    public Integer saveInboundInstruct2(InboundInstruct inboundInstruct){
        Integer result=Constant.SUCCESS_FLAG;
        //先查找在更新

            try{
                inboundInstructRepository.save(inboundInstruct);
            }catch (Exception e)
            {
                result=Constant.FAIL_FLAG;
                LOG.error(e.getMessage(),e);
                throw e;
            }

            return result;

    }


}
