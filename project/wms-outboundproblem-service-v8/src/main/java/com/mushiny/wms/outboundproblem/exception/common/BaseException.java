package com.mushiny.wms.outboundproblem.exception.common;

public enum BaseException {

    /**
     * IN BOUND ENTITY EXCEPTION
     **/

    /*EX_RECEIVING_CATEGORY_NAME_UNIQUE,// 过滤规则:{0}已存在!

    EX_RECEIVING_CATEGORY_RULE_NAME_UNIQUE,// 规则:{0}已存在!

    EX_RECEIVING_DESTINATION_NAME_UNIQUE,// 目的地:{0}已存在!

    EX_RECEIVING_STATION_NAME_UNIQUE,// 工作站:{0}已存在!

    EX_RECEIVING_STATION_TYPE_NAME_UNIQUE,// 工作站类型:{0}已存在!

    EX_ITEM_DATA_TYPE_GRADE_STATS_ITEM_DATA_UNIQUE,// SKU:{0}已存在!

    EX_REPLENISH_STRATEGY_UNIQUE,// 补充策略已存在!*/

    /**
     * IN BOUND RECEIVE AND STOW EXCEPTION
     **/
//    EX_AMOUNT_IS_ZERO,// 数量不容许为零！

    /*EX_SN_IS_NULL, // SN不容许为空！
    EX_SN_HAS_USED,// SN:{0}已经存在,请自己核查！

    EX_SKU_NOT_FOUND,// SKU:{0}不存在！
    EX_SKU_NOT_CUBI_SCAN,// 商品:{0},没有测量！

    EX_DN_SKU_NOT_FOUND,// DN:{0}中不存在SKU:{1}！
    EX_DN_HAS_DELETED,// DN:{0}已被标记为删除！
    EX_DN_HAS_ACTIVATED,// DN:{0}已激活！
    EX_DN_NOT_ACTIVATED,// DN:{0}没有激活！

    EX_RECEIVING_STATION_HAS_DELETED,// 工作站:{0}已被标记为删除！
    EX_RECEIVING_STATION_CONTAINER_AMOUNT_MAX,// 工作站:{0}绑定容器已经达到最大值！

    EX_RECEIVING_DESTINATION_HAS_DELETED,// 目的地:{0}已被标记为删除！
    EX_RECEIVING_DESTINATION_HAS_USED,// 工作站已绑定{0}目的地！
    EX_RECEIVING_DESTINATION_NOT_FOUND_IN_POSITION,// 工作站不能绑定{0}目的地

    EX_RECEIVING_CATEGORY_NOT_FOUND,// 客户:{0}没有设定收货规则！
    EX_REPLENISH_STRATEGY_NOT_FOUND,// 客户:{0}的补充策略不存在！

    EX_STORAGE_LOCATION_NOT_IS_DAMAGE,// 货位无效：{0}，货位不是残品货位！
    EX_STORAGE_LOCATION_NOT_IS_INVENTORY,// 货位无效：{0}，货位不是存货货位！


    EX_STORAGE_LOCATION_SKU_DIFFERENT_LOT,// 货位无效:{0},存在相同商品不同有效期商品!

    EX_STORAGE_LOCATION_SKU_DIFFERENT_ITEM_GROUP,// 货位无效：{0}，货位和商品类型不符
    EX_STORAGE_LOCATION_SKU_ITEMS_MAX_AMOUNT,// 货位无效：{0}，货位中商品种类超过系统设置数量

    EX_CONTAINER_IS_NOT_EMPTY_STATE,// 容器{0}正在使用！
    EX_CONTAINER_NOT_IS_RECEIVE,// 收货容器{0}类型不属于残品容器！
    EX_CONTAINER_NOT_IS_STOW,// 容器{0}不能进行上架，请重新扫描新的车牌上架！
    EX_CONTAINER_NOT_IS_DAMAGE,// 容器{0}类型不属于残品容器！
    EX_CONTAINER_NOT_IS_CUBI_SCAN,// 容器{0}类型不属于带测量容器！
    EX_CUBI_SCAN_CONTAINER_HAS_THIS_SKU,// 待扫描容器{0}中已经存在SKU:{1}的商品！

    EX_CONTAINER_SKU_AMOUNT_IS_ZERO,// 容器：{0},中商品{0}已经全部上架完成！
    EX_CONTAINER_SKU_AMOUNT_IS_NOT_ZERO,// 容器：{0},存在货物！
    EX_CONTAINER_SKU_NOT_FOUND,// 商品条码无效：{0}！
    */

    EX_LOT_ERROR("有效期错误"),

    EX_STORAGE_LOCATION_SKU_DIFFERENT_CLIENT("不同客户的相同商品是不容许放入同一个货位中"),

    EX_CONTAINER_SKU_DIFFERENT_CLIENT("EX_CONTAINER_SKU_DIFFERENT_CLIENT"),

    EX_CONTAINER_SKU_DIFFERENT_LOT("EX_CONTAINER_SKU_DIFFERENT_LOT"),

    EX_STORAGE_LOCATION_SKU_CLIENT_DIFFERENT("货位与商品的客户不符"),

    EX_SCANNING_OBJECT_NOT_FOUND("扫描对象不存在"),

    EX_AMOUNT_ERROR("数量错误,请重新核实"),

    EX_LOT_ITEM_DATA_NOT_FOUND_DATE("此商品为有效期商品,但尚未录入有效期,请将商品放回原车牌");

    private String name;//定义自定义的变量

    BaseException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
