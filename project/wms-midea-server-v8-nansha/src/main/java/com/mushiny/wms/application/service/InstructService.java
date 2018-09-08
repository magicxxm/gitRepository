package com.mushiny.wms.application.service;

/**
 * Created by Administrator on 2018/7/10.
 */
public interface InstructService {
    boolean cancelInstruct(String cancelRequest);
    Object getInstruct(String param);
    Integer updateInstruct(String param);
}
