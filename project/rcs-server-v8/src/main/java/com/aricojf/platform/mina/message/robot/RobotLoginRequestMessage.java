/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message.robot;

import com.mushiny.rcs.global.KivaBusConfig;
import com.mushiny.rcs.global.Robot2RCSMessageTypeConfig;
import java.util.Date;

/**
 * 车辆登录数据包
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RobotLoginRequestMessage extends RobotMessage {

    private long password;//登录密码，
    private short robotType;//车型
    private short hardvareVersion;//硬件版本
    private short softVersion;//软件版本
    private long chuChangDate;//出厂日期
    private long leiJiShiChang;//累计时长
    private long zuiJinWeiXiuShiJian;//最近维修时间
    private int shengYuDianLiang;//剩余电量
    private long addressCodeID;//地址码ID
    private short addressCodeInfoX;//地址码信息，X坐标，2字节有符号整数
    private short addressCodeInfoY;//地址码信息，Y坐标，2字节有符号整数
    private float addressCodeInfoTheta;//地址码信息，θ角度，为4字节单精度浮点型
    private long podCodeID;//货架码ID
    private short podCodeInfoX;//货架码信息，X坐标，2字节有符号整数
    private short podCodeInfoY;//货架码信息，Y坐标，2字节有符号整数
    private float podCodeInfoTheta;//货架码信息，θ角度，为4字节单精度浮点型
    private long selectedPodCodeID;//绑定货架ID
    private short juShengZhuangTai;//举升状态
    private short chongDianZhuangTai;//充电状态
    private int errorID;//故障ID
    private short xinHaoZhiLiang;//信号质量
    private int ruQinJianCeCeShu;//入侵检测次数
    private int coolReset;//冷复位次数
    private int hotReset;//热复位次数
    public RobotLoginRequestMessage(){
        this(-1);
    }
    public RobotLoginRequestMessage(long robotID) {
        super();
        this.robotID = robotID;
        this.messageBodyLength=64;
        this.frameDataLength = 73;
        this.functionWordCode = Robot2RCSMessageTypeConfig.ROBOT_LOGIN_REQUEST_MESSAGE;
         this.head = KivaBusConfig.ROBOT2RCS_HEAD;
    }

    public void toMessage() {
        byte[] tmpMessageBody = new byte[messageBodyLength];        
        //登录密码
        tmpMessageBody[0] = (byte) ((getPassword()) & 0xff);
        tmpMessageBody[1] = (byte) ((getPassword() >> 8) & 0xff);
        tmpMessageBody[2] = (byte) ((getPassword() >> 16) & 0xff);
        tmpMessageBody[3] = (byte) ((getPassword() >> 24) & 0xff);
        //车型
        tmpMessageBody[4] = (byte) ((getRobotType()) & 0xff);
        //硬件版本
        tmpMessageBody[5] = (byte) ((getHardvareVersion()) & 0xff);
        //软件版本
        tmpMessageBody[6] = (byte) ((getSoftVersion()) & 0xff);
        //出厂日期
        tmpMessageBody[7] = (byte) ((getChuChangDate()) & 0xff);
        tmpMessageBody[8] = (byte) ((getChuChangDate() >> 8) & 0xff);
        tmpMessageBody[9] = (byte) ((getChuChangDate() >> 16) & 0xff);
        tmpMessageBody[10] = (byte) ((getChuChangDate() >> 24) & 0xff);
        //累计时长
        tmpMessageBody[11] = (byte) ((getLeiJiShiChang()) & 0xff);
        tmpMessageBody[12] = (byte) ((getLeiJiShiChang() >> 8) & 0xff);
        tmpMessageBody[13] = (byte) ((getLeiJiShiChang() >> 16) & 0xff);
        tmpMessageBody[14] = (byte) ((getLeiJiShiChang() >> 24) & 0xff);
        //最近维修时间
        tmpMessageBody[15] = (byte) ((getZuiJinWeiXiuShiJian()) & 0xff);
        tmpMessageBody[16] = (byte) ((getZuiJinWeiXiuShiJian() >> 8) & 0xff);
        tmpMessageBody[17] = (byte) ((getZuiJinWeiXiuShiJian() >> 16) & 0xff);
        tmpMessageBody[18] = (byte) ((getZuiJinWeiXiuShiJian() >> 24) & 0xff);
        //剩余电量
        tmpMessageBody[19] = (byte) ((getShengYuDianLiang()) & 0xff);
        tmpMessageBody[20] = (byte) ((getShengYuDianLiang() >> 8) & 0xff);
        //地址码ID
        tmpMessageBody[21] = (byte) ((getAddressCodeID()) & 0xff);
        tmpMessageBody[22] = (byte) ((getAddressCodeID() >> 8) & 0xff);
        tmpMessageBody[23] = (byte) ((getAddressCodeID() >> 16) & 0xff);
        tmpMessageBody[24] = (byte) ((getAddressCodeID() >> 24) & 0xff);
        //地址码信息，X
        tmpMessageBody[25] = (byte) ((getAddressCodeInfoX()) & 0xff);
        tmpMessageBody[26] = (byte) ((getAddressCodeInfoX() >> 8) & 0xff);
        //地址码信息，y
        tmpMessageBody[27] = (byte) ((getAddressCodeInfoY()) & 0xff);
        tmpMessageBody[28] = (byte) ((getAddressCodeInfoY() >> 8) & 0xff);
        //地址码信息，θ角度
        tmpMessageBody[29] = (byte) ((Float.floatToIntBits(getAddressCodeInfoTheta())) & 0xff);
        tmpMessageBody[30] = (byte) ((Float.floatToIntBits(getAddressCodeInfoTheta()) >> 8) & 0xff);
        tmpMessageBody[31] = (byte) ((Float.floatToIntBits(getAddressCodeInfoTheta()) >> 16) & 0xff);
        tmpMessageBody[32] = (byte) ((Float.floatToIntBits(getAddressCodeInfoTheta()) >> 24) & 0xff);
        //货架码ID
        tmpMessageBody[33] = (byte) ((getPodCodeID()) & 0xff);
        tmpMessageBody[34] = (byte) ((getPodCodeID() >> 8) & 0xff);
        tmpMessageBody[35] = (byte) ((getPodCodeID() >> 16) & 0xff);
        tmpMessageBody[36] = (byte) ((getPodCodeID() >> 24) & 0xff);
        //货架码信息，X
        tmpMessageBody[37] = (byte) ((getPodCodeInfoX()) & 0xff);
        tmpMessageBody[38] = (byte) ((getPodCodeInfoX() >> 8) & 0xff);
        //货架码信息，y
        tmpMessageBody[39] = (byte) ((getPodCodeInfoY()) & 0xff);
        tmpMessageBody[40] = (byte) ((getPodCodeInfoY() >> 8) & 0xff);
        //货架码信息，θ角度
        tmpMessageBody[41] = (byte) ((Float.floatToIntBits(getPodCodeInfoTheta())) & 0xff);
        tmpMessageBody[42] = (byte) ((Float.floatToIntBits(getPodCodeInfoTheta()) >> 8) & 0xff);
        tmpMessageBody[43] = (byte) ((Float.floatToIntBits(getPodCodeInfoTheta()) >> 16) & 0xff);
        tmpMessageBody[44] = (byte) ((Float.floatToIntBits(getPodCodeInfoTheta()) >> 24) & 0xff);
        //绑定货架ID
        tmpMessageBody[45] = (byte) ((getSelectedPodCodeID()) & 0xff);
        tmpMessageBody[46] = (byte) ((getSelectedPodCodeID() >> 8) & 0xff);
        tmpMessageBody[47] = (byte) ((getSelectedPodCodeID() >> 16) & 0xff);
        tmpMessageBody[48] = (byte) ((getSelectedPodCodeID() >> 24) & 0xff);
        //举升状态
        tmpMessageBody[49] = (byte) ((getJuShengZhuangTai()) & 0xff);
        //充电状态
        tmpMessageBody[50] = (byte) ((getChongDianZhuangTai()) & 0xff);
        //故障ID
        tmpMessageBody[51] = (byte) ((getErrorID()) & 0xff);
        tmpMessageBody[52] = (byte) ((getErrorID() >> 8) & 0xff);
        //信号质量
        tmpMessageBody[53] = (byte) ((getXinHaoZhiLiang()) & 0xff);
        //入侵检测次数
        tmpMessageBody[54] = (byte) ((getRuQinJianCeCeShu()) & 0xff);
        tmpMessageBody[55] = (byte) ((getRuQinJianCeCeShu()) & 0xff);
        //冷复位次数
        tmpMessageBody[56] = (byte) ((getCoolReset()) & 0xff);
        tmpMessageBody[57] = (byte) ((getCoolReset()) & 0xff);
        //热复位次数
        tmpMessageBody[58] = (byte) ((getHotReset()) & 0xff);
        tmpMessageBody[59] = (byte) ((getHotReset()) & 0xff);
        //保留1
        tmpMessageBody[60] = (byte) (0);
        tmpMessageBody[61] = (byte) (0);
        //保留2
        tmpMessageBody[62] = (byte) (0);
        tmpMessageBody[63] = (byte) (0);
        setMessageBody(tmpMessageBody);
        super.toMessage();
    }

    public void toObject() {
        byte[] messageBytes = (byte[]) getMessage();
        super.toObject();
        //登录密码
        password = messageBytes[15];
        password = (long) ((password << 8) | (messageBytes[14] & 0xff));
        password = (long) ((password << 8) | (messageBytes[13] & 0xff));
        password = (long) ((password << 8) | (messageBytes[12] & 0xff));
        //车型
        robotType = messageBytes[16];
        //硬件版本
        hardvareVersion = messageBytes[17];
        //软件版本
        softVersion = messageBytes[18];
        //出厂日期
        chuChangDate = messageBytes[22];
        chuChangDate = (long) ((chuChangDate << 8) | (messageBytes[21] & 0xff));
        chuChangDate = (long) ((chuChangDate << 8) | (messageBytes[20] & 0xff));
        chuChangDate = (long) ((chuChangDate << 8) | (messageBytes[19] & 0xff));
        //累计时长
        leiJiShiChang = messageBytes[26];
        leiJiShiChang = (long) ((leiJiShiChang << 8) | (messageBytes[25] & 0xff));
        leiJiShiChang = (long) ((leiJiShiChang << 8) | (messageBytes[24] & 0xff));
        leiJiShiChang = (long) ((leiJiShiChang << 8) | (messageBytes[23] & 0xff));
        //最近维修时间
        zuiJinWeiXiuShiJian = messageBytes[30];
        zuiJinWeiXiuShiJian = (long) ((zuiJinWeiXiuShiJian << 8) | (messageBytes[29] & 0xff));
        zuiJinWeiXiuShiJian = (long) ((zuiJinWeiXiuShiJian << 8) | (messageBytes[28] & 0xff));
        zuiJinWeiXiuShiJian = (long) ((zuiJinWeiXiuShiJian << 8) | (messageBytes[27] & 0xff));
        //剩余电量
        shengYuDianLiang = messageBytes[32];
        shengYuDianLiang = (int) ((shengYuDianLiang << 8) | (messageBytes[31] & 0xff));
        //地址码ID
        addressCodeID = messageBytes[36];
        addressCodeID = (long) ((addressCodeID << 8) | (messageBytes[35] & 0xff));
        addressCodeID = (long) ((addressCodeID << 8) | (messageBytes[34] & 0xff));
        addressCodeID = (long) ((addressCodeID << 8) | (messageBytes[33] & 0xff));
        //地址码信息X
        addressCodeInfoX = messageBytes[38];
        addressCodeInfoX = (short) ((addressCodeInfoX << 8) | (messageBytes[37] & 0xff));
        //地址码信息Y
        addressCodeInfoY = messageBytes[40];
        addressCodeInfoY = (short) ((addressCodeInfoY << 8) | (messageBytes[39] & 0x00ff));
        //地址码θ
        int theta = 0;
        theta = messageBytes[44];
        theta = (int) ((theta << 8) | (messageBytes[43] & 0xff));
        theta = (int) ((theta << 8) | (messageBytes[42] & 0xff));
        theta = (int) ((theta << 8) | (messageBytes[41] & 0xff));
        addressCodeInfoTheta = Float.intBitsToFloat(theta);

        //货架码ID
        podCodeID = messageBytes[48];
        podCodeID = (long) ((podCodeID << 8) | (messageBytes[47] & 0xff));
        podCodeID = (long) ((podCodeID << 8) | (messageBytes[46] & 0xff));
        podCodeID = (long) ((podCodeID << 8) | (messageBytes[45] & 0xff));
        //地址码信息X
        podCodeInfoX = messageBytes[50];
        podCodeInfoX = (short) ((podCodeInfoX << 8) | (messageBytes[49] & 0x00ff));
        //地址码信息Y
        podCodeInfoY = messageBytes[52];
        podCodeInfoY = (short) ((podCodeInfoY << 8) | (messageBytes[51] & 0x00ff));
        //货架码信息θ
        theta = 0;
        theta = messageBytes[56];
        theta = (int) ((theta << 8) | (messageBytes[55] & 0xff));
        theta = (int) ((theta << 8) | (messageBytes[54] & 0xff));
        theta = (int) ((theta << 8) | (messageBytes[53] & 0xff));
        podCodeInfoTheta = Float.intBitsToFloat(theta);
        //绑定货架ID
        selectedPodCodeID = messageBytes[60];
        selectedPodCodeID = (long) ((selectedPodCodeID << 8) | (messageBytes[59] & 0xff));
        selectedPodCodeID = (long) ((selectedPodCodeID << 8) | (messageBytes[58] & 0xff));
        selectedPodCodeID = (long) ((selectedPodCodeID << 8) | (messageBytes[57] & 0xff));
        //举升状态
        juShengZhuangTai = (short) (messageBytes[61] & 0x00ff);
        //充电状态
        chongDianZhuangTai = (short) (messageBytes[62] & 0x00ff);
        //故障ID
        errorID = messageBytes[64];
        errorID = (int) ((errorID << 8) | (messageBytes[63] & 0x000000ff));
        //信号质量
        xinHaoZhiLiang = (short) (messageBytes[65] & 0x00ff);
        //入侵检测次数
        ruQinJianCeCeShu = messageBytes[67];
        ruQinJianCeCeShu = (int) ((ruQinJianCeCeShu << 8) | (messageBytes[66] & 0xff));
        //冷复位次数
        coolReset = messageBytes[69];
        coolReset = (int) ((coolReset << 8) | (messageBytes[68] & 0xff));
        //热复位次数
        hotReset = messageBytes[71];
        hotReset = (int) ((hotReset << 8) | (messageBytes[70] & 0xff));
        //保留1，保留2 （共4字节）
    }
    public String toString(){
          StringBuilder builder = new StringBuilder();
          builder.append("车辆登录消息:");
          builder.append(super.toString());
          builder.append("\r\n");
          builder.append("密码:");
          builder.append(getPassword());
          builder.append("车型");
          builder.append(getRobotType());
          builder.append("硬件版本:");
          builder.append(getHardvareVersion());
          builder.append("软件版本:");
          builder.append(getSoftVersion());
          builder.append("出厂日期:");
          builder.append(new Date(getChuChangDate()*1000l));
          builder.append("累计时长:");
          builder.append(getLeiJiShiChang());
          builder.append("最近维修时间:");
          builder.append(getZuiJinWeiXiuShiJian());
          builder.append("\r\n");
          builder.append("剩余电量:");
          builder.append(getShengYuDianLiang());
          builder.append("地址码:");
          builder.append(getAddressCodeID());
          builder.append("地址码X:");
          builder.append(getAddressCodeInfoX());
          builder.append("地址码Y:");
          builder.append(getAddressCodeInfoY());
          builder.append("地址码theta:");
          builder.append(getAddressCodeInfoTheta());
          builder.append("货架码ID:");
          builder.append(getPodCodeID());
          builder.append("货架码X:");
          builder.append(getPodCodeInfoX());
          builder.append("货架码Y:");
          builder.append(getPodCodeInfoY());
          builder.append("货架码theta:");
          builder.append("\r\n");
          builder.append(getPodCodeInfoTheta());
          builder.append("绑定货架ID:");
          builder.append(getSelectedPodCodeID());
          builder.append("举升状态:");
          builder.append(getJuShengZhuangTai());
          builder.append("充电状态:");
          builder.append(getChongDianZhuangTai());
          builder.append("故障ID:");
          builder.append(getErrorID());
          builder.append("信号质量:");
          builder.append(getXinHaoZhiLiang());
          builder.append("入侵检测次数:");
          builder.append(getRuQinJianCeCeShu());
          builder.append("冷复位次数:");
          builder.append(getCoolReset());
          builder.append("热复位次数:");
          builder.append(getHotReset());
          return builder.toString();
      }

    /**
     * @return the password
     */
    public long getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(long password) {
        this.password = password;
    }

    /**
     * @return the robotType
     */
    public short getRobotType() {
        return robotType;
    }

    /**
     * @param robotType the robotType to set
     */
    public void setRobotType(short robotType) {
        this.robotType = robotType;
    }

    /**
     * @return the hardvareVersion
     */
    public short getHardvareVersion() {
        return hardvareVersion;
    }

    /**
     * @param hardvareVersion the hardvareVersion to set
     */
    public void setHardvareVersion(short hardvareVersion) {
        this.hardvareVersion = hardvareVersion;
    }

    /**
     * @return the softVersion
     */
    public short getSoftVersion() {
        return softVersion;
    }

    /**
     * @param softVersion the softVersion to set
     */
    public void setSoftVersion(short softVersion) {
        this.softVersion = softVersion;
    }

    /**
     * @return the chuChangDate
     */
    public long getChuChangDate() {
        return chuChangDate;
    }

    /**
     * @param chuChangDate the chuChangDate to set
     */
    public void setChuChangDate(long chuChangDate) {
        this.chuChangDate = chuChangDate;
    }

    /**
     * @return the leiJiShiChang
     */
    public long getLeiJiShiChang() {
        return leiJiShiChang;
    }

    /**
     * @param leiJiShiChang the leiJiShiChang to set
     */
    public void setLeiJiShiChang(long leiJiShiChang) {
        this.leiJiShiChang = leiJiShiChang;
    }

    /**
     * @return the zuiJinWeiXiuShiJian
     */
    public long getZuiJinWeiXiuShiJian() {
        return zuiJinWeiXiuShiJian;
    }

    /**
     * @param zuiJinWeiXiuShiJian the zuiJinWeiXiuShiJian to set
     */
    public void setZuiJinWeiXiuShiJian(long zuiJinWeiXiuShiJian) {
        this.zuiJinWeiXiuShiJian = zuiJinWeiXiuShiJian;
    }

    /**
     * @return the shengYuDianLiang
     */
    public int getShengYuDianLiang() {
        return shengYuDianLiang;
    }

    /**
     * @param shengYuDianLiang the shengYuDianLiang to set
     */
    public void setShengYuDianLiang(int shengYuDianLiang) {
        this.shengYuDianLiang = shengYuDianLiang;
    }

    /**
     * @return the addressCodeID
     */
    public long getAddressCodeID() {
        return addressCodeID;
    }

    /**
     * @param addressCodeID the addressCodeID to set
     */
    public void setAddressCodeID(long addressCodeID) {
        this.addressCodeID = addressCodeID;
    }

    /**
     * @return the addressCodeInfoX
     */
    public short getAddressCodeInfoX() {
        return addressCodeInfoX;
    }

    /**
     * @param addressCodeInfoX the addressCodeInfoX to set
     */
    public void setAddressCodeInfoX(short addressCodeInfoX) {
        this.addressCodeInfoX = addressCodeInfoX;
    }

    /**
     * @return the addressCodeInfoY
     */
    public short getAddressCodeInfoY() {
        return addressCodeInfoY;
    }

    /**
     * @param addressCodeInfoY the addressCodeInfoY to set
     */
    public void setAddressCodeInfoY(short addressCodeInfoY) {
        this.addressCodeInfoY = addressCodeInfoY;
    }

    /**
     * @return the addressCodeInfoSinta
     */
    public float getAddressCodeInfoTheta() {
        return addressCodeInfoTheta;
    }

    /**
     * @param addressCodeInfoTheta the addressCodeInfoSinta to set
     */
    public void setAddressCodeInfoTheta(float addressCodeInfoTheta) {
        this.addressCodeInfoTheta = addressCodeInfoTheta;
    }

    /**
     * @return the podCodeID
     */
    public long getPodCodeID() {
        return podCodeID;
    }

    /**
     * @param podCodeID the podCodeID to set
     */
    public void setPodCodeID(long podCodeID) {
        this.podCodeID = podCodeID;
    }

    /**
     * @return the podCodeInfoX
     */
    public short getPodCodeInfoX() {
        return podCodeInfoX;
    }

    /**
     * @param podCodeInfoX the podCodeInfoX to set
     */
    public void setPodCodeInfoX(short podCodeInfoX) {
        this.podCodeInfoX = podCodeInfoX;
    }

    /**
     * @return the podCodeInfoY
     */
    public short getPodCodeInfoY() {
        return podCodeInfoY;
    }

    /**
     * @param podCodeInfoY the podCodeInfoY to set
     */
    public void setPodCodeInfoY(short podCodeInfoY) {
        this.podCodeInfoY = podCodeInfoY;
    }

    /**
     * @return the podCodeInfoSinta
     */
    public float getPodCodeInfoTheta() {
        return podCodeInfoTheta;
    }

    /**
     * @param podCodeInfoTheta the podCodeInfoSinta to set
     */
    public void setPodCodeInfoTheta(float podCodeInfoTheta) {
        this.podCodeInfoTheta = podCodeInfoTheta;
    }

    /**
     * @return the selectedPodCodeID
     */
    public long getSelectedPodCodeID() {
        return selectedPodCodeID;
    }

    /**
     * @param selectedPodCodeID the selectedPodCodeID to set
     */
    public void setSelectedPodCodeID(long selectedPodCodeID) {
        this.selectedPodCodeID = selectedPodCodeID;
    }

    /**
     * @return the juShengZhuangTai
     */
    public short getJuShengZhuangTai() {
        return juShengZhuangTai;
    }

    /**
     * @param juShengZhuangTai the juShengZhuangTai to set
     */
    public void setJuShengZhuangTai(short juShengZhuangTai) {
        this.juShengZhuangTai = juShengZhuangTai;
    }

    /**
     * @return the chongDianZhuangTai
     */
    public short getChongDianZhuangTai() {
        return chongDianZhuangTai;
    }

    /**
     * @param chongDianZhuangTai the chongDianZhuangTai to set
     */
    public void setChongDianZhuangTai(short chongDianZhuangTai) {
        this.chongDianZhuangTai = chongDianZhuangTai;
    }

    /**
     * @return the errorID
     */
    public int getErrorID() {
        return errorID;
    }

    /**
     * @param errorID the errorID to set
     */
    public void setErrorID(int errorID) {
        this.errorID = errorID;
    }

    /**
     * @return the xinHaoZhiLiang
     */
    public short getXinHaoZhiLiang() {
        return xinHaoZhiLiang;
    }

    /**
     * @param xinHaoZhiLiang the xinHaoZhiLiang to set
     */
    public void setXinHaoZhiLiang(short xinHaoZhiLiang) {
        this.xinHaoZhiLiang = xinHaoZhiLiang;
    }

    /**
     * @return the ruQinJianCeCeShu
     */
    public int getRuQinJianCeCeShu() {
        return ruQinJianCeCeShu;
    }

    /**
     * @param ruQinJianCeCeShu the ruQinJianCeCeShu to set
     */
    public void setRuQinJianCeCeShu(int ruQinJianCeCeShu) {
        this.ruQinJianCeCeShu = ruQinJianCeCeShu;
    }

    /**
     * @return the coolReset
     */
    public int getCoolReset() {
        return coolReset;
    }

    /**
     * @param coolReset the coolReset to set
     */
    public void setCoolReset(int coolReset) {
        this.coolReset = coolReset;
    }

    /**
     * @return the hotReset
     */
    public int getHotReset() {
        return hotReset;
    }

    /**
     * @param hotReset the hotReset to set
     */
    public void setHotReset(int hotReset) {
        this.hotReset = hotReset;
    }

}
