package tt.kulu.bi.message.pojo;

import javax.websocket.Session;

import tt.kulu.bi.user.pojo.LoginUserPojo;

public class WebSocketDataPojo {
	private String sessionId = "";
	private String dataId = "";// 数据ID,01:车载；02为手表
	private Session session = null;
	private LoginUserPojo user = null;

	public WebSocketDataPojo(String sessionId, LoginUserPojo user,
			String dataId, Session session) {
		this.sessionId = sessionId;
		this.user = user;
		this.dataId = dataId;
		this.session = session;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public LoginUserPojo getUser() {
		return user;
	}

	public void setUser(LoginUserPojo user) {
		this.user = user;
	}

}
