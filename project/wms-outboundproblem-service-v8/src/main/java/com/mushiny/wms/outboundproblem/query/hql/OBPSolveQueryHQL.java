package com.mushiny.wms.outboundproblem.query.hql;


import com.mushiny.wms.outboundproblem.domain.OBPSolve;
import com.mushiny.wms.outboundproblem.domain.common.ItemDataSerialNumber;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OBPSolveQueryHQL {

    @PersistenceContext
    private EntityManager entityManager;

    public List<OBPSolve> queryProblem(String warehouseId, String obpStationId, String obpWallId, String shipmentNo, String state) {
        String hql = "select o from OBPSolve o where o.warehouseId = :warehouseId and o.obproblem != null and o.obpCell !=null ";
        if (!obpStationId.isEmpty()) {
            hql = hql + " and o.obpStation.id = :obpStationId ";
        }
        if (!obpWallId.isEmpty()) {
            hql = hql + " and o.obpWall.id = :obpWallId ";
        }
        if (!shipmentNo.isEmpty()) {
            hql = hql + " and o.customerShipment.shipmentNo = :shipmentNo ";
        }
        if (!state.isEmpty()) {
            hql = hql + " and o.state = :state ";
        }
        hql = hql + "order by o.obproblem.createdDate desc ";
        TypedQuery<OBPSolve> query = entityManager.createQuery(hql, OBPSolve.class);
        query.setParameter("warehouseId", warehouseId);
        if (!obpStationId.isEmpty()) {
            query.setParameter("obpStationId", obpStationId);
        }
        if (!obpWallId.isEmpty()) {
            query.setParameter("obpWallId", obpWallId);
        }
        if (!shipmentNo.isEmpty()) {
            query.setParameter("shipmentNo", shipmentNo);
        }
        if (!state.isEmpty()) {
            query.setParameter("state", state);
        }
        return query.getResultList();
    }

    public List<OBPSolve> queryProblemByShipment(String warehouseId, String obpStationId, String obpWallId, String shipmentNo, String state) {
        String hql = "select o from OBPSolve o where o.warehouseId = :warehouseId " +
                "and o.obpStation.id =:obpStationId and o.obpWall.id =:obpWallId and o.state=:state ";
//                "and o.obproblem != null "; //为了查询出已处理完成的客户删单的订单
        if (!shipmentNo.isEmpty()) {
            hql = hql + " and o.customerShipment.shipmentNo = :shipmentNo ";
        }
       // hql = hql + "order by o.obproblem.createdDate desc ";
        TypedQuery<OBPSolve> query = entityManager.createQuery(hql, OBPSolve.class);
            query.setParameter("warehouseId", warehouseId);

            query.setParameter("obpStationId", obpStationId);

            query.setParameter("obpWallId", obpWallId);
            if(!shipmentNo.isEmpty()) {
               query.setParameter("shipmentNo", shipmentNo);
            }
            query.setParameter("state", state);

            return query.getResultList();
    }

    public List<Object[]> queryShipment(String shipmentNo) {
        String sql = "select c.SHIPMENT_ID as shipmentId, c.ITEMDATA_ID as itemId, SUM(c.AMOUNT) as amount, c.WAREHOUSE_ID as warehouseId " +
                "from OB_CUSTOMERSHIPMENTPOSITION c LEFT JOIN OB_CUSTOMERSHIPMENT cs ON c.SHIPMENT_ID = cs.ID " +
                "where cs.SHIPMENT_NO = :shipmentNo " +
                "group by c.SHIPMENT_ID,c.ITEMDATA_ID,c.WAREHOUSE_ID";

//        TypedQuery<Map> query = entityManager.createQuery(hql,Map.class);
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("shipmentNo", shipmentNo);
        List<Object[]> resultList = query.getResultList();
        return resultList;
    }

    public ItemDataSerialNumber checkSerialNo(String serialNo) {
        String hql = "select i from ItemDataSerialNumber i where i.serialNo = :serialNo";

        TypedQuery<ItemDataSerialNumber> query = entityManager.createQuery(hql,ItemDataSerialNumber.class);
        query.setParameter("serialNo", serialNo);
        return query.getSingleResult();
    }

    public List<String> getCellByWallId(String warehouseId,String obpWallId){
        String sql = "select DISTINCT o.CELL_ID from OBP_OBPSOLVE o,OBP_OBPSOLVEPOSITION p where o.ID=p.SOLVE_ID " +
                "and o.WALL_ID=:obpWallId and o.WAREHOUSE_ID=:warehouseId " +
                "and p.SOLVE_KEY='ASSIGNED_LOCATION'";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("warehouseId", warehouseId);
        query.setParameter("obpWallId", obpWallId);
        List<String> resultList = query.getResultList();
        return resultList;
    }

    public List<Object[]> getStorageLocationBySolveKey(String warehouseId,String obpWallId,String cellId){
        String sql="select p.AMOUNT,p.AMOUNT_SCANED,p.location,p.ISCALLPOD " +
                "from OBP_OBPLOCATION p " +
                "inner join OBP_OBPSOLVE obp on obp.ID=p.SOLVE_ID " +
                "inner join OBP_OBPSOLVEPOSITION o on o.SOLVE_ID=obp.ID " +
                "and obp.CELL_ID=:cellId " +
                "and obp.WALL_ID=:obpWallId " +
                "and obp.WAREHOUSE_ID=:warehouseId " +
                "and o.SOLVE_KEY='ASSIGNED_LOCATION'";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("warehouseId", warehouseId);
        query.setParameter("obpWallId", obpWallId);
        query.setParameter("cellId", cellId);
        List<Object[]> resultList = query.getResultList();
        return resultList;
    }
}
