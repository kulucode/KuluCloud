package tt.kulu.out.base;

import java.util.HashMap;

import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.company.pojo.CompanyPojo;
import tt.kulu.bi.message.biclass.MQSender;
import tt.kulu.bi.user.pojo.LoginUserPojo;
import tt.kulu.bi.user.pojo.UserPojo;
import tt.kulu.out.call.BICompany;
import tt.kulu.out.call.BILogin;
import tt.kulu.out.call.BIRedis;
import tt.kulu.out.call.BIUser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tt4j2ee.BSCommon;
import com.tt4j2ee.BSGuid;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSLogUserPojo;
import com.tt4j2ee.m.BSObject;
import com.tt4j2ee.md5.MD5Imp;
import com.tt4j2ee.security.BSDes;

/**
 * <p>
 * 标题: BSFrame
 * </p>
 * <p>
 * 功能描述: 登录BS接口类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * <p>
 * 创建日期: 2014-4-1
 * </p>
 */
public class BSLogin {
	/**
	 * <p>
	 * 方法名称: do_clearUser
	 * </p>
	 * <p>
	 * 方法功能描述: 清除当前登录用户信息
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 */
	public BSObject do_clearUser(BSObject m_bs) throws Exception {
		m_bs.getPrivateMap().clear();
		m_bs.getPublicMap().clear();
		JSONObject retJSON = new JSONObject();
		HashMap<String, BSLogUserPojo> userList = (HashMap<String, BSLogUserPojo>) m_bs
				.getRequest().getSession().getServletContext()
				.getAttribute("BS_ONLINE_LIST");
		retJSON.put("out", BILogin.checkUserMax(m_bs));
		System.out.println("--db check start---");
		try {
			SqlExecute sqlHelper = new SqlExecute();
			try {
				System.out.println("--db ok---");
				retJSON.put("r", 0);
			} catch (Exception eex) {
				eex.printStackTrace();
				throw eex;
			} finally {
				sqlHelper.close();
			}

		} catch (Exception ex) {
			retJSON.put("out", true);
			retJSON.put("r", 1000);
		}
		retJSON.put("error", URLlImplBase.ErrorMap.get(retJSON.getInt("r")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名称: do_clearUser
	 * </p>
	 * <p>
	 * 方法功能描述: 清除当前登录用户信息
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 */
	public BSObject do_clearRedisData(BSObject m_bs) throws Exception {
		JSONObject retJSON = new JSONObject();
		BIRedis redisBI = new BIRedis();
		redisBI.clearAll(URLlImplBase.REDIS_KULUDATA);
		retJSON.put("r", 0);
		retJSON.put("error", URLlImplBase.ErrorMap.get(retJSON.getInt("r")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名称: checkUser
	 * </p>
	 * <p>
	 * 方法功能描述: 验证用户
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 */
	public BSObject do_checkUser(BSObject m_bs) throws Exception {
		JSONObject retJSON = new JSONObject();
		retJSON.put("r", 999);
		// 验证验证码
		try {
			if (BILogin.checkUserMax(m_bs)) {
				retJSON.put("r", 996);
			} else {
				retJSON.put("r", this.checkCode(m_bs));
				if (retJSON.getInt("r") == 0) {
					// 验证用户登录信息
					UserPojo inUser = new UserPojo();
					BILogin loginBI = new BILogin(null, m_bs);
					inUser.setId((String) m_bs.getPrivateMap().get("username"));
					// 后台管理员登录
					String hm = (String) m_bs.getPrivateMap().get("bs_type");
					if (hm != null && hm.equals("hm")) {
						// 设置密码与后台管理license用户一致
						inUser.setPassword(BSCommon.getConfigValue(
								"license_password").trim());
					} else {
						// 设置用户密码与用户管理员密码一致
						inUser.setPassword(MD5Imp.enCode(((String) m_bs
								.getPrivateMap().get("userkey")).trim()));
					}
					retJSON = loginBI.checkLoginUser(inUser, "ID", hm);
					if (retJSON.getInt("r") == 0) {
						// LoginUserPojo outUser = (LoginUserPojo) JSONObject
						// .toBean(retJSON.getJSONObject("u"),
						// LoginUserPojo.class);
						// 放入用户信息
						m_bs.setPublicValue(m_bs.getCheckLogin().getLogName(),
								retJSON.getString("u"));
						// m_bs.setSessionID(loginBI.setUserToSession(outUser));
						retJSON.put("r", 0);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			retJSON.put("r", 2);
		} finally {
			retJSON.put("error", URLlImplBase.ErrorMap.get(retJSON.getInt("r")));
		}
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名称: do_indexIni
	 * </p>
	 * <p>
	 * 方法功能描述: 初始化首页
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 */
	public BSObject do_getLoginMsg(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONObject retJSON = new JSONObject();
		retJSON.put("ldate", m_bs.getDateEx().getThisDate(0, 0));
		retJSON.put("logindate", m_bs.getDateEx().getThisDate(0, 0));
		JSONObject userJSON = new JSONObject();
		userJSON.put("instid", user.getUserInst());
		userJSON.put("id", user.getUserId());
		userJSON.put("name", user.getUserName());
		userJSON.put("phone", user.getPhone());
		userJSON.put("group", user.getGroupName());
		userJSON.put("allgroup", user.getGroupAllName());
		// userJSON.put("roles", user.getRoleWhere());
		userJSON.put("sex", user.getSex());
		userJSON.put("sexn", UserPojo.SEX_NAME[user.getSex()]);
		// 返回数据
		retJSON.put("user", userJSON);
		// 菜单
		// 得到应用
		BILogin loginBI = new BILogin(null, m_bs);
		JSONArray ms = loginBI.getUserMenu(m_bs, user, 0);
		if (ms.size() > 0) {
			retJSON.put("menus", ms);
			// 当前运营商
			BICompany compBI = new BICompany(m_bs);
			CompanyPojo onePojo = compBI.getOneCompanyByType(1);
			JSONObject compJ = new JSONObject();
			if (onePojo != null) {
				compJ.put("id", onePojo.getId());
				compJ.put("name", onePojo.getName());
				compJ.put("link", onePojo.getLinkMan());
				compJ.put("linkphone", onePojo.getLinkPhone());
				compJ.put("lat", onePojo.getLatitude());
				compJ.put("lon", onePojo.getLongitude());
			}
			retJSON.put("company", compJ);

			retJSON.put("r", 0);
		} else {
			retJSON.put("r", 8);
		}
		retJSON.put("error", URLlImplBase.ErrorMap.get(retJSON.getInt("r")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名称: do_SysBaseIni
	 * </p>
	 * <p>
	 * 方法功能描述: 初始化首页
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 */
	public BSObject do_SysBaseIni(BSObject m_bs) throws Exception {
		JSONObject retJSON = new JSONObject();
		retJSON.put("r", 5);
		// 得到super_admin帐号
		BIUser userBI = new BIUser(null, m_bs);
		UserPojo onePojo = userBI.getOneUserByInstId("SUPER_ADMIN");
		if (onePojo != null
				&& onePojo.getPassword().equals(
						MD5Imp.enCode(((String) m_bs.getPrivateMap().get(
								"t_sakey")).trim()))) {

			if (new BILogin(null, m_bs).SysBaseIni() > 0) {
				BICompany compBI = new BICompany(m_bs);
				CompanyPojo oneComp = compBI.getOneCompanyByType(1);

				retJSON.put("id", oneComp.getId());
				if (oneComp.getArea() != null) {
					retJSON.put("areaid", oneComp.getArea().getId());
					retJSON.put("areaname", oneComp.getArea().getAllName()
							.replaceAll(",", "-"));
				} else {
					retJSON.put("areaid", "");
					retJSON.put("areaname", "");
				}
				retJSON.put("lat", oneComp.getLatitude());
				retJSON.put("lon", oneComp.getLongitude());
				retJSON.put("name", oneComp.getName());
				retJSON.put("desc", oneComp.getDesc());
				retJSON.put("linkman", oneComp.getLinkMan());
				retJSON.put("linkphone", oneComp.getLinkPhone());
				retJSON.put("r", 0);
			} else {
				retJSON.put("r", 888);
			}
		}
		retJSON.put("error", URLlImplBase.ErrorMap.get(retJSON.getInt("r")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	// 验证码效验
	private int checkCode(BSObject m_bs) throws Exception {
		int ret = 6;
		if (m_bs.getPrivateMap().get("usertype") != null
				&& ((String) m_bs.getPrivateMap().get("usertype")).equals("0")) {
			ret = 0;
		} else {
			BIRedis redisBI = new BIRedis();
			String inCode = (String) m_bs.getPrivateMap().get("usercode");
			String sysCode = redisBI.getStringData(
					"code_" + m_bs.getSessionID(), 1);
			// 验证码效验
			if (inCode != null && sysCode != null && sysCode.equals(inCode)) {
				ret = 0;
			}
			redisBI.delData("code_" + m_bs.getSessionID(), 1);
		}
		return ret;
	}
}
