package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.utils.LevelUtil;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackCellDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackWallDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.PickPackCellMapper;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackCell;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackWall;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackWallTypePosition;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.DigitalLabelRepository;
import com.mushiny.wms.masterdata.obbasics.repository.PickPackCellRepository;
import com.mushiny.wms.masterdata.obbasics.repository.PickPackWallRepository;
import com.mushiny.wms.masterdata.obbasics.repository.PickPackWallTypePositionRepository;
import com.mushiny.wms.masterdata.obbasics.service.PickPackCellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PickPackCellServiceImpl implements PickPackCellService {

    private final PickPackCellRepository pickPackCellRepository;
    private final ApplicationContext applicationContext;
    private final PickPackCellMapper pickPackCellMapper;
    private final PickPackWallRepository pickPackWallRepository;
    private final DigitalLabelRepository digitalLabelRepository;
    private final PickPackWallTypePositionRepository pickPackWallTypePositionRepository;

    @Autowired
    public PickPackCellServiceImpl(PickPackCellRepository pickPackCellRepository,
                                   ApplicationContext applicationContext,
                                   PickPackCellMapper pickPackCellMapper,
                                   PickPackWallRepository pickPackWallRepository, DigitalLabelRepository digitalLabelRepository,
                                   PickPackWallTypePositionRepository pickPackWallTypePositionRepository) {
        this.pickPackCellRepository = pickPackCellRepository;
        this.applicationContext = applicationContext;
        this.pickPackCellMapper = pickPackCellMapper;
        this.pickPackWallRepository = pickPackWallRepository;
        this.digitalLabelRepository = digitalLabelRepository;
        this.pickPackWallTypePositionRepository = pickPackWallTypePositionRepository;
    }

    @Override
    public PickPackCellDTO create(PickPackCellDTO dto) {
        PickPackCell entity = pickPackCellMapper.toEntity(dto);
        checkName(entity.getWarehouseId(), entity.getName());
        return pickPackCellMapper.toDTO(pickPackCellRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        PickPackCell entity = pickPackCellRepository.retrieve(id);
        pickPackCellRepository.delete(entity);
    }

    @Override
    public PickPackCellDTO update(PickPackCellDTO dto) {
        PickPackCell entity = pickPackCellRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkName(entity.getWarehouseId(), dto.getName());
        }
        pickPackCellMapper.updateEntityFromDTO(dto, entity);
        return pickPackCellMapper.toDTO(pickPackCellRepository.save(entity));
    }

    @Override
    public PickPackCellDTO retrieve(String id) {
        return pickPackCellMapper.toDTO(pickPackCellRepository.retrieve(id));
    }

    @Override
    public List<PickPackCellDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<PickPackCell> entities = pickPackCellRepository.getBySearchTerm(searchTerm, sort);
        return pickPackCellMapper.toDTOList(entities);
    }

    @Override
    public Page<PickPackCellDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "orderIndex"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<PickPackCell> entities = pickPackCellRepository.getBySearchTerm(searchTerm, pageable);
        return pickPackCellMapper.toDTOPage(pageable, entities);
    }

    private void checkName(String warehouse, String areaName) {
        PickPackCell pickPackCell = pickPackCellRepository.getByName(warehouse, areaName);
        if (pickPackCell != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_PICKPACK_CELL_NAME_UNIQUE.toString(), areaName);
        }
    }

    @Override
    public void updateMore(PickPackWallDTO dto) {
        PickPackWall pickPackWall = pickPackWallRepository.getByName(applicationContext.getCurrentWarehouse(), dto.getName());
        List<String> digitalLabels1 = dto.getDigitalLabel1();
        List<String> digitalLabels2 = dto.getDigitalLabel2();
        List<PickPackCell> pickPackCells = pickPackWall.getPickPackCell();
        List<PickPackWallTypePosition> pickPackWallTypePositions = pickPackWallTypePositionRepository.getByType(pickPackWall.getPickPackWallType());
        for (int i = 0; i < pickPackWallTypePositions.size(); i++) {
            for (int j = i + 1; j < pickPackWallTypePositions.size(); j++) {
                if (pickPackWallTypePositions.get(i).getOrderIndex() == pickPackWallTypePositions.get(j).getOrderIndex()) {
                    pickPackWallTypePositions.remove(pickPackWallTypePositions.get(j));
                    j--;
                }
            }
        }
        int listSize = 0;
        int orderIndex = 1;
        for (PickPackWallTypePosition pickPackWallTypePosition : pickPackWallTypePositions) {
            int columns = pickPackWallTypePosition.getPickPackFieldType().getNumberOfColumns();
            int rows = pickPackWallTypePosition.getPickPackFieldType().getNumberOfRows();
            for (int y = rows; y > 0; y--) {
                for (int x = 1; x <= columns; x++) {
                    PickPackCell pickPackCell = new PickPackCell();
                    PickPackWallTypePosition pickPackWallTypePosition2 = pickPackWallTypePositionRepository.getByOrderIndex(x, y, orderIndex, pickPackWallTypePosition.getPickPackWallType());
                    pickPackCell.setName(dto.getName() + "-" + pickPackWallTypePosition2.getOrderIndex() + "-" + LevelUtil.getLevel(y) + String.format("%02d", x));
                    for (int i = 0; i < pickPackCells.size(); i++) {
                        if (pickPackCells.get(i).getName().equals(pickPackCell.getName())) {
                            pickPackCells.get(i).setDigitalLabel1(digitalLabelRepository.findOne(digitalLabels1.get(listSize)));
                            pickPackCells.get(i).setDigitalLabel2(digitalLabelRepository.findOne(digitalLabels2.get(listSize)));
                            if (x == columns && y == 1) {
                                orderIndex++;
                            }
                            listSize++;
                            break;
                        }
                    }
                }
            }
        }
        pickPackCellRepository.save(pickPackCells);
    }
}