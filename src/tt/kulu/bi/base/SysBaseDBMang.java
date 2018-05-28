package tt.kulu.bi.base;

import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

public class SysBaseDBMang extends BSDBBase {
	public SysBaseDBMang(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: SysBaseIni
	 * </p>
	 * <p>
	 * 方法功能描述: 添加企业。
	 * </p>
	 * <p>
	 * 输入参数描述: GroupPojo onePojo：企业对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int SysBaseIni() throws Exception {
		int count = 0;
		// 设备
		count += this.sqlHelper.updateBySql("delete from T_WATCH_BRE");
		count += this.sqlHelper.updateBySql("delete from T_WATCH_BRO");
		// count += this.sqlHelper.updateBySql("delete from T_WATCH_FALL");
		count += this.sqlHelper.updateBySql("delete from T_WATCH_STEP");
		// count += this.sqlHelper.updateBySql("delete from T_WATCH_BBE");
		count += this.sqlHelper.updateBySql("delete from T_WATCH_HRE");
		count += this.sqlHelper.updateBySql("delete from T_WATCH_HR");
		count += this.sqlHelper.updateBySql("delete from T_VEHICLE_DATA");
		count += this.sqlHelper.updateBySql("delete from T_EQUIPMENT_INST_R");
		count += this.sqlHelper.updateBySql("delete from T_EQP_INST_GEOMETRY");
		count += this.sqlHelper
				.updateBySql("delete from T_EQP_INST_GEOMETRY_HOUR");
		count += this.sqlHelper.updateBySql("delete from T_EQUIPMENT_WORKLOG");
		count += this.sqlHelper.updateBySql("delete from T_EQUIPMENT_INST");
		count += this.sqlHelper.updateBySql("delete from T_EQUIPMENT_DEF");
		// 车辆
		count += this.sqlHelper.updateBySql("delete from T_TRUCK_FIX_LOGS");
		count += this.sqlHelper.updateBySql("delete from T_INSPECT_PLAN");
		count += this.sqlHelper.updateBySql("delete from T_INSPECT_DEF");
		count += this.sqlHelper.updateBySql("delete from T_TRUCK_WORK_STATS");
		// count += this.sqlHelper.updateBySql("delete from T_TRUCK_FALUT");
		count += this.sqlHelper
				.updateBySql("delete from T_TRUCK_WORK_DAY_LOGS");
		count += this.sqlHelper.updateBySql("delete from T_TRUCK_INST_VIDEO");
		count += this.sqlHelper.updateBySql("delete from T_DRIVER_SCHEDULING");
		count += this.sqlHelper.updateBySql("delete from T_FANCE_TRUCK_R");
		count += this.sqlHelper.updateBySql("delete from T_TRUCK_INST_USER");
		count += this.sqlHelper.updateBySql("delete from T_TRUCK_INST");
		count += this.sqlHelper.updateBySql("delete from T_TRUCK_DEF");
		// 人员
		count += this.sqlHelper.updateBySql("delete from T_USER_WORK_DAY_LOGS");
		count += this.sqlHelper.updateBySql("delete from T_USER_SCHEDULING");
		count += this.sqlHelper.updateBySql("delete from T_FANCE_USER_R");
		count += this.sqlHelper
				.updateBySql("delete from T_ORG_USER_R where ORG_ID not in ('ADMIN_GROUP')");
		count += this.sqlHelper
				.updateBySql("delete from T_ROLE_USER_R where USER_INSTID not in ('SUPER_ADMIN')");
		count += this.sqlHelper
				.updateBySql("delete from T_ORG where ORG_ID not in ('ADMIN_GROUP')");
		count += this.sqlHelper
				.updateBySql("delete from T_USER where USER_INSTID not in ('SUPER_ADMIN')");
		// 其他
		count += this.sqlHelper
				.updateBySql("delete from T_COMPANY where COMP_ID not in ('0000')");
		count += this.sqlHelper.updateBySql("delete from T_FAULTREPORT");
		count += this.sqlHelper.updateBySql("delete from T_FANCE");
		count += this.sqlHelper.updateBySql("delete from T_FILE");

		return count;
	}
}
