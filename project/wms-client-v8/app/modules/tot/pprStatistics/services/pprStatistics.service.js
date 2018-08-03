(function () {
  "use strict";

  angular.module("myApp").service('pprStatisticsService', function (commonService, TOT_CONSTANT) {
    return {
      // 取tot statistics data
        getPprStatisticsData: function (success,searchParams) {
        commonService.ajaxMushiny({
          url: TOT_CONSTANT.getPprStatisticsData, params:searchParams,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
        getRecordsForPprDetail: function (success,searchParams) {
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.getRecordsForPprDetail, params:searchParams,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        queryCtimedetail: function (userName,cb) {
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.queryCtimedetail + "?warehouseId=DEFAULT&clientId=SYSTEM&startDate="+"2017-06-26 00:00:00"+"&endDate="+"2017-06-26 23:59:59"+"&userName="+userName,
                success: function (datas) {
                    cb && cb(datas.data);
                }
            });
        },
        searchCtimedetail : function (success,warehouseId,clientId,dayDate,startDate,endDate,userName) {
          //  alert("serve::::::"+warehouseId+","+clientId+","+dayDate+","+startDate+","+endDate+","+userName);
            var getString = '?';
            if(warehouseId)getString = getString + "warehouseId=" + warehouseId;
            if(clientId)getString = getString + "&clientId=" + clientId;
            if(dayDate)getString = getString + "&dayDate=" + dayDate;
            if(startDate)getString = getString + "&startDate=" + startDate;
            if(endDate)getString = getString + "&endDate=" + endDate;
            if(userName)getString = getString + "&userName=" + userName;
            if(typeof(warehouseId) =="undefined" && typeof(clientId) =="undefined" && typeof(dayDate) =="undefined"
                && typeof(startDate) =="undefined" && typeof(endDate) =="undefined" && typeof(userName) =="undefined" )
            {
                getString ='';
            }
          //   alert("endgetString=====:"+getString);
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.queryCtimedetail+getString,
                success: function (datas) {
                    success && success(datas.data);
                }
            });

        },
        getJobTotal: function (cb,employeeCode, warehouseId,clientId,startTime,endTime) {
            //alert("serve:"+warehouseId+","+clientId+","+startDate+","+endDate+","+userName)
            var getString = "/"+ employeeCode+"/"+warehouseId+"/"+clientId+"/"+startTime+"/"+endTime;
            //   alert("endgetString=====:"+getString);
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.getJobTotal+getString,
                success: function (datas) {
                    cb && cb(datas.data);
                }
            });
        },
        getJobDetail: function (cb,employeeCode, warehouseId,clientId,startTime,endTime) {
            //alert("serve:"+warehouseId+","+clientId+","+startDate+","+endDate+","+userName)
            var getString = "/"+ employeeCode+"/"+warehouseId+"/"+clientId+"/"+startTime+"/"+endTime;
            //   alert("endgetString=====:"+getString);
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.getJobDetail + getString,
                success: function (datas) {
                    cb && cb(datas.data);
                }
            });
        },
        getUserInfo: function (employeeCode,success,error) {
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.getUserInfo+"?employeeCode="+employeeCode,
                success:function (datas) {
                    success && success(datas.data);
                },error:function (datas) {
                    alert("error");
                }
            });
        },
        // 取客户
        getClientByWarehouse: function(warehouseId, cb){
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.getClientInfoByWarehouse+"?warehouseId="+ warehouseId,
                success: function(datas){
                    cb && cb(datas.data);
                }
            });
        },
        getWeekOfMouth: function(successFun, dateTime){
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.getWeekOfMonth+"?dateTime="+ dateTime,
                success: function(datas){
                    successFun && successFun(datas.data);
                }
            });
        }
    };
  });
})();
