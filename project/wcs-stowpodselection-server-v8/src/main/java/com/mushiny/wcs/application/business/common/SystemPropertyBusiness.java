package com.mushiny.wcs.application.business.common;

import com.mushiny.wcs.application.business.enums.SystemPropertyKeyEnum;
import com.mushiny.wcs.application.domain.SystemProperty;
import com.mushiny.wcs.application.exception.CustomException;
import com.mushiny.wcs.application.respository.SystemPropertyRepository;
import com.mushiny.wcs.common.context.ApplicationContext;
import com.mushiny.wcs.common.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SystemPropertyBusiness {

    private final SystemPropertyRepository systemPropertyRepository;

    @Autowired
    public SystemPropertyBusiness(SystemPropertyRepository systemPropertyRepository) {
        this.systemPropertyRepository = systemPropertyRepository;
    }

    public int getStowPodStationMaxPod(String warehouseId){
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.STOW_POD_STATION_MAX_POD.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return Integer.valueOf(systemValue);
    }
    public int getBinCheckStationMaxPod(String warehouseId){
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.EN_ROUTE_MAX_PODS_BINCHENK.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return Integer.valueOf(systemValue);
    }
    public int getProblemResolveStationMaxWorkLoad(String warehouseId){
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.EN_ROUTE_MAX_WORK_PS.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return Integer.valueOf(systemValue);
    }
    public int getBinCheckStationMaxWorkLoad(String warehouseId){
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.EN_ROUTE_MAX_WORK_BINCHECK.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return Integer.valueOf(systemValue);
    }
    public int getProblemResolveStationCycle(String warehouseId){
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.CYCLE_TIME_PS.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return Integer.valueOf(systemValue);
    }
    public int getProblemResolveStationMaxPod(String warehouseId){
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.EN_ROUTE_MAX_PODS_PS.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return Integer.valueOf(systemValue);
    }
    public int getBinCheckStationCycle(String warehouseId){
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.CYCLE_TIME_BINCHECK.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return Integer.valueOf(systemValue);
    }
    public BigDecimal getStowPodSelectionMinVolume(String warehouseId){
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.STOW_POD_SELECTION_MIN_VOLUME.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return new BigDecimal(systemValue);
    }

    public BigDecimal getStowPodSelectionMinWeight(String warehouseId){
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.STOW_POD_SELECTION_MIN_WEIGHT.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return new BigDecimal(systemValue);
    }

    public int getStowPodSelectionMinItems(String warehouseId){
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.STOW_POD_SELECTION_MIN_ITEMS.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return Integer.valueOf(systemValue);
    }

    public BigDecimal getStowPodFaceSelectionMinVolume(String warehouseId){
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.STOW_POD_FACE_SELECTION_MIN_VOLUME.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return new BigDecimal(systemValue);
    }
    public BigDecimal getStowPodFaceSelectionMinWeight(String warehouseId){
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.STOW_POD_FACE_SELECTION_MIN_WEIGHT.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return new BigDecimal(systemValue);
    }

    public int getStowPodFaceSelectionMinItems(String warehouseId){
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.STOW_POD_FACE_SELECTION_MIN_ITEMS.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return Integer.valueOf(systemValue);
    }

    public BigDecimal getStowPodSelectionVolumeConstant(String warehouseId){
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.STOW_POD_SELECTION_VOLUME_CONSTANT.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return new BigDecimal(systemValue);
    }

    public BigDecimal getStowPodSelectionWeightConstant(String warehouseId){
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.STOW_POD_SELECTION_WEIGHT_CONSTANT.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return new BigDecimal(systemValue);
    }

    public BigDecimal getStowPodSelectionItemsConstant(String warehouseId){
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.STOW_POD_SELECTION_ITEMS_CONSTANT.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return new BigDecimal(systemValue);
    }


    private SystemProperty getSystemProperty(String systemKey,String warehouseId){
        SystemProperty systemProperty = systemPropertyRepository.getBySystemKey(systemKey ,warehouseId);
        if(systemProperty == null){
            throw new ApiException(CustomException.EX_SPS_SYSTEM_PROPERTY_NOT_FOUND.toString(), systemKey);
        }
        return systemProperty;
    }
}
