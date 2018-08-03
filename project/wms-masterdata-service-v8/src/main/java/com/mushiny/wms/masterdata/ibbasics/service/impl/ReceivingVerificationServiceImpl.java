package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.business.AdviceRequestBusiness;
import com.mushiny.wms.masterdata.ibbasics.business.ReceivingDestinationBusiness;
import com.mushiny.wms.masterdata.ibbasics.business.ReceivingStationBusiness;
import com.mushiny.wms.masterdata.ibbasics.business.StockUnitBusiness;
import com.mushiny.wms.masterdata.ibbasics.business.dto.ItemDataAmountDTO;
import com.mushiny.wms.masterdata.ibbasics.business.dto.ScanningReceivingStationDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.AdviceRequestDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveDestinationDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceivingProcessContainerDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.*;
import com.mushiny.wms.masterdata.ibbasics.domain.*;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.*;
import com.mushiny.wms.masterdata.ibbasics.service.ReceivingVerificationService;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.ItemDataMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemData;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ReceivingVerificationServiceImpl implements ReceivingVerificationService {

    private final AdviceRequestBusiness adviceRequestBusiness;
    private final ReceivingDestinationBusiness receivingDestinationBusiness;
    private final ReceivingStationBusiness receivingStationBusiness;
    private final StockUnitBusiness stockUnitBusiness;
    private final ApplicationContext applicationContext;
    private final ReceiveStationRepository receivingStationRepository;
    private final ReceiveStationTypePositionRepository receivingStationTypePositionRepository;
    private final AdviceRequestRepository adviceRequestRepository;
    private final ItemDataRepository itemDataRepository;
    private final GoodsReceiptPositionRepository goodsReceiptPositionRepository;
    private final ReceiveStationMapper receivingStationMapper;
    private final ReceiveDestinationMapper receivingDestinationMapper;
    private final ItemDataMapper itemDataMapper;
    private final AdviceRequestMapper adviceRequestMapper;
    private final ReceiveStationTypePositionMapper receivingStationTypePositionMapper;

    @Autowired
    public ReceivingVerificationServiceImpl(AdviceRequestRepository adviceRequestRepository,
                                            ReceiveStationRepository receivingStationRepository,
                                            ReceiveDestinationMapper receivingDestinationMapper,
                                            ItemDataMapper itemDataMapper,
                                            ItemDataRepository itemDataRepository,
                                            ReceiveStationMapper receivingStationMapper,
                                            ReceiveStationTypePositionRepository receivingStationTypePositionRepository,
                                            GoodsReceiptPositionRepository goodsReceiptPositionRepository,
                                            AdviceRequestMapper adviceRequestMapper,
                                            ReceivingDestinationBusiness receivingDestinationBusiness,
                                            AdviceRequestBusiness adviceRequestBusiness,
                                            ReceivingStationBusiness receivingStationBusiness,
                                            ReceiveStationTypePositionMapper receivingStationTypePositionMapper,
                                            StockUnitBusiness stockUnitBusiness,
                                            ApplicationContext applicationContext) {
        this.adviceRequestRepository = adviceRequestRepository;
        this.receivingStationRepository = receivingStationRepository;
        this.receivingDestinationMapper = receivingDestinationMapper;
        this.itemDataMapper = itemDataMapper;
        this.itemDataRepository = itemDataRepository;
        this.receivingStationMapper = receivingStationMapper;
        this.receivingStationTypePositionRepository = receivingStationTypePositionRepository;
        this.goodsReceiptPositionRepository = goodsReceiptPositionRepository;
        this.adviceRequestMapper = adviceRequestMapper;
        this.receivingDestinationBusiness = receivingDestinationBusiness;
        this.adviceRequestBusiness = adviceRequestBusiness;
        this.receivingStationBusiness = receivingStationBusiness;
        this.receivingStationTypePositionMapper = receivingStationTypePositionMapper;
        this.stockUnitBusiness = stockUnitBusiness;
        this.applicationContext = applicationContext;
    }

    @Override
    public AdviceRequestDTO scanAdviceRequest(String adviceNo) {
        AdviceRequest adviceRequest = adviceRequestBusiness.getAdviceRequest(adviceNo);
        // 判断收货单是否已被激活
        adviceRequestBusiness.checkActivatedDN(adviceRequest);
        return adviceRequestMapper.toDTO(adviceRequest);
    }

    @Override
    public ScanningReceivingStationDTO scanReceivingStation(String name) {
        ReceiveStation receivingStation = receivingStationBusiness.getReceivingStation(name);
        // 设置工作站可以绑定小车的最大数量
        long maxProcessContainer = receivingStationTypePositionRepository.countByReceivingStationType(
                receivingStation.getReceivingStationType());
        ScanningReceivingStationDTO dto = new ScanningReceivingStationDTO();
        List<ReceiveStationTypePosition> positions = receivingStationTypePositionRepository
                .getPositions(receivingStation.getReceivingStationType());
        dto.setReceivingStation(receivingStationMapper.toDTO(receivingStation));
        dto.setReceivingStationTypePositions(receivingStationTypePositionMapper.toDTOList(positions));
        dto.setMaxAmount(maxProcessContainer);
        return dto;
    }

    @Override
    public ReceiveDestinationDTO scanReceivingDestination(String stationId, String name) {
        ReceiveStation receivingStation = receivingStationRepository.retrieve(stationId);
        ReceiveDestination receivingDestination = receivingDestinationBusiness.getReceivingDestination(name);
        // 目的地是否已经被该工作站绑定
        receivingDestinationBusiness.usedToReceivingStation(receivingStation, receivingDestination);
        // 绑定的目的地必须在ReceivingStationTypePosition中存在
        receivingDestinationBusiness.checkReceivingStationTypePosition(
                receivingStation.getReceivingStationType(), receivingDestination);
        return receivingDestinationMapper.toDTO(receivingDestination);
    }

    /**
    @Override
    public ContainerDTO scanContainer(String name) {
        Warehouse warehouse = permissionsContext.getCurrentWarehouse();
        Container container = Optional
                .ofNullable(containerRepository.getByName(warehouse, name))
                .orElseThrow(() -> new ApiException(InBoundException.EX_SCANNING_OBJECT_NOT_FOUND.toString(), name));
        // 判断小车的状态是否为空车
        if (!container.getState().equalsIgnoreCase(ContainerState.Empty.toString())) {
            throw new ApiException(InBoundException
                    .EX_CONTAINER_IS_NOT_EMPTY_STATE.toString(), container.getName());
        }
        return containerMapper.toDTO(container);
    }
     */

    @Override
    public ItemDataAmountDTO scanAdvicePositionItemData(String adviceId, String itemNo) {
        // 获取收货单（在根据ID获取的时候就检查了仓库和客户的权限）
        AdviceRequest adviceRequest = adviceRequestRepository.retrieve(adviceId);
        adviceRequestBusiness.checkActivatedDN(adviceRequest);
        // 检查SKU
        ItemData itemData = Optional.ofNullable(itemDataRepository.getByItemNo(
                adviceRequest.getWarehouseId(), adviceRequest.getClientId(), itemNo, Constant.NOT_LOCKED))
                .orElseThrow(() -> new ApiException(InBoundException.EX_SKU_NOT_FOUND.toString(), itemNo));
        // 判断该商品SKU是否在收货单上
        adviceRequestBusiness.checkAdviceRequestItemData(adviceRequest, itemData);
        ItemDataAmountDTO itemDataAmount = new ItemDataAmountDTO();
        itemDataAmount.setItemData(itemDataMapper.toDTO(itemData));
        itemDataAmount.setAmount(adviceRequestBusiness.getAdviceRequestItemDataAmount(adviceRequest, itemData));
        itemDataAmount.setReceiveAmount(adviceRequestBusiness.getGoodsReceiptItemDataAmount(adviceRequest, itemData));
        itemDataAmount.setDnAmount(adviceRequestBusiness.getGoodsReceiptAmount(adviceRequest));
        return itemDataAmount;
    }

    @Override
    public ReceiveDestinationDTO screeningReceivingDestination(String itemDataId, String receivingType) {
        ItemData itemData = itemDataRepository.retrieve(itemDataId);
        // 筛选目的地
        ReceiveDestination receivingDestination = receivingDestinationBusiness.screening(itemData, receivingType);
        return receivingDestinationMapper.toDTO(receivingDestination);
    }

    @Override
    public List<ReceivingProcessContainerDTO> getReceivingProcessContainers(String receivingStationId) {
        return null;
    }

    /**
    @Override
    public ContainerDTO checkReceivingContainer(String containerId, String itemDataId, LocalDate useNotAfter) {
        Container container = containerRepository.retrieve(containerId);
        ItemData itemData = itemDataRepository.retrieve(itemDataId);
        // 如果是待测量小车，判断该小车是否存在该SKU商品
        if (container.getContainerType().getName().contains(ContainerTypeName.CUBI_SCAN.getName())) {
            // 如果是测量容器，那么只容许放入该SKU一件商品就好
            stockUnitBusiness.checkCubiScanItemData(container, itemData);
        }
        // 容器中不能存放不同客户的相同商品
        stockUnitBusiness.checkItemClient(container, itemData);
        // 容器中不能存放同一商品的不同有效期
        if (itemData.isLotMandatory()) {
            stockUnitBusiness.checkItemLot(container, itemData, useNotAfter);
        }
        return containerMapper.toDTO(container);
    }


    @Override
    public List<ReceivingProcessContainerDTO> getReceivingProcessContainers(String receivingStationId) {
        // 获取工作站下所有车牌以及相应的目的地以及每个车中的商品数量
        ReceiveStation receivingStation = receivingStationRepository.retrieve(receivingStationId);
        List<ReceivingProcessContainer> processContainers = receivingProcessContainerRepository
                .getByReceivingStation(receivingStation, ReceivingProcessContainerState.Started.toString());
        List<ReceivingProcessContainerDTO> dtoList = new ArrayList<>();
        for (ReceivingProcessContainer processContainer : processContainers) {
            BigDecimal amount = goodsReceiptPositionRepository.sumByProcessContainer(processContainer);
            ReceivingProcessContainerDTO dto = receivingProcessContainerMapper.toDTO(processContainer);
            dto.setAmount(amount);
            dtoList.add(dto);
        }
        return dtoList;
    }
     */
}
