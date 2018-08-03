/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.server;

import com.aricojf.platform.mina.common.MinaConfig;
import com.mingchun.mu.util.ExceptionUtil;
import com.mushiny.kiva.robot.servercoder.ServerByteArrayCodecFactory;
import com.mushiny.rcs.server.RCSMessageOperation;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class MinaServer {

    static Logger LOG = LoggerFactory.getLogger(MinaServer.class.getName());
    public SocketAcceptor acceptor;
    private String hostName = MinaConfig.HOST_NAME;
    private int port = MinaConfig.PORT;
    private int readBufferSize = MinaConfig.READ_BUFFER_SIZE;
    private int writeBufferSize = MinaConfig.WRITE_BUFFER_SIZE;
    private Charset charset = MinaConfig.CHARSET_UTF8;

    public MinaServer() {
        setCharset(Charset.forName("UTF-8"));
    }

//    public MinaServer(String hostName, int port) {
//        this.hostName = hostName;
//        this.port = port;
//        setCharset(Charset.forName("UTF-8"));
//    }
//
//    public MinaServer(String hostName, int port, int readBufferSize, int writeBufferSize) {
//        this.hostName = hostName;
//        this.port = port;
//        this.readBufferSize = readBufferSize;
//        this.writeBufferSize = writeBufferSize;
//        setCharset(Charset.forName("UTF-8"));
//    }

    public boolean Begin() {
        acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);//防止上次退出时端口没有释放
        acceptor.setReuseAddress(true);
        DefaultIoFilterChainBuilder chainBuilder = acceptor.getFilterChain();
        chainBuilder.addLast("kivaCodec", new ProtocolCodecFilter(new ServerByteArrayCodecFactory()));//KIVA Wifi连接
        //chainBuilder.addLast("kivaCodec", new ProtocolCodecFilter(new KivaCodecFactory()));//KIVA Wifi连接
        acceptor.getSessionConfig().setReadBufferSize(getReadBufferSize());
        acceptor.getSessionConfig().setSendBufferSize(getWriteBufferSize());
        //--因为启用了心跳功能，所以此功能没有任何意义acceptor.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, MinaConfig.IDLE_TIME);
        //acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, MinaConfig.IDLE_TIME);
        acceptor.setHandler(new RCSMessageOperation());
        acceptor.setDefaultLocalAddress(new InetSocketAddress(getPort()));
        //=========================心跳设置================================================
        //ServerKeepAliveMessageFactoryImp heartBeatFactory = new ServerKeepAliveMessageFactoryImp();
        //ServerKeepAliveRequestTimeoutHandlerImpl heartBeatHandler = new ServerKeepAliveRequestTimeoutHandlerImpl();
        //实例化一个  KeepAliveFilter  过滤器，传入 KeepAliveMessageFactory引用，IdleStatus参数为 BOTH_IDLE,
        //及表明如果当前连接的读写通道都空闲的时候在指定的时间间隔getRequestInterval后发送出发Idle事件
        //KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory, IdleStatus.BOTH_IDLE, heartBeatHandler);
        /*idle事件回发  当session进入idle状态的时候 依然调用handler中的idled方法
        尤其 注意该句话，使用了 KeepAliveFilter之后，IoHandlerAdapter中的 sessionIdle方法默认是不会再被调用的！ 
        所以必须加入这句话 sessionIdle才会被调用
        
         增加此句，心跳超时进入，public void sessionIdle(IoSession session, IdleStatus status)进入;
         且：acceptor.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, MinaConfig.IDLE_TIME)不在起作用;
        即：如果使用了MINA本身的心跳功能后，session的setIdelTime不再起作用！
         */
        //heartBeat.setForwardEvent(true);
        /*本服务器为被定型心跳  即需要每MinaConfig.KEEP_ALIVE_TIME_INTERVAL秒接受一个心跳请求  否则该连接进入
        空闲状态 并且发出idled方法回调
        说明：设置心跳包请求时间间隔，其实对于被动型的心跳机制来说，设置心跳包请求间隔貌似是没有用的，
        因为它是不会发送心跳包的，但是它会触发 sessionIdle事件， 我们利用该方法，
        可以来判断客户端是否在该时间间隔内没有发心跳包，一旦 sessionIdle方法被调用，则认为 客户端丢失连接并将其
        踢出 。因此其中参数 其实就是服务器对于客户端的IDLE监控时间。*/
        //heartBeat.setRequestInterval(MinaConfig.SERVER_KEEP_ALIVE_TIME_INTERVAL);
        //acceptor.getFilterChain().addLast("heartbeat", heartBeat);
        //=============================心跳设置完毕=======================================
        try {
            acceptor.bind();
            LOG.info("===================================================");
            LOG.info("======启动主机端服务成功!");
            LOG.info("======port:" + getPort());
            LOG.info("======name:" + getHostName());
            LOG.info("=======readBufferSize:" + getReadBufferSize());
            LOG.info("=======writeBufferSize:" + getWriteBufferSize());
            LOG.info("===================================================");
            return true;
        } catch (Exception e) {
            LOG.info("======启动主机端服务失败:\n" + ExceptionUtil.getMessage(e));
            return false;
        }
    }

    public void Stop() {
        if (acceptor != null) {
            acceptor.dispose();
            LOG.info("======关闭主机端服务成功!");
            LOG.info("======port:" + getPort());
            LOG.info("======name:" + getHostName());
            LOG.info("=======readBufferSize:" + getReadBufferSize());
            LOG.info("=======writeBufferSize:" + getWriteBufferSize());
        }
    }

    /**
     * @return the name
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param name the name to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the timeout
     */
    /**
     * @return the readBufferSize
     */
    public int getReadBufferSize() {
        return readBufferSize;
    }

    /**
     * @param readBufferSize the readBufferSize to set
     */
    public void setReadBufferSize(int readBufferSize) {
        this.readBufferSize = readBufferSize;
    }

    /**
     * @return the writeBufferSize
     */
    public int getWriteBufferSize() {
        return writeBufferSize;
    }

    /**
     * @param writeBufferSize the writeBufferSize to set
     */
    public void setWriteBufferSize(int writeBufferSize) {
        this.writeBufferSize = writeBufferSize;
    }

    /**
     * @return the charset
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * @param charset the charset to set
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
    }
}
