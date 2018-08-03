/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.robot.clientcoder;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aricochen
 */
public class ClientByteArrayDecoder extends ProtocolDecoderAdapter{
   
    private static Logger LOG = LoggerFactory.getLogger(ClientByteArrayDecoder.class.getName());
    private List<Byte> bufferList = new CopyOnWriteArrayList();
    private boolean isValidateHead = false;
   
    public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
            throws Exception {
       int limit = in.limit();
        byte[] bytes = new byte[limit];
        in.get(bytes);
        out.write(bytes);

    }
   
}