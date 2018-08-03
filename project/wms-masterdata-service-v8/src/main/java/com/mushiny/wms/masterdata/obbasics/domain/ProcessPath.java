package com.mushiny.wms.masterdata.obbasics.domain;


import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;
import com.mushiny.wms.masterdata.general.domain.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OB_PROCESSPATH", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"NAME", "WAREHOUSE_ID"})
})
public class ProcessPath extends BaseWarehouseAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PICK_WAY", nullable = false)
    private String pickWay;
    @Column(name = "REGENERATE_SHORTED_PICKS")
    private String regenerateShortedPicks;

    @Column(name = "MIN_SHIPMENTS_PER_BATCH")
    private int minShipmentsPerBatch = 0;

    @Column(name = "MAX_SHIPMENTS_PER_BATCH")
    private int maxShipmentsPerBatch = 0;

    @Column(name = "MIN_ITEMS_PER_BATCH")
    private int minItemsPerBatch = 0;

    @Column(name = "MAX_ITEMS_PER_BATCH")
    private int maxItemsPerBatch = 0;

    @Column(name = "COLLATE_DOCUMENTS")
    private String collateDocuments;

    @Column(name = "TARGET_PICK_RATE")
    private int targetPickRate = 0;

    @Column(name = "PROCESS_PAD")
    private int processPad = 0;

    @Column(name = "BATCH_LIMIT")
    private int batchLimit = 0;
    @Column(name = "TOTE_LIMIT")
    private int toteLimit = 0;
    @Column(name = "IS_HOTPICK")
    private Boolean isHotpick = false;
    @ManyToOne(optional = false)
    @JoinColumn(name = "PICKDESTINATION_ID")
    private PickStationType pickStationType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "REBINDESTINATION_ID")
    private ReBinStationType reBinDestination;

    @ManyToOne(optional = false)
    @JoinColumn(name = "REBINWALLTYPE_ID")
    private ReBinWallType reBinWallType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PACKDESTINATION_ID")
    private PackingStationType packDestination;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID")
    private ProcessPathType processPathType;
    @ManyToMany
    @OrderBy("username")
    @JoinTable(
            name = "OB_PICKINGPROCESSELIGIBILITY",
            joinColumns = @JoinColumn(name = "PROCESSPATH_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    private List<User> users = new ArrayList<>();

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

    public String getRegenerateShortedPicks() {
        return regenerateShortedPicks;
    }

    public void setRegenerateShortedPicks(String regenerateShortedPicks) {
        this.regenerateShortedPicks = regenerateShortedPicks;
    }

    public int getMinShipmentsPerBatch() {
        return minShipmentsPerBatch;
    }

    public void setMinShipmentsPerBatch(int minShipmentsPerBatch) {
        this.minShipmentsPerBatch = minShipmentsPerBatch;
    }

    public int getMaxShipmentsPerBatch() {
        return maxShipmentsPerBatch;
    }

    public void setMaxShipmentsPerBatch(int maxShipmentsPerBatch) {
        this.maxShipmentsPerBatch = maxShipmentsPerBatch;
    }

    public int getMinItemsPerBatch() {
        return minItemsPerBatch;
    }

    public void setMinItemsPerBatch(int minItemsPerBatch) {
        this.minItemsPerBatch = minItemsPerBatch;
    }

    public int getMaxItemsPerBatch() {
        return maxItemsPerBatch;
    }

    public void setMaxItemsPerBatch(int maxItemsPerBatch) {
        this.maxItemsPerBatch = maxItemsPerBatch;
    }

    public String getCollateDocuments() {
        return collateDocuments;
    }

    public void setCollateDocuments(String collateDocuments) {
        this.collateDocuments = collateDocuments;
    }

    public ReBinStationType getReBinDestination() {
        return reBinDestination;
    }

    public void setReBinDestination(ReBinStationType reBinDestination) {
        this.reBinDestination = reBinDestination;
    }

    public ReBinWallType getReBinWallType() {
        return reBinWallType;
    }

    public void setReBinWallType(ReBinWallType reBinWallType) {
        this.reBinWallType = reBinWallType;
    }

    public PackingStationType getPackDestination() {
        return packDestination;
    }

    public void setPackDestination(PackingStationType packDestination) {
        this.packDestination = packDestination;
    }

    public ProcessPathType getProcessPathType() {
        return processPathType;
    }

    public void setProcessPathType(ProcessPathType processPathType) {
        this.processPathType = processPathType;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getTargetPickRate() {
        return targetPickRate;
    }

    public void setTargetPickRate(int targetPickRate) {
        this.targetPickRate = targetPickRate;
    }

    public int getProcessPad() {
        return processPad;
    }

    public void setProcessPad(int processPad) {
        this.processPad = processPad;
    }


    public int getBatchLimit() {
        return batchLimit;
    }

    public void setBatchLimit(int batchLimit) {
        this.batchLimit = batchLimit;
    }

    public PickStationType getPickStationType() {
        return pickStationType;
    }

    public void setPickStationType(PickStationType pickStationType) {
        this.pickStationType = pickStationType;
    }

    public Boolean getHotpick() {
        return isHotpick;
    }

    public void setHotpick(Boolean hotpick) {
        isHotpick = hotpick;
    }

    public int getToteLimit() {
        return toteLimit;
    }

    public void setToteLimit(int toteLimit) {
        this.toteLimit = toteLimit;
    }

    public String getPickWay() {
        return pickWay;
    }

    public void setPickWay(String pickWay) {
        this.pickWay = pickWay;
    }
}
