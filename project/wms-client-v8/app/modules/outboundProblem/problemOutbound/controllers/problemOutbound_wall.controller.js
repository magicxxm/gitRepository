/**
 * Created by frank.zhou on 2017/5/15.
 * Updated by frank.zhou on 2017/07/10.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("problemOutboundWallCtl", function ($scope, $rootScope, $state, $sce, FLOOR_COLOR, problemOutboundBaseService, problemOutboundService) {

    // 放置有货 每行颜色
    problemOutboundService.problemCellPlaceGoods($scope.obpWallId, function(datas){
      //
      for(var i = 0, items = []; i < datas.numberOfRows; i++){
        items[i] = datas.obpCellPositions.slice(i*datas.numberOfColumns, (i+1)*datas.numberOfColumns);
      }
      //
      for(var i = 0, htmls = []; i < datas.numberOfRows; i++){
        htmls.push("<tr>");
        for(var cells = items[i], j = cells.length -1 ; j >= 0; j--){
          var cell = cells[j], pos = problemOutboundBaseService.transNumToStr(cell.yPos-1);
          htmls.push("<td id='"+ cell.id+ "' name='"+ cell.name+ "' state='"+ cell.state+ "' style='background:"+ (cell.state == "occupied"?FLOOR_COLOR[pos]: "#d9d9d9")+ ";cursor:pointer;font-size:20px;color:"+ (cell.state == "occupied"?"white": "")+ "'>"+ cell.name.split("-")[1]+ "</td>");
        }
        htmls.push("</tr>");
      }
      $scope.obpWallContent = $sce.trustAsHtml(htmls.join(""));
      setTimeout(function(){
        $("#obp_wallContent td").each(function(){
          $(this).bind("click", function(){
            var name = $(this).attr("name"), id = $(this).attr("id");
            $rootScope.obpCellName = name;
            if($(this).attr("state") != "occupied")
              $rootScope.problemCellStatus == "bindCell" && problemOutboundService.bindCell($rootScope.shipmentNo, name, function(){
                $state.go("main.problemOutboundDetail");
              });
            else
              $rootScope.problemCellStatus == "read" && problemOutboundService.getShipmentNoByCellName($rootScope.obpStationId, $rootScope.obpWallId, $rootScope.obpCellName, function(data){
                $rootScope.shipmentNo = data[0].shipmentNo;
                $state.go("main.problemOutboundDetail");
              });
          });
        });
      }, 300);
    });

    $scope.returnBackShipment = function(){
      $state.go("main.problemOutboundShipment");
    }
  });
})();