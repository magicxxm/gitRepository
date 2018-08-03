package com.mushiny.wms.outboundproblem.exception;

public enum OutBoundProblemException {

    /**
     * IN BOUND ENTITY EXCEPTION
     **/
//    EX_RECEIVING_CATEGORY_NAME_UNIQUE,// 过滤规则:{0}已存在!
//
//    EX_RECEIVING_CATEGORY_RULE_NAME_UNIQUE,// 规则:{0}已存在!
//
//    EX_RECEIVING_DESTINATION_NAME_UNIQUE,// 目的地:{0}已存在!
//
//    EX_RECEIVING_STATION_NAME_UNIQUE,// 工作站:{0}已存在!
//
//    EX_RECEIVING_STATION_TYPE_NAME_UNIQUE,// 工作站类型:{0}已存在!
//
//    EX_ITEM_DATA_TYPE_GRADE_STATS_ITEM_DATA_UNIQUE,// SKU:{0}已存在!
//
//    EX_REPLENISH_STRATEGY_UNIQUE,// 补充策略已存在!

    EX_IT_ITEMDATA_SN_HAS_USED("序列号已经存在"),

    EX_IT_ITEMDATA_SN_ERROR("序列号错误"),

    //EX_IT_STORAGE_LOCATION_IS_MEASURED,// {0}容器不能为测量容器！

    EX_REPORT_PROBLEMS_FAILED("EX_REPORT_PROBLEMS_FAILED"),

    EX_ITEM_HAS_REPORTED_PROBLEMS("商品已报问题"),

    EX_SCANNING_OBJECT_NOT_FOUND("扫描对象不存在,请重新扫描"),

    //EX_SCANNING_OBJECT_HAS_SCANED,// 扫描对象{0},已扫描完！

    EX_SCANNING_OBJECT_HAS_LOSED("商品已丢失,无法扫描"),

    //EX_SCANNING_OBJECT_UNABLE_SCAN_SKU,// 扫描对象{0},已丢失，无法扫描！

    EX_SCANNING_OBJECT_MORE_THAN_ONE("该商品为多条码商品,请扫描唯一编码"),

    EX_SCANNING_SHIPMENT_IS_NORMAL("EX_SCANNING_SHIPMENT_IS_NORMAL"),

    EX_SCANNING_SHIPMENT_NOT_BIND_CELL("EX_SCANNING_SHIPMENT_NOT_BIND_CELL"),

    EX_CELL_HAS_BIND("问题货格已被绑定"),

    EX_SN_NOT_FOUND("商品序列号错误"),

    EX_SN_ERROR("此序列号已被扫描"),

    EX_SN_NOT_ASSIGN("此件序列号商品不是系统分配的那一件,请重新扫描"),

    EX_OBPROBLEM_STATION_HAS_USED("EX_OBPROBLEM_STATION_HAS_USED"),

    EX_SHIPMENT_HAS_NOT_PICK_UNITLOAD("数据错误,订单还没有拣货"),

    EX_SHIPMENT_CAN_NOT_DELETE("订单还没拣货完成或是订单已出货,不能删除"),

    //EX_ITEM_DATA_NOT_FOUND,//商品不存在

    EX_SHIPMENT_DELETE_ERROR("订单还未Rebin不能刪除"),

    EX_IT_STORAGE_LOCATION_NOT_USED("容器不允许操作"),

    EX_IT_USERNAME_NOT_FOUND("用户不存在"),

    EX_IT_STORAGE_LOCATION_NOT_FOUND("容器不存在"),

    // OBP_NO_UNTIE_WORKSTATION,//无权限解绑此工作站

    OBP_WORKSTATION_SOMEONE("OBP_WORKSTATION_SOMEONE"),

    OBP_NO_WORKSTATION("OBP_NO_WORKSTATION"),

    OBP_DEAL_WORKSTATION("OBP_DEAL_WORKSTATION"),

    EX_IT_SKU_NOT_FOUND("商品条码不存在"),

    EX_IT_SKU_HAS_MORE_ITEM_DATA("此商品为多条码商品"),

    //EX_IT_STORAGE_LOCATION_NOT_FOUND_SKU,//{0}中不存在商品{1}！

    EX_IT_STORAGE_LOCATION_SKU_DIFFERENT_CLIENT("EX_IT_STORAGE_LOCATION_SKU_DIFFERENT_CLIENT"),

    EX_IT_STORAGE_LOCATION_SKU_DIFFERENT_LOT("EX_IT_STORAGE_LOCATION_SKU_DIFFERENT_LOT"),

    EX_IT_AMOUNT_MORE_THAN_SYSTEM_AMOUNT("库存不足,移货数量超过系统值"),

    EX_IT_STORAGE_LOCATION_SKU_ITEMS_MAX_AMOUNT("货位中商品种类超过系统设置数量"),

    EX_IT_STORAGE_LOCATION_WEIGHT_HAS_MAX("货位超过系统设置载重量"),

    EX_IT_STORAGE_LOCATION_SKU_DIFFERENT_ITEM_GROUP("与商品种类不符合"),

    EX_SHIPMENT_IS_SOLVING_AT_OTHER_WALL("订单已在其他问题WALL中处理,不能重复处理"),

    EX_SHIPMENT_HAS_SOLVED("订单已在处理中,不能重复处理"),

    EX_SHIPMENT_IS_SOLVING("订单正在进行问题处理,清除问题格后再强制删除"),

    EX_SHIPMENT_IS_SOLVED("客户删单的订单已处理完成"),

//    EX_IT_INVENTORY_STATE_IS_DIFFERENT,// 修改库存属性与目的容器不符合！
//
//    EX_STOCK_UNIT_HAS_NOT_RECORD,// STOCKUNIT:不存在{0}中有SKU:{1}！

    EX_IT_UNITLOAD_IS_LOCKED("容器已被锁定"),

//    EX_QUANTITY_ILLEGAL,//输入数量不合法
//
//    EX_STOCK_UNIT_NOT_FOUND_ITEM,//没有库存

    EX_CONTAINER_IS_NOT_EMPTY("容器里存在商品"),


//    EX_RESOURCE_KEY_LOCALE_UNIQUE,// 语言:{0}下的资源:{1}不存在!
//
//    EX_SKU_NEED_SCAN_SN,// 商品{0},需扫描SN码！
//
//    EX_AMOUNT_ERROR,// 数量错误,请重新核实！
//    EX_AMOUNT_IS_ZERO,// 数量不容许为零！
//    EX_LOT_ITEM_DATA_NOT_FOUND_DATE,// 商品:{0}为有效期商品,但尚未录入有效期;请将商品放回原车牌,交给问题处理处!
//
//    EX_SN_IS_NULL, // SN不容许为空！
//    EX_SN_HAS_USED,// SN:{0}已经存在,请自己核查！
//
//    EX_SKU_NOT_FOUND,// SKU:{0}不存在！
//    EX_SKU_NOT_CUBI_SCAN,// 商品:{0},没有测量！
//
//    EX_DN_SKU_NOT_FOUND,// DN:{0}中不存在SKU:{1}！
//    EX_DN_HAS_DELETED,// DN:{0}已被标记为删除！
//    EX_DN_HAS_ACTIVATED,// DN:{0}已激活！
//    EX_DN_NOT_ACTIVATED,// DN:{0}没有激活！

      EX_RECEIVING_STATION_HAS_DELETED("工作站已被标记为删除"),

//    EX_RECEIVING_STATION_CONTAINER_AMOUNT_MAX,// 工作站:{0}绑定容器已经达到最大值！
//
//    EX_RECEIVING_DESTINATION_HAS_DELETED,// 目的地:{0}已被标记为删除！
//    EX_RECEIVING_DESTINATION_HAS_USED,// 工作站已绑定{0}目的地！
//    EX_RECEIVING_DESTINATION_NOT_FOUND_IN_POSITION,// 工作站不能绑定{0}目的地

//    EX_RECEIVING_CATEGORY_NOT_FOUND,// 客户:{0}没有设定收货规则！
//    EX_REPLENISH_STRATEGY_NOT_FOUND,// 客户:{0}的补充策略不存在！
//
//    EX_STORAGE_LOCATION_NOT_IS_DAMAGE,// 货位无效：{0}，货位不是残品货位！
//    EX_STORAGE_LOCATION_NOT_IS_INVENTORY,// 货位无效：{0}，货位不是存货货位！
//    EX_STORAGE_LOCATION_SKU_DIFFERENT_CLIENT,// 货位无效：{0}，存在不同供应商相同商品！
//    EX_STORAGE_LOCATION_SKU_DIFFERENT_LOT,// 货位无效:{0},存在相同商品不同有效期商品!
//    EX_STORAGE_LOCATION_SKU_CLIENT_DIFFERENT,// 货位无效：{0}，货位与商品的客户不符！
//    EX_STORAGE_LOCATION_SKU_DIFFERENT_ITEM_GROUP,// 货位无效：{0}，货位和商品类型不符
//    EX_STORAGE_LOCATION_SKU_ITEMS_MAX_AMOUNT,// 货位无效：{0}，货位中商品种类超过系统设置数量

    EX_OBPWALL_IS_NOT_OCCUPIED_STATE("问题处理车正在使用"),//

    EX_CONTAINER_NOT_IS_CONFORMITY("扫描对象不存在,请重新扫描"),//

    EX_CREATE_VIRTUAL_STORAGELOCATION_IS_FAILED("新建虚拟容器错误"),

    EX_OBPPROBLEM_TYPE_ERROR("问题转换错误"),

    //    EX_CONTAINER_NOT_IS_RECEIVE,// 收货容器{0}类型不属于残品容器！
//    EX_CONTAINER_NOT_IS_STOW,// 容器{0}不能进行上架，请重新扫描新的车牌上架！

    EX_CONTAINER_NOT_IS_DAMAGE("容器类型不属于残品容器"),//

//    EX_CONTAINER_NOT_IS_CUBI_SCAN,// 容器{0}类型不属于带测量容器！
//    EX_CUBI_SCAN_CONTAINER_HAS_THIS_SKU,// 待扫描容器{0}中已经存在SKU:{1}的商品！
//    EX_CONTAINER_SKU_DIFFERENT_CLIENT,// 容器无效：{0}，存在不同供应商相同商品！
//    EX_CONTAINER_SKU_DIFFERENT_LOT,// 容器无效:{0},存在相同商品不同有效期商品!
//    EX_CONTAINER_SKU_AMOUNT_IS_ZERO,// 容器：{0},中商品{0}已经全部上架完成！
//    EX_CONTAINER_SKU_AMOUNT_IS_NOT_ZERO,// 容器：{0},存在货物！
//    EX_CONTAINER_SKU_NOT_FOUND,// 商品条码无效：{0}！

    Outbound_Problem_ItemNo_And_SkuNo_NotNoll("Outbound_Problem_ItemNo_And_SkuNo_NotNoll"),

    Outbound_Problem_ItemNo_Or_SkuNo_NotCode("Outbound_Problem_ItemNo_Or_SkuNo_NotCode"),

    Outbound_Problem_Not_Found("Outbound_Problem_Not_Found"),

    EX_THIS_ITEM_HAS_SCANED("此种商品已扫描完成"),

    EX_THIS_SHIPMENT_HAS_SOLVED("此订单已处理完成,请不要重复处理"),

    EX_THIS_SHIPMENT_SHOULD_SOLVED("此订单还未处理完成,请在待处理页面进行处理"),

    EX_SHIPMENT_SHOULD_SCAN("订单里正常商品未扫描完成或者订单不支持拆单发货"),

    EX_CONTAINER_NOT_HAS_HOTICK_GOODS("车牌内不存在生成拣货任务的商品"),

    EX_ITEMDATA_HAS_TO_CONTAINER("订单里的残品或待调查商品已经放入问题处理车牌,不能清除问题格"),

    EX_CONTAINER_HAS_MORE_THAN_ONE_SHIPMENT("容器里有多个订单"),

    EX_SCANNING_SHIPMENT_NOT_FOUND("订单不存在"),

    EX_SKU_NOT_MATCH("商品不匹配"),

    EX_BIN_NOT_FOUND("不是已分配的货位"),

    EX_BIN_NOT_NOW_POD("货位不属于当前工作站的POD");

    private String name;//定义自定义的变量

    OutBoundProblemException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
  }
