package mq;

import com.aricojf.platform.mina.common.MinaConfig;
import com.mingchun.mu.util.ExceptionUtil;
import com.mushiny.kiva.map.KivaMap;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class MQManager {
    private Logger LOG = LoggerFactory.getLogger(MQManager.class.getName());
    private Boolean isConnectionSucess = Boolean.valueOf(false);
    protected static ConnectionFactory factory;
    protected static Connection publishConnection;
    protected static Channel channel;
    protected static KivaMap kivaMap;
    protected String COM_EXCHANGE = "section1";
    protected long sectionID = 1;
    protected long startServiceTime = System.currentTimeMillis();

    private String user;
    private String password;
    private String url;
    private int port;

    private XmlParser parser = new XmlParser();
    public MQManager() {
        String confPath = SubjectManager.RCS_CONFIG_PATH;
        String sectID = parser.getText(confPath, "config/section_id");
        if("".equals(sectID)) {
            System.exit(0);
        }
        this.sectionID = Long.parseLong(sectID);
        COM_EXCHANGE = SubjectManager.COM_EXCHANGE+this.sectionID;

        MinaConfig.PORT = Integer.parseInt(parser.getText(confPath, "config/mina_port"));
        user = parser.getText(confPath, "config/rabbit/user");
        password = parser.getText(confPath, "config/rabbit/password");
        url = parser.getText(confPath, "config/rabbit/url");
        String portStr = parser.getText(confPath, "config/rabbit/port");
        port = "".equals(portStr) ? 5672:Integer.parseInt(portStr);

    }

    public boolean connect2mqServer() {
        return connect2mqServer(url, port, user, password);
    }

    public boolean connect2mqServer(String url, int port, String user, String password) {
        try {
            factory = new ConnectionFactory();
            factory.setHost(url);
            factory.setPort(port);
            factory.setPassword(user);
            factory.setUsername(password);

            /*// 关键所在，指定线程池
            ExecutorService service = Executors.newFixedThreadPool(10);
            factory.setSharedExecutor(service);
            // 设置自动恢复
            factory.setAutomaticRecoveryEnabled(true);
            factory.setNetworkRecoveryInterval(1000);// 设置 没1s ，重试一次
            factory.setTopologyRecoveryEnabled(false);// 设置不重新声明交换器，队列等信息。*/
            publishConnection = factory.newConnection();

            channel = publishConnection.createChannel();
            channel.exchangeDeclare(COM_EXCHANGE, "direct", true);
            this.LOG.info("MQ连接成功...");
            return true;
        } catch (Exception var6) {
            this.LOG.error("MQ连接失败，服务已关闭，请检查参数...\n"+ ExceptionUtil.getMessage(var6));
            var6.printStackTrace();
            return false;
        }
    }

    public boolean isConnection() {
        return this.isConnectionSucess.booleanValue();
    }

    public static Channel getChannel() {
        return channel;
    }
}
