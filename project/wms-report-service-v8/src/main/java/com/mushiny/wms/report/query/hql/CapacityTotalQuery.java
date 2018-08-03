package com.mushiny.wms.report.query.hql;

import com.mushiny.wms.report.query.dto.capacityTotal.Capacity;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Component
public class CapacityTotalQuery implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Capacity> getCapacityTotal() {

        String sql = "SELECT DISTINCT " +
                "  z.NAME                                    AS zoneName, " +
                "  slt.NAME                                  AS binType, " +
                "  B.itemTotalAmountM3 / A.binNotNullTotalM3 AS binTypeUtilization, " +
                "  D.itemTotalAmountM3 / C.binNotNullTotalM3 AS totalUtilization " +
                "FROM MD_ZONE z " +
                "  LEFT JOIN MD_STORAGELOCATION sl ON z.ID = sl.ZONE_ID " +
                "  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID " +
                "  LEFT JOIN " +
//                "  # 所有区域 各bin类型 不为空 bin的总体积 " +
                "  (SELECT " +
                "     z.NAME                       AS zoneName, " +
                "     slt.NAME                     AS binType, " +
                "     COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3 " +
                "   FROM MD_ZONE z " +
                "     LEFT JOIN MD_STORAGELOCATION sl ON z.ID = sl.ZONE_ID " +
                "     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID " +
                "     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID " +
                "     LEFT JOIN (SELECT DISTINCT su.UNITLOAD_ID " +
                "                FROM INV_STOCKUNIT su " +
                "                WHERE su.AMOUNT > 0 " +
                "                      AND su.ENTITY_LOCK = 0) su " +
                "       ON ul.ID = su.UNITLOAD_ID " +
                "   WHERE su.UNITLOAD_ID IS NOT NULL " +
                "         AND ul.ENTITY_LOCK = 0 " +
                "         AND slt.STORAGETYPE = 'BIN' " +
                "   GROUP BY zoneName, binType) A " +
                "    ON z.NAME = A.zoneName " +
                "       AND slt.NAME = A.binType " +
                "  LEFT JOIN " +
                "  ( " +
//                "    # 所有区域 各bin类型的  所有商品的面积合计 " +
                "    SELECT " +
                "      z.NAME                     AS zoneName, " +
                "      slt.NAME                   AS binType, " +
                "      sum(su.AMOUNT * it.VOLUME) AS itemTotalAmountM3 " +
                "    FROM MD_ZONE z " +
                "      LEFT JOIN MD_STORAGELOCATION sl ON sl.ZONE_ID = z.ID " +
                "      LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID " +
                "      LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID " +
                "      LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID " +
                "      LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID " +
                "    WHERE su.AMOUNT <> 0 " +
                "    GROUP BY zoneName, binType) B " +
                "    ON A.zoneName = B.zoneName " +
                "       AND A.binType = B.binType " +
                "  LEFT JOIN " +
//                "  # 所有区域的 不为空的bin 面积合计 " +
                "  (SELECT " +
                "     z.NAME                       AS zoneName, " +
                "     COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3 " +
                "   FROM MD_ZONE z " +
                "     LEFT JOIN MD_STORAGELOCATION sl ON sl.ZONE_ID = z.ID " +
                "     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID " +
                "     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID " +
                "     LEFT JOIN (SELECT DISTINCT su.UNITLOAD_ID " +
                "                FROM INV_STOCKUNIT su " +
                "                WHERE su.AMOUNT > 0 " +
                "                      AND su.ENTITY_LOCK = 0) su " +
                "       ON ul.ID = su.UNITLOAD_ID " +
                "   WHERE su.UNITLOAD_ID IS NOT NULL " +
                "         AND ul.ENTITY_LOCK = 0 " +
                "         AND slt.STORAGETYPE = 'BIN' " +
                "   GROUP BY zoneName) C ON z.NAME = C.zoneName " +
                "  LEFT JOIN " +
//                "  # 所有区域的 所有商品的面积合计 " +
                "  (SELECT " +
                "     z.NAME                     AS zoneName, " +
                "     sum(su.AMOUNT * it.VOLUME) AS itemTotalAmountM3 " +
                "   FROM MD_ZONE z " +
                "     LEFT JOIN MD_STORAGELOCATION sl ON sl.ZONE_ID = z.ID " +
                "     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID " +
                "     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID " +
                "     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID " +
                "     LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID " +
                "   WHERE su.AMOUNT <> 0 AND ul.ENTITY_LOCK = 0 " +
                "   GROUP BY zoneName) D ON z.NAME = D.zoneName " +
                "WHERE A.binNotNullTotalM3 IS NOT NULL " +
                "      AND slt.STORAGETYPE = 'BIN' ";

        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(Capacity.class));
        List<Capacity> capacitys = query.getResultList();
        return capacitys;
    }

    //    总计 --> 各bin 类型的 不为空 bin的面积和商品的面积合计 *
    public List<Capacity> totalCapacity() {
        String sql = "SELECT DISTINCT " +
                "  slt.NAME                                  AS binType, " +
                "  B.itemTotalAmountM3 / A.binNotNullTotalM3 AS binTypeUtilization " +
                "FROM MD_ZONE z " +
                "  LEFT JOIN MD_STORAGELOCATION sl ON sl.ZONE_ID = z.ID " +
                "  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID " +
                "  LEFT JOIN " +
//                "  # 所有区域 各bin类型 不为空 bin的总体积 " +
                "  (SELECT " +
                "     slt.NAME                     AS binType, " +
                "     COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3 " +
                "   FROM MD_ZONE z " +
                "     LEFT JOIN MD_STORAGELOCATION sl ON sl.ZONE_ID = z.ID " +
                "     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID " +
                "     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID " +
                "     LEFT JOIN (SELECT DISTINCT su.UNITLOAD_ID " +
                "                FROM INV_STOCKUNIT su " +
                "                WHERE su.AMOUNT > 0 " +
                "                      AND su.ENTITY_LOCK = 0) su " +
                "       ON ul.ID = su.UNITLOAD_ID " +
                "   WHERE su.UNITLOAD_ID IS NOT NULL " +
                "         AND ul.ENTITY_LOCK = 0 " +
                "         AND slt.STORAGETYPE = 'BIN' " +
                "   GROUP BY binType " +
                "  ) A " +
                "    ON slt.NAME = A.binType " +
                "  LEFT JOIN " +
                "  ( " +
//                "    # 所有区域 各bin类型的  所有商品的面积合计 " +
                "    SELECT " +
                "      slt.NAME                   AS binType, " +
                "      sum(su.AMOUNT * it.VOLUME) AS itemTotalAmountM3 " +
                "    FROM MD_ZONE z " +
                "      LEFT JOIN MD_STORAGELOCATION sl ON sl.ZONE_ID = z.ID " +
                "      LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID " +
                "      LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID " +
                "      LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID " +
                "      LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID " +
                "    WHERE su.AMOUNT <> 0 " +
                "          AND ul.ENTITY_LOCK = 0 " +
                "          AND slt.STORAGETYPE = 'BIN' " +
                "    GROUP BY binType " +
                "  ) B " +
                "    ON A.binType = B.binType " +
                "WHERE A.binNotNullTotalM3 IS NOT NULL " +
                "      AND slt.STORAGETYPE = 'BIN' " +
                "ORDER BY slt.NAME  ";

        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(Capacity.class));
        List<Capacity> total = query.getResultList();
        return total;
    }


    public BigDecimal getTotalToTotal() {
//        #  合计的TOTAL  所有区域的 商品面积合计/不为空的bin 面积合计
        String sql = " SELECT D.itemTotalAmountM3 / C.binNotNullTotalM3 AS totalUtilization " +
                "FROM " +
                "  (SELECT COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3 " +
                "   FROM MD_ZONE z " +
                "     LEFT JOIN MD_STORAGELOCATION sl ON sl.ZONE_ID = z.ID " +
                "     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID " +
                "     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID " +
                "     LEFT JOIN (SELECT DISTINCT su.UNITLOAD_ID " +
                "                FROM INV_STOCKUNIT su " +
                "                WHERE su.AMOUNT > 0 " +
                "                      AND su.ENTITY_LOCK = 0) su " +
                "       ON ul.ID = su.UNITLOAD_ID " +
                "   WHERE su.UNITLOAD_ID IS NOT NULL " +
                "         AND ul.ENTITY_LOCK = 0 " +
                "         AND slt.STORAGETYPE = 'BIN' " +
                "  ) C, " +
//                "  # 所有区域的 所有商品的面积合计 " +
                "  (SELECT sum(su.AMOUNT * it.VOLUME) AS itemTotalAmountM3 " +
                "   FROM MD_ZONE z " +
                "     LEFT JOIN MD_STORAGELOCATION sl ON sl.ZONE_ID = z.ID " +
                "     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID " +
                "     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID " +
                "     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID " +
                "     LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID " +
                "   WHERE su.AMOUNT <> 0 AND slt.STORAGETYPE = 'BIN' " +
                "  ) D ";
        Query query = entityManager.createNativeQuery(sql);
        BigDecimal total = BigDecimal.ZERO;
        if (query.getSingleResult() != null) {
            total = new BigDecimal(query.getSingleResult().toString());
        }
        return total;
    }


}
