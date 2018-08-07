package com.mushiny.service.impl;

import com.mushiny.business.AdviceBusiness;
import com.mushiny.business.CommanBusiness;
import com.mushiny.business.UnitLoadBusiness;
import com.mushiny.common.crud.AccessDTO;
import com.mushiny.model.*;
import com.mushiny.repository.*;
import com.mushiny.service.AdviceService;
import com.mushiny.utils.DateUtil;
import com.mushiny.utils.StockStateUtil;
import com.mushiny.utils.StringUtil;
import com.mushiny.web.dto.AdviceReceiptDTO;
import com.mushiny.web.dto.AdviceReceiptPositionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by 123 on 2018/2/2.
 */
@Service
@Transactional
public class AdviceServiceImpl implements AdviceService {
    private final Logger log = LoggerFactory.getLogger(AdviceServiceImpl.class);

    private final AdviceBusiness adviceBusiness;
    private final UnitLoadBusiness unitLoadBusiness;
    private final WarehouseRepository warehouseRepository;
    private final UnitLoadRepository unitLoadRepository;
    private final StockUnitRepository stockUnitRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final ClientRepository clientRepository;
    private final CommanBusiness commanBusiness;
    private final ItemDataRepository itemDataRepository;
    private final ItemDataGloableRepository itemDataGloableRepository;
    private final LotRepository lotRepository;

    @Autowired
    public AdviceServiceImpl(AdviceBusiness adviceBusiness,
                             UnitLoadBusiness unitLoadBusiness,
                             WarehouseRepository warehouseRepository,
                             UnitLoadRepository unitLoadRepository,
                             StockUnitRepository stockUnitRepository,
                             StorageLocationRepository storageLocationRepository,
                             ClientRepository clientRepository,
                             CommanBusiness commanBusiness,
                             ItemDataRepository itemDataRepository,
                             ItemDataGloableRepository itemDataGloableRepository,
                             LotRepository lotRepository) {
        this.adviceBusiness = adviceBusiness;
        this.unitLoadBusiness = unitLoadBusiness;
        this.warehouseRepository = warehouseRepository;
        this.unitLoadRepository = unitLoadRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.clientRepository = clientRepository;
        this.commanBusiness = commanBusiness;
        this.itemDataRepository = itemDataRepository;
        this.itemDataGloableRepository = itemDataGloableRepository;
        this.lotRepository = lotRepository;
    }

    @Override
    public AccessDTO createAdviceRequest(AdviceReceiptDTO dto) {
        AccessDTO accessDTO = new AccessDTO();

        accessDTO.setOrderNo(dto.getAdviceNo());

        Warehouse warehouse  = warehouseRepository.getByWarehouseNo(dto.getWarehouseNo());
        if (warehouse == null) {
            log.info("上架单：" + dto.getAdviceNo() + " 对应的仓库在系统中不存在。。。");
            accessDTO.setMsg("上架单：" + dto.getAdviceNo() + " 对应的仓库在系统中不存在");
            accessDTO.setMsg("1");
            return accessDTO;
        }

        //检查该收货单号是否已经存在
        UnitLoad unitLoad = null;

        unitLoad = unitLoadRepository.getByLabel(dto.getAdviceNo(), warehouse.getId());
        if (unitLoad != null) {
            log.info("收货单号： " + dto.getAdviceNo() + " 在系统中已存在");
            accessDTO.setMsg("收货单号： " + dto.getAdviceNo() + " 在系统中已存在");
            accessDTO.setCode("1");
            return accessDTO;
        }

        //检查该上架单对应的容器在系统中是否可用，不可用需要提示
        String storageLocationName = dto.getContainerNo();

        StorageLocation storageLocation = storageLocationRepository.getByNameAndWarehouseId(storageLocationName, warehouse.getId());
        if (storageLocation == null) {
            log.info("货框号： " + storageLocationName + " 在系统中不存在");
            accessDTO.setMsg("货框号： " + storageLocationName + " 在系统中不存在");
            accessDTO.setCode("1");
            return accessDTO;
        }

        UnitLoad ul = unitLoadRepository.getByStorageLocationAndWarehouse(storageLocation, warehouse.getId());
        if (ul != null) {
            if (ul.getEntityLock() == 1) {
                log.info("货框号： " + storageLocationName + " 已被锁定，不能使用。。。");
                accessDTO.setMsg("货框号： " + storageLocationName + " 已被锁定，不能使用");
                accessDTO.setCode("1");
                return accessDTO;
            }

            if(ul.getEntityLock() == 0 && ul.getStationName() != null){
                log.info("货框号： " + storageLocationName + " 已被"+ ul.getStationName() +"，不能使用。。。");
                accessDTO.setMsg("货框号： " + storageLocationName + " 已被"+ ul.getStationName() +"，不能使用。。。");
                accessDTO.setCode("1");
                return accessDTO;
            }

            BigDecimal amountStock = stockUnitRepository.getAmountByUnitLoad(ul, warehouse);
            if (amountStock != null && BigDecimal.ZERO.compareTo(amountStock) < 0) {
                log.info("货框号： " + storageLocationName + " 在系统中存在库存，不能使用。。。");
                accessDTO.setMsg("货框号： " + storageLocationName + " 在系统中存在库存，请先处理该货框");
                accessDTO.setCode("1");
                return accessDTO;
            }

            ul.setEntityLock(2);
        }
        //每个上架单都新建一个unitLoad
        unitLoad = unitLoadBusiness.generateUnitLoad(dto.getAdviceNo(), storageLocation, warehouse);

        //将对应的入库单信息转入到容器库存
        BigDecimal totalWeight = BigDecimal.ZERO;
        List<AdviceReceiptPositionDTO> dtoList = dto.getPositions();
        for (AdviceReceiptPositionDTO position : dtoList) {
            //检查上架单中的货主是否已录入系统，如果没有，则新增
            Client client = clientRepository.getByClientNo(position.getClientNo());
            if (client == null) {
                log.info("新增客户： " + position.getClientNo());
                client = commanBusiness.generateClient(position.getClientNo());

                //新增仓库客户关系
                log.info("新增仓库客户对应关系 client:" + client.getClientNo() +" ,,warhouse: " + warehouse.getWarehouseNo());
                commanBusiness.generateClientWarehouse(client,warehouse);

            }
            //检查该客户对应的商品在系统中是否存在，如果没有，则新增
            ItemData itemData = itemDataRepository.getByItemNo(position.getItemNo());
//            ItemDataGlobal itemDataGlobal = itemDataGloableRepository.getByItemDataNo(position.getItemNo());
            if (itemData == null) {
                log.info("上架单： " + dto.getAdviceNo() + " 中的商品 ： " + position.getItemNo() + " 在系统中不存在。。。");
                accessDTO.setCode("1");
                accessDTO.setMsg("上架单： " + dto.getAdviceNo() + " 中的商品 ： " + position.getItemNo() + " 在系统中不存在");
                //删除保存的库存历史记录
                unitLoadBusiness.deleteStockUnitRecorade(unitLoad);

                //删除已经保存的库存信息
                unitLoadBusiness.deleteStockUnit(unitLoad);

                //删除已经创建的unitLoad
                unitLoadBusiness.deleteUnitLoad(unitLoad);

                return accessDTO;
            }
            //检查有效期是否录入，如果没有，则新增有效期
            LocalDate date = DateUtil.toLocalDate(position.getProductDate());
            if(date == null){
                date = DateUtil.getLotDate();
            }
            LocalDate bestBeforeEnd = DateUtil.toLocalDate(position.getEndDate());
            Lot lot = null;
            if (bestBeforeEnd != null) {
                lot = lotRepository.getByDate(bestBeforeEnd, itemData, client.getId());
                if (lot == null) {
                    log.info("为客户: " + client.getClientNo() + " 的商品: " + position.getItemNo() + " 创建有效期lot，到期日期是： " + position.getEndDate());
                    lot = commanBusiness.generateLot(date, bestBeforeEnd, itemData, client, warehouse);
                }
            }

            //获取该收货单中该商品的总重量
            BigDecimal weight = itemData.getWeight().multiply(StringUtil.stringToDouble(position.getNotifiedAmount()));
            totalWeight = totalWeight.add(weight);

            String state = StockStateUtil.toWmsState(position.getStockState());
            //将收货单商品转库存保存
            StockUnit stockUnit = commanBusiness.generateStockUnit(StringUtil.stringToDouble(position.getNotifiedAmount()), state, itemData, unitLoad, lot, position.getOutBatchNo(), client, warehouse);

            //新建库存历史记录数据
            StockUnitRecord stockUnitRecord = commanBusiness.generateStockUnitRecord(stockUnit, itemData, lot, unitLoad, client, warehouse);
        }

        unitLoad.setWeightCalculated(totalWeight);

        return accessDTO;
    }
}
