package com.mushiny.map;

import com.mushiny.beans.Section;
import com.mushiny.beans.WorkStation;
import com.mushiny.comm.CommonUtils;
import com.mushiny.jdbc.repositories.JdbcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

/**
 * Created by Tank.li on 2017/7/17.
 */
@Component
public class MapManager {

    private final static Logger logger = LoggerFactory.getLogger(MapManager.class);

    public static final int MAPID = 3;
    @Autowired
    private JdbcRepository jdbcRepository;

    /**
     * 查找地图的行列
     *
     * @param sectionId
     * @return
     */
    public List<Integer> MapRowAndColumn(Long sectionId) {
        List params = new ArrayList();
        params.add(sectionId);
        List<Map> list = this.jdbcRepository.queryByKey("MapManager.MapRowAndColumn", params);
        if (list == null || list.size() == 0) {
            //throw new RuntimeException("MapRowAndColumn is empty!");
            list = new ArrayList<>();
            Map data = new HashMap();
            data.put("NUMBEROFROWS",0);
            data.put("NUMBEROFCOLUMNS",0);
            list.add(data);
        }
        Map rowData = list.get(0);
        int row = (int) rowData.get("NUMBEROFROWS");
        int column = (int) rowData.get("NUMBEROFCOLUMNS");
        int mapid = Math.toIntExact(sectionId);//(int) rowData.get("ID");
        List<Integer> rowColumn = new ArrayList<>();
        rowColumn.add(row);
        rowColumn.add(column);
        rowColumn.add(mapid);
        return rowColumn;
    }

    /**
     * 查找不可走的点
     *
     * @param mapId
     * @return
     */
    public List<Long> unwalkablePoint(String mapId) {
        List<Long> list = new ArrayList<Long>();
        /*List params = new ArrayList();
        params.add(mapId);*/
        try {
            String sql = "SELECT * FROM WD_NODE,WD_MAP \n" +
                    "WHERE WD_NODE.`MAP_ID` = WD_MAP.`ID` AND WD_MAP.`ACTIVE` = TRUE AND BLOCKED = TRUE\n" +
                    "AND WD_MAP.`SECTION_ID`= ?";
            List<Map> rowData = this.jdbcRepository.queryBySql(sql, mapId);
            for (int i = 0; i < rowData.size(); i++) {
                Map map = rowData.get(i);
                //list.add((Long) map.get("ADDRESSCODEID"));
                list.add(CommonUtils.parseLong("ADDRESSCODEID",map));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static String sql = "SELECT MD_TURNAREA.`STATION_ID`,MD_TURNAREAPOSITION.`TURNAREANODETYPE`,MD_TURNAREAPOSITION.`ADDRESSCODEID` \n" +
            "FROM MD_TURNAREAPOSITION,MD_TURNAREA WHERE MD_TURNAREA.`ID`= MD_TURNAREAPOSITION.`TURNAREA_ID` \n" +
            "AND MD_TURNAREA.`STATION_ID`=? AND MAP_ID=? \n" +
            "ORDER BY MD_TURNAREAPOSITION.`TURNAREANODETYPE`";
    private static Map workStationMap = new HashMap();

    public static Map getWorkStationMap() {
        return workStationMap;
    }

    /**
     * 查找旋转区
     *
     * @param mapId
     * @return
     */
    public Map<String, List<Long>> MapTurnArea(long mapId) {
        Map<String, List<Long>> turnAreaMap = new HashMap<String, List<Long>>();
        List params2 = new ArrayList();
        params2.add(mapId);
        String stationId;
        try {
            String idSql = "SELECT DISTINCT(WD_NODE.STATION_ID),WD_NODE.`MAP_ID`,MD_WORKSTATION.`STOPPOINT`\n" +
                    "FROM WD_NODE,WD_MAP,WD_SECTION,MD_WORKSTATION\n" +
                    "WHERE WD_NODE.`MAP_ID` = WD_MAP.`ID` AND WD_MAP.`SECTION_ID`=WD_SECTION.`ID` \n" +
                    "AND WD_NODE.`STATION_ID`=MD_WORKSTATION.`ID`\n" +
                    "AND WD_SECTION.RCS_SECTIONID=? \n" +
                    "AND WD_MAP.`ACTIVE`=TRUE AND STATION_ID IS NOT NULL";
            List<Map> rowData = this.jdbcRepository.queryBySql(idSql, params2);

            for (int i = 0; i < rowData.size(); i++) {
                Map row = rowData.get(i);
                List<Long> tidList = new ArrayList<Long>();
                String sql_Node = "SELECT ADDRESSCODEID FROM WD_NODE WHERE MAP_ID = ? AND STATION_ID=? AND TURNAREA_ID IS NOT NULL ORDER BY ADDRESSCODEID";
                List params = new ArrayList();
                stationId = (String) row.get("STATION_ID");
                String wdMapId = CommonUtils.parseString("MAP_ID",row);
                params.add(stationId);
                params.add(wdMapId);

                List<Map> ads = this.jdbcRepository.queryBySql(sql, params);
                for (int j = 0; j < ads.size(); j++) {
                    Map map = ads.get(j);
                    tidList.add(CommonUtils.parseLong("ADDRESSCODEID",map));//取出来是
                }

                if (tidList.size() != 4) {
                    //throw new RuntimeException("旋转区大小设置有误");
                    logger.error("旋转区大小设置有误: MAPID:"+wdMapId+" stationId:" + stationId);
                    continue;
                }
                //取第一个节点，判断是否为0，如果是0 按原先的方式获取田字形的旋转区，放在后面
                Long firstNode = tidList.get(0);
                if(firstNode == 0){
                    //田字形获取
                    List<Map> nodes = this.jdbcRepository.queryBySql(sql_Node, wdMapId,stationId);
                    if(nodes.size()!=4){
                        logger.error("田字形的旋转区定义有误!");
                        continue;
                    }
                    //删除后面的节点
                    tidList.remove(1);
                    tidList.remove(1);
                    tidList.remove(1);
                    Map map0 = nodes.get(0);
                    Map map1 = nodes.get(1);
                    Map map2 = nodes.get(2);
                    Map map3 = nodes.get(3);
                    tidList.add(CommonUtils.parseLong("ADDRESSCODEID",map0));
                    tidList.add(CommonUtils.parseLong("ADDRESSCODEID",map1));
                    tidList.add(CommonUtils.parseLong("ADDRESSCODEID",map3));
                    tidList.add(CommonUtils.parseLong("ADDRESSCODEID",map2));
                }

                for (Long tid : tidList) {
                    logger.debug(stationId + "--TURNAREAID--" + tid + "----");
                }
                //stationId = stationId.hashCode()+""; 换成停止点
                turnAreaMap.put(CommonUtils.parseString("STOPPOINT",row), tidList);
                workStationMap.put(CommonUtils.parseString("STOPPOINT",row), stationId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return turnAreaMap;
    }

    public static void main(String[] args) throws Exception {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://192.168.1.201:3306/wms_v8";
        String username = "root";
        String password = "123456";
        Connection conn = null;

        Class.forName(driver); //classLoader,加载对应驱动
        conn = (Connection) DriverManager.getConnection(url, username, password);

        String sql = "SELECT DISTINCT(STATION_ID) AS STATIONID FROM WD_NODE WHERE map_id = ? AND STATION_ID IS NOT NULL";//
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            pstmt.setObject(1,143);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            System.out.println("============================");
            while (rs.next()) {
                for (int i = 1; i <= col; i++) {
                    System.out.print(rs.getString(i) + "\t");
                    if ((i == 2) && (rs.getString(i).length() < 8)) {
                        System.out.print("\t");
                    }
                }
                System.out.println("");
            }
            System.out.println("============================");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 跟车自行点 取工位停止点
     * @param section
     * @return
     */
    public List<Long> queryFollowCells(Section section) {
        List<Long> followCells = new ArrayList<>();
        Iterator<WorkStation> iterator = section.workStationMap.values().iterator();
        while (iterator.hasNext()) {
            WorkStation workStation = iterator.next();
            followCells.add(Long.parseLong(workStation.getStopPoint()));
        }
        return followCells;
    }
}
