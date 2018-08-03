/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.robot.clientcoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 *
 * @author aricochen
 */
public class ClientByteArrayEncoder extends ProtocolEncoderAdapter{
    public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		byte[] bytes = (byte[])message;		
		IoBuffer buffer = IoBuffer.allocate(64);
		buffer.setAutoExpand(true);
		
		buffer.put(bytes);
		buffer.flip();
		
		out.write(buffer);
		out.flush();
		
		buffer.free();
	}
}
