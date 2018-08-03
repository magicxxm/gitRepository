/**
 * Created by preston.zhang on 2017/9/6.
 */
(function () {
    "use strict";
    angular.module('myApp').factory("mySocket",function ($websocket,INBOUND_CONSTANT) {
        console.log("loaded factory...");
        //是否维护队列
        var isCollectMessage = false;
        //存放消息的queue
        var collection = [];
        var dataStream = null;
        //建立websocket连接
        function buildConnection(workStationId,successCallBack,errorCallBack,onMessageCallBack,onCloseCallBack) {
            console.log("start to building connection...");
            dataStream = $websocket(INBOUND_CONSTANT.readWebSocketPod+workStationId);
            dataStream.onOpen(function (datas) {
                successCallBack();
            });
            dataStream.onError(function (datas) {
                errorCallBack();
            });
            dataStream.onMessage(function (datas) {
                var serverData = JSON.parse(datas.data);
                if(serverData.pod!=='success'){
                    onMessageCallBack(serverData);
                }
            });
            dataStream.onClose(function (datas) {
                onCloseCallBack();
            });
        }

        /**
         *  webSocketBuilder
         * @type {{build: build, send: send,onOpen:onOpen, onMessage: onMessage, close: close, onClose: onClose, reConnect: reConnect, onError: onError}}
         */
        var webSocketBuilder = {
            /**
             * 建立连接
             * @param url
             * @param options
             */
            build:function (url,options) {
                dataStream = $websocket(url,null,options||{
                    initialTimeout:1000
                });
            },
            /**
             * onOpen
             * @param successCallBack
             */
            onOpen:function (successCallBack) {
              dataStream.onOpen(function (datas) {
                  successCallBack();
              });
            },
            /**
             * 发送消息
             * @param sendCallBack
             */
            send:function (sendCallBack) {
                dataStream.send(function (datas) {
                    sendCallBack(JSON.parse(datas));
                });
            },
            /**
             * 收到消息
             * @param onMessage
             */
            onMessage:function (onMessage) {
                if(dataStream===null||dataStream===undefined){
                    console.log("dataStream is null");
                }
                dataStream.onMessage(function (datas) {
                    var serverData = JSON.parse(datas.data);
                    if(serverData.pod!=='success'){
                        onMessage(serverData);
                    }
                });
            },
            /**
             * 关闭连接
             * @param isForce 是否强制关闭
             */
            close:function (isForce) {
                dataStream.close(isForce);
            },
            /**
             * 关闭连接时
             * @param closeCallBack
             */
            onClose:function (closeCallBack) {
                dataStream.onClose(function (datas) {
                    closeCallBack(datas);
                });
            },
            /**
             * 重新连接
             */
            reConnect:function () {
                dataStream.reconnect();
            },
            /**
             * 错误
             * @param onErrorCallBack
             */
            onError:function (onErrorCallBack) {
                dataStream.onError(function (datas) {
                    onErrorCallBack(JSON.parse(datas.data));
                });
            }
        };
        return {
            buildConnection:buildConnection,
            webSocketBuilder:webSocketBuilder,
            collection:collection
        };
    });
})();