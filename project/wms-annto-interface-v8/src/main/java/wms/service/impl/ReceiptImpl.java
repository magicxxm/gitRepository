package wms.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wms.business.AnntoBusiness;
import wms.business.ReceiptBusiness;
import wms.business.dto.ReceiptConfirmItemsDTO;
import wms.common.crud.AccessDTO;
import wms.crud.dto.ReceiptConfirmDTO;
import wms.crud.dto.ReceiptPositionResult;
import wms.crud.dto.ReceiptUpdateDTO;
import wms.domain.AnntoReceipt;
import wms.domain.GoodsReceipt;
import wms.domain.GoodsReceiptPosition;
import wms.domain.ItemData;
import wms.domain.common.*;
import wms.repository.AnntoReceiptRepository;
import wms.repository.GoodsReceiptRepository;
import wms.repository.ReceiptRequestPositionRepository;
import wms.repository.ReceiptRequestRepository;
import wms.repository.common.*;
import wms.service.Receipt;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional
public class ReceiptImpl implements Receipt{
    private final Logger log = LoggerFactory.getLogger(ReceiptImpl.class);

    private final ReceiptBusiness receiptBusiness;
    private final AnntoBusiness anntoBusiness;
    private final AnntoReceiptRepository anntoReceiptRepository;
    private final ClientRepository clientRepository;
    private final ItemDataRepository itemDataRepository;
    private final ReceiptRequestRepository receiptRequestRepository;
    private final ReceiptRequestPositionRepository receiptRequestPositionRepository;
    private final WarehouseRepository warehouseRepository;
    private final ItemDataSerialNumberRepository itemDataSerialNumberRepository;
    private final GoodsReceiptRepository goodsReceiptRepository;
    private final LotRepository lotRepository;
    private final EntityManager manager;

    @Autowired
    public ReceiptImpl(ReceiptBusiness receiptBusiness,
                       AnntoReceiptRepository anntoReceiptRepository,
                       AnntoBusiness anntoBusiness,
                       ReceiptRequestPositionRepository receiptRequestPositionRepository,
                       ItemDataSerialNumberRepository itemDataSerialNumberRepository,
                       ReceiptRequestRepository receiptRequestRepository,
                       WarehouseRepository warehouseRepository,
                       ClientRepository clientRepository,
                       GoodsReceiptRepository goodsReceiptRepository,
                       LotRepository lotRepository,
                       EntityManager manager,
                       ItemDataRepository itemDataRepository) {
        this.anntoReceiptRepository = anntoReceiptRepository;
        this.receiptBusiness = receiptBusiness;
        this.anntoBusiness = anntoBusiness;
        this.receiptRequestRepository = receiptRequestRepository;
        this.receiptRequestPositionRepository = receiptRequestPositionRepository;
        this.itemDataSerialNumberRepository = itemDataSerialNumberRepository;
        this.clientRepository = clientRepository;
        this.itemDataRepository = itemDataRepository;
        this.goodsReceiptRepository = goodsReceiptRepository;
        this.warehouseRepository = warehouseRepository;
        this.lotRepository = lotRepository;
        this.manager = manager;
    }

    @Override
    public AccessDTO update(ReceiptUpdateDTO dto){
        AccessDTO accessDTO = new AccessDTO();
        /**
         * 检查是否存在该收货单
         */
        String code  = dto.getCode();//收货单号
        String warehouseCode = dto.getWarehouseCode();
        AnntoReceipt anntoReceipt = anntoReceiptRepository.getByCode(code);
        if(anntoReceipt != null){
            log.error("入库单号: " + dto.getCode()+" 在牧星系统已存在。。。");
            accessDTO.setMsg("入库单号" + dto.getCode()+"已存在");
            accessDTO.setCode("1");
            return accessDTO;
        }
        /**
         * 将收货单存入annto表中
         */
        receiptBusiness.createAnntoReceipt(dto);
        log.info("将安得传的入库单信息存入 annto 表中");

        /**
         * 将收货单信息存入wms库
         */
        receiptBusiness.saveToWmsReceipt(dto);

        log.info("将安得传的入库单信息存入 mushiny 表中");

        return accessDTO;
    }

    @Override
    public void confirm(String receiptNo) {
        log.info("入库单确认请求发起  入库单号： " + receiptNo);

        //获取对应得收货单
        GoodsReceipt goodsReceipt = goodsReceiptRepository.getByGrNo(receiptNo);
        AdviceRequest adviceRequest = goodsReceipt.getRelatedAdvice();

        AnntoReceipt request = anntoReceiptRepository.getByCode(adviceRequest.getAdviceNo());

        ReceiptConfirmDTO receiptConfirmDTO = new ReceiptConfirmDTO();

        Warehouse warehouse = warehouseRepository.findOne(adviceRequest.getWarehouseId());
        Client client = clientRepository.findOne(adviceRequest.getClientId());

        receiptConfirmDTO.setCode(request.getCode());
        receiptConfirmDTO.setAnntoCode(request.getAnntoCode());
        receiptConfirmDTO.setWarehouseCode(warehouse.getWarehouseNo());
        receiptConfirmDTO.setCompanyCode(client.getClientNo());
        receiptConfirmDTO.setReceiptType(request.getReceiptType());
        //获取最后一个操作人和时间
        GoodsReceiptPosition position = getLastGoodsPostion(goodsReceipt,null,null);
        if(position != null){
            receiptConfirmDTO.setOperatorName(position.getOperator().getName());
            receiptConfirmDTO.setOperatorCode(position.getOperator().getUsername());
            receiptConfirmDTO.setOperateTime(position.getCreatedDate());
        }

        List<ReceiptConfirmItemsDTO> dtoList = this.getPositions(goodsReceipt);


        receiptConfirmDTO.setOrderItems(dtoList);

      /*  ReceiptConfirmDTO receiptConfirmDTO = new ReceiptConfirmDTO();
        receiptConfirmDTO.setCode(receiptNo);
        receiptConfirmDTO.setAnntoCode(receiptNo);
        receiptConfirmDTO.setWarehouseCode("W200286");
        receiptConfirmDTO.setCompanyCode("MD");
        receiptConfirmDTO.setReceiptType("PI");

        List<ReceiptConfirmItemsDTO> dtoList = this.getReceiptPositions();
        receiptConfirmDTO.setOrderItems(dtoList);*/

        anntoBusiness.confirmReceipt(receiptConfirmDTO);

    }

    private List<ReceiptConfirmItemsDTO> getReceiptPositions() {
        Random random = new Random();
        List<ReceiptConfirmItemsDTO> receiptConfirmItemsDTOList = new ArrayList<>();
        for(int i = 1;i < 2;i++){
            ReceiptConfirmItemsDTO receiptConfirmItemsDTO = new ReceiptConfirmItemsDTO();
            receiptConfirmItemsDTO.setInventorySts("ZP");
            receiptConfirmItemsDTO.setManufactureDate("2018-01-18 14:20:21");
            receiptConfirmItemsDTO.setExpirationDate("2018-01-18 14:20:21");
            receiptConfirmItemsDTO.setAgingDate("2018-01-18 14:20:21");
            receiptConfirmItemsDTO.setQuantity(1000);
            receiptConfirmItemsDTO.setItemCode("21038110000236");
            receiptConfirmItemsDTO.setItemName("测试商品111");
            receiptConfirmItemsDTO.setLineNo(String.valueOf(i));
            receiptConfirmItemsDTO.setSnCode("237975,1264873,34769283");
            receiptConfirmItemsDTO.setUnit("EA");
            receiptConfirmItemsDTOList.add(receiptConfirmItemsDTO);
        }
        return receiptConfirmItemsDTOList;
    }

    private GoodsReceiptPosition getLastGoodsPostion(GoodsReceipt goodsReceipt,String itemId,String lotNo) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT g FROM ");
        sb.append(GoodsReceiptPosition.class.getSimpleName() + " g");
        sb.append(" WHERE g.goodsReceipt = :goodsReceipt");
        if(itemId != null && !"".equalsIgnoreCase(itemId)){
            sb.append(" AND g.itemData = :itemId");
        }
        if(lotNo != null && !"".equalsIgnoreCase(lotNo)){
            sb.append(" AND g.lot = :lotNo");
        }
        sb.append(" ORDER BY g.createdDate DESC");
        Query query = manager.createQuery(sb.toString());
        query.setParameter("goodsReceipt",goodsReceipt);
        if(itemId != null && !"".equalsIgnoreCase(itemId)){
            query.setParameter("itemId",itemId);
        }
        if(lotNo != null && !"".equalsIgnoreCase(lotNo)){
            query.setParameter("lotNo",lotNo);
        }
        List<GoodsReceiptPosition> gps = query.getResultList();
        if(gps != null){
            return gps.get(0);
        }
        return null;
    }

    private List<ReceiptConfirmItemsDTO> getPositions(GoodsReceipt goodsReceipt) {

        List<ReceiptConfirmItemsDTO> receiptConfirmItemsDTOList = new ArrayList<>();
        //获取有有效期商品的信息
        Query query = manager.createQuery("SELECT NEW " + ReceiptPositionResult.class.getName() +
                "(SUM(g.amount),g.itemData,g.lot,g.state)" +
                " FROM " +
                GoodsReceiptPosition.class.getSimpleName() +
                " g" +
                " WHERE 1 = 1 " +
                " GROUP BY g.itemData,g.lot,g.state ");

        List<ReceiptPositionResult> receiptPositionResults = query.getResultList();

        if(receiptPositionResults != null){
            for (int i =0;i < receiptPositionResults.size();i++) {
                ReceiptConfirmItemsDTO receiptConfirmItemsDTO = new ReceiptConfirmItemsDTO();
                receiptConfirmItemsDTO.setInventorySts(receiptPositionResults.get(i).getState());
                //商品单位
                ItemData itemData = itemDataRepository.findOne(receiptPositionResults.get(i).getItemId());
                receiptConfirmItemsDTO.setUnit(itemData.getItemUnit().getName());
                //商品有效期
                Lot lot = lotRepository.getByLotNo(receiptPositionResults.get(i).getLotNo());
                if(lot == null){
                    receiptConfirmItemsDTO.setManufactureDate(null);
                    receiptConfirmItemsDTO.setExpirationDate(null);
                    //收货日期
                    GoodsReceiptPosition position = getLastGoodsPostion(goodsReceipt,receiptPositionResults.get(i).getItemId(),null);
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
//                    try {
                        receiptConfirmItemsDTO.setAgingDate(position.getCreatedDate().toString());
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
                }else {
                    receiptConfirmItemsDTO.setManufactureDate(lot.getProductDate().toString());
                    receiptConfirmItemsDTO.setExpirationDate(lot.getUseNotAfter().toString());
                    //收货日期
                    receiptConfirmItemsDTO.setAgingDate(lot.getLotDate().toString());
                }
                receiptConfirmItemsDTO.setQuantity(receiptPositionResults.get(i).getAmountTotal().intValue());
                receiptConfirmItemsDTO.setItemCode(itemData.getItemNo());
                receiptConfirmItemsDTO.setItemName(itemData.getName());
                receiptConfirmItemsDTO.setLineNo(UUID.randomUUID().toString());
                //获取该批次商品的序列号
                List<ItemDataSerialNumber> ls = acceiptSN(goodsReceipt,lot,receiptPositionResults.get(i).getState());
                String sn = "";
                if(!ls.isEmpty()){
                    for (ItemDataSerialNumber l:ls) {
                        sn = sn + l.getSerialNo() + ",";
                    }
                }
                receiptConfirmItemsDTO.setSnCode(sn);

                receiptConfirmItemsDTOList.add(receiptConfirmItemsDTO);
            }
            return receiptConfirmItemsDTOList;
        }
        return null;
    }

    private List<ItemDataSerialNumber> acceiptSN(GoodsReceipt p,Lot lot,String state) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT i FROM ");
        sb.append(ItemDataSerialNumber.class.getSimpleName() + " i ");
        sb.append(" WHERE i.goodsReceiptId = :goodsReceiptId");

        if(state != null){
            sb.append(" AND i.stockState = :state");
        }
        if(lot != null){
            sb.append(" AND i.productDate = :prodectDate");
        }
        Query query = manager.createQuery(sb.toString());
        query.setParameter("goodsReceiptId",p.getId());

        if(state != null){
            query.setParameter("state",state);
        }
        if(lot != null){
            query.setParameter("prodectDate",lot.getProductDate());
        }

        return query.getResultList();
    }

}
