/**
 * Created by frank.zhou on 2017/03/02.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("zoneProcessPathCtl", function ($scope, $window, zoneProcessPathService) {

    $window.localStorage["currentItem"] = "zoneProcessPath";

    // 取zoneProcessPath
    function refresh(){
      zoneProcessPathService.getZoneProcessPath(function(datas){
        if(datas == "") return;
        var processAmount = datas.processAmount;
        // columns和dataSource
        var columns = [{width: 150, template: "#= processPathName #", headerTemplate: ""}]; // 首列
        for(var i = 0, dataSource = []; i < datas.processAmount.length; i++){
          var processEach = processAmount[i], pickAreaAmount = processEach.pickAreaAmount;
          var data = {"processPathName": processEach.processPathName, "amount": processEach.amount};
          for(var j = 0; j < pickAreaAmount.length; j++){
            var pickAreaEach = pickAreaAmount[j], field = pickAreaEach.pickAreaName.replace(/-/g, "");
            data[field] = pickAreaEach.amount;
            if(columns.length != pickAreaAmount.length +1)
              columns.push({"field": field, headerTemplate: pickAreaEach.pickAreaName});
          }
          dataSource.push(data);
        }
        columns.push({"field": "amount", headerTemplate: "<span translate='SUM'></span>"}); // 最后一列
        // 最后一行数据
        var lastData = {"processPathName": "汇总", "amount": 0};
        for(var i = 0, pickAreaAmount = datas.pickAreaAmount; i < pickAreaAmount.length; i++){
          var pickEach = pickAreaAmount[i], field = pickEach.pickAreaName.replace(/-/g, "");
          lastData[field] = pickEach.amount;
          lastData["amount"] = lastData.amount + pickEach.amount;
        }
        dataSource.push(lastData);
        // 加载表
        var grid = $("#zoneProcessPathGRID").data("kendoGrid");
        grid.setOptions({columns: columns, dataSource: dataSource});
      });
    }

    // 初始化
    $scope.zoneProcessPathGridOptions = {height: $(document.body).height() - 190, selectable: false, sortable: true, resizable: true}; // grid
    refresh();

  });
})();