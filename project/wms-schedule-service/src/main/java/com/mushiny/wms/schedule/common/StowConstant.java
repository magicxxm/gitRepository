package com.mushiny.wms.schedule.common;

public class StowConstant {
    public static final String ReceiveStationValidCode = "0";
    public static final String ReceiveStationValidCodeWithNoData = "1";
    public static final String StorageLocationValidCodeWithData = "2";
    public static final String ReceiveStationInvalidCode = "-1";
    public static final String CIPERNUM  = "车牌号码";
    public static final String SKUSEEMTOOHIGH = "货位中存在高相似度商品,请重新扫描货位";
    public static final String CLIENTNOTSEEM = "该货位存在不同供应商商品,请重新扫描";
    public static final String STORAGECATALOGNOTSEEM = "该货位商品属性与货位属性不符,请重新扫描";
    public static final String STORAGEBEYOUNGMAXCATALOG = "已超过货位商品种类最大值,请重新扫描";
    public static final String STORAGEBEYOUNGMAXWERIGHT = "已超过货位承载最大值,请重新扫描";
    public static final String STORAGEUNVALUE = "货位无效";
}
