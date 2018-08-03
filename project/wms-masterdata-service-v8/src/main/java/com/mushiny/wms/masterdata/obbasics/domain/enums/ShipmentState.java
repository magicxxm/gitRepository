package com.mushiny.wms.masterdata.obbasics.domain.enums;

public class ShipmentState {

    public static final int STARTED = 500;
    /**
     * The processing cannot continue, but is not finished.
     */
    public static final int PENDING = 550;
    /**
     * The material has been taken.
     */
    public static final int PICKED = 600;
    /**
     * The material has been rebatched.
     */
    public static final int REBATCHED = 610;
    /**
     * The material has been rebinbuffered.
     */
    public static final int REBINBUFFERED = 620;
    /**
     * The material has been rebined.
     */
    public static final int REBINED = 630;
    /**
     * The material has been packing.
     */
    public static final int PACKING = 640;
    /**
     * The material has been packed.
     */
    public static final int PACKED = 650;
    /**
     * The material has been sorted.
     */
    public static final int SORTED = 660;
}
