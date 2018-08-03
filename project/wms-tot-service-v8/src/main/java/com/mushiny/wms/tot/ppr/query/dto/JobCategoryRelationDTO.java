package com.mushiny.wms.tot.ppr.query.dto;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.tot.ppr.domain.JobCategoryRelation;

import java.math.BigDecimal;

/**
 * Created by Laptop-8 on 2017/6/29.
 * 工作计划页面DTO
 */
public class JobCategoryRelationDTO extends BaseDTO {

    private  String  mainProcesses;
    private  String  coreProcesses;
    private  String  categoryName;
    private  String  lineItems;
    private  String reorder;
    private Double planRate;

    public JobCategoryRelationDTO(  ) {

    }
    public JobCategoryRelationDTO(JobCategoryRelation entity ) {
        super(entity);
    }

    public String getMainProcesses() {
        return mainProcesses;
    }

    public void setMainProcesses(String mainProcesses) {
        this.mainProcesses = mainProcesses;
    }

    public String getCoreProcesses() {
        return coreProcesses;
    }

    public void setCoreProcesses(String coreProcesses) {
        this.coreProcesses = coreProcesses;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLineItems() {
        return lineItems;
    }

    public void setLineItems(String lineItems) {
        this.lineItems = lineItems;
    }

    public String getReorder() {
        return reorder;
    }

    public void setReorder(String reorder) {
        this.reorder = reorder;
    }

    public Double getPlanRate() {
        return planRate;
    }

    public void setPlanRate(Double planRate) {
        this.planRate = planRate;
    }
}
