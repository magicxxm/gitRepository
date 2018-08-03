package com.mushiny.wcs.application.business;

import com.mushiny.wcs.application.business.common.BuildEntityBusiness;
import com.mushiny.wcs.application.business.dto.SelectionPod;
import com.mushiny.wcs.application.business.selection.CalculationBusiness;
import com.mushiny.wcs.application.business.common.CommonBusiness;
import com.mushiny.wcs.application.business.common.SystemPropertyBusiness;
import com.mushiny.wcs.application.domain.*;
import com.mushiny.wcs.application.domain.Map;
import com.mushiny.wcs.application.exception.CustomException;
import com.mushiny.wcs.application.respository.MapNodeRepository;
import com.mushiny.wcs.application.respository.MapRepository;
import com.mushiny.wcs.application.respository.PodFaceCaluRepository;
import com.mushiny.wcs.application.respository.PodRepository;
import com.mushiny.wcs.common.exception.ApiException;
import com.mushiny.wcs.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;

@Component
public class PodSelectionBusiness {
    private static final Logger LOGGER = LoggerFactory.getLogger(PodSelectionBusiness.class);
    private final PodRepository podRepository;
    private final CalculationBusiness calculationBusiness;
    private final SystemPropertyBusiness systemPropertyBusiness;
    private final CommonBusiness commonBusiness;
    private final BuildEntityBusiness buildEntityBusiness;
    private final MapRepository mapRepository;
    private final MapNodeRepository mapNodeRepository;

    @Autowired
    public PodSelectionBusiness(PodRepository podRepository,
                                CalculationBusiness calculationBusiness,
                                SystemPropertyBusiness systemPropertyBusiness,
                                CommonBusiness commonBusiness,
                                BuildEntityBusiness buildEntityBusiness,MapRepository mapRepository,MapNodeRepository mapNodeRepository) {
        this.podRepository = podRepository;
        this.calculationBusiness = calculationBusiness;
        this.systemPropertyBusiness = systemPropertyBusiness;
        this.commonBusiness = commonBusiness;
        this.buildEntityBusiness = buildEntityBusiness;
        this.mapNodeRepository=mapNodeRepository;
        this.mapRepository=mapRepository;

    }

    /**
     * 呼叫上架POD
     */
    public void callStowPods(StowStation stowStation,
                             ReceiveStation receiveStation,
                             List<StorageLocationType> storageLocationTypes,
                             WorkStation workStation,
                             int podAmount) {
        String warehouseId = workStation.getWarehouseId();
        // 检查是否有Pod包含StorageLocationTypes
        List<Pod> pods = podRepository.getPodByBinTypeList(storageLocationTypes,workStation.getSectionId());
        if (pods.isEmpty()) {
           if( LOGGER.isDebugEnabled()){
               List<String> typeIds=new ArrayList<>();

               for(StorageLocationType tt:storageLocationTypes)
               {
                   typeIds.add(tt.getId());
               }
               LOGGER.debug("未找到section {} binType{},对应的pod",workStation.getSectionId(),JSONUtil.toJSon(typeIds) );
            }
            throw new ApiException(CustomException.EX_SPS_BIN_TYPES_NOT_FOUND_POD.toString());
        }
        // 获取工作站的坐标
        int stationX = workStation.getxPos();
        int stationY = workStation.getyPos();
        // 获取所有符合条件的POD
        List<SelectionPod> selectionPods = calculationBusiness
                .calculateStowPods(pods, storageLocationTypes, stationX, stationY, warehouseId);
        if (selectionPods.isEmpty()) {
            if(LOGGER.isDebugEnabled())
            {
                List<String> typeIds=new ArrayList<>();

                for(Pod tt:pods)
                {
                    typeIds.add(tt.getName());
                }
                LOGGER.debug("POD{}不符合条件",JSONUtil.toJSon(typeIds));
            }
           return;
        }
        // 处理筛选结果，呼叫合适的POD
        // 根据计算效率进行排序
        selectionPods.sort(Comparator.comparing(SelectionPod::getCalculationResults));

        Map map=mapRepository.getBySectionIdAndWarehouseId(workStation.getSectionId(),workStation.getWarehouseId());
        if(ObjectUtils.isEmpty(map))
        {
            LOGGER.debug("未找到section {} wareHouse{},对应的map",workStation.getSectionId(),workStation.getWarehouseId() );
           return;
        }
        List<MapNode> mapNodes= mapNodeRepository.getInsideStorageNode(map.getId());



        if(CollectionUtils.isEmpty(mapNodes))
        {
            LOGGER.debug("未找到map {}对应的存储点",map.getId() );

        }
        java.util.Map<Integer, MapNode> cash=new HashMap<>();

        for(MapNode mn:mapNodes)
        {
            cash.put(mn.getAddressCodeId(),mn);
        }
        List<SelectionPod> inside=new ArrayList<>();
        List<SelectionPod> outside=new ArrayList<>();

        for(SelectionPod sp:selectionPods)
        {
            if(ObjectUtils.isEmpty(cash.get(sp.getPod().getPlaceMark())))
            {
                outside.add(sp);

            }else{
                inside.add(sp);
            }
        }
        // 获取POD面最小体积
        BigDecimal faceMinVolume = systemPropertyBusiness.getStowPodFaceSelectionMinVolume(warehouseId);
        BigDecimal faceMinWeight = systemPropertyBusiness.getStowPodFaceSelectionMinWeight(warehouseId);


        // 获取POD面最小商品种类数量
        int faceMinItems = systemPropertyBusiness.getStowPodFaceSelectionMinItems(warehouseId);
        int tripCount=0;
        tripCount=buildFace(outside,faceMinItems,faceMinVolume,faceMinWeight,storageLocationTypes,podAmount,stowStation,workStation,receiveStation,tripCount);
        if(tripCount<podAmount)
        {
            buildFace(inside,faceMinItems,faceMinVolume,faceMinWeight,storageLocationTypes,podAmount,stowStation,workStation,receiveStation,tripCount);
        }

    }


    private int buildFace(List<SelectionPod> selectionPods,int faceMinItems,BigDecimal faceMinVolume,BigDecimal faceMinWeight,List<StorageLocationType> storageLocationTypes,int podAmount,StowStation stowStation,WorkStation workStation, ReceiveStation receiveStation,int tripCount){
        int countPod = tripCount;

        for (SelectionPod selectionPod : selectionPods) {
            // 获取POD可使用的面
            List<String> availableFaces = calculationBusiness
                    .calculatePodFace(faceMinVolume,faceMinWeight, faceMinItems, selectionPod, storageLocationTypes);
            if (availableFaces.isEmpty()) {
                if(LOGGER.isDebugEnabled())
                {

                    LOGGER.debug("POD{}无可用的面",selectionPod.getPod().getName());
                }
                continue;
            }
            buildEntityBusiness.buildTrip(selectionPod.getPod(), workStation, availableFaces);
            if (stowStation != null) {
                buildEntityBusiness.buildEnRoutePod(selectionPod.getPod(), stowStation);
            } else if (receiveStation != null) {
                buildEntityBusiness.buildEnRoutePod(selectionPod.getPod(), receiveStation);
            } else {
                throw new ApiException(CustomException.EX_SPS_STATION_NOT_FOUND.toString());
            }

            countPod++;
            if (countPod >podAmount-1) {
                break;
            }
        }
        return countPod;

    }
}
