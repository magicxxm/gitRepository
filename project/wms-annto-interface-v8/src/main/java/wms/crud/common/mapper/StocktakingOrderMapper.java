package wms.crud.common.mapper;

/**
 * Created by PC-4 on 2017/8/4.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.common.context.ApplicationContext;
import wms.common.crud.mapper.BaseMapper;
import wms.crud.common.dto.StocktakingOrderDTO;
import wms.domain.StocktakingOrder;
import wms.repository.StocktakingRepository;
import wms.repository.common.StocktakingRuleRepository;

@Component
public class StocktakingOrderMapper implements BaseMapper<StocktakingOrderDTO, StocktakingOrder> {

    private final StocktakingMapper stocktakingMapper;
    private final StocktakingRepository stocktakingRepository;
    private final StocktakingRuleRepository stocktakingRuleRepository;
    private final ApplicationContext permissionsContext;

    @Autowired
    public StocktakingOrderMapper(StocktakingMapper stocktakingMapper,
                                  StocktakingRepository stocktakingRepository,
                                  StocktakingRuleRepository stocktakingRuleRepository,
                                  ApplicationContext permissionsContext) {
        this.stocktakingMapper = stocktakingMapper;
        this.stocktakingRepository = stocktakingRepository;
        this.stocktakingRuleRepository = stocktakingRuleRepository;
        this.permissionsContext = permissionsContext;
    }

    @Override
    public StocktakingOrderDTO toDTO(StocktakingOrder entity) {
        if (entity == null) {
            return null;
        }
        StocktakingOrderDTO dto = new StocktakingOrderDTO(entity);
        dto.setAreaName(entity.getAreaName());
        dto.setCountingDate(entity.getCountingDate());
        dto.setLocationName(entity.getLocationName());
        dto.setParameter(entity.getParameter());
        dto.setOperator(entity.getOperator());
        dto.setState(entity.getState());
        dto.setTimes(entity.getTimes());
        dto.setUnitLoadLabel(entity.getUnitLoadLabel());
        dto.setWarehouse(entity.getWarehouseId());
//        dto.setStocktakingRule(stocktakingRuleMapper.toDTO(entity.getStocktakingRule()));
        dto.setStocktaking(stocktakingMapper.toDTO(entity.getStocktaking()));
        return dto;
    }

    @Override
    public StocktakingOrder toEntity(StocktakingOrderDTO dto) {
        if (dto == null) {
            return null;
        }
        StocktakingOrder entity = new StocktakingOrder();
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setAreaName(dto.getAreaName());
        entity.setCountingDate(dto.getCountingDate());
        entity.setLocationName(dto.getLocationName());
        entity.setParameter(dto.getParameter());
        entity.setOperator(dto.getOperator());
        entity.setState(dto.getState());
        entity.setTimes(dto.getTimes());
        entity.setUnitLoadLabel(dto.getUnitLoadLabel());
        entity.setWarehouseId(permissionsContext.getCurrentWarehouse());
        if (dto.getStocktakingRuleId() != null) {
            entity.setStocktakingRule(stocktakingRuleRepository.retrieve(dto.getStocktakingRuleId()));
        }
        if (dto.getStocktakingId() != null) {
            entity.setStocktaking(stocktakingRepository.retrieve(dto.getStocktakingId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(StocktakingOrderDTO dto, StocktakingOrder entity) {

    }
}
