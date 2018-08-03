package com.mushiny.wms.tot.jobrecord.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.tot.general.domain.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "IB_RECEIVETHRESHOLD")
public class ReceiveThreshold extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "THRESHOLD")
    private BigDecimal threshold;

    @ManyToMany
    @OrderBy("username")
    @JoinTable(
            name = "IB_RECEIVEELIGIBILITY",
            joinColumns = @JoinColumn(name = "RECEIVETHRESHOLD_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    private Set<User> receiveThresholds = new HashSet<>();

//    @ManyToMany
//    @OrderBy("username")
//    @JoinTable(
//            name = "USER_WAREHOUSE",
//            joinColumns = @JoinColumn(name = "WAREHOUSE_ID"),
//            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
//    private Set<User> users = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getThreshold() {
        return threshold;
    }

    public void setThreshold(BigDecimal threshold) {
        this.threshold = threshold;
    }

    public Set<User> getReceiveThresholds() {
        return receiveThresholds;
    }

    public void setReceiveThresholds(Set<User> receiveThresholds) {
        this.receiveThresholds = receiveThresholds;
    }
}
