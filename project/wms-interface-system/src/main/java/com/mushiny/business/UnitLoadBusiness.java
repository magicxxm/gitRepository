package com.mushiny.business;

import com.mushiny.common.crud.AccessDTO;
import com.mushiny.model.*;
import com.mushiny.repository.StockUnitRepository;
import com.mushiny.repository.StorageLocationRepository;
import com.mushiny.repository.UnitLoadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;

/**
 * Created by 123 on 2018/2/8.
 */
@Component
public class UnitLoadBusiness {
    private final Logger log = LoggerFactory.getLogger(UnitLoadBusiness.class);

    private final UnitLoadRepository unitLoadRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final StockUnitRepository stockUnitRepository;
    private final EntityManager entityManager;

    @Autowired
    public UnitLoadBusiness(UnitLoadRepository unitLoadRepository,
                            StorageLocationRepository storageLocationRepository,
                            StockUnitRepository stockUnitRepository,
                            EntityManager entityManager) {
        this.unitLoadRepository = unitLoadRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.entityManager = entityManager;
    }

    public AccessDTO checkContainer(String storageLocationName, Warehouse warehouse) {
        AccessDTO accessDTO = new AccessDTO();
        StorageLocation storageLocation = storageLocationRepository.getByNameAndWarehouseId(storageLocationName,warehouse.getId());
        if(storageLocation == null){
            log.info("货框号： " + storageLocationName +" 在系统中不存在");
            accessDTO.setMsg("货框号： " + storageLocationName +" 在系统中不存在");
            accessDTO.setCode("1");
            return accessDTO;
        }

        UnitLoad unitLoad = unitLoadRepository.getByStorageLocationAndWarehouse(storageLocation,warehouse.getId());
        if(unitLoad != null){
            if(unitLoad.getEntityLock() == 1){
                log.info("货框号： " + storageLocationName + " 已被锁定，不能使用。。。");
                accessDTO.setMsg("货框号： " + storageLocationName + " 已被锁定，不能使用");
                accessDTO.setCode("1");
                return accessDTO;
            }

            BigDecimal amountStock = stockUnitRepository.getAmountByUnitLoad(unitLoad,warehouse);
            if(amountStock != null && BigDecimal.ZERO.compareTo(amountStock) < 0){
                log.info("货框号： " + storageLocationName + " 在系统中存在库存，不能使用。。。");
                accessDTO.setMsg("货框号： " + storageLocationName + " 在系统中存在库存，请先处理该货框");
                accessDTO.setCode("1");
                return accessDTO;
            }
        }
        return accessDTO;
    }

    public UnitLoad generateUnitLoad(String label,StorageLocation storageLocation, Warehouse warehouse) {
        UnitLoad unitLoad = new UnitLoad();

        unitLoad.setLabelId(label);
        unitLoad.setStorageLocation(storageLocation);
        unitLoad.setWarehouseId(warehouse.getId());

        entityManager.persist(unitLoad);

        return unitLoad;
    }

    public int deleteUnitLoad(UnitLoad unitLoad) {
        Query query = entityManager.createQuery("delete from "+
        UnitLoad.class.getSimpleName() +
                " u " +
        " where u.labelId = :label");
        query.setParameter("label",unitLoad.getLabelId());
        int a = query.executeUpdate();
        if(a > 0){
            log.info("由于上架单存在系统中没有的商品，删除已经创建的UnitLoad ：" + unitLoad.getLabelId());
        }
        return a;
    }

    public void deleteStockUnit(UnitLoad unitLoad) {
        Query query = entityManager.createQuery("delete from " +
                StockUnit.class.getSimpleName() +
                " s " +
                " where s.unitLoad = :unitLoad ");
        query.setParameter("unitLoad",unitLoad);
        int a = query.executeUpdate();
        if(a > 0){
            log.info("由于上架单存在系统中没有的商品，删除已经创建的 Stockunit ：" + unitLoad.getLabelId());
        }
        return;
    }

    public void deleteStockUnitRecorade(UnitLoad unitLoad) {
        Query query = entityManager.createQuery("delete from " +
        StockUnitRecord.class.getSimpleName() +
                " s " +
                " where s.toUnitLoad = :unitLoad ");
        query.setParameter("unitLoad",unitLoad.getLabelId());
        int a = query.executeUpdate();
        if(a > 0){
            log.info("由于上架单存在系统中没有的商品，删除已经创建的 StockunitRecord ：" + unitLoad.getLabelId());
        }
        return;
    }
}
