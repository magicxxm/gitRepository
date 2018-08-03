package com.mushiny.wms.masterdata.mdbasics.business;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.utils.LevelUtil;
import com.mushiny.wms.masterdata.mdbasics.business.dto.PodStorageLocationsDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.*;
import com.mushiny.wms.masterdata.mdbasics.exception.MasterDataException;
import com.mushiny.wms.masterdata.mdbasics.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PodBusiness {

    private final PodRepository podRepository;
    private final PodTypeRepository podTypeRepository;
    private final PodTypePositionRepository podTypePositionRepository;
    private final ZoneRepository zoneRepository;
    private final AreaRepository areaRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final ApplicationContext applicationContext;
    private final SectionRepository sectionRepository;
    private final MapRepository mapRepository;

    @Autowired
    public PodBusiness(ApplicationContext applicationContext,
                       PodRepository podRepository,
                       PodTypeRepository podTypeRepository,
                       PodTypePositionRepository podTypePositionRepository,
                       ZoneRepository zoneRepository,
                       AreaRepository areaRepository,
                       StorageLocationRepository storageLocationRepository,
                       SectionRepository sectionRepository, MapRepository mapRepository) {
        this.applicationContext = applicationContext;
        this.podRepository = podRepository;
        this.podTypeRepository = podTypeRepository;
        this.podTypePositionRepository = podTypePositionRepository;
        this.zoneRepository = zoneRepository;
        this.areaRepository = areaRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.sectionRepository = sectionRepository;
        this.mapRepository = mapRepository;
    }

    public void createMore(PodStorageLocationsDTO dto) {
        // 验证传入的参数
        int xpos = dto.getXpos();
        int ypos = dto.getYpos();
        String warehouse = applicationContext.getCurrentWarehouse();
        String client = dto.getClientId();
        Zone zone = zoneRepository.retrieve(dto.getZoneId());
        PodType podType = podTypeRepository.retrieve(dto.getPodTypeId());
        Area area = areaRepository.retrieve(dto.getAreaId());
        //验证区域和功能是否属于同一个client下
        if (!client.equalsIgnoreCase(zone.getClientId())
                || !client.equalsIgnoreCase(area.getClientId())) {
            throw new ApiException(MasterDataException.EX_MD_POD_CLIENT_ERROR.toString(), dto.getClientId());
        }
        int fromBay = dto.getFromPod();
        int toBay = dto.getToPod();
        //判断fromBay到toBay之间的数据是否已经创建，如果有直接报异常
        for (int j = fromBay; j <= toBay; j++) {
            Pod p = podRepository.getPod(j, warehouse);
            if (p != null) {
                throw new ApiException("PodIndex为" + j + "已经存在，不可重复创建！");
            }
        }
        // 取出PodType下所有PodTypePosition
        List<PodTypePosition> podTypePositions = podTypePositionRepository.getByPodType(podType);
        // 取pod的最大index
//        int maxPodIndex = podRepository.getMaxIndexPod(warehouse);
        // 生成Pod
        for (int j = fromBay; j <= toBay; j++) {
//            maxPodIndex++;
            String podName = "P" + String.format("%07d", j);
            Pod pod = new Pod();
            pod.setPodType(podType);
            pod.setName(podName);
            pod.setZone(zone);
            pod.setPodIndex(j);
            pod.setClientId(client);
            pod.setxPos(xpos);
            pod.setyPos(ypos);
            pod.setWarehouseId(warehouse);
            pod.setDescription(dto.getDescription());
            pod.setSellingDegree(dto.getSellingDegree());
            pod.setPlaceMark(dto.getPlaceMark());
            pod.setToWard(dto.getToWard());
            //先初始个值，可能会再改
            pod.setState(dto.getState());
            pod.setyPosTar(dto.getyPosTar());
            pod.setxPosTar(dto.getxPosTar());
            pod.setAddrcodeIdTar(dto.getAddrCodeIdTar());
            if (zone.getSectionId() != null) {
                pod.setSection(sectionRepository.retrieve(zone.getSectionId()));
            } else {
                pod.setSection(null);
            }

            checkPodName(warehouse, podName);
            pod = podRepository.save(pod);
            // 生成Pod下的StorageLocation
            int binIndexA = 0;
            int binIndexB = 0;
            int binIndexC = 0;
            int binIndexD = 0;
            for (PodTypePosition podTypePosition : podTypePositions) {
                for (int c = 1; c <= podTypePosition.getNumberOfColumns(); c++) {
                    StorageLocation storageLocation = new StorageLocation();
                    String locationName = pod.getName() + podTypePosition.getFace() +
                            LevelUtil.getLevel(podTypePosition.getLevel()) + String.format("%02d", c);
                    storageLocation.setName(locationName);
                    storageLocation.setStorageLocationType(podTypePosition.getStorageLocationType());
                    storageLocation.setArea(area);
                    storageLocation.setZone(zone);
                    storageLocation.setDropZone(podTypePosition.getDropZone());
                    storageLocation.setPod(pod);
                    storageLocation.setxPos(podTypePosition.getLevel());
                    storageLocation.setyPos(c);
                    storageLocation.setzPos(podTypePosition.getStorageLocationType().getDepth().intValue());
                    storageLocation.setAllocation(BigDecimal.ZERO);
                    storageLocation.setAllocationState(0);
                    storageLocation.setFace(podTypePosition.getFace());
                    storageLocation.setColor(podTypePosition.getColor());
                    int binIndex = 0;
                    switch (podTypePosition.getFace()) {
                        case "A":
                            binIndex = ++binIndexA;
                            break;
                        case "B":
                            binIndex = ++binIndexB;
                            break;
                        case "C":
                            binIndex = ++binIndexC;
                            break;
                        case "D":
                            binIndex = ++binIndexD;
                            break;
                        default:
                            throw new ApiException(MasterDataException.EX_MD_POD_FACE_ERROR.toString(), podTypePosition.getFace());
                    }
                    storageLocation.setOrderIndex(binIndex);
                    if (zone.getSectionId() != null) {
                        storageLocation.setSection(sectionRepository.retrieve(zone.getSectionId()));
                    } else {
                        storageLocation.setSection(null);
                    }
                    storageLocation.setClientId(client);
                    storageLocation.setWarehouseId(warehouse);
                    storageLocationRepository.save(storageLocation);
                }
            }
        }
    }

    private void checkPodName(String warehouse, String podName) {
        Pod pod = podRepository.getByName(warehouse, podName);
        if (pod != null) {
            throw new ApiException(MasterDataException.EX_MD_POD_NAME_UNIQUE.toString(), podName);
        }
    }
}
