package com.mushiny.wms.tot.report.query.dto;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.tot.job.domain.Job;
//直接项目页面新增修改页面下拉框查询数据对应DTO
public class JobTypeDTO extends BaseDTO {
    private String name;
    private String description;
    private String warehouseId;

    public JobTypeDTO() {
    }
    public JobTypeDTO(Job entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

}
