package com.mushiny.wms.outboundproblem.business;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.config.security.SecurityUtils;
import com.mushiny.wms.outboundproblem.business.common.OutBoundProblemCommonBusiness;
import com.mushiny.wms.outboundproblem.business.enums.SolveResoult;
import com.mushiny.wms.outboundproblem.business.enums.StockUnitState;
import com.mushiny.wms.outboundproblem.business.utils.StringDateUtil;
import com.mushiny.wms.outboundproblem.crud.dto.OBPSolvePositionDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBProblemDTO;
import com.mushiny.wms.outboundproblem.crud.mapper.OBPSolvePositionMapper;
import com.mushiny.wms.outboundproblem.crud.mapper.OBProblemMapper;
import com.mushiny.wms.outboundproblem.domain.*;
import com.mushiny.wms.outboundproblem.domain.common.*;
import com.mushiny.wms.outboundproblem.domain.enums.InventoryState;
import com.mushiny.wms.outboundproblem.domain.enums.OBPSolveState;
import com.mushiny.wms.outboundproblem.domain.enums.ProblemType;
import com.mushiny.wms.outboundproblem.exception.OutBoundProblemException;
import com.mushiny.wms.outboundproblem.query.SolveDamageQuery;
import com.mushiny.wms.outboundproblem.repository.*;
import com.mushiny.wms.outboundproblem.repository.common.*;
import com.mushiny.wms.outboundproblem.service.OBProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class SolveDamagedBusiness {
    private final ApplicationContext applicationContext;
    private final OBPSolveRepository obpSolveRepository;
    private final OBPSolvePositionRepository obpSolvePositionRepository;
    private final OBPSolvePositionMapper obpSolvePositionMapper;
    private final SolveDamageQuery solveDamageQuery;
    private final OutBoundProblemCommonBusiness outBoundProblemCommonBusiness;
    private final OBProblemService obProblemService;
    private final ItemDataRepository itemDataRepository;
    private final CustomerShipmentRepository customerShipmentRepository;
    private final OBPSolveBusiness obpSolveBusiness;
    private final OBProblemRepository obProblemRepository;
    private final OBProblemMapper obProblemMapper;
    private final MoveGoodsBusiness moveGoodsBusiness;
    private final StorageLocationRepository storageLocationRepository;
    private final BuildBusiness buildBusiness;
    private final UnitLoadBusiness unitLoadBusiness;
    private final UnitLoadRepository unitLoadRepository;
    private final LocationRepository locationRepository;
    private final StockUnitRepository stockUnitRepository;
    private final StockUnitRecordRepository stockUnitRecordRepository;
    private final OBPShipmentSerialNoRepository obpShipmentSerialNoRepository;
    private final GeneralBusiness generalBusiness;

    @Autowired
    public SolveDamagedBusiness(ApplicationContext applicationContext,
                                OBPSolveRepository obpSolveRepository,
                                OBPSolvePositionRepository obpSolvePositionRepository,
                                OBPSolvePositionMapper obpSolvePositionMapper,
                                SolveDamageQuery solveDamageQuery,
                                OutBoundProblemCommonBusiness outBoundProblemCommonBusiness,
                                OBProblemService obProblemService,
                                ItemDataRepository itemDataRepository,
                                CustomerShipmentRepository customerShipmentRepository,
                                OBPSolveBusiness obpSolveBusiness,
                                OBProblemRepository obProblemRepository,
                                OBProblemMapper obProblemMapper,
                                MoveGoodsBusiness moveGoodsBusiness,
                                StorageLocationRepository storageLocationRepository,
                                BuildBusiness buildBusiness,
                                UnitLoadBusiness unitLoadBusiness,
                                UnitLoadRepository unitLoadRepository,
                                LocationRepository locationRepository,
                                StockUnitRepository stockUnitRepository,
                                StockUnitRecordRepository stockUnitRecordRepository,
                                OBPShipmentSerialNoRepository obpShipmentSerialNoRepository,
                                GeneralBusiness generalBusiness) {
        this.applicationContext = applicationContext;
        this.obpSolveRepository = obpSolveRepository;
        this.obpSolvePositionRepository = obpSolvePositionRepository;
        this.obpSolvePositionMapper = obpSolvePositionMapper;
        this.solveDamageQuery = solveDamageQuery;
        this.outBoundProblemCommonBusiness = outBoundProblemCommonBusiness;
        this.obProblemService = obProblemService;
        this.itemDataRepository = itemDataRepository;
        this.customerShipmentRepository = customerShipmentRepository;
        this.obpSolveBusiness = obpSolveBusiness;
        this.obProblemRepository = obProblemRepository;
        this.obProblemMapper = obProblemMapper;
        this.moveGoodsBusiness = moveGoodsBusiness;
        this.storageLocationRepository = storageLocationRepository;
        this.buildBusiness = buildBusiness;
        this.unitLoadBusiness = unitLoadBusiness;
        this.unitLoadRepository = unitLoadRepository;
        this.locationRepository = locationRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.stockUnitRecordRepository = stockUnitRecordRepository;
        this.obpShipmentSerialNoRepository = obpShipmentSerialNoRepository;
        this.generalBusiness = generalBusiness;
    }

    public OBPSolvePosition solveOBProblem(OBPSolvePositionDTO dto) {
        OBPSolvePosition solvePosition;
        List<OBPSolve> solveProblems = obpSolveRepository.getProblemGoodsByShipmentAndItem(applicationContext.getCurrentWarehouse(), dto.getShipmentNo(), dto.getItemNo());
        OBPSolve solveProblem = new OBPSolve();
        OBPSolve obpSolve = obpSolveRepository.getGoodsByShipmentAndItem(applicationContext.getCurrentWarehouse(), dto.getShipmentNo(), dto.getItemNo());
        String state = getStateBySolveKey(dto.getSolveKey());
        //表示正常那条数据转问题
        OBPSolve solveNormal = new OBPSolve();
        BigDecimal amountConfirmProblem=getAmountConfirmProblem(dto);

        String problemType = "";
        switch (dto.getSolveKey()) {
            case "CONFIRM_DAMAGED":
                problemType = ProblemType.DAMAGED.toString();
                break;
            case "GOODS_DAMAGED":
                problemType = ProblemType.DAMAGED.toString();
                break;
            case "GOODS_LOSS":
                problemType = ProblemType.LOSE.toString();
                break;
            case "UNABLE_SCAN_SKU":
                problemType = ProblemType.UNABLE_SCAN_SKU.toString();
                break;
            case "UNABLE_SCAN_SN":
                problemType = ProblemType.UNABLE_SCAN_SN.toString();
                break;
        }
        boolean a=verifySolved(obpSolve,dto.getAmountConfirmProblem(),problemType);
        if(dto.getSerialNo()!=null&&!dto.getSerialNo().isEmpty()) verifySerialNo(dto);

        if (solveProblems == null || solveProblems.isEmpty()) {
            //将正常商品转为问题商品-添加问题（solveProblem为空，表示之前该类商品没有问题）
            if (dto.getProblemResources().equals(SolveResoult.NORMAL.toString())) {
                OBProblemDTO problemDTO = new OBProblemDTO();
                problemDTO.setDescription("");
                problemDTO.setItemDataId(itemDataRepository.getByItemNo(applicationContext.getCurrentWarehouse(),applicationContext.getCurrentClient(),dto.getItemNo()).getId());
                problemDTO.setItemNo(dto.getItemNo());
                problemDTO.setContainer(obpSolve.getObpCell().getName());
                problemDTO.setJobType("OBPS");
                problemDTO.setLotNo("");
                problemDTO.setSolvedBy(SecurityUtils.getCurrentUsername());
                problemDTO.setSolveAmount(BigDecimal.ZERO);
                //正常商品转问题 默认数量为0
                problemDTO.setAmount(amountConfirmProblem);
                problemDTO.setProblemStoragelocation(obpSolve.getObpCell().getName());
                problemDTO.setProblemType(problemType);
                problemDTO.setReportBy(SecurityUtils.getCurrentUsername());
                problemDTO.setReportDate(StringDateUtil.getNowFormat());
                problemDTO.setState("unsolved");
                problemDTO.setSerialNo(null);
                problemDTO.setShipmentId(customerShipmentRepository.getByShipmentNo(dto.getShipmentNo()).getId());
                problemDTO.setSkuNo(itemDataRepository.getByItemNo(applicationContext.getCurrentWarehouse(),applicationContext.getCurrentClient(),dto.getItemNo()).getSkuNo());
                problemDTO.setClientId(applicationContext.getCurrentClient());
                problemDTO.setWarehouseId(applicationContext.getCurrentWarehouse());
                obProblemService.creatOBProblem(problemDTO);

                obpSolveBusiness.addProblemToOBPSolve(obpSolve.getObpStation().getId(), obpSolve.getObpWall().getId(), dto.getShipmentNo());

                solveProblems = obpSolveRepository.getProblemGoodsByShipmentAndItem(applicationContext.getCurrentWarehouse(), dto.getShipmentNo(), dto.getItemNo());
                solveProblem = solveProblems.get(0);
                if (obpSolve.getScaned().equals(OBPSolveState.scaned.toString())) {
                    if (obpSolve.getAmountShipment().compareTo(dto.getAmountConfirmProblem()) >= 0) {
                        solveProblem.setAmountScanedProblem(BigDecimal.ZERO);
                        solveProblem.setObpCell(obpSolve.getObpCell());
                        solveProblem.setScaned(OBPSolveState.unscaned.toString());
                        obpSolveRepository.save(solveProblem);
                        //正常商品扫描完成再转条码无法扫描
                        obpSolve.setAmountScaned(obpSolve.getAmountScaned().subtract(amountConfirmProblem));
                        if(problemType.equals(ProblemType.UNABLE_SCAN_SKU.toString())) //正常流程是不存在这种情况
                            obpSolve.setScaned(OBPSolveState.unscaned.toString());
                        obpSolveRepository.save(obpSolve);
                    }
                }else{
                    //如果两件正常商品，扫描了一件，另外一件报丢失，否则正常的那件商品无法转移到问题格
                    if(problemType.equals(ProblemType.LOSE.toString())) {
                        boolean flag= obpSolveBusiness.updateScaned(obpSolve,solveProblem);
                        if(flag) obpSolveBusiness.addRecord(obpSolve.getCustomerShipment().getShipmentNo(), obpSolve.getObpCell().getName());
                    }
                }
            }
        } else {
            //获取当前将要操作的是哪条数据
            solveProblem = obpSolveRepository.retrieve(dto.getSolveId());
            //打印条码传过来的solveId有误
            if(dto.getSolveKey().equals("PRINT_SKU_REPAIR")&&solveProblem.getObproblem()==null){
                solveProblem= obpSolveRepository.getByAmountShipmentAndSku(applicationContext.getCurrentWarehouse(),dto.getShipmentNo(),dto.getItemNo());
            }
            if(dto.getSolveKey().equals("TO_BE_INVESTIGATED")&&solveProblem.getObproblem()==null){
                solveProblem= obpSolveRepository.getByAmountShipmentAndSN(applicationContext.getCurrentWarehouse(),dto.getShipmentNo(),dto.getItemNo());
            }
            solvePosition = obpSolvePositionRepository.getSolvePositionByShipmentNoAndSolve(applicationContext.getCurrentWarehouse(),dto.getShipmentNo(),solveProblem.getId());

            //此流程主要涉及到处理问题时，将会要转问题类型
            String[] solveKey = {SolveResoult.GOODS_LOSS.toString(),SolveResoult.UNABLE_SCAN_SKU.toString(),SolveResoult.UNABLE_SCAN_SN.toString(),SolveResoult.CONFIRM_DAMAGED.toString()};
            if(Arrays.asList(solveKey).contains(dto.getSolveKey())){
                OBProblem problem = solveProblem.getObproblem();
                OBProblem obProblem = new OBProblem();
                // OBProblem solveProblemType = new OBProblem();
                OBPSolve obpSolveProblem = new OBPSolve();
                //flag用来判断类型是否存在
                boolean flag = false;
                for (OBPSolve solve : solveProblems) {
                    if (solve.getObproblem().getProblemType().equals(problemType)) {
                        flag = true;
                        //solveProblemType = solve.getObproblem();
                        if (problem == null) {
                            obProblem = solve.getObproblem();
                            obpSolveProblem = solve;
                            //问题类型已存在，则solvePosition更新为此类问题的处理记录
                            solvePosition = obpSolvePositionRepository.getSolvePositionByShipmentNoAndSolve(applicationContext.getCurrentWarehouse(),dto.getShipmentNo(),solve.getId());
                        }
                        break;
                    }
                }
                //如果该订单下这类商品所转换的问题不存在或是问题类型不存在，则新建问题
                if (problem == null && !flag) {
                    problem = new OBProblem();
                    problem.setAmount(amountConfirmProblem);
                    problem.setDescription("");
                    problem.setItemDataId(itemDataRepository.getByItemNo(applicationContext.getCurrentWarehouse(),applicationContext.getCurrentClient(),dto.getItemNo()).getId());
                    problem.setItemNo(dto.getItemNo());
                    problem.setContainer(obpSolve.getObpCell().getName());
                    problem.setJobType("OBPS");
                    problem.setLotNo("");
                    problem.setSolvedBy(SecurityUtils.getCurrentUsername());
                    problem.setSolveAmount(BigDecimal.ZERO);
                    problem.setProblemStoragelocation(obpSolve.getObpCell().getName());
                    problem.setProblemType(problemType);
                    problem.setReportBy(SecurityUtils.getCurrentUsername());
                    problem.setReportDate(LocalDateTime.now());
                    problem.setState("unsolved");
                    problem.setSerialNo(null);
                    problem.setShipmentId(customerShipmentRepository.getByShipmentNo(dto.getShipmentNo()).getId());
                    problem.setSkuNo(itemDataRepository.getByItemNo(applicationContext.getCurrentWarehouse(),applicationContext.getCurrentClient(),dto.getItemNo()).getSkuNo());
                    problem.setClientId(applicationContext.getCurrentClient());
                    problem.setWarehouseId(applicationContext.getCurrentWarehouse());

                    problem = obProblemService.creatOBProblem(obProblemMapper.toDTO(problem));

                    obpSolveBusiness.addProblemToOBPSolve(obpSolve.getObpStation().getId(), obpSolve.getObpWall().getId(), dto.getShipmentNo());

                    //此时obpSolveProblem为新增加的记录
                    obpSolveProblem = obpSolveRepository.getByProblemAndShipmentAndStation(applicationContext.getCurrentWarehouse(),problem.getId(),dto.getShipmentNo(),obpSolve.getObpStation().getId());
                    obpSolveProblem.setObpCell(obpSolve.getObpCell());
                    solveNormal=obpSolveProblem;
                    if(problemType.equals(ProblemType.DAMAGED.toString())) {
                        obpSolve.setAmountScaned(obpSolve.getAmountScaned().subtract(amountConfirmProblem));
                    }
                    //updateSolve(solveProblem,solveProblems,dto.getAmountConfirmProblem());
                    obpSolveRepository.save(solveProblem);
                    obpSolveRepository.save(obpSolveProblem);
                    obpSolveRepository.save(obpSolve);

                } else if (problem == null && flag) {
                    //点击的是正常商品，且需要转的问题已存在
                    obProblem.setJobType("OBPS");
                    obProblem.setModifiedBy(SecurityUtils.getCurrentUsername());
                    obProblem.setModifiedDate(LocalDateTime.now());
                    obProblem.setReportBy(SecurityUtils.getCurrentUsername());
                    obProblem.setReportDate(LocalDateTime.now());
                    obProblem.setProblemStoragelocation(obpSolve.getObpCell().getName());
                    obProblem.setContainer(obpSolve.getObpCell().getName());
                    //updateSolve(solveProblem,solveProblems,dto.getAmountConfirmProblem());
                    obProblem.setAmount(obProblem.getAmount().add(amountConfirmProblem));
                    //更新问题商品栏位信息
                    //正常已经扫描商品转问题(残损)
                    if(obpSolve.getAmountScaned().compareTo(BigDecimal.ZERO)>0){
                        //obpSolveProblem.setAmountScanedProblem(obpSolveProblem.getAmountScanedProblem().add(dto.getAmountConfirmProblem()));
                        if(problemType.equals(ProblemType.LOSE.toString())){
                            obpSolveProblem.setAmountScanedProblem(BigDecimal.ZERO);
                        }
                        if(problemType.equals(ProblemType.DAMAGED.toString())){
                            obpSolveProblem.setAmountScanedProblem(BigDecimal.ZERO);
                            solveProblem.setAmountScaned(solveProblem.getAmountScaned().subtract(amountConfirmProblem));
                            //操作错误出现已扫描数大于总数
                            if(solveProblems.size()==1&&solveProblem.getAmountScaned().compareTo(solveProblem.getAmountShipment().subtract(obProblem.getAmount()))>0){
                                solveProblem.setAmountScaned(solveProblem.getAmountScaned().add(amountConfirmProblem).subtract(obProblem.getAmount()));
                            }
                        }
                    }else{
                        obpSolveProblem.setAmountScanedProblem(BigDecimal.ZERO);
                    }
                    obpSolveProblem.setAmountScaned(BigDecimal.ZERO);
                    obpSolveProblem.setScaned(OBPSolveState.unscaned.toString());
                    obpSolveProblem.setAmountProblem(obProblem.getAmount());

                    obpSolveRepository.save(solveProblem);
                    obpSolveRepository.save(obpSolveProblem);
                    obProblemRepository.save(obProblem);
                    solveNormal=obpSolveProblem;
                } else if (problem != null) {
                    //选择的是有问题栏位的商品，则更新原来的问题，将原来的问题类型转换成将要更换的问题类型
                    problem.setJobType("OBPS");
                    problem.setModifiedBy(SecurityUtils.getCurrentUsername());
                    problem.setModifiedDate(LocalDateTime.now());
                    problem.setProblemType(problemType);
                    problem.setReportBy(SecurityUtils.getCurrentUsername());
                    problem.setReportDate(LocalDateTime.now());
                    problem.setProblemStoragelocation(obpSolve.getObpCell().getName());
                    problem.setContainer(obpSolve.getObpCell().getName());

                    if (dto.getAmountConfirmProblem().compareTo(BigDecimal.ZERO) > 0) {
                        // problem.setAmount(problem.getAmount().subtract(dto.getAmountConfirmProblem()));
                        solveProblem.setAmountProblem(dto.getAmountConfirmProblem());
                        problem.setAmount(dto.getAmountConfirmProblem());

                        solveProblem.setAmountScanedProblem(BigDecimal.ZERO);
                        solveProblem.setScaned(OBPSolveState.unscaned.toString());
                        solveProblem.setState(OBPSolveState.unsolved.toString());
                        //设置正常商品数据项的已扫描数(原来的已扫描数+问题商品数)
                        if(obpSolve.getAmountScaned().compareTo(BigDecimal.ZERO)>0) {
                            if(dto.getSolveKey().equals(SolveResoult.CONFIRM_DAMAGED.toString()))
                                obpSolve.setAmountScaned(obpSolve.getAmountScaned().subtract(dto.getAmountConfirmProblem()));
                        }
                        obpSolveRepository.save(obpSolve);
                        obpSolveRepository.save(solveProblem);
                    }else{
                        problem.setAmount(BigDecimal.ONE);
                    }
                    obProblemRepository.save(problem);
                }
                //如果问题类型为商品丢失，需默认在OBPSolvePosition表中添加一条相应的确认丢失的记录(前台需要根据solveKey来显示生成HotPick的图标)
                if (problemType.equals(ProblemType.LOSE.toString())) {
                    if (solvePosition == null) {
                        OBPSolvePositionDTO solvePositiondto = new OBPSolvePositionDTO();
                        solvePositiondto.setSolveId(solveProblem.getId());
                        solvePositiondto.setSolveKey(SolveResoult.GOODS_LOSS.toString());
                        solvePositiondto.setItemNo(dto.getItemNo());
                        solvePositiondto.setState(OBPSolveState.unsolved.toString());
                        solvePositiondto.setShipmentNo(dto.getShipmentNo());
                        obpSolvePositionRepository.save(obpSolvePositionMapper.toEntity(solvePositiondto));
                    } else {
                        solvePosition.setModifiedDate(LocalDateTime.now());
                        solvePosition.setModifiedBy(SecurityUtils.getCurrentUsername());
                        solvePosition.setSolveKey(SolveResoult.GOODS_LOSS.toString());
                        obpSolvePositionRepository.save(solvePosition);
                    }
                    if(dto.getIfScaned().equals("true")){
                        obpSolve.setAmountScaned(obpSolve.getAmountScaned().subtract(amountConfirmProblem));
                        obpSolveRepository.save(obpSolve);
                        // solveProblem.setScaned(OBPSolveState.unscaned.toString());
                        solveProblem.setState(OBPSolveState.unsolved.toString());  //丢失将状态改为unsolved
                        solveProblem.setAmountScanedProblem(BigDecimal.ZERO);
                        obpSolveRepository.save(solveProblem);
                    }
                    //正常商品已扫描数不变
                    verifyProblemType(obpSolve,problem,problemType,dto.getShipmentNo());
                    if(obpSolveProblem.getObproblem()==null) obpSolveProblem=solveProblem;
                    boolean bl= obpSolveBusiness.updateScaned(obpSolve,obpSolveProblem);
                    if(bl&&!a) obpSolveBusiness.addRecord(obpSolve.getCustomerShipment().getShipmentNo(), obpSolve.getObpCell().getName());
                } else {
                    if (solvePosition != null) {
                        solvePosition.setModifiedDate(LocalDateTime.now());
                        solvePosition.setModifiedBy(SecurityUtils.getCurrentUsername());
                        // solvePosition.setSolveKey(null);
                        solvePosition.setSolveKey(dto.getSolveKey());
                        obpSolvePositionRepository.save(solvePosition);
                    }
                    if (problemType.equals(ProblemType.DAMAGED.toString())&& solveProblem.getAmountScanedProblem().compareTo(BigDecimal.ZERO) > 0) {
                        //确认残损需清空已扫描数,序列号无法扫描
                        solveProblem.setScaned(OBPSolveState.unscaned.toString());
                        solveProblem.setState(OBPSolveState.unsolved.toString());
                        solveProblem.setAmountScanedProblem(BigDecimal.ZERO);
                        obpSolve.setAmountScaned(obpSolve.getAmountScaned().subtract(amountConfirmProblem));
                        obpSolveRepository.save(solveProblem);
                        obpSolveRepository.save(obpSolve);
                    }
//                    if(problemType.equals(ProblemType.UNABLE_SCAN_SN.toString())||problemType.equals(ProblemType.DAMAGED.toString())){
//                        if(obpSolveProblem.getObproblem()==null) obpSolveProblem=solveProblem;
//                        obpSolveProblem.setScaned(OBPSolveState.scaned.toString());
//                        obpSolveRepository.save(solveProblem);
//                    }
                    if(problemType.equals(ProblemType.UNABLE_SCAN_SKU.toString())) {
                        //已扫描完成
                        if (dto.getIfScaned().equals("true")) {
                            obpSolve.setAmountScaned(obpSolve.getAmountScaned().subtract(amountConfirmProblem));
                            obpSolve.setScaned(OBPSolveState.unscaned.toString());
                        }
                        //未扫描完成，已扫描数不变
                        solveProblem.setScaned(OBPSolveState.unscaned.toString());
                        solveProblem.setState(OBPSolveState.unsolved.toString());
                        solveProblem.setAmountScanedProblem(BigDecimal.ZERO);
                        obpSolveRepository.save(solveProblem);
                        obpSolveRepository.save(obpSolve);
                    }
                    verifyProblemType(obpSolve, problem, problemType, dto.getShipmentNo());
                }
            } else {
                //此流程主要涉及到处理问题时，将不会转换问题类型
                //更新问题处理表
                solveProblem.setState(state);
                solveProblem.setModifiedDate(LocalDateTime.now());

                //如果有传确认问题数量过来，将更新solve表问题数(等于0表示没有传确认问题数量)
                if (state.equals(OBPSolveState.unsolved.toString()) && dto.getAmountConfirmProblem().compareTo(BigDecimal.ZERO) > 0) {
                    if (dto.getProblemResources().equals(SolveResoult.PROBLEM.toString()) && !dto.getSolveKey().equals(SolveResoult.DAMAGED_TO_NORMAL.toString()))
                        solveProblem.setAmountProblem(dto.getAmountConfirmProblem());
                    else if (dto.getProblemResources().equals(SolveResoult.NORMAL.toString()))
                        solveProblem.setAmountProblem(solveProblem.getAmountProblem().add(dto.getAmountConfirmProblem()));
                    else if (dto.getProblemResources().equals(SolveResoult.PROBLEM.toString()) && dto.getSolveKey().equals(SolveResoult.DAMAGED_TO_NORMAL.toString())) {
                        solveProblem.setAmountProblem(solveProblem.getAmountProblem().subtract(dto.getAmountConfirmProblem()));
                        //残品转正品数量和原来的问题数相等，可将状态改为solved
                        if(solveProblem.getAmountProblem().compareTo(BigDecimal.ZERO)==0){
                            solveProblem.setState(OBPSolveState.solved.toString());
                        }
                    }
                    solveProblem.getObproblem().setAmount(solveProblem.getAmountProblem());
                }
                //生成HOT_PICK 分配货位
                if(dto.getSolveKey().equals(SolveResoult.HAS_HOT_PICK.toString())||dto.getSolveKey().equals(SolveResoult.ASSIGNED_LOCATION.toString())){
                    solveProblem.setAdditionalContent(dto.getSolveKey());
                }
                if (state.equals(OBPSolveState.solved.toString())) {
                    solveProblem.setAmount(solveProblem.getAmount().add(BigDecimal.ONE));
                    solveProblem.setSolveBy(SecurityUtils.getCurrentUsername());
                    solveProblem.setSolveDate(LocalDateTime.now());
                } else {
                    solveProblem.setAmountScanedProblem(BigDecimal.ZERO);
                    if (dto.getSolveKey() != null && dto.getSolveKey().equals(SolveResoult.DAMAGED_TO_NORMAL.toString())) {
                        if (dto.getAmountConfirmProblem().compareTo(BigDecimal.ZERO) == 0) {
                            solveProblem.setAmountProblem(solveProblem.getAmountProblem().subtract(BigDecimal.ONE));
                            solveProblem.getObproblem().setAmount(solveProblem.getAmountProblem());
                            //把单件的残损转为正品，将状态改为solved
                            solveProblem.setState(OBPSolveState.solved.toString());
                        }
                        solveProblem.setAmountScanedProblem(solveProblem.getAmountProblem());
                    }
                    solveProblem.setScaned(OBPSolveState.unscaned.toString());

                    if (obpSolve.getScaned().equals(OBPSolveState.scaned.toString())
                            && !dto.getSolveKey().equals(SolveResoult.HAS_HOT_PICK.toString())
                            && !dto.getSolveKey().equals(SolveResoult.ASSIGNED_LOCATION.toString())
                            && !dto.getSolveKey().equals(SolveResoult.PRINT_SKU_REPAIR.toString())
                            && !dto.getSolveKey().equals(SolveResoult.DAMAGED_TO_NORMAL.toString())) {
                        if (dto.getAmountConfirmProblem().compareTo(BigDecimal.ZERO) > 0)
                            obpSolve.setAmountScaned(obpSolve.getAmountScaned().subtract(dto.getAmountConfirmProblem()));
                        else if (dto.getAmountConfirmProblem().compareTo(BigDecimal.ZERO) == 0)
                            obpSolve.setAmountScaned(obpSolve.getAmountScaned().subtract(BigDecimal.ONE));
                    }
                    obpSolveRepository.save(obpSolve);
                }
                obpSolveRepository.save(solveProblem);
            }
        }
        //问题处理详情表中添加处理记录
        String[] solveKeyDel = {SolveResoult.DELETE_ORDER_CUSTOMER.toString(),SolveResoult.DELETE_ORDER_FORCE.toString(),SolveResoult.OUT_OF_STOCK_DELETE_ORDER.toString()};
        if(Arrays.asList(solveKeyDel).contains(dto.getSolveKey())){
            solvePosition = outBoundProblemCommonBusiness.checkAndSaveSolvePosition(solveProblem, dto.getSolveKey(), dto.getLocationContainer());
        } else {
            //如果solveProblem是正常的那条数据
            if(solveProblem.getObproblem()==null)
                solveProblem=solveNormal;
            solvePosition = obpSolvePositionRepository.getSolvePositionByShipmentNoAndSolve(applicationContext.getCurrentWarehouse(),dto.getShipmentNo(),solveProblem.getId());
            if (solvePosition != null) {
                obpSolvePositionMapper.updateEntityFromDTO(dto,solvePosition);
                if(dto.getSolveKey().equals(SolveResoult.ASSIGNED_LOCATION.toString())){
                    buildObpLocation(dto,solvePosition,obpSolve);
                }
                obpSolvePositionRepository.saveAndFlush(solvePosition);
            } else {
                dto.setSolveId(solveProblem.getId());
                dto.setState(state);
                solvePosition = obpSolvePositionRepository.save(obpSolvePositionMapper.toEntity(dto));
            }
        }
        return solvePosition;
    }

    public void buildObpLocation(OBPSolvePositionDTO dto,OBPSolvePosition obpSolvePosition,OBPSolve obpSolve){
            String locations[]=dto.getLocation().split("/");
            String descriptions[]=dto.getDescription().split("/");
            for(int i=0;i<locations.length;i++){
                 OBPLocation obpLocation=new OBPLocation();
                 obpLocation.setAmount(Integer.parseInt(descriptions[i]));
                 obpLocation.setAmountScaned(0);
                 obpLocation.setLocation(locations[i]);
                 obpLocation.setSolvePositionId(obpSolvePosition.getId());
                 obpLocation.setSolveId(obpSolvePosition.getObpSolve().getId());
                 obpLocation.setState("unsolved");
                 obpLocation.setWarehouseId(applicationContext.getCurrentWarehouse());
                 obpLocation.setItemNo(dto.getItemNo());
                 obpLocation.setShipmentNo(dto.getShipmentNo());
                 obpLocation.setCellName(obpSolve.getObpCell().getName());
                 obpLocation.setCallPod(true);
                 obpLocation.setItemName(obpSolve.getItemData().getName());
                 locationRepository.save(obpLocation);
                 StorageLocation storageLocation= storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(),obpLocation.getLocation());
                 //增加库存reserved数量
                 List<StockUnit> stockUnits=stockUnitRepository.getByItemDataNo(storageLocation,obpLocation.getItemNo());
                 for(StockUnit stockUnit:stockUnits){
                      if(stockUnit.getAmount().compareTo(stockUnit.getReservedAmount().add(new BigDecimal(obpLocation.getAmount())))>=0) {
                          stockUnit.setReservedAmount(stockUnit.getReservedAmount().add(new BigDecimal(obpLocation.getAmount())));
                          stockUnitRepository.save(stockUnit);
                          break;
                      }
                 }
            }
    }

    //生成Hot Pick
    public void generateHotPick(OBPSolvePositionDTO obpSolvePositionDTO) {
        solveOBProblem(obpSolvePositionDTO);
    }

    public List<Map> searchStorageLocationByItemNo(String itemDateNo,String sectionId) {
        List<Map> maps = solveDamageQuery.searchStorageLocationByItemNo(itemDateNo,sectionId);
        if (maps == null)
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        return maps;

    }

    public void putDamagedToContainer(String containerName, String shipmentNo, String itemNo, LocalDate useNotAfter, String serialNo) {
        StorageLocation toStorageLocation = outBoundProblemCommonBusiness.checkProcessContainer(containerName, InventoryState.DAMAGE.toString());

        OBPSolve solve = obpSolveRepository.getGoodsByShipmentAndItem(applicationContext.getCurrentWarehouse(), shipmentNo, itemNo);
        // 容器中不能存在不同客户的相同商品
        outBoundProblemCommonBusiness.checkItemClient(toStorageLocation, solve.getItemData());
        String shipmentId = customerShipmentRepository.getByShipmentNo(shipmentNo).getId();
        OBProblem obProblem = obProblemRepository.getDamagedByItem(applicationContext.getCurrentWarehouse(), shipmentId, itemNo);
        UnitLoad unitLoad = unitLoadRepository.getByShipmentNo(shipmentNo);
        if (unitLoad == null)
            throw new ApiException(OutBoundProblemException.EX_SHIPMENT_HAS_NOT_PICK_UNITLOAD.getName());

        StorageLocation fromStorageLocation = unitLoad.getStorageLocation();
        if (fromStorageLocation == null)
            throw new ApiException(OutBoundProblemException.EX_SHIPMENT_HAS_NOT_PICK_UNITLOAD.getName());
        //不需要判断扫描SN,因为前面流程已判断过
        //判断商品是否需要输入有效期，若需要将添加相关有效期记
        Lot lot=null;
        if (solve.getItemData().isLotMandatory()) {
            lot=outBoundProblemCommonBusiness.checkItemLot(fromStorageLocation, toStorageLocation, solve.getItemData(), useNotAfter, obProblem.getAmount(), InventoryState.DAMAGE.toString());
        }
        //生成移货历史记录，更改库存记录
        UnitLoad toUnitLoad = unitLoadBusiness.getByStorageLocation(toStorageLocation);
        if (toUnitLoad == null) {
            toUnitLoad = buildBusiness.buildUnitLoad(toStorageLocation);
        }
        if(serialNo!=null&&!serialNo.isEmpty()) {
            moveGoodsBusiness.movingGoodsToContainer(unitLoad, toUnitLoad, solve.getItemData(), obProblem.getAmount().subtract(obProblem.getSolveAmount()), serialNo,lot);
        }else{
            moveGoodsBusiness.movingGoods(unitLoad, toUnitLoad,toStorageLocation,solve.getItemData(), obProblem.getAmount().subtract(obProblem.getSolveAmount()),null,lot);
        }
        obProblem.setSolveAmount(obProblem.getAmount());
        obProblem.setContainer(containerName);
        obProblemRepository.save(obProblem);
        OBPSolve obpSolveProblem=obpSolveRepository.getByProblemId(obProblem.getId());
        obpSolveProblem.setScaned(OBPSolveState.scaned.toString());
        obpSolveRepository.save(obpSolveProblem);
    }

    private String getStateBySolveKey(String solveKey) {
        String state = OBPSolveState.unsolved.toString();
        if (solveKey.contentEquals(SolveResoult.CLEARANCE_CELL.toString())) {
            state = OBPSolveState.solved.toString();
        }
        return state;
    }


    //更新正常商品栏位的信息
    private void updateSolve(OBPSolve obpSolve,List<OBPSolve> solveProblems,BigDecimal amountConfirmProblem) {
        obpSolve.setAmountScanedProblem(BigDecimal.ZERO);
        //当订单中有多种类型的问题时，设置该类问题商品中正常商品的已扫描数(已扫描数，即订单下该类商品数 - 该类问题商品中所有种类问题的总数)
        if (obpSolve.getScaned().equals(OBPSolveState.scaned.toString())) {
            BigDecimal amountScaned = BigDecimal.ZERO;
            for (OBPSolve solve : solveProblems) {
                amountScaned = solve.getAmountProblem().add(amountScaned);
            }
            if (amountConfirmProblem.compareTo(BigDecimal.ZERO) > 0)
                obpSolve.setAmountScaned(obpSolve.getAmountShipment().subtract(amountConfirmProblem).subtract(amountScaned));
            else
                obpSolve.setAmountScaned(obpSolve.getAmountShipment().subtract(BigDecimal.ONE).subtract(amountScaned));
        }
    }
    //在所有问题处理完成后再报丢失或者无法扫描，正常处理是没有的
    //正常转残损(库存不变) 正常转丢失( -1) 正常转无法扫描(库存清0)
    private boolean verifySolved(OBPSolve obpSolve,BigDecimal amount,String type) {
        boolean flag=false;
        if(amount.compareTo(BigDecimal.ZERO)==0) amount=BigDecimal.ONE;
        List<StockUnit> stockUnits = stockUnitRepository.getByNameAndItem(obpSolve.getObpCell().getName(), obpSolve.getItemData().getItemNo());
        if (!stockUnits.isEmpty()&&type.equals(ProblemType.LOSE.toString())) {
            flag=true;
            //库存减去问题数
            StockUnit stockUnit=stockUnits.get(0);
            stockUnit.setAmount(stockUnit.getAmount().subtract(amount));
            stockUnitRepository.save(stockUnit);
            //库存历史记录减去问题数
            StockUnitRecord stockUnitRecord=stockUnitRecordRepository.getByStockUnitId(stockUnit.getId(),obpSolve.getItemData().getItemNo());
            stockUnitRecord.setAmount(stockUnitRecord.getAmount().subtract(amount));
            stockUnitRecord.setAmountStock(stockUnitRecord.getAmountStock().subtract(amount));
            stockUnitRecordRepository.save(stockUnitRecord);
        }else if(!stockUnits.isEmpty()&&type.equals(ProblemType.UNABLE_SCAN_SKU.toString())){
            StockUnit stockUnit=stockUnits.get(0);
            stockUnit.setAmount(BigDecimal.ZERO);
            stockUnitRepository.save(stockUnit);
            StockUnitRecord stockUnitRecord=stockUnitRecordRepository.getByStockUnitId(stockUnit.getId(),obpSolve.getItemData().getItemNo());
            stockUnitRecord.setAmount(BigDecimal.ZERO);
            stockUnitRecord.setAmountStock(BigDecimal.ZERO);
            stockUnitRecordRepository.save(stockUnitRecord);
        }else if(stockUnits.isEmpty()&&type.equals(ProblemType.LOSE.toString())){
            //报丢失将原库存盘亏
            UnitLoad unitLoad= unitLoadRepository.getByShipmentNo(obpSolve.getCustomerShipment().getShipmentNo());
            if(unitLoad==null)
                throw new ApiException(OutBoundProblemException.EX_SHIPMENT_HAS_NOT_PICK_UNITLOAD.getName());
            ItemData itemData=obpSolve.getItemData();
            BigDecimal sourceAmount = generalBusiness.getStockUnitAmount(unitLoad, itemData);
            if (sourceAmount.compareTo(amount) == -1)
                throw new ApiException(OutBoundProblemException.EX_IT_AMOUNT_MORE_THAN_SYSTEM_AMOUNT.getName());
            List<StockUnit> stockUnitList=stockUnitRepository.getByUnitLoadAndItemData(unitLoad,itemData);
            for(StockUnit stockUnit:stockUnitList){
                BigDecimal stockAmount=stockUnit.getAmount();
                if(stockAmount.compareTo(amount)>=0) {
                    //盘亏
                    stockUnit.setAmount(stockAmount.subtract(amount));
                    break;
                } else {
                    amount =amount.subtract(stockAmount);
                    stockUnit.setAmount(BigDecimal.ZERO);
                }
            }
            stockUnitRepository.save(stockUnitList);
        }
        return flag;
    }
    private void verifySerialNo(OBPSolvePositionDTO dto) {
        //判断序列号是否是正确的
        OBPShipmentSerialNo obpShipmentSerialNo=Optional.ofNullable(obpShipmentSerialNoRepository.getSerialNo(applicationContext.getCurrentWarehouse(),dto.getSerialNo(),dto.getShipmentNo())).
                orElseThrow(()->new ApiException(OutBoundProblemException.EX_SN_NOT_FOUND.getName()));
        if(dto.getSolveKey().equals(SolveResoult.CONFIRM_DAMAGED.toString())){
            obpShipmentSerialNo.setState(StockUnitState.DAMAGE.getName());
        }else if(dto.getSolveKey().equals(SolveResoult.DAMAGED_TO_NORMAL.toString())){
            obpShipmentSerialNo.setState(StockUnitState.INVENTORY.getName());
        }
        obpShipmentSerialNoRepository.save(obpShipmentSerialNo);
   }

    private void verifyProblemType(OBPSolve obpSolve,OBProblem problem,String problemType,String shipmentNo){
        List<OBProblem> obProblems=obProblemRepository.getByShipmentAndType(applicationContext.getCurrentWarehouse(),obpSolve.getCustomerShipment().getId(),obpSolve.getItemData().getId(),problemType);
        //此方法在正常流程不存在，主要解决问题商品再转其它问题，并且此问题已经存在
        if(obProblems.size()>1){
            for(OBProblem obProblem1:obProblems){
                if(!obProblem1.getId().equals(problem.getId())){
                    OBPSolve obpSolve1=obpSolveRepository.getByProblemId(obProblem1.getId());
                    if(obpSolve1.getAdditionalContent()!=null)
                        throw new ApiException(OutBoundProblemException.EX_OBPPROBLEM_TYPE_ERROR.getName());
                    OBPSolve obpSolve2=obpSolveRepository.getByProblemId(problem.getId());
                    OBPSolvePosition obpSolvePosition=obpSolvePositionRepository.getByShipmentNo(applicationContext.getCurrentWarehouse(),obpSolve1.getId(),shipmentNo);
                    if(obpSolvePosition!=null)
                        obpSolvePositionRepository.delete(obpSolvePosition);
                    problem.setAmount(problem.getAmount().add(obProblem1.getAmount()));
                    problem.setSolveAmount(problem.getSolveAmount().add(obProblem1.getSolveAmount()));
                    obpSolve2.setAmountProblem(problem.getAmount());
                    obpSolve2.setAmountScanedProblem(obpSolve2.getAmountScanedProblem().add(obpSolve1.getAmountScanedProblem()));
                    if(problemType.equals(ProblemType.DAMAGED.toString()))
                        obpSolve2.setAmountScanedProblem(BigDecimal.ZERO);
                    obpSolveRepository.save(obpSolve2);
                    obProblemRepository.save(problem);
                    obProblemRepository.delete(obProblem1);
                    obpSolveRepository.delete(obpSolve1);
                }
            }
        }
    }

    private BigDecimal getAmountConfirmProblem(OBPSolvePositionDTO dto){
        BigDecimal amount=BigDecimal.ONE;
        if(dto.getAmountConfirmProblem().compareTo(BigDecimal.ZERO)>0){
            amount=dto.getAmountConfirmProblem();
        }
        return amount;
    }
}
