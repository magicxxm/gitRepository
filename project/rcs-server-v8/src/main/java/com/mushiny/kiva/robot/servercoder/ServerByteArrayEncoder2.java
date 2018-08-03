package com.mushiny.kiva.robot.servercoder;

import com.aricojf.platform.common.HexBinaryUtil;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class ServerByteArrayEncoder2 extends ProtocolEncoderAdapter{
    public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		byte[] bytes = (byte[])message;		
		IoBuffer buffer = IoBuffer.allocate(128);
		buffer.setAutoExpand(true);

		buffer.put(bytes);
		buffer.flip();

		out.write(buffer);
		out.flush();

		buffer.free();

//		System.out.println("写出数据包："+ HexBinaryUtil.byteArrayToHexString2(bytes));

	}
}
