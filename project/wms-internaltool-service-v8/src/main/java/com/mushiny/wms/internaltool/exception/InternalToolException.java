package com.mushiny.wms.internaltool.exception;

public enum InternalToolException {

    EX_IT_UNITLOAD_IS_LOCKED("容器已被问题组标记"),//{0}已被锁定！

    EX_AMOUNT_IS_ZERO("数量不允许为0"),// 数量不容许为零！

    EX_IT_ITEMDATA_SN_HAS_USED("SN已被使用"),//{0}SN已经存在！

    EX_IT_ITEMDATA_SN_ERROR("SN错误"),//{0}SN非法！

    EX_IT_STORAGE_LOCATION_DIFFERENT_INVENTORY_STATE("容器属性不符合"),// 容器属性不符合！

    EX_IT_STORAGE_LOCATION_IS_MEASURED("容器不能为测量容器"),// {0}容器不能为测量容器！

    EX_IT_STORAGE_LOCATION_NOT_USED("容器不容许操作"),//{0}容器不容许操作！

    EX_IT_USERNAME_NOT_FOUND("用户不存在"),//{0}用户不存在！

    EX_IT_STORAGE_LOCATION_NOT_FOUND("容器不存在"),//{0}不存在！

    EX_IT_SKU_NOT_FOUND("商品条码不存在"),//{0}不存在！

    EX_IT_SKU_HAS_MORE_ITEM_DATA("多条码商品"),//{0}为多条码商品！

    EX_IT_STORAGE_LOCATION_NOT_FOUND_SKU("容器中不存在此条码商品"),//{0}中不存在商品{1}！

    EX_IT_STORAGE_LOCATION_SKU_DIFFERENT_CLIENT("容器中存在不同客户的相同商品"),//{0}中存在不同客户的相同商品！

    EX_IT_STORAGE_LOCATION_SKU_DIFFERENT_LOT("容器中存在不同有效期的相同商品"),//{0}中存在不同有效期的相同商品！

    EX_IT_AMOUNT_MORE_THAN_SYSTEM_AMOUNT("移货数量超过系统设置"),// 数量超过系统设置！

    EX_IT_STORAGE_LOCATION_SKU_ITEMS_MAX_AMOUNT("容器中商品种类超过系统设置数量"),// {0}中商品种类超过系统设置数量

    EX_IT_STORAGE_LOCATION_WEIGHT_HAS_MAX("容器载重量超过系统设置最大值"),// {0}载重量超过系统设置数量

    EX_IT_STORAGE_LOCATION_SKU_DIFFERENT_ITEM_GROUP("容器类型与商品种类不符合"),// {0}与商品种类不符合

    EX_IT_INVENTORY_STATE_IS_DIFFERENT("修改库存属性与目的容器不符合"),// 修改库存属性与目的容器不符合！

    EX_SEMBLENCE_SKU("货位中存在高相似度商品,请重新扫描目的容器");

    private String name;

    public String getName() {
        return name;
    }
    InternalToolException(String name){
        this.name=name;

    }
}
