package com.mushiny.wms.report.query.hql;

import com.mushiny.wms.report.query.dto.CapacityPodDTO;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Component
public class CapacitySideQuery implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public List<CapacityPodDTO> getByPodName(String podName) {
        String sql = "SELECT " +
                "  concat(P.podName,P.face)          AS podName, " +
                "  P.podType                         AS podType, " +
                "  P.location                        AS location, " +
                "  P.face                            AS face, " +
                "  A.itemTotalAmount                 AS itemTotalAmount, " +
                "  C.skuNoUnit                       AS skuNoUnit, " +
                "  B.itemNoUnit                      AS itemNoUnit, " +
                "  coalesce(A.itemTotalM3,0)/1000000000         AS itemTotalM3, " +
                "  coalesce(D.binTotalM3,0)/1000000000          AS binTotalM3, " +
                "  coalesce(E.binNotNullTotalM3, 0)/1000000000  AS binNotNullTotalM3, " +
                "  coalesce(F.binNullTotalM3,0)/1000000000      AS binNullTotalM3, " +
                "  coalesce(D.binTotal,0)                       AS binTotal, " +
                "  coalesce(E.binNotNullTotal, 0)    AS binNotNullTotal, " +
                "  coalesce(F.binNullTotal,0)        AS binNullTotal, " +
                "  coalesce(G.bufferBinNullTotal, 0) AS bufferBinNullTotal " +
                "FROM " +
                "  (SELECT DISTINCT " +
                "     pod.NAME      AS podName, " +
                "     podt.NAME     AS podType, " +
                "     pod.PLACEMARK AS location, " +
                "     potp.FACE     AS face " +
                "   FROM MD_POD pod " +
                "     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID " +
                "     LEFT JOIN MD_PODTYPEPOSITION potp ON podt.ID = potp.PODTYPE_ID " +
                "   ) P " +
                "  LEFT JOIN " +
//                "  # 获取Pod 名字，类型，当前位置，Pod中-> 所有面 商品总体积 + 个数 " +
                "  (SELECT " +
                "     pod.NAME                     AS podName, " +
                "     sl.FACE                      AS face, " +
                "     coalesce(sum(su.AMOUNT * imd.VOLUME), 0) AS itemTotalM3, " +
                "     coalesce(sum(su.AMOUNT), 0)  AS itemTotalAmount " +
                "   FROM MD_STORAGELOCATION sl " +
                "     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID " +
                "     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID " +
                "     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID " +
                "     LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID " +
                "   GROUP BY podName, face) A " +
                "    ON P.podName = A.podName AND P.face = A.face " +
                "  LEFT JOIN " +
//                "  # 获取Pod 名字，类型，当前位置， 所有面 itemNo数量 " +
                "  (SELECT " +
                "     pod.NAME           AS podName, " +
                "     sl.FACE            AS face, " +
                "     count(DISTINCT imd.ITEM_NO) AS itemNoUnit " +
                "   FROM MD_STORAGELOCATION sl " +
                "     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID " +
                "     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID " +
                "     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID " +
                "     LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID " +
                "   GROUP BY podName, face) B " +
                "    ON P.podName = B.podName AND P.face = B.face " +
                "  LEFT JOIN " +
//                "  # 获取Pod 名字，类型，当前位置， 所有面 skuNo数量 " +
                "  (SELECT " +
                "     pod.NAME          AS podName, " +
                "     sl.FACE           AS face, " +
                "     count(DISTINCT imd.SKU_NO) AS skuNoUnit " +
                "   FROM MD_STORAGELOCATION sl " +
                "     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID " +
                "     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID " +
                "     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID " +
                "     LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID " +
                "   GROUP BY podName, face) C " +
                "    ON P.podName = C.podName AND P.face = C.face " +
                "  LEFT JOIN " +
//                "  # 获取Pod 名字，类型，当前位置，Pod中-> 所有面 货位总体积+个数 " +
                "  (SELECT " +
                "     pod.NAME                     AS podName, " +
                "     sl.FACE                      AS face, " +
                "     coalesce(sum(slt.VOLUME), 0) AS binTotalM3, " +
                "     count(sl.ID)                 AS binTotal " +
                "   FROM MD_STORAGELOCATION sl " +
                "     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID " +
                "     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID " +
                "   GROUP BY podName, face) D " +
                "    ON P.podName = D.podName AND P.face = D.face " +
                "  LEFT JOIN " +
//                "  # 获取Pod 名字，类型，当前位置，Pod中->所有面 已使用的货位总体积+个数 " +
                "  (SELECT " +
                "     pod.NAME                     AS podName, " +
                "     sl.FACE                      AS face, " +
                "     COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3, " +
                "     COUNT(sl.ID)                 AS binNotNullTotal " +
                "   FROM MD_POD pod " +
                "     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID " +
                "     LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID " +
                "     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID " +
                "     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID " +
                "     LEFT JOIN (SELECT DISTINCT su.UNITLOAD_ID " +
                "                FROM INV_STOCKUNIT su " +
                "                WHERE su.AMOUNT > 0 " +
                "                      AND su.ENTITY_LOCK = 0) su " +
                "       ON ul.ID = su.UNITLOAD_ID " +
                "   WHERE su.UNITLOAD_ID IS NOT NULL " +
                "         AND ul.ENTITY_LOCK = 0 " +
                "   GROUP BY podName,face) E " +
                "    ON P.podName = E.podName AND P.face = E.face " +
                "  LEFT JOIN " +
//                "  # 获取Pod 名字，类型，当前位置，Pod中->所有面 未使用的货位总体积+个数 " +
                "  (SELECT " +
                "     pod.NAME                     AS podName, " +
                "     sl.FACE                      AS face, " +
                "     coalesce(sum(slt.VOLUME), 0) AS binNullTotalM3, " +
                "     count(sl.ID)                 AS binNullTotal " +
                "   FROM MD_STORAGELOCATION sl " +
                "     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID " +
                "     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID " +
                "     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID " +
                "     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID " +
                "   WHERE su.UNITLOAD_ID IS NULL " +
                "   GROUP BY podName, face) F " +
                "    ON P.podName = F.podName AND P.face = F.face " +
                "  LEFT JOIN " +
//                "  # 获取Pod 名字，类型，当前位置，Pod中-> 所有面 未使用的 BufferBin的数量 " +
                "  (SELECT " +
                "     pod.NAME     AS podName, " +
                "     sl.FACE      AS face, " +
                "     count(sl.ID) AS bufferBinNullTotal " +
                "   FROM MD_STORAGELOCATION sl " +
                "     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID " +
                "     LEFT JOIN MD_AREA ar ON sl.AREA_ID = ar.ID " +
                "     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID " +
                "     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID " +
                "     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID " +
                "   WHERE su.UNITLOAD_ID IS NULL " +
                "         AND ar.USE_FOR_STORAGE = TRUE " +
                "   GROUP BY podName, face) G " +
                "    ON P.podName = G.podName AND P.face = G.face " +
                "WHERE 1=1 ";
        if (podName != null && !podName.equals("")) {
            sql = sql + " AND P.podName =:podName";
        }
        sql = sql + " ORDER BY podName, face ";
        Query query = entityManager.createNativeQuery(sql);
        if (podName != null && !podName.equals("")) {
            query.setParameter("podName", podName);
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(CapacityPodDTO.class));
        List<CapacityPodDTO> sides = query.getResultList();
        //百分率计算
        for (CapacityPodDTO dto : sides) {
            dto.setUseUtilization();
            dto.setBufferBinUtilization();
            dto.setTotalUtilization();
            dto.setBinUtilization();
        }
        return sides;
    }

}
