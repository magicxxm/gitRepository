package com.mushiny.wms.masterdata.mdbasics.exception;

public enum MasterDataException {

    /**
     * MASTER DATA ENTITY EXCEPTION
     **/
    EX_MD_AREA_NAME_UNIQUE,// 功能区:{0}已存在!

    EX_MD_BATTERCONFIG_NAME_UNIQUE,// 名称:{0}已存在!

    EX_MD_POD_NAME_UNIQUE, // 货架:{0}已存在!

    EX_MD_POD_TYPE_NAME_UNIQUE,// 货架类型:{0}已存在!

    EX_MD_POD_TYPE_ROWS_ERROR,// 长度不符:{0}已存在!

    EX_MD_POD_FACE_ERROR,//FACE：系统中只有ABCD四个面!

    EX_MD_DROP_ZONE_NAME_UNIQUE,// DROP ZONE:{0}已存在!

    EX_MD_ITEM_DATA_NO_UNIQUE,// SKU:{0}已存在!

    EX_MD_ITEM_GROUP_NAME_UNIQUE,// 货物分组:{0}已存在!

    EX_MD_ITEM_UNIT_NAME_UNIQUE,// 单位:{0}已存在!

    EX_MD_STORAGE_LOCATION_NAME_UNIQUE,// 货位:{0}已存在!

    EX_MD_STORAGE_LOCATION_TYPE_NAME_UNIQUE,// 货位类型:{0}已存在!

    EX_MD_ZONE_NAME_UNIQUE,// 区域:{0}已存在!

    EX_MD_ROBOT_NAME_UNIQUE,// id:{0}已存在!

    EX_MD_ROBOT_ENTER_ERROR,// 登入出错!

    EX_MD_WORK_STATION_TYPE_NAME_UNIQUE,// 名称:{0}已存在!

    EX_MD_WORK_STATION_NAME_UNIQUE,// 名称:{0}已存在!

    EX_MD_POD_CLIENT_ERROR;//客户:{0}不一致!
}
