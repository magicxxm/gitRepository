package wms.service.impl;

import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wms.business.dto.InventoryDTO;
import wms.business.dto.StockUnitDTO;
import wms.common.crud.AccessDTO;
import wms.common.crud.InventoryAccessDTO;
import wms.crud.dto.StockUnitCheckDTO;
import wms.domain.ItemData;
import wms.domain.common.*;
import wms.repository.common.LotRepository;
import wms.service.StockUnitService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 123 on 2017/8/10.
 */
@Service
@Transactional(readOnly = true)
public class StockUnitServiceImpl implements StockUnitService {
    private Logger log = LoggerFactory.getLogger(StockUnitServiceImpl.class);

    @Autowired
    private EntityManager manager;
    @Autowired
    private LotRepository lotRepository;

    @Override
    public InventoryAccessDTO getStock(String warehouseCode, String customerCode, String itemCode,
                                       String inventoryType, String page, String pageNo) {
        log.info("开始查询库存，查询条件是：warehouseCode="+warehouseCode+",customerCode="+customerCode+"" +
                " ,itemCode= "+ itemCode +" ,inventoryType="+inventoryType+", page="+page+", pageNo="+pageNo);

        InventoryAccessDTO dto = new InventoryAccessDTO();
        List<StockUnitDTO> datas = getStockUnitList(warehouseCode,customerCode,itemCode,inventoryType,page,pageNo);
        JSONArray jsonArray = JSONArray.fromObject(datas);

        dto.setCode("0");
        dto.setMessage("Success");
        dto.setCount(datas.size());
        dto.setData(jsonArray.toString());

        return dto;
    }

    @Override
    public AccessDTO getStockUnit(StockUnitCheckDTO stockUnitCheckDTO) {

        log.info("开始库存查询。。。。");
        AccessDTO accessDTO = new AccessDTO();

        String warehouseCode = stockUnitCheckDTO.getWarehouseCode();
        String companyCode = stockUnitCheckDTO.getCompanyCode();
        String itemDatas = stockUnitCheckDTO.getItemCodeArray().trim();
        String inventorySts = stockUnitCheckDTO.getInventoryStsArray().trim();
        //查询所有
        if("".equals(itemDatas) && "".equals(inventorySts)){
            List<StockUnitDTO> stockUnitDTOList = getStockList(warehouseCode,companyCode,null,null);
            JSONArray jsonObject = JSONArray.fromObject(stockUnitDTOList);
            accessDTO.setData(jsonObject.toString());
            accessDTO.setCount(stockUnitDTOList.size());
        }
        //查询多种状态的所有商品
        if("".equals(itemDatas) && !"".equals(inventorySts)){
            String inventoryStsArray[] = stockUnitCheckDTO.getInventoryStsArray().trim().split(",");//库存状态的数组
            List<StockUnitDTO> stockUnitDTOList = new ArrayList<>();
            for (int i = 0;i<inventoryStsArray.length;i++){
//                String state = "";
//                state = exchanage(inventoryStsArray[i]);
                List<StockUnitDTO> dto = getStockList(warehouseCode,companyCode,null,inventoryStsArray[i]);
                for (StockUnitDTO d:dto) {
                    stockUnitDTOList.add(d);
                }
            }
            JSONArray jsonObject = JSONArray.fromObject(stockUnitDTOList);
            accessDTO.setData(jsonObject.toString());
            accessDTO.setCount(stockUnitDTOList.size());
        }
        //查询部分商品的所有状态
        if(!"".equals(itemDatas) && "".equals(inventorySts)){
            String itemCodeArray[] = stockUnitCheckDTO.getItemCodeArray().trim().split(",");//商品编码的数组
            List<StockUnitDTO> stockUnitDTOList = new ArrayList<>();
            for (int i = 0;i < itemCodeArray.length;i++){
                List<StockUnitDTO> dto = getStockList(warehouseCode,companyCode,itemCodeArray[i],null);
                for (StockUnitDTO d:dto) {
                    stockUnitDTOList.add(d);
                }
            }
            JSONArray jsonObject = JSONArray.fromObject(stockUnitDTOList);
            accessDTO.setData(jsonObject.toString());
            accessDTO.setCount(stockUnitDTOList.size());
        }
        //查询部分商品的部分状态
        if(!"".equals(itemDatas) && !"".equals(inventorySts)){
            String inventoryStsArray[] = stockUnitCheckDTO.getInventoryStsArray().trim().split(",");//库存状态的数组
            String itemCodeArray[] = stockUnitCheckDTO.getItemCodeArray().trim().split(",");//商品编码的数组
            List<StockUnitDTO> stockUnitDTOList = new ArrayList<>();
            for (int i = 0;i < itemCodeArray.length; i++){
                for (int j = 0; j < inventoryStsArray.length; j++){
                    List<StockUnitDTO> dto = getStockList(warehouseCode,companyCode,itemCodeArray[i],inventoryStsArray[j]);
                    for (StockUnitDTO d:dto) {
                        stockUnitDTOList.add(d);
                    }
                }
            }
            JSONArray jsonObject = JSONArray.fromObject(stockUnitDTOList);
            accessDTO.setData(jsonObject.toString());
            accessDTO.setCount(stockUnitDTOList.size());
        }
        accessDTO.setMsg("success");
        accessDTO.setCode("0");

        return accessDTO;
    }

    private String exchanage(String s) {
        if("".equalsIgnoreCase(s)){
            return "";
        }

        if("ZP".equalsIgnoreCase(s)){
            return "Inventory";
        }

        if("CP".equalsIgnoreCase(s)){
            return "Damage";
        }

        if("DDC".equalsIgnoreCase(s)){
            return "Pending";
        }

        if("DCL".equalsIgnoreCase(s)){
            return "Measure";
        }

        if("DTZ".equalsIgnoreCase(s)){
            return "Adjust";
        }

        return "Inventory";
    }

    @Override
    public InventoryAccessDTO acceptStockUnit(StockUnitCheckDTO stockUnitCheckDTO) {

        return null;
    }

    private List<StockUnitDTO> getStockList(String warehouseCode, String companyCode, String itemCode,String inventorySts) {

        List<StockUnitDTO> dtos = new ArrayList<>();

        //转换为mushiny的状态
        inventorySts=exchanage(inventorySts);
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT NEW ");
        sb.append(InventoryDTO.class.getName());
        sb.append("(SUM(s.amount),SUM(s.reservedAmount),m.itemNo,s.state,s.lotId)");
        sb.append(" FROM ");
        sb.append(StockUnit.class.getSimpleName()+" s,");
        sb.append(ItemData.class.getSimpleName()+" m,");
        sb.append(UnitLoad.class.getSimpleName()+" u,");
        sb.append(StorageLocation.class.getSimpleName()+" st,");
        sb.append(Warehouse.class.getSimpleName()+" w,");
        sb.append(Client.class.getSimpleName()+" c");
        sb.append(" WHERE s.warehouseId=w.id AND s.clientId=c.id AND s.itemData=m");
        sb.append(" AND s.unitLoad = u AND u.storageLocation = st");
        sb.append(" AND s.entityLock = 0 AND s.amount > 0");
        sb.append(" AND st.storageLocationType IS NOT NULL");
        sb.append(" AND w.warehouseNo=:warehouseCode AND c.clientNo=:companyCode");
        if(!"".equals(inventorySts) && inventorySts != null){
            sb.append(" AND s.state=:state");
        }
        if(!"".equals(itemCode) && itemCode != null){
            sb.append(" AND m.itemNo=:itemCode");
        }

        sb.append(" GROUP BY s.state,m.itemNo,s.lotId");

        Query query = manager.createQuery(sb.toString());
        query.setParameter("warehouseCode",warehouseCode);
        query.setParameter("companyCode",companyCode);
        if(!"".equals(itemCode) && itemCode != null){
            query.setParameter("itemCode",itemCode);
        }
        if(inventorySts != null && !"".equals(inventorySts)){
            query.setParameter("state",inventorySts);
        }

        List<InventoryDTO> list = query.getResultList();

        for (InventoryDTO d:list) {
            if(d.getAmountTotel().compareTo(BigDecimal.ZERO) == 0){
                continue;
            }
            StockUnitDTO dto = new StockUnitDTO();
            dto.setInventoryType(exchanageToAnnto(d.getState()));
            dto.setQuantity(d.getAmountTotel().subtract(d.getAmountReserved()));
            dto.setItemCode(d.getItemNo());
            if(d.getLotId() != null){
                Lot lot = lotRepository.findOne(d.getLotId());
                dto.setManufactureDate(lot.getProductDate());
                dto.setExpirationDate(lot.getUseNotAfter());
            }else {
                dto.setManufactureDate(null);
                dto.setExpirationDate(null);
            }
            dto.setWarehouseCode(warehouseCode);
            dtos.add(dto);
        }
        return dtos;
    }

    private String exchanageToAnnto(String s) {
        if("".equalsIgnoreCase(s)){
            return "";
        }

        if("Inventory".equalsIgnoreCase(s)){
            return "ZP";
        }

        if("Damage".equalsIgnoreCase(s)){
            return "CP";
        }

        if("Pending".equalsIgnoreCase(s)){
            return "DDC";
        }

        if("Measure".equalsIgnoreCase(s)){
            return "DCL";
        }

        if("Adjust".equalsIgnoreCase(s)){
            return "DTZ";
        }

        return "ZP";
    }

    private List<StockUnitDTO> getStockUnitList(String warehouseCode, String customerCode, String itemCode,
                                                String inventoryType, String page, String pageNo) {

        List<StockUnitDTO> dtos = new ArrayList<>();

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT NEW ");
        sb.append(InventoryDTO.class.getName());
        sb.append("(SUM(s.amount),SUM(s.reservedAmount),m.itemNo,s.state)");
        sb.append(" FROM ");
        sb.append(StockUnit.class.getSimpleName()+" s,");
        sb.append(ItemData.class.getSimpleName()+" m,");
        sb.append(Warehouse.class.getSimpleName()+" w,");
        sb.append(Client.class.getSimpleName()+" c");
        sb.append(" WHERE s.warehouseId=w.id AND s.clientId=c.id AND s.itemData=m");
        sb.append(" AND s.entityLock = 0 AND s.amount > 0");
        if(!"".equals(inventoryType) && inventoryType != null){
            sb.append(" AND s.state=:state");
        }
        if(!"".equals(itemCode) && itemCode != null){
            sb.append(" AND m.itemNo=:itemCode");
        }
        sb.append(" AND w.warehouseNo=:warehouseCode AND c.clientNo=:customerCode");
        sb.append(" GROUP BY s.state,m.itemNo");

        Query query = manager.createQuery(sb.toString());
        query.setParameter("warehouseCode",warehouseCode);
        query.setParameter("customerCode",customerCode);
        if(!"".equals(itemCode) && itemCode != null){
            query.setParameter("itemCode",itemCode);
        }
        if(inventoryType != null && !"".equals(inventoryType)){
            query.setParameter("state",inventoryType);
        }
        if(!"".equals(pageNo) && pageNo != null){
            int pageNum = Integer.parseInt(pageNo);
            int p = 1;
            int start = 0;
            if(!"".equals(page) && page != null){
                p = Integer.parseInt(page);
            }
            start = (p-1)*pageNum;
            query.setFirstResult(start);
            query.setMaxResults(pageNum);
        }

        List<InventoryDTO> list = query.getResultList();

        for (InventoryDTO d:list) {
            if(d.getAmountTotel().compareTo(BigDecimal.ZERO) == 0){
                continue;
            }
            StockUnitDTO dto = new StockUnitDTO();
            dto.setInventoryType(d.getState());
            dto.setQuantity(d.getAmountTotel().subtract(d.getAmountReserved()));
            dto.setItemCode(d.getItemNo());
            dto.setWarehouseCode(warehouseCode);
            dtos.add(dto);
        }
        return dtos;
    }
}
