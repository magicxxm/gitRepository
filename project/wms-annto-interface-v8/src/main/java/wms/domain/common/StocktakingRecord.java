package wms.domain.common;


import wms.common.entity.BaseWarehouseAssignedEntity;
import wms.domain.StocktakingOrder;

import javax.persistence.*;

@Entity
@Table(name = "ICQA_STOCKTAKINGRECORD")
public class StocktakingRecord extends BaseWarehouseAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "TIMES")
    private int times;

    @Column(name = "STATE", nullable = false)
    private String state;

    @Column(name = "COUNTED_QUANTITY", nullable = false)
    private int countedQuantity;

    @ManyToOne
    @JoinColumn(name = "STOCKUNIT_ID")
    private StockUnit stockUnit;

    @Column(name = "ITEM_NO")
    private String itemNo;

    @Column(name = "LOT_NO")
    private String lotNo;

    @Column(name = "LOCATION_NAME", nullable = false)
    private String locationName;

    @Column(name = "PLANNED_QUANTITY")
    private int plannedQuantity;

    @Column(name = "SERIAL_NO")
    private String serialNo;

    @Column(name = "ULTYPE_NO")
    private String ulTypeNo;

    @Column(name = "UNITLOAD_LABEL")
    private String unitLoadLabel;

    @Column(name = "CLIENT_NO")
    private String clientNo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "STOCKTAKINGORDER_ID")
    private StocktakingOrder stocktakingOrder;

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getCountedQuantity() {
        return countedQuantity;
    }

    public void setCountedQuantity(int countedQuantity) {
        this.countedQuantity = countedQuantity;
    }

    public StockUnit getStockUnit() {
        return stockUnit;
    }

    public void setStockUnit(StockUnit stockUnit) {
        this.stockUnit = stockUnit;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getPlannedQuantity() {
        return plannedQuantity;
    }

    public void setPlannedQuantity(int plannedQuantity) {
        this.plannedQuantity = plannedQuantity;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getUlTypeNo() {
        return ulTypeNo;
    }

    public void setUlTypeNo(String ulTypeNo) {
        this.ulTypeNo = ulTypeNo;
    }

    public String getUnitLoadLabel() {
        return unitLoadLabel;
    }

    public void setUnitLoadLabel(String unitLoadLabel) {
        this.unitLoadLabel = unitLoadLabel;
    }

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public StocktakingOrder getStocktakingOrder() {
        return stocktakingOrder;
    }

    public void setStocktakingOrder(StocktakingOrder stocktakingOrder) {
        this.stocktakingOrder = stocktakingOrder;
    }
}
