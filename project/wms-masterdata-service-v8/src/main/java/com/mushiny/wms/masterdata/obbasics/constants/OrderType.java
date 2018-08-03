package com.mushiny.wms.masterdata.obbasics.constants;

public enum OrderType {

    /**
     * Goods going into a production process
     */
    TO_PRODUCTION,

    /**
     * Goods for internal use, some locks won't take effect
     */
    INTERNAL,

    /**
     * Goods going to a customer
     */
    TO_CUSTOMER,

    /**
     * Goods for replenishing
     */
    TO_REPLENISH,

    /**
     * Goods should be extinguished
     */
    TO_EXTINGUISH,

    /**
     * Goods should be brought from one site to another
     */
    TO_OTHER_SITE
}
