/**
 * Created by preston.zhang on 2017/8/18.
 */
(function () {
    "use strict";
    angular.module("myApp").controller("transferStockOutConfigCtl",function ($rootScope,$scope,$window,$http,outboundService,transOutService,commonService,TRANSOUT_CONSTANTS) {
        $window.localStorage["currentItem"] = "transferStockOutConfig";
        $scope.out = "";
        $scope.accpet = "";
        var columns = [
            {field:"id",width:80,template:"<a ui-sref='main.transferStockOutConfigRead({id:dataItem.id})'>#: id # </a>",headerTemplate:"<span translate='ID'></span>"},
            {field:"out_warehouse",width:80,headerTemplate:"<span translate='发货库房'></span>"},
            {field:"accept_warehouse",width:80,headerTemplate:"<span translate='接收库房'></span>"},
            {field:"out_time",width:80,headerTemplate:"<span translate='发货时间'></span>"},
            {field:"trans_time",width:80,headerTemplate:"<span translate='运输时间'></span>"},
            {field:"arriveTime",width:80,headerTemplate:"<span translate='预计到货时间'></span>"}];
        $scope.transferStockOutConfigGridOptions=outboundService.reGrids(TRANSOUT_CONSTANTS.getTransOutConfigList,columns,$(document.body).height()-203);
        $scope.search = function () {
            transOutService.getTransOutConfigList($scope.out,$scope.accpet,function (data) {
                var grid=$("#transferStockOutConfigGrid").data("kendoGrid");
                grid.setOptions({dataSource:data});
            });
        };
        $scope.search();
        $scope.import = function (key) {
            commonService.dialogMushiny($scope.window, {
                title: "<span>导入</span>",
                width: 400,
                height: 280,
                url: "modules/masterData/base/templates/ImportWindow.html",
                open: function () {
                    // 确认选择
                    $rootScope.importToSql = function (win) {
                        importUploading(key);
                        win.close();
                    };
                }
            });
        };

        $scope.export = function (key) {
            commonService.exportFile(key,{
                url:TRANSOUT_CONSTANTS.exportTransOutConfigData
            });
        };

        function importUploading(key) {
            var fd = new FormData();
            var file = document.querySelector('input[type=file]').files[0];
            fd.append('file', file);
            var fileName = $("#fileId").val();
            if (fileName == '') {
                alert("请选择excel,再上传");
            } else if (fileName.lastIndexOf(".xls") < 0) {//可判断以.xls和.xlsx结尾的excel
                alert("请选择excel,再上传");
            } else {
                if(key === 'transout'){
                    commonService.importAjaxAsync(key,{
                        url:TRANSOUT_CONSTANTS.importTransOutConfigData,
                        data:fd
                    });
                }
            }
        }

    }).controller("transferStockOutConfigCreateCtl", function ($scope, $state,transOutService){
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                transOutService.CreateTransOutConfig({
                    "out_warehouse":$scope.out_warehouse,
                    "accept_warehouse":$scope.accept_warehouse,
                    "out_time":$scope.out_time,
                    "trans_time":$scope.trans_time,
                    "arriveTime":(function () {
                        var hour = parseInt($scope.out_time.substr(0,2))+$scope.trans_time;
                        if(hour<10){
                            hour="0"+hour;
                        }
                        return hour+":"+$scope.out_time.substr(3);
                    })()
                }, function () {
                    $state.go("main.transferStockOutConfig");
                });
            }
        };
    }).controller("transferStockOutConfigReadCtl",function ($scope, $state,$window, $stateParams,transOutService) {
        transOutService.ReadTransOutConfig($stateParams.id,function (data) {
            for(var k in data) $scope[k] = data[k];
        });
    }).controller("transferStockOutConfigUpdateCtl",function ($scope, $state,$window, $stateParams,transOutService) {
        transOutService.ReadTransOutConfig($stateParams.id,function (data) {
            for(var k in data) {
                $scope[k] = data[k];
            };
        });
        $scope.arriveTime = kendo.format("{0:HH:mm:ss}",new Date(new Date().setDate(new Date(new Date().setDate($scope.shippingDate)).getHours()+$scope.transTime)));
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                transOutService.UpdateTransOutConfig({
                    "id":$stateParams.id,
                    "out_warehouse":$scope.out_warehouse,
                    "accept_warehouse":$scope.accept_warehouse,
                    "out_time":$scope.out_time,
                    "trans_time":$scope.trans_time,
                    "arriveTime":(function () {
                        var hour = parseInt($scope.out_time.substr(0,2))+$scope.trans_time;
                        if(hour<10){
                            hour="0"+hour;
                        }
                        return hour+":"+$scope.out_time.substr(3);
                    })()
                }, function () {
                    $state.go("main.transferStockOutConfig");
                });
            }
        };
    });
})();