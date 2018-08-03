package com.mushiny.wcs.application.business.score;

import com.mushiny.wcs.application.domain.enums.TripState;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

@Component
public class EnRouteWorkBusiness {

    @PersistenceContext
    private EntityManager entityManager;

    public BigDecimal getStowTripEnRouteWork(String warehouseId) {
        String sql = "SELECT S.SYSTEM_VALUE FROM SYS_SYSTEMPROPERTY S " +
                " WHERE S.SYSTEM_KEY = 'StowPodEnRouteWorkValue' AND S.WAREHOUSE_ID = :warehouseId";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("warehouseId", warehouseId);
        Object result = query.getSingleResult();
        if (result == null) {
            return BigDecimal.ZERO;
        } else {
            return new BigDecimal(String.valueOf(result));
        }
    }

    public BigDecimal getPQATripEnRouteWork(String podId) {
        String sql = "SELECT P.WORKLOAD FROM PQA_ENROUTEPOD P WHERE POD_ID = :podId";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("podId", podId);
        List result = query.getResultList();
        if(CollectionUtils.isEmpty(result))
        {
            return BigDecimal.ZERO;
        }else{
            return (BigDecimal) result.get(0);
        }
    }

    public BigDecimal getPickTripEnRouteWork(String workStationId) {
        BigDecimal enRouteWork = BigDecimal.ZERO;
        List<String> podIds = getPickPods(workStationId);
        if (!podIds.isEmpty()) {
            for (String podId : podIds) {
                BigDecimal podEnRouteWork = sumPodScore(podId);
                enRouteWork = enRouteWork.add(podEnRouteWork);
            }
        }
        return enRouteWork;
    }

    private List<String> getPickPods(String workStationId) {
        String sql = "SELECT t.POD_ID FROM RCS_TRIP t " +
                "    WHERE t.WORKSTATION_ID = :workStationId " +
                "      AND t.TRIP_STATE = :tripState " +
                "      AND t.POD_ID != NULL " +
                "      AND t.POD_ID != (" +
                "        SELECT p.ID FROM MD_POD p " +
                "        WHERE p.PLACEMARK = (" +
                "            SELECT w.PLACEMARK " +
                "            FROM MD_WORKSTATION w " +
                "            WHERE w.ID = :workStationId))";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("tripState", TripState.PROCESS.getName());
        query.setParameter("workStationId", workStationId);
        return query.getResultList();
    }

    private BigDecimal sumPodScore(String podId) {
        String sql = "SELECT SUM(POP.AMOUNT*(3600/PP.TARGET_PICK_RATE)) " +
                "   FROM OB_PICKINGORDERPOSITION POP " +
                "   LEFT JOIN OB_PICKINGORDER PO ON PO.ID = POP.PICKINGORDER_ID " +
                "   LEFT JOIN OB_PROCESSPATH PP ON PP.ID = PO.PROCESSPATH_ID " +
                "   WHERE POP.POD_ID = :podId";
        Query query = entityManager.createNativeQuery(sql, BigDecimal.class);
        query.setParameter("podId", podId);
        List result = query.getResultList();
        if(CollectionUtils.isEmpty(result))
        {
            return BigDecimal.ZERO;
        }else{
            return (BigDecimal) result.get(0);
        }

    }
}
