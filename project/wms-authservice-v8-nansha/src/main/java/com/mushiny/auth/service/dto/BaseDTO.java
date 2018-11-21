package com.mushiny.auth.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.auth.domain.BaseClientAssignedEntity;
import com.mushiny.auth.domain.BaseEntity;
import com.mushiny.auth.domain.BaseWarehouseAssignedEntity;
import com.mushiny.auth.domain.User;
import com.mushiny.auth.service.exception.VersionException;

import java.io.Serializable;
import java.time.ZonedDateTime;

public abstract class BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private ZonedDateTime createdDate;

    private String createdBy;

    private ZonedDateTime modifiedDate;

    private String modifiedBy;

    private String additionalContent;

    private int lock = 0;

    private long version;

    @JsonIgnore
    private String warehouseId;

    @JsonIgnore
    private String clientId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private WarehouseDTO warehouse;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ClientDTO client;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
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

    protected BaseDTO() {
    }

    protected BaseDTO(BaseEntity entity) {
        this.id = entity.getId();
        this.version = entity.getVersion();
        this.createdDate = entity.getCreatedDate();
        this.createdBy = entity.getCreatedBy();
        this.modifiedDate = entity.getModifiedDate();
        this.modifiedBy = entity.getModifiedBy();
        this.lock = entity.getLock();
        this.additionalContent = entity.getAdditionalContent();
        if (entity instanceof BaseWarehouseAssignedEntity) {
            BaseWarehouseAssignedEntity we = (BaseWarehouseAssignedEntity) entity;
            if (we.getWarehouse() != null) {
                this.warehouse = new WarehouseDTO(we.getWarehouse());
            }
        }

        if (entity instanceof BaseClientAssignedEntity) {
            BaseClientAssignedEntity ce = (BaseClientAssignedEntity) entity;
            if (ce.getWarehouse() != null) {
                this.warehouse = new WarehouseDTO(ce.getWarehouse());
            }

            if (ce.getClient() != null) {
                this.client = new ClientDTO(ce.getClient());
            }
        }
    }

    protected void merge(BaseEntity entity) throws VersionException {
//        if (entity.getVersion() != this.version) {
//            throw new VersionException(entity.getClass().getSimpleName());
//        }

        entity.setLock(this.lock);
        entity.setAdditionalContent(this.additionalContent);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        BaseDTO dto;

        if (obj == null) return false;

        if (obj == this) return true;

        if (obj instanceof BaseDTO)
            dto = (BaseDTO) obj;
        else
            return false;
        if (this.id == null || dto.id == null) {
            return false;
        }
        return this.id.equals(dto.id) && this.version == dto.version;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (version ^ (version >>> 32));
        return result;
    }

    protected class UserDTO implements Serializable {

        private static final long serialVersionUID = 1L;

        private String id;

        private String username;

        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public UserDTO(User user) {
            if (user != null) {
                this.id = user.getId();
                this.username = user.getUsername();
                this.name = user.getName();
            }
        }
    }
}
