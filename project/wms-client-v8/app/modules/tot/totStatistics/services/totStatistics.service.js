(function () {
  "use strict";

  angular.module("myApp").service('totStatisticsService', function (commonService, TOT_CONSTANT) {
    return {
      // 取tot statistics data
        getTotStatisticsData: function (success,param) {

        commonService.ajaxMushiny({
          url: TOT_CONSTANT.getTotStatisticsData,
          params:param,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
        queryCtimedetail: function (cb,param) {
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.queryCtimedetail ,
                params:param,
                success: function (datas) {
                    cb && cb(datas.data);
                }
            });
        },
        searchCtimedetail : function (success,param) {

            commonService.ajaxMushiny({
                url: TOT_CONSTANT.queryCtimedetail,
                params:param,
                success: function (datas) {
                    success && success(datas.data);
                }
            });

        },
        searchClockTimedetail : function (success,params) {

            commonService.ajaxMushiny({
                url: TOT_CONSTANT.searchClockTimedetail,
                params:params,
                success: function (datas) {
                    success && success(datas.data);
                }
            });

        },
        deleteClockTimedetail : function (success,params) {

            commonService.ajaxMushiny({
                url: TOT_CONSTANT.deleteClockTimedetail,
                params:params,
                success: function (datas) {
                    success && success(datas.data);
                }
            });

        },
        addClockTimedetail : function (success,params) {

            commonService.ajaxMushiny({
                url: TOT_CONSTANT.addClockTimedetail,
                params:params,
                success: function (datas) {
                    success && success(datas.data);
                }
            });

        },
        getJobTotal: function (cb,params) {

            commonService.ajaxMushiny({
                url: TOT_CONSTANT.getJobTotal,
                params:params,
                success: function (datas) {
                    for (var i=0;i<datas.data.length;i++)
                    {
                        datas.data[i].entityLock = datas.data[i].entityLock == 0 ? "正常" : (datas.data[i].entityLock == 1 ? "问题":"重包")
                    }
                    cb && cb(datas.data);
                }
            });
        },
        getJobDetail: function (cb,params) {
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.getJobDetail,
                params:params,
                success: function (datas) {
                    for (var i=0;i<datas.data.length;i++)
                    {
                        datas.data[i].entityLock = datas.data[i].entityLock == 0 ? "正常" : (datas.data[i].entityLock == 1 ? "问题":"重包")
                    }
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
        getWareHouseAndClient:function(cb,param)
        {
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.getWareHouseAndClient,
                params:param || {},
                success: function(datas){
                    cb && cb(datas.data);
                }
            });
        },
        getWarehouse:function (cb) {
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.getWarehouse,
                success: function(datas){
                    cb && cb(datas.data);
                }
            });
        },
        getClientByCurrentWarehouse:function (cb) {
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.getClientByCurrentWarehouse,
                success: function(datas){
                    cb && cb(datas.data);
                }
            });
        },
        // 取数据源(特殊)
        getDataSourceMy: function (opts) {
            opts.value == null && (opts.value = "id");
            return {
                serverFiltering: false,
                transport: {
                    read: function(options){
                        commonService.ajaxSync({
                            url: opts.key,
                            async: true,
                            data: opts.data,
                            success: function(result){
                                options.success(result);
                            }
                        });
                    }
                },
                schema: {
                    data: function(datas){
                        for(var i = 0, items = []; i < datas.length; i++){
                            var data = datas[i], itemMap = {id: data.id};
                            itemMap[opts.text] = (opts.text === "resourceKey"? $translate.instant(data[opts.text]): data[opts.text]);
                            itemMap[opts.value] = data[opts.value];
                            items.push(angular.extend(data, itemMap));
                        }
                        return items;
                    }
                }
            };
        },
        //扫描员工卡号和间接条码
        addIndirectJobRecord: function (param,success,error) {
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.addIndirectJobRecord,
                params:param,
                success:function (datas) {
                    success && success(datas.data);
                },error:function (datas) {
                    error && error(datas.data);
                }
            });
        }
    };
  });
})();
