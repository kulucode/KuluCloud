package tt.kulu.out.base;

import net.sf.json.JSONObject;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.message.biclass.BIKuluMQAdapter;

import com.tt4j2ee.m.BSObject;

public class BSTest {
	/**
	 * <p>
	 * 方法名：do_TestTruckMsg
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

	public BSObject do_TestTruckMsg(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		String mqType = m_bs.getPrivateMap().get("t_mqtype");
		BIKuluMQAdapter mqBI = new BIKuluMQAdapter();
		if (mqType.equals("1")) {
			mqBI.VehicleGeoToRedis(m_bs.getPrivateMap().get("pg_text"));
		} else {
			mqBI.doWINGSERV_TO_CLOUD_MQ(m_bs.getPrivateMap().get("pg_text"));
		}

		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}
}
