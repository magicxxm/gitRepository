package com.mushiny.wcs.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mushiny.wcs.common.Constant;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@JsonIgnoreProperties({"createdDate", "createdBy", "modifiedDate", "modifiedBy", "additionalContent", "ENTITY_LOCK", "VERSION"})
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "internaltool-uuid")
    @GenericGenerator(name = "internaltool-uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "CREATED_BY", updatable = false)
    private String createdBy = "admin";

    @Column(name = "MODIFIED_DATE", nullable = false)
    private LocalDateTime modifiedDate = LocalDateTime.now();

    @Column(name = "MODIFIED_BY")
    private String modifiedBy = "admin";

    @Column(name = "ADDITIONAL_CONTENT")
    private String additionalContent;

    @Column(name = "ENTITY_LOCK")
    private Integer entityLock = Constant.NOT_LOCKED;

    @Column(name = "VERSION", nullable = false)
    @Version
    @JsonIgnore
    private Integer version = 0;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
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

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id='" + id + '\'' +
                '}';
    }
}
