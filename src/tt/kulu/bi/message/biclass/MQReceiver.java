package tt.kulu.bi.message.biclass;

import java.util.Random;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import net.sf.json.JSONObject;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import tt.kulu.bi.base.URLlImplBase;

import com.tt4j2ee.BSCommon;
import com.tt4j2ee.BSDateEx;

public class MQReceiver extends Thread {
	private ConnectionFactory connectionFactory;
	// Connection ：JMS 客户端到JMS Provider 的连接
	private Connection connection = null;
	// Session： 一个发送或接收消息的线程
	private Session session;
	// Destination ：消息的目的地;消息发送给谁.
	private Destination destination;
	// 消费者，消息接收者
	private MessageConsumer consumer1;
	private MessageConsumer consumer3;

	public static MQReceiver instance = null;// 单例

	public static MQReceiver getInstance() {
		if (instance == null) {
			instance = new MQReceiver();
		}
		return instance;
	}

	public void shutdown() {
		try {
			System.out.println("-------------MQ stop！！----------");
			this.consumer1.close();
			this.consumer3.close();
			this.connection.close();
			MQReceiver.instance = null;
			System.out.println("-------------MQ stop ok！！----------");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		// ConnectionFactory ：连接工厂，JMS 用它创建连接
		this.connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD,
				BSCommon.getConfigValue("mq_server") + ":"
						+ BSCommon.getConfigValue("mq_port"));
		try {
			Thread.sleep(5000);
			System.out.println("-------------MQ start ！！----------");
			// 构造从工厂得到连接对象
			this.connection = connectionFactory.createConnection();
			// 启动
			this.connection.start();
			// 获取操作连接
			this.session = connection.createSession(Boolean.FALSE,
					Session.CLIENT_ACKNOWLEDGE);
			// TRUCKSERV_TO_CLOUD
			// 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
			this.consumer1 = session.createConsumer(session
					.createQueue(BSCommon.getConfigValue("mq_pre")
							+ URLlImplBase.MQTopicM.get(1)));
			this.consumer1.setMessageListener(new MessageListener() {
				public void onMessage(Message msg) {
					try {
						BIKuluMQAdapter mqAd = new BIKuluMQAdapter();
						TextMessage message = (TextMessage) msg;
						String timesID = (new BSDateEx()).getSeqDate();
						System.out.println("");
						System.out.println("[" + timesID + "]---Receiver ["
								+ BSCommon.getConfigValue("mq_pre")
								+ "TRUCKSERV_TO_CLOUD]:" + message.getText());
						mqAd.VehicleGeoToRedis(message.getText());
						// mqAd.doTRUCKSERV_TO_CLOUD_MQ(message.getText());
						System.out.println("[" + timesID
								+ "]---Receiver over!!!");
						message.acknowledge();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {

					}
				}

			});

			// WINGSERV_TO_CLOUD
			// 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
			this.consumer3 = session.createConsumer(session
					.createQueue(BSCommon.getConfigValue("mq_pre")
							+ URLlImplBase.MQTopicM.get(3)));
			this.consumer3.setMessageListener(new MessageListener() {
				public void onMessage(Message msg) {
					try {
						BIKuluMQAdapter mqAd = new BIKuluMQAdapter();
						TextMessage message = (TextMessage) msg;
						String timesID = (new BSDateEx()).getSeqDate();
						System.out.println("");
						System.out.println("[" + timesID + "]---Receiver ["
								+ BSCommon.getConfigValue("mq_pre")
								+ "WINGSERV_TO_CLOUD]:" + message.getText());
						Thread.sleep(new Random().nextInt(1000));
						mqAd.doWINGSERV_TO_CLOUD_MQ(message.getText());
						System.out.println("[" + timesID
								+ "]---Receiver over!!!");
						message.acknowledge();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {

					}
				}

			});

		} catch (Exception e) {
			try {
				if (null != this.connection)
					this.connection.close();
			} catch (Throwable ignore) {
			}
			e.printStackTrace();
		} finally {

		}
	}
}