/**
 * Created by zhihan.dong on 2017/4/25.
 */


(function () {
    "use strict";

    angular.module("myApp").service('moveToolService', function (commonService, INTERNAL_TOOL_CONSTANT) {
        return {

            moveScanningSource: function (sourceName, success, error) {

                if (!sourceName) sourceName = '';
                commonService.ajaxMushiny({
                    url: INTERNAL_TOOL_CONSTANT.moveScanningSource + "?sourceName=" + sourceName,
                    success: function (datas) {
                        success && success(datas.data);
                    }
                });
            },

            moveScanningItemData: function (sourceId, sku, success, error) {
                commonService.ajaxMushiny({
                    url: INTERNAL_TOOL_CONSTANT.moveScanningItemData + "?sourceId=" + sourceId + "&sku=" + sku,
                    success: function (datas) {
                        success && success(datas.data);
                    }
                });
            },
            moveScanningDestination: function (sourceId, itemDataId, destinationName, success, error) {
                commonService.ajaxMushiny({
                    url: INTERNAL_TOOL_CONSTANT.moveScanningDestination + "?sourceId=" + sourceId + "&itemDataId=" + itemDataId + "&destinationName=" + destinationName,
                    success: function (datas) {
                        success && success(datas.data);
                    }
                });
            },
            moveMeasuring: function (data, success, error) {
                commonService.ajaxMushiny({
                    method: "POST",
                    url: INTERNAL_TOOL_CONSTANT.moveMeasuring,
                    data: data,
                    success: function (datas) {
                        success && success(datas.data);
                    }
                });
            }
        };
    });
})();