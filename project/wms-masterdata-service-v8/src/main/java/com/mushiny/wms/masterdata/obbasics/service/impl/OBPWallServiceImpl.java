package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.utils.LevelUtil;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPWallDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.OBPWallMapper;
import com.mushiny.wms.masterdata.obbasics.domain.OBPCell;
import com.mushiny.wms.masterdata.obbasics.domain.OBPWall;
import com.mushiny.wms.masterdata.obbasics.domain.OBPWallType;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.*;
import com.mushiny.wms.masterdata.obbasics.service.OBPWallService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OBPWallServiceImpl implements OBPWallService {

    private final OBPWallRepository obpWallRepository;
    private final OBPWallMapper obpWallMapper;
    private final OBPWallTypeRepository obpWallTypeRepository;

    private final OBPCellRepository obpCellRepository;
    private final OBPCellTypeRepository obpCellTypeRepository;
    private final ApplicationContext applicationContext;

    public OBPWallServiceImpl(OBPWallRepository obpWallRepository,
                              OBPWallMapper obpWallMapper,
                              OBPWallTypeRepository obpWallTypeRepository,
                              OBPCellRepository obpCellRepository,
                              OBPCellTypeRepository obpCellTypeRepository,
                              ApplicationContext applicationContext) {
        this.obpWallRepository = obpWallRepository;
        this.obpWallMapper = obpWallMapper;
        this.obpWallTypeRepository = obpWallTypeRepository;
        this.obpCellRepository = obpCellRepository;
        this.obpCellTypeRepository = obpCellTypeRepository;
        this.applicationContext = applicationContext;
    }

    @Override
    public OBPWallDTO create(OBPWallDTO dto) {
        return null;
    }

    @Override
    public void createMore(OBPWallDTO dto) {
        OBPWall entity = obpWallMapper.toEntity(dto);
        checkOBPWallName(entity.getWarehouseId(), entity.getName());
        OBPWallType obpWallType = obpWallTypeRepository.retrieve(dto.getTypeId());

        OBPWall obpWall = new OBPWall();
        obpWall.setName(dto.getName());
        obpWall.setDescription(dto.getDescription());
        obpWall.setNumberOfRows(dto.getNumberOfRows());
        obpWall.setNumberOfColumns(dto.getNumberOfColumns());
        if (dto.getState() != null && dto.getState().length() > 0) {
            obpWall.setState(dto.getState());
        } else {
            obpWall.setState("unoccupied");
        }

        obpWall.setObpWallType(obpWallTypeRepository.retrieve(dto.getTypeId()));
        obpWall.setWarehouseId(applicationContext.getCurrentWarehouse());
        obpWall = obpWallRepository.save(obpWall);

        int orderIndex = 1;
        //取FieldType下行、列进行循环生成X、Y坐标
        int columns = obpWall.getNumberOfColumns();
        int rows = obpWall.getNumberOfRows();
        for (int x = 1; x <= columns; x++) {
            for (int y = 1; y <= rows; y++) {
                OBPCell obpCell = new OBPCell();
                obpCell.setName(dto.getName() + "-" + LevelUtil.getLevel(y) + String.format("%02d", x));
                obpCell.setObpWall(obpWall);
                obpCell.setxPos(x);
                obpCell.setyPos(y);
                obpCell.setzPos(1);
                obpCell.setOrderIndex(orderIndex);
                obpCell.setState("unoccupied");
                orderIndex++;
                obpCell.setWarehouseId(applicationContext.getCurrentWarehouse());
                obpCell.setObpCellType(obpCellTypeRepository.retrieve(dto.getObpCellTypeId()));
                obpCellRepository.save(obpCell);
            }
        }

    }

    @Override
    public void delete(String id) {
        OBPWall entity = obpWallRepository.retrieve(id);
        obpWallRepository.delete(entity);
    }

    @Override
    public OBPWallDTO update(OBPWallDTO dto) {
        OBPWall entity = obpWallRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkOBPWallName(entity.getWarehouseId(), dto.getName());
        }
        obpWallMapper.updateEntityFromDTO(dto, entity);
        return obpWallMapper.toDTO(obpWallRepository.save(entity));
    }

    @Override
    public OBPWallDTO retrieve(String id) {
        return obpWallMapper.toDTO(obpWallRepository.retrieve(id));
    }

    @Override
    public List<OBPWallDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<OBPWall> entities = obpWallRepository.getNotLockList(null, sort);
        return obpWallMapper.toDTOList(entities);
    }

    @Override
    public List<OBPWallDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<OBPWall> entities = obpWallRepository.getBySearchTerm(searchTerm, sort);
        return obpWallMapper.toDTOList(entities);
    }

    @Override
    public Page<OBPWallDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<OBPWall> entities = obpWallRepository.getBySearchTerm(searchTerm, pageable);
        return obpWallMapper.toDTOPage(pageable, entities);
    }

    private void checkOBPWallName(String warehouse, String name) {
        OBPWall obpWall = obpWallRepository.getByName(warehouse, name);
        if (obpWall != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_REBIN_WALL_NAME_UNIQUE.toString(), name);
        }
    }
}
