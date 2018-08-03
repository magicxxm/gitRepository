package com.mushiny.wms.outboundproblem.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.outboundproblem.business.MoveGoodsBusiness;
import com.mushiny.wms.outboundproblem.business.OBProblemBusiness;
import com.mushiny.wms.outboundproblem.business.utils.ShipmentState;
import com.mushiny.wms.outboundproblem.crud.common.dto.InboundProblemRuleDTO;
import com.mushiny.wms.outboundproblem.crud.common.dto.MoveGoodsDTO;
import com.mushiny.wms.outboundproblem.crud.common.mapper.InboundProblemRuleMapper;
import com.mushiny.wms.outboundproblem.crud.dto.*;
import com.mushiny.wms.outboundproblem.crud.mapper.OBPStationMapper;
import com.mushiny.wms.outboundproblem.crud.mapper.OBProblemCheckMapper;
import com.mushiny.wms.outboundproblem.crud.mapper.OBProblemMapper;
import com.mushiny.wms.outboundproblem.domain.*;
import com.mushiny.wms.outboundproblem.domain.common.*;
import com.mushiny.wms.outboundproblem.domain.enums.OBPSolveState;
import com.mushiny.wms.outboundproblem.domain.enums.ProblemType;
import com.mushiny.wms.outboundproblem.query.OBPSolveQuery;
import com.mushiny.wms.outboundproblem.repository.*;
import com.mushiny.wms.outboundproblem.exception.OutBoundProblemException;
import com.mushiny.wms.outboundproblem.query.hql.OBProblemQuery;
import com.mushiny.wms.outboundproblem.repository.common.CustomerShipmentRepository;
import com.mushiny.wms.outboundproblem.repository.common.*;
import com.mushiny.wms.outboundproblem.service.OBProblemService;
import com.mushiny.wms.outboundproblem.crud.common.dto.ItemDataGlobalDTO;
import com.mushiny.wms.outboundproblem.crud.common.mapper.ItemDataGlobalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OBProblemServiceImpl implements OBProblemService {

    private final OBProblemBusiness obProblemBusiness;
    private final InboundProblemRuleRepository inboundProblemRuleRepository;
    private final InboundProblemRuleMapper inboundProblemRuleMapper;
    private final OBProblemRepository obProblemRepository;
    private final ItemDataGlobalRepository itemDataGlobalRepository;
    private final OBPSolveCheckRepository obpSolveCheckRepository;
    private final OBProblemMapper obProblemMapper;
    private final CustomerShipmentRepository customerShipmentRepository;
    private final OBPSolveRepository obpSolveRepository;
    private final OBProblemQuery obProblemQuery;
    private final ItemDataGlobalMapper itemDataGlobalMapper;
    private final StorageLocationRepository storageLocationRepository;
    private final ItemDataRepository itemDataRepository;
    private final MoveGoodsBusiness moveGoodsBusiness;
    private final ApplicationContext applicationContext;
    private final OBPSolveQuery obpSolveQuery;
    private final OBPStationRepository obpStationRepository;
    private final WorkStationRepository workStationRepository;
    private final OBPStationMapper obpStationMapper;
    private final UserRepository userRepository;
    private final OBProblemCheckMapper obProblemCheckMapper;
    private final OBProblemCheckRepository obProblemCheckRepository;
    private final OBPCheckStateRepository obPCheckStateRepository;
//    private final OBPWallRepository obpWallRepository;

    @Autowired
    public OBProblemServiceImpl(OBProblemRepository obProblemRepository,
                                OBProblemMapper obProblemMapper,
                                ApplicationContext applicationContext,
                                CustomerShipmentRepository customerShipmentRepository,
                                OBPSolveRepository obpSolveRepository,
                                OBProblemBusiness obProblemBusiness,
                                InboundProblemRuleRepository inboundProblemRuleRepository,
                                InboundProblemRuleMapper inboundProblemRuleMapper,
                                ItemDataGlobalRepository itemDataGlobalRepository,
                                OBPSolveCheckRepository obpSolveCheckRepository,
                                ItemDataGlobalMapper itemDataGlobalMapper,
                                OBProblemQuery obProblemQuery,
                                StorageLocationRepository storageLocationRepository,
                                ItemDataRepository itemDataRepository,
                                MoveGoodsBusiness moveGoodsBusiness,
                                OBPSolveQuery obpSolveQuery,
                                OBPStationRepository obpStationRepository,
                                WorkStationRepository workStationRepository,
                                OBPStationMapper obpStationMapper,
                                UserRepository userRepository,
                                OBProblemCheckMapper obProblemCheckMapper,
                                OBProblemCheckRepository obProblemCheckRepository,
                                OBPCheckStateRepository obPCheckStateRepository) {
        this.obProblemBusiness = obProblemBusiness;
        this.inboundProblemRuleRepository = inboundProblemRuleRepository;
        this.inboundProblemRuleMapper = inboundProblemRuleMapper;
        this.obProblemRepository = obProblemRepository;
        this.itemDataGlobalRepository = itemDataGlobalRepository;
        this.obpSolveCheckRepository = obpSolveCheckRepository;
        this.obpSolveRepository = obpSolveRepository;
        this.obProblemMapper = obProblemMapper;
        this.applicationContext = applicationContext;
        this.customerShipmentRepository = customerShipmentRepository;
        this.itemDataGlobalMapper = itemDataGlobalMapper;
        this.obProblemQuery= obProblemQuery;
        this.storageLocationRepository = storageLocationRepository;
        this.itemDataRepository = itemDataRepository;
        this.moveGoodsBusiness = moveGoodsBusiness;
        this.obpSolveQuery = obpSolveQuery;
        this.obpStationRepository  = obpStationRepository;
        this.workStationRepository = workStationRepository;
//        this.obpStationRepository = obpStationRepository;
//        this.obpWallRepository = obpWallRepository;
        this.obpStationMapper = obpStationMapper;
        this.userRepository = userRepository;
        this.obProblemCheckMapper = obProblemCheckMapper;
        this.obProblemCheckRepository = obProblemCheckRepository;
        this.obPCheckStateRepository = obPCheckStateRepository;
    }

    @Override
    public OBProblemCheckDTO create(OBProblemCheckDTO dto) {
        //sku多货时 ,判断 是否有打码
        if (dto.getProblemType().equalsIgnoreCase("MORE")) {
            if (dto.getItemNo() != null && dto.getSkuNo() != null) {
                checkSkuNoOrItemNo(dto.getSkuNo(), dto.getItemNo());
            } else {
                throw new ApiException(OutBoundProblemException.Outbound_Problem_ItemNo_And_SkuNo_NotNoll.toString());
            }
        }

        OBProblemCheck entity = obProblemCheckMapper.toEntity(dto);
        return obProblemCheckMapper.toDTO(obProblemCheckRepository.save(entity));
    }

    @Override
    /*批量报少货*/
    public BigDecimal createLess(String storageLocationId) {
        List<Object[]> list = obProblemRepository.getStockUnitByStorageLocationId(storageLocationId);
        if (list == null || list.isEmpty()) {
            return BigDecimal.ZERO;
        }
        List<OBProblem> obProblems = new ArrayList<>();
        BigDecimal sumAmount = BigDecimal.ZERO;
        for (Object row : list) {
            OBProblem obProblem = new OBProblem();
            Object[] arr = (Object[]) row;
            obProblem.setAmount((BigDecimal) arr[0]);
            obProblem.setProblemStoragelocation((String) arr[1]);
            if (arr[2] != null) {
                obProblem.setLotNo((String) arr[2]);
            }
            obProblem.setItemDataId((String) arr[3]);
            if (arr[4] != null) {
                obProblem.setItemNo((String) arr[4]);
            }
            obProblem.setWarehouseId((String) arr[5]);

            sumAmount = sumAmount.add(obProblem.getAmount());

            obProblem.setSolveAmount(BigDecimal.ZERO);
            obProblem.setJobType("STOW");
            obProblem.setState("close");
            obProblem.setReportBy(obProblemRepository.getByUserId(applicationContext.getCurrentUser()));
            obProblem.setReportDate(LocalDateTime.now());
            obProblem.setProblemType("LESS");
            obProblems.add(obProblem);
        }
        obProblemRepository.save(obProblems);
        return sumAmount;
    }

    @Override
    public void delete(String id) {
        OBProblem entity = obProblemRepository.retrieve(id);
        obProblemRepository.delete(entity);
    }

    @Override
     /* 异常处理完（state：CLOSE）并判断是否解绑小车  */
    public OBProblemCheckDTO update(OBProblemCheckDTO dto) {
        OBProblemCheck entity = obProblemCheckRepository.retrieve(dto.getId());
        obProblemCheckMapper.updateEntityFromDTO(dto, entity);
        entity = obProblemBusiness.update(entity);
        return obProblemCheckMapper.toDTO(entity);
    }

    //批量修改State "CLOSE"
    @Override
    public List<OBProblemCheckDTO> updateClose(List<String> ids,String name) {
        List<OBProblemCheck> entities = new ArrayList<>();
        //批量将问题state设置 CLOSE
        for (String id : ids) {
            OBProblemCheck entity = obProblemCheckRepository.retrieve(id);
            entity.setState("CLOSE");
            entities.add(entity);
        }
        obProblemBusiness.update(entities);
        //将处理的问题，存入处理问题记录表
        for (OBProblemCheck obProblem : entities) {
            OBPCheckState obpSolve = new OBPCheckState();
            obpSolve.setObproblem(obProblem);
            obpSolve.setState("CLOSE");
            obpSolve.setItemData(itemDataRepository.retrieve(obProblem.getItemData().getId()));
            obpSolve.setAmount(obProblem.getAmount());
            obpSolve.setAmountProblem(obProblem.getAmount());
            obpSolve.setInboundProblemRule(inboundProblemRuleRepository.getByName(name));
            obpSolve.setWarehouseId(obProblem.getWarehouseId());
            if (obpSolve.getItemData() != null) {
                obpSolve.setClientId(obpSolve.getItemData().getClientId());
            }
//            obpSolve.setClientId(itemDataRepository.retrieve(obProblem.getItemDataId()).getClientId());
            obpSolve.setStorageLocation(obProblemRepository.
            getByStorageLocationName(obProblem.getProblemStoragelocation()));
//            obpSolve.setObpStation(obpStationRepository.getByName(applicationContext.getCurrentWarehouse(),obpStationName));
//            obpSolve.setObpWall(obpWallRepository.getByName(applicationContext.getCurrentWarehouse(),obpWallName));
//            obpSolve.setCustomerShipment(customerShipmentRepository.retrieve(obProblem.getShipmentId()));
            obPCheckStateRepository.save(obpSolve);
        }
        return obProblemCheckMapper.toDTOList(entities);
    }

    @Override
    public List<OBProblemDTO> getByContainer(String container) {
        List<OBProblem> entities = obProblemCheckRepository.getByContainer(container);
        return obProblemMapper.toDTOList(entities);
    }

    @Override
    /* 获取商品信息 */
    public ItemDataGlobalDTO getByItem(String obproblemId) {
        OBProblemCheck obProblemCheck = obProblemCheckRepository.retrieve(obproblemId);
        String itemNo;
        if (obProblemCheck.getProblemType().equalsIgnoreCase("MORE")) {
            if (obProblemCheck.getItemData() != null ){
                itemNo =obProblemCheck.getItemData().getItemNo();
            } else {
                itemNo = obProblemCheck.getItemNo();
            }
        } else {
            if (obProblemCheck.getItemData() != null ){
                itemNo =obProblemCheck.getItemData().getItemNo();
            }else{
                itemNo = obProblemCheck.getItemNo();
            }
        }
        return itemDataGlobalMapper.toDTO(itemDataGlobalRepository.getByItemDataNo(itemNo));
    }

    //rebin 车记录
    @Override
    public List<OBProblemDataDTO> findLocationByItem(String problemStoragelocation,String itemNo,String jobType) {
        List<OBProblemDataDTO> entities = new ArrayList<OBProblemDataDTO>();
        List<Object[]> list = obProblemQuery.getByObproblem(problemStoragelocation,itemNo,jobType);
        for (Object row : list) {
            Object[] arr = (Object[]) row;
            OBProblemDataDTO dto = new OBProblemDataDTO();
            if (arr[0] != null) {
                dto.setItemNo((String) arr[0]);
            }
            if (arr[1] != null) {
                dto.setItemSku((String) arr[1]);
            }
            dto.setWall((String) arr[2]);
            dto.setCell((String) arr[3]);
            if (arr[4] != null) {
                dto.setAmount((BigDecimal) arr[4]);
            }
            if (arr[5] != null) {
                dto.setRebinAmount((BigDecimal) arr[5]);
            }

//            //未处理标记为'H',已处处理标记已处理状态 “NG"与"OK"
//            if (!storageLocationIds.contains((String) arr[0])) {
//                dto.setUnexamined("H");
//            } else {
//                String storageLocationId = (String) arr[0];
//                if (storageLocationId != null) {
//                    String state = obpSolveCheckRepository.getByState(obproblemCheck.getId(), storageLocationId);
//                    dto.setUnexamined(state);
//                }
//            }
            entities.add(dto);
        }
        return entities;
    }

    @Override
    public void findOverageGoods(String storagelocation, String itemdataSku, BigDecimal amount ,String fromName,String jobType) {
        StorageLocation storageLocation = storageLocationRepository.retrieve(storagelocation);
        List<OBProblemCheck> obproblemCheck = obProblemCheckRepository.getByStorageLocationName(fromName, itemdataSku);
        ItemData itemData;
        if(obproblemCheck.isEmpty()) {
            throw new ApiException(OutBoundProblemException.Outbound_Problem_Not_Found.toString());
        }
        if (obproblemCheck.get(0).getItemData()!=null){
             itemData = itemDataRepository.retrieve(obproblemCheck.get(0).getItemData().getId());
        }else {
            itemData = obProblemCheckRepository.getByItemData(itemdataSku);
        }

        obProblemBusiness.stowOverageGoods(storageLocation, itemData, amount,jobType);
    }

    @Override
    public void findlossGoods(String fromName, String toName, String itemdataSku, BigDecimal amount,String jobType) {
        StorageLocation from = storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(), fromName);
        StorageLocation to = storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(), toName);
        ItemData itemData;
        List<OBProblemCheck> obproblemCheck = obProblemCheckRepository.getByStorageLocationName(from.getName(), itemdataSku);
        if(obproblemCheck.isEmpty()) {
            throw new ApiException(OutBoundProblemException.Outbound_Problem_Not_Found.toString());
        }
        if (obproblemCheck.get(0).getItemData()!=null){
            itemData = itemDataRepository.retrieve(obproblemCheck.get(0).getItemData().getId());
        }else {
            itemData = obProblemCheckRepository.getByItemData(itemdataSku);
        }
        obProblemBusiness.stowLossGoods(from, to, itemData, amount,jobType);
    }

    @Override
    public OBProblemCheckDTO retrieve(String id) {
        return obProblemCheckMapper.toDTO(obProblemCheckRepository.retrieve(id));
    }

    @Override
    public List<Map> getAnalysis(List<String> ids) {
        if (ids.isEmpty()) {
            return null;
        }
        List<Map> recondAnalysis = new ArrayList<Map>();
        for (String i : ids) {
            OBProblemCheck entity = obProblemCheckRepository.retrieve(i);
            String jobType = entity.getJobType();
            System.out.println(jobType);
            if (entity.getJobType().equalsIgnoreCase("Rebin")) {
                if (entity.getItemNo() != null && !entity.getItemNo().equals("")) {
                    recondAnalysis.addAll(obProblemQuery.
                            getByRecordRebin(entity.getProblemStoragelocation(), entity.getItemNo()));
                } else {
                    recondAnalysis.addAll(obProblemQuery.
                            getByRecordRebin(entity.getProblemStoragelocation(), entity.getItemData().getItemNo()));
                }
            }else if(entity.getJobType().equalsIgnoreCase("Pack")){
                if (entity.getItemNo() != null && !entity.getItemNo().equals("")) {
                    recondAnalysis.addAll(obProblemQuery.
                            getByRecordPack(entity.getProblemStoragelocation(), entity.getItemNo()));
                } else {
                    recondAnalysis.addAll(obProblemQuery.
                            getByRecordPack(entity.getProblemStoragelocation(), entity.getItemData().getItemNo()));
                }

            }
        }
        return recondAnalysis;
    }

    @Override
    public void moving(MoveGoodsDTO moveGoodsDTO) {
        StorageLocation source = storageLocationRepository.retrieve(moveGoodsDTO.getSourceId());
        StorageLocation destination = storageLocationRepository.retrieve(moveGoodsDTO.getDestinationId());
        ItemData itemData = itemDataRepository.retrieve(moveGoodsDTO.getItemDataId());
        moveGoodsBusiness.movingProblem(source, destination, itemData, moveGoodsDTO.getAmount());
    }

    @Override
    public BigDecimal sumByProblemStorageLocationAndOpen(String problemStorageLocation) {
        return obProblemRepository.sumByProblemStorageLocationAndOpen(problemStorageLocation);
    }

    @Override
    //获取容器的上货历史记录
    public List<StowStockunitRecordDTO> sumStockunitRecordByProblem(String inboundProblemId,String jobType) {
        OBProblemCheck obproblemCheck = obProblemCheckRepository.retrieve(inboundProblemId);
        String itemNo;
        //多货或少货
        if (obproblemCheck.getProblemType().equalsIgnoreCase("MORE")) {
            itemNo = obproblemCheck.getItemNo();
            //SKU多货
            if (itemNo == null) {
                itemNo = obProblemRepository.getBySkuNo(obproblemCheck.getSkuNo());
            }

        } else {
            if (obproblemCheck.getItemData() != null ){
                itemNo = obproblemCheck.getItemData().getItemNo();
            } else {
                itemNo = obproblemCheck.getItemNo();
            }

        }
        List<StowStockunitRecordDTO> entities = new ArrayList<StowStockunitRecordDTO>();

        List<Object[]> list = obProblemQuery.sumStockunitRecordByPoblem(itemNo,
                obproblemCheck.getProblemStoragelocation(),jobType);

        List<String> storageLocationIds = obpSolveCheckRepository.getByStorageLocationId(obproblemCheck.getId());

        for (Object row : list) {
            Object[] arr = (Object[]) row;
            StowStockunitRecordDTO dto = new StowStockunitRecordDTO();
            dto.setStorageLocationId((String) arr[0]);
            dto.setName((String) arr[1]);
            if (arr[2] != null) {
                dto.setAmount((BigDecimal) arr[2]);
            }
            if (arr[3] != null) {
                dto.setActualAmount((BigDecimal) arr[3]);
            }
            dto.setTotalAmount((BigDecimal) arr[4]);
            dto.setClientId((String) arr[5]);
            dto.setClientName((String) arr[6]);
            dto.setLotId((String) arr[7]);
            if (arr[8] != null) {
                dto.setItemDataId((String) arr[8]);
            } else if (obproblemCheck.getItemData() != null) {
                dto.setItemDataId(obproblemCheck.getItemData().getId());
            } else {
                dto.setItemDataId(obProblemRepository.getByItemData(obproblemCheck.getItemNo()).getId());
            }
            //未处理标记为'H',已处处理标记已处理状态 “NG"与"OK"
            if (!storageLocationIds.contains((String) arr[0])) {
                dto.setUnexamined("H");
            } else {
                String storageLocationId = (String) arr[0];
                if (storageLocationId != null) {
                    String state = obpSolveCheckRepository.getByState(obproblemCheck.getId(), storageLocationId);
                    dto.setUnexamined(state);
                }
            }
            entities.add(dto);
        }
        return entities;
    }

    @Override
    //全局搜索 根据状态，用户，搜索内容，开始时间或结束时间
    public List<OBProblemCheckDTO> getBySeek(String state, String userName, String seek,
                                             String startDate, String endDate) {
        List<OBProblemCheck> entities = obProblemQuery.getBySeek(state, userName, seek, startDate, endDate);
        return obProblemCheckMapper.toDTOList(entities);
    }

    @Override
    public List<OBProblemCheckDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<OBProblemCheck> entities = obProblemCheckRepository.getBySearchTerm(searchTerm, sort);
        return obProblemCheckMapper.toDTOList(entities);
    }

    @Override
    public Page<OBProblemCheckDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        Page<OBProblemCheck> entities = obProblemCheckRepository.getBySearchTerm(searchTerm, pageable);
        return obProblemCheckMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<OBProblemCheckDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "reportBy"));
        List<OBProblemCheck> entities = obProblemCheckRepository.getNotLockList(null, sort);
        return obProblemCheckMapper.toDTOList(entities);
    }

    public void checkSkuNoOrItemNo(String skuNo, String itemNo) {

        List<ItemDataGlobal> itemDataGlobals = obProblemRepository.getByItemNoOrSkuNo(skuNo, itemNo);
        //没有此商品
        if (itemDataGlobals.isEmpty()) {
            throw new ApiException(OutBoundProblemException.Outbound_Problem_ItemNo_And_SkuNo_NotNoll.toString());
        }
        //商品未打码
        if (itemDataGlobals.size() != 1) {
            throw new ApiException(OutBoundProblemException.Outbound_Problem_ItemNo_Or_SkuNo_NotCode.toString());
        }

    }

    @Override
    public void overageGoods(OverageGoodsDTO overageGoodsDTO) {
        obProblemBusiness.overageGoods(overageGoodsDTO);
    }

    @Override
    public void lossGoods(LossGoodsDTO lossGoodsDTO) {
        obProblemBusiness.lossGoods(lossGoodsDTO);
    }

    @Override
    public void stowoverageGoods(String storagelocation, String itemdataId, BigDecimal amount,String jobType ) {
        StorageLocation storageLocation = storageLocationRepository.retrieve(storagelocation);
        ItemData itemData = itemDataRepository.retrieve(itemdataId);
        obProblemBusiness.stowOverageGoods(storageLocation, itemData, amount,jobType);
    }

    @Override
    public void stowlossGoods(String fromName, String toName, String itemdataId, BigDecimal amount,String jobType) {
        StorageLocation from = storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(), fromName);
        StorageLocation to = storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(), toName);
        ItemData itemData = itemDataRepository.retrieve(itemdataId);
        obProblemBusiness.stowLossGoods(from, to, itemData, amount,jobType);
    }

    @Override
    public InboundProblemRuleDTO getRule(String name) {
        InboundProblemRule entities = inboundProblemRuleRepository.getByName(name);
        return inboundProblemRuleMapper.toDTO(entities);
    }


    @Override
    public OBProblem generateOBProblem(OBProblemDTO obProblemDTO) {
        OBProblem problem=new OBProblem();

        //报多货问题时将会无法知道当前商品是属于哪个订单，所以需要过滤掉
        if (!obProblemDTO.getProblemType().equals(ProblemType.MORE.toString())) {
            List<OBProblem> problemList = obProblemRepository.getByShipmentAndItem(applicationContext.getCurrentWarehouse(),obProblemDTO.getShipmentId(),obProblemDTO.getItemDataId());
            List<java.lang.Object[]> lists = obpSolveQuery.queryShipment(customerShipmentRepository.retrieve(obProblemDTO.getShipmentId()).getShipmentNo());
            BigDecimal value = BigDecimal.ONE;
            if (lists.size() > 0) {
                for (int i = 0; i < lists.size(); i++) {
                    if (String.valueOf(lists.get(i)[1]).equals(obProblemDTO.getItemDataId())) {
                        value = new BigDecimal(String.valueOf(lists.get(i)[2]));
                        break;
                    }
                }
            }
            //如果该订单下当前商品只有一件，说明只能报一个问题
            if (problemList != null && !problemList.isEmpty() && value.compareTo(BigDecimal.ONE) == 0) {
                if(problemList.get(0).getState().equals(OBPSolveState.unsolved.toString()))
                   throw new ApiException(OutBoundProblemException.EX_ITEM_HAS_REPORTED_PROBLEMS.toString(),obProblemDTO.getItemNo());
            }

            //更新订单状态
            CustomerShipment customerShipment = customerShipmentRepository.retrieve(obProblemDTO.getShipmentId());
            if (customerShipment != null) {
                customerShipment.setState(ShipmentState.PROBLEM);
            } else {
                throw new ApiException(OutBoundProblemException.EX_REPORT_PROBLEMS_FAILED.toString(),obProblemDTO.getItemNo());
            }
            Optional.ofNullable(customerShipmentRepository.save(customerShipment))
                    .orElseThrow(() -> new ApiException(OutBoundProblemException.EX_REPORT_PROBLEMS_FAILED.toString(),obProblemDTO.getItemNo()));
            //少货和其他问题
            problem = Optional.ofNullable(obProblemRepository.save(obProblemMapper.toEntity(obProblemDTO)))
                    .orElseThrow(() -> new ApiException(OutBoundProblemException.EX_REPORT_PROBLEMS_FAILED.toString(),obProblemDTO.getItemNo()));

       //同时向OBProblemCheck表添加数据
        if(obProblemDTO.getProblemType().equals(ProblemType.LOSE.toString())){
           Optional.ofNullable(obProblemCheckRepository.save(obProblemCheckMapper.toEntity(obProblemCheckMapper.toProblemDTO(obProblemDTO))))
                    .orElseThrow(() -> new ApiException(OutBoundProblemException.EX_REPORT_PROBLEMS_FAILED.toString(), obProblemDTO.getItemNo()));

        }
        }else {
            //多货
            Optional.ofNullable(obProblemCheckRepository.save(obProblemCheckMapper.toEntity(obProblemCheckMapper.toProblemDTO(obProblemDTO))))
                    .orElseThrow(() -> new ApiException(OutBoundProblemException.EX_REPORT_PROBLEMS_FAILED.toString(), obProblemDTO.getItemNo()));
        }
        return problem;
    }

    @Override
    public OBProblem creatOBProblem(OBProblemDTO obProblemDTO) {
        CustomerShipment customerShipment = customerShipmentRepository.retrieve(obProblemDTO.getShipmentId());
        if (customerShipment != null) {
            customerShipment.setState(ShipmentState.PROBLEM);
            Optional.ofNullable(customerShipmentRepository.save(customerShipment))
                    .orElseThrow(() -> new ApiException(OutBoundProblemException.EX_REPORT_PROBLEMS_FAILED.toString(),obProblemDTO.getItemNo()));
        } else {
            throw new ApiException(OutBoundProblemException.EX_REPORT_PROBLEMS_FAILED.toString(),obProblemDTO.getItemNo());
        }
        OBProblem problem = Optional.ofNullable(obProblemRepository.save(obProblemMapper.toEntity(obProblemDTO)))
                .orElseThrow(() -> new ApiException(OutBoundProblemException.EX_REPORT_PROBLEMS_FAILED.toString(),obProblemDTO.getItemNo()));
        return problem;
    }

    private String[] duplicateRemoval(String[] array) {
        Set<String> set = new HashSet<>();
        for(int i=0;i<array.length;i++){
            set.add(array[i]);
        }
        return  (String[]) set.toArray(new String[set.size()]);
    }
    //绑定工作站
    @Override
    public OBPStationDTO bindingWorkstation(String name) {
        //获得工作站
        OBPStation obpStation = obpStationRepository.getByName(
                applicationContext.getCurrentWarehouse(), name);

        //判断工作站是否存在
        if (obpStation.getEntityLock() != 0 || obpStation == null || obpStation.equals("")) {
            throw new ApiException(OutBoundProblemException.OBP_NO_WORKSTATION.toString(), name);
        }
        if (!obpStation.getObpStationType().getName().equalsIgnoreCase("OBP_CHECK")){
            throw new ApiException(OutBoundProblemException.OBP_DEAL_WORKSTATION.toString(), name);
        }

        //判断逻辑工作站所对用的物理工作站是否有人
        if (obpStation.getWorkStation().getOperator() != null) {
            //物理工作站有人， 判断当前用户与绑定人员是否为同一个人
            if (!obpStation.getWorkStation().getOperator().getName().equalsIgnoreCase(userRepository.retrieve(applicationContext.getCurrentUser()).getName())){
//               if (!obpStation.getWorkStation().getStationName().equalsIgnoreCase(name)){
                    throw new ApiException(OutBoundProblemException.OBP_WORKSTATION_SOMEONE.toString(),
                        obpStation.getWorkStation().getOperator().getName());
//               }
            }
            if (!obpStation.getWorkStation().getStationName().equalsIgnoreCase(name)){
                throw new ApiException(OutBoundProblemException.OBP_WORKSTATION_SOMEONE.toString(),
                        obpStation.getWorkStation().getOperator().getName());
            }
        }
        if (obpStation.getOperator() != null) {
            //如果逻辑工作站有人， 判断当前用户与绑定人员是否为同一个人
            if (!obpStation.getOperator().getName().equalsIgnoreCase(userRepository.retrieve(applicationContext.getCurrentUser()).getName())) {
                throw new ApiException(OutBoundProblemException.OBP_WORKSTATION_SOMEONE.toString(),
                        obpStation.getOperator().getName());
            }
        }
        //工作站无人使用时，绑定当前用户
        obpStation.setOperator(userRepository.retrieve(applicationContext.getCurrentUser()));
        obpStation.getWorkStation().setOperator(userRepository.retrieve(applicationContext.getCurrentUser()));
        obpStation.getWorkStation().setStationName(obpStation.getName());

        return obpStationMapper.toDTO(obpStationRepository.save(obpStation));
    }

    //解绑工作站
    @Override
    public OBPStationDTO untieWorkstation(String name) {

        //获得工作站
        OBPStation obpStation = obpStationRepository.getByName(applicationContext.getCurrentWarehouse(), name);


        //判断工作站是否存在
        if (obpStation.getEntityLock() != 0 || obpStation == null || obpStation.equals("")) {
            throw new ApiException(OutBoundProblemException.OBP_NO_WORKSTATION.toString(), name);
        }

//        //判断解绑人是否是此工作站的操作人
//        if (!applicationContext.getCurrentUser().equals(obpStation.getOperator().getName())) {
//            throw new ApiException(OutBoundProblemException.OBP_NO_UNTIE_WORKSTATION.toString(),
//                    obpStation.getOperator().getName());

        //解绑当前工作站
        obpStation.setOperator(null);
        //解除与物理工作站的绑定
        obpStation.getWorkStation().setOperator(null);
        obpStation.getWorkStation().setStationName(null);
        obpStation.getWorkStation().setCallPod(false);


        return obpStationMapper.toDTO(obpStationRepository.save(obpStation));
    }

    //停止呼叫pod和恢复分配pod
    @Override
    public void getStopCallPod(String workStationId, String type) {
        OBPStation obpStation = obpStationRepository.retrieve(workStationId);

//        WorkStation workStation = workStationRepository.retrieve(obpStation.getWorkStation());
        WorkStation workStation = obpStation.getWorkStation();
        if ("stop".equals(type)) {
            workStation.setCallPod(false);
        } else if ("start".equals(type)) {
            workStation.setCallPod(true);

        }
        workStationRepository.saveAndFlush(workStation);
    }
    // 过滤 pod
    @Override
    public List<Map> getPodFace(List<String> outboundProblemIds,String sectionId,String jobType) {
        List<Map> podFace = new ArrayList<>();
        List<String> storageLocationIds = new ArrayList<>();

        for (String outboundProblemId : outboundProblemIds) {
            List<StowStockunitRecordDTO> dtos = sumStockunitRecordByProblem(outboundProblemId,jobType);

            storageLocationIds.addAll(dtos.stream().filter(dto -> dto.getUnexamined().equals("H")).
                    map(StowStockunitRecordDTO::getStorageLocationId).distinct().
                    collect(Collectors.toList()));
        }

        storageLocationIds.stream().distinct();
        for (String storageLocationId : storageLocationIds) {
            podFace.addAll(obProblemQuery.getPodFace(storageLocationId,sectionId));
        }
        podFace.stream().distinct();
        return podFace;
    }

    @Override
    public Boolean getWorkStationPodState(String workStationId) {
        WorkStation workStation = workStationRepository.findOne(workStationId);
        return workStation.isCallPod();
    }

    @Override
    public Boolean getYesOrNoFinsh(String name){
        Boolean bool = false;
        //获得工作站
        OBPStation obpStation = obpStationRepository.getByName(applicationContext.getCurrentWarehouse(), name);

        List<RcsTrip> rcsTripList = obpStationRepository.getRcsTrip(obpStation.getWorkStation().getId(),applicationContext.getCurrentWarehouse());
        if (rcsTripList.size() > 0){
            bool = true;
//            for (RcsTrip rcsTrip : rcsTripList){
//                if (!"Finish".equalsIgnoreCase(rcsTrip.getTripState())){
//                    bool = true;
//                    return bool;
//                }else{
//                    bool = false;// 正常退出
//                }
//            }
        }else {
            bool = false;
        }
        return bool;
    }





}
