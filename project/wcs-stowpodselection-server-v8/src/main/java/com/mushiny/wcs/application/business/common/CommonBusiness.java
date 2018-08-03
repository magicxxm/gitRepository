package com.mushiny.wcs.application.business.common;

import com.mushiny.wcs.application.business.callPods.CommonCallPodBusiness;
import com.mushiny.wcs.application.business.callPods.ICQACallPodBusiness;
import com.mushiny.wcs.application.domain.Pod;
import com.mushiny.wcs.application.domain.StorageLocationType;
import com.mushiny.wcs.application.domain.WorkStation;
import com.mushiny.wcs.application.domain.enums.PodStateEnum;
import com.mushiny.wcs.application.domain.enums.TripState;
import com.mushiny.wcs.application.respository.PodRepository;
import com.mushiny.wcs.application.respository.TripPositionRepository;
import com.mushiny.wcs.application.respository.TripRepository;
import com.mushiny.wcs.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommonBusiness {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonBusiness.class);
    private final PodRepository podRepository;
    private final TripRepository tripRepository;
    private final TripPositionRepository tripPositionRepository;

    @Autowired
    public CommonBusiness(PodRepository podRepository,
                          TripRepository tripRepository,
                          TripPositionRepository tripPositionRepository) {
        this.podRepository = podRepository;
        this.tripRepository = tripRepository;
        this.tripPositionRepository=tripPositionRepository;
    }


    /**
     * 获取POD可以的体积
     */
    public BigDecimal getPodAvailableVolume(Pod pod, List<StorageLocationType> binTypeList) {
        // 获取POD所有BIN位的总体积
        BigDecimal podAllBinVolume = podRepository.sumPodAllBinVolume(pod.getId(), binTypeList);

        // 获取POD所有BIN位上商品的总体积
        BigDecimal podItemVolume = podRepository.sumPodItemVolume(pod.getId(), binTypeList);
        // 计算POD的可用体积
        if(LOGGER.isDebugEnabled())
        {
            List<String> temp=new ArrayList<>();
            for(StorageLocationType ty:binTypeList)
            {
                temp.add(ty.getId());
            }
            LOGGER.debug("计算pod{} binType{}\n总体积{} 以使用{}",pod.getName(), JSONUtil.toJSon(temp),podAllBinVolume,podItemVolume);
        }
        return podAllBinVolume.subtract(podItemVolume);
    }



    /**
     * 获取POD可以的体积
     */
    public BigDecimal getPodAvailableVolume(Pod pod, StorageLocationType binType) {
        // 获取POD所有BIN位的总体积
        BigDecimal podAllBinVolume = podRepository.sumPodAllBinVolume2(pod.getId(), binType);

        // 获取POD所有BIN位上商品的总体积
        BigDecimal podItemVolume = podRepository.sumPodItemVolume2(pod.getId(), binType);
        // 计算POD的可用体积
        if(LOGGER.isDebugEnabled())
        {

            LOGGER.debug("计算pod{} binType{}\n总体积{} 以使用{}",pod.getName(), JSONUtil.toJSon(binType),podAllBinVolume,podItemVolume);
        }
        return podAllBinVolume.subtract(podItemVolume);
    }

    /**
     * 获取POD可以的体积
     */
    public BigDecimal getPodAvailableWeight(Pod pod, StorageLocationType binType) {
        // 获取POD所有BIN位的总体积
        BigDecimal podAllBinWeight = podRepository.sumPodAllBinWeight(pod.getId(), binType);

        // 获取POD所有BIN位上商品的总体积
        BigDecimal podItemWeight = podRepository.sumPodItemWeight(pod.getId(), binType);
        // 计算POD的可用体积
        if(LOGGER.isDebugEnabled())
        {

            LOGGER.debug("计算pod{} binType{}\n总重量{} 以使用{}",pod.getName(),binType.getId() ,podAllBinWeight,podItemWeight);
        }
        return podAllBinWeight.subtract(podItemWeight);
    }

    /**
     * 获取POD可用的商品种类
     */
    public int getPodAvailableItems(Pod pod, List<StorageLocationType> binTypeList) {
        // 获取POD所有BIN位可以存放商品种类的总数量
        int podMaxItemsAmount = podRepository.sumPodMaxItemsAmount(pod.getId(), binTypeList);
        // 获取POD所有BIN位已存放商品种类的总数量
        int podItemsAmount = podRepository.countPodItemsAmount(pod.getId(), binTypeList);
        // 计算POD可以存放商品种类的总数量
        if(LOGGER.isDebugEnabled())
        {
            List<String> temp=new ArrayList<>();
            for(StorageLocationType ty:binTypeList)
            {
                temp.add(ty.getId());
            }
            LOGGER.debug("计算pod{} binType{}\n商品种类的总数量{} 已存放商品种类的总数量{}",pod.getName(), JSONUtil.toJSon(temp),podMaxItemsAmount,podItemsAmount);
        }
        return podMaxItemsAmount - podItemsAmount;
    }

    /**
     * 获取POD可用的商品种类
     */
    public int getPodAvailableItems(Pod pod, StorageLocationType binType) {
        // 获取POD所有BIN位可以存放商品种类的总数量
        int podMaxItemsAmount = podRepository.sumPodMaxItemsAmount2(pod.getId(), binType);
        // 获取POD所有BIN位已存放商品种类的总数量
        int podItemsAmount = podRepository.countPodItemsAmount2(pod.getId(), binType);
        int binCount=podRepository.countPodBinTypeAmount(pod.getId(), binType);
        int result=0;
        if(binCount!=0)
        {
            result=(podMaxItemsAmount - podItemsAmount)/binCount;
        }


        // 计算POD可以存放商品种类的总数量
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("计算pod{} binType{}\n商品种类的总数量{} 已存放商品种类的总数量{},平均{}",pod.getName(), binType.getId(),podMaxItemsAmount,podItemsAmount,result);
        }
        return result ;
    }

    /**
     * 计算POD当前面可用体积
     */
    public BigDecimal getPodFaceAvailableVolume(Pod pod,String face, List<StorageLocationType> binTypeList) {
        // 获取POD当前面所有BIN位的总体积
        BigDecimal faceAllVolume = podRepository.sumPodFacePodAllBinVolume(pod.getId(), face, binTypeList);
        // 获取POD当前面所有BIN位上商品的总体积
        BigDecimal faceVolume = podRepository.sumPodFaceItemVolume(pod.getId(), face, binTypeList);

        if(LOGGER.isDebugEnabled())
        {
            List<String> temp=new ArrayList<>();
            for(StorageLocationType ty:binTypeList)
            {
                temp.add(ty.getId());
            }
            LOGGER.debug("计算pod{} 面{}binType{}\n当前面所有BIN位的总体积{} 所有BIN位上商品的总体积{}",pod.getName(), face,JSONUtil.toJSon(temp),faceAllVolume,faceVolume);
        }
        return faceAllVolume.subtract(faceVolume);
    }

    /**
     * 计算POD当前面可用体积
     */
    public BigDecimal getPodFaceAvailableVolume(Pod pod,String face, StorageLocationType binType) {
        // 获取POD当前面所有BIN位的总体积
        BigDecimal faceAllVolume = podRepository.sumPodFacePodAllBinVolume(pod.getId(), face, binType);
        // 获取POD当前面所有BIN位上商品的总体积
        BigDecimal faceVolume = podRepository.sumPodFaceItemVolume(pod.getId(), face, binType);

        if(LOGGER.isDebugEnabled())
        {

            LOGGER.debug("计算pod{} 面{}binType{}\n当前面所有BIN位的总体积{} 所有BIN位上商品的总体积{}",pod.getName(), face,binType.getId(),faceAllVolume,faceVolume);
        }
        return faceAllVolume.subtract(faceVolume);
    }

    /**
     * 计算POD当前面可用体积
     */
    public BigDecimal getPodFaceAvailableWeight(Pod pod,String face, StorageLocationType binType) {
        // 获取POD当前面所有BIN位的总体积
        BigDecimal faceAllWeight = podRepository.sumPodFacePodAllBinWeight(pod.getId(), face, binType);
        // 获取POD当前面所有BIN位上商品的总体积
        BigDecimal faceWeight = podRepository.sumPodFaceItemWeight(pod.getId(), face, binType);

        if(LOGGER.isDebugEnabled())
        {

            LOGGER.debug("计算pod{} 面{}binType{}\n当前面BIN位的总重量{}  BIN位上商品的总重量{},可用重量{}",pod.getName(), face,binType.getId(),faceAllWeight,faceWeight,faceAllWeight.subtract(faceWeight));
        }
        return faceAllWeight.subtract(faceWeight);
    }

    /**
     * 计算POD当前面可用的商品种类
     */
    public int getPodFaceAvailableItems(Pod pod,String face, List<StorageLocationType> binTypeList) {
        // 获取POD当前面所有BIN位可以存放商品种类的总数量
        int podFaceMaxItemsAmount = podRepository.sumPodFaceMaxItemsAmount(pod.getId(),face, binTypeList);
        // 获取POD当前面所有BIN位已存放商品种类的总数量
        int podFaceItemsAmount = podRepository.countPodFaceItemsAmount(pod.getId(),face, binTypeList);
        // 计算POD当前面可以存放商品种类的总数量

        if(LOGGER.isDebugEnabled())
        {
            List<String> temp=new ArrayList<>();
            for(StorageLocationType ty:binTypeList)
            {
                temp.add(ty.getId());
            }
            LOGGER.debug("计算pod{} 面{}binType{}\n当前面所有BIN位可以存放商品种类的总数量{} 已存放商品种类的总数量{}",pod.getName(), face,JSONUtil.toJSon(temp),podFaceMaxItemsAmount,podFaceItemsAmount);
        }
        return podFaceMaxItemsAmount - podFaceItemsAmount;
    }


    /**
     * 计算POD当前面可用的商品种类
     */
    public int getPodFaceAvailableItems(Pod pod,String face, StorageLocationType binType) {
        // 获取POD当前面所有BIN位可以存放商品种类的总数量
        int podFaceMaxItemsAmount = podRepository.sumPodFaceMaxItemsAmount(pod.getId(),face, binType);
        // 获取POD当前面所有BIN位已存放商品种类的总数量
        int podFaceItemsAmount = podRepository.countPodFaceItemsAmount(pod.getId(),face, binType);
        int binAmount = podRepository.countPodFaceBinAmount(pod.getId(),face, binType);

        // 计算POD当前面可以存放商品种类的总数量
        int result=0;
        if(binAmount!=0)
        {
            result=(podFaceMaxItemsAmount - podFaceItemsAmount)/binAmount;
            if(LOGGER.isDebugEnabled())
            {

                LOGGER.debug("计算pod{} 面{}binType{}\n当前面所有BIN位可以存放商品种类的总数量{} 已存放商品种类的总数量{},平均可用数量{}",pod.getName(), face,binType.getId(),podFaceMaxItemsAmount,podFaceItemsAmount,result);
            }
        }

        return result;
    }
    /**
     * 计算POD当前面可用的商品种类
     */
    public synchronized boolean reservePod(Pod pod) {
        boolean result=false;
          if(pod.getState().equalsIgnoreCase(PodStateEnum.AVAILABLE.getName())) {
              pod.setState(PodStateEnum.RESERVED.getName());
              podRepository.saveAndFlush(pod);
              result=true;
              if (LOGGER.isDebugEnabled()) {
                  LOGGER.debug("reserved pod {} 成功", pod.getId());
              }
          }
          return  result;
    }
    /**
     * 计算此工作站已经绑定的POD数量
     */
    public int countPqaWorkStationAvailablePod(WorkStation workStation){
        List<String> tripStates = new ArrayList<>();
        tripStates.add(TripState.NEW.getName());
        tripStates.add(TripState.AVAILABLE.getName());
        long podNumber = tripRepository.countByWorkStationId(workStation.getId(),tripStates);
        return (int) podNumber;
    }
    /**
     * 计算此工作站已经绑定的POD数量
     */
    public int countWorkStationAvailablePod(WorkStation workStation){
        List<String> tripStates = new ArrayList<>();
        tripStates.add(TripState.NEW.getName());
        tripStates.add(TripState.AVAILABLE.getName());
        tripStates.add(TripState.PROCESS.getName());
        long podNumber = tripRepository.countByWorkStationId(workStation.getId(),tripStates);
        return (int) podNumber;
    }
}
