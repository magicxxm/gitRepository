package com.mushiny.wms.system.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SYS_MENU")
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Column(name = "ORDER_INDEX", nullable = false)
    private Integer orderIndex;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CHILD_ID")
    private Module module;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PARENT_ID")
    private Module parentModule;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Module getParentModule() {
        return parentModule;
    }

    public void setParentModule(Module parentModule) {
        this.parentModule = parentModule;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id='" + id + '\'' +
                '}';
    }
}
