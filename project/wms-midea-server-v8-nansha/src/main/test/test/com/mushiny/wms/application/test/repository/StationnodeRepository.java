package test.com.mushiny.wms.application.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import test.com.mushiny.wms.application.test.domain.Stationnode;

import java.util.List;

/**
 * Created by Administrator on 2018/7/10 0010.
 */
public interface StationnodeRepository extends JpaRepository<Stationnode, String> {

    @Query(value = "SELECT \n" +
            "  t.ID,\n" +
            "  t.NAME,\n" +
            "  t.TYPE,\n" +
            "  t.IS_CALL_POD,\n" +
            "  GROUP_CONCAT(n.ADDRESSCODEID) ADDRS,\n" +
            "  p.ID MAP_ID,\n" +
            "  p.NAME MAP_NAME,\n" +
            "  s.ID SECTION_ID,\n" +
            "  s.NAME SECTION_NAME,\n" +
            "  s.RCS_SECTIONID \n" +
            "FROM\n" +
            "  MD_STATIONNODE t \n" +
            "  LEFT JOIN MD_STATIONNODEPOSITION t2 \n" +
            "    ON t.ID = t2.STATIONNODE_ID \n" +
            "  LEFT JOIN WD_NODE n \n" +
            "    ON n.ID = t2.NODE_ID \n" +
            "  LEFT JOIN WD_MAP p \n" +
            "    ON n.MAP_ID = p.ID \n" +
            "  LEFT JOIN WD_SECTION s \n" +
            "    ON s.ID = p.SECTION_ID \n" +
            "GROUP BY t.ID,\n" +
            "  t.NAME,\n" +
            "  t.TYPE,\n" +
            "  t.IS_CALL_POD,\n" +
            "  p.ID,\n" +
            "  p.NAME,\n" +
            "  s.ID,\n" +
            "  s.NAME,\n" +
            "  s.RCS_SECTIONID", nativeQuery = true)
    List<Stationnode> getAllStationNode();


}
