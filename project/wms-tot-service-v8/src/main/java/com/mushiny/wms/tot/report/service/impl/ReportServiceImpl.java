package com.mushiny.wms.tot.report.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.tot.attendance.repository.AttendanceRepository;
import com.mushiny.wms.tot.general.crud.dto.UserDTO;
import com.mushiny.wms.tot.general.crud.mapper.UserMapper;
import com.mushiny.wms.tot.general.domain.User;
import com.mushiny.wms.tot.general.domain.Warehouse;
import com.mushiny.wms.tot.general.repository.UserRepository;
import com.mushiny.wms.tot.general.repository.WarehouseRepository;
import com.mushiny.wms.tot.jobrecord.repository.JobrecordRepository;
import com.mushiny.wms.tot.jobthreshold.crud.mapper.JobthresholdMapper;
import com.mushiny.wms.tot.jobthreshold.domain.Jobthreshold;
import com.mushiny.wms.tot.jobthreshold.repository.JobthresholdRepository;
import com.mushiny.wms.tot.report.query.dto.CTimeDetailDTO;
import com.mushiny.wms.tot.report.query.dto.Clock;
import com.mushiny.wms.tot.report.query.dto.JobTypeDTO;
import com.mushiny.wms.tot.report.query.dto.NewStatisticsDTO;
import com.mushiny.wms.tot.report.query.hql.StatisticsQuery;
import com.mushiny.wms.tot.report.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);
    private final UserRepository userRepository;
    private final StatisticsQuery statisticsQuery;
    private final AttendanceRepository attendanceRepository;
    private final JobrecordRepository jobrecordRepository;
    private final JobthresholdRepository jobthresholdRepository;
    private final UserMapper userMapper;
    private final ApplicationContext applicationContext;
    private final WarehouseRepository warehouseRepository;

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    public ReportServiceImpl(UserRepository userRepository,
                             StatisticsQuery statisticsQuery,
                             AttendanceRepository attendanceRepository,
                             JobrecordRepository jobrecordRepository,
                             JobthresholdRepository jobthresholdRepository,
                             UserMapper userMapper,
                             JobthresholdMapper jobthresholdMapper,
                             ApplicationContext applicationContext,
                             WarehouseRepository warehouseRepository) {
        this.userRepository = userRepository;
        this.statisticsQuery = statisticsQuery;
        this.attendanceRepository = attendanceRepository;
        this.jobrecordRepository = jobrecordRepository;
        this.jobthresholdRepository = jobthresholdRepository;
        this.userMapper = userMapper;
        this.applicationContext = applicationContext;
        this.warehouseRepository = warehouseRepository;
    }

    int thresholdA;
    int thresholdB;

    @Override
    public List<NewStatisticsDTO> getStatistics(String warehouseId, String clientId, String dayDate, String startDate, String
            endDate, String userName) throws ParseException {
        getThreshold();
        Warehouse warehouse = warehouseRepository.retrieve(warehouseId);
        Set<User> users = warehouse.getUsers();
        List<NewStatisticsDTO> newEntitys = new LinkedList<NewStatisticsDTO>();
        if (users.size() > 0) {
            for (User user : users) {
                if ("ALL".equals(clientId) || user.getClient().getClientNo().equals(clientId) ) {
                    String username = user.getUsername();
                    String name = user.getName();
                    String client = user.getClient().getName();
                    //员工打卡数据
                    List<Object[]> statistics = statisticsQuery.getStatisticsTotal(username, startDate, endDate, warehouseId);
                    if (null != statistics && statistics.size() > 0) {
                        NewStatisticsDTO newStatisticsDTO = new NewStatisticsDTO();
                        newStatisticsDTO.setEmployeeCode(username);
                        newStatisticsDTO.setEmployeeName(name);
                        newStatisticsDTO.setWarehouseId((String) (statistics.get(0)[5]));
                        newStatisticsDTO.setClientId(clientId);
                        String tmpTime = "";
                        double h = 0d; //总打卡时间
                        double totalNoJobTime = 0d; //总无工作时间
                        boolean flag = false;//标志位用来判断最后一次打卡类型（上班or下班）
                        for (int i = 0; i < statistics.size(); i++) {
                            Object[] statistic = statistics.get(i);
                            String clockType = String.valueOf(statistic[3]);
                            String clockTime = String.valueOf(statistic[0]);
                            Clock clock = new Clock(clockType, clockTime);
                            newStatisticsDTO.getZonesList().add(clock);
                            //计算打卡时间
                            if (clockType.equals("上班")) {
                                flag = true;
                                tmpTime = clockTime;
                            } else {
                                flag = false;
                                Date date2 = DateTimeUtil.getDateforStr(clockTime);
                                Date date1;
                                if (i == 0) {
                                    // 打卡类型是下班且是第一次打卡的特殊情况计算
                                    if (StringUtils.isEmpty(dayDate))
                                        date1 = DateTimeUtil.getDateforStr(startDate.replace("T", " "));
                                    else date1 = DateTimeUtil.getDateFirst(clockTime);
                                } else {
                                    date1 = DateTimeUtil.getDateforStr(tmpTime);
                                }
                                h += DateTimeUtil.getHourAndMin(date2, date1, "hour");

                                //计算无工作时间=============start
                                if (i == 0) {
                                    if (StringUtils.isEmpty(dayDate)) tmpTime = startDate.replace("T", " ");
                                    else tmpTime = clockTime.substring(0, clockTime.indexOf(" ")) + " 00:00:00";
                                }
                                //直接间接工作时间
                                List<Object[]> jobRecord = statisticsQuery.getRecords(warehouseId, username, tmpTime,
                                        clockTime, null);
                                String tmp = tmpTime;
                                String jobType;
                                Date dateS;
                                double timeSlot = 0d;
                                //判断第一次打卡类型是下班 这时间之前到 上次下班 时间之后 的最后一次工作是间接工作
                                String lastJobType = "";
                                if (i == 0) {
                                    String smallDate = "1970-01-01 00:00:00";
                                    String bigDate;
                                    if (StringUtils.isEmpty(dayDate)) bigDate = startDate;
                                    else bigDate = dayDate + " 00:00:00";
                                    List<Object[]> maxClockTime = statisticsQuery.getMaxClockTime(username, smallDate, bigDate, warehouseId, null);
                                    String lastClockTime = String.valueOf(maxClockTime.get(0)[0]);
                                    //判断时间段之前最近一次工作是否为间接工作
                                    List<Object[]> lastJobRecord = statisticsQuery.getMaxJobAction(warehouseId, username, lastClockTime,
                                            bigDate, null);
                                    if (lastJobRecord != null && lastJobRecord.size() > 0) {
                                        lastJobType = String.valueOf(lastJobRecord.get(0)[0]);
                                        if (lastJobType.equals("INDIRECT")) {
                                            String lastJobAction = String.valueOf(lastJobRecord.get(0)[2]);
                                            String lastIndirectType = String.valueOf(lastJobRecord.get(0)[3]);
                                            //ppr
                                            String lastJobName = String.valueOf(lastJobRecord.get(0)[4]);
                                            String lastEmployeeCode = String.valueOf(lastJobRecord.get(0)[5]);
                                            String lastClient = String.valueOf(lastJobRecord.get(0)[6]);
                                            String lastWareHouse = String.valueOf(lastJobRecord.get(0)[7]);
                                            //j.RECORD_TIME,j.JOB_TYPE,j.JOB_ACTION,j.INDIRECT_TYPE,j.JOB_NAME,j.EMPLOYEE_CODE,j.CLIENT_ID,j.WAREHOUSE_ID
                                            Object[] job = {bigDate, lastJobType, lastJobAction, lastIndirectType, lastJobName,
                                                    lastEmployeeCode, lastClient, lastWareHouse};
                                            jobRecord.add(0, job);
                                        }
                                    }
                                }
                                for (int n = 0; n < jobRecord.size(); n++) {
                                    Object[] job = jobRecord.get(n);
                                    String jobTime = String.valueOf(job[0]);
                                    Date dateE = DateTimeUtil.getDateforStr(jobTime);
                                    dateS = DateTimeUtil.getDateforStr(tmp);
                                    timeSlot = DateTimeUtil.getHourAndMin(dateE, dateS, "min");
                                    if (n == 0) {
                                        if (timeSlot > thresholdB) {
                                            totalNoJobTime += timeSlot;
                                        }
                                    } else {
                                        if (String.valueOf(jobRecord.get(n - 1)[1]).equals("DIRECT")) {
                                            if (timeSlot > thresholdB) {
                                                totalNoJobTime += timeSlot;
                                            }
                                        }
                                    }
                                    tmp = jobTime;
                                }
                                Date dateEnd = DateTimeUtil.getDateforStr(clockTime);
                                dateS = DateTimeUtil.getDateforStr(tmp);
                                timeSlot = DateTimeUtil.getHourAndMin(dateEnd, dateS, "min");
                                if (jobRecord.size() > 0) {
                                    jobType = String.valueOf(jobRecord.get(jobRecord.size() - 1)[1]);
                                    if (jobType.equals("DIRECT")) {
                                        if (timeSlot > thresholdB) {
                                            totalNoJobTime += timeSlot;
                                        }
                                    }
                                } else {
                                    if (lastJobType.equals("DIRECT") || lastJobType.equals(""))
                                        totalNoJobTime += timeSlot;
                                }
                                //=============================end
                            }
                        }
                        if (flag) {
                            // 最后一次打卡类型是上班的特殊情况计算
                            Date date1 = DateTimeUtil.getDateforStr(tmpTime);
                            Date date2;
                            String newStr;
                            if (endDate != null) {
                                date2 = DateTimeUtil.getDateforStr(endDate.replace("T", " "));
                                newStr = endDate.replace("T", " ");
                            } else {
                                date2 = DateTimeUtil.getDateLast(tmpTime);
                                newStr = tmpTime.substring(0, tmpTime.indexOf(" ")) + " 23:59:59";
                            }
                            //对比当前时间
                            Date dateNow = new Date();
                            if (dateNow.before(date2)) {
                                date2 = dateNow;
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                newStr = dateFormat.format(dateNow);
                            }
                            h += DateTimeUtil.getHourAndMin(date2, date1, "hour");
                            //=========
                            List<Object[]> jobRecord = statisticsQuery.getRecords(warehouseId, username, tmpTime, newStr,
                                    null);
                            String tmp = tmpTime;
                            String jobType;
                            Date dateS;
                            double timeSlot = 0d;
                            for (int n = 0; n < jobRecord.size(); n++) {
                                Object[] job = jobRecord.get(n);
                                String jobTime = String.valueOf(job[0]);
                                Date dateE = DateTimeUtil.getDateforStr(jobTime);
                                dateS = DateTimeUtil.getDateforStr(tmp);
                                timeSlot = DateTimeUtil.getHourAndMin(dateE, dateS, "min");
                                if (n == 0) {
                                    if (timeSlot > thresholdB) {
                                        totalNoJobTime += timeSlot;
                                    }
                                } else {
                                    if (String.valueOf(jobRecord.get(n - 1)[1]).equals("DIRECT")) {
                                        if (timeSlot > thresholdB) {
                                            totalNoJobTime += timeSlot;
                                        }
                                    }
                                }
                                tmp = jobTime;
                            }
                            dateS = DateTimeUtil.getDateforStr(tmp);
                            timeSlot = DateTimeUtil.getHourAndMin(date2, dateS, "min");
                            if (jobRecord.size() > 0) {
                                jobType = String.valueOf(jobRecord.get(jobRecord.size() - 1)[1]);
                                if (jobType.equals("DIRECT")) {
                                    if (timeSlot > thresholdB) {
                                        totalNoJobTime += timeSlot;
                                    }
                                }
                            } else totalNoJobTime += timeSlot;
                        }
                        double totalJobTime = Math.abs(h - totalNoJobTime / 60);
                        newStatisticsDTO.setEcWorkTime(String.valueOf(totalJobTime));
                        newStatisticsDTO.setTotalClockTime(String.valueOf(h));
                        if (h > 0) {
                            double timeRate = totalJobTime / h * 100;
                            newStatisticsDTO.setEcWorkRate(String.valueOf(timeRate));
                        }
                        newEntitys.add(newStatisticsDTO);
                    } else {
                        String smallDate = "1970-01-01 00:00:00";
                        String bigDate;
                        if (StringUtils.isEmpty(dayDate)) bigDate = startDate;
                        else bigDate = dayDate + " 00:00:00";
                        List<Object[]> maxClockTime = statisticsQuery.getMaxClockTime(username, smallDate, bigDate, warehouseId, null);
                        if (maxClockTime != null && maxClockTime.size() > 0) {
                            String lastClockType = String.valueOf(maxClockTime.get(0)[3]);
                            if (lastClockType.equals("上班")) {
                                String lastClockTime = String.valueOf(maxClockTime.get(0)[0]);
                                String lastWarehouseId = String.valueOf(maxClockTime.get(0)[5]);
                                NewStatisticsDTO newStatisticsDTO = new NewStatisticsDTO();
                                newStatisticsDTO.setEmployeeCode(username);
                                newStatisticsDTO.setEmployeeName(name);
                                newStatisticsDTO.setWarehouseId(lastWarehouseId);
                                newStatisticsDTO.setClientId(client);

                                double h = 0d;
                                Date date1;
                                Date date2;
                                String smallTime;
                                String bigTime;
                                if (endDate != null) {
                                    smallTime = startDate.replace("T", " ");
                                    bigTime = endDate.replace("T", " ");
                                    date1 = DateTimeUtil.getDateforStr(smallTime);
                                    date2 = DateTimeUtil.getDateforStr(bigTime);
                                } else {
                                    smallTime = dayDate + " 00:00:00";
                                    bigTime = dayDate + " 23:59:59";
                                    date1 = DateTimeUtil.getDateforStr(smallTime);
                                    date2 = DateTimeUtil.getDateforStr(bigTime);
                                }
                                //对比当前时间
                                Date dateNow = new Date();
                                if (dateNow.before(date2)) {
                                    date2 = dateNow;
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    bigTime = dateFormat.format(dateNow);
                                }
                                h += DateTimeUtil.getHourAndMin(date2, date1, "hour");
                                newStatisticsDTO.setTotalClockTime(String.valueOf(h));

                                //直接间接工作时间
                                List<Object[]> jobRecord = statisticsQuery.getRecords(warehouseId, username, smallTime,
                                        bigTime, null);
                                String tmp = smallTime;
                                String jobType;
                                Date dateS;
                                double totalNoJobTime = 0d; //总无工作时间
                                double timeSlot = 0d;
                                //判断时间段之前最近一次工作是否为间接工作
                                List<Object[]> lastJobRecord = statisticsQuery.getMaxJobAction(warehouseId, username, lastClockTime,
                                        smallTime, null);
                                String lastJobType = "";
                                if (lastJobRecord != null && lastJobRecord.size() > 0) {
                                    lastJobType = String.valueOf(lastJobRecord.get(0)[0]);
                                    if (lastJobType.equals("INDIRECT")) {
                                        String lastJobAction = String.valueOf(lastJobRecord.get(0)[2]);
                                        String lastIndirectType = String.valueOf(lastJobRecord.get(0)[3]);
                                        //ppr
                                        String lastJobName = String.valueOf(lastJobRecord.get(0)[4]);
                                        String lastEmployeeCode = String.valueOf(lastJobRecord.get(0)[5]);
                                        String lastClient = String.valueOf(lastJobRecord.get(0)[6]);
                                        String lastWareHouse = String.valueOf(lastJobRecord.get(0)[7]);
                                        //j.RECORD_TIME,j.JOB_TYPE,j.JOB_ACTION,j.INDIRECT_TYPE,j.JOB_NAME,j.EMPLOYEE_CODE,j.CLIENT_ID,j.WAREHOUSE_ID
                                        Object[] job = {smallTime, lastJobType, lastJobAction, lastIndirectType, lastJobName,
                                                lastEmployeeCode, lastClient, lastWareHouse};
                                        jobRecord.add(0, job);
                                    }
                                }

                                for (int n = 0; n < jobRecord.size(); n++) {
                                    newStatisticsDTO.setWarehouseId((String) (jobRecord.get(0)[7]));
                                    newStatisticsDTO.setClientId(client);
                                    Object[] job = jobRecord.get(n);
                                    String jobTime = String.valueOf(job[0]);
                                    Date dateE = DateTimeUtil.getDateforStr(jobTime);
                                    dateS = DateTimeUtil.getDateforStr(tmp);
                                    timeSlot = DateTimeUtil.getHourAndMin(dateE, dateS, "min");
                                    if (n == 0) {
                                        if (timeSlot > thresholdB) {
                                            totalNoJobTime += timeSlot;
                                        }
                                    } else {
                                        if (String.valueOf(jobRecord.get(n - 1)[1]).equals("DIRECT")) {
                                            if (timeSlot > thresholdB) {
                                                totalNoJobTime += timeSlot;
                                            }
                                        }
                                    }
                                    tmp = jobTime;
                                }
                                Date dateEnd = DateTimeUtil.getDateforStr(bigTime);
                                dateS = DateTimeUtil.getDateforStr(tmp);
                                timeSlot = DateTimeUtil.getHourAndMin(dateEnd, dateS, "min");
                                if (jobRecord.size() > 0) {
                                    jobType = String.valueOf(jobRecord.get(jobRecord.size() - 1)[1]);
                                    if (jobType.equals("DIRECT")) {
                                        if (timeSlot > thresholdB) {
                                            totalNoJobTime += timeSlot;
                                        }
                                    }
                                } else {
                                    if (lastJobType.equals("DIRECT") || lastJobType.equals(""))
                                        totalNoJobTime += timeSlot;
                                }

                                double totalJobTime = Math.abs(h - totalNoJobTime / 60);
                                newStatisticsDTO.setEcWorkTime(String.valueOf(totalJobTime));
                                if (h > 0) {
                                    double timeRate = totalJobTime / h * 100;
                                    newStatisticsDTO.setEcWorkRate(String.valueOf(timeRate));
                                }

                                newEntitys.add(newStatisticsDTO);
                            }
                        }
                    }
                }
            }
        }
        for (NewStatisticsDTO tmp : newEntitys) {
            double ecTime = 0d;
            double totalTime = 0d;
            double ecRate = 0d;
            if(tmp.getEcWorkTime()!=null)
            ecTime = DateTimeUtil.getNewDecimal(tmp.getEcWorkTime(),2);
            if(tmp.getEcWorkTime() != null)
            totalTime = DateTimeUtil.getNewDecimal(tmp.getTotalClockTime(),2);
            if(tmp.getEcWorkRate()!=null)
            ecRate = DateTimeUtil.getNewDecimal(tmp.getEcWorkRate(),2);
            tmp.setEcWorkTime(String.valueOf(ecTime));
            tmp.setTotalClockTime(String.valueOf(totalTime));
            tmp.setEcWorkRate(String.valueOf(ecRate));
        }
        return newEntitys;
    }
    //返回工作时间详情页面DTO
    @Override
    public List<CTimeDetailDTO> getCtimedetail(String warehouseId, String clientId, String startDate, String
            endDate,String dayDate, String userName) throws ParseException {
        getThreshold();
        List<CTimeDetailDTO> entitys = new LinkedList<CTimeDetailDTO>();
        //员工打卡数据
        List<Object[]> statistics = statisticsQuery.getStatisticsTotal(userName, startDate, endDate, warehouseId);
        if (null != statistics && statistics.size() > 0) {
            String tmpTime = "";
            String lastEndTime = "";//用来记录上次下班打卡时间
            boolean flag = false;//标志位用来判断最后一次打卡类型（上班or下班）
            for (int i = 0; i < statistics.size(); i++) {
                Object[] statistic = statistics.get(i);
                String clockType = String.valueOf(statistic[3]);
                String clockTime = String.valueOf(statistic[0]);
                //计算打卡时间h
                if (clockType.equals("上班")) {
                    flag = true;
                    tmpTime = clockTime;
                    if (i == 0) {
                        if (StringUtils.isEmpty(dayDate)) lastEndTime = startDate.replace("T", " ");
                        else lastEndTime = clockTime.substring(0, clockTime.indexOf(" ")) + " 00:00:00";
                    }
                    CTimeDetailDTO cTimeDetailDTO = new CTimeDetailDTO();
                    cTimeDetailDTO.setMessage("Offclock");
                    cTimeDetailDTO.setActivityStartTime(lastEndTime);
                    cTimeDetailDTO.setActivityEndTime(clockTime);
                    Date date2 = DateTimeUtil.getDateforStr(clockTime);
                    Date date1 = DateTimeUtil.getDateforStr(lastEndTime);
                    double timeSlot = DateTimeUtil.getHourAndMin(date2, date1, "min");
                    cTimeDetailDTO.setTotal(timeSlot);
                    cTimeDetailDTO.setMap("Offclock");
                    entitys.add(cTimeDetailDTO);
                } else {
                    flag = false;
                    lastEndTime = clockTime;
                    if (i == 0) {
                        if (StringUtils.isEmpty(dayDate)) tmpTime = startDate.replace("T", " ");
                        else tmpTime = clockTime.substring(0, clockTime.indexOf(" ")) + " 00:00:00";
                    }
                    //直接间接工作时间
                    List<Object[]> jobRecord = statisticsQuery.getRecords(warehouseId, userName, tmpTime, clockTime,
                            null);
                    //==========计算第一步============
                    Date dateS;
                    double timeSlot;
                    String tmpT = tmpTime;
                    String jobType;
                    CTimeDetailDTO cTimeDetailDTO = new CTimeDetailDTO();
                    cTimeDetailDTO.setMessage("Onclock");
                    cTimeDetailDTO.setActivityStartTime(tmpTime);
                    cTimeDetailDTO.setActivityEndTime(clockTime);
                    Date date2 = DateTimeUtil.getDateforStr(clockTime);
                    Date date1 = DateTimeUtil.getDateforStr(tmpTime);
                    timeSlot = DateTimeUtil.getHourAndMin(date2, date1, "min");
                    cTimeDetailDTO.setTotal(timeSlot);
                    cTimeDetailDTO.setMap("Onclock");
                    entitys.add(cTimeDetailDTO);
                    if (jobRecord.size() == 0) {
                        CTimeDetailDTO cTDTO = new CTimeDetailDTO();
                        cTDTO.setMessage("No work");
                        cTDTO.setActivityStartTime(tmpTime);
                        cTDTO.setActivityEndTime(clockTime);
                        cTDTO.setTotal(timeSlot);
                        cTDTO.setMap("No work");
                        entitys.add(cTDTO);
                    }
                    getOnclockWork(entitys, clockTime, jobRecord, tmpT);
                    //========计算第一步完==================
                }
            }
            if (flag) {
                // 最后一次打卡类型是上班的特殊情况计算
                Date date2;
                String newStr;
                if (endDate != null) {
                    date2 = DateTimeUtil.getDateforStr(endDate.replace("T", " "));
                    newStr = endDate.replace("T", " ");
                } else {
                    date2 = DateTimeUtil.getDateLast(tmpTime);
                    newStr = tmpTime.substring(0, tmpTime.indexOf(" ")) + " 23:59:59";
                }
                //对比当前时间
                Date dateNow = new Date();
                if (dateNow.before(date2)) {
//                    date2 = dateNow;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    newStr = dateFormat.format(dateNow);
                }
                //=========
                List<Object[]> jobRecord = statisticsQuery.getRecords(warehouseId,userName, tmpTime, newStr, null);
                //==========计算第二步==========
                String tmpT = tmpTime;
                double timeSlot = 0d;
                CTimeDetailDTO cTimeDetailDTO = new CTimeDetailDTO();
                cTimeDetailDTO.setMessage("Onclock");
                cTimeDetailDTO.setActivityStartTime(tmpTime);
                cTimeDetailDTO.setActivityEndTime(newStr);
                Date dateTwo = DateTimeUtil.getDateforStr(newStr);
                Date date1 = DateTimeUtil.getDateforStr(tmpTime);
                timeSlot = DateTimeUtil.getHourAndMin(dateTwo, date1, "min");
                cTimeDetailDTO.setTotal(timeSlot);
                cTimeDetailDTO.setMap("Onclock");
                entitys.add(cTimeDetailDTO);
                if (jobRecord.size() == 0) {
                    CTimeDetailDTO cTDTO = new CTimeDetailDTO();
                    cTDTO.setMessage("No work");
                    cTDTO.setActivityStartTime(tmpTime);
                    cTDTO.setActivityEndTime(newStr);
                    cTDTO.setTotal(timeSlot);
                    cTDTO.setMap("No work");
                    entitys.add(cTDTO);
                }
                getOnclockWork(entitys, newStr, jobRecord, tmpT);
                //==========计算第二步完=========
            } else {
                Date date2;
                String newStr;
                if (endDate != null) {
                    date2 = DateTimeUtil.getDateforStr(endDate.replace("T", " "));
                    newStr = endDate.replace("T", " ");
                } else {
                    date2 = DateTimeUtil.getDateLast(lastEndTime);
                    newStr = lastEndTime.substring(0, lastEndTime.indexOf(" ")) + " 23:59:59";
                }
                //对比当前时间
                Date dateNow = new Date();
                if (dateNow.before(date2)) {
                    date2 = dateNow;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    newStr = dateFormat.format(dateNow);
                }
                Date date1 = DateTimeUtil.getDateforStr(lastEndTime);
                double timeSlot = DateTimeUtil.getHourAndMin(date2, date1, "min");
                CTimeDetailDTO cTimeDetailDTO = new CTimeDetailDTO();
                cTimeDetailDTO.setMessage("Offclock");
                cTimeDetailDTO.setActivityStartTime(lastEndTime);
                cTimeDetailDTO.setActivityEndTime(newStr);
                cTimeDetailDTO.setTotal(timeSlot);
                cTimeDetailDTO.setMap("Offclock");
                entitys.add(cTimeDetailDTO);
            }
        }
        else {
            String smallDate = "1970-01-01 00:00:00";
            String bigDate=startDate;
            List<Object[]> maxClockTime = statisticsQuery.getMaxClockTime(userName, smallDate, bigDate, warehouseId, null);
            if (maxClockTime != null && maxClockTime.size() > 0) {
                //选择日期段之前最近一次打卡类型
                String lastClockType = String.valueOf(maxClockTime.get(0)[3]);
                if (lastClockType.equals("上班")) {
                    //选择日期段之前最近一次打卡时间
                    String lastClockTime = String.valueOf(maxClockTime.get(0)[0]);
                    Date date1;
                    Date date2;
                    String smallTime;
                    String bigTime;
                    if (endDate != null) {
                        smallTime = startDate.replace("T", " ");
                        bigTime = endDate.replace("T", " ");
                        date1 = DateTimeUtil.getDateforStr(smallTime);
                        date2 = DateTimeUtil.getDateforStr(bigTime);
                    } else {
                        smallTime = startDate;
                        bigTime = endDate;

                        date1 = DateTimeUtil.getDateforStr(smallTime);
                        date2 = DateTimeUtil.getDateforStr(bigTime);
                    }
                    //对比当前时间
                    Date dateNow = new Date();
                    if (dateNow.before(date2)) {
                        date2 = dateNow;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        bigTime = dateFormat.format(dateNow);
                    }
                    //直接间接工作时间
                    List<Object[]> jobRecord = statisticsQuery.getRecords(warehouseId, userName, smallTime,
                            bigTime, null);

                    String tmpT = smallTime;
                    double timeSlot = 0d;
                    CTimeDetailDTO cTimeDetailDTO = new CTimeDetailDTO();
                    cTimeDetailDTO.setMessage("Onclock");
                    cTimeDetailDTO.setActivityStartTime(smallTime);
                    cTimeDetailDTO.setActivityEndTime(bigTime);
                    Date dateTwo = DateTimeUtil.getDateforStr(bigTime);
                    Date dateOne = DateTimeUtil.getDateforStr(smallTime);
                    timeSlot = DateTimeUtil.getHourAndMin(dateTwo, dateOne, "min");
                    cTimeDetailDTO.setTotal(timeSlot);
                    cTimeDetailDTO.setMap("Onclock");
                    entitys.add(cTimeDetailDTO);

                    List<Object[]> lastJobRecord = statisticsQuery.getMaxJobAction(warehouseId, userName, lastClockTime,
                            smallTime, null);
                    if (lastJobRecord != null && lastJobRecord.size() > 0) {
                        String lastJobType = String.valueOf(lastJobRecord.get(0)[0]);
                        if (lastJobType.equals("INDIRECT")) {
                            String lastJobAction = String.valueOf(lastJobRecord.get(0)[2]);
                            String lastIndirectType = String.valueOf(lastJobRecord.get(0)[3]);
                            //ppr
                            String lastJobName = String.valueOf(lastJobRecord.get(0)[4]);
                            String lastEmployeeCode = String.valueOf(lastJobRecord.get(0)[5]);
                            String lastClient = String.valueOf(lastJobRecord.get(0)[6]);
                            String lastWareHouse = String.valueOf(lastJobRecord.get(0)[7]);

                            //j.RECORD_TIME,j.JOB_TYPE,j.JOB_ACTION,j.INDIRECT_TYPE,j.JOB_NAME,j.EMPLOYEE_CODE,j.CLIENT_ID,j.WAREHOUSE_ID
                            Object[] job = {smallTime,lastJobType,lastJobAction,lastIndirectType,lastJobName,
                                    lastEmployeeCode,lastClient,lastWareHouse};
                            jobRecord.add(0,job);
                            getOnclockWork(entitys, bigTime, jobRecord, tmpT);
                        }
                        else{
                            if (jobRecord.size() == 0) {
                                CTimeDetailDTO cTDTO = new CTimeDetailDTO();
                                cTDTO.setMessage("No work");
                                cTDTO.setActivityStartTime(smallTime);
                                cTDTO.setActivityEndTime(bigTime);
                                cTDTO.setTotal(timeSlot);
                                cTDTO.setMap("No work");
                                entitys.add(cTDTO);
                            }
                            getOnclockWork(entitys, bigTime, jobRecord, tmpT);
                        }
                    }
                    else{
                        if (jobRecord.size() == 0) {
                            CTimeDetailDTO cTDTO = new CTimeDetailDTO();
                            cTDTO.setMessage("No work");
                            cTDTO.setActivityStartTime(smallTime);
                            cTDTO.setActivityEndTime(bigTime);
                            cTDTO.setTotal(timeSlot);
                            cTDTO.setMap("No work");
                            entitys.add(cTDTO);
                        }
                        getOnclockWork(entitys, bigTime, jobRecord, tmpT);
                    }
                }
            }
        }
        /**
         * 如果当前项message的后一项不是正常工作且前一项不是Onclock(要排除当前项是Onclock。。。)
         * 那么当前项位置后添加 当前正常工作项 时间为十秒 且 后一项时间减十秒
         * 或者是一秒操作完成的也加十秒
         */
        for (int n = 1; n < entitys.size() - 1; n++) {
            CTimeDetailDTO list3 = entitys.get(n-1);
            CTimeDetailDTO list1 = entitys.get(n);
            CTimeDetailDTO list2 = entitys.get(n + 1);
            if ((!"Onclock".equals(list3.getMessage()) && !list2.getMessage().equals(list2.getMap())
            && !"Onclock".equals(list1.getMessage())) || list1.getTotal() == 0) {
                CTimeDetailDTO addList = new CTimeDetailDTO();
                addList.setMessage(list1.getMap());
                addList.setActivityStartTime(list1.getActivityEndTime());
                //添加项时间为十秒,后一项开始时间减十秒
                String newDateTime = getNewDateTime(list1.getActivityEndTime());
                addList.setActivityEndTime(newDateTime);
                addList.setTotal(1.0/6);
                list2.setActivityStartTime(newDateTime);
                double ts = DateTimeUtil.getNewDecimal((list2.getTotal() - 1.0/6),2);
                list2.setTotal(ts);
                addList.setMap(list1.getMap());
                addList.setActionType(list1.getActionType());
                entitys.add(n + 1, addList);
                n++;
            }
        }
        /**
         * 因为之前算法：1.前间接A后B 间隔不做判断记入B直接工作 2.前直接A后B 间隔小于阈值记入B直接工作
         * 所以后者所有正常工作的message和map都应赋予前者message(首尾除外)
         * 此步骤修改错误数据保证数据正确
         */
        for (int n = entitys.size() - 2; n > 0; n--) {
            CTimeDetailDTO list1 = entitys.get(n);
            CTimeDetailDTO list2 = entitys.get(n - 1);
            CTimeDetailDTO list3 = entitys.get(n + 1);
            if (!((list1.getMessage().equals("Offclock")) || (list1.getMessage().equals("Onclock")) || (list3.getMessage()
                    .equals("Offclock")) || (list2.getMessage().equals("Onclock")))
                    && (list1.getMessage().equals(list1.getMap()))) {
                list1.setMessage(list2.getMap());
                list1.setMap(list2.getMap());
                list1.setActionType(list2.getActionType());
            }
        }
        for (int n = entitys.size() - 1; n > 0; n--) {
            CTimeDetailDTO list1 = entitys.get(n);
            CTimeDetailDTO list2 = entitys.get(n - 1);
            if ((list1.getMessage().equals(list2.getMessage()))//工作详情(Onclock,Offclock,No work,prepareatory work,JOB_ACTION)
                    && (list1.getMap().equals(list2.getMap()))//(Onclock,Offclock,打卡期间全部No work)以及三种对应的JOB_ACTION
                    && (list1.getMessage().equals(list1.getMap()))) {
                list2.setActivityEndTime(list1.getActivityEndTime());
                double ts = list1.getTotal() + list2.getTotal();
                list2.setTotal(ts);
                entitys.remove(n);
            }
        }
        for (CTimeDetailDTO tmp : entitys) {
            double total = DateTimeUtil.getNewDecimal(tmp.getTotal(),2);
            tmp.setTotal(total);
        }
        return entitys;
    }

    @Override
    public UserDTO getUserInfo(String employeeCode) {
        return userMapper.toDTO(userRepository.findByUsername(employeeCode));
    }

    @Override
    public UserDTO checkUserInfo(String employeeCode) {
        String currentWarehouseId = applicationContext.getCurrentWarehouse();
        Warehouse warehouse = warehouseRepository.retrieve(currentWarehouseId);
        Set<User> users = warehouse.getUsers();
        for (User user : users) {
            if(employeeCode.equals(user.getUsername())) {
                return userMapper.toDTO(user);
            }
        }
        throw new ApiException("TOT_EMPLOYEECODE_ERROR");
//        User user = userRepository.findByUsername(employeeCode);
//        if (user == null) {
//            throw new ApiException("TOT_EMPLOYEECODE_ERROR");
//        }
//        return userMapper.toDTO(user);
    }

    @Override
    public List<JobTypeDTO> getDJobType(String typeTable) {
        List<Object[]> dJobType = statisticsQuery.getDJobType(typeTable);
        List<JobTypeDTO> jobTypeDTOList = new LinkedList<>();
        for (Object row : dJobType) {
            JobTypeDTO jobTypeDTO = new JobTypeDTO();
            Object[] arr = (Object[]) row;
            jobTypeDTO.setName((String) arr[0]);
            jobTypeDTO.setDescription((String) arr[1]);
            jobTypeDTOList.add(jobTypeDTO);
        }
        return jobTypeDTOList;
    }

    public void getThreshold() {
        String warehouseId = applicationContext.getCurrentWarehouse();
        Jobthreshold jobthreshold = jobthresholdRepository.getJobthreshold(warehouseId);
        if (null == jobthreshold) {
            Jobthreshold insertJobthreshold = new Jobthreshold();
            insertJobthreshold.setThresholdA(10);
            insertJobthreshold.setThresholdB(20);
            insertJobthreshold.setWarehouseId(warehouseId);
            try {
                entityManager.persist(insertJobthreshold);
            } catch (Exception e) {
                LOGGER.error("插入失败"+e.getMessage(),e);
                throw new ApiException("JOBTHRESHOLD_ERROR");
            }
            thresholdA = 10;
            thresholdB = 20;
        }
        else {
            thresholdA = jobthreshold.getThresholdA();
            thresholdB = jobthreshold.getThresholdB();
        }
    }

//    Jobthreshold h = getThreshold();

//    int a = h.getThresholdA();

    //获取工作时间内工作详情(抽取方法)
    private void getOnclockWork(List<CTimeDetailDTO> entitys, String clockTime, List<Object[]> jobRecord, String tmpT) throws
            ParseException {
        String jobType;
        Date dateS;
        double timeSlot;
        for (int n = 0; n < jobRecord.size(); n++) {
            CTimeDetailDTO cTDTO = new CTimeDetailDTO();
            Object[] job = jobRecord.get(n);
            String jobTime = String.valueOf(job[0]);
            jobType = String.valueOf(job[1]);
            String jobAction = String.valueOf(job[2]);
            String indirectType = String.valueOf(job[3]);
            Date dateE = DateTimeUtil.getDateforStr(jobTime);
            dateS = DateTimeUtil.getDateforStr(tmpT);
            timeSlot = DateTimeUtil.getHourAndMin(dateE, dateS, "min");
            cTDTO.setActivityStartTime(tmpT);
            cTDTO.setActivityEndTime(jobTime);
            cTDTO.setTotal(timeSlot);
            cTDTO.setMap(jobAction);
            //正常设置actionType和message
            setActionTypeAndMessage(jobRecord, timeSlot, jobType, n, cTDTO, jobAction, indirectType);
            tmpT = jobTime;
            entitys.add(cTDTO);
        }
        if (jobRecord.size() > 0) {
            CTimeDetailDTO cTDTO = new CTimeDetailDTO();
            String time = String.valueOf(jobRecord.get(jobRecord.size() - 1)[0]);
            jobType = String.valueOf(jobRecord.get(jobRecord.size() - 1)[1]);
            String action = String.valueOf(jobRecord.get(jobRecord.size() - 1)[2]);
            String indirectType = String.valueOf(jobRecord.get(jobRecord.size() - 1)[3]);
            cTDTO.setActivityStartTime(time);
            cTDTO.setActivityEndTime(clockTime);
            Date dateE = DateTimeUtil.getDateforStr(clockTime);
            Date dateF = DateTimeUtil.getDateforStr(time);
            timeSlot = DateTimeUtil.getHourAndMin(dateE, dateF, "min");
            cTDTO.setTotal(timeSlot);
            cTDTO.setMap(action);
            if (jobType.equals("DIRECT")) {
                cTDTO.setActionType("直接工作");
                if (timeSlot > thresholdB) {
                    cTDTO.setMessage("No work");
                }
                if (timeSlot <= thresholdB && timeSlot > thresholdA) {
                    cTDTO.setMessage("preparatory work-" + action);
                }
                if (timeSlot <= thresholdA) {
                    cTDTO.setMessage(action);
                }
            } else {
                cTDTO.setActionType(indirectType);
                cTDTO.setMessage(action);
            }
            entitys.add(cTDTO);
        }
    }

    //时间加十秒
    private static String getNewDateTime(String dateTime) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        Date date = DateTimeUtil.getDateforStr(dateTime);
        // 对 calendar 设置为 date 所定的日期
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, 10);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 设置工作类型和message
     * (1.前间接A后B 间隔不做判断记入B直接工作 2.前直接A后B 间隔小于阈值记入B直接工作)*最后修改* 修改或者合并最后进行
     *
     * @param jobRecord
     * @param timeSlot
     * @param jobType
     * @param n
     * @param cTDTO
     * @param jobAction
     * @param indirectType
     */
    private void setActionTypeAndMessage(List<Object[]> jobRecord, double timeSlot, String jobType, int n, CTimeDetailDTO
            cTDTO, String jobAction, String indirectType) {
        if (jobType.equals("DIRECT")) {
            cTDTO.setActionType("直接工作");
            if (n > 0 && String.valueOf(jobRecord.get(n - 1)[1]).equals("INDIRECT")) {
                //前间接A后直接B 记入B直接工作*最后修改*
                cTDTO.setMessage(jobAction);
            } else {
                if (timeSlot > thresholdB) {
                    cTDTO.setMessage("No work");
                }
                if (timeSlot <= thresholdB && timeSlot > thresholdA) {
                    cTDTO.setMessage("preparatory work-" + jobAction);
                }
                if (timeSlot <= thresholdA) {
                    cTDTO.setMessage(jobAction);
                }
            }
        } else {
            cTDTO.setActionType(indirectType);
            if (n > 0 && String.valueOf(jobRecord.get(n - 1)[1]).equals("DIRECT")) {
                if (timeSlot > thresholdB) {
                    cTDTO.setMessage("No work");
                }
                if (timeSlot <= thresholdB && timeSlot > thresholdA) {
                    cTDTO.setMessage("preparatory work-" + jobAction);
                }
                if (timeSlot <= thresholdA) {
                    cTDTO.setMessage(jobAction);
                }
            } else {
                if (n == 0) {
                    if (timeSlot > thresholdB) {
                        cTDTO.setMessage("No work");
                    }
                    if (timeSlot <= thresholdB && timeSlot > thresholdA) {
                        cTDTO.setMessage("preparatory work-" + jobAction);
                    }
                    if (timeSlot <= thresholdA) {
                        cTDTO.setMessage(jobAction);
                    }
                }
                //前间接A后间接B 记入B直接工作*最后修改*
                else cTDTO.setMessage(jobAction);
            }
        }
    }
}
