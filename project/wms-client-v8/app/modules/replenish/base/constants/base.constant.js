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
  var items = ["inventoryAnalysis"];
    var baseConstant = {};
    for(var i = 0; i < items.length; i++){
      var item = items[i], key = item.substring(0, 1).toUpperCase()+ item.substring(1);
      item = "replenish/"+ item; // 转换item
      baseConstant["find"+ key] = item+ "s";
      baseConstant["create"+ key] = item+ "s";
      baseConstant["get"+ key] = item+ "s";
      baseConstant["read"+ key] = item+ "s/#id#";
      baseConstant["update"+ key] = item+ "s";
      baseConstant["delete"+ key] = item+ "s/#id#";
    }
      angular.module("myApp").constant("REPLENISH_FILTER", {
       }).constant("REPLENISH_CONSTANT", angular.extend(baseConstant, {
          //上下班打卡
          "startCron": "replenish/scheduled/startCron",
          "stopCron": "replenish/scheduled/stopCron",
          "getTimeConfig": "replenish/timeConfig/selectTime",
          "updateTime": "replenish/timeConfig/updateTime",
          "updateStopTime": "replenish/timeConfig/updateStopTime"
  }));
})();