package cb.dfs.trail;

import java.sql.Connection;
import java.util.Date;
//import java.sql.Date;

//import org.apache.log4j.Logger;

public class TrailManagerSubro2 extends TrailManager {

	//final static Logger logger = //logger.getLogger(TrailManagerSubro2.class);
	
	
	public TrailManagerSubro2(Connection _external_connection) {
		super(_external_connection);
	}
	
	
	public TrailManagerSubro2(String url, String user, String password)
			throws Exception {
		super(url, user, password);
	}
	
	
	/*
	 *  Возвращает: true если был запуск, иначе false
	 */ 
	public boolean launch_trail_if_ready(TrailBase trail, String scenario_name,
			int odi_session_id) throws Exception {
		//logger.debug("-- before getSydateFromMaster() in launch_trail_if_ready() ");
		//Date dt = getSydateFromMaster();
		Date dt = new Date();
		if(dt==null) {
			//logger.error("Ошибка! getSydateFromMaster вернул null.");
			return false;
		}	
		//logger.debug("-- after  getSydateFromMaster() in launch_trail_if_ready() ");
		//System.out.println("-- DEBUG -- Check ready to start in launch_trail_if_ready() ");
		if(trail==null) {
			//logger.error("Ошибка! trail is null.");
			return false;
		}
		try {	
			if(trail.is_ready_to_start(dt)) {
				//logger.debug("-- before do_trail_subro(). ");
				//System.out.println("-- DEBUG -- Redy to start. ");
				do_trail_subro(trail, scenario_name, odi_session_id);
				//logger.debug("-- after do_trail_subro(). ");
				return true;
			} else {
				return false;
			}
		} catch(Exception ex) {
			//logger.error("-- Ошибка в launch_trail_if_ready() trail.is_ready_to_start(dt) " + ex.getMessage());
			return false;
			//throw new Exception(ex.getMessage());
		}
			
	}

	public void launch_trail_if_ready(String trail_key, String scenario_name,
			int odi_session_id) throws Exception {
			//logger.debug("-- before read(trail_key) --");
			TrailBase trail = read(trail_key);
			//logger.debug("-- after read(trail_key) --");
			launch_trail_if_ready(trail, scenario_name, odi_session_id);
	}

	/*
	 * 
	 */
//	public void do_trail_subro(String trail_key, String scenario_name,
//			int odi_session_id) throws Exception {
//		TrailBase trail = read(trail_key);
//		do_trail_subro(trail, scenario_name, odi_session_id);
//	}
	
	
	/*
	 * TODO: Сделать метод protected. Все запуски должны идти через launch_trail_if_ready()
	 */
	public void do_trail_subro(TrailBase trail, String scenario_name,
			int odi_session_id) throws Exception {
		//logger.debug("-- do_trail_subro 1 -- ");
		trail.setStatusRunning();
		//logger.debug("-- do_trail_subro 2 -- ");
		trail.clearRetStrs();
		//logger.debug("-- do_trail_subro 3 -- ");
		trail.add_observable_object(getConnection(), scenario_name, odi_session_id);
		//logger.debug("-- do_trail_subro 4 -- ");
		trail.overwrite(getConnection());
		//logger.debug("-- do_trail_subro 5 -- ");
		getConnection().commit();
		try {
			//logger.debug("-- do_trail_subro 6 -- ");
			do_trail(trail);
			//logger.debug("-- do_trail_subro 7 -- ");
		} catch(Exception ex) {
			throw new Exception(ex.getMessage());
		} finally {
			//logger.debug("-- do_trail_subro 8 -- ");
			trail.overwrite(getConnection());
			//logger.debug("-- do_trail_subro 9 -- ");
			trail.add_observable_object(getConnection(), scenario_name, odi_session_id);
			//logger.debug("-- do_trail_subro 10 -- ");
			getConnection().commit();
		}
	}

	
	public static void main(String[] args) {
		if(args.length==0) {
			System.out.println(" Usage: java -jar cb_dfs_trail_ver_*** <trail_key> <url> <user> <pwd>");
			return;
		}
		TrailManagerSubro2 tm = null;
		try {
			tm = new TrailManagerSubro2(args[1], args[2], args[3]);
			//logger.debug("-- tm -- :"+tm);
		} catch(Exception e) {
			System.err.println("Error:"+e.getMessage());
			return;
		}
		try {
			tm.launch_trail_if_ready(args[0], "scenario_name", (int)(Math.random() * 10000));
			System.out.println("-- Выполнено. ");
		} catch(Exception e) {
			System.err.println("Error in launch_trail_if_ready():"+e.getMessage());
		}
			
		TrailBase tb = null;	
		try {	
			tb = tm.read(args[0]);
		} catch(Exception e) {
			System.err.println("Error:"+e.getMessage());
			return;
		}

		if(tb instanceof TrailHttp) {
			System.out.println(" Body: "+((TrailHttp) tb).getBody());
			System.out.println(" Cookies: "+((TrailHttp) tb).getCookies());
		}
		System.out.println(" Status: "+tb.getStatus()); 
		System.out.println(" Out msg: "+tb.getRetOutStr()); 
		System.out.println(" Err msg: "+tb.getRetErrStr()); 
		try {
			tm.getConnection().close();
		} catch(Exception e) {
			System.err.println("Error:"+e.getMessage());
			return;
		}
	}
	
	
}