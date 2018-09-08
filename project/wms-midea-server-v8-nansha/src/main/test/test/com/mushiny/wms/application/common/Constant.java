package test.com.mushiny.wms.application.common;

public class Constant {

    public final static String HEADER_WAREHOUSE_KEY = "Warehouse";

    public final static String SYSTEM_CLIENT = "SYSTEM";

    public final static String SUPER_ROLE = "ADMIN";

    public final static String DEFAULT_LOCALE = "CN";

    public final static String MODULE_ROOT = "ROOT";

    public final static Integer NOT_LOCKED = 0;

    public final static Integer GENERAL = 1;

    public final static Integer GOING_TO_DELETE = 2;

    /*
    数据存库成功
     */
    public final static Integer SUCCESS_FLAG = 1;
    /*
    数据存库失败
     */
    public final static Integer FAIL_FLAG = 0;
    /*
    美的同步物料信息接口
     */
    public final static String MIDEA_MITEM_SUBFIX = "mItems";  // mItems 为本地测试接口，注意修改
    /*
    美的同步物料信息接口url
     */
    public final static String MIDEA_MITEM_URL = "http://localhost:12004/" + MIDEA_MITEM_SUBFIX;  // http://localhost:12004/mItems 为本地测试url，注意修改
    public final static long MITEM_TIME = 30 * 60 * 1000; // 物料同步的间隔时间  一般默认30min
    public static long LAST_UPDATE_DATE = System.currentTimeMillis(); // 物料同步默认参数  最后更新时间
    public static int INV_ORG_ID = 200124; // 物料同步默认参数  库存组织




}
