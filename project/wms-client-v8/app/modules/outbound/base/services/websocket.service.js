/**
 * Created by feiyu.pan on 2017/4/20.
 */
(function(){
  "use strict";

  angular.module("myApp").service("webSocketService", function($translate,$http, commonService,BACKEND_CONFIG){
      function initSocket(option) {
          //服务器地址
          var url = BACKEND_CONFIG.websocket+option.url;
          console.log("url:",url);
          var onmessageCall = option.onmessageCall;
          if (typeof onmessageCall != "function") {
              console.log('onmessageCall 必须为函数');
              return false;
          }
          var websocket = new ReconnectingWebSocket(url);
          var date = new Date();
          console.log("时间"+date.toLocaleString());
          //连接发生错误的回调方法
          websocket.onerror = function (msg) {
              console.log(date.toLocaleString()+"->连接websocket服务器出错");
          };
          //连接成功建立的回调方法
          websocket.onopen = function (event) {
              console.log(date.toLocaleString()+"->websocket连接已经打开");

          }
          //接收到消息的回调方法
          websocket.onmessage = function (event) {
              onmessageCall(event.data);
          }

          //连接关闭的回调方法
          websocket.onclose = function (msg) {
              console.log(date.toLocaleString()+"->websocket连接关闭");
          }
          //每隔2分钟重连
          websocket.reconnectInterval = 1000 * 60 * 2;
          //最大重连次数
          websocket.maxReconnectAttempts = 3;
          websocket.reconnectDecay = 2;
          //最大重连时间5分钟
          websocket.maxReconnectInterval = 1000 * 60 * 5;
          //当窗口关闭时，主动去关闭websocket连接
          window.onbeforeunload = function () {
              console.log(date.toLocaleString()+"->浏览器关闭,关闭websocket连接");
              websocket.close();
          }
          return websocket;
      }
     return  {
        initSocket:initSocket
    };
  });
})();