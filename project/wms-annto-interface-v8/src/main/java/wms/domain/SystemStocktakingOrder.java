package wms.domain;

import wms.common.entity.BaseClientAssignedEntity;
import wms.domain.common.Pod;
import wms.domain.common.StockUnit;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by 123 on 2017/8/30.
 */
@Entity
@Table(name = "ICQA_SYSTEM_STOCKTAKINGORDER")
public class SystemStocktakingOrder extends BaseClientAssignedEntity{

    @Column(name = "AMOUNT_SYSTEM")
    private BigDecimal amountSystem;

    @Column(name = "AMONUT_CHECK")
    private BigDecimal amountCheck;

    @Column(name = "STATE")
    private int state;

    @ManyToOne
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;

    @Column(name = "FROMLOCATION_NAME")
    private String fromLocationName;

    @ManyToOne
    @JoinColumn(name = "FROMSTOCKUNIT_ID")
    private StockUnit stockUnit;

    @ManyToOne
    @JoinColumn(name = "STOCKTAKINGPOSITION_ID")
    private SystemStocktakingPosition position;

    @ManyToOne
    @JoinColumn(name = "STOCKTAKING_ID")
    private SystemStocktaking systemStocktaking;

    @Column(name = "STOCK_INDEX")
    private int stockIndex;

    @ManyToOne
    @JoinColumn(name = "POD_ID")
    private Pod pod;

    @Column(name = "OPERATOR")
    private String operator;

    @Column(name = "STOCKTAKING_TIME")
    private LocalDateTime stocktakingTime;

    @Column(name = "STOCKTAKINGSTATION")
    private String stocktakingStation;

    @Column(name = "BATCH")
    private String batch;

    @Column(name = "LOT_NUMBER")
    private String lotNumber;

    public BigDecimal getAmountSystem() {
        return amountSystem;
    }

    public void setAmountSystem(BigDecimal amountSystem) {
        this.amountSystem = amountSystem;
    }

    public BigDecimal getAmountCheck() {
        return amountCheck;
    }

    public void setAmountCheck(BigDecimal amountCheck) {
        this.amountCheck = amountCheck;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }

    public String getFromLocationName() {
        return fromLocationName;
    }

    public void setFromLocationName(String fromLocationName) {
        this.fromLocationName = fromLocationName;
    }

    public StockUnit getStockUnit() {
        return stockUnit;
    }

    public void setStockUnit(StockUnit stockUnit) {
        this.stockUnit = stockUnit;
    }

    public SystemStocktakingPosition getPosition() {
        return position;
    }

    public void setPosition(SystemStocktakingPosition position) {
        this.position = position;
    }

    public SystemStocktaking getSystemStocktaking() {
        return systemStocktaking;
    }

    public void setSystemStocktaking(SystemStocktaking systemStocktaking) {
        this.systemStocktaking = systemStocktaking;
    }

    public int getStockIndex() {
        return stockIndex;
    }

    public void setStockIndex(int stockIndex) {
        this.stockIndex = stockIndex;
    }

    public Pod getPod() {
        return pod;
    }

    public void setPod(Pod pod) {
        this.pod = pod;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public LocalDateTime getStocktakingTime() {
        return stocktakingTime;
    }

    public void setStocktakingTime(LocalDateTime stocktakingTime) {
        this.stocktakingTime = stocktakingTime;
    }

    public String getStocktakingStation() {
        return stocktakingStation;
    }

    public void setStocktakingStation(String stocktakingStation) {
        this.stocktakingStation = stocktakingStation;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }
}
