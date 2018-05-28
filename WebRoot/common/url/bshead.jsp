<%@ page import="com.tt4j2ee.m.BSObject"%>
<%@ page import="com.tt4j2ee.BSCommon"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP   1.1   
	response.setHeader("Pragma", "no-cache"); //HTTP   1.0   
	response.setHeader("P3P",
			"CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
	response.setHeader("Access-Control-Allow-Headers",
			"Content-Type, api_key, Authorization");
	response.setHeader("Access-Control-Allow-Methods",
			"HEAD, POST, OPTIONS, GET");
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setDateHeader("Expires", 0); //prevents   caching   at   the   proxy   server   
	BSObject m_bs = null;
	try {
		if (session.getAttribute("m_bs") != null) {
			m_bs = (BSObject) session.getAttribute("m_bs");
		}
		if (m_bs == null) {
			m_bs = new BSObject();
			m_bs.setIni(request, response, true);
		}
	} catch (Exception mex) {
		System.out.println(mex.getMessage());
	} finally {
		if (m_bs == null) {
			m_bs = new BSObject();
		}
		m_bs.setRequest(request, response);
	}
%>
