package tt.kulu.bi.base;

import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: CheckLogin
 * </p>
 * <p>
 * 功能描述: 用户Session是否丢失校验类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: V0.1
 * </p>
 * 
 * <p>
 * 创建日期: 2003-12-16
 * </p>
 */
public class CheckLogin {
	public String do_check(BSObject m_bs) throws Exception {
		String retStr = "F";
		// redisBI.delData(ttkey, 1);
		// 根据登录成功后的sessionID从redis中得到用户JSON
		String loginUserJSON = m_bs.getPublicMap().get(
				m_bs.getCheckLogin().getLogName());
		// System.out.println("loginUserJSON:" + loginUserJSON);
		if (loginUserJSON != null && !loginUserJSON.equals("")) {
			retStr = "";
		}

		return retStr;
	}

}
