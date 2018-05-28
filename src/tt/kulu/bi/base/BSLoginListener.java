package tt.kulu.bi.base;

import java.io.PrintStream;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import tt.kulu.bi.message.biclass.MQReceiver;

import com.tt4j2ee.BSCommon;
import com.tt4j2ee.BSDateEx;
import com.tt4j2ee.Debug;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSLogUserPojo;

/**
 * <p>
 * 标题: BSLoginListener
 * </p>
 * <p>
 * 功能描述: 用户登录监听
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * 
 * <p>
 * 创建日期: 2006-8-24
 * </p>
 */
public class BSLoginListener implements HttpSessionListener,
		HttpSessionAttributeListener, ServletContextListener {
	private ServletContext application = null;

	public void attributeAdded(HttpSessionBindingEvent se) {
	}

	public void attributeRemoved(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		this.attributeAdded(arg0);
	}

	public void sessionCreated(HttpSessionEvent se) {
	}

	public void sessionDestroyed(HttpSessionEvent se) {

	}

	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("---session stop---");
		MQReceiver mq = MQReceiver.getInstance();
		mq.shutdown();
		// mq.stop();
		// System.out.println("---mq stop---");
		// TTSessionContext.getInstance().shutdown();
	}

	public void contextInitialized(ServletContextEvent event) {
		BSCommon bscomm = new BSCommon(event.getServletContext(), false);
		try {
			SqlExecute sqlHelper = new SqlExecute();
			try {
				// TTEduStatic.iniStaticData(null, null, sqlHelper);
			} catch (Exception ex) {
				ex.printStackTrace();
				throw ex;
			} finally {
				sqlHelper.close();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// 初始化一个application对象
		this.application = event.getServletContext();
		String mqFlg = BSCommon.getConfigValue("mq_open");
		if (mqFlg != null && mqFlg.equals("true")) {
			MQReceiver mq = MQReceiver.getInstance();
			mq.start();
		}
	}
}
