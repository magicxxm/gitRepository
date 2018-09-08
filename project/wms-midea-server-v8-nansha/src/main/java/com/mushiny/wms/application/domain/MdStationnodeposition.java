package com.mushiny.wms.application.domain;

import com.mushiny.wms.common.entity.BaseEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2018/7/12.
 */
@Entity
@Table(name = "MD_STATIONNODEPOSITION")
public class MdStationnodeposition extends BaseEntity{
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "STATIONNODE_ID")
    private Stationnode stationnode;
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "NODE_ID")
    private MapNode node;
    @Column(name="NODE_TYPE")
    private Integer nodeType;
    @Column(name="DIRECTION")
    private String direction;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Stationnode getStationnode() {
        return stationnode;
    }

    public void setStationnode(Stationnode stationnode) {
        this.stationnode = stationnode;
    }

    public MapNode getNode() {
        return node;
    }

    public void setNode(MapNode node) {
        this.node = node;
    }

    public Integer getNodeType() {
        return nodeType;
    }

    public void setNodeType(Integer nodeType) {
        this.nodeType = nodeType;
    }
}
