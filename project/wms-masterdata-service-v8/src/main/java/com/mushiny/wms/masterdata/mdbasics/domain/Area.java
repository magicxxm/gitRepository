package com.mushiny.wms.masterdata.mdbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;

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

    @Column(name = "USE_FOR_RETURN", nullable = false)
    private boolean userForReturn;

    @Column(name = "USE_FOR_TRANSPORT", nullable = false)
    private boolean userForTransport;

//    @ManyToOne(optional = false)
//    @JoinColumn(name = "CLIENT_ID")
//    private Client client;

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

    public boolean isUserForReturn() {
        return userForReturn;
    }

    public void setUserForReturn(boolean userForReturn) {
        this.userForReturn = userForReturn;
    }

    public boolean isUserForTransport() {
        return userForTransport;
    }

    public void setUserForTransport(boolean userForTransport) {
        this.userForTransport = userForTransport;
    }

//    public Client getClient() {
//        return client;
//    }
//
//    public void setClient(Client client) {
//        this.client = client;
//    }
}
