package com.mushiny.wms.outboundproblem.domain.common;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.outboundproblem.domain.OBProblem;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MD_ITEMDATA", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "ITEM_NO", "CLIENT_ID", "WAREHOUSE_ID"
        })
})
public class ItemData extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "ITEM_NO")
    private String itemNo;

    @Column(name = "SKU_NO")
    private String skuNo;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SAFETY_STOCK")
    private int safetyStock = 0;

    @Column(name = "LOT_MANDATORY")
    private boolean lotMandatory;

    @Column(name = "SERIAL_RECORD_TYPE")
    private String serialRecordType;

    @Column(name = "LOT_TYPE")
    private String lotType;

    @Column(name = "HEIGHT")
    private BigDecimal height;

    @Column(name = "WIDTH")
    private BigDecimal width;

    @Column(name = "DEPTH")
    private BigDecimal depth;

    @Column(name = "VOLUME")
    private BigDecimal volume;

    @Column(name = "WEIGHT")
    private BigDecimal weight;

    @Column(name = "MULTIPLE_PART")
    private boolean multiplePart;

    @Column(name = "MULTIPLE_PART_AMOUNT")
    private Integer multiplePartAmount;

    @Column(name = "MEASURED")
    private boolean measured;

    @Column(name = "PREFER_OWNBOX")
    private boolean preferOwnBox;

    @Column(name = "PREFER_BAG")
    private boolean preferBag;

    @Column(name = "USE_BUBBLEFILM")
    private boolean useBubbleFilm;

    @Column(name = "ITEMDATAGLOBAL_ID")
    private String itemDataGlobalId;

    @OrderBy("serialNo")
    @OneToMany(mappedBy = "itemData", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<ItemDataSerialNumber> serialNos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "ITEMGROUP_ID")
    private ItemGroup itemGroup;

    @Column(name = "SERIAL_RECORD_LENGTH")
    private int serialRecordLength = 0;

//    @ManyToOne
//    @JoinColumn(name = "HANDLINGUNIT_ID")
//    private ItemUnit itemUnit;

    @Column(name = "ITEMSELLINGDEGREE")
    private String itemSellingDegree;

    @Column(name = "LOT_UNIT")
    private String lotUnit;

    @Column(name = "LOT_THRESHOLD")
    private int lotThreshold;

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

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

    public int getSafetyStock() {
        return safetyStock;
    }

    public void setSafetyStock(int safetyStock) {
        this.safetyStock = safetyStock;
    }

    public boolean isLotMandatory() {
        return lotMandatory;
    }

    public void setLotMandatory(boolean lotMandatory) {
        this.lotMandatory = lotMandatory;
    }

    public String getSerialRecordType() {
        return serialRecordType;
    }

    public void setSerialRecordType(String serialRecordType) {
        this.serialRecordType = serialRecordType;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getDepth() {
        return depth;
    }

    public void setDepth(BigDecimal depth) {
        this.depth = depth;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public boolean isMultiplePart() {
        return multiplePart;
    }

    public void setMultiplePart(boolean multiplePart) {
        this.multiplePart = multiplePart;
    }

    public Integer getMultiplePartAmount() {
        return multiplePartAmount;
    }

    public void setMultiplePartAmount(Integer multiplePartAmount) {
        this.multiplePartAmount = multiplePartAmount;
    }

    public boolean isMeasured() {
        return measured;
    }

    public void setMeasured(boolean measured) {
        this.measured = measured;
    }

    public boolean isPreferOwnBox() {
        return preferOwnBox;
    }

    public void setPreferOwnBox(boolean preferOwnBox) {
        this.preferOwnBox = preferOwnBox;
    }

    public boolean isPreferBag() {
        return preferBag;
    }

    public void setPreferBag(boolean preferBag) {
        this.preferBag = preferBag;
    }

    public boolean isUseBubbleFilm() {
        return useBubbleFilm;
    }

    public void setUseBubbleFilm(boolean useBubbleFilm) {
        this.useBubbleFilm = useBubbleFilm;
    }

    public String getItemDataGlobalId() {
        return itemDataGlobalId;
    }

    public void setItemDataGlobalId(String itemDataGlobalId) {
        this.itemDataGlobalId = itemDataGlobalId;
    }

    public String getLotType() {
        return lotType;
    }

    public void setLotType(String lotType) {
        this.lotType = lotType;
    }

    public ItemGroup getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(ItemGroup itemGroup) {
        this.itemGroup = itemGroup;
    }

    public int getSerialRecordLength() {
        return serialRecordLength;
    }

    public String getItemSellingDegree() {
        return itemSellingDegree;
    }

    public void setItemSellingDegree(String itemSellingDegree) {
        this.itemSellingDegree = itemSellingDegree;
    }

    public void setSerialRecordLength(int serialRecordLength) {
        this.serialRecordLength = serialRecordLength;

    }

    public String getLotUnit() {
        return lotUnit;
    }

    public void setLotUnit(String lotUnit) {
        this.lotUnit = lotUnit;
    }

    public int getLotThreshold() {
        return lotThreshold;
    }

    public void setLotThreshold(int lotThreshold) {
        this.lotThreshold = lotThreshold;
    }

    public List<ItemDataSerialNumber> getSerialNos() {
        return serialNos;
    }

    public void setSerialNos(List<ItemDataSerialNumber> serialNos) {
        this.serialNos = serialNos;
    }
}
