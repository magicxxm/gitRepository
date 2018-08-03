/**
 * Created by frank.zhou on 2017/04/21.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("itemDataCtl", function ($scope, $window, $rootScope, $state, Excel, itemDataService, commonService, masterService) {

    $window.localStorage["currentItem"] = "itemData";
    $scope.clientId;
    //
    $rootScope.columns = [
      {
        field: "itemNo",
        width: 140,
        template: "<a ui-sref='main.itemDataRead({id:dataItem.id})'>#: itemNo # </a>",
        headerTemplate: "<span translate='ITEM_NO'></span>"
      },
      {field: "skuNo", width: 140, headerTemplate: "<span translate='SKU_NO'></span>"},
      {field: "name", width: 180, headerTemplate: "<span translate='NAME'></span>"},
      //{field: "description", width:100, headerTemplate: "<span translate='DESCRIPTION'></span>"},
      {field: "safetyStock", width: 100, headerTemplate: "<span translate='SAFETY_STOCK'></span>"},
      {
        field: "itemGroup.name",
        width: 100,
        template: "<a ui-sref='main.item_group'>#: itemGroup.name # </a>",
        headerTemplate: "<span translate='ITEM_GROUP'></span>"
      },

      {field: "lotMandatory", width: 70, headerTemplate: "<span translate='LOT_MANDATORY'></span>"},
      //{field: "lotType", width:70, headerTemplate: "<span translate='LOT_TYPE'></span>"},
      //{field: "lotUnit", width:70, headerTemplate: "<span translate='LOT_UNIT'></span>"},
      {field: "lotThreshold", width: 65, headerTemplate: "<span translate='LOT_THRESHOLD'></span>"},
      {
        field: "itemUnit.name",
        width: 100,
        template: "<a ui-sref='main.item_unit'>#: itemUnit.name # </a>",
        headerTemplate: "<span translate='HANDING_UNIT'></span>"
      },

      {field: "measured", width: 70, headerTemplate: "<span translate='MEASURED'></span>"},
      {field: "width", width: 100, headerTemplate: "<span translate='LENGTH'></span><span>(mm)</span>"},
      {field: "depth", width: 100, headerTemplate: "<span translate='WIDTH'></span><span>(mm)</span>"},
      {field: "height", width: 100, headerTemplate: "<span translate='HEIGHT'></span><span>(mm)</span>"},
      //{field: "volume", width:100, headerTemplate: "<span translate='VOLUME'></span>"},
      {field: "size", width: 100, headerTemplate: "<span>Size</span>"},
      {field: "weight", width: 100, headerTemplate: "<span translate='WEIGHT'></span><span>(g)</span>"},

      {field: "multiplePart", width: 70, headerTemplate: "<span translate='MULTIPLE_PART'></span>"},
      {field: "multiplePartAmount", width: 65, headerTemplate: "<span translate='MULTIPLE_PART_AMOUNT'></span>"},
      {field: "preferOwnBox", width: 70, headerTemplate: "<span translate='PREFER_OWN_BOX'></span>"},
      {field: "preferBag", width: 70, headerTemplate: "<span translate='PREFER_BAG'></span>"},
      {field: "useBubbleFilm", width: 70, headerTemplate: "<span translate='USE_BUBBLE_FILM'></span>"},
      {field: "itemSellingDegree", width: 100, headerTemplate: "<span translate='ITEM_SELLING_DEGREE'></span>"}
    ];
    $scope.itemDataGridOptions = commonService.gridMushiny({
      columns: $rootScope.columns,
      dataSource: masterService.getGridDataSource("itemData")
    });
    /*************************************导出excel*************************************************************/
    $rootScope.excelColumns = [
      {
        field: "itemNo",
        width: 140,
        template: "<a ui-sref='main.itemDataRead({id:dataItem.id})'>#: itemNo # </a>",
        headerTemplate: "<span translate='ITEM_NO'></span>"
      },
      {field: "skuNo", width: 140, headerTemplate: "<span translate='SKU_NO'></span>"},
      {field: "name", width: 180, headerTemplate: "<span translate='NAME'></span>"},
      {field: "safetyStock", width: 100, headerTemplate: "<span translate='SAFETY_STOCK'></span>"},
      {
        field: "itemGroup.name",
        width: 100,
        template: "<a ui-sref='main.item_group'>#: itemGroup.name # </a>",
        headerTemplate: "<span translate='ITEM_GROUP'></span>"
      },

      {field: "lotMandatory", width: 70, headerTemplate: "<span translate='LOT_MANDATORY'></span>"},
      {
        field: "lotType", width: 90, headerTemplate: "<span translate='LOT_TYPE'></span>",
        template: function (dataItem) {
          return (dataItem.lotType ? (dataItem.lotType == "MANUFACTURE" ? "按生产日期" : dataItem.lotType == "EXPIRATION" ? "按到期日期" : "") : "")
        }
      },
      {
        field: "lotUnit",
        width: 70,
        headerTemplate: "<span translate='LOT_UNIT'></span>",
        template: function (dataItem) {
          return (dataItem.lotUnit ? (dataItem.lotUnit == "YEAR" ? "年" : dataItem.lotUnit == "MONTH" ? "月" : dataItem.lotUnit == "DAY" ? "日" : "") : "")
        }
      },
      {field: "lotThreshold", width: 65, headerTemplate: "<span translate='LOT_THRESHOLD'></span>"},
      {
        field: "itemUnit.name",
        width: 90,
        template: "<a ui-sref='main.item_unit'>#: itemUnit.name # </a>",
        headerTemplate: "<span translate='HANDING_UNIT'></span>"
      },

      {field: "measured", width: 80, headerTemplate: "<span translate='MEASURED'></span>"},
      {field: "width", width: 90, headerTemplate: "<span translate='LENGTH'></span><span>(mm)</span>"},
      {field: "depth", width: 90, headerTemplate: "<span translate='WIDTH'></span><span>(mm)</span>"},
      {field: "height", width: 90, headerTemplate: "<span translate='HEIGHT'></span><span>(mm)</span>"},
      {field: "volume", width: 95, headerTemplate: "<span translate='VOLUME'></span><span>（mm3）</span>"},
      {field: "size", width: 85, headerTemplate: "<span>Size</span>"},
      {field: "weight", width: 90, headerTemplate: "<span translate='WEIGHT'></span><span>(g)</span>"},

      {field: "multiplePart", width: 70, headerTemplate: "<span translate='MULTIPLE_PART'></span>"},
      {field: "multiplePartAmount", width: 65, headerTemplate: "<span translate='MULTIPLE_PART_AMOUNT'></span>"},
      {field: "preferOwnBox", width: 70, headerTemplate: "<span translate='PREFER_OWN_BOX'></span>"},
      {field: "preferBag", width: 70, headerTemplate: "<span translate='PREFER_BAG'></span>"},
      {field: "useBubbleFilm", width: 70, headerTemplate: "<span translate='USE_BUBBLE_FILM'></span>"},
      {field: "itemSellingDegree", width: 100, headerTemplate: "<span translate='ITEM_SELLING_DEGREE'></span>"},
      {field: "description", width: 100, headerTemplate: "<span translate='DESCRIPTION'></span>"},
      {field: "clientId", width: 100, headerTemplate: "<span>客户</span>"},
      {field: "warehouseId", width: 100, headerTemplate: "<span>仓库</span>"},
    ];

    $scope.downloadTemplate=function(){
      getSelect({key: "getClient", text: "name", value: "id"});
      var columnName = ["客户","itemNo"];
      var cellWidths = [150,150];
      exportToExcelLine("#itemDataGrid","SKU模板",cellWidths,columnName);
    };
    function exportToExcelLine(tableId, tableName, columnsWidths, columnsName) {
       var rows = [];
       var headCells = [];
       var cellWidths = [];
       for (var k = 0; k < columnsName.length; k++) {
           cellWidths.push({width: columnsWidths[k]});
           var cellValue = columnsName[k];
           if (cellValue != null) {
              headCells.push({value: cellValue});
          }
       }
      var headRow = {cells: headCells}; //标题行
      rows.push(headRow); //将标题行添加到rows集合中
      var rowInfo=[];
      for(var i = 0; i < columnsName.length; i++){
         var client="";
         if(i==0){
            for(var j=0;j<$scope.sysClient.length;j++){
               client=client+$scope.sysClient[j].name+"/";
            }
              rowInfo.push({value:client.substring(0,client.length-1)});
           }else{
             rowInfo.push({value:""});
         }
        }
       rows.push({cells:rowInfo});
       //创建工作册
       var workbook = new kendo.ooxml.Workbook({
           sheets: [{
              columns: cellWidths,
              title: tableName,
              rows: rows
            }]
        });
       //保存为excel文件
       kendo.saveAs({dataURI: workbook.toDataURL(), fileName: tableName + '.xlsx'});
    }
    function getSelect(opts){
       masterService.getSelectData(opts,function(datas){
           if(opts.key=="getClient") $scope.sysClient=datas;
       });
   }

    /**********************************************************************************************/
  }).controller("itemDataCreateCtl", function ($scope, $rootScope, $state, masterService) {
    // 选择itemDataGlobal
    $scope.selectItemDataGlobal = function () {
      $rootScope.selectInWindow({
        title: "ITEM_DATA_GLOBAL",
        srcKey: "itemDataGlobal",
        srcColumns: [
          {field: "skuNo", headerTemplate: "<span translate='SKU_NO'></span>"},
          {"field": "name", headerTemplate: "<span translate='NAME'></span>"},
          {field: "itemNo", headerTemplate: "<span translate='ITEM_NO'></span>"}
        ],
        init: function (options) {
          options.showSkuNo = true;
        },
        back: function (data) {
          $scope.itemDataGlobal = data;
        }
      });
    };
    // 保存
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("itemData", {
          "name": $scope.itemDataGlobal.name,
          "description": $scope.itemDataGlobal.description,
          "safetyStock": $scope.itemDataGlobal.safetyStock,
          "itemDataGlobalId": $scope.itemDataGlobal.id,
          "clientId": $scope.client ? $scope.client.id : null
        }, function () {
          $state.go("main.item_data");
        });
      }
    };
  }).controller("itemDataUpdateCtl", function ($scope, $rootScope, $stateParams, $state, masterService) {
    masterService.read("itemData", $stateParams.id, function (data) {
      for (var k in data) {
        if (data[k] === true) data[k] = "true";
        else if (data[k] === false) data[k] = "false";
        $scope[k] = (k === "serialRecordType" ? masterService.toMap(data[k]) : data[k]);
      }
      $scope.client = {id: data.clientId};
    });
    // 选择itemDataGlobal
    $scope.selectItemDataGlobal = function () {
      $rootScope.selectInWindow({
        title: "ITEM_DATA_GLOBAL",
        srcKey: "itemDataGlobal",
        srcColumns: [
          {field: "skuNo", headerTemplate: "<span translate='SKU_NO'></span>"},
          {"field": "name", headerTemplate: "<span translate='NAME'></span>"},
          {field: "itemNo", headerTemplate: "<span translate='ITEM_NO'></span>"}
        ],
        init: function (options) {
          options.showSkuNo = true;
        },
        back: function (data) {
          $scope.itemDataGlobal = data;
        }
      });
    };
    // 修改
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("itemData", {
          "id": $scope.id,
          "name": $scope.name,
          "description": $scope.description,
          "safetyStock": $scope.safetyStock,
          "itemDataGlobalId": $scope.itemDataGlobalId,
          "clientId": $scope.client ? $scope.client.id : null
        }, function () {
          $state.go("main.item_data");
        });
      }
    };
  }).controller("itemDataReadCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("itemData", $stateParams.id, function (data) {
      for (var k in data) {
        if (data[k] === true) data[k] = "true";
        else if (data[k] === false) data[k] = "false";
        $scope[k] = (k === "serialRecordType" ? masterService.toMap(data[k]) : data[k]);
      }
    });
  }).controller("itemDataExportCtl", function ($scope, $rootScope, Excel, commonService, masterService) {
    $scope.itemDataExportGridOptions = commonService.gridMushiny({
      columns: $rootScope.excelColumns,
      dataSource: masterService.getGridDataExportSource("itemData")
    });
    var columnsName = ["唯一编码", "商品编码", "名称", "安全库存", "货位分组", "有效期商品", "有效期类型", "有效期单位", "商品距离有效期最小值", "商品单位", "测量", "长（mm）",
      "宽（mm）", "高（mm）", "体积（mm3）", "Size", "重量（g）", "一套多件", "一套多件数量", "自带包装", "袋子优先", "试用气垫膜", "热销度", "描述","客户","仓库"];
    //列宽
    var cellWidths1 = [130, 130, 260, 90, 90, 100, 80, 80, 140, 90, 60, 70, 70, 70, 70, 70, 70, 90, 110, 90, 90, 90, 90, 100,90,90];
    $rootScope.exportData = function () {
      Excel.exportToExcel("#itemDataExportGrid", "itemData", cellWidths1, columnsName);
    };
  });
})
();