/**
 * Created by frank.zhou on 2017/04/18.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("userCtl", function ($scope, $rootScope, $window, Excel, commonService, systemService, masterService) {

    $window.localStorage["currentItem"] = "user";
    $rootScope.userGroupSource = systemService.getDataSource({key: "getUserGroup", text: "name", value: "id"});
    $rootScope.languageSource = systemService.getDataSource({
      key: "getSelectionBySelectionKey",
      value: "selectionValue",
      text: "resourceKey",
      data: {selectionKey: "LANGUAGE"}
    });
    /**************************************主界面的列****************************************************/
    var columns = [{
      field: "username",
      template: "<a ui-sref='main.userRead({id:dataItem.id})'>#: username # </a>",
      headerTemplate: "<span translate='USERNAME'></span>"
    },
      {field: "name", headerTemplate: "<span translate='NAME'></span>"},
      {
        field: "userGroup", headerTemplate: "<span translate='USERGROUP'></span>", template: function (item) {
        return item.userGroup ? item.userGroup.name : "";
      }
      },
      {field: "phone", headerTemplate: "<span translate='PHONE'></span>"},
      {field: "email", headerTemplate: "<span translate='EMAIL'></span>"},
      {field: "locale", headerTemplate: "<span translate='LOCALE'></span>"}];
    $scope.userGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: systemService.getGridDataSource("user")
    });

    /********************************导出start*************************************************************/
      //列名
    var columnsName = ["账号", "名称", "用户组", "电话", "邮箱", "传真"];
    //列宽
    var cellWidths1 = [150, 150, 150, 150, 200, 150];
    $scope.export = function () {
      Excel.exportToExcel("#userGrid", "user", cellWidths1, columnsName);
    };

    $scope.downloadTemplate=function(){
      getSelect({key: "getWarehouse", text: "name", value: "id"});
      getSelect({key: "getClient", text: "name", value: "id"});
      getSelect({key: "getSelectionBySelectionKey", text: "resourceKey", data: {selectionKey: "LANGUAGE"}});
      getSelect({key: "getUserGroup", text: "name", value: "id"});
      var columnName = ["仓库","客户","账号","密码","名称","邮箱","电话","语言","用户组"];
      var cellWidths = [100,100,100,100,100,150,150,100,200];
          exportToExcelLine("#userGrid","用户模板",cellWidths,columnName);
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
          var warehouse="",client="",language="",userGroup="";
          if(i==0) {
              for(var j=0;j<$scope.sysWarehouse.length;j++){
                  warehouse=warehouse+$scope.sysWarehouse[j].name+"/";
              }
             rowInfo.push({value:warehouse.substring(0,warehouse.length-1)})
           } else if(i==1) {
               for(var j=0;j<$scope.sysClient.length;j++){
                   client=client+$scope.sysClient[j].name+"/";
               }
               rowInfo.push({value:client.substring(0,client.length-1)})
           }else if(i==7) {
              for(var j=0;j<$scope.sysLanguage.length;j++){
                  language=language+$scope.sysLanguage[j].description+"/";
              }
              rowInfo.push({value:language.substring(0,language.length-1)})
          }else if(i==8) {
              for(var j=0;j<$scope.sysUserGroup.length;j++){
                  userGroup=userGroup+$scope.sysUserGroup[j].name+"/";
              }
              rowInfo.push({value:userGroup.substring(0,userGroup.length-1)})
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
          if(opts.key=="getWarehouse") $scope.sysWarehouse=datas;
          if(opts.key=="getClient") $scope.sysClient=datas;
          if(opts.data!=null&&opts.data.selectionKey=="LANGUAGE") $scope.sysLanguage=datas;
          if(opts.key=="getUserGroup") $scope.sysUserGroup=datas;
       });
    }
    /********************************导出end*************************************************************/
  }).controller("userCreateCtl", function ($scope, $state, systemService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        systemService.create("user", {
          "username": $scope.username,
          "password": $scope.password,
          "name": $scope.name,
          "email": $scope.email,
          "phone": $scope.phone,
          "locale": $scope.locale.selectionValue,
          "userGroupId": $scope.userGroup ? $scope.userGroup.id : null,
          "clientId": $scope.client ? $scope.client.id : null,
          "warehouseId": $scope.warehouse ? $scope.warehouse.id : null
        }, function () {
          $state.go("main.user");
        });
      }
    }
  }).controller("userUpdateCtl", function ($scope, $state, $stateParams, systemService) {
    systemService.read("user", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = (k == "locale" ? systemService.toMap(data[k]) : data[k]);
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        systemService.update("user", {
          "id": $scope.id,
          "username": $scope.username,
          "name": $scope.name,
          "email": $scope.email,
          "phone": $scope.phone,
          "locale": $scope.locale.selectionValue,
          "userGroupId": $scope.userGroup ? $scope.userGroup.id : null,
          "clientId": $scope.client ? $scope.client.id : null,
          "warehouseId": $scope.warehouse ? $scope.warehouse.id : null
        }, function () {
          $state.go("main.user");
        });
      }
    }
  }).controller("userReadCtl", function ($scope, $stateParams, systemService) {
    systemService.read("user", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = (k == "locale" ? systemService.toMap(data[k]) : data[k]);
    });
  })
})();