package wms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wms.service.Allocation;
import wms.business.AnntoBusiness;
import wms.common.crud.AccessDTO;
import wms.common.exception.ApiException;
import wms.crud.common.dto.ReplenishmentDTO;
import wms.crud.common.dto.ReplenishmentPositionDTO;
import wms.crud.common.mapper.ReplenishmentMapper;
import wms.crud.common.mapper.ReplenishmentPositionMapper;
import wms.crud.dto.AllocationConfirmDTO;
import wms.domain.Replenishment;
import wms.exception.ITFException;
import wms.repository.common.ReplenishmentRepository;

@Service
@Transactional
public class AllocationImpl implements Allocation{

    private final AnntoBusiness anntoBusiness;
    private final ReplenishmentMapper replenishmentMapper;
    private final ReplenishmentPositionMapper replenishmentPositionMapper;
    private final ReplenishmentRepository replenishmentRepository;

    @Autowired
    public AllocationImpl(ReplenishmentMapper replenishmentMapper,
                          AnntoBusiness anntoBusiness,
                          ReplenishmentPositionMapper replenishmentPositionMapper,
                          ReplenishmentRepository replenishmentRepository){
        this.replenishmentMapper = replenishmentMapper;
        this.anntoBusiness = anntoBusiness;
        this.replenishmentPositionMapper = replenishmentPositionMapper;
        this.replenishmentRepository = replenishmentRepository;
    }

    @Override
    public AccessDTO update(ReplenishmentDTO dto) {
        Replenishment entity = replenishmentMapper.toEntity(dto);
        checkOrderCode(entity.getWarehouseId(), entity.getOrdercode());
        for (ReplenishmentPositionDTO replenishmentPositionDTO : dto.getOrderItems()) {
            entity.addPosition(replenishmentPositionMapper.toEntity(replenishmentPositionDTO));
        }
        replenishmentRepository.save(entity);
        AccessDTO accessDTO = new AccessDTO();
        return accessDTO;
    }

    @Override
    public void confirm(AllocationConfirmDTO allocationConfirmDTO) {
        anntoBusiness.confirmAllocation(allocationConfirmDTO);
    }

    public void checkOrderCode(String warehouseId, String orderCode){
        Replenishment replenishment = replenishmentRepository.getByOrderCode(warehouseId,orderCode);
        if(replenishment != null){
            throw new ApiException(ITFException.EX_RELENISHMENT_ORDERCODE_UNIQUE.toString(), "1");
        }
    }
}
