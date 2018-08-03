package com.mushiny.wms.outboundproblem.query;


import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.outboundproblem.domain.common.Pod;
import com.mushiny.wms.outboundproblem.query.hql.SolveDamageQueryHQL;
import com.mushiny.wms.outboundproblem.repository.common.PodRepository;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SolveDamageQuery {

    @PersistenceContext
    private EntityManager entityManager;

    private final SolveDamageQueryHQL solveDamageQueryHQL;
    private final PodRepository podRepository;
    private final ApplicationContext applicationContext;

    @Autowired
    public SolveDamageQuery(SolveDamageQueryHQL solveDamageQueryHQL, PodRepository podRepository, ApplicationContext applicationContext) {
        this.solveDamageQueryHQL = solveDamageQueryHQL;
        this.podRepository = podRepository;
        this.applicationContext = applicationContext;
    }

    public List<Map> searchStorageLocationByItemNo(String itemNo,String sectionId) {
        List<Map> maps = new ArrayList<>();
        List<Object[]> lists = solveDamageQueryHQL.searchStorageLocationByItemNo(itemNo,sectionId);
        for ( int i = 0; i < lists.size(); i ++ ) {
            HashMap<String, Object> map = new HashMap<>();
            Pod pod = podRepository.getByName(applicationContext.getCurrentWarehouse(),String.valueOf(lists.get(i)[0]).substring(0,8));
            if(((BigDecimal)lists.get(i)[1]).compareTo(BigDecimal.ZERO)>0&&pod.getPlaceMark()>0){
                map.put("name",lists.get(i)[0]);
                map.put("amount",lists.get(i)[1]);
                maps.add(map);
            }
        }
        return maps;
    }

    public List<Map> getPodFace(String name,String warehouseId) {
        String sql = "select DISTINCT p.ID  as podId ," +
                " sl.FACE as face " +
                " from MD_STORAGELOCATION sl  " +
                " left join MD_POD p on sl.POD_ID = p.ID  " +
                " where sl.POD_ID is not null  " +
                " and sl.NAME = :name  " +
                " and sl.WAREHOUSE_ID=:warehouseId";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("name", name);
        query.setParameter("warehouseId",warehouseId);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> maps = query.getResultList();
        return maps;
    }
}
