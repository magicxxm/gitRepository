/**
 * Created by 123 on 2017/4/20.
 */
// 对话框

(function(){
    "use strict";
    angular.module("myApp").service("receiving_commonService", function($http, $window,$rootScope, $translate, BACKEND_CONFIG,commonService, INBOUND_CONSTANT){
        var inputcrrentid;
        var selectedArrays = [];
        var selectedBinArrays = [];
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
            win.refresh();
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

        /**根据数据动态生成div表格
         *  @param rootdiv 生成div表格的容器
         * @param row 表格的行数
         * @param column 表格的列数
         * @param flag动态生成的div加标志位
         * @param ispercentage 宽高单位是否为百分比
         * @param tablespace 分割线距离
         */
        function fillGrid(rootdiv,row,flag,temlateClass,dataresource,levelTypes) {
            if(rootdiv==null||rootdiv==undefined||row<1)return;
            var count  = "";
            var levels = getObjCount(dataresource);
            console.log("总层数-->"+levels);
            console.log("levelTypes-->",levelTypes);
            var multi_height = 100-levels;
            for(var key = levels-1;key>=0;key--){
                var length = dataresource[key];
                var height = levelTypes[key]*multi_height;
                if(levels===1){
                    height=10;
                }
                console.log("层数："+key+"/length-->"+length);
                var multi = 100-length;
                var yushu = multi%length;
                var width = parseInt(multi/length);
                var leftrightspace = (yushu+1)/2;
                var num = 0;
                count += "<div style='float: left;width:100%;height:"+height+"%;margin-top: 1%'>";
                for (var j=0;j<length;j++){
                    if(num===0){
                        count +="<div class='"+temlateClass+"' id='"+flag+key+num+"'"+
                            "style='width:"+(function () {
                                if(length===1){
                                    return "33%;margin-left:33%;";
                                }else{
                                    return width+"%;margin-left:"+leftrightspace+"%";
                                }
                            })()+";height:100%;'> " +
                            "<span id='"+flag+key+j+"' style='color: #FFFFFF;line-height: 2;font-size:large'></span> " +
                            "</div>";
                    }else if(num===length-1){
                        count +="<div class='"+temlateClass+"' id='"+flag+key+num+"'"+
                            "style='width:"+width+"%;height:100%;margin-right: "+leftrightspace+"%'> " +
                            "<span id='"+flag+key+j+"' style='color: #FFFFFF;line-height: 2;font-size:large'></span> " +
                            "</div>";
                    }else{
                        count +="<div class='"+temlateClass+"' id='"+flag+key+num+"'"+
                            "style='width:"+width+"%;height:100%;margin-left: 1%'> " +
                            "<span id='"+flag+key+j+"' style='color: #FFFFFF;line-height: 2;;font-size:large'></span> " +
                            "</div>";
                    }
                    num++;
                }
                count+="</div>";
            }
            rootdiv.innerHTML = count;
        }

		function findStorageLocation(stoName,resources){
            var length = getObjCount(resources);
            for(var i=0;i<length;i++){
                if(resources[i].name.toLowerCase()===stoName.toLowerCase()){
                    console.log("xpos-->"+(resources[i].xpos-1)+"/ypos-->"+(resources[i].ypos-1));
                    return "receiving_pod_layout"+(resources[i].xpos-1)+(resources[i].ypos-1);
                }
            }
        }

        function findStorageLocationInTote(stoName,resources,callback){
		    console.log("开始找tote筐。。。。");
            var length = getObjCount(resources);
            for(var i=0;i<length;i++){
                if(resources[i].storageName.toLowerCase()===stoName.toLowerCase()){
                    console.log("storageIndex--->"+resources[i].positionIndex);
                    if(callback===undefined||callback===null){
                        return resources[i].positionIndex;
                    }else{
                        callback(resources[i].positionIndex);
                    }
                }
            }
        }

        function findStorageLocationIndexInTote(stoName,resources,callback){
            console.log("开始找tote筐。。。。");
            var length = getObjCount(resources);
            for(var i=0;i<length;i++){
                if(resources[i].storageName.toLowerCase()===stoName.toLowerCase()){
                    console.log("storageIndex--->"+resources[i].positionIndex);
                    if(callback===undefined||callback===null){
                        return i;
                    }else{
                        callback(i);
                    }
                }
            }
        }
        function findStorageLocationIndexInAllTote(stoName,resources,callback){
            console.log("开始找tote筐。。。。");
            var length = getObjCount(resources);
            for(var i=0;i<length;i++){
                if(resources[i].receiveStorageName.toLowerCase()===stoName.toLowerCase()){
                    console.log("storageIndex--->"+resources[i].positionIndex);
                    if(callback===undefined||callback===null){
                        return i;
                    }else{
                        callback(i);
                    }
                }
            }
        }

        function findSideStorageLocation(stoName,resources,location){
            var length = getObjCount(resources);
            var x = '';
            var y = '';
            var storageName = null;
            for(var i=0;i<length;i++){
                if(resources[i].name===stoName){
                    switch(location){
                        case 'up':{
                            x = resources[i].xpos-1;
                            y = resources[i].ypos;
                        }break;
                        case 'down':{
                            x = resources[i].xpos+1;
                            y = resources[i].ypos;
                        }break;
                        case 'left':{
                            x = resources[i].xpos;
                            y = resources[i].ypos-1;
                        }break;
                        case 'right':{
                            x = resources[i].xpos;
                            y = resources[i].ypos+1;
                        }break;
                    }
                    console.log("x-->"+x+"/y--->"+y);
                    for (var l=0;l<length;l++){
                        if(resources[l].ypos===y&&resources[l].xpos===x){
                            storageName =  resources[l].id;
                        }
                    }
                }
            }
            return storageName;
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
        
        function grid_BayType(typearrays,column,row) {
            selectedArrays.length = 0;
            //计算行数
            var isSELECT = false;
            var SELECTID;
            var countNum = 0;
            var root_div = $("#select_bin_grid");
            var typearraylength = getObjCount(typearrays);
            var count  = "";
            for (var i = 0;i<row;i++){
                countNum = i*column;
                count += "<div style='float: left;margin-left:8%;width:80%;height:25%'>"
                for (var j=0;j<column;j++){
                    var num = countNum+j;
                    console.log("num-->"+num);
                    if(num===typearraylength){
                        break;
                    }else{
                        count +="<div class='box_shadow_with_radius' id='bin_item"+num+"'>"+
                            "<span id='bin_item_title' style='font-size: 18px;color: #242424;line-height: 2'>"+typearrays[num].name+" </span> " +
                            "</div>";
                        continue;
                    }
                }
                count+="</div>";
            }
            root_div.html(count);
            $('.box_shadow_with_radius').each(function(){
                $(this).click(function(){
                    var index = this.id.replace('selected_','').substring(8);
                    if(this.id.substring(0,this.id.indexOf("bin_item"))==='selected_'){//已经选中
                        this.id = this.id.replace('selected_','');
                        document.getElementById(this.id).style.backgroundColor = '#E0EEEE';
                        removeByValue(selectedArrays,typearrays[index]);
                    }else{//未选中
                        this.style.backgroundColor = '#00a2eb';
                        this.id='selected_'+this.id;
                        selectedArrays.push(typearrays[index]);
                    }
                })
            })
        }

        function grid_BayTypeInPage(typearrays,selectedArrays,column) {
            selectedBinArrays.length = 0;
            //计算行数
            var isSELECT = false;
            var SELECTID;
            var countNum = 0;
            var root_div = $("#show_bin_grid");
            var typearraylength = getObjCount(typearrays);
            var count  = "";
            var row = parseInt((typearraylength/2));
            var yushu = typearraylength%2;
            if(yushu>0){
                row++;
            }
            for (var i = 0;i<row;i++){
                count += "<div style='float: left;width:98%;height:25%;'>"
                if(yushu>0&&i===(row-1)){
                    for (var j=0;j<yushu;j++){
                        console.log("countNum--->"+countNum);
                        count +="<div class='box_shadow_with_radius_show_bin_item' style='width:45%;height: 25%;line-height: 2.5;background-color:#3f51b5;color: #FFFFFF;margin-left: 5%' id='"+typearrays[countNum].id+"'"+
                            "<span id='showbin_item_title' style='font-size: 22px;line-height: 35px;color: #FFFFFF;'>"+(countNum+1)+"."+typearrays[countNum].name+"</span> " +
                            "</div>";
                        countNum++;
                    }
                }else{
                    for (var j=0;j<column;j++){
                        console.log("countNum--->"+countNum);
                        count +="<div class='box_shadow_with_radius_show_bin_item' style='width:45%;height: 25%;line-height: 2.5;background-color:#3f51b5;color: #FFFFFF;margin-left: 5%' id='"+typearrays[countNum].id+"'"+
                            "<span id='showbin_item_title' style='font-size: 22px;line-height: 35px;color: #FFFFFF;'>"+(countNum+1)+"."+typearrays[countNum].name+"</span> " +
                            "</div>";
                        countNum++;
                    }
                }
                count+="</div>";
            }
            root_div.html(count);
            selectedBinArrays = selectedArrays;
            console.log("selectBinArrays--->",selectedBinArrays);
            $('.box_shadow_with_radius_show_bin_item').each(function(){
                var allLength = getObjCount(typearrays);
                var selectedlong = getObjCount(selectedArrays);
                document.getElementById(this.id).style.backgroundColor = '#949494';
                for (var i=0;i<allLength;i++){
                    if(selectedBinArrays[i]===undefined||selectedBinArrays[i]===null||selectedBinArrays[i]===''){
                        continue;
                    }
                    if(this.id===selectedBinArrays[i].id){//已经选中
                        document.getElementById(this.id).style.backgroundColor = '#3f51b5';
                    }
                }
                $(this).click(function(){
                    console.log("点击返回ID-->"+this.id);
                    var selectedLength = getObjCount(selectedBinArrays);
                    for (var l=0;l<allLength;l++){
                        if(this.id===typearrays[l].id){//已经选中
                            var flag = false;
                            for (var i=0;i<selectedLength;i++){
                                if(this.id===selectedBinArrays[i].id){//已经选中
                                    document.getElementById(this.id).style.backgroundColor = '#949494';
                                    removeByValue(selectedBinArrays,selectedBinArrays[i]);
                                    flag = true;
                                    selectedLength = getObjCount(selectedBinArrays);
                                }
                            }
                            if(!flag){
                                document.getElementById(this.id).style.backgroundColor = '#3f51b5';
                                selectedBinArrays.push(typearrays[l]);
                                selectedLength = getObjCount(selectedBinArrays);
                            }
                        }
                    }
                })
            });
        }

        //批量渲染推荐货位
        function colorIntroStorage(storages,resources) {
            console.log("storages--->",storages);
            console.log("resources--->",resources);
            for (var l=0;l<resources.length;l++){
                for (var i=0;i<storages.length;i++){
                    if(resources[l].name===storages[i].name){
                        console.log("resources[l].name-->"+resources[l].name);
                        console.log("xpos-->"+(resources[l].xpos-1)+"/ypos-->"+(resources[l].ypos-1));
                        $("#"+"receiving_pod_layout"+(resources[l].xpos-1)+(resources[l].ypos-1)).css({"backgroundColor":"#00a2eb"});
                    }
                }
            }
        }
        //批量恢复货位颜色
        function backPodStorageColor(resources) {
            for (var i=0;i<resources.length;i++){
                $("#"+"receiving_pod_layout"+(resources[i].xpos-1)+(resources[i].ypos-1)).css({"backgroundColor":"#949494"});
            }
        }
        function grid_ReportMenu(typearrays,column,callback) {
            console.log("typearrays--->",typearrays);
            var typearraylength = getObjCount(typearrays);
            var root_div = $("#report_light_grid");
            console.log("typearraylength-->"+typearraylength);
            var count  = "";
            //计算行数
            var countNum = 0;
            var row = parseInt((typearraylength/2));
            var yushu = (typearraylength%2);
            if(yushu>0){
                row++;
            }
            console.log("reportmenu--row-->"+row);
            for (var i = 0;i<row;i++){
                if(countNum===0){
                    count += "<div style='float: left;width:100%;height:12%'>"
                }else{
                    count += "<div style='float: left;width:100%;height:12%;margin-top: 3%'>"
                }
                if(yushu>0&&i===(row-1)){
                    for (var j=0;j<yushu;j++){
                        count +="<div class='box_shadow_with_radius_report_item' id='"+typearrays[countNum].id+"'>"+
                            "<span id='"+typearrays[countNum].id+"_title' style='font-size: 20px;line-height:2;color: #FFFFFF;'>"+(countNum+1)+"."+typearrays[countNum].name+"</span> " +
                            "</div>";
                        countNum++;
                    }
                }else{
                    for (var j=0;j<column;j++){
                        count +="<div class='box_shadow_with_radius_report_item' id='"+typearrays[countNum].id+"'>"+
                            "<span id='"+typearrays[countNum].id+"_title' style='font-size: 20px;line-height:2;color: #FFFFFF;'>"+(countNum+1)+"."+typearrays[countNum].name+"</span> " +
                            "</div>";
                        countNum++;
                    }
                }
                count+="</div>";
            }
            root_div.html(count);
            $('.box_shadow_with_radius_report_item').each(function(){
                $(this).click(function(){
                    console.log("点击返回ID-->"+this.id);
                    for(var l = 0;l<countNum;l++){
                        if(typearrays[l].id!==this.id){
                            $("#"+typearrays[l].id).css({"backgroundColor":"#949494"});
                        }else{
                            $("#"+this.id).css({"backgroundColor":"#00a2eb"});
                        }
                    }
                    callback(this.id);
                })
            })
        }

        function grid_SideStorage(typearrays,column,callback) {
            var root_div = $("#scan_side_window_grid");
            var typearraylength = getObjCount(typearrays);
            console.log("typearraylength-->"+typearraylength);
            var count  = "";
            var countNum  = 0;
            //计算行数
            var row = parseInt((typearraylength/2));
            var yushu = (typearraylength%2);
            if(yushu>0){
                row++;
            }
            console.log("reportmenu--row-->"+row);
            for (var i = 0;i<row;i++){
                count += "<div style='float: left;margin-left:5%;width:85%;height:50%'>"
                if(yushu>0&&i===(row-1)){
                    for (var j=0;j<yushu;j++){
                        count +="<div class='box_shadow_with_radius_report_side_item' id='"+typearrays[countNum].id+"'>"+
                            "<span id='"+typearrays[countNum].id+"_title' style='font-size: 18px;color: #FFFFFF;'>"+(countNum+1)+"."+typearrays[countNum].name+"</span> " +
                            "</div>";
                        countNum++;
                    }
                }else{
                    for (var j=0;j<column;j++){
                        count +="<div class='box_shadow_with_radius_report_side_item' id='"+typearrays[countNum].id+"'>"+
                            "<span id='"+typearrays[countNum].id+"_title' style='font-size: 18px;color: #FFFFFF;'>"+(countNum+1)+"."+typearrays[countNum].name+"</span> " +
                            "</div>";
                        countNum++;
                    }
                }
                count+="</div>";
            }
            root_div.html(count);
            $('.box_shadow_with_radius_report_side_item').each(function(){
                $(this).click(function(){
                    for(var l = 0;l<countNum;l++){
                        if(typearrays[l].id!==this.id){
                            $("#"+typearrays[l].id).css({"backgroundColor":"#949494"});
                        }else{
                            $("#"+this.id).css({"backgroundColor":"#00a2eb"});
                        }
                    }
                    callback(this.id);
                })
            })
        }

        function grid_AreaList(typearrays,column,row) {
            //计算行数
            var isSELECT = false;
            var SELECTID;
            var countNum = 0;
            var root_div = $("#select_area_grid");
            var typearraylength = getObjCount(typearrays);
            var count  = "";
            for (var i = 0;i<row;i++){
                countNum = i*column;
                count += "<div style='float: left;margin-left:8%;width:80%;height:25%'>"
                for (var j=0;j<column;j++){
                    var num = countNum+j;
                    console.log("num-->"+num);
                    if(num===typearraylength){
                        break;
                    }else{
                        count +="<div class='box_shadow_with_radius' id='area_item"+num+"'"+
                            "<span id='bin_area_title' style='font-size: 24px;line-height: 80px;color: #FFFFFF;'>"+typearrays[num].name+"</span> " +
                            "</div>";
                        continue;
                    }
                }
                count+="</div>";
            }
            root_div.html(count);
            $('.box_shadow_with_radius').each(function(){
                $(this).click(function(){
                    var index = this.id.replace('selected_','').substring(8);
                    if(this.id.substring(0,this.id.indexOf("area_item"))==='selected_'){//已经选中
                        this.id = this.id.replace('selected_','');
                        document.getElementById(this.id).style.backgroundColor = '#E0EEEE';
                        removeByValue(selectedAreaArrays,typearrays[index]);
                    }else{//未选中
                        this.style.backgroundColor = '#00a2eb';
                        this.id='selected_'+this.id;
                        selectedAreaArrays.push(typearrays[index]);
                    }
                })
            })
        }
        //$("#keyboard_keys"),2,5,"keyboard","keyboard_layout_item"
        function keyboard_fillGrid(rootdiv,row,column,flag,temlateClass,callback) {
            if(rootdiv==null||rootdiv==undefined||row<1||column<1)return;
            console.log("debug--->");
            var count  = "";
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

                    if(inputcrrentid===undefined||inputcrrentid===null){
                        return;
                    }
                    //get input domelement
                    var numsinput = $("#"+inputcrrentid).val();
                    var numlength = numsinput.length;
                    if(numlength<=0&&parseInt(numsinput)<=0){
                        //if (inputcrrentid=="keyboard_inputer"){
                            return;
                        //}
                    }
                    var clickValue = $("#"+this.id).children("span").html();
                    console.log("clickValue:"+clickValue);
                    console.log(inputcrrentid+":"+$("#"+inputcrrentid).val()+clickValue);
                    $("#"+inputcrrentid).val($("#"+inputcrrentid).val()+clickValue);
                })
            })
            if(callback!==undefined){
                callback();
            }
            // rootdiv.html(count);
        }
        function avatime_keyboard_fillGrid(rootdiv,row,column,flag,temlateClass,tableSpace,SpanFontSize) {
            if(rootdiv==null||rootdiv==undefined||row<1||column<1)return;
            var count  = "";
            for (var i = 0;i<row;i++){
                count += "<div style='float: left;width:100%;height:45%;margin-top: 2%'>"
                for (var j=0;j<column;j++){
                    if(i==0){
                        count +="<div class='"+temlateClass+"' id='"+flag+i+j+"'"+
                            "style='width:18%;height:97.5%;'> " +
                            "<span id='"+flag+i+j+"_span' style='color: #0f0f0f;font-size: 36px;font-weight:500;line-height: 2'>"+(j+1)+"</span> " +
                            "</div>";
                    }else if(i==1){
                        if(j==(column-1)){
                            count +="<div class='"+temlateClass+"' id='"+flag+i+j+"'"+
                                "style='width:18%;height:97.5%;'> " +
                                "<span id='"+flag+i+j+"_span' style='color: #0f0f0f;font-size: 36px;font-weight:500;line-height: 2'>0</span> " +
                                "</div>";
                        }else{
                            count +="<div class='"+temlateClass+"' id='"+flag+i+j+"'"+
                                "style='width:18%;height:97.5%;'> " +
                                "<span id='"+flag+i+j+"_span' style='color: #0f0f0f;font-size: 36px;font-weight:500;line-height: 2'>"+(column+j+1)+"</span> " +
                                "</div>";
                        }
                    }
                }
                count+="</div>";
            }
            rootdiv.html(count);
            $('.'+temlateClass).each(function(){
                $(this).click(function(){
                    if(inputcrrentid===undefined||inputcrrentid===null){
                        return;
                    }
                    console.log("inputcrrentid-->"+inputcrrentid);
                    var avainput = document.getElementById(inputcrrentid);
                    var span = $("#"+this.id).children("span");
                    if(avainput.value<1){
                        if(span.text()==='0'){
                            return;
                        }
                    }
                    avainput.value = avainput.value+span.text();
                    var value = parseInt(avainput.value);
                    console.log("avatime_pop_value-->"+value);
                    if(value.length<2){
                        avainput.value = '0'+avainput.value;
                    }
                    if(value.length>2&&value.indexOf('0')===0){
                        avainput.value = avainput.value.replace('0','');
                    }
                    var year = parseInt(new Date().getFullYear());
                    var span = $("#avatime_year_span").html();
                    if(span.indexOf('生产')!=-1&&inputcrrentid==='avatime_pop_window_madeyear'&&value>year){
                        $("#avatime_tip").html("不可大于当前年份");
                        $("#avatime_pop_window_madeyear").val("");
                    }
                    if(inputcrrentid==='avatime_pop_window_mademonth'&&value>12){
                        $("#avatime_tip").html("不可大于当前月份");
                        $("#avatime_pop_window_mademonth").val("");
                    }
                    if(inputcrrentid==='avatime_pop_window_madeday'){
                        var mon = $("#avatime_pop_window_mademonth").val();
                        if(mon==='1'||
                            mon==='3'||
                            mon==='5'||
                            mon==='7'||
                            mon==='8'||
                            mon==='10'||
                            mon==='12'){
                            if(value>31){
                                $("#avatime_tip").html("不可大于当前日期");
                                $("#avatime_pop_window_madeday").val("");
                            }
                        }
                        if(mon==='2'){
                            if(value>28){
                                $("#avatime_tip").html("不可大于当前日期");
                                $("#avatime_pop_window_madeday").val("");
                            }
                        }
                        if(mon==='4'||
                            mon==='6'||
                            mon==='9'||
                            mon==='11'){
                            if(value>30){
                                $("#avatime_tip").html("不可大于当前日期");
                                $("#avatime_pop_window_madeday").val("");
                            }
                        }
                    }
                })
            })
        }

        function switchUnitLot(type) {
            console.log("dateType-->"+type);
            var datetype = '';
            if(type==='DAY'){
                datetype = '天';
            }
            if(type==='MONTH'){
                datetype = '月';
            }
            if(type==='YEAR'){
                datetype = '年';
            }
            return datetype;
        }

        function deleteinput(inputid) {
            if(inputid===null||inputid===undefined){
                return;
            }
            console.log("inputer.valueType-->"+typeof $("#"+inputid).val());
            if('number'===typeof inputid){
                if($("#"+inputid).val()<=0){
                    return;
                }else{
                    $("#"+inputid).val($("#"+inputid).val()-1);
                }
            }
            if('string'=== typeof inputid){
                var inputlength = $("#"+inputid).val().length;
                if(inputlength<=0){
                    $("#"+inputid).val("");
                }else{
                    $("#"+inputid).val($("#"+inputid).val().substring(0,inputlength-1));
                }
            }
        }
        
        function reveive_ToteFillGrid(rootdiv,totalnum,column,flag,temlateClass,tableSpace,SpanFontSize,arrayResource) {
            console.log("maxAmount--->"+$rootScope.maxAmount);
            if(($rootScope.maxAmount-3)<=0){
                return;
            }
            if(rootdiv==null||rootdiv==undefined||row<1||column<1)return;
            var count  = "";
            var row = parseInt(totalnum/3);
            var yushu = parseInt(totalnum%3);
            var isYushu = false;
            var num = 0;
            if(yushu>0){
                row=row+1;
                isYushu = true;
            }
            var countNum = 0;
            for (var i = 0;i<row;i++){
                count += "<div style='float: left;width:100%;height:45%;margin-top: 2%'>"
                for (var j=0;j<3;j++){
                    count +="<div id='"+arrayResource[countNum].positionIndex+"' class='"+temlateClass+"' style='width:32%;height:97.5%;margin-left: 1%'> " +
                        "<span class='receiveToToteSpan'>"+(countNum+1)+"</span>"+
                        "<div id='"+arrayResource[countNum].positionIndex+"_div' class='receiveToToteInnerDiv' style='background-color:#8c8c8c '>" +
                        "<span id='"+arrayResource[countNum].positionIndex+"_span' style='font-size: 24px' " +
                        "class='receiving-uptopod-label'></span>" +
                        "</div>"+
                        "</div>";
                    countNum++;
                }
                if(i===(row-1)&&isYushu){
                    for (var j=0;j<yushu;j++){
                        count +="<div id='"+arrayResource[countNum].positionIndex+" class='"+temlateClass+"' id='"+flag+i+j+"'"+
                            "style='width:32%;height:97.5%;margin-left: 1%'> " +
                            "<span class='receiveToToteSpan'>"+(countNum+1)+"</span>"+
                            "<div id='"+arrayResource[countNum].positionIndex+"_div' class='receiveToToteInnerDiv' style='background-color:#8c8c8c' >" +
                            "<span id='"+arrayResource[countNum].positionIndex+"_span' style='font-size: 24px' " +
                            "class='receiving-uptopod-label'></span>" +
                            "</div>"+
                            "</div>";
                    }
                    countNum++;
                }
                count+="</div>";
                num++;
            }
            rootdiv.html(count);
        }

        function getavatimeid(currentid) {
            inputcrrentid = currentid;
        }



        function getLocationTypes(callback) {
            callback:callback(selectedArrays,selectedAreaArrays);
        }

        function getLocationTypesInPage(callback) {
            callback:callback(selectedBinArrays);
        }
        function removeByValue(arrays,val){
            for(var i=0; i<arrays.length; i++) {
                if(arrays[i] == val) {
                    arrays.splice(i, 1);
                    break;
                }
            }
        }

        function getObjCount(obj){
            var objType = typeof obj;
            if(objType == "string"){
                return obj.length;
            }else if(objType == "object"){
                var objLen = 0;
                for(var i in obj){
                    objLen++;
                }
                return objLen;
            }
            return false;
        }

        function getDays(currentDate,pushTime,type) {//输入时间，有效期，类型
            var days = null;
            if(type===INBOUND_CONSTANT.YEAR){
                var cc1 = new Date(currentDate);
                cc1.setFullYear((cc1.getFullYear())+parseInt(pushTime));
                var ddd = cc1.getTime();
                /*days = pushTime*365;
                var ddd = new Date(currentDate.replace("-"/g,",")).getTime()+(days*24*60*60*1000);*/
            }
            if(type===INBOUND_CONSTANT.MONTH){
               // currentDate.replace("-",",");
                var cc1 = new Date(currentDate);
                cc1.setMonth((cc1.getMonth())+parseInt(pushTime));
                var ddd = cc1.getTime();
                //days = pushTime*30;
            }
            if(type===INBOUND_CONSTANT.DAY){
                var cc1 = new Date(currentDate);
                cc1.setDate((cc1.getDate())+parseInt(pushTime));
                var ddd = cc1.getTime();
                /*days = pushTime;
                var ddd = new Date(currentDate.replace("-"/g,",")).getTime()+(days*24*60*60*1000);*/
            }
            return ddd;
        }

        function getDateFormat(date) {
            var currentDate = '';
            var date1 = new Date(date);
            currentDate+= date1.getFullYear() + "-";
            var Month = date1.getMonth()+1;
            var Day = date1.getDate();

            if (Month >= 10 )
            {
                currentDate += Month + "-";
            }
            else
            {
                currentDate += "0" + Month + "-";
            }
            if (Day >= 10 )
            {
                currentDate += Day ;
            }
            else {
                currentDate += "0" + Day;
            }
            return currentDate;
        }

        function getDate() {
            var day = new Date();
            var Year = day.getFullYear();
            var Month = day.getMonth()+1;
            var Day = day.getDate();
            var CurrentDate = '';
            CurrentDate+= Year + "-";
            if (Month >= 10 )
            {
                CurrentDate += Month + "-";
            }
            else
            {
                CurrentDate += "0" + Month + "-";
            }
            if (Day >= 10 )
            {
                CurrentDate += Day ;
            }
            else {
                CurrentDate += "0" + Day;
            }
            return CurrentDate;
        }

        //自动匹配扫的是DN还是商品编码
        function isDN(str) {
            if(str===undefined||str===''||str===null)
                return;
            var strs = str.toUpperCase();
            var indexOne = strs.indexOf("D");
            var indexTwo = strs.indexOf("N");
            var length = getObjCount(strs);
            console.log("D--->"+indexOne);
            console.log("N--->"+indexTwo);
            if(indexOne===0&&indexTwo===1&&length===17){
                return true;
            }else{
                return false;
            }
        }
        //自动识别是否是车牌
        function isCiper(str) {
            var indexOne = str.indexOf("C");
            var indexTwo = str.indexOf("I");
            var length = getObjCount(str);
            console.log("C--->"+indexOne);
            console.log("I--->"+indexTwo);
            if(indexOne===0&&indexTwo===1){
                return true;
            }else{
                return false;
            }
        }

        function closePopWindow(windowid) {
            var window = $("#"+windowid).data("kendoWindow");
            console.log(""+windowid+"/window-->"+window);
            window.close();
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
            reveive_ToteFillGrid:reveive_ToteFillGrid,
            grid_BayType:grid_BayType,
            getObjCount:getObjCount,
            closePopWindow:closePopWindow,
            fillGrid:fillGrid,
            getLocationTypes:function (callback) {
                callback(selectedArrays);
            },
            deleteinput:deleteinput,
            findStorageLocation:findStorageLocation,
            isDN :isDN,
            isCiper:isCiper,
            getDays:getDays,
            getDate:getDate,
            getDateFormat:getDateFormat,
            switchUnitLot:switchUnitLot,
            grid_ReportMenu:grid_ReportMenu,
            grid_SideStorage:grid_SideStorage,
            findSideStorageLocation:findSideStorageLocation,
            grid_BayTypeInPage:grid_BayTypeInPage,
            getLocationTypesInPage:getLocationTypesInPage,
            findStorageLocationInTote:findStorageLocationInTote,
            removeByValue:removeByValue,
            findStorageLocationIndexInTote:findStorageLocationIndexInTote,
            findStorageLocationIndexInAllTote:findStorageLocationIndexInAllTote,
            colorIntroStorage:colorIntroStorage,
            backPodStorageColor:backPodStorageColor
        };
    });
    })();