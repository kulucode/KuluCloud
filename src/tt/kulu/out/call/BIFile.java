package tt.kulu.out.call;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import tt.kulu.bi.area.pojo.AreaPojo;
import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.dic.dbclass.StaticDBMang;
import tt.kulu.bi.dic.pojo.DicItemPojo;
import tt.kulu.bi.file.biclass.ResizeImage;
import tt.kulu.bi.file.biclass.SFTPThread;
import tt.kulu.bi.file.dbclass.BSFileDBMang;
import tt.kulu.bi.file.pojo.BFSFilePojo;
import tt.kulu.bi.logs.biclass.SysLogsBIMang;
import tt.kulu.bi.logs.pojo.SysLogsPojo;
import tt.kulu.bi.power.dbclass.BSPowerDBMang;
import tt.kulu.bi.power.pojo.RolePojo;
import tt.kulu.bi.power.pojo.RoleUserPojo;
import tt.kulu.bi.storage.dbclass.EquipmentDBMang;
import tt.kulu.bi.storage.pojo.EquipmentDefPojo;
import tt.kulu.bi.storage.pojo.EquipmentInstPojo;
import tt.kulu.bi.truck.pojo.TruckDefinePojo;
import tt.kulu.bi.truck.pojo.TruckPojo;
import tt.kulu.bi.user.dbclass.BSUserDBMang;
import tt.kulu.bi.user.pojo.OrgPojo;
import tt.kulu.bi.user.pojo.UserPojo;

import com.tt4j2ee.BSCommon;
import com.tt4j2ee.BSDateEx;
import com.tt4j2ee.BSGuid;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

public class BIFile extends BSDBBase {
	// 用户
	public final static int F_USER_HEAD = 0;// 用户头像

	// 报警
	public final static int F_FAULT_CODE = 10;
	public final static int F_FAULT_OP = 11;
	// 维护日志
	public final static int F_FIXLOGS = 30;

	public BIFile() throws Exception {

	}

	public BIFile(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getFileList
	 * </p>
	 * <p>
	 * 方法功能描述:得到文件列表
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<BFSFilePojo> getFileList(JSONObject paras, long f, long t)
			throws Exception {
		ArrayList<BFSFilePojo> list = new ArrayList<BFSFilePojo>();
		// 得到文件列表
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getFileList(sqlHelper, paras, f, t);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlHelper.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getFileList
	 * </p>
	 * <p>
	 * 方法功能描述:得到文件列表
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<BFSFilePojo> getFileList(SqlExecute sqlHelper,
			JSONObject paras, long f, long t) throws Exception {
		ArrayList<BFSFilePojo> list = new ArrayList<BFSFilePojo>();
		String where = "";
		Iterator<String> keys = paras.keys();
		String key = "";
		List<Object> vList = new ArrayList<Object>();
		String orderBy = " t.F_INDEX, t.U_DATE desc";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("key")) {
					where += " and t.F_NAME like ?";
					vList.add("%" + v + "%");
				}
				if (key.equals("bid")) {
					where += " and t.B_ID=?";
					vList.add(v);
				}
				if (key.equals("type")) {
					where += " and t.F_TYPE=?";
					vList.add(Integer.parseInt(v));
				}
			}
		}
		// 得到文件列表
		BSFileDBMang fileMang = new BSFileDBMang(sqlHelper, m_bs);
		paras.put("max", fileMang.getFileCount(where, vList));
		list = fileMang.getFileList(where, orderBy, vList, f, t);
		return list;
	}

	/**
	 * <p>
	 * 方法名称: deleteFile
	 * </p>
	 * <p>
	 * 方法功能描述: 删除文件表的文件
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int deleteFile(BFSFilePojo file) throws Exception {
		int count = 0;
		String where = " and t.f_id=?";
		List<Object> vList = new ArrayList<Object>();
		vList.add(file.getInstId());
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			BSFileDBMang fileMang = new BSFileDBMang(sqlHelper, m_bs);
			count = fileMang.deleteFile(where, vList);
			sqlHelper.commit();
		} catch (Exception e) {
			sqlHelper.rollback();
			e.printStackTrace();
		}
		if (count > 0) {
			this.deleteOneFile(file);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteFile
	 * </p>
	 * <p>
	 * 方法功能描述: 删除文件表的文件
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int deleteFiles(String bid, boolean isDel) throws Exception {
		int count = 0;
		String where = " and t.b_id=?";
		List<Object> vList = new ArrayList<Object>();
		vList.add(bid);
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			BSFileDBMang fileMang = new BSFileDBMang(sqlHelper, m_bs);
			if (isDel) {
				ArrayList<BFSFilePojo> fileList = fileMang.getFileList(where,
						"", vList);
				for (BFSFilePojo oneFile : fileList) {
					// 删除服务器文件
					this.deleteOneFile(oneFile);
				}
			}
			count = fileMang.deleteFile(where, vList);
			sqlHelper.commit();
		} catch (Exception e) {
			sqlHelper.rollback();
			e.printStackTrace();
		}
		if (count > 0) {
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteFile
	 * </p>
	 * <p>
	 * 方法功能描述: 删除文件表的文件
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int deleteFile(String bid, int index) throws Exception {
		int count = 0;
		String where = " and t.b_id=? and t.F_INDEX=?";
		List<Object> vList = new ArrayList<Object>();
		vList.add(bid);
		vList.add(index);
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			BSFileDBMang fileMang = new BSFileDBMang(sqlHelper, m_bs);
			count = fileMang.deleteFile(where, vList);
			sqlHelper.commit();
		} catch (Exception e) {
			sqlHelper.rollback();
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteFile
	 * </p>
	 * <p>
	 * 方法功能描述: 删除文件表的文件
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int deleteFiles(String bid) throws Exception {
		int count = 0;
		String where = " and t.b_id=?";
		List<Object> vList = new ArrayList<Object>();
		vList.add(bid);
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			BSFileDBMang fileMang = new BSFileDBMang(sqlHelper, m_bs);
			count = fileMang.deleteFile(where, vList);
			sqlHelper.commit();
		} catch (Exception e) {
			sqlHelper.rollback();
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 删除空目录
	 * 
	 * @param dir
	 *            将要删除的目录路径
	 */
	public void doDeleteEmptyDir(String dir) {
		boolean success = (new File(dir)).delete();
		if (success) {
			System.out.println("Successfully deleted empty directory: " + dir);
		} else {
			System.out.println("Failed to delete empty directory: " + dir);
		}
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * 
	 * @param dir
	 *            将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	public boolean deleteDir(File dir, boolean delThis) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]), delThis);
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		if (delThis) {
			return dir.delete();
		} else {
			return true;
		}
	}

	/**
	 * <p>
	 * 方法名称: saveUploadUserFile
	 * </p>
	 * <p>
	 * 方法功能描述: 保存文件
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public String saveUploadUserFile(BSObject m_bs, BFSFilePojo oneFile,
			int tableColTyp, List<FileItem> fileList) throws Exception {
		String returObj = "T";
		boolean saveDB = true;
		File f = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			BSFileDBMang fileMang = new BSFileDBMang(sqlHelper, m_bs);
			boolean unZip = false;
			// 得到上传的文件
			for (FileItem fi : fileList) {
				// fi.get
				boolean isDel = false;
				saveDB = true;
				if (!fi.isFormField()) {
					oneFile.setFileName(fi.getName());
					if (oneFile.getInstId() == null
							|| oneFile.getInstId().trim().equals("")) {
						oneFile.setInstId(BSGuid.getRandomGUID());
					}
					oneFile.setSize(String.valueOf(fi.getSize()));
					oneFile.setHashID(String.valueOf(System.currentTimeMillis()));
					// 得到后缀名
					if (fi.getName().lastIndexOf(".") >= 0) {
						oneFile.setFileEx(fi.getName().substring(
								fi.getName().lastIndexOf(".")));
					} else {
						oneFile.setFileEx(".tmp");
					}
					// 上传文件服务器
					switch (tableColTyp) {
					case BIFile.F_USER_HEAD:// 头像
						oneFile.setType(0);
						oneFile.setTitle("用户头像：" + oneFile.getFileName());
						oneFile.setFilePath("userhead");
						oneFile.setFileUrl(oneFile.getFilePath());
						break;
					case BIFile.F_FAULT_CODE:// 报警图片
						oneFile.setInstId(BSGuid.getRandomGUID());
						oneFile.setType(0);
						oneFile.setTitle("报警图片文件：" + oneFile.getFileName());
						break;
					case BIFile.F_FAULT_OP:// 报警图片
						oneFile.setInstId(BSGuid.getRandomGUID());
						oneFile.setType(0);
						oneFile.setTitle("报警处理图片文件：" + oneFile.getFileName());
						break;
					case BIFile.F_FIXLOGS:// 车辆维护图片
						oneFile.setInstId(BSGuid.getRandomGUID());
						oneFile.setType(0);
						oneFile.setTitle("车辆维护图片文件：" + oneFile.getFileName());
						break;
					default:
						break;
					}
					if (oneFile.getFileEx().equals(".temp")) {
						oneFile.setFileName(oneFile.getFileName().substring(0,
								oneFile.getFileName().lastIndexOf(".temp")));
					}
					if (oneFile.getFileEx().equals(".tmp")) {
						oneFile.setFileName(oneFile.getFileName().substring(0,
								oneFile.getFileName().lastIndexOf(".tmp")));
					}
					oneFile.setFileEx(BIFile.getFileExtension(oneFile
							.getFileName()));

					if (isDel) {
						this.deleteFile(oneFile);
					}
					// 上传
					if (this.saveLocalhostFile(m_bs, oneFile, fi, unZip)
							.equals("T")) {
						// 保存数据库
						if (saveDB) {
							fileMang.updateFile(oneFile);
						}
						if (isDel) {
							oneFile.setInstId(BSGuid.getRandomGUID());
						}
					}
				}
			}

			returObj = "T";
			sqlHelper.commit();
		} catch (Exception e) {
			sqlHelper.rollback();
			e.printStackTrace();
		} finally {
			if (f != null && f.exists()) {
				f.delete();
			}
		}
		return returObj;
	}

	// 导入文件
	public static JSONObject importFile(BSObject m_bs, BFSFilePojo file,
			ServletFileUpload multi) throws Exception {
		JSONObject ret = new JSONObject();
		ret.put("r", 1002);
		SysLogsPojo oneLogs = new SysLogsPojo();
		oneLogs.setCreateUser(BILogin.getLoginUser(m_bs));
		// 保存临时文件
		BIFile fileBI = new BIFile();
		switch (file.getOpType()) {
		case "DICITEM":
			for (FileItem fi : m_bs.getFileList()) {
				ret = fileBI.importDitItemData(file, fi.getInputStream());
				oneLogs.setName("导入数据字典");
			}
			break;
		case "WATCH":
			for (FileItem fi : m_bs.getFileList()) {
				ret = fileBI.importWatchUser(file, fi.getInputStream());
				oneLogs.setName("导入用户云环数据");
			}
			break;
		case "USER":
			for (FileItem fi : m_bs.getFileList()) {
				ret = fileBI.importUser(file, fi.getInputStream());
				oneLogs.setName("导入用户数据");
			}
			break;
		case "TRUCKDEF":
			// for (FileItem fi : m_bs.getFileList()) {
			// ret = fileBI.importTruck(file, fi.getInputStream());
			// }
			break;
		case "TRUCK":
			for (FileItem fi : m_bs.getFileList()) {
				ret = fileBI.importTruck(file, fi.getInputStream());
				oneLogs.setName("导入车辆云盒数据");
			}
			break;
		case "AREA":
			for (FileItem fi : m_bs.getFileList()) {
				ret = fileBI.importArea(file, fi.getInputStream());
				oneLogs.setName("导入地域数据");
			}
			break;
		case "EQUIPMENTDEF":
			// for (FileItem fi : m_bs.getFileList()) {
			// ret = fileBI.importEquipmentDef(file, fi.getInputStream());
			// }
			break;
		case "EQUIPMENTINST":
			for (FileItem fi : m_bs.getFileList()) {
				ret = fileBI.importEquipmentInst(file, fi.getInputStream());
				oneLogs.setName("导入设备数据");
			}
			break;
		default:
			break;
		}

		if (ret.getInt("r") == 0) {
			oneLogs.setType(1);
			oneLogs.setContent("操作:" + oneLogs.getName());
			SysLogsBIMang slbi = new SysLogsBIMang(oneLogs, m_bs);
			slbi.start();
		}

		return ret;
	}

	/**
	 * <p>
	 * 方法名称: saveUploadUserFile
	 * </p>
	 * <p>
	 * 方法功能描述: 保存文件
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public String saveLocalhostFile(BSObject m_bs, BFSFilePojo oneFile,
			FileItem fi, boolean isUnZip) throws Exception {
		String returObj = "T";
		// 保存物理路径
		if (!fi.isFormField()) {
			String fname = fi.getName();
			// 设置新路径
			String path = BSCommon.getConfigValue("upload_path")
					+ oneFile.getFilePath();
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			file.setExecutable(true, false);// 设置可执行权限
			file.setReadable(true, false);// 设置可读权限
			file.setWritable(true, false);// 设置可写权限
			// System.out.println("path:" + file.getPath());
			// 设定新的文件名
			String nname = oneFile.getInstId();
			if (isUnZip) {
				// 需要解压的文件
				fi.write(new File(BSCommon.getConfigValue("upload_path")
						+ nname + "." + oneFile.getFileEx()));
			} else {
				fi.write(new File(file.getPath() + "/" + nname + "."
						+ oneFile.getFileEx()));
			}
			// System.out.println("newpath:" + file.getPath() + "/" + nname
			// + oneFile.getFileEx());
			File b = null;
			File m = null;
			if (fi.getName().toLowerCase().endsWith(".png")
					|| oneFile.getFileEx().equals("png")) {
				b = new File(file.getPath() + "/" + nname + "."
						+ oneFile.getFileEx());
				m = new File(file.getPath() + "/" + nname + "_min" + "."
						+ oneFile.getFileEx());
				// 缩略图
				this.saveFileMin(b, "png", m);
			} else if (fi.getName().toLowerCase().endsWith(".jpg")
					|| oneFile.getFileEx().equals("jpg")) {
				b = new File(file.getPath() + "/" + nname + "."
						+ oneFile.getFileEx());
				m = new File(file.getPath() + "/" + nname + "_min" + "."
						+ oneFile.getFileEx());
				// 缩略图
				this.saveFileMin(b, "jpg", m);
			} else if (fi.getName().toLowerCase().endsWith(".jpeg")
					|| oneFile.getFileEx().equals("jpeg")) {
				b = new File(file.getPath() + "/" + nname + "."
						+ oneFile.getFileEx());
				m = new File(file.getPath() + "/" + nname + "_min" + "."
						+ oneFile.getFileEx());
				// 缩略图
				this.saveFileMin(b, "jpeg", m);
			}
			if (b != null && b.isFile()) {
				b.setExecutable(true, false);// 设置可执行权限
				b.setReadable(true, false);// 设置可读权限
				b.setWritable(true, false);// 设置可写权限
			}
			if (m != null && m.isFile()) {
				m.setExecutable(true, false);// 设置可执行权限
				m.setReadable(true, false);// 设置可读权限
				m.setWritable(true, false);// 设置可写权限
			}
			// sfpt
			if (BSCommon.getConfigValue("upload_type").equals("1")) {
				SFTPThread sftp = new SFTPThread(oneFile.getFileUrl(),
						b.getPath() + "," + m.getPath());
				sftp.start();
			}
		}

		return returObj;
	}

	// 导入数据字典项目
	public JSONObject importDitItemData(BFSFilePojo file, InputStream realFile)
			throws Exception {
		JSONObject retJSON = new JSONObject();
		retJSON.put("r", 1002);
		Workbook book = null;
		int clRow = 0;
		int okRow = 0;
		if (file.getBissId() != null && !file.getBissId().equals("")) {
			SqlExecute sqlHelper = new SqlExecute();
			try {
				book = Workbook.getWorkbook(realFile);
				if (book != null) {
					Sheet sheet = book.getSheet(0);
					DicItemPojo onePojo = new DicItemPojo();
					StaticDBMang staticDB = new StaticDBMang(sqlHelper);
					List<Object> vList = new ArrayList<Object>();
					DicItemPojo fromDI = null;
					String fromDIStr = "";
					for (int r = 1, rows = sheet.getRows(); r < rows; r++) {
						onePojo.getDic().setId(file.getBissId());
						// 名称
						onePojo.setName((sheet.getCell(1, r).getContents())
								.trim());
						// 值1
						onePojo.setValue((sheet.getCell(2, r).getContents())
								.trim());
						// 值2
						onePojo.setValue2((sheet.getCell(3, r).getContents())
								.trim());
						// 索引
						if (!sheet.getCell(4, r).getContents().equals("")) {
							onePojo.setIndex(Integer.parseInt(sheet.getCell(4,
									r).getContents()));
						} else {
							onePojo.setIndex(r);
						}
						// 联动来源
						if (!sheet.getCell(5, r).getContents().equals("")) {
							fromDIStr = sheet.getCell(5, r).getContents();
							if (!fromDIStr.equals("")) {
								if (fromDI == null
										|| !fromDIStr.equals(fromDI.getName())) {
									// 不是重复的联动
									fromDI = staticDB.getOneDicItemByName(sheet
											.getCell(5, r).getContents());
								}
							}
							if (fromDI != null) {
								onePojo.setPitemid(fromDI.getId());
							}

						}
						if (onePojo.getName() != null
								|| !onePojo.getName().equals("")) {
							vList.clear();
							vList.add(onePojo.getName());
							if (sqlHelper
									.queryIntBySql(
											"select count(t.ITEM_ID) from TB_DIC_ITEM t where t.ITEM_NAME=?",
											vList) > 0) {
								// 更新
								staticDB.updateDicItemByName(onePojo);
							} else {
								// 新增
								onePojo.setId(BSGuid.getRandomGUID());
								staticDB.insertDicItem(onePojo);
							}
							okRow++;
						}
					}
				} else {
					JSONObject oneErr = new JSONObject();
					oneErr.put("row", 0);
					oneErr.put("error", "文件不存在!");
				}
			} catch (Exception e) {
				JSONObject oneErr = new JSONObject();
				oneErr.put("error", URLlImplBase.ErrorMap.get(1100));
				e.printStackTrace();
			} finally {
				sqlHelper.close();
				if (book != null) {
					book.close();
				}
				retJSON.put("oknum", okRow);
				// file.delete();
			}
		} else {
			retJSON.put("r", 1100);
		}
		retJSON.put("row", clRow);
		retJSON.put("okrow", okRow);
		return retJSON;
	}

	// 导入员工手环
	// 导入员工
	public JSONObject importUser(BFSFilePojo file, InputStream realFile)
			throws Exception {
		JSONObject retJSON = new JSONObject();
		retJSON.put("r", 1002);
		BSDateEx bsDate = new BSDateEx();
		Workbook book = null;
		int clRow = 0;
		int okRow = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			book = Workbook.getWorkbook(realFile);
			if (book != null) {
				Sheet sheet = book.getSheet(1);
				UserPojo onePojo = null;
				BIUser userBI = new BIUser(sqlHelper, null);
				BSUserDBMang userDB = new BSUserDBMang(sqlHelper, null);
				BSPowerDBMang powerDB = new BSPowerDBMang(sqlHelper, null);
				BICompany compBI = new BICompany(sqlHelper, m_bs);
				List<Object> vList = new ArrayList<Object>();
				// 性别
				String org01 = "";
				String org02 = "";
				String[] userRoleStr = null;
				String sexName = "";
				String sbFlgStr = "";
				OrgPojo temOrg = null;
				OrgPojo temSubOrg = null;
				for (int r = 1, rows = sheet.getRows(); r < rows; r++) {
					boolean isNew = false;
					onePojo = userDB.getOneUserById((sheet.getCell(2, r)
							.getContents()).trim());
					if (onePojo == null) {
						isNew = true;
						onePojo = new UserPojo();
						onePojo.setId((sheet.getCell(2, r).getContents())
								.trim());
						onePojo.setCreateDate(bsDate.getThisDate(0, 0));
					}
					onePojo.setUpdateDate(onePojo.getCreateDate());
					// 名称
					onePojo.setName((sheet.getCell(3, r).getContents()).trim());
					// 性别
					onePojo.setSex(2);
					// 内部名称
					onePojo.setInName((sheet.getCell(5, r).getContents())
							.trim());
					// 是否参保
					sbFlgStr = sheet.getCell(7, r).getContents();
					if (sbFlgStr != null && sbFlgStr.trim().equals("是")) {
						onePojo.setSbFlg(1);
					} else {
						onePojo.setSbFlg(0);
					}
					// 身份证
					onePojo.setIdCard((sheet.getCell(8, r).getContents())
							.trim());
					if (onePojo.getIdCard().length() >= 18) {
						onePojo.setBirthday(URLlImplBase
								.getBirthdayFromCard(onePojo.getIdCard())
								+ " 00:00:00");
					} else {
						onePojo.setBirthday("");
					}
					// 电话
					onePojo.setmPhone((sheet.getCell(5, r).getContents())
							.trim());
					// 机构
					temOrg = null;
					temSubOrg = null;
					if (!sheet.getCell(0, r).getContents().equals("")) {
						// 项目公司
						org01 = sheet.getCell(0, r).getContents();
						temOrg = userDB.getOneOrgByName(org01, 2);
						if (temOrg == null) {
							temOrg = new OrgPojo();
							temOrg.setCreateStaff(file.getUser());
							temOrg.setName(org01);
							temOrg.setCreateDate(onePojo.getCreateDate());
							temOrg.setType(2);
							temOrg.setPorgId("");
							temOrg.setCompany(compBI.getThisCompanyByRedis());
							temOrg.setArea(temOrg.getCompany().getArea());
							userDB.insertOrg(temOrg);
						}
						// 项目组
						if (temOrg != null
								&& !sheet.getCell(1, r).getContents()
										.equals("")) {
							org02 = sheet.getCell(1, r).getContents();
							temSubOrg = userDB.getOneOrgByName(temOrg.getId(),
									org02, 1);
							if (temSubOrg == null) {
								temSubOrg = new OrgPojo();
								temSubOrg.setPorgId(temOrg.getId());
								temSubOrg.setCreateStaff(file.getUser());
								temSubOrg
										.setCreateDate(onePojo.getCreateDate());
								temSubOrg.setName(org02);
								temSubOrg.setType(1);
								temSubOrg.setCompany(compBI
										.getThisCompanyByRedis());
								temSubOrg.setArea(temSubOrg.getCompany()
										.getArea());
								userDB.insertOrg(temSubOrg);
							}
						}
					}
					if (temSubOrg != null) {
						onePojo.setOrg(temSubOrg);
					} else if (temOrg != null) {
						onePojo.setOrg(temOrg);
					}
					// 角色
					onePojo.setRoleList(new ArrayList<RolePojo>());
					onePojo.getRoleList()
							.add(new RolePojo("BASE_ROLE", "", ""));
					onePojo.setUgType(0);
					userRoleStr = null;
					if (!sheet.getCell(9, r).getContents().equals("")) {
						// “,”隔开
						userRoleStr = (sheet.getCell(9, r).getContents())
								.split(",");
						for (String oneRName : userRoleStr) {
							if (!oneRName.equals("")) {
								RolePojo oneRole = powerDB
										.getOneRoleByName(oneRName);
								if (oneRole != null) {
									onePojo.getRoleList().add(oneRole);
									if (oneRole.getId().equals("TRUCK_MANG")
											|| oneRole.getId().equals(
													"USER_MANG")
											|| oneRole.getId().equals(
													"PRO_MANG")
											|| oneRole.getId().equals(
													"COMP_MANG")) {
										onePojo.setUgType(1);
									}
								}
							}
						}
					}
					if (onePojo.getId() != null || !onePojo.getId().equals("")) {
						vList.clear();
						vList.add(onePojo.getId());
						if (!isNew) {
							// 更新
							userDB.updateUser(onePojo);
						} else {
							// 新增
							onePojo.setInstId("");
							userDB.insertUser(onePojo);
						}
						if (onePojo.getUgType() == 1
								&& onePojo.getOrg().getId().equals("")) {
							userBI.setGroupToRedis(onePojo.getOrg().getId());
						}
						// 角色
						vList.clear();
						vList.add(onePojo.getInstId());
						powerDB.deleteUserRole(" and USER_INSTID=?", vList);
						for (RolePojo oneRole : onePojo.getRoleList()) {
							if (oneRole != null) {
								powerDB.insertUserRole(new RoleUserPojo(onePojo
										.getInstId(), oneRole.getId()));
							}
						}
						okRow++;
					}
					retJSON.put("r", 0);
				}
			} else {
				retJSON.put("row", 1003);
			}
		} catch (Exception e) {
			retJSON.put("r", 999);
			retJSON.put("error", e.getMessage());
			e.printStackTrace();
		} finally {
			sqlHelper.close();
			if (book != null) {
				book.close();
			}
			retJSON.put("oknum", okRow);
			// file.delete();
		}

		retJSON.put("row", clRow);
		retJSON.put("okrow", okRow);
		if (!retJSON.containsKey("error")) {
			retJSON.put("error", URLlImplBase.ErrorMap.get(retJSON.getInt("r")));
		}
		return retJSON;
	}

	// 导入员工手环
	public JSONObject importWatchUser(BFSFilePojo file, InputStream realFile)
			throws Exception {
		JSONObject retJSON = new JSONObject();
		retJSON.put("r", 1002);
		BSDateEx bsDate = new BSDateEx();
		Workbook book = null;
		int clRow = 0;
		int okRow = 0;
		try {
			book = Workbook.getWorkbook(realFile);
			if (book != null) {
				Sheet sheet = book.getSheet(1);
				UserPojo onePojo = null;
				BIUser userBI = new BIUser(null, null);
				BIPower powerBI = new BIPower(null, null);
				BICompany compBI = new BICompany(null, m_bs);
				BIEquipment eqpBI = new BIEquipment(null, m_bs);
				List<Object> vList = new ArrayList<Object>();
				// 性别
				String[] userRoleStr = null;
				String sexName = "";
				EquipmentInstPojo oneEqp = null;
				String sbFlgStr = "";
				OrgPojo temOrg = null;
				OrgPojo temSubOrg = null;
				for (int r = 1, rows = sheet.getRows(); r < rows; r++) {
					if (sheet.getCell(1, r).getContents().trim().equals("")) {
						continue;
					}
					boolean isNew = false;
					boolean isNewEqp = false;
					onePojo = userBI.getOneUserByIdNotState((sheet
							.getCell(2, r).getContents()).trim());
					if (onePojo == null) {
						isNew = true;
						onePojo = new UserPojo();
						onePojo.setId((sheet.getCell(2, r).getContents())
								.trim());
						onePojo.setCreateDate(bsDate.getThisDate(0, 0));
					}
					onePojo.setUpdateDate(onePojo.getCreateDate());
					onePojo.setState(1);
					// 名称
					onePojo.setName((sheet.getCell(3, r).getContents()).trim());
					// 内部名称
					onePojo.setInName((sheet.getCell(6, r).getContents())
							.trim());
					// 性别
					onePojo.setSex(2);
					// 是否参保
					sbFlgStr = sheet.getCell(7, r).getContents();
					if (sbFlgStr != null && sbFlgStr.trim().equals("是")) {
						onePojo.setSbFlg(1);
					} else {
						onePojo.setSbFlg(0);
					}
					// 身份证
					onePojo.setIdCard((sheet.getCell(8, r).getContents())
							.trim());
					if (onePojo.getIdCard().length() >= 18) {
						onePojo.setBirthday(URLlImplBase
								.getBirthdayFromCard(onePojo.getIdCard())
								+ " 00:00:00");
					} else {
						onePojo.setBirthday("");
					}
					// 电话
					onePojo.setmPhone((sheet.getCell(5, r).getContents())
							.trim());
					// 机构
					temOrg = null;
					temSubOrg = null;
					if (!sheet.getCell(0, r).getContents().equals("")) {
						// 项目公司
						temOrg = userBI.getOneOrgByName(sheet.getCell(0, r)
								.getContents().trim(), 2);
						if (temOrg == null) {
							temOrg = new OrgPojo();
							temOrg.setCreateStaff(file.getUser());
							temOrg.setName(sheet.getCell(0, r).getContents()
									.trim());
							temOrg.setCreateDate(onePojo.getCreateDate());
							temOrg.setType(2);
							temOrg.setPorgId("");
							temOrg.getArea().setId("430000");
							temOrg.setCompany(compBI.getThisCompanyByRedis());
							userBI.insertOrg(temOrg);
						}
						// 项目组
						if (temOrg != null
								&& !sheet.getCell(1, r).getContents()
										.equals("")) {
							temSubOrg = userBI
									.getOneOrgByName(temOrg.getId(),
											sheet.getCell(1, r).getContents()
													.trim(), 1);
							if (temSubOrg == null) {
								temSubOrg = new OrgPojo();
								temSubOrg.setPorgId(temOrg.getId());
								temSubOrg.setCreateStaff(file.getUser());
								temSubOrg
										.setCreateDate(onePojo.getCreateDate());
								temSubOrg.setName(sheet.getCell(1, r)
										.getContents().trim());
								temSubOrg.setType(1);
								temSubOrg.getArea().setId("430000");
								temSubOrg.setCompany(compBI
										.getThisCompanyByRedis());
								userBI.insertOrg(temSubOrg);
							}
						}
					}
					if (temSubOrg != null) {
						onePojo.setOrg(temSubOrg);
					} else if (temOrg != null) {
						onePojo.setOrg(temOrg);
					}
					// 角色
					onePojo.setRoleList(new ArrayList<RolePojo>());
					onePojo.getRoleList()
							.add(new RolePojo("BASE_ROLE", "", ""));
					onePojo.getRoleList().add(
							new RolePojo("OUTDOORS_STAFF", "", ""));
					onePojo.setUgType(0);
					// userRoleStr = null;
					// if (!sheet.getCell(11, r).getContents().equals("")) {
					// // “,”隔开
					// userRoleStr = (sheet.getCell(11, r).getContents())
					// .split(",");
					// for (String oneRName : userRoleStr) {
					// if (!oneRName.equals("")) {
					// onePojo.getRoleList().add(
					// powerDB.getOneRoleByName(oneRName));
					// if (oneRName.equals("TRUCK_MANG")
					// || oneRName.equals("USER_MANG")
					// || oneRName.equals("PRO_MANG")
					// || oneRName.equals("COMP_MANG")) {
					// onePojo.setUgType(1);
					// }
					// }
					// }
					// }

					if (onePojo.getId() != null || !onePojo.getId().equals("")) {
						vList.clear();
						vList.add(onePojo.getId());
						if (!isNew) {
							// 更新
							userBI.updateUser(onePojo);
						} else {
							// 新增
							onePojo.setInstId("");
							userBI.insertUser(onePojo);
						}
						if (onePojo.getUgType() == 1
								&& onePojo.getOrg().getId().equals("")) {
							userBI.setGroupToRedis(onePojo.getOrg().getId());
						}
						// 角色
						vList.clear();
						vList.add(onePojo.getInstId());
						powerBI.deleteUserRole(" and USER_INSTID=?", vList);
						for (RolePojo oneRole : onePojo.getRoleList()) {
							if (oneRole != null) {
								powerBI.insertUserRole(new RoleUserPojo(onePojo
										.getInstId(), oneRole.getId()));
							}
						}
						// 云环设备信息
						if (!sheet.getCell(12, r).getContents().trim()
								.equals("")
								&& !sheet.getCell(13, r).getContents().trim()
										.equals("")) {
							oneEqp = eqpBI
									.getOneEquipmentInstByWyCode(sheet
											.getCell(12, r).getContents()
											.trim()
											+ "|"
											+ sheet.getCell(13, r)
													.getContents().trim());
							isNewEqp = false;
							if (oneEqp == null) {
								// 该设备不能存在
								oneEqp = new EquipmentInstPojo();
								oneEqp.setWyCode(sheet.getCell(12, r)
										.getContents().trim()
										+ "|"
										+ sheet.getCell(13, r).getContents()
												.trim());
								oneEqp.setToken(oneEqp.getWyCode());
								isNewEqp = true;
							}
							oneEqp.setState(0);
							oneEqp.setQrCode(sheet.getCell(11, r).getContents()
									.trim());
							oneEqp.setProDate(this.bsDate.getThisDate(0, 0));
							oneEqp.setUpdateDate(oneEqp.getProDate());
							// ICCID
							oneEqp.setPara1(sheet.getCell(14, r).getContents()
									.trim());
							oneEqp.setName("云环 " + oneEqp.getQrCode());
							oneEqp.setEqpDef(eqpBI
									.getEqpDefByRedis(sheet.getCell(9, r)
											.getContents().trim()
											+ "_"
											+ sheet.getCell(10, r)
													.getContents().trim()));
							oneEqp.setMangUser(onePojo);
							if (temSubOrg != null) {
								oneEqp.setOrg(temSubOrg);
							}
							if (oneEqp.getEqpDef() == null) {
								// 设备类型不存在，则新增
								oneEqp.setEqpDef(new EquipmentDefPojo());
								oneEqp.getEqpDef().setStyle(1);
								oneEqp.getEqpDef().setId(
										sheet.getCell(9, r).getContents()
												.trim()
												+ "_"
												+ sheet.getCell(10, r)
														.getContents().trim());
								oneEqp.getEqpDef().setName(
										"云环 "
												+ sheet.getCell(9, r)
														.getContents().trim());
								oneEqp.getEqpDef().setBrand("酷陆云环");
								oneEqp.getEqpDef().setManufacturer("酷陆科技");
								oneEqp.getEqpDef().setNo(
										sheet.getCell(9, r).getContents()
												.trim());
								oneEqp.getEqpDef().setProvider(
										sheet.getCell(10, r).getContents()
												.trim());
								oneEqp.getEqpDef().getEqpType()
										.setId("EQUIPMENT_DEFTYPE_1");
								eqpBI.insertEquipmentDef(oneEqp.getEqpDef());
							}
							if (isNewEqp) {
								oneEqp.setMangUser(onePojo);
								if (temSubOrg != null) {
									oneEqp.setOrg(temSubOrg);
								}
								eqpBI.insertEquipmentInst(oneEqp);
							} else {
								eqpBI.updateEquipmentInst(oneEqp);
							}
						}
						okRow++;
					}
					retJSON.put("r", 0);
				}
			} else {
				retJSON.put("r", 1003);
			}
		} catch (Exception e) {
			retJSON.put("r", 999);
			if (e.getMessage().indexOf("Unable to recognize OLE stream") >= 0) {
				retJSON.put("r", 1004);
			} else {
				retJSON.put("error", e.getMessage());
			}
			e.printStackTrace();
		} finally {
			if (book != null) {
				book.close();
			}
			retJSON.put("oknum", okRow);
		}

		retJSON.put("row", clRow);
		retJSON.put("okrow", okRow);
		if (!retJSON.containsKey("error")) {
			retJSON.put("error", URLlImplBase.ErrorMap.get(retJSON.getInt("r")));
		}
		return retJSON;
	}

	// 导入车辆
	public JSONObject importTruck(BFSFilePojo file, InputStream realFile)
			throws Exception {
		JSONObject retJSON = new JSONObject();
		retJSON.put("r", 1002);
		Workbook book = null;
		int clRow = 0;
		int okRow = 0;
		try {
			book = Workbook.getWorkbook(realFile);
			if (book != null) {
				Sheet sheet = book.getSheet(1);
				// 效验导入文档是否正确
				retJSON = this._checkImportFile(file, sheet);
				if (retJSON.getInt("r") == 0) {
					retJSON.put("r", 1002);
					TruckPojo onePojo = new TruckPojo();
					BITruck truckBI = new BITruck(sqlHelper, null);
					BICompany compBI = new BICompany(null, m_bs);
					BIUser userBI = new BIUser(sqlHelper, null);
					BIEquipment eqpBI = new BIEquipment(null, m_bs);
					OrgPojo temOrg = null;
					OrgPojo temSubOrg = null;
					EquipmentInstPojo oneEqp = null;
					for (int r = 1, rows = sheet.getRows(); r < rows; r++) {
						if (sheet.getCell(1, r).getContents().trim().equals("")) {
							continue;
						}
						boolean isNew = false;
						onePojo.setPlateNum((sheet.getCell(4, r).getContents())
								.trim());
						onePojo = truckBI
								.getOneTruckById(onePojo.getPlateNum());
						if (onePojo == null) {
							onePojo = new TruckPojo();
							onePojo.setId(BSGuid.getRandomGUID());
							onePojo.setPlateNum((sheet.getCell(4, r)
									.getContents()).trim());
							isNew = true;
						}
						// 内部名称
						onePojo.setInName(sheet.getCell(10, r).getContents()
								.trim());
						// 时间
						String time = (sheet.getCell(11, r).getContents())
								.trim();
						if (!time.equals("") && !time.equals("无")) {
							time = this.bsDate.getCalendarToString(URLlImplBase
									.getCalendarFromDateStr(time, "yyyyMMdd"));
							onePojo.setInDate(time.substring(0, 10)
									+ " 00:00:00");
						} else {
							onePojo.setInDate(this.bsDate.getThisDate(0, 0)
									.substring(0, 10) + " 00:00:00");
						}
						onePojo.setProDate(onePojo.getInDate());
						// 车架号
						onePojo.setCjNo((sheet.getCell(15, r).getContents())
								.trim());
						// 内部编码
						onePojo.setNo((sheet.getCell(2, r).getContents())
								.trim());
						// 负责人
						if (!(sheet.getCell(13, r).getContents()).equals("")) {
							onePojo.setMangUser(userBI.getOneUserById((sheet
									.getCell(13, r).getContents()).trim()));
							if (onePojo.getMangUser() == null) {
								onePojo.setMangUser(new UserPojo());
							}
						} else {
							onePojo.setMangUser(new UserPojo());
						}
						// 上装
						onePojo.setUpNo((sheet.getCell(7, r).getContents())
								.trim());
						time = (sheet.getCell(8, r).getContents()).trim();
						if (!time.equals("") && !time.equals("无")) {
							time = this.bsDate.getCalendarToString(URLlImplBase
									.getCalendarFromDateStr(time, "yyyyMM"));
							onePojo.setUpDate(time.substring(0, 10)
									+ " 00:00:00");
						} else {
							onePojo.setUpDate(this.bsDate.getThisDate(0, 0)
									.substring(0, 10) + " 00:00:00");
						}
						// 机构
						temOrg = null;
						temSubOrg = null;
						if (!sheet.getCell(0, r).getContents().equals("")) {
							// 项目公司
							temOrg = userBI.getOneOrgByName(sheet.getCell(0, r)
									.getContents().trim(), 2);
							if (temOrg == null) {
								temOrg = new OrgPojo();
								temOrg.setCreateStaff(file.getUser());
								temOrg.setName(sheet.getCell(0, r)
										.getContents().trim());
								temOrg.setCreateDate(this.bsDate.getThisDate(0,
										0));
								temOrg.setType(2);
								temOrg.setPorgId("");
								temOrg.getArea().setId("430000");
								temOrg.setCompany(compBI
										.getThisCompanyByRedis());
								userBI.insertOrg(temOrg);
							}
							// 项目组
							if (temOrg != null
									&& !sheet.getCell(1, r).getContents()
											.equals("")) {
								temSubOrg = userBI.getOneOrgByName(
										temOrg.getId(), sheet.getCell(1, r)
												.getContents().trim(), 1);
								if (temSubOrg == null) {
									temSubOrg = new OrgPojo();
									temSubOrg.setPorgId(temOrg.getId());
									temSubOrg.setCreateStaff(file.getUser());
									temSubOrg.setCreateDate(this.bsDate
											.getThisDate(0, 0));
									temSubOrg.setName(sheet.getCell(1, r)
											.getContents().trim());
									temSubOrg.setType(1);
									temSubOrg.getArea().setId("430000");
									temSubOrg.setCompany(compBI
											.getThisCompanyByRedis());
									userBI.insertOrg(temSubOrg);
								}
							}
						}
						if (temSubOrg != null) {
							onePojo.setOrg(temSubOrg);
						} else if (temOrg != null) {
							onePojo.setOrg(temOrg);
						}
						// 车载设备
						// EquipmentInstPojo vehiclePojo = eqpBI
						// .getOneEquipmentInstById((sheet.getCell(12, r)
						// .getContents()).trim());
						// if (vehiclePojo != null) {
						// vehiclePojo.setTruck(onePojo);
						// vehiclePojo.setOrg(onePojo.getOrg());
						// vehiclePojo.setMangUser(onePojo.getMangUser());
						// }
						//
						// 车型
						String defId = (sheet.getCell(6, r).getContents())
								.trim();
						if (defId.equals("") || defId.equals("无")) {
							defId = (sheet.getCell(5, r).getContents().trim() + "_unknown");
						}
						onePojo.setDefine(truckBI.getTruckDefByRedis(defId));
						if (onePojo.getDefine() == null) {
							onePojo.setDefine(new TruckDefinePojo());
							onePojo.getDefine().setId(defId);
							onePojo.getDefine().setNo(defId);
							if (!(sheet.getCell(6, r).getContents()).trim()
									.equals("")
									&& !(sheet.getCell(6, r).getContents())
											.trim().equals("无")) {
								onePojo.getDefine().setName(
										sheet.getCell(5, r).getContents()
												.trim()
												+ " "
												+ sheet.getCell(3, r)
														.getContents().trim()
												+ "["
												+ onePojo.getDefine().getNo()
												+ "]");
								onePojo.getDefine().setBrand(
										(sheet.getCell(5, r).getContents())
												.trim()
												+ " "
												+ sheet.getCell(3, r)
														.getContents().trim()
														.trim());
								onePojo.getDefine().setCompany(
										(sheet.getCell(5, r).getContents())
												.trim());
								onePojo.getDefine().setSaleDate(
										onePojo.getInDate());
								// 邮箱
								// onePojo.getDefine().setOilMJ(
								// (sheet.getCell(9, r).getContents())
								// .trim());
							} else {
								onePojo.getDefine().setName(
										sheet.getCell(5, r).getContents()
												.trim()
												+ " 未知车型");
								onePojo.getDefine().setBrand(
										sheet.getCell(5, r).getContents()
												.trim());
								onePojo.getDefine().setCompany(
										sheet.getCell(5, r).getContents()
												.trim());
								onePojo.getDefine().setSaleDate(
										onePojo.getInDate());
								// 邮箱
//								onePojo.getDefine().setOilMJ("");
							}
							truckBI.insertTruckDef(onePojo.getDefine());
						}
						// 邮箱
						String oil = (sheet.getCell(9, r).getContents()).trim();
						if (!oil.equals("")) {
							String[] oils = oil.split(",");
							for (String oneDef : oils) {
								if (oneDef.indexOf("=") > 0) {
									onePojo.getOilDef().put(
											oneDef.split("=")[0],
											oneDef.split("=")[1]);
								}

							}
						}
						onePojo.setName(onePojo.getInName() + "【"
								+ onePojo.getPlateNum() + "】");
						if (isNew) {
							// 更新
							truckBI.insertTruck(onePojo);
						} else {
							// 新增
							truckBI.updateTruck(onePojo);
						}
						// 设备 判断是否存在该条码的设备，存在则直接关联设备
						// 设备
						oneEqp = eqpBI.getOneEquipmentInstByWyCode(sheet
								.getCell(16, r).getContents().trim()
								+ "|"
								+ sheet.getCell(17, r).getContents().trim()
								+ "|"
								+ sheet.getCell(18, r).getContents().trim());
						boolean isNewEqp = false;
						if (oneEqp == null) {
							isNewEqp = true;
							// 该设备不能存在
							oneEqp = new EquipmentInstPojo();
							oneEqp.setWyCode(sheet.getCell(16, r).getContents()
									.trim()
									+ "|"
									+ sheet.getCell(17, r).getContents().trim()
									+ "|"
									+ sheet.getCell(18, r).getContents().trim());
							oneEqp.setToken("");
						}
						// 物联网号码
						oneEqp.setPhone(sheet.getCell(19, r).getContents()
								.trim());
						// ICCID
						oneEqp.setPara1(sheet.getCell(20, r).getContents()
								.trim());
						oneEqp.setQrCode(oneEqp.getWyCode());
						oneEqp.setProDate(this.bsDate.getThisDate(0, 0));
						oneEqp.setUpdateDate(oneEqp.getProDate());
						oneEqp.setName("云盒 "
								+ oneEqp.getQrCode().replaceAll("\\|", "_"));
						oneEqp.setEqpDef(eqpBI.getEqpDefByRedis(sheet
								.getCell(16, r).getContents().trim()
								+ "_"
								+ sheet.getCell(17, r).getContents().trim()));
						if (oneEqp.getEqpDef() == null) {
							// 设备类型不存在，则新增
							oneEqp.setEqpDef(new EquipmentDefPojo());
							oneEqp.getEqpDef().setStyle(0);
							oneEqp.getEqpDef().setId(
									sheet.getCell(16, r).getContents().trim()
											+ "_"
											+ sheet.getCell(17, r)
													.getContents().trim());
							oneEqp.getEqpDef().setName(
									"云盒 " + oneEqp.getEqpDef().getId());
							oneEqp.getEqpDef().setBrand("酷陆云盒");
							oneEqp.getEqpDef().setManufacturer("酷陆科技");
							oneEqp.getEqpDef().setNo(
									sheet.getCell(16, r).getContents().trim());
							oneEqp.getEqpDef().setProvider(
									sheet.getCell(17, r).getContents().trim());
							oneEqp.getEqpDef().getEqpType()
									.setId("EQUIPMENT_DEFTYPE_0");
							eqpBI.insertEquipmentDef(oneEqp.getEqpDef());
						}
						oneEqp.setMangUser(onePojo.getMangUser());
						oneEqp.setOrg(onePojo.getOrg());
						oneEqp.setTruck(onePojo);

						if (isNewEqp) {
							eqpBI.insertEquipmentInst(oneEqp);
						} else {
							eqpBI.updateEquipmentInst(oneEqp);
						}
						okRow++;
					}
					retJSON.put("r", 0);
				}
			} else {
				JSONObject oneErr = new JSONObject();
				oneErr.put("row", 0);
				retJSON.put("r", 1003);
			}
		} catch (Exception e) {
			retJSON.put("r", 999);
			retJSON.put("error", e.getMessage());
			e.printStackTrace();
		} finally {
			if (book != null) {
				book.close();
			}
			retJSON.put("oknum", okRow);
			// file.delete();
		}

		retJSON.put("row", clRow);
		retJSON.put("okrow", okRow);
		if (!retJSON.containsKey("error")) {
			retJSON.put("error", URLlImplBase.ErrorMap.get(retJSON.getInt("r")));
		}
		return retJSON;
	}

	// 导入设备实例
	public JSONObject importEquipmentInst(BFSFilePojo file, InputStream realFile)
			throws Exception {
		JSONObject retJSON = new JSONObject();
		retJSON.put("r", 1002);
		Workbook book = null;
		int clRow = 0;
		int okRow = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			book = Workbook.getWorkbook(realFile);
			if (book != null) {
				Sheet sheet = book.getSheet(0);
				EquipmentInstPojo onePojo = new EquipmentInstPojo();
				EquipmentDefPojo eqpDefPojo = null;
				BIDic dicBI = new BIDic(sqlHelper, null);
				BIEquipment eqpBI = new BIEquipment(sqlHelper, null);
				BITruck truckBI = new BITruck(sqlHelper, null);
				BIUser userBI = new BIUser(sqlHelper, null);
				for (int r = 1, rows = sheet.getRows(); r < rows; r++) {
					boolean isNew = false;
					onePojo = eqpBI.getOneEquipmentInstByWyCode((sheet.getCell(
							5, r).getContents()).trim());
					if (onePojo == null) {
						isNew = true;
						onePojo = new EquipmentInstPojo();
						onePojo.setInstId(BSGuid.getRandomGUID());
					}
					onePojo.setUpdateDate(this.bsDate.getThisDate(0, 0));
					DicItemPojo oneDefName = dicBI.getOneDicItemByName(
							sqlHelper, "EQUIPMENT_DEFTYPE",
							(sheet.getCell(2, r).getContents()).trim());
					eqpDefPojo = eqpBI.getOneEquipmentDefByEqpNo(
							oneDefName.getId(),
							(sheet.getCell(1, r).getContents()).trim());
					if (eqpDefPojo != null) {
						onePojo.setEqpDef(eqpDefPojo);
						// 设备基本信息
						onePojo.setWyCode((sheet.getCell(5, r).getContents())
								.trim());
						onePojo.setQrCode((sheet.getCell(6, r).getContents())
								.trim());
						onePojo.setPhone((sheet.getCell(7, r).getContents())
								.trim());
						onePojo.setName(onePojo.getEqpDef().getName() + "["
								+ onePojo.getQrCode() + "]");
						// 生产日期
						String time = (sheet.getCell(8, r).getContents())
								.trim();
						if (time.length() >= 10) {
							onePojo.setProDate(time.substring(0, 10)
									+ " 00:00:00");
						} else {
							onePojo.setProDate(time.substring(0, 4) + "-"
									+ time.substring(4, 6) + "-"
									+ time.substring(6, 8) + " 00:00:00");
						}

						// 项目公司-项目组
						OrgPojo xmgsOrg = userBI.getOneOrgByName("",
								(sheet.getCell(3, r).getContents()).trim());
						if (xmgsOrg != null) {
							onePojo.setOrg(userBI.getOneOrgByName(
									xmgsOrg.getId(),
									(sheet.getCell(4, r).getContents()).trim()));
							if (onePojo.getOrg() == null) {
								onePojo.setOrg(xmgsOrg);
							}
						}
						// 联系人
						onePojo.setMangUser(userBI.getOneUserById(sqlHelper,
								(sheet.getCell(9, r).getContents()).trim()));
						// 车辆
						TruckPojo oneTruck = truckBI.getOneTruckById((sheet
								.getCell(10, r).getContents()).trim());
						if (oneTruck != null) {
							onePojo.setTruck(oneTruck);
						}

						if (isNew) {
							// 更新
							eqpBI.insertEquipmentInst(sqlHelper, onePojo);
						} else {
							// 新增
							eqpBI.updateEquipmentInst(sqlHelper, onePojo);
						}
						// 如果跟车载有关联关系
						if (onePojo.getTruck() != null
								&& !onePojo.getTruck().getId().equals("")
								&& onePojo.getEqpDef().getEqpType().getId()
										.equals("EQUIPMENT_DEFTYPE_2")) {
							// 液面传感器与车载关联
							EquipmentInstPojo vehiclePojo = eqpBI
									.getOnevehicleInstByTruck(oneTruck.getId());
							if (vehiclePojo != null) {
								eqpBI.updateEquipmentInstRel(
										vehiclePojo.getInstId(),
										onePojo.getInstId());
							}
						}
						okRow++;
					}

				}
				retJSON.put("r", 0);
			} else {
				JSONObject oneErr = new JSONObject();
				oneErr.put("row", 0);
				retJSON.put("r", 1003);
			}
		} catch (Exception e) {
			retJSON.put("r", 999);
			retJSON.put("error", e.getMessage());
			e.printStackTrace();
		} finally {
			sqlHelper.close();
			if (book != null) {
				book.close();
			}
			retJSON.put("oknum", okRow);
			// file.delete();
		}

		retJSON.put("row", clRow);
		retJSON.put("okrow", okRow);
		if (!retJSON.containsKey("error")) {
			retJSON.put("error", URLlImplBase.ErrorMap.get(retJSON.getInt("r")));
		}
		return retJSON;
	}

	// 导入地域
	public JSONObject importArea(BFSFilePojo file, InputStream realFile)
			throws Exception {
		JSONObject retJSON = new JSONObject();
		retJSON.put("r", 1002);
		Workbook book = null;
		int clRow = 0;
		int okRow = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			book = Workbook.getWorkbook(realFile);
			if (book != null) {
				Sheet sheet = book.getSheet(0);
				AreaPojo onePojo = new AreaPojo();
				AreaPojo onePPojo = new AreaPojo();
				String areaShen = "8A018C8ABD83E9BB46BC631B163F9EF2";
				String areaShi = "E4540EE863745BC75145C2B447AABA7C";
				String areaXian = "639677DA89AC15814C64913956609EA4";
				BIArea areaBI = new BIArea(sqlHelper, null);
				for (int r = 1, rows = sheet.getRows(); r < rows; r++) {
					boolean isNew = false;
					onePojo = areaBI.getAreaByRedis((sheet.getCell(0, r)
							.getContents()).trim());
					if (onePojo == null) {
						isNew = true;
						onePojo = new AreaPojo();
						onePojo.setId((sheet.getCell(0, r).getContents())
								.trim());
					}
					onePojo.setName((sheet.getCell(1, r).getContents()).trim());
					onePojo.getAreaType().setId(
							"75672DBD0E76CEEEE3507A1A79863446");
					// 上级
					if (onePojo.getId().endsWith("0000")) {
						// 省（自治区、直辖市）
						onePojo.getAreaClass().setId(areaShen);
						onePojo.setpId("");
					} else if (onePojo.getId().endsWith("00")) {
						// 地区（市、州、盟）
						onePojo.getAreaClass().setId(areaShi);
						onePojo.setpId(onePojo.getId().substring(0, 2) + "0000");
					} else {
						// 县（区、市、旗）
						onePojo.getAreaClass().setId(areaXian);
						onePojo.setpId(onePojo.getId().substring(0, 4) + "00");
						onePPojo = areaBI.getAreaByRedis(onePojo.getpId());
						if (onePPojo == null) {
							onePojo.setpId(onePojo.getId().substring(0, 2)
									+ "0000");
						} else {
							onePojo.setpId(onePPojo.getId());
						}
					}

					if (isNew) {
						// 更新
						areaBI.insertArea(sqlHelper, onePojo);
					} else {
						// 新增
						areaBI.updateArea(sqlHelper, onePojo);
					}
					okRow++;
				}
				retJSON.put("r", 0);
			} else {
				JSONObject oneErr = new JSONObject();
				oneErr.put("row", 0);
				retJSON.put("r", 1003);
			}
		} catch (Exception e) {
			retJSON.put("r", 999);
			retJSON.put("error", e.getMessage());
			e.printStackTrace();
		} finally {
			sqlHelper.close();
			if (book != null) {
				book.close();
			}
			retJSON.put("oknum", okRow);
			// file.delete();
		}

		retJSON.put("row", clRow);
		retJSON.put("okrow", okRow);
		if (!retJSON.containsKey("error")) {
			retJSON.put("error", URLlImplBase.ErrorMap.get(retJSON.getInt("r")));
		}
		return retJSON;
	}

	// 发布数据
	public String deleteOneFile(BFSFilePojo onePojo) throws Exception {
		String ret = "";
		// 设置旧路径
		String oldPath = BSCommon.getConfigValue("upload_path")
				+ onePojo.getFilePath();

		if (onePojo.getFileEx() == null
				|| onePojo.getFileEx().trim().equals("")) {
			onePojo.setFileEx(".png");
		}
		if (!onePojo.getFileEx().startsWith(".")) {
			onePojo.setFileEx("." + onePojo.getFileEx());
		}
		new File(oldPath + "/" + onePojo.getInstId() + onePojo.getFileEx())
				.delete();
		new File(oldPath + "/" + onePojo.getInstId() + "_min"
				+ onePojo.getFileEx()).delete();
		ret = "T";
		return ret;

	}

	public static String getFileExtension(String fileName) {
		String exStr = "";
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
			exStr = fileName.substring(fileName.lastIndexOf(".") + 1);
		}
		exStr = exStr.toLowerCase();
		return exStr;
	}

	/**
	 * <p>
	 * 方法名称: saveFileMin
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑广告图片。
	 * </p>
	 * <p>
	 * 输入参数描述: GroupPojo onePojo：用户信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int saveFileMin(File bigFile, String fileEx, File minFile)
			throws Exception {
		int count = 0;
		try {
			// 拷贝到新文件
			this.saveFile(new FileInputStream(bigFile), minFile.getPath());
			ResizeImage f = new ResizeImage();
			f.cut(minFile, fileEx, 128, 128);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return count;
	}

	// 保存文件到目录
	public File saveFile(InputStream in, String fileName) {
		File file = new File(fileName);
		(new File(file.getParent())).mkdir();
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			while ((in.read(buffer)) != -1) {
				os.write(buffer);
			}
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * <p>
	 * 方法名称: getFileByTableCol
	 * </p>
	 * <p>
	 * 方法功能描述: 得到文件
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public static String GetImgURL(BFSFilePojo file) throws Exception {
		String imgUrl = "";
		if (file != null && !file.getInstId().equals("")
				&& file.getHashID().equals("")) {
			SqlExecute sqlHelper = new SqlExecute();
			try {
				BSFileDBMang fileMang = new BSFileDBMang(sqlHelper, null);
				file = fileMang.getOneFile(file.getInstId());

			} catch (Exception ep) {
				ep.printStackTrace();
			} finally {
				sqlHelper.close();
			}
		}
		if (file != null && !file.getInstId().equals("")) {
			// 判断文件是否存在，如果不存在则显示缺省图
			imgUrl = BSCommon.getConfigValue("upload_url") + file.getFilePath()
					+ "/" + file.getInstId() + "." + file.getFileEx() + "?h="
					+ file.getHashID();
		}
		return imgUrl;
	}

	private JSONObject _checkImportFile(BFSFilePojo file, Sheet sheet) {
		JSONObject retJSON = new JSONObject();
		retJSON.put("r", 1100);
		switch (file.getOpType()) {
		case "DICITEM":

			break;
		case "WATCH":

			break;
		case "USER":

			break;
		case "TRUCKDEF":

			break;
		case "TRUCK":
			if (!sheet.getCell(0, 0).getContents().trim().equals("项目公司")) {
				retJSON.put("error", "【项目公司】列标题或位置不正确！");
				break;
			}
			if (!sheet.getCell(1, 0).getContents().trim().equals("项目组")) {
				retJSON.put("error", "【项目组】列标题或位置不正确！");
				break;
			}
			if (!sheet.getCell(2, 0).getContents().trim().equals("资产编号")) {
				retJSON.put("error", "【资产编号】列标题或位置不正确！");
				break;
			}
			if (!sheet.getCell(3, 0).getContents().trim().equals("设备类型")) {
				retJSON.put("error", "【设备类型】列标题或位置不正确！");
				break;
			}
			if (!sheet.getCell(4, 0).getContents().trim().equals("车牌号")) {
				retJSON.put("error", "【车牌号】列标题或位置不正确！");
				break;
			}
			if (!sheet.getCell(5, 0).getContents().trim().equals("车辆品牌")) {
				retJSON.put("error", "【车辆品牌】列标题或位置不正确！");
				break;
			}
			if (!sheet.getCell(6, 0).getContents().trim().equals("设备型号")) {
				retJSON.put("error", "【设备型号】列标题或位置不正确！");
				break;
			}
			if (!sheet.getCell(7, 0).getContents().trim().equals("上装出厂编号")) {
				retJSON.put("error", "【上装出厂编号】列标题或位置不正确！");
				break;
			}
			if (!sheet.getCell(8, 0).getContents().trim().equals("上装出厂时间")) {
				retJSON.put("error", "【上装出厂时间】列标题或位置不正确！");
				break;
			}
			if (sheet.getCell(9, 0).getContents().trim().indexOf("油箱尺寸") < 0) {
				retJSON.put("error", "【油箱尺寸mm(宽*厚*高)】列标题或位置不正确！");
				break;
			}
			if (!sheet.getCell(10, 0).getContents().trim().equals("内部名称")) {
				retJSON.put("error", "【内部名称】列标题或位置不正确！");
				break;
			}
			if (!sheet.getCell(11, 0).getContents().trim().equals("采购时间")) {
				retJSON.put("error", "【采购时间】列标题或位置不正确！");
				break;
			}
			if (!sheet.getCell(12, 0).getContents().trim().equals("负责人姓名")) {
				retJSON.put("error", "【负责人姓名】列标题或位置不正确！");
				break;
			}
			if (!sheet.getCell(13, 0).getContents().trim().equals("负责人工号")) {
				retJSON.put("error", "【负责人工号】列标题或位置不正确！");
				break;
			}
			if (!sheet.getCell(14, 0).getContents().trim().equals("联系方式")) {
				retJSON.put("error", "【联系方式】列标题或位置不正确！");
				break;
			}
			if (!sheet.getCell(15, 0).getContents().trim().equals("车架号")) {
				retJSON.put("error", "【车架号】列标题或位置不正确！");
				break;
			}
			if (!sheet.getCell(16, 0).getContents().trim().equals("云盒型号")) {
				retJSON.put("error", "【云盒型号】列标题或位置不正确！");
				break;
			}
			if (!sheet.getCell(17, 0).getContents().trim().equals("云盒厂商ID")) {
				retJSON.put("error", "【云盒厂商ID】列标题或位置不正确！");
				break;
			}
			if (!sheet.getCell(18, 0).getContents().trim().equals("云盒编号")) {
				retJSON.put("error", "【云盒编号】列标题或位置不正确！");
				break;
			}
			if (!sheet.getCell(19, 0).getContents().trim().equals("云盒电话号码")) {
				retJSON.put("error", "【云盒电话号码】列标题或位置不正确！");
				break;
			}
			if (!sheet.getCell(20, 0).getContents().trim().equals("ICCID")) {
				retJSON.put("error", "【ICCID】列标题或位置不正确！");
				break;
			}
			retJSON.put("r", 0);
			break;
		case "AREA":

			break;
		case "EQUIPMENTDEF":

			break;
		case "EQUIPMENTINST":

			break;
		default:
			break;
		}
		return retJSON;
	}
}
