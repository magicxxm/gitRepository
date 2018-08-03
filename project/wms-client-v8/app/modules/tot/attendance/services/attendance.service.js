/**
 * Created by yaohua.di on 2017/6/12.
 */
(function () {
  "use strict";

  angular.module("myApp").service('attendanceService', function (commonService,$httpParamSerializer,TOT_CONSTANT) {
    return{
      //扫描员工卡号
      checkEmployeeCode: function (employeeCode,success,error) {
        commonService.ajaxMushiny({
          url: TOT_CONSTANT.checkEmployeeCode+"?employeeCode="+employeeCode,
            success: function (datas) {
                success && success(datas.data);
            },
            error: function (datas) {
                error && error(datas.data);
            }
        });
      },
      //获取考核表信息
       getAttendanceInfo: function (employeeCode,success,error) {
              commonService.ajaxMushiny({
                url: TOT_CONSTANT.getAttendanceInfo+"?employeeCode="+employeeCode,
                success:function (datas) {
                alert("type:"+datas.clockMethod);
                alert("time:"+datas.clockTime);
                },error:function (datas) {
               // alert(33)
                            }
              });
        }
    }
  });
})();