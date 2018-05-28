package tt.kulu.bi.message.biclass;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.message.pojo.WebSocketDataPojo;

/**
 * <p>
 * 标题: BITTWebSocketSend
 * </p>
 * <p>
 * 功能描述: 酷路跨服务适配类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * 
 * <p>
 * 创建日期: 2011-1-25
 * </p>
 */
public class BITTWebSocketSend extends Thread {
	private String message = "";
	private String dataId = "";

	public BITTWebSocketSend(String message, String dataId) {
		this.message = message;
		this.dataId = dataId;
	}

	public void run() {
		// 遍历用户
		try {
			Iterator iter = URLlImplBase.webSocketMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				WebSocketDataPojo val = (WebSocketDataPojo) entry.getValue();
				if (val.getDataId().equals(dataId)) {
					val.getSession().getBasicRemote().sendText(message);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
