package tt.kulu.out.biss;

import java.util.ArrayList;

import org.apache.commons.fileupload.FileItem;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.dic.pojo.DicItemPojo;
import tt.kulu.bi.fault.pojo.FaultReportPojo;
import tt.kulu.bi.file.pojo.BFSFilePojo;
import tt.kulu.bi.truck.pojo.TruckPojo;
import tt.kulu.bi.user.pojo.LoginUserPojo;
import tt.kulu.out.call.BIDic;
import tt.kulu.out.call.BIFault;
import tt.kulu.out.call.BIFile;
import tt.kulu.out.call.BILogin;
import tt.kulu.out.call.BITruck;

import com.tt4j2ee.BSGuid;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BSFault
 * </p>
 * <p>
 * 功能描述: 故障Web接口类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * <p>
 * 创建日期: 2014-10-29
 * </p>
 */
public class BSFault {

	/**
	 * <p>
	 * 方法名：do_FaultIni
	 * </p>
	 * <p>
	 * 方法描述：得到车辆初始化信息页面：www/admin/role/index.html
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_FaultIni(BSObject m_bs) throws Exception {
		// 数据准备
		JSONObject fretObj = new JSONObject();
		BIDic dicBI = new BIDic(m_bs);
		// type
		JSONObject _paras = new JSONObject();
		_paras.put("dic", "T_FAULTREPORT_FR_TYPE");
		ArrayList<DicItemPojo> dicList = dicBI.getDicItemList(_paras);
		_paras.put("dic", "T_FAULTREPORT_FAULT_ID");
		JSONObject frtype = new JSONObject();
		for (DicItemPojo oneItem : dicList) {
			JSONObject oneI = JSONObject.fromObject(oneItem);
			_paras.put("pitemid", oneItem.getId());
			oneI.put("frfault", dicBI.getDicItemList(_paras));
			frtype.put(oneItem.getId(), oneI);
		}
		fretObj.put("frtype", frtype);
		// 来源
		_paras.put("dic", "T_FAULTREPORT_FR_FROM");
		fretObj.put("frfrome", dicBI.getDicItemList(_paras));

		fretObj.put("opstate", FaultReportPojo.STATE_NAME);

		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchFaultReportList
	 * </p>
	 * <p>
	 * 方法描述：得到故障信息页面：www/admin/role/index.html
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchFaultReportList(BSObject m_bs) throws Exception {
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		long startNum = 1;
		int pageNum = 0;
		int pageSize = 0;
		String sText = m_bs.getPrivateMap().get("pg_text");
		String state = m_bs.getPrivateMap().get("pg_state");
		String faultFrom = m_bs.getPrivateMap().get("pg_frfrom");
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
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
		startNum = pageSize * pageNum;
		BIFault faultBI = new BIFault(null, m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("key", sText);
		if (faultFrom != null) {
			_paras.put("from", faultFrom);
		}
		if (state != null) {
			_paras.put("state", state);
		}

		ArrayList<FaultReportPojo> list = faultBI.getFaultReportList(_paras,
				startNum, startNum + pageSize - 1);
		for (FaultReportPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("faule", onePojo.getFaultCode().getName());
			oneObj.put("type", onePojo.getFaultType().getName());
			oneObj.put("from", onePojo.getFaultFrom().getName());
			oneObj.put("hdate", onePojo.getHappenDate());
			oneObj.put("edate", onePojo.getEndDate());
			//
			oneObj.put("cuser", onePojo.getCreateUser().getName() + "["
					+ onePojo.getCreateUser().getmPhone() + "]");
			//
			oneObj.put("fruser",
					(!onePojo.getFrUser().getInstId().equals("") ? onePojo
							.getFrUser().getName()
							+ "["
							+ onePojo.getFrUser().getmPhone() + "]" : ""));
			oneObj.put("freqp",
					(!onePojo.getEqpInst().getInstId().equals("") ? onePojo
							.getEqpInst().getName()
							+ "["
							+ onePojo.getEqpInst().getWyCode() + "]" : ""));
			oneObj.put("frtruck", (!onePojo.getEqpInst().getTruck().getId()
					.equals("") ? onePojo.getEqpInst().getTruck().getName()
					+ "[" + onePojo.getEqpInst().getTruck().getPlateNum() + "]"
					: ""));
			//
			oneObj.put("opuser",
					(!onePojo.getOpUser().getInstId().equals("") ? onePojo
							.getOpUser().getName()
							+ "["
							+ onePojo.getOpUser().getId()
							+ "] "
							+ onePojo.getOpUser().getmPhone() : ""));
			oneObj.put("opstatev", onePojo.getOpState());
			oneObj.put("opstate",
					FaultReportPojo.STATE_NAME[onePojo.getOpState()]);
			oneObj.put("opdate", onePojo.getOpDate());

			retObj.add(oneObj);
		}
		fretObj.put("list", retObj);
		fretObj.put("max", _paras.getLong("max"));
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getFaultReportById
	 * </p>
	 * <p>
	 * 方法描述：根据数据字典ID得到一个数据字典
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_getFaultReportById(BSObject m_bs) throws Exception {
		String type = m_bs.getPrivateMap().get("in_type");
		JSONObject fretObj = new JSONObject();
		FaultReportPojo onePojo = new FaultReportPojo();
		if (type.equals("new")) {
			/** 新增 */
			onePojo.setId(BSGuid.getRandomGUID());
		} else if (type.equals("edit")) {
			/** 编辑 */
			onePojo = new BIFault(m_bs).getOneFaultReportById((String) m_bs
					.getPrivateMap().get("faultid"));
		}
		fretObj.put("id", onePojo.getId());
		fretObj.put("name", onePojo.getName());
		fretObj.put("content", onePojo.getContent());
		fretObj.put("faule", onePojo.getFaultCode().getId());
		fretObj.put("type", onePojo.getFaultType().getId());
		fretObj.put("from", onePojo.getFaultFrom().getId());
		fretObj.put("hdate", onePojo.getHappenDate());
		fretObj.put("edate", onePojo.getEndDate());
		//
		fretObj.put("cuser", onePojo.getCreateUser().getName() + "["
				+ onePojo.getCreateUser().getmPhone() + "]");
		// 关联用户
		if (onePojo.getFrUser() != null
				&& !onePojo.getFrUser().getInstId().equals("")) {
			fretObj.put("fruserinst", onePojo.getFrUser().getInstId());
			fretObj.put("fruser", onePojo.getFrUser().getName() + "["
					+ onePojo.getFrUser().getName() + "] "
					+ onePojo.getFrUser().getmPhone());
		}
		// 关联设备
		if (onePojo.getEqpInst() != null
				&& !onePojo.getEqpInst().getInstId().equals("")) {
			fretObj.put("freqpinst", onePojo.getEqpInst().getInstId());
			fretObj.put("freqp", onePojo.getEqpInst().getName());
		}
		// 关联车辆
		if (onePojo.getEqpInst() != null
				&& onePojo.getEqpInst().getTruck() != null
				&& !onePojo.getEqpInst().getTruck().getId().equals("")) {
			fretObj.put("frtruckid", onePojo.getEqpInst().getTruck().getId());
			fretObj.put("frtruck", onePojo.getEqpInst().getTruck().getName()
					+ "[" + onePojo.getEqpInst().getTruck().getPlateNum() + "]");
		}
		// 操作人
		if (onePojo.getOpUser() != null
				&& !onePojo.getOpUser().getInstId().equals("")) {
			fretObj.put("opuserinst", onePojo.getOpUser().getInstId());
			fretObj.put("opuser", onePojo.getOpUser().getName() + "["
					+ onePojo.getOpUser().getName() + "] "
					+ onePojo.getOpUser().getmPhone());
		}
		fretObj.put("opstate", onePojo.getOpState());
		fretObj.put("opdate", onePojo.getOpDate());
		fretObj.put("optext", onePojo.getOpDesc());
		// 文件
		JSONArray fileJSON = new JSONArray();
		for (BFSFilePojo oneFile : onePojo.getFaultFiles()) {
			JSONObject oneFileJSON = new JSONObject();
			oneFileJSON.put("type", oneFile.getType());
			oneFileJSON.put("fid", oneFile.getInstId());
			oneFileJSON.put("name", oneFile.getFileName());
			oneFileJSON.put("url", BIFile.GetImgURL(oneFile));
			fileJSON.add(oneFileJSON);
		}
		fretObj.put("ffiles", fileJSON);
		fileJSON.clear();
		for (BFSFilePojo oneFile : onePojo.getOpFiles()) {
			JSONObject oneFileJSON = new JSONObject();
			oneFileJSON.put("type", oneFile.getType());
			oneFileJSON.put("fid", oneFile.getInstId());
			oneFileJSON.put("name", oneFile.getFileName());
			oneFileJSON.put("url", BIFile.GetImgURL(oneFile));
			fileJSON.add(oneFileJSON);
		}
		fretObj.put("opfiles", fileJSON);
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateFaultReport
	 * </p>
	 * <p>
	 * 方法描述：保存故障报告
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updateFaultReport(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		String type = m_bs.getPrivateMap().get("in_type");
		FaultReportPojo onePojo = new FaultReportPojo();
		onePojo.setId(m_bs.getPrivateMap().get("t_faultid"));
		onePojo.setName(m_bs.getPrivateMap().get("t_faultname"));
		onePojo.setContent(m_bs.getPrivateMap().get("t_faulttext"));
		onePojo.setHappenDate(m_bs.getPrivateMap().get("t_faulthdate"));
		onePojo.setEndDate(m_bs.getPrivateMap().get("t_faultedate"));
		onePojo.getFaultType().setId(m_bs.getPrivateMap().get("s_frtype"));
		onePojo.getFaultCode().setId(m_bs.getPrivateMap().get("s_frfault"));
		onePojo.getFaultFrom().setId(m_bs.getPrivateMap().get("s_faultfrom"));
		onePojo.getCreateUser().setInstId(user.getUserInst());
		// 设备
		onePojo.getEqpInst()
				.setInstId(m_bs.getPrivateMap().get("t_faulteqp_v"));
		// 用户
		onePojo.getFrUser().setInstId(
				m_bs.getPrivateMap().get("t_faultfruser_v"));
		// 车辆
		onePojo.getEqpInst().getTruck()
				.setId(m_bs.getPrivateMap().get("t_faulttruck_v"));
		// 处理
		onePojo.setOpState(Integer.parseInt(m_bs.getPrivateMap().get(
				"s_faultopstate")));
		if (!m_bs.getPrivateMap().get("t_faultopuser_v").equals("")) {
			onePojo.getOpUser().setInstId(
					m_bs.getPrivateMap().get("t_faultopuser_v"));
			onePojo.setOpDate(m_bs.getPrivateMap().get("t_faultopdate"));
			onePojo.setOpDesc(m_bs.getPrivateMap().get("t_faultoptext"));
		}
		BIFault faultBI = new BIFault(null, m_bs);
		int count = 0;
		if (type.equals("new")) {
			count = faultBI.insertFaultReport(onePojo);
		} else if (type.equals("edit")) {
			count = faultBI.updateFaultReport(onePojo);
		}
		JSONObject fretObj = new JSONObject();
		fretObj.put("id", onePojo.getId());
		if (count > 0) {
			fretObj.put("r", 0);
			fretObj.put("id", onePojo.getId());
			// 上传图片
			ArrayList<FileItem> faultFiles = new ArrayList<FileItem>();
			ArrayList<FileItem> faultOpFiles = new ArrayList<FileItem>();
			for (int i = 0, fsize = m_bs.getFileList().size(); i < fsize; i++) {
				FileItem fi = (FileItem) m_bs.getFileList().get(i);
				if (fi.getFieldName().startsWith("f_faultfimg_")) {
					// 图片
					faultFiles.add(fi);
				} else if (fi.getFieldName().startsWith("f_faultopfimg_")) {
					// 图片
					faultOpFiles.add(fi);
				}
			}
			// 上传图片
			BIFile fileBI = new BIFile(null, m_bs);
			if (faultFiles.size() > 0) {
				fileBI.deleteFiles(onePojo.getId() + "_FILES", true);
				BFSFilePojo file = new BFSFilePojo();
				file.setUser(onePojo.getCreateUser());
				file.setInstId("");
				file.setOldId("");
				file.setBissId(onePojo.getId() + "_FILES");
				file.setType(1);
				file.setUpdateDate(m_bs.getDateEx().getThisDate(0, 0));
				file.setEditDate(file.getEditDate());
				file.setFileUrl("/fault/" + onePojo.getId());
				file.setFilePath(file.getFileUrl());
				fileBI.saveUploadUserFile(m_bs, file, BIFile.F_FAULT_CODE,
						faultFiles);
			}
			if (faultOpFiles.size() > 0) {
				fileBI.deleteFiles(onePojo.getId() + "_OPFILES", true);
				BFSFilePojo file = new BFSFilePojo();
				file.setUser(onePojo.getCreateUser());
				file.setInstId("");
				file.setOldId("");
				file.setBissId(onePojo.getId() + "_OPFILES");
				file.setType(1);
				file.setUpdateDate(m_bs.getDateEx().getThisDate(0, 0));
				file.setEditDate(file.getEditDate());
				file.setFileUrl("/fault/" + onePojo.getId());
				file.setFilePath(file.getFileUrl());
				fileBI.saveUploadUserFile(m_bs, file, BIFile.F_FAULT_OP,
						faultOpFiles);
			}
		} else {
			fretObj.put("r", 990);
		}
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}
}
