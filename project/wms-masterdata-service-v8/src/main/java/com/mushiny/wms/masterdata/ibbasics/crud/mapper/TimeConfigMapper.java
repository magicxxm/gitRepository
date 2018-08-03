package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.crud.mapper.UserMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.IBPStationDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.TimeConfigDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.UnitLoadDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStation;
import com.mushiny.wms.masterdata.ibbasics.domain.TimeConfig;
import com.mushiny.wms.masterdata.ibbasics.repository.IBPStationTypeRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationRepository;
import org.springframework.stereotype.Component;

@Component
public class TimeConfigMapper implements BaseMapper<TimeConfigDTO, TimeConfig> {

    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;


    public TimeConfigMapper(ApplicationContext applicationContext,
                            WarehouseMapper warehouseMapper) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public TimeConfigDTO toDTO(TimeConfig entity) {
        if (entity == null) {
            return null;
        }
        TimeConfigDTO dto = new TimeConfigDTO(entity);
        dto.setRefreshDay(entity.getRefreshDay());
        dto.setRefreshTime(entity.getRefreshTime());
        return dto;
    }

    @Override
    public TimeConfig toEntity(TimeConfigDTO dto) {
        return null;
    }

    @Override
    public void updateEntityFromDTO(TimeConfigDTO dto, TimeConfig entity) {

    }
}
