package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.general.crud.dto.UserDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStation;
import com.mushiny.wms.masterdata.ibbasics.domain.TimeConfig;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationDTO;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class TimeConfigDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private String refreshTime;
    private int refreshDay;

    public TimeConfigDTO(TimeConfig TimeConfig) {
        super(TimeConfig);
    }

    public String getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(String refreshTime) {
        this.refreshTime = refreshTime;
    }

    public int getRefreshDay() {
        return refreshDay;
    }

    public void setRefreshDay(int refreshDay) {
        this.refreshDay = refreshDay;
    }
}
