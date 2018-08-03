package com.mushiny.wms.outboundproblem.service.impl;

import com.mushiny.wms.outboundproblem.crud.dto.OBPCheckStateDTO;
import com.mushiny.wms.outboundproblem.crud.mapper.OBPCheckStateMapper;
import com.mushiny.wms.outboundproblem.domain.OBPCheckState;
import com.mushiny.wms.outboundproblem.repository.OBPCheckStateRepository;
import com.mushiny.wms.outboundproblem.service.OBProblemSolveService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OBProblemSolveServiceImpl implements OBProblemSolveService {

    private final OBPCheckStateMapper obPCheckStateMapper;
    private final OBPCheckStateRepository obPCheckStateRepository;

    public OBProblemSolveServiceImpl(OBPCheckStateMapper obPCheckStateMapper,
                                     OBPCheckStateRepository obPCheckStateRepository) {
        this.obPCheckStateMapper = obPCheckStateMapper;
        this.obPCheckStateRepository = obPCheckStateRepository;
    }


    @Override
    public OBPCheckStateDTO create(OBPCheckStateDTO dto) {
        OBPCheckState entity = obPCheckStateMapper.toEntity(dto);
        return obPCheckStateMapper.toDTO(obPCheckStateRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        OBPCheckState entity = obPCheckStateRepository.retrieve(id);
        obPCheckStateRepository.delete(entity);

    }

    @Override
    public OBPCheckStateDTO update(OBPCheckStateDTO dto) {

        return null;
    }

    @Override
    public OBPCheckStateDTO retrieve(String id) {
        return null;
    }

    @Override
    public List<OBPCheckStateDTO> getBySearchTerm(String searchTerm, Sort sort) {
        return null;
    }

    @Override
    public Page<OBPCheckStateDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        return null;
    }


}
