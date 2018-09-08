package com.mushiny.wms.application.business.score;

import com.mushiny.wms.application.domain.MdStationnodeposition;
import com.mushiny.wms.application.domain.Pod;

/**
 * Created by Administrator on 2018/7/12.
 */
public class StationPodScore {
    private Pod pod;
    private MdStationnodeposition stationnodeposition;
    private Integer score=0;

    public Pod getPod() {
        return pod;
    }

    public void setPod(Pod pod) {
        this.pod = pod;
    }

    public MdStationnodeposition getStationnodeposition() {
        return stationnodeposition;
    }

    public void setStationnodeposition(MdStationnodeposition stationnodeposition) {
        this.stationnodeposition = stationnodeposition;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
