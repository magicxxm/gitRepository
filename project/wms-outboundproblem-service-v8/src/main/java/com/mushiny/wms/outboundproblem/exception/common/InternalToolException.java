package com.mushiny.wms.outboundproblem.exception.common;

public enum InternalToolException {

    EX_AMOUNT_IS_ZERO("数量不允许为零"),

//    EX_IT_STORAGE_LOCATION_NOT_USED,//{0}容器不容许操作！
//
//    EX_IT_USERNAME_NOT_FOUND,//{0}用户不存在！

    EX_IT_STORAGE_LOCATION_NOT_FOUND("容器不存在"),

    EX_IT_SKU_NOT_FOUND("商品条码不存在"),

//    EX_IT_SKU_HAS_MORE_ITEM_DATA,//{0}为多条码商品！

    EX_IT_STORAGE_LOCATION_NOT_FOUND_SKU("容器中不存在商品"),

//    EX_IT_STORAGE_LOCATION_SKU_DIFFERENT_CLIENT,//{0}中存在不同客户的相同商品！
//
//    EX_IT_STORAGE_LOCATION_SKU_DIFFERENT_LOT,//{0}中存在不同有效期的相同商品！

    EX_IT_AMOUNT_MORE_THAN_SYSTEM_AMOUNT("移货数量超过系统值");

//    EX_IT_STORAGE_LOCATION_SKU_ITEMS_MAX_AMOUNT,// {0}中商品种类超过系统设置数量
//
//    EX_IT_STORAGE_LOCATION_SKU_DIFFERENT_ITEM_GROUP,// {0}与商品种类不符合
//
//    EX_IT_INVENTORY_STATE_IS_DIFFERENT,// 修改库存属性与目的容器不符合！

    private String name;//定义自定义的变量

    InternalToolException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
