package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStation;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStation;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStation;
import com.mushiny.wms.masterdata.ibbasics.domain.StowStation;
import com.mushiny.wms.masterdata.ibbasics.repository.IBPStationRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.ReceiveStationRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.StockTakingStationRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.StowStationRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.RobotDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.RobotLaveBatteryDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.TripDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.RobotMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.Robot;
import com.mushiny.wms.masterdata.mdbasics.exception.MasterDataException;
import com.mushiny.wms.masterdata.mdbasics.repository.RobotRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationRepository;
import com.mushiny.wms.masterdata.mdbasics.service.RobotService;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStation;
import com.mushiny.wms.masterdata.obbasics.domain.PickStation;
import com.mushiny.wms.masterdata.obbasics.repository.OBPStationRepository;
import com.mushiny.wms.masterdata.obbasics.repository.PickStationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RobotServiceImpl implements RobotService {

    private final RobotRepository robotRepository;
    private final ApplicationContext applicationContext;
    private final RobotMapper robotMapper;
    private final PickStationRepository pickStationRepository;
    private final StowStationRepository stowStationRepository;
    private final IBPStationRepository ibpStationRepository;
    private final OBPStationRepository obpStationRepository;
    private final StockTakingStationRepository stockTakingStationRepository;
    private final WorkStationRepository workStationRepository;
    private final ReceiveStationRepository receiveStationRepository;
    private Logger log= LoggerFactory.getLogger(RobotServiceImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public RobotServiceImpl(RobotRepository robotRepository,
                            ApplicationContext applicationContext,
                            RobotMapper robotMapper, PickStationRepository pickStationRepository,
                            StowStationRepository stowStationRepository,
                            IBPStationRepository ibpStationRepository,
                            OBPStationRepository obpStationRepository,
                            StockTakingStationRepository stockTakingStationRepository,
                            WorkStationRepository workStationRepository,
                            ReceiveStationRepository receiveStationRepository) {
        this.robotRepository = robotRepository;
        this.applicationContext = applicationContext;
        this.robotMapper = robotMapper;
        this.pickStationRepository = pickStationRepository;
        this.stowStationRepository = stowStationRepository;
        this.ibpStationRepository = ibpStationRepository;
        this.obpStationRepository = obpStationRepository;
        this.stockTakingStationRepository = stockTakingStationRepository;
        this.workStationRepository = workStationRepository;
        this.receiveStationRepository = receiveStationRepository;
    }

    @Override
    public RobotDTO create(RobotDTO dto) {
        Robot entity = robotMapper.toEntity(dto);
        checkRobotName(entity.getWarehouseId(), entity.getRobot());
        return robotMapper.toDTO(robotRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        Robot entity = robotRepository.retrieve(id);
        robotRepository.delete(entity);
    }

    @Override
    public RobotDTO update(RobotDTO dto) {
        Robot entity = robotRepository.retrieve(dto.getId());
        if (!(entity.getRobot().equalsIgnoreCase(dto.getRobot()))) {
            checkRobotName(entity.getWarehouseId(), dto.getRobot());
        }
        robotMapper.updateEntityFromDTO(dto, entity);
        return robotMapper.toDTO(robotRepository.save(entity));
    }

    @Override
    public RobotDTO retrieve(String id) {
        return robotMapper.toDTO(robotRepository.retrieve(id));
    }

    @Override
    public List<RobotDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<Robot> entities = robotRepository.getBySearchTerm(searchTerm, sort);
        return robotMapper.toDTOList(entities);
    }

    @Override
    public Page<RobotDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        Page<Robot> entities = robotRepository.getBySearchTerm(searchTerm, pageable);
        return robotMapper.toDTOPage(pageable, entities);
    }

    @Override
    public void enter(String robotId, String password) {
        checkEnter(robotId, password);
        return;
    }

    @Override
    public List<RobotLaveBatteryDTO> getLaveBattery(String id) {
        String sql="SELECT r.ROBOT_ID AS robotId,r.VOLTAGE AS voltage,r.LAVEBATTERY AS laveBattery " +
                " FROM WCS_ROBOT r WHERE r.STATUS>-1 ";
        if(id!=null&&!"undefined".equals(id)&&!id.isEmpty()){
            sql=sql+" AND r.ROBOT_ID='"+id+"'";
        }
        sql=sql+" ORDER BY r.VOLTAGE ASC";
        Query query=entityManager.createNativeQuery(sql);
//        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(RobotLaveBatteryDTO.class));
        List<Object[]> objects=query.getResultList();
        List<RobotLaveBatteryDTO> list=new ArrayList<>();
        for(Object[] obj:objects){
            RobotLaveBatteryDTO dto=new RobotLaveBatteryDTO();
            dto.setRobotId(Integer.valueOf(obj[0].toString()));
            dto.setVoltage(Integer.valueOf(obj[1].toString()));
            dto.setLaveBattery(Integer.valueOf(obj[2].toString()));
            list.add(dto);
        }
        return list;
    }

    @Override
    public Page<TripDTO> getPageRcsTrip(String startTime, String endTime, List<String> types, boolean isFinish, String seek, boolean isExport, Pageable pageable) {
        List<TripDTO> list = new ArrayList<>();
        String sql="SELECT RCS_TRIP.CREATED_DATE AS createDate,RCS_TRIP.MODIFIED_DATE AS modifiedDate," +
                "RCS_TRIP.TRIP_TYPE AS tripType,RCS_TRIP.TRIP_STATE AS tripState," +
                "RCS_TRIP.DRIVE_ID AS driveId,MD_POD.NAME AS podName,MD_WORKSTATION.NAME AS stationName," +
                "MD_CHARGER.CHARGER_ID AS charger,RCS_TRIP.CREATED_BY AS userName,RCS_TRIP.WORKSTATION_ID as workstationId," ;

        String str= "FROM RCS_TRIP LEFT JOIN MD_POD ON RCS_TRIP.POD_ID=MD_POD.ID " +
                "LEFT JOIN MD_WORKSTATION ON RCS_TRIP.WORKSTATION_ID=MD_WORKSTATION.ID " +
                "LEFT JOIN MD_CHARGER ON RCS_TRIP.CHARGER_ID=MD_CHARGER.ID "+
                "WHERE 1=1 ";
        //已经完成的调度单
        if(isFinish){
             sql=sql+"TIMESTAMPDIFF(MINUTE,RCS_TRIP.CREATED_DATE,RCS_TRIP.MODIFIED_DATE) AS time " +str;
        }
        if(!isFinish){
            //服务器时间加8个时区
           sql= sql+ "TIMESTAMPDIFF(MINUTE,RCS_TRIP.CREATED_DATE,DATE_ADD(now(),INTERVAL 8 HOUR)) AS time " +str;
        }
        if (seek != null && !"undefined".equals(seek) && !seek.isEmpty()) {
            sql = sql + " AND concat(COALESCE(RCS_TRIP.DRIVE_ID,'')," +
                    " COALESCE(MD_POD.NAME,'')," +
                    " COALESCE(MD_WORKSTATION.NAME,'')," +
                    " COALESCE(RCS_TRIP.CREATED_BY,'')" +
                    " ) LIKE:seek ";
            seek = "%" + seek + "%";
        }
        if(startTime!=null&&!startTime.isEmpty()){
            startTime=startTime+" 00:00:00";
            sql=sql+" AND RCS_TRIP.CREATED_DATE >'"+startTime+"'";
        }
        if(endTime!=null && !endTime.isEmpty()){
            endTime=endTime+" 23:59:59";
            sql=sql+" AND RCS_TRIP.MODIFIED_DATE<'"+endTime+"'";
        }
        if(isFinish){
            sql=sql+" AND RCS_TRIP.TRIP_STATE='Finish'";
        }
        if(!isFinish){
            sql=sql+" AND RCS_TRIP.TRIP_STATE<>'Finish'";
        }
        if(!types.isEmpty() && !types.get(0).equals("undefined")){
            String sb="(";
            for(String type:types){
                sb=sb+"'"+type+"',";
            }
            sb=sb.substring(0,sb.length()-1)+")";
            sql=sql+" AND RCS_TRIP.TRIP_TYPE in"+sb+"";
        }
        if(pageable.getSort()==null){
            sql = sql + " ORDER BY createDate ASC ";
        }else{
            Sort sort=pageable.getSort();
            for(Sort.Order order :sort){
                if(order.getProperty().equals("logicName") )
                    sql = sql + " ORDER BY stationName "+ order.getDirection();
                else if(order.getProperty().equals("itemDataAmount")
                        || order.getProperty().equals("storageLocationAmount")
                        || order.getProperty().equals("faceAmount")){
                    sql = sql + " ORDER BY createDate "+order.getDirection();
                }else
                  sql = sql + " ORDER BY "+order.getProperty()+" "+ order.getDirection();
            }
        }
        Query query = entityManager.createNativeQuery(sql);
        //获取总数
        Query query2 = entityManager.createNativeQuery(sql);
        //导出数据导出全部，不需要分页
        if(!isExport){
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }
        if (seek != null && !"undefined".equals(seek) && !seek.isEmpty()) {
            query.setParameter("seek", seek);
            query2.setParameter("seek", seek);
        }
        List<Object[]> rcsTripList = query.getResultList();
        List<Object[]> rcsTripList2=query2.getResultList();
        long count=rcsTripList2.size();
        log.info("调度单数量："+count);
        for (Object[] obj : rcsTripList) {
            TripDTO dto = new TripDTO();
            dto.setCreateDate(obj[0].toString().substring(0, obj[0].toString().length() - 2));
            dto.setModifiedDate(obj[1].toString().substring(0, obj[1].toString().length() - 2));
            if (obj[2] != null)
                dto.setTripType(obj[2].toString());
            if (obj[3] != null)
                dto.setTripState(obj[3].toString());
            if (obj[4] != null)
                dto.setDriveId(obj[4].toString());
            if (obj[5] != null) {
                dto.setPodName(obj[5].toString());
                if(("PickPod").equals(dto.getTripType()) || ("StowPod").equals(dto.getTripType())) {
                    String sqlQuery ="";
                    if(("PickPod").equals(dto.getTripType())) {
                        sqlQuery = "SELECT COALESCE(SUM(INV_STOCKUNITRECORD.AMOUNT),0)," +
                                "COUNT(DISTINCT(INV_STOCKUNITRECORD.FROM_STORAGELOCATION))," +
                                "COUNT(DISTINCT(SUBSTRING(INV_STOCKUNITRECORD.FROM_STORAGELOCATION,9,1)))" +
                                " FROM INV_STOCKUNITRECORD" +
                                " WHERE INV_STOCKUNITRECORD.CREATED_DATE > '" + obj[0].toString() + "'" +
                                " AND INV_STOCKUNITRECORD.MODIFIED_DATE < '" + obj[1].toString() + "'" +
                                " AND INV_STOCKUNITRECORD.RECORD_TOOL ='Pick' " +
                                " AND INV_STOCKUNITRECORD.FROM_STORAGELOCATION like '" + obj[5].toString() + "%' ";
                    } else{
                        sqlQuery = "SELECT COALESCE(SUM(INV_STOCKUNITRECORD.AMOUNT),0)," +
                                "COUNT(DISTINCT(INV_STOCKUNITRECORD.TO_STORAGELOCATION))," +
                                "COUNT(DISTINCT(SUBSTRING(INV_STOCKUNITRECORD.TO_STORAGELOCATION,9,1)))" +
                                " FROM INV_STOCKUNITRECORD" +
                                " WHERE INV_STOCKUNITRECORD.CREATED_DATE > '" + obj[0].toString() + "'" +
                                " AND INV_STOCKUNITRECORD.MODIFIED_DATE < '" + obj[1].toString() + "'" +
                                " AND INV_STOCKUNITRECORD.RECORD_TOOL IN('Stow','Receive')" +
                                " AND INV_STOCKUNITRECORD.TO_STORAGELOCATION like '" + obj[5].toString() + "%' ";
                    }
                    Query query3=entityManager.createNativeQuery(sqlQuery);
                    List<Object[]> li=query3.getResultList();
                    dto.setItemDataAmount(new BigDecimal(li.get(0)[0].toString()).intValue());
                    dto.setStorageLocationAmount(Integer.parseInt(li.get(0)[1].toString()));
                    dto.setFaceAmount(Integer.parseInt(li.get(0)[2].toString()));
                }
            }
            if (obj[6] != null) {
                dto.setStationName(obj[6].toString());
                //获取逻辑工作站
                if (("StowPod").equals(dto.getTripType())) {
                    //上架工作站
                    List<StowStation> stations = stowStationRepository.getByWorkStation(obj[9].toString());
                    if (stations!=null && stations.size() > 0) {
                        dto.setLogicName(stations.get(0).getName());
                    }else{
                        // 收货上架工作站
                        List<ReceiveStation> station= receiveStationRepository.getByWorkStation(obj[9].toString());
                        if (stations!=null && station.size() > 0)
                            dto.setLogicName(station.get(0).getName());
                    }
                } else if (("PickPod").equals(dto.getTripType())) {
                    List<PickStation> stations = pickStationRepository.getByWorkStation(obj[9].toString());
                    if (stations!=null && stations.size() > 0)
                        dto.setLogicName(stations.get(0).getName());
                } else if (("IBPPod").equals(dto.getTripType())) {
                    List<IBPStation> stations = ibpStationRepository.getByWorkStation(obj[9].toString());
                    if (stations!=null && stations.size() > 0)
                        dto.setLogicName(stations.get(0).getName());
                } else if (("OBPPod").equals(dto.getTripType())) {
                    List<OBPStation> stations = obpStationRepository.getByWorkStation(obj[9].toString());
                    if (stations!=null && stations.size() > 0)
                        dto.setLogicName(stations.get(0).getName());
                } else if (("ICQAPod").equals(dto.getTripType())) {
                    List<StockTakingStation> stations = stockTakingStationRepository.getByWorkStation(obj[9].toString());
                    if (stations!=null && stations.size() > 0)
                        dto.setLogicName(stations.get(0).getName());
                }
            }
            if (obj[7] != null)
                dto.setCharger(obj[7].toString());
            dto.setUserName(obj[8].toString());
            dto.setTime(Long.parseLong(obj[10].toString()));
            list.add(dto);
        }
        return new PageImpl<TripDTO>(list,pageable,count);
    }

    private void checkRobotName(String warehouse, String robotId) {
        Robot robot = robotRepository.getByName(warehouse, robotId);
        if (robot != null) {
            throw new ApiException(MasterDataException.EX_MD_ROBOT_NAME_UNIQUE.toString(), robotId);
        }
    }

    private void checkEnter(String robotId, String password) {
        Robot robot = robotRepository.getByEnter(robotId, password);
        if (robot == null) {
            throw new ApiException(MasterDataException.EX_MD_ROBOT_ENTER_ERROR.toString(), robotId);
        }
    }
}
