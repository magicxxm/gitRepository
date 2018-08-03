package com.mushiny.beans.order;

import java.util.HashMap;
import java.util.Map;

/**
 * 支持IBPPod OBPPod ICQAPod 三种Trip调度类型
 * Created by Tank.li on 2017/9/25.
 */
public class PqaPodOrder extends StationPodOrder{
    @Override
    public void finish() {
        super.finish();
        //通过Trip调度类型 和 PodID删除ENROUTEPOD表记录 PQA_ENROUTEPOD
        /*if(this.getIndex() == Order.BUFFER2STORAGE){
            unBindEnroutePod("PQA_ENROUTEPOD",this.getPod().getPodId());
        }*/
    }
}
