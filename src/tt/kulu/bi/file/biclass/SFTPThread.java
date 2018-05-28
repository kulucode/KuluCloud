package tt.kulu.bi.file.biclass;

import com.tt4j2ee.BSCommon;
import com.tt4j2ee.BSSFtp;

public class SFTPThread extends Thread {
	private String serv = BSCommon.getConfigValue("upload_sftpserv");
	private String user = BSCommon.getConfigValue("upload_sftpuser");
	private String key = BSCommon.getConfigValue("upload_sftpkey");
	private String base = BSCommon.getConfigValue("upload_basepath");
	private String port = BSCommon.getConfigValue("upload_sftpport");
	private String path = "";
	private String file = "";

	public SFTPThread(String path, String file) {
		this.path = path;
		this.file = file;
	}

	public void run() {
		BSSFtp sftp = new BSSFtp();
		String files[] = this.file.split(",");
		if (files.length > 0) {
			try {
				if (sftp.connect(
						serv,
						this.port.equals("") ? 22 : Integer.parseInt(this.port),
						this.user, this.key)) {

					for (String oneF : files) {
						if (!oneF.equals("")) {
							sftp.upload(this.base + "/" + this.path, oneF, true);
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (sftp != null) {
					sftp.disconnect();
				}
			}
		}
	}
}
