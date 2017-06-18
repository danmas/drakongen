package cb.dfs.trail;

import java.io.File;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import cb.dfs.trail.common.Constants;
import cb.dfs.trail.utils.Enviroment;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class TrailSshKey  extends TrailBase {

	final static Logger logger = Logger.getLogger(TrailSshKey.class);
	
	protected String host, port, user, script;

	public TrailSshKey(String trail_key)  throws Exception {
		super("", trail_key, Constants.TT_SSH_KEY, "", "0", "0", "0");
	}
	
	public TrailSshKey(String run_agent, String trail_key, String description, String _host, String _port,
			String _user, String _script, String max_duration_in_sec
			, String launch_period_in_sec, String control_time_delay_in_sec) throws Exception {
		super(run_agent, trail_key, Constants.TT_SSH_KEY, description, max_duration_in_sec
				, launch_period_in_sec, control_time_delay_in_sec);
		host=_host; port=_port; user=_user;
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
		case "script": script = value; break;
		default:
			try {
				super.setParam(param, value);
			} catch (Exception ex) {
				throw new Exception("Неправильно задано имя параметра <"+param+"> или значение <"+value+">"
						+ "\nИсключение в  cb.dfs.trail.TrailSshKey.setParam()");
			}
		}
	}
	
	@Override
	public String toString() {
		return "TrailSshKey [host=" + host + ", port=" + port + ", user="
				+ user + ", script=" + script + ", retOutStr=" + retOutStr
				+ ", retErrStr=" + retErrStr + ", status=" + status
				+ ", run_agent=" + run_agent + ", trail_key=" + trail_key
				+ ", trail_type=" + trail_type + ", description=" + description
				+ ", max_duration_in_sec=" + max_duration_in_sec
				+ ", heartbeat_in_sec=" + launch_period_in_sec + ", duration="
				+ duration + "]";
	}

	@Override
	public void run() {
		try {
			JSch jsch = new JSch(); 

			Session session = jsch.getSession(user, host, new Integer(port));

			String privateKey = "";

			String home = System.getProperty("user.home");
			logger.debug("-- home dir : " + home);
			if (new File(home + "/.ssh/id_rsa").exists()) {
				privateKey = home + "/.ssh/id_rsa";
			} else {
				logger.debug("-- SSH_KEY_PATH : "	+ Enviroment.getEnvVar("SSH_KEY_PATH"));
				privateKey = Enviroment.getEnvVar("SSH_KEY_PATH");
			}

			File f = new File(privateKey);
			if (!f.exists()) {
				addRetErrStr("Не удается найти файл ключа.\n");
				setStatusError();
				return;
			}

			logger.debug("-- trying add identity with "	+ privateKey);
			try {
				jsch.addIdentity(privateKey);
			} catch (Exception ex) {
				logger.error("-- identity WAS NOT added! ");
				addRetErrStr("Не удается найти файл ключа.\n");
				setStatusError();
				return;
			}
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config); 

			TrailSsh.do_trail(this, session, script);
			return; 
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			addRetErrStr("ERROR: " + ex.getMessage());
			setStatusError();
			return;
		}
	}

	/*
	@Override
	public void overwrite(Connection conn) throws Exception {
		
		super.overwrite(conn,"v_trails_ssh_key");
		
		PreparedStatementHelper stmt = null;
		PreparedStatement prepStmt = null;
		
		String sql_str = "update v_trails_ssh_key "
				+ " set ssh_host=?, ssh_port=?, ssh_user=?, script=? where trail_key=?";
		try {
			prepStmt = conn.prepareStatement(sql_str);
			stmt = new PreparedStatementHelper(prepStmt);
			int i = 1;
			stmt.setString(i++, host);
			stmt.setString(i++, port);
			stmt.setString(i++, user);
			Clob myClob = conn.createClob();
			myClob.setString(1, script);
			stmt.setClob(i++, myClob);
			//stmt.setString(i++, script);
			stmt.setString(i++, trail_key);

			int cnt = stmt.executeUpdate();
			if (cnt != 1) {
				String str = "Не удалось сделать запись.\n sql: "+ stmt.to_string(sql_str);
				System.err.println(str);
				throw new Exception(str);
			}

			stmt.close();
		} catch (SQLException se) {
			String str = "Ошибка " + se.getMessage()
					+ stmt.to_string(sql_str)
					+ "\nSQL исключение в TrailSshKey.overwrite()";
			throw new Exception(str);
		} catch (Exception e) {
			String str = "Ошибка " + e.getMessage()
					+ stmt.to_string(sql_str)
					+ "\nИсключение в TrailSshKey.overwrite()";
			throw new Exception(str);
		} finally {
			try {if (stmt != null)	stmt.close();} catch (SQLException se2) {}
		}
	}

	@Override
	public void read(Connection conn) throws Exception {

		super.read(conn);

		PreparedStatement stmt = null;
		try {
			stmt = conn
					.prepareStatement("select ssh_host, ssh_port, ssh_user, script from v_trails_ssh_key"
							+ " where trail_key=?");
			stmt.setString(1, trail_key);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				host = rs.getString("ssh_host");
				port = rs.getString("ssh_port");
				user = rs.getString("ssh_user");
				script = rs.getString("script");
			}
			rs.close();
			stmt.close();
		} catch (SQLException se) {
			String str = "Ошибка " + se.getMessage()
					+ "\nSQL исключение в TrailSshKey.read()";
			throw new Exception(str);
		} catch (Exception e) {
			String str = "Ошибка " + e.getMessage()
					+ "\nИсключение в TrailSshKey.read()";
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
*/
	

}
