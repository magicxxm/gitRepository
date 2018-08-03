package com.mushiny.wms.masterdata.ibbasics.business.enums;


public enum SerialNoRecordType {

    /**
     * Serial numbers will not be recorded during processes.
     */
    NO_RECORD,

    /**
     * Serial numbers will only be recorded during goods out process.
     */
    GOODS_OUT_RECORD,

    /**
     * Serial numbers will be recorded during all processes.
     */
    ALWAYS_RECORD
}

