package tt.kulu.bi.logs.biclass;

import com.tt4j2ee.m.BSObject;

import tt.kulu.bi.logs.pojo.MsgLogsPojo;
import tt.kulu.out.call.BILogs;

public class MsgLogsBIMang extends Thread {
	private MsgLogsPojo logs = null;
	private BSObject m_bs = null;

	public MsgLogsBIMang(MsgLogsPojo logs, BSObject m_bs) {
		this.logs = logs;
		this.m_bs = m_bs;
	}

	public MsgLogsBIMang() {
	}

	public void run() {
		synchronized (this) {
			try {
				BILogs logBI = new BILogs(this.m_bs);
				// 写入接口日志
				logBI.insertMsgLogs(this.logs);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public MsgLogsPojo getLogs() {
		return logs;
	}

	public void setLogs(MsgLogsPojo logs) {
		this.logs = logs;
	}

}
