package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.utils.RandomUtil;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.AdviceRequestDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.AdviceRequestPositionDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.AdviceRequestMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.AdviceRequestPositionMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.AdviceRequest;
import com.mushiny.wms.masterdata.ibbasics.domain.AdviceRequestPosition;
import com.mushiny.wms.masterdata.ibbasics.domain.GoodsReceipt;
import com.mushiny.wms.masterdata.ibbasics.domain.enums.AdviceRequestState;
import com.mushiny.wms.masterdata.ibbasics.repository.AdviceRequestPositionRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.AdviceRequestRepository;
import com.mushiny.wms.masterdata.ibbasics.service.AdviceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AdviceRequestServiceImpl implements AdviceRequestService {

    private final AdviceRequestRepository adviceRequestRepository;
    private final ApplicationContext applicationContext;
    private final AdviceRequestMapper adviceRequestMapper;
    private final AdviceRequestPositionMapper adviceRequestPositionMapper;
    private final AdviceRequestPositionRepository adviceRequestPositionRepository;

    @Autowired
    public AdviceRequestServiceImpl(AdviceRequestRepository adviceRequestRepository,
                                    ApplicationContext applicationContext,
                                    AdviceRequestMapper adviceRequestMapper,
                                    AdviceRequestPositionMapper adviceRequestPositionMapper,
                                    AdviceRequestPositionRepository adviceRequestPositionRepository) {
        this.adviceRequestRepository = adviceRequestRepository;
        this.applicationContext = applicationContext;
        this.adviceRequestMapper = adviceRequestMapper;
        this.adviceRequestPositionMapper = adviceRequestPositionMapper;
        this.adviceRequestPositionRepository = adviceRequestPositionRepository;
    }

    @Override
    public AdviceRequestDTO create(AdviceRequestDTO dto) {
        AdviceRequest entity = adviceRequestMapper.toEntity(dto);
        entity.setAdviceState(AdviceRequestState.Raw.toString());
        String adviceNo;
        boolean randomFlag = true;
        while (randomFlag) {
            adviceNo = "DN" + RandomUtil.getAdviceNo();
            AdviceRequest adviceRequest = adviceRequestRepository.getByAdviceNo(adviceNo);
            if (adviceRequest == null) {
                entity.setAdviceNo(adviceNo);
                randomFlag = false;
            }
        }

        List<AdviceRequestPositionDTO> adviceRequestPositionDTOList= dto.getPositions();

        for (int i=0; i<adviceRequestPositionDTOList.size();i++) {
            String itemId=adviceRequestPositionDTOList.get(i).getItemDataId();
            BigDecimal number=adviceRequestPositionDTOList.get(i).getNotifiedAmount();
            for (int j=i+1; j<adviceRequestPositionDTOList.size();j++) {
               String itemId1= adviceRequestPositionDTOList.get(j).getItemDataId();
                if(itemId.equals(itemId1)||itemId==itemId1){
                    number=number.add(adviceRequestPositionDTOList.get(j).getNotifiedAmount());
                    adviceRequestPositionDTOList.remove(adviceRequestPositionDTOList.get(j));
                    j--;
                }
            }
            adviceRequestPositionDTOList.get(i).setNotifiedAmount(number);
            adviceRequestPositionDTOList.get(i).setPositionNo(i+1);
            entity.addPosition(adviceRequestPositionMapper.toEntity(adviceRequestPositionDTOList.get(i)));
        }
        return adviceRequestMapper.toDTO(adviceRequestRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        AdviceRequest entity = adviceRequestRepository.retrieve(id);
        entity.setEntityLock(Constant.GOING_TO_DELETE);
        adviceRequestRepository.save(entity);
    }

    @Override
    public AdviceRequestDTO update(AdviceRequestDTO dto) {
        AdviceRequest entity = adviceRequestRepository.retrieve(dto.getId());
        adviceRequestMapper.updateEntityFromDTO(dto, entity);
        entity.getPositions().clear();
        List<AdviceRequestPosition> positions = adviceRequestPositionMapper.toEntityList(dto.getPositions());
        for (AdviceRequestPosition position : positions) {
            entity.addPosition(position);
        }
        return adviceRequestMapper.toDTO(adviceRequestRepository.save(entity));
    }

    @Override
    public AdviceRequestDTO retrieve(String id) {
        AdviceRequest entity = adviceRequestRepository.retrieve(id);
        AdviceRequestDTO dto = adviceRequestMapper.toDTO(entity);
        dto.getPositions().addAll(adviceRequestPositionMapper.toDTOList(entity.getPositions()));
        return dto;
    }

    @Override
    public List<AdviceRequestDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<AdviceRequest> entities = adviceRequestRepository.getBySearchTerm(searchTerm, sort);
        return adviceRequestMapper.toDTOList(entities);
    }

    @Override
    public Page<AdviceRequestDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "expectedDelivery"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<AdviceRequest> entities = adviceRequestRepository.getBySearchTerm(searchTerm, pageable);

        for (AdviceRequest request : entities) {
            int count = 0;
            int count1 = 0;
            List<AdviceRequestPosition> adviceRequestPosition = request.getPositions();
            if (adviceRequestPosition.size() != 0) {
                for (AdviceRequestPosition position : adviceRequestPosition) {
                    if (position.getNotifiedAmount() != null) {
                        count1 = position.getNotifiedAmount().intValue();
                    }
                    count = count + count1;
                }
            }
            request.setSize(count);
        }
        Page<AdviceRequestDTO> dtoPage = adviceRequestMapper.toDTOPage(pageable, entities);
        return dtoPage;
    }

    @Override
    public List<AdviceRequestDTO> getByClientId(String clientId) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "adviceNo"));
        applicationContext.isCurrentClient(clientId);
        List<AdviceRequest> entities = adviceRequestRepository.getNotLockList(clientId, sort);
        return adviceRequestMapper.toDTOList(entities);
    }
    @Override
    public AdviceRequestDTO getByIdAndLockDN(String id) {
        AdviceRequest adviceRequest= adviceRequestRepository.retrieve(id);
        adviceRequest.setAdviceState(AdviceRequestState.Lock.toString());
        return adviceRequestMapper.toDTO(adviceRequestRepository.save(adviceRequest));
    }

}
