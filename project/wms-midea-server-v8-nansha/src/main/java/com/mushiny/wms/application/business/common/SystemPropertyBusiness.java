package com.mushiny.wms.application.business.common;

import com.mushiny.wms.application.domain.SystemProperty;
import com.mushiny.wms.application.domain.enums.SystemPropertyKeyEnum;
import com.mushiny.wms.application.repository.SystemPropertyRepository;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public int getDriveChargerFullValue(String warehouseId) {
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.DRIVE_CHARGER_Full_VALUE.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return Integer.valueOf(systemValue);
    }
    public int getRobotVoltageMinValue(String warehouseId) {
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.Robot_Voltage_MinValue.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return Integer.valueOf(systemValue);
    }
    public int getRobotVoltageMaxValue(String warehouseId) {
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.Robot_Voltage_MaxValue.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return Integer.valueOf(systemValue);
    }

    public int getDriveOutChargerMinValue(String warehouseId) {
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.DRIVE_OUT_CHARGER_MIN_VALUE.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return Integer.valueOf(systemValue);
    }

    public int getDriveInChargerMinValue(String warehouseId) {
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.DRIVE_IN_CHARGER_MIN_VALUE.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return Integer.valueOf(systemValue);
    }

    public int getDriveChargerConstant(String warehouseId) {
        SystemProperty systemProperty = getSystemProperty(
                SystemPropertyKeyEnum.DRIVE_CHARGER_CONSTANT.getName(), warehouseId);
        String systemValue = systemProperty.getSystemValue();
        return Integer.valueOf(systemValue);
    }

    private SystemProperty getSystemProperty(String systemKey, String warehouseId) {
        SystemProperty systemProperty = systemPropertyRepository.getBySystemKey(systemKey, warehouseId);
        if (systemProperty == null) {
            throw new ApiException(CustomException.EX_SPS_SYSTEM_PROPERTY_NOT_FOUND.toString(), systemKey);
        }
        return systemProperty;
    }
}
