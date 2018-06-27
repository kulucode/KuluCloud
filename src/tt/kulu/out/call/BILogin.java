package tt.kulu.out.call;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tt4j2ee.BSCommon;
import com.tt4j2ee.BSGuid;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.base.SysBaseDBMang;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.company.pojo.CompanyPojo;
import tt.kulu.bi.power.dbclass.BSPowerDBMang;
import tt.kulu.bi.power.pojo.RoleMenuPojo;
import tt.kulu.bi.power.pojo.RolePojo;
import tt.kulu.bi.user.dbclass.BSUserDBMang;
import tt.kulu.bi.user.pojo.LoginUserPojo;
import tt.kulu.bi.user.pojo.UserOrgRPojo;
import tt.kulu.bi.user.pojo.UserPojo;

/**
 * <p>
 * 标题: BILogin
 * </p>
 * <p>
 * 功能描述: 登录类
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
public class BILogin extends BSDBBase {
	public BILogin(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: do_checkUser
	 * </p>
	 * <p>
	 * 方法功能描述: 用户密码和验证码效验
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 */
	public JSONObject checkLoginUser(UserPojo inUser, String type, String hm)
			throws Exception {
		JSONObject retJSON = new JSONObject();
		retJSON.put("r", -999);
		SqlExecute sqlHelper = new SqlExecute();
		try {
			// 员工登录
			BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
			LoginUserPojo outUser = this._getUserById(userDB, inUser.getId());

			if (outUser != null) {
				// 得到登录KEY（判断是管理员还是正常的用户）
				// 如果是管理员则判断是否是license密码，如果不是管理员则正常判断
				if ((hm != null && hm.equals("hm") && inUser.getPassword()
						.equals(BSCommon.getConfigValue("license_password")
								.trim()))
						|| inUser.getPassword().equals(outUser.getPassword())) {
					// 得到角色信息
					BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
					ArrayList<RolePojo> list = new ArrayList<RolePojo>();
					List<Object> vList = new ArrayList<Object>();
					vList.add(outUser.getUserInst());
					outUser.setRoleWhere(roleDB
							.getRoles(
									list,
									" and t.ROLE_ID in (select ROLE_ID from T_ROLE_USER_R where USER_INSTID=?)",
									vList));
					// 设置roleWhere
					String roleWhere = "";
					for (int i = 0, size = list.size(); i < size; i++) {
						roleWhere += (",'" + list.get(i).getId() + "'");
					}
					if (list.size() > 0) {
						roleWhere = roleWhere.substring(1);
					}
					outUser.setRoleWhere(roleWhere);
					// 得到所有orgId
					ArrayList<UserOrgRPojo> ugList = userDB.getUserOrgList(
							" and t.USER_INSTID=?", vList, "");
					for (UserOrgRPojo oneR : ugList) {
						String[] aos = oneR.getOrg().getAllOrgId().split(",");
						for (String oneAO : aos) {
							if (!oneAO.equals("")) {
								if (outUser.getGroupAllId()
										.indexOf("," + oneAO) < 0) {
									outUser.setGroupAllId(outUser
											.getGroupAllId() + "," + oneAO);
								}
							}
						}
						if (outUser.getGroupId().indexOf(
								"," + oneR.getOrg().getId()) < 0) {
							outUser.setGroupId(outUser.getGroupId() + ","
									+ oneR.getOrg().getId());
						}

					}

					retJSON.put("u", outUser);
					retJSON.put("r", 0);
				} else {
					retJSON.put("r", 5);
				}
			} else {
				retJSON.put("r", 4);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			sqlHelper.close();
		}
		if (retJSON.getInt("r") == 0) {
			// 记录登陆日志
			// LogIF.addLoginLog(new LoginRecordPojo(user, "", m_bs.getRequest()
			// .getRemoteAddr()
			// + "_"
			// + m_bs.getRequest().getRemoteUser()
			// + "_" + m_bs.getRequest().getHeader("user-agent")));
		}
		return retJSON;
	}

	// 得到应用
	private JSONArray _getWinApps(BSObject m_bs, LoginUserPojo user, int state,
			int style, SqlExecute sqlHelper) throws Exception {
		JSONArray menuList = new JSONArray();
		JSONObject paras = new JSONObject();
		JSONObject cparas = new JSONObject();
		if (!user.getUserInst().equals(BSCommon.getConfigValue("license_user"))
				&& !user.getUserInst().equals("CONFIG")) {
			paras.put("state", state);
			paras.put("style", style);
			paras.put("class", 0);
			paras.put("role", user.getRoleWhere().replaceAll("'", ""));
			BIPower powerBI = new BIPower(sqlHelper, m_bs);
			ArrayList<RoleMenuPojo> rootMenuList = powerBI.getRootWinApps(
					sqlHelper, paras);

			// 加载应用
			cparas.put("state", state);
			cparas.put("class", 1);
			cparas.put("style", style);
			cparas.put("role", user.getRoleWhere().replaceAll("'", ""));
			for (RoleMenuPojo oneMenu : rootMenuList) {
				JSONObject oneMenuJson = new JSONObject();
				oneMenuJson.put("id", oneMenu.getMenu().getMenuId());
				oneMenuJson.put("url", oneMenu.getMenu().getToPage());
				oneMenuJson.put("img", oneMenu.getMenu().getImgName());
				oneMenuJson.put("name", oneMenu.getMenu().getMenuName());
				JSONArray menuCList = new JSONArray();
				if (oneMenu.getMenu().getCount() > 0) {
					// 下级
					cparas.put("parent", oneMenu.getMenu().getMenuId());
					ArrayList<RoleMenuPojo> cMenuList = powerBI.getWinApps(
							sqlHelper, cparas);
					for (RoleMenuPojo oneCMenu : cMenuList) {
						JSONObject oneCMenuJson = new JSONObject();
						oneCMenuJson.put("id", oneCMenu.getMenu().getMenuId());
						String url = oneCMenu.getMenu().getToPage();
						if (url != null && !url.startsWith("http:")) {
							String conPath = m_bs.getContextPath();
							// if (!conPath.equals("")) {
							// url = m_bs.getContextPath() + "/" + url;
							// }
							if (!url.startsWith("/")) {
								url = "/" + url;
							}
						}
						oneCMenuJson.put("url", url);
						oneCMenuJson
								.put("img", oneCMenu.getMenu().getImgName());
						oneCMenuJson.put("name", oneCMenu.getMenu()
								.getMenuName());
						menuCList.add(oneCMenuJson);
					}
				}
				oneMenuJson.put("sub", menuCList);
				menuList.add(oneMenuJson);
			}
		}
		return menuList;
	}

	// 得到应用
	public JSONArray getUserMenu(BSObject m_bs, LoginUserPojo user, int style)
			throws Exception {
		JSONArray menuList = new JSONArray();
		if (m_bs.getPublicMap().get("MY_MENULIST") != null) {
			menuList = JSONArray.fromObject(m_bs.getPublicMap().get(
					"MY_MENULIST"));
		}
		String isNew = (String) m_bs.getPrivateMap().get("isnewfun");
		if ((isNew != null && isNew.equals("1")) || menuList == null
				|| menuList.size() == 0) {

			// 更新应用列表
			SqlExecute sqlHelper = new SqlExecute();
			try {
				// 添加应用
				menuList = this._getWinApps(m_bs, user, 1, style, sqlHelper);
				sqlHelper.close();
			} catch (Exception ex) {
				sqlHelper.close();
				ex.printStackTrace();
			}
			// 存入公共变量里
			m_bs.setPublicValue("MY_MENULIST", menuList.toString());
		}

		return menuList;
	}

	// 得到应用
	public JSONArray getUserMenu(BSObject m_bs, String userInst, int style)
			throws Exception {
		JSONArray menuList = new JSONArray();
		// 更新应用列表
		SqlExecute sqlHelper = new SqlExecute();
		try {
			// 添加应用
			LoginUserPojo user = new LoginUserPojo();
			user.setUserInst(userInst);
			// 得到角色信息
			BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
			ArrayList<RolePojo> list = new ArrayList<RolePojo>();
			List<Object> vList = new ArrayList<Object>();
			vList.add(user.getUserInst());
			user.setRoleWhere(roleDB
					.getRoles(
							list,
							" and t.ROLE_ID in (select ROLE_ID from T_ROLE_USER_R where USER_INSTID=?)",
							vList));
			// 设置roleWhere
			String roleWhere = "";
			for (int i = 0, size = list.size(); i < size; i++) {
				roleWhere += (",'" + list.get(i).getId() + "'");
			}
			if (list.size() > 0) {
				roleWhere = roleWhere.substring(1);
			}
			user.setRoleWhere(roleWhere);
			menuList = this._getWinApps(m_bs, user, 1, style, sqlHelper);
			sqlHelper.close();
		} catch (Exception ex) {
			sqlHelper.close();
			ex.printStackTrace();
		}

		return menuList;
	}

	// 得到应用
	public JSONArray getUserMenuQD(BSObject m_bs, LoginUserPojo user, int style)
			throws Exception {
		JSONArray menuList = new JSONArray();
		if (m_bs.getPublicMap().get("MY_MENULIST_QD") != null) {
			menuList = JSONArray.fromObject(m_bs.getPublicMap().get(
					"MY_MENULIST_QD"));
		}
		String isNew = (String) m_bs.getPrivateMap().get("isnewfun");
		if ((isNew != null && isNew.equals("1")) || menuList == null
				|| menuList.size() == 0) {

			// 更新应用列表
			SqlExecute sqlHelper = new SqlExecute();
			try {
				// 添加应用
				menuList = this._getWinApps(m_bs, user, 1, style, sqlHelper);
				sqlHelper.close();
			} catch (Exception ex) {
				sqlHelper.close();
				ex.printStackTrace();
			}
			// 存入公共变量里
			m_bs.setPublicValue("MY_MENULIST_QD", menuList.toString());
		}

		return menuList;
	}

	// 效验是否超过用户最大数
	public static boolean checkUserMax(BSObject m_bs) {
		boolean isOut = true;
		BIRedis redisBI = new BIRedis();
		long userS = redisBI.getKeyCount(Integer.parseInt(BSCommon
				.getConfigValue("userconfig_loginobj_redisdb")));
		String userMax = BSCommon.getConfigValue("userconfig_loginobj_usermax");
		if (userMax == null || userMax.trim().equals("")) {
			userMax = "1000";
		}
		if (userS >= Integer.parseInt(userMax)) {
			isOut = true;
		} else {
			isOut = false;
		}
		return isOut;

	}

	public String setUserToSession(LoginUserPojo oneUser) throws Exception {
		// 从redis中得到
		BIRedis redisBI = new BIRedis();
		String ttkey = BSGuid.getRandomGUID();
		redisBI.setStringData(ttkey, JSONObject.fromObject(oneUser).toString(),
				Integer.parseInt(BSCommon
						.getConfigValue("userconfig_loginobj_session")),
				Integer.parseInt(BSCommon
						.getConfigValue("userconfig_loginobj_redisdb")));
		return ttkey;
	}

	public static LoginUserPojo getLoginUser(BSObject m_bs) {
		LoginUserPojo onePojo = null;
		String loginUserJSON = m_bs.getPublicMap().get(
				m_bs.getCheckLogin().getLogName());
		onePojo = (LoginUserPojo) JSONObject.toBean(
				JSONObject.fromObject(loginUserJSON), LoginUserPojo.class);
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: do_checkUser
	 * </p>
	 * <p>
	 * 方法功能描述: 用户密码和验证码效验
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 */
	public UserPojo getLoginUser(UserPojo inUser) throws Exception {
		UserPojo outUser = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			// 员工登录
			BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
			outUser = userDB.getOneUserByInstId(inUser.getInstId());
			if (outUser != null) {
				// 得到角色信息
				BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
				List<Object> vList = new ArrayList<Object>();
				vList.add(outUser.getInstId());
				outUser.setRoleList(roleDB
						.getRoles(
								" and t.ROLE_ID in (select ROLE_ID from T_ROLE_USER_R where USER_INSTID=?)",
								"", vList));
				// 设置roleWhere
				String roleWhere = "";
				for (RolePojo oneR : outUser.getRoleList()) {
					roleWhere += ("," + oneR.getId());
				}
				if (outUser.getRoleList().size() > 0) {
					roleWhere = roleWhere.substring(1);
				}
				outUser.setRoleWhere(roleWhere);
				// 得到所有orgId
				ArrayList<UserOrgRPojo> ugList = userDB.getUserOrgList(
						" and t.USER_INSTID=?", vList, "");
				for (UserOrgRPojo oneR : ugList) {
					String[] aos = oneR.getOrg().getAllOrgId().split(",");
					outUser.setGroupAllName(outUser.getGroupAllName() + ","
							+ oneR.getOrg().getAllName().replaceAll(",", "-"));
					for (String oneAO : aos) {
						if (!oneAO.equals("")) {
							if (outUser.getGroupAllId().indexOf("," + oneAO) < 0) {
								outUser.setGroupAllId(outUser.getGroupAllId()
										+ "," + oneAO);

							}
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			sqlHelper.close();
		}
		return outUser;
	}

	private LoginUserPojo _getUserById(BSUserDBMang userDB, String id)
			throws Exception {
		LoginUserPojo oneUser = null;
		UserPojo outUser = userDB.getOneUserById(id);
		if (outUser != null) {
			oneUser = new LoginUserPojo();
			oneUser.setUserInst(outUser.getInstId());
			oneUser.setUserId(outUser.getId());
			oneUser.setUserName(outUser.getName());
			oneUser.setSex(outUser.getSex());
			oneUser.setPassword(outUser.getPassword());
			oneUser.setPhone(outUser.getmPhone());
			oneUser.setEmail(outUser.getEmail());
			if (outUser.getOrg() != null) {
				oneUser.setGroupId("," + outUser.getOrg().getId());
				oneUser.setGroupAllId(outUser.getOrg().getAllOrgId());
			}
		}
		return oneUser;
	}

	// 效验是否超过用户最大数
	public long SysBaseIni() throws Exception {
		long count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			SysBaseDBMang sysDB = new SysBaseDBMang(sqlHelper, m_bs);
			count = sysDB.SysBaseIni();
			sqlHelper.commit();
		} catch (Exception ex) {
			count = 0;
			sqlHelper.rollback();
			ex.printStackTrace();
		}
		if (count > 0) {
			// 删除附件目录
			BIFile fileBI = new BIFile();
			fileBI.deleteDir((new File(BSCommon.getConfigValue("upload_path")
					+ "/fault")), false);
			fileBI.deleteDir((new File(BSCommon.getConfigValue("upload_path")
					+ "/fixlogs")), false);
			// 删除redis
			BIRedis redisBI = new BIRedis();
			redisBI.clearAll(URLlImplBase.REDIS_KULUDATA);
			// 新增运营公司
			CompanyPojo oneComp = new CompanyPojo();
			oneComp.setId(BSCommon.getConfigValue("userconfig_company_id"));
			oneComp.setName("新的运营商");
			oneComp.getArea().setId("110000");
			oneComp.setType(1);
			(new BICompany(null, null)).insertCompany(oneComp);

		}

		return count;
	}
}
