package com.mushiny.wms.internaltool.common.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "MD_ITEMDATA", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "ITEM_NO", "CLIENT_ID", "WAREHOUSE_ID"
        })
})
public class ItemData extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "ITEM_NO", nullable = false)
    private String itemNo;

    @Column(name = "SKU_NO", nullable = false)
    private String skuNo;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SAFETY_STOCK", nullable = false)
    private int safetyStock = 0;

    @Column(name = "LOT_MANDATORY", nullable = false)
    private boolean lotMandatory;

    @Column(name = "LOT_TYPE")
    private String lotType;

    @Column(name = "LOT_UNIT")
    private String lotUnit;

    @Column(name = "LOT_THRESHOLD")
    private int lotThreshold;

    @Column(name = "SERIAL_RECORD_TYPE", nullable = false)
    private String serialRecordType;

    @Column(name = "SERIAL_RECORD_LENGTH")
    private int serialRecordLength = 0;

    @Column(name = "HEIGHT")
    private BigDecimal height;

    @Column(name = "WIDTH")
    private BigDecimal width;

    @Column(name = "DEPTH")
    private BigDecimal depth;

    @Column(name = "VOLUME")
    private BigDecimal volume;
    @Column(name = "SIZE")
    private String size;
    @Column(name = "WEIGHT")
    private BigDecimal weight;

    @Column(name = "MULTIPLE_PART", nullable = false)
    private boolean multiplePart;

    @Column(name = "MULTIPLE_PART_AMOUNT")
    private Integer multiplePartAmount;

    @Column(name = "MEASURED")
    private boolean measured;

    @Column(name = "PREFER_OWNBOX", nullable = false)
    private boolean preferOwnBox;

    @Column(name = "PREFER_BAG", nullable = false)
    private boolean preferBag;

    @Column(name = "USE_BUBBLEFILM", nullable = false)
    private boolean useBubbleFilm;

    @Column(name = "ITEMDATAGLOBAL_ID", nullable = false)
    private String itemDataGlobalId;

    @ManyToOne
    @JoinColumn(name = "HANDLINGUNIT_ID")
    private ItemUnit itemUnit;

    @Column(name = "ITEMSELLINGDEGREE")
    private String itemSellingDegree;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEMGROUP_ID")
    private ItemGroup itemGroup;

    @ManyToOne
    @JoinColumn(name = "CLIENT_ID")
    private Client client;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

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

    public ItemGroup getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(ItemGroup itemGroup) {
        this.itemGroup = itemGroup;
    }

    public ItemUnit getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(ItemUnit itemUnit) {
        this.itemUnit = itemUnit;
    }

    public String getItemSellingDegree() {
        return itemSellingDegree;
    }

    public void setItemSellingDegree(String itemSellingDegree) {
        this.itemSellingDegree = itemSellingDegree;
    }

    public String getLotType() {
        return lotType;
    }

    public void setLotType(String lotType) {
        this.lotType = lotType;
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

    public int getSerialRecordLength() {
        return serialRecordLength;
    }

    public void setSerialRecordLength(int serialRecordLength) {
        this.serialRecordLength = serialRecordLength;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
