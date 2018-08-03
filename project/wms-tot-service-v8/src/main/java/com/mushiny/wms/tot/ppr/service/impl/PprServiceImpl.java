package com.mushiny.wms.tot.ppr.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.tot.attendance.repository.AttendanceRepository;
import com.mushiny.wms.tot.general.crud.mapper.UserMapper;
import com.mushiny.wms.tot.general.domain.User;
import com.mushiny.wms.tot.general.domain.Warehouse;
import com.mushiny.wms.tot.general.repository.UserRepository;
import com.mushiny.wms.tot.general.repository.WarehouseRepository;
import com.mushiny.wms.tot.job.repository.JobRepository;
import com.mushiny.wms.tot.jobcategory.crud.dto.JobcategoryDTO;
import com.mushiny.wms.tot.jobcategory.domain.Jobcategory;
import com.mushiny.wms.tot.jobcategory.repository.JobcategoryRepository;
import com.mushiny.wms.tot.jobrecord.repository.JobrecordRepository;
import com.mushiny.wms.tot.jobthreshold.crud.mapper.JobthresholdMapper;
import com.mushiny.wms.tot.jobthreshold.domain.Jobthreshold;
import com.mushiny.wms.tot.jobthreshold.repository.JobthresholdRepository;
import com.mushiny.wms.tot.jobthreshold.service.impl.JobthresholdServiceImpl;
import com.mushiny.wms.tot.ppr.domain.JobCategoryRelation;
import com.mushiny.wms.tot.ppr.query.dto.PprCTimeDetailDTO;
import com.mushiny.wms.tot.ppr.query.dto.PprDetailOfEmployeeDTO;
import com.mushiny.wms.tot.ppr.query.dto.PprDetailOfJobDTO;
import com.mushiny.wms.tot.ppr.query.dto.PprMainPageDTO;
import com.mushiny.wms.tot.ppr.query.enums.NormalSize;
import com.mushiny.wms.tot.ppr.query.hql.PprStatisticsQuery;
import com.mushiny.wms.tot.ppr.repository.JobCategoryRelationRepository;
import com.mushiny.wms.tot.ppr.service.JobCategoryRelationService;
import com.mushiny.wms.tot.ppr.service.PprService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mushiny.wms.tot.job.domain.Job;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class PprServiceImpl implements PprService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PprServiceImpl.class);
    private final UserRepository userRepository;
    private final PprStatisticsQuery pprStatisticsQuery;
    private final AttendanceRepository attendanceRepository;
    private final JobrecordRepository jobrecordRepository;
    private final JobthresholdRepository jobthresholdRepository;
    private final UserMapper userMapper;
    private final JobCategoryRelationService jobCategoryRelationService;
    private final JobRepository jobRepository;
    private final JobcategoryRepository jobcategoryRepository;
    private final WarehouseRepository warehouseRepository;
    private final ApplicationContext applicationContext;

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    public PprServiceImpl(UserRepository userRepository,
                          PprStatisticsQuery pprStatisticsQuery,
                          AttendanceRepository attendanceRepository,
                          JobrecordRepository jobrecordRepository,
                          JobthresholdRepository jobthresholdRepository,
                          UserMapper userMapper,
                          JobthresholdMapper jobthresholdMapper,
                          JobCategoryRelationService jobCategoryRelationService,
                          JobRepository jobRepository,
                          JobcategoryRepository jobcategoryRepository,
                          WarehouseRepository warehouseRepository,
                          ApplicationContext applicationContext) {
        this.userRepository = userRepository;
        this.pprStatisticsQuery = pprStatisticsQuery;
        this.attendanceRepository = attendanceRepository;
        this.jobrecordRepository = jobrecordRepository;
        this.jobthresholdRepository = jobthresholdRepository;
        this.userMapper = userMapper;
        this.jobCategoryRelationService = jobCategoryRelationService;
        this.jobRepository = jobRepository;
        this.jobcategoryRepository = jobcategoryRepository;
        this.warehouseRepository = warehouseRepository;
        this.applicationContext = applicationContext;
    }

    int thresholdA;
    int thresholdB;

    //返回工作时间详情页面DTO
    public List<PprCTimeDetailDTO> getCtimedetail(String warehouseId, String clientId, String startDate, String endDate,
                                                  String userName) throws ParseException {
        List<PprCTimeDetailDTO> entitys = getPprCTimeDetailDTOS(warehouseId, clientId, startDate, endDate, userName);
        for (int i = 0; i < entitys.size(); i++) {
            for (int n = i+1; n < entitys.size(); n++) {
                if(entitys.get(n).getCategoryName().equals(entitys.get(i).getCategoryName()) &&
                        (entitys.get(n).getMessage().contains("preparatory work-")==entitys.get(i).getMessage().contains("preparatory work-"))) {
                    if("直接工作".equals(entitys.get(n).getActionType())) {
                        if (entitys.get(i).getSize().trim().equals(entitys.get(n).getSize().trim())) {
                            entitys.get(i).setTotal(entitys.get(i).getTotal()+entitys.get(n).getTotal());
                            entitys.remove(n);
                            n--;
                        }
                    }
                    else {
                        entitys.get(i).setTotal(entitys.get(i).getTotal()+entitys.get(n).getTotal());
                        entitys.remove(n);
                        n--;
                    }
                }
            }
        }
        return entitys;
    }

    @Override
    public List<PprMainPageDTO> getRecordsForPpr(String warehouseId, String clientId, String startDate, String endDate) throws ParseException {
        List<PprMainPageDTO> lastList = jobCategoryRelationService.getJobCategoryRelations();
        Warehouse warehouse = warehouseRepository.retrieve(warehouseId);
        Set<User> users = warehouse.getUsers();
        if (users.size() > 0) {
            for (User user : users) {
                String userName = user.getUsername();
                List<Object[]> recordForPprList = pprStatisticsQuery.getRecordByPpr(warehouseId, clientId, startDate, endDate, userName,false);
                List<Object[]> tmpCompareList = pprStatisticsQuery.getRecordByPpr(warehouseId, clientId, startDate, endDate, userName,true);
                List<PprCTimeDetailDTO> listForHours = getCtimedetail(warehouseId, clientId, startDate, endDate, userName);
                Long count = 0L;
                for (PprCTimeDetailDTO tmp : listForHours) {
                    if(tmp.getMessage().contains("preparatory work")) count++;
                }
                //所选时间前间接工作的延续，所以第一次为间接工作 而tmpCompareList没有记录
                if(tmpCompareList.size() < (listForHours.size()-count)) {
                    Object[] job = {listForHours.get(0).getCategoryName(),null,null,null,"间接工作",
                            listForHours.get(0).getWarehouseId(),listForHours.get(0).getClientId(),listForHours.get(0).getJobName()};
                    recordForPprList.add(0,job);
                }
                List<PprMainPageDTO> pprList = new LinkedList<>();
                List<PprMainPageDTO> modelList = new LinkedList<>();
                if (null != recordForPprList && recordForPprList.size() > 0) {
                    for (int i = 0; i < recordForPprList.size(); i++) {
                        Object[] tmpList = recordForPprList.get(i);
                        String size = null;
                        if ("小".equals(tmpList[1])) size = "Small";
                        if ("中".equals(tmpList[1])) size = "Medium";
                        if ("大".equals(tmpList[1])) size = "Large";
                        if ("超大".equals(tmpList[1])) size = "OverSize";

                        //遍历直接工作记录
                        boolean flag = false;
                        PprMainPageDTO pprMainPageDTO = new PprMainPageDTO();
                        pprMainPageDTO.setCategoryName((String) tmpList[0]);
                        pprMainPageDTO.setJobType((String) tmpList[4]);
                        pprMainPageDTO.setWarehouseId((String)tmpList[5]);
                        pprMainPageDTO.setClientId((String)tmpList[6]);
                        if("直接工作".equals(tmpList[4])) {
                            pprMainPageDTO.setLineItems(tmpList[0] + "-" + size);
                            pprMainPageDTO.setUnit((String) tmpList[2]);
                            pprMainPageDTO.setAmount(((BigDecimal) tmpList[3]).longValue());
                        }
                        else {
                            pprMainPageDTO.setLineItems((String) tmpList[0]);
                            pprMainPageDTO.setUnit("-");
                            pprMainPageDTO.setAmount(0);
                        }
                        pprMainPageDTO.setHours(0);
                        pprMainPageDTO.setRates(0);

                        for (PprCTimeDetailDTO tmpDTO : listForHours) {
                            PprMainPageDTO tmp = new PprMainPageDTO();
                            tmp.setCategoryName((String) tmpList[0]);
                            tmp.setJobType((String) tmpList[4]);
                            tmp.setWarehouseId((String) tmpList[5]);
                            tmp.setClientId((String) tmpList[6]);
                            if ("直接工作".equals(tmpDTO.getActionType())) {
                                if (tmpList[0].equals(tmpDTO.getCategoryName()) && tmpList[1].equals(tmpDTO.getSize())) {
                                    if (tmpDTO.getMessage().contains("preparatory work")) {
                                        tmp.setLineItems(tmpList[0] + "-" + "Prep");
                                        tmp.setUnit((String) tmpList[2]);
                                        tmp.setAmount(0);
                                        tmp.setHours(tmpDTO.getTotal());
                                        tmp.setRates(0);
                                    } else {
                                        tmp.setLineItems(tmpList[0] + "-" + size);
                                        tmp.setUnit((String) tmpList[2]);
                                        tmp.setAmount(((BigDecimal) tmpList[3]).longValue());
                                        tmp.setHours(tmpDTO.getTotal());
                                        double rate = tmp.getAmount() / tmpDTO.getTotal();
                                        tmp.setRates(rate);
                                    }
                                    pprList.add(tmp);
                                    flag = true;
                                }
                            }
                            //遍历间接工作记录
                            if ("普通间接".equals(tmpDTO.getActionType()) || "超级间接".equals(tmpDTO.getActionType())) {
                                if (tmpList[0].equals(tmpDTO.getCategoryName())) {
                                    tmp.setCategoryName((String) tmpList[0]);
                                    tmp.setJobType("间接工作");
                                    tmp.setLineItems((String) tmpList[0]);
                                    tmp.setUnit("-");
                                    tmp.setAmount(0);
                                    tmp.setHours(tmpDTO.getTotal());
                                    tmp.setRates(0);
                                    pprList.add(tmp);
                                    flag = true;
                                }
                            }
                        }
                        if(!flag) pprList.add(pprMainPageDTO);
                    }
                    //合并所有的amount，hours
                    for (int n = 0; n < pprList.size(); n++) {
                        String lineItems = pprList.get(n).getLineItems();
                        for (int m = n + 1; m < pprList.size(); m++) {
                            String nextLineItems = pprList.get(m).getLineItems();
                            if (lineItems.equals(nextLineItems)) {
                                pprList.get(n).setAmount(pprList.get(n).getAmount() + pprList.get(m).getAmount());
                                double hour = pprList.get(n).getHours() + pprList.get(m).getHours();
                                pprList.get(n).setHours(hour);
                                pprList.get(n).setRates(pprList.get(n).getAmount() / hour);
                                pprList.remove(m);
                                m--;
                            }
                        }
                    }
                    // 建伟那边完整DTO
                    modelList = getLastList(pprList);
                    //添加大部分间接工作amount，rate
                    for (PprMainPageDTO tmp : modelList) {
                        if ("INDIRECT".equals(tmp.getJobType())) {
                            long amount = 0;
                            for (PprMainPageDTO tmp1 : modelList) {
                                if ("直接工作".equals(tmp1.getJobType()) && tmp.getCoreProcesses().equals(tmp1.getCoreProcesses())) {
                                    amount += tmp1.getAmount();
                                }
                            }
                            tmp.setAmount(amount);
                            if (tmp.getHours() != 0) {
                                tmp.setRates(tmp.getAmount() / tmp.getHours());
                            }
                        }
                    }
                    for (PprMainPageDTO tmp : modelList) {
                        if ("OUT_INDIRECT".equals(tmp.getJobType())) {
                            tmp.setAmount(modelList.get(0).getAmount());
                        }
                    }
                    //特殊处理！！
                    List<Object[]> specialForPprPallet = pprStatisticsQuery.getSpecialByPallet(warehouseId, clientId, startDate, endDate, userName);
                    List<Object[]> specialForSD = pprStatisticsQuery.getSpecialBySD(warehouseId, clientId, startDate, endDate, userName);
                    for (int i = 0; i < modelList.size(); i++) {
                        //求三个total的时间的amount 不包括Prep的时间
                        setSpecialTotal(pprList, modelList, i, "Rebatch");
                        setSpecialTotal(pprList, modelList, i, "Sort-Total");
                        setSpecialTotal(pprList, modelList, i, "Dock");
                    }
                    for (PprMainPageDTO tmp : modelList) {
                        // Line Items项 Total添加
                        if (tmp.getLineItems().contains("Total") && !tmp.getCategoryName().contains("Total")) {
                            long amounts = 0L;
                            double hours = 0d;
                            String unit = "-";
                            for (PprMainPageDTO tmp1 : modelList) {
                                if (tmp.getCategoryName().equals(tmp1.getCategoryName())) {
                                    if(tmp1.getAmount() > 0 && "-".equals(unit)) unit = tmp1.getUnit();
                                    amounts += tmp1.getAmount();
                                    hours += tmp1.getHours();
                                }
                            }
                            tmp.setUnit(unit);
                            tmp.setAmount(amounts);
                            if (hours > 0) {
                                tmp.setHours(hours);
                                tmp.setRates(amounts / hours);
                            }
                        }
                    }
                    for (PprMainPageDTO tmp : modelList) {
                        //项目下的 Total添加
                        if (tmp.getCategoryName().contains("Total") && !tmp.getCoreProcesses().contains("Total")) {
                            long amounts = 0;
                            double hours = 0;
                            String unit = "-";
                            for (PprMainPageDTO tmp1 : modelList) {
                                if (tmp.getCoreProcesses().equals(tmp1.getCoreProcesses()) &&
                                        ("INDIRECT".equals(tmp1.getJobType()))) {
                                    hours += tmp1.getHours();
//                                    if ("-".equals(unit)) unit = tmp1.getUnit();
                                     if(amounts==0) amounts = tmp1.getAmount();
                                }
                                if("Rebatch".equals(tmp.getCoreProcesses()) || "Sort".equals(tmp.getCoreProcesses())
                                        ||"Dock".equals(tmp.getCoreProcesses())) {
                                    if (tmp.getCoreProcesses().equals(tmp1.getCoreProcesses())) {
                                        hours += tmp1.getHours();
                                        amounts += tmp1.getAmount();
                                    }
                                }
                            }
                            tmp.setUnit(unit);
                            tmp.setAmount(amounts);
                            if (hours > 0) {
                                tmp.setHours(hours);
                                tmp.setRates(amounts / hours);
                            }
                        }
                    }

                    for (PprMainPageDTO tmp : modelList) {
                        //core下的 Total添加
                        if ("IB Total".equals(tmp.getCoreProcesses())) {
                            long amounts = 0;
                            double hours = 0d;
                            String unit = "-";
                            for (PprMainPageDTO tmp1 : modelList) {
                                if (tmp.getMainProcesses().equals(tmp1.getMainProcesses()) &&
                                        ("OUT_INDIRECT".equals(tmp.getJobType())
                                                || tmp1.getCategoryName().contains("Total"))) {
                                    hours += tmp1.getHours();
//                                    if ("-".equals(unit)) unit = tmp1.getUnit();
                                }
                                if ("Receive Dock".equals(tmp1.getLineItems().trim())) {
                                    amounts = tmp1.getAmount();
                                }
                            }
                            tmp.setUnit(unit);
                            tmp.setAmount(amounts);
                            if (hours > 0) {
                                tmp.setHours(hours);
                                tmp.setRates(amounts / hours);
                            }
                        }
                        else {
                            if ("OB Total".equals(tmp.getCoreProcesses()) || "OB_OUT_INDIRECT".equals(tmp.getJobType())) {
                                long amounts = 0;
                                double hours = 0d;
                                String unit = "-";
                                for (PprMainPageDTO tmp1 : modelList) {
                                    if ("OB Total".equals(tmp.getCoreProcesses()) &&
                                            tmp.getMainProcesses().equals(tmp1.getMainProcesses()) &&
                                            ("间接工作".equals(tmp.getJobType()) || "INDIRECT".equals(tmp.getJobType()) ||
                                    "OB_OUT_INDIRECT".equals(tmp.getJobType()) || tmp1.getCategoryName().contains("Total"))) {
                                        hours += tmp1.getHours();
//                                        if ("-".equals(unit)) unit = tmp1.getUnit();
                                    }
                                    if ("Dock".equals(tmp1.getLineItems().trim())) {
                                        amounts = tmp1.getAmount();
                                    }
                                }
                                tmp.setUnit(unit);
                                tmp.setAmount(amounts);
                                if (hours > 0) {
                                    tmp.setHours(hours);
                                    tmp.setRates(amounts / hours);
                                }
                            }
                        }
                    }
                    //特殊处理
                    for (int i = 0; i < modelList.size(); i++) {
                        //求俩个特殊amount to from from
                        setSpecialNoSize(modelList, specialForPprPallet, i, "Pallet Receive To Stow-Pallet");
                        setSpecialNoSize(modelList, specialForSD, i, "Sort");
//                        setSpecialNoSize(modelList, specialForSD, i, "Dock");
                    }
                    // 合并各个员工数据
                    for (PprMainPageDTO tmp : lastList) {
                        for (PprMainPageDTO tmp1 : modelList) {
                            if(tmp.getLineItems().equals(tmp1.getLineItems())) {
                                tmp.setWarehouseId(tmp1.getWarehouseId());
                                tmp.setClientId(tmp1.getClientId());
                                tmp.setUnit(tmp1.getUnit());
                                tmp.setAmount(tmp.getAmount()+tmp1.getAmount());
                                tmp.setHours(tmp.getHours()+tmp1.getHours());
                                tmp.setPlanHours(tmp.getAmount()/tmp.getPlanRate());
                                if(tmp.getHours()>0) {
                                    tmp.setRates(tmp.getAmount()/tmp.getHours());
                                    DecimalFormat df = new DecimalFormat("0%");
                                    tmp.setQuotient(df.format(tmp.getPlanHours()/tmp.getHours()));
                                }
                                tmp.setIncrement(tmp.getHours()-tmp.getPlanHours());
                                break;
                            }
                        }
                    }
                }
            }
        }
        for (PprMainPageDTO tmp : lastList) {
            tmp.setCategoryName(tmp.getCategoryName().trim());
            double hour = DateTimeUtil.getNewDecimal(tmp.getHours(),3);
            double rate = DateTimeUtil.getNewDecimal(tmp.getRates(),3);
            double planHours = DateTimeUtil.getNewDecimal(tmp.getPlanHours(),3);
            double increment = DateTimeUtil.getNewDecimal(tmp.getIncrement(),3);
            tmp.setHours(hour);
            tmp.setRates(rate);
            tmp.setPlanHours(planHours);
            tmp.setIncrement(increment);
        }
        return lastList;
    }

    @Override
    public List getRecordsForPprDetail(String warehouseId, String clientId, String category,
                                                          String startDate, String endDate) throws ParseException {
        List allList = new LinkedList<>();
        List<JobCategoryRelation> jobCategoryRelation = jobCategoryRelationService.getRelationsByCategory(category);
        int relationSize = jobCategoryRelation.size();
        List<PprDetailOfJobDTO> headList = getPprDetailOfJobDTOS(category,jobCategoryRelation);
        boolean lastFlag =false;
        Warehouse warehouse = warehouseRepository.retrieve(warehouseId);
        Set<User> users = warehouse.getUsers();
        List jobCodeList =new LinkedList();//有记录的工作存放list
        for (User user : users) {
            String userName = user.getUsername();
            List<Object[]> recordForPprJobList = pprStatisticsQuery.getRecordByPprJob(warehouseId, clientId, category, startDate, endDate, userName,false);
            List<Object[]> tmpCompareList = pprStatisticsQuery.getRecordByPprJob(warehouseId, clientId, category, startDate, endDate, userName,true);
            List<PprCTimeDetailDTO> listForHours = getCtimedetailByJob(warehouseId, clientId, startDate, endDate, userName);
            //listForHours算的是时间段所有工作的时间详情，故遍历去掉非所选category的工作
            for (int i =0;i<listForHours.size();i++) {
                if (!listForHours.get(i).getCategoryName().equals(category)) {
                    listForHours.remove(i);
                    i--;
                }
            }
            Long count = 0L;
            for (PprCTimeDetailDTO tmp : listForHours) {
                if(tmp.getMessage().contains("preparatory work")) count++;
            }
            //考虑所需时间段没有间接工作记录，但首次是间接工作的情况
            if(tmpCompareList.size() < listForHours.size()-count) {
                Object[] job = {listForHours.get(0).getJobCode(),null,BigInteger.valueOf(1),null,"间接工作", listForHours.get(0).getEmployeeCode(),
                        listForHours.get(0).getEmployeeName(),listForHours.get(0).getCategoryName(),listForHours.get(0).getJobName()};
                recordForPprJobList.add(0,job);
            }
            List<PprDetailOfJobDTO> pprList = new LinkedList<>();
            if (null != recordForPprJobList && recordForPprJobList.size() > 0) {
                lastFlag = true;
                if ("间接工作".equals(recordForPprJobList.get(0)[4])) {
                    List<PprMainPageDTO> allData = getRecordsForPpr(warehouseId, clientId, startDate, endDate);
                    for (int i = 0; i < recordForPprJobList.size(); i++) {
                        Object[] tmpList = recordForPprJobList.get(i);
                        boolean flag = true;
                        PprDetailOfJobDTO noHoursTmp = new PprDetailOfJobDTO();
                        noHoursTmp.setJobCode((String) tmpList[0]);
                        noHoursTmp.setJobName((String) tmpList[8]);
                        noHoursTmp.setTimes(((BigInteger) tmpList[2]).longValue());
                        noHoursTmp.setJobType((String) tmpList[4]);
                        noHoursTmp.setEmployeeCode((String) tmpList[5]);
                        noHoursTmp.setEmployeeName((String) tmpList[6]);
                        noHoursTmp.setCategoryName((String) tmpList[7]);
                        for (PprMainPageDTO pprMainPageDTO : allData) {
                            if(category.equals(pprMainPageDTO.getCategoryName())) {
                                noHoursTmp.setAmount(pprMainPageDTO.getAmount());
                                break;
                            }
                        }
                        for (PprCTimeDetailDTO tmpDTO : listForHours) {
                            PprDetailOfJobDTO tmp = new PprDetailOfJobDTO();
                            tmp.setJobCode((String) tmpList[0]);
                            tmp.setJobName((String) tmpList[8]);
                            tmp.setTimes(((BigInteger) tmpList[2]).longValue());
                            tmp.setJobType((String) tmpList[4]);
                            tmp.setEmployeeCode((String) tmpList[5]);
                            tmp.setEmployeeName((String) tmpList[6]);
                            tmp.setCategoryName((String) tmpList[7]);
                            for (PprMainPageDTO pprMainPageDTO : allData) {
                                if(category.equals(pprMainPageDTO.getCategoryName())) {
                                    tmp.setAmount(pprMainPageDTO.getAmount());
                                    break;
                                }
                            }
                            if (tmpList[0].equals(tmpDTO.getJobCode())
                                    && !tmpDTO.getMessage().contains("preparatory work")) {//间接工作不计准备工作
                                tmp.setHours(tmpDTO.getTotal());
                                pprList.add(tmp);
                                flag = false;
                            }
                        }
                        if (flag) pprList.add(noHoursTmp);
                    }
                    //合并所有的hours
                    for (int n = 0; n < pprList.size(); n++) {
                        String jobCode = pprList.get(n).getJobCode();
                        for (int m = n + 1; m < pprList.size(); m++) {
                            String nextjobCode = pprList.get(m).getJobCode();
                            if (jobCode.equals(nextjobCode)) {
                                pprList.get(n).setTimes(pprList.get(n).getTimes() + pprList.get(m).getTimes());
                                double hour = pprList.get(n).getHours() + pprList.get(m).getHours();
                                pprList.get(n).setHours(hour);
                                pprList.remove(m);
                                m--;
                            }
                        }
                    }
                    //去掉查询项目下没记录的工作---start
                    for (PprDetailOfJobDTO tmp : pprList) {
                        if (!jobCodeList.contains(tmp.getJobCode())) {
                            jobCodeList.add(tmp.getJobCode());
                        }
                    }
                    for (PprDetailOfJobDTO tmp : headList) {
                        for (PprDetailOfJobDTO tmp1 : pprList) {
                            if (tmp.getJobCode().equals(tmp1.getJobCode())) {
                                tmp.setJobType(tmp1.getJobType());
                                tmp.setEmployeeCode(tmp1.getEmployeeCode());
                                tmp.setEmployeeName(tmp1.getEmployeeName());
                                tmp.setCategoryName(tmp1.getCategoryName());
                                tmp.setAmount(tmp1.getAmount());
                                tmp.setTimes(tmp.getTimes() + tmp1.getTimes());
                                tmp.setHours(tmp.getHours() + tmp1.getHours());
                                if (tmp.getHours() > 0) tmp.setUnitHourAmount(tmp.getAmount() / tmp.getHours());
                            }
                        }
                    }
                }
                else {
                        for (int i = 0; i < recordForPprJobList.size(); i++) {
                            Object[] tmpList = recordForPprJobList.get(i);
                            String size = null;
                            if ("小".equals(tmpList[1])) size = "Small";
                            if ("中".equals(tmpList[1])) size = "Medium";
                            if ("大".equals(tmpList[1])) size = "Large";
                            if ("超大".equals(tmpList[1])) size = "OverSize";
                            boolean flag = true;
                            PprDetailOfJobDTO noHoursTmp = new PprDetailOfJobDTO();
                            noHoursTmp.setJobCode((String) tmpList[0]);
                            noHoursTmp.setJobName((String) tmpList[8]);
                            noHoursTmp.setTimes(((BigInteger) tmpList[2]).longValue());
                            if ("直接工作".equals(tmpList[4])) {
                                noHoursTmp.setAmount(((BigDecimal) tmpList[3]).longValue());
                                noHoursTmp.setSize(size);
                            }
                            noHoursTmp.setJobType((String) tmpList[4]);
                            noHoursTmp.setEmployeeCode((String) tmpList[5]);
                            noHoursTmp.setEmployeeName((String) tmpList[6]);
                            noHoursTmp.setCategoryName((String) tmpList[7]);
                            for (PprCTimeDetailDTO tmpDTO : listForHours) {
                                PprDetailOfJobDTO tmp = new PprDetailOfJobDTO();
                                tmp.setJobCode((String) tmpList[0]);
                                tmp.setJobName((String) tmpList[8]);
                                tmp.setTimes(((BigInteger) tmpList[2]).longValue());
                                tmp.setJobType((String) tmpList[4]);
                                tmp.setEmployeeCode((String) tmpList[5]);
                                tmp.setEmployeeName((String) tmpList[6]);
                                tmp.setCategoryName((String) tmpList[7]);
                                if ("直接工作".equals(tmpDTO.getActionType())) {
                                    if (tmpList[0].equals(tmpDTO.getJobCode()) && tmpList[1].equals(tmpDTO.getSize())) {
                                        if (tmpDTO.getMessage().contains("preparatory work")) {
                                            tmp.setSize("Prep");
                                            tmp.setAmount(0);
                                            tmp.setTimes(0);
                                            tmp.setHours(tmpDTO.getTotal());
                                        } else {
                                            tmp.setSize(size);
                                            tmp.setAmount(((BigDecimal) tmpList[3]).longValue());
                                            tmp.setHours(tmpDTO.getTotal());
                                        }
                                        pprList.add(tmp);
                                        flag = false;
                                    }
                                }
                                //遍历间接工作记录
                                if ("普通间接".equals(tmpDTO.getActionType()) || "超级间接".equals(tmpDTO.getActionType())) {
                                    if (tmpList[0].equals(tmpDTO.getJobCode())) {
                                        tmp.setHours(tmpDTO.getTotal());
                                        pprList.add(tmp);
                                        flag = false;
                                    }
                                }
                            }
                            if (flag) pprList.add(noHoursTmp);
                        }
                        //合并所有的amount，hours
                        for (int n = 0; n < pprList.size(); n++) {
                            String jobCode = pprList.get(n).getJobCode();
                            String size = pprList.get(n).getSize();
                            for (int m = n + 1; m < pprList.size(); m++) {
                                String nextjobCode = pprList.get(m).getJobCode();
                                String nextSize = pprList.get(m).getSize();
                                if (jobCode.equals(nextjobCode) && (null == size || size.equals(nextSize))) {
                                    pprList.get(n).setAmount(pprList.get(n).getAmount() + pprList.get(m).getAmount());
                                    pprList.get(n).setTimes(pprList.get(n).getTimes() + pprList.get(m).getTimes());
                                    double hour = pprList.get(n).getHours() + pprList.get(m).getHours();
                                    pprList.get(n).setHours(hour);
                                    pprList.remove(m);
                                    m--;
                                }
                            }
                        }
                        //去掉查询项目下没记录的工作---start
                        for (PprDetailOfJobDTO tmp : pprList) {
                            if (!jobCodeList.contains(tmp.getJobCode())) {
                                jobCodeList.add(tmp.getJobCode());
                            }
                        }
                        //表头合并(特殊情况 特殊显示的Rebatch&Dock&Sort) 这一步Rebatch&Dock处理完 三个都是不包括Prep算出次数数量时间
                        for (PprDetailOfJobDTO tmp : headList) {
                            for (PprDetailOfJobDTO tmp1 : pprList) {
                                if (tmp.getJobCode().equals(tmp1.getJobCode()) && ((tmp1.getSize() == null || tmp1.getSize().equals(tmp.getSize())) ||

                                        (("Rebatch".equals(tmp.getSize()) || "Sort-Total".equals(tmp.getSize()) || "Dock".equals
                                                (tmp.getSize())
                                                || "Sort".equals(tmp.getSize()) || "Pallet".equals(tmp.getSize()))
                                                && !(null == tmp1.getSize() || tmp1.getSize().contains("Prep"))))) {
                                    tmp.setJobType(tmp1.getJobType());
                                    tmp.setEmployeeCode(tmp1.getEmployeeCode());
                                    tmp.setEmployeeName(tmp1.getEmployeeName());
                                    tmp.setCategoryName(tmp1.getCategoryName());
                                    if (!("Sort".equals(tmp.getSize()) || "Pallet".equals(tmp.getSize())))
                                        tmp.setAmount(tmp.getAmount() + tmp1.getAmount());
                                    if (!"Sort-Total".equals(tmp.getSize())) {
                                        tmp.setTimes(tmp.getTimes() + tmp1.getTimes());
                                        tmp.setHours(tmp.getHours() + tmp1.getHours());
                                    }
                                    if (tmp.getTimes() > 0) tmp.setUnitAmount((double) tmp.getAmount() / tmp.getTimes());
                                    if (tmp.getHours() > 0) {
                                        tmp.setUnitHourTimes(tmp.getTimes() / tmp.getHours());
                                        tmp.setUnitHourAmount(tmp.getAmount() / tmp.getHours());
                                    }
                                }
                            }
                        }
                        //特殊情况 这一步处理Pallect&Sort特殊的amount
                        List<String> tmpList = new LinkedList<>();
                        for (int i = 0; i < headList.size(); i++) {
                            if ("Pallet".equals(headList.get(i).getSize()) || "Sort".equals(headList.get(i).getSize())) {
                                String jobCode = headList.get(i).getJobCode();
                                long specialAmount;
                                if ("Pallet Receive To Stow".equals(category)) {
                                    List<Object[]> specialPallet = pprStatisticsQuery.getSpecialByPallet(warehouseId, clientId, startDate, endDate, userName);
                                    specialAmount = getSpecialAmountForDetail(specialPallet, jobCode);
                                } else {
                                    List<Object[]> specialSD = pprStatisticsQuery.getSpecialBySD(warehouseId, clientId, startDate, endDate, userName);
                                    specialAmount = getSpecialAmountForDetail(specialSD, jobCode);
                                }
                                //减少循环次数，只循环当前jobCode对应的行数
                                for (int j = i; j < (i + relationSize < headList.size() ? i + relationSize : headList.size()); j++) {
                                    if ("Pallet".equals(headList.get(j).getSize()) || "Sort".equals(headList.get(j).getSize())) {
                                        if (!tmpList.contains(jobCode)) {
                                            //if判断避免重复多个size的重复叠加amount
                                            headList.get(j).setAmount(headList.get(i).getAmount() + specialAmount);
                                            if (headList.get(j).getHours() > 0) {
                                                headList.get(j).setUnitAmount((double) headList.get(j).getAmount() / headList.get(j).getTimes());
                                                headList.get(j).setUnitHourAmount(headList.get(j).getAmount() / headList.get(j)
                                                        .getHours());
                                            }
                                            tmpList.add(jobCode);
                                        }
                                    }
                                }
                            }
                        }
//                        //添加间接工作amount
//                        for (PprDetailOfJobDTO tmp : headList) {
//                            if ("间接工作".equals(tmp.getJobType())) {
//                                long amount = 0;
//                                for (PprDetailOfJobDTO tmp1 : headList) {
//                                    if ("直接工作".equals(tmp1.getJobType()) && true) {
//                                        amount += tmp1.getAmount();
//                                    }
//                                }
//                                tmp.setAmount(amount);
//                                if (tmp.getHours() != 0) {
//                                    tmp.setUnitHourAmount(tmp.getAmount() / tmp.getHours());
//                                }
//                            }
//                        }
                    }
            }
        }
        for (int i=0;i<headList.size();i++) {
            if(!(jobCodeList.contains(headList.get(i).getJobCode())||
                    "Total".equals(headList.get(i).getJobCode()))) {
                headList.remove(i);
                i--;
            }
        }
        //--end
        if (null != headList && headList.size() > 0) {
            if ("间接工作".equals(headList.get(0).getJobType())) {
                for (PprDetailOfJobDTO tmp : headList) {
                    if ("Total".equals(tmp.getJobCode())) {
                        double hours = 0d;
                        for (PprDetailOfJobDTO tmp1 : headList) {
                            hours += tmp1.getHours();
                        }
                        tmp.setHours(hours);
                        tmp.setAmount(headList.get(0).getAmount());
                        if (tmp.getHours() > 0) tmp.setUnitHourAmount(tmp.getAmount() / hours);
                    }
                }
            } else {
                //计算总Total
                for (PprDetailOfJobDTO tmp : headList) {
                    if ("Total".equals(tmp.getJobCode())) {
                        double hours = 0d;
                        long times = 0;
                        long amount = 0;
                        for (PprDetailOfJobDTO tmp1 : headList) {
                            hours += tmp1.getHours();
                            times += tmp1.getTimes();
                            if (!("Sort".equals(tmp1.getSize()) || "Pallet".equals(tmp1.getSize())))
                                amount += tmp1.getAmount();
                        }
                        tmp.setHours(hours);
                        tmp.setTimes(times);
                        tmp.setAmount(amount);
                        if (tmp.getTimes() > 0) tmp.setUnitAmount((double) amount / times);
                        if (tmp.getHours() > 0) {
                            tmp.setUnitHourTimes(times / hours);
                            tmp.setUnitHourAmount(amount / hours);
                        }
                    }
                }
                //计算total
                for (PprDetailOfJobDTO tmp : headList) {
                    if (null != tmp.getSize() && tmp.getSize().contains("Total")) {
                        double hours = 0d;
                        long times = 0;
                        long amount = 0;
                        for (PprDetailOfJobDTO tmp1 : headList) {
                            if (tmp1.getJobCode().equals(tmp.getJobCode())) {
                                hours += tmp1.getHours();
                                times += tmp1.getTimes();
                                if (!("Sort".equals(tmp1.getSize()) || "Dock".equals(tmp1.getSize()) || "Pallet".equals(tmp1.getSize())))


                                    amount += tmp1.getAmount();
                            }
                        }
                        tmp.setHours(hours);
                        tmp.setTimes(times);
                        tmp.setAmount(amount);
                        if (tmp.getTimes() > 0) tmp.setUnitAmount((double) amount / times);
                        if (tmp.getHours() > 0) {
                            tmp.setUnitHourTimes(times / hours);
                            tmp.setUnitHourAmount(amount / hours);
                        }
                    }
                }
            }
        }
        allList.add(headList);
        if (lastFlag) {
            getRecordsForPprDetails(warehouseId, clientId, category, startDate, endDate,users,allList);
            List<PprDetailOfJobDTO> totalList = (LinkedList<PprDetailOfJobDTO>)allList.get(0);
            for (PprDetailOfJobDTO tmp : totalList) {
                double hour = DateTimeUtil.getNewDecimal(tmp.getHours(),3);
                double unitAmount = DateTimeUtil.getNewDecimal(tmp.getUnitAmount(),3);
                double unitHourTimes = DateTimeUtil.getNewDecimal(tmp.getUnitHourTimes(),3);
                double unitHourAmount = DateTimeUtil.getNewDecimal(tmp.getUnitHourAmount(),3);
                tmp.setHours(hour);
                tmp.setUnitAmount(unitAmount);
                tmp.setUnitHourTimes(unitHourTimes);
                tmp.setUnitHourAmount(unitHourAmount);
            }
            for (int i=1; i<allList.size(); i++) {
                List<PprDetailOfEmployeeDTO> detailList = (LinkedList<PprDetailOfEmployeeDTO>)allList.get(i);
                for (PprDetailOfEmployeeDTO tmp : detailList) {
                    double sHours = DateTimeUtil.getNewDecimal(tmp.getsHours(),3);
                    double mHours = DateTimeUtil.getNewDecimal(tmp.getmHours(),3);
                    double lHours = DateTimeUtil.getNewDecimal(tmp.getlHours(),3);
                    double oHours = DateTimeUtil.getNewDecimal(tmp.getoHours(),3);
                    double orderHours = DateTimeUtil.getNewDecimal(tmp.getOrderHours(),3);
                    double tHours = DateTimeUtil.getNewDecimal(tmp.gettHours(),3);
                    double sUnitHourAmount = DateTimeUtil.getNewDecimal(tmp.getsUnitHourAmount(),3);
                    double mUnitHourAmount = DateTimeUtil.getNewDecimal(tmp.getmUnitHourAmount(),3);
                    double lUnitHourAmount = DateTimeUtil.getNewDecimal(tmp.getlUnitHourAmount(),3);
                    double oUnitHourAmount = DateTimeUtil.getNewDecimal(tmp.getoUnitHourAmount(),3);
                    double tUnitHourAmount = DateTimeUtil.getNewDecimal(tmp.gettUnitHourAmount(),3);
                    double unitAmount = DateTimeUtil.getNewDecimal(tmp.getUnitAmount(),3);
                    double unitHourTimes = DateTimeUtil.getNewDecimal(tmp.getUnitHourTimes(),3);
                    tmp.setsHours(sHours);
                    tmp.setmHours(mHours);
                    tmp.setlHours(lHours);
                    tmp.setoHours(oHours);
                    tmp.setOrderHours(orderHours);
                    tmp.settHours(tHours);
                    tmp.setsUnitHourAmount(sUnitHourAmount);
                    tmp.setmUnitHourAmount(mUnitHourAmount);
                    tmp.setlUnitHourAmount(lUnitHourAmount);
                    tmp.setoUnitHourAmount(oUnitHourAmount);
                    tmp.settUnitHourAmount(tUnitHourAmount);
                    tmp.setUnitAmount(unitAmount);
                    tmp.setUnitHourTimes(unitHourTimes);
                }
            }
            return allList;
        }
        else return null;
    }

    public void getRecordsForPprDetails(String warehouseId, String clientId, String category, String startDate,
                                                String endDate,Set<User> users,List allList) throws ParseException {
        List<Object[]> recordForList = new LinkedList<>();
        List<PprCTimeDetailDTO> hoursForList = new LinkedList<>();
        //key表示jobCode，value代表每个job下面的员工list
        Map<Object,List> tmpForMap =new TreeMap<Object, List>();
        //有该项目工作记录的员工
        List employeeList = new LinkedList();
        for (User user : users) {
            String userName = user.getUsername();
            List<Object[]> recordForPprJobList = pprStatisticsQuery.getRecordByPprJob(warehouseId, clientId, category, startDate, endDate, userName,false);
            List<Object[]> tmpCompareList = pprStatisticsQuery.getRecordByPprJob(warehouseId, clientId, category, startDate, endDate, userName,true);
            List<PprCTimeDetailDTO> listForHours = getCtimedetailByJob(warehouseId, clientId, startDate, endDate, userName);
            for (int i =0;i<listForHours.size();i++) {
                if (!listForHours.get(i).getCategoryName().equals(category)) {
                    listForHours.remove(i);
                    i--;
                }
            }
            Long count = 0L;
            for (PprCTimeDetailDTO tmp : listForHours) {
                if(tmp.getMessage().contains("preparatory work")) count++;
            }
            if(tmpCompareList.size() < listForHours.size()-count) {
                Object[] job = {listForHours.get(0).getJobCode(),null, BigInteger.valueOf(1),null,"间接工作", listForHours.get(0).getEmployeeCode(),
                        listForHours.get(0).getEmployeeName(),listForHours.get(0).getCategoryName(),listForHours.get(0).getJobName()};
                recordForPprJobList.add(0,job);
            }
            recordForList.addAll(recordForPprJobList);
            hoursForList.addAll(listForHours);
        }
        for (Object[] tmp:recordForList) {
            if(!tmpForMap.containsKey(tmp[0])){
                List list = new LinkedList();
                for (Object[] tmp1:recordForList) {
                    if(tmp1[0].equals(tmp[0]) && !list.contains(tmp1[5])){
                        list.add(tmp1[5]);
                    }
                }
                tmpForMap.put(tmp[0],list);
            }
            if (!employeeList.contains(tmp[5])) {
                employeeList.add(tmp[5]);
            }
        }
        for (int i=0;i<hoursForList.size();i++) {
            if(!tmpForMap.containsKey(hoursForList.get(i).getJobCode())) {
                hoursForList.remove(i);
                i--;
            }
        }
        //按照employeeCode分类得出
        List<PprDetailOfEmployeeDTO> employeeAllList = new LinkedList<>();
        for (Object employeeCode : employeeList) {
            PprDetailOfEmployeeDTO tmp = new PprDetailOfEmployeeDTO();
            tmp.setEmployeeCode((String) employeeCode);
            for (Object[] tmpList : recordForList) {
                if (tmpList[5].equals(employeeCode)) {
                    if ("间接工作".equals(tmpList[4])) {
                        tmp.setJobCode("员工--汇总");
                    }
                    else {
                        tmp.setJobCode("员工");
                        tmp.setJobName("汇总");
                    }
                    tmp.setJobType((String) tmpList[4]);
                    tmp.setEmployeeName((String) tmpList[6]);
                    tmp.setTimes(tmp.getTimes() + ((BigInteger) tmpList[2]).longValue());
                    if ("直接工作".equals(tmpList[4])) {
                        if ("小".equals(tmpList[1])) {
                            tmp.setsAmount(tmp.getsAmount() + ((BigDecimal) tmpList[3]).longValue());
                        }
                        if ("中".equals(tmpList[1])) {
                            tmp.setmAmount(tmp.getmAmount() + ((BigDecimal) tmpList[3]).longValue());
                        }
                        if ("大".equals(tmpList[1])) {
                            tmp.setlAmount(tmp.getlAmount() + ((BigDecimal) tmpList[3]).longValue());
                        }
                        if ("超大".equals(tmpList[1])) {
                            tmp.setoAmount(tmp.getoAmount() + ((BigDecimal) tmpList[3]).longValue());
                        }
                    }
                }
            }
            //通过hoursForList计算时间
            for (PprCTimeDetailDTO tmpDTO : hoursForList) {
                if ("直接工作".equals(tmpDTO.getActionType())) {
                    if (employeeCode.equals(tmpDTO.getEmployeeCode())) {
                        tmp.setJobType("直接工作");
                        if (tmpDTO.getMessage().contains("preparatory work")) {
//                                tmp.setSize("准备");
                            tmp.settAmount(0);
                            tmp.setOrderHours(tmp.getOrderHours() + tmpDTO.getTotal());
                        } else {
                            if ("Small".equals(tmpDTO.getSize())) {
                                tmp.setsHours(tmp.getsHours() + tmpDTO.getTotal());
                            }
                            if ("Medium".equals(tmpDTO.getSize())) {
                                tmp.setmHours(tmp.getmHours() + tmpDTO.getTotal());
                            }
                            if ("Large".equals(tmpDTO.getSize())) {
                                tmp.setlHours(tmp.getlHours() + tmpDTO.getTotal());
                            }
                            if ("OverSize".equals(tmpDTO.getSize())) {
                                tmp.setoHours(tmp.getoHours() + tmpDTO.getTotal());
                            }
                        }
                    }
                }
                //遍历间接工作记录
                if ("普通间接".equals(tmpDTO.getActionType()) || "超级间接".equals(tmpDTO.getActionType())) {
                    if (employeeCode.equals(tmpDTO.getEmployeeCode())) {
                        tmp.setJobType("间接工作");
                        if (tmpDTO.getMessage().contains("preparatory work")) {
                            //v.1 不做处理，间接工作不计准备工作
                            //v.2 间接工作也要计算时间
                            tmp.setOrderHours(tmp.getOrderHours() + tmpDTO.getTotal());
                        }
                        else tmp.settHours(tmp.gettHours() + tmpDTO.getTotal());
                    }
                }
            }
            if("直接工作".equals(tmp.getJobType())) {
                double tHours = tmp.getsHours() + tmp.getmHours() + tmp.getlHours() + tmp.getoHours() + tmp.getOrderHours();
                long tAmount = tmp.getsAmount() + tmp.getmAmount() + tmp.getlAmount() + tmp.getoAmount();
                double unitAmount = (double) tAmount/tmp.getTimes();
                if (tHours>0) {
                    double unitHourTimes = tmp.getTimes() / tHours;
                    double tUnitHourAmount = tAmount / tHours;
                    tmp.setUnitHourTimes(unitHourTimes);
                    tmp.settUnitHourAmount(tUnitHourAmount);
                }
                tmp.settHours(tHours);
                tmp.settAmount(tAmount);
                tmp.setUnitAmount(unitAmount);
                if(tmp.getsHours()>0) {
                    double sUnitHourAmount = tmp.getsAmount()/tmp.getsHours();
                    tmp.setsUnitHourAmount(sUnitHourAmount);
                }
                if(tmp.getmHours()>0) {
                    double mUnitHourAmount = tmp.getmAmount()/tmp.getmHours();
                    tmp.setmUnitHourAmount(mUnitHourAmount);
                }
                if(tmp.getlHours()>0) {
                    double lUnitHourAmount = tmp.getlAmount()/tmp.getlHours();
                    tmp.setlUnitHourAmount(lUnitHourAmount);
                }
                if(tmp.getoHours()>0) {
                    double oUnitHourAmount = tmp.getoAmount()/tmp.getoHours();
                    tmp.setoUnitHourAmount(oUnitHourAmount);
                }
            }
            employeeAllList.add(tmp);
        }
        allList.add(employeeAllList);
        //依次按jobCode，employeeCode分类得出
        for (Object jobCode : tmpForMap.keySet()){
            List<PprDetailOfEmployeeDTO> pprList = new LinkedList<>();
            for (Object employeeCode : tmpForMap.get(jobCode)) {
                PprDetailOfEmployeeDTO tmp = new PprDetailOfEmployeeDTO();
                tmp.setJobCode((String) jobCode);
                tmp.setEmployeeCode((String) employeeCode);
                //通过recordForList计算amount
                for (Object[] tmpList : recordForList) {
                    if (tmpList[0].equals(jobCode) && tmpList[5].equals(employeeCode)) {
                        tmp.setJobName((String) tmpList[8]);
                        tmp.setJobType((String) tmpList[4]);
                        tmp.setEmployeeName((String) tmpList[6]);
                        tmp.setTimes(tmp.getTimes() + ((BigInteger) tmpList[2]).longValue());
                        if ("直接工作".equals(tmpList[4])) {
                            if ("小".equals(tmpList[1])) {
                                tmp.setsAmount(((BigDecimal) tmpList[3]).longValue());
                            }
                            if ("中".equals(tmpList[1])) {
                                tmp.setmAmount(((BigDecimal) tmpList[3]).longValue());
                            }
                            if ("大".equals(tmpList[1])) {
                                tmp.setlAmount(((BigDecimal) tmpList[3]).longValue());
                            }
                            if ("超大".equals(tmpList[1])) {
                                tmp.setoAmount(((BigDecimal) tmpList[3]).longValue());
                            }
                        }
                    }
                }
                //通过hoursForList计算时间
                for (PprCTimeDetailDTO tmpDTO : hoursForList) {
                    if ("直接工作".equals(tmpDTO.getActionType())) {
                        if (jobCode.equals(tmpDTO.getJobCode()) && employeeCode.equals(tmpDTO.getEmployeeCode())) {
                            tmp.setJobType("直接工作");
                            if (tmpDTO.getMessage().contains("preparatory work")) {
//                               tmp.setSize("prep");
                                tmp.settAmount(0);
                                tmp.setOrderHours(tmp.getOrderHours() + tmpDTO.getTotal());
                            } else {
                                if ("小".equals(tmpDTO.getSize())) {
                                    tmp.setsHours(tmpDTO.getTotal());
                                }
                                if ("中".equals(tmpDTO.getSize())) {
                                    tmp.setmHours(tmpDTO.getTotal());
                                }
                                if ("大".equals(tmpDTO.getSize())) {
                                    tmp.setlHours(tmpDTO.getTotal());
                                }
                                if ("超大".equals(tmpDTO.getSize())) {
                                    tmp.setoHours(tmpDTO.getTotal());
                                }
                            }
                        }
                    }
                    //遍历间接工作记录
                    if ("普通间接".equals(tmpDTO.getActionType()) || "超级间接".equals(tmpDTO.getActionType())) {
                        if (jobCode.equals(tmpDTO.getJobCode()) && employeeCode.equals(tmpDTO.getEmployeeCode())) {
                            tmp.setJobType("间接工作");
                            if (tmpDTO.getMessage().contains("preparatory work")) {
                                //v.1 不做处理，间接工作不计准备工作
                                //v.2 间接工作也要计算时间
                                tmp.setOrderHours(tmp.getOrderHours() + tmpDTO.getTotal());
                            }
                            else tmp.settHours(tmpDTO.getTotal());
                        }
                    }
                }
                //详情表 特殊处理
                //一起处理
                if("直接工作".equals(tmp.getJobType())) {
                    double tHours = tmp.getsHours() + tmp.getmHours() + tmp.getlHours() + tmp.getoHours() + tmp.getOrderHours();
                    long tAmount = tmp.getsAmount() + tmp.getmAmount() + tmp.getlAmount() + tmp.getoAmount();
                    double unitAmount = (double) tAmount/tmp.getTimes();
                    if (tHours>0) {
                        double unitHourTimes = tmp.getTimes() / tHours;
                        double tUnitHourAmount = tAmount / tHours;
                        tmp.setUnitHourTimes(unitHourTimes);
                        tmp.settUnitHourAmount(tUnitHourAmount);
                    }
                    tmp.settHours(tHours);
                    tmp.settAmount(tAmount);
                    tmp.setUnitAmount(unitAmount);
                    if(tmp.getsHours()>0) {
                        double sUnitHourAmount = tmp.getsAmount()/tmp.getsHours();
                        tmp.setsUnitHourAmount(sUnitHourAmount);
                    }
                    if(tmp.getmHours()>0) {
                        double mUnitHourAmount = tmp.getmAmount()/tmp.getmHours();
                        tmp.setmUnitHourAmount(mUnitHourAmount);
                    }
                    if(tmp.getlHours()>0) {
                        double lUnitHourAmount = tmp.getlAmount()/tmp.getlHours();
                        tmp.setlUnitHourAmount(lUnitHourAmount);
                    }
                    if(tmp.getoHours()>0) {
                        double oUnitHourAmount = tmp.getoAmount()/tmp.getoHours();
                        tmp.setoUnitHourAmount(oUnitHourAmount);
                    }
                }
                pprList.add(tmp);
            }
            //工作的Total
            PprDetailOfEmployeeDTO tmpList = new PprDetailOfEmployeeDTO();
            tmpList.setEmployeeCode("Total");
            List<PprDetailOfJobDTO> totalList = (LinkedList<PprDetailOfJobDTO>)allList.get(0);
            for (PprDetailOfJobDTO tmp : totalList) {
                if(jobCode.equals(tmp.getJobCode())) {
                    if("Small".equals(tmp.getSize())) {
                        tmpList.setsHours(tmp.getHours());
                        tmpList.setsAmount(tmp.getAmount());
                        tmpList.setsUnitHourAmount(tmp.getUnitHourAmount());
                    }
                    else {
                        if ("Medium".equals(tmp.getSize())) {
                            tmpList.setmHours(tmp.getHours());
                            tmpList.setmAmount(tmp.getAmount());
                            tmpList.setmUnitHourAmount(tmp.getUnitHourAmount());
                        }
                        else {
                            if ("Large".equals(tmp.getSize())) {
                                tmpList.setlHours(tmp.getHours());
                                tmpList.setlAmount(tmp.getAmount());
                                tmpList.setlUnitHourAmount(tmp.getUnitHourAmount());
                            }
                            else {
                                if ("OverSize".equals(tmp.getSize())) {
                                    tmpList.setoHours(tmp.getHours());
                                    tmpList.setoAmount(tmp.getAmount());
                                    tmpList.setoUnitHourAmount(tmp.getUnitHourAmount());
                                }
                                else {
                                    if ("Prep".equals(tmp.getSize())) {
                                        tmpList.setOrderHours(tmp.getHours());
                                    }
                                    else {
                                        if ("Total".equals(tmp.getSize())) {
                                            tmpList.settHours(tmp.getHours());
                                            tmpList.settAmount(tmp.getAmount());
                                            tmpList.settUnitHourAmount(tmp.getUnitHourAmount());
                                            tmpList.setTimes(tmp.getTimes());
                                            tmpList.setUnitAmount(tmp.getUnitAmount());
                                            tmpList.setUnitHourTimes(tmp.getUnitHourTimes());
                                        } else {
                                            tmpList.settHours(tmp.getHours());
                                            tmpList.settAmount(tmp.getAmount());
                                            tmpList.settUnitHourAmount(tmp.getUnitHourAmount());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            pprList.add(tmpList);
            allList.add(pprList);
        }
    }

    //表头
    private List<PprDetailOfJobDTO> getPprDetailOfJobDTOS(String category,List<JobCategoryRelation> jobCategoryRelation) {
        List<PprDetailOfJobDTO> headList = new LinkedList<>();
        List<Job> jobs = jobRepository.getJobByCategory(category);
        //正常直接工作表头
        for (Job job : jobs) {
            for (JobCategoryRelation tmp : jobCategoryRelation) {
                PprDetailOfJobDTO pprDetailOfJobDTO = new PprDetailOfJobDTO();
                String jobType;
                if("DIRECT".equals(job.getJobType())) jobType="直接工作";
                else jobType="间接工作";
                pprDetailOfJobDTO.setJobType(jobType);
                pprDetailOfJobDTO.setJobCode(job.getCode());
                pprDetailOfJobDTO.setJobName(job.getName());
                pprDetailOfJobDTO.setCategoryName(category);
                String size = job.getName();
                if(tmp.getLineItems().contains("-Small")) size = "Small";
                if(tmp.getLineItems().contains("-Medium")) size = "Medium";
                if(tmp.getLineItems().contains("-Large")) size = "Large";
                if(tmp.getLineItems().contains("-OverSize")) size = "OverSize";
                if(tmp.getLineItems().contains("-Prep")) size = "Prep";
                if(tmp.getLineItems().contains("-Pallet")) size = "Pallet";
                if(tmp.getLineItems().contains("-Total")) size = "Total";
                if("Rebatch".equals(tmp.getLineItems())) size = "Rebatch";
                if("Sort".equals(tmp.getLineItems())) size = "Sort";
                if("Dock".equals(tmp.getLineItems())) size = "Dock";
                pprDetailOfJobDTO.setSize(size);
                headList.add(pprDetailOfJobDTO);
            }
        }
        if(headList.size()>0) {
            if("Sort".equals(category) || "Dock".equals(category) || "Rebatch".equals(category)){
                PprDetailOfJobDTO sizeTotalDTO = new PprDetailOfJobDTO();
                sizeTotalDTO.setJobCode(headList.get(0).getJobCode());
                sizeTotalDTO.setJobName(headList.get(0).getJobName());
                sizeTotalDTO.setSize(category+"-Total");
                sizeTotalDTO.setJobType(headList.get(0).getJobType());
                sizeTotalDTO.setCategoryName(headList.get(0).getCategoryName());
                headList.add(sizeTotalDTO);
            }
            PprDetailOfJobDTO totalDTO = new PprDetailOfJobDTO();
            totalDTO.setJobCode("Total");
            headList.add(totalDTO);
        }
        return headList;
    }

    public List<PprCTimeDetailDTO> getCtimedetailByJob(String warehouseId, String clientId, String startDate, String endDate,
                                                  String userName) throws ParseException {
        List<PprCTimeDetailDTO> entitys = getPprCTimeDetailDTOS(warehouseId, clientId, startDate, endDate, userName);
        for (int i = 0; i < entitys.size(); i++) {
            for (int n = i+1; n < entitys.size(); n++) {
                if(entitys.get(n).getJobCode().equals(entitys.get(i).getJobCode())
                        && entitys.get(n).getMessage().equals(entitys.get(i).getMessage())) {
                    if("直接工作".equals(entitys.get(n).getActionType())) {
                        if (entitys.get(i).getSize().trim().equals(entitys.get(n).getSize().trim())) {
                            entitys.get(i).setTotal(entitys.get(i).getTotal()+entitys.get(n).getTotal());
                            entitys.remove(n);
                            n--;
                        }
                    }
                    else {
                        entitys.get(i).setTotal(entitys.get(i).getTotal()+entitys.get(n).getTotal());
                        entitys.remove(n);
                        n--;
                    }
                }
            }
        }
        return entitys;
    }

    //设置特殊行Lineitems total 的字段
    private void setSpecialTotal(List<PprMainPageDTO> pprList, List<PprMainPageDTO> modelList, int i,String itemName) {
        if(itemName.equals(modelList.get(i).getLineItems().trim())) {
            long amounts = 0;
            double hours =0d;
            for (PprMainPageDTO tmp : pprList){
                if(itemName.contains(tmp.getCategoryName()) && !tmp.getLineItems().contains("Prep")){
                    amounts += tmp.getAmount();
                    hours += tmp.getHours();
                }
            }
            modelList.get(i).setJobType("直接工作");
            modelList.get(i).setAmount(amounts);
            if (hours > 0){
                modelList.get(i).setHours(hours);
                modelList.get(i).setRates(amounts/hours);
            }
        }
    }

    //设置特殊行Lineitems的字段
    private void setSpecialNoSize(List<PprMainPageDTO> modelList, List<Object[]> specialForPprList, int i,String itemName) {
        if(itemName.equals(modelList.get(i).getLineItems().trim())) {
            modelList.get(i).setJobType("直接工作");
            modelList.get(i).setUnit(modelList.get(i+1).getUnit());
            modelList.get(i).setHours(modelList.get(i+1).getHours()-modelList.get(i-1).getHours());
            String categoryName = itemName;
            long amount;
            if(itemName.contains("-")) {
                amount = specialForPprList.size();
            }
            else {
                amount = getSpecialAmount(specialForPprList,categoryName);
            }
            modelList.get(i).setAmount(amount);
            if(modelList.get(i).getHours()>0) {
                modelList.get(i).setRates(amount/modelList.get(i).getHours());
            }
        }
    }
    //设置特殊列的amount
    private long getSpecialAmount(List<Object[]> specialList,String categoryName) {
        long amount = 0;
        for (Object[] obj : specialList) {
            if(categoryName.equals(obj[0])){
                amount ++;
            }
        }
        return  amount;
    }

    //设置特殊列的amount 工作详情页
    private long getSpecialAmountForDetail(List<Object[]> specialList,String jobCode) {
        long amount = 0;
        for (Object[] obj : specialList) {
            if(jobCode.equals(obj[5])){
                amount ++;
            }
        }
        return  amount;
    }

    //将工作记录放入完整DTO
    private List<PprMainPageDTO> getLastList(List<PprMainPageDTO> pprList) {
        List<PprMainPageDTO> modelList = jobCategoryRelationService.getJobCategoryRelations();
        for(PprMainPageDTO tmp : modelList) {
            for (PprMainPageDTO tmp1 : pprList){
                if(tmp1.getLineItems().equals(tmp.getLineItems().trim())){
                    tmp.setUnit(tmp1.getUnit());
                    tmp.setAmount(tmp1.getAmount());
                    tmp.setHours(tmp1.getHours());
                    tmp.setRates(tmp1.getRates());
                    if ("直接工作".equals(tmp1.getJobType()))
                        tmp.setJobType(tmp1.getJobType());
                    tmp.setWarehouseId(tmp1.getWarehouseId());
                    tmp.setClientId(tmp1.getClientId());
                    break;
                }
            }
        }
        return modelList;
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

    //获取工作时间内工作详情(抽取方法)
    private void getOnclockWork(List<PprCTimeDetailDTO> entitys, String clockTime, List<Object[]> jobRecord, String tmpT) throws
            ParseException {
        String jobType;
        Date dateS;
        double timeSlot;
        for (int n = 0; n < jobRecord.size(); n++) {
            PprCTimeDetailDTO cTDTO = new PprCTimeDetailDTO();
            Object[] job = jobRecord.get(n);
            String jobTime = String.valueOf(job[0]);
            jobType = String.valueOf(job[1]);
            String jobAction = String.valueOf(job[2]);
            String indirectType = String.valueOf(job[3]);
            //ppr
            String categoryName = String.valueOf(job[4]);
            String size = String.valueOf(job[5]);
            String jobCode = String.valueOf(job[6]);
            String employeeCode = String.valueOf(job[7]);
            String employeeName = String.valueOf(job[8]);
            String client = String.valueOf(job[9]);
            String wareHouse = String.valueOf(job[10]);
            String jobName =  String.valueOf(job[11]);
            Date dateE = DateTimeUtil.getDateforStr(jobTime);
            dateS = DateTimeUtil.getDateforStr(tmpT);
            timeSlot = DateTimeUtil.getHourAndMin(dateE, dateS, "min");
            cTDTO.setActivityStartTime(tmpT);
            cTDTO.setActivityEndTime(jobTime);
            cTDTO.setTotal(timeSlot);
            cTDTO.setMap(jobAction);
            cTDTO.setCategoryName(categoryName);
            cTDTO.setSize(size);
            cTDTO.setJobCode(jobCode);
            cTDTO.setEmployeeCode(employeeCode);
            cTDTO.setEmployeeName(employeeName);
            cTDTO.setWarehouseId(wareHouse);
            cTDTO.setClientId(client);
            cTDTO.setJobName(jobName);
            //正常设置actionType和message
            setActionTypeAndMessage(jobRecord, timeSlot, jobType, n, cTDTO, jobAction, indirectType,categoryName,size);
            tmpT = jobTime;
            entitys.add(cTDTO);
        }
        if (jobRecord.size() > 0) {
            PprCTimeDetailDTO cTDTO = new PprCTimeDetailDTO();
            String time = String.valueOf(jobRecord.get(jobRecord.size() - 1)[0]);
            jobType = String.valueOf(jobRecord.get(jobRecord.size() - 1)[1]);
            String action = String.valueOf(jobRecord.get(jobRecord.size() - 1)[2]);
            String indirectType = String.valueOf(jobRecord.get(jobRecord.size() - 1)[3]);
            //ppr
            String categoryName = String.valueOf(jobRecord.get(jobRecord.size() - 1)[4]);
            String size = String.valueOf(jobRecord.get(jobRecord.size() - 1)[5]);
            String jobCode = String.valueOf(jobRecord.get(jobRecord.size() - 1)[6]);
            String employeeCode = String.valueOf(jobRecord.get(jobRecord.size() - 1)[7]);
            String employeeName = String.valueOf(jobRecord.get(jobRecord.size() - 1)[8]);
            String wareHouse = String.valueOf(jobRecord.get(jobRecord.size() - 1)[9]);
            String client = String.valueOf(jobRecord.get(jobRecord.size() - 1)[10]);
            String jobName = String.valueOf(jobRecord.get(jobRecord.size() - 1)[11]);
            cTDTO.setActivityStartTime(time);
            cTDTO.setActivityEndTime(clockTime);
            Date dateE = DateTimeUtil.getDateforStr(clockTime);
            Date dateF = DateTimeUtil.getDateforStr(time);
            timeSlot = DateTimeUtil.getHourAndMin(dateE, dateF, "min");
            cTDTO.setTotal(timeSlot);
            cTDTO.setMap(action);
            cTDTO.setCategoryName(categoryName);
            cTDTO.setSize(size);
            cTDTO.setJobCode(jobCode);
            cTDTO.setEmployeeCode(employeeCode);
            cTDTO.setEmployeeName(employeeName);
            cTDTO.setWarehouseId(wareHouse);
            cTDTO.setClientId(client);
            cTDTO.setJobName(jobName);
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
    private void setActionTypeAndMessage(List<Object[]> jobRecord, double timeSlot, String jobType, int n, PprCTimeDetailDTO
            cTDTO, String jobAction, String indirectType, String categoryName, String size) {
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
                else {
                    cTDTO.setMessage(jobAction);
                }
            }
        }
    }

    //查询并计算表jobrecord里数据供ppr调用
    private List<PprCTimeDetailDTO> getPprCTimeDetailDTOS(String warehouseId, String clientId, String startDate, String
            endDate, String userName) throws ParseException {
        getThreshold();
        List<PprCTimeDetailDTO> entitys = new LinkedList<PprCTimeDetailDTO>();
        //员工打卡数据
        List<Object[]> statistics = pprStatisticsQuery.getStatisticsTotal(userName, startDate, endDate, warehouseId, clientId);
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
                        lastEndTime = startDate.replace("T", " ");
                    }
                    PprCTimeDetailDTO pprCTimeDetailDTO = new PprCTimeDetailDTO();
                    pprCTimeDetailDTO.setMessage("Offclock");
                    pprCTimeDetailDTO.setActivityStartTime(lastEndTime);
                    pprCTimeDetailDTO.setActivityEndTime(clockTime);
                    Date date2 = DateTimeUtil.getDateforStr(clockTime);
                    Date date1 = DateTimeUtil.getDateforStr(lastEndTime);
                    double timeSlot = DateTimeUtil.getHourAndMin(date2, date1, "min");
                    pprCTimeDetailDTO.setTotal(timeSlot);
                    pprCTimeDetailDTO.setMap("Offclock");
                    entitys.add(pprCTimeDetailDTO);
                } else {
                    flag = false;
                    lastEndTime = clockTime;
                    if (i == 0) {
                        if (startDate != null) tmpTime = startDate.replace("T", " ");
                        else tmpTime = clockTime.substring(0, clockTime.indexOf(" ")) + " 00:00:00";
                    }
                    //直接间接工作时间
                    List<Object[]> jobRecord = pprStatisticsQuery.getRecords(warehouseId, clientId, userName, tmpTime, clockTime);
                    //==========计算第一步============
                    Date dateS;
                    double timeSlot;
                    String tmpT = tmpTime;
                    String jobType;
                    PprCTimeDetailDTO pprCTimeDetailDTO = new PprCTimeDetailDTO();
                    pprCTimeDetailDTO.setMessage("Onclock");
                    pprCTimeDetailDTO.setActivityStartTime(tmpTime);
                    pprCTimeDetailDTO.setActivityEndTime(clockTime);
                    Date date2 = DateTimeUtil.getDateforStr(clockTime);
                    Date date1 = DateTimeUtil.getDateforStr(tmpTime);
                    timeSlot = DateTimeUtil.getHourAndMin(date2, date1, "min");
                    pprCTimeDetailDTO.setTotal(timeSlot);
                    pprCTimeDetailDTO.setMap("Onclock");
                    entitys.add(pprCTimeDetailDTO);
                    if (jobRecord.size() == 0) {
                        PprCTimeDetailDTO cTDTO = new PprCTimeDetailDTO();
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
                Date date2 = DateTimeUtil.getDateforStr(endDate.replace("T", " "));
                String newStr = endDate.replace("T", " ");
                //对比当前时间
                Date dateNow = new Date();
                if (dateNow.before(date2)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    newStr = dateFormat.format(dateNow);
                }
                //=========
                List<Object[]> jobRecord = pprStatisticsQuery.getRecords(warehouseId, clientId, userName, tmpTime, newStr);
                //==========计算第二步==========
                String tmpT = tmpTime;
                double timeSlot = 0d;
                PprCTimeDetailDTO pprCTimeDetailDTO = new PprCTimeDetailDTO();
                pprCTimeDetailDTO.setMessage("Onclock");
                pprCTimeDetailDTO.setActivityStartTime(tmpTime);
                pprCTimeDetailDTO.setActivityEndTime(newStr);
                Date dateTwo = DateTimeUtil.getDateforStr(newStr);
                Date date1 = DateTimeUtil.getDateforStr(tmpTime);
                timeSlot = DateTimeUtil.getHourAndMin(dateTwo, date1, "min");
                pprCTimeDetailDTO.setTotal(timeSlot);
                pprCTimeDetailDTO.setMap("Onclock");
                entitys.add(pprCTimeDetailDTO);
                if (jobRecord.size() == 0) {
                    PprCTimeDetailDTO cTDTO = new PprCTimeDetailDTO();
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
                Date date2 = DateTimeUtil.getDateforStr(endDate.replace("T", " "));
                String newStr = endDate.replace("T", " ");
                //对比当前时间
                Date dateNow = new Date();
                if (dateNow.before(date2)) {
                    date2 = dateNow;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    newStr = dateFormat.format(dateNow);
                }
                Date date1 = DateTimeUtil.getDateforStr(lastEndTime);
                double timeSlot = DateTimeUtil.getHourAndMin(date2, date1, "min");
                PprCTimeDetailDTO pprCTimeDetailDTO = new PprCTimeDetailDTO();
                pprCTimeDetailDTO.setMessage("Offclock");
                pprCTimeDetailDTO.setActivityStartTime(lastEndTime);
                pprCTimeDetailDTO.setActivityEndTime(newStr);
                pprCTimeDetailDTO.setTotal(timeSlot);
                pprCTimeDetailDTO.setMap("Offclock");
                entitys.add(pprCTimeDetailDTO);
            }
        }
        else {
            String smallDate = "2017-08-01 00:00:00";
            String bigDate= startDate;
            List<Object[]> maxClockTime = pprStatisticsQuery.getMaxClockTime(userName, smallDate, bigDate, warehouseId,
                    clientId);
            if (maxClockTime != null && maxClockTime.size() > 0) {
                //选择日期段之前最近一次打卡类型
                String lastClockType = String.valueOf(maxClockTime.get(0)[3]);
                if (lastClockType.equals("上班")) {
                    //选择日期段之前最近一次打卡时间
                    String lastClockTime = String.valueOf(maxClockTime.get(0)[0]);
                    String smallTime = startDate.replace("T", " ");
                    String bigTime = endDate.replace("T", " ");
                    Date date2 = DateTimeUtil.getDateforStr(bigTime);
                    //对比当前时间
                    Date dateNow = new Date();
                    if (dateNow.before(date2)) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        bigTime = dateFormat.format(dateNow);
                    }
                    //直接间接工作时间
                    List<Object[]> jobRecord = pprStatisticsQuery.getRecords(warehouseId, clientId, userName, smallTime, bigTime);
                    String tmpT = smallTime;
                    double timeSlot = 0d;
                    PprCTimeDetailDTO pprCTimeDetailDTO = new PprCTimeDetailDTO();
                    pprCTimeDetailDTO.setMessage("Onclock");
                    pprCTimeDetailDTO.setActivityStartTime(smallTime);
                    pprCTimeDetailDTO.setActivityEndTime(bigTime);
                    Date dateTwo = DateTimeUtil.getDateforStr(bigTime);
                    Date dateOne = DateTimeUtil.getDateforStr(smallTime);
                    timeSlot = DateTimeUtil.getHourAndMin(dateTwo, dateOne, "min");
                    pprCTimeDetailDTO.setTotal(timeSlot);
                    pprCTimeDetailDTO.setMap("Onclock");
                    entitys.add(pprCTimeDetailDTO);

                    List<Object[]> lastJobRecord = pprStatisticsQuery.getMaxJobAction(warehouseId, clientId, userName, lastClockTime,
                            smallTime);
                    if (lastJobRecord != null && lastJobRecord.size() > 0) {
                        String lastJobType = String.valueOf(lastJobRecord.get(0)[1]);
                        if (lastJobType.equals("INDIRECT")) {
                            String lastJobAction = String.valueOf(lastJobRecord.get(0)[2]);
                            String lastIndirectType = String.valueOf(lastJobRecord.get(0)[3]);
                            //ppr
                            String lastCategoryName = String.valueOf(lastJobRecord.get(0)[4]);
                            String lastSize = String.valueOf(lastJobRecord.get(0)[5]);
                            String lastJobCode = String.valueOf(lastJobRecord.get(0)[6]);
                            String lastEmployeeCode = String.valueOf(lastJobRecord.get(0)[7]);
                            String lastEmployeeName = String.valueOf(lastJobRecord.get(0)[8]);
                            String lastClient = String.valueOf(lastJobRecord.get(0)[9]);
                            String lastWareHouse = String.valueOf(lastJobRecord.get(0)[10]);
                            String lastJobName = String.valueOf(lastJobRecord.get(0)[11]);
//                            if (jobRecord.size() == 0) {
//                                PprCTimeDetailDTO cTDTO = new PprCTimeDetailDTO();
//                                cTDTO.setMessage(lastJobAction);
//                                cTDTO.setActivityStartTime(smallTime);
//                                cTDTO.setActivityEndTime(bigTime);
//                                cTDTO.setTotal(timeSlot);
//                                cTDTO.setMap(lastJobAction);
//                                cTDTO.setActionType(lastIndirectType);
//                                cTDTO.setCategoryName(lastCategoryName);
//                                cTDTO.setSize(lastSize);
//                                cTDTO.setJobCode(lastJobCode);
//                                cTDTO.setEmployeeCode(lastEmployeeCode);
//                                cTDTO.setEmployeeName(lastEmployeeName);
//                                cTDTO.setClientId(lastClient);
//                                cTDTO.setWarehouseId(lastWareHouse);
//                                entitys.add(cTDTO);
//                            }
                            Object[] job = {smallTime,lastJobType,lastJobAction,lastIndirectType,lastCategoryName,lastSize,
                                    lastJobCode,lastEmployeeCode,lastEmployeeName,lastClient,lastWareHouse,lastJobName};
                            jobRecord.add(0,job);
                            getOnclockWork(entitys, bigTime, jobRecord, tmpT);
                        }
                        else{
                            if (jobRecord.size() == 0) {
                                PprCTimeDetailDTO cTDTO = new PprCTimeDetailDTO();
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
                            PprCTimeDetailDTO cTDTO = new PprCTimeDetailDTO();
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
         * 如果当前项message的后一项不是正常工作，且前一项不是Onclock
         * 那么当前项位置后添加 当前正常工作项 时间为十秒 且 后一项时间减十秒
         * 或者是一秒操作完成的也加十秒
         */
        for (int n = 1; n < entitys.size() - 1; n++) {
            PprCTimeDetailDTO list3 = entitys.get(n-1);
            PprCTimeDetailDTO list1 = entitys.get(n);
            PprCTimeDetailDTO list2 = entitys.get(n + 1);
            if ((!("Onclock".equals(list3.getMessage())) && !(list2.getMessage().equals(list2.getMap())))  || list1.getTotal() == 0) {
                PprCTimeDetailDTO addList = new PprCTimeDetailDTO();
                addList.setMessage(list1.getMap());
                //ppr
                addList.setCategoryName(list1.getCategoryName());
                addList.setSize(list1.getSize());
                addList.setJobCode(list1.getJobCode());
                addList.setEmployeeCode(list1.getEmployeeCode());
                addList.setEmployeeName(list1.getEmployeeName());
                addList.setWarehouseId(list1.getWarehouseId());
                addList.setClientId(list1.getClientId());
                addList.setJobName(list1.getJobName());
                addList.setActivityStartTime(list1.getActivityEndTime());
                //添加项时间为十秒,后一项开始时间减十秒
                String newDateTime = getNewDateTime(list1.getActivityEndTime());
                addList.setActivityEndTime(newDateTime);
                addList.setTotal(1.0/6);
                list2.setActivityStartTime(newDateTime);
                list2.setTotal(list2.getTotal() - 1.0/6);
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
            PprCTimeDetailDTO list1 = entitys.get(n);
            PprCTimeDetailDTO list2 = entitys.get(n - 1);
            PprCTimeDetailDTO list3 = entitys.get(n + 1);
            if (!((list1.getMessage().equals("Offclock")) || (list1.getMessage().equals("Onclock")) || (list3.getMessage()
                    .equals("Offclock")) || (list2.getMessage().equals("Onclock")))
                    && (list1.getMessage().equals(list1.getMap()))) {
                list1.setMessage(list2.getMap());
                list1.setMap(list2.getMap());
                list1.setActionType(list2.getActionType());
                list1.setCategoryName(list2.getCategoryName());
                list1.setSize(list2.getSize());
                list1.setJobCode(list2.getJobCode());
                list1.setEmployeeCode(list2.getEmployeeCode());
                list1.setEmployeeName(list2.getEmployeeName());
                list1.setWarehouseId(list2.getWarehouseId());
                list1.setClientId(list2.getClientId());
                list1.setJobName(list2.getJobName());
            }
        }

        //前后同一项record时合并
        for (int n = entitys.size() - 1; n > 0; n--) {
            PprCTimeDetailDTO list1 = entitys.get(n);
            PprCTimeDetailDTO list2 = entitys.get(n - 1);
            if ((list1.getMessage().equals(list2.getMessage()))//工作详情(Onclock,Offclock,No work,prepareatory work,JOB_ACTION)
                    && (list1.getMap().equals(list2.getMap()))//(Onclock,Offclock,打卡期间全部No work)以及三种对应的JOB_ACTION
                    && (list1.getMessage().equals(list1.getMap()))) {
                if("直接工作".equals(list1.getActionType())||"直接工作".equals(list2.getActionType())) {
                    if (list1.getSize().trim().equals(list2.getSize().trim())) {
                        list2.setActivityEndTime(list1.getActivityEndTime());
                        list2.setTotal(list1.getTotal() + list2.getTotal());
                        entitys.remove(n);
                    }
                }
                else {
                    list2.setActivityEndTime(list1.getActivityEndTime());
                    list2.setTotal(list1.getTotal() + list2.getTotal());
                    entitys.remove(n);
                }
            }
        }
        for (int i = 0; i < entitys.size(); i++) {
            String msg = entitys.get(i).getMessage();
            entitys.get(i).setTotal(entitys.get(i).getTotal()/60);
            if ("No work".equals(msg) || "Offclock".equals(msg) || "Onclock".equals(msg)) {
                entitys.remove(i);
                i--;
            }
        }
        return entitys;
    }
}
