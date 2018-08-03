/**
 * Created by PC-7 on 2017/3/14.
 */


(function () {
  "use strict";

  angular.module("myApp").service('measureService', function ($http, commonService, INTERNAL_TOOL_CONSTANT) {
    return {
      /*         measureToolmeasure: function (source, success, error) {
       commonService.ajaxMushiny({
       url: INTERNAL_TOOL_CONSTANT.inWeight + source,
       success: function (datas) {
       success && success(datas.data);
       },
       error: function (datas) {
       error && error(datas.data);
       }
       });
       },*/
      getSizeFilterRule: function (success) {
        commonService.ajaxMushiny({
          url: INTERNAL_TOOL_CONSTANT.getSizeFilterRule,
          success: function (datas) {
            success && success(datas.data);
          },
        });
      },
      //临时
      measureToolmeasure: function (source, success, error) {
        $http.get(INTERNAL_TOOL_CONSTANT.inWeight + source).then(success)
      },
      saveMeasure: function (data, success, error) {
        commonService.ajaxMushiny({
          method: "POST",
          data: data,
          url: INTERNAL_TOOL_CONSTANT.saveMeasure,
          success: function (datas) {
            success && success(datas.data);
          },
          error: function (datas) {
            error && error(datas.data);
          }
        });
      },
      scanningSource: function (source, success, error) {
        commonService.ajaxMushiny({
          url: INTERNAL_TOOL_CONSTANT.scanningSource + "?sourceName=" + source,
          success: function (datas) {
            success && success(datas.data);
          },
          error: function (datas) {
            error && error(datas.data);
          }
        });
      },
      scanningDestination: function (source, itemDataId, destinationName, success, error) {
        commonService.ajaxMushiny({
          url: INTERNAL_TOOL_CONSTANT.scanningDestination + "?sourceId=" + source + "&itemDataId=" + itemDataId + "&destinationName=" + destinationName,
          success: function (datas) {
            success && success(datas.data);
          },
          error: function (datas) {
            error && error(datas.data);
          }
        });
      },
      scanningItemData: function (sku, source, success, error) {
        commonService.ajaxMushiny({
          url: INTERNAL_TOOL_CONSTANT.scanningItemData + "?sku=" + sku + "&sourceId=" + source,
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