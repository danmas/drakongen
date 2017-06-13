package cb.dfs.trail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.net.Authenticator;
//import java.net.HttpURLConnection;
//import java.net.PasswordAuthentication;
//import java.net.URL;
//import java.net.URLConnection;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.net.CookieHandler;
//import java.net.CookieManager;
//import java.net.CookiePolicy;
//import java.net.CookieStore;
//import java.net.HttpCookie;

//import org.apache.log4j.Logger;

import cb.dfs.trail.common.Constants;
import cb.dfs.trail.utils.Strings;

public class TrailHttp extends TrailBase {

	//final static Logger logger = Logger.getLogger(TrailHttp.class);

	String url = null;
	String url_auth = null;
	String proxy_host = null;
	String proxy_port = null;
	String proxy_user = null;
	String proxy_auth = null;
	
	String properties = null;
	String cookies = null;
	String bad_strings = null;
	String login_strings = null;
	String body = "";

	boolean is_read_page = true;

	public TrailHttp(String trail_key) throws Exception {
		super("", trail_key, Constants.TT_HTTP, "", "0", "0", "0");
	}

	public TrailHttp(String run_agent, String trail_key, String description, String _urlString, String _url_auth,
			String _proxy_host, String _proxY_port, String _proxy_user, String _proxy_password, String _properties,
			String _cookies, String _bad_strings, String _login_strings, boolean _is_read_page, String max_duration_in_sec,
			String launch_period_in_sec, String control_time_delay_in_sec) throws Exception {
		super(run_agent, trail_key, Constants.TT_HTTP, description, max_duration_in_sec, launch_period_in_sec,
				control_time_delay_in_sec);
		url = _urlString;
		url_auth = _url_auth;
		properties = _properties;
		cookies = _cookies;
		bad_strings = _bad_strings;
		login_strings = _login_strings;
		is_read_page = _is_read_page;
		proxy_host = _proxy_host;
		proxy_port = _proxY_port;
		proxy_user = _proxy_user;
		proxy_auth = _proxy_password;
	}

	public TrailHttp(String run_agent, String trail_key, String description, String _urlString, String _url_auth,
			String _properties, String _cookies, String _bad_strings, String _login_strings, boolean _is_read_page, String max_duration_in_sec,
			String launch_period_in_sec, String control_time_delay_in_sec) throws Exception {
		super(run_agent, trail_key, Constants.TT_HTTP, description, max_duration_in_sec, launch_period_in_sec,
				control_time_delay_in_sec);
		url = _urlString;
		url_auth = _url_auth;
		properties = _properties;
		cookies = _cookies;
		bad_strings = _bad_strings;
		login_strings = _login_strings;
		is_read_page = _is_read_page;
	}

	@Override
	public void setParam(String param, String value) throws Exception {
		switch (param.trim().toLowerCase()) {
		case "url":
			url = value;
			break;
		case "properties":
			properties = value;
			break;
		case "cookies":
			cookies = value;
			break;
		case "bad_strings":
			bad_strings = value;
			break;
		case "login_strings":
			login_strings = value;
			break;
		default:
			try {
				super.setParam(param, value);
			} catch (Exception ex) {
				throw new Exception("Неправильно задано имя параметра <" + param + "> или значение <" + value + ">"
						+ "\nИсключение в  cb.dfs.trail.TrailHttp.setParam()");
			}
		}
	}

	@Override
	public String toString() {
		return "TrailHttp [url=" + url + ", proxy_host=" + proxy_host + ", proxy_port=" + proxy_port + ", proxy_user="
				+ proxy_user + ", proxy_auth=" + proxy_auth + ", properties=" + properties + ", cookies=" + cookies
				+ ", is_read_page=" + is_read_page + ", retOutStr=" + retOutStr + ", retErrStr=" + retErrStr
				+ ", status=" + status + ", ob_object_id=" + ob_object_id + ", run_agent=" + run_agent + ", trail_key="
				+ trail_key + ", trail_type=" + trail_type + ", description=" + description + ", max_duration_in_sec="
				+ max_duration_in_sec + ", launch_period_in_sec=" + launch_period_in_sec
				+ ", control_time_delay_in_sec=" + control_time_delay_in_sec + ", last_start_date=" + last_start_date
				+ ", duration=" + duration + ", launch_period=" + launch_period + ", control_time_delay="
				+ control_time_delay + "]";
	}

	@Override
	public void overwrite(Connection conn) throws Exception {

		super.overwrite(conn, "v_trails_http");

		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(
					"update v_trails_http " + " set url=?, url_auth=?, properties=?, cookies=?, bad_strings=?, login_strings=? "
							+ ", proxy_host=?, proxy_port=?, proxy_user=?, proxy_auth=? " + " where trail_key=?");
			int i = 1;
			stmt.setString(i++, url);
			stmt.setString(i++, url_auth);

			Clob myClob = conn.createClob();
			myClob.setString(1, properties);
			stmt.setClob(i++, myClob);
			Clob myClob2 = conn.createClob();
			myClob2.setString(1, cookies);
			stmt.setClob(i++, myClob2);
			Clob myClob3 = conn.createClob();
			myClob3.setString(1, bad_strings);
			stmt.setClob(i++, myClob3);
			Clob myClob4 = conn.createClob();
			myClob4.setString(1, login_strings);
			stmt.setClob(i++, myClob4);

			stmt.setString(i++, proxy_host);
			stmt.setString(i++, proxy_port);
			stmt.setString(i++, proxy_user);
			stmt.setString(i++, proxy_auth);
			stmt.setString(i++, trail_key);

			int cnt = stmt.executeUpdate();
			if (cnt != 1) {
				throw new Exception("Не удалось сделать запись.");
			}

			stmt.close();
		} catch (SQLException se) {
			String str = "Ошибка " + se.getMessage() + "\nSQL исключение в TrailHttp.write()";
			throw new Exception(str);
		} catch (Exception e) {
			String str = "Ошибка " + e.getMessage() + "\nИсключение в TrailHttp.write()";
			throw new Exception(str);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} 
		} 
	}

	@Override
	public void read(Connection conn) throws Exception {

		super.read(conn);

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("select url, url_auth, properties, cookies, bad_strings, login_strings"
					+ ", proxy_host, proxy_port, proxy_user, proxy_auth " + " from V_TRAILS_HTTP"
					+ " where trail_key=?");
			stmt.setString(1, trail_key);
			ResultSet rs = stmt.executeQuery();
			int cnt = 0;
			while (rs.next()) {
				cnt++;
				url = rs.getString("url");
				url_auth = rs.getString("url_auth");
				properties = rs.getString("properties");
				cookies = rs.getString("cookies");
				bad_strings = rs.getString("bad_strings");
				login_strings = rs.getString("login_strings");
				
				proxy_host = rs.getString("proxy_host");
				proxy_port = rs.getString("proxy_port");
				proxy_user = rs.getString("proxy_user");
				proxy_auth = rs.getString("proxy_auth");
			}
			rs.close();
			stmt.close();
			if (cnt != 1) {
				String str = "Должна быть одна запись для операции проверки <" + trail_key + ">.";
				throw new Exception(str);
			}
		} catch (SQLException se) {
			String str = "Ошибка " + se.getMessage() + "\nSQL исключение в TrailHttp.read()";
			throw new Exception(str);
		} catch (Exception e) {
			String str = "Ошибка " + e.getMessage() + "\nИсключение в TrailHttp.read()";
			throw new Exception(str);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
		}
	}

	
	protected boolean has_bad_string() {
		if (bad_strings != null && bad_strings.trim().length() > 0) {
			List<String> bsm = new ArrayList<String>();
			Strings.fillValueList(bad_strings, bsm, Constants.HTTP_BADSTRINGS_DELIMITER);
			for (String val : bsm) {
				//logger.debug("-- bad string val = " + val);
				if (body.indexOf(val) != -1) {
					return true;
				}
			}
		}
		return false;
	}
	
	protected boolean has_login_string() {
		if (login_strings != null && login_strings.trim().length() > 0) {
			List<String> bsm = new ArrayList<String>();
			Strings.fillValueList(login_strings, bsm, Constants.HTTP_LOGIN_STRINGS_DELIMITER);
			for (String val : bsm) {
				//logger.debug("-- login string val = " + val);
				if (body.indexOf(val) != -1) {
					return true;
				}
			}
		}
		return false;
	}
	
	protected int read(String _url, boolean use_cookie) throws Exception {
		body = "";
		int ret_code = -1; // -- -1 значит нельзя установить код возврата
		
		final String AUTH_PROXY = proxy_host;      // "http-proxy.srv.rdtex.ru";
		final String AUTH_PROXY_PORT = proxy_port; // "3128";
		final String AUTH_USER = proxy_user;       // "roman_eremeev";
		final String AUTH_PASSWORD = proxy_auth;   // "xxxxx";

		if (proxy_host != null && proxy_host != "") {
			System.setProperty("java.net.useSystemProxies", "true");
			System.setProperty("http.proxyHost", AUTH_PROXY);
			System.setProperty("http.proxyPort", AUTH_PROXY_PORT);
			if (AUTH_USER != null && AUTH_PASSWORD != null) {
				System.setProperty("http.proxyUser", AUTH_USER);
				System.setProperty("http.proxyPassword", AUTH_PASSWORD);
			}

			if (AUTH_USER != null && AUTH_PASSWORD != null) {
				Authenticator.setDefault(new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(AUTH_USER, AUTH_PASSWORD.toCharArray());
					}
				});
			}
		}

		CookieManager manager = null; 
		if(use_cookie) {
			manager = new CookieManager();
			manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
			CookieHandler.setDefault(manager);
		}

		URL url_ = null;
		try {
			url_ = new URL(url);
		} catch (Exception ex) {
			String str = "Невозможно преобразовать строку URL в URL-адрес.\n" + ex.getMessage()
					+ "Инциндент в процедуре: cb.dfs.trail.TrailHttp.run()";
			throw new Exception(str);
		}

		URLConnection httpConn;
		try {
			httpConn = url_.openConnection();
		} catch (Exception ex) {
			String str = "Ошибка при открытии доступа к URL-адресу <" + url + ">.\n" + ex.getMessage()
					+ "Инциндент в процедуре: cb.dfs.trail.TrailHttp.run()";
			throw new Exception(str);
		}

		// httpConn.setReadTimeout(duration*1000);

		// -- записываем ренее сохраненные свойства в url
		if (properties != null && properties.trim().length() > 0) {
			Map<String, String> request_properties = new HashMap<String, String>();
			Strings.fillNameValueMap(properties, request_properties, Constants.HTTP_PROPERTY_DELIMITER, "=");

			for (Map.Entry<String, String> entry : request_properties.entrySet()) {
				httpConn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}

		if(use_cookie) {
			try {
				// -- записываем ренее сохраненные куки в url
				if (cookies != null && cookies.trim().length() > 0) {
					Map<String, String> request_cookies = new HashMap<String, String>();
					Strings.fillNameValueMap(cookies, request_cookies, Constants.HTTP_COOKIE_DELIMITER, "=");
	
					CookieStore cookieJar = manager.getCookieStore();
					for (Map.Entry<String, String> entry : request_cookies.entrySet()) {
						HttpCookie cookie = new HttpCookie(entry.getKey(), entry.getValue());
						cookieJar.add(url_.toURI(), cookie);
					}
				}
			} catch (Exception ex) {
				String str = "Ошибка при открытии доступа к URL-адресу <" + url + ">.\n" + ex.getMessage()
						+ "Инциндент в процедуре: cb.dfs.trail.TrailHttp.run()";
				throw new Exception(str);
			}
		}

		try {
			httpConn.connect();
		} catch (Exception ex) {
			String str = "Ошибка при открытии доступа к URL-адресу <" + url + ">.\n" + ex.getMessage()
					+ "Инциндент в процедуре: cb.dfs.trail.TrailHttp.run()";
			throw new Exception(str);
		}

		// -- читаем куки
		if(use_cookie) {
			try {
				httpConn.getContent();
				cookies = "";
				CookieStore cookieJar = manager.getCookieStore();
				List<HttpCookie> l_cookies = cookieJar.getCookies();
				for (HttpCookie cookie : l_cookies) {
					if (cookies.indexOf(cookie.toString()) == -1) {
						cookies += cookie + Constants.HTTP_COOKIE_DELIMITER;
						addRetOutStr(cookie + Constants.HTTP_COOKIE_DELIMITER);
					}
				}
			} catch (Exception ex) {
				String str = "Не удается получить куки. " + ex.getMessage()
						+ "Инциндент в процедуре: cb.dfs.trail.TrailHttp.run()";
				//logger.error("-- Не удается получить куки. " + ex.getMessage());
				addRetOutStr(str);
				//throw new Exception(str);
			}
		}

		try {
			if (httpConn instanceof HttpURLConnection) {
				HttpURLConnection httpConnection = (HttpURLConnection) httpConn;
				ret_code = httpConnection.getResponseCode();
				//addRetOutStr("При обращении к " + url + " вернулся код: " + ret_code);
			} else {
				//addRetOutStr("При обращении к " + url + " нельзя получить код возврата.");
			}
		} catch (Exception ex) {
			String str = "Ошибка при открытии доступа к URL-адресу <" + url + ">.\n" + ex.getMessage()
					+ "Инциндент в процедуре: cb.dfs.trail.TrailHttp.run()";
			throw new Exception(str);
		}
		/*
		if (ret_code != -1 && ret_code >= 400 && ret_code != 401) {
			addRetErrStr("Ошибка с кодом:" + ret_code + " при обращении к URL-адресу <" + url + ">.\n"
					+ "Инциндент в процедуре: cb.dfs.trail.TrailHttp.run()\n");
			setStatusError();
			return;
		}*/
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			while (true) {
				String line = reader.readLine();
				if (line == null)
					break;
				body += line + "\n";
			}
			//System.out.println(body);
			if (is_read_page) {
				
				addRetOutStr(body.substring(0, body.length() < Constants.BODY_MAX_LENGTH_IN_HTML_READ ? body.length() : Constants.BODY_MAX_LENGTH_IN_HTML_READ ));
			}
		} catch (Exception ex) {
			String str = "Ошибка при чтении содержимого страницы <" + url + ">.\n" + ex.getMessage()
					+ "Инциндент в процедуре: cb.dfs.trail.TrailHttp.run()";
			//System.err.println("body :"+body);
			throw new Exception(str);
		}
		return ret_code;
	}
		
	protected void addRetErrStr0(String str) {
		//System.out.println("--- err ---\n"+str);
		super.addRetErrStr(str); 
	}
	
	
	protected void addRetOutStr0(String str) {
		//System.out.println("---out ---\n"+str);
		super.addRetOutStr(str); 
	}
	
	
	public void read_0() {
		int code = -1;
		//logger.debug("-- читаем страницу с выставлением куков и свойств ---");
		try {
			code = read(url, true);
		} catch (Exception ex) {
			addRetErrStr0(ex.getMessage());
			//setStatusError();
			//return;
		}
		if( code==401 || code==-1 || has_login_string()) {
			if(url_auth!=null) {
				//logger.debug("-- пытаемся аутентифицироваться и сохранить куки ---");
				try {
					code = read(url_auth, false);
				} catch (Exception ex) {
					addRetErrStr0(ex.getMessage());
					setStatusError();
					return;
				}
				if(code<400) {
					try {
						//logger.debug("-- перечитываем страницу ---"); 
						code = read(url, true);
					} catch (Exception ex) {
						addRetErrStr0(ex.getMessage());
						setStatusError();
						return;
					}
				}
			}
		}
		if(code == -1 || code > 399) {
			//logger.debug("-- code: "+ code +" body --\n"+body);
			addRetErrStr0("Код возврата:"+code+" Не удалось выполнить проверку страницы <" + url + ">.\n");
			setStatusError();
			return;
		}
		if(has_bad_string()) {
			//System.out.println("-- code: "+ code +" body --\n"+body);
			addRetErrStr0("Страница содержит ошибочные строки. Не удалось выполнить проверку страницы <" + url + ">.\n");
			setStatusError();
			return;
		}
		
		//System.out.println("--- body ---\n" + body);
		if (is_read_page) {
			addRetOutStr0(body);
		}
		setStatusSuccess();
	}


	@Override
	public void run() {
		read_0();
	}
	
	/*
	//@Override
	public void run_0() {
		final String AUTH_PROXY = proxy_host; // "http-proxy.srv.rdtex.ru";
		final String AUTH_PROXY_PORT = proxy_port; // "3128";
		final String AUTH_USER = proxy_user; // "roman_eremeev";
		final String AUTH_PASSWORD = proxy_auth; // "";

		if (proxy_host != null && proxy_host != "") {
			System.setProperty("java.net.useSystemProxies", "true");
			System.setProperty("http.proxyHost", AUTH_PROXY);
			System.setProperty("http.proxyPort", AUTH_PROXY_PORT);
			if (AUTH_USER != null && AUTH_PASSWORD != null) {
				System.setProperty("http.proxyUser", AUTH_USER);
				System.setProperty("http.proxyPassword", AUTH_PASSWORD);
			}

			if (AUTH_USER != null && AUTH_PASSWORD != null) {
				Authenticator.setDefault(new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(AUTH_USER, AUTH_PASSWORD.toCharArray());
					}
				});
			}
		}

		// instantiate CookieManager; make sure to set CookiePolicy
		CookieManager manager = new CookieManager();
		manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		CookieHandler.setDefault(manager);

		URL url_ = null;
		try {
			url_ = new URL(url);
		} catch (Exception ex) {
			addRetErrStr("Невозможно преобразовать строку URL в URL-адрес.\n" + ex.getMessage()
					+ "Инциндент в процедуре: cb.dfs.trail.TrailHttp.run()");
			setStatusError();
			return;
		}

		URLConnection httpConn;
		try {
			httpConn = url_.openConnection();
		} catch (Exception ex) {
			addRetErrStr("Ошибка при открытии доступа к URL-адресу <" + url + ">.\n" + ex.getMessage()
					+ "Инциндент в процедуре: cb.dfs.trail.TrailHttp.run()");
			setStatusError();
			System.out.println(" -- status error --");
			return;
		}

		// httpConn.setReadTimeout(duration*1000);

		// -- записываем ренее сохраненные свойства в url
		if (properties != null && properties.trim().length() > 0) {
			Map<String, String> request_properties = new HashMap<String, String>();
			Strings.fillNameValueMap(properties, request_properties, Constants.HTTP_PROPERTY_DELIMITER, "=");

			for (Map.Entry<String, String> entry : request_properties.entrySet()) {
				httpConn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}

		try {
			// -- записываем ренее сохраненные куки в url
			if (cookies != null && cookies.trim().length() > 0) {
				Map<String, String> request_cookies = new HashMap<String, String>();
				Strings.fillNameValueMap(cookies, request_cookies, Constants.HTTP_COOKIE_DELIMITER, "=");

				CookieStore cookieJar = manager.getCookieStore();
				for (Map.Entry<String, String> entry : request_cookies.entrySet()) {
					HttpCookie cookie = new HttpCookie(entry.getKey(), entry.getValue());
					cookieJar.add(url_.toURI(), cookie);
					System.out.println(entry.getKey() + " = " + entry.getValue());
				}
			}
		} catch (Exception ex) {
			addRetErrStr("Ошибка при открытии доступа к URL-адресу <" + url + ">.\n" + ex.getMessage()
					+ "Инциндент в процедуре: cb.dfs.trail.TrailHttp.run()");
			setStatusError();
			System.out.println(" -- status error --");
			return;
		}

		try {
			httpConn.connect();
		} catch (Exception ex) {
			addRetErrStr("Ошибка при открытии доступа к URL-адресу <" + url + ">.\n" + ex.getMessage()
					+ "Инциндент в процедуре: cb.dfs.trail.TrailHttp.run()");
			setStatusError();
			//System.out.println(" -- status error --");
			return;
		}

		// -- читаем куки
		try {
			httpConn.getContent();
			cookies = "";
			CookieStore cookieJar = manager.getCookieStore();
			List<HttpCookie> l_cookies = cookieJar.getCookies();
			for (HttpCookie cookie : l_cookies) {
				if (cookies.indexOf(cookie.toString()) == -1) {
					cookies += cookie + Constants.HTTP_COOKIE_DELIMITER;
					addRetOutStr(cookie + Constants.HTTP_COOKIE_DELIMITER);
				}
			}
		} catch (Exception ex) {
			System.out.println("Unable to get cookie using CookieHandler");
			addRetErrStr("Не удается получить куки. " + ex.getMessage()
					+ "Инциндент в процедуре: cb.dfs.trail.TrailHttp.run()");
			//setStatusError();
			//return;
		}

		int ret_code = -1; // -- -1 значит нельзя установить код возврата
		try {
			if (httpConn instanceof HttpURLConnection) {
				HttpURLConnection httpConnection = (HttpURLConnection) httpConn;
				ret_code = httpConnection.getResponseCode();
				addRetOutStr("При обращении к " + url + " вернулся код: " + ret_code);
			} else {
				addRetOutStr("При обращении к " + url + " нельзя получить код возврата.");
			}
		} catch (Exception ex) {
			addRetErrStr("Ошибка при открытии доступа к URL-адресу <" + url + ">.\n" + ex.getMessage()
					+ "Инциндент в процедуре: cb.dfs.trail.TrailHttp.run()");
			setStatusError();
			return;
		}
		if (ret_code != -1 && ret_code >= 400 && ret_code != 401) {
			addRetErrStr("Ошибка с кодом:" + ret_code + " при обращении к URL-адресу <" + url + ">.\n"
					+ "Инциндент в процедуре: cb.dfs.trail.TrailHttp.run()\n");
			setStatusError();
			return;
		}

		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			String body = "";
			while (true) {
				String line = reader.readLine();
				if (line == null)
					break;
				body += line + "\n";
			}
			//System.out.println(body);
			if (is_read_page) {
				addRetOutStr(body);
			}
			if (bad_strings != null && bad_strings.trim().length() > 0) {
				List<String> bsm = new ArrayList<String>();
				Strings.fillValueList(bad_strings, bsm, Constants.HTTP_BADSTRINGS_DELIMITER);
				for (String val : bsm) {
					System.out.println(" bad string val = " + val);
					if (body.indexOf(val) != -1) {
						addRetErrStr("В ответе сервера найдена строка ошибки:" + val);
						setStatusError();
						return;
					}
				}
			}
		} catch (Exception ex) {
			addRetErrStr("Ошибка при открытии доступа к URL-адресу <" + url + ">.\n" + ex.getMessage()
					+ "Инциндент в процедуре: cb.dfs.trail.TrailHttp.run()");
			setStatusError();
			return;
		}
		setStatusSuccess();
		return;
	}
	*/

	public String getBody() {
		return body;
	}
	
	public String getCookies() {
		return cookies;
	}
	

	public String getBad_strings() {
		return bad_strings;
	}
	

	public String getLogin_strings() {
		return login_strings;
	}
	

	// Your browser is not supported by Oracle BI Presentation Services.
	// Enter your user id and password.
	public static void main0(String[] args) {
		// URL url = new URL("http://softlib-p.srv.rdtex.ru/");
		// URL url = new URL("http://yandex.ru/yandsearch?text=*********");
		// Map<String, String> req_props = new HashMap<String, String>();
		// req_props.put("Cookie",
		// "User-Agent=Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0)
		// Gecko/20100101 Firefox/26.0; name2=value2");
		// TrailManager trail = new TrailManager();
		// String urlString =
		// "http://vm-ocdm-demo.srv.rdtex.ru:9704/analytics/saw.dll?Dashboard&PortalPath=%2Fshared%2FOCDM%203.2-Dashboards%2FProduct%20Management%2FProduct%20Performance&page=Product%20Performance";
		// String urlString =
		// "http://vm-ocdm-demo.srv.rdtex.ru:9704/analytics/saw.dll?Go&Action=Download&PortalPath=%2Fshared%2FOCDM%203.2-Dashboards%2FProduct%20Management%2FProduct%20Performance&page=Product%20Performance";
		// /shared/OCDM/ODWT Mining/ODWT Mining/Customer Acquisiti

		// String urlString =
		// "http://vm-ocdm-demo.srv.rdtex.ru:9704/analytics/saw.dll?Go&Action=Download&path=%2Fshared%2FOCDM%203.2%2FRevenue%2FCustomer%20Contract%2FTerminated%20Contract%20Analysis";
		// String urlString =
		// "http://vm-ocdm-demo.srv.rdtex.ru:9704/analytics/saw.dll?Dashboard&PortalPath=%2Fshared%2FOCDM%203.2%2FDemo%20%2FCustomer%20Churn%20Analysis&page=Building%20Network%20InfrastrucXXXture";
		// Enter your user id and password.

		// String urlString =
		// "http://vm-ocdm-demo.srv.rdtex.ru:9704/analytics/saw.dll?Go&NQUser=ocdm&NQPassword=ocdm1234&Action=Download&path=%2Fshared%2FOCDM%203.2%2FRevenue%2FCustomer%20Contract%2FTerminated%20Contract%20Analysis";
		String req_props = "User-Agent=Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0; name2=value2";
		// TrailHttp trail = new TrailHttp("AGENT0", "TEST_HTTP", "Внутренний
		// тест", urlString, null
		// , req_props, null, true, "0", "0", "0");

		// "http://vm-ocdm-demo.srv.rdtex.ru:9704/analytics/saw.dll?Go&NQUser=ocdm&NQPassword=ocdm1234&Action=open&itemType=.xdo&bipPath=%2F~ocdm%2FDrafts%2FDraft%20-%2030.11.2016%2013-02-32.xdo&path=%2Fusers%2Focdm%2FDrafts%2FDraft%20-%2030.11.2016%2013-02-32.xdo";
		// "http://vm-ocdm-demo.srv.rdtex.ru:9704/analytics/saw.dll?bipublisherEntry&Action=open&itemType=.xdo&bipPath=%2F~ocdm%2FDrafts%2FDraft%20-%2030.11.2016%2013-02-32.xdo&path=%2Fusers%2Focdm%2FDrafts%2FDraft%20-%2030.11.2016%2013-02-32.xdo";
		// String urlString =
		// "http://vm-ocdm-demo.srv.rdtex.ru:9704/analytics/saw.dll?Go&NQUser=ocdm&NQPassword=ocdm1234&Action=Download&itemType=.xdo&bipPath=%2F~ocdm%2FDrafts%2Ftest.xdo&path=%2Fusers%2Focdm%2FDrafts%2Ftest.xdo";
		String urlString = "http://vm-ocdm-demo.srv.rdtex.ru:9704/analytics/saw.dll?Go&NQUser=ocdm&NQPassword=ocdm1234&path=%2Fshared%2FOCDM%203.2%2FRevenue%2FCustomer%20Contract%2FTerminated%20Contract%20Analysis";
		String url_authString = null;
		// String urlString =
		// "http://www.proza.ru/cgi-bin/login/intro.pl?&login=bestelesny&password=uiaait_5";
		// String urlString = "http://www.proza.ru";
		String bad_str = "Your browser is not supported by Oracle BI Presentation Services.";

		// ver=www.proza.ru;pcode=6260948426;login=bestelesny;
		// String urlString = "http://www.proza.ru/cgi-bin/login/intro.pl";
		// String bad_str = "<input type=\"password\"";
		String log_str = "<input type=\"password\"";
		// String url_authString =
		// "http://www.proza.ru/cgi-bin/login/intro.pl?&login=danmastest&password=2241401281";
		// proza danmastest 2241401281

		// String urlString =
		// "http://vm-ocdm-demo.srv.rdtex.ru:9704/analytics/saw.dll?Dashboard&PortalPath=%2Fshared%2FOCDM%203.2%2FDemo%20%2FCustomer%20Churn%20Analysis&page=Building%20Network%20InfrastrucXXXture";

		try {
			TrailManagerSubro trailSubroManager = null;
			trailSubroManager = new TrailManagerSubro(Constants.test_url, Constants.test_user, Constants.test_pwd);

			// TrailHttp trail = new TrailHttp("RDAgentVM3", "TEST_HTTP
			// www.proza.ru", "Внутренний тест", urlString, null
			// , "http-proxy.srv.rdtex.ru", "3128", "roman_eremeev", "xxxxxxxx"
			// , null, null, null, true, "60", "100", "0");
			TrailHttp trail = new TrailHttp("erv_local", "+", "Внутренний тест", urlString, url_authString, null, null,
					null, null, req_props, null, bad_str, log_str, true, "60", "100", "0");

			// TrailHttp trail = new TrailHttp("erv_local", "++ TEST_HTTP
			// www.proza.ru", "Внутренний тест"
			// , urlString, url_authString
			// , "192.168.11.2", "3128", req_props, null, null, null, bad_str,
			// log_str, true, "60", "100", "0");

			trail.overwrite(trailSubroManager.getConnection());
			trailSubroManager.getConnection().commit();

			trailSubroManager.launch_trail_if_ready((TrailBase) trail, "scenario_name", (int) (Math.random() * 1000.));

			trail.overwrite(trailSubroManager.getConnection());
			trailSubroManager.getConnection().commit();

			System.out.println(trail.to_string() + " cookies: " + trail.cookies);
			System.out.println(trail.getBody());
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}

	public static int do_trail_noproxy_with_reqproperties() {

		String s;
		try {
			URL myURL = // new URL("http://server:port/analytics/saw.dll?Dashboard&PortalPath=%2Fshared%2FHE%20Analytics%2F_portal%2FHE%20Analytics&NQUser=weblogic&NQPassword=welcome1");
					//new URL("http://vm-ocdm-demo.srv.rdtex.ru:9704/analytics/saw.dll?Go&Action=Download&NQUser=ocdm&NQPassword=ocdm1234&path=%2Fshared%2FOCDM%203.2%2FRevenue%2FCustomer%20Contract%2FTerminated%20Contract%20Analysis"
					//-- ответ формой ввода пароля
					//new URL("http://ocdm:ocdm1234@vm-ocdm-demo.srv.rdtex.ru:9704/analytics/saw.dll?Go&Path=%2Fshared%2FOCDM%203.2%2FRevenue%2FCustomer%20Contract%2FTerminated%20Contract%20Analysis"
					//new URL("http://vm-ocdm-demo.srv.rdtex.ru:9704/analytics/saw.dll?Go&Path=%2Fshared%2FOCDM%203.2%2FRevenue%2FCustomer%20Contract%2FTerminated%20Contract%20Analysis&NQUser=ocdm&NQPassword=ocdm1234"
					//-- get SOAP
					new URL("http://vm-ocdm-demo.srv.rdtex.ru:9704/analytics/saw.dll/wsdl/v6"
			);
			URLConnection myURLConnection = myURL.openConnection();
			myURLConnection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)");
			myURLConnection.setReadTimeout(1000);
			myURLConnection.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				System.out.println(inputLine);
			//in.close();

			try{
			    Thread.sleep(1500);
			}catch(InterruptedException e){
			    System.out.println("got interrupted!");
			}
			
			//myURLConnection.connect();
			//BufferedReader in2 = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
			//String inputLine;
			while ((inputLine = in.readLine()) != null)
				System.out.println("-2-"+ inputLine);
			in.close();


		} catch (MalformedURLException e) {
			System.out.println("Exception " + e);
		} catch (IOException e) {
			System.out.println("Exception " + e);
		}
		return 0;

		/*
		URL url;
		try {
			url = new URL(urlString);
		} catch (Exception ex) {
			System.err.println("Невозможно преобразовать строку URL в URL-адрес.\n" + ex.getMessage()
					+ "Инциндент в процедуре: cb.dfs.trail.TrailHttp.run()");
			return -1;
		}
		try {
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setReadTimeout(readTimeout); // Set<String>
			keys = request_properties.keySet(); // for(String key : keys) { //
			httpConn.addRequestProperty(key, request_properties.get(key)); // //
			System.out.println(" === " + key + " " + request_properties.get(key)); // }
			for (Map.Entry<String, String> entry : request_properties.entrySet()) {
				httpConn.addRequestProperty(entry.getKey(), entry.getValue());
				System.out.println(entry.getKey() + " = " + entry.getValue());
			}

			// httpConn.addRequestProperty("User-Agent",
			// "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101
			// Firefox/26.0"
			// ); //httpConn.addRequestProperty("User-Agent",
			// "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML,
			// like Gecko) Chrome/47.0.2526.80 SputnikBrowser/2.1.947.0
			// Safari/537.36"
			// );

			try {
				int ret_code = httpConn.getResponseCode(); // System.out.println("--
															// DEBUG -- При
															// обращение к "
															// +urlString+"
															// вернулся код: " +
															// ret_code);
				trail.addRetOutStr("При обращении к " + urlString + " вернулся код: " + ret_code);
				if (is_read_page) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(url.openConnection().getInputStream(), "UTF-8"));
					while (true) {
						String line = reader.readLine();
						if (line == null)
							break;
						trail.addRetOutStr(line + "\n");
					}
				}

			} catch (java.net.SocketTimeoutException ex) {
				System.out.println(

				"-- DEBUG -- TIMEOUT");
				System.err.println(("Превышено время ожидания ответа при обращении к URL-адресу <" + urlString + ">.\n"
						+ ex.getMessage() + "Инциндент в процедуре: cb.dfs.trail.TrailHttp.run()\n");
				return -1;
			}
			int code = httpConn.getResponseCode();
			if (code >= 400 && code != 401) {
				System.err.println(("Ошибка с кодом:" + code + " при обращении к URL-адресу <" + urlString + ">.\n"
						+ "Инциндент в процедуре: cb.dfs.trail.TrailHttp.run()\n");
				return -1;
			}
			return 0;
		} catch (Exception ex) {
			System.err.println(("Ошибка при открытии доступа к URL-адресу <" + urlString + ">.\n" + ex.getMessage()
					+ "Инциндент в процедуре: cb.dfs.trail.TrailHttp.run()");
			return -1;
		}
		*/
	}

	public static void main(String[] args) {

		// int ret = check_url(trail, "http://www.gazeta.ru/", 1000, //
		// "http-proxy.srv.rdtex.ru", "3128", "roman_eremeev", "roman_mx5");

		int ret = do_trail_noproxy_with_reqproperties();
		System.out.println(" -- ret " + ret);

	}

	/*
	 * public static int check_url(Trail trail, String urlString, int
	 * readTimeout , String proxy_host, String proxY_port , String proxy_user,
	 * String proxy_password) throws Exception {
	 * 
	 * final String AUTH_PROXY = proxy_host; //"http-proxy.srv.rdtex.ru"; final
	 * String AUTH_PROXY_PORT = proxY_port; //"3128"; final String AUTH_USER =
	 * proxy_user; //"roman_eremeev"; final String AUTH_PASSWORD =
	 * proxy_password; //"";
	 * 
	 * if(proxy_host!=null && proxy_host!="") {
	 * System.setProperty("java.net.useSystemProxies", "true");
	 * System.setProperty("http.proxyHost", AUTH_PROXY);
	 * System.setProperty("http.proxyPort", AUTH_PROXY_PORT);
	 * System.setProperty("http.proxyUser", AUTH_USER);
	 * System.setProperty("http.proxyPassword", AUTH_PASSWORD);
	 * 
	 * Authenticator.setDefault( new Authenticator() { public
	 * PasswordAuthentication getPasswordAuthentication() { return new
	 * PasswordAuthentication(AUTH_USER, AUTH_PASSWORD.toCharArray()); } } ); }
	 * URL url = new URL(urlString);
	 * 
	 * HttpURLConnection httpConn =(HttpURLConnection)url.openConnection();
	 * httpConn.setReadTimeout(readTimeout);
	 * 
	 * InputStream is; try { int ret_code = httpConn.getResponseCode();
	 * System.out.println("ОБращение к URL вернуло код: "+ret_code); }
	 * catch(java.net.SocketTimeoutException ex) { trail.addRetErrStr(" \n");
	 * System.out.println("TIMEOUT"); }
	 * 
	 * if (httpConn.getResponseCode() >= 400) { is = httpConn.getErrorStream();
	 * } else { is = httpConn.getInputStream(); } BufferedReader reader = new
	 * BufferedReader( new InputStreamReader(is, "UTF-8")); while (true) {
	 * String line = reader.readLine(); if (line == null) break; //--
	 * System.out.println(line); } return 0; }
	 */
	/**/
	/*
	 * public static String post(String urlSpec, String data) throws Exception {
	 * URL url = new URL(urlSpec);
	 * 
	 * URLConnection connection = url.openConnection();
	 * 
	 * HttpURLConnection httpConn = (HttpURLConnection) connection; InputStream
	 * is; if (httpConn.getResponseCode() >= 400) { is =
	 * httpConn.getErrorStream(); } else { is = httpConn.getInputStream(); }
	 * 
	 * connection.setDoOutput(true); OutputStreamWriter writer = new
	 * OutputStreamWriter( connection.getOutputStream()); writer.write(data);
	 * writer.flush();
	 * 
	 * BufferedReader reader = new BufferedReader(new InputStreamReader(
	 * connection.getInputStream())); String line = ""; StringBuilder builder =
	 * new StringBuilder(); while ((line = reader.readLine()) != null) {
	 * builder.append(line); } return builder.toString(); }
	 */
}