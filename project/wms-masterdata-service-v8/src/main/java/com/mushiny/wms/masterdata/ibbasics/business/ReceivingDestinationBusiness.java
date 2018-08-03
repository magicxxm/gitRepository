package com.mushiny.wms.masterdata.ibbasics.business;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.business.enums.ReceivingCategoryFilter;
import com.mushiny.wms.masterdata.ibbasics.domain.*;
import com.mushiny.wms.masterdata.ibbasics.domain.enums.ReceivingCategoryRuleZoneType;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.*;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class ReceivingDestinationBusiness {

    private final ApplicationContext applicationContext;
    private final ReceiveDestinationRepository receivingDestinationRepository;
    private final ReceiveStationTypePositionRepository receivingStationTypePositionRepository;
    private final ReceiveCategoryRepository receivingCategoryRepository;
    private final ReplenishStrategyRepository replenishStrategyRepository;

    @Autowired
    public ReceivingDestinationBusiness(ApplicationContext applicationContext,
                                        ReceiveDestinationRepository receivingDestinationRepository,
                                        ReceiveStationTypePositionRepository receivingStationTypePositionRepository,
                                        ReceiveCategoryRepository receivingCategoryRepository,
                                        ReplenishStrategyRepository replenishStrategyRepository) {
        this.applicationContext = applicationContext;
        this.receivingDestinationRepository = receivingDestinationRepository;
        this.receivingStationTypePositionRepository = receivingStationTypePositionRepository;
        this.receivingCategoryRepository = receivingCategoryRepository;
        this.replenishStrategyRepository = replenishStrategyRepository;
    }


    public ReceiveDestination getReceivingDestination(String name) {
        String warehouse = applicationContext.getCurrentWarehouse();
        ReceiveDestination receivingDestination = Optional
                .ofNullable(receivingDestinationRepository.getByName(warehouse, name))
                .orElseThrow(() -> new ApiException(InBoundException
                        .EX_SCANNING_OBJECT_NOT_FOUND.toString(), name));
        // 判断目的地是否被删除
        if (!receivingDestination.getEntityLock().equals(Constant.NOT_LOCKED)) {
            throw new ApiException(InBoundException
                    .EX_RECEIVING_DESTINATION_HAS_DELETED.toString(), name);
        }
        return receivingDestination;
    }

    public ReceiveStationTypePosition checkReceivingStationTypePosition(ReceiveStationType receivingStationType,
                                                                        ReceiveDestination receivingDestination) {
        // 绑定的目的地必须在ReceivingStationTypePosition中存在
        ReceiveStationTypePosition position = receivingStationTypePositionRepository.getPosition(
                receivingStationType, receivingDestination);
        if (position == null) {
            throw new ApiException(InBoundException
                    .EX_RECEIVING_DESTINATION_NOT_FOUND_IN_POSITION.toString(), receivingDestination.getName());
        }
        return position;
    }

    public void usedToReceivingStation(ReceiveStation receivingStation, ReceiveDestination receivingDestination) {
        // 判断目的地是否已经被绑定
//        List<ReceivingProcessContainer> processContainers = receivingProcessContainerRepository
//                .getByStationAndDestination(receivingStation, receivingDestination,
//                        ReceivingProcessContainerState.Started.toString());
//        if (processContainers != null && !processContainers.isEmpty()) {
//            throw new ApiException(InBoundException
//                    .EX_RECEIVING_DESTINATION_HAS_USED.toString(), receivingDestination.getName());
//        }
    }

    public ReceiveDestination screening(ItemData itemData, String receivingType) {
        // 筛选出SKU的目的地
        List<ReceiveCategory> categories = receivingCategoryRepository.getByReceivingType(
                itemData.getWarehouseId(), itemData.getClientId(), receivingType);
        if (categories == null || categories.isEmpty()) {
            throw new ApiException(InBoundException
//                    .EX_RECEIVING_CATEGORY_NOT_FOUND.toString(), itemData.getClientId);
                    .EX_RECEIVING_CATEGORY_NOT_FOUND.toString(), itemData.getClientId());
        }
        ReceiveCategory receivingCategory = null;
        for (ReceiveCategory category : categories) {
            List<ReceiveCategoryPosition> categoryPositions = category.getPositions();
            if (categoryPositions == null || categoryPositions.isEmpty()) {
                continue;
            }
            boolean resultFlag = true;
            for (ReceiveCategoryPosition categoryPosition : categoryPositions) {
                // 如果其中一个条件结果为false，那么结束本次判断。
                if (!runReceivingCategoryPosition(categoryPosition, itemData)) {
                    resultFlag = false;
                    break;
                }
            }
            if (resultFlag) {
                receivingCategory = category;
                break;
            }
        }
//
//        ReceiveCategory receivingCategory = screeningReceivingDestinationBusiness
//                .screeningReceivingCategory(itemData, receivingType);
        if (receivingCategory == null) {
            return null;
        }
        return receivingCategory.getReceiveDestination();
    }

    private boolean runReceivingCategoryPosition(ReceiveCategoryPosition categoryPosition, ItemData itemData) {
        if (categoryPosition == null) {
            return false;
        }
        // 执行判断条件
        String ruleName = categoryPosition.getReceivingCategoryRule().getDecisionKey();
        String operator = categoryPosition.getOperator();
        String value = categoryPosition.getCompKey();
        boolean resultFlag = false;
        switch (ruleName) {
            case "NEW_SKU":
                // 是否测量
                if (operator.equalsIgnoreCase(ReceivingCategoryFilter.NOT_MEASURED.toString())) {
                    if (itemData.isMeasured()) {
//                        List<StockUnit> stockUnits = stockUnitRepository.getCubiScanItemNoByItemNo(itemData.getItemNo());
//                        if (stockUnits == null || stockUnits.isEmpty()) {
//                            resultFlag = true;
//                        }
                    }
                }
                break;
            case "FLAG":
                // SKU属于哪个ItemGroup
                if (operator.equalsIgnoreCase(ReceivingCategoryFilter.MATCH_AT_LEAST_ONE.toString())) {
                    if (value.toUpperCase().contains(itemData.getItemGroup().getId().toUpperCase())) {
                        resultFlag = true;
                    }
                }
                break;
            case "MARK":
                // SKU属于哪个热销的
//                if (operator.equalsIgnoreCase(ReceivingCategoryFilter.MATCH_AT_LEAST_ONE.toString())) {
//                    if (value.toUpperCase().contains(itemData.getItemSellingDegree().getId().toUpperCase())) {
//                        resultFlag = true;
//                    }
//                }
                break;
            case "ZONE_TYPE":
                if (operator.equalsIgnoreCase(ReceivingCategoryFilter.EQUAL.toString())) {
                    resultFlag = checkZoneType(itemData, value);
                }
                break;
            case "VOLUME":
                // SKU的体积
                resultFlag = checkVolume(operator, value, itemData.getVolume());
            case "CLIENT":
                // SKU属于哪个客户
//                if (operator.equalsIgnoreCase(ReceivingCategoryFilter.EQUAL.toString())) {
//                    if (value.toUpperCase().contains(itemData.getClient().getId().toUpperCase())) {
//                        resultFlag = true;
//                    }
//                }
                break;
            case "PROBLEM":
                // 兜底处理
                if (operator.equalsIgnoreCase(ReceivingCategoryFilter.NA.toString())) {
                    resultFlag = true;
                }
                break;
            default:
                resultFlag = false;
        }
        return resultFlag;
    }

    private boolean checkZoneType(ItemData itemData, String comparisonValue) {
        // SKU收到哪里PICKING_ZONE或者是BUFFER_ZONE
//        ItemSellingDegree sellingDegree = itemData.getItemSellingDegree();
//        // 如果ItemSellingDegree中的ReplenishDoc为0，结束该筛选
//        if (sellingDegree.getReplenishDoc() == 0) {
//            return false;
//        }
        // 如果为BUFFER_ZONE默认执行成功
        if (comparisonValue.equalsIgnoreCase(ReceivingCategoryRuleZoneType.BUFFER_ZONE.toString())) {
            return true;
        }
        // 如果为PICKING_ZONE，执行下面判断
        if (comparisonValue.equalsIgnoreCase(ReceivingCategoryRuleZoneType.PICKING_ZONE.toString())) {
            ReplenishStrategy replenishStrategy = replenishStrategyRepository
                    .getByClient(itemData.getWarehouseId(), itemData.getClientId());
            if (replenishStrategy == null) {
                throw new ApiException(InBoundException
//                        .EX_REPLENISH_STRATEGY_NOT_FOUND.toString(), itemData.getClient().getName());
                        .EX_REPLENISH_STRATEGY_NOT_FOUND.toString(), itemData.getClientId());
            }
//            // 如果ReplenishStrategy中的ReceivePrime为CLOSED,直接结束这个判断条件
//            if (replenishStrategy.getReceivePrime().equalsIgnoreCase("CLOSED")) {
//                return false;
//            }
            /**
            // 如果ItemDataTypeGradeStats中的UNITS_DAY为0，那么UNITS_DAY=ReplenishStrategy的S/D * U/S
            ItemDataTypeGradeStats gradeStats = itemDataTypeGradeStatsRepository.getByItemData(itemData);
            BigDecimal unitsDay;
            if (gradeStats == null || gradeStats.getUnitsDay() == 0) {
                unitsDay = replenishStrategy.getShipmentDay().multiply(replenishStrategy.getUnitsShipment());
            } else {
                unitsDay = new BigDecimal(gradeStats.getUnitsDay());
            }
            // 在StockUnit中的StorageLocation中某个商品的总数量(AMOUNT-RESERVED_AMOUNT)
            BigDecimal amount = stockUnitRepository.sumByItemData(itemData);
            if (replenishStrategy.getReceivePrimeStrategy().equalsIgnoreCase("RECEIVE_DOC")) {
                BigDecimal receiveDoc = new BigDecimal(sellingDegree.getReceiveDoc());
                if (amount.compareTo(unitsDay.multiply(receiveDoc)) == -1) {
                    return true;
                }
            } else if (replenishStrategy.getReceivePrimeStrategy().equalsIgnoreCase("SAFETY_DOC")) {
                BigDecimal safetyDoc = new BigDecimal(sellingDegree.getSafetyDoc());
                if (amount.compareTo(unitsDay.multiply(safetyDoc)) == -1) {
                    return true;
                }
            }
             */
        }
        return false;
    }

    private boolean checkVolume(String operator, String value, BigDecimal itemDataVolume) {
        BigDecimal volume = new BigDecimal(value);
        boolean resultFlag = false;
        switch (operator.toUpperCase()) {
            case "EQUAL":
                if (itemDataVolume.compareTo(volume) == 0) {
                    resultFlag = true;
                }
                break;
            case "GREATER_THAN":
                if (itemDataVolume.compareTo(volume) == 1) {
                    resultFlag = true;
                }
                break;
            case "GREATER_THAN_OR_EQUAL_TO":
                if (itemDataVolume.compareTo(volume) >= 0) {
                    resultFlag = true;
                }
                break;
            case "LESS_THAN":
                if (itemDataVolume.compareTo(volume) == -1) {
                    resultFlag = true;
                }
                break;
            case "LESS_THAN_OR_EQUAL_TO":
                if (itemDataVolume.compareTo(volume) <= 0) {
                    resultFlag = true;
                }
                break;
            default:
                resultFlag = false;
        }
        return resultFlag;
    }
}
