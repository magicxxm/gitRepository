package com.mushiny.wms.tot.jobrecord.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.tot.general.domain.User;
import com.mushiny.wms.tot.general.domain.Warehouse;
import com.mushiny.wms.tot.general.repository.WarehouseRepository;
import com.mushiny.wms.tot.job.domain.Job;
import com.mushiny.wms.tot.job.repository.JobRepository;
import com.mushiny.wms.tot.jobrecord.crud.dto.JobrecordDTO;
import com.mushiny.wms.tot.jobrecord.crud.mapper.JobrecordMapper;
import com.mushiny.wms.tot.jobrecord.domain.Jobrecord;
import com.mushiny.wms.tot.jobrecord.query.hql.JobrecordQurey;
import com.mushiny.wms.tot.jobrecord.repository.JobrecordRepository;
import com.mushiny.wms.tot.jobrecord.service.JobrecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class JobrecordServiceImpl implements JobrecordService {
    private final JobrecordRepository jobrecordRepository;
    private final JobRepository jobRepository;
    private final JobrecordMapper jobrecordMapper;
    private final ApplicationContext applicationContext;
    private final WarehouseRepository warehouseRepository;
    private final JobrecordQurey jobrecordQurey;

    @Autowired
    public JobrecordServiceImpl(JobrecordRepository jobrecordRepository,
                                JobRepository jobRepository,
                                JobrecordMapper jobrecordMapper,
                                ApplicationContext applicationContext,
                                WarehouseRepository warehouseRepository,
                                JobrecordQurey jobrecordQurey) {
        this.jobrecordRepository = jobrecordRepository;
        this.jobRepository = jobRepository;
        this.jobrecordMapper = jobrecordMapper;
        this.applicationContext = applicationContext;
        this.warehouseRepository = warehouseRepository;
        this.jobrecordQurey = jobrecordQurey;
    }

    @Override
    public JobrecordDTO create(JobrecordDTO dto) {
        Jobrecord jobrecord = jobrecordMapper.toEntity(dto);
        return jobrecordMapper.toDTO(jobrecordRepository.save(jobrecord));
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public JobrecordDTO update(JobrecordDTO dto) {
        return null;
    }

    @Override
    public JobrecordDTO retrieve(String id) {
        return null;
    }

    @Override
    public List<JobrecordDTO> getBySearchTerm(String searchTerm, Sort sort) {
        return null;
    }

    @Override
    public Page<JobrecordDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        Page<Jobrecord> entities = jobrecordRepository.getBySearchTerm(searchTerm, pageable);
        return jobrecordMapper.toDTOPage(pageable, entities);
    }

    @Override
    public JobrecordDTO getJobrecord(String employeeCode, String jobCode) {
//        User user = userRepository.findByUsername(employeeCode);
//        if (user == null) {
//            throw new ApiException("员工不存在");
//        }
        String currentWarehouseId = applicationContext.getCurrentWarehouse();
        Warehouse warehouse = warehouseRepository.retrieve(currentWarehouseId);
        Set<User> users = warehouse.getUsers();
        User user=null;
        for (User tmp: users) {
            if (employeeCode.equals(tmp.getUsername())) {
                user = tmp;
                break;
            }
        }
        if(null==user) throw new ApiException("TOT_EMPLOYEECODE_ERROR");

        Job job = jobRepository.getJob(jobCode);
        if(job == null){
            throw new ApiException("TOT_INDIRECTJOB_ERROR");
        }
        else{
            if(job.getJobType().equals("DIRECT")) throw new ApiException("TOT_INDIRECTJOB_ERROR");
        }
        Jobrecord jobrecord = new Jobrecord();
        jobrecord.setRecordTime(DateTimeUtil.getDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        jobrecord.setEmployeeCode(user.getUsername());
        jobrecord.setEmployeeName(user.getName());
        jobrecord.setJobAction(job.getCategory().getName()+"-"+job.getName());
        jobrecord.setCategoryName(job.getCategory().getName());
        jobrecord.setIndirectType(job.getIndirectType());
        jobrecord.setJobCode(job.getCode());
        jobrecord.setJobName(job.getName());
        jobrecord.setJobType(job.getJobType());
        jobrecord.setDescription(job.getDescription());
        jobrecord.setWarehouseId(currentWarehouseId);
        //待定
        jobrecord.setClientId(user.getClient().getId());
        JobrecordDTO entity = jobrecordMapper.toDTO(jobrecordRepository.save(jobrecord));
        return entity;
    }

    @Override
    public Page<JobrecordDTO> getDetail(Pageable pageable, String employeeCode, String warehouseId, String clientId,String startTime,String endTime) {
        List<Object[]> entities = jobrecordQurey.getDetailByPage(
                pageable.getPageNumber(),pageable.getPageSize(),employeeCode,warehouseId,startTime,endTime);
        List<Jobrecord> jobrecordList = new LinkedList<>();
        if (null!=entities && !entities.isEmpty()) {
            for(Object[] obj : entities){
                Jobrecord jobrecord = new Jobrecord();
                jobrecord.setRecordTime(String.valueOf(obj[0]).substring(0,19));
                jobrecord.setJobAction((String)obj[1]);
                jobrecord.setSkuNo((String)obj[2]);
                jobrecord.setNewBarcode((String)obj[3]);
                jobrecord.setUnitType((String)obj[4]);
                jobrecord.setSize((String)obj[5]);
                jobrecord.setQuantity(Integer.valueOf(String.valueOf(obj[6])));
                jobrecord.setFromStoragelocation((String)obj[7]);
                jobrecord.setToStoragelocation((String)obj[8]);
                jobrecord.setClientId((String)obj[9]);
                jobrecord.setTool((String)obj[10]);
                jobrecord.setShipmentNo((String)obj[11]);
                jobrecord.setEntityLock(Integer.valueOf(String.valueOf(obj[12])));
                jobrecordList.add(jobrecord);
            }
        }
//        List<JobrecordDTO> dto = jobrecordMapper.toDTOList(jobrecordList);
        Page jobrecordPage = new PageImpl<>(jobrecordList, pageable,
                Long.parseLong(String.valueOf(jobrecordQurey.getCountByPage(employeeCode,warehouseId,startTime,endTime)) ));
        return jobrecordMapper.toDTOPage(pageable,jobrecordPage);
    }

    @Override
    public List<JobrecordDTO> getTotal(String employeeCode, String warehouseId, String clientId,String startTime,String endTime) {
        List<Object[]> list= jobrecordRepository.getTotal(
                employeeCode,warehouseId,startTime,endTime);

        List<Jobrecord> jobrecordList = new ArrayList<>();
        for (Object row : list) {
            Jobrecord jobrecord = new Jobrecord();
            Object[] arr = (Object[]) row;

            jobrecord.setTool((String) arr[0]);
            jobrecord.setJobAction((String) arr[1]);
            if (arr[2] != null) {
                jobrecord.setUnitType((String) arr[2]);
            }
            if(arr[3] !=null){
                jobrecord.setSize((String)arr[3]);
            }
            if (arr[4] != null) {
                long sum = (Long) arr[4];
                jobrecord.setQuantity((int) sum);
            }
            if (arr[5] != null) {
                int code = (int) arr[5];
                jobrecord.setEntityLock(code);
            }
            jobrecordList.add(jobrecord);
        }
        List<JobrecordDTO> dto = jobrecordMapper.toDTOList(jobrecordList);
        return dto;
    }

    @Override
    public JobrecordDTO addJobrecord(String employeeCode,String dateTime, String jobCode) {
        String currentWarehouseId = applicationContext.getCurrentWarehouse();
        Warehouse warehouse = warehouseRepository.retrieve(currentWarehouseId);
        Job job = jobRepository.getJob(jobCode);
        if(job == null){
            throw new ApiException("TOT_DROPDOWNLIST_ERROR");
        }
        Set<User> users = warehouse.getUsers();
        User user=null;
        for (User tmp: users) {
            if (employeeCode.equals(tmp.getUsername())) {
                user = tmp;
                break;
            }
        }

        Jobrecord jobrecord = new Jobrecord();
        try {
            jobrecord.setRecordTime(getNewDateTime(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        jobrecord.setEmployeeCode(employeeCode);
        jobrecord.setEmployeeName(user.getName());
        jobrecord.setJobAction(job.getCategory().getName()+"-"+job.getName());
        jobrecord.setCategoryName(job.getCategory().getName());
        jobrecord.setIndirectType(job.getIndirectType());
        jobrecord.setJobCode(jobCode);
        jobrecord.setJobName(job.getName());
        jobrecord.setJobType(job.getJobType());
        jobrecord.setDescription(job.getDescription());
        jobrecord.setWarehouseId(currentWarehouseId);
        //待定
        jobrecord.setClientId(user.getClient().getId());
        JobrecordDTO entity = jobrecordMapper.toDTO(jobrecordRepository.save(jobrecord));
        return entity;
    }

    //时间加一秒
    private static String getNewDateTime(String dateTime) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        Date date = DateTimeUtil.getDateforStr(dateTime);
        // 对 calendar 设置为 date 所定的日期
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(calendar.getTime());
    }
}
