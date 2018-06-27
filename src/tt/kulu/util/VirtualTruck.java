package tt.kulu.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by Zhangka in 2018/06/15
 */
public class VirtualTruck {
    public static void huangshanVirtualTruck(JSONArray userlist)
    {
        String[] lines = { "洗扫车,皖J12155,中联牌,5180TXSDFE5,0,29.65,118.3272,29.709136", "洗扫车,皖J19512,中联牌,5160TXSDFE5,0,46.78,118.3473,29.733793", "洒水车,皖J19089,新中绿,XZL5250GPS5,0,83.70,118.373009,29.735726", "清洗车,皖J18200,中联牌,5070GQXQLE5,0,68.78,118.360837,29.729096", "洗扫车,皖J11006,中联牌,5160TXSDFE5,0,86.32,118.350507,29.718048", "洗扫车,皖J18532,中联牌,5160TXSDFE5,0,69.94,118.363873,29.744291", "清洗车,皖J15382,中联牌,5163GQXE4,0,26.08,118.290994,29.723835", "洗扫车,皖J16582,中联牌,5180TXSDFE5,0,38.00,118.279931,29.716198", "洒水车,皖J16263,新中绿,XZL5112GPS5,0,81.39,118.294241,29.70943", "洒水车,皖J13614,中联牌,5183GQXLZE5,0,98.36,118.29704,29.705779", "压缩式垃圾车,皖J12117,中联牌,5160ZYSDFE5,0,82.23,118.299959,29.715194", "压缩式对接垃圾车,皖J17477,中联牌,5162ZDJDFE4,0,28.34,118.305942,29.718142", "压缩式对接垃圾车,皖J13692,中联牌,5120ZDJDFE5,0,34.86,118.306714,29.721643", "路面养护车,皖J17062,中联牌,5020TYHSCE5,0,41.15,118.304509,29.725827", "路面养护车,皖J16256,中联牌,5020TYHDFE5,0,23.70,118.302124,29.737149", "压缩式垃圾车,皖J11345,中联牌,5160ZYSDFE5,0,42.40,118.290846,29.759479", "压缩式垃圾车,皖J19443,中联牌,5080ZYSJXE5,0,19.50,118.269448,29.75982", "压缩式垃圾车,皖J14810,中联牌,5120ZYSDF1E5,0,96.27,118.318743,29.69596", "洗扫车,皖J11168,中联牌,5100TXSQLE5,0,43.68,118.315015,29.699775", "路面养护车,皖J12967,中联牌,5020TYHSCE5,0,21.95,118.328584,29.698834" };
        for (String line : lines)
        {
            JSONObject oneObj = getVritualTruck();
            String[] fs = line.split(",");

            oneObj.put("truckorg", "屯溪项目公司-项目一组");
            oneObj.put("paltenum", fs[1]);
            oneObj.put("truckbrand", fs[2] + " " + fs[0]);
            oneObj.put("truckname", "【" + fs[1] + "】");
            oneObj.put("trucktype", fs[2] + " " + fs[0] + "[" + fs[3] + "]");
            oneObj.put("oil", fs[5]);
            oneObj.put("bdlat", fs[7]);
            oneObj.put("bdlon", fs[6]);
            oneObj.put("tbindex", Integer.valueOf(userlist.size() + 1));

            userlist.add(oneObj);
        }
    }

    public static void hanshanVirtualTruck(JSONArray userlist) {
        String[] lines = {
                "3T,后装压缩车,16号车,程力威牌,CLW5070ZYS4,0,78.23,118.042456,31.723979,下线",
                "5T,后装压缩车,17号车,中联牌,ZLJ5120ZYSDFE5,0,87.91,118.143412,31.743506,下线",
                "3T,后装压缩车,18号车,中联牌,ZLJ5120ZYSDFE5,0,89.95,118.119427,31.697974,下线",
                "3T,箱式垃圾车,19号车,中联牌,ZLJ5120ZYSDFE5,0,67.55,118.015956,31.753723,下线",
                "3T,拉臂车,20号车,中联牌,ZLJ5120ZYSDFE5,0,35.38,117.951817,31.719389,下线",
                "5T,拉臂车,26号车,中联牌,ZLJ5120ZYSDFE5,0,84.43,118.039294,31.641576,下线",
                "3T,拉臂车,27号车,中联牌,ZLJ5120ZYSDFE5,0,89.67,117.990066,31.642913,下线",
                "5T,拉臂车,28号车,中联牌,ZLJ5120ZYSDFE5,0,59.75,118.06472,31.721979,下线",
                "3T,箱式自卸车,29号车,中联牌,ZLJ5070ZYSEQE5,0,13.05,118.007853,31.72252,下线",
                "3T,箱式车自卸车,30号车,中联牌,ZLJ5070ZYSEQE5,0,27.53,118.089805,31.769456,下线",
                "3T,拉臂车,36号车,中联牌,ZLJ5070ZYSEQE5,0,25.12,118.183076,31.778274,下线",
                "3T,后装压缩车,37号车,中联牌,ZLJ5070ZYSEQE5,0,97.64,118.145922,31.726459,下线"};
        for (String line : lines)
        {
            JSONObject oneObj = getVritualTruck();
            String[] fs = line.split(",");

            oneObj.put("truckorg", "含山项目公司-项目一组");
            oneObj.put("trucknno", fs[0]);
            oneObj.put("paltenum", fs[2]);
            oneObj.put("truckbrand", fs[3] + " " + fs[1]);
            oneObj.put("truckname", "【" + fs[1] + "】");
            oneObj.put("trucktype", fs[3] + " " + fs[1] + "[" + fs[4] + "]");
            oneObj.put("oil", fs[6]);
            oneObj.put("bdlat", fs[8]);
            oneObj.put("bdlon", fs[7]);
            oneObj.put("tbindex", Integer.valueOf(userlist.size() + 1));


            userlist.add(oneObj);
        }
    }

    public static JSONObject getVritualTruck()
    {
        JSONObject oneObj = new JSONObject();
        oneObj.put("date", "2018-06-15 11:24:53");
        oneObj.put("fanceflg", Integer.valueOf(1));
        oneObj.put("geodate", "2018-05-25 16:37:05");
        oneObj.put("oildate", "018-05-25 16:37:05");
        oneObj.put("oildiff", "0");
        oneObj.put("online", "下线");
        oneObj.put("onlinev", Integer.valueOf(0));
        oneObj.put("speed", "0");
        oneObj.put("trucknno", "");

        return oneObj;
    }
}
