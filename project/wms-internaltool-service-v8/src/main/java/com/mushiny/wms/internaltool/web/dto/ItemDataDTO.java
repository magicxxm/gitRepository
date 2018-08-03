package com.mushiny.wms.internaltool.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 123 on 2017/11/9.
 */
public class ItemDataDTO implements Serializable {
    @NotNull
    private String name;

    private String description;

    @NotNull
    private int safetyStock = 0;

    private String itemNo;

    private String skuNo;

    private boolean adviceMandatory;

    private boolean lotMandatory;

    private String lotSubstitutionType;

    private String serialRecordType;

    private int serialRecordLength = 0;

    private BigDecimal height;

    private BigDecimal width;

    private BigDecimal depth;

    private BigDecimal volume;

    private BigDecimal weight;

    private boolean multiplePart;

    private Integer multiplePartAmount;

    private boolean measured;

    private boolean preferOwnBox;

    private boolean preferBag;

    private boolean useBubbleFilm;

    private String itemDataGlobalId;

    private String lotType;

    private String lotUnit;

    private int lotThreshold;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String itemGroupId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String handlingUnitId;

    //    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private String itemSellingDegreeId;
    private String itemSellingDegree;

//    private ItemGroupDTO itemGroup;
//
//    private ItemUnitDTO itemUnit;

//    private ItemSellingDegreeDTO itemSellingDegree;

    private String clientName;

    private String warehouse;

    public ItemDataDTO() {
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
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

    public boolean isAdviceMandatory() {
        return adviceMandatory;
    }

    public void setAdviceMandatory(boolean adviceMandatory) {
        this.adviceMandatory = adviceMandatory;
    }

    public boolean isLotMandatory() {
        return lotMandatory;
    }

    public void setLotMandatory(boolean lotMandatory) {
        this.lotMandatory = lotMandatory;
    }

    public String getLotSubstitutionType() {
        return lotSubstitutionType;
    }

    public void setLotSubstitutionType(String lotSubstitutionType) {
        this.lotSubstitutionType = lotSubstitutionType;
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

    public String getItemGroupId() {
        return itemGroupId;
    }

    public void setItemGroupId(String itemGroupId) {
        this.itemGroupId = itemGroupId;
    }

    public String getHandlingUnitId() {
        return handlingUnitId;
    }

    public void setHandlingUnitId(String handlingUnitId) {
        this.handlingUnitId = handlingUnitId;
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
}
