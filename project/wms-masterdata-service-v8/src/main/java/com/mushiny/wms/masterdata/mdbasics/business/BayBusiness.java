package com.mushiny.wms.masterdata.mdbasics.business;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.utils.LevelUtil;
import com.mushiny.wms.masterdata.mdbasics.business.dto.BayStorageLocationsDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.*;
import com.mushiny.wms.masterdata.mdbasics.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class BayBusiness {

    private final BayRepository bayRepository;
    private final PodTypeRepository podTypeRepository;
    private final PodTypePositionRepository podTypePositionRepository;
    private final ZoneRepository zoneRepository;
    private final AreaRepository areaRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final ApplicationContext applicationContext;

    @Autowired
    public BayBusiness(BayRepository bayRepository, PodTypeRepository podTypeRepository,
                       PodTypePositionRepository podTypePositionRepository, ZoneRepository zoneRepository,
                       AreaRepository areaRepository, StorageLocationRepository storageLocationRepository,
                       ApplicationContext applicationContext) {
        this.bayRepository = bayRepository;
        this.podTypeRepository = podTypeRepository;
        this.podTypePositionRepository = podTypePositionRepository;
        this.zoneRepository = zoneRepository;
        this.areaRepository = areaRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.applicationContext = applicationContext;
    }

    public void createMore(BayStorageLocationsDTO dto) {
        // 验证传入的参数
        String warehouseid = applicationContext.getCurrentWarehouse();
        String clientid = applicationContext.getCurrentClient();
        PodType podType = podTypeRepository.retrieve(dto.getPodTypeId());
        Zone zone = zoneRepository.retrieve(dto.getZoneId());
        Area area = areaRepository.retrieve(dto.getAreaId());
        if (!clientid.equalsIgnoreCase(zone.getClientId())
                || !clientid.equalsIgnoreCase(area.getClientId())) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
//        List<String> itemSellingDegrees = dto.getItemSellingDegrees();
//        Set<ItemSellingDegree> itemSellingDegreeSet = new HashSet<>();
//        if (itemSellingDegrees != null && !itemSellingDegrees.isEmpty()) {
//            for (String itemSellingDegree : itemSellingDegrees) {
//                itemSellingDegreeSet.add(itemSellingDegreeRepository.retrieve(itemSellingDegree));
//            }
//        }
        int fromAisle = dto.getFromAisle();
        int toAisle = dto.getToAisle();
        int fromBay = dto.getFromBay();
        int toBay = dto.getToBay();
        int bayIndex = dto.getIndex();
        int binIndex;
        // 取出BayType下所有BayTypePosition
        List<PodTypePosition> podTypePositions = podTypePositionRepository.getByPodType(podType);
        // 取bay的最大index
        Bay maxBay = bayRepository.getMaxIndexBay(warehouseid);
        int maxBayIndex = 0;
        if (maxBay != null) {
            maxBayIndex = maxBay.getBayIndex();
        }
        // 判断这批Bay是新增还是插入
        if (bayIndex > maxBayIndex) {// 新增
            bayIndex = maxBayIndex + 1;
            if (maxBay != null) {
                binIndex = storageLocationRepository.getBayMaxIndex(maxBay) + 1;
            } else {
                binIndex = 1;
            }
        } else {// 插入
            binIndex = storageLocationRepository.getBayMinIndex(maxBay);
            // 计算一共可以生成多少个Bay
            int bayCount = (toAisle - fromAisle + 1) * (toBay - fromBay + 1);
            // 计算一共可以生成多少个Bin
            int binCount = 0;
            for (PodTypePosition podTypePosition : podTypePositions) {
                for (int c = 1; c <= podTypePosition.getNumberOfColumns(); c++) {
                    binCount++;
                }
            }
            binCount = bayCount * binCount;
            // 更新顺序
            if (bayIndex > 0 && bayCount > 0) {
                bayRepository.updateCreateBayIndex(bayCount, bayIndex, warehouseid);
                if (binIndex > 0 && binCount > 0) {
                    storageLocationRepository.updateCreateBinOrderIndex(binCount, binIndex, warehouseid);
                }
            }
        }
        // 生成Bay
        for (int i = fromAisle; i <= toAisle; i++) {
            for (int j = fromBay; j <= toBay; j++) {
                String aisle = String.format("%03d", i);
                String bayName = String.format("%03d", j);
                Bay bay = new Bay();
                bay.setName(zone.getName() + aisle + "-" + bayName);
                bay.setAisle(aisle);
                bay.setPodType(podType);
                bay.setBayIndex(bayIndex);
                bayIndex++;
                bay.setClientId(clientid);
                bay.setWarehouseId(warehouseid);
                checkBayName(warehouseid, bayName);
                bay = bayRepository.save(bay);
                // 生成Bay下的StorageLocation
                for (PodTypePosition podTypePosition : podTypePositions) {
                    for (int c = 1; c <= podTypePosition.getNumberOfColumns(); c++) {
                        StorageLocation storageLocation = new StorageLocation();
                        String locationName = bay.getName() + podTypePosition.getFace() +
                                LevelUtil.getLevel(podTypePosition.getLevel()) + String.format("%02d", c);
                        storageLocation.setName(locationName);
                        storageLocation.setStorageLocationType(podTypePosition.getStorageLocationType());
                        storageLocation.setArea(area);
//                        storageLocation.setLocationCluster(podTypePosition.getLocationCluster());
                        storageLocation.setZone(zone);
                        storageLocation.setDropZone(podTypePosition.getDropZone());
                        storageLocation.setBay(bay);
                        storageLocation.setxPos(podTypePosition.getLevel());
                        storageLocation.setyPos(c);
                        storageLocation.setzPos(1);
                        storageLocation.setOrderIndex(0);
//                        storageLocation.setScanCode(storageLocation.getName());
                        storageLocation.setAllocation(BigDecimal.ZERO);
                        storageLocation.setAllocationState(0);
                        storageLocation.setFace(podTypePosition.getFace());
                        storageLocation.setColor(podTypePosition.getColor());
                        storageLocation.setOrderIndex(binIndex);
                        binIndex++;
                        storageLocation.setClientId(clientid);
                        storageLocation.setWarehouseId(warehouseid);
                        checkStorageLocationName(warehouseid, storageLocation.getName());
                        storageLocation = storageLocationRepository.save(storageLocation);
                        // 生成Bin下的热销度
//                        for (ItemSellingDegree itemSellingDegree : itemSellingDegreeSet) {
//                            StorageLocationItemSellingDegree binISD = new StorageLocationItemSellingDegree();
//                            binISD.setStorageLocation(storageLocation);
//                            binISD.setItemSellingDegree(itemSellingDegree);
//                            checkStorageLocationItemSellingDegree(storageLocation, itemSellingDegree);
//                            storageLocationItemSellingDegreeRepository.save(binISD);
//                        }
                    }
                }
            }
        }
    }

    private void checkBayName(String warehouse, String bayName) {
        Bay bay = bayRepository.getByName(warehouse, bayName);
        if (bay != null) {
            throw new ApiException("该bayName已经存在");
        }
    }

    private void checkStorageLocationName(String warehouse, String name) {
        StorageLocation storageLocation = storageLocationRepository.getByName(warehouse, name);
        if (storageLocation != null) {
            throw new ApiException("该name已经存在");
        }
    }

//    private void checkStorageLocationItemSellingDegree(StorageLocation storageLocation, ItemSellingDegree itemSellingDegree) {
//        StorageLocationItemSellingDegree si = storageLocationItemSellingDegreeRepository
//                .getByStorageLocationAndItemSellingDegree(storageLocation, itemSellingDegree);
//        if (si != null) {
//            throw new ApiException(
//                    MasterDataException.EX_STORAGE_LOCATION_ITEM_SELLING_DEGREE_UNIQUE.toString(),
//                    si.getStorageLocation().getName(), si.getItemSellingDegree().getName());
//        }
//    }
}
