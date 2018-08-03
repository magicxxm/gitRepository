package com.mushiny.wms.outboundproblem.service;


import com.mushiny.wms.outboundproblem.crud.common.dto.ScanningOBPStationDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBPWallDTO;

public interface OBPVerificationService {

    ScanningOBPStationDTO scanOBPStation(String name);

    OBPWallDTO scanOBPWall(String name);
}
