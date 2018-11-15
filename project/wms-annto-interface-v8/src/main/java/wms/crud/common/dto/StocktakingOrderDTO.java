package wms.crud.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import wms.common.crud.dto.BaseWarehouseAssignedDTO;
import wms.domain.StocktakingOrder;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

public class StocktakingOrderDTO extends BaseWarehouseAssignedDTO {

    private static final long serialVersionUID = 1L;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stocktakingId;

    private StocktakingDTO stocktaking;

    private String areaName;

    private LocalDateTime countingDate;

    private String locationName;

    private String parameter;

    private String operator;

    private String state;

    private Integer times;

    private String unitLoadLabel;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String StocktakingRuleId;

//    private StocktakingRuleDTO stocktakingRule;

    public StocktakingOrderDTO() {
    }
    public StocktakingOrderDTO(StocktakingOrder entity) {
        super(entity);
    }
    private String StocktakingName;

    private BigDecimal amount;

    private String color;

    private String countinDate;

    private Integer bayIndex;

    private String storageLocationId;

    private String createdByUser;

    private Integer stockStorageAmount;

    private LocalDateTime createDate;

    private LocalDateTime countDate;

    private BigInteger amount1;

    private BigInteger amount2;

    private BigInteger amount3;

    private BigInteger amount4;

    private String companycode;

    private String locationcode;

    private String itemcode;

    private String itemname;

    private String inventorysts;

    private int systemqty;

    private int countedqty;

    private String countedby;

    private LocalDateTime countedat;

    private LocalDateTime completedat;

    private int adjustqty;

    public String getStocktakingId() {
        return stocktakingId;
    }

    public void setStocktakingId(String stocktakingId) {
        this.stocktakingId = stocktakingId;
    }

    public StocktakingDTO getStocktaking() {
        return stocktaking;
    }

    public void setStocktaking(StocktakingDTO stocktaking) {
        this.stocktaking = stocktaking;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public LocalDateTime getCountingDate() {
        return countingDate;
    }

    public void setCountingDate(LocalDateTime countingDate) {
        this.countingDate = countingDate;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public String getUnitLoadLabel() {
        return unitLoadLabel;
    }

    public void setUnitLoadLabel(String unitLoadLabel) {
        this.unitLoadLabel = unitLoadLabel;
    }

    public String getStocktakingRuleId() {
        return StocktakingRuleId;
    }

    public void setStocktakingRuleId(String stocktakingRuleId) {
        StocktakingRuleId = stocktakingRuleId;
    }

//    public StocktakingRuleDTO getStocktakingRule() {
//        return stocktakingRule;
//    }
//
//    public void setStocktakingRule(StocktakingRuleDTO stocktakingRule) {
//        this.stocktakingRule = stocktakingRule;
//    }

    public String getStocktakingName() {
        return StocktakingName;
    }

    public void setStocktakingName(String stocktakingName) {
        StocktakingName = stocktakingName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCountinDate() {
        return countinDate;
    }

    public void setCountinDate(String countinDate) {
        this.countinDate = countinDate;
    }

    public Integer getBayIndex() {
        return bayIndex;
    }

    public void setBayIndex(Integer bayIndex) {
        this.bayIndex = bayIndex;
    }

    public String getStorageLocationId() {
        return storageLocationId;
    }

    public void setStorageLocationId(String storageLocationId) {
        this.storageLocationId = storageLocationId;
    }

    public String getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    public Integer getStockStorageAmount() {
        return stockStorageAmount;
    }

    public void setStockStorageAmount(Integer stockStorageAmount) {
        this.stockStorageAmount = stockStorageAmount;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getCountDate() {
        return countDate;
    }

    public void setCountDate(LocalDateTime countDate) {
        this.countDate = countDate;
    }

    public BigInteger getAmount1() {
        return amount1;
    }

    public void setAmount1(BigInteger amount1) {
        this.amount1 = amount1;
    }

    public BigInteger getAmount2() {
        return amount2;
    }

    public void setAmount2(BigInteger amount2) {
        this.amount2 = amount2;
    }

    public BigInteger getAmount3() {
        return amount3;
    }

    public void setAmount3(BigInteger amount3) {
        this.amount3 = amount3;
    }

    public BigInteger getAmount4() {
        return amount4;
    }

    public void setAmount4(BigInteger amount4) {
        this.amount4 = amount4;
    }

    public String getCompanycode() {
        return companycode;
    }

    public void setCompanycode(String companycode) {
        this.companycode = companycode;
    }

    public String getLocationcode() {
        return locationcode;
    }

    public void setLocationcode(String locationcode) {
        this.locationcode = locationcode;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getInventorysts() {
        return inventorysts;
    }

    public void setInventorysts(String inventorysts) {
        this.inventorysts = inventorysts;
    }

    public int getSystemqty() {
        return systemqty;
    }

    public void setSystemqty(int systemqty) {
        this.systemqty = systemqty;
    }

    public int getCountedqty() {
        return countedqty;
    }

    public void setCountedqty(int countedqty) {
        this.countedqty = countedqty;
    }

    public String getCountedby() {
        return countedby;
    }

    public void setCountedby(String countedby) {
        this.countedby = countedby;
    }

    public LocalDateTime getCountedat() {
        return countedat;
    }

    public void setCountedat(LocalDateTime countedat) {
        this.countedat = countedat;
    }

    public LocalDateTime getCompletedat() {
        return completedat;
    }

    public void setCompletedat(LocalDateTime completedat) {
        this.completedat = completedat;
    }

    public int getAdjustqty() {
        return adjustqty;
    }

    public void setAdjustqty(int adjustqty) {
        this.adjustqty = adjustqty;
    }
}
