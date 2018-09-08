package com.mushiny.wms.application.service;

import java.util.List;

/**
 * Created by Administrator on 2018/7/11.
 */
public interface BatchService<T> {
     void batchInsert(List<T> list);

   void batchUpdate(List<T> list);
}
