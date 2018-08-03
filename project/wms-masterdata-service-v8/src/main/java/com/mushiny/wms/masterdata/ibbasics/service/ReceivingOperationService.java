package com.mushiny.wms.masterdata.ibbasics.service;

import com.mushiny.wms.masterdata.ibbasics.business.dto.ReceivingGoodsDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.AdviceRequestDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceivingProcessContainerDTO;

public interface ReceivingOperationService {

    AdviceRequestDTO activateAdviceNo(String adviceNo);

    void receivingGoods(ReceivingGoodsDTO receiving);

    void saveReceivingProcessContainer(ReceivingProcessContainerDTO receivingProcessContainer);

    void deleteReceivingProcessContainers(String receivingStationId);

    void replaceContainer(String oldContainerId, String newContainerId);
}
