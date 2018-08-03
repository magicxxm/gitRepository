package com.mushiny.wcs.common.exception;

public enum ExceptionEnum {

    /**
     * BASE EXCEPTION
     **/

    EX_USER_NOT_LOGIN, //用户没有登录！

    EX_NOT_FOUND_WAREHOUSE,// 仓库不存在！

    EX_NOT_CURRENT_WAREHOUSE,// 操作仓库与当前选择仓库不一致！

    EX_NOT_FOUND_CLIENT,// 客户不存在！

    EX_NOT_CURRENT_CLIENT,// 操作客户与当前选择客户不一致！

    EX_SERVER_ERROR, // 服务器错误！

    EX_OBJECT_NOT_FOUND,// 数据不存在！
    EX_MAP_CLASSVALUE_NOT_LEGAL,//map 的classvalue 值不合法
    EX_MAP_NODE_NOT_FOUND//没有找到地图节点
}
