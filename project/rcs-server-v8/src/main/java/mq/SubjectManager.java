package mq;

/**
 * On 2017/7/7.
 *
 * @author wangdong
 *         TODO :
 */
public class SubjectManager
{
    //ROBOT实时数据
    public static final String RCS_ROBOT_RT="RCS_WCS_ROBOT_RT";
    //ROBOT周期性数据
    public static final String RCS_ROBOT_STATUS="RCS_WCS_ROBOT_STATUS";
    //ROBOT错误数据
    public static final String RCS_ROBOT_ERROR="RCS_WCS_ROBOT_ERROR";
    //ROBOT心跳数据
    public static final String RCS_ROBOT_HEART_BEAT="RCS_WCS_ROBOT_HEART_BEAT";
    //ROBOT登录数据
    public static final String RCS_WCS_ROBOT_LOGIN ="RCS_WCS_ROBOT_LOGIN";
    //AGV登录包的回复
    public static final String WCS_RCS_ROBOT_LOGIN_RESPONSE ="WCS_RCS_ROBOT_LOGIN_RESPONSE";
    //ROBOT连接到RCS事件
    public static final String RCS_ROBOT_CONNECT_RCS ="RCS_WCS_ROBOT_CONNECT_RCS";
    //ROBOT关闭到RCS
    public static final String RCS_ROBOT_CLOSE_CONNECTION="RCS_WCS_ROBOT_CLOSE_CONNECTION";
    //ROBOT重新连接
    public static final String ROBOT_RECONNECTED="RCS_WCS_ROBOT_RECONNECTED";
    //地图请求主题
        public static final String RCS_MAP_REQUEST = "RCS_WCS_MAP_REQUEST";
    //地图回复主题
    public static final String RCS_MAP_RESPONSE= "WCS_RCS_MAP_RESPONSE";

    //AGV位置改变
    public static final String RCS_WCS_AGV_POSITION_CHANGE = "RCS_WCS_AGV_POSITION_CHANGE";
    //AGV状态事件
    public static final String RCS_WCS_AGV_STATUS = "RCS_WCS_AGV_STATUS";
    //AGV位置不改变超时
    public static final String AGV_NOMOVE_TIMMEOUT = "RCS_WCS_AGV_NOMOVE_TIMMEOUT";
    //心跳或实时包未收到超时
    public static final String AGV_HEART_RT_TIMEOUT = "RCS_WCS_AGV_HEART_RT_TIMEOUT";
    //AGV重新连接到RCS并且位置错误
    public static final String AGV_REPEAT_CONNECT2_RCS_POSITION_ERROR = "AGV_REPEAT_CONNECT2_RCS_POSITION_ERROR";
    //AGV路径下发
    public static final String WCS_RCS_AGV_SERIESPATH = "WCS_RCS_AGV_SERIESPATH";

    //停到最近二维码，减速到0 / 回复
    public static final String AGV_PARKING_NEAREST = "WCS_RCS_AGV_PARKING_NEAREST";
    public static final String AGV_PARKING_RESPONSE = "RCS_WCS_AGV_PARKING_RESPONSE";
    //急停 / 回复
    public static final String URGENT_STOP = "WCS_RCS_URGENT_STOP";
    public static final String URGENT_STOP_RESPONSE = "RCS_WCS_URGENT_STOP_RESPONSE";
    //AGV所有电机断电 / 回复
    public static final String AGV_ALL_MOTOR_CUT = "WCS_RCS_ALL_MOTOR_CUT";
    public static final String AGV_ALL_MOTOR_CUT_RESPONSE = "RCS_WCS_ALL_MOTOR_CUT_RESPONSE";
    //开始休眠 / 回复
    public static final String AGV_START_SLEEP = "WCS_RCS_START_SLEEP";
    public static final String AGV_START_SLEEP_RESPONSE = "RCS_WCS_START_SLEEP_RESPONSE";
    //AGV结束休眠 / 回复
    public static final String AGV_STOP_SLEEP = "WCS_RCS_STOP_SLEEP";
    public static final String AGV_STOP_SLEEP_RESPONSE = "RCS_WCS_STOP_SLEEP_RESPONSE";
    //AGV启动 / 回复
    public static final String WCS_RCS_AGV_START = "WCS_RCS_AGV_START";
    public static final String WCS_RCS_AGV_START_RESPONSE = "WCS_RCS_AGV_START_RESPONSE";

    //清除已下发路径-清除AGV缓存路径和RCS缓存路径 / 回复WCS_RCS_CLEAR_ALLPATH
    public static final String AGV_CLEAR_ALLPATH = "WCS_RCS_CLEAR_ALLPATH";
    public static final String WCS_RCS_CLEAR_PATH_FOR_POSITION_NO_CHANGING = "WCS_RCS_CLEAR_PATH_FOR_POSITION_NO_CHANGING";
    public static final String AGV_CLEAR_PATH_RESPONSE = "RCS_WCS_CLEAR_PATH_RESPONSE";

    //所有AGV停到最近二维码
    public static final String WCS_RCS_ALL_AGV_PARKING_OR_START_UP = "WCS_RCS_ALL_AGV_PARKING_OR_START_UP";
    //将需要人工处理的AGV从RCS队列移除
    public static final String WCS_RCS_REMOVE_ERROR_AGV = "WCS_RCS_REMOVE_ERROR_AGV";

    //通用充电协议
    public static final String WCS_RCS_AGV_CHARGE = "WCS_RCS_AGV_CHARGE";
    //美的充电
    public static final String WCS_RCS_MIDEA_CHARGE = "WCS_RCS_MIDEA_CHARGE";
    //结束充电指令
    public static final String WCS_RCS_AGV_STOP_CHARGE = "WCS_RCS_AGV_STOP_CHARGE";

    //AGV锁格超时
    public static final String RCS_WCS_LOCK_CELL_TIMEOUT = "RCS_WCS_LOCK_CELL_TIMEOUT";
    //通知WCS AGV当前路径中有临时不可走
    public static final String RCS_WCS_SP_UNWALK_CELL = "RCS_WCS_SP_UNWALK_CELL";
    //AGV请求WCS重新规划路径
    public static final String RCS_WCS_AGV_REQUEST_PATH = "RCS_WCS_AGV_REQUEST_PATH";


    // mingchun.mu@mushiny.com  2017-09-05 ----------------------------------
    public static final String RCS_WCS_SCAN_POD = "RCS_WCS_SCAN_POD";// 初始化pod位置 扫描pod路由
    public static final String WCS_RCS_PODSCAN_PATH = "WCS_RCS_PODSCAN_PATH";// 初始化pod位置 扫描pod路由
    public static final String RCS_CHARGING_PILE_START_CONNECTING = "RCS_CHARGING_PILE_START_CONNECTING";// 小车到达充电点， 要求充电桩准备对接
    public static final String RCS_CHARGING_PILE_END_CHARGE = "RCS_CHARGING_PILE_END_CHARGE";//rcs收到充电路径，要求结束对小车充电，以便执行任务
    public static final String WCS_RCS_ACTION_COMMAND = "WCS_RCS_ACTION_COMMAND";//动作命令路由  例如举升，下降
    public static final String RCS_WCS_ACTION_FINISHED_COMMAND = "RCS_WCS_ACTION_FINISHED_COMMAND";// 初始化pod位置 扫描pod路由
    public static final String WCS_RCS_UPDATE_CELLS = "WCS_RCS_UPDATE_CELLS";// 格子锁格需求
    public static final String RCS_CHARGING_PILE_START_CHARGING = "RCS_CHARGING_PILE_START_CHARGING";// 小车到达充电点， 通知充电桩开始充电
    public static final String RCS_WCS_POD_ERRORPLACE = "RCS_WCS_POD_ERRORPLACE";// 小车在举升点pod扫描错误
    public static final String RCS_CONFIG_PATH = "/config/RCSConfig.xml";
    public static final String COM_EXCHANGE = "section";
    public static final String WCS_RCS_REQUEST_ITEM_INFO = "WCS_RCS_REQUEST_ITEM_INFO";
    public static final String RCS_WCS_RESPONSE_ITEM_INFO = "RCS_WCS_RESPONSE_ITEM_INFO";
    public static final String WCS_RCS_REQUEST_ALL_AGV_INFO = "WCS_RCS_REQUEST_ALL_AGV_INFO"; // 获取所有agv信息
    public static final String RCS_WCS_RESPONSE_ALL_AGV_INFO = "RCS_WCS_RESPONSE_ALL_AGV_INFO"; // 响应所有agv的信息
    public static final String WCS_RCS_CHANGING_POD_POSITION = "WCS_RCS_CHANGING_POD_POSITION"; // 更改pod位置
    public static final String WCS_RCS_REMOVING_POD = "WCS_RCS_REMOVING_POD"; // 移除POD
    public static final String WCS_RCS_OFFLINE_ROBOT = "WCS_RCS_OFFLINE_ROBOT"; // 从地图中移除AGV

    public static final String WCS_RCS_AGV_PATH_RESEND = "WCS_RCS_AGV_PATH_RESEND"; // agv在转弯处断网可能导致的路径丢失

    public static final String RCS_WCS_RESPONSE_MEDIA_ERROR = "RCS_WCS_RESPONSE_MEDIA_ERROR";// 美的故障数据包
    public static final String WCS_WRS_CLEAR_MEDIA_ERROR = "WCS_WRS_CLEAR_MEDIA_ERROR";// 美的故障数据包

    public static final String WCS_WRS_CONFIG_AGV_INFO = "WCS_WRS_CONFIG_AGV_INFO";// 配置小车新信息

    // 配置数据包
    public static final String WCS_RCS_REQUEST_MEDIA_AGV_CONFIG_PARAMETERS = "WCS_RCS_REQUEST_MEDIA_AGV_CONFIG_PARAMETERS";// 回读
    public static final String RCS_WCS_RESPONSE_MEDIA_AGV_CONFIG_PARAMETERS = "RCS_WCS_RESPONSE_MEDIA_AGV_CONFIG_PARAMETERS";// 响应
    public static final String WCS_RCS_UPDATE_MEDIA_AGV_CONFIG_PARAMETERS = "WCS_RCS_UPDATE_MEDIA_AGV_CONFIG_PARAMETERS";// 配置
    public static final String WCS_RCS_REQUEST_ACTION_COMMAND = "WCS_RCS_REQUEST_ACTION_COMMAND"; // 动作命令码请求
    public static final String RCS_WCS_RESPONSE_ACTION_COMMAND = "RCS_WCS_RESPONSE_ACTION_COMMAND"; // 动作命令码响应回复
    public static final String RCS_WCS_UNLOCKED_CELL_LIST = "RCS_WCS_UNLOCKED_CELL_LIST"; // 动作命令码响应回复
    public static final String RCS_WCS_ADDRESS_CODE_ID_LOST_INFO = "RCS_WCS_ADDRESS_CODE_ID_LOST_INFO"; // 地址码丢失
    public static final String WCS_RCS_CHECKING_AGV_PATH = "WCS_RCS_CHECKING_AGV_PATH";// 核实agv路径
    public static final String WCS_RCS_CHECKING_AGV_PATH_RESPONSE = "WCS_RCS_CHECKING_AGV_PATH_RESPONSE";// 核实agv路径信息反馈
    public static final String WCS_RCS_SEND_TO_AGV_ACTIVE_MESSAGE = "WCS_RCS_SEND_TO_AGV_ACTIVE_MESSAGE";// 向agv发送激活数据包


    // mingchun.mu@mushiny.com  2017-09-05 ----------------------------------


}
