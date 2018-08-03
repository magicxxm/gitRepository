package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.ibbasics.business.*;
import com.mushiny.wms.masterdata.ibbasics.business.dto.ReceivingGoodsDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.AdviceRequestDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceivingProcessContainerDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.AdviceRequestMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.*;
import com.mushiny.wms.masterdata.ibbasics.domain.enums.AdviceRequestState;
import com.mushiny.wms.masterdata.ibbasics.repository.*;
import com.mushiny.wms.masterdata.ibbasics.service.ReceivingOperationService;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReceivingOperationServiceImpl implements ReceivingOperationService {

    private final AdviceRequestBusiness adviceRequestBusiness;
    private final GoodsReceiptBusiness goodsReceiptBusiness;
    private final ReceivingGoodsBusiness receivingGoodsBusiness;
    private final ReceivingDestinationBusiness receivingDestinationBusiness;

    private final StockUnitRepository stockUnitRepository;
    private final ItemDataRepository itemDataRepository;
    private final ApplicationContext applicationContext;
    private final ReceiveStationRepository receivingStationRepository;
    private final AdviceRequestRepository adviceRequestRepository;
    private final ReceiveStationTypePositionRepository receivingStationTypePositionRepository;

    private final AdviceRequestMapper adviceRequestMapper;

    @Autowired
    public ReceivingOperationServiceImpl(AdviceRequestRepository adviceRequestRepository,
                                         ReceiveStationRepository receivingStationRepository,
                                         AdviceRequestMapper adviceRequestMapper,
                                         ReceivingGoodsBusiness receivingGoodsBusiness,
                                         AdviceRequestBusiness adviceRequestBusiness,
                                         GoodsReceiptBusiness goodsReceiptBusiness,
                                         ReceivingDestinationBusiness receivingDestinationBusiness,
                                         StockUnitRepository stockUnitRepository,
                                         ApplicationContext applicationContext,
                                         ReceiveStationTypePositionRepository receivingStationTypePositionRepository,
                                         ItemDataRepository itemDataRepository) {
        this.adviceRequestRepository = adviceRequestRepository;
        this.receivingStationRepository = receivingStationRepository;
        this.adviceRequestMapper = adviceRequestMapper;
        this.receivingGoodsBusiness = receivingGoodsBusiness;
        this.adviceRequestBusiness = adviceRequestBusiness;
        this.goodsReceiptBusiness = goodsReceiptBusiness;
        this.receivingDestinationBusiness = receivingDestinationBusiness;
        this.stockUnitRepository = stockUnitRepository;
        this.applicationContext = applicationContext;
        this.receivingStationTypePositionRepository = receivingStationTypePositionRepository;
        this.itemDataRepository = itemDataRepository;
    }

    @Override
    public AdviceRequestDTO activateAdviceNo(String adviceNo) {
        AdviceRequest adviceRequest = adviceRequestBusiness.getAdviceRequest(adviceNo);
        // 判断收货单是否已被激活
        adviceRequestBusiness.checkNotActivatedDN(adviceRequest);
        // 激活DN  修改狀態為Fulfilled
        adviceRequest.setAdviceState(AdviceRequestState.Fulfilled.toString());
        adviceRequest = adviceRequestRepository.save(adviceRequest);
        goodsReceiptBusiness.buildAndSaveGoodsReceipt(adviceRequest);
        return adviceRequestMapper.toDTO(adviceRequest);
    }

    @Override
    public void saveReceivingProcessContainer(ReceivingProcessContainerDTO receivingProcessContainer) {
        /**
        ReceivingProcessContainer processContainer = receivingProcessContainerMapper
                .toEntity(receivingProcessContainer);
        // 绑定的目的不能重复
        receivingDestinationBusiness.usedToReceivingStation(
                processContainer.getReceivingStation(), processContainer.getReceivingDestination());
        // 绑定的目的地必须在ReceivingStationTypePosition中存在
        ReceiveStationTypePosition position = receivingDestinationBusiness.checkReceivingStationTypePosition(
                processContainer.getReceivingStation().getReceivingStationType(),
                processContainer.getReceivingDestination());
        // 判断小车的状态是否为空车
        if (!processContainer.getContainer().getState().equalsIgnoreCase(ContainerState.Empty.toString())) {
            throw new ApiException(InBoundException
                    .EX_CONTAINER_IS_NOT_EMPTY_STATE.toString(), processContainer.getContainer().getName());
        }
        // 绑定数量不能超过工作站设置的总数量
        long maxAmount = receivingStationTypePositionRepository.countByReceivingStationType(
                processContainer.getReceivingStation().getReceivingStationType());
        long amount = receivingProcessContainerRepository.countByReceivingStation(
                processContainer.getReceivingStation(), ReceivingProcessContainerState.Started.toString());
        if (amount >= maxAmount) {
            throw new ApiException(InBoundException
                    .EX_RECEIVING_STATION_CONTAINER_AMOUNT_MAX.toString(), processContainer.getReceivingStation().getName());
        }

        processContainer.setPositionIndex(position.getPositionNo());
        processContainer.setState(ReceivingProcessContainerState.Started.toString());
        receivingProcessContainerRepository.save(processContainer);
        // 把小车标记为满车状态
        Container container = processContainer.getContainer();
        container.setState(ContainerState.Full.toString());
        containerRepository.save(container);
         */
    }

    @Override
    public void receivingGoods(ReceivingGoodsDTO receiving) {
        /**
        Container container = containerRepository.retrieve(receiving.getReceivingContainerId());
        AdviceRequest adviceRequest = adviceRequestRepository.retrieve(receiving.getAdviceId());
        ItemData itemData = itemDataRepository.retrieve(receiving.getItemDataId());
        if (container.getContainerType().getName().contains(ContainerTypeName.DAMAGE.getName())) {
            // 收残货
            receivingGoodsBusiness.receivingDamageGoods(container, adviceRequest, itemData, receiving);
        } else if (container.getContainerType().getName().contains(ContainerTypeName.CUBI_SCAN.getName())) {
            // 收待扫描货
            receivingGoodsBusiness.receivingCubiScanGoods(container, adviceRequest, itemData, receiving);
        } else {
            // 正常收货
            receivingGoodsBusiness.receivingInventoryGoods(container, adviceRequest, itemData, receiving);
        }
        */
    }

    @Override
    public void deleteReceivingProcessContainers(String receivingStationId) {
        /**
        ReceiveStation receivingStation = receivingStationRepository.retrieve(receivingStationId);
        List<ReceivingProcessContainer> processContainers = receivingProcessContainerRepository
                .getByReceivingStation(receivingStation, ReceivingProcessContainerState.Started.toString());
        // 解绑所有小车、目的地以及工作站的关系
        for (ReceivingProcessContainer processContainer : processContainers) {
            Container container = processContainer.getContainer();
            // 注意，在上架完成后，需要把StockUnit中的container设置为空
            List<StockUnit> stockUnits = stockUnitRepository.getByContainer(container);
            // 如果小车没有收货则把小车设置为空
            if (stockUnits == null || stockUnits.isEmpty()) {
                container.setState(ContainerState.Empty.toString());
                processContainer.setState(ReceivingProcessContainerState.Finished.toString());
                containerRepository.save(container);
            } else {
                // 检查小车中是否存在不属于当前客户的客户产品
                for (StockUnit stockUnit : stockUnits) {
                    permissionsContext.isCurrentClient(stockUnit.getClient());
                }
                processContainer.setState(ReceivingProcessContainerState.Processing.toString());
            }
        }
        receivingProcessContainerRepository.save(processContainers);
         */
    }

    @Override
    public void replaceContainer(String oldContainerId, String newContainerId) {
        /**
        /// 跟换车牌
        Container oldContainer = containerRepository.retrieve(oldContainerId);
        Container newContainer = containerRepository.retrieve(newContainerId);
        ReceivingProcessContainer oldProcessContainer = receivingProcessContainerBusiness.
                getStartedProcessContainer(oldContainer);
        if (oldProcessContainer == null) {
            throw new ApiException(ExceptionEnum.EX_OPERATION_ERROR.toString());
        }
        // 注意，在上架完成后，需要把StockUnit中的container设置为空
        List<StockUnit> stockUnits = stockUnitRepository.getByContainer(oldContainer);
        // 如果小车没有收货则把小车设置为空
        if (stockUnits == null || stockUnits.isEmpty()) {
            oldContainer.setState(ContainerState.Empty.toString());
            oldProcessContainer.setState(ReceivingProcessContainerState.Finished.toString());
            containerRepository.save(oldContainer);
        } else {
            oldProcessContainer.setState(ReceivingProcessContainerState.Processing.toString());
        }
        receivingProcessContainerRepository.save(oldProcessContainer);
        // 判断小车的状态是否为空车
        if (!newContainer.getState().equalsIgnoreCase(ContainerState.Empty.toString())) {
            throw new ApiException(InBoundException
                    .EX_CONTAINER_IS_NOT_EMPTY_STATE.toString(), newContainer.getName());
        }
        newContainer.setState(ContainerState.Full.toString());
        containerRepository.save(newContainer);
        // 绑定新小车
        ReceivingProcessContainer newProcessContainer = new ReceivingProcessContainer();
        newProcessContainer.setState(ReceivingProcessContainerState.Started.toString());
        newProcessContainer.setPositionIndex(oldProcessContainer.getPositionIndex());
        newProcessContainer.setReceivingStation(oldProcessContainer.getReceivingStation());
        newProcessContainer.setReceivingDestination(oldProcessContainer.getReceivingDestination());
        newProcessContainer.setContainer(newContainer);
        newProcessContainer.setWarehouse(oldProcessContainer.getWarehouse());
        receivingProcessContainerRepository.save(newProcessContainer);
         */
    }
}
