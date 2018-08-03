/**
 * Created by 123 on 2017/11/8.
 */
(function () {
    "use strict";

    angular.module("myApp").service('stockUnitMeasureService', function (commonService, INTERNAL_TOOL_CONSTANT) {
        return {
            //
            getAllStockUnit : function (success,error) {
                commonService.ajaxMushiny({
                    url: INTERNAL_TOOL_CONSTANT.getAllStockUnit,
                    success: function (datas) {
                        success && success(datas.data);
                    },
                    error:function (datas) {
                        error && error(datas.data);
                    }
                });
            },
            getItemdata:function (itemNo,client,success,error) {
                commonService.ajaxMushiny({
                    url: INTERNAL_TOOL_CONSTANT.getItemdata + "?itemNo="+itemNo+"&clientName="+client,
                    success: function (datas) {
                        success && success(datas.data);
                    },
                    error:function (datas) {
                        error && error(datas.data);
                    }
                });
            },
            getItemRecords: function (sku, success,error) {
                commonService.ajaxMushiny({
                    url: INTERNAL_TOOL_CONSTANT.getItemRecords + "?sku=" + sku,
                    success: function (datas) {
                        success && success(datas.data);
                    },
                    error: function (datas) {
                        error && error(datas.data);
                    }
                });
            },
            getStockUnits:function(success){
                commonService.ajaxMushiny({
                    url: INTERNAL_TOOL_CONSTANT.getStockUnitMeasure,
                    success: function (datas) {
                        success && success(datas.data);
                    }
                });
            },
            getByParamSearch:function(param,success){
                commonService.ajaxMushiny({
                    url: INTERNAL_TOOL_CONSTANT.getByParamSearch+"?param="+param,
                    success: function (datas) {
                        success && success(datas.data);
                    }
                });
            }
        };
    });
})();