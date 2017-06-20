package cb.dfs.trail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
//import java.sql.Clob;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import cb.dfs.trail.common.Constants;

public class TrailOsScript extends TrailBase {

	final static Logger logger = Logger.getLogger(TrailOsScript.class);
	
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

	/*
	 * Получение параметров из JSON
	 * если в json не указан параметр то старый не переписыавется!
	 */
	@Override
	public void updateParamsFromJ(JSONObject jo) throws Exception {
		super.updateParamsFromJ(jo);
		if(jo.get("script")!=null) {
			script = (String)(jo.get("script")); 
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
			logger.debug("-- Скрипт вернул "+exitVal+". Out:"+getRetOutStr());
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
			trail = new TrailOsScript("RDAgent", "Ping test ", "Авто тест "
					, "ping -n 3 carlhost"
					, "10", "10","0");
			trail.launch_single();
			System.out.println(trail.to_string());
		} catch (Exception ex) {
			System.err.println("---" + ex.getMessage());
			System.err.println(trail.to_string());
		}
	}
		 
}	 
