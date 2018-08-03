/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.server;

import com.aricojf.platform.common.HexBinaryUtil;
import com.aricojf.platform.security.CRC16;
import com.mushiny.rcs.global.KivaBusConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 *
 * @author aricochen
 */
public class DataValidate {
    private static final Logger LOG = LoggerFactory.getLogger(DataValidate.class.getName());
    private final LinkedList<Byte> bufferList = new LinkedList();
    private boolean validateHead = false;
    private long count = 0;
    private long errorByteCount=0;
    public DataValidate() {
        
    }
    public void postData(Object message) {
        byte[] bytes = (byte[]) message;
        for (int i = 0; i < bytes.length; i++) {
            bufferList.addLast(bytes[i]);
        }
    }
    //是否有报文头及头位置
    public void checkData() {
        validateHead = false;
        for (Byte b : bufferList) {
            if ((byte) b == KivaBusConfig.ROBOT2RCS_HEAD) {
                validateHead = true;
                return;
            } else {
                errorByteCount++;
                LOG.error("##########错误数据->无帧头字节【"+HexBinaryUtil.byteToHexString(b)+"】丢弃...");
                LOG.warn("##########无效字节数量累计="+errorByteCount+"##########");
                bufferList.remove(b);
            }
        }
    }

    //获取一个完整的数据包
    public byte[] getValidateData() {
        if (bufferList.size() <= 3) {//只有包头和帧长，返回
            LOG.error("#########错误：此数据不予处理！！【原因：数据包长度小于等于3】##########");
            return null;
        }
        byte[] packageLengthBytes = new byte[2];
        //下位机对于数值型的传输模式为：先传地位，再传高位
        packageLengthBytes[0] = (byte) bufferList.get(2);//高位
        packageLengthBytes[1] = (byte) bufferList.get(1);//低位
        int packageLength;
        packageLength = (int) (packageLengthBytes[0] & 0xFF);
        packageLength = (int) (packageLength << 8) | (packageLengthBytes[1] & 0xFF);
        
        //预估总数据包长度
        int totalPackageLength = 1 + 2 + packageLength + 2;
        if (bufferList.size() < totalPackageLength) {//缓冲区长度不合法
            LOG.error("#########错误：此数据不予处理！！【原因：数据包长度不合法，帧长和真实长度不相等】##########");
            return null;
        }
        //合法长度数据包
        byte[] tmpByte = new byte[totalPackageLength];
        for (int i = 0; i < totalPackageLength; i++) {
            tmpByte[i] = (byte) bufferList.removeFirst();
        }
        //CRC16校验
        if (isCRC16(tmpByte)) {
            count+=tmpByte.length;
            return tmpByte;
        }else {
           LOG.error("#########错误：此数据不予处理！！【原因：CRC16校验不通过】##########");
        }      
        return null;
    }

    /*
     对一个数组检测是否CRC16,规定：CRC16的高8位在前，低8位在后
     */
    public boolean isCRC16(byte[] bs) {
        if (bs.length <= 2) {
            LOG.error("数据包长度=" + bs.length + ",不合法，CRC16验证不通过...");
            return false;
        }
        byte rsCRC16_H = bs[bs.length - 2];
        byte rsCRC16_L = bs[bs.length - 1];
        int rsCRC16;
        rsCRC16 = (int)(rsCRC16_H & 0xff);//注意
        rsCRC16 = (int)(((rsCRC16 << 8) | (rsCRC16_L & 0xff)));

        int tmpCRC16 = CRC16.calcCrc16(bs, 1, bs.length - 3);
        // int tmpCRC16 = CRC16Util.makeCRC(b);

        if (rsCRC16 == tmpCRC16) {
            // LOG.debug("CRC16校验结果=通过");
        } else {
            LOG.warn("=============================================");
            LOG.warn("CRC16验证结果，不通过!!(报文=" + HexBinaryUtil.byteArrayToHexString2(bs));
            LOG.warn("CRC16赋值结果=" + Integer.toHexString(rsCRC16) + "(" + rsCRC16 + ")");
            LOG.warn("CRC16计算结果=" + Integer.toHexString(tmpCRC16) + "(" + tmpCRC16 + ")");
        }
        return rsCRC16 == tmpCRC16;
    }

    /**
     * @return the validateHead
     */
    public boolean isValidateHead() {
        return validateHead;
    }
    
}
