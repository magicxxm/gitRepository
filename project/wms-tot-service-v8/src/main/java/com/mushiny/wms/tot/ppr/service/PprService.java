package com.mushiny.wms.tot.ppr.service;

import com.mushiny.wms.tot.ppr.query.dto.PprDetailOfJobDTO;
import com.mushiny.wms.tot.ppr.query.dto.PprMainPageDTO;

import java.text.ParseException;
import java.util.List;

public interface PprService {

    List<PprMainPageDTO> getRecordsForPpr(String warehouseId, String clientId, String startDate, String endDate) throws ParseException;

    List getRecordsForPprDetail(String warehouseId, String clientId, String category,
                                                   String startDate, String endDate) throws ParseException;
}
