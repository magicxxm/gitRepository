/**
 * Created by frank.zhou on 2017/05/08.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("pickPackWallTypeCtl", function ($scope, $rootScope, $window, commonService, masterService) {
        // ===================================================pickPackWallType====================================================
        $window.localStorage["currentItem"] = "pickPackWallType";

        var columns = [
            {
                field: "name",
                template: "<a ui-sref='main.pickPackWallTypeRead({id:dataItem.id})'>#: name # </a>",
                headerTemplate: "<span translate='NAME'></span>"
            },
            {
                width: 350,
                headerTemplate: "<span translate='PICK_PACK_FIELD_TYPE'></span>",
                template: function (item) {
                    var pickPackFieldTypes = item.pickPackFieldTypes || [];
                    for (var i = 0, datas = []; i < pickPackFieldTypes.length; i = i + 2) {
                        var pickPackFieldType = pickPackFieldTypes[i], next = pickPackFieldTypes[i + 1];
                        var htmlStr = "<div style='margin:1px;'>";
                        pickPackFieldType && (htmlStr += "<div class='gridCellList'>" + pickPackFieldType.name + "</div>");
                        next && (htmlStr += "<div class='gridCellList' style='margin-left:5px;'>" + next.name + "</div>");
                        htmlStr += "</div>";
                        datas.push(htmlStr);
                    }
                    return datas.join("");
                }
            },
            {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
        ];
        $scope.pickPackWallTypeGridOptions = commonService.gridMushiny({
            columns: columns,
            dataSource: masterService.getGridDataSource("pickPackWallType")
        });

        // =================================================pickPackWallTypePosition===============================================
        // 函数
        function pickPackFieldEditor(container, options) {
            var source = masterService.getDataSource({key: "getPickPackFieldType", value: "id", text: "name"});
            masterService.selectEditor(container, options, source);
        }

        // stationTypePosition-column
        var stationTypePositionColumns = [
            {
                field: "orderIndex",
                editor: masterService.numberEditor,
                headerTemplate: "<span translate='ORDER_INDEX'></span>"
            },
            {
                field: "pickPackFieldType",
                editor: pickPackFieldEditor,
                headerTemplate: "<span translate='PICK_PACK_FIELD_TYPE'></span>",
                template: function (item) {
                    return item.pickPackFieldType ? item.pickPackFieldType.name : "";
                }
            }
        ];
        $rootScope.stationTypePositionGridOptions = masterService.editGrid({
            // height: Math.max(300, $rootScope.mainHeight - 20 - 34 * 2 - 10 - 20 - 20 - 40),
            columns: stationTypePositionColumns
        });

    }).controller("pickPackWallTypeCreateCtl", function ($scope, $state, masterService) {
        // 转数组
        $scope.toItems = function (number) {
            return new Array(number);
        };
        $scope.setCellIndex = function () {
            var data = $("#stationTypePositionGrid").data("kendoGrid").dataSource.data();
            if (data.length == 0) {
                return;
            } else {
                //对data按照orderIndex排序
                for (var c = 0; c < data.length; c++) {
                    for (var b = c; b < data.length; b++) {
                        var a1 = data[c].orderIndex;
                        var a2 = data[b].orderIndex;
                        var a3;
                        if (a1 > a2) {
                            a3 = data[c];
                            data[c] = data[b];
                            data[b] = a3;
                        }
                    }
                }
                var htmls=[];
                //获取rows的最大值
                var maxRows=data[0].pickPackFieldType.numberOfRows;
                for(var i=0;i<data.length;i++){
                    if(data[i].pickPackFieldType.numberOfRows>maxRows){
                        maxRows=data[i].pickPackFieldType.numberOfRows;
                    }
                }
                for(var i=0;i<data.length;i++){
                    var rows=data[i].pickPackFieldType.numberOfRows;
                    var columns=data[i].pickPackFieldType.numberOfColumns;
                    createHtml(data[i],rows,columns,htmls,maxRows);
                }
            }
        };

        function createHtml(data,rows,columns,htmls,maxRows){
            var sub=(maxRows-rows)*15;
            htmls.push("<div style='float:left;margin-top:"+ sub +"px'><table>");
            //var a=maxRows;
            for(var i=rows;i>0;i--){
                htmls.push("<tr>");
                for(var j=1;j<=columns;j++){
                   // var y=j;
                    htmls.push("<td><input width='20' min='0' yPos='"+i+"' xPos='"+j+"' orderIndex='"+data.orderIndex+"' fieldTypeId='"+data.pickPackFieldType.id+"' type='number' class='k-textbox wallTypePosition"+data.orderIndex+"' style='margin-left:0px;'/></td>");
                }
                htmls.push("</tr>");
            }
            htmls.push("</table></div>");
            htmls.push("<div style='width: 30px;'></div>");
            $("#pickPackCellPosition").html(htmls.join(""));
            //绑定明细input的改变事件
            $("input[class*=wallTypePosition]").bind("change", function (e) {
                $scope.cell=$(this);//当前的格子
                var text=$scope.cell.val();//当前填写的数
                var number = [];//明细中已经填写的数据
                var max=$(this).attr("orderIndex");
                $("input[class~=wallTypePosition"+max+"]").each(function () {
                    var text1=$(this).val();
                    if (text1===text) {
                        number.push(text);
                    }
                    var flag = 0;
                    for (var i = 0; i < number.length; i++) {
                        if (number[i] === text) {
                            flag++;
                            if (flag === 2) {
                                $scope.cell.val("");
                            }
                        }
                    }
                });
            });
        }

        //取CELL明细
        function getCellPosition(){
            var positions = [];
            $("input[class*=wallTypePosition]").each(function () {
                positions.push({
                    "orderIndex":$(this).attr("orderIndex"),
                    "fieldTypeId":$(this).attr("fieldTypeId"),
                    "position":parseInt($(this).val()),
                    "xPos":$(this).attr("xPos"),
                    "yPos":$(this).attr("yPos"),
                    "zPos":1
                });
            });
            return positions;
        }

        // 保存
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                var cellPositions=getCellPosition();
                masterService.create("pickPackWallType", {
                    "name": $scope.name,
                    "description": $scope.description,
                    "positions": cellPositions
                }, function () {
                    $state.go("main.pick_pack_wall_type");
                });
            }
        };
    }).controller("pickPackWallTypeUpdateCtl", function ($scope, $state, $stateParams, masterService) {
        masterService.read("pickPackWallType", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
            var grid = $("#stationTypePositionGrid").data("kendoGrid");
            var positions=data.positions;
            var allPositions=[];
            for(var k=0;k<positions.length;k++){
                allPositions.push(positions[k]);
            }
            for(var k=0;k<positions.length;k++){
                for(var m=k+1;m<positions.length;m++){
                    if(positions[k].orderIndex===positions[m].orderIndex){
                        positions.splice(m,1);
                        m--;
                    }
                }
            }
            grid.setDataSource(new kendo.data.DataSource({data: positions}));
            //创建表格
            var maxRows=allPositions[0].pickPackFieldType.numberOfRows;
            for(var i=0;i<allPositions.length;i++){
                if(allPositions[i].pickPackFieldType.numberOfRows>maxRows){
                    maxRows=allPositions[i].pickPackFieldType.numberOfRows;
                }
            }
            var htmls=[];
            for(var j=0;j<positions.length;j++){
                var rows=positions[j].pickPackFieldType.numberOfRows;
                var columns=positions[j].pickPackFieldType.numberOfColumns;
                createHtml(htmls,rows,columns,maxRows,allPositions,positions[j]);
            }
        });

        function createHtml(htmls,rows,columns,maxRows,positions,data){
            var sub=(maxRows-rows)*15;
            htmls.push("<div style='float:left;margin-top: "+sub+"px'><table>");
            //var a=maxRows;
            for(var i=rows;i>0;i--){
                htmls.push("<tr>");
                for(var j=1;j<=columns;j++){
                    if(positions.length>0) {
                        for (var k = 0; k < positions.length; k++) {
                            if (positions[k].xPos === j && positions[k].yPos === i && positions[k].orderIndex === data.orderIndex) {
                                htmls.push("<td><input width='20' min='0' type='number' yPos='" + i + "' xPos='" + j + "' orderIndex='" + data.orderIndex + "' fieldTypeId='" + data.pickPackFieldType.id + "' value='" + positions[k].position + "' class='k-textbox wallTypePosition"+data.orderIndex+"' style='margin-left:0px;'/></td>");
                            }
                        }
                    }else{
                        htmls.push("<td><input width='20' min='0' type='number' yPos='" + i + "' xPos='" + j + "' orderIndex='" + data.orderIndex + "' fieldTypeId='" + data.pickPackFieldType.id + "' class='k-textbox wallTypePosition"+data.orderIndex+"' style='margin-left:0px;'/></td>");
                    }
                }
                htmls.push("</tr>");
            }
            htmls.push("</table></div>");
            htmls.push("<div style='width: 30px;'></div>");
            $("#pickPackCellPosition").html(htmls.join(""));
            $("input[class*=wallTypePosition]").bind("change", function (e) {
                $scope.cell = $(this);//当前的格子
                var text = $scope.cell.val();//当前填写的数
                var number = [];//明细中已经填写的数据
                var max = $(this).attr("orderIndex");
                $("input[class~=wallTypePosition" + max + "]").each(function () {
                    var text1 = $(this).val();
                    if (text1 === text) {
                        number.push(text);
                    }
                    var flag = 0;
                    for (var i = 0; i < number.length; i++) {
                        if (number[i] === text) {
                            flag++;
                            if (flag === 2) {
                                $scope.cell.val("");
                            }
                        }
                    }
                });
            });
        }

        $scope.setCellIndex = function () {
            var data = $("#stationTypePositionGrid").data("kendoGrid").dataSource.data();
            if (data.length == 0) {
                return;
            } else {
                //对data按照orderIndex排序
                for (var c = 0; c < data.length; c++) {
                    for (var b = c; b < data.length; b++) {
                        var a1 = data[c].orderIndex;
                        var a2 = data[b].orderIndex;
                        var a3;
                        if (a1 > a2) {
                            a3 = data[c];
                            data[c] = data[b];
                            data[b] = a3;
                        }
                    }
                }
                var htmls=[];
                //获取rows的最大值
                var maxRows=data[0].pickPackFieldType.numberOfRows;
                for(var i=0;i<data.length;i++){
                    if(data[i].pickPackFieldType.numberOfRows>maxRows){
                        maxRows=data[i].pickPackFieldType.numberOfRows;
                    }
                }
                for(var i=0;i<data.length;i++){
                    var rows=data[i].pickPackFieldType.numberOfRows;
                    var columns=data[i].pickPackFieldType.numberOfColumns;
                    createHtml(htmls,rows,columns,maxRows,[],data[i]);
                }
            }
        };

        function getCellPosition(){
            var positions = [];
            $("input[class*=wallTypePosition]").each(function () {
                positions.push({
                    "orderIndex":$(this).attr("orderIndex"),
                    "fieldTypeId":$(this).attr("fieldTypeId"),
                    "position":parseInt($(this).val()),
                    "xPos":$(this).attr("xPos"),
                    "yPos":$(this).attr("yPos"),
                    "zPos":1
                });
            });
            console.log("位置---------",positions);
            return positions;
        }

        // 修改
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                var cellPositions=getCellPosition();
                masterService.update("pickPackWallType", {
                    "id": $scope.id,
                    "name": $scope.name,
                    "description": $scope.description,
                    "positions": cellPositions
                }, function () {
                    $state.go("main.pick_pack_wall_type");
                });
            }
        };
    }).controller("pickPackWallTypeReadCtl", function ($scope, $stateParams, masterService) {
        masterService.read("pickPackWallType", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
            var grid = $("#stationTypePositionGrid").data("kendoGrid");
            grid.setOptions({"editable": false});
            var positions=data.positions;
            var allPositions=[];
            for(var k=0;k<positions.length;k++){
                allPositions.push(positions[k]);
            }
            for(var k=0;k<positions.length;k++){
                for(var m=k+1;m<positions.length;m++){
                    if(positions[k].orderIndex===positions[m].orderIndex){
                        positions.splice(m,1);
                        m--;
                    }
                }
            }
            grid.setDataSource(new kendo.data.DataSource({data: positions}));
            //创建表格
            var maxRows=allPositions[0].pickPackFieldType.numberOfRows;
            for(var i=0;i<allPositions.length;i++){
                if(allPositions[i].pickPackFieldType.numberOfRows>maxRows){
                    maxRows=allPositions[i].pickPackFieldType.numberOfRows;
                }
            }
            var htmls=[];
            for(var j=0;j<positions.length;j++){
                var rows=positions[j].pickPackFieldType.numberOfRows;
                var columns=positions[j].pickPackFieldType.numberOfColumns;
                createHtml(htmls,rows,columns,maxRows,allPositions,positions[j]);
            }
        });

        function createHtml(htmls,rows,columns,maxRows,positions,data){
            var sub=(maxRows-rows)*15;
            htmls.push("<div style='float:left;margin-top:"+ sub +"px'><table>");
            for(var i=rows;i>0;i--){
                htmls.push("<tr>");
                for(var j=1;j<=columns;j++){
                    for(var k=0;k<positions.length;k++){
                        var y=j;
                        if(positions[k].xPos===y && positions[k].yPos===i && data.orderIndex===positions[k].orderIndex){
                            var position=positions[k].position;
                            if(position==undefined) {position="";}
                            htmls.push("<td><input width='20' value='"+position+"'class='k-textbox wallTypePosition' readonly= 'true' style='margin-left:0px;'/></td>");
                        }
                    }
                }
                htmls.push("</tr>");
            }
            htmls.push("</table></div>");
            htmls.push("<div style='width: 30px;'></div>");
            $("#pickPackCellPosition").html(htmls.join(""));
        }
    });
})();