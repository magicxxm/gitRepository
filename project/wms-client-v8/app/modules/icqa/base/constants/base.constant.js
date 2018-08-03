/**
 * Created by thoma.bian on 2017/5/10.
 * Updated by frank.zhou on 2017/5/22.
 */

(function() {
  "use strict";

  // ==========================================================================================================
  function transformItem(str){
    for(var i = 0, outs = [], preIdx = 0; i < str.length; i++){
      var c = str.charAt(i);
      if(i == str.length - 1){
        var last = str.substring(preIdx);
        outs.push(last.substring(0, 1).toLowerCase()+ last.substring(1));
      }
      if(c < "A" || c > "Z") continue;
      var mid = str.substring(preIdx, i);
      outs.push(outs.length? mid.substring(0, 1).toLowerCase()+ mid.substring(1): mid);
      outs.push("-");
      preIdx = i;
    }
    return outs.join("");
  }

  // ==========================================================================================================
  var items = ["stocktakingStation", "stocktakingStationType"];
  var baseConstant = {};
  for(var i = 0; i < items.length; i++){
    var item = items[i], key = item.substring(0, 1).toUpperCase()+ item.substring(1);
    item = "masterdata/icqa/"+ transformItem(item); // 转换item
    baseConstant["find"+ key] = item+ "s";
    baseConstant["create"+ key] = item+ "s";
    baseConstant["get"+ key] = item+ "s";
    baseConstant["read"+ key] = item+ "s/#id#";
    baseConstant["update"+ key] = item+ "s";
    baseConstant["delete"+ key] = item+ "s/#id#";
  }
  angular.module("myApp").constant("ICQA_FILTER", {
    "stocktakingStation": [{"field": "name"}],
    "stocktakingStationType": [{"field": "name"}]
  }).constant("ICQA_CONSTANT", angular.extend(baseConstant, {
    "getWarehouse": "system/warehouses",
    "getClient": "system/clients",
    "getSelectionBySelectionKey": "system/selections",
    "getWorkstation": "masterdata/workstations",
    //
    "getAndonMasters":"andon-masters/universalSearch",
    "updateAndonMasters":"andon-masters",
    "selectAndonType":"andon-master/pe-solve/anDonMasterType",
    "getItemAdjust":"andon-masters/lossAndOverage/search",
    "updateAdjustment":"andon-masters/update/stockUnitRecord",
     //呼叫POD
      "callPodAndon":"andon-masters/callPodAndon",
      "callPodIcqaAdjustment":"andon-masters/callPodAdjustment",
      // 呼叫pod接口
      "callPodInterface":"commonStation/calling/pods",
      // 释放 pod 接口
      "releasePod1":"wcs/podRelease",

      // 是否有在途pod
      "yesOrNoFinsh":"icqa/stocktakings/yesOrNoFinsh/goBack",

      // 刷新pod信息
      "refreshNewPod":"wcs/callNewPod",

      "podWebSocket":"websocket/getPod/",

      //停止呼叫pod 恢复分配pod
      "stopCallPod":"icqa/stocktakings/stopCallPod",
      //在盘点明细表中插入盘点货位和规则
    "getStocktaking0rder": "icqa/stocktaking-orders",
      //得到盘点人员(下拉框)
    "getStocktaking0rderUser": "icqa/stocktaking-orders/users",
    "createStocktaking": "icqa/stocktakings",
      //获得盘点任务
    "findSelectRoundOfInventory": "icqa/stocktaking-orders/round-of-inventorys",
      //得到盘点详情
    "selectRoundOfInventoryId": "icqa/stocktaking-orders/round-of-inventory-ones",
      //获得一轮二轮盘点下的 orders 详情
    "select0rdersByStocktakingIds": "icqa/stocktaking-orders/details-webs",
    "getZone": "icqa/stocktaking-orders/zones",
    "getStocktakingRules": "icqa/stocktaking-rules",
    "saveStocktaking":"icqa/stocktakings",
      //保存盘点用户
    "saveStocktakingUser": "icqa/stocktaking-users",
      //判断二轮三轮是否指定过人了
    "checkInventory": "icqa/stocktaking-users/check-users-times",
      //指定用户时判断前一轮是否结束
    "checkOneInventory": "icqa/stocktaking-orders/check-lefts",
      //删除盘点人员
    "deleteUsers": "icqa/stocktaking-users/delete-users",
      //得到一轮，二轮，三轮剩余数量
    "selectInventoryCount": "icqa/stocktaking-orders/amounts",
      // 绑定工作站
      "getAndonStation": "icqa/stocktakings/binding-workstation",
      //解绑工作站
      "exitAndonStation": "icqa/stocktakings/untie-workstation",

      //系统盘点
      //获得盘点任务
      "getSelectRoundOfInventory": "icqa/systemStocktaking-orders/round-of-inventorys",
      //获得盘点
      "enterSelectRoundOfInventoryId": "icqa/systemStocktaking-orders/round-of-inventory-ones",
      //得到盘点详情
      "selectStocktakingIds": "icqa/systemStocktaking-orders/details-webs",
      //得到一轮，二轮，三轮剩余数量
      "selectInventoryCounts": "icqa/systemStocktaking-orders/amounts",
      //创建盘点任务
      "inventoryTask":"icqa/systemStocktaking-orders",

      "getItemData":"icqa/systemStocktaking-record/check-goods",

      "getItemDataNameAmount":"icqa/systemStocktaking-record/itemDataNameAmount",
      // 美的待调整
      "getPendingAdjust":"andon-masters/lossAndOverage/pending/search",

      // SKU盘点 创建插入数据
      "saveSkuStocktaking":"icqa/systemStocktakings",

      // SKU盘点 创建 position 中插入数据
      "saveSkuStocktakingPosition": "icqa/systemStocktaking-orders/insertPosition",

      // 按SKU导入 导入数据保存
      "saveImportItemData": "icqa/systemStocktakings/itemData/import/file",
      // 按货位导入 导入数据保存
      "saveImportStorageLocation": "icqa/systemStocktakings/LocationName/import/file",

      // 手动关闭
      "getCloseStocktaking": "/icqa/stocktakings/closeStocktaking",

      "getCloseSkuStocktaking" :"/icqa/stocktakings/closeStocktakingSku",

      "getNotStocktakingAmount" : "/icqa/stocktaking-orders/notStocktakingAmount",

      "callPod":"/icqa/wms/mushiny/callPod"















  }));
})();