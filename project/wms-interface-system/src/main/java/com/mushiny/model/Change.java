package com.mushiny.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by 123 on 2018/2/24.
 */
@Entity
@Table(name = "SUNING_ZRFC_AGV_CHANGE")
public class Change {

    @Id
    @GeneratedValue(generator = "annto-uuid")
    @GenericGenerator(name = "annto-uuid", strategy = "uuid2")
    @Column(name = "UID")
    private String id;

    @Column(name = "DISTRIBUTION_SYSTEM")
    private String system;

    @Column(name = "CHANGE_ORDER")
    private String changeOrder;

    @Column(name = "FSTIME")
    private String fsTime;

    @Column(name = "NEED_RESPONSE")
    private String needResponse;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getChangeOrder() {
        return changeOrder;
    }

    public void setChangeOrder(String changeOrder) {
        this.changeOrder = changeOrder;
    }

    public String getFsTime() {
        return fsTime;
    }

    public void setFsTime(String fsTime) {
        this.fsTime = fsTime;
    }

    public String getNeedResponse() {
        return needResponse;
    }

    public void setNeedResponse(String needResponse) {
        this.needResponse = needResponse;
    }
}
