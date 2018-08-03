/**
 * Created by yaohua.di on 2017/6/12.
 */
(function () {
  "use strict";

  angular.module("myApp").service('jobrecordService', function (commonService,$httpParamSerializer,TOT_CONSTANT) {
    return{

        validitySource: function (employeeCodeName, success, error) {
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.validitySource+"?employeeCode="+employeeCodeName,
                success: function (datas) {
                    success && success(datas.data);
                },
                error: function (datas) {
                    error && error(datas.data);
                }
            });
        },
      //扫描员工卡号和间接条码
      checkEmployeeCodeAndJobCode: function (employeeCode,jobCode,success,error) {
        commonService.ajaxMushiny({
          url: TOT_CONSTANT.checkEmployeeCodeAndJobCode+"?employeeCode="+employeeCode+"&jobCode="+jobCode,
          success:function (datas) {
            success && success(datas.data);
          },error:function (datas) {
            error && error(datas.data);
            }
        });
      }


    }
  });
})();