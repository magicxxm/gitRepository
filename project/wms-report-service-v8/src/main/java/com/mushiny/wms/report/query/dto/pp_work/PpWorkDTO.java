package com.mushiny.wms.report.query.dto.pp_work;

import java.io.Serializable;

public class PpWorkDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private PpNameData total;

    private PpNameData readyToPick;

    private PpNameData pickingNotYetPicked;

    private PpNameData pickingPicked;

    private PpNameData rebatched;

    private PpNameData rebinBuffer;

    private PpNameData rebined;

    private PpNameData scanVerify;

    private PpNameData packed;

    private PpNameData problem;

    private PpNameData sorted;

    public PpNameData getTotal() {
        return total;
    }

    public void setTotal(PpNameData total) {
        this.total = total;
    }

    public PpNameData getReadyToPick() {
        return readyToPick;
    }

    public void setReadyToPick(PpNameData readyToPick) {
        this.readyToPick = readyToPick;
    }

    public PpNameData getPickingNotYetPicked() {
        return pickingNotYetPicked;
    }

    public void setPickingNotYetPicked(PpNameData pickingNotYetPicked) {
        this.pickingNotYetPicked = pickingNotYetPicked;
    }

    public PpNameData getPickingPicked() {
        return pickingPicked;
    }

    public void setPickingPicked(PpNameData pickingPicked) {
        this.pickingPicked = pickingPicked;
    }

    public PpNameData getRebatched() {
        return rebatched;
    }

    public void setRebatched(PpNameData rebatched) {
        this.rebatched = rebatched;
    }

    public PpNameData getRebinBuffer() {
        return rebinBuffer;
    }

    public void setRebinBuffer(PpNameData rebinBuffer) {
        this.rebinBuffer = rebinBuffer;
    }

    public PpNameData getRebined() {
        return rebined;
    }

    public void setRebined(PpNameData rebined) {
        this.rebined = rebined;
    }

    public PpNameData getScanVerify() {
        return scanVerify;
    }

    public void setScanVerify(PpNameData scanVerify) {
        this.scanVerify = scanVerify;
    }

    public PpNameData getPacked() {
        return packed;
    }

    public void setPacked(PpNameData packed) {
        this.packed = packed;
    }

    public PpNameData getProblem() {
        return problem;
    }

    public void setProblem(PpNameData problem) {
        this.problem = problem;
    }

    public PpNameData getSorted() {
        return sorted;
    }

    public void setSorted(PpNameData sorted) {
        this.sorted = sorted;
    }

    //    private List<PpNames> total = new ArrayList<>();
//
//    private List<PpNames> readyToPick = new ArrayList<>();
//
//    private List<PpNames> pickingNotYetPicked = new ArrayList<>();
//
//    private List<PpNames> pickingPicked = new ArrayList<>();
//
//    private List<PpNames> rebatched = new ArrayList<>();
//
//    private List<PpNames> rebinBuffer = new ArrayList<>();
//
//    private List<PpNames> rebined = new ArrayList<>();
//
//    private List<PpNames> scanVerify = new ArrayList<>();
//
//    private List<PpNames> packed = new ArrayList<>();
//
//    private List<PpNames> problem = new ArrayList<>();
//
//    private List<PpNames> sorted = new ArrayList<>();
//
//    public List<PpNames> getReadyToPick() {
//        return readyToPick;
//    }
//
//    public void setReadyToPick(List<PpNames> readyToPick) {
//        this.readyToPick = readyToPick;
//    }
//
//    public List<PpNames> getTotal() {
//        return total;
//    }
//
//    public void setTotal(List<PpNames> total) {
//        this.total = total;
//    }
//
//    public List<PpNames> getPickingNotYetPicked() {
//        return pickingNotYetPicked;
//    }
//
//    public void setPickingNotYetPicked(List<PpNames> pickingNotYetPicked) {
//        this.pickingNotYetPicked = pickingNotYetPicked;
//    }
//    public List<PpNames> getPickingPicked() {
//        return pickingPicked;
//    }
//
//    public void setPickingPicked(List<PpNames> pickingPicked) {
//        this.pickingPicked = pickingPicked;
//    }
//
//    public List<PpNames> getRebatched() {
//        return rebatched;
//    }
//
//    public void setRebatched(List<PpNames> rebatched) {
//        this.rebatched = rebatched;
//    }
//
//    public List<PpNames> getRebinBuffer() {
//        return rebinBuffer;
//    }
//
//    public void setRebinBuffer(List<PpNames> rebinBuffer) {
//        this.rebinBuffer = rebinBuffer;
//    }
//
//    public List<PpNames> getRebined() {
//        return rebined;
//    }
//
//    public void setRebined(List<PpNames> rebined) {
//        this.rebined = rebined;
//    }
//
//    public List<PpNames> getScanVerify() {
//        return scanVerify;
//    }
//
//    public void setScanVerify(List<PpNames> scanVerify) {
//        this.scanVerify = scanVerify;
//    }
//
//    public List<PpNames> getPacked() {
//        return packed;
//    }
//
//    public void setPacked(List<PpNames> packed) {
//        this.packed = packed;
//    }
//
//    public List<PpNames> getProblem() {
//        return problem;
//    }
//
//    public void setProblem(List<PpNames> problem) {
//        this.problem = problem;
//    }
//
//    public List<PpNames> getSorted() {
//        return sorted;
//    }
//
//    public void setSorted(List<PpNames> sorted) {
//        this.sorted = sorted;
//    }
//
//    @Override
//    public String toString() {
//        return "PpWorkDTO{" +
//                "total=" + total +
//                ", pickingNotYetPicked=" + pickingNotYetPicked +
//                ", pickingPicked=" + pickingPicked +
//                ", rebatched=" + rebatched +
//                ", rebinBuffer=" + rebinBuffer +
//                ", rebined=" + rebined +
//                ", scanVerify=" + scanVerify +
//                ", packed=" + packed +
//                ", problem=" + problem +
//                ", sorted=" + sorted +
//                '}';
//    }
}
