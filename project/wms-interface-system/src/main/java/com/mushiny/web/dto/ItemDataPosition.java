package com.mushiny.web.dto;

/**
 * Created by 123 on 2018/2/23.
 */
public class ItemDataPosition {

    private String itemNo;//商品唯一码

//    private String skuNo;//商品条码

    private String name;//商品名称

    private String description;//描述

    private String unit;// 商品单位

    private String length;//长

    private String width;//宽

    private String height;//高

    private String volume;//体积

    private String weight;//毛重

    private String shelflife;//商品保质期

    private String hasLot;//批号标识

    private String serialNumber;//序列号管理

    private String attributes;//商品属性

    private String category;//商品类别

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getShelflife() {
        return shelflife;
    }

    public void setShelflife(String shelflife) {
        this.shelflife = shelflife;
    }

    public String getHasLot() {
        return hasLot;
    }

    public void setHasLot(String hasLot) {
        this.hasLot = hasLot;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
}
