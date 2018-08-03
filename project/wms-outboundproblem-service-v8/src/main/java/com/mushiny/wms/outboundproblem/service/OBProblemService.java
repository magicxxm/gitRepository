package com.mushiny.wms.outboundproblem.service;


import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.outboundproblem.crud.common.dto.InboundProblemRuleDTO;
import com.mushiny.wms.outboundproblem.crud.common.dto.MoveGoodsDTO;
import com.mushiny.wms.outboundproblem.crud.common.dto.StockUnitRecordDTO;
import com.mushiny.wms.outboundproblem.crud.dto.*;
import com.mushiny.wms.outboundproblem.crud.common.dto.ItemDataGlobalDTO;
import com.mushiny.wms.outboundproblem.domain.OBProblem;
import com.mushiny.wms.outboundproblem.domain.OBProblemCheck;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OBProblemService extends BaseService<OBProblemCheckDTO> {
    //报问题到OB问题处理
    OBProblem generateOBProblem(OBProblemDTO obProblemDTO);
    //OB问题处理报问题
    OBProblem creatOBProblem(OBProblemDTO obProblemDTO);

    List<OBProblemCheckDTO> updateClose(List<String> ids,String name);

    BigDecimal sumByProblemStorageLocationAndOpen(String problemStorageLocation);

    BigDecimal createLess(String storageLocationId);

    List<OBProblemDTO> getByContainer(String container);

    List<StowStockunitRecordDTO> sumStockunitRecordByProblem(String inboundProblemId,String jobType);

    //待问题处理区,正在进行问题处理区 查询与搜索
    List<OBProblemCheckDTO> getBySeek(String state, String userName, String seek,
                                      String startDate, String endDate);

    ItemDataGlobalDTO getByItem(String obproblemId);

    List<OBProblemDataDTO> findLocationByItem(String problemStoragelocation,String itemNo,String jobType);

    void findOverageGoods(String storageLocation, String itemSku, BigDecimal amount,String fromName,String jobType);

    void findlossGoods(String fromName, String toName, String itemSku, BigDecimal amount,String jobType);

    List<OBProblemCheckDTO> getAll();

    List<Map> getAnalysis(List<String> ids);

    void moving(MoveGoodsDTO moveGoodsDTO);

    void overageGoods(OverageGoodsDTO overageGoodsDTO);

    void lossGoods(LossGoodsDTO lossGoodsDTO);

    void stowoverageGoods(String storageLocation, String itemdataId, BigDecimal amount,String jobType);

    void stowlossGoods(String fromName, String toName, String itemdataId, BigDecimal amount,String jobType);

    InboundProblemRuleDTO getRule(String rule);

    OBPStationDTO bindingWorkstation(String name);

    OBPStationDTO untieWorkstation(String name);

    //停止呼叫pod和恢复分配pod
    void getStopCallPod(String workStationId, String type);

    List<Map> getPodFace(List<String> outboundProblemIds,String sectionId,String jobType);


    Boolean getWorkStationPodState(String workStationId);

    Boolean getYesOrNoFinsh(String name);


}
