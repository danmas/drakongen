package cb.dfs.trail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//import org.apache.log4j.Logger;

import cb.dfs.trail.common.Constants;

public class TrailOsScript extends TrailBase {

	//final static Logger logger = //logger.getLogger(TrailOsScript.class);
	
	protected String script;

	public TrailOsScript(String trail_key)  throws Exception {
		super("", trail_key, Constants.TT_OS_SCRIPT, "", "0", "0", "0");
	}
	
	public TrailOsScript(String run_agent, String trail_key, String description, String _script,
			String max_duration_in_sec, String launch_period_in_sec, String control_time_delay_in_sec)
			throws Exception {
		super(run_agent, trail_key, Constants.TT_OS_SCRIPT, description,
				max_duration_in_sec, launch_period_in_sec, control_time_delay_in_sec);
		script = _script;
	}

	@Override
	public void setParam(String param, String value) throws Exception {
		switch(param.trim().toLowerCase()) {
		case "script": script = value; break;
		default:
			try {
				super.setParam(param, value);
			} catch (Exception ex) {
				throw new Exception("Неправильно задано имя параметра <"+param+"> или значение <"+value+">"
						+ "\nИсключение в  cb.dfs.trail.TrailOsScript.setParam()");
			}
		}
	}
	
	
	@Override
	public void overwrite(Connection conn) throws Exception {
		PreparedStatement stmt = null;

		super.overwrite(conn,"v_trails_os_script");
		
		try {
			stmt = conn
					.prepareStatement("update v_trails_os_script "
							+ " set script=? where trail_key=?");
			int i=1;
			Clob myClob = conn.createClob();
			myClob.setString(1, script);
			stmt.setClob(i++, myClob);
			//stmt.setString(i++, script);
			stmt.setString(i++, trail_key);
			
			int cnt = stmt.executeUpdate();
			if (cnt != 1) {
				throw new Exception("Не удалось сделать запись.");
			}

			stmt.close();
		} catch (SQLException se) {
			String str = "Ошибка " + se.getMessage()
					+ "\n sql:" + stmt.toString()
					+ "\nSQL исключение в TrailOsScript.overwrite()";
			throw new Exception(str);
		} catch (Exception e) {
			String str = "Ошибка " + e.getMessage()
					+ "\nИсключение в TrailOsScript.overwrite()";
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

	@Override
	public void read(Connection conn) throws Exception {

		super.read(conn);

		PreparedStatement stmt = null;
		try {
			stmt = conn
					.prepareStatement("select script from V_TRAILS_OS_SCRIPT"
							+ " where trail_key=?");
			stmt.setString(1, trail_key);
			ResultSet rs = stmt.executeQuery();
			int cnt = 0;
			while (rs.next()) {
				cnt++; 
				script = rs.getString("script");
			}
			rs.close();
			stmt.close();
			if(cnt!=1) {
				String str = "Должна быть одна запись для операции проверки <"+ trail_key+">.";
				throw new Exception(str);
			}
		} catch (SQLException se) {
			String str = "Ошибка " + se.getMessage()
					+ "\nSQL исключение в TrailOsScript.read()";
			throw new Exception(str);
		} catch (Exception e) {
			String str = "Ошибка " + e.getMessage()
					+ "\nИсключение в TrailOsScript.read()";
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
	public void run() {
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(script);

			BufferedReader out = new BufferedReader(new InputStreamReader(
					proc.getInputStream())); // ,"UTF8"));
			BufferedReader err = new BufferedReader(new InputStreamReader(
					proc.getErrorStream())); // ,"UTF8"));

			int exitVal = proc.waitFor();

			while (out.ready()) {
				addRetOutStr(out.readLine());
			}
			while (err.ready()) {
				String str = err.readLine();
				addRetErrStr(str);
			}
			setStatusError();
			//logger.debug("-- Скрипт вернул "+exitVal+". Out:"+getRetOutStr());
			if (exitVal == 0) {
				setStatusSuccess();
				return;
			} else {
				setStatusError();
				addRetErrStr(getRetOutStr());
			}
			return;
		} catch (Throwable t) {
			// t.printStackTrace();
			addRetErrStr("Ошибка при выполнении скрипта.\n" + t.getMessage()
					+ "\n"
					+ "Инциндент в процедуре: cb.dfs.trail.TrailOsScript.run()");
			setStatusError();
			return;
		} finally {
		}
	}

	@Override
	public String toString() {
		return "TrailOsScript [script=" + script + ", retOutStr=" + retOutStr + ", retErrStr=" + retErrStr + ", status="
				+ status + ", ob_object_id=" + ob_object_id + ", run_agent=" + run_agent + ", trail_key=" + trail_key
				+ ", trail_type=" + trail_type + ", description=" + description + ", max_duration_in_sec="
				+ max_duration_in_sec + ", heartbeat_in_sec=" + launch_period_in_sec + ", duration=" + duration
				+ ", heartbeat=" + launch_period + "]";
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	 public static void main(String[] args) {
		 TrailOsScript trail = null;
		 try {
			TrailManagerSubro trailSubroManager = null;
			trailSubroManager = new TrailManagerSubro(Constants.test_url
					, Constants.test_user, Constants.test_pwd);
			
			trail = new TrailOsScript("RDAgent", "Ping test ", "Авто тест "
					, "ping -n 30 vm-cb3-bi.vm-p.rdtex.ru"
					, "10", "10","0");
			
			trail.overwrite(trailSubroManager.getConnection());
			trailSubroManager.getConnection().commit();
			trailSubroManager.launch_trail_if_ready((TrailBase) trail, "scenario_name", (int) (Math.random() * 1000.));
			
			System.out.println(trail.to_string());
		} catch (Exception ex) {
			System.err.println("---" + ex.getMessage());
			System.err.println(trail.to_string());
		}
	}
		 
}	 