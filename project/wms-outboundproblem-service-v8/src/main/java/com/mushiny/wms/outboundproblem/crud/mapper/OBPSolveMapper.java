package com.mushiny.wms.outboundproblem.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.utils.ConversionUtil;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.outboundproblem.crud.common.mapper.InboundProblemRuleMapper;
import com.mushiny.wms.outboundproblem.crud.common.mapper.StorageLocationMapper;
import com.mushiny.wms.outboundproblem.business.utils.LocalDateTimeUtil;
import com.mushiny.wms.outboundproblem.crud.common.mapper.WarehouseMapper;
import com.mushiny.wms.outboundproblem.crud.dto.OBPCheckStateDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBPSolveDTO;
import com.mushiny.wms.outboundproblem.domain.OBPSolve;
import com.mushiny.wms.outboundproblem.domain.OBProblem;
import com.mushiny.wms.outboundproblem.domain.OBProblemCheck;
import com.mushiny.wms.outboundproblem.repository.OBPStationTypeRepository;
import com.mushiny.wms.outboundproblem.repository.OBProblemRepository;
import com.mushiny.wms.outboundproblem.repository.common.InboundProblemRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class OBPSolveMapper implements BaseMapper<OBPSolveDTO, OBPSolve> {

    private final OBPStationTypeRepository obpStationTypeRepository;
    private final ApplicationContext applicationContext;
    private final OBPStationTypeMapper obpStationTypeMapper;
    private final WarehouseMapper warehouseMapper;
    private final OBProblemMapper obProblemMapper;
    private final OBProblemRepository obProblemRepository;
    private final InboundProblemRuleMapper inboundProblemRuleMapper;
    private final InboundProblemRuleRepository inboundProblemRuleRepository;
    private final StorageLocationMapper storageLocationMapper;
    private final OBProblemCheckMapper obProblemCheckMapper;

    @Autowired
    public OBPSolveMapper(OBPStationTypeRepository obpStationTypeRepository,
                          ApplicationContext applicationContext,
                          OBPStationTypeMapper obpStationTypeMapper,
                          WarehouseMapper warehouseMapper,
                          OBProblemMapper obProblemMapper,
                          OBProblemRepository obProblemRepository,
                          InboundProblemRuleMapper inboundProblemRuleMapper,
                          InboundProblemRuleRepository inboundProblemRuleRepository,
                          StorageLocationMapper storageLocationMapper, OBProblemCheckMapper obProblemCheckMapper) {
        this.obpStationTypeRepository = obpStationTypeRepository;
        this.applicationContext = applicationContext;
        this.obpStationTypeMapper = obpStationTypeMapper;
        this.warehouseMapper = warehouseMapper;
        this.obProblemMapper = obProblemMapper;
        this.obProblemRepository = obProblemRepository;
        this.inboundProblemRuleMapper = inboundProblemRuleMapper;
        this.inboundProblemRuleRepository = inboundProblemRuleRepository;
        this.storageLocationMapper = storageLocationMapper;
        this.obProblemCheckMapper = obProblemCheckMapper;
    }

    @Override
    public OBPSolveDTO toDTO(OBPSolve entity) {
        if (entity == null) {
            return null;
        }
        OBPSolveDTO dto = new OBPSolveDTO(entity);

        if (entity.getId() != null) {
            if (entity.getCustomerShipment()!=null) {
                LocalDateTime exSD = entity.getCustomerShipment().getCustomerOrder().getDeliveryDate();
                dto.setShipmentNo(entity.getCustomerShipment().getShipmentNo());
                dto.setCell(null);
                if (entity.getObpCell() != null)
                    dto.setCell(entity.getObpCell().getName());

//            String id = entity.getProblemId();
                OBProblem problem = entity.getObproblem();
                if (problem != null) {
                    dto.setProblemType(problem.getProblemType());
                    dto.setReportBy(problem.getReportBy());
                    dto.setJobType(problem.getJobType());
                    dto.setReportDate(problem.getReportDate());
                    dto.setProblemStorageLocation(problem.getProblemStoragelocation());
                }
                dto.setObpsCreateDate(entity.getCreatedDate());
                dto.setExSD(exSD);
                dto.setSerialRecordType(entity.getItemData().getSerialRecordType());
                dto.setSolveDate(entity.getSolveDate());
                dto.setTimeCondition("");
                if (exSD != null) {
                    String localDateTimeExSD = DateTimeUtil.getDateFormat(LocalDateTimeUtil.LocalDateTimeToDate(exSD), "yyyy-MM-dd HH:mm:ss");
                    ZonedDateTime zonedDateTimeExSD = ConversionUtil.toZonedDateTime(localDateTimeExSD);
                    dto.setTimeCondition(DateTimeUtil.getTimeString(zonedDateTimeExSD, DateTimeUtil.getNowDateTime()));
                }
            }else {
                dto.setObProblem(obProblemMapper.toDTO(entity.getObproblem()));
                dto.setAmount(entity.getAmount());
                dto.setInboundProblemRule(inboundProblemRuleMapper.toDTO(entity.getInboundProblemRule()));
                dto.setStorageLocation(storageLocationMapper.toDTO(entity.getStorageLocation()));
                dto.setState(entity.getState());
                dto.setSolveBy(entity.getSolveBy());
                dto.setSolveDate(entity.getSolveDate());
//        dto.setClient(entity.getClientId());
                dto.setWarehouse(entity.getWarehouseId());
            }
                dto.setWarehouse(entity.getWarehouseId());
        }
        dto.setState(entity.getState());
        if(entity.getCustomerShipment()!=null) {
            dto.setShipmentNo(entity.getCustomerShipment().getShipmentNo());
        }
        return dto;
    }

    @Override
    public OBPSolve toEntity(OBPSolveDTO dto) {
        if (dto == null) {
            return null;
        }
        OBPSolve entity = new OBPSolve();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(OBPSolveDTO dto, OBPSolve entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
    }

    /*
    * InboundProblem 处理中的数据 转 InboundProblemSolveDTO
    * 问题记录数据转 问题处理记录数据，
     */
    public List<OBPSolveDTO> toSolveList(List<OBProblem> inboundProblems) {
        List<OBPSolveDTO> list = new ArrayList<>();
        for (OBProblem obProblem : inboundProblems) {
            OBPSolveDTO dto = new OBPSolveDTO();
            dto.setObProblem(obProblemMapper.toDTO(obProblem));
           dto.setState("1");
            dto.setInboundProblemRule(inboundProblemRuleMapper.toDTO(inboundProblemRuleRepository.getByName("Process")));
            dto.setAmount(obProblem.getAmount().subtract(obProblem.getSolveAmount()));
            dto.setSolveDate(LocalDateTime.now());
            dto.setSolveBy(obProblemRepository.getByUserIds(applicationContext.getCurrentUser()));
            dto.setStorageLocation(storageLocationMapper.toDTO(obProblemRepository.
                    getByProblemStorageLocationName(obProblem.getProblemStoragelocation())));
            list.add(dto);
        }
        return list;
    }

}
