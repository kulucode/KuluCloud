<%@ page contentType="image/jpeg"%>
<%@ page import="java.awt.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.awt.image.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.imageio.*"%>
<%@ page import="com.tt4j2ee.BSCommon"%>
<%@ page import="tt.kulu.out.call.BIRedis"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP   1.1   
	response.setHeader("Pragma", "no-cache"); //HTTP   1.0   
	response.setHeader("P3P", "CP=CAO PSA OUR");
	response.setHeader("Access-Control-Allow-Headers",
			"Content-Type, api_key, Authorization");
	response.setHeader("Access-Control-Allow-Methods",
			"HEAD, POST, OPTIONS, GET");
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Credentials", "true");
	response.setDateHeader("Expires", 0); //prevents   caching   at   the   proxy   server   
	String rand = BSCommon.getRandStr(4);
	try {
		String sess = "";
		System.out.println("TTSSID:" + request.getParameter("TTSSID"));
		String sessIdStr = request.getParameter("TTSSID");
		String[] sessId = null;
		if (sessIdStr != null) {
			sessId = sessIdStr.split("\\|");
			if (sessId.length > 0) {
				sess = sessId[0];
			}
		}
		if (sess != null && !sess.equals("")) {
			BIRedis redisBI = new BIRedis();
			redisBI.setStringData("code_" + sess, rand, 120, 1);
		}
	} catch (Exception mex) {
		System.out.println(mex.getMessage());
	}
%>
<%!Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}%>

<%
	out.clear();
	//	response.reset();

	//å¨åå­ä¸­åå»ºå¾ç
	int iWidth = 100, iHeight = 39;
	BufferedImage image = new BufferedImage(iWidth, iHeight,
			BufferedImage.TYPE_INT_RGB);
	//Ã¨ÂÂ·Ã¥ÂÂÃ¥ÂÂ¾Ã¥Â½Â¢Ã¤Â¸ÂÃ¤Â¸ÂÃ¦ÂÂ
	Graphics g = image.getGraphics();
	//Ã¨Â®Â¾Ã¥Â®ÂÃ¨ÂÂÃ¦ÂÂ¯Ã¨ÂÂ²
	g.setColor(getRandColor(200, 250));
	//Ã§ÂÂ»Ã¨Â¾Â¹Ã¦Â¡Â
	g.fillRect(0, 0, iWidth, iHeight);
	Random random = new Random();
	g.setColor(getRandColor(160, 200));
	for (int i = 0; i < 155; i++) {
		int x = random.nextInt(iWidth);
		int y = random.nextInt(iHeight);
		int xl = random.nextInt(12);
		int yl = random.nextInt(12);
		g.drawLine(x, y, x + xl, y + yl);
	}

	//Ã¥Â¾ÂÃ¥ÂÂ°Ã©ÂÂÃ¥ÂÂ³Ã§Â ÂÃ¯Â¼Â4Ã¤Â½ÂÃ¯Â¼Â
	//String rand=BSDes.decrypt(request.getParameter("Rand"),"");

	//Ã¥Â°ÂÃ©ÂªÂÃ¨Â¯ÂÃ§Â ÂÃ¦ÂÂ¾Ã§Â¤ÂºÃ¥ÂÂ°Ã¥ÂÂ¾Ã¥ÂÂÃ¤Â¸Â­
	g.setFont(new Font("Verdana", Font.PLAIN, 30));
	for (int i = 0; i < rand.length(); i++) {
		g.setColor(new Color(20 + random.nextInt(110), 20 + random
				.nextInt(110), 20 + random.nextInt(110)));
		g.drawString(rand.substring(i, i + 1), 20 * i + 10, 30);
	}
	//Ã¥ÂÂ¾Ã¥ÂÂÃ§ÂÂÃ¦ÂÂ
	g.dispose();
	try {
		OutputStream os = response.getOutputStream();
		ImageIO.write(image, "JPEG", os);
		//os.flush();
		os.close();
		out.clear();
		out = pageContext.pushBody();
	} catch (Exception e) {
	}
%>
