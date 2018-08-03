/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.robot.servercoder;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 *
 * @author aricochen
 */
public class ServerByteArrayCodecFactory implements ProtocolCodecFactory{
    private ServerByteArrayDecoder5 decoder;
    private ServerByteArrayEncoder2 encoder;
    
    public ServerByteArrayCodecFactory() {
    	encoder = new ServerByteArrayEncoder2();
        decoder = new ServerByteArrayDecoder5();
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }
}
