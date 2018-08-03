package com.mushiny.wms.outboundproblem.domain.enums;

public enum ProblemType {
    MORE,//多货
    LESS,//少货
    DAMAGED,//商品残损
    LOSE,//商品丢失
    UNABLE_SCAN_SKU,//条码无法扫描
    UNABLE_SCAN_SN,//序列号无法扫描
    DELETE_ORDER_CUSTOMER//客户删单
}
