package cb.dfs.trail;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;

import org.json.simple.JSONObject;

import cb.dfs.trail.common.Constants;

public class TrailTcpPort extends TrailBase {
	String host;
	int iport;

	public TrailTcpPort(String trail_key)  throws Exception {
		super("",trail_key, Constants.TT_TCP_PORT, "", "0", "0","0");
	}

	public TrailTcpPort(String run_agent, String trail_key, String description, String _host, String port,
			String max_duration_in_sec, String launch_period_in_sec
			, String  control_time_delay_in_sec) throws Exception {
		super(run_agent, trail_key, Constants.TT_TCP_PORT, description
				, max_duration_in_sec, launch_period_in_sec, control_time_delay_in_sec);
		host = _host;
		iport = getPort(port);
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
			iport = getPort((String)(jo.get("port"))); 
		}
	}
		
	@Override
	public void setParam(String param, String value) throws Exception {
		switch(param.trim().toLowerCase()) {
		case "host": host = value; break;
		case "port": String port = value; 
			iport = getPort(port);
			break;
		default:
			try {
				super.setParam(param, value);
			} catch (Exception ex) {
				throw new Exception("Неправильно задано имя параметра <"+param+"> или значение <"+value+">"
						+ "\nИсключение в  cb.dfs.trail.TrailTcpPort.setParam()");
			}
		}
	}
	
	@Override
	public String toString() {
		return "TrailTcpPort [host=" + host + ", iport=" + iport + ", retOutStr=" + retOutStr + ", retErrStr="
				+ retErrStr + ", status=" + status + ", ob_object_id=" + ob_object_id + ", run_agent=" + run_agent
				+ ", trail_key=" + trail_key + ", trail_type=" + trail_type + ", description=" + description
				+ ", max_duration_in_sec=" + max_duration_in_sec + ", heartbeat_in_sec=" + launch_period_in_sec
				+ ", duration=" + duration + ", heartbeat=" + launch_period + "]";
	}

	@Override
	public void run() {
		try {
			do_trail();
		} catch (Exception ex) {
			// System.err.println(e.getMessage());
			addRetErrStr("Невозможно открыть доступ к "+host+" по порту "+ iport+".\n"  + ex.getMessage());
			setStatusError();
		}
	}
/*
	@Override
	public void overwrite(Connection conn) throws Exception {
		
		super.overwrite(conn,"v_trails_tcp_port");
		
		PreparedStatement stmt = null;
		
		try {
			stmt = conn
					.prepareStatement("update v_trails_tcp_port "
							+ " set host=?, port=? where trail_key=?");
			int i=1;
			stmt.setString(i++, host);
			stmt.setString(i++, String.valueOf(iport));
			stmt.setString(i++, trail_key);

			int cnt = stmt.executeUpdate();
			if (cnt != 1) {
				throw new Exception("Не удалось сделать запись.");
			}

			stmt.close();
		} catch (SQLException se) {
			String str = "Ошибка " + se.getMessage() + "\nSQL исключение в TrailTcpPort.write()";
			throw new Exception(str);
		} catch (Exception e) {
			String str = "Ошибка " + e.getMessage() + "\nИсключение в TrailTcpPort.write()";
			throw new Exception(str);
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
		} // end try

	}

	@Override
	public void read(Connection conn) throws Exception {

		super.read(conn);

		PreparedStatement stmt = null;
		try {
			stmt = conn
					.prepareStatement("select host, port from V_TRAILS_TCP_PORT"
							+ " where trail_key=?");
			stmt.setString(1, trail_key);
			ResultSet rs = stmt.executeQuery();
			int cnt = 0;
			while (rs.next()) {
				cnt++; 

				host = rs.getString("host");
				String port = rs.getString("port");
				iport = getPort(port);
			}
			rs.close();
			stmt.close();
			if(cnt!=1) {
				String str = "Должна быть одна запись для операции проверки <"+ trail_key+">.";
				throw new Exception(str);
			}
		} catch (SQLException se) {
			String str = "Ошибка " + se.getMessage()
					+ "\nSQL исключение в TrailTcpPort.read()";
			throw new Exception(str);
		} catch (Exception e) {
			String str = "Ошибка " + e.getMessage()
					+ "\nИсключение в TrailTcpPort.read()";
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
	
	public void do_trail() throws Exception {
		// try {
		// iport = Integer.valueOf(port);
		// } catch(Exception ex) {
		// throw new Exception("Неверно задан порт <"+server+":"+port+">.");
		// }
		Socket s = new Socket(host, iport);
		OutputStream os = s.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		dos.writeDouble(1.0);
		setStatusSuccess();
		dos.close();
		os.close();
	}

	/*
	 * public static void trail_tcp_port(String server_port) throws Exception {
	 * try { int i = server_port.indexOf(":"); if(i==-1) { throw new Exception(
	 * "Неверный формат для указания сервера и порта <"+server_port+
	 * ">. Должно быть: server:port"); } server = server_port.substring(0, i);
	 * port = Integer.valueOf(server_port.substring(i+1, server_port.length()));
	 * } catch(Exception ex) { throw new Exception(
	 * "Неверно заданы сервер или порт <"+server_port+">."); } Socket s = new
	 * Socket(server, port); OutputStream os = s.getOutputStream();
	 * DataOutputStream dos = new DataOutputStream(os); dos.writeDouble(1.0); }
	 */

	// public static void main(String[] args) {
	// try {
	// trail_tcp_port("http-proxy.srv.rdtex.ru:3128");
	// } catch(Exception ex) {
	// System.err.println(ex.getMessage());
	// }
	// }

}
