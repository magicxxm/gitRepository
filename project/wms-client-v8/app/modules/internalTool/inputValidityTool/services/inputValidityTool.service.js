/**
 * Created by zhihan.dong on 2017/04/24.
 * updated by zhihan.dong on 2017/05/04.
 */


(function () {
    "use strict";

    angular.module("myApp").service('inputValidityToolService', function (commonService, INTERNAL_TOOL_CONSTANT) {
        return {

            validityScanningSource: function (sourceName, success, error) {

                commonService.ajaxMushiny({
                    url: INTERNAL_TOOL_CONSTANT.validityScanningSource+"?sourceName="+sourceName,
                    success: function (datas) {
                        success && success(datas.data);
                    },
                    error: function (datas) {
                        error && error(datas.data);
                    }
                });
            },

            validityScanningItemData: function (sourceId,sku, success, error) {

                commonService.ajaxMushiny({
                    url: INTERNAL_TOOL_CONSTANT.validityScanningItemData + "?sourceId=" + sourceId+"&sku="+sku,
                    success: function (datas) {
                        success && success(datas.data);
                    },
                    error: function (datas) {
                        error && error(datas.data);
                    }
                });
            },
            validityEntering: function (source, success, error) {

                commonService.ajaxMushiny({
                    url: INTERNAL_TOOL_CONSTANT.validityEntering,
                    data:source,
                    method: "POST",
                    success: function (datas) {
                        success && success(datas.data);
                    },
                    error: function (datas) {
                        error && error(datas.data);
                    }
                });
            }
        };
    });
})();