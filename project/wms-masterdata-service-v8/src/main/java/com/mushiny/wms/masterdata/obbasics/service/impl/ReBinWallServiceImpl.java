package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinWallDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.ReBinWallMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.ReBinWallTypeMapper;
import com.mushiny.wms.masterdata.obbasics.domain.*;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinCellRepository;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinWallRepository;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinWallTypePositionRepository;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinWallTypeRepository;
import com.mushiny.wms.masterdata.obbasics.service.ReBinWallService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReBinWallServiceImpl implements ReBinWallService {

    private final ReBinWallRepository reBinWallRepository;
    private final ReBinWallMapper reBinWallMapper;
    private final ReBinWallTypeMapper reBinWallTypeMapper;
    private final ReBinWallTypeRepository reBinWallTypeRepository;
    private final ReBinWallTypePositionRepository reBinWallTypePositionRepository;
    private final ReBinCellRepository reBinCellRepository;

    private final ApplicationContext applicationContext;

    public ReBinWallServiceImpl(ReBinWallRepository reBinWallRepository,
                                ReBinWallMapper reBinWallMapper,
                                ReBinWallTypeMapper reBinWallTypeMapper,
                                ReBinWallTypeRepository reBinWallTypeRepository,
                                ReBinWallTypePositionRepository reBinWallTypePositionRepository,
                                ReBinCellRepository reBinCellRepository,
                                ApplicationContext applicationContext) {
        this.reBinWallRepository = reBinWallRepository;
        this.reBinWallMapper = reBinWallMapper;
        this.reBinWallTypeMapper = reBinWallTypeMapper;
        this.reBinWallTypeRepository = reBinWallTypeRepository;
        this.reBinWallTypePositionRepository = reBinWallTypePositionRepository;
        this.reBinCellRepository = reBinCellRepository;
        this.applicationContext = applicationContext;
    }

    @Override
    public ReBinWallDTO create(ReBinWallDTO dto) {
        ReBinWall entity = reBinWallMapper.toEntity(dto);
        checkReBinWallName(entity.getWarehouseId(), entity.getName());
        return reBinWallMapper.toDTO(reBinWallRepository.save(entity));
    }

    @Override
    public void createMore(ReBinWallDTO dto) {
        ReBinWall entity = reBinWallMapper.toEntity(dto);
        checkReBinWallName(entity.getWarehouseId(), entity.getName());
        ReBinWallType reBinWallType = reBinWallTypeRepository.retrieve(dto.getTypeId());

        ReBinWall reBinWall = new ReBinWall();
        reBinWall.setName(dto.getName());
        reBinWall.setDescription(dto.getDescription());
        reBinWall.setNumberOfRows(dto.getNumberOfRows());
        reBinWall.setNumberOfColumns(dto.getNumberOfColumns());
//        reBinWall.setState(dto.getState());
        reBinWall.setReBinWallType(reBinWallTypeRepository.retrieve(dto.getTypeId()));
        reBinWall.setWarehouseId(applicationContext.getCurrentWarehouse());
        reBinWall = reBinWallRepository.save(reBinWall);

        List<ReBinWallTypePosition> reBinWallTypePositions = reBinWallTypePositionRepository.getByType(reBinWallType);
        for (ReBinWallTypePosition reBinWallTypePosition : reBinWallTypePositions) {
            ReBinCell reBinCell = new ReBinCell();
            reBinCell.setName(dto.getName() + "-" + reBinWallTypePosition.getName());
            reBinCell.setType(reBinWallTypePosition.getReBinCellType());
            reBinCell.setxPos(reBinWallTypePosition.getxPos());
            reBinCell.setyPos(reBinWallTypePosition.getyPos());
            reBinCell.setzPos(reBinWallTypePosition.getzPos());
            reBinCell.setOrderIndex(reBinWallTypePosition.getOrderIndex());
//            reBinCell.setState("Unoccupied");
            reBinCell.setWarehouseId(applicationContext.getCurrentWarehouse());
            reBinCell.setReBinWall(reBinWall);
            reBinCellRepository.save(reBinCell);
        }
    }

    @Override
    public void delete(String id) {
        ReBinWall entity = reBinWallRepository.retrieve(id);
        reBinWallRepository.delete(entity);
    }

    @Override
    public ReBinWallDTO update(ReBinWallDTO dto) {
        ReBinWall entity = reBinWallRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkReBinWallName(entity.getWarehouseId(), dto.getName());
        }
        reBinWallMapper.updateEntityFromDTO(dto, entity);
        return reBinWallMapper.toDTO(reBinWallRepository.save(entity));
    }

    @Override
    public ReBinWallDTO retrieve(String id) {
        return reBinWallMapper.toDTO(reBinWallRepository.retrieve(id));
    }

    @Override
    public List<ReBinWallDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<ReBinWall> entities = reBinWallRepository.getNotLockList(null, sort);
        return reBinWallMapper.toDTOList(entities);
    }

    @Override
    public List<ReBinWallDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ReBinWall> entities = reBinWallRepository.getBySearchTerm(searchTerm, sort);
        return reBinWallMapper.toDTOList(entities);
    }

    @Override
    public Page<ReBinWallDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<ReBinWall> entities = reBinWallRepository.getBySearchTerm(searchTerm, pageable);
        return reBinWallMapper.toDTOPage(pageable, entities);
    }

    private void checkReBinWallName(String warehouse, String name) {
        ReBinWall reBinWall = reBinWallRepository.getByName(warehouse, name);
        if (reBinWall != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_REBIN_WALL_NAME_UNIQUE.toString(), name);
        }
    }
}
