package com.mushiny.wms.application.domain;

import com.mushiny.wms.common.entity.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @program: wms-midea-server
 * @description: midea 工作站
 * @author: mingchun.mu@mushiny.com
 * @create: 2018-07-10 17:04
 **/

@Entity
@Table(name = "MD_STATIONNODE")
public class Stationnode extends BaseEntity {

    @Column(name = "NAME")
    private String name;
    @Column(name = "IS_CALL_POD")
    private Boolean isCallPod;
    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "SECTION_ID")
    private String sectionId;

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;



    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCallPod() {
        return isCallPod;
    }

    public void setCallPod(Boolean callPod) {
        isCallPod = callPod;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }



    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }


}
