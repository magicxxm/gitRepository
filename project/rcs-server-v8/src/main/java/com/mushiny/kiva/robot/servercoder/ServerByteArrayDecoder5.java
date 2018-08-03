package com.mushiny.kiva.robot.servercoder;

import com.aricojf.platform.common.HexBinaryUtil;
import com.aricojf.platform.security.CRC16;
import com.mushiny.rcs.global.KivaBusConfig;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class ServerByteArrayDecoder5 extends CumulativeProtocolDecoder {
    private boolean head = false;
    byte[] headData = new byte[1];
    private static Logger LOG = LoggerFactory.getLogger(ServerByteArrayDecoder5.class.getName());
    //解码
    public boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
            throws Exception {
//        LOG.info(" < < < - - -  过滤器收到："+HexBinaryUtil.byteArrayToHexString2(in.array()));
        if (in.remaining() >= 14) {
            if (!head) {
                in.mark();
                in.get(headData);
                if (headData[0] != KivaBusConfig.ROBOT2RCS_HEAD) {
                    return true;
                } else {
                    in.reset();
                    head = true;
                }
            }
            //=====================
            in.mark();
            byte[] lenTmp = new byte[3];
            in.get(lenTmp);
            int len = lenTmp[2];
            len = (int) ((len << 8) | (lenTmp[1] & 0xff));
            len = len + 2;//--加2个字节的crc16
            //检查包体数据长度合法性,超过最大数据,则舍弃
            if (len >= KivaBusConfig.AGV2RCS_MAX_BYTES) {
               return true;
            }
            if (in.remaining() < len) {
                in.reset();
                return false;
            } else {
                in.reset();
                int sumlen = 3 + len;
                byte[] dataPackArray = new byte[sumlen];
                in.get(dataPackArray, 0, sumlen);
                if (isCRC16(dataPackArray)) {
                    out.write(dataPackArray);
                }
                head = false;
                if (in.remaining() > 0) {
                    return true;
                }
                return false;
            }
        } else {
            return false;
        }

    }


    /*
     对一个数组检测是否CRC16,规定：CRC16的高8位在前，低8位在后
     */
    public boolean isCRC16(byte[] bs) {
        byte rsCRC16_H = bs[bs.length - 2];
        byte rsCRC16_L = bs[bs.length - 1];
        int rsCRC16;
        rsCRC16 = (int) (rsCRC16_L & 0xff);//注意
        rsCRC16 = (int) (((rsCRC16 << 8) | (rsCRC16_H & 0xff)));

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
}
