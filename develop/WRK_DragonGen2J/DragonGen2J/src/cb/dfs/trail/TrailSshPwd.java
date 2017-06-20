package cb.dfs.trail;

//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;

import cb.dfs.trail.common.Constants;

import org.json.simple.JSONObject;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class TrailSshPwd extends TrailBase {

	String host, port, user, password;
	protected String script;

	
	public TrailSshPwd(String trail_key)  throws Exception {
		super("", trail_key, Constants.TT_SSH_PWD, "", "0", "0", "0");
	}
	
	
	public TrailSshPwd(String run_agent, String trail_key, String description, String _host,
			String _port, String _user, String _password, String _script,
			String max_duration_in_sec
			, String launch_period_in_sec, String control_time_delay_in_sec)
			throws Exception {
		super(run_agent, trail_key, Constants.TT_SSH_PWD, description,
				max_duration_in_sec, launch_period_in_sec, control_time_delay_in_sec);
		host = _host;
		port = _port;
		user = _user;
		password = _password;
		script = _script;
	}
	
	
	/*
	 * Получение параметров из JSON
	 * если в json не указан параметр то старый не переписыавется!
	 */
	@Override
	public void updateParamsFromJ(JSONObject jo) throws Exception {
		super.updateParamsFromJ(jo);
		if(jo.get("host")!=null) {
			host = (String)(jo.get("host")); 
		}
		if(jo.get("port")!=null) {
			port = (String)(jo.get("port")); 
		}
		if(jo.get("user")!=null) {
			user = (String)(jo.get("user")); 
		}
		if(jo.get("password")!=null) {
			password = (String)(jo.get("password")); 
		}
		if(jo.get("script")!=null) {
			script = (String)(jo.get("script")); 
		}
	}
	
	@Override
	public void setParam(String param, String value) throws Exception {
		switch(param.trim().toLowerCase()) {
		case "host": host = value; break;
		case "port": port = value; break;
		case "user": user = value; break;
		case "password": password = value; break;
		case "script": script = value; break;
		default:
			try {
				super.setParam(param, value);
			} catch (Exception ex) {
				throw new Exception("Неправильно задано имя параметра <"+param+"> или значение <"+value+">"
						+ "\nИсключение в  cb.dfs.trail.TrailSshPwd.setParam()");
			}
		}
	}
	
	@Override
	public String toString() {
		return "TrailSshPwd [host=" + host + ", port=" + port + ", user="
				+ user + ", password=" + password + ", script=" + script
				+ ", retOutStr=" + retOutStr + ", retErrStr=" + retErrStr
				+ ", status=" + status + ", run_agent=" + run_agent
				+ ", trail_key=" + trail_key + ", trail_type=" + trail_type
				+ ", description=" + description + ", max_duration_in_sec="
				+ max_duration_in_sec + ", heartbeat_in_sec="
				+ launch_period_in_sec + ", duration=" + duration + "]";
	}

	@Override
	public void run() {
		//System.out.println("~~~~~~~~~:"+toString() );
		try {
			JSch jsch = new JSch();

			Session session = jsch.getSession(user, host, new Integer(port));

			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);

			TrailSsh.do_trail(this, session, script);
			return;
		} catch (Exception ex) {
			// System.err.println(e.getMessage());
			addRetErrStr("ERROR: " + ex.getMessage());
			setStatusError();
			return;
		}

	}
}
