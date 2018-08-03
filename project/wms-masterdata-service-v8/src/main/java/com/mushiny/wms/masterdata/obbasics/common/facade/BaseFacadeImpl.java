package com.mushiny.wms.masterdata.obbasics.common.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class BaseFacadeImpl implements BaseFacade {

    private final Logger log = LoggerFactory.getLogger(BaseFacadeImpl.class);
}
