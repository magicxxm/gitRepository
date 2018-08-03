/**
 * Created by 123 on 2017/4/20.
 */
// 对话框

(function(){
    "use strict";
    angular.module("myApp").service("stow_commonService", function($http, $window, $translate, BACKEND_CONFIG){
        var inputcrrentid;
        function receiving_dialog(window, options,callBackMethod){
            window.setOptions({
                width: options.width || 300,
                height: options.height || 105,
                title: options.title || "",
                content: {
                    url: options.url || "modules/common/templates/"+ (options.type || "delete")+ "Window.html"
                },
                open: function(){
                    options.open && options.open(this);
                },
                close:callBackMethod
            });
            window.refresh();
            window.center();
            window.open();
        }

        function receiving_tip_dialog(window_div_id,options) {
            $('#'+window_div_id).parent().addClass("myWindow"); // title css
            var win = $("#"+window_div_id).data("kendoWindow");
            win.setOptions({
                    width: options.width||500,
                    height: options.height||400,
                    title: options.title,
                    open: options.open,
                    close:options.close
                }
            );
            win.center();
            win.open();
        }

        function receiving_tip_dialog_normal(window_div_id,options) {
            $('#'+window_div_id).parent().addClass("mySelect"); // title css
            var win = $("#"+window_div_id).data("kendoWindow");
            win.setOptions({
                    width: options.width||500,
                    height: options.height||400,
                    title: options.title,
                    open: options.open,
                    close:options.close
                }
            );
            win.center();
            win.open();
        }

        function autoClose(e,window_div_id) {
            autoAddEvent(e);
            var window = $("#"+window_div_id).data("kendoWindow");
            window.close();
        }
        function CloseWindowByBtn(window_div_id) {
            var window = $("#"+window_div_id).data("kendoWindow");
            window.close();
        }
        function autoAddEvent(event) {
            var keyCode = window.event? event.keyCode: event.which;
            return keyCode == 13 ? true:false;
        }

        function keyboard_fillGrid(rootdiv,row,column,flag,temlateClass,tableSpace,unit) {
            if(rootdiv==null||rootdiv==undefined||row<1||column<1)return;
            var count  = "";
            // var root_width = rootdiv.style.width;
            // var root_height = rootdiv.style.height;
            // var all_space_horizon = (column+1)*tableSpace;
            // var all_space_ver = (row+1)*tableSpace;
            // var item_width = parseInt((root_width - all_space_horizon)/column);
            // var item_height = parseInt((root_height - all_space_ver)/row);
            console.log("开始生成键盘布局...");
            for (var i = 0;i<row;i++){
                count += "<div style='float: left;width:100%;height:45%;margin-top: 2%'>"
                for (var j=0;j<column;j++){
                    if(i==0){
                        count +="<div class='"+temlateClass+"' id='"+flag+i+j+"'"+
                            "style='width:18%;height:97.5%;'> " +
                            "<span id='"+flag+i+j+"_span' style='color: #0f0f0f;font-size: 48px;line-height: 2'>"+(j+1)+"</span> " +
                            "</div>";
                    }else if(i==1){
                        if(j==(column-1)){
                            count +="<div class='"+temlateClass+"' id='"+flag+i+j+"'"+
                                "style='width:18%;height:97.5%;'> " +
                                "<span id='"+flag+i+j+"_span' style='color: #0f0f0f;font-size: 48px;line-height: 2'>0</span> " +
                                "</div>";
                        }else{
                            count +="<div class='"+temlateClass+"' id='"+flag+i+j+"'"+
                                "style='width:18%;height:97.5%;'> " +
                                "<span id='"+flag+i+j+"_span' style='color: #0f0f0f;font-size: 48px;line-height: 2'>"+(column+j+1)+"</span> " +
                                "</div>";
                        }
                    }
                }
                count+="</div>";
            }
            rootdiv.html(count);
            $('.'+temlateClass).each(function(){
                $(this).click(function(){
                    var inputer = $("#keyboard_inputer");
                    var span = $("#"+this.id).children("span");
                    if(inputer.val()<1){
                        if(span.text()==='0'){
                            return;
                        }
                    }
                    document.getElementById("keyboard_inputer").value = inputer.val()+span.text();
                })
            })
            // rootdiv.html(count);
        }
        function avatime_keyboard_fillGrid(rootdiv,row,column,flag,temlateClass,tableSpace,SpanFontSize) {
            if(rootdiv==null||rootdiv==undefined||row<1||column<1)return;
            var count  = "";
            // var root_width = rootdiv.style.width;
            // var root_height = rootdiv.style.height;
            // var all_space_horizon = (column+1)*tableSpace;
            // var all_space_ver = (row+1)*tableSpace;
            // var item_width = parseInt((root_width - all_space_horizon)/column);
            // var item_height = parseInt((root_height - all_space_ver)/row);
            for (var i = 0;i<row;i++){
                count += "<div style='float: left;width:100%;height:45%;margin-top: 2%'>"
                for (var j=0;j<column;j++){
                    if(i==0){
                        count +="<div class='"+temlateClass+"' id='"+flag+i+j+"'"+
                            "style='width:18%;height:97.5%;'> " +
                            "<span id='"+flag+i+j+"_span' style='color: #0f0f0f;font-size: "+SpanFontSize+"px;line-height: 2'>"+(j+1)+"</span> " +
                            "</div>";
                    }else if(i==1){
                        if(j==(column-1)){
                            count +="<div class='"+temlateClass+"' id='"+flag+i+j+"'"+
                                "style='width:18%;height:97.5%;'> " +
                                "<span id='"+flag+i+j+"_span' style='color: #0f0f0f;font-size: "+SpanFontSize+"px;line-height: 2'>0</span> " +
                                "</div>";
                        }else{
                            count +="<div class='"+temlateClass+"' id='"+flag+i+j+"'"+
                                "style='width:18%;height:97.5%;'> " +
                                "<span id='"+flag+i+j+"_span' style='color: #0f0f0f;font-size: "+SpanFontSize+"px;line-height: 2'>"+(column+j+1)+"</span> " +
                                "</div>";
                        }
                    }
                }
                count+="</div>";
            }
            rootdiv.html(count);
            $('.'+temlateClass).each(function(){
                $(this).click(function(){
                    console.log("点击了");
                    if(inputcrrentid===undefined||inputcrrentid===null){
                        console.log("id--->"+inputcrrentid);
                        console.log("输入框id未获取到");
                        return;
                    }
                    var avainput = document.getElementById(inputcrrentid);
                    var span = $("#"+this.id).children("span");
                    console.log("span.text-->"+span.text());
                    console.log("input.value-->"+avainput.id);
                    console.log("avadom--->"+avainput.width);
                    if(avainput.value<1){
                        if(span.text()==='0'){
                            return;
                        }
                    }
                    avainput.value = avainput.value+span.text();
                })
            })
            // rootdiv.html(count);
        }
        function reveive_ToteFillGrid(rootdiv,row,column,flag,temlateClass,tableSpace,SpanFontSize) {
            if(rootdiv==null||rootdiv==undefined||row<1||column<1)return;
            var count  = "";
            // var root_width = rootdiv.style.width;
            // var root_height = rootdiv.style.height;
            // var all_space_horizon = (column+1)*tableSpace;
            // var all_space_ver = (row+1)*tableSpace;
            // var item_width = parseInt((root_width - all_space_horizon)/column);
            // var item_height = parseInt((root_height - all_space_ver)/row);
            console.log("开始生成键盘布局...");
            for (var i = 0;i<row;i++){
                count += "<div style='float: left;width:100%;height:45%;margin-top: 2%'>"
                for (var j=0;j<column;j++){
                    if(i==0){
                        count +="<div class='"+temlateClass+"' id='"+flag+i+j+"'"+
                            "style='width:32%;height:97.5%;margin-left: 1%'> " +
                                "<span class='receiveToToteSpan'>"+(j+1)+"</span>"+
                                "<div class='receiveToToteInnerDiv'>" +
                            "<span ng-show='scanbadcib==='"+"'0'" +
                            "class='receiving-uptopod-label' style='background-color:red ;'>"+j+"</span>" +
                            "</div>"+
                            "</div>";
                    }else if(i==1){
                        count +="<div class='"+temlateClass+"' id='"+flag+i+j+"'"+
                            "style='width:32%;height:97.5%;margin-left: 1%'> " +
                            "<span class='receiveToToteSpan'>"+(column+j+1)+"</span>"+
                            "<div class='receiveToToteInnerDiv'>" +
                            "<span ng-show='scanbadcib==='"+"'0'" +
                            "class='receiving-uptopod-label' style='background-color:red ;'>"+j+"</span>" +
                            "</div>"+
                            "</div>";
                    }
                }
                count+="</div>";
            }
            console.log("rootdiv.id-->"+rootdiv.id);
            rootdiv.html(count);
            $('.'+temlateClass).each(function(){
                $(this).click(function(){
                    console.log("点击了");
                    if(inputcrrentid===undefined||inputcrrentid===null){
                        console.log("id--->"+inputcrrentid);
                        console.log("输入框id未获取到");
                        return;
                    }
                    var avainput = document.getElementById(inputcrrentid);
                    var span = $("#"+this.id).children("span");
                    console.log("span.text-->"+span.text());
                    console.log("input.value-->"+avainput.id);
                    console.log("avadom--->"+avainput.width);
                    if(avainput.value<1){
                        if(span.text()==='0'){
                            return;
                        }
                    }
                    avainput.value = avainput.value+span.text();
                })
            })
            // rootdiv.html(count);
        }

        function getavatimeid(currentid) {
            console.log("id---------->"+currentid);
            inputcrrentid = currentid;
        }
        return {
            receiving_dialog: receiving_dialog,
            receiving_tip_dialog:receiving_tip_dialog,
            autoAddEvent:autoAddEvent,
            autoClose:autoClose,
            keyboard_fillGrid:keyboard_fillGrid,
            CloseWindowByBtn:CloseWindowByBtn,
            receiving_tip_dialog_normal:receiving_tip_dialog_normal,
            avatime_keyboard_fillGrid:avatime_keyboard_fillGrid,
            getavatimeid:getavatimeid,
            reveive_ToteFillGrid:reveive_ToteFillGrid
        };
    });
    })();