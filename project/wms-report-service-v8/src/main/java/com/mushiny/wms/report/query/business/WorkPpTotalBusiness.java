package com.mushiny.wms.report.query.business;

import com.mushiny.wms.report.query.dto.WorkFlowCutlineDTO;
import com.mushiny.wms.report.query.dto.WorkFlowDTO;
import com.mushiny.wms.report.query.dto.WorkPpDTO;
import com.mushiny.wms.report.repository.DeliveryTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class WorkPpTotalBusiness implements Serializable {

    private final DeliveryTimeRepository workPpTotalRepository;

    @Autowired
    public WorkPpTotalBusiness(DeliveryTimeRepository workPpTotalRepository) {
        this.workPpTotalRepository = workPpTotalRepository;
    }

    //获取单个PpName所有时间的数据
    public WorkPpDTO getWorkPpNames(String ppName, List<LocalDateTime> times) {

        WorkPpDTO workPp = new WorkPpDTO(ppName);

        for (LocalDateTime exsdTime : times) {

            WorkFlowCutlineDTO workFlowCutline = getWorkFlowCutline(ppName, exsdTime);

            if (workFlowCutline.getTotal().compareTo(BigDecimal.ZERO) > 0) {

                workPp.getWorkflows().add(new WorkFlowDTO(exsdTime, workFlowCutline));
            }
        }
        return workPp;
    }


    /* 获取 WorkFlow 属性数据 */
    public WorkFlowCutlineDTO getWorkFlowCutline(String ppName, LocalDateTime exsdTime) {
        WorkFlowCutlineDTO workFlowCutlineDTO = new WorkFlowCutlineDTO();
        BigDecimal sum = BigDecimal.ZERO;

        sum = workPpTotalRepository.getByReadyToPick(ppName, exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setReadyToPick(sum);
        }

        sum = workPpTotalRepository.getByPickingNotYetPicked(ppName, exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setPickingNotYetPicked(sum);
        }

        sum = workPpTotalRepository.getByPickingPicked(ppName, exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setPickingPicked(sum);
        }

        sum = workPpTotalRepository.getByRebatched(ppName, exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setRebatched(sum);
        }

        sum = workPpTotalRepository.getByRebined(ppName, exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setRebined(sum);
        }

        sum = workPpTotalRepository.getByRebinBuffer(ppName, exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setRebinBuffer(sum);
        }

        sum = workPpTotalRepository.getByScanVerify(ppName, exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setScanVerify(sum);
        }

        sum = workPpTotalRepository.getByPacked(ppName, exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setPacked(sum);
        }

        sum = workPpTotalRepository.getByProblem(ppName, exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setProblem(sum);
        }

        sum = workPpTotalRepository.getBySorted(ppName, exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setSorted(sum);
        }

        /*初始化运算*/
        workFlowCutlineDTO.setNeedToReplenish();
        workFlowCutlineDTO.setTotalPicking();
        workFlowCutlineDTO.setTotalWorkInProcess();
        workFlowCutlineDTO.setTotalShipping();
        workFlowCutlineDTO.setTotal();
        return workFlowCutlineDTO;
    }


}
