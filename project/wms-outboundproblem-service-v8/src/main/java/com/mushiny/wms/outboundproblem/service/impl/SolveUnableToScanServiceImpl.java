package com.mushiny.wms.outboundproblem.service.impl;


import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.utils.ConversionUtil;
import com.mushiny.wms.outboundproblem.business.*;
import com.mushiny.wms.outboundproblem.business.common.OutBoundProblemCommonBusiness;
import com.mushiny.wms.outboundproblem.crud.dto.OBPSolvePositionDTO;
import com.mushiny.wms.outboundproblem.domain.OBPCell;
import com.mushiny.wms.outboundproblem.domain.OBPSolve;
import com.mushiny.wms.outboundproblem.domain.OBPSolvePosition;
import com.mushiny.wms.outboundproblem.domain.OBProblem;
import com.mushiny.wms.outboundproblem.domain.common.*;
import com.mushiny.wms.outboundproblem.domain.enums.InventoryState;
import com.mushiny.wms.outboundproblem.domain.enums.OBPSolveState;
import com.mushiny.wms.outboundproblem.domain.enums.VirtualStorageLocation;
import com.mushiny.wms.outboundproblem.exception.OutBoundProblemException;
import com.mushiny.wms.outboundproblem.repository.OBPShipmentSerialNoRepository;
import com.mushiny.wms.outboundproblem.repository.OBPSolvePositionRepository;
import com.mushiny.wms.outboundproblem.repository.OBPSolveRepository;
import com.mushiny.wms.outboundproblem.repository.OBProblemRepository;
import com.mushiny.wms.outboundproblem.repository.common.*;
import com.mushiny.wms.outboundproblem.service.SolveUnableToScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Service
@Transactional
public class SolveUnableToScanServiceImpl implements SolveUnableToScanService {
    private final SolveDamagedBusiness solveDamagedBusiness;
    private final ItemDataGlobalRepository itemDataGlobalRepository;
    private final OBProblemRepository obProblemRepository;
    private final ApplicationContext applicationContext;
    private final StorageLocationRepository storageLocationRepository;
    private final ItemDataRepository itemDataRepository;
    private final MoveGoodsBusiness moveGoodsBusiness;
    private final OutBoundProblemCommonBusiness outBoundProblemCommonBusiness;
    private final OBPSolveRepository obpSolveRepository;
    private final OBPSolveBusiness obpSolveBusiness;
    private final OBPSolvePositionRepository obpSolvePositionRepository;
    private final UnitLoadRepository unitLoadRepository;
    private final OBPShipmentSerialNoRepository obpShipmentSerialNoRepository;
    private final CustomerShipmentRepository customerShipmentRepository;
    private final StockUnitRepository stockUnitRepository;
    private final UnitLoadBusiness unitLoadBusiness;
    private final BuildBusiness buildBusiness;
    private final StorageLocationTypeRepository storageLocationTypeRepository;

    @Autowired
    public SolveUnableToScanServiceImpl(SolveDamagedBusiness solveDamagedBusiness,
                                        ItemDataGlobalRepository itemDataGlobalRepository,
                                        OBProblemRepository obProblemRepository,
                                        ApplicationContext applicationContext,
                                        StorageLocationRepository storageLocationRepository,
                                        ItemDataRepository itemDataRepository,
                                        MoveGoodsBusiness moveGoodsBusiness,
                                        OutBoundProblemCommonBusiness outBoundProblemCommonBusiness,
                                        OBPSolveRepository obpSolveRepository,
                                        OBPSolveBusiness obpSolveBusiness,
                                        OBPSolvePositionRepository obpSolvePositionRepository,
                                        UnitLoadRepository unitLoadRepository,
                                        OBPShipmentSerialNoRepository obpShipmentSerialNoRepository,
                                        CustomerShipmentRepository customerShipmentRepository,
                                        StockUnitRepository stockUnitRepository,
                                        UnitLoadBusiness unitLoadBusiness,
                                        BuildBusiness buildBusiness,
                                        StorageLocationTypeRepository storageLocationTypeRepository) {
        this.solveDamagedBusiness = solveDamagedBusiness;
        this.itemDataGlobalRepository = itemDataGlobalRepository;
        this.obProblemRepository = obProblemRepository;
        this.applicationContext = applicationContext;
        this.storageLocationRepository = storageLocationRepository;
        this.itemDataRepository = itemDataRepository;
        this.moveGoodsBusiness = moveGoodsBusiness;
        this.outBoundProblemCommonBusiness = outBoundProblemCommonBusiness;
        this.obpSolveRepository = obpSolveRepository;
        this.obpSolveBusiness = obpSolveBusiness;
        this.obpSolvePositionRepository = obpSolvePositionRepository;
        this.unitLoadRepository = unitLoadRepository;
        this.obpShipmentSerialNoRepository = obpShipmentSerialNoRepository;
        this.customerShipmentRepository = customerShipmentRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.unitLoadBusiness = unitLoadBusiness;
        this.buildBusiness = buildBusiness;
        this.storageLocationTypeRepository = storageLocationTypeRepository;
    }

    @Override
    public ItemDataGlobal printSKUNo(OBPSolvePositionDTO obpSolvePositionDTO) {

        solveDamagedBusiness.solveOBProblem(obpSolvePositionDTO);

        ItemDataGlobal itemDataGlobal=itemDataGlobalRepository.getBySku(obpSolvePositionDTO.getItemNo());
        //补打条码
        return itemDataGlobal;
    }

    @Override
    public void pendingInvestigation(OBPSolvePositionDTO obpSolvePositionDTO) {
        //更新处理结果
        solveDamagedBusiness.solveOBProblem(obpSolvePositionDTO);

        StorageLocation storageLocation = outBoundProblemCommonBusiness.checkProcessContainer(obpSolvePositionDTO.getLocationContainer(),
                InventoryState.PENDING.toString());
        if (!obpSolvePositionDTO.getShipmentNo().isEmpty() && !obpSolvePositionDTO.getItemNo().isEmpty()) {

            //检测商品是否在该订单里
            List<OBPSolve> entityList = obpSolveRepository.getProblemGoodsByShipmentAndItem(applicationContext.getCurrentWarehouse(),
                    obpSolvePositionDTO.getShipmentNo(), obpSolvePositionDTO.getItemNo());
            OBPSolve entity = null;
            if (entityList != null && !entityList.isEmpty()) {
                entity = entityList.get(0);
            }
            if (entity == null) {
                throw new ApiException(OutBoundProblemException.EX_SCANNING_OBJECT_NOT_FOUND.getName(), obpSolvePositionDTO.getShipmentNo());
            }
            ItemData itemData = entity.getItemData();
            // 容器中不能存在不同客户的相同商品
            outBoundProblemCommonBusiness.checkItemClient(storageLocation, entity.getItemData());
//            String fromContainer = unitLoadRepository.getByShipmentNo(
//                    obpSolvePositionDTO.getShipmentNo()).getStorageLocation().getName();
            UnitLoad unitLoad = unitLoadRepository.getByShipmentNo(obpSolvePositionDTO.getShipmentNo());
            if (unitLoad == null) {
                throw new ApiException(OutBoundProblemException.EX_SHIPMENT_HAS_NOT_PICK_UNITLOAD.getName());
            }
            StorageLocation sourceStorageLoacation = unitLoad.getStorageLocation();
            if (sourceStorageLoacation == null) {
                throw new ApiException(OutBoundProblemException.EX_SHIPMENT_HAS_NOT_PICK_UNITLOAD.getName());
            }
            //同种商品只会报一个序列号无法扫描
            //更新OBPROBLEM表
            CustomerShipment customerShipment = customerShipmentRepository.getByShipmentNo(obpSolvePositionDTO.getShipmentNo());
            OBProblem obProblem = obProblemRepository.getByShipmentAndItemSN(applicationContext.getCurrentWarehouse(), customerShipment.getId(), itemData.getId());
            //判断商品是否需要输入有效期
            Lot lot=null;
            if (entity.getItemData().isLotMandatory()) {
                String useNotAfter = obpSolvePositionDTO.getUseNotAfter() + " 00:00:00";
                lot=outBoundProblemCommonBusiness.checkItemLot(sourceStorageLoacation, storageLocation, entity.getItemData(),
                        ConversionUtil.toZonedDateTime(useNotAfter).toLocalDate(), obpSolvePositionDTO.getAmountScaned(), InventoryState.PENDING.toString());
            }

            OBPSolvePosition solvePosition = outBoundProblemCommonBusiness.checkAndSaveSolvePosition(entity, obpSolvePositionDTO.getSolveKey(),
                    obpSolvePositionDTO.getLocationContainer());
            solvePosition.setAmountScaned(solvePosition.getAmountScaned().add(BigDecimal.ONE));
            obpSolvePositionRepository.save(solvePosition);
            UnitLoad destinationUnitLoad = unitLoadBusiness.getByStorageLocation(storageLocation);
            if (destinationUnitLoad == null) {
                destinationUnitLoad = buildBusiness.buildUnitLoad(storageLocation);
            }
            moveGoodsBusiness.movingGoods(unitLoad, destinationUnitLoad, storageLocation, itemData, obProblem.getAmount().subtract(obProblem.getSolveAmount()), "moveToPending",lot);
            obProblem.setContainer(obpSolvePositionDTO.getLocationContainer());
            obProblem.setSolveAmount(obProblem.getAmount());
            obProblemRepository.save(obProblem);
            OBPSolve obpSolve = obpSolveRepository.getByItemNoShipmentNo(applicationContext.getCurrentWarehouse(),obpSolvePositionDTO.getItemNo(),obpSolvePositionDTO.getShipmentNo());
            OBPSolve entityProblem = obpSolveRepository.getByProblemId(obProblem.getId());
            entityProblem.setScaned(OBPSolveState.scaned.toString());
            obpSolveRepository.save(entityProblem);
            //获得该订单里丢失的问题数量
            BigDecimal loseAmount = BigDecimal.ZERO;
            OBProblem problem = obProblemRepository.getByShipmentAndItemLose(applicationContext.getCurrentWarehouse(), obpSolve.getCustomerShipment().getId(), itemData.getId());
            if (problem != null) loseAmount = problem.getAmount();
            if (obpSolve.getAmountShipment().subtract(loseAmount).subtract(obpSolve.getAmountScaned()).compareTo(entityProblem.getAmountProblem()) == 0) {
                obpSolve.setScaned(OBPSolveState.scaned.toString());
                obpSolveRepository.save(obpSolve);
                obpSolveBusiness.addRecord(obpSolvePositionDTO.getShipmentNo(), entity.getObpCell().getName());
            }
        }
    }
}
