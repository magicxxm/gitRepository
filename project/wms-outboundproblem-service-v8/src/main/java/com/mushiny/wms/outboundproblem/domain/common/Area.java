package com.mushiny.wms.outboundproblem.domain.common;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "MD_AREA", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "CLIENT_ID", "WAREHOUSE_ID"
        })
})
public class Area extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "USE_FOR_GOODSIN", nullable = false)
    private boolean useForGoodsIn;

    @Column(name = "USE_FOR_GOODSOUT", nullable = false)
    private boolean useForGoodsOut;

    @Column(name = "USE_FOR_PICKING", nullable = false)
    private boolean useForPicking;

    @Column(name = "USE_FOR_REPLENISH", nullable = false)
    private boolean useForReplenish;

    @Column(name = "USE_FOR_STORAGE", nullable = false)
    private boolean useForStorage;

    @Column(name = "USE_FOR_TRANSFER", nullable = false)
    private boolean useForTransfer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isUseForGoodsIn() {
        return useForGoodsIn;
    }

    public void setUseForGoodsIn(boolean useForGoodsIn) {
        this.useForGoodsIn = useForGoodsIn;
    }

    public boolean isUseForGoodsOut() {
        return useForGoodsOut;
    }

    public void setUseForGoodsOut(boolean useForGoodsOut) {
        this.useForGoodsOut = useForGoodsOut;
    }

    public boolean isUseForPicking() {
        return useForPicking;
    }

    public void setUseForPicking(boolean useForPicking) {
        this.useForPicking = useForPicking;
    }

    public boolean isUseForReplenish() {
        return useForReplenish;
    }

    public void setUseForReplenish(boolean useForReplenish) {
        this.useForReplenish = useForReplenish;
    }

    public boolean isUseForStorage() {
        return useForStorage;
    }

    public void setUseForStorage(boolean useForStorage) {
        this.useForStorage = useForStorage;
    }

    public boolean isUseForTransfer() {
        return useForTransfer;
    }

    public void setUseForTransfer(boolean useForTransfer) {
        this.useForTransfer = useForTransfer;
    }
}
