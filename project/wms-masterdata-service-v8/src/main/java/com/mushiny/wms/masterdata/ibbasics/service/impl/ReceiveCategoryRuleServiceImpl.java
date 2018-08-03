package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveCategoryRuleDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.ReceiveCategoryRuleMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveCategoryRule;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.ReceiveCategoryRuleRepository;
import com.mushiny.wms.masterdata.ibbasics.service.ReceiveCategoryRuleService;
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
public class ReceiveCategoryRuleServiceImpl implements ReceiveCategoryRuleService {

    private final ReceiveCategoryRuleRepository receivingCategoryRuleRepository;
    private final ReceiveCategoryRuleMapper receivingCategoryRuleMapper;

    @Autowired
    public ReceiveCategoryRuleServiceImpl(ReceiveCategoryRuleRepository receivingCategoryRuleRepository,
                                          ReceiveCategoryRuleMapper receivingCategoryRuleMapper) {
        this.receivingCategoryRuleRepository = receivingCategoryRuleRepository;
        this.receivingCategoryRuleMapper = receivingCategoryRuleMapper;
    }

    @Override
    public ReceiveCategoryRuleDTO create(ReceiveCategoryRuleDTO dto) {
        return null;
    }

    @Override
    public void delete(String id) {
    }

    @Override
    public ReceiveCategoryRuleDTO update(ReceiveCategoryRuleDTO dto) {
        ReceiveCategoryRule entity = receivingCategoryRuleRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkRuleName(dto.getName());
        }
        receivingCategoryRuleMapper.updateEntityFromDTO(dto, entity);
        return receivingCategoryRuleMapper.toDTO(receivingCategoryRuleRepository.save(entity));
    }

    @Override
    public ReceiveCategoryRuleDTO retrieve(String id) {
        return receivingCategoryRuleMapper.toDTO(receivingCategoryRuleRepository.retrieve(id));
    }

    @Override
    public List<ReceiveCategoryRuleDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ReceiveCategoryRule> entities = receivingCategoryRuleRepository.getBySearchTerm(searchTerm, sort);
        return receivingCategoryRuleMapper.toDTOList(entities);
    }

    @Override
    public Page<ReceiveCategoryRuleDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<ReceiveCategoryRule> entities = receivingCategoryRuleRepository.getBySearchTerm(searchTerm,pageable);
        return receivingCategoryRuleMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<ReceiveCategoryRuleDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<ReceiveCategoryRule> entities = receivingCategoryRuleRepository.getNotLockList(null, sort);
        return receivingCategoryRuleMapper.toDTOList(entities);
    }

    private void checkRuleName(String name) {
        ReceiveCategoryRule rule = receivingCategoryRuleRepository.getByName(name);
        if (rule != null) {
            throw new ApiException(InBoundException.EX_MD_IN_RECEIVE_CATEGORY_RULE_NAME_UNIQUE.toString(), name);
        }
    }
}
