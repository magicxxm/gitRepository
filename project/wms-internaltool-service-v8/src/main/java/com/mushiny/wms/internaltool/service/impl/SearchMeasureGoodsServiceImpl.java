package com.mushiny.wms.internaltool.service.impl;

import com.mushiny.wms.internaltool.common.domain.MeasureRecord;
import com.mushiny.wms.internaltool.service.SearchMeasureGoodsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SearchMeasureGoodsServiceImpl implements SearchMeasureGoodsService {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<MeasureRecord> getBySearchTerm(String searchTerm, String startDate, String endDate) {
        String hql = "SELECT m FROM MeasureRecord m LEFT JOIN m.client c WHERE 1 = 1 ";
        if (startDate != null && !startDate.equals("")) {
            hql = hql + " AND  str(m.createdDate) > '" + startDate + "'";
        }
        if (endDate != null && !endDate.equals("")) {
            hql = hql + " AND  str(m.createdDate) < '" + endDate + "'";
        }
        if (searchTerm != null && !searchTerm.equals("")) {
            hql = hql + " AND (m.sku LIKE '%" + searchTerm + "%' " +
                    " OR m.itemNo LIKE '%" + searchTerm + "%' " +
                    " OR m.fromStorageLocation LIKE '%" + searchTerm + "%' " +
                    " OR m.createdBy LIKE '%" + searchTerm + "%') " +
                    " OR c.name LIKE '%" + searchTerm + "%' )";
        }
        TypedQuery<MeasureRecord> query = entityManager.createQuery(hql, MeasureRecord.class);
        return query.getResultList();
    }
}
