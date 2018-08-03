package com.mushiny.wms.outboundproblem.web;

import com.mushiny.wms.outboundproblem.crud.common.dto.InboundProblemRuleDTO;
import com.mushiny.wms.outboundproblem.crud.common.dto.MoveGoodsDTO;
import com.mushiny.wms.outboundproblem.crud.dto.*;
import com.mushiny.wms.outboundproblem.service.OBProblemService;
import com.mushiny.wms.outboundproblem.crud.common.dto.ItemDataGlobalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/outboundproblem")
public class OBProblemController {

    private final OBProblemService obproblemService;

    @Autowired
    public OBProblemController(OBProblemService obproblemService) {
        this.obproblemService = obproblemService;
    }

    @RequestMapping(value = "/generate-obp",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> generateOBProblem(@RequestBody OBProblemDTO obProblemDTO) {
        obproblemService.generateOBProblem(obProblemDTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBProblemCheckDTO> create(@RequestBody OBProblemCheckDTO dto) {
        return ResponseEntity.ok(obproblemService.create(dto));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"storageLocationId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BigDecimal> createContainersAndSumAmount(@RequestParam String storageLocationId) {
        return ResponseEntity.ok(obproblemService.createLess(storageLocationId));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        obproblemService.delete(id);
        return ResponseEntity.ok().build();
    }
    //左右移动更新状态  unsolved --- process 异常处理完（state：CLOSE）并判断是否解绑小车
    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBProblemCheckDTO> update(@RequestBody OBProblemCheckDTO dto) {
        return ResponseEntity.ok(obproblemService.update(dto));
    }

    @RequestMapping(value = "/updateCloses",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OBProblemCheckDTO>> updateClose(@RequestParam List<String> ids,
                                                          @RequestParam String name) {
        return ResponseEntity.ok(obproblemService.updateClose(ids,name));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBProblemCheckDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(obproblemService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"problemId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OBProblemDTO>> getByContainer(@RequestParam String container){
        return ResponseEntity.ok(obproblemService.getByContainer(container));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"problemStorageLocation"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BigDecimal> getByLocationNo(@RequestParam String problemStorageLocation) {
        return ResponseEntity.ok(obproblemService.sumByProblemStorageLocationAndOpen(problemStorageLocation));
    }

    @RequestMapping(value = "/universalSearch",
            method = RequestMethod.GET,
            params = {"state", "seek"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OBProblemCheckDTO>> getBySeek(
            @RequestParam String state,
            @RequestParam(required = false) String userName,
            @RequestParam String seek,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(obproblemService.getBySeek(state, userName, seek, startDate, endDate));
    }

    @RequestMapping(value = "/item/{obproblemId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDataGlobalDTO> getByItem(@PathVariable String obproblemId) {
        return ResponseEntity.ok(obproblemService.getByItem(obproblemId));
    }
    //获取容器的上货历史记录
    @RequestMapping(value = "/records/{obproblemId}/{jobType}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StowStockunitRecordDTO>> sumByAmountAndActualAndTotal(@PathVariable String obproblemId,
                                                                                     @PathVariable String jobType) {
        return ResponseEntity.ok(obproblemService.sumStockunitRecordByProblem(obproblemId,jobType));
    }
    //rebin车记录
    @RequestMapping(value = "/findList",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OBProblemDataDTO>> findLocationByItem(@RequestParam String problemStoragelocation,
                                                                     @RequestParam String itemNo,
                                                                     @RequestParam String jobType) {
        return ResponseEntity.ok(obproblemService.findLocationByItem(problemStoragelocation ,itemNo,jobType));
    }
    //找到多货位置
    @RequestMapping(value = "/find-overage-goods",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> findoverageGoods(@RequestParam String storageLocation,
                                                 @RequestParam String itemSku,
                                                 @RequestParam BigDecimal amount,
                                                 @RequestParam String fromName,
                                                 @RequestParam String jobType) {
        obproblemService.findOverageGoods(storageLocation, itemSku, amount,fromName,jobType);
        return ResponseEntity.ok().build();
    }
    //找到少货位置
    @RequestMapping(value = "/find-loss-goods",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> findlossGoods(@RequestParam String fromName,
                                              @RequestParam String toName,
                                              @RequestParam String itemSku,
                                              @RequestParam BigDecimal amount,
                                              @RequestParam String jobType) {
        obproblemService.findlossGoods(fromName,toName, itemSku, amount,jobType);
        return ResponseEntity.ok().build();
    }
    //多貨上架
    @RequestMapping(value = "/stow-overage-goods",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> stowoverageGoods(@RequestParam String storageLocation,
                                                 @RequestParam String itemDataId,
                                                 @RequestParam BigDecimal amount,
                                                 @RequestParam String jobType) {
        obproblemService.stowoverageGoods(storageLocation, itemDataId, amount,jobType);
        return ResponseEntity.ok().build();
    }
    //少貨上架
    @RequestMapping(value = "/stow-loss-goods",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> stowlossGoods(@RequestParam String fromName,
                                              @RequestParam String toName,
                                              @RequestParam String itemDataId,
                                              @RequestParam BigDecimal amount,
                                              @RequestParam String jobType) {
        obproblemService.stowlossGoods(fromName, toName, itemDataId, amount,jobType);
        return ResponseEntity.ok().build();
    }

    //盘盈
    @RequestMapping(value = "/overage-goods",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> overageGoods(@RequestBody OverageGoodsDTO overageGoodsDTO) {
        obproblemService.overageGoods(overageGoodsDTO);
        return ResponseEntity.ok().build();
    }
    //盘亏
    @RequestMapping(value = "/loss-goods",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> lossGoods(@RequestBody LossGoodsDTO lossGoodsDTO) {
        obproblemService.lossGoods(lossGoodsDTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OBProblemCheckDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(obproblemService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<OBProblemCheckDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(obproblemService.getBySearchTerm(search, pageable));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OBProblemCheckDTO>> getAll() {
        return ResponseEntity.ok(obproblemService.getAll());
    }

    @RequestMapping(value = "/analysis/{ids}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map>> getAnalysis(@PathVariable List<String> ids) {
        return ResponseEntity.ok(obproblemService.getAnalysis(ids));
    }

    @RequestMapping(value = "/moving",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> moving(@RequestBody MoveGoodsDTO moveGoodsDTO) {
        obproblemService.moving(moveGoodsDTO);
        return ResponseEntity.ok().build();
    }


    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"rule"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InboundProblemRuleDTO> getRule(@RequestParam String rule){
        return ResponseEntity.ok(obproblemService.getRule(rule));
    }


    //OB问题核实  绑定工作站
    @RequestMapping(value = "/binding-workstation",
            method = RequestMethod.GET,
            params = {"name"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBPStationDTO> bindingWorkstation(@RequestParam String name) {
        return ResponseEntity.ok(obproblemService.bindingWorkstation(name));
    }

    //OB问题核实 解绑工作站
    @RequestMapping(value = "/untie-workstation",
            method = RequestMethod.GET,
            params = {"name"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBPStationDTO> untieWorkstation(@RequestParam String name) {
        return ResponseEntity.ok(obproblemService.untieWorkstation(name));
    }

    //停止呼叫pod和恢复分配pod
    @RequestMapping(value = "/stopCallPod",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> getStopCallPod(@RequestParam String workStationId, @RequestParam String type) {
        obproblemService.getStopCallPod(workStationId, type);
        return ResponseEntity.ok().build();
    }

    //获取上货历史记录的PodFace
    @RequestMapping(value = "/records/podFace",
            method = RequestMethod.GET,
            params = {"outboundProblemIds","sectionId","jobType"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map>> getPodFace(@RequestParam List<String> outboundProblemIds,@RequestParam String sectionId,@RequestParam String jobType) {
        return ResponseEntity.ok(obproblemService.getPodFace(outboundProblemIds,sectionId,jobType));
    }


    // 返回时查状态
    @RequestMapping(value = "/workStationPodState",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> getWorkStationPodState(@RequestParam String workStationId) {
        return ResponseEntity.ok(obproblemService.getWorkStationPodState(workStationId));
    }

    // 检查是否有在途中的Pod
    @RequestMapping(value = "/yesOrNoFinsh/goBack",
            method = RequestMethod.GET,
            params = {"name"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> yesOrNoFinsh(@RequestParam("name")String name) {
        return ResponseEntity.ok(obproblemService.getYesOrNoFinsh(name));
    }







}
