/**
 * Created by frank.zhou on 2017/05/08.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("pickPackCellCtl", function ($scope, $rootScope, $window, commonService, masterService,$state) {

    $window.localStorage["currentItem"] = "pickPackCell";

    $rootScope.pickPackCellTypeSource = masterService.getDataSource({key: "getPickPackCellType", text: "name", value: "id"});
    $rootScope.pickPackWallSource = masterService.getDataSource({key: "getPickPackWall", text: "name", value: "id"});
    $rootScope.pickPackWallTypeSource =  masterService.getDataSource({key: "getPickPackWallType", text: "name", value: "id"});

    var columns = [
      {field: "name", template: "<a ui-sref='main.pickPackCellRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "pickPackCellType.name",template: "<a ui-sref='main.pick_pack_cell_type'>#: pickPackCellType.name # </a>", headerTemplate: "<span translate='PICK_PACK_CELL_TYPE'></span>"},
      {field: "pickPackWall.name",template: "<a ui-sref='main.pick_pack_wall'>#: pickPackWall.name # </a>", headerTemplate: "<span translate='PICK_PACK_WALL'></span>"},
      {field: "xPos", headerTemplate: "<span translate='X_POS'></span>"},
      {field: "yPos", headerTemplate: "<span translate='Y_POS'></span>"},
      {field: "zPos", headerTemplate: "<span translate='Z_POS'></span>"},
      {field: "field", headerTemplate: "<span translate='FIELD'></span>"},
      {field: "fieldIndex", headerTemplate: "<span translate='FIELD_INDEX'></span>"},
      {field: "digitalLabel1", headerTemplate: "<span translate='DIGITAL_LABEL_FRONT'></span>", template: function(item){
        return item.digitalLabel1? item.digitalLabel1.name: "";
      }},
      {field: "digitalLabel2", headerTemplate: "<span translate='DIGITAL_LABEL_BACK'></span>", template: function(item){
        return item.digitalLabel2? item.digitalLabel2.name: "";
      }},
      {field: "orderIndex", headerTemplate: "<span translate='ORDER_INDEX'></span>"},
        {
            field: "area",
            width: 120,
            headerTemplate: "<span translate='AREA'></span>",
            template: function (dataItem) {
                return (dataItem.area ? "<a ui-sref='main.area'>" + dataItem.area.name + "</a>" : "");
            }
        },
        {
            field: "storageLocationType",
            width: 120,

            headerTemplate: "<span translate='STORAGE_LOCATION_TYPE'></span>",
            template: function (dataItem) {
                return (dataItem.storageLocationType ? "<a ui-sref='main.storage_location_type'>" + dataItem.storageLocationType.name + "</a>" : "");
            }
        }
    ];
    $scope.pickPackCellGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("pickPackCell")});

    $scope.batchDelete=function(){
      $state.go("main.pickPackCellBatchUpdate");
    }
  }).controller("pickPackCellCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("pickPackCell", {
          "name": $scope.name,
          "typeId": $scope.pickPackCellType? $scope.pickPackCellType.id: null,
          "pickPackWallId": $scope.pickPackWall? $scope.pickPackWall.id: null,
          "xPos": $scope.xPos,
          "yPos": $scope.yPos,
          "zPos": $scope.zPos,
          "field": $scope.field,
          "fieldIndex": $scope.fieldIndex,
          "orderIndex": $scope.orderIndex,
          "labelColor": $scope.labelColor
        }, function () {
          $state.go("main.pick_pack_cell");
        });
      }
    };
  }).controller("pickPackCellUpdateCtl", function ($scope, $state, $stateParams, masterService){
    $scope.digitalLabelSource = masterService.getDataSource({key: "getDigitalLabel", text: "name", value: "id"});
      //容器类型
      $scope.storageLocationTypeDataSource = masterService.getDataSource({
          key: "getStorageLocationType",
          text: "name",
          value: "id"
      });
      //所在区域
      $scope.areaDataSource = masterService.getDataSource({key: "getArea", text: "name", value: "id"});
    masterService.read("pickPackCell", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("pickPackCell", {
          "id": $scope.id,
          "name": $scope.name,
          "typeId": $scope.pickPackCellType? $scope.pickPackCellType.id: null,
          "pickPackWallId": $scope.pickPackWall? $scope.pickPackWall.id: null,
          "xPos": $scope.xPos,
          "yPos": $scope.yPos,
          "zPos": $scope.zPos,
          "field": $scope.field,
          "fieldIndex": $scope.fieldIndex,
          "orderIndex": $scope.orderIndex,
          "labelColor": $scope.labelColor,
          "digitalLabel1Id": $scope.digitalLabel1? $scope.digitalLabel1.id: null,
          "digitalLabel2Id": $scope.digitalLabel2? $scope.digitalLabel2.id: null,
          "area":$scope.area,
          "storageLocationType":$scope.storageLocationType ? $scope.storageLocationType : null,
        }, function () {
          $state.go("main.pick_pack_cell");
        });
      }
    };
  }).controller("pickPackCellReadCtl", function ($scope, $stateParams, masterService){
    masterService.read("pickPackCell", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  }).controller("pickPackCellBatchUpdateCtl",function($scope,$rootScope,$state,$stateParams, masterService,commonService,pickPackWallService,pickPackCellService){
      $rootScope.pickPackWallSource = masterService.getDataSource({key: "getPickPackWall", text: "name", value: "id"});
      $rootScope.labelControllerSource =  masterService.getDataSource({key: "getLabelController", text: "name", value: "id"});
      // 转数组
      $scope.toItems = function(number){
          return new Array(number);
      };
      //选择pick pack wall
      $scope.changeWall = function(pickPackWall){
          masterService.read("pickPackWall", pickPackWall.id, function(data) {
              masterService.read("pickPackWallType", data.pickPackWallType.id, function(data) {
                  var pickPackFieldTypes = data.pickPackFieldTypes;
                  $scope.pickPackFieldTypes = data.pickPackFieldTypes;
                  for(var i = 0, width = 0, fieldTypeIds = []; i < pickPackFieldTypes.length; i++){
                      var fieldType = pickPackFieldTypes[i];
                      fieldTypeIds.push(fieldType.id);
                      width += 120*fieldType.numberOfColumns;
                  }
                  $scope.frontTableWidth = width;
                  $scope.backTableWidth = width;
                  $scope.fieldTypeIds = fieldTypeIds;
              })
          })
      };
      // 选择标签控制器
      $scope.changeLabelController = function(labelController){
          if(!labelController.length){
              $scope.digitalLabelSource = [];
              return;
          }
          // 标签控制器
          for(var i = 0, ids = []; i < labelController.length; i++) ids.push(labelController[i].id);
          // 取所有电子标签
          pickPackWallService.getDigitalLabelByLabel(ids, function(data){
              $scope.digitalLabelSource = {data: data, sort: {field: "name", dir: "asc"}}; // 数据源
              // 设置电子标签事件
              $scope.selectItems = []; // 已选项
              $scope.pickPackFieldTypes.forEach(function(fieldType){
                  // 电子标签正面
                  $("input[class~=front"+ fieldType.name+ "][data-role=combobox]").each(function(){ $scope.changeDigital(this); });
                  // 电子标签反面
                  $("input[class~=back"+ fieldType.name+ "][data-role=combobox]").each(function(){ $scope.changeDigital(this); });
              });
          });
      };
      // 数据源过滤
      $scope.filterSource = function(){
          for(var i=0, filters = []; i < $scope.selectItems.length; i++) filters.push({"field": "name", "operator": "neq", "value": $scope.selectItems[i]});
          //
          $scope.pickPackFieldTypes.forEach(function(fieldType){
              // 电子标签正面
              $("input[class~=front"+ fieldType.name+ "][data-role=combobox]").each(function(){ $(this).data("kendoComboBox").dataSource.filter(filters);});
              // 电子标签反面
              $("input[class~=back"+ fieldType.name+ "][data-role=combobox]").each(function(){ $(this).data("kendoComboBox").dataSource.filter(filters);});
          });
      };
      // 选择电子标签
      $scope.changeDigital = function(evt){
          var comboBox = $(evt).data("kendoComboBox");
          comboBox.bind("change", function(e){
              var lastText = $(this).attr("lastText"), text = this.text();
              if(lastText != "" && lastText != null){
                  var idx = $scope.selectItems.indexOf(lastText);
                  idx >= 0 && $scope.selectItems.splice(idx, 1);
              }
              $scope.selectItems.push(text);
              $(this).attr("lastText", text);
              $scope.filterSource(); // 过滤
          });
      };
      // 修改
      $scope.validate = function (event) {
          event.preventDefault();
          if ($scope.validator.validate()) {
              // 电子标签
              var digitalLabel1 = [], digitalLabel2 = [];
              $scope.pickPackFieldTypes.forEach(function(fieldType){
                  // 电子标签正面
                  $("input[class~=front"+ fieldType.name+ "][data-role=combobox]").each(function(){
                      var comboBox = $(this).data("kendoComboBox"), value = comboBox.value();
                      digitalLabel1.push(value!=""? value: null);
                  });
                  // 电子标签反面
                  $("input[class~=back"+ fieldType.name+ "][data-role=combobox]").each(function(){
                      var comboBox = $(this).data("kendoComboBox"), value = comboBox.value();
                      digitalLabel2.push(value!=""? value: null);
                  });
              });
              // 修改
              pickPackCellService.updateMore({
                  "name": $scope.pickPackWall.name,
                  "digitalLabel1": digitalLabel1,
                  "digitalLabel2": digitalLabel2
              },function(){
                  $state.go("main.pick_pack_cell");
              });
          }
      };
  });
})();