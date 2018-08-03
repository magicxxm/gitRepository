package com.mushiny.wms.masterdata.ibbasics.business.enums;

public enum ReceivingCategoryFilter {

    NOT_MEASURED,//是否测量
    MATCH_AT_LEAST_ONE,// 至少包含一个
    EQUAL,  //等于
    GREATER_THAN, //大于
    GREATER_THAN_OR_EQUAL_TO,//大于等于
    LESS_THAN,//小于
    LESS_THAN_OR_EQUAL_TO,//小于等于
    NA,
}
