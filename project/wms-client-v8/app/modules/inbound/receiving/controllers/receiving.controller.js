
/**
 * Created by frank.zhou on 2016/12/13.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("receivingCtl", function ($scope, $window){
    $("#receiving_user").html($window.localStorage["name"]); // 当前用户
      console.log("recevingCtl loaded...");
  }).controller("receivingContainerCtl", function($scope, $rootScope, $state, $stateParams,receiving_commonService,receivingService,commonService,INBOUND_CONSTANT){
      console.log("recevingContainerCtl loaded...");
    // ========================================================================================
      $scope.status = '0';
      $scope.scanhead = '0';
      var scan_pod = false;
      var scan_product_content= false;
      var scan_DN = false;
      var scan_product_info= false;
      var thisid;
      $("#tipDiv").hide();
    function refreshReceivingContainer(receivingStationId, isInit){
      isInit == null && (isInit = false);
      receivingService.getReceivingContainer(receivingStationId, function(datas){
        var isMax = (datas.length === $scope.maxAmount), gridId = "receivingGRID";
        // 初始化
        if(isInit){
          $scope.status = (isMax? "max": "normal");
          $scope.status === "normal" && (setTimeout(function(){ $("#receiving_destination").focus();}, 100));
          isMax && (gridId = "receivedGRID");
        }
        // 数据
        var grid = $("#"+ gridId).data("kendoGrid");
        grid.setDataSource(new kendo.data.DataSource({data: datas}));
        $rootScope.receivingProcessContainers = datas;
        // 跳转收货页面
        if(!isInit && isMax){
          setTimeout(function(){
            $state.go($scope.receivingCurrent==='single'? "main.receivingSingle": "main.receivingPallet");
          }, 1000);
        }
      });
    }
    // 跳转收货页面
    $scope.toReceiving = function(){
        if($scope.receivingCurrent==='Each Receive To Stow'){
            $rootScope.currentReceive = 'Each Receive To Stow';
            $state.go('main.receivingSingle');
        }
        if($scope.receivingCurrent==='Pallet Receive To Stow'){
            $rootScope.currentReceive = 'Pallet Receive To Stow';
            $state.go('main.receivingPallet');
        }
        if($scope.receivingCurrent==='Each Receive To Tote'){
            $rootScope.currentReceive = 'Each Receive To Tote';
            $state.go('main.receivingTote');
        }
    };
    // 扫描工作站
    $scope.scanStation = function(e){
          var keyCode = window.event? e.keyCode: e.which;
          if(keyCode != 13) {
              return;
          }
          receivingService.scanStation($scope.station,$stateParams.id,function (data) {
              $("#tipDiv").hide();
              console.log("data-->",data);
              $rootScope.scan_product_content_DAMAGED=true;
              $rootScope.scan_product_content_MEASURED=true;
              $rootScope.scan_product_content_TO_INVESTIGATE=true;//已绑框为true,未绑框为false
              $scope.maxAmount=data.cls.maxAmount;//应绑框数
              $scope.stationId = data.cls.receiveStationId; // 工作站id
              $scope.stationName = data.cls.receiveStationName; // 工作站name
              $rootScope.stationId = $scope.stationId;
              $rootScope.stationName = $scope.stationName;
              $rootScope.sectionId = data.cls.sectionId;//地图区域
              $rootScope.workStationId = data.cls.workStationId;
              $rootScope.locationTypeSize=data.cls.locationTypeSize;//绑几种类型的bin，为空则提示选择bin类型
              $rootScope.maxAmount=data.cls.maxAmount;
              $rootScope.processSize = data.cls.processSize;//实际绑框数
              $rootScope.areaSize = data.cls.areasSize;
              $rootScope.receiveProcessDTOList = data.cls.receiveProcessDTOList;
              $rootScope.normalStorageList  = new Array();
              $rootScope.receivecallingpodflag = data.cls.callingPodFlag;
              var length = receiving_commonService.getObjCount($rootScope.receiveProcessDTOList);
              var array = new Array();
              for (var i=0;i<length;i++){//Damage
                  if($rootScope.receiveProcessDTOList[i].storageType.toLowerCase()===INBOUND_CONSTANT.DAMAGED.toLowerCase()){
                      $rootScope.demagedDestinationId = $rootScope.receiveProcessDTOList[i].destinationId;
                      $rootScope.demagedPositionIndex = $rootScope.receiveProcessDTOList[i].positionIndex;
                      if($rootScope.receiveProcessDTOList[i].receiveStorageName===null||
                          $rootScope.receiveProcessDTOList[i].receiveStorageName===''||
                          $rootScope.receiveProcessDTOList[i].receiveStorageName===undefined){
                          $rootScope.scan_product_content_DAMAGED=false;

                      }
                  }//Measure
                  if($rootScope.receiveProcessDTOList[i].storageType.toLowerCase()===INBOUND_CONSTANT.MEASURED.toLowerCase()||
                      $rootScope.receiveProcessDTOList[i].storageType.toLowerCase()==="measure"){
                      $rootScope.measuredDestinationId = $rootScope.receiveProcessDTOList[i].destinationId;
                      $rootScope.measuredPositionIndex = $rootScope.receiveProcessDTOList[i].positionIndex;
                      if($rootScope.receiveProcessDTOList[i].receiveStorageName===null||
                          $rootScope.receiveProcessDTOList[i].receiveStorageName===''||
                          $rootScope.receiveProcessDTOList[i].receiveStorageName===undefined){
                          $rootScope.scan_product_content_MEASURED=false;
                      }
                  }//Pending
                  if($rootScope.receiveProcessDTOList[i].storageType.toLowerCase()===INBOUND_CONSTANT.TO_INVESTIGATE.toLowerCase()){
                      $rootScope.investDestinationId = $rootScope.receiveProcessDTOList[i].destinationId;
                      $rootScope.investPositionIndex = $rootScope.receiveProcessDTOList[i].positionIndex;
                      if($rootScope.receiveProcessDTOList[i].receiveStorageName===null||
                          $rootScope.receiveProcessDTOList[i].receiveStorageName===''||
                          $rootScope.receiveProcessDTOList[i].receiveStorageName===undefined){
                          $rootScope.scan_product_content_TO_INVESTIGATE=false;

                      }
                  }//Inventory
                  if($rootScope.receiveProcessDTOList[i].storageType.toLowerCase()===INBOUND_CONSTANT.GENUINE.toLowerCase()){
                      console.log("正品--->"+$rootScope.receiveProcessDTOList[i].storageType);
                      $rootScope.normalStorageList.push({
                          "index":i,
                          "positionIndex":$rootScope.receiveProcessDTOList[i].positionIndex,
                          "destinationId":$rootScope.receiveProcessDTOList[i].destinationId,
                          "storageName":$rootScope.receiveProcessDTOList[i].receiveStorageName
                      });
                  }
                  //后台当绑框时才会给receiveStorageName赋值
                  if($rootScope.receiveProcessDTOList[i].receiveStorageName!==null&&
                      $rootScope.receiveProcessDTOList[i].receiveStorageName!==''&&
                      $rootScope.receiveProcessDTOList[i].receiveStorageName!==undefined){
                      array.push($rootScope.receiveProcessDTOList[i]);
                  }
              }
              if($rootScope.processSize===0){
                  $rootScope.scan_product_content_DAMAGED=false;
                  $rootScope.scan_product_content_MEASURED=false;
                  $rootScope.scan_product_content_TO_INVESTIGATE=false;
                  receivingService
                  if($scope.receivingCurrent==='Each Receive To Stow'){
                      $rootScope.currentReceive = 'Each Receive To Stow';
                      $state.go('main.receivingSingle');
                  }
                  if($scope.receivingCurrent==='Pallet Receive To Stow'){
                      $rootScope.currentReceive = 'Pallet Receive To Stow';
                      $state.go('main.receivingPallet');
                  }
                  if($scope.receivingCurrent==='Each Receive To Tote'){
                      $rootScope.currentReceive = 'Each Receive To Tote';
                      $state.go('main.receivingTote');
                  }
              }else{
                  $scope.status='max';//让是否满筐界面显示
                  $scope.scanhead='1';//隐藏扫描工作站界面
                  $scope.receivingButton='!confirm';//默认选中继续使用已绑定框按钮
                  $("#receivedGRID").data("kendoGrid").setDataSource(new kendo.data.DataSource({data: array}));//显示已绑框的表格信息
              }
          },function (data) {
              $scope.scanstatus = '1';
              $scope.station = '';
              $("#warnStation").html(data.key||data.message);
              $scope.LOGINSTATE = data.key||data.message;
              $("#tipDiv").fadeIn(500);
              // setTimeout(function () {
              //     $("#tipDiv").fadeOut(1000);
              // },500);
          });
      };
    // 自动满筐
    $scope.deleteReceivingContainer = function(e){
        $scope.status= 'init';
        $scope.receivingMode= 'init';
        $scope.receivingButton= 'init';
        console.log("$rootScope.normalStorageList--->",$rootScope.normalStorageList);
        receivingService.autoFullStorageLocations($rootScope.stationId,function () {
            $rootScope.scan_product_content_DAMAGED=false;
            $rootScope.scan_product_content_MEASURED=false;
            $rootScope.scan_product_content_TO_INVESTIGATE=false;

            if($scope.receivingCurrent==='Each Receive To Stow'){
                $rootScope.currentReceive = 'Each Receive To Stow';
                $state.go('main.receivingSingle');
            }
            if($scope.receivingCurrent==='Pallet Receive To Stow'){
                $rootScope.currentReceive = 'Pallet Receive To Stow';
                $state.go('main.receivingPallet');
            }
            if($scope.receivingCurrent==='Each Receive To Tote'){
                $rootScope.currentReceive = 'Each Receive To Tote';
               // receivingService.findgridStorageInfo($rootScope.stationName,$rootScope.currentReceive,function (data) {
                //    $rootScope.receiveProcessDTOList = data.cls.receiveProcessDTOList;
                for (var l=0;l<$rootScope.normalStorageList.length;l++){
                     $rootScope.normalStorageList[l].storageName=null;
                }
               // },function (data) {
               // });
                $state.go('main.receivingTote');
            }
        },function (data) {
            alert("满筐失败...");
        });
    };

    $scope.autoClose = function (e) {
        if(!receiving_commonService.autoAddEvent(e)) return;
        var window = $("#tipwindow").data("kendoWindow");
        window.close();
    };
    //有效期输入框焦点函数
    $scope.avatimemethod = function (currentid) {
        receiving_commonService.getavatimeid(currentid);
    };


    $scope.startPod = function () {
        // 跳转收货页面
        setTimeout(function(){
            $state.go($scope.receivingCurrent==='single'? "main.receivingSingle": "main.receivingPallet");
        }, 1000);
        setTimeout(function(){ $("#receiving-inputer").focus();}, 200);
    };


    // 初始化
    $scope.receivingCurrent = $stateParams.id; // 当前收货模式
    console.log("收货模式-->"+$scope.receivingCurrent);
    var headerStyle = {style:"font-size:16px;line-height:2;color:white;height:35px;background-color:'#ef7421';overflow:hidden"};
    var baseStyle = {style: "font-size:16px;height:25px;text-align:center;background-color:light;overflow:hidden"};
    var columns= [
          {field: "positionIndex", headerTemplate: "<span translate='NO'></span>", attributes: baseStyle,headerAttributes:headerStyle},
          {field: "desinationName", headerTemplate: "<span translate='POSITION_NO'></span>", attributes: baseStyle,headerAttributes:headerStyle},
          {field: "receiveStorageName", headerTemplate: "<span translate='PICKCAR_NO'></span>", attributes: baseStyle,headerAttributes:headerStyle},
          {field: "amount", headerTemplate: "<span translate='SKU_COUNT'></span>", attributes: baseStyle,headerAttributes:headerStyle}
      ];
    $scope.receivingGridOptions = { height: 260, columns: columns};
    $scope.receivedGridOptions = { height: 260, columns: columns};
    setTimeout(function(){ $("#receiving_station").focus();}, 200); // 首获焦
  });
})();