/**
 * Created by frank.zhou on 2017/04/14.
 * Updated by frank.zhou on 2017/04/17.
 */
(function(){
  "use strict";

  angular.module("myApp").service("commonService", function($http,$window,$websocket,$rootScope, $translate,$httpParamSerializer, BACKEND_CONFIG){

      function doLogin(username, password, warehouseId, success){
          $http({
              url: BACKEND_CONFIG.login+ "uaa/oauth/token",
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
    function install(edit,params)
    {
            $rootScope.dataStream = $websocket(BACKEND_CONFIG.webSocket+'wcs-manage-server-v8/showInstallModuleLog');
            $rootScope.dataStream.send(params);
            $rootScope.dataStream.onMessage(function(message) {
                edit.exec("inserthtml", { value: "<p>"+message.data+"</p>" });

            });

    }
      function ajaxSync(options){
          // 临时
          var preUrl = "";
          if(options.url.indexOf("driver") >= 0) preUrl=BACKEND_CONFIG.driver;
          if(options.url.indexOf("path") >= 0) preUrl=BACKEND_CONFIG.path;
          if(options.url.indexOf("stop") >= 0) preUrl=BACKEND_CONFIG.main+"wcs-manage-server-v8/";
          if(options.url.indexOf("download") >= 0) preUrl=BACKEND_CONFIG.main+"wcs-manage-server-v8/";
          if(options.url.indexOf("getAllPackage") >= 0) preUrl=BACKEND_CONFIG.main+"wcs-manage-server-v8/";

          $.ajax({
              type: options.type || "GET",
              url:preUrl+ options.url,
              cache: false,
              async: (options.async!=null? options.async: false),
              dataType: options.dataType || "json",
              contentType: options.contentType || "application/json;charset=utf-8",
              data: options.data || {},
              beforeSend: function(XMLHttpRequest){
                  XMLHttpRequest.setRequestHeader("Authorization", "Bearer "+ $window.localStorage["accessToken"]);
              },
              success: function(data){
                  options.success && options.success(data);
              },
              error: function(){
                  options.error()
              }
          });
      }
    return {

        install:install,
        doLogin:doLogin,
        ajaxSync:ajaxSync
    };
  });
})();