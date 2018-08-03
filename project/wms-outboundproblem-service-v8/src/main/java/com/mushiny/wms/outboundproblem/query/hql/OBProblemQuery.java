package com.mushiny.wms.outboundproblem.query.hql;

import com.mushiny.wms.outboundproblem.crud.dto.OBPCheckStateDTO;
import com.mushiny.wms.outboundproblem.domain.OBPCheckState;
import com.mushiny.wms.outboundproblem.domain.OBPSolve;
import com.mushiny.wms.outboundproblem.domain.OBProblemCheck;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class OBProblemQuery {

    @PersistenceContext
    private EntityManager entityManager;

    /* 根据itemNo，problem 汇总 上货历史记录的上货数量 */
    public List<Object[]> sumStockunitRecordByPoblem(String itemNo, String problem,String jobType) {

        String sql = " SELECT  DISTINCT     " +
                "         SL.ID          AS storageLocationId,          " +
                "         SL.NAME        AS name,     " +
                "         COALESCE(C.amount,0)       AS amount,     " +
                "         COALESCE (B.actualAmount,0) AS actualAmount,     " +
                "         COALESCE(A.total_amuont,0) AS totalAmount,     " +
                "         B.clientId     AS clientId, "   +
                "         A.clientName   AS clientName,     " +
                "         B.lotId        AS lotId, " +
                "         B.itemId       AS itemDataId     " +
                "        FROM          " +
//                "                      #   容器的历史上货容器          " +
                "        (SELECT DISTINCT SUR.FROM_STORAGELOCATION AS name     " +
                "        FROM INV_STOCKUNITRECORD SUR          " +
                "          LEFT JOIN INV_UNITLOAD U ON SUR.TO_UNITLOAD = U.LABEL     " +
                "        WHERE          " +
                "          U.ENTITY_LOCK = 1     " +
                "          AND SUR.TO_STORAGELOCATION = :problem " +
                "          AND SUR.RECORD_TOOL IN ('Pick' ,'Rebin','Pack') ) L     " +
                "       LEFT JOIN MD_STORAGELOCATION SL ON L.name = SL.NAME          " +
                "       LEFT JOIN MD_STORAGELOCATIONTYPE SLY ON SL.TYPE_ID = SLY.ID     " +
                "       LEFT JOIN          " +
//                "                            # 货位商品当前总数    " +
                "       (SELECT DISTINCT     " +
                "          SL.ID         AS storageLocationId,          " +
                "          C.NAME        AS clientName,    " +
                "          SUM(S.AMOUNT) AS total_amuont          " +
                "        FROM INV_STOCKUNIT S          " +
                "          LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID          " +
                "          LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID          " +
                "          LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID          " +
                "        WHERE 1 =1     " +
                "        GROUP BY SL.ID,C.NAME ) A ON SL.ID = A.storageLocationId     " +
                "       LEFT JOIN          " +
                "       (          " +
//                "                              # 货位商品当前数量          " +
                "         SELECT DISTINCT     " +
                "           SL.ID         AS storageLocationId,     " +
                "           C.ID          AS clientId, " +
                "           C.NAME        AS clientName,     " +
                "           L.ID          AS lotId, " +
                "           I.ID          AS itemId,          " +
                "           SUM(S.AMOUNT) AS actualAmount          " +
                "         FROM INV_STOCKUNIT S          " +
                "           LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID          " +
                "           LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID          " +
                "           LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID          " +
                "           LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID          " +
                "           LEFT JOIN INV_LOT L ON S.LOT_ID = L.ID          " +
                "         WHERE 1 = 1          " +
                "               AND I.ITEM_NO = :itemNo     " +
                "         GROUP BY SL.ID, I.ID, C.ID, C.NAME, L.ID          " +
                "       ) B          " +
                "         ON A.storageLocationId = B.storageLocationId     " +
                "       LEFT JOIN          " +
                "       (          " +
//                "                              #当前容器,当前商品的上货历史记录数据          " +
                "         SELECT DISTINCT     " +
                "           SUR.FROM_STORAGELOCATION AS storageLocationName,          " +
                "           I.ID                   AS itemId,          " +
                "           SUM(SUR.AMOUNT)        AS amount     " +
                "         FROM INV_STOCKUNITRECORD SUR          " +
                "           LEFT JOIN INV_UNITLOAD U ON SUR.TO_UNITLOAD = U.LABEL     " +
                "           LEFT JOIN MD_ITEMDATA I ON SUR.ITEMDATA_ITEMNO = I.ITEM_NO          " +
                "         WHERE     " +
                "           SUR.TO_STORAGELOCATION = :problem     " +
//                "           AND SUR.AMOUNT > 0     " +
                "           AND U.ENTITY_LOCK = 1     " +
                "           AND U.STORAGELOCATION_ID IS NOT NULL     " +
                "           AND SUR.ITEMDATA_ITEMNO = :itemNo     " +
                "         GROUP BY SUR.FROM_STORAGELOCATION,     " +
                "           SUR.ITEMDATA_ITEMNO, I.ID     " +
                "       ) C          " +
                "         ON SL.NAME = C.storageLocationName          " +
                "       WHERE SLY.STORAGETYPE ='BIN'     " +
                "           AND B.itemId = C.itemId          " +
//                "       WHERE SLY.STORAGETYPE ='BIN'     " +
//                "        AND A.total_amuont >0     " +
                "     ORDER BY name DESC ";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("itemNo", itemNo);
        query.setParameter("problem", problem);
//        query.setParameter("jobType", jobType);
        List<Object[]> entities = query.getResultList();
        return entities;
    }

    public List<OBProblemCheck> getBySeek(String state, String userName, String seek,
                                          String startDate, String endDate) {
        String sql = "SELECT  I " +
                " FROM OBProblemCheck AS I " +
                " LEFT JOIN I.itemData ips " +
//                " WHERE  (I.problemType = 'MORE' or I.problemType = 'LOSE') " +
                " WHERE I.state LIKE :state  " +
                "      AND coalesce(I.solvedBy, '') LIKE :userName " +
                "      AND concat(coalesce(I.itemNo, ''), " +
                "                 coalesce(ips.itemNo, ''),  " +
                "                 coalesce(I.problemType, ''), " +
                "                 coalesce(I.reportBy, ''), " +
                "                 coalesce(I.skuNo,''), " +
                "                 coalesce(I.solvedBy,''), " +
                "                 coalesce(I.problemStoragelocation,'') " +
                "      )LIKE :seek ";
        if (startDate != null && !startDate.equals("")) {
            sql = sql + " AND I.reportDate >= :startDate ";
        }
        if (endDate != null && !endDate.equals("")) {
            sql = sql + " AND I.reportDate <= :endDate ";
        }
        sql = sql + " ORDER BY I.reportDate DESC ";

        Query query = entityManager.createQuery(sql);

        if (state == null || state.equals("")) {
            state = "%";
        }
        if (userName == null || userName.equals("")) {
            userName = "%";
        }

        if (startDate != null && !startDate.equals("")) {
            query.setParameter("startDate", LocalDateTime.parse(startDate));
        }
        if (endDate != null && !endDate.equals("")) {
            query.setParameter("endDate", LocalDateTime.parse(endDate));
        }
        seek = "%" + seek + "%";
        query.setParameter("state", state);
        query.setParameter("userName", userName);
        query.setParameter("seek", seek);
        List<OBProblemCheck> list = query.getResultList();

        return list;
    }

    public List<OBPCheckState> getSolveBySeek(String state, String userName, String seek,
                                              String startDate, String endDate) {
        String sql = "SELECT IPS " +
                "FROM OBPCheckState AS IPS " +
                " LEFT JOIN IPS.obProblem AS ob " +
                " LEFT JOIN IPS.obProblem.itemData AS ipd " +
                " LEFT JOIN IPS.storageLocation AS st " +
                " LEFT JOIN IPS.inboundProblemRule AS br " +
                "WHERE IPS.state LIKE :state " +
                "  AND coalesce(ob.solvedBy,'') LIKE :userName " +
                "  AND concat( " +
                "        coalesce(ob.itemNo,''), " +
                "        coalesce(ipd.itemNo, ''), " +
                "        coalesce(st.name,''), " +
                "        coalesce(ob.problemStoragelocation,''),  " +
                "        coalesce(ob.createdDate,''), " +
                "        coalesce(ob.skuNo,''), " +
                "        coalesce(br.description,''), " +
                "        coalesce(IPS.createdBy, ''), " +
                "        coalesce(IPS.solveBy,'') " +
                "      )LIKE :seek ";

        if (startDate != null && !startDate.equals("")) {
            sql = sql + " AND ob.reportDate >= :startDate ";
        }
        if (endDate != null && !endDate.equals("")) {
            sql = sql + " AND ob.reportDate <= :endDate ";
        }
        if (state == null || state.equals("")) {
            state = "%";
        }
        if (userName == null || userName.equals("")) {
            userName = "%";
        }
        sql = sql + " ORDER BY ob.reportDate ";

        Query query = entityManager.createQuery(sql);
        if (startDate != null && !startDate.equals("")) {
            query.setParameter("startDate", LocalDateTime.parse(startDate));
        }
        if (endDate != null && !endDate.equals("")) {
            query.setParameter("endDate", LocalDateTime.parse(endDate));
        }
        seek = "%" + seek + "%";
        query.setParameter("state", state);
        query.setParameter("userName", userName);
        query.setParameter("seek", seek);

        List<OBPCheckState> entities = query.getResultList();
        return entities;
    }
    //rebin车记录
    public List<Object[]> getByObproblem(String problemLocation,String itemNo,String jobType){
//        String sql = "SELECT su.ITEMDATA_ITEMNO               AS ItemNo," +
//                "            su.ITEMDATA_SKU                  AS ItemSku," +
//                "            su.TO_STORAGELOCATION            AS Wall, " +
////                "            left(su.TO_STORAGELOCATION, 8)   AS Wall, " +
//                "            right(su.TO_STORAGELOCATION, 4)  AS Cell, " +
//                "            ob.AMOUNT                        AS Amount," +
//                "            SUM(su.AMOUNT)                   AS RebinNumber " +
//                " FROM INV_STOCKUNITRECORD su " +
//                " LEFT JOIN INV_UNITLOAD ul ON su.FROM_UNITLOAD = ul.LABEL " +
//                " LEFT JOIN MD_STORAGELOCATION sl ON ul.STORAGELOCATION_ID = sl.ID " +
//                " LEFT JOIN OBP_OBPROBLEM ob ON su.TO_STORAGELOCATION = ob.PROBLEM_STORAGELOCATION " +
//                " WHERE ul.ENTITY_LOCK " +
//                " AND su.FROM_STORAGELOCATION = :problemLocation " +
//                " GROUP BY su.ITEMDATA_ITEMNO,su.ITEMDATA_SKU,su.TO_STORAGELOCATION,ob.AMOUNT ";

        String sql = " SELECT  DISTINCT   " +
                "  B.ItemNo               AS ItemNo,   " +
                "  B.ItemSku                  AS ItemSku,   " +
                "  SL.NAME        AS Wall,   " +
                "  right(L.TO_STORAGELOCATION, 4)  AS Cell,   " +
                "  COALESCE (B.actualAmount,0) AS Amount,   " +
                "  COALESCE(A.total_amuont,0) AS RebinNumber   " +
                " FROM   " +
//                "  #   容器的历史上货容器   " +
                "  (SELECT DISTINCT SUR.TO_STORAGELOCATION AS TO_STORAGELOCATION  " +
//                "     SUR.ITEMDATA_ITEMNO               AS ITEMDATA_ITEMNO,   " +
//                "     SUR.ITEMDATA_SKU                  AS ITEMDATA_SKU   " +
                "   FROM INV_STOCKUNITRECORD SUR   " +
                "     LEFT JOIN INV_UNITLOAD U ON ((SUR.FROM_UNITLOAD = U.ID) OR (SUR.FROM_UNITLOAD = U.LABEL))   " +
                "   WHERE   " +
                "     U.ENTITY_LOCK = 1   " +
                "     AND SUR.FROM_STORAGELOCATION = :problemLocation AND SUR.RECORD_TOOL = 'Rebin') L   " +
                "  LEFT JOIN MD_STORAGELOCATION SL ON L.TO_STORAGELOCATION = SL.NAME   " +
                "  LEFT JOIN   " +
//                "  # 货位中各商品的当前总数量   " +
                "  (SELECT   " +
                "     SL.ID         AS storageLocationId ,   " +
                "     SUM(S.AMOUNT) AS total_amuont   " +
                "   FROM INV_STOCKUNIT S   " +
                "     LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID   " +
                "     LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID   " +
                "   WHERE 1 =1 " +
//                "   S.AMOUNT > 0   " +
                "   GROUP BY SL.ID ) A ON SL.ID = A.storageLocationId   " +
                "  LEFT JOIN   " +
                "  (   " +
//                "    # 货位中相应商品的当前数量   " +
                "    SELECT   " +
                "      SUM(S.AMOUNT) AS actualAmount,   " +
                "      I.ITEM_NO     AS ItemNo, " +
                "      I.SKU_NO AS ItemSku, "  +
                "      SL.ID AS storageLocationId   " +
                "    FROM INV_STOCKUNIT S   " +
                "      LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID   " +
                "      LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID   " +
                "      LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID   " +
                "    WHERE 1 = 1" +
//                " S.AMOUNT > 0   " +
                "          AND I.ITEM_NO = :itemNo   " +
                "    GROUP BY storageLocationId,ItemSku,ItemNo   " +
                "  ) B   " +
                "    ON A.storageLocationId = B.storageLocationId   " +
                " ORDER BY ItemNo, ItemSku,Wall,Cell,Amount,RebinNumber DESC   ";
        
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("problemLocation", problemLocation);
        query.setParameter("itemNo",itemNo);
//        query.setParameter("jobType",jobType);
        List<Object[]> entities = query.getResultList();
        return entities;
    }
    //对应调整 Rebin
    public List<Map> getByRecordRebin(String storageLocation,String itemNo){
        String sql = "SELECT DISTINCT    " +
                "  obr.REBINFROMCONTAINER_NAME AS problemStoragelocation,    " +
                "  obr.AMOUNT_REBINED        AS amount,    " +
                "  po.PICKINGORDER_NO        AS batchId,    " +
                "  ul.NAME                   AS stationName,    " +
                "  u.USERNAME                AS rebinUser,    " +
                "  c.NAME                    AS customer,    " +
                "  br.MODIFIED_DATE          AS rebinDate    " +
                " FROM OB_REBINREQUESTPOSITION obr    " +
                " LEFT JOIN MD_ITEMDATA it ON obr.ITEMDATA_ID = it.ID    " +
                " LEFT JOIN OB_REBINREQUEST br ON obr.REBINREQUEST_ID = br.ID    " +
                " LEFT JOIN OB_PICKINGORDER po ON br.PICKINGORDER_ID = po.ID    " +
                " LEFT JOIN OB_REBINSTATION ul ON br.REBINSTATION_ID = ul.ID    " +
                " LEFT JOIN SYS_USER u ON br.OPERATOR_ID = u.ID    " +
                " LEFT JOIN SYS_CLIENT c ON obr.CLIENT_ID = c.ID    " +
                " WHERE it.ITEM_NO = :itemNo    " +
                "  AND obr.REBINFROMCONTAINER_NAME = :storageLocation    " +
                "  AND obr.AMOUNT_REBINED >0 ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("storageLocation", storageLocation);
        query.setParameter("itemNo", itemNo);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> entities = query.getResultList();
        return entities;
    }
    // 对应调整 Pack
    public List<Map> getByRecordPack(String storageLocation,String itemNo){
        String sql = "  SELECT DISTINCT   " +
                "   sl.NAME                   AS storagelocation,    " +
                "   obr.AMOUNT_PACKED        AS amount,    " +
                "   POP.PICKINGORDER_NO        AS batchId,    " +
                "   ul.NAME                   AS stationName,    " +
                "   u.USERNAME                AS rebinUser,    " +
                "   c.NAME                    AS customer,    " +
                "   br.MODIFIED_DATE          AS rebinDate    " +
                "FROM OB_PACKINGREQUESTPOSITION obr    " +
                "LEFT JOIN MD_ITEMDATA it ON obr.ITEMDATA_ID = it.ID    " +
                "LEFT JOIN OB_PACKINGREQUEST br ON obr.PACKINGREQUEST_ID = br.ID    " +
                "LEFT JOIN INV_UNITLOAD ud ON br.FROMUNITLOAD_ID = ud.ID    " +
                "LEFT JOIN MD_STORAGELOCATION sl ON ud.STORAGELOCATION_ID = sl.ID    " +
                "LEFT JOIN OB_PICKINGORDERPOSITION POP ON obr.CUSTOMERSHIPMENTPOSITION_ID = POP.CUSTOMERSHIPMENTPOSITION_ID    " +
                "LEFT JOIN OB_PACKINGSTATION ul ON br.PACKINGSTATION_ID = ul.ID    " +
                "LEFT JOIN SYS_USER u ON br.OPERATOR_ID = u.ID    " +
                "LEFT JOIN SYS_CLIENT c ON obr.CLIENT_ID = c.ID    " +
                "WHERE it.ITEM_NO = :itemNo    " +
                "      AND sl.NAME = :storageLocation ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("storageLocation", storageLocation);
        query.setParameter("itemNo", itemNo);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> entities = query.getResultList();
        return entities;
    }
    public List<Map> getPodFace(String storageLocationId,String sectionId){
        String sql = "  select distinct  " +
                " concat(p.NAME,sl.FACE) as podFace, " +
                " p.ID  AS podId, " +
                " sl.FACE AS face " +
                " from MD_STORAGELOCATION sl  " +
                " left join MD_POD p on sl.POD_ID = p.ID   " +
                " where sl.POD_ID is not null  " +
                " and sl.ID = :storageLocationId " +
                " and p.SECTION_ID = :sectionId " +
                "  and p.STATE = 'Available' " +
                "  and p.PLACEMARK > 0 " +
                " order by podFace ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("storageLocationId", storageLocationId);
        query.setParameter("sectionId", sectionId);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> entities = query.getResultList();
        return entities;

    }
}