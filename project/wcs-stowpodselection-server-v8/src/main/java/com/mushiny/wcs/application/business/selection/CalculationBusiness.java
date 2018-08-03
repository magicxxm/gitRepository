package com.mushiny.wcs.application.business.selection;

import com.mushiny.wcs.application.business.PodSelectionBusiness;
import com.mushiny.wcs.application.business.common.CommonBusiness;
import com.mushiny.wcs.application.business.common.SystemPropertyBusiness;
import com.mushiny.wcs.application.business.dto.SelectionPod;
import com.mushiny.wcs.application.domain.Pod;
import com.mushiny.wcs.application.domain.PodCalu;
import com.mushiny.wcs.application.domain.PodFaceCalu;
import com.mushiny.wcs.application.domain.StorageLocationType;
import com.mushiny.wcs.application.domain.enums.PodStateEnum;
import com.mushiny.wcs.application.respository.*;
import com.mushiny.wcs.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CalculationBusiness {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculationBusiness.class);
    private final SystemPropertyBusiness systemPropertyBusiness;
    private final CommonBusiness commonBusiness;
    private final PodRepository podRepository;
    private final PodCaluRepository podCaluRepository;
    private final PodFaceCaluRepository podFaceCaluRepository;
    @Autowired
    public CalculationBusiness(SystemPropertyBusiness systemPropertyBusiness,
                               CommonBusiness commonBusiness,
                               PodRepository podRepository,PodCaluRepository podCaluRepository,PodFaceCaluRepository podFaceCaluRepository) {
        this.systemPropertyBusiness = systemPropertyBusiness;
        this.commonBusiness = commonBusiness;
        this.podRepository = podRepository;
        this.podCaluRepository=podCaluRepository;
        this.podFaceCaluRepository=podFaceCaluRepository;

    }

    public List<SelectionPod> calculateStowPods(List<Pod> pods,
                                                List<StorageLocationType> binTypeList,
                                                int stationX,
                                                int stationY,
                                                String warehouseId) {
        // 获取系统设置POD体积最小值
        BigDecimal stowPodMinVolume = systemPropertyBusiness.getStowPodSelectionMinVolume(warehouseId);
        BigDecimal stowPodMinWeight = systemPropertyBusiness.getStowPodSelectionMinWeight(warehouseId);
        List<PodCalu> pc=new ArrayList<>();

        // 获取系统设置POD商品种类最小值
        int stowPodMinItems = systemPropertyBusiness.getStowPodSelectionMinItems(warehouseId);
        // 获取计算最优POD时系统设定的体积系数
        BigDecimal stowPodVolumeConstant = systemPropertyBusiness.getStowPodSelectionVolumeConstant(warehouseId);
        BigDecimal stowPodWeightConstant = systemPropertyBusiness.getStowPodSelectionWeightConstant(warehouseId);
        // 获取计算最优POD时系统设定的商品种类系数
        BigDecimal stowPodItemsConstant = systemPropertyBusiness.getStowPodSelectionItemsConstant(warehouseId);
        // 定义满足条件的POD的返回列表
        List<SelectionPod> selectionPods = new ArrayList<>();
        for (Pod pod : pods) {
            // 计算POD的可用体积

            int xPos = Math.abs(stationX - pod.getxPos());
            int yPos = Math.abs(stationY - pod.getyPos());
            BigDecimal calculationResults=BigDecimal.valueOf(xPos + yPos);
            boolean has=false;
            for(StorageLocationType st:binTypeList)
            {
                PodCalu pcTemp=new PodCalu();
                pcTemp.setPodIndex(pod.getPodIndex());
                pcTemp.setPodMinVolume(stowPodMinVolume);
                pcTemp.setPodMiWeight(stowPodMinWeight);
                pcTemp.setPodMinItems(stowPodMinItems);
                pcTemp.setPodItemsConstant(stowPodItemsConstant);
                pcTemp.setPodWeightConstant(stowPodWeightConstant);
                pcTemp.setPodVolumeConstant(stowPodVolumeConstant);
                BigDecimal podAvailableVolume = commonBusiness.getPodAvailableVolume(pod, st);
                pcTemp.setPodAvailableVolume(podAvailableVolume);
                pcTemp.setBinType(st.getName());
                pc.add(pcTemp);
                // 检查体积是否符合系统设定值
                if (podAvailableVolume.compareTo(stowPodMinVolume) < 0) {
                    if(LOGGER.isDebugEnabled()){
                        LOGGER.debug("pod{} binType{}可用体积{} 小于系统设置的{}",pod.getName(),st.getId(),podAvailableVolume,stowPodMinVolume);
                    }
                    continue;
                }

                BigDecimal podAvailableWeight = commonBusiness.getPodAvailableWeight(pod, st);
                pcTemp.setPodAvailableWeight(podAvailableWeight);
                // 检查体积是否符合系统设定值
                if (podAvailableWeight.compareTo(stowPodMinWeight) < 0) {
                    if(LOGGER.isDebugEnabled()){
                        LOGGER.debug("pod{} binType{}可用重量{} 小于系统设置的{}",pod.getName(),st.getId(),podAvailableWeight,stowPodMinWeight);
                    }
                    continue;
                }

                // 计算POD可以存放商品种类的总数量
                int podAvailableItems = commonBusiness.getPodAvailableItems(pod, st);
                pcTemp.setPodvailableItems(podAvailableItems);
                // 检查商品种类数量是否符合系统设定值
                if (podAvailableItems < stowPodMinItems) {
                    if(LOGGER.isDebugEnabled()){
                        LOGGER.debug("pod{} binType{} 存放商品种类的平均数量{} 小于系统设置的{}",pod.getName(),st.getId(),podAvailableItems,stowPodMinItems);
                    }
                    continue;
                }
                // 获取决定坐标路径


                calculationResults.subtract(stowPodVolumeConstant.multiply(podAvailableVolume))
                        .subtract(stowPodItemsConstant.multiply(BigDecimal.valueOf(podAvailableItems))).subtract(stowPodWeightConstant.multiply(podAvailableWeight));
                has=true;
                // 构建返回结果

            }
            if(has){
                SelectionPod selectionPod = new SelectionPod();
                selectionPod.setPod(pod);
                selectionPod.setCalculationResults(calculationResults);
                selectionPods.add(selectionPod);
            }


        }

        if(!CollectionUtils.isEmpty(pc))
        {
            podCaluRepository.save(pc);

        }

        return selectionPods;
    }




    /**
     * 计算POD可使用的面
     */
    public List<String> calculatePodFace(BigDecimal faceMinVolume,BigDecimal faceMinWeight,
                                         int faceMinItems,
                                         SelectionPod selectionPod,
                                         List<StorageLocationType> binTypeList) {
        List<String> availableFaces = new ArrayList<>();
        List<PodFaceCalu> podFaceCalus=new ArrayList<>();
        List<String> temp=new ArrayList<>();
        for(StorageLocationType ty:binTypeList)
        {
            temp.add(ty.getId());
        }
        // 获取可以使用的面
        String[] faces = {"A", "B", "C", "D"};
        Map<String, BigDecimal> faceMap = new HashMap<>();
        for (String face : faces) {
            BigDecimal faceWeight=BigDecimal.ZERO;
            boolean canUse=false;
            for(StorageLocationType st:binTypeList)
            {
                PodFaceCalu podFaceCalu=new PodFaceCalu();
                podFaceCalu.setBinType(st.getName());
                podFaceCalu.setPodIndex(selectionPod.getPod().getPodIndex());
                podFaceCalu.setPodFace(face);
                BigDecimal faceAvailableVolume = commonBusiness
                        .getPodFaceAvailableVolume(selectionPod.getPod(), face, st);

                BigDecimal faceAvailableWeight = commonBusiness
                        .getPodFaceAvailableWeight(selectionPod.getPod(), face, st);
                int faceAvailableItems = commonBusiness
                        .getPodFaceAvailableItems(selectionPod.getPod(), face, st);
                podFaceCalu.setPodFaceMinItems(faceMinItems);
                podFaceCalu.setPodFaceMinVolume(faceMinVolume);
                podFaceCalu.setPodFaceMiWeight(faceMinWeight);
                podFaceCalu.setPodFaceAvailableItems(faceAvailableItems);
                podFaceCalu.setPodFaceAvailableVolume(faceAvailableVolume);
                podFaceCalu.setPodFaceAvailableWeight(faceAvailableWeight);
                podFaceCalus.add(podFaceCalu);
                if(LOGGER.isDebugEnabled())
                {

                    LOGGER.debug("计算pod{} binType{}\n面{}还可以存放商品体积{} 系统要求体积{}\n" + "还可以存放商品重量{} 系统要求重量{}\n"+
                            "还可以存放商品种类的数量{} 系统要求数量{}",selectionPod.getPod().getName(), st.getId(), face,faceAvailableVolume,faceMinVolume,faceAvailableWeight,faceMinWeight,faceAvailableItems,faceMinItems);
                }
                if (faceAvailableVolume.compareTo(faceMinVolume) < 0
                        || faceAvailableItems < faceMinItems||faceAvailableWeight.compareTo(faceMinWeight)<0) {
                    continue;
                }

                canUse=true;
                faceWeight=faceWeight.add(faceAvailableWeight);

            }
           if(canUse)
           {
               faceMap.put(face, faceWeight);
           }

        }

        if(LOGGER.isDebugEnabled())
        {

            LOGGER.debug("计算pod{} binType{}可用面{}"
                    ,selectionPod.getPod().getName(), JSONUtil.toJSon(temp), JSONUtil.toJSon(faceMap));
        }
        // 锁定POD
        Pod pod = podRepository.findOne(selectionPod.getPod().getId());
        if(!CollectionUtils.isEmpty(podFaceCalus))
        {
            podFaceCaluRepository.save(podFaceCalus) ;
        }
        if(faceMap.isEmpty()||!commonBusiness.reservePod(pod))
        {
            return null;
        }

        // 根据可用体积进行排序
        faceMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(m -> availableFaces.add(m.getKey()));
        return availableFaces;
    }
}
