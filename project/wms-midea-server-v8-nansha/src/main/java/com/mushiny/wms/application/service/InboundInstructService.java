package com.mushiny.wms.application.service;

import com.mushiny.wms.application.domain.InboundInstruct;
import com.mushiny.wms.application.domain.WmsInstructInPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Administrator on 2018/7/6.
 */
public interface InboundInstructService {

    Integer saveInboundInstruct(String inboundInstruct);

    Page<InboundInstruct> getByInboundLabelNo(String startTime, String endTime, String labelNo, Pageable pageable);

    List<WmsInstructInPosition> getInboundByLabelNo(String labelNo);
}
