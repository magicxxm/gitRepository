package com.mushiny.wms.report.query.business;

import com.mushiny.wms.report.query.dto.pp_work.*;
import com.mushiny.wms.report.query.hql.PpWorkTotalQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@Component
public class PpWorkBusiness implements Serializable {
    private final PpWorkTotalQuery ppWorkTotalQuery;

    @Autowired
    public PpWorkBusiness(PpWorkTotalQuery ppWorkTotalQuery) {
        this.ppWorkTotalQuery = ppWorkTotalQuery;
    }

    //过滤 日期为NULL的PpName列   日期排序
    public PpWorkDTO file(PpWorkDTO ppWorkDTO) {

        //日期排序
        TreeSet ts = new TreeSet(ppWorkDTO.getTotal().getTimes());
        ppWorkDTO.getTotal().setTimes(ts);

        ts = new TreeSet(ppWorkDTO.getReadyToPick().getTimes());
        ppWorkDTO.getReadyToPick().setTimes(ts);

        ts = new TreeSet(ppWorkDTO.getPickingNotYetPicked().getTimes());
        ppWorkDTO.getPickingNotYetPicked().setTimes(ts);

        ts = new TreeSet(ppWorkDTO.getPickingPicked().getTimes());
        ppWorkDTO.getPickingPicked().setTimes(ts);

        ts = new TreeSet(ppWorkDTO.getRebatched().getTimes());
        ppWorkDTO.getRebatched().setTimes(ts);

        ts = new TreeSet(ppWorkDTO.getRebinBuffer().getTimes());
        ppWorkDTO.getRebinBuffer().setTimes(ts);

        ts = new TreeSet(ppWorkDTO.getRebined().getTimes());
        ppWorkDTO.getRebined().setTimes(ts);

        ts = new TreeSet(ppWorkDTO.getScanVerify().getTimes());
        ppWorkDTO.getScanVerify().setTimes(ts);

        ts = new TreeSet(ppWorkDTO.getPacked().getTimes());
        ppWorkDTO.getPacked().setTimes(ts);

        ts = new TreeSet(ppWorkDTO.getProblem().getTimes());
        ppWorkDTO.getProblem().setTimes(ts);

        ts = new TreeSet(ppWorkDTO.getSorted().getTimes());
        ppWorkDTO.getSorted().setTimes(ts);

        return ppWorkDTO;
    }


    public PpWorkDTO getPpWorkDTO(List<LocalDateTime> exsdTimes) {

        PpWorkDTO ppWorkDTO = new PpWorkDTO();

        PpNameData total = new PpNameData();
        total.setPpNameData(addPpNames());


        PpNameData readyToPick = new PpNameData();
        readyToPick.setPpNameData(addPpNames());

        PpNameData pickingNotYetPicked = new PpNameData();
        pickingNotYetPicked.setPpNameData(addPpNames());

        PpNameData pickingPicked = new PpNameData();
        pickingPicked.setPpNameData(addPpNames());

        PpNameData rebatched = new PpNameData();
        rebatched.setPpNameData(addPpNames());

        PpNameData rebinBuffer = new PpNameData();
        rebinBuffer.setPpNameData(addPpNames());

        PpNameData rebined = new PpNameData();
        rebined.setPpNameData(addPpNames());

        PpNameData scanVerify = new PpNameData();
        scanVerify.setPpNameData(addPpNames());

        PpNameData packed = new PpNameData();
        packed.setPpNameData(addPpNames());

        PpNameData problem = new PpNameData();
        problem.setPpNameData(addPpNames());

        PpNameData sorted = new PpNameData();
        sorted.setPpNameData(addPpNames());

        for (LocalDateTime exsdTime : exsdTimes) {
            List<PpNames> row = new ArrayList<>();

            row = rowNormaliser(ppWorkTotalQuery.getByTotal(exsdTime), exsdTime);
            if (row.stream().anyMatch(ppNames -> ppNames.getWorkPpNameTime() != null)) {
                total.getTimes().add(exsdTime);
            }
            total.setPpNameData(addLine(total.getPpNameData(), row));


            row = rowNormaliser(ppWorkTotalQuery.getByReadyToPick(exsdTime), exsdTime);
            if (row.stream().anyMatch(ppNames -> ppNames.getWorkPpNameTime() != null)) {
                readyToPick.getTimes().add(exsdTime);
            }
            readyToPick.setPpNameData(addLine(readyToPick.getPpNameData(), row));


            row = rowNormaliser(ppWorkTotalQuery.getByPickingNotYetPicked(exsdTime), exsdTime);
            if (row.stream().anyMatch(ppNames -> ppNames.getWorkPpNameTime() != null)) {
                pickingNotYetPicked.getTimes().add(exsdTime);
            }
            pickingNotYetPicked.setPpNameData(addLine(pickingNotYetPicked.getPpNameData(), row));


            row = rowNormaliser(ppWorkTotalQuery.getByPickingPicked(exsdTime), exsdTime);
            if (row.stream().anyMatch(ppNames -> ppNames.getWorkPpNameTime() != null)) {
                pickingPicked.getTimes().add(exsdTime);
            }
            pickingPicked.setPpNameData(addLine(pickingPicked.getPpNameData(), row));

            row = rowNormaliser(ppWorkTotalQuery.getByRebatched(exsdTime), exsdTime);
            if (row.stream().anyMatch(ppNames -> ppNames.getWorkPpNameTime() != null)) {
                rebatched.getTimes().add(exsdTime);
            }
            rebatched.setPpNameData(addLine(rebatched.getPpNameData(), row));


            row = rowNormaliser(ppWorkTotalQuery.getByRebinBuffer(exsdTime), exsdTime);
            if (row.stream().anyMatch(ppNames -> ppNames.getWorkPpNameTime() != null)) {
                rebinBuffer.getTimes().add(exsdTime);
            }
            rebinBuffer.setPpNameData(addLine(rebinBuffer.getPpNameData(), row));

            row = rowNormaliser(ppWorkTotalQuery.getByRebined(exsdTime), exsdTime);
            if (row.stream().anyMatch(ppNames -> ppNames.getWorkPpNameTime() != null)) {
                rebined.getTimes().add(exsdTime);
            }
            rebined.setPpNameData(addLine(rebined.getPpNameData(), row));

            row = rowNormaliser(ppWorkTotalQuery.getByScanVerify(exsdTime), exsdTime);
            if (row.stream().anyMatch(ppNames -> ppNames.getWorkPpNameTime() != null)) {
                scanVerify.getTimes().add(exsdTime);
            }
            scanVerify.setPpNameData(addLine(scanVerify.getPpNameData(), row));

            row = rowNormaliser(ppWorkTotalQuery.getByPacked(exsdTime), exsdTime);
            if (row.stream().anyMatch(ppNames -> ppNames.getWorkPpNameTime() != null)) {
                packed.getTimes().add(exsdTime);
            }
            packed.setPpNameData(addLine(packed.getPpNameData(), row));

            row = rowNormaliser(ppWorkTotalQuery.getByProblem(exsdTime), exsdTime);
            if (row.stream().anyMatch(ppNames -> ppNames.getWorkPpNameTime() != null)) {
                problem.getTimes().add(exsdTime);
            }
            problem.setPpNameData(addLine(problem.getPpNameData(), row));

            row = rowNormaliser(ppWorkTotalQuery.getBySorted(exsdTime), exsdTime);
            if (row.stream().anyMatch(ppNames -> ppNames.getWorkPpNameTime() != null)) {
                sorted.getTimes().add(exsdTime);
            }
            sorted.setPpNameData(addLine(sorted.getPpNameData(), row));
        }

        ppWorkDTO.setTotal(total);
        ppWorkDTO.setReadyToPick(readyToPick);
        ppWorkDTO.setPickingNotYetPicked(pickingNotYetPicked);
        ppWorkDTO.setPickingPicked(pickingPicked);
        ppWorkDTO.setRebatched(rebatched);
        ppWorkDTO.setRebinBuffer(rebinBuffer);
        ppWorkDTO.setRebined(rebined);
        ppWorkDTO.setScanVerify(scanVerify);
        ppWorkDTO.setPacked(packed);
        ppWorkDTO.setProblem(problem);
        ppWorkDTO.setSorted(sorted);

        return file(ppWorkDTO);
    }

    public List<PpNames> addPpNames() {
        List<PpNames> ppNames = new ArrayList<>();

        //获取Ppname列表名
        List<String> names = ppWorkTotalQuery.getByPpName();

        //将所有ppName 添加 到 ppNameLists
        names.stream().forEach(name -> {
            ppNames.add(new PpNames(name));
        });

        return ppNames;
    }

    //添加一列数据
    public List<PpNames> addLine(List<PpNames> ppNames, List<PpNames> line) {

        if (line.size() != 0 && !line.isEmpty()) {
            for (PpNames l : line) {
                String name = l.getPpName();
                if (name != null && !name.equals("")) {
                    for (PpNames p : ppNames) {
                        if (name.equalsIgnoreCase(p.getPpName()) && !l.getWorkPpNameTime().isEmpty()) {
                            p.getWorkPpNameTime().addAll(l.getWorkPpNameTime());
                        }
                    }
                }

            }
        }
        return ppNames;
    }


    //将 List{[name，amount]} 转  List {name[exsdtime,amount]}
    public List<PpNames> rowNormaliser(List<PpName> ppNames, LocalDateTime exsdTime) {
        List<PpNames> ppNameLists = new ArrayList<>();

        if (ppNames != null && !ppNames.isEmpty()) {

            for (PpName name : ppNames) {

                WorkPpNameTime ppNameTime = new WorkPpNameTime(exsdTime, name.getAmount());

                PpNames ppNameList = new PpNames();

                ppNameList.setPpName(name.getName());
                ppNameList.getWorkPpNameTime().add(ppNameTime);

                ppNameLists.add(ppNameList);
            }
        }

        return ppNameLists;
    }


}
