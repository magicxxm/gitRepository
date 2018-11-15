package wms.repository.common.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaContext;
import wms.common.context.ApplicationContext;
import wms.domain.common.*;
import wms.repository.common.LotRepository;
import wms.repository.common.StockUnitRecordRepositoryCustom;
import wms.repository.common.UserRepository;
import wms.service.EntityGenerator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

public class StockUnitRecordRepositoryImpl implements StockUnitRecordRepositoryCustom {

    private final Logger log = LoggerFactory.getLogger(StockUnitRecordRepositoryImpl.class);

    private final EntityManager manager;
    private final EntityGenerator entityGenerator;
    private final ApplicationContext applicationContext;
    private final UserRepository userRepository;
    private final LotRepository lotRepository;

    public StockUnitRecordRepositoryImpl(JpaContext context,
                                         EntityGenerator entityGenerator,
                                         ApplicationContext applicationContext,
                                         UserRepository userRepository,
                                         LotRepository lotRepository) {
        this.manager = context.getEntityManagerByManagedType(StockUnitRecord.class);
        this.entityGenerator = entityGenerator;
        this.applicationContext = applicationContext;
        this.userRepository = userRepository;
        this.lotRepository = lotRepository;
    }

    @Override
    public StockUnitRecord recordCreation(BigDecimal amount, StockUnit to, String recodeType, String recodeTool) {
        if (BigDecimal.ZERO.compareTo(amount) == 0) {
            log.debug("Do not record zero amount creation");
            return null;
        }
        StockUnitRecord rec = entityGenerator.generateEntity(StockUnitRecord.class);
        rec.setAmount(amount);
        rec.setAmountStock(amount);
        rec.setFromState(to.getState());
        rec.setFromStockUnit(to.getId());
        if (to.getUnitLoad().getStorageLocation() != null) {
            rec.setFromStorageLocation(to.getUnitLoad().getStorageLocation().getName());
            rec.setToStorageLoaction(to.getUnitLoad().getStorageLocation().getName());
        }
        rec.setFromUnitLoad(to.getUnitLoad().getLabel());
        rec.setItemDataItemNo(to.getItemData().getItemNo());
        rec.setItemDataSKU(to.getItemData().getSkuNo());
        if(rec.getLot() != null){
            Lot lot = lotRepository.findOne(to.getLotId());
            rec.setLot(lot.getLotNo());
        }
        rec.setSerialNumber(to.getSerialNo());
        rec.setToState(to.getState());
        rec.setToStockUnit(to.getId());
        rec.setToUnitLoad(to.getUnitLoad().getLabel());
        rec.setRecordType(recodeType);
        rec.setRecordCode(recodeTool);
        rec.setRecordTool("M");

        manager.persist(rec);

        return rec;
    }

    @Override
    public StockUnitRecord recordTransfer(BigDecimal amount, StockUnit su, UnitLoad old, UnitLoad dest, String comment, String operator, String recodeType, String recodeTool) {
        if (BigDecimal.ZERO.compareTo(amount) == 0) {
            log.debug("Do not record zero amount creation");
            return null;
        }
        StockUnitRecord rec = entityGenerator.generateEntity(StockUnitRecord.class);
        rec.setAmount(amount);
        rec.setToUnitLoad(dest.getLabel());
        if(dest.getStorageLocation()!=null){

            rec.setToStorageLoaction(dest.getStorageLocation().getName());
        }
        rec.setToStockUnit(su.getId());
        rec.setToState(su.getState());
        if(su.getLotId()!=null){
            Lot lot = lotRepository.findOne(su.getLotId());
            rec.setLot(lot.getLotNo());
        }
        rec.setSerialNumber(su.getSerialNo());
        rec.setItemDataSKU(su.getItemData().getSkuNo());
        rec.setItemDataItemNo(su.getItemData().getItemNo());
        rec.setFromUnitLoad(old.getLabel());
        rec.setFromStockUnit(su.getId());
        rec.setWarehouseId(applicationContext.getCurrentWarehouse());
        rec.setClientId(applicationContext.getCurrentClient());
        rec.setOperator(userRepository.findOne(applicationContext.getCurrentUser()).getUsername());
        rec.setFromState(su.getState());
        if(old.getStorageLocation()!=null){
            rec.setFromStorageLocation(old.getStorageLocation().getName());
        }
        rec.setRecordTool(recodeTool);
        rec.setRecordCode("M");
        rec.setRecordType(recodeType);

        manager.persist(rec);
        return rec;
    }

    @Override
    public StockUnitRecord createRecord(PickingOrderPosition pick, StockUnit toStock, BigDecimal amountDamaged, String recodeType) {
        if (BigDecimal.ZERO.compareTo(amountDamaged) == 0) {
            log.debug("Do not record zero amount creation");
            return null;
        }
        StockUnitRecord rec = entityGenerator.generateEntity(StockUnitRecord.class);
        rec.setRecordType(recodeType);
        rec.setRecordCode("M");
        rec.setRecordTool("Pick");
        rec.setClientId(toStock.getClientId());
        rec.setWarehouseId(toStock.getWarehouseId());
        rec.setFromStorageLocation(pick.getPickFromLocationName());
        rec.setFromState(pick.getStockUnit().getState());
        rec.setFromStockUnit(pick.getStockUnit().getId());
        rec.setItemDataItemNo(toStock.getItemData().getItemNo());
        rec.setFromUnitLoad(pick.getPickFromUnitLoadLabel());
        rec.setItemDataSKU(toStock.getItemData().getSkuNo());
        if(toStock.getLotId()!=null){
            Lot lot = lotRepository.findOne(toStock.getLotId());
            rec.setLot(lot.getLotNo());
        }
        rec.setSerialNumber(toStock.getSerialNo());
        rec.setToState(toStock.getState());
        rec.setToStockUnit(toStock.getId());
        if(toStock.getUnitLoad() != null){
            rec.setToStorageLoaction(toStock.getUnitLoad().getStorageLocation().getName());
            rec.setToUnitLoad(toStock.getUnitLoad().getLabel());
        }
        rec.setAmount(amountDamaged);
        rec.setOperator(pick.getOperator().getUsername());

        manager.persist(rec);
        manager.flush();

        return rec;
    }

    @Override
    public StockUnitRecord createRecord(PickingOrderPosition pick,StockUnit stockUnit) {
        StockUnitRecord rec = entityGenerator.generateEntity(StockUnitRecord.class);
        rec.setRecordType("INVENTORY LOSS");
        rec.setRecordCode("M");
        rec.setRecordTool("Pick");
        rec.setClientId(stockUnit.getClientId());
        rec.setWarehouseId(stockUnit.getWarehouseId());
        rec.setFromStorageLocation(pick.getPickFromLocationName());
        rec.setFromState(pick.getStockUnit().getState());
        rec.setFromStockUnit(pick.getStockUnit().getId());
        rec.setItemDataItemNo(stockUnit.getItemData().getItemNo());
        rec.setFromUnitLoad(pick.getPickFromUnitLoadLabel());
        rec.setItemDataSKU(stockUnit.getItemData().getSkuNo());
        if(stockUnit.getLotId()!=null){
            Lot lot = lotRepository.findOne(stockUnit.getLotId());
            rec.setLot(lot.getLotNo());
        }
        rec.setSerialNumber(stockUnit.getSerialNo());
        rec.setToState(stockUnit.getState());
        rec.setAmount(pick.getAmount());
        rec.setOperator(pick.getOperator().getUsername());

        manager.persist(rec);
        manager.flush();

        return rec;
    }

}
