package com.mushiny.wms.masterdata.obbasics.common.constants;

public class CommonPropertyKey {

    public static final String PROPERTY_GROUP_CLIENT = "NB";
    public static final String PROPERTY_GROUP_MOBILE = "MOBILE";
    public static final String PROPERTY_GROUP_SERVER = "SERVER";

    /**
     * The value for the sender of a mail
     */
    public static final String MAIL_SENDER = "MAIL_SENDER";

    /**
     * The name of the mail-server
     */
    public static final String MAIL_SERVER = "MAIL_SERVER";

    /**
     * Set this to true, if authorization for sending a mail is required.
     */
    public static final String MAIL_AUTHOZIZE = "MAIL_AUTHORIZE";

    /**
     * The authorized user on the mail-server. Only used, if authorization for sending a mail is required.
     */
    public static final String MAIL_HOST_USER = "MAIL_HOST_USER";

    /**
     * The password of the user on the mail-server. Only used, if authorization for sending a mail is required.
     */
    public static final String MAIL_HOST_PASSWORD = "MAIL_HOST_PASSWORD";

    /**
     * In the detail area the property-panel will be shown per default
     */
    public static final String NBCLIENT_SHOW_DETAIL_PROPERTIES = "NBCLIENT_SHOW_DETAIL_PROPERTIES";

    /**
     * Opened tabs will be restored on next start
     */
    public static final String NBCLIENT_RESTORE_TABS = "NBCLIENT_RESTORE_TABS";

    /**
     * Dialogs of entity-explorer will select data when started
     */
    public static final String NBCLIENT_SELECTION_ON_START = "NBCLIENT_SELECTION_ON_START";

    /**
     * Allow unlimited selection
     */
    public static final String NBCLIENT_SELECTION_UNLIMITED = "NBCLIENT_SELECTION_UNLIMITED";

    /**
     * Regular expression to match valid client version, e.g. ".*" matches all, "1\.4" matches 1.4
     */
    public static final String NBCLIENT_VERSION_MATCHER = "NBCLIENT_VERSION_MATCHER";
}
