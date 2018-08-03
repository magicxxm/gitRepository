package com.mushiny.wms.report.query.dto;

import java.io.Serializable;
import java.math.BigDecimal;

// Workflow 合计数据
public class WorkFlowCutlineDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /*Replenishment*/
    private BigDecimal needToReplenish = BigDecimal.ZERO;

    private BigDecimal stockEnough = BigDecimal.ZERO;

    private BigDecimal replenishing = BigDecimal.ZERO;

    private BigDecimal totalReplenishment = BigDecimal.ZERO;

    /*Picking*/
    private BigDecimal pending = BigDecimal.ZERO;

    private BigDecimal readyToPick = BigDecimal.ZERO;

    private BigDecimal pickingNotYetPicked = BigDecimal.ZERO;

    private BigDecimal totalPicking = BigDecimal.ZERO;

    /*Work In Process */
    private BigDecimal pickingPicked = BigDecimal.ZERO;

    private BigDecimal rebatched = BigDecimal.ZERO;

    private BigDecimal rebinBuffer = BigDecimal.ZERO;

    private BigDecimal rebined = BigDecimal.ZERO;

    private BigDecimal scanVerify = BigDecimal.ZERO;

    private BigDecimal packed = BigDecimal.ZERO;

    private BigDecimal totalWorkInProcess = BigDecimal.ZERO;

    /*Problem Solve In Process*/
    private BigDecimal problem = BigDecimal.ZERO;

    /*Ship*/
    private BigDecimal sorted = BigDecimal.ZERO;

    private BigDecimal loaded = BigDecimal.ZERO;

    private BigDecimal manifested = BigDecimal.ZERO;

    private BigDecimal totalShipping = BigDecimal.ZERO;

    private BigDecimal total = BigDecimal.ZERO;

    /*Replenishment*/

    //    public void setNeedToReplenish(BigDecimal needToReplenish) {
//        this.needToReplenish = needToReplenish;
//    }
    /*----- totalReplenishment - stockEnough - replenishing -----*/
    public void setNeedToReplenish() {
        this.needToReplenish = this.totalReplenishment.
                subtract(this.stockEnough).
                subtract(this.replenishing);
    }

    public BigDecimal getPending() {
        return pending;
    }

    public void setPending(BigDecimal pending) {
        this.pending = pending;
    }

    public void setStockEnough(BigDecimal stockEnough) {
        this.stockEnough = stockEnough;
    }

    public void setReplenishing(BigDecimal replenishing) {
        this.replenishing = replenishing;
    }

    public void setTotalReplenishment(BigDecimal totalReplenishment) {
        this.totalReplenishment = totalReplenishment;
    }


    /*Picking*/
    public void setReadyToPick(BigDecimal readyToPick) {
        this.readyToPick = readyToPick;
    }

    public void setPickingNotYetPicked(BigDecimal pickingNotYetPicked) {
        this.pickingNotYetPicked = pickingNotYetPicked;
    }

    /*-----readyToPick + pickingNotYetPicked -----*/
    public void setTotalPicking() {
        this.totalPicking = this.getPending().add(this.readyToPick).add(this.pickingNotYetPicked);
    }


    /* Work In Process */
    public void setPickingPicked(BigDecimal pickingPicked) {
        this.pickingPicked = pickingPicked;
    }

    public void setRebatched(BigDecimal rebatched) {
        this.rebatched = rebatched;
    }

    public void setRebinBuffer(BigDecimal rebinBuffer) {
        this.rebinBuffer = rebinBuffer;
    }

    public void setRebined(BigDecimal rebined) {
        this.rebined = rebined;
    }

    public void setScanVerify(BigDecimal scanVerify) {
        this.scanVerify = scanVerify;
    }

    public void setPacked(BigDecimal packed) {
        this.packed = packed;
    }

    /*----- pickingPicked + rebatched + rebinBuffer + rebined + scanVerify + packed -----*/
    public void setTotalWorkInProcess() {
        this.totalWorkInProcess = this.pickingPicked.
                add(this.rebatched).
                add(this.rebinBuffer).
                add(this.rebined).
                add(this.scanVerify).
                add(this.packed);
    }

    /*Problem Solve In Process*/
    public void setProblem(BigDecimal problem) {
        this.problem = problem;
    }

    /*Ship*/
    public void setSorted(BigDecimal sorted) {
        this.sorted = sorted;
    }

    public void setLoaded(BigDecimal loaded) {
        this.loaded = loaded;
    }

    public void setManifested(BigDecimal manifested) {
        this.manifested = manifested;
    }

    /*----- sorted + manifested -----*/
    public void setTotalShipping() {
        this.totalShipping = this.sorted.add(this.loaded).add(this.manifested);
    }


    /*----- replenishing + totalReplenishment + totalPicking + totalWorkInProcess + problem + totalShipping -----*/
    public void setTotal() {
//        this.total = this.totalReplenishment.
//                add(this.totalPicking).
//                add(this.totalWorkInProcess).
//                add(this.problem).
//                add(this.totalShipping);
        this.total = this.totalPicking.
                add(this.totalWorkInProcess).
                add(this.problem).
                add(this.totalShipping);
    }


    /**/
    public BigDecimal getNeedToReplenish() {
        return needToReplenish;
    }

    public BigDecimal getStockEnough() {
        return stockEnough;
    }

    public BigDecimal getReplenishing() {
        return replenishing;
    }

    public BigDecimal getTotalReplenishment() {
        return totalReplenishment;
    }

    public BigDecimal getReadyToPick() {
        return readyToPick;
    }

    public BigDecimal getPickingNotYetPicked() {
        return pickingNotYetPicked;
    }

    public BigDecimal getTotalPicking() {
        return totalPicking;
    }

    public BigDecimal getPickingPicked() {
        return pickingPicked;
    }

    public BigDecimal getRebatched() {
        return rebatched;
    }

    public BigDecimal getRebinBuffer() {
        return rebinBuffer;
    }

    public BigDecimal getRebined() {
        return rebined;
    }

    public BigDecimal getScanVerify() {
        return scanVerify;
    }

    public BigDecimal getPacked() {
        return packed;
    }

    public BigDecimal getTotalWorkInProcess() {
        return totalWorkInProcess;
    }

    public BigDecimal getProblem() {
        return problem;
    }

    public BigDecimal getSorted() {
        return sorted;
    }

    public BigDecimal getLoaded() {
        return loaded;
    }

    public BigDecimal getManifested() {
        return manifested;
    }

    public BigDecimal getTotalShipping() {
        return totalShipping;
    }

    public BigDecimal getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "CutlineDTO{" +
                ", needToReplenish=" + needToReplenish +
                ", stockEnough=" + stockEnough +
                ", replenishing=" + replenishing +
                ", totalReplenishment=" + totalReplenishment +
                ", pending=" + pending +
                ", readyToPick=" + readyToPick +
                ", pickingNotYetPicked=" + pickingNotYetPicked +
                ", totalPicking=" + totalPicking +
                ", pickingPicked=" + pickingPicked +
                ", rebatched=" + rebatched +
                ", rebinBuffer=" + rebinBuffer +
                ", rebined=" + rebined +
                ", scanVerify=" + scanVerify +
                ", packed=" + packed +
                ", totalWorkInProcess=" + totalWorkInProcess +
                ", problem=" + problem +
                ", sorted=" + sorted +
                ", loaded=" + loaded +
                ", manifested=" + manifested +
                ", totalShipping=" + totalShipping +
                ", total=" + total +
                '}';
    }
}
