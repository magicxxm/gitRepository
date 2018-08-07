package com.mushiny.service;

import com.mushiny.common.crud.AccessDTO;
import com.mushiny.web.dto.ChuanMaInfo;

/**
 * Created by 123 on 2018/2/8.
 */
public interface SequenceService {

    AccessDTO createSequence(ChuanMaInfo chuanMaInfo);
}
