//package com.mushiny.wms.masterdata.obbasics.crud.mapper;
//
//import com.mushiny.wms.common.context.ApplicationContext;
//import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class PickingCateGoryRulePositionMapper implements BaseMapper<PickingCateGoryRulePositionDTO, PickingCateGoryRulePosition> {
//
//    private final ApplicationContext applicationContext;
//    private final WarehouseMapper warehouseMapper;
//    private final PickingCateGoryRuleMapper pickingCateGoryRuleMapper;
//    private final PickingCateGoryRuleRepository pickingCateGoryRuleRepository;
//
//    @Autowired
//    public PickingCateGoryRulePositionMapper(ApplicationContext applicationContext,
//                                             WarehouseMapper warehouseMapper,
//                                             PickingCateGoryRuleMapper pickingCateGoryRuleMapper,
//                                             PickingCateGoryRuleRepository pickingCateGoryRuleRepository) {
//        this.applicationContext = applicationContext;
//        this.warehouseMapper = warehouseMapper;
//        this.pickingCateGoryRuleMapper = pickingCateGoryRuleMapper;
//        this.pickingCateGoryRuleRepository = pickingCateGoryRuleRepository;
//    }
//
//    @Override
//    public PickingCateGoryRulePositionDTO toDTO(PickingCateGoryRulePosition entity) {
//        if (entity == null) {
//            return null;
//        }
//        PickingCateGoryRulePositionDTO dto = new PickingCateGoryRulePositionDTO(entity);
//        dto.setPositionNo(entity.getPositionNo());
//        dto.setOrderIndex(entity.getOrderIndex());
//        dto.setOperator(entity.getOperator());
//        dto.setPickingCateGoryRule(pickingCateGoryRuleMapper.toDTO(entity.getPickingCateGoryRule()));
//
//        return dto;
//    }
//
//    @Override
//    public PickingCateGoryRulePosition toEntity(PickingCateGoryRulePositionDTO dto) {
//        if (dto == null) {
//            return null;
//        }
//        PickingCateGoryRulePosition entity = new PickingCateGoryRulePosition();
//
//        entity.setId(dto.getId());
//        entity.setAdditionalContent(dto.getAdditionalContent());
//        entity.setPositionNo(dto.getPositionNo());
//        entity.setOperator(dto.getOperator());
//        entity.setOrderIndex(dto.getOrderIndex());
//        if (dto.getPickingCateGoryRuleId() != null) {
//            entity.setPickingCateGoryRule(pickingCateGoryRuleRepository.retrieve(dto.getPickingCateGoryRuleId()));
//        }
//
//        return entity;
//    }
//
//    @Override
//    public void updateEntityFromDTO(PickingCateGoryRulePositionDTO dto, PickingCateGoryRulePosition entity) {
//    }
//}
