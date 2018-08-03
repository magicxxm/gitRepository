/**
 * Created by hgf on 2017/07/10.
 */
(function () {
  "use strict";
  angular
    .module("myApp").factory('Excel', function ($window) {
    return {
      //参数为表id 表名，列宽，列名，文件名跟能表名同名
      exportToExcel: function (tableId, tableName, columnsWidths, columnsName) {
        var grid = $(tableId).data("kendoGrid");
        if (grid.items().length > 0) {//判断列表中是否存在有效数据
          var rows = [];
          var headCells = [];
          var cellWidths = [];
          for (var k = 0; k < grid.columns.length; k++) {
            cellWidths.push({width: columnsWidths[k]});
            var cellValue = columnsName[k];
            if (cellValue != null) {
              headCells.push({value: cellValue});
            }
          }
          var headRow = {cells: headCells}; //标题行
          rows.push(headRow); //将标题行添加到rows集合中
          var cRows = grid.items(); //获取当前列表内容的行数
          for (var i = 0; i < cRows.length; i++) {
            var rowItem = cRows[i]; //获取列表的当前行
            var data = grid.dataItem(rowItem); //获取列表的当前行的数据源
            var currentRow = []; //创建当前行的数组变量
            for (var j = 0; j < rowItem.cells.length; j++) {
              var a = $(rowItem.cells[j]).find("a").html();
              var sp = $(rowItem.cells[j]).find("span").html();
              var td = $(rowItem.cells[j]).html();
              var currentCell = a == undefined ? (sp == undefined ? (td == undefined ? '' : td) : sp) : a;
              if (currentCell != null) {
                currentRow.push({value: currentCell});
              }
            }
            rows.push({cells: currentRow}); //添加行元素
          }
          //创建工作册
          var workbook = new kendo.ooxml.Workbook({
            sheets: [{
              columns: cellWidths,
              title: tableName,
              rows: rows
            }]
          });
          //保存为excel文件
          kendo.saveAs({dataURI: workbook.toDataURL(), fileName: tableName + 'Export.xlsx'});
        } else {
          alert("无有效数据需要导出！");
          return;
        }
      },
      //导出列名到excel
      exportToExcelLine: function (tableId, tableName, columnsWidths, columnsName) {
        var grid = $(tableId).data("kendoGrid");
          var rows = [];
          var headCells = [];
          var cellWidths = [];
          for (var k = 0; k < grid.columns.length; k++) {
            cellWidths.push({width: columnsWidths[k]});
            var cellValue = columnsName[k];
            if (cellValue != null) {
              headCells.push({value: cellValue});
            }
          }
          var headRow = {cells: headCells}; //标题行
          rows.push(headRow); //将标题行添加到rows集合中

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
      },
    }
  });
})();