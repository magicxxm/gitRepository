(function () {
  "use strict";

  angular.module("myApp").service('totStatisticsDetailService', function () {
      // 查询条件是天=1；时间段为=2

      this.format = function (fmt,date)
      {
          var o = {
              "M+": date.getMonth() + 1, //月份
              "d+": date.getDate(), //日
              "h+": date.getHours(), //小时
              "m+": date.getMinutes(), //分
              "s+": date.getSeconds(), //秒
              "q+": Math.floor((date.getMonth() + 3) / 3), //季度
              "S": date.getMilliseconds() //毫秒
          };
          if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
          for (var k in o)
              if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
          return fmt;
      };
    this.timeType=1;
    this.dayDate=this.format('yyyy-MM-dd',new Date());
    this.startDate='';
    this.endDate='';
    this.employeeName='';
    this.employeeCode='';
    this.warehouseId='';
    this.clientId='';


  });
})();
