package com.mushiny.wms.internaltool.query;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.internaltool.common.domain.StockUnitRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Component
public class HistoricalRecordsQuery {

    @PersistenceContext
    private EntityManager entityManager;
    private final ApplicationContext applicationContext;

    @Autowired
    public HistoricalRecordsQuery(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Page<StockUnitRecord> getHistoricalRecords(String storageLocation,
                                                      String sku,
                                                      String createdDate,
                                                      String username,
                                                      String fromStorageLocation,
                                                      String toStorageLocation,
                                                      String recordCode,
                                                      String recordTool,
                                                      String recordType,
                                                      Pageable pageable) {
        String queryHQL = "SELECT s FROM StockUnitRecord s ";
        String countHQL = "SELECT count (s.id) FROM StockUnitRecord s ";
        String whereHql = " WHERE s.warehouseId = '" + applicationContext.getCurrentWarehouse() + "'";
        if (storageLocation != null && !storageLocation.isEmpty()) {
            whereHql = whereHql + " AND (s.fromStorageLocation = '" + storageLocation +
                    "' OR s.toStorageLocation = '" + storageLocation + "')";
        }
        if (sku != null && !sku.isEmpty()) {
            whereHql = whereHql + " AND (s.itemNo = '" + sku +
                    "' OR s.sku = '" + sku + "')";
        }
        if (createdDate != null) {
            whereHql = whereHql + " AND str(s.createdDate) like '%" + createdDate + "%'";
        }
        if (username != null && !username.isEmpty()) {
            whereHql = whereHql + " AND s.operator = '" + username + "'";
        }
        if (fromStorageLocation != null && !fromStorageLocation.isEmpty()) {
            whereHql = whereHql + " AND s.fromStorageLocation = '" + fromStorageLocation + "'";
        }
        if (toStorageLocation != null && !toStorageLocation.isEmpty()) {
            whereHql = whereHql + " AND s.toStorageLocation = '" + toStorageLocation + "'";
        }
        if (recordCode != null && !recordCode.isEmpty()) {
            whereHql = whereHql + " AND s.recordCode = '" + recordCode + "'";
        }
        if (recordTool != null && !recordTool.isEmpty()) {
            whereHql = whereHql + " AND s.recordTool = '" + recordTool + "'";
        }
        if (recordType != null && !recordType.isEmpty()) {
            whereHql = whereHql + " AND s.recordType = '" + recordType + "'";
        }
        if (pageable.getSort() != null) {
            for (Sort.Order order : pageable.getSort()) {
                String orderProperty = "ORDER BY s." + order.getProperty();
                if (order.getDirection().equals(Sort.Direction.DESC)) {
                    queryHQL = queryHQL + whereHql + orderProperty + " DESC";
                } else {
                    queryHQL = queryHQL + whereHql + orderProperty + " ASC";
                }
            }
        } else {
            queryHQL = queryHQL + whereHql + " ORDER BY s.createdDate ";
        }
        countHQL = countHQL + whereHql;
        TypedQuery<StockUnitRecord> query = entityManager.createQuery(queryHQL, StockUnitRecord.class);
        TypedQuery<Long> count = entityManager.createQuery(countHQL, Long.class);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List<StockUnitRecord> stockUnitRecords = query.getResultList();
        Long countSize = count.getSingleResult();
        return new PageImpl<>(stockUnitRecords, pageable, countSize);
    }
}
