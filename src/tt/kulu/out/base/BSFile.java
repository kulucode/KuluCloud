package tt.kulu.out.base;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.file.pojo.BFSFilePojo;
import tt.kulu.bi.user.pojo.LoginUserPojo;
import tt.kulu.bi.user.pojo.UserPojo;
import tt.kulu.out.call.BIFile;
import tt.kulu.out.call.BILogin;

import com.tt4j2ee.BSDateEx;
import com.tt4j2ee.BSGuid;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BSFile
 * </p>
 * <p>
 * 功能描述: 文件管理Web接口类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * <p>
 * 创建日期: 2014-10-23
 * </p>
 */
public class BSFile {

	/**
	 * <p>
	 * 方法名称: do_ImportFile
	 * </p>
	 * <p>
	 * 方法功能描述: 导入文件(web接口)
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 */
	// 保存文件
	public BSObject do_ImportFile(BSObject m_bs) throws Exception {
		JSONObject retObj = new JSONObject();
		retObj.put("r", 0);
		if (m_bs == null) {
			m_bs = new BSObject();
		}
		LoginUserPojo logUser = BILogin.getLoginUser(m_bs);
		UserPojo user = new UserPojo();
		String userInstId = m_bs.getPrivateMap().get("cuid");
		if (logUser == null && userInstId != null
				&& !userInstId.trim().equals("")) {
			user.setInstId(userInstId);
		} else {
			user.setInstId(logUser.getUserInst());
		}
		try {
			ServletFileUpload multi = new ServletFileUpload(
					new DiskFileItemFactory());
			BFSFilePojo file = new BFSFilePojo();
			file.setUser(user);
			file.setType(1);
			file.setOpType(m_bs.getPrivateMap().get("filetype"));
			file.setBissId(m_bs.getPrivateMap().get("bissid"));
			retObj = BIFile.importFile(m_bs, file, multi);
		} catch (Exception e) {
			e.printStackTrace();
			retObj.put("r", 71);
		}

		if (!retObj.containsKey("error")) {
			retObj.put("error", URLlImplBase.ErrorMap.get(retObj.getInt("r")));
		}
		m_bs.setRetrunObj(retObj);
		return m_bs;
	}
}
