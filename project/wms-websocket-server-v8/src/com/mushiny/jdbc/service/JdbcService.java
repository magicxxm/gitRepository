package com.mushiny.jdbc.service;

import com.mushiny.jdbc.domain.IParam;
import com.mushiny.jdbc.domain.InOutParam;
import com.mushiny.jdbc.domain.InParam;
import com.mushiny.jdbc.domain.OutParam;
import com.mushiny.jdbc.repositories.JdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 测试类 没用
 * Created by Tank.li on 2017/6/13.
 */
@Component
public class JdbcService {
    @Autowired
    private JdbcRepository jdbcRepository;
    private List<Object> workStations = new ArrayList();

    /*    @Autowired
        public JdbcService(JdbcRepository jdbcRepositorys){

            jdbcRepository=jdbcRepositorys;
        }*/
    public List<Object> getWorkStations() {
        return workStations;
    }

    public List gePackWallStation() {
        return this.jdbcRepository.queryByKey("getPackWorkStation");
    }

    public List getPickPackWalls() {
        return this.jdbcRepository.queryByKey("getPickPackWalls");
    }

    public long getPackWallDiaState(String pickpackID) {
        List param = new ArrayList<>();
        param.add(pickpackID);
        List resultTemp = this.jdbcRepository.queryByKey("getPackWallDiaState", param);
        long result = 0;
        if (!CollectionUtils.isEmpty(resultTemp)) {
            Map row = (Map) resultTemp.get(0);
            if (!StringUtils.isEmpty(row.get("COUNTNUM"))) {
                result = (Long) row.get("COUNTNUM");
            }
        }

        return result;
    }

    public List getPackWorkStationState(String packingStation, String pickpackWall) {
        List param = new ArrayList<>();
        param.add(packingStation);
        param.add(pickpackWall);
        List resultTemp = this.jdbcRepository.queryByKey("getPackWorkStationState", param);
        return resultTemp;
    }

    public List getShipment() {

        return this.jdbcRepository.queryByKey("getShipment");
    }

    public List getWorkStationState(String stationId) {
        List param = new ArrayList<>();
        param.add(stationId);
        List resultTemp = this.jdbcRepository.queryByKey("getWorkStationState", param);
        int len = resultTemp.size();
        List result = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            Map row = (Map) resultTemp.get(i);
            if (row.get("STATE") == null
                    || "".equals(row.get("STATE"))) {
                continue;
            }
            result.add(row.get("STATE"));
        }
        return result;
    }

    public List getPodFace() {

        List resultTemp = this.jdbcRepository.queryByKey("getPodFace");
        int len = resultTemp.size();
        List result = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            Map row = (Map) resultTemp.get(i);
            if (row.get("PODFACE") == null
                    || "".equals(row.get("PODFACE"))) {
                continue;
            }
            result.add(row.get("PODFACE"));
        }
        return result;
    }

    @Transactional
    public int getDigitallabelState(String id) {
        int result = -1;
        List param = new ArrayList<>();
        param.add(id);
        List list = jdbcRepository.queryByKey("getDigitallabelState", param);
        if (!CollectionUtils.isEmpty(list)) {
            Map temp = (Map) (list.get(0));
            result = (int) temp.get("STATE");
        }
        return result;

    }

    @Transactional
    public int updateDigitallabelShipment(String station, int state, String id) {
        List param = new ArrayList<>();
        param.add(station);
        param.add(state);
        param.add(id);
        int result = jdbcRepository.updateByKey("updateDigitallabelShipment", param);
        return result;
    }

    public void initWorkStations() {
        List list2 = jdbcRepository.queryByKey("getWorkstation");
        for (int i = 0; i < list2.size(); i++) {
            Map row = (Map) list2.get(i);
            workStations.add(row.get("ID"));
        }
    }

    @Transactional
    public List<Object> getDigitallabel(String id) {
        List result = new ArrayList();
        List list = jdbcRepository.queryByKey("getDigitallabel");
        for (int i = 0; i < list.size(); i++) {
            Map row = (Map) list.get(i);
            result.add(row.get("ID"));
        }

        return result;

    }
    public boolean isStopPack(String workStationId)
    {
        boolean result=false;
        List<String> param=new ArrayList<>();
        param.add(workStationId);
        List list = jdbcRepository.queryByKey("isStopPack",param);
        if(!CollectionUtils.isEmpty(list))
        {
            Map<String,Boolean> row = (Map) list.get(0);
            result=row.get("ISCALLPOD");

        }
        return result;


    }


}
