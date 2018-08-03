/**
 * Created by frank.zhou on 2017/04/06.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("collateLivePickerCtl", function ($scope, $window, outboundService, collateLivePickerService) {

    $window.localStorage["currentItem"] = "collateLivePicker";

    // 刷新
    function refresh(processPathId){
      collateLivePickerService.getLivePicker(processPathId, function(datas){
        var grid = $("#collateLivePickerGRID").data("kendoGrid");
        grid.setOptions({dataSource: datas});
      });
    }

    // 搜索
    $scope.search = function(){
      refresh($scope.processPath? $scope.processPath.id: null);
    };

    // 初始化
    // $scope.processPathSource = outboundService.getDataSource({key: "getProcessPath", text: "name", value: "id"});
    var columns = [
      {field: "processPathName", headerTemplate: "<span translate='PROCESS_PATH'></span>"},
      {headerTemplate: "<span translate='LIVE_PICKER'></span>", template: function(dataItem){
        var datas = [], base = "width:160px;color:white;font-size:14px;text-align:center;display:inline-block;";
        // near
        for(var i = 0, nears = dataItem.nearPickers; i < nears.length; i= i+2){
          var prev = nears[i], next = nears[i+1];
          var htmlStr = "<div style='margin:1px;'>";
          prev && (htmlStr += "<div style='"+ base+ "background:red;'>"+ prev+ "</div>");
          next && (htmlStr += "<div style='"+ base+ "background:red;margin-left:5px;'>"+ next+ "</div>");
          htmlStr += "</div>";
          datas.push(htmlStr);
        }
        // middle
        for(var i = 0, middles = dataItem.mediatePickers; i < nears.length; i= i+2){
          var prev = middles[i], next = middles[i+1];
          var htmlStr = "<div style='margin:1px;'>";
          prev && (htmlStr += "<div style='"+ base+ "background:yellow;'>"+ prev+ "</div>");
          next && (htmlStr += "<div style='"+ base+ "background:yellow;margin-left:5px;'>"+ next+ "</div>");
          htmlStr += "</div>";
          datas.push(htmlStr);
        }
        // far
        for(var i = 0, fars = dataItem.farPickers; i < nears.length; i= i+2){
          var prev = fars[i], next = fars[i+1];
          var htmlStr = "<div style='margin:1px;'>";
          prev && (htmlStr += "<div style='"+ base+ "background:green;'>"+ prev+ "</div>");
          next && (htmlStr += "<div style='"+ base+ "background:green;margin-left:5px;'>"+ next+ "</div>");
          htmlStr += "</div>";
          datas.push(htmlStr);
        }
        return datas.join("");
      }},
      {headerTemplate: "<span translate='ORDER_POOL'></span>", template: function(dataItem){
        var categories = dataItem.categories;
        for(var i = 0, datas = []; i < categories.length; i= i+2){
          var category = categories[i], next = categories[i+1];
          var htmlStr = "<div style='margin:1px;'>";
          category && (htmlStr += "<div class='gridCellList'>"+ category+ "</div>");
          next && (htmlStr += "<div class='gridCellList' style='margin-left:5px;'>"+ next+ "</div>");
          htmlStr += "</div>";
          datas.push(htmlStr);
        }
        return datas.join("");
      }}
    ];
    $scope.collateLivePickerGridOptions = {
      selectable: false,
      height: $(document.body).height() - 191,
      sortable: true,
      scrollable: true,
      columns: columns
    };
    refresh();
  });
})();