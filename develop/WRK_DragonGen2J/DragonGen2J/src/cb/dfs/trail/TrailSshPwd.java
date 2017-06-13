package cb.dfs.trail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cb.dfs.trail.common.Constants;

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
	public void overwrite(Connection conn) throws Exception {
		super.overwrite(conn,"v_trails_ssh_pwd");
		
		PreparedStatement stmt = null;
		
		try {
			stmt = conn
					.prepareStatement("update v_trails_ssh_pwd "
							+ " set ssh_host=?, ssh_port=?, ssh_user=?, script=?, ssh_auth=?"
							+ " where trail_key=?");
			int i = 1;
			stmt.setString(i++, host);
			stmt.setString(i++, port);
			stmt.setString(i++, user);
			stmt.setString(i++, script);
			stmt.setString(i++, password);
			stmt.setString(i++, trail_key);

			int cnt = stmt.executeUpdate();
			if (cnt != 1) {
				throw new Exception("Не удалось сделать запись.");
			}

			stmt.close();
		} catch (SQLException se) {
			String str = "Ошибка " + se.getMessage()
					+ "\nSQL исключение в TrailSshPwd.write()";
			throw new Exception(str);
		} catch (Exception e) {
			String str = "Ошибка " + e.getMessage()
					+ "\nИсключение в TrailSshPwd.write()";
			throw new Exception(str);
		} finally {
			try { if (stmt != null) stmt.close(); } catch (SQLException se2) {}
		}
	}

	
	@Override
	public void read(Connection conn) throws Exception {
		super.read(conn);

		PreparedStatement stmt = null;
		try {
			stmt = conn
					.prepareStatement("select ssh_host, ssh_port, ssh_user, ssh_auth, script from v_trails_ssh_pwd"
							+ " where trail_key=?");
			stmt.setString(1, trail_key);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				host = rs.getString("ssh_host");
				port = rs.getString("ssh_port");
				user = rs.getString("ssh_user");
				password = rs.getString("ssh_auth");
				script = rs.getString("script");
			}
			rs.close();
			stmt.close();
		} catch (SQLException se) {
			String str = "Ошибка " + se.getMessage()
					+ "\nSQL исключение в TrailSshPwd.read()";
			throw new Exception(str);
		} catch (Exception e) {
			String str = "Ошибка " + e.getMessage()
					+ "\nИсключение в TrailSshPwd.read()";
			throw new Exception(str);
		} finally {
			try { if (stmt != null) stmt.close(); } catch (SQLException se2) {}
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