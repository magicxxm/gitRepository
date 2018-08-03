package com.mushiny.wms.outboundproblem.query.hql;


import com.mushiny.wms.outboundproblem.business.dto.ForceDeleteShipmentDTO;
import com.mushiny.wms.outboundproblem.business.utils.StringDateUtil;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DeleteShipmentQueryHQL {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> queryForceDeleteShipment(String startDate, String endDate, String shipmentNo, String state,String obpStationId) {
        String hql = "select sp.shipmentNo as shipmentNo, sp.obpSolve.customerShipment.deliveryDate as exSD, sp.solveDate as solveDate, " +
                "sp.description as description, sp.solveBy as solveBy,sp.locationContainer as container,sp.state as state from OBPSolvePosition sp " +
                "where sp.solveKey = 'DELETE_ORDER_FORCE' and sp.obpSolve.obpStation.id=:obpStationId";
        if (!startDate.isEmpty()) {
            hql = hql + " and sp.solveDate >= :startDateTime ";
        }
        if (!endDate.isEmpty()) {
            hql = hql + " and sp.solveDate <= :endDateTime ";
        }
        if (!shipmentNo.isEmpty()) {
            hql = hql + " and sp.shipmentNo = :shipmentNo ";
        }
        if (!state.isEmpty()) {
            hql = hql + " and sp.state = :state ";
        }
        hql = hql + " order by exSD desc ";
//        TypedQuery<ForceDeleteShipmentDTO> query = entityManager.createQuery(hql, ForceDeleteShipmentDTO.class);
        Query query = entityManager.createQuery(hql);
        if (!startDate.isEmpty()) {
            LocalDateTime startDateTime  = StringDateUtil.getLocaDatetimeByString(startDate);
            query.setParameter("startDateTime", startDateTime);
        }
        if (!endDate.isEmpty()) {
            LocalDateTime endDateTime  = StringDateUtil.getLocaDatetimeByString(endDate);
            query.setParameter("endDateTime", endDateTime);
        }
        if (!shipmentNo.isEmpty()) {
            query.setParameter("shipmentNo", shipmentNo);
        }
        if (!state.isEmpty()) {
            query.setParameter("state", state);
        }
        query.setParameter("obpStationId",obpStationId);
        List<Object[]> list = query.getResultList();
        return list;
    }
}
