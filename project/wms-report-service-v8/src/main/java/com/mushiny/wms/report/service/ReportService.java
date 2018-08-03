package com.mushiny.wms.report.service;


import com.mushiny.wms.report.query.dto.*;
import com.mushiny.wms.report.query.dto.capacityTotal.CapacityDTO;
import com.mushiny.wms.report.query.dto.pp_work.PpWorkDTO;

import java.util.List;
import java.util.Map;

public interface ReportService {

    //获取FUD数据
    List<FudDTO> getFud();

    //获取遗留数据
    List<FudDTO> getContainerLegacyData();

    //获取 WorkFlow 合计
    List<WorkFlowDTO> getWorkFlows(String startTime, String endTime, String goodsType);

    //获取WorkFlow 明细
    List<WorkFlowDetailDTO> getWorkFlowDetails(String ppName, String exsdTime, String workflowType);

    // 获取Capacity 数据
    List<CapacityDTO> getCapacity();

    //获取Capacity-Pod 数据
    List<CapacityPodDTO> getPods();

    //获取Capacity-pod-side 所有面 数据
    List<CapacityPodDTO> getByPodName(String podName);

    //获取某个pod下 -> 某个面下 -> 所有bin(货位) 数据
    List<CapacityPodDTO> getByPodNameAndFace(String podName);

    //获取 PickArea
    List<PickDTO> getPickArea(String ppName, String zoneName, String deliveryDate);

    List<PickDTO> getPickExSD(String ppName, String zoneName, String deliveryDate);

    //获取Process Path-Work Pool 合计
    PpWorkDTO getPpWork(String startTime, String endTime, String goodsType);

    //获取 WorkPool-Process Path 合计
    List<WorkPpDTO> getByWorkPps(String startTime, String endTime, String goodsType);


}
