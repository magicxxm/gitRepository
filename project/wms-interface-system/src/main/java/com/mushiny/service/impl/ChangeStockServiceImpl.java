package com.mushiny.service.impl;

import com.mushiny.business.CommanBusiness;
import com.mushiny.common.crud.AccessDTO;
import com.mushiny.constants.ChangeType;
import com.mushiny.model.*;
import com.mushiny.repository.*;
import com.mushiny.service.ChangeStockService;
import com.mushiny.utils.DateUtil;
import com.mushiny.utils.StockStateUtil;
import com.mushiny.utils.StringUtil;
import com.mushiny.web.dto.StockChangeInfo;
import com.mushiny.web.dto.StockChangePosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by 123 on 2018/2/9.
 */
@Service
@Transactional
public class ChangeStockServiceImpl implements ChangeStockService {
    private final Logger log = LoggerFactory.getLogger(ChangeStockServiceImpl.class);

    private final StockUnitRepository stockUnitRepository;
    private final ClientRepository clientRepository;
    private final EntityManager manager;
    private final LotRepository lotRepository;
    private final ItemDataRepository itemDataRepository;
    private final WarehouseRepository warehouseRepository;
    private final CommanBusiness commanBusiness;
    private final ChangePositionRepository changePositionRepository;
    private final ChangeRepository changeRepository;

    @Autowired
    public ChangeStockServiceImpl(StockUnitRepository stockUnitRepository,
                                  ClientRepository clientRepository,
                                  EntityManager manager,
                                  LotRepository lotRepository,
                                  ItemDataRepository itemDataRepository,
                                  WarehouseRepository warehouseRepository,
                                  ChangePositionRepository changePositionRepository,
                                  ChangeRepository changeRepository,
                                  CommanBusiness commanBusiness) {
        this.stockUnitRepository = stockUnitRepository;
        this.clientRepository = clientRepository;
        this.manager = manager;
        this.lotRepository = lotRepository;
        this.itemDataRepository = itemDataRepository;
        this.warehouseRepository = warehouseRepository;
        this.commanBusiness = commanBusiness;
        this.changePositionRepository = changePositionRepository;
        this.changeRepository = changeRepository;
    }

    @Override
    public AccessDTO changeStockUnit(StockChangeInfo dto) {
        log.info("开始库存调整");
        AccessDTO accessDTO = new AccessDTO();

        int type = StringUtil.stringToint(dto.getChangeType());

        Warehouse warehouse = null;
//        if ("suning".equalsIgnoreCase(this.warehouseCtrl)) {
            warehouse = warehouseRepository.getByWarehouseNo(dto.getWarehouseNo());
//        } else {
//            warehouse = warehouseRepository.getByWarehouseNo("DEFAULT");
//        }

        //创建反馈结果主表
//        this.generateChangeTable(dto);
        String message = "";
        //调整状态
        if(ChangeType.STATE == type){
            try {
                message =  this.changeStockUnitState(dto,warehouse);
            }catch (Exception e){
                log.error(" 调整状态出现异常 ： " + e.getMessage());
            }
        }
        //调整货主
        if(ChangeType.CLIENT == type){
            message = this.changeStockUnitClient(dto,warehouse);
        }
        //调整有效期
        if(ChangeType.LOT == type){
            message = this.changeStockUnitLot(dto,warehouse);
        }

        //调整批次号
        if(ChangeType.BATCH == type){
            message = this.changeBatchNo(dto,warehouse);
        }

        //调整数量
        if(ChangeType.AMOUNT == type){
            message = this.changeAmount(dto,warehouse);
        }
        accessDTO.setMsg(message);
        accessDTO.setOrderNo(dto.getChangeNo());
        return accessDTO;
    }


    /**
     * 更改库存状态
     * @param dto
     */
    private String changeStockUnitState(StockChangeInfo dto,Warehouse warehouse) {
        //获取需要改变状态的库存
        String message = "成功";
        for (StockChangePosition position:dto.getPositions()) {
            Client client = clientRepository.getByClientNo(position.getClientNo());
            BigDecimal amountNeedChange = StringUtil.stringToDouble(position.getAmount());
            log.info(amountNeedChange + " 件商品需要将库存状态：" + StockStateUtil.toWmsState(position.getStockState()) + "  改为 ：" + StockStateUtil.toWmsState(position.getChangeStockState()));
            List<StockUnit> stockUnits = stockUnitRepository.getStockUnitList(client.getId(),DateUtil.stringToLocalDate(position.getEndDate()),position.getItemNo(),
                    StockStateUtil.toWmsState(position.getStockState()),position.getOutBatchNo(),warehouse.getId());
            boolean changeMsg = true;
            if(stockUnits.isEmpty()){
                log.info("商品itemNo : " + position.getItemNo() +" 没有状态："+position.getStockState()+"，效期："+position.getEndDate()+", 的库存");
                message = "商品itemNo : " + position.getItemNo() +" 没有状态："+position.getStockState()+"，效期："+position.getEndDate()+", 的库存";
                changeMsg = false;
            }
            for (StockUnit s:stockUnits) {
                BigDecimal amount = s.getAmount();
                if(amount.compareTo(amountNeedChange) <= 0){
                    s.setState(StockStateUtil.toWmsState(position.getChangeStockState()));
                    amountNeedChange = amountNeedChange.subtract(amount);
                    continue;
                }
                if(amount.compareTo(amountNeedChange) > 0){
                    //拆分库存信息，把需要改变数量的商品库存状态改变，不要改变的拆分正常
                    StockUnit stockUnit = generateStock(s,StockStateUtil.toWmsState(position.getChangeStockState()),s.getClientId(),s.getLot(),amountNeedChange,s.getBatchOrder());
                    s.setAmount(amount.subtract(amountNeedChange));
                    amountNeedChange = BigDecimal.ZERO;
                }
                if(BigDecimal.ZERO.compareTo(amountNeedChange) == 0){
                    break;
                }
            }
            if(amountNeedChange.compareTo(BigDecimal.ZERO) > 0 && changeMsg){
                log.info("需要改变状态的数量比库存实际数量多,未进行转换的数量： "+amountNeedChange);
                message = "需要改变状态的数量比库存实际数量多,未进行转换的数量： "+amountNeedChange;
            }
            //创建该调整单行的反馈数据
//            this.generateChangePosition(dto.getChangeNo(),position.getLineNo(),message);
            //修改对应调整行的信息
            this.updateChangePosition(dto.getChangeNo(),position.getLineNo(),message,changeMsg);
            log.info("调整单： "+dto.getChangeNo()+" 修改调整行号：" + position.getLineNo()+" 状态成功");
        }

        this.updateChange(dto.getChangeNo());
        log.info("修改调整单： "+dto.getChangeNo()+" 状态成功");
        return message;
    }


    /**
     * 更改库存商品货主
     * @param dto
     */
    private String changeStockUnitClient(StockChangeInfo dto,Warehouse warehouse) {
        //获取需要改变货主的库存
        String message = "成功";
        for (StockChangePosition position:dto.getPositions()) {
            //获取需要改变的货主信息
            Client clientNeed = clientRepository.getByClientNo(position.getChangeClient());

            Client client = clientRepository.getByClientNo(position.getClientNo());
            BigDecimal amountNeedChange = StringUtil.stringToDouble(position.getAmount());
            log.info(amountNeedChange + " 件商品需要将库存货主：" + position.getClientNo() + "  改为 ：" + position.getChangeClient());
            List<StockUnit> stockUnits = stockUnitRepository.getStockUnitList(client.getId(),
                    DateUtil.stringToLocalDate(position.getEndDate()),position.getItemNo(),
                    StockStateUtil.toWmsState(position.getStockState()),position.getOutBatchNo(),warehouse.getId());
            boolean changeMsg = true;
            if(stockUnits.isEmpty()){
                log.info("商品itemNo : " + position.getItemNo() +" 没有状态："+position.getStockState()+"，效期："+position.getEndDate()+", 的库存");
                message = "商品itemNo : " + position.getItemNo() +" 没有状态："+position.getStockState()+"，效期："+position.getEndDate()+", 的库存";
                changeMsg = false;
            }
            for (StockUnit s:stockUnits) {
                BigDecimal amount = s.getAmount();
                if(amount.compareTo(amountNeedChange) <= 0){
                    s.setClientId(clientNeed.getId());
                    amountNeedChange = amountNeedChange.subtract(amount);
                    continue;
                }
                if(amount.compareTo(amountNeedChange) > 0){
                    //拆分库存信息，把需要改变数量的商品库存货主改变，不要改变的拆分正常
                    StockUnit stockUnit = generateStock(s,s.getState(),clientNeed.getId(),s.getLot(),amountNeedChange,s.getBatchOrder());
                    s.setAmount(amount.subtract(amountNeedChange));
                    amountNeedChange = BigDecimal.ZERO;
                }
                if(BigDecimal.ZERO.compareTo(amountNeedChange) == 0){
                    break;
                }
            }

            if(amountNeedChange.compareTo(BigDecimal.ZERO) > 0 && changeMsg){
                log.info("需要改变状态的数量比库存实际数量多,未进行转换的数量： "+amountNeedChange);
                message = "需要改变状态的数量比库存实际数量多,未进行转换的数量： "+amountNeedChange;
            }
            //创建该调整单行的反馈数据
//            this.generateChangePosition(dto.getChangeNo(),position.getLineNo(),message);
            //修改对应调整行的信息
            this.updateChangePosition(dto.getChangeNo(),position.getLineNo(),message,changeMsg);
            log.info("调整单： "+dto.getChangeNo()+" 修改调整行号：" + position.getLineNo()+" 状态成功");
        }
        this.updateChange(dto.getChangeNo());
        log.info("修改调整单： "+dto.getChangeNo()+" 状态成功");
        return message;
    }
    /**
     * 更改库存有效期
     */
    private String changeStockUnitLot(StockChangeInfo dto,Warehouse warehouse) {
        //获取需要改变有效期的库存
        String message = "成功";
        for (StockChangePosition position:dto.getPositions()) {
            //获取需要改变的有效期信息
            Client client = clientRepository.getByClientNo(position.getClientNo());
            ItemData itemData = itemDataRepository.getByItemNoAndClientId(position.getItemNo(),client.getId());

            Lot lot = lotRepository.getByDate(DateUtil.stringToLocalDate(position.getChangeEndDate()),itemData,client.getId());
            //如果没有该有效期，则新增
            if(lot == null){
                LocalDate lotDate = DateUtil.getLotDate();//取当前时间作为生成时间
                lot = commanBusiness.generateLot(lotDate,DateUtil.stringToLocalDate(position.getChangeEndDate()),itemData,client,warehouse);
            }

            BigDecimal amountNeedChange = StringUtil.stringToDouble(position.getAmount());
            log.info(amountNeedChange + " 件商品需要将库存有效期：" + position.getChangeEndDate() + "  改为 ：" + position.getChangeEndDate());
            List<StockUnit> stockUnits = stockUnitRepository.getStockUnitList(client.getId(),DateUtil.stringToLocalDate(position.getEndDate()),position.getItemNo(),
                    StockStateUtil.toWmsState(position.getStockState()),position.getOutBatchNo(),warehouse.getId());
            boolean changeMsg = true;
            if(stockUnits.isEmpty()){
                log.info("商品itemNo : " + position.getItemNo() +" 没有状态："+position.getStockState()+"，效期："+position.getEndDate()+", 的库存");
                message = "商品itemNo : " + position.getItemNo() +" 没有状态："+position.getStockState()+"，效期："+position.getEndDate()+", 的库存";
                changeMsg = false;
            }
            for (StockUnit s:stockUnits) {
                BigDecimal amount = s.getAmount();
                if(amount.compareTo(amountNeedChange) <= 0){
                    s.setLot(lot);
                    amountNeedChange = amountNeedChange.subtract(amount);
                    continue;
                }
                if(amount.compareTo(amountNeedChange) > 0){
                    //拆分库存信息，把需要改变数量的商品库存有效期改变，不要改变的拆分正常
                    StockUnit stockUnit = generateStock(s,s.getState(),s.getClientId(),lot,amountNeedChange,s.getBatchOrder());
                    s.setAmount(amount.subtract(amountNeedChange));
                    amountNeedChange = BigDecimal.ZERO;
                }
                if(BigDecimal.ZERO.compareTo(amountNeedChange) == 0){
                    break;
                }
            }
            if(amountNeedChange.compareTo(BigDecimal.ZERO) > 0 && changeMsg){
                log.info("需要改变状态的数量比库存实际数量多,未进行转换的数量： "+amountNeedChange);
                message = "需要改变状态的数量比库存实际数量多,未进行转换的数量： "+amountNeedChange;
            }
            //创建该调整单行的反馈数据
//            this.generateChangePosition(dto.getChangeNo(),position.getLineNo(),message);
            //修改对应调整行的信息
            this.updateChangePosition(dto.getChangeNo(),position.getLineNo(),message,changeMsg);
            log.info("调整单： "+dto.getChangeNo()+" 修改调整行号：" + position.getLineNo()+" 状态成功");
        }
        this.updateChange(dto.getChangeNo());
        log.info("修改调整单： "+dto.getChangeNo()+" 状态成功");
        return message;
    }

    private String changeAmount(StockChangeInfo dto,Warehouse warehouse) {
        String message = "";
        return message;
    }

    private String changeBatchNo(StockChangeInfo dto,Warehouse warehouse) {
        //获取需要改变批次的库存
        String message = "成功";
        for (StockChangePosition position:dto.getPositions()) {
            //获取需要改变的批次信息
            Client client = clientRepository.getByClientNo(position.getClientNo());
            ItemData itemData = itemDataRepository.getByItemNoAndClientId(position.getItemNo(),client.getId());
//            Lot lot = lotRepository.getByDate(DateUtil.toLocalDate(position.getChangeEndDate()),itemData,client.getId());
            BigDecimal amountNeedChange = StringUtil.stringToDouble(position.getAmount());
            log.info(amountNeedChange + " 件商品需要将库存批次号：" + position.getOutBatchNo() + "  改为 ：" + position.getChangeOutBatch());
            List<StockUnit> stockUnits = stockUnitRepository.getStockUnitList(client.getId(),DateUtil.stringToLocalDate(position.getEndDate()),position.getItemNo(),
                    StockStateUtil.toWmsState(position.getStockState()),position.getOutBatchNo(),warehouse.getId());
            boolean changeMsg = true;
            if(stockUnits.isEmpty()){
                log.info("商品itemNo : " + position.getItemNo() +" 没有状态："+position.getStockState()+"，效期："+position.getEndDate()+", 的库存");
                message = "商品itemNo : " + position.getItemNo() +" 没有状态："+position.getStockState()+"，效期："+position.getEndDate()+", 的库存";
                changeMsg = false;
            }
            for (StockUnit s:stockUnits) {
                BigDecimal amount = s.getAmount();
                if(amount.compareTo(amountNeedChange) <= 0){
                    s.setBatchOrder(position.getChangeOutBatch());
                    amountNeedChange = amountNeedChange.subtract(amount);
                    continue;
                }
                if(amount.compareTo(amountNeedChange) > 0){
                    //拆分库存信息，把需要改变数量的商品库存批次改变，不要改变的拆分正常
                    StockUnit stockUnit = generateStock(s,s.getState(),s.getClientId(),s.getLot(),amountNeedChange,position.getChangeOutBatch());
                    s.setAmount(amount.subtract(amountNeedChange));
                    amountNeedChange = BigDecimal.ZERO;
                }
                if(BigDecimal.ZERO.compareTo(amountNeedChange) == 0){
                    break;
                }
            }
            if(amountNeedChange.compareTo(BigDecimal.ZERO) > 0 && changeMsg){
                log.info("需要改变状态的数量比库存实际数量多,未进行转换的数量： "+amountNeedChange);
                message = "需要改变状态的数量比库存实际数量多,未进行转换的数量： "+amountNeedChange;
            }
            //创建该调整单行的反馈数据
//            this.generateChangePosition(dto.getChangeNo(),position.getLineNo(),message);
            //修改对应调整行的信息
            this.updateChangePosition(dto.getChangeNo(),position.getLineNo(),message,changeMsg);
            log.info("调整单： "+dto.getChangeNo()+" 修改调整行号：" + position.getLineNo()+" 状态成功");
        }

        //修改调整单状态
        this.updateChange(dto.getChangeNo());
        log.info("修改调整单： "+dto.getChangeNo()+" 状态成功");
        return message;

    }

    private void updateChange(String changeNo) {
        /*Change change = changeRepository.getByChangeOrder(changeNo);
        change.setNeedResponse("1");*/
        Query query = manager.createNativeQuery("UPDATE SUNING_ZRFC_AGV_CHANGE SET SUNING_ZRFC_AGV_CHANGE.NEED_RESPONSE = '1' " +
                " WHERE SUNING_ZRFC_AGV_CHANGE.CHANGE_ORDER = '"+changeNo+"'");
        int amount = query.executeUpdate();
        if(amount > 0){
            log.info("成功设置需反馈的差异单： " + changeNo);
        }
    }

    private void updateChangePosition(String changeNo, String lineNo, String message,boolean flag) {
        /*ChangePosition  changePosition = changePositionRepository.getByChangeOrderaAndAndZitem(changeNo,lineNo);
        if(!"成功".equalsIgnoreCase(message)){
            changePosition.setStatus(ChangeType.FAIL);
        }else {
            changePosition.setStatus(ChangeType.SUCCESS);
        }
        changePosition.setMessage(message);*/
        String status = "";
        if(!flag){
            status = ChangeType.FAIL;
        }else {
            status = ChangeType.SUCCESS;
        }

        Query query = manager.createNativeQuery("UPDATE SUNING_ZRFC_AGV_CHANGEPOSITION SET SUNING_ZRFC_AGV_CHANGEPOSITION.STATUS = '"+status+"', " +
                " SUNING_ZRFC_AGV_CHANGEPOSITION.MESSAGE = '"+message+"' WHERE " +
                " SUNING_ZRFC_AGV_CHANGEPOSITION.CHANGE_ORDER = '"+changeNo+"' AND SUNING_ZRFC_AGV_CHANGEPOSITION.ZITEM ='"+lineNo+"'");

        query.executeUpdate();
    }

    private void generateChangeTable(StockChangeInfo dto) {
        Change change = new Change();
        change.setSystem(dto.getSystem());
        change.setChangeOrder(dto.getChangeNo());

        manager.persist(change);
    }

    private void generateChangePosition(String changeNo, String lineNo, String message) {
        ChangePosition  changePosition = new ChangePosition();
        changePosition.setChangeOrder(changeNo);
        changePosition.setZitem(lineNo);
        if(!"成功".equalsIgnoreCase(message)){
            changePosition.setStatus(ChangeType.FAIL);
        }else {
            changePosition.setStatus(ChangeType.SUCCESS);
        }
        changePosition.setMessage(message);

        manager.persist(changePosition);
    }

    private StockUnit generateStock(StockUnit s, String stockState,String clientId, Lot lot,BigDecimal amountNeedChange,String batchNo) {
        StockUnit stockUnit = new StockUnit();
        stockUnit.setAmount(amountNeedChange);
        stockUnit.setState(stockState);
        stockUnit.setClientId(clientId);
        stockUnit.setUnitLoad(s.getUnitLoad());
        stockUnit.setLot(lot);
        stockUnit.setBatchOrder(batchNo);
        stockUnit.setItemData(s.getItemData());
        stockUnit.setWarehouseId(s.getWarehouseId());

        manager.persist(stockUnit);
        return stockUnit;
    }
}
