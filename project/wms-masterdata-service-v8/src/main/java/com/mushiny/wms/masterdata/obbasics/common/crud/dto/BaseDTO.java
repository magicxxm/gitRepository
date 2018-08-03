package com.mushiny.wms.masterdata.obbasics.common.crud.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.common.entity.BaseEntity;
import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;
import com.mushiny.wms.masterdata.general.crud.dto.ClientDTO;
import com.mushiny.wms.masterdata.general.crud.dto.WarehouseDTO;
import com.mushiny.wms.masterdata.obbasics.common.exception.VersionException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public abstract class BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

//    @JsonIgnore
    private LocalDateTime createdDate;

//    @JsonIgnore
    private String createdBy;

//    @JsonIgnore
    private LocalDateTime modifiedDate;

//    @JsonIgnore
    private String modifiedBy;

    private String additionalContent;

    private int lock = 0;

    @JsonIgnore
    private long version;

    @JsonIgnore
    private WarehouseDTO warehouse;

    @JsonIgnore
    private ClientDTO client;

    private String warehouseId;

    private String warehouseNumber;

    private String warehouseName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String clientId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String clientNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String clientName;

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

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public WarehouseDTO getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseDTO warehouse) {
        this.warehouse = warehouse;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public String getWarehouseNumber() {
        return warehouseNumber;
    }

    public void setWarehouseNumber(String warehouseNumber) {
        this.warehouseNumber = warehouseNumber;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    protected BaseDTO() {
    }

    protected BaseDTO(BaseEntity entity) {
        this.id = entity.getId();
        this.version = entity.getVersion();
        this.createdDate = entity.getCreatedDate();
        this.createdBy = entity.getCreatedBy();
        this.modifiedDate = entity.getModifiedDate();
        this.modifiedBy = entity.getModifiedBy();
        this.lock = entity.getEntityLock();
        this.additionalContent = entity.getAdditionalContent();
        if (entity instanceof BaseWarehouseAssignedEntity) {
            BaseWarehouseAssignedEntity we = (BaseWarehouseAssignedEntity) entity;
            if (we.getWarehouseId() != null) {
//                this.warehouse = new WarehouseDTO(we.getWarehouse());
                this.warehouseId = we.getWarehouseId();
                this.warehouseNumber = we.getWarehouseId();
                this.warehouseName = we.getWarehouseId();
            }
        }

        if (entity instanceof BaseClientAssignedEntity) {
            BaseClientAssignedEntity ce = (BaseClientAssignedEntity) entity;
            if (ce.getWarehouseId() != null) {
//                this.warehouse = new WarehouseDTO(ce.getWarehouse());
                this.warehouseId = ce.getWarehouseId();
                this.warehouseNumber = ce.getWarehouseId();
                this.warehouseName = ce.getWarehouseId();
            }

            if (ce.getClientId() != null) {
//                this.client = new ClientDTO(ce.getClient());
                this.clientId = ce.getClientId();
                this.clientNumber = ce.getClientId();
                this.clientName = ce.getClientId();
            }
        }
    }

    protected void merge(BaseEntity entity) throws VersionException {
//        if (entity.getVersion() != this.version) {
//            throw new VersionException(entity.getClass().getSimpleName());
//        }

        entity.setEntityLock(this.lock);
        entity.setAdditionalContent(this.additionalContent);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        com.mushiny.wms.common.crud.dto.BaseDTO dto;

        if (obj == null) return false;

        if (obj == this) return true;

        if (obj instanceof com.mushiny.wms.common.crud.dto.BaseDTO)
            dto = (com.mushiny.wms.common.crud.dto.BaseDTO) obj;
        else
            return false;
        if (this.id == null || dto.getId()== null) {
            return false;
        }
        return this.id.equals(dto.getId()) && this.version == 0;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (version ^ (version >>> 32));
        return result;
    }
}
