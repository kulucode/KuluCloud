package tt.kulu.bi.logs.biclass;

import com.tt4j2ee.m.BSObject;

import tt.kulu.bi.logs.pojo.SysLogsPojo;
import tt.kulu.out.call.BILogs;

public class SysLogsBIMang extends Thread {
	private SysLogsPojo logs = null;
	private BSObject m_bs = null;

	public SysLogsBIMang(SysLogsPojo logs, BSObject m_bs) {
		this.logs = logs;
		this.m_bs = m_bs;
	}

	public void run() {
		synchronized (this) {
			try {
				BILogs logBI = new BILogs(this.m_bs);
				// 写入接口日志
				logBI.insertSysLogs(this.logs);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
