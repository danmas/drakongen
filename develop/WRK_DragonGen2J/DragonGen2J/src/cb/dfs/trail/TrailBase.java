package cb.dfs.trail;

import java.sql.Clob;
import java.sql.Connection;
//import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import cb.dfs.trail.common.Constants;
import cb.dfs.trail.common.Version;
import cb.dfs.trail.common.Versioned;
import cb.dfs.trail.db.PreparedStatementHelper;
import cb.dfs.trail.utils.Strings;
//import org.apache.log4j.Logger;


@Version(major = Constants.CUR_VER_MAJOR, minor = Constants.CUR_VER_MINOR, revision = Constants.CUR_VER_REVIS)
public abstract class TrailBase extends Versioned implements Runnable {

	//final static Logger logger = Logger.getLogger(TrailBase.class);

	protected volatile String retOutStr = "";
	protected volatile String retErrStr = "";
	protected volatile String status = Constants.ST_UNKNOWN;
	protected volatile int ob_object_id = 0; // -- Если != 0 то id
												// контролируемого объекта в
												// СУБРО

	protected String run_agent = null;
	protected String trail_key = null;
	protected String trail_type = null;
	protected String description = null;
	protected String max_duration_in_sec = null;
	protected String launch_period_in_sec = null;
	protected String control_time_delay_in_sec = null;
	protected Date last_start_date = null;

	int duration = 0;
	int launch_period = 0;
	int control_time_delay = 0;
	
	public static void main(String[] args) {
		System.out.println(" --- Main по-русски ");
	}
	
	boolean debug = false;
	
	
	public boolean isDebug() {
		return debug;
	}
	

	public void setDebugOn() {
		this.debug = true;
	}
	public void setDebugOff() {
		this.debug = false;
	}
	

	public void debug(String msg) {
		if(debug) {
			addRetOutStr("DEBUG - " + msg);
		}
	}
	 
	public void error(String msg) {
		//if(debug) {
			addRetOutStr("ERROR - " + msg);
		//}
	}
	 
	protected String getDebugInfo() {
		return this.getClass().getName()+":"+Thread.currentThread().getStackTrace()[2].getMethodName()+":"+
				Thread.currentThread().getStackTrace()[2].getLineNumber();
	}
	
	public TrailBase(String _trail_key) {
		trail_key = _trail_key;
	}
	
	public TrailBase(String _run_agent, String _trail_key, String _trail_type, String _description
			, String _max_duration_in_sec, String _launch_period_in_sec
			, String  _control_time_delay_in_sec) throws Exception {
		run_agent = _run_agent;
		trail_key = _trail_key;
		trail_type = _trail_type;
		max_duration_in_sec = _max_duration_in_sec;
		duration = getDuration(_max_duration_in_sec);
		description = _description;
		launch_period_in_sec = _launch_period_in_sec;
		launch_period = getLaunchPeriod(_launch_period_in_sec);
		control_time_delay_in_sec = _control_time_delay_in_sec;
		control_time_delay= getControlTimeDelay(control_time_delay_in_sec);
	}

	
	boolean is_ready_to_start(Date cur_time) {
		//logger.debug("-- is_ready_to_start("+cur_time+") "); 
		if(last_start_date==null)
			return true;
		if(launch_period==0) {
			return false;
		}
		//System.out.println(last_start_date.getTime());
		boolean ret = cur_time.getTime() > last_start_date.getTime() + launch_period*1000;
		//logger.debug("-- is_ready_to_start("+cur_time+") returns "+ret); 
		return ret;
	}
	
	public void setParam(String param, String value) throws Exception {
		switch(param.trim().toLowerCase()) {
		case "run_agent": run_agent = value; break;
		case "max_duration_in_sec": max_duration_in_sec = value;
			duration = getDuration(max_duration_in_sec);
			break;
		case "description": description = value; break;
		case "launch_period_in_sec": launch_period_in_sec = value;
			launch_period = getLaunchPeriod(launch_period_in_sec);
			break;
		case "control_time_delay_in_sec": control_time_delay_in_sec = value;
			control_time_delay = getControlTimeDelay(control_time_delay_in_sec);
			break;
				
		default:
			throw new Exception("Неправильно задано имя параметра <"+param+"> или значение <"+value+">"
					+ "\nИсключение в  cb.dfs.trail.TrailBase.setParam()");
		}
	}
	
	@Override
	public String toString() {
		return "TrailBase [retOutStr=" + retOutStr + ", retErrStr=" + retErrStr + ", status=" + status
				+ ", ob_object_id=" + ob_object_id + ", run_agent=" + run_agent + ", trail_key=" + trail_key
				+ ", trail_type=" + trail_type + ", description=" + description + ", max_duration_in_sec="
				+ max_duration_in_sec + ", launch_period_in_sec=" + launch_period_in_sec
				+ ", control_time_delay_in_sec=" + control_time_delay_in_sec + ", last_start_date=" + last_start_date
				+ "]";
	}
	
	
	public void setLastStartDate(Date dt) throws Exception {
		last_start_date = dt;
	}
	
	
	public abstract void run(); 
	
	public abstract void overwrite(Connection conn) throws Exception;

	public void overwrite(Connection conn, String view_name) throws Exception {
		/*
		PreparedStatementHelper stmt = null;
		PreparedStatement prepStmt = null;
		String sql_str =  null;
		try {
			sql_str = "select count(*) cnt, max(ob_object_id) obid from "+view_name 
					+ " where trail_key=?";
			prepStmt = conn.prepareStatement(sql_str);
			stmt = new PreparedStatementHelper(prepStmt);
			
			stmt.setString(1, trail_key);
			ResultSet rs = stmt.executeQuery();
			int cnt = 0;
			int obid = 0;
			while (rs.next()) {
				cnt = rs.getInt("cnt");
				obid = rs.getInt("obid");
			}
			rs.close();
			stmt.close();
			if (cnt == 1) {
				ob_object_id = obid;
			}
			
			sql_str = "delete from " + view_name
					+ " where trail_key=?";
			prepStmt = conn.prepareStatement(sql_str);
			stmt = new PreparedStatementHelper(prepStmt);
			
			stmt.setString(1, trail_key);
			stmt.execute();
			stmt.close();
			
			sql_str = "insert into " + view_name 
							+ " (run_agent, trail_key,trail_type,description,max_duration_in_sec"
							+ ", launch_period_in_sec, control_time_delay_in_sec, status, out_msg, err_msg, ob_object_id"
							+ ", last_start_date "
							+ ") " + " values (?,?,?,?,?,?,?,?,?,?, ?, ?)";
			prepStmt = conn.prepareStatement(sql_str);
			stmt = new PreparedStatementHelper(prepStmt);
			int i=1;
			stmt.setString(i++, run_agent);
			stmt.setString(i++, trail_key);
			stmt.setString(i++, trail_type);
			stmt.setString(i++, description);
			stmt.setString(i++, max_duration_in_sec);
			stmt.setString(i++, launch_period_in_sec);
			stmt.setString(i++, control_time_delay_in_sec);
			
			stmt.setString(i++, status);

			Clob myClob = conn.createClob();
			myClob.setString(1, retOutStr);
			stmt.setClob(i++, myClob);
			//stmt.setString(i++, retOutStr);
			Clob myClob2 = conn.createClob();
			myClob2.setString(1, retErrStr);
			stmt.setClob(i++, myClob2);
			
			//stmt.setString(i++, retErrStr);
			stmt.setInt(i++, ob_object_id);
			stmt.setDate(i++, last_start_date);
			
			cnt = stmt.executeUpdate();
			if (cnt != 1) {
				throw new Exception("Не удалось сделать запись в <"+view_name+">."
						+ "\nОшибка в TrailBase.overwrite()");
			}

			stmt.close();
		} catch (SQLException se) {
			String str = "Ошибка " + se.getMessage()
					+ "\n sql:" + stmt.to_string(sql_str)
					+ "\nSQL исключение в TrailBase.overwrite()";
			throw new Exception(str);
		} catch (Exception e) {
			String str = "Ошибка " + e.getMessage()
					+ "\n sql: "+ stmt.to_string(sql_str)
					+ "\nИсключение в TrailBase.overwrite()";
			throw new Exception(str);
		} finally {
			try {if (stmt != null) stmt.close();} catch (SQLException se2) {}
		}*/
	}

	public void add_observable_object(Connection conn, String scenario_name, int odi_session_id) throws Exception {
		PreparedStatementHelper stmt = null;
		PreparedStatement prepStmt = null;
		String sql_str = "merge into D_OBSERVABLE_OBJECTS o  "
					+ "using (select ? OB_SUBRO_NAME from dual) n on "
					+ "(o.OB_SUBRO_NAME = n.OB_SUBRO_NAME) when matched then"
					+ "  update set OB_HEARTBEAT_STAMP=sysdate"
					+ ", OB_CONTROL_DATETIME= sysdate+?/(24*3600)+?/(24*3600)"
					+ ", OB_ODI_SESSION=?"
					+ ", OB_FORMAL_STATE=?"
					+ ", OB_STATE_TEXT_INFO=?"
					+ "    when not matched then"
					+ "    insert (OB_SCENARIO_NAME, OB_SUBRO_NAME, OB_DESCRIPTION"
					+ ", OB_HEARTBEAT_STAMP, OB_CONTROL_DATETIME "
					+ ", OB_ODI_SESSION, OB_FORMAL_STATE, OB_STATE_TEXT_INFO) values ("
					+ " ?, ?, ?"
					+ ", sysdate, sysdate+?/(24*3600)+?/(24*3600)"
					+ ", ?, ?, ?)";
			try {
				prepStmt = conn.prepareStatement(sql_str);
				stmt = new PreparedStatementHelper(prepStmt);
				
				String str_text_info = "";
				String str_formal_state = "RED";
				
				if(status.equals(Constants.ST_SUCCESS)) {
					str_formal_state = "GREEN";
					str_text_info = "Успешно выполнено. "+ retOutStr;
				} else if(status.equals(Constants.ST_RUNNING)) {
					str_formal_state = "GREEN";
					str_text_info = "Выполняется. "+ retOutStr;
				} else if(status.equals(Constants.ST_ERROR)) {
					str_formal_state = "RED";
					str_text_info = "Проверка закончилась с ошибкой. "+ retErrStr;
				} else if(status.equals(Constants.ST_UNKNOWN)) {
					str_formal_state = "RED";
					str_text_info = "Статус операции проверки неизвестен. ";
				}
				str_text_info = str_text_info.substring(0, str_text_info.length() > 2000 ? 2000 : str_text_info.length());
				int i = 1;
				stmt.setString(i++, trail_key);
				stmt.setInt(i++, launch_period); stmt.setInt(i++, control_time_delay);
				stmt.setInt(i++, odi_session_id);
				stmt.setString(i++, str_formal_state);
				//stmt.setClob(i++, myClob);
				stmt.setString(i++, str_text_info);
				
				stmt.setString(i++, scenario_name);
				stmt.setString(i++, trail_key);
				stmt.setString(i++, description);
				stmt.setInt(i++, launch_period); stmt.setInt(i++, control_time_delay);
				stmt.setInt(i++, odi_session_id);
				stmt.setString(i++, str_formal_state);
				//stmt.setClob(i++, myClob);
				stmt.setString(i++, str_text_info);
				
				//cstmt.registerOutParameter(i, java.sql.Types.NUMERIC);
				//System.out.println("-- DEBUG-- sql: "+ stmt.to_string(sql_str));
				stmt.execute();
				//ob_object_id = cstmt.getInt(i);
				stmt.close();
		} catch (SQLException se) {
			String str = "Ошибка " + se.getMessage()
					+ "\n sql: "+ stmt.to_string(sql_str)
					+ "\nSQL исключение в TrailBase.add_observable_object()";
			//System.out.println(str);
			throw new Exception(str);
		} catch (Exception e) {
			String str = "Ошибка " + e.getMessage() 
					+ "\n sql: "+ stmt.to_string(sql_str)
					+ "\nИсключение в TrailBase.add_observable_object()";
			//System.out.println(str);
			throw new Exception(str);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
		}

	}

	public void read(Connection conn) throws Exception {
		PreparedStatementHelper stmt = null;
		PreparedStatement prepStmt = null;
		String sql_str = "select run_agent, trail_type, description, max_duration_in_sec "
				+ ", status, out_msg, err_msg"
				+ ", launch_period_in_sec, control_time_delay_in_sec, ob_object_id, last_start_date" 
				+ " from d_trails"
				+ " where trail_key=?";
		try {
			prepStmt = conn.prepareStatement(sql_str);
			stmt = new PreparedStatementHelper(prepStmt);
			stmt.setString(1, trail_key);
			//System.out.println("-- DEBUG-- sql: "+ stmt.to_string(sql_str));
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				// Retrieve by column name
				run_agent = rs.getString("run_agent");
				trail_type = rs.getString("trail_type");
				// description = rs.getClob("description").toString();
				description = rs.getString("description").toString();
				max_duration_in_sec = rs.getString("max_duration_in_sec");
				duration = getDuration(max_duration_in_sec);
				launch_period_in_sec = rs.getString("launch_period_in_sec");
				launch_period = getLaunchPeriod(launch_period_in_sec);
				control_time_delay_in_sec = rs.getString("control_time_delay_in_sec");
				control_time_delay= getControlTimeDelay(control_time_delay_in_sec);
				last_start_date = rs.getDate("last_start_date");
				
				status = rs.getString("status");
				retOutStr = rs.getString("out_msg");
				retErrStr = rs.getString("err_msg");
				ob_object_id = rs.getInt("ob_object_id");
			}

			rs.close();
			stmt.close();
		} catch (SQLException se) {
			String str = "Ошибка " + se.getMessage() 
					+ "\n sql: "+ stmt.to_string(sql_str)
					+ "\nSQL исключение в TrailBase.read()";
			throw new Exception(str);
		} catch (Exception e) {
			String str = "Ошибка " + e.getMessage() 
					+ "\n sql: "+ stmt.to_string(sql_str)
					+ "\nИсключение в TrailBase.read()";
			throw new Exception(str);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
		}
	}

	public void mergeIntoDb() {

	}

	protected void setStatusError() {
		this.status = Constants.ST_ERROR;
	}

	protected void setStatusRunning() {
		this.status = Constants.ST_RUNNING;
	}

	protected void setStatusSuccess() {
		this.status = Constants.ST_SUCCESS;
	}

	public int getDuration() {
		return duration;
	}

	protected int getDuration(String max_duration_in_sec) throws Exception {
		int duration;
		try {
			duration = Strings.stringToPositivInt(max_duration_in_sec);
		} catch (Exception ex) {
			String str = "Неверно задано время выполнения в секундах <" + max_duration_in_sec + ">.";
			addRetErrStr(str);
			throw new Exception(str);
		}
		return duration;
	}

	protected int getLaunchPeriod(String launch_period_in_sec) throws Exception {
		int ival;
		try {
			ival = Strings.stringToPositivInt(launch_period_in_sec);
		} catch (Exception ex) {
			String str = "Неверно задан период повторного исполнения в секундах <" + launch_period_in_sec + ">.";
			addRetErrStr(str);
			throw new Exception(str);
		}
		return ival;
	}

	protected int getControlTimeDelay(String _control_time_delay_in_sec) throws Exception {
		int ival;
		try {
			ival = Strings.stringToPositivInt(_control_time_delay_in_sec);
		} catch (Exception ex) {
			String str = "Неверно задана задержка контроля запуска проверочной операции в секундах <" + _control_time_delay_in_sec + ">.";
			addRetErrStr(str);
			throw new Exception(str);
		}
		return ival;
	}
	
	
	protected int getPort(String sport) throws Exception {
		int iport;
		if(sport==null || sport.equals("")) {
			return 0;
		}
		try {
			iport = Strings.stringToPositivInt(sport);
		} catch (Exception ex) {
			String str = "Неверно задан номер порта <" + sport + ">.";
			addRetErrStr(str);
			throw new Exception(str);
		}
		return iport;
	}

	public String to_string() {
		return super.to_string() + " status: " + getStatus() + "\nerr:" + retErrStr + "\nout:\n" + retOutStr;
	}

	public String getRetOutStr() {
		return retOutStr;
	}

	// public void setRetOutStr(String retOutStr) {
	// retOutStr = retOutStr;
	// }

	public String getRetErrStr() {
		return retErrStr;
	}

	// public void setRetErrStr(String retErrStr) {
	// retErrStr = retErrStr;
	// }

	public void clearRetStrs() {
		retOutStr = ""; retErrStr = ""; 
	}
	
	public void addRetErrStr(String str) {
		retErrStr += str + "\n";
	}

	public void addRetOutStr(String str) {
		retOutStr += str + "\n";
	}

	public String getStatus() {
		return status;
	}

}