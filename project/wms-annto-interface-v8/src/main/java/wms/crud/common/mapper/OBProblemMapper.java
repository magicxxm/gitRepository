package wms.crud.common.mapper;

import org.springframework.stereotype.Component;
import wms.business.dto.ProblemDTO;
import wms.domain.OBProblem;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class OBProblemMapper{

    public OBProblem toEntity(ProblemDTO dto, LocalDateTime ldt) {
        if (dto == null) {
            return null;
        }
        OBProblem entity = new OBProblem();

        entity.setAmount(new BigDecimal(dto.getAmount()));
        entity.setDescription(dto.getDescription());
        entity.setItemDataId(dto.getItemDataId());
//        if(dto.getProblemType().equalsIgnoreCase(ProblemType.MORE.toString())) {
//            entity.setInboundProblemRule(inboundProblemRuleRepository.getByName("More_FindBin"));
//        }else if(dto.getProblemType().equalsIgnoreCase(ProblemType.LESS.toString())){
//            entity.setInboundProblemRule(inboundProblemRuleRepository.getByName("Less_FindBin"));

        entity.setItemNo(dto.getItemCode());
        entity.setContainer(dto.getContainerCode());
        entity.setJobType(dto.getJobType());
        entity.setLotNo(dto.getLotNo());
        //entity.setSolveAmount(dto.getSolveAmount());
        entity.setSolveAmount(BigDecimal.ZERO);
        entity.setProblemStoragelocation(dto.getProblemStoragelocation());
        entity.setProblemType(dto.getProblemType());
        entity.setReportBy(dto.getReportBy());
        entity.setReportDate(ldt);
        entity.setState("unsolved");
        entity.setSerialNo(dto.getSerialNo());
        if (dto.getShipmentId().isEmpty())
            dto.setShipmentId(null);
        entity.setShipmentId(dto.getShipmentId());
        entity.setSkuNo(dto.getBarCode());

//        applicationContext.isCurrentClient(dto.getClientId());
//        entity.setClientId(applicationContext.getCurrentClient());
//        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }
}
