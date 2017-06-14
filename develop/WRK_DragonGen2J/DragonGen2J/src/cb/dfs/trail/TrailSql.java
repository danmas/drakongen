package cb.dfs.trail;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//import org.apache.log4j.Logger;

import cb.dfs.trail.common.Constants;
import oracle.jdbc.driver.OracleDriver;

public class TrailSql extends TrailBase {

	protected String script;
	protected String jdbc_url, jdbc_user, jdbc_auth;

	//final static Logger logger = //logger.getLogger(TrailSql.class);
	
	public static void main(String[] args) {
		try {
			TrailManagerSubro trailSubroManager = null;
			trailSubroManager = new TrailManagerSubro(Constants.test_url, Constants.test_user, Constants.test_pwd);

			TrailSql trail = new TrailSql("RDAgent", " SQL Test ", "Авто тест SQL Test"
					, "select '1' from dual", Constants.test_url, Constants.test_user, Constants.test_pwd
					, "5000", "10","0");
			
			trail.overwrite(trailSubroManager.getConnection());
			trailSubroManager.getConnection().commit();
			trailSubroManager.launch_trail_if_ready((TrailBase) trail, "scenario_name", (int) (Math.random() * 1000.));

		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}

	
	public TrailSql(String trail_key) throws Exception {
		super("", trail_key, Constants.TT_SQL, "", "0", "0", "0");
	}

	public TrailSql(String run_agent, String trail_key, String description
			, String _script, String _jdbc_url, String _jdbc_user, String _jdbc_auth
			, String max_duration_in_sec, String launch_period_in_sec, String control_time_delay_in_sec) throws Exception {
		super(run_agent, trail_key, Constants.TT_SQL, description, max_duration_in_sec, launch_period_in_sec,
				control_time_delay_in_sec);
		script = _script;
		jdbc_url = _jdbc_url; jdbc_user = _jdbc_user; jdbc_auth = _jdbc_auth;
	}

	@Override
	public void setParam(String param, String value) throws Exception {
		switch (param.trim().toLowerCase()) {
		case "script":
			script = value;
			break;
		case "jdbc_url":
			jdbc_url = value;
			break;
		case "jdbc_user":
			jdbc_user = value;
			break;
		case "jdbc_auth":
			jdbc_auth = value;
			break;
		default:
			try {
				super.setParam(param, value);
			} catch (Exception ex) {
				throw new Exception("Неправильно задано имя параметра <" + param + "> или значение <" + value + ">"
						+ "\nИсключение в  cb.dfs.trail.TrailSql.setParam()");
			}
		}
	}

	@Override
	public void overwrite(Connection conn) throws Exception {
		PreparedStatement stmt = null;

		super.overwrite(conn, "v_trails_sql");

		try {
			stmt = conn.prepareStatement(
					"update v_trails_sql " + " set script=?, jdbc_url=?, jdbc_user=?, jdbc_auth=? "
					+ " where trail_key=?");
			int i = 1;
			Clob myClob = conn.createClob();
			myClob.setString(1, script);
			stmt.setClob(i++, myClob);
			stmt.setString(i++, jdbc_url);
			stmt.setString(i++, jdbc_user);
			stmt.setString(i++, jdbc_auth);
			stmt.setString(i++, trail_key);

			int cnt = stmt.executeUpdate();
			if (cnt != 1) {
				throw new Exception("Не удалось сделать запись."+ "\nИсключение в TrailSql.overwrite()");
			}
			stmt.close();
		} catch (SQLException se) {
			String str = "Ошибка " + se.getMessage() + "\n sql:" + stmt.toString()
					+ "\nSQL исключение в TrailSql.overwrite()";
			throw new Exception(str);
		} catch (Exception e) {
			String str = "Ошибка " + e.getMessage() + "\nИсключение в TrailSql.overwrite()";
			throw new Exception(str);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {} 
		} 
	}

	
	@Override
	public void read(Connection conn) throws Exception {

		super.read(conn);

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(
					"select script, jdbc_url, jdbc_user, jdbc_auth from V_TRAILS_SQL" + " where trail_key=?");
			stmt.setString(1, trail_key);
			ResultSet rs = stmt.executeQuery();
			int cnt = 0;
			while (rs.next()) {
				cnt++;
				script = rs.getString("script");
				jdbc_url = rs.getString("jdbc_url");
				jdbc_user = rs.getString("jdbc_user");
				jdbc_auth = rs.getString("jdbc_auth");
			}
			rs.close();
			stmt.close();
			if (cnt != 1) {
				String str = "Должна быть одна запись для операции проверки <" + trail_key + ">.";
				throw new Exception(str);
			}
		} catch (SQLException se) {
			String str = "Ошибка " + se.getMessage() + "\nSQL исключение в TrailSql.read()";
			throw new Exception(str);
		} catch (Exception e) {
			String str = "Ошибка " + e.getMessage() + "\nИсключение в TrailSql.read()";
			throw new Exception(str);
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
		}
	}

	@Override
	public String toString() {
		return "TrailSql [script=" + script + ", jdbc_url=" + jdbc_url + ", jdbc_user=" + jdbc_user + ", jdbc_auth="
				+ jdbc_auth + ", retOutStr=" + retOutStr + ", retErrStr=" + retErrStr + ", status=" + status
				+ ", ob_object_id=" + ob_object_id + ", run_agent=" + run_agent + ", trail_key=" + trail_key
				+ ", trail_type=" + trail_type + ", description=" + description + ", max_duration_in_sec="
				+ max_duration_in_sec + ", launch_period_in_sec=" + launch_period_in_sec
				+ ", control_time_delay_in_sec=" + control_time_delay_in_sec + ", last_start_date=" + last_start_date
				+ ", duration=" + duration + ", launch_period=" + launch_period + ", control_time_delay="
				+ control_time_delay + "]";
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	// public static void main(String[] args) throws java.io.IOException,
	// java.lang.InterruptedException {
	// // exec_script("cmd /c dir");
	// // exec_script("cmd /c dir");
	// Trail trail = new Trail();
	// TrailOsScript trailOsScript = new TrailOsScript(trail, "cmd /c dir");
	//
	// Thread thread = new Thread(trailOsScript);
	// thread.start();
	// try {
	// Thread.sleep(200); // Приостанавливает поток на 1 секунду
	// if (thread.isAlive()) {
	// System.err.println(" Случился таймаут!");
	// thread.interrupt();
	// }
	// } catch (InterruptedException e) {
	// System.err
	// .println(" Возникло исключение 'Прерывание исполнения' в главном
	// потоке.");
	// }
	// System.out.println(" Из главного потока. " + trail.to_string());
	// }

	@Override
	public void run() {
		//logger.debug(" run() with param "+jdbc_url+","+jdbc_user+","+jdbc_auth);
			Connection conn = null;
			try {
		        DriverManager.registerDriver(new OracleDriver());
		        conn = DriverManager.getConnection(jdbc_url, jdbc_user, jdbc_auth);
			} catch (Exception ex) {
				String str = "Ошибка при создании коннекции для выполнении SQL скрипта.\n"
						+ "["+jdbc_url+"],["+jdbc_user+"],["+jdbc_auth+"]\n"
						+ ex.getMessage()
						+ "\nИнциндент в процедуре: cb.dfs.trail.TrailSql.run()";
				//logger.error(str);
				addRetErrStr(str);
				setStatusError();
				return;
			}

			Statement stmt = null;
			try {
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(script);
				int cnt = 0;
				setStatusSuccess();
				String str = "";
				while(rs.next()) {
					cnt++;
					str += rs.getString(1) + "\n";
				}
				if(cnt == 0) {
					setStatusError();
					str = "Ошибка при выполнении проверочного SQL скрипта: "+script
							+"\nНе вернулось ни одной записи.";
					addRetErrStr(str); 
					//logger.error(str);
					rs.close();
					return;
				}
				addRetOutStr(str);
				rs.close();
				return;
			} catch (SQLException se) {
				String str = "Ошибка при выполнении скрипта: "+script+"\n" + se.getMessage()
						+ "\n"
						+ "Инциндент в процедуре: cb.dfs.trail.TrailSql.run()";
				addRetErrStr(str);
				//logger.error(str);
				setStatusError();
			} catch (Exception e) {
				String str = "Ошибка при выполнении скрипта: "+script+"\n" + e.getMessage()
						+ "\n"
						+ "Инциндент в процедуре: cb.dfs.trail.TrailSql.run()";
				addRetErrStr(str);
				//logger.error(str);
				setStatusError();
			} finally {
				try { if (stmt != null)
						stmt.close();
				} catch (SQLException se2) {}
				try { if (conn != null)
						conn.close();
				} catch (SQLException se) {se.printStackTrace();}
			}
	}


}
