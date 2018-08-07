package com.mushiny.service;

import com.mushiny.common.crud.AccessDTO;

/**
 * Created by 123 on 2018/3/15.
 */
public interface RebackService {
    AccessDTO updateCustomer(String orderNo);

    AccessDTO updateInbound(String orderNo);
}
