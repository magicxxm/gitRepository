/**
 * Created by frank.zhou on 2017/04/14.
 * Updated by frank.zhou on 2017/04/17.
 */
(function(){
  "use strict";

  angular.module("myApp").service("commonService", function($http,$window, $translate, BACKEND_CONFIG){
    // grid请求
    function gridMushiny(options){
      return {
        dataSource: options.dataSource,
        selectable: options.selectModel || "row",
        height: options.height || $(document.body).height() - 191,
        sortable: true,
        scrollable: true,
        pageable: {
          pageSize: 50,
          pageSizes: [50, 100, 200],
          previousNext: true,
          numeric: true,
          input: false,
          info: true
        },
        columns: options.columns,
        detailInit: options.detailInit
      };
    }
    //导出数据时设置不分页加载
    function gridMushiny1(options){
      return {
        dataSource: options.dataSource,
        selectable: options.selectModel || "row",
        height: options.height || $(document.body).height() - 191,
        sortable: true,
        scrollable: true,
        pageable: {
          numeric: true,
        },
        columns: options.columns,
        detailInit: options.detailInit
      };
    }
    // 对话框
    function dialogMushiny(window, options){
      window.setOptions({
        width: options.width || 300,
        height: options.height || 105,
        title: options.title || "",
        content: {
          url: options.url || "modules/common/templates/"+ (options.type || "delete")+ "Window.html"
        },
        open: function(){
          options.open && options.open(this);
        }
      });
      window.refresh();
      window.center();
      window.open();
    }

    // ajax请求(同步)
    function ajaxSync(options){
      // 临时
      var preUrl = "";
      if(options.url.indexOf("http://") >= 0) preUrl = "";
      else if(options.url.indexOf(".json") >= 0) preUrl = "http://localhost:8080/";
      else if(options.url.indexOf("replenish/") >= 0) preUrl = BACKEND_CONFIG.replenish;
      else if(options.url.indexOf("tot/") >= 0) preUrl = BACKEND_CONFIG.tot;
      else if(options.url.indexOf("masterdata") >= 0) preUrl = BACKEND_CONFIG.masterData;
      else if(options.url.indexOf("andon") >= 0) preUrl = BACKEND_CONFIG.andon;
      else if(options.url.indexOf("icqa") >= 0) preUrl = BACKEND_CONFIG.icqa;
      else if(options.url.indexOf("inbound-problem") >= 0) preUrl = BACKEND_CONFIG.inboundProblem;
      else if(options.url.indexOf("inbound") >= 0) preUrl = BACKEND_CONFIG.inbound;
      else if(options.url.indexOf("internal") >= 0) preUrl = BACKEND_CONFIG.internalTool;
      else if(options.url.indexOf("outboundproblem") >= 0) preUrl = BACKEND_CONFIG.outboundProblem;
      else if(options.url.indexOf("outbound") >= 0) preUrl = BACKEND_CONFIG.outbound;
      else if(options.url.indexOf("report") >= 0) preUrl = BACKEND_CONFIG.report;
      else if(options.url.indexOf("system") >= 0) preUrl = BACKEND_CONFIG.system;
      else if(options.url.indexOf("stow-station/calling") >= 0)preUrl=BACKEND_CONFIG.wcs;
      else if(options.url.indexOf("receive-station/calling") >= 0)preUrl=BACKEND_CONFIG.wcs;
      else if(options.url.indexOf("wcs/callNewPod") >= 0)preUrl=BACKEND_CONFIG.wcsPod;
      else if(options.url.indexOf("wcs/podRelease") >= 0)preUrl=BACKEND_CONFIG.wcsPod;
      else if(options.url.indexOf("commonStation") >= 0)preUrl=BACKEND_CONFIG.callPod;
      else if(options.url.indexOf("websocket") >= 0)preUrl=BACKEND_CONFIG.websocket;
      else preUrl = BACKEND_CONFIG.main;
      //
      $.ajax({
        type: options.type || "GET",
        url: preUrl+ options.url,
        cache: false,
        async: (options.async!=null? options.async: false),
        dataType: options.dataType || "json",
        contentType: options.contentType || "application/json;charset=utf-8",
        data: options.data || {},
        beforeSend: function(XMLHttpRequest){
          XMLHttpRequest.setRequestHeader("Warehouse", $window.localStorage["warehouseId"]);
          XMLHttpRequest.setRequestHeader("Authorization", "Bearer "+ $window.localStorage["accessToken"]);
        },
        success: function(data){
          options.success && options.success(data);
        },
        error: function(){
          console.log("ajax error", arguments);
        }
      });
    }

    // ajax请求(异步）
    function ajaxAsync(options){
      // 临时
      var preUrl = "";
      if(options.url.indexOf("http://") >= 0) preUrl = "";
      else if(options.url.indexOf(".json") >= 0) preUrl = "http://localhost:8080/";
      else if(options.url.indexOf("replenish/") >= 0) preUrl = BACKEND_CONFIG.replenish;
      else if(options.url.indexOf("tot/") >= 0) preUrl = BACKEND_CONFIG.tot;
      else if(options.url.indexOf("masterdata") >= 0) preUrl = BACKEND_CONFIG.masterData;
      else if(options.url.indexOf("andon") >= 0) preUrl = BACKEND_CONFIG.andon;
      else if(options.url.indexOf("icqa") >= 0) preUrl = BACKEND_CONFIG.icqa;
      else if(options.url.indexOf("inbound-problem") >= 0) preUrl = BACKEND_CONFIG.inboundProblem;
      else if(options.url.indexOf("inbound") >= 0) preUrl = BACKEND_CONFIG.inbound;
      else if(options.url.indexOf("internal") >= 0) preUrl = BACKEND_CONFIG.internalTool;
      else if(options.url.indexOf("outboundproblem") >= 0) preUrl = BACKEND_CONFIG.outboundProblem;
      else if(options.url.indexOf("outbound") >= 0) preUrl = BACKEND_CONFIG.outbound;
      else if(options.url.indexOf("outgoods") >= 0) preUrl = BACKEND_CONFIG.outbound;
      else if(options.url.indexOf("transout") >= 0) preUrl = BACKEND_CONFIG.outbound;
      else if(options.url.indexOf("report") >= 0) preUrl = BACKEND_CONFIG.report;
      else if(options.url.indexOf("system") >= 0) preUrl = BACKEND_CONFIG.system;
      else if(options.url.indexOf("station/calling") >= 0)preUrl=BACKEND_CONFIG.wcs;
      else if(options.url.indexOf("wcs/callNewPod") >= 0)preUrl=BACKEND_CONFIG.wcsPod;
      else if(options.url.indexOf("wcs/podRelease") >= 0)preUrl=BACKEND_CONFIG.wcsPod;
      else if(options.url.indexOf("commonStation") >= 0)preUrl=BACKEND_CONFIG.callPod;
      else if(options.url.indexOf("websocket") >= 0)preUrl=BACKEND_CONFIG.websocket;
      else preUrl = BACKEND_CONFIG.main;
      //
      kendo.ui.progress($(document.body), true); // 加载请求
      $http({
        url: preUrl+ options.url,
        method: options.method || "GET",
        headers: {"Content-Type": options.contentType || "application/json;charset=utf-8",
            "Warehouse": $window.localStorage["warehouseId"],
            "Authorization": "Bearer "+ $window.localStorage["accessToken"]},
        data: options.data || {},
          params:options.params || {}
      }).then(function(datas){
        kendo.ui.progress($(document.body), false); // 结束请求
        options.success && options.success(datas);
      }, function(datas){
        kendo.ui.progress($(document.body), false); // 结束请求
        if(options.error)
          options.error(datas);
        else{
          var win = $("#mushinyWindow").data("kendoWindow");
          dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
            setTimeout(function(){
              $("#warnContent").html($translate.instant(datas.data.message))
            }, 200);
          }});
        }
      });
    }

    function generalNet(options) {
        var preUrl = "";
        if(options.url.indexOf("http://") >= 0) preUrl = "";
        else if(options.url.indexOf(".json") >= 0) preUrl = "http://localhost:8080/";
        else if(options.url.indexOf("replenish/") >= 0) preUrl = BACKEND_CONFIG.replenish;
        else if(options.url.indexOf("tot/") >= 0) preUrl = BACKEND_CONFIG.tot;
        else if(options.url.indexOf("masterdata") >= 0) preUrl = BACKEND_CONFIG.masterData;
        else if(options.url.indexOf("andon") >= 0) preUrl = BACKEND_CONFIG.andon;
        else if(options.url.indexOf("icqa") >= 0) preUrl = BACKEND_CONFIG.icqa;
        else if(options.url.indexOf("inbound-problem") >= 0) preUrl = BACKEND_CONFIG.inboundProblem;
        else if(options.url.indexOf("inbound") >= 0) preUrl = BACKEND_CONFIG.inbound;
        else if(options.url.indexOf("internal") >= 0) preUrl = BACKEND_CONFIG.internalTool;
        else if(options.url.indexOf("outboundproblem") >= 0) preUrl = BACKEND_CONFIG.outboundProblem;
        else if(options.url.indexOf("outbound") >= 0) preUrl = BACKEND_CONFIG.outbound;
        else if(options.url.indexOf("outgoods") >= 0) preUrl = BACKEND_CONFIG.outbound;
        else if(options.url.indexOf("transout") >= 0) preUrl = BACKEND_CONFIG.outbound;
        else if(options.url.indexOf("report") >= 0) preUrl = BACKEND_CONFIG.report;
        else if(options.url.indexOf("system") >= 0) preUrl = BACKEND_CONFIG.system;
        else if(options.url.indexOf("station/calling") >= 0)preUrl=BACKEND_CONFIG.wcs;
        else if(options.url.indexOf("wcs/callNewPod") >= 0)preUrl=BACKEND_CONFIG.wcsPod;
        else if(options.url.indexOf("wcs/podRelease") >= 0)preUrl=BACKEND_CONFIG.wcsPod;
        else if(options.url.indexOf("commonStation") >= 0)preUrl=BACKEND_CONFIG.callPod;
        else if(options.url.indexOf("websocket") >= 0)preUrl=BACKEND_CONFIG.websocket;
        else preUrl = BACKEND_CONFIG.main;
        kendo.ui.progress($(document.body), true);
        $http({
            url: preUrl+ options.url,
            method: options.method || "GET",
            data: options.data || {},
            params:options.params || {}
        }).then(function(datas){
            kendo.ui.progress($(document.body), false); // 结束请求
            options.success && options.success(datas);
        }, function(datas){
            kendo.ui.progress($(document.body), false); // 结束请求
            if(options.error)
                options.error(datas);
            else{
                var win = $("#mushinyWindow").data("kendoWindow");
                dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                    setTimeout(function(){
                        $("#warnContent").html($translate.instant(datas.data.message))
                    }, 200);
                }});
            }
        });
    }
// importAjax请求(异步）
    function importAjaxAsync(key,options){
      // 临时
      var preUrl = "";
      if(options.url.indexOf("http://") >= 0) preUrl = "";
      else if(options.url.indexOf(".json") >= 0) preUrl = "http://localhost:8080/";
      else if(options.url.indexOf("masterdata") >= 0) preUrl = BACKEND_CONFIG.masterData;
      else if(options.url.indexOf("icqa") >= 0) preUrl = BACKEND_CONFIG.icqa;
      else if(options.url.indexOf("system") >= 0) preUrl = BACKEND_CONFIG.system;
      else if(options.url.indexOf("transout") >= 0) preUrl = BACKEND_CONFIG.outbound;
      else preUrl = BACKEND_CONFIG.main;
      kendo.ui.progress($(document.body), true); // 加载请求
      $http({
        url: preUrl+ options.url,
        method: 'POST',
        headers: {
          'Content-Type': undefined,
          "Warehouse": $window.localStorage["warehouseId"],
          "Authorization": "Bearer "+ $window.localStorage["accessToken"]
        },
        data: options.data ,
        transformRequest: angular.identity
      }).then(function(datas){
        kendo.ui.progress($(document.body), false); // 成功结束请求
        options.success && options.success(datas);
        var win = $("#mushinyWindow").data("kendoWindow");
        dialogMushiny(win, {title:"上传文件成功",width: 320, height: 160,type: "warn", open: function(){
          setTimeout(function(){
            $("#warnContent").html("文件上传成功！")
          }, 1000);
        }});
        //成功后刷新Grid数据
        // var grid = $("#" + key + "Grid").data("kendoGrid");
          if (key != "stocktakingOrders"){
              var grid = $("#" + key + "Grid").data("kendoGrid");
              grid.dataSource.read(); // 刷新表格
          }
        // grid.dataSource.read(); // 刷新表格
      }, function(datas){
        kendo.ui.progress($(document.body), false); // 失败结束请求
          var win = $("#mushinyWindow").data("kendoWindow");
          dialogMushiny(win, {title:"上传文件失败",width: 320, height: 160,type: "warn", open: function(){
            setTimeout(function(){
              $("#warnContent").html("文件上传失败！")
            }, 1000);
          }});
      });
    }
    function exportFile(key,options) {
        // 临时
        var preUrl = "";
        if(options.url.indexOf("http://") >= 0) preUrl = "";
        else if(options.url.indexOf(".json") >= 0) preUrl = "http://localhost:8080/";
        else if(options.url.indexOf("transout") >= 0) preUrl = BACKEND_CONFIG.outbound;
        else preUrl = BACKEND_CONFIG.main;
        kendo.ui.progress($(document.body), true); // 加载请求
        $http({
            url:  preUrl+ options.url,
            method: "GET",//接口方法
            params:options.params||null,
            headers: {
                'Content-type': 'application/json',
                "Warehouse": $window.localStorage["warehouseId"],
                "Authorization": "Bearer "+ $window.localStorage["accessToken"]
            },
            responseType: 'arraybuffer'
        }).success(function (data, status, headers, config) {
            kendo.ui.progress($(document.body), false);
            var blob = new Blob([data], {type: "application/vnd.ms-excel"});
            var objectUrl = URL.createObjectURL(blob);
            var a = document.createElement('a');
            document.body.appendChild(a);
            a.setAttribute('style', 'display:none');
            a.setAttribute('href', objectUrl);
            var filename=options.fileName||"调拨配置数据("+kendo.format("{0:yyyy-MM-dd HH:mm:ss}",new Date())+")"+".xls";
            a.setAttribute('download', filename);
            a.click();
            URL.revokeObjectURL(objectUrl);
        }).error(function (data, status, headers, config) {
            kendo.ui.progress($(document.body), false);
        });
    }
    // ajax请求
    function ajaxMushiny(options){
      options.async == null && (options.async = true); // 默认异步
      if(options.async) this.ajaxAsync(options);
      else this.ajaxSync(options);
    }

    return {
      importAjaxAsync:importAjaxAsync,
      gridMushiny1: gridMushiny1,
      gridMushiny: gridMushiny,
      dialogMushiny: dialogMushiny,
      ajaxSync: ajaxSync,
      ajaxAsync: ajaxAsync,
      ajaxMushiny: ajaxMushiny,
      exportFile:exportFile,
      generalNet:generalNet
    };
  });
})();