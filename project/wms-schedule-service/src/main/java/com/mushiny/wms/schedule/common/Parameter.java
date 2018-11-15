package com.mushiny.wms.schedule.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Parameter {

    @Value("${wms.parameter.stowLogicStation}")
    private String stowLogicStation;

    @Value("${wms.parameter.stowWorkStation}")
    private String stowWorkStation;

    @Value("${wms.parameter.userName}")
    private String userName;

    @Value("${wms.parameter.warehouse}")
    private String warehouse;

    @Value("${wms.url.url_releasePod}")
    private String releasePodUrl;

    @Value("${wms.url.url_refreshPod}")
    private String refreshPodUrl;

    @Value("${wms.parameter.time}")
    private String time;

    @Value("${wms.parameter.amount}")
    private String amount;

    @Value("${wms.parameter.client}")
    private String client;

    @Value("${wms.parameter.eachStowAmount}")
    private String eachStowAmount;

    @Value("${wms.parameter.pickLogicStation}")
    private String pickLogicStation;

    @Value("${wms.parameter.pickWorkStation}")
    private String pickWorkStation;

    @Value("${wms.parameter.packWorkStation}")
    private String packWorkStation;

    @Value("${wms.parameter.packLogicStation}")
    private String packLogicStation;

    @Value("${wms.url.url_pickConfirm}")
    private String pickConfirm;

    @Value("${wms.url.url_scanItemNo}")
    private String scanItemNo;

    @Value("${wms.url.url_pickOrderPosition}")
    private String pickOrderPosition;

    @Value("${wms.url.url_pick_pack_cell}")
    private String pickPackCell;

    @Value("${wms.url.url_shipment_position}")
    private String shipmentPosition;

    @Value("${wms.url.url_shipment_weight}")
    private String shipmentWeight;

    @Value("${wms.url.url_item_scanData}")
    private String itemScanData;

    @Value("${wms.url.url_item_confirm}")
    private String itemConfirm;

    @Value("${wms.url.url_SN_scan}")
    private String scanSn;

    @Value("${wms.url.url_scanBox_finished}")
    private String scanBox;
            ;
    public String getStowLogicStation() {
        return stowLogicStation;
    }

    public void setStowLogicStation(String stowLogicStation) {
        this.stowLogicStation = stowLogicStation;
    }

    public String getStowWorkStation() {
        return stowWorkStation;
    }

    public void setStowWorkStation(String stowWorkStation) {
        this.stowWorkStation = stowWorkStation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getEachStowAmount() {
        return eachStowAmount;
    }

    public void setEachStowAmount(String eachStowAmount) {
        this.eachStowAmount = eachStowAmount;
    }

    public String getReleasePodUrl() {
        return releasePodUrl;
    }

    public void setReleasePodUrl(String releasePodUrl) {
        this.releasePodUrl = releasePodUrl;
    }

    public String getRefreshPodUrl() {
        return refreshPodUrl;
    }

    public void setRefreshPodUrl(String refreshPodUrl) {
        this.refreshPodUrl = refreshPodUrl;
    }

    public String getPickLogicStation() {
        return pickLogicStation;
    }

    public void setPickLogicStation(String pickLogicStation) {
        this.pickLogicStation = pickLogicStation;
    }

    public String getPickWorkStation() {
        return pickWorkStation;
    }

    public void setPickWorkStation(String pickWorkStation) {
        this.pickWorkStation = pickWorkStation;
    }

    public String getPackWorkStation() {
        return packWorkStation;
    }

    public void setPackWorkStation(String packWorkStation) {
        this.packWorkStation = packWorkStation;
    }

    public String getPackLogicStation() {
        return packLogicStation;
    }

    public void setPackLogicStation(String packLogicStation) {
        this.packLogicStation = packLogicStation;
    }

    public String getPickConfirm() {
        return pickConfirm;
    }

    public void setPickConfirm(String pickConfirm) {
        this.pickConfirm = pickConfirm;
    }

    public String getScanItemNo() {
        return scanItemNo;
    }

    public void setScanItemNo(String scanItemNo) {
        this.scanItemNo = scanItemNo;
    }

    public String getPickOrderPosition() {
        return pickOrderPosition;
    }

    public void setPickOrderPosition(String pickOrderPosition) {
        this.pickOrderPosition = pickOrderPosition;
    }

    public String getPickPackCell() {
        return pickPackCell;
    }

    public void setPickPackCell(String pickPackCell) {
        this.pickPackCell = pickPackCell;
    }

    public String getShipmentPosition() {
        return shipmentPosition;
    }

    public void setShipmentPosition(String shipmentPosition) {
        this.shipmentPosition = shipmentPosition;
    }

    public String getShipmentWeight() {
        return shipmentWeight;
    }

    public void setShipmentWeight(String shipmentWeight) {
        this.shipmentWeight = shipmentWeight;
    }

    public String getItemScanData() {
        return itemScanData;
    }

    public void setItemScanData(String itemScanData) {
        this.itemScanData = itemScanData;
    }

    public String getItemConfirm() {
        return itemConfirm;
    }

    public void setItemConfirm(String itemConfirm) {
        this.itemConfirm = itemConfirm;
    }

    public String getScanSn() {
        return scanSn;
    }

    public void setScanSn(String scanSn) {
        this.scanSn = scanSn;
    }

    public String getScanBox() {
        return scanBox;
    }

    public void setScanBox(String scanBox) {
        this.scanBox = scanBox;
    }
}
