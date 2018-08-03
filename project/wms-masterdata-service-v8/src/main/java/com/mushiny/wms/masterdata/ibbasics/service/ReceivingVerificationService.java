package com.mushiny.wms.masterdata.ibbasics.service;

import com.mushiny.wms.masterdata.ibbasics.business.dto.ItemDataAmountDTO;
import com.mushiny.wms.masterdata.ibbasics.business.dto.ScanningReceivingStationDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.AdviceRequestDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveDestinationDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceivingProcessContainerDTO;
//import com.mushiny.wms.masterdata.mdbasics.crud.dto.ContainerDTO;

import java.util.List;

public interface ReceivingVerificationService {

    AdviceRequestDTO scanAdviceRequest(String adviceNo);

    ScanningReceivingStationDTO scanReceivingStation(String name);

    ReceiveDestinationDTO scanReceivingDestination(String stationId, String name);

//    ContainerDTO scanContainer(String name);

    ItemDataAmountDTO scanAdvicePositionItemData(String adviceId, String itemNo);

    ReceiveDestinationDTO screeningReceivingDestination(String itemDataId, String receivingType);

//    ContainerDTO checkReceivingContainer(String containerId, String itemDataId, LocalDate useNotAfter);

    List<ReceivingProcessContainerDTO> getReceivingProcessContainers(String receivingStationId);
}
