package com.mushiny.repository;

import com.mushiny.model.*;
import com.mushiny.utils.StockStateUtil;
import org.springframework.data.jpa.repository.JpaContext;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by 123 on 2018/2/8.
 */
public class StockUnitRepositoryImpl implements StockUnitRepositoryCustom {

    private final EntityManager manager;

    public StockUnitRepositoryImpl(JpaContext jpaContext){
        this.manager = jpaContext.getEntityManagerByManagedType(StockUnit.class);
    }


    @Override
    public BigDecimal getAmountByUnitLoad(UnitLoad unitLoad, Warehouse warehouse) {

        Query query = manager.createQuery("SELECT SUM(s.amount) FROM " +
                StockUnit.class.getSimpleName() +
                " s " +
                " WHERE s.unitLoad = :unitLoad AND s.warehouseId = :warehouseId ");

        query.setParameter("unitLoad",unitLoad);
        query.setParameter("warehouseId",warehouse.getId());
        BigDecimal amount = (BigDecimal)query.getSingleResult();
        return amount;
    }

    @Override
    public List<StockUnit> getStockUnitList(String clientId, LocalDate endDate, String itemNo, String stockState,String batchNo,String warehoueId) {
        StringBuffer sb =  new StringBuffer();
        sb.append("SELECT s FROM ");
        sb.append(StockUnit.class.getSimpleName() + " s,");
        if(endDate != null) {
            sb.append(Lot.class.getSimpleName() + " l, ");
        }
        sb.append(ItemData.class.getSimpleName() + " i");
        sb.append(" WHERE s.itemData = i AND i.itemNo = :itemNo AND s.state = :state ");
        if(endDate != null){
            sb.append(" AND s.lot = l AND l.bestBeforeEnd = :endDate ");
        }
        sb.append(" AND s.clientId = :clientId ");
        sb.append(" AND s.warehouseId = :warehouseId");
        if(batchNo != null && !"".equalsIgnoreCase(batchNo)){
            sb.append(" AND s.batchOrder = :batchNo ");
        }

        sb.append(" ORDER BY s.amount DESC");
        Query query = manager.createQuery(sb.toString());

        query.setParameter("itemNo",itemNo);
        query.setParameter("state", stockState);
        query.setParameter("clientId",clientId);
        query.setParameter("warehouseId",warehoueId);
        if(endDate != null){
            query.setParameter("endDate",endDate);
        }
        if(batchNo != null && !"".equalsIgnoreCase(batchNo)){
            query.setParameter("batchNo",batchNo);
        }

        return query.getResultList();
    }
}
