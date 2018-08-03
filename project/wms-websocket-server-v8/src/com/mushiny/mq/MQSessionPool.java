package com.mushiny.mq;


import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;

import javax.jms.JMSException;
import javax.jms.Session;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * On 2017/6/13.
 *
 * @author wangdong
 *         TODO :
 */
public class MQSessionPool {
    private int currentNum = 0;

    //最大线程数未配置时默认给20
    private int maxSession = 20;

    private Session curSession;

    private List<Session> pool;

    public ActiveMQConnection connection;

    public MQSessionPool() {
        pool = Collections.synchronizedList(new LinkedList<Session>());
        //解析MQ服务器的配置文件 获取链接参数

        String user = ConfigAnalysis.getProperty(MQCommon.MQSERVER_USER);
        String password = ConfigAnalysis.getProperty(MQCommon.MQSERVER_PASSWORD);
        String url = ConfigAnalysis.getProperty(MQCommon.MQSERVER_URL);
        String sessionNum = ConfigAnalysis.getProperty(MQCommon.MAX_SESSION);
        maxSession = "".equals(sessionNum) || null == sessionNum ? maxSession : Integer.valueOf(sessionNum);

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);

        try {
            connection = (ActiveMQConnection) connectionFactory.createConnection();
            connection.start();
        } catch (JMSException e) {
            e.printStackTrace();
            colseConn();
        }

    }


    public Session getSession() {
        if (pool.size() == 0 && currentNum < maxSession) {
            // 如果当前池中无对象可用，而且已创建的对象数目小于所限制的最大值，创建一个新的对象
            try {
                curSession = connection.createSession(false, ActiveMQSession.AUTO_ACKNOWLEDGE);
            } catch (JMSException e) {
                e.printStackTrace();
            }
            pool.add(curSession);
            currentNum++;
        } else if (pool.size() == 0 && currentNum >= maxSession) {
            while (pool.size() == 0) {
                // 如果当前池中无对象可用，而且所创建的对象数目已达到所限制的最大值,
                // 就只能等待其它线程返回对象到池中
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            curSession = pool.remove(0);
        } else {
            // 如果当前池中有可用的对象，就直接从池中取出对象
            curSession = pool.remove(0);
        }
        return curSession;
    }

    /**
     * 放回连接池
     *
     * @param session
     */
    public void returnSession(Session session) {
        pool.add(session);
    }

    public ActiveMQConnection getConn() {
        return connection;
    }

    public void colseConn() {
        try {
            if (null != connection)
                connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

