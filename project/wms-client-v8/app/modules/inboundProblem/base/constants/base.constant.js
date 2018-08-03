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
  var items = ["ibpStation", "ibpStationType"];
  var baseConstant = {};
  for(var i = 0; i < items.length; i++){
    var item = items[i], key = item.substring(0, 1).toUpperCase()+ item.substring(1);
    item = "masterdata/ibp/"+ transformItem(item); // 转换item
    baseConstant["find"+ key] = item+ "s";
    baseConstant["create"+ key] = item+ "s";
    baseConstant["get"+ key] = item+ "s";
    baseConstant["read"+ key] = item+ "s/#id#";
    baseConstant["update"+ key] = item+ "s";
    baseConstant["delete"+ key] = item+ "s/#id#";
  }
  angular.module("myApp").constant("IBP_FILTER", {
    "ibpStation": [{"field": "name"}],
    "ibpStationType": [{"field": "name"}]
  }).constant("PROBLEM_INBOUND", angular.extend(baseConstant, {
    "getWarehouse": "system/warehouses",
    "getClient": "system/clients",
    "getSelectionBySelectionKey": "system/selections",
    "getWorkstation": "masterdata/workstations",
    //
    "getInboundProblem":"inbound-problem/universalSearch",
    "updateInboundProblem":"inbound-problem",
    "findInboundProblem": "inbound-problem",
    "getGoodsInformation": "inbound-problem/item",
    "inboundProblemRecord":"inbound-problem/records",
    "getProblemProductsNumber":"inbound-problem/check",
    "getInboundProblemRule":"inbound-problem/solve/universalSearch",
    //全部关闭
    "updateInboundProblemClose":"inbound-problem/updateCloses",
    "findInboundProblemSolve":"inbound-problem/solve",
      //在inbound问题管理表中插入数据
     "getInboundDeal":"inbound-problem/rule",
     "getInboudProblemState":"inbound-problem/solve",

    //原始容器id
    "getDestinationId":"inbound-problem/locationName",
    // 上架多货
    "getStowingOverage": "inbound-problem/stow-overage-goods",

    "checkIsMore":"inbound-problem/records",
    // 上架少货
     "getStowingLoss": "inbound-problem/stow-loss-goods",
      //并案分析
      "getAnalysis": "inbound-problem/record/analysis",
    // 移动------并案调整
    "getMoveGoods": "inbound-problem/moving/goods",
    // 盘盈
    "getOverageGoods": "inbound-problem/overage-goods",
    // 盘亏
    "getLossGoods": "inbound-problem/loss-goods",
    //绑定工作站
    "getInboundProblemStation":"inbound-problem/binding-workstation",
    //退出工作站
    "exitInboundProblemStation":"inbound-problem/untie-workstation",

    "yesOrNoFinsh":"inbound-problem/yesOrNoFinsh/goBack",
    // // 商品
    // "getContainerGoods": "inventory/container/sku",
    // "getClientGoods": "inventory/client/sku"
      // 呼叫POD
      "callPodInboundProblem":"inbound-problem/records/podFace",
      // 停止呼叫POD恢复分配pod
      "stopCallPod":"inbound-problem/stopCallPod",

      "workStationPodState":"inbound-problem/workStationPodState",

      "getLocation":"inbound-problem/getLocation",
      // 呼叫pod接口
      "callPodInterface":"commonStation/calling/pods",
      // 释放 pod 接口
      "releasePod1":"wcs/podRelease",

      "podWebSocket":"websocket/getPod/",
      // 刷新pod
      "refreshNewPod":"wcs/callNewPod"


  }));
})();