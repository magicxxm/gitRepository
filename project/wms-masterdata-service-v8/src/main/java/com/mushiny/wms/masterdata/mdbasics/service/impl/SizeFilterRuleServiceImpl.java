package com.mushiny.wms.masterdata.mdbasics.service.impl;


import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.SizeFilterRuleDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.SizeFilterRuleMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.HardWare;
import com.mushiny.wms.masterdata.mdbasics.domain.SizeFilterRule;
import com.mushiny.wms.masterdata.mdbasics.repository.SizeFilterRuleRepository;
import com.mushiny.wms.masterdata.mdbasics.service.SizeFilterRuleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Laptop-11 on 2017/6/8.
 */
@Service
@Transactional
public class SizeFilterRuleServiceImpl implements SizeFilterRuleService {
    private final ApplicationContext applicationContext;
    private final SizeFilterRuleMapper sizeFilterRuleMapper;
    private final SizeFilterRuleRepository sizeFilterRuleRepository;

    public SizeFilterRuleServiceImpl(ApplicationContext applicationContext, SizeFilterRuleMapper sizeFilterRuleMapper, SizeFilterRuleRepository sizeFilterRuleRepository) {
        this.applicationContext = applicationContext;
        this.sizeFilterRuleMapper = sizeFilterRuleMapper;
        this.sizeFilterRuleRepository = sizeFilterRuleRepository;
    }

    @Override
    public SizeFilterRuleDTO create(SizeFilterRuleDTO dto) {
        SizeFilterRule sizeFilterRule = sizeFilterRuleMapper.toEntity(dto);
        checkData(sizeFilterRule);
        return sizeFilterRuleMapper.toDTO(sizeFilterRuleRepository.save(sizeFilterRule));
    }

    @Override
    public void delete(String id) {
        SizeFilterRule sizeFilterRule = sizeFilterRuleRepository.retrieve(id);
        sizeFilterRuleRepository.delete(sizeFilterRule);
    }

    @Override
    public SizeFilterRuleDTO update(SizeFilterRuleDTO dto) {
        SizeFilterRule entity = sizeFilterRuleRepository.retrieve(dto.getId());
        //更新后的数据
        SizeFilterRule sizeFilterRule = sizeFilterRuleMapper.toEntity(dto);
        if (entity.getName().equals(sizeFilterRule.getName()) && entity.getRule().equals(sizeFilterRule.getRule())) {
        } else {
            checkData(sizeFilterRule);
        }
        sizeFilterRuleMapper.updateEntityFromDTO(dto, entity);
        return sizeFilterRuleMapper.toDTO(sizeFilterRuleRepository.save(entity));
    }

    @Override
    public SizeFilterRuleDTO retrieve(String id) {
        return sizeFilterRuleMapper.toDTO(sizeFilterRuleRepository.retrieve(id));
    }

    @Override
    public List<SizeFilterRuleDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<SizeFilterRule> entities = sizeFilterRuleRepository.getBySearchTerm(searchTerm, sort);
        return sizeFilterRuleMapper.toDTOList(entities);
    }

    @Override
    public Page<SizeFilterRuleDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "number"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<SizeFilterRule> entities = sizeFilterRuleRepository.getBySearchTerm(searchTerm, pageable);
        return sizeFilterRuleMapper.toDTOPage(pageable, entities);
    }

    private void checkData(SizeFilterRule sizeFilterRule) {
        List<SizeFilterRule> sizeFilterRuleList = sizeFilterRuleRepository.byNameAndRule(sizeFilterRule.getName(),
                sizeFilterRule.getRule(),  Constant.NOT_LOCKED);
        if (sizeFilterRuleList != null && sizeFilterRuleList.size() > 0) {
            throw new ApiException("名称为" + sizeFilterRuleList.get(0).getName() + "AND规则为" + sizeFilterRuleList.get(0).getRule() + "的数据已存在，不可重复添加");
        }
    }

    @Override
    public List<SizeFilterRuleDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "number"));
        List<SizeFilterRule> entities = sizeFilterRuleRepository.getList(null, sort);
        return sizeFilterRuleMapper.toDTOList(entities);
    }
}
