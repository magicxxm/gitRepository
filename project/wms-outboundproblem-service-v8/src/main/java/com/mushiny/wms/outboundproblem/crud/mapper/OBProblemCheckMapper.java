package com.mushiny.wms.outboundproblem.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.outboundproblem.business.utils.LocalDateTimeUtil;
import com.mushiny.wms.outboundproblem.crud.common.mapper.InboundProblemRuleMapper;
import com.mushiny.wms.outboundproblem.crud.common.mapper.ItemDataMapper;
import com.mushiny.wms.outboundproblem.crud.dto.OBProblemCheckDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBProblemDTO;
import com.mushiny.wms.outboundproblem.domain.OBProblemCheck;
import com.mushiny.wms.outboundproblem.domain.common.ItemData;
import com.mushiny.wms.outboundproblem.domain.enums.ProblemType;
import com.mushiny.wms.outboundproblem.repository.common.ClientRepository;
import com.mushiny.wms.outboundproblem.repository.common.InboundProblemRuleRepository;
import com.mushiny.wms.outboundproblem.repository.common.ItemDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OBProblemCheckMapper implements BaseMapper<OBProblemCheckDTO, OBProblemCheck> {

    private final ApplicationContext applicationContext;
    private final ItemDataMapper itemDataMapper;
    private final InboundProblemRuleMapper inboundProblemRuleMapper;
    private final InboundProblemRuleRepository inboundProblemRuleRepository;
    private final ClientRepository clientRepository;
    private final ItemDataRepository itemDataRepository;


    @Autowired
    public OBProblemCheckMapper(ApplicationContext applicationContext,
                                ItemDataMapper itemDataMapper,
                                InboundProblemRuleMapper inboundProblemRuleMapper,
                                InboundProblemRuleRepository inboundProblemRuleRepository,
                                ClientRepository clientRepository, ItemDataRepository itemDataRepository) {
        this.itemDataMapper = itemDataMapper;
        this.applicationContext = applicationContext;
        this.inboundProblemRuleMapper = inboundProblemRuleMapper;
        this.inboundProblemRuleRepository = inboundProblemRuleRepository;
        this.clientRepository = clientRepository;
        this.itemDataRepository = itemDataRepository;
    }

    @Override
    public OBProblemCheckDTO toDTO(OBProblemCheck entity) {
        if (entity == null) {
            return null;
        }
        OBProblemCheckDTO dto = new OBProblemCheckDTO(entity);
        dto.setAmount(entity.getAmount());
        dto.setDescription(entity.getDescription());
        dto.setInboundProblemRule(inboundProblemRuleMapper.toDTO(entity.getInboundProblemRule()));
        dto.setItemData(itemDataMapper.toDTO(entity.getItemData()));
        dto.setItemNo(entity.getItemNo());
        dto.setJobType(entity.getJobType());
        dto.setLotNo(entity.getLotNo());
        dto.setSolvedBy(entity.getSolvedBy());
        dto.setSolveAmount(entity.getSolveAmount());
        dto.setProblemStoragelocation(entity.getProblemStoragelocation());
        dto.setProblemType(entity.getProblemType());
        dto.setReportBy(entity.getReportBy());
        dto.setReportDate(DateTimeUtil.getDateFormat(LocalDateTimeUtil.LocalDateTimeToDate(entity.getReportDate()),"yyyy-MM-dd HH:mm:ss"));
        dto.setState(entity.getState());
        dto.setSkuNo(entity.getSkuNo());
     //   dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public OBProblemCheck toEntity(OBProblemCheckDTO dto) {
        if (dto == null) {
            return null;
        }
        OBProblemCheck entity = new OBProblemCheck();

        entity.setAmount(dto.getAmount());
        entity.setDescription(dto.getDescription());

        if (dto.getItemDataId() != null) {
            entity.setItemData(itemDataRepository.findOne(dto.getItemDataId()));
        } else {
            entity.setItemData(null);
        }
        if(dto.getProblemType().equalsIgnoreCase(ProblemType.MORE.toString())) {
            entity.setInboundProblemRule(inboundProblemRuleRepository.getByName("More_FindBin"));
        }else if(dto.getProblemType().equalsIgnoreCase(ProblemType.LESS.toString())){
            entity.setInboundProblemRule(inboundProblemRuleRepository.getByName("Less_FindBin"));
        } else{
            entity.setInboundProblemRule(null);
        }
        entity.setItemNo(dto.getItemNo());
        entity.setJobType(dto.getJobType());
        entity.setLotNo(dto.getLotNo());
        entity.setSolvedBy(dto.getSolvedBy());
        entity.setSolveAmount(dto.getSolveAmount());
        entity.setProblemStoragelocation(dto.getProblemStoragelocation());
        entity.setProblemType(dto.getProblemType());
        entity.setReportBy(dto.getReportBy());
        entity.setReportDate(dto.getReportDate());
        if (dto.getState() == null || dto.getState().isEmpty()) {
            entity.setState("unsolved");
        } else {
            entity.setState(dto.getState());
        }
        entity.setSkuNo(dto.getSkuNo());

        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(applicationContext.getCurrentClient());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(OBProblemCheckDTO dto, OBProblemCheck entity) {
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
            entity.setItemData(itemDataRepository.retrieve(dto.getItemDataId()));
        }
        entity.setItemNo(dto.getItemNo());
        entity.setJobType(dto.getJobType());
        entity.setLotNo(dto.getLotNo());
        entity.setSolvedBy(dto.getSolvedBy());
        entity.setSolveAmount(dto.getSolveAmount());
        entity.setProblemStoragelocation(dto.getProblemStoragelocation());
        entity.setProblemType(dto.getProblemType());
        entity.setReportBy(dto.getReportBy());
        entity.setReportDate(dto.getReportDate());
        entity.setState(dto.getState());
    }

    public OBProblemCheckDTO toProblemDTO(OBProblemDTO dto){
        if(dto==null){
            return null;
        }
        OBProblemCheckDTO obProblemCheckDTO=new OBProblemCheckDTO();

        obProblemCheckDTO.setAmount(dto.getAmount());
        obProblemCheckDTO.setDescription(dto.getDescription());
        obProblemCheckDTO.setInboundProblemRule(dto.getInboundProblemRule());
        ItemData entity = itemDataRepository.retrieve(dto.getItemDataId());
        obProblemCheckDTO.setItemData(itemDataMapper.toDTO(entity));
        obProblemCheckDTO.setItemDataId(dto.getItemDataId());
        obProblemCheckDTO.setItemNo(dto.getItemNo());
        obProblemCheckDTO.setJobType(dto.getJobType());
        obProblemCheckDTO.setLotNo(dto.getLotNo());
        obProblemCheckDTO.setSolvedBy(dto.getSolvedBy());
        obProblemCheckDTO.setSolveAmount(dto.getSolveAmount());
        obProblemCheckDTO.setProblemStoragelocation(dto.getProblemStoragelocation());
        obProblemCheckDTO.setProblemType(dto.getProblemType());
        obProblemCheckDTO.setReportBy(dto.getReportBy());
        obProblemCheckDTO.setReportDate(DateTimeUtil.getDateFormat(LocalDateTimeUtil.LocalDateTimeToDate(dto.getReportDate()),"yyyy-MM-dd HH:mm:ss"));
        obProblemCheckDTO.setState(dto.getState());
        obProblemCheckDTO.setSkuNo(dto.getSkuNo());
        obProblemCheckDTO.setClientId(applicationContext.getCurrentClient());
        obProblemCheckDTO.setWarehouseId(dto.getWarehouseId());
        return obProblemCheckDTO;
    }
}
