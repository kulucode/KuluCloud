package tt.kulu.bi.message.biclass;

import java.io.IOException;
import java.util.HashMap;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import net.sf.json.JSONObject;

import com.tt4j2ee.BSCommon;
import com.tt4j2ee.m.BSObject;

import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.message.pojo.WebSocketDataPojo;
import tt.kulu.bi.user.pojo.LoginUserPojo;

//该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket。类似Servlet的注解mapping。无需在web.xml中配置。
@ServerEndpoint(value = "/TTWebsocket/{userid}")
public class TTWebsocket {
	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;

	/**
	 * 连接建立成功调用的方法
	 * 
	 * @param session
	 *            可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	@OnOpen
	public void onOpen(@PathParam("userid") String userid, Session session,
			EndpointConfig conf) {
		try {
			String data01 = (session.getRequestParameterMap().get("dataid"))
					.get(0);
			if (userid != null && data01 != null && !userid.equals("")
					&& !data01.equals("")) {
				LoginUserPojo oneUser = this.getLoginUser(userid);
				if (oneUser != null) {
					WebSocketDataPojo oneD = new WebSocketDataPojo(
							session.getId(), oneUser, data01, session);
					URLlImplBase.webSocketMap.put(session.getId(), oneD);
					System.out.println("有新连接加入[" + session.getId()
							+ "]！当前在线人数为" + URLlImplBase.webSocketMap.size());
					session.getBasicRemote().sendText(
							"{\"opfun\":\"login\",\"r\":0}");

				} else {
					session.getBasicRemote()
							.sendText(
									"{\"opfun\":\"login\",\"r\":3,\"error\":\"用户未登录！\"}");
				}
			} else {
				session.getBasicRemote().sendText(
						"{\"opfun\":\"login\",\"r\":3,\"error\":\"非法用户！\"}");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(Session session, CloseReason reason) {
		URLlImplBase.webSocketMap.remove(session.getId());
		System.out.println("one client[" + session.getId() + "]closed！online :"
				+ URLlImplBase.webSocketMap.size());
	}

	/**
	 * 收到客户端消息后调用的方法
	 * 
	 * @param message
	 *            客户端发送过来的消息
	 * @param session
	 *            可选的参数
	 */
	@OnMessage
	public void onMessage(String message, Session session) throws IOException,
			InterruptedException {
		System.out.println("from client[" + session.getId() + "]:" + message);

	}

	/**
	 * 发生错误时调用
	 * 
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("error:[" + session.getId() + "]");
		error.printStackTrace();
	}

	/**
	 * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void sendMessage(String uid, String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

	// 得到当前登陆用户
	private LoginUserPojo getLoginUser(String session) {
		LoginUserPojo user = null;
		BSObject m_bs = new BSObject();
		if (m_bs.getPublicMapBySession(session) != null) {
			HashMap<String, String> pubMap = m_bs
					.getPublicMapBySession(session);
			if (pubMap != null) {
				String loginUserJSON = pubMap.get(BSCommon
						.getConfigValue("userconfig_loginobj_name"));
				user = (LoginUserPojo) JSONObject.toBean(
						JSONObject.fromObject(loginUserJSON),
						LoginUserPojo.class);
			}
		}

		return user;
	}
}
