package com.mushiny.wms.outboundproblem.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.outboundproblem.business.utils.LocalDateTimeUtil;
import com.mushiny.wms.outboundproblem.crud.common.mapper.InboundProblemRuleMapper;
import com.mushiny.wms.outboundproblem.crud.common.mapper.ItemDataMapper;
import com.mushiny.wms.outboundproblem.crud.dto.OBProblemDTO;
import com.mushiny.wms.outboundproblem.domain.OBProblem;
import com.mushiny.wms.outboundproblem.domain.enums.ProblemType;
import com.mushiny.wms.outboundproblem.repository.common.ClientRepository;
import com.mushiny.wms.outboundproblem.repository.common.CustomerShipmentRepository;
import com.mushiny.wms.outboundproblem.repository.common.ItemDataRepository;
import com.mushiny.wms.outboundproblem.repository.common.InboundProblemRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OBProblemMapper implements BaseMapper<OBProblemDTO, OBProblem> {

    private final CustomerShipmentRepository customerShipmentRepository;
    private final ApplicationContext applicationContext;
    private final ItemDataMapper itemDataMapper;
    private final ItemDataRepository itemDataRepository;
    private final InboundProblemRuleMapper inboundProblemRuleMapper;
    private final InboundProblemRuleRepository inboundProblemRuleRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public OBProblemMapper(CustomerShipmentRepository customerShipmentRepository,
                           ApplicationContext applicationContext,
                           ItemDataMapper itemDataMapper,
                           ItemDataRepository itemDataRepository,
                           InboundProblemRuleMapper inboundProblemRuleMapper,
                           InboundProblemRuleRepository inboundProblemRuleRepository,
                           ClientRepository clientRepository) {
        this.customerShipmentRepository = customerShipmentRepository;
        this.itemDataRepository = itemDataRepository;
        this.itemDataMapper = itemDataMapper;
        this.applicationContext = applicationContext;
        this.inboundProblemRuleMapper = inboundProblemRuleMapper;
        this.inboundProblemRuleRepository = inboundProblemRuleRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public OBProblemDTO toDTO(OBProblem entity) {
        if (entity == null) {
            return null;
        }
        OBProblemDTO dto = new OBProblemDTO(entity);

        dto.setAmount(entity.getAmount());
        dto.setDescription(entity.getDescription());
        dto.setItemDataId(entity.getItemDataId());
        dto.setInboundProblemRule(inboundProblemRuleMapper.toDTO(entity.getInboundProblemRule()));
        dto.setItemData(itemDataMapper.toDTO(itemDataRepository.retrieve(entity.getItemDataId())));
        dto.setItemNo(entity.getItemNo());
        dto.setContainer(entity.getContainer());
        dto.setJobType(entity.getJobType());
        dto.setLotNo(entity.getLotNo());
        dto.setSolvedBy(entity.getSolvedBy());
        dto.setSolveAmount(entity.getSolveAmount());
        dto.setProblemStoragelocation(entity.getProblemStoragelocation());
        dto.setProblemType(entity.getProblemType());
        dto.setReportBy(entity.getReportBy());
        dto.setReportDate(DateTimeUtil.getDateFormat(LocalDateTimeUtil.LocalDateTimeToDate(entity.getReportDate()),"yyyy-MM-dd HH:mm:ss"));
        dto.setSerialNo(entity.getSerialNo());
        dto.setShipmentId(entity.getShipmentId());
        dto.setState(entity.getState());
        dto.setSkuNo(entity.getSkuNo());
        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public OBProblem toEntity(OBProblemDTO dto) {
        if (dto == null) {
            return null;
        }
        OBProblem entity = new OBProblem();

        entity.setAmount(dto.getAmount());
        entity.setDescription(dto.getDescription());
        if (dto.getItemDataId().isEmpty())
            dto.setItemDataId(null);
        entity.setItemDataId(dto.getItemDataId());
        if(dto.getProblemType().equalsIgnoreCase(ProblemType.MORE.toString())) {
            entity.setInboundProblemRule(inboundProblemRuleRepository.getByName("More_FindBin"));
        }else if(dto.getProblemType().equalsIgnoreCase(ProblemType.LESS.toString())){
            entity.setInboundProblemRule(inboundProblemRuleRepository.getByName("Less_FindBin"));
        } else{
            entity.setInboundProblemRule(null);
        }
        entity.setItemNo(dto.getItemNo());
        entity.setContainer(dto.getContainer());
        entity.setJobType(dto.getJobType());
        entity.setLotNo(dto.getLotNo());
        entity.setSolvedBy(dto.getSolvedBy());
        //entity.setSolveAmount(dto.getSolveAmount());
        entity.setSolveAmount(BigDecimal.ZERO);
        entity.setProblemStoragelocation(dto.getProblemStoragelocation());
        entity.setProblemType(dto.getProblemType());
        entity.setReportBy(dto.getReportBy());
        entity.setReportDate(dto.getReportDate());
        if (dto.getState() == null || dto.getState().isEmpty()) {
            entity.setState("unsolved");
        } else {
            entity.setState(dto.getState());
        }
        entity.setSerialNo(dto.getSerialNo());
        if (dto.getShipmentId().isEmpty())
            dto.setShipmentId(null);
        entity.setShipmentId(dto.getShipmentId());
        entity.setSkuNo(dto.getSkuNo());

        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(applicationContext.getCurrentClient());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(OBProblemDTO dto, OBProblem entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAmount(dto.getAmount());
        entity.setDescription(dto.getDescription());
        if(dto.getRule() != null ) {
            entity.setInboundProblemRule(inboundProblemRuleRepository.retrieve(dto.getRule()));
        } else {
            entity.setInboundProblemRule(null);
        }
        if(dto.getItemDataId() != null) {
            entity.setItemDataId(dto.getItemDataId());
        }
        entity.setItemNo(dto.getItemNo());
        entity.setContainer(dto.getContainer());
        entity.setJobType(dto.getJobType());
        entity.setLotNo(dto.getLotNo());
        entity.setSolvedBy(dto.getSolvedBy());
        entity.setSolveAmount(dto.getSolveAmount());
        entity.setProblemStoragelocation(dto.getProblemStoragelocation());
        entity.setProblemType(dto.getProblemType());
        entity.setReportBy(dto.getReportBy());
        entity.setReportDate(dto.getReportDate());
        entity.setState(dto.getState());
        entity.setSerialNo(dto.getSerialNo());
    }
}
