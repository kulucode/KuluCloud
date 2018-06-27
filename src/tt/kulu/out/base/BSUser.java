package tt.kulu.out.base;

import java.io.File;
import java.util.ArrayList;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.company.pojo.CompanyPojo;
import tt.kulu.bi.power.pojo.RolePojo;
import tt.kulu.bi.user.pojo.LoginUserPojo;
import tt.kulu.bi.user.pojo.OrgPojo;
import tt.kulu.bi.user.pojo.UserOrgRPojo;
import tt.kulu.bi.user.pojo.UserPojo;
import tt.kulu.out.call.BICompany;
import tt.kulu.out.call.BILogin;
import tt.kulu.out.call.BIUser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tt4j2ee.BSCommon;
import com.tt4j2ee.Const;
import com.tt4j2ee.m.BSObject;
import com.tt4j2ee.md5.MD5Imp;

/**
 * <p>
 * 标题: BSUser
 * </p>
 * <p>
 * 功能描述: 用户Web接口类
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
public class BSUser {
	/**
	 * <p>
	 * 方法名：do_UserIni
	 * </p>
	 * <p>
	 * 方法描述：搜索用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */

	public BSObject do_UserIni(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		// 当前运营商
		CompanyPojo onePojo = (new BICompany(null)).getThisCompanyByRedis();
		JSONObject compJ = new JSONObject();
		if (onePojo != null) {
			compJ.put("id", onePojo.getId());
			compJ.put("name", onePojo.getName());
		} else {
			onePojo = new CompanyPojo();
			compJ.put("id", "");
			compJ.put("name", "未设定运营商");
		}
		fretObj.put("comp", onePojo);
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getOrgTree
	 * </p>
	 * <p>
	 * 方法描述：机构树
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */

	public BSObject do_getOrgTree(BSObject m_bs) throws Exception {
		String pid = m_bs.getPrivateMap().get("in_pid");
		String type = m_bs.getPrivateMap().get("pg_type");
		String compId = m_bs.getPrivateMap().get("in_compid");
		if (pid == null) {
			pid = "";
		}
		// 数据准备
		JSONObject fretObj = new JSONObject();
		JSONArray listObj = new JSONArray();
		// 得到一级单位
		JSONObject paras = new JSONObject();
		if (pid.equals("") || pid.equals("root")) {
			paras.put("root", "root");
		} else {
			paras.put("sub", pid);
		}
		if (compId != null) {
			paras.put("comp", compId);
		}
		if (type != null) {
			paras.put("type", type);
		}
		BIUser userBI = new BIUser(null, m_bs);
		ArrayList<OrgPojo> list = userBI.getOrgList(paras, 0, 100);
		for (OrgPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			// oneObj.put("code", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("desc", onePojo.getDesc());
			oneObj.put("allname", onePojo.getAllName().replaceAll(",", "-"));
			oneObj.put("allid", onePojo.getAllOrgId());
			oneObj.put("pid", onePojo.getPorgId());
			oneObj.put("pname", onePojo.getPorgName());
			oneObj.put("type", onePojo.getType());
			oneObj.put("cnum", onePojo.getSubOrgNum());
			oneObj.put("snum", onePojo.getUserNum());
			if (onePojo.getSubOrgNum() > 0) {
				paras.put("root", "");
				paras.put("sub", onePojo.getId());
				oneObj.put("children", this._getChildOrgList(userBI, paras));
			}
			listObj.add(oneObj);
		}
		fretObj.put("list", listObj);
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getOrgList
	 * </p>
	 * <p>
	 * 方法描述：机构树
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */

	public BSObject do_getOrgList(BSObject m_bs) throws Exception {
		String pid = m_bs.getPrivateMap().get("in_pid");
		String compId = m_bs.getPrivateMap().get("in_compid");
		String userInst = m_bs.getPrivateMap().get("pg_user");
		// 数据准备
		JSONObject fretObj = new JSONObject();
		JSONArray listObj = new JSONArray();
		// 得到一级单位
		JSONObject paras = new JSONObject();
		if (pid != null) {
			paras.put("root", pid);
		}
		if (compId != null) {
			paras.put("comp", compId);
		}
		if (userInst != null) {
			paras.put("user", userInst);
		}
		BIUser userBI = new BIUser(null, m_bs);
		ArrayList<OrgPojo> list = userBI.getOrgList(paras, 0, 100);
		for (OrgPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("desc", onePojo.getDesc());
			oneObj.put("allname", onePojo.getAllName().replaceAll(",", "-"));
			oneObj.put("allid", onePojo.getAllOrgId());
			oneObj.put("pid", onePojo.getPorgId());
			oneObj.put("vtype", onePojo.getType());
			oneObj.put("type", OrgPojo.TYPE_NAME[onePojo.getType()]);
			oneObj.put("pname", onePojo.getPorgName());
			oneObj.put("cnum", onePojo.getSubOrgNum());
			oneObj.put("snum", onePojo.getUserNum());

			listObj.add(oneObj);
		}
		fretObj.put("list", listObj);
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchOrgLookUp
	 * </p>
	 * <p>
	 * 方法描述：机构树
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */

	public BSObject do_searchOrgLookUp(BSObject m_bs) throws Exception {
		String text = m_bs.getPrivateMap().get("pg_text");
		String pid = m_bs.getPrivateMap().get("in_pid");
		String compId = m_bs.getPrivateMap().get("in_compid");
		String type = m_bs.getPrivateMap().get("pg_type");
		String userInst = m_bs.getPrivateMap().get("pg_user");
		// 数据准备
		JSONObject fretObj = new JSONObject();
		JSONArray listObj = new JSONArray();
		// 得到一级单位
		JSONObject paras = new JSONObject();
		paras.put("key", text);
		if (pid != null) {
			paras.put("root", pid);
		}
		if (compId != null) {
			paras.put("comp", compId);
		}
		if (userInst != null) {
			paras.put("muser", userInst);
		}
		if (type != null) {
			paras.put("type", type);
		}
		BIUser userBI = new BIUser(null, m_bs);
		ArrayList<OrgPojo> list = userBI.getOrgList(paras, 0, 20);
		for (OrgPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("value", onePojo.getId());
			oneObj.put("label", onePojo.getAllName().replaceAll(",", "-"));
			listObj.add(oneObj);
		}
		fretObj.put("list", listObj);
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getOneOrg
	 * </p>
	 * <p>
	 * 方法描述：得到单个机构
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_getOneOrg(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		String type = m_bs.getPrivateMap().get("in_type");
		OrgPojo oneOrg = new OrgPojo();
		if (type.equals("new")) {
			/** 新增 */
			oneOrg.setId("");
		} else if (type.equals("edit")) {
			/** 编辑 */
			oneOrg = new BIUser(null, m_bs).getOneOrgById(m_bs.getPrivateMap()
					.get("orgid"));
		}
		fretObj.put("id", oneOrg.getId());
		fretObj.put("name", oneOrg.getName());
		fretObj.put("desc", oneOrg.getDesc());
		fretObj.put("allname", oneOrg.getAllName().replaceAll(",", "-"));
		fretObj.put("pid", oneOrg.getPorgId());
		fretObj.put("pname", oneOrg.getPorgName());
		if (oneOrg.getId().equals(oneOrg.getPorgId())) {
			fretObj.put("pid", "");
			fretObj.put("pname", "无");
		}
		fretObj.put("type", oneOrg.getType());
		fretObj.put("lat", oneOrg.getLatitude());
		fretObj.put("lon", oneOrg.getLongitude());
		if (oneOrg.getArea() != null) {
			fretObj.put("area", oneOrg.getArea().getId());
			fretObj.put("areaname",
					oneOrg.getArea().getAllName().replaceAll(",", "-"));
		} else {
			fretObj.put("area", "");
			fretObj.put("areaname", "");
		}

		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getOneOrgByUser
	 * </p>
	 * <p>
	 * 方法描述：得到单个机构
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_getOneOrgByUser(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		String userInst = m_bs.getPrivateMap().get("pg_user");
		OrgPojo oneOrg = new BIUser(null, m_bs).getOneOrgByUser(userInst);
		if (oneOrg != null) {
			fretObj.put("id", oneOrg.getId());
			fretObj.put("name", oneOrg.getName());
			fretObj.put("allname", oneOrg.getAllName().replaceAll(",", "-"));
		} else {
			fretObj.put("id", "");
			fretObj.put("name", "");
			fretObj.put("allname", "");
		}

		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateOneOrg
	 * </p>
	 * <p>
	 * 方法描述：更新单个机构
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updateOneOrg(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", 990);
		String type = m_bs.getPrivateMap().get("in_type");
		OrgPojo oneOrg = this._getOrgFromWeb(m_bs);
		BIUser userBI = new BIUser(null, m_bs);
		int count = 0;
		if (type.equals("new")) {
			oneOrg.getCreateStaff().setInstId(user.getUserInst());
			oneOrg.setCreateDate(m_bs.getDateEx().getThisDate(0, 0));
			count = userBI.insertOrg(oneOrg);
		} else if (type.equals("edit")) {
			count = userBI.updateOrg(oneOrg);
		}
		if (count >= 0) {
			fretObj.put("r", 0);
			JSONObject funcObj = new JSONObject();
			funcObj.put("id", oneOrg.getId());
			funcObj.put("name", oneOrg.getName());
			fretObj.put("data", funcObj);
		}
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_delOneOrg
	 * </p>
	 * <p>
	 * 方法描述：更新单个机构
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_delOneOrg(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", 990);
		BIUser userBI = new BIUser(null, m_bs);
		int count = userBI.deleteOneOrg(m_bs.getPrivateMap().get("orgid"));
		if (count >= 0) {
			fretObj.put("r", 0);
		}
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchUserLookUp
	 * </p>
	 * <p>
	 * 方法描述：搜索用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 * o
	 */

	public BSObject do_searchUserLookUp(BSObject m_bs) throws Exception {
		// 数据准备
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		BIUser userBI = new BIUser(null, m_bs);
		JSONObject paras = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_text");
		paras.put("key", sText);
		ArrayList<UserPojo> list = userBI.getUserList(paras, 0, 20);
		for (UserPojo oneUser : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("value", oneUser.getInstId());
			oneObj.put("label", oneUser.getName() + "[" + oneUser.getId()
					+ "] " + oneUser.getmPhone());
			retObj.add(oneObj);
		}
		fretObj.put("list", retObj);
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchUser
	 * </p>
	 * <p>
	 * 方法描述：搜索用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */

	public BSObject do_searchUser(BSObject m_bs) throws Exception {
		// 数据准备
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		long startNum = 1;
		int pageNum = 0;
		int pageSize = 0;
		BIUser userBI = new BIUser(null, m_bs);
		JSONObject paras = new JSONObject();
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
		String sText = m_bs.getPrivateMap().get("pg_text");
		String role = m_bs.getPrivateMap().get("pg_role");
		String state = m_bs.getPrivateMap().get("pg_state");
		String group = m_bs.getPrivateMap().get("pg_group");
		String notFance = m_bs.getPrivateMap().get("pg_notfance");
		String inFance = m_bs.getPrivateMap().get("pg_infance");
		String havePhone = m_bs.getPrivateMap().get("pg_hasphone");
		String notTruck = m_bs.getPrivateMap().get("pg_nottruck");
		paras.put("key", sText);

		if (role != null) {
			paras.put("role", role.replaceAll(",", "','"));
		}
		if (group != null) {
			paras.put("group", group);
		}
		if (state != null) {
			paras.put("state", state);
		}
		if (notTruck != null) {
			paras.put("nottruck", notTruck);
		}
		if (notFance != null) {
			paras.put("notfance", notFance);
		}
		if (inFance != null) {
			paras.put("infance", inFance);
		}
		if (pageNumStr != null) {
			pageNum = Integer.parseInt(pageNumStr);
		} else {
			pageNum = 0;
		}
		if (pageNum < 0) {
			pageNum = 0;
		}
		if (pageSizeStr != null) {
			pageSize = Integer.parseInt(pageSizeStr);
		} else {
			pageSize = 20;
		}
		if (havePhone != null && havePhone.equals("1")) {
			paras.put("havephone", 1);
		}

		startNum = pageSize * pageNum;
		ArrayList<UserPojo> list = userBI.getUserList(paras, startNum, startNum
				+ pageSize - 1);
		for (UserPojo oneUser : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("instid", oneUser.getInstId());
			oneObj.put("id", oneUser.getId());
			oneObj.put("state", oneUser.getState());
			oneObj.put("name", oneUser.getName());
			oneObj.put("sex", UserPojo.SEX_NAME[oneUser.getSex()]);
			oneObj.put("phone", oneUser.getmPhone());
			oneObj.put("link",
					oneUser.getmPhone() + "<br/>" + oneUser.getEmail());
			if (oneUser.getOrg() != null) {
				oneObj.put("orgname", oneUser.getOrg().getName());
				oneObj.put("orgallname", oneUser.getOrg().getAllName()
						.replaceAll(",", "-"));
			}
			retObj.add(oneObj);
		}
		fretObj.put("data", retObj);
		fretObj.put("max", paras.getLong("max"));
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getOneUser
	 * </p>
	 * <p>
	 * 方法描述：得到一个用户初始化信息
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_getOneUser(BSObject m_bs) throws Exception {
		JSONObject retObj = new JSONObject();
		String instid = m_bs.getPrivateMap().get("instid");
		String type = m_bs.getPrivateMap().get("in_type");
		UserPojo oneUser = new UserPojo();
		if (type.equals("edit")) {
			BIUser userBI = new BIUser(null, m_bs);
			oneUser = userBI.getOneUserByInstId(instid);
		}
		// 基本信息
		retObj.put("instid", oneUser.getInstId());
		retObj.put("id", oneUser.getId());
		retObj.put("name", oneUser.getName());
		retObj.put("email", oneUser.getEmail());
		retObj.put("phone", oneUser.getmPhone());
		retObj.put("sex", oneUser.getSex());
		retObj.put("state", oneUser.getState());
		retObj.put("cdate", oneUser.getCreateDate());
		retObj.put("idcard", oneUser.getIdCard());
		retObj.put("sbflg", oneUser.getSbFlg());
		retObj.put("birthday", (oneUser.getBirthday().length() >= 10) ? oneUser
				.getBirthday().substring(0, 10) : oneUser.getBirthday());
		if (oneUser.getOrg() != null) {
			retObj.put("orgid", oneUser.getOrg().getId());
			retObj.put("orgname", oneUser.getOrg().getName());
			retObj.put("orgallname",
					oneUser.getOrg().getAllName().replaceAll(",", "-"));
		}
		// 下拉框
		retObj.put("sexsel", UserPojo.SEX_NAME);
		retObj.put("statesel", UserPojo.STATE_NAME);

		retObj.put("r", 0);
		retObj.put("error", URLlImplBase.ErrorMap.get(retObj.getInt("r")));
		m_bs.setRetrunObj(retObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchUserOrgList
	 * </p>
	 * <p>
	 * 方法描述：搜索用户机构关系
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */

	public BSObject do_searchUserOrgList(BSObject m_bs) throws Exception {
		// 数据准备
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		BIUser userBI = new BIUser(null, m_bs);
		JSONObject paras = new JSONObject();
		String userInst = m_bs.getPrivateMap().get("pg_userinstid");
		String orgId = m_bs.getPrivateMap().get("pg_orgid");
		String type = m_bs.getPrivateMap().get("pg_type");

		if (userInst != null) {
			paras.put("user", userInst);
		}
		if (orgId != null) {
			paras.put("group", orgId);
		}
		if (type != null) {
			paras.put("type", type);
		}

		ArrayList<UserOrgRPojo> list = userBI.getUserOrgList(paras);
		for (UserOrgRPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("userinstid", onePojo.getUser().getInstId());
			oneObj.put("userid", onePojo.getUser().getId());
			oneObj.put("orgid", onePojo.getOrg().getId());
			oneObj.put("orgname", onePojo.getOrg().getName());
			oneObj.put("orgallname",
					(onePojo.getOrg().getAllName()).replaceAll(",", "-"));
			oneObj.put("type", UserOrgRPojo.TYPE_NAME[onePojo.getType()]);
			retObj.add(oneObj);
		}
		fretObj.put("data", retObj);
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_selectUserOrg
	 * </p>
	 * <p>
	 * 方法描述：搜索用户机构关系
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */

	public BSObject do_selectUserOrg(BSObject m_bs) throws Exception {
		// 数据准备
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", 990);
		BIUser userBI = new BIUser(null, m_bs);
		String userInst = m_bs.getPrivateMap().get("pg_userinstid");
		String orgId = m_bs.getPrivateMap().get("pg_org");
		UserOrgRPojo onePojo = new UserOrgRPojo(userInst, orgId, 1);
		if (userBI.selectUserOrg(onePojo, 1) > 0) {
			fretObj.put("r", 0);
		}

		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_deleteUserOrg
	 * </p>
	 * <p>
	 * 方法描述：搜索用户机构关系
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */

	public BSObject do_deleteUserOrg(BSObject m_bs) throws Exception {
		// 数据准备
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", 990);
		BIUser userBI = new BIUser(null, m_bs);
		String userInst = m_bs.getPrivateMap().get("pg_userinstid");
		String orgId = m_bs.getPrivateMap().get("pg_org");
		UserOrgRPojo onePojo = new UserOrgRPojo(userInst, orgId, 1);
		if (userBI.selectUserOrg(onePojo, 0) > 0) {
			fretObj.put("r", 0);
		}

		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateUser
	 * </p>
	 * <p>
	 * 方法描述：得到用户管理初始化信息 页面：cdoom/user/index.jsp
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */

	public BSObject do_updateUser(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", 990);
		String type = m_bs.getPrivateMap().get("in_type");

		UserPojo oneUser = this._getUserFromWeb(m_bs);
		int count = 0;
		BIUser userBI = new BIUser(null, m_bs);
		if ("new".equals(type)) {
			oneUser.setPassword("000000");
			oneUser.getRoleList().add(new RolePojo("BASE_ROLE", "", ""));
			count = userBI.insertUser(oneUser);
		} else if ("edit".equals(type)) {
			oneUser.setInstId(m_bs.getPrivateMap().get("t_instid"));
			count = userBI.updateUser(oneUser);
		}
		if (count > 0) {
			fretObj.put("r", 0);
			fretObj.put("instid", oneUser.getInstId());
		}
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getOneUser
	 * </p>
	 * <p>
	 * 方法描述：得到一个用户初始化信息
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_deleteOneUser(BSObject m_bs) throws Exception {
		JSONObject retObj = new JSONObject();
		retObj.put("r", 0);
		String instid = m_bs.getPrivateMap().get("pg_inst");
		String type = m_bs.getPrivateMap().get("pg_type");
		int count = 0;
		BIUser userBI = new BIUser(null, m_bs);
		if (type.equals("delete")) {
			// 物理删除
			// count = userBI.deleteOneUser(instid);
		} else if (type.equals("state")) {
			// 逻辑删除
			count = userBI.updateOneUserState(instid, 0);
		} else if (type.equals("reset")) {
			// 还原
			count = userBI.updateOneUserState(instid, 1);
		}
		if (count > 0) {
			retObj.put("r", 0);
		}
		retObj.put("error", URLlImplBase.ErrorMap.get(retObj.getInt("r")));
		m_bs.setRetrunObj(retObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名称: do_changeMyKey
	 * </p>
	 * <p>
	 * 方法功能描述: 修改自己的密码
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 */
	public BSObject do_changeMyKey(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", 990);
		String oldUserKey = m_bs.getPrivateMap().get("thisUserKey");
		String new_userkey = m_bs.getPrivateMap().get("oneUserKey");
		// 判断旧密码是否一致
		if (user.getPassword().equals(MD5Imp.enCode(oldUserKey.trim()))) {
			BIUser userBI = new BIUser(null, m_bs);
			userBI.updateUserKey(user.getUserInst(), MD5Imp.enCode(new_userkey));
			fretObj.put("r", 0);
		} else {
			fretObj.put("r", 24);
		}
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名称: do_changeMyKey
	 * </p>
	 * <p>
	 * 方法功能描述: 修改自己的密码
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 */
	public BSObject do_changeKey(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", 990);
		String user = m_bs.getPrivateMap().get("in_uid");
		String new_userkey = m_bs.getPrivateMap().get("t_userkey");
		// 判断旧密码是否一致
		BIUser userBI = new BIUser(null, m_bs);
		userBI.updateUserKey(user, MD5Imp.enCode(new_userkey));
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchSellerList
	 * </p>
	 * <p>
	 * 方法描述：搜索商家
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchUserLookup(BSObject m_bs) throws Exception {
		JSONObject retJSON = new JSONObject();
		String sText = m_bs.getPrivateMap().get("inkey");
		JSONObject paras = new JSONObject();
		if (sText != null) {
			paras.put("key", sText);
		}
		// 调用BI
		BIUser userBI = new BIUser(null, m_bs);
		// 返回数据
		JSONArray userlist = new JSONArray();
		ArrayList<UserPojo> list = userBI.getUserList(paras, 1, 10);
		for (UserPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("label", onePojo.getName() + "[" + onePojo.getId() + "]");
			oneObj.put("value", onePojo.getInstId());
			userlist.add(oneObj);
		}
		retJSON.put("list", userlist);
		retJSON.put("r", 0);
		retJSON.put("error", URLlImplBase.ErrorMap.get(retJSON.getInt("r")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名称: do_exportUserList
	 * </p>
	 * <p>
	 * 方法功能描述:导出用户报表统计。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public BSObject do_exportUserList(BSObject m_bs) throws Exception {
		JSONObject retJSON = new JSONObject();
		String filepath = BSCommon.getConfigValue("upload_path") + "/report/";
		String fileName = "user_" + m_bs.getDateEx().getSeqDate() + ".xls";
		JSONObject paras = new JSONObject();
		paras.put("state", 1);
		WritableWorkbook workbook = null;
		try {
			(new File(filepath)).mkdirs();
			workbook = Workbook.createWorkbook(new File(filepath + "/"
					+ fileName));
			// 定义xls式样
			WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 12);
			WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 14,
					WritableFont.BOLD);
			WritableFont BoldFont2 = new WritableFont(WritableFont.ARIAL, 13,
					WritableFont.BOLD);
			WritableCellFormat wcf_title = new WritableCellFormat(BoldFont);
			wcf_title.setBorder(Border.ALL, BorderLineStyle.THIN);
			wcf_title.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf_title.setAlignment(Alignment.CENTRE);
			wcf_title.setWrap(true);

			WritableCellFormat wcf_center = new WritableCellFormat(NormalFont);
			wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN);
			wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf_center.setAlignment(Alignment.CENTRE);
			wcf_center.setWrap(true);

			WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
			wcf_left.setBorder(Border.ALL, BorderLineStyle.THIN);
			wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf_left.setAlignment(Alignment.LEFT);
			wcf_left.setWrap(true);

			WritableCellFormat wcf_boldFont = new WritableCellFormat(BoldFont2);
			wcf_boldFont.setBorder(Border.ALL, BorderLineStyle.THIN);
			wcf_boldFont.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf_boldFont.setAlignment(Alignment.CENTRE);
			wcf_boldFont.setWrap(true);
			//
			WritableSheet sheet = null;
			sheet = workbook.createSheet("员工列表", 0);
			// 列行合并
			sheet.mergeCells(0, 0, 10, 0);// x,y
			// 设置表头
			sheet.setRowView(0, 700);
			sheet.setRowView(1, 600);
			sheet.setRowView(2, 600);
			sheet.addCell(new Label(0, 0, "员工列表", wcf_boldFont));
			sheet.setColumnView(0, 10);
			sheet.setColumnView(1, 40);
			sheet.setColumnView(2, 18);
			sheet.setColumnView(3, 18);
			sheet.setColumnView(4, 15);
			sheet.setColumnView(5, 30);
			sheet.setColumnView(6, 30);
			sheet.setColumnView(7, 20);
			sheet.setColumnView(8, 30);
			sheet.setColumnView(9, 30);
			sheet.setColumnView(10, 60);
			sheet.addCell(new Label(0, 1, "ID", wcf_title));
			sheet.addCell(new Label(1, 1, "机构", wcf_title));
			sheet.addCell(new Label(2, 1, "员工编号", wcf_title));
			sheet.addCell(new Label(3, 1, "姓名", wcf_title));
			sheet.addCell(new Label(4, 1, "年龄", wcf_title));
			sheet.addCell(new Label(5, 1, "联系方式", wcf_title));
			sheet.addCell(new Label(6, 1, "身份证", wcf_title));
			sheet.addCell(new Label(7, 1, "角色", wcf_title));
			sheet.addCell(new Label(8, 1, "前端功能", wcf_title));
			sheet.addCell(new Label(9, 1, "后端功能", wcf_title));
			sheet.addCell(new Label(10, 1, "监管机构", wcf_title));
			// 调用BI
			BIUser userBI = new BIUser(null, m_bs);
			BILogin loginBI = new BILogin(null, m_bs);
			// 返回数据
			JSONArray retlist = new JSONArray();
			ArrayList<UserPojo> list = userBI.getUserList(paras, 0, -1);
			int i = 2;
			for (UserPojo onePojo : list) {
				onePojo = loginBI.getLoginUser(onePojo);
				if (onePojo != null) {
					int rowH = 2;
					int age = 0;
					if (!onePojo.getBirthday().equals("")) {
						age = URLlImplBase.getAge(onePojo.getBirthday());
					}
					String org = "";
					if (onePojo.getOrg() != null) {
						org = onePojo.getOrg().getAllName()
								.replaceAll(",", "-");
					}
					// 角色
					String roles = "";
					if (onePojo.getRoleList() != null) {
						if (onePojo.getRoleList().size() > rowH) {
							rowH = onePojo.getRoleList().size();
						}
						for (RolePojo oneRole : onePojo.getRoleList()) {
							roles += ((!roles.equals("") ? "\r\n" : "") + oneRole
									.getName());
						}
					}
					sheet.addCell(new Label(0, i,
							String.valueOf(retlist.size() + 1), wcf_center));
					sheet.addCell(new Label(1, i, org, wcf_center));
					sheet.addCell(new Label(2, i, onePojo.getId(), wcf_center));
					sheet.addCell(new Label(3, i, onePojo.getName(), wcf_center));
					sheet.addCell(new Label(4, i, String.valueOf(age),
							wcf_center));
					sheet.addCell(new Label(5, i, onePojo.getmPhone(),
							wcf_center));
					sheet.addCell(new Label(6, i, onePojo.getIdCard(),
							wcf_center));
					sheet.addCell(new Label(7, i, roles, wcf_center));
					// 前端功能
					String funName = "";
					JSONArray ms = loginBI.getUserMenu(m_bs,
							onePojo.getInstId(), 1);
					int rowF = 0;
					for (int r = 0, rsize = ms.size(); r < rsize; r++, rowF++) {
						JSONObject oneF = ms.getJSONObject(r);
						funName += ((!funName.equals("") ? "\r\n" : "") + oneF
								.getString("name"));
						if (oneF.containsKey("sub")) {
							JSONArray oneFSList = oneF.getJSONArray("sub");
							for (int s = 0, ssize = oneFSList.size(); s < ssize; s++, rowF++) {
								JSONObject oneFS = oneFSList.getJSONObject(s);
								funName += ("\r\n  --" + oneFS
										.getString("name"));
							}
						}
					}
					if (rowF > rowH) {
						rowH = rowF;
					}
					sheet.addCell(new Label(8, i, funName, wcf_left));
					// 后端功能
					rowF = 0;
					funName = "";
					ms = loginBI.getUserMenu(m_bs, onePojo.getInstId(), 0);
					for (int r = 0, rsize = ms.size(); r < rsize; r++, rowF++) {
						JSONObject oneF = ms.getJSONObject(r);
						funName += ((!funName.equals("") ? "\r\n" : "") + oneF
								.getString("name"));
						if (oneF.containsKey("sub")) {
							JSONArray oneFSList = oneF.getJSONArray("sub");
							for (int s = 0, ssize = oneFSList.size(); s < ssize; s++, rowF++) {
								JSONObject oneFS = oneFSList.getJSONObject(s);
								funName += ("\r\n  --" + oneFS
										.getString("name"));
							}
						}
					}
					if (rowF > rowH) {
						rowH = rowF;
					}
					sheet.addCell(new Label(9, i, funName, wcf_left));
					// 监管机构
					String orgs = "";
					String[] orgList = onePojo.getGroupAllName().split(",");
					for (String oneO : orgList) {
						if (!oneO.equals("")) {
							orgs += ((!orgs.equals("") ? "\r\n" : "") + oneO);
						}
					}
					if (orgList.length - 1 > rowH) {
						rowH = orgList.length - 1;
					}
					sheet.addCell(new Label(10, i, orgs, wcf_left));
					if (rowH < 2) {
						rowH = 2;
					}
					sheet.setRowView(i, rowH * 300);
					i++;
				}
			}
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			if (workbook != null) {
				workbook.write();
				workbook.close();
			}
			if (!filepath.equals("")) {
				retJSON.put("path", "/files/report/" + fileName);
				m_bs.setPrivateValue(Const.BS_DOWNLOAD_DELETE_FLAG, "true");
				m_bs.setPrivateValue(Const.BS_DOWNLOAD_FILE, filepath + "/"
						+ fileName);
				File file = new File(filepath + fileName);
				file.setExecutable(true, false);// 设置可执行权限
				file.setReadable(true, false);// 设置可读权限
				file.setWritable(true, false);// 设置可写权限
			}
		}
		retJSON.put("code", 0);
		retJSON.put("msg", URLlImplBase.ErrorMap.get(retJSON.getInt("code")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;

	}

	// 设置编辑页面
	private UserPojo _getUserFromWeb(BSObject m_bs) throws Exception {
		// 基本信息
		UserPojo onePojo = new UserPojo();
		onePojo.setInstId(m_bs.getPrivateMap().get("t_instid"));
		// 工号
		onePojo.setId(m_bs.getPrivateMap().get("t_userid"));
		// 姓名
		onePojo.setName(m_bs.getPrivateMap().get("t_uname"));
		// 邮箱
		onePojo.setEmail(m_bs.getPrivateMap().get("t_uemail"));
		// 电话号码
		onePojo.setmPhone(m_bs.getPrivateMap().get("t_uphone"));
		// 是否参保
		onePojo.setSbFlg(Integer.parseInt(m_bs.getPrivateMap().get("t_usbflg")));
		// 机构
		onePojo.setOrg(new OrgPojo());
		onePojo.getOrg().setId(m_bs.getPrivateMap().get("t_uorg_v"));
		// 性别
		onePojo.setSex(Integer.parseInt(m_bs.getPrivateMap().get("t_usex")));
		// 身份证
		onePojo.setIdCard(m_bs.getPrivateMap().get("t_idcard"));
		// 生日
		onePojo.setBirthday(m_bs.getPrivateMap().get("t_birthday"));
		if (onePojo.getBirthday().length() >= 10) {
			onePojo.setBirthday(onePojo.getBirthday().substring(0, 10)
					+ " 00:00:00");
		}
		// // 状态
		// onePojo.setState(Integer.parseInt(m_bs.getPrivateMap().get(
		// "t_state")));
		// 角色
		onePojo.setRoleList(new ArrayList<RolePojo>());
		if (m_bs.getPrivateMap().get("t_roles") != null) {
			String roles[] = (m_bs.getPrivateMap().get("t_roles")).split(",");
			for (String oneR : roles) {
				if (!oneR.equals("")) {
					onePojo.getRoleList().add(new RolePojo(oneR, "", ""));
				}
			}
		}
		return onePojo;
	}

	private JSONArray _getChildOrgList(BIUser userBI, JSONObject paras)
			throws Exception {
		JSONArray orgs = new JSONArray();
		ArrayList<OrgPojo> list = userBI.getOrgList(paras, 0, 100);
		for (OrgPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("desc", onePojo.getDesc());
			oneObj.put("allname", onePojo.getAllName().replaceAll(",", "-"));
			oneObj.put("allid", onePojo.getAllOrgId());
			oneObj.put("pid", onePojo.getPorgId());
			oneObj.put("pname", onePojo.getPorgName());
			oneObj.put("cnum", onePojo.getSubOrgNum());
			oneObj.put("snum", onePojo.getUserNum());
			oneObj.put("type", onePojo.getType());
			if (onePojo.getSubOrgNum() > 0) {
				paras.put("root", "");
				paras.put("sub", onePojo.getId());
				oneObj.put("children", this._getChildOrgList(userBI, paras));
			}
			orgs.add(oneObj);
		}
		return orgs;
	}

	/** 设置编辑页面 */
	private OrgPojo _getOrgFromWeb(BSObject m_bs) throws Exception {
		// 基本信息
		OrgPojo onePojo = new OrgPojo();
		onePojo.setId(m_bs.getPrivateMap().get("t_orgid"));
		onePojo.setName(m_bs.getPrivateMap().get("t_orgname"));
		onePojo.getCompany().setId(m_bs.getPrivateMap().get("t_orgcomp"));
		onePojo.getArea().setId(m_bs.getPrivateMap().get("t_areaid_v"));
		onePojo.setDesc(m_bs.getPrivateMap().get("t_orgdesc"));
		onePojo.setPorgId(m_bs.getPrivateMap().get("t_porgid"));
		onePojo.setType(Integer.parseInt(m_bs.getPrivateMap().get("s_orgtype")));
		onePojo.setLatitude(m_bs.getPrivateMap().get("t_orglat"));
		onePojo.setLongitude(m_bs.getPrivateMap().get("t_orglon"));
		return onePojo;
	}

}
