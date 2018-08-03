package com.mushiny.wms.outboundproblem.business.enums;


public enum SolveResoult {

    /**
     * 商品转为正品.
     */
    DAMAGED_TO_NORMAL,

    /**
     * 确认残损.
     */
    CONFIRM_DAMAGED,

    /**
     * 商品丢失.
     */
    GOODS_LOSS,

    /**
     * 商品残损.
     */
    GOODS_DAMAGED,

    /**
     * 条码无法扫描.
     */
    UNABLE_SCAN_SKU,

    /**
     * 序列号无法扫描.
     */
    UNABLE_SCAN_SN,

    /**
     * 已生成拣货任务.
     */
    HAS_HOT_PICK,

    /**
     * 已分配货位.
     */
    ASSIGNED_LOCATION,

    /**
     * 客户删单.
     */
    DELETE_ORDER_CUSTOMER,

    /**
     * 强制删单.
     */
    DELETE_ORDER_FORCE,


    /**
     * 拆单发货.
     */
    DISMANTLE_SHIPMENT,

    /**
     * 无库存删单.
     */
    OUT_OF_STOCK_DELETE_ORDER,

    /**
     * 补打条码.
     */
    PRINT_SKU_REPAIR,

    /**
     * 转为待调查状态.
     */
    TO_BE_INVESTIGATED,

    /**
     * 清除问题处理格.
     */
    CLEARANCE_CELL,

    /**
     * 释放问题处理格.
     */
    RELEASE_CELL,

    /**
     * 点问题商品确认问题.
     */
    PROBLEM,

    /**
     * 点正常商品确认问题.
     */
    NORMAL




}

