package com.mushiny.wcs.application.business.selection;

import com.mushiny.wcs.application.business.common.CommonBusiness;
import com.mushiny.wcs.application.business.common.SystemPropertyBusiness;
import com.mushiny.wcs.application.business.dto.SelectionPod;
import com.mushiny.wcs.application.domain.Pod;
import com.mushiny.wcs.application.domain.StorageLocationType;
import com.mushiny.wcs.application.respository.PodRepository;
import com.mushiny.wcs.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CalculationBusiness2 {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculationBusiness2.class);
    private final SystemPropertyBusiness systemPropertyBusiness;
    private final CommonBusiness commonBusiness;
    private final PodRepository podRepository;

    @Autowired
    public CalculationBusiness2(SystemPropertyBusiness systemPropertyBusiness,
                                CommonBusiness commonBusiness,
                                PodRepository podRepository) {
        this.systemPropertyBusiness = systemPropertyBusiness;
        this.commonBusiness = commonBusiness;
        this.podRepository = podRepository;
    }

    public List<SelectionPod> calculateStowPods(List<Pod> pods,
                                                List<StorageLocationType> binTypeList,
                                                int stationX,
                                                int stationY,
                                                String warehouseId) {
        // 获取系统设置POD体积最小值
        BigDecimal stowPodMinVolume = systemPropertyBusiness.getStowPodSelectionMinVolume(warehouseId);
        // 获取系统设置POD商品种类最小值
        int stowPodMinItems = systemPropertyBusiness.getStowPodSelectionMinItems(warehouseId);
        // 获取计算最优POD时系统设定的体积系数
        BigDecimal stowPodVolumeConstant = systemPropertyBusiness.getStowPodSelectionVolumeConstant(warehouseId);
        // 获取计算最优POD时系统设定的商品种类系数
        BigDecimal stowPodItemsConstant = systemPropertyBusiness.getStowPodSelectionItemsConstant(warehouseId);
        // 定义满足条件的POD的返回列表
        List<SelectionPod> selectionPods = new ArrayList<>();
        for (Pod pod : pods) {
            // 计算POD的可用体积
            BigDecimal podAvailableVolume = commonBusiness.getPodAvailableVolume(pod, binTypeList);
            // 检查体积是否符合系统设定值
            if (podAvailableVolume.compareTo(stowPodMinVolume) < 0) {
                if(LOGGER.isDebugEnabled()){
                    LOGGER.debug("pod{}可用体积{} 小于系统设置的{}",pod.getName(),podAvailableVolume,stowPodMinVolume);
                }
                continue;
            }
            // 计算POD可以存放商品种类的总数量
            int podAvailableItems = commonBusiness.getPodAvailableItems(pod, binTypeList);
            // 检查商品种类数量是否符合系统设定值
            if (podAvailableItems < stowPodMinItems) {
                if(LOGGER.isDebugEnabled()){
                    LOGGER.debug("pod{}存放商品种类的总数量{} 小于系统设置的{}",pod.getName(),podAvailableVolume,stowPodMinItems);
                }
                continue;
            }
            // 获取决定坐标路径
            int xPos = Math.abs(stationX - pod.getxPos());
            int yPos = Math.abs(stationY - pod.getyPos());
            // 计算POD的最优系数
            BigDecimal calculationResults = BigDecimal.valueOf(xPos + yPos)
                    .subtract(stowPodVolumeConstant.multiply(podAvailableVolume))
                    .subtract(stowPodItemsConstant.multiply(BigDecimal.valueOf(podAvailableItems)));
            // 构建返回结果
            SelectionPod selectionPod = new SelectionPod();
            selectionPod.setPod(pod);
            selectionPod.setCalculationResults(calculationResults);
            selectionPod.setPodAvailableItems(podAvailableItems);
            selectionPod.setPodAvailableVolume(podAvailableVolume);
            selectionPods.add(selectionPod);
        }
        return selectionPods;
    }

    /**
     * 计算POD可使用的面
     */
    public List<String> calculatePodFace(BigDecimal faceMinVolume,
                                         int faceMinItems,
                                         SelectionPod selectionPod,
                                         List<StorageLocationType> binTypeList) {
        List<String> availableFaces = new ArrayList<>();
        List<String> temp=new ArrayList<>();
        for(StorageLocationType ty:binTypeList)
        {
            temp.add(ty.getId());
        }
        // 获取可以使用的面
        String[] faces = {"A", "B", "C", "D"};
        Map<String, BigDecimal> faceMap = new HashMap<>();
        for (String face : faces) {
            BigDecimal faceAvailableVolume = commonBusiness
                    .getPodFaceAvailableVolume(selectionPod.getPod(), face, binTypeList);
            int faceAvailableItems = commonBusiness
                    .getPodFaceAvailableItems(selectionPod.getPod(), face, binTypeList);
            if(LOGGER.isDebugEnabled())
            {

                LOGGER.debug("计算pod{} binType{}\n面{}还可以存放商品体积{} 系统要求体积{}\n" +
                        "还可以存放商品种类的数量{} 系统要求数量{}",selectionPod.getPod().getName(), JSONUtil.toJSon(temp), face,faceAvailableVolume,faceMinVolume,faceAvailableItems,faceMinItems);
            }
            if (faceAvailableVolume.compareTo(faceMinVolume) < 0
                    || faceAvailableItems < faceMinItems) {
                continue;
            }
            faceMap.put(face, faceAvailableVolume);
        }

        if(LOGGER.isDebugEnabled())
        {

            LOGGER.debug("计算pod{} binType{}可用面{}"
                    ,selectionPod.getPod().getName(), JSONUtil.toJSon(temp), JSONUtil.toJSon(faceMap));
        }
        // 锁定POD
        Pod pod = podRepository.findOne(selectionPod.getPod().getId());

        if(faceMap.isEmpty()||!commonBusiness.reservePod(pod))
        {
            return null;
        }
       /* if ((!faceMap.isEmpty())
                && pod.getState().equalsIgnoreCase(PodStateEnum.AVAILABLE.getName())) {
            pod.setState(PodStateEnum.RESERVED.getName());
            podRepository.saveAndFlush(pod);
        } else {
            return null;
        }*/
        // 根据可用体积进行排序
        faceMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(m -> availableFaces.add(m.getKey()));
        return availableFaces;
    }
}
