package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.masterdata.ibbasics.crud.dto.AdviceRequestDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.GoodsReceiptDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.AdviceRequestMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.AdviceRequestPositionMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.GoodsReceiptMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.GoodsReceiptPositionMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.AdviceRequest;
import com.mushiny.wms.masterdata.ibbasics.domain.AdviceRequestPosition;
import com.mushiny.wms.masterdata.ibbasics.domain.GoodsReceipt;
import com.mushiny.wms.masterdata.ibbasics.domain.GoodsReceiptPosition;
import com.mushiny.wms.masterdata.ibbasics.domain.enums.GoodsReceiptState;
import com.mushiny.wms.masterdata.ibbasics.repository.AdviceRequestPositionRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.AdviceRequestRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.GoodsReceiptRepository;
import com.mushiny.wms.masterdata.ibbasics.service.GoodsReceiptService;
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
public class GoodsReceiptServiceImpl implements GoodsReceiptService {

    private final GoodsReceiptRepository goodsReceiptRepository;
    private final GoodsReceiptMapper goodsReceiptMapper;
    private final GoodsReceiptPositionMapper goodsReceiptPositionMapper;
    private final AdviceRequestPositionRepository adviceRequestPositionRepository;
    private final AdviceRequestPositionMapper adviceRequestPositionMapper;
    private final AdviceRequestMapper adviceRequestMapper;

    @Autowired
    public GoodsReceiptServiceImpl(GoodsReceiptRepository goodsReceiptRepository,
                                   GoodsReceiptMapper goodsReceiptMapper,
                                   GoodsReceiptPositionMapper goodsReceiptPositionMapper,
                                   AdviceRequestPositionRepository adviceRequestPositionRepository,
                                   AdviceRequestPositionMapper adviceRequestPositionMapper,
                                   AdviceRequestMapper adviceRequestMapper) {
        this.goodsReceiptRepository = goodsReceiptRepository;
        this.goodsReceiptMapper = goodsReceiptMapper;
        this.goodsReceiptPositionMapper = goodsReceiptPositionMapper;
        this.adviceRequestPositionRepository=adviceRequestPositionRepository;
        this.adviceRequestPositionMapper=adviceRequestPositionMapper;
        this.adviceRequestMapper=adviceRequestMapper;
    }

    @Override
    public GoodsReceiptDTO retrieve(String id) {
        GoodsReceipt entity = goodsReceiptRepository.retrieve(id);
        GoodsReceiptDTO dto = goodsReceiptMapper.toDTO(entity);
        AdviceRequestDTO adviceRequestDTO=dto.getRelatedAdvice();
        AdviceRequest AdviceRequestEntity= entity.getRelatedAdvice();
        List<AdviceRequestPosition>  AdviceRequestPositionList=adviceRequestPositionRepository.getByAdviceRequestId(AdviceRequestEntity.getId());
       if(AdviceRequestPositionList.size()!=0){
        adviceRequestDTO.setPositions(adviceRequestPositionMapper.toDTOList(AdviceRequestPositionList));
        dto.setRelatedAdvice(adviceRequestDTO);
       }
        dto.setPositions(goodsReceiptPositionMapper.toDTOList(entity.getPositions()));
        return dto;
    }

    @Override
    public Page<GoodsReceiptDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "receiptDate"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<GoodsReceipt> entities = goodsReceiptRepository.getBySearchTerm(searchTerm, pageable);
        for(GoodsReceipt goodsReceiptEntities:entities){
            int count = 0;
            int count1 = 0;
            List<GoodsReceiptPosition> goodsReceiptPosition = goodsReceiptEntities.getPositions();
            if (goodsReceiptPosition.size() != 0) {
                for (GoodsReceiptPosition position : goodsReceiptPosition) {
                    if (position.getAmount() != null) {
                        count1 = position.getAmount().intValue();
                    }
                    count = count + count1;
                }
            }
            goodsReceiptEntities.setSize(count);
        }
        return goodsReceiptMapper.toDTOPage(pageable, entities);
    }

    @Override
    public GoodsReceiptDTO getByIdAndClosed(String id) {
        GoodsReceipt  goodsReceiptEntities=goodsReceiptRepository.retrieve(id);
        if(goodsReceiptEntities.getReceiptState().equals(GoodsReceiptState.Accepted.toString())){
            goodsReceiptEntities.setReceiptState(GoodsReceiptState.Finished.toString());
            goodsReceiptRepository.save(goodsReceiptEntities);
        }
        return goodsReceiptMapper.toDTO(goodsReceiptEntities);
    }

    @Override
    public GoodsReceiptDTO getByIdAndOpen(String id) {
        GoodsReceipt  goodsReceiptEntities=goodsReceiptRepository.retrieve(id);
        if(goodsReceiptEntities.getReceiptState().equals(GoodsReceiptState.Finished.toString())){
            goodsReceiptEntities.setReceiptState(GoodsReceiptState.Accepted.toString());
            goodsReceiptRepository.save(goodsReceiptEntities);
        }
        return goodsReceiptMapper.toDTO(goodsReceiptEntities);
    }
}
