/**
 * Created by 123 on 2017/11/8.
 */

(function () {
    "use strict";

    angular.module("myApp").service('lotManagerService', function (commonService, INTERNAL_TOOL_CONSTANT) {
        return {
            //
            getAllStockUnit : function (success,error) {
                commonService.ajaxMushiny({
                    url: INTERNAL_TOOL_CONSTANT.getStockByLot,
                    success: function (datas) {
                        success && success(datas.data);
                    },
                    error:function (datas) {
                        error && error(datas.data);
                    }
                });
            },

            //查询
            getByParam:function (param,success,error) {
                commonService.ajaxMushiny({
                    url: INTERNAL_TOOL_CONSTANT.getByParam+"?param="+param,
                    success: function (datas) {
                        success && success(datas.data);
                    }
                });
            }
        };
    });
})();