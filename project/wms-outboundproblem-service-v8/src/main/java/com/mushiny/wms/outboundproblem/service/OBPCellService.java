package com.mushiny.wms.outboundproblem.service;


import com.mushiny.wms.outboundproblem.crud.dto.CellStorageLocationDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBPCellDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBPSolveDTO;
import com.mushiny.wms.outboundproblem.crud.dto.ReliveCellDTO;

import java.util.List;

public interface OBPCellService {

    //扫描Wall返回Cell格
    OBPCellDTO getCellByWallId(String obpWallId);

    void bindCell(String shipmentNo,String cellName);

    List<OBPSolveDTO> getProblemsByCell(String stationId, String wallId, String cellName);

    //清除问题处理格和释放问题处理格（根据前面传过来的solveKey判断是哪种处理方式）
    void unbindCell(String shipmentNo, String obpStationId, String cellName, String solveKey);

    List<CellStorageLocationDTO> getStoragelocationByWallName(String obpWallId,String podNo,String location);

    void relieveCell(ReliveCellDTO reliveCellDTO);

}
