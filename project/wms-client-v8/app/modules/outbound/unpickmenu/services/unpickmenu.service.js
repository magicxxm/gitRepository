/**
 * Created by zhihan.dong on 2017/04/24.
 * updated by zhihan.dong on 2017/05/04.
 */
(function () {
  "use strict";

  angular.module("myApp").service('unpickmenuService', function (commonService, OUTBOUND_CONSTANT,ICQA_CONSTANT) {
    return {
        searchUnpickMenuData:function (stationname,success) {
            commonService.ajaxMushiny({
                url:OUTBOUND_CONSTANT.getUnPickMenuData+"?stationName="+ stationname,
                success:function (datas) {
                    success&&success(datas.data);
                }
            });
        },
        isCallPod:function (podName,face,stationName,success) {
            commonService.ajaxMushiny({
                url: ICQA_CONSTANT.callPod + "?podName=" + podName + "&face=" + face + "&stationName=" + stationName + "&type=PickPod",
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        }
        
    };
  });


})();
