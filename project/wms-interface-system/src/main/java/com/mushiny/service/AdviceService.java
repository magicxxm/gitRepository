package com.mushiny.service;

import com.mushiny.common.crud.AccessDTO;
import com.mushiny.web.dto.AdviceReceiptDTO;

/**
 * Created by 123 on 2018/2/2.
 */
public interface AdviceService {

    AccessDTO createAdviceRequest(AdviceReceiptDTO adviceReceiptDTO);
}
