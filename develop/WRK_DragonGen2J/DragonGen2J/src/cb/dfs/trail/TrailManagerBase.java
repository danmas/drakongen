package cb.dfs.trail;

//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import cb.dfs.trail.common.Constants;
import cb.dfs.trail.common.Version;
//import cb.dfs.trail.db.OraConnector;

@Version(major = Constants.CUR_VER_MAJOR, minor = Constants.CUR_VER_MINOR, revision = Constants.CUR_VER_REVIS)
public class TrailManagerBase extends TrailRunner {

	
	final static Logger logger = Logger.getLogger(TrailManager.class);
	
	protected String getDebugInfo() {
		return this.getClass().getName()+":"+Thread.currentThread().getStackTrace()[2].getMethodName()+":"+
				Thread.currentThread().getStackTrace()[2].getLineNumber();
	}
	
	
	//protected OraConnector connector = null;;
	//Connection external_connection = null;
	
	public TrailManagerBase() {
		super();
	}

	/*
	public TrailManager(Connection _external_connection) {
		super();
		external_connection = _external_connection;
	    //logger.debug("-- TrailManager with connection "+_external_connection+" was opened.");
	    //trail.debug(getDebugInfo()+"-- TrailManager with connection "+_external_connection+" was opened.");
	}*/
	
	
	/*
	public TrailManager(String url, String user, String password)
			throws Exception {
		super();
		connector = OraConnector.getOraConnector(url, user, password);
	}*/

	public void do_trail(TrailBase trail) throws Exception {
		//logger.debug("-- do_trail 1 -- ");
		trail.debug(getDebugInfo()+"--  do_trail 1 -- ");
		Date dt = getSydateFromMaster(trail);
		//logger.debug("-- do_trail 2 -- ");
		trail.debug(getDebugInfo()+"--  do_trail 2 -- ");
		trail.setLastStartDate(dt);
		//logger.debug("-- do_trail 3 -- ");
		trail.debug(getDebugInfo()+"--  do_trail 3 -- ");
		super.do_trail(trail);
		//logger.debug("-- do_trail 4 -- ");
		trail.debug(getDebugInfo()+"--  do_trail 4 -- ");
		//trail.overwrite(getConnection());
		//logger.debug("-- do_trail 5 -- ");
		trail.debug(getDebugInfo()+"--  do_trail 5 -- ");
	}
	
	
//	public Connection getConnection() throws Exception {
//		if(external_connection!=null)
//			return external_connection;
//		return connector.getConnection();
//	}

	
	public void setParam(String trail_key, String param, String value) throws Exception {
		TrailBase trail = read(trail_key);
		trail.setParam(param, value); 
	}
	
	public Date getSydateFromMaster(TrailBase trail) throws Exception {
		//DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		//System.out.println(dateFormat.format(cal.getTime()));
		return cal.getTime();
	}
	
	/*
	public Date getSydateFromMasterDB(TrailBase trail) throws Exception {
		Date dt = null;
		PreparedStatementHelper stmt = null;
		PreparedStatement prepStmt = null;
		try {
			//logger.debug("-- getSydateFromMaster() 1 -- ");
			trail.debug(getDebugInfo()+"-- getSydateFromMaster() 1 -- ");
			String sql_str = "select sysdate from dual";
			prepStmt = getConnection().prepareStatement(sql_str);
			stmt = new PreparedStatementHelper(prepStmt);
			//logger.debug("-- getSydateFromMaster() 2 -- ");
			trail.debug(getDebugInfo()+"-- getSydateFromMaster() 2 -- ");
			ResultSet rs = stmt.executeQuery();
			//logger.debug("-- getSydateFromMaster() 3 -- ");
			trail.debug(getDebugInfo()+"-- getSydateFromMaster() 3 -- ");

			while (rs.next()) {
				dt = rs.getDate(1); 
			}
			if(dt==null) {
				logger.error("Ошибка! В getSydateFromMaster(): не вернулась дата.");
				trail.error(getDebugInfo()+"-- Ошибка! В getSydateFromMaster(): не вернулась дата.");
				return null;
			}	
			//logger.debug("-- getSydateFromMaster() 4 -- ");
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
	*/
	
	/*
	 * Читает из базы и создает ОП по ключу
	 */
	public TrailBase read(String trail_key) throws Exception {
		//String trail_type = readTrailType(trail_key);
		String trail_type = trail_key;
		TrailBase trail = null;
		switch(trail_type) {
		case Constants.TT_OS_SCRIPT:
			trail = new_trail_os_script(trail_key);
			//trail.read(getConnection());
			return trail;
		case Constants.TT_TCP_PORT:
			trail = new_trail_tcp_port(trail_key);
			//trail.read(getConnection());
			return trail;
		case Constants.TT_HTTP:
			trail = new_trail_http(trail_key);
			//trail.read(getConnection());
			return trail;
		case Constants.TT_SSH_KEY:
			trail = new_trail_ssh_key(trail_key);
			//trail.read(getConnection());
			return trail;
		case Constants.TT_SSH_PWD:
			trail = new_trail_ssh_pwd(trail_key);
			//trail.read(getConnection());
			return trail;
		case Constants.TT_SQL:
			trail = new_trail_sql(trail_key);
			//trail.read(getConnection());
			return trail;
		default :	
			throw new Exception("Неизвестный тип ОП <" + trail_type + ">.");
		}
	}

	/*
	 * Записывает ОП в БД
	 */
	public void write(TrailBase trail) throws Exception {
		//trail.overwrite(getConnection());
	}

	

	public static void main(String[] args) {

		try {
			//TrailManager tm = new TrailManager(Constants.test_url,Constants.test_user,Constants.test_pwd);
			TrailManagerBase tm = new TrailManagerBase();
			System.out.println(tm.getVersion());
			// trail.do_test();
	
			String trail_key = "(G) Проверка SQL. время отклика 5 сек";
			TrailBase trail = tm.read(trail_key);
			trail.setParam("run_agent", "RDAgent");
			trail.setParam("launch_period_in_sec", "10");
			tm.write(trail);
			//tm.getConnection().commit();
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
