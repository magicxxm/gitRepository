package com.mushiny.service;

import com.mushiny.beans.Address;
import com.mushiny.beans.Section;

import java.util.List;
import java.util.Map;

/**
 * Created by Tank.li on 2017/7/30.
 */
public interface PodService {

    void restorePodStatus();

    String callPod(String workStationId);

    void hotCreateHistory();

    void hotCreateWareHouse();

    void hotCreateCurrent(Section section, Map itemHots);

    String computeTargetAddress(String podId, String sectionId);

    boolean isAvaliable(Address address);

}
