/**
 * Created frank.zhou on 2017/04/17.
 */
(function(){
  "use strict";

  angular.module("myApp").service("loginService", function($http, $httpParamSerializer, commonService, BACKEND_CONFIG, LOGIN_CONSTANT){
    return {
      // listView
      getJsonForListView: function(options){
        options.template == null && (options.template = "#:name#");
        return {
          dataSource: {data: options.datas},
          selectable: options.selectable || "multiple",
          template: "<div style='height:20px;vertical-align:middle;padding:4px;'>"+ options.template+ "</div>",
          change: function(){
            options.change && options.change(this);
          }
        };
      },
      getUserWarehouse: function(username, password, success, error){
        $http({
          url: BACKEND_CONFIG.login + LOGIN_CONSTANT.getUserWarehouse,
          method: "POST",
          headers: {
            "Content-Type": "application/json;",
            "Accept": "application/json"
          },
          data: {
            "username": username,
            "password": password
          }
        }).then(function(datas){
          success && success(datas.data);
        }, function(){
          error && error();
        });
      },
      doLogin: function(username, password, warehouseId, success){
        $http({
          url: BACKEND_CONFIG.login+ LOGIN_CONSTANT.login,
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded;charset=utf-8",
            "Accept": "application/json",
            "Warehouse": warehouseId
          },
          data: $httpParamSerializer({
            "username": username,
            "password": password,
            "grant_type": "password",
            "scope": "ui",
            "client_id": "web_app"
          })
        }).then(function(datas){
          success && success(datas.data)
        });
      }
    };
  });
})();