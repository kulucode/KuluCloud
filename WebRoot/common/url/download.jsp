<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@page import="java.io.*"%>
<%@page import="java.net.*"%>
<%
	String filePath = "";
	String fileName = "123";
	if (request.getParameter("file") != null) {
		filePath = request.getParameter("file");
	}
	if (request.getParameter("name") != null) {
		fileName = request.getParameter("name");
	}
	if (new File(filePath).exists()) {
		response.setContentType("application/x-msdownload");
		response.setHeader(
				"Content-disposition",
				"attachment;filename="
						+ new String(fileName.getBytes("GBK"),
								"ISO_8859_1"));
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(filePath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Excetpion :" + e);
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
		out.clear();
		out = pageContext.pushBody();
	} else {
		response.getWriter().write("找不到文件");
	}
%>
