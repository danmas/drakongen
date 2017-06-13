package cb.dfs.trail;

import cb.dfs.trail.common.Constants;
import cb.dfs.trail.common.Version;
import cb.dfs.trail.common.Versioned;

@Version(major = Constants.CUR_VER_MAJOR, minor = Constants.CUR_VER_MINOR, revision = Constants.CUR_VER_REVIS)
public class TrailRunner extends Versioned {

	
	
	public TrailBase new_trail_os_script(String trail_key)  throws Exception {
		return new_trail_os_script("", trail_key, "", "","0","0","0");
	}

	
	public TrailBase new_trail_os_script(String run_agent, String trail_key, String description, String script,
			String max_duration_in_sec, String launch_period_in_sec, String control_time_delay_in_sec)  throws Exception {
		TrailOsScript trail = null;
		trail = new TrailOsScript(run_agent, trail_key, description, script, max_duration_in_sec
				, launch_period_in_sec, control_time_delay_in_sec);
		return trail;
	}

	public TrailBase new_trail_ssh_key(String trail_key) throws Exception {
		return new_trail_ssh_key("", trail_key, "", "", "", "",
				"", "0", "0", "0");
	}
	public TrailBase new_trail_ssh_key(String run_agent, String trail_key, String description, String host, String port, String user,
			String script, String max_duration_in_sec
			, String launch_period_in_sec, String control_time_delay_in_sec) throws Exception {
		// System.out.println("-- DEBUG -- exec_os_script()");
		TrailSshKey trail = null;
			trail = new TrailSshKey(run_agent, trail_key, description, host, port, user, script,
					max_duration_in_sec, launch_period_in_sec, control_time_delay_in_sec);
		return trail;
	}


	
	public TrailBase new_trail_sql(String trail_key)
			throws Exception {
		return new_trail_sql("",trail_key, ""
				, "", "", "", "","0", "0", "0");
	}

	public TrailBase new_trail_sql(String run_agent, String trail_key, String description
			, String _script, String _jdbc_url, String _jdbc_user, String _jdbc_auth
			, String max_duration_in_sec, String launch_period_in_sec, String control_time_delay_in_sec)
			throws Exception {
		// System.out.println("-- DEBUG -- exec_os_script()");
		TrailSql trail = null;
		trail = new TrailSql(run_agent, trail_key, description
				, _script,  _jdbc_url,  _jdbc_user,  _jdbc_auth
				, max_duration_in_sec, launch_period_in_sec, control_time_delay_in_sec);
		return trail;
	}
	

	public TrailBase new_trail_ssh_pwd(String trail_key)
			throws Exception {
		return new_trail_ssh_pwd("",trail_key, "", "", "", "",
				"", "", "0", "0", "0");
	}

	public TrailBase new_trail_ssh_pwd(String run_agent, String trail_key, String description
				, String host, String port, String user,
			String password, String script, String max_duration_in_sec
			, String launch_period_in_sec, String control_time_delay_in_sec)
			throws Exception {
		// System.out.println("-- DEBUG -- exec_os_script()");
		TrailSshPwd trail = null;
		trail = new TrailSshPwd(run_agent, trail_key, description, host, port, user, password,
					script, max_duration_in_sec, launch_period_in_sec, control_time_delay_in_sec);
		return trail;
	}
	
	public TrailBase new_trail_tcp_port(String trail_key)
			throws Exception {
		return new_trail_tcp_port("",trail_key, "", "", ""
				, "0", "0", "0");
	}

	public TrailBase new_trail_tcp_port(String run_agent, String trail_key, String description, String _server, String port,
			String max_duration_in_sec
			, String launch_period_in_sec, String control_time_delay_in_sec)
			throws Exception {
		// System.out.println("-- DEBUG -- exec_os_script()");
		TrailTcpPort trail = null;
		trail = new TrailTcpPort(run_agent, trail_key, description, _server, port
				, max_duration_in_sec, launch_period_in_sec, control_time_delay_in_sec);
		return trail;
	}
	
	public TrailBase new_trail_http(String trail_key)
			throws Exception {
		TrailHttp trail = null;
		trail = new TrailHttp(trail_key);
		return trail;
	}
	
	public void do_trail(TrailBase trail) throws Exception {
		do_runnable(trail, trail.getDuration());
		if(trail.getStatus()!=Constants.ST_SUCCESS) {
			throw new Exception(trail.getRetErrStr());
		}
	}
	
	protected void do_runnable(TrailBase trail, int duration) { // throws Exception {
		Thread thread = new Thread(trail);
		trail.setStatusRunning();
		try {
			thread.start();
			for (int cur_duration = 0; cur_duration < duration * 1000; cur_duration += 100) {
				Thread.sleep(1 * 100); // Приостанавливает поток на 100 милисек TODO: ВЫНЕСТИ В НАСТРОЙКИ!
				if (!thread.isAlive()) {
//					System.out.println(" -- status ??  -- "+trail.getStatus());
					return;
				}
			}
			if (thread.isAlive()) {
				String str = "Превышено время исполнения.";
				trail.addRetErrStr(str);
				thread.interrupt();
				trail.setStatusError();
				//throw new Exception(str);
				return;
			}
		} catch (InterruptedException e) {
			// System.err.println("Возникло исключение 'Прерывание исполнения' в главном потоке.");
			String str = "Возникло исключение 'Прерывание исполнения' в главном потоке.";
			// addRetErrStr(str);
			if (thread.isAlive())
				thread.interrupt();
			trail.addRetErrStr(str);
			trail.setStatusError();
			//throw new Exception(str);
			return;
		} catch (Exception e) {
			// System.err.println("Возникло исключение 'Прерывание исполнения' в главном потоке.");
			String str = "Возникло исключение '"+e.getMessage()+"' в потоке выполнения ОП.";
			if (thread.isAlive())
				thread.interrupt();
			trail.addRetErrStr(str);
			trail.setStatusError();
			//throw new Exception(str);
			return;
		}
		
		// System.out.println(" Из главного потока. " + trail.to_string());
	}

}