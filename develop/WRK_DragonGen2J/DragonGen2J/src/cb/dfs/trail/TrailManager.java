package cb.dfs.trail;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//import org.apache.log4j.Logger;

import cb.dfs.trail.common.Constants;
import cb.dfs.trail.common.Version;
import cb.dfs.trail.db.OraConnector;
import cb.dfs.trail.db.PreparedStatementHelper;

@Version(major = Constants.CUR_VER_MAJOR, minor = Constants.CUR_VER_MINOR, revision = Constants.CUR_VER_REVIS)
public class TrailManager extends TrailRunner {

	
	//final static Logger logger = //logger.getLogger(TrailManager.class);
	
	protected String getDebugInfo() {
		return this.getClass().getName()+":"+Thread.currentThread().getStackTrace()[2].getMethodName()+":"+
				Thread.currentThread().getStackTrace()[2].getLineNumber();
	}
	
	
	protected OraConnector connector = null;;
	Connection external_connection = null;
	
	public TrailManager() {
		super();
	}

	
	public TrailManager(Connection _external_connection) {
		super();
		external_connection = _external_connection;
	    ////logger.debug("-- TrailManager with connection "+_external_connection+" was opened.");
	    //trail.debug(getDebugInfo()+"-- TrailManager with connection "+_external_connection+" was opened.");
	}
	
	
	public TrailManager(String url, String user, String password)
			throws Exception {
		super();
		connector = OraConnector.getOraConnector(url, user, password);
	}

	public void do_trail(TrailBase trail) throws Exception {
		////logger.debug("-- do_trail 1 -- ");
		trail.debug(getDebugInfo()+"--  do_trail 1 -- ");
		Date dt = getSydateFromMaster(trail);
		////logger.debug("-- do_trail 2 -- ");
		trail.debug(getDebugInfo()+"--  do_trail 2 -- ");
		trail.setLastStartDate(dt);
		////logger.debug("-- do_trail 3 -- ");
		trail.debug(getDebugInfo()+"--  do_trail 3 -- ");
		super.do_trail(trail);
		////logger.debug("-- do_trail 4 -- ");
		trail.debug(getDebugInfo()+"--  do_trail 4 -- ");
		trail.overwrite(getConnection());
		////logger.debug("-- do_trail 5 -- ");
		trail.debug(getDebugInfo()+"--  do_trail 5 -- ");
	}
	
	
	public Connection getConnection() throws Exception {
		if(external_connection!=null)
			return external_connection;
		return connector.getConnection();
	}

	
	public void setParam(String trail_key, String param, String value) throws Exception {
		TrailBase trail = read(trail_key);
		trail.setParam(param, value); 
	}
	
	
	public Date getSydateFromMaster(TrailBase trail) throws Exception {
		Date dt = null;
		PreparedStatementHelper stmt = null;
		PreparedStatement prepStmt = null;
		try {
			////logger.debug("-- getSydateFromMaster() 1 -- ");
			trail.debug(getDebugInfo()+"-- getSydateFromMaster() 1 -- ");
			String sql_str = "select sysdate from dual";
			prepStmt = getConnection().prepareStatement(sql_str);
			stmt = new PreparedStatementHelper(prepStmt);
			////logger.debug("-- getSydateFromMaster() 2 -- ");
			trail.debug(getDebugInfo()+"-- getSydateFromMaster() 2 -- ");
			ResultSet rs = stmt.executeQuery();
			////logger.debug("-- getSydateFromMaster() 3 -- ");
			trail.debug(getDebugInfo()+"-- getSydateFromMaster() 3 -- ");

			while (rs.next()) {
				dt = rs.getDate(1); 
			}
			if(dt==null) {
				//logger.error("Ошибка! В getSydateFromMaster(): не вернулась дата.");
				trail.error(getDebugInfo()+"-- Ошибка! В getSydateFromMaster(): не вернулась дата.");
				return null;
			}	
			////logger.debug("-- getSydateFromMaster() 4 -- ");
			trail.debug(getDebugInfo()+"-- getSydateFromMaster() 4 -- ");
			rs.close();
			stmt.close();
			return dt;
		} catch (SQLException se) {
			String str = "Ошибка " + se.getMessage() + "\nSQL исключение в TrailManager.getSydateFromMaster()";
			throw new Exception(str);
		} catch (Exception e) {
			String str = "Ошибка " + e.getMessage() + "\nИсключение в TrailManager.getSydateFromMaster()";
			throw new Exception(str);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
		}
	}
	
	/*
	 * Читает из базы и создает ОП по ключу
	 */
	public TrailBase read(String trail_key) throws Exception {
		String trail_type = readTrailType(trail_key);
		TrailBase trail = null;
		switch(trail_type) {
		case Constants.TT_OS_SCRIPT:
			trail = new_trail_os_script(trail_key);
			trail.read(getConnection());
			return trail;
		case Constants.TT_TCP_PORT:
			trail = new_trail_tcp_port(trail_key);
			trail.read(getConnection());
			return trail;
		case Constants.TT_HTTP:
			trail = new_trail_http(trail_key);
			trail.read(getConnection());
			return trail;
		case Constants.TT_SSH_KEY:
			trail = new_trail_ssh_key(trail_key);
			trail.read(getConnection());
			return trail;
		case Constants.TT_SSH_PWD:
			trail = new_trail_ssh_pwd(trail_key);
			trail.read(getConnection());
			return trail;
		case Constants.TT_SQL:
			trail = new_trail_sql(trail_key);
			trail.read(getConnection());
			return trail;
		default :	
			throw new Exception("Неизвестный тип ОП <" + trail_type + ">.");
		}
	}

	/*
	 * Записывает ОП в БД
	 */
	public void write(TrailBase trail) throws Exception {
		trail.overwrite(getConnection());
	}

	

	/*
	 * Удаление ОП из БД по ключу
	 */
	public void delete(String trail_key) throws Exception {
		////logger.debug("-- delete("+trail_key+").");
		//trail.debug(getDebugInfo()+"-- 
		Connection conn = getConnection();
		
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			//-- тупо по всем вьюхам
			stmt.executeUpdate("delete from v_trails_os_script where trail_key='"+trail_key+"'");
			stmt.executeUpdate("delete from v_trails_ssh_key where trail_key='"+trail_key+"'");
			stmt.executeUpdate("delete from v_trails_http where trail_key='"+trail_key+"'");
			stmt.executeUpdate("delete from v_trails_tcp_port where trail_key='"+trail_key+"'");
			stmt.executeUpdate("delete from v_trails_sql where trail_key='"+trail_key+"'");
			stmt.executeUpdate("delete from v_trails_ssh_pwd where trail_key='"+trail_key+"'");
			stmt.close();

		} catch (SQLException se) {
			String str = "Ошибка " + se.getMessage()
					+ "\nSQL исключение в TrailManager.delete()";
			//logger.error(str);
			//trail.error(getDebugInfo()+"-- 
			throw new Exception(str);
		} catch (Exception e) {
			String str = "Ошибка " + e.getMessage()
					+ "\nИсключение в TrailManager.delete()";
			//logger.error(str);
			//trail.error(getDebugInfo()+"--
			throw new Exception(str);
		} finally { try { if (stmt != null) stmt.close(); } catch (SQLException se2) {}
		}

	}
	
	/*
	 * Удаление всех ОП из БД
	 */
	public void deleteAll() throws Exception {
		Connection conn = connector.getConnection();

		Statement stmt = null;
		try {
			stmt = conn.createStatement();

			stmt.executeUpdate("delete from v_trails_os_script");
			stmt.executeUpdate("delete from v_trails_ssh_key");
			stmt.executeUpdate("delete from v_trails_ssh_pwd ");
			stmt.executeUpdate("delete from v_trails_tcp_port ");
			stmt.executeUpdate("delete from v_trails_sql ");
			stmt.executeUpdate("delete from v_trails_http ");
			stmt.close();

		} catch (SQLException se) {
			String str = "Ошибка " + se.getMessage()
					+ "\nSQL исключение в TrailManager.deleteAll()";
			throw new Exception(str);
		} catch (Exception e) {
			String str = "Ошибка " + e.getMessage()
					+ "\nИсключение в TrailManager.deleteAll()";
			throw new Exception(str);
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}// nothing we can do
		}// end try

	}

	/*
	 * Читает из базы и создает ОП по ключу
	 */
	protected String readTrailType(String trail_key) throws Exception {
		String trail_type = null;
		PreparedStatement stmt = null;
		Connection conn = getConnection();
		try {
			stmt = conn.prepareStatement("select trail_type from d_trails"
					+ " where trail_key='"+trail_key+"'");
			ResultSet rs = stmt.executeQuery();
			int cnt = 0;
			while (rs.next()) {
				cnt++;
				trail_type = rs.getString("trail_type");
			}
			rs.close();
			stmt.close();
			if (cnt == 0) {
				String str = "Не найдена запись для операции проверки <"
						+ trail_key + ">.";
				//logger.error(str);
				//trail.error(getDebugInfo()+"--
				throw new Exception(str);
			}
			if (cnt > 1) {
				String str = "Должна быть одна запись для операции проверки <"
						+ trail_key + ">, а их "+cnt+".";
				//logger.error(str);
				//trail.error(getDebugInfo()+"--
				throw new Exception(str);
			}
		} catch (SQLException se) {
			String str = "Ошибка " + se.getMessage()
					+ "\nSQL исключение в TrailManager.readTrailType()";
			//logger.error(str);
			//trail.error(getDebugInfo()+"--
			throw new Exception(str);
		} catch (Exception e) {
			String str = "Ошибка " + e.getMessage()
					+ "\nИсключение в TrailManager.readTrailType()";
			//logger.error(str);
			//trail.error(getDebugInfo()+"--
			throw new Exception(str);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
		}
		return trail_type;
	}


//	public void open_dialogs() {
//		ObservableObjectsDialog ood = new ObservableObjectsDialog();
//		ood.setConnectParams(connector.getUrl(), connector.getUser(), connector.getPassword());
//		ood.show();
//		
//		REM_TrailsDialog td = new REM_TrailsDialog();
//		td.setConnectParams(connector.getUrl(), connector.getUser(), connector.getPassword());
//		td.show();
//	}
//	
//	public static void open_dialogs(String url, String user, String password) {
//		ObservableObjectsDialog ood = new ObservableObjectsDialog();
//		ood.setConnectParams(url, user, password);
//		ood.show();
//		
//		REM_TrailsDialog td = new REM_TrailsDialog();
//		td.setConnectParams(url, user, password);
//		td.show();
//	}
	
	/*
	 * public int do_taril_http(String urlString, String max_duration_in_sec) {
	 * int duration; try { duration = Integer.valueOf(max_duration_in_sec); if
	 * (duration < 0) { addRetErrStr("\n" + "ERROR: " +
	 * "Время выполнения не может быть отрицательным <" + max_duration_in_sec +
	 * ">."); return -1; } } catch (Exception ex) { addRetErrStr("\n" +
	 * "ERROR: " + "Неверно задано время выполнения в секундах <" +
	 * max_duration_in_sec + ">."); return -1; } return TrailHttp.do_trail(this,
	 * urlString, duration, null, true); }
	 * 
	 * public int do_taril_http(String urlString, String max_duration_in_sec,
	 * Map<String, String> request_properties, boolean is_read_page) { int
	 * duration; try { duration = Integer.valueOf(max_duration_in_sec); if
	 * (duration < 0) { addRetErrStr("\n" + "ERROR: " +
	 * "Время выполнения не может быть отрицательным <" + max_duration_in_sec +
	 * ">."); return -1; } } catch (Exception ex) { addRetErrStr("\n" +
	 * "ERROR: " + "Неверно задано время выполнения в секундах <" +
	 * max_duration_in_sec + ">."); return -1; } return TrailHttp.do_trail(this,
	 * urlString, duration, request_properties, is_read_page); }
	 * 
	 * public int do_taril_http(String urlString, String max_duration_in_sec,
	 * Map<String, String> request_properties, boolean is_read_page, String
	 * proxy_host, String proxY_port, String proxy_user, String proxy_password)
	 * { // System.out.println("-- DEBUG -- check_url("+urlString+")"); int
	 * duration; try { duration = Integer.valueOf(max_duration_in_sec); if
	 * (duration < 0) { addRetErrStr("\n" + "ERROR: " +
	 * "Время выполнения не может быть отрицательным <" + max_duration_in_sec +
	 * ">."); return -1; } } catch (Exception ex) { addRetErrStr("\n" +
	 * "ERROR: " + "Неверно задано время выполнения в секундах <" +
	 * max_duration_in_sec + ">."); return -1; } try { return
	 * TrailHttp.do_trail(this, urlString, proxy_host, proxY_port, proxy_user,
	 * proxy_password, duration, request_properties, is_read_page); } catch
	 * (Exception ex) { this.addRetErrStr(
	 * "Ошибка во время выполнения проверки доступности гиперссылок по протоколу HTTP.\n"
	 * + ex.getMessage()); return -1; } }
	 * 
	 * public int do_trail_tcp_port(String server, String port) {
	 * System.out.println("-- DEBUG -- exec_telnet(" + server + ":" + port +
	 * ")"); try { TrailTcpPort.do_trail(server, port);
	 * this.addRetOutStr("Проверка TCP соединения на " +
	 * "выбранный сетевой порт <" + server + ":" + port +
	 * "> выполнена успешно. "); } catch (Exception ex) {
	 * this.addRetErrStr("Ошибка во время выполнения проверки TCP соединения " +
	 * "на выбранный сетевой порт <" + server + ":" + port + ">. " +
	 * ex.getMessage()); return -1; } return 0; }
	 */

	public static void main(String[] args) {

		try {
			TrailManager tm = new TrailManager(Constants.test_url,Constants.test_user,Constants.test_pwd);
			System.out.println(tm.getVersion());
			// trail.do_test();
	
			String trail_key = "(G) Проверка SQL. время отклика 5 сек";
			TrailBase trail = tm.read(trail_key);
			trail.setParam("run_agent", "RDAgent");
			trail.setParam("launch_period_in_sec", "10");
			tm.write(trail);
			tm.getConnection().commit();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		
		// +
		// "cmd /c \"copy C:\\RDTEX\\CB\\CB-CPO-BI\\work\\installer\\current_ver\\mon\\libs\\cb_dfs_trail.jar D:\\OdiHome\\user_projects\\domains\\base_domain\\lib\\cb_dfs_trail.jar\"";
		// String script = ""
		// + "ping /n 3 192.168.30.46";

		// int ret = trail.do_trail("TT_OS_SCRIPT", null, null, null, null,
		// script, "0");
		// if(ret==0) {
		// System.out.println("-- DEBUG--  SUCCESS. "+ trail.to_string());
		// } else {
		// System.err.println("-- DEBUG--  ERROR! exit-status: "+ ret + "" +
		// trail.to_string());
		// }/**/

		// trail.exec_os_script(args[0]);
		// System.out.println("-- DEBUG --+ trail.to_string():"+trail.to_string());
	}

	public int do_test() {
		String host, user, password, command;
		String port;
		// System.out.println(" -- DEBUG -- do_test() ---");

		// host = "192.168.40.135";
		// user = "test";
		// password = "test";
		// command = "cd /home/owner/test\n pwd\n ls -la\n ./1.sh\n";
		/*
		 * host = "vm-cb2-bi.vm-p.rdtex.ru"; user = "oracle"; password =
		 * "or_dbms"; //command = "cd /home/oracle/test\n ./2.sh\n pwd\n ls\n";
		 * command = "ping -c 3 XXXXXXXXXXX"; port = "22";
		 * 
		 * int ret = do_trail("TT_SSH_PWD", host, port, user, password, command,
		 * "0"); if(ret==0) { System.out.println("-- DEBUG--  SUCCESS. "+
		 * to_string()); } else {
		 * System.err.println("-- DEBUG--  ERROR! exit-status: "+ ret + "" +
		 * to_string()); }
		 */

		/**/
		// String script = ""
		// +
		// "cmd /c set LIBS_OUT_PATH=C:\\RDTEX\\CB\\CB-CPO-BI\\work\\installer\\current_ver\\mon\\libs"
		// +
		// "cmd /c set AGENTS_LIB=D:\\OdiHome\\user_projects\\domains\\base_domain\\lib"
		// +
		// "cmd /c copy %LIBS_OUT_PATH%\\cb_dfs_trail.jar %AGENTS_LIB%\\cb_dfs_trail.jar";

		// String script = ""
		// +
		// "set LIBS_OUT_PATH=C:\\RDTEX\\CB\\CB-CPO-BI\\work\\installer\\current_ver\\mon\\libs\n"
		// +
		// "set AGENTS_LIB=D:\\OdiHome\\user_projects\\domains\\base_domain\\lib\n"
		// +
		// "copy %LIBS_OUT_PATH%\\cb_dfs_trail.jar %AGENTS_LIB%\\cb_dfs_trail.jar\n";

		String script = ""
				+ "cmd /c \"copy C:\\RDTEX\\CB\\CB-CPO-BI\\work\\installer\\current_ver\\mon\\libs\\cb_dfs_trail.jar D:\\OdiHome\\user_projects\\domains\\base_domain\\lib\\cb_dfs_trail.jar\"";
		// String script = ""
		// + "ping /n 3 192.168.30.46";

		// int ret = do_trail("TT_OS_SCRIPT", null, null, null, null, script,
		// "0");
		// if(ret==0) {
		// System.out.println("-- DEBUG--  SUCCESS. "+ to_string());
		// } else {
		// System.err.println("-- DEBUG--  ERROR! exit-status: "+ ret + "" +
		// to_string());
		// }/**/

		/*
		 * int ret = do_trail("OS_SCRIPT", null, null, null, null ,
		 * "set NLS_LANG=AMERICAN_AMERICA.UTF8\n chcp 65001\n sqlplus.exe crc/test@vm_cb3\n"
		 * ,"0"); if(ret==0) { System.out.println("-- DEBUG--  SUCCESS. "+
		 * to_string()); } else {
		 * System.err.println("-- DEBUG--  ERROR! exit-status: "+ ret + "" +
		 * to_string()); }
		 */

		return -2;

	}

	/*
	 * Выполняет одину проверку Возвращает код возврата выполнения скрипта -1 -
	 * ошибка
	 * 
	 * @depricated
	 */
	/*
	 * public int do_trail(String trail_type, String host, String port , String
	 * user, String password, String script, String max_duration_in_sec) {
	 * retOutStr = ""; retErrStr = "";
	 * //System.out.println("-- DEBUG-- Русский текст -- do_trail(["
	 * +trail_type+"],["+host+"],["+user+"])");
	 * 
	 * // String trail_type=null, host=null, port=null, user=null,
	 * password=null, script=null; // try { // trail_type = new
	 * String(p_trail_type.trim().toUpperCase().getBytes("UTF8")); // host = new
	 * String(p_host.trim().getBytes("UTF8")); // port = new
	 * String(p_port.trim().getBytes("UTF8")); // user = new
	 * String(p_user.trim().getBytes("UTF8")); // password = new
	 * String(p_password.trim().getBytes("UTF8")); // script = new
	 * String(p_script.trim().getBytes("UTF8")); // } catch(Exception e) {} try
	 * { if(trail_type==null){
	 * //System.err.println("-- DEBUG-- ERROR! Не задан тип trail_type: NULL");
	 * retErrStr += "\n" +
	 * "ERROR! Не задан тип trail_type: NULL  cb.dfs.trail.Trail.do_trail(...)";
	 * return -1; } if(trail_type.equals("TT_SSH_PWD")) { return
	 * exec_ssh_pwd(host, port, user, password, script); } else
	 * if(trail_type.equals("TT_SSH_KEY")) { return exec_ssh_key(host, port,
	 * user, script); } else if(trail_type.equals("TT_OS_SCRIPT")) { return
	 * exec_os_script(script); } else if(trail_type.equals("TT_TCP_PORT")) {
	 * return do_trail_tcp_port(host,port); } else
	 * if(trail_type.equals("TT_HTTP")) { return do_taril_http(script,
	 * max_duration_in_sec) , host, port, user, password); }
	 * System.err.println("-- DEBUG-- ERROR! Неизвестный тип проверки. trail_type: "
	 * + trail_type+" cb.dfs.trail.Trail.do_trail(...)");
	 * addRetErrStr("ERROR! Неизвестный тип проверки. trail_type: "+
	 * trail_type+" cb.dfs.trail.Trail.do_trail(...)"); return -1; } catch
	 * (Exception ex) {
	 * addRetErrStr("Ошибка во время выполнения операции проверки.\n" +
	 * ex.getMessage() + "\n" +
	 * "Инциндент в процедуре: cb.dfs.trail.Trail.do_trail(" +
	 * "trail_type=>"+trail_type+", host=>"+host+", port=>"+port
	 * +", user=>"+user+", password=>"+password+", script=>"+script +
	 * ", max_duration_in_sec=>"+max_duration_in_sec + ")\n"); return -1; }
	 * 
	 * }
	 */

}