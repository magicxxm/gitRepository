/**
 * Created by thoma.bian on 2017/5/10.
 * Updated by frank.zhou on 2017/05/10.
 */
(function(){
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
  var items = ["obpCell", "obpCellType", "obpStation", "obpStationType", "obpWall", "obpWallType"];
  var baseConstant = {};
  for(var i = 0; i < items.length; i++){
    var item = items[i], key = item.substring(0, 1).toUpperCase()+ item.substring(1);
    item = "masterdata/obp/"+ transformItem(item); // 转换item
    baseConstant["find"+ key] = item+ "s";
    baseConstant["create"+ key] = item+ "s";
    baseConstant["get"+ key] = item+ "s";
    baseConstant["read"+ key] = item+ "s/#id#";
    baseConstant["update"+ key] = item+ "s";
    baseConstant["delete"+ key] = item+ "s/#id#";
  }
  angular.module("myApp").constant("OBP_FILTER", {
    "obpCell": [{"field": "name"}],
    "obpCellType": [{"field": "name"}],
    "obpStation": [{"field": "name"}],
    "obpStationType": [{"field": "name"}],
    "obpWall": [{"field": "name"}],
    "obpWallType": [{"field": "name"}]
  }).constant("PROBLEM_OUTBOUND", angular.extend(baseConstant, {
    "getWarehouse": "system/warehouses",
    "getClient": "system/clients",
    "getSelectionBySelectionKey": "system/selections",
    "getWorkstation": "masterdata/workstations",
    //
    "getOutboundProblemStation": "outboundproblem/scanning/obp-station",
    "getOutboundProblemHandingCar": "outboundproblem/scanning/obp-wall",
    "getShipmentDealProblem": "outboundproblem/scanning/shipment",
    "exitShipment": "outboundproblem/obp-solve/sign-out-station",
    "problemCellPlaceGoods": "outboundproblem/obp-cell",
    "problemCellStorageLocation": "outboundproblem/obp-wall-storagelocation",
    "scanGoodsStorageLocation": "outboundproblem/obp-solve/assign-location", //分配货位
    "bindCell": "outboundproblem/obp-bindCell",
    "gotoPacking": "outboundproblem/obp-solve/goto-packing",
    "gotoPack": "outboundproblem/obp-solve/go-to-pack",//测试包装
    "batchToPacking":"outboundproblem/obp-solve/batch-to-packing",
    "getShipmentNoByCellName": "outboundproblem/cell-shipment",
    "getOrderDetails": "outboundproblem/scanning/shipment",
    "getOrderGoodsDetails": "outboundproblem/obp-solve/shipment-position",
    "printOrder":"outboundproblem/obp-solve/print-shipment-position",
    "saveGoodsBySN": "outboundproblem/obp-solve/scan-sn",
    "saveGoodsInformation": "outboundproblem/obp-solve/scan-goods",
    "damageConfirm": "outboundproblem/obp-damaged/confirm-damage",
    "damageGoods": "outboundproblem/obp-damaged/damaged-to-container",
    "saveGoodsToGenuine": "outboundproblem/obp-damaged/to-normal",
    "releaseQuestionCell": "outboundproblem/obp-unbindCell",
    "relieveQuestionCell":"outboundproblem/obp-relieveCell",//测试释放问题格
    "generateNewPickingTasks": "outboundproblem/obp-damaged/generate-hot-pick",
    "getAssignedLocation": "outboundproblem/obp-damaged/get-location",
    "assignedPicking": "outboundproblem/obp-damaged/allocation-location",
    "callPodOutboundProblem":"outboundproblem/obp-damaged/getPod",
    "demolitionShip": "outboundproblem/obp-damaged/dismantle-shipment",
    "deleteOrderSuccess": "outboundproblem/obp-damaged/delete-shipment",
    "deleteOrderScanGoods": "outboundproblem/obp-damaged/delete-shipment-scan-goods",
    "markUpBarcode": "outboundproblem/unable-to-scan/print-sku",
    "getInvestigated": "outboundproblem/unable-to-scan/to-investigated",
    "moveShelvesLicensePlate": "outboundproblem/obp-delete-shipment/force-delete",
    "dealWithProblem": "outboundproblem/normal-to-problem",
    "scanPickingLicensePlate":"outboundproblem/obp-solve/get-cell-container",
    "checkScanGoods": "outboundproblem/obp-solve/get-cell-itemNo",
    "forcedDeleteGrid": "outboundproblem/obp-delete-shipment/query-force-delete",
    "forcedDeleteOrder": "outboundproblem/obp-delete-shipment",
    "putForceDeleteGoodsToContainer": "outboundproblem/obp-delete-shipment/force-delete",
    "verifySingOutObpStation" :"outboundproblem/obp-solve/verify-sign-out-station",
    "getPodPosition":"outboundproblem/obp-solve/getPodPosition",
    "scanPickingGoods":"outboundproblem/obp-solve/scan-picking-goods",
    "scanPickgGoodsSerialNo":"outboundproblem/obp-solve/scan-goods-sn",
    "hotPickGoodsSerialNo":"outboundproblem/obp-solve/scan-hotPick-goods-sn",
    //
    "getOutboundProblem": "outboundproblem/universalSearch",
    "updateOutboundProblemVerify": "outboundproblem",
    "findOutboundProblem": "outboundproblem",
    "getGoodsInformation": "outboundproblem/item",
    "getOutboundProblemSolve":"outboundproblem/obp-solve/universalSearch",
    "getOutbountProblemState":"outboundproblem/solve",
      //rebin 车记录
    "getRebinCarRecords": "outboundproblem/findList",
    "outboundProblemRecord": "outboundproblem/records",
    "getProblemProductsNumber": "outboundproblem/obp-solve-check",

    "getDestinationId":"outboundproblem/obp-solve-check/get-storageLocation-id",
    // 找到多货位置
    "findOverageGoods": "outboundproblem/find-overage-goods",
    // 少货找到位置
    "findLossGoods": "outboundproblem/find-loss-goods",
    "updateOutboundProblemList": "outboundproblem/updateCloses",
    "getStowingOverage": "outboundproblem/stow-overage-goods",
    "getStowingLoss": "outboundproblem/stow-loss-goods",
    "getOverageGoods": "outboundproblem/overage-goods",
    "getLossGoods": "outboundproblem/loss-goods",
    "getAnalysis": "outboundproblem/analysis",
    "getMoveGoods": "outboundproblem/moving",
    "getRule": "outboundproblem",
    // 呼叫POD
    "callPodInboundProblem":"outboundproblem/records/podFace",
      // 呼叫pod接口
    "callPodInterface":"commonStation/calling/pods",

    // 停止呼叫POD恢复分配pod
    "stopCallPod":"outboundproblem/stopCallPod",
    //绑定工作站
    "getOnboundProblemStation":"outboundproblem/binding-workstation",
    //退出工作站
    "exitOnboundProblemStation":"outboundproblem/untie-workstation",

      // 释放 pod 接口
      "releasePod1":"wcs/podRelease",
      "podWebSocket":"websocket/getPod",
      "yesOrNoFinsh":"outboundproblem/yesOrNoFinsh/goBack",

      "workStationPodState":"outboundproblem/workStationPodState",
      //刷新pod
      "refreshNewPod":"wcs/callNewPod"
  }));
})();