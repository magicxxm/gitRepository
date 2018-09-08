package com.mushiny.wms.application.service;

import com.mushiny.wms.application.domain.OutboundInstruct;
import com.mushiny.wms.application.domain.WmsInstructOutPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Administrator on 2018/7/6.
 */
public interface OutboundInstructService {

    Integer saveOnboundInstruct(String outboundInstruct);

    Page<OutboundInstruct> getByOutboundLabelNo(String startTime, String endTime, String labelNo, Pageable pageable);

    List<WmsInstructOutPosition> getOutboundByLabelNo(String labelNo);

}
