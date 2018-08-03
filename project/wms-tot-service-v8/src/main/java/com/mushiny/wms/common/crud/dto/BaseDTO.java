package com.mushiny.wms.common.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.entity.BaseEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class BaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime modifiedDate;
    private String modifiedBy;
    private String additionalContent;
    private Integer entityLock = Constant.NOT_LOCKED;
    public BaseDTO() {
    }
    public BaseDTO(BaseEntity baseEntity) {
        this.id = baseEntity.getId();
        this.additionalContent = baseEntity.getAdditionalContent();
        this.entityLock = baseEntity.getEntityLock();
        this.createdBy = baseEntity.getCreatedBy();
        this.createdDate = baseEntity.getCreatedDate();
        this.modifiedBy = baseEntity.getModifiedBy();
        this.modifiedDate = baseEntity.getModifiedDate();
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    public String getModifiedBy() {
        return modifiedBy;
    }
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    public String getAdditionalContent() {
        return additionalContent;
    }
    public void setAdditionalContent(String additionalContent) {
        this.additionalContent = additionalContent;
    }
    public Integer getEntityLock() {
        return entityLock;
    }
    public void setEntityLock(Integer entityLock) {
        this.entityLock = entityLock;
    }
}
