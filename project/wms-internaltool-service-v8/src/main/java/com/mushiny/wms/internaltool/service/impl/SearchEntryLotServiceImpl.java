package com.mushiny.wms.internaltool.service.impl;

import com.mushiny.wms.internaltool.common.domain.LotRecord;
import com.mushiny.wms.internaltool.service.SearchEntryLotService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SearchEntryLotServiceImpl implements SearchEntryLotService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<LotRecord> getBySearchTerm(String searchTerm, String startDate, String endDate) {
        String hql = "SELECT l FROM LotRecord l LEFT JOIN l.client c WHERE 1 = 1 ";
        if (startDate != null && !startDate.equals("")) {
            hql = hql + " AND  str(l.createdDate) > '" + startDate + "'";
        }
        if (endDate != null && !endDate.equals("")) {
            hql = hql + " AND  str(l.createdDate) < '" + endDate + "'";
        }
        if (searchTerm != null && !searchTerm.equals("")) {
            hql = hql + " AND (l.sku LIKE '%" + searchTerm + "%' " +
                    " OR l.itemNo LIKE '%" + searchTerm + "%' " +
                    " OR l.fromStorageLocation LIKE '%" + searchTerm + "%' " +
                    " OR l.createdBy LIKE '%" + searchTerm + "%') " +
                    " OR c.name LIKE '%" + searchTerm + "%' )";
        }
        TypedQuery<LotRecord> query = entityManager.createQuery(hql, LotRecord.class);
        return query.getResultList();
    }
}
