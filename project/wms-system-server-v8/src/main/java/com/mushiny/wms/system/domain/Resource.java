package com.mushiny.wms.system.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "SYS_RESOURCE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "RESOURCE_KEY", "LOCALE"
        })
})
public class Resource extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "RESOURCE_KEY", nullable = false)
    private String resourceKey;

    @Column(name = "LOCALE", nullable = false)
    private String locale;

    @Column(name = "RESOURCE_VALUE")
    private String resourceValue;

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getResourceValue() {
        return resourceValue;
    }

    public void setResourceValue(String resourceValue) {
        this.resourceValue = resourceValue;
    }
}
