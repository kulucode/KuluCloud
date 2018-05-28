package tt.kulu.bi.base;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.fileupload.FileItem;

import tt.kulu.bi.file.pojo.BFSFilePojo;
import tt.kulu.bi.user.pojo.UserPojo;

import com.tt4j2ee.BSCommon;
import com.tt4j2ee.BSDateEx;
import com.tt4j2ee.BSGuid;

/**
 * 简单的HTTP请求处理器，能够以POST和GET方式访问URL。
 */
public class HttpRequestHandler {

	// 序列化UID
	private static final long serialVersionUID = -4304310068298079604L;
	// 数据编码
	private static final String ENCODING = "UTF-8";

	// 连接超时值
	private int connectTimeout = 20000;
	// 读取超时值
	private int readTimeout = 20000;

	/**
	 * 
	 * @return 获取当前的链接超时值
	 */
	public int getConnectTimeout() {
		return connectTimeout;
	}

	/**
	 * 设置连接超时值
	 * 
	 * @param connectTimeout
	 *            新的连接超时值（毫秒），-1代表不设置
	 */
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * 
	 * @return 获取当前的读取超时值
	 */
	public int getReadTimeout() {
		return readTimeout;
	}

	/**
	 * 设置读取超时值
	 * 
	 * @param readTimeout
	 *            新的读取超时值（毫秒）， -1 代表不设置
	 */
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	/**
	 * 构造函数
	 */
	public HttpRequestHandler() {
		// empty
	}

	/**
	 * 构造函数
	 * 
	 * @param connectTimeout
	 *            连接超时值（毫秒），-1代表不设置
	 * @param readTimeout
	 *            读取超时值（毫秒），-1代表不设置
	 */
	public HttpRequestHandler(int connectTimeout, int readTimeout) {
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}

	/**
	 * 关闭对象。
	 * 
	 * @param f
	 *            待关闭的对象
	 */
	public static void close(Closeable f) {
		if (f != null) {
			try {
				f.close();
			} catch (IOException ex) {
				Logger.getAnonymousLogger().info(ex.toString());
			}
		}
	}

	/**
	 * POST方式访问URL
	 * 
	 * @param methodUrl
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String postRequest(String path, List<String> params, UserPojo user)
			throws Exception {
		URL methodUrl = new URL(path);
		HttpURLConnection conn = null;
		OutputStream out = null;
		InputStream in = null;
		BSDateEx dateEX = new BSDateEx();
		try {
			params.add("uu=" + BSGuid.getRandomGUID());
			// 构造连接
			conn = (HttpURLConnection) methodUrl.openConnection();
			conn.setConnectTimeout(2000);
			conn.setReadTimeout(30000);

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("contentType", "utf-8");
			conn.setRequestProperty("Charset", "utf-8");
			conn.setRequestProperty("Content-type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Accept-encoding", "gzip,deflate");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 调用3次
			try {
				conn.connect();
			} catch (Exception e1) {
				try {
					conn.connect();
				} catch (Exception e2) {
					try {
						conn.connect();
					} catch (Exception e3) {
						throw e3;
					}
				}
			}

			// 写入POST数据
			out = conn.getOutputStream();
			CharSequence paramString = (null == params) ? "" : merge(params,
					"&");
			// out.write(URLEncoder.encode(paramString.toString(), "utf-8")
			// .getBytes(ENCODING));
			out.write(paramString.toString().getBytes(ENCODING));

			// 读取服务器响应
			in = conn.getInputStream();
			// gzip解压
			// 打印
			System.out.println("推送消息到:"
					+ BSCommon.getConfigValue("userconfig_msg_server") + "||"
					+ paramString.toString());

			return this.uncompress(in);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("network_error:", ex);
		} finally {
			try {
				out.flush();
				in.close();
				out.close();
			} catch (IOException ex2) {

			}
			if (conn != null) {
				conn.disconnect();
			}
			System.out.println("-----end send-----" + dateEX.getThisDate(0, 0));
		}
	}

	/**
	 * POST方式访问URL
	 * 
	 * @param methodUrl
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String postFileRequest(String path, HashMap<String, String> params,
			BFSFilePojo oneFile, FileItem fi) throws Exception {
		URL methodUrl = new URL(path);
		HttpURLConnection conn = null;
		OutputStream out = null;
		InputStream in = null;
		String BOUNDARY = "-----files";
		String endline = "--" + BOUNDARY + "--\r\n";// 数据结束标志
		try {
			// 构造连接
			conn = (HttpURLConnection) methodUrl.openConnection();
			if (connectTimeout != -1) {
				conn.setConnectTimeout(connectTimeout);
			}
			if (readTimeout != -1) {
				conn.setReadTimeout(readTimeout);
			}
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("contentType", "utf-8");
			conn.setRequestProperty("Charset", "utf-8");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-type",
					"multipart/form-data; boundary=" + BOUNDARY);
			conn.setRequestProperty("Accept-encoding", "gzip,deflate");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.connect();

			// 写入POST数据
			out = conn.getOutputStream();

			// 写入文件
			StringBuffer strBuf = new StringBuffer();
			String fileName = new String(oneFile.getFileName().getBytes(),
					"UTF-8");
			if (fi != null && fi.getSize() > 0) {
				strBuf.append("--"
						+ BOUNDARY
						+ "\r\nContent-Disposition: form-data; name=\"Filename\"\r\n\r\n"
						+ fileName + "\r\n");
				strBuf.append("--"
						+ BOUNDARY
						+ "\r\nContent-Disposition: form-data; name=\"Filedata\"; filename=\""
						+ fileName + "\"\r\n");
				strBuf.append("Content-Type: application/octet-stream\r\n\r\n");
				// System.out.println(strBuf.toString());
				out.write((strBuf.toString()).getBytes(ENCODING));

				DataInputStream fin = new DataInputStream(fi.getInputStream());
				int bytes = 0;
				byte[] bufferOut = new byte[1024];
				while ((bytes = fin.read(bufferOut)) != -1) {
					out.write(bufferOut, 0, bytes);
				}
				fin.close();
			}
			// 数据
			strBuf = new StringBuffer();
			strBuf.append("--" + BOUNDARY
					+ "\r\nContent-Disposition: form-data;name=\"uu\"\r\n\r\n"
					+ BSGuid.getRandomGUID() + "\r\n");
			if (params != null && params.size() > 0) {
				Iterator<Map.Entry<String, String>> iter = params.entrySet()
						.iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("--" + BOUNDARY
							+ "\r\nContent-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n" + inputValue + "\r\n");
				}
			}
			strBuf.append(endline);
			out.write((strBuf.toString()).getBytes(ENCODING));
			out.flush();
			out.close();

			// 读取服务器响应
			in = conn.getInputStream();
			// gzip解压
			return this.uncompress(in);
		} catch (IOException ex) {
			throw new Exception("network_error", ex);
		} finally {
			close(in);
			close(out);
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	/**
	 * POST方式访问URL
	 * 
	 * @param methodUrl
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String postFileRequest(String path, HashMap<String, String> params,
			BFSFilePojo onePojo, InputStream fi) throws Exception {
		URL methodUrl = new URL(path);
		HttpURLConnection conn = null;
		OutputStream out = null;
		InputStream in = null;
		String BOUNDARY = "-----files";
		String endline = "--" + BOUNDARY + "--\r\n";// 数据结束标志
		try {
			// 构造连接
			conn = (HttpURLConnection) methodUrl.openConnection();
			if (connectTimeout != -1) {
				conn.setConnectTimeout(connectTimeout);
			}
			if (readTimeout != -1) {
				conn.setReadTimeout(readTimeout);
			}
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("contentType", "utf-8");
			conn.setRequestProperty("Charset", "utf-8");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-type",
					"multipart/form-data; boundary=" + BOUNDARY);
			conn.setRequestProperty("Accept-encoding", "gzip,deflate");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.connect();

			// 写入POST数据
			out = conn.getOutputStream();

			// 写入文件
			StringBuffer strBuf = new StringBuffer();
			if (onePojo != null && !onePojo.getSize().equals("")) {
				strBuf.append("--"
						+ BOUNDARY
						+ "\r\nContent-Disposition: form-data; name=\"Filename\"\r\n\r\n"
						+ onePojo.getFileName() + "\r\n");
				strBuf.append("--"
						+ BOUNDARY
						+ "\r\nContent-Disposition: form-data; name=\"Filedata\"; filename=\""
						+ onePojo.getFileName() + "\"\r\n");
				strBuf.append("Content-Type: application/octet-stream\r\n\r\n");
				// System.out.println(strBuf.toString());
				out.write((strBuf.toString()).getBytes(ENCODING));

				DataInputStream fin = new DataInputStream(fi);
				int bytes = 0;
				byte[] bufferOut = new byte[1024];
				while ((bytes = fin.read(bufferOut)) != -1) {
					out.write(bufferOut, 0, bytes);
				}
				fin.close();
			}
			// 数据
			strBuf = new StringBuffer();
			strBuf.append("--" + BOUNDARY
					+ "\r\nContent-Disposition: form-data;name=\"uu\"\r\n\r\n"
					+ BSGuid.getRandomGUID() + "\r\n");
			if (params != null && params.size() > 0) {
				Iterator<Map.Entry<String, String>> iter = params.entrySet()
						.iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("--" + BOUNDARY
							+ "\r\nContent-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n" + inputValue + "\r\n");
				}
			}
			strBuf.append(endline);
			out.write((strBuf.toString()).getBytes(ENCODING));
			out.flush();
			out.close();

			// 读取服务器响应
			in = conn.getInputStream();
			// gzip解压
			return this.uncompress(in);
		} catch (IOException ex) {
			throw new Exception("network_error", ex);
		} finally {
			close(in);
			close(out);
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	/**
	 * 执行HTTP GET请求。
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param charset
	 *            字符集，如UTF-8, GBK, GB2312
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String getRequest(String url, Map<String, String> params,
			String charset) throws IOException {
		HttpURLConnection conn = null;
		String rsp = null;

		try {
			String ctype = "application/x-www-form-urlencoded;charset="
					+ charset;
			String query = buildQuery(params, charset);
			try {
				conn = getConnection(buildGetUrl(url, query), "GET", ctype);
			} catch (IOException e) {
				Map<String, String> map = getParamsFromUrl(url);
				throw e;
			}

			try {
				rsp = getResponseAsString(conn);
			} catch (IOException e) {
				Map<String, String> map = getParamsFromUrl(url);
				throw e;
			}

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return rsp;
	}

	public static String buildQuery(Map<String, String> params, String charset)
			throws IOException {
		if (params == null || params.isEmpty()) {
			return null;
		}

		StringBuilder query = new StringBuilder();
		Set<Entry<String, String>> entries = params.entrySet();
		boolean hasParam = false;

		for (Entry<String, String> entry : entries) {
			String name = entry.getKey();
			String value = entry.getValue();
			// 忽略参数名或参数值为空的参数
			if (areNotEmpty(name, value)) {
				if (hasParam) {
					query.append("&");
				} else {
					hasParam = true;
				}

				query.append(name).append("=")
						.append(URLEncoder.encode(value, charset));
			}
		}

		return query.toString();
	}

	private static URL buildGetUrl(String strUrl, String query)
			throws IOException {
		URL url = new URL(strUrl);
		if (isEmpty(query)) {
			return url;
		}

		if (isEmpty(url.getQuery())) {
			if (strUrl.endsWith("?")) {
				strUrl = strUrl + query;
			} else {
				strUrl = strUrl + "?" + query;
			}
		} else {
			if (strUrl.endsWith("&")) {
				strUrl = strUrl + query;
			} else {
				strUrl = strUrl + "&" + query;
			}
		}

		return new URL(strUrl);
	}

	/**
	 * 将参数列表连接为一个字符串，以delimiter为连接符
	 * 
	 * @param params
	 *            待连接的参数列表
	 * @param delimiter
	 *            两个参数之间的连接符
	 * @return 连接后的字符串
	 */
	private static CharSequence merge(List<String> params, String delimiter) {
		if (params == null || params.isEmpty()) {
			return "";
		}
		StringBuilder buffer = new StringBuilder();
		boolean isFirst = true;
		for (String param : params) {
			if (isFirst == false) {
				buffer.append(delimiter);
			} else {
				isFirst = false;
			}
			buffer.append(param.toString());
		}
		return buffer;
	}

	/**
	 * 检查指定的字符串是否为空。
	 * <ul>
	 * <li>SysUtils.isEmpty(null) = true</li>
	 * <li>SysUtils.isEmpty("") = true</li>
	 * <li>SysUtils.isEmpty("   ") = true</li>
	 * <li>SysUtils.isEmpty("abc") = false</li>
	 * </ul>
	 * 
	 * @param value
	 *            待检查的字符串
	 * @return true/false
	 */
	public static boolean isEmpty(String value) {
		int strLen;
		if (value == null || (strLen = value.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(value.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	private static Map<String, String> getParamsFromUrl(String url) {
		Map<String, String> map = null;
		if (url != null && url.indexOf('?') != -1) {
			map = splitUrlQuery(url.substring(url.indexOf('?') + 1));
		}
		if (map == null) {
			map = new HashMap<String, String>();
		}
		return map;
	}

	/**
	 * 从URL中提取所有的参数。
	 * 
	 * @param query
	 *            URL地址
	 * @return 参数映射
	 */
	public static Map<String, String> splitUrlQuery(String query) {
		Map<String, String> result = new HashMap<String, String>();

		String[] pairs = query.split("&");
		if (pairs != null && pairs.length > 0) {
			for (String pair : pairs) {
				String[] param = pair.split("=", 2);
				if (param != null && param.length == 2) {
					result.put(param[0], param[1]);
				}
			}
		}

		return result;
	}

	/**
	 * 检查指定的字符串列表是否不为空。
	 */
	public static boolean areNotEmpty(String... values) {
		boolean result = true;
		if (values == null || values.length == 0) {
			result = false;
		} else {
			for (String value : values) {
				result &= !isEmpty(value);
			}
		}
		return result;
	}

	protected static String getResponseAsString(HttpURLConnection conn)
			throws IOException {
		String charset = getResponseCharset(conn.getContentType());
		InputStream es = conn.getErrorStream();
		if (es == null) {
			return getStreamAsString(conn.getInputStream(), charset);
		} else {
			String msg = getStreamAsString(es, charset);
			if (isEmpty(msg)) {
				throw new IOException(conn.getResponseCode() + ":"
						+ conn.getResponseMessage());
			} else {
				throw new IOException(msg);
			}
		}
	}

	private static String getStreamAsString(InputStream stream, String charset)
			throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					stream, charset));
			StringWriter writer = new StringWriter();

			char[] chars = new char[256];
			int count = 0;
			while ((count = reader.read(chars)) > 0) {
				writer.write(chars, 0, count);
			}

			return writer.toString();
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	private static String getResponseCharset(String ctype) {
		String charset = ENCODING;

		if (!isEmpty(ctype)) {
			String[] params = ctype.split(";");
			for (String param : params) {
				param = param.trim();
				if (param.startsWith("charset")) {
					String[] pair = param.split("=", 2);
					if (pair.length == 2) {
						if (!isEmpty(pair[1])) {
							charset = pair[1].trim();
						}
					}
					break;
				}
			}
		}

		return charset;
	}

	private static HttpURLConnection getConnection(URL url, String method,
			String ctype) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(method);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html");
		conn.setRequestProperty("User-Agent", "top-sdk-java");
		conn.setRequestProperty("Content-Type", ctype);
		// conn.setRequestProperty("Accept-encoding", "gzip,deflate");
		return conn;
	}

	private String uncompress(InputStream in) throws IOException {
		String outStr = "";
		boolean isOk = true;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			GZIPInputStream gunzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = gunzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			outStr = out.toString();
			// toString()使用平台默认编码，也可以显式的指定如toString(&quot;GBK&quot;)
		} catch (Exception ep) {
			isOk = false;
		}
		if (!isOk) {
			Reader reader = new BufferedReader(new InputStreamReader(in,
					"UTF-8"));
			StringBuilder buffer = new StringBuilder();
			char[] buf = new char[1000];
			int len = 0;
			while (len >= 0) {
				buffer.append(buf, 0, len);
				len = reader.read(buf);
			}
			outStr = "{\"" + buffer.toString();
		}
		return outStr;
	}
}
