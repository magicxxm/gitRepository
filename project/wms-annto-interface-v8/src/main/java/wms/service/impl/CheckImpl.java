package wms.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wms.crud.dto.AdjustConfirmDTO;
import wms.domain.common.Warehouse;
import wms.repository.common.WarehouseRepository;
import wms.service.Check;
import wms.business.AnntoBusiness;
import wms.business.CheckBusiness;
import wms.business.dto.CheckConfirmItemsDTO;
import wms.common.context.ApplicationContext;
import wms.common.crud.AccessDTO;
import wms.common.exception.ApiException;
import wms.constants.CheckState;
import wms.crud.dto.CheckConfirmDTO;
import wms.crud.dto.CheckUpdateDTO;
import wms.domain.AnntoStocktaking;
import wms.domain.SystemStocktaking;
import wms.domain.SystemStocktakingOrder;
import wms.domain.SystemStocktakingPosition;
import wms.exception.OutBoundException;
import wms.repository.AnntoStocktakingRepository;
import wms.repository.SystemStocktakingOrderRepository;
import wms.repository.SystemStocktakingRepository;
import wms.repository.common.ClientRepository;
import wms.repository.common.ItemDataRepository;
import wms.web.vm.AdjustItemDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CheckImpl implements Check{
    private final Logger log = LoggerFactory.getLogger(CheckImpl.class);

    private final CheckBusiness checkBusiness;
    private final AnntoBusiness anntoBusiness;
    private  final AnntoStocktakingRepository anntoStocktakingRepository;
    private final ApplicationContext applicationContext;
    private final ItemDataRepository itemDataRepository;
    private final SystemStocktakingRepository systemStocktakingRepository;
    private final ClientRepository clientRepository;
    private final SystemStocktakingOrderRepository systemStocktakingOrderRepository;
    private final WarehouseRepository warehouseRepository;

    @Autowired
    public CheckImpl(AnntoBusiness anntoBusiness,
                     CheckBusiness checkBusiness,
                     WarehouseRepository warehouseRepository,
                     AnntoStocktakingRepository anntoStocktakingRepository,
                     ApplicationContext applicationContext,
                     SystemStocktakingRepository systemStocktakingRepository,
                     SystemStocktakingOrderRepository systemStocktakingOrderRepository,
                     ClientRepository clientRepository,
                     ItemDataRepository itemDataRepository) {
        this.checkBusiness = checkBusiness;
        this.anntoStocktakingRepository = anntoStocktakingRepository;
        this.anntoBusiness = anntoBusiness;
        this.applicationContext = applicationContext;
        this.systemStocktakingRepository = systemStocktakingRepository;
        this.itemDataRepository = itemDataRepository;
        this.clientRepository = clientRepository;
        this.warehouseRepository = warehouseRepository;
        this.systemStocktakingOrderRepository= systemStocktakingOrderRepository;
    }

    @Override
    public AccessDTO update(CheckUpdateDTO dto) {

        AccessDTO accessDTO = new AccessDTO();
        Warehouse warehouse = warehouseRepository.getByWarehouseNo(dto.getWarehouseCode());
        AnntoStocktaking anntoStocktaking = anntoStocktakingRepository.getByCode(dto.getOriginalCountId(),warehouse.getWarehouseNo());
        if(anntoStocktaking != null){
            log.error("盘点单号：" + dto.getOriginalCountId() + " 已存在。。。");
            accessDTO.setCode("1");
            accessDTO.setMsg("该盘点单号已存在。。。");
            return accessDTO;
        }
        /**
         * 先将数据存至ANNTO信息表
         *
         * 美的计划内传参缺少warehouseCode、
         * 明细表中必要栏位：STATE 参数空、
         * 其他可为空但缺少传入参数的栏位为：AREA_NAME、 STOCKTAKINGRULE_ID、 OPERATOR、 TIMES、 UNITLOAD_LABEL
         */
        checkBusiness.saveToAnntoStocktaking(dto);

        /**
         * 在wms_v8对应表中保存
         */
        checkBusiness.saveToWmsSysStocktaking(dto);

        log.info("盘点单：" + dto.getOriginalCountId() +" 同步成功");

        return accessDTO;
    }

    @Override
    public void confirm(String stocktakingNo) {
        CheckConfirmDTO dto = new CheckConfirmDTO();
        //获取盘点单
        SystemStocktaking systemStocktaking = systemStocktakingRepository.getByStockNo(stocktakingNo);
        if(systemStocktaking.getState() < CheckState.FINISHED){
            throw new ApiException(OutBoundException.EX_STOCKTAKING_NOT_FINISHED.name(),"盘点单:"+stocktakingNo+" 没有完成！");
        }
        dto.setOriginalCountId(stocktakingNo);
        dto.setRemark(systemStocktaking.getRemark());
        dto.setCountType(systemStocktaking.getStockType());

        List<CheckConfirmItemsDTO> orderItems = new ArrayList<>();
        List<SystemStocktakingPosition> positions = systemStocktaking.getPositions();
        for (SystemStocktakingPosition p:positions) {
            if(p.getState() < CheckState.FINISHED){
                throw new ApiException(OutBoundException.EX_ITEMDATA_NOT_FINISHED_STOCKTAKING.name(),"商品 "+p.getItemName()+"没有完成盘点！");
            }
            CheckConfirmItemsDTO item = new CheckConfirmItemsDTO();
            item.setLocationCode(p.getLocationCode());
            item.setItemName(p.getItemName());
            item.setItemCode(p.getItemNo());
            item.setInventorySts(p.getInventorySts());
            item.setCountedBy(p.getOperator());
            item.setCompletedAt(p.getEnded().toString());
            item.setCountedAt(p.getStarted().toString());
            item.setCompanyCode(clientRepository.findOne(p.getClientId()).getClientNo());

            List<SystemStocktakingOrder> orders = systemStocktakingOrderRepository.getAll(p);
            //获取商品的系统数量,这里是牧星系统查询的商品的库存数量
//            BigDecimal amountSys = orders.stream().map(SystemStocktakingOrder::getAmountSystem).reduce(BigDecimal.ZERO,BigDecimal::add);
//            item.setSystemQTY(amountSys.intValue());
            //获取盘点数量
            BigDecimal amountCheck = orders.stream().map(SystemStocktakingOrder::getAmountCheck).reduce(BigDecimal.ZERO,BigDecimal::add);
            item.setCountedQty(amountCheck.intValue());

            p.setAmountStocking(amountCheck);
        }

        dto.setOrderItems(orderItems);

        anntoBusiness.confirmCheck(dto);
    }

    @Override
    public void confirm(CheckConfirmDTO checkConfirmDTO) {
        if(checkConfirmDTO == null){
            log.error("盘点单是null，确认错误");
            return;
        }
        anntoBusiness.confirmCheck(checkConfirmDTO);
    }

    @Override
    public void adjustItem(List<AdjustItemDTO> adjustItemDTOS) {
        anntoBusiness.adjustItem(adjustItemDTOS);
    }

    @Override
    public AccessDTO adjustConfirm(List<AdjustConfirmDTO> adjustConfirmDTOS) {
        return anntoBusiness.adjustConfirm(adjustConfirmDTOS);
    }
}
