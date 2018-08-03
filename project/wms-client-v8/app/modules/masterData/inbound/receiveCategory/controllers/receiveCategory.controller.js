/**
 * Created by frank.zhou on 2017/04/25.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("receiveCategoryCtl", function ($scope, $rootScope, $window, $state, $translate, commonService, masterService) {

    $window.localStorage["currentItem"] = "receiveCategory";

    $rootScope.categoryTypeSource = ["EACH RECEIVE", "PALLET RECEIVE"];
    $rootScope.receiveDestinationSource = masterService.getDataSource({key: "getReceiveDestination", text: "name", value: "id"});
    $rootScope.receiveCategoryRuleSource = masterService.getDataSource({key: "getReceiveCategoryRule", text: "name", value: "id"});

    $rootScope.addReceivingCategoryPosition = function (object) {
      var grid = $("#receiveCategoryPositionGrid").data("kendoGrid");
      grid.dataSource.insert(0, {receiveCategoryRule: object});
    };

    function getListName(ids, lists){
      for(var i = 0, names = [], items = []; i < lists.length; i++){
        var list = lists[i];
        ids.indexOf(list.id) >= 0 && names.push(list.name) && items.push({id: list.id, name: list.name});
      }
      return [names, items];
    }

    // main
    var columns = [
      {field: "name", headerTemplate: "<span translate='NAME'></span>"},
      {field: "categoryType", headerTemplate: "<span translate='CATEGORY_TYPE'></span>"},
      {field: "receiveDestination.name", headerTemplate: "<span translate='RECEIVE_DESTINATION'></span>"},
      {field: "orderIndex", headerTemplate: "<span translate='ORDER_INDEX'></span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];

    $scope.receiveCategoryGridOptions = commonService.gridMushiny({
      dataSource: masterService.getGridDataSource("receiveCategory"),
      columns: columns,
      detailInit: function(e){
        masterService.read("receiveCategory", e.data.id, function(data) {
          $("<div></div>").appendTo(e.detailCell).kendoGrid({
            dataSource: data.positions,
            height: 220,
            selectable: "row",
            sortable: true,
            scrollable: true,
            columns: [
              {field: "positionNo", width: 150, headerTemplate: $translate.instant("POSITION_NO")},
              {field: "receiveCategoryRule.name", width: 200, headerTemplate: $translate.instant("RULE_NAME")},
              {field: "operator", width: 200, headerTemplate: $translate.instant("OPERATE")},
              {field: "compKey", headerTemplate: $translate.instant("COMP_KEY"), template: function(item){
                var rule = item.receiveCategoryRule, values = [item.compKey];
                if(rule.comparisonType === "VALUE_FROM_CONTEXT" && rule.decisionKey != "ZONE_TYPE"){
                  var ids = item.compKey.split(","), selectList = rule.selectList;
                  values = getListName(ids, selectList)[0];
                }
                return values.join(",");
              }}
            ]
          });
        });
      }
    });

    // position
    $rootScope.receiveCategoryPositionGridOptions = masterService.editGrid({
      height: 220,
      columns: [
        {field: "positionNo", editor: masterService.numberEditor, width: 150, headerTemplate: "<span translate='POSITION_NO'></span>"},
        {field: "receiveCategoryRule.name", width: 200, headerTemplate: "<span translate='RULE_NAME'></span>"},
        {field: "operator", width: 200, headerTemplate: "<span translate='OPERATE'></span>", editor: function(container, options){
          masterService.selectEditor(container, options, options.model.receiveCategoryRule.operators);
        }},
        {field: "compKey", headerTemplate: "<span translate='COMP_KEY'></span>", editor: function(container, options){
          if(options.model.receiveCategoryRule.comparisonType === "VALUE_FROM_CONTEXT")
            masterService.selectEditor(container, options, options.model.receiveCategoryRule.selectList);
          else
            $('<input name="' + options.field + '" class="k-textbox" />').appendTo(container);
        }, template: function(item){
          var datas = [];
          if(item.compKey){
            // start: 初始id需转换
            var rule = item.receiveCategoryRule;
            if(rule.comparisonType === "VALUE_FROM_CONTEXT" && rule.decisionKey != "ZONE_TYPE" && typeof item.compKey === "string"){
              item.compKey = getListName(item.compKey.split(","), rule.selectList)[1];
            }
            // end
            if(typeof item.compKey === "string") datas.push(item.compKey);
            else for(var i = 0; i < item.compKey.length; i++) datas.push(item.compKey[i].name);
          }
          return datas.join(",");
        }}
      ]
    });

  }).controller("receiveCategoryCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        var grid = $("#receiveCategoryPositionGrid").data("kendoGrid"), datas = grid.dataSource.data();
        for(var i = 0, details = []; i < datas.length; i++){
          var data = datas[i], items = [];
          if(data.compKey){
            if(typeof data.compKey === "string") items.push(data.compKey);
            else for(var j = 0; j < data.compKey.length; j++) items.push(data.compKey[j].id);
          }
          details.push({
            "positionNo": data.positionNo,
            "name": data.receiveCategoryRule.name,
            "ruleId": data.receiveCategoryRule.id,
            "operator": data.operator,
            "compKey": items.join(","),
            "clientId": $scope.client.id
          });
        }
        masterService.create("receiveCategory", {
          "name": $scope.name,
          "categoryType": $scope.categoryType,
          "orderIndex": $scope.orderIndex,
          "description": $scope.description,
          "receiveDestinationId": $scope.receiveDestination.id,
          "clientId": $scope.client? $scope.client.id: null,
          "positions": details
        }, function () {
          $state.go("main.receive_category");
        });
      }
    };
  }).controller("receiveCategoryUpdateCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("receiveCategory", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
      $scope.client = {id: data.clientId};
      var grid = $("#receiveCategoryPositionGrid").data("kendoGrid");
      grid.setDataSource(new kendo.data.DataSource({data: data.positions}));
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        var grid = $("#receiveCategoryPositionGrid").data("kendoGrid"), datas = grid.dataSource.data();
        for(var i = 0, details = []; i < datas.length; i++){
          var data = datas[i], items = [];
          if(data.compKey){
            if(typeof data.compKey === "string") items.push(data.compKey);
            else for(var j = 0; j < data.compKey.length; j++) items.push(data.compKey[j].id);
          }
          details.push({
            "id": data.id || null,
            "positionNo": data.positionNo,
            "name": data.receiveCategoryRule.name,
            "ruleId": data.receiveCategoryRule.id,
            "operator": data.operator,
            "compKey": items.join(","),
            "clientId": $scope.client.id
          });
        }
        masterService.update("receiveCategory", {
          "id": $scope.id,
          "name": $scope.name,
          "categoryType": $scope.categoryType,
          "orderIndex": $scope.orderIndex,
          "description": $scope.description,
          "receiveDestinationId": $scope.receiveDestination.id,
          "clientId": $scope.client? $scope.client.id: null,
          "positions": details
        }, function () {
          $state.go("main.receive_category");
        });
      }
    };
  });
})();