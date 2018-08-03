package com.mushiny.wms.outboundproblem.query.hql;


import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

@Component
public class SolveDamageQueryHQL {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> searchStorageLocationByItemNo(String itemNo,String sectionId) {
        String sql = "SELECT sl.name as name, COALESCE(sum(su.AMOUNT)-sum(su.RESERVED_AMOUNT),0) " +
                "as amount from INV_STOCKUNIT su " +
                "LEFT JOIN INV_UNITLOAD ul ON su.UNITLOAD_ID = ul.ID " +
                "LEFT JOIN MD_STORAGELOCATION sl ON ul.STORAGELOCATION_ID = sl.id " +
                "LEFT JOIN MD_STORAGELOCATIONTYPE slt ON slt.ID = sl.TYPE_ID " +
                "LEFT JOIN MD_ITEMDATA idt ON su.ITEMDATA_ID = idt.id " +
                "WHERE slt.STORAGETYPE = 'BIN' " +
                "and idt.ITEM_NO = :itemNo " +
                "and sl.SECTION_ID=:sectionId "+
                "and su.amount>0 "+
                "GROUP BY sl.name";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("itemNo", itemNo);
        query.setParameter("sectionId",sectionId);
        List<Object[]> resultList = query.getResultList();
        return resultList;
    }
}
