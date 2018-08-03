/**
 * TransOutSystem service js
 * Created by preston.zhang on 2017/8/18.
 */
(function () {
   "use strict";
   angular.module("myApp").service("transOutService",function (commonService, $httpParamSerializer,TRANSOUT_CONSTANTS) {
      //获取调拨配置
       function getTransOutConfigList(outWare,accept,success) {
           commonService.ajaxMushiny({
               url:TRANSOUT_CONSTANTS.getTransOutConfigList+"?outWareHouse="+outWare+"&acceptWareHouse="+accept,
               success:function (datas) {
                   success&&success(datas.data);
               }
           });
       }
       //创建调拨配置
       function CreateTransOutConfig(data,success){
           commonService.ajaxMushiny({
               url:TRANSOUT_CONSTANTS.CreateTransOutConfig,
               data:data,
               method:"POST",
               success:function (datas) {
                   success&&success(datas.data);
               }
           });
       }
       //根据id获取调拨配置
       function ReadTransOutConfig(id,success) {
           commonService.ajaxMushiny({
               url:TRANSOUT_CONSTANTS.ReadTransOutConfig+"/"+id,
               success:function (datas) {
                   success&&success(datas.data);
               }
           });
       }
       //更新调拨配置
       function UpdateTransOutConfig(data,success) {
           commonService.ajaxMushiny({
               url:TRANSOUT_CONSTANTS.UpdateTransOutConfig,
               method:"POST",
               data:data,
               success:function (datas) {
                   success&&success(datas.data);
               }
           });
       }
       //删除调拨配置
       function DeleteTransOutConfig(id,success) {
           commonService.ajaxMushiny({
               url:TRANSOUT_CONSTANTS.DeleteTransOutConfig+"/"+id,
               method:"DELETE",
               success:function (datas) {
                   success&&success(datas.data);
               }
           });
       }
       //导入调拨配置数据
       function importTransOutConfigData(multifile,success) {
           commonService.ajaxMushiny({
               url:TRANSOUT_CONSTANTS.importTransOutConfigData,
               method:"POST",
               data:multifile,
               success:function (datas) {
                   success&&success(datas.data);
               }
           });
       }
       //调拨详情页面数据
       function transOutDetail(No,success) {
           commonService.ajaxMushiny({
               url:TRANSOUT_CONSTANTS.transOutDetail+"?No="+No,
               success:function (datas) {
                   success&&success(datas.data);
               }
           });
       }
       //调拨出主页面数据
       function transOutMainPage(startTime,endTime,searchOption,success) {
           commonService.ajaxMushiny({
               url:TRANSOUT_CONSTANTS.transOutMainPage+"?startTime="+startTime+"&endTime="+endTime+"&searchTerm="+searchOption,
               success:function (datas) {
                   success&&success(datas.data);
               }
           });
       }
       return {
           getTransOutConfigList:getTransOutConfigList,
           ReadTransOutConfig:ReadTransOutConfig,
           UpdateTransOutConfig:UpdateTransOutConfig,
           DeleteTransOutConfig:DeleteTransOutConfig,
           importTransOutConfigData:importTransOutConfigData,
           CreateTransOutConfig:CreateTransOutConfig,
           transOutDetail:transOutDetail,
           transOutMainPage:transOutMainPage
       }
   });
})();