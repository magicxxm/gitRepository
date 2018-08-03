package com.mushiny.wms.common.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemDataDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemDataGlobalDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.StorageLocationDTO;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<ItemDataGlobalDTO> upDateSize;

    public List<ItemDataGlobalDTO> getUpDateSize() {
        return upDateSize;
    }

    public void setUpDateSize(List<ItemDataGlobalDTO> upDateSize) {
        this.upDateSize = upDateSize;
    }
}
