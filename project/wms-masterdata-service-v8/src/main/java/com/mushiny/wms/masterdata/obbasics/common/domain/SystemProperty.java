package com.mushiny.wms.masterdata.obbasics.common.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_SYSTEMPROPERTY")
public class SystemProperty extends BaseClientAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "SYSTEM_KEY", nullable = false)
    private String systemKey;

    @Column(name = "SYSTEM_VALUE", nullable = false)
    private String systemValue;

    @Column(name = "DESCRIPTION")
    private String description;

//    @Column(name = "WORKSTATION", nullable = false)
//    private String workstation = WORKSTATION_DEFAULT;
//
//    @Column(name = "GROUP_NAME")
//    private String groupName;
//
//    @Column(name = "HIDDEN", nullable = false)
//    private boolean hidden = false;

    public String getSystemKey() {
        return systemKey;
    }

    public void setSystemKey(String systemKey) {
        this.systemKey = systemKey;
    }

    public String getSystemValue() {
        return systemValue;
    }

    public void setSystemValue(String systemValue) {
        this.systemValue = systemValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public String getWorkstation() {
//        return workstation;
//    }
//
//    public void setWorkstation(String workstation) {
//        this.workstation = workstation;
//    }
//
//    public String getGroupName() {
//        return groupName;
//    }
//
//    public void setGroupName(String groupName) {
//        this.groupName = groupName;
//    }
//
//    public boolean isHidden() {
//        return hidden;
//    }
//
//    public void setHidden(boolean hidden) {
//        this.hidden = hidden;
//    }
}
