package com.mushiny.wms.internaltool.service.impl;

import com.mushiny.wms.internaltool.common.domain.ItemData;
import com.mushiny.wms.internaltool.common.domain.RcsTrip;
import com.mushiny.wms.internaltool.common.domain.Robot;
import com.mushiny.wms.internaltool.common.repository.ItemDataRepository;
import com.mushiny.wms.internaltool.service.CommonToolService;
import com.mushiny.wms.internaltool.web.dto.RobotDTO;
import com.mushiny.wms.internaltool.web.dto.TripDTO;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommonToolServiceImpl implements CommonToolService {

    private final ItemDataRepository itemDataRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CommonToolServiceImpl(ItemDataRepository itemDataRepository) {
        this.itemDataRepository = itemDataRepository;
    }

    @Override
    public ItemData getByItemNo(String itemNo, String clientId, String warehouseId) {
        return itemDataRepository.getByItemNo(itemNo, clientId, warehouseId);
    }

    @Override
    public List<TripDTO> getRcsTrip(String seek) {
        List<TripDTO> list = new ArrayList<>();
        String sql = "SELECT r.CREATED_DATE as createDate,TRIP_TYPE as tripType,TRIP_STATE as tripSatate," +
                "DRIVE_ID as driveId,p.NAME as podName,m.NAME as stationName " +
                "from RCS_TRIP r LEFT JOIN MD_POD p on POD_ID=p.ID " +
                "LEFT JOIN MD_WORKSTATION m on WORKSTATION_ID=m.ID " +
                "where TRIP_STATE <>'Finish'";
        if (seek != null && !"undefined".equals(seek) && !seek.isEmpty()) {
            sql = sql + " and concat(coalesce(r.TRIP_TYPE,'')," +
                    " coalesce(r.TRIP_STATE,'')," +
                    " coalesce(r.DRIVE_ID,'')," +
                    " coalesce(p.NAME,'')," +
                    " coalesce(m.NAME,'')" +
                    " ) like:seek ";
            seek = "%" + seek + "%";
        }
        sql = sql + " order by createDate asc";
        Query query = entityManager.createNativeQuery(sql);
        if (seek != null && !"undefined".equals(seek) && !seek.isEmpty()) {
            query.setParameter("seek", seek);
        }
        List<Object[]> rcsTripList = query.getResultList();
        for (Object[] obj : rcsTripList) {
            TripDTO dto = new TripDTO();
            dto.setCreateDate(obj[0].toString().substring(0, obj[0].toString().length() - 2));
            if (obj[1] != null)
                dto.setTripType(obj[1].toString());
            if (obj[2] != null)
                dto.setTripState(obj[2].toString());
            if (obj[3] != null)
                dto.setDriveId(obj[3].toString());
            if (obj[4] != null)
                dto.setPodName(obj[4].toString());
            if (obj[5] != null)
                dto.setStationName(obj[5].toString());
            list.add(dto);
        }
        return list;
    }

    @Override
    public List<RobotDTO> getLaveBattery(String id) {
        String sql="select r.ROBOT_ID as robotId,r.VOLTAGE as voltage,r.LAVEBATTERY as laveBattery " +
                " from WCS_ROBOT r where 1=1 ";
        if(id!=null&&!"undefined".equals(id)&&!id.isEmpty()){
            sql=sql+" and r.ROBOT_ID='"+id+"'";
        }
        sql=sql+" ORDER BY r.VOLTAGE ASC";
        Query query=entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(RobotDTO.class));
        return query.getResultList();
    }
}
