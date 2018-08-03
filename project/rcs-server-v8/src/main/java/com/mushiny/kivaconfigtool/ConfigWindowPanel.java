/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kivaconfigtool;

import com.aricojf.platform.common.Global;
import com.aricojf.platform.common.HexBinaryUtil;
import com.aricojf.platform.mina.message.*;
import com.aricojf.platform.mina.message.robot.*;
import com.aricojf.platform.mina.server.ServerManager;
import com.aricojf.platform.mina.server.ServerMessageService;
import com.mingchun.mu.util.ExceptionUtil;
import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.KivaMap;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.kiva.map.MapWindow;
import com.mushiny.kiva.path.RotateArea;
import com.mushiny.kiva.path.RotatePathManager;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.kiva.pathviewtool.PathViewApplication;
import com.mushiny.rcs.global.AGVConfig;
import com.mushiny.rcs.global.KivaBusConfig;
import com.mushiny.rcs.global.Robot2RCSMessageTypeConfig;
import com.mushiny.rcs.kiva.bus.*;
import com.mushiny.rcs.listener.AGVListener;
import com.mushiny.rcs.listener.AGVTimeoutListener;
import com.mushiny.rcs.server.*;
import com.mushiny.rcs.wcs.WCSChargeSeriesPath;
import com.mushiny.rcs.wcs.WCSMediaChargeSeriesPath;
import com.mushiny.rcs.wcs.WCSSeriesPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.net.InetSocketAddress;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author aricochen
 */
public class ConfigWindowPanel extends javax.swing.JPanel implements ServerStatusChanagedMessageListener, OnReceivedDataMessageListener, AGVListener, AGVTimeoutListener {

    static Logger LOG = LoggerFactory.getLogger(ConfigWindowPanel.class.getName());
    private ServerMessageService kivaServer;
    private RCSTimer rcsTimer = RCSTimer.getInstance();
    private final MapManager mapManager;
    private final PathTestMapIOManager pathTestMapIOManager;
    private KivaMap kivaMap;
    public GridBagLayout gbaglayout;
    public GridBagConstraints constraints;

    private RobotMessage robotMessage;
    protected RobotRTMessage rtMessage;
    protected RobotStatusMessage statusMessage;
    protected RobotErrorMessage errorMessage;
    protected RobotHeartBeatRequestMessage heartBeatMessage;
    protected RobotLoginRequestMessage loginMessage;
    protected RobotOpenConnectionMessage openConnectionMessage;
    protected RobotCloseConnectionMessage closeConnectionMessage;
    protected RobotResponseConfigMessage reponseConfigMessage;
    protected Robot2RCSActionCommandResponseMessage actionCommandReponseMessage;

    private int messageCount = 0;
    private KivaAGV currentAGV;
    private WCSSeriesPath wcsSeriesPath;
    private DecimalFormat decimalFormat = new DecimalFormat("#.00000");
    private PathTestTask pathTestTask;
    private boolean isPathOK = false;
    private boolean isRunTestPath = false;

    private RotateArea ra;//本地图可设置一个旋转区作为测试

    //--------------------------------------------------------
    private StartCommand startCommand;
    private StopByNearCodeCommand stopByNearCodeCommand;
    private StopImmediatelyCommand stopImmediatelyCommand;
    private StopMotoPowerCommand stopMotoPowerCommand;
    private Rotate10Command rotate10Command;
    private Rotate11Command rotate11Command;
    private Rotate12Command rotate12Command;
    private Rotate13Command rotate13Command;
    private UpCommand upCommand;
    private DownCommand downCommand;
    private BeginSleepCommand beginSleepCommand;
    private StopSleepCommand stopSleepCommand;
    private ClearPathCommand clearPathCommand;

    private PathViewApplication pathViewPathApplication;

    /**
     * Creates new form ConfigWindowPanel
     */
    public ConfigWindowPanel() {
        initComponents();
        mapManager = MapManager.getInstance();
        pathTestMapIOManager = new PathTestMapIOManager(mapManager.getMapWindow());
        mapManager.registerMapViewChangedListener(pathTestMapIOManager);
        setGBL();
    }

    public void initUI() {
        clearRobotInfo();
        addPathMap();
        initTestCommand();

        jLabel22.setText("服务器未开启");
        jTextField5.setText("");
        jTextField5.setEnabled(false);
        jTextField12.setText("180");
        jTextField19.setText("0");
        jTextField20.setText("0");
        jTextField23.setText("0");
        jCheckBox4.setEnabled(false);
        jTextField5.setEnabled(false);
        jTextField20.setEnabled(false);
        jTextField21.setEnabled(false);
        jTextField22.setEnabled(false);
        jTextField23.setEnabled(false);
        jTextField17.setText("8");//路径地图行
        jTextField18.setText("5");//路径地图列

        jTextField22.setText("");//测试路径的起点
        jTextField21.setText("");//测试路径的终点

        //jTextField3.setText("c:\\");//写入报文文件目录
        //jTextField3.setEnabled(false);
        heartBeatMessage = new RobotHeartBeatRequestMessage();
        rtMessage = new RobotRTMessage();
        statusMessage = new RobotStatusMessage();
        errorMessage = new RobotErrorMessage();
        loginMessage = new RobotLoginRequestMessage();
        openConnectionMessage = new RobotOpenConnectionMessage();
        closeConnectionMessage = new RobotCloseConnectionMessage();
        reponseConfigMessage = new RobotResponseConfigMessage();
        actionCommandReponseMessage = new Robot2RCSActionCommandResponseMessage();
    }

    //增加地图
    public void addPathMap() {
        jPanel6.removeAll();
        jPanel6.setLayout(gbaglayout);
        jPanel6.add(mapManager.getMapView(), constraints);
        jPanel6.repaint();
        jPanel6.revalidate();
    }

    //清空车辆信息
    public void clearRobotInfo() {
        jLabel16.setText("");
        jLabel17.setText("");
        jLabel18.setText("");
        jLabel19.setText("");
        jLabel31.setText("无");
        jLabel34.setText("");
        jLabel36.setText("");
        jLabel38.setText("");
        jLabel40.setText("");
        jLabel42.setText("");
        jLabel44.setText("");
        jLabel46.setText("");
        jLabel48.setText("");
        jLabel50.setText("");
        jLabel58.setText("");

        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField14.setText("");
        jTextField15.setText("");
        jTextField16.setText("");
        jTextField6.setText("");
        jTextField7.setText("");
        jTextField8.setText("");
        jTextField9.setText("");
        jTextField10.setText("");
        jTextField11.setText("");
        // jTextField12.setText("");
        jTextField13.setText("");

    }

    public void initTestCommand() {
        startCommand = new StartCommand();
        stopByNearCodeCommand = new StopByNearCodeCommand();
        stopImmediatelyCommand = new StopImmediatelyCommand();
        stopMotoPowerCommand = new StopMotoPowerCommand();
        rotate10Command = new Rotate10Command();
        rotate11Command = new Rotate11Command();
        rotate12Command = new Rotate12Command();
        rotate13Command = new Rotate13Command();
        upCommand = new UpCommand();
        downCommand = new DownCommand();
        beginSleepCommand = new BeginSleepCommand();
        stopSleepCommand = new StopSleepCommand();
        clearPathCommand = new ClearPathCommand();
        jComboBox1.removeAllItems();
        jComboBox1.addItem("==选择==");
        jComboBox1.addItem(startCommand);
        jComboBox1.addItem(stopByNearCodeCommand);
        jComboBox1.addItem(stopImmediatelyCommand);
        jComboBox1.addItem(stopMotoPowerCommand);
        jComboBox1.addItem(rotate10Command);
        jComboBox1.addItem(rotate11Command);
        jComboBox1.addItem(rotate12Command);
        jComboBox1.addItem(rotate13Command);
        jComboBox1.addItem(upCommand);
        jComboBox1.addItem(downCommand);
        jComboBox1.addItem(beginSleepCommand);
        jComboBox1.addItem(stopSleepCommand);
        jComboBox1.addItem(clearPathCommand);

    }

    //============================AGV监听实现=============================================
    //AGV收到全局路径
    public void OnSendGlobalPath2AGV(AGVMessage agv, SeriesPath globalPath) {

    }

    //AGV解锁CELL
    public void OnAGVUnLockedCell(AGVMessage agv, LinkedList<CellNode> unLockedCellNodeList) {

    }

    //AGV锁定CELL
    public void OnAGVLockedCell(AGVMessage agv, LinkedList<CellNode> lockedCellNodeList) {

    }

    //AGV到达全局路径的目标CELL
    public void OnAGVArriveAtGlobalPathTargetCell(AGVMessage agv, SeriesPath globalPath) {

    }

   

    //AGV状态事件
    public void OnAGVStatusChange(AGVMessage agv, int oldStatus, int newStatus) {
        jLabel58.setText(AGVConfig.getAGVStatusInfo(newStatus));
        if (newStatus == AGVConfig.AGV_STATUS_NO_CONNECTION) {//如果是TCP断开状态
            jLabel31.setText("0_0_0_0:00");
        }
    }

    //AGV位置改变
    public void OnAGVPositionChange(AGVMessage agv, long oldAddressIDCode, long newAddressIDCode) {

    }

    //AGV连接到RCS
    public void OnAGVOpenConnection2RCS(AGVMessage agv) {
        if (currentAGV != null) {
            return;
        }
        currentAGV = AGVManager.getInstance().getFirstRobot();
        if (currentAGV == null) {
            clearRobotInfo();
        } else {
            InetSocketAddress address = (InetSocketAddress) currentAGV.getSession().getRemoteAddress();
            jLabel31.setText(address.getAddress().getHostAddress() + ":" + address.getPort());
            //车辆ID
            jLabel16.setText(Long.toString(currentAGV.getID()));
            jTextField4.setText(Long.toString(currentAGV.getID()));
        }
    }

    //AGV关闭到RCS
    public void OnAGVCloseConnection2RCS(AGVMessage agv) {
        currentAGV = AGVManager.getInstance().getFirstRobot();
        if (currentAGV == null) {
            clearRobotInfo();
        } else {
            InetSocketAddress address = (InetSocketAddress) currentAGV.getSession().getRemoteAddress();
            jLabel31.setText(address.getAddress().getHostAddress() + ":" + address.getPort());
            //车辆ID
            jLabel16.setText(Long.toString(currentAGV.getID()));
            jTextField4.setText(Long.toString(currentAGV.getID()));
        }
    }

    //AGV重新连接到RCS
    public void OnAGVRepeatConnection2RCS(AGVMessage agv) {
        if (currentAGV == null) {
            return;
        }
        InetSocketAddress address = (InetSocketAddress) currentAGV.getSession().getRemoteAddress();
        jLabel31.setText(address.getAddress().getHostAddress() + ":" + address.getPort());
        jLabel58.setText(AGVConfig.getAGVStatusInfo(currentAGV.getAGVStatus()));
        showMessage("AGV重新连接事件,目前AGV数量=" + AGVManager.getInstance().getAGVCount() + "\r\n");
    }
     //路径中出现临时不可用的CELL
    public void OnAGVSPUnWalkedCell(AGVMessage agv,CellNode cellNode){
        
    }
      //请求WCS重新发送路径
    public void OnAGVRequestWCSPath(AGVMessage agv){
        
    }

    @Override
    public void onArrivedChargingPile(AGVMessage agv, long addressCodeID) {

    }

    //===========================AGV异常监听====================================
    //心跳超时
    public void onAGVBeatTimeout(KivaAGV agv) {

    }

    //实时数据包超时
    public void onAGVRTTimeout(KivaAGV agv) {

    }

    //心跳或实时数据包超时
    public void onAGVBeatOrRTTimeout(KivaAGV agv) {
        showMessage("####检测到AGV(ID=" + agv.getID() + ",IP=" + agv.getIP() + ")心跳和实时数据包超时....");

    }

    //位置不改变超时
    public void onAGVPositionNoChanageTimeout(KivaAGV agv, Map<String, Object> paramMap) {

    }
    //锁格超时
    public void onAGVLockCellTimeout(KivaAGV agv, Map<String, Object> paramMap){
        
    }

    //AGV重新连接后
    public void onAGVReConnection(KivaAGV agv) {

    }

    //TCP断开重连后,位置错误,不在锁格路径范围之内
    public void OnAGVRepeatConnection2RCS_PositionError(AGVMessage agv) {
        jLabel58.setText(AGVConfig.getAGVStatusInfo(currentAGV.getAGVStatus()));
    }

    //==========================SERVER状态监听实现==============================
    //状态消息
    @Override
    public void onServerStatusChanaged(ServerStatusMessage status) {
        showMessage(status.toString());
        //未开始服务
        if (status.getStatusCode() == StatusMessageCode.S_SERVER_CLOSE_SERVICE) {
            jLabel22.setText(status.getMessage().toString());
            jButton15.setText("开启服务");
        }
        //正在开启服务
        if (status.getStatusCode() == StatusMessageCode.S_SERVER_OPENING_SERVICE) {
            jLabel22.setText(status.getMessage().toString());
            jButton15.setText("正在开启服务...");
        }
        //已开启服务
        if (status.getStatusCode() == StatusMessageCode.S_SERVER_OPEN_SERVICE) {
            jLabel22.setText(status.getMessage().toString());
            jButton15.setText("关闭服务");
        }
    }

    public void showMessage(String msg) {
        if (jCheckBox2.isSelected()) {
            showLogMessage(msg);
        } else {
            showDataMessage(msg);
        }
    }
    //=========================AGV 消息监听========================================
    //报文监听

    public void onReceivedMessage(RobotMessage message) {
        messageCount++;
        if (message == null) {
            return;
        }

        robotMessage = message;
        if (robotMessage.getVelifyFunctionCode() == KivaBusConfig.ANALYSIS_DATAMESSAGE_ERROR) {
            showMessage("  #非法命令字");
            return;
        }

        //1.实时数据包
        if (robotMessage.getVelifyFunctionCode() == Robot2RCSMessageTypeConfig.ROBOT_REALTIME_MESSAGE) {
            rtMessage.setMessage(robotMessage.getMessage());
            rtMessage.toObject();
            //--currentAGV.setID(rtMessage.getRobotID());
            jLabel16.setText(rtMessage.getRobotID() + "");
            jTextField4.setText(rtMessage.getRobotID() + "");
            showMessage("-->" + rtMessage.toString());
        }
        //2.周期性状态数据包
        if (robotMessage.getVelifyFunctionCode() == Robot2RCSMessageTypeConfig.ROBOT_STATUS_MESSAGE) {
            statusMessage.setMessage(robotMessage.getMessage());
            statusMessage.toObject();
            //--currentAGV.setID(statusMessage.getRobotID());
            jLabel16.setText(statusMessage.getRobotID() + "");
            jTextField4.setText(statusMessage.getRobotID() + "");
            //剩余电量
            jLabel17.setText(Integer.toString(statusMessage.getShengYuDianLiang()));
            //电机温度
//            jLabel34.setText(Long.toString(statusMessage.getMotorTemperature()));
            showMessage("-->" + statusMessage.toString());
        }
        //3.故障数据包
        if (robotMessage.getVelifyFunctionCode() == Robot2RCSMessageTypeConfig.ROBOT_ERROR_MESSAGE) {
            errorMessage.setMessage(robotMessage.getMessage());
            errorMessage.toObject();
            //--currentAGV.setID(heartBeatMessage.getRobotID());
            jLabel16.setText(errorMessage.getRobotID() + "");
            jTextField4.setText(errorMessage.getRobotID() + "");
            //故障ID
            jLabel19.setText(Integer.toString(errorMessage.getErrorID()));
            showMessage("-->" + errorMessage.toString());
        }
        //4.心跳包
        if (robotMessage.getVelifyFunctionCode() == Robot2RCSMessageTypeConfig.HEART_BEAT_REQUEST_MESSAGE) {
            heartBeatMessage.setMessage(robotMessage.getMessage());
            heartBeatMessage.toObject();
            //-currentAGV.setID(heartBeatMessage.getRobotID());
            jLabel16.setText(heartBeatMessage.getRobotID() + "");
            jTextField4.setText(heartBeatMessage.getRobotID() + "");
            if (jCheckBox1.isSelected()) {
                showMessage("-->" + heartBeatMessage.toString());
            }
        }
        //5.登录数据包
        if (robotMessage.getVelifyFunctionCode() == Robot2RCSMessageTypeConfig.ROBOT_LOGIN_REQUEST_MESSAGE) {
            loginMessage.setMessage(robotMessage.getMessage());
            loginMessage.toObject();
            currentAGV.setID(loginMessage.getRobotID());
            jLabel16.setText(loginMessage.getRobotID() + "");
            jTextField4.setText(loginMessage.getRobotID() + "");
            //硬件版本
            jLabel36.setText(loginMessage.getHardvareVersion() + "");
            //软件版本
            jLabel42.setText(loginMessage.getSoftVersion() + "");
            //出厂日期
            jLabel38.setText((new Date(loginMessage.getChuChangDate())).toString().substring(0, 10));
            //最近维修
            jLabel44.setText((new Date(loginMessage.getZuiJinWeiXiuShiJian())).toString().substring(0, 10));
            //信号质量
            jLabel40.setText(loginMessage.getXinHaoZhiLiang() + "");
            //入侵检测
            jLabel46.setText(loginMessage.getRuQinJianCeCeShu() + "");
            //冷复位
            jLabel48.setText(loginMessage.getCoolReset() + "");
            //热复位
            jLabel50.setText(loginMessage.getHotReset() + "");

            showMessage("-->" + loginMessage.toString());
            //回复登陆数据包【此处进行登录的合法性检查！！】
            currentAGV.sendLoginOKMessage();
        }
        //6.收到的是配置信息回读
        if (robotMessage.getVelifyFunctionCode() == Robot2RCSMessageTypeConfig.ROBOT_RESPONSE_CONFIG_MESSAGE) {
            reponseConfigMessage.setMessage(robotMessage.getMessage());
            reponseConfigMessage.toObject();
            showMessage(reponseConfigMessage.toString());
            //--currentAGV.setID(heartBeatMessage.getRobotID());
            jLabel16.setText(reponseConfigMessage.getRobotID() + "");
            jTextField4.setText(reponseConfigMessage.getRobotID() + "");
            //行进速度
            StringBuilder builder = new StringBuilder();
            builder.append(reponseConfigMessage.getStraightSpeed1());
            builder.append(",");
            builder.append(reponseConfigMessage.getStraightSpeed2());
            builder.append(",");
            builder.append(reponseConfigMessage.getStraightSpeed3());
            builder.append(",");
            builder.append(reponseConfigMessage.getStraightSpeed4());
            builder.append(",");
            builder.append(reponseConfigMessage.getStraightSpeed5());
            jTextField1.setText(builder.toString());
            //转弯速度
            builder = new StringBuilder();
            builder.append(reponseConfigMessage.getCornerSpeed1());
            builder.append(",");
            builder.append(reponseConfigMessage.getCornerSpeed2());
            builder.append(",");
            builder.append(reponseConfigMessage.getCornerSpeed3());
            jTextField15.setText(builder.toString());
            //加减速度
            jTextField2.setText(reponseConfigMessage.getAcceleration() + "");
            jTextField16.setText(reponseConfigMessage.getDragAcceleration() + "");
            //X.PID
            jTextField6.setText(decimalFormat.format(reponseConfigMessage.getXP()));
            jTextField7.setText(decimalFormat.format(reponseConfigMessage.getXI()));
            jTextField8.setText(decimalFormat.format(reponseConfigMessage.getXD()));
            //THETA.PID
            jTextField9.setText(decimalFormat.format(reponseConfigMessage.getThetaP()));
            jTextField10.setText(decimalFormat.format(reponseConfigMessage.getThetaI()));
            jTextField11.setText(decimalFormat.format(reponseConfigMessage.getThetaD()));
        }
        //7.收到的动作命令包回复
        if (robotMessage.getVelifyFunctionCode() == Robot2RCSMessageTypeConfig.ROBOT_REPONSE_ACTION_COMMAND_MESSAGE) {
            actionCommandReponseMessage.setMessage(robotMessage.getMessage());
            actionCommandReponseMessage.toObject();
            RobotCommand actionCommand = actionCommandReponseMessage.getCommand();
            showMessage("####收到动作命令回复数据=" + actionCommand);
        }
    }

    /*
      显示报文消息
     */
    public void showDataMessage(String message) {
        try {
            StyledDocument doc1 = jTextPane1.getStyledDocument(); // 获得JTextPane的Document
            SimpleAttributeSet attrSet1 = new SimpleAttributeSet();
            StyleConstants.setFontFamily(attrSet1, "宋体");//字体
            StyleConstants.setFontSize(attrSet1, 15);//字体大小
            doc1.insertString(doc1.getLength(), message + "\r\n", attrSet1);
            jTextPane1.setCaretPosition(jTextPane1.getDocument().getLength());
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(ExceptionUtil.getMessage(e));
        }
    }

    /*
      显示日志消息
     */
    public void showLogMessage(String message) {
        try {
            StyledDocument doc3 = jTextPane3.getStyledDocument(); // 获得JTextPane的Document
            SimpleAttributeSet attrSet1 = new SimpleAttributeSet();
            StyleConstants.setFontFamily(attrSet1, "宋体");//字体
            StyleConstants.setFontSize(attrSet1, 15);//字体大小
            doc3.insertString(doc3.getLength(), "========================\r\n", attrSet1);
            doc3.insertString(doc3.getLength(), message + "\r\n", attrSet1);
            jTextPane3.setCaretPosition(jTextPane3.getDocument().getLength());
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(ExceptionUtil.getMessage(e));
        }
    }

    public void operation() {
        if (jButton15.getText().equals("开启服务")) {
            if (kivaServer == null) {
                kivaServer = ServerManager.getMessageServerInstance();
                kivaServer.registeReceiveDataMessageListener(this);
                RCSStatusService.getInstance().registerRCSStatusListener(this);
                AGVManager.getInstance().registeAGVListener(this);
                AGVTimeoutManager.getInstance().registeAGVTimeoutListener(this);
                rcsTimer.start();
                kivaServer.Begin();
                jButton15.setText("关闭服务");
            } else {
                kivaServer.Begin();
                jButton15.setText("关闭服务");
            }
        } else {//关闭
            if (kivaServer != null) {
                kivaServer.Stop();
                jButton15.setText("开启服务");
            }
        }
    }

    //消息解析
    public void analysisMessage(DataMessage tmpMessage) {

    }

    // 一般性提示信息
    public void ShowCommonInfo(String Info) {
        JOptionPane.showMessageDialog(this, Info, "提示",
                JOptionPane.INFORMATION_MESSAGE, null);
    }

    // 警告提示信息
    public void ShowWarningInfo(String Info) {
        JOptionPane.showMessageDialog(this, Info, "提示",
                JOptionPane.WARNING_MESSAGE, null);
    }

    // 出错提示信息
    public void ShowErrorInfo(String Info) {
        JOptionPane.showMessageDialog(this, Info, "提示",
                JOptionPane.ERROR_MESSAGE, null);
    }

    // 询问提示信息
    public int ShowAskInfo(String Info) {
        return JOptionPane.showOptionDialog(this, Info, "提示",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                null, null);
    }

    public void setGBL() {
        gbaglayout = new GridBagLayout();
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        jButton15 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel51 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel14 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jCheckBox4 = new javax.swing.JCheckBox();
        jTextField5 = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jPanel23 = new javax.swing.JPanel();
        jTextField21 = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jTextField22 = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jPanel17 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel30 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jPanel18 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jCheckBox3 = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        jPanel22 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jPanel8 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jButton9 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jTextField14 = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jButton13 = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane3 = new javax.swing.JTextPane();

        setBackground(new java.awt.Color(212, 226, 255));
        setLayout(new java.awt.GridBagLayout());

        jTabbedPane1.setBackground(new java.awt.Color(212, 226, 255));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        jPanel1.setBackground(new java.awt.Color(212, 226, 255));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel3.setBackground(new java.awt.Color(212, 226, 255));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "连接信息", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("sansserif", 0, 12))); // NOI18N
        jPanel9.setLayout(new java.awt.GridBagLayout());

        jLabel4.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel4.setText("目前连接车辆：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel9.add(jLabel4, gridBagConstraints);

        jLabel31.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 0, 51));
        jLabel31.setText("jLabel31");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel9.add(jLabel31, gridBagConstraints);

        jLabel35.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel35.setText("硬件版本：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel9.add(jLabel35, gridBagConstraints);

        jLabel36.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel36.setText("jLabel36");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel9.add(jLabel36, gridBagConstraints);

        jLabel37.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel37.setText("出厂日期：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanel9.add(jLabel37, gridBagConstraints);

        jLabel38.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel38.setText("jLabel38");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel9.add(jLabel38, gridBagConstraints);

        jLabel39.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel39.setText("信号质量：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        jPanel9.add(jLabel39, gridBagConstraints);

        jLabel40.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel40.setText("jLabel40");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel9.add(jLabel40, gridBagConstraints);

        jLabel41.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel41.setText("软件版本：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jPanel9.add(jLabel41, gridBagConstraints);

        jLabel42.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel42.setText("jLabel42");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel9.add(jLabel42, gridBagConstraints);

        jLabel43.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel43.setText("最近维修：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        jPanel9.add(jLabel43, gridBagConstraints);

        jLabel44.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel44.setText("jLabel44");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel9.add(jLabel44, gridBagConstraints);

        jLabel45.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel45.setText("入侵检测：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        jPanel9.add(jLabel45, gridBagConstraints);

        jLabel46.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel46.setText("jLabel46");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel9.add(jLabel46, gridBagConstraints);

        jLabel47.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel47.setText("冷复位：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        jPanel9.add(jLabel47, gridBagConstraints);

        jLabel48.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel48.setText("jLabel48");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel9.add(jLabel48, gridBagConstraints);

        jLabel49.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel49.setText("热复位：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        jPanel9.add(jLabel49, gridBagConstraints);

        jLabel50.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel50.setText("jLabel50");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel9.add(jLabel50, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel5.add(jPanel9, gridBagConstraints);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel10.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel1.setText("行进速度");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel10.add(jLabel1, gridBagConstraints);

        jTextField1.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jTextField1.setText("jTextField1");
        jTextField1.setMinimumSize(new java.awt.Dimension(88, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel10.add(jTextField1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel2.setText("加速度");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel10.add(jLabel2, gridBagConstraints);

        jTextField2.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jTextField2.setText("jTextField2");
        jTextField2.setMinimumSize(new java.awt.Dimension(88, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel10.add(jTextField2, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel6.setText("X_P");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanel10.add(jLabel6, gridBagConstraints);

        jTextField6.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jTextField6.setText("jTextField6");
        jTextField6.setMinimumSize(new java.awt.Dimension(88, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel10.add(jTextField6, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel7.setText("X_I");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        jPanel10.add(jLabel7, gridBagConstraints);

        jTextField7.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jTextField7.setText("jTextField7");
        jTextField7.setMinimumSize(new java.awt.Dimension(88, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel10.add(jTextField7, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel8.setText("X_D");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        jPanel10.add(jLabel8, gridBagConstraints);

        jTextField8.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jTextField8.setText("jTextField8");
        jTextField8.setMinimumSize(new java.awt.Dimension(88, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel10.add(jTextField8, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel9.setText("θ_P");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        jPanel10.add(jLabel9, gridBagConstraints);

        jLabel10.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel10.setText("θ_I");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        jPanel10.add(jLabel10, gridBagConstraints);

        jLabel11.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel11.setText("θ_D");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        jPanel10.add(jLabel11, gridBagConstraints);

        jTextField9.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jTextField9.setText("jTextField9");
        jTextField9.setMinimumSize(new java.awt.Dimension(88, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel10.add(jTextField9, gridBagConstraints);

        jTextField10.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jTextField10.setText("jTextField10");
        jTextField10.setMinimumSize(new java.awt.Dimension(88, 35));
        jTextField10.setPreferredSize(new java.awt.Dimension(88, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel10.add(jTextField10, gridBagConstraints);

        jTextField11.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jTextField11.setText("jTextField11");
        jTextField11.setMinimumSize(new java.awt.Dimension(88, 35));
        jTextField11.setPreferredSize(new java.awt.Dimension(88, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel10.add(jTextField11, gridBagConstraints);

        jLabel23.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel23.setText("转弯速度");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel10.add(jLabel23, gridBagConstraints);

        jTextField15.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jTextField15.setText("jTextField15");
        jTextField15.setMinimumSize(new java.awt.Dimension(88, 35));
        jTextField15.setPreferredSize(new java.awt.Dimension(88, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel10.add(jTextField15, gridBagConstraints);

        jLabel24.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel24.setText("减速度");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jPanel10.add(jLabel24, gridBagConstraints);

        jTextField16.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jTextField16.setText("jTextField16");
        jTextField16.setMinimumSize(new java.awt.Dimension(88, 35));
        jTextField16.setPreferredSize(new java.awt.Dimension(88, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel10.add(jTextField16, gridBagConstraints);

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setLayout(new java.awt.GridBagLayout());

        jButton3.setBackground(new java.awt.Color(201, 242, 255));
        jButton3.setText("回读");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        jPanel21.add(jButton3, gridBagConstraints);

        jButton14.setBackground(new java.awt.Color(201, 242, 255));
        jButton14.setText("写入");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel21.add(jButton14, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel10.add(jPanel21, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel5.add(jPanel10, gridBagConstraints);

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel20.setLayout(new java.awt.GridBagLayout());

        jButton15.setBackground(new java.awt.Color(201, 242, 255));
        jButton15.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jButton15.setText("开启服务");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel20.add(jButton15, gridBagConstraints);

        jLabel22.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 0, 51));
        jLabel22.setText("jLabel22");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel20.add(jLabel22, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel5.add(jPanel20, gridBagConstraints);

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel5.add(jPanel16, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanel5, gridBagConstraints);

        jPanel6.setBackground(new java.awt.Color(201, 242, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel6.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanel6, gridBagConstraints);

        jPanel7.setBackground(new java.awt.Color(201, 242, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        jPanel11.setBackground(new java.awt.Color(201, 242, 255));
        jPanel11.setLayout(new java.awt.GridBagLayout());

        jButton1.setBackground(new java.awt.Color(201, 242, 255));
        jButton1.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jButton1.setText("激活");
        jButton1.setMaximumSize(new java.awt.Dimension(180, 80));
        jButton1.setMinimumSize(new java.awt.Dimension(80, 30));
        jButton1.setPreferredSize(new java.awt.Dimension(80, 30));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel11.add(jButton1, gridBagConstraints);

        jLabel51.setText("车辆ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        jPanel11.add(jLabel51, gridBagConstraints);

        jTextField4.setText("jTextField4");
        jTextField4.setMinimumSize(new java.awt.Dimension(80, 30));
        jTextField4.setPreferredSize(new java.awt.Dimension(80, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel11.add(jTextField4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel7.add(jPanel11, gridBagConstraints);

        jPanel12.setBackground(new java.awt.Color(201, 242, 255));
        jPanel12.setLayout(new java.awt.GridBagLayout());

        jLabel12.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel12.setText("车辆ID：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel12.add(jLabel12, gridBagConstraints);

        jLabel13.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel13.setText("剩余电量：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jPanel12.add(jLabel13, gridBagConstraints);

        jLabel14.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel14.setText("货架ID：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel12.add(jLabel14, gridBagConstraints);

        jLabel15.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel15.setText("故障ID：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jPanel12.add(jLabel15, gridBagConstraints);

        jLabel16.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel16.setText("jLabel16");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel12.add(jLabel16, gridBagConstraints);

        jLabel17.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel17.setText("jLabel17");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel12.add(jLabel17, gridBagConstraints);

        jLabel18.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel18.setText("jLabel18");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel12.add(jLabel18, gridBagConstraints);

        jLabel19.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel19.setText("jLabel19");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel12.add(jLabel19, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel5.setText("电机温度：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jPanel12.add(jLabel5, gridBagConstraints);

        jLabel34.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel34.setText("jLabel34");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel12.add(jLabel34, gridBagConstraints);

        jLabel57.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel57.setText("AGV状态:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        jPanel12.add(jLabel57, gridBagConstraints);

        jLabel58.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(204, 0, 0));
        jLabel58.setText("jLabel58");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        jPanel12.add(jLabel58, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel7.add(jPanel12, gridBagConstraints);

        jPanel13.setBackground(new java.awt.Color(201, 242, 255));
        jPanel13.setLayout(new java.awt.GridBagLayout());

        jLabel20.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel20.setText("命令码:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel13.add(jLabel20, gridBagConstraints);

        jLabel21.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel21.setText("命令参数");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel13.add(jLabel21, gridBagConstraints);

        jTextField13.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jTextField13.setText("jTextField13");
        jTextField13.setMinimumSize(new java.awt.Dimension(88, 35));
        jTextField13.setPreferredSize(new java.awt.Dimension(88, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel13.add(jTextField13, gridBagConstraints);

        jButton4.setBackground(new java.awt.Color(201, 242, 255));
        jButton4.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jButton4.setText("发送");
        jButton4.setMaximumSize(new java.awt.Dimension(180, 80));
        jButton4.setMinimumSize(new java.awt.Dimension(80, 30));
        jButton4.setPreferredSize(new java.awt.Dimension(80, 30));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel13.add(jButton4, gridBagConstraints);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel13.add(jComboBox1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel7.add(jPanel13, gridBagConstraints);

        jPanel14.setBackground(new java.awt.Color(201, 242, 255));
        jPanel14.setLayout(new java.awt.GridBagLayout());

        jButton5.setBackground(new java.awt.Color(201, 242, 255));
        jButton5.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jButton5.setText("设定路径");
        jButton5.setMaximumSize(new java.awt.Dimension(180, 80));
        jButton5.setMinimumSize(new java.awt.Dimension(100, 30));
        jButton5.setPreferredSize(new java.awt.Dimension(100, 30));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel14.add(jButton5, gridBagConstraints);

        jButton6.setBackground(new java.awt.Color(201, 242, 255));
        jButton6.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jButton6.setText("清除路径");
        jButton6.setMaximumSize(new java.awt.Dimension(180, 80));
        jButton6.setMinimumSize(new java.awt.Dimension(100, 30));
        jButton6.setPreferredSize(new java.awt.Dimension(100, 30));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel14.add(jButton6, gridBagConstraints);

        jButton7.setBackground(new java.awt.Color(201, 242, 255));
        jButton7.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jButton7.setText("启动");
        jButton7.setMaximumSize(new java.awt.Dimension(180, 80));
        jButton7.setMinimumSize(new java.awt.Dimension(80, 30));
        jButton7.setPreferredSize(new java.awt.Dimension(80, 30));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel14.add(jButton7, gridBagConstraints);

        jButton8.setBackground(new java.awt.Color(201, 242, 255));
        jButton8.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jButton8.setText("停止");
        jButton8.setMaximumSize(new java.awt.Dimension(180, 80));
        jButton8.setMinimumSize(new java.awt.Dimension(80, 30));
        jButton8.setPreferredSize(new java.awt.Dimension(80, 30));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanel14.add(jButton8, gridBagConstraints);

        jCheckBox4.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jCheckBox4.setText("经过旋转区,旋转角度：");
        jCheckBox4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox4StateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        jPanel14.add(jCheckBox4, gridBagConstraints);

        jTextField5.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField5.setText("jTextField5");
        jTextField5.setMinimumSize(new java.awt.Dimension(80, 30));
        jTextField5.setPreferredSize(new java.awt.Dimension(80, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        jPanel14.add(jTextField5, gridBagConstraints);

        jLabel53.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jLabel53.setText("举升地址码：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel14.add(jLabel53, gridBagConstraints);

        jTextField20.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField20.setText("jTextField20");
        jTextField20.setMinimumSize(new java.awt.Dimension(80, 30));
        jTextField20.setPreferredSize(new java.awt.Dimension(80, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        jPanel14.add(jTextField20, gridBagConstraints);

        jPanel23.setLayout(new java.awt.GridBagLayout());

        jTextField21.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField21.setText("jTextField21");
        jTextField21.setMinimumSize(new java.awt.Dimension(80, 30));
        jTextField21.setPreferredSize(new java.awt.Dimension(80, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel23.add(jTextField21, gridBagConstraints);

        jLabel54.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jLabel54.setText("路径终点");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel23.add(jLabel54, gridBagConstraints);

        jTextField22.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField22.setText("jTextField22");
        jTextField22.setMinimumSize(new java.awt.Dimension(80, 30));
        jTextField22.setPreferredSize(new java.awt.Dimension(80, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel23.add(jTextField22, gridBagConstraints);

        jLabel55.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jLabel55.setText("路径起点");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        jPanel23.add(jLabel55, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel14.add(jPanel23, gridBagConstraints);

        jLabel56.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jLabel56.setText("降落地址码：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel14.add(jLabel56, gridBagConstraints);

        jTextField23.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField23.setText("jTextField23");
        jTextField23.setMinimumSize(new java.awt.Dimension(80, 30));
        jTextField23.setPreferredSize(new java.awt.Dimension(80, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        jPanel14.add(jTextField23, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel7.add(jPanel14, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        jPanel7.add(jSeparator2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        jPanel7.add(jSeparator3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(jSeparator5, gridBagConstraints);

        jPanel17.setBackground(new java.awt.Color(201, 242, 255));
        jPanel17.setLayout(new java.awt.GridBagLayout());

        jLabel25.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel25.setText("行");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel17.add(jLabel25, gridBagConstraints);

        jTextField17.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jTextField17.setText("jTextField17");
        jTextField17.setMinimumSize(new java.awt.Dimension(88, 35));
        jTextField17.setPreferredSize(new java.awt.Dimension(88, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel17.add(jTextField17, gridBagConstraints);

        jLabel26.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel26.setText("列");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel17.add(jLabel26, gridBagConstraints);

        jTextField18.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jTextField18.setText("jTextField18");
        jTextField18.setMinimumSize(new java.awt.Dimension(88, 35));
        jTextField18.setPreferredSize(new java.awt.Dimension(88, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel17.add(jTextField18, gridBagConstraints);

        jButton10.setBackground(new java.awt.Color(201, 242, 255));
        jButton10.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jButton10.setText("新地图");
        jButton10.setMaximumSize(new java.awt.Dimension(180, 300));
        jButton10.setMinimumSize(new java.awt.Dimension(110, 30));
        jButton10.setPreferredSize(new java.awt.Dimension(110, 30));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel17.add(jButton10, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel7.add(jPanel17, gridBagConstraints);

        jLabel27.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel27.setText("地图");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        jPanel7.add(jLabel27, gridBagConstraints);

        jLabel28.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel28.setText("路径设置");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        jPanel7.add(jLabel28, gridBagConstraints);

        jLabel29.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel29.setText("车辆信息");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel7.add(jLabel29, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(jSeparator6, gridBagConstraints);

        jLabel30.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel30.setText("手动命令");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        jPanel7.add(jLabel30, gridBagConstraints);

        jLabel32.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel32.setText("充电口");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        jPanel7.add(jLabel32, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(jSeparator7, gridBagConstraints);

        jPanel18.setBackground(new java.awt.Color(201, 242, 255));
        jPanel18.setLayout(new java.awt.GridBagLayout());

        jLabel33.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel33.setText("充电口");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel18.add(jLabel33, gridBagConstraints);

        jTextField19.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jTextField19.setText("jTextField19");
        jTextField19.setMinimumSize(new java.awt.Dimension(97, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel18.add(jTextField19, gridBagConstraints);

        jLabel59.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel59.setText("旋转角度");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel18.add(jLabel59, gridBagConstraints);

        jTextField12.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jTextField12.setText("jTextField12");
        jTextField12.setMinimumSize(new java.awt.Dimension(97, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel18.add(jTextField12, gridBagConstraints);

        jCheckBox3.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jCheckBox3.setText("美的");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        jPanel18.add(jCheckBox3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel7.add(jPanel18, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel3.setText("旋转区");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        jPanel7.add(jLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        jPanel7.add(jSeparator8, gridBagConstraints);

        jPanel22.setBackground(new java.awt.Color(201, 242, 255));
        jPanel22.setLayout(new java.awt.GridBagLayout());

        jLabel52.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jLabel52.setText("旋转区.地址码");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel22.add(jLabel52, gridBagConstraints);

        jTextField3.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jTextField3.setText("jTextField3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel22.add(jTextField3, gridBagConstraints);

        jButton2.setBackground(new java.awt.Color(201, 242, 255));
        jButton2.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jButton2.setText("设置旋转区");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        jPanel22.add(jButton2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel7.add(jPanel22, gridBagConstraints);

        jPanel15.setBackground(new java.awt.Color(201, 242, 255));
        jPanel15.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel7.add(jPanel15, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 4.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jPanel3, gridBagConstraints);

        jPanel4.setBackground(new java.awt.Color(212, 226, 255));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setViewportView(jTextPane1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(jScrollPane1, gridBagConstraints);

        jPanel8.setBackground(new java.awt.Color(189, 212, 255));
        jPanel8.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel8.add(jSeparator1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        jPanel8.add(jSeparator4, gridBagConstraints);

        jButton9.setBackground(new java.awt.Color(201, 242, 255));
        jButton9.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        jButton9.setText("清除");
        jButton9.setMaximumSize(new java.awt.Dimension(180, 80));
        jButton9.setMinimumSize(new java.awt.Dimension(80, 30));
        jButton9.setPreferredSize(new java.awt.Dimension(80, 30));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        jPanel8.add(jButton9, gridBagConstraints);

        jCheckBox1.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        jCheckBox1.setText("心跳显示");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel8.add(jCheckBox1, gridBagConstraints);

        jCheckBox2.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        jCheckBox2.setText("日志区");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel8.add(jCheckBox2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel4.add(jPanel8, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jPanel4, gridBagConstraints);

        jTabbedPane1.addTab("参数配置", jPanel1);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jTextField14.setText("jTextField14");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jTextField14, gridBagConstraints);

        jButton12.setText("发送");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel2.add(jButton12, gridBagConstraints);

        jScrollPane2.setViewportView(jTextPane2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jScrollPane2, gridBagConstraints);

        jButton13.setText("清空");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel2.add(jButton13, gridBagConstraints);

        jTabbedPane1.addTab("收发报文", jPanel2);

        jPanel19.setLayout(new java.awt.GridBagLayout());

        jScrollPane3.setViewportView(jTextPane3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel19.add(jScrollPane3, gridBagConstraints);

        jTabbedPane1.addTab("日志", jPanel19);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jTabbedPane1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        if (mapManager.getMapWindow().getMapModel() == MapWindow.VIEW_MODEL) {//设定路径状态
            if (this.ShowAskInfo("设定路径?") != 0) {
                return;
            }
            isPathOK = false;
            mapManager.getMapWindow().setMapModel(MapWindow.EDIT_MODEL);
            jCheckBox4.setEnabled(true);
            jTextField5.setEnabled(true);
            jTextField20.setEnabled(true);
            jTextField21.setEnabled(true);
            jTextField22.setEnabled(true);
            jTextField23.setEnabled(true);
            jButton5.setText("完成设定");
        } else {//完成路径设置
            if (this.ShowAskInfo("完成设定?") != 0) {
                return;
            }

            if (!pathTestMapIOManager.getSelectedCellPathList().isEmpty()) {
                LinkedList<Long> addressCodeIDList = new LinkedList();
                for (CellNode cellNode : pathTestMapIOManager.getSelectedCellPathList()) {
                    addressCodeIDList.add(cellNode.getAddressCodeID());
                }
                //起点终点是否设置
                long beginAddressCodeID, endAddressCodeID;
                try {
                    beginAddressCodeID = Long.parseLong(jTextField22.getText());
                    endAddressCodeID = Long.parseLong(jTextField21.getText());
                    boolean flag = true;
                    for (long b : addressCodeIDList) {
                        if (b == beginAddressCodeID) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        ShowErrorInfo("输入的路径起点不在路径串中!");
                        return;
                    }
                    flag = true;
                    for (long b : addressCodeIDList) {
                        if (b == endAddressCodeID) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        ShowErrorInfo("输入的路径终点不在路径串中!");
                        return;
                    }
                } catch (Exception e) {
                    ShowErrorInfo("路径起点终点不合法!");
                    LOG.error("路径起点终点不合法:\n"+ExceptionUtil.getMessage(e));
                    return;
                }
                //判断路径串是否合法
                LinkedList<CellNode> tmpCellNodeList = new LinkedList();
                for (long v : addressCodeIDList) {
                    CellNode tmpCellNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(v);
                    if (tmpCellNode == null) {
                        ShowErrorInfo("设置的路径不合法（CELL不存在）!");
                        return;
                    }
                    tmpCellNodeList.addLast(tmpCellNode);
                }

                CellNode beginCellNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(beginAddressCodeID);
                CellNode endCellNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(endAddressCodeID);
                long upPodAddressCodeID = 0;
                long downPodAddressCodeID = 0;
                try {
                    upPodAddressCodeID = Long.parseLong(jTextField20.getText());
                    downPodAddressCodeID = Long.parseLong(jTextField23.getText());
                    boolean flag = true;
                    if (upPodAddressCodeID > 0) {
                        for (long b : addressCodeIDList) {
                            if (b == upPodAddressCodeID) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            ShowErrorInfo("输入的举升地址码不在路径串中!");
                            return;
                        }
                    }
                    if (downPodAddressCodeID > 0) {
                        flag = true;
                        for (long b : addressCodeIDList) {
                            if (b == downPodAddressCodeID) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            ShowErrorInfo("输入的降落地址码不在路径串中!");
                            return;
                        }
                    }
                } catch (Exception e) {
                    ShowErrorInfo("举升或降落地址码输入的不合法!");
                    LOG.error("举升或降落地址码输入的不合法:\n"+ExceptionUtil.getMessage(e));
                    return;
                }
                if (!jCheckBox4.isSelected()) {
                    LinkedList<CellNode> rsList = (LinkedList<CellNode>) RotatePathManager.getInstance().getSortBunchListByListBeginEndRotateArea(tmpCellNodeList, beginCellNode, endCellNode, null);
                    if (rsList == null) {
                        ShowErrorInfo("设置的路径不合法!");
                        return;
                    }
                    LinkedList<Long> rsAddressCodeIDList = new LinkedList();
                    for (CellNode v : rsList) {
                        rsAddressCodeIDList.addLast(v.getAddressCodeID());
                    }
                    if (!beginCellNode.equals(rsList.getFirst())
                            || !endCellNode.equals(rsList.getLast())) {
                        ShowErrorInfo("起点和终点不在规划路径中!");
                        return;
                    }
                    //-----------充电->条件:充电口不为0----------------
                    long chargeNode = 0;
                    int angle = 0;
                    boolean isMedia = false;
                    if (!"".equals(jTextField19.getText())) {
                        chargeNode = Long.parseLong(jTextField19.getText());
                        angle = Integer.parseInt(jTextField12.getText());
                        isMedia = jCheckBox3.isSelected();
                    }
                    if (chargeNode != 0) {
                        if (isMedia) {
                            //美的充电路径测试
//                            wcsSeriesPath = new WCSMediaChargeSeriesPath(chargeNode, angle, rsAddressCodeIDList);
                            String aa = "0013A2004166E894";
                            wcsSeriesPath = new WCSMediaChargeSeriesPath(angle, rsAddressCodeIDList, aa);
                        } else {
                            //mushiny充电路径测试
                            wcsSeriesPath = new WCSChargeSeriesPath(chargeNode, angle, rsAddressCodeIDList);
                        }
                    } else {
                        //测试扫描podID的路径
//                        wcsSeriesPath = new WCSScanPodIdPath(rsAddressCodeIDList);


                       /* rsAddressCodeIDList = new LinkedList<>();
                        rsAddressCodeIDList.add(1L);
                        rsAddressCodeIDList.add(2L);
                        rsAddressCodeIDList.add(3L);
                        rsAddressCodeIDList.add(2L);
                        rsAddressCodeIDList.add(1L);*/


                        wcsSeriesPath = new WCSSeriesPath(upPodAddressCodeID, downPodAddressCodeID, false, 0, rsAddressCodeIDList);
                    }

                    if (!wcsSeriesPath.checkWCSSeriesPath()) {
                        ShowErrorInfo("非法路径!");
                        return;
                    }
                    System.out.println("WCS路径串=" + wcsSeriesPath.toString());
                } else {
                    try {
                        int theta = Integer.parseInt(jTextField5.getText());
                        if (theta != 0 && theta != 90 && theta != 180 && theta != 270) {
                            ShowErrorInfo("经过旋转区，旋转角度不合法!");
                            return;
                        }
                        if (ra == null) {
                            ShowErrorInfo("没有设置旋转区!");
                            return;
                        }
                        LinkedList<CellNode> rsList = (LinkedList<CellNode>) RotatePathManager.getInstance().getSortBunchListByListBeginEndRotateArea(tmpCellNodeList, beginCellNode, endCellNode, ra);
                        if (rsList == null) {
                            ShowErrorInfo("设置的路径不合法!");
                            return;
                        }
                        LinkedList<Long> rsAddressCodeIDList = new LinkedList();
                        for (CellNode v : rsList) {
                            rsAddressCodeIDList.addLast(v.getAddressCodeID());
                        }
                        if (!beginCellNode.equals(rsList.getFirst())
                                || !endCellNode.equals(rsList.getLast())) {
                            ShowErrorInfo("起点和终点不在规划路径中!");
                            return;
                        }
                        wcsSeriesPath = new WCSSeriesPath(upPodAddressCodeID, 0, true, theta, rsAddressCodeIDList);
                        if (!wcsSeriesPath.checkWCSSeriesPath()) {
                            ShowErrorInfo("非法路径!");
                            return;
                        }
                        LOG.info("WCS路径串=" + wcsSeriesPath.toString());
                    } catch (Exception e) {
                        ShowErrorInfo("经过旋转区，举升POD地址码或旋转角度不合法!");
                        LOG.error("经过旋转区，举升POD地址码或旋转角度不合法:\n"+ExceptionUtil.getMessage(e));
                        return;
                    }
                }
                isPathOK = true;
            }
            mapManager.getMapWindow().setMapModel(MapWindow.VIEW_MODEL);
            jCheckBox4.setEnabled(false);
            jTextField5.setEnabled(false);
            jTextField20.setEnabled(false);
            jTextField21.setEnabled(false);
            jTextField22.setEnabled(false);
            jTextField23.setEnabled(false);
            jButton5.setText("设定路径");
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        if (ShowAskInfo("生成新的地图？") != 0) {
            return;
        }
        try {
            Integer.parseInt(jTextField17.getText());
            Integer.parseInt(jTextField18.getText());
        } catch (Exception e) {
            ShowErrorInfo("错误的路径行或列!");
            LOG.error("错误的路径行或列:\n"+ExceptionUtil.getMessage(e));
            return;
        }
        mapManager.showTip(10, 30, "正在形成地图....");
        kivaMap = new KivaMap(Integer.parseInt(jTextField17.getText()), Integer.parseInt(jTextField18.getText()));
        kivaMap.initKivaMap();
        mapManager.installMap(kivaMap);
        mapManager.loadingMap();

//        pathViewPathApplication = new PathViewApplication(kivaMap);
//        pathViewPathApplication.visible();

    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        if (mapManager.getMapWindow().getMapModel() == MapWindow.EDIT_MODEL) {//只有在“设定路径”模式才能清楚路径
            if (this.ShowAskInfo("是否清除路径?") != 0) {
                return;
            }
            pathTestMapIOManager.clearPath();
            isPathOK = false;
        } else {
            ShowWarningInfo("只能在路径设置状态才能清除路径！");
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        short actionParameter;
        Object selectedObj = jComboBox1.getSelectedItem();
        if (selectedObj instanceof String) {
            this.ShowWarningInfo("命令码不合法！");
            return;
        }
        RobotCommand robotCommand = (RobotCommand) selectedObj;
        if (robotCommand instanceof AbstractRotateCommand) {
            try {
                actionParameter = Short.parseShort(jTextField13.getText());
                robotCommand.setCommandParameter(actionParameter);
            } catch (Exception e) {
                this.ShowWarningInfo("输入的命令参数不合法！");
                LOG.error("输入的命令参数不合法:\n"+ExceptionUtil.getMessage(e));
                return;
            }
        }

        if (kivaServer == null) {
            this.ShowWarningInfo("服务没有启动！");
            return;
        }
        if (!kivaServer.isRun()) {
            this.ShowWarningInfo("服务没有启动！");
        }

        if (currentAGV == null) {
            this.ShowWarningInfo("无连接车辆！");
            return;
        }
        //----------------------发送命令码时,需要对AGV进行判定--------------
        //1.如果发送的是,0xf0停到最近二维码,0xf1急停,则AGV必须是任务状态
        if (robotCommand instanceof StopByNearCodeCommand
                || robotCommand instanceof StopImmediatelyCommand) {
            if (currentAGV.getAGVStatus() != AGVConfig.AGV_STATUS_TASKED) {
                this.ShowWarningInfo(robotCommand + " 执行时,AGV必须处于任务状态!");
                return;
            }
        }
        //2. 如果处于任务状态,则不能执行旋转动作\顶升\下降\开始睡眠
        if (robotCommand instanceof AbstractRotateCommand
                || robotCommand instanceof BeginSleepCommand
                || robotCommand instanceof UpCommand
                || robotCommand instanceof DownCommand) {
            if (currentAGV.getAGVStatus() == AGVConfig.AGV_STATUS_TASKED) {
                this.ShowWarningInfo(robotCommand + " 执行时,AGV必须处于任务状态!");
                return;
            }
        }
        //3.如果是启动,则AGV必须处于"停到最近二维码"
        if (robotCommand instanceof StartCommand) {
            if (currentAGV.getAGVStatus() != AGVConfig.AGV_STATUS_STOP_NEAR_CODE) {
                this.ShowWarningInfo(robotCommand + " 执行时,AGV状态必须处于'停到最近二维码'!");
                return;
            }
        }

        //-----------------下发命令
        //启动
        if (robotCommand instanceof StartCommand) {
            currentAGV.startCommand();
        }
        //停到最近二维码
        if (robotCommand instanceof StopByNearCodeCommand) {
            currentAGV.stopNearCodeCommand();
        }
        //急停
        if (robotCommand instanceof StopImmediatelyCommand) {
            currentAGV.stopImmediatelyCommand();
        }
        //所有电机供电断电
        if (robotCommand instanceof StopMotoPowerCommand) {
            currentAGV.stopMotoPowerCommand();
        }
        //开始休眠
        if (robotCommand instanceof BeginSleepCommand) {
            currentAGV.beginSleepCommand();
        }
        //结束休眠
        if (robotCommand instanceof StopSleepCommand) {
            currentAGV.stopSleepCommand();
        }
        //清除已下发AGV路径缓冲
        if (robotCommand instanceof ClearPathCommand) {
            currentAGV.clearAGVBufferPathCommand();
        }
        //顶升,下降,旋转,清除已下发路径
        if (robotCommand instanceof UpCommand
                || robotCommand instanceof DownCommand
                || robotCommand instanceof AbstractRotateCommand) {
            currentAGV.sendActionCommand(robotCommand);
        }

        this.ShowCommonInfo("发送命令成功！");

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        operation();
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        if (kivaServer == null) {
            this.ShowWarningInfo("服务没有启动！");
            return;
        }
        if (!kivaServer.isRun()) {
            this.ShowWarningInfo("服务没有启动！");
        }

        if (currentAGV == null) {
            this.ShowWarningInfo("无连接车辆！");
            return;
        }
        long robotID = 0;
        if (currentAGV.getID() <= 0) {
            try {
                robotID = Long.parseLong(jTextField4.getText());
            } catch (Exception e) {
                this.ShowWarningInfo("输入的激活车辆ID不合法！");
                LOG.error("输入的激活车辆ID不合法:\n"+ExceptionUtil.getMessage(e));
                return;
            }
        }
        RCS2RobotActiveMessage message = new RCS2RobotActiveMessage(robotID);
        message.toMessage();
        currentAGV.getSession().write(message.getMessage());
        showLogMessage("<<==发送激活码:" + HexBinaryUtil.byteArrayToHexString2((byte[]) message.getMessage()));
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        jTextPane1.setText("");
        messageCount = 0;

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if (currentAGV == null) {
            this.ShowWarningInfo("无连接的车辆！");
            return;
        }
        if (currentAGV.getID() <= 0) {
            this.ShowWarningInfo("无车辆ID！(车辆没有激活？)");
            return;
        }
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField6.setText("");
        jTextField7.setText("");
        jTextField8.setText("");
        jTextField9.setText("");
        jTextField10.setText("");
        jTextField11.setText("");
        jTextField15.setText("");
        jTextField16.setText("");
        RCSRequestConfigMessage requestConfigMessage = new RCSRequestConfigMessage(currentAGV.getID());
        requestConfigMessage.toMessage();
        currentAGV.getSession().write(requestConfigMessage.getMessage());
        showLogMessage(Global.LINE);
        showLogMessage("<<==发送配置信息request:" + HexBinaryUtil.byteArrayToHexString2((byte[]) requestConfigMessage.getMessage()));
        this.ShowCommonInfo("已发送回读命令！");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        if (currentAGV == null) {
            this.ShowWarningInfo("无连接的车辆！");
            return;
        }
        RCSWriteConfigMessage configMessage = new RCSWriteConfigMessage(currentAGV.getID());
        //速度档位
        if (jTextField1.getText().isEmpty()) {
            this.ShowWarningInfo("行进速度档位不合法！");
            return;
        }
        String[] straightSpeeds = jTextField1.getText().split(",");
        if (straightSpeeds.length != 5) {
            this.ShowWarningInfo("行进速度档位是5档！");
            return;
        }
        for (String s : straightSpeeds) {
            try {
                Short.parseShort(s);
            } catch (Exception e) {
                this.ShowWarningInfo("行进速度档位不合法！");
                LOG.error("行进速度档位不合法:\n"+ExceptionUtil.getMessage(e));
                return;
            }
        }
        configMessage.setStraightSpeed1(Short.parseShort(straightSpeeds[0]));
        configMessage.setStraightSpeed2(Short.parseShort(straightSpeeds[1]));
        configMessage.setStraightSpeed3(Short.parseShort(straightSpeeds[2]));
        configMessage.setStraightSpeed4(Short.parseShort(straightSpeeds[3]));
        configMessage.setStraightSpeed5(Short.parseShort(straightSpeeds[4]));
        //转弯速度
        if (jTextField15.getText().isEmpty()) {
            this.ShowWarningInfo("转弯速度档位不合法！");
            return;
        }
        String[] cornerSpeeds = jTextField15.getText().split(",");
        if (cornerSpeeds.length != 3) {
            this.ShowWarningInfo("转弯速度档位数量不合法！");
            return;
        }
        for (String s : cornerSpeeds) {
            try {
                Short.parseShort(s);
            } catch (Exception e) {
                this.ShowWarningInfo("转弯速度档位不合法 !");
                LOG.error("转弯速度档位不合法:\n"+ExceptionUtil.getMessage(e));
                return;
            }
        }
        configMessage.setCornerSpeed1(Short.parseShort(cornerSpeeds[0]));
        configMessage.setCornerSpeed2(Short.parseShort(cornerSpeeds[1]));
        configMessage.setCornerSpeed3(Short.parseShort(cornerSpeeds[2]));
        //加减速度
        try {
            configMessage.setAcceleration(Short.parseShort(jTextField2.getText()));
        } catch (Exception e) {
            this.ShowWarningInfo("加速度不合法!");
            LOG.error("加速度不合法:\n"+ExceptionUtil.getMessage(e));
            return;
        }
        try {
            configMessage.setDragAcceleration(Short.parseShort(jTextField16.getText()));
        } catch (Exception e) {
            this.ShowWarningInfo("减速度不合法!");
            LOG.error("减速度不合法:\n"+ExceptionUtil.getMessage(e));
            return;
        }
        //x.pid
        try {
            configMessage.setXP(Float.parseFloat(jTextField6.getText()));
            configMessage.setXI(Float.parseFloat(jTextField7.getText()));
            configMessage.setXD(Float.parseFloat(jTextField8.getText()));
        } catch (Exception e) {
            this.ShowWarningInfo("X PID 数值不合法！");
            LOG.error("X PID 数值不合法:\n"+ExceptionUtil.getMessage(e));
            return;
        }
        //theta.pid
        try {
            configMessage.setThetaP(Float.parseFloat(jTextField9.getText()));
            configMessage.setThetaI(Float.parseFloat(jTextField10.getText()));
            configMessage.setThetaD(Float.parseFloat(jTextField11.getText()));
        } catch (Exception e) {
            this.ShowWarningInfo("Theta PID 数值不合法！");
            LOG.error("Theta PID 数值不合法:\n"+ExceptionUtil.getMessage(e));
            return;
        }
        configMessage.toMessage();
        currentAGV.sendMessageToAGV(configMessage);
        showLogMessage(Global.LINE);
        showLogMessage("写入配置信息:" + HexBinaryUtil.byteArrayToHexString2((byte[]) configMessage.getMessage()));
        this.ShowCommonInfo("已发送写入命令！");
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:

        if (currentAGV == null) {
            this.ShowWarningInfo("无AGV连接！");
            return;
        }
        if (currentAGV.getID() <= 0) {
            this.ShowWarningInfo("AGV需要登录！(发送激活包)");
            return;
        }
        if (!isPathOK) {
            this.ShowWarningInfo("路径没有设置完成！");
            return;
        }

        if (wcsSeriesPath == null) {
            this.ShowWarningInfo("空路径！");
            return;
        }

        if (ShowAskInfo("按照指定的路径启动AGV？") != 0) {
            return;
        }
        pathTestTask = new PathTestTask(currentAGV, wcsSeriesPath, false);
        pathTestTask.start();

        isRunTestPath = true;
        this.ShowCommonInfo("启动成功！");
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:

        if (pathTestTask == null || !isRunTestPath) {
            this.ShowWarningInfo("无运行的测试！");
            return;
        }
        if (ShowAskInfo("停止AGV路径测试？") != 0) {
            return;
        }
        pathTestTask.setStop(true);
        isRunTestPath = false;
        pathTestTask = null;
        this.ShowCommonInfo("已停止路径测试！");
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if (kivaMap == null) {
            this.ShowWarningInfo("必须先生成地图！");
            return;
        }
        String[] rotateAreaAddressCodeIDStr = jTextField3.getText().split(",");
        if (rotateAreaAddressCodeIDStr.length != 4) {
            this.ShowWarningInfo("旋转区地址码数量不合法！");
            return;
        }
        long[] rotateAreaAddressCodeID = new long[4];
        int i = 0;
        for (String s : rotateAreaAddressCodeIDStr) {
            try {
                rotateAreaAddressCodeID[i] = Long.parseLong(s);
                i++;
            } catch (Exception e) {
                this.ShowWarningInfo("旋转区地址码不合法！");
                LOG.error("旋转区地址码不合法:\n"+ExceptionUtil.getMessage(e));
                return;
            }
        }
        if (ra != null) {//旧的旋转区
            kivaMap.unInstallRotateArea(ra);
        }
        CellNode leftUpCellNode = kivaMap.getMapCellByAddressCodeID(rotateAreaAddressCodeID[0]);
        CellNode rightUpCellNode = kivaMap.getMapCellByAddressCodeID(rotateAreaAddressCodeID[1]);
        CellNode rightDownCellNode = kivaMap.getMapCellByAddressCodeID(rotateAreaAddressCodeID[2]);
        CellNode leftDownCellNode = kivaMap.getMapCellByAddressCodeID(rotateAreaAddressCodeID[3]);
        LinkedList<RotateArea> rotateAreaList = new LinkedList();
        ra = new RotateArea(1L, leftUpCellNode, rightUpCellNode, rightDownCellNode, leftDownCellNode);
        if (!ra.isValidate()) {
            this.ShowWarningInfo("旋转区不合法！");
            return;
        }
        rotateAreaList.addLast(ra);
        kivaMap.installRotateArea(rotateAreaList);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCheckBox4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox4StateChanged
        // TODO add your handling code here:
        jTextField5.setEnabled(jCheckBox4.isSelected());

    }//GEN-LAST:event_jCheckBox4StateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JTextPane jTextPane3;
    // End of variables declaration//GEN-END:variables
}
