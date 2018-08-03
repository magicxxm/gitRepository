package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.AdviceRequest;
import com.mushiny.wms.masterdata.ibbasics.domain.UnitLoad;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UnitLoadDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

   private String stationName;


    public UnitLoadDTO(UnitLoad unitLoad) {
       super(unitLoad);
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
}
