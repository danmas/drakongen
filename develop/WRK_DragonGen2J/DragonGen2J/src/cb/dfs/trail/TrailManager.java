package cb.dfs.trail;

//import java.sql.Connection;
import java.util.Date;

//import java.sql.Connection;
//import java.sql.Date;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue; 

public class TrailManager extends TrailManagerBase {

	final static Logger logger = Logger.getLogger(TrailManagerBase.class);
	

	public TrailManager() {
		super();
	}
	
	/*
	public TrailManagerSubro(Connection _external_connection) {
		super(_external_connection);
	}
	
	
	public TrailManagerSubro(String url, String user, String password)
			throws Exception {
		super(url, user, password);
	}*/
	
	/*
	 *  Возвращает: true если был запуск, иначе false
	 */
	protected boolean launch_trail_if_ready(TrailBase trail, String scenario_name,
			int odi_session_id) throws Exception {
		logger.debug("-- before getSydateFromMaster() in launch_trail_if_ready() ");
		trail.debug(getDebugInfo()+"-- before getSydateFromMaster() in launch_trail_if_ready() ");
		Date dt = getSydateFromMaster(trail);
		if(dt==null) {
			logger.error("Ошибка! getSydateFromMaster вернул null.");
			trail.error(getDebugInfo()+"Ошибка! getSydateFromMaster вернул null.");
			return false;
		}	
		logger.debug("-- after  getSydateFromMaster() in launch_trail_if_ready() ");
		trail.debug(getDebugInfo()+"-- after  getSydateFromMaster() in launch_trail_if_ready() ");
		//System.out.println("-- DEBUG -- Check ready to start in launch_trail_if_ready() ");
		if(trail==null) {
			logger.error("Ошибка! trail is null.");
			trail.error(getDebugInfo()+"Ошибка! trail is null.");
			return false;
		}
		try {	
			if(trail.is_ready_to_start(dt)) {
				logger.debug("-- before do_trail_subro(). ");
				trail.debug(getDebugInfo()+"-- before do_trail_subro(). ");
				do_trail_subro(trail, scenario_name, odi_session_id);
				logger.debug("-- after do_trail_subro(). ");
				trail.debug(getDebugInfo()+"-- after do_trail_subro(). ");
				return true;
			} else {
				return false;
			}
		} catch(Exception ex) {
			logger.error("-- Ошибка в launch_trail_if_ready() trail.is_ready_to_start(dt) " + ex.getMessage());
			trail.error(getDebugInfo()+"-- Ошибка в launch_trail_if_ready() trail.is_ready_to_start(dt) " + ex.getMessage());
			return false;
			//throw new Exception(ex.getMessage());
		}
			
	}

	//!!! Специально протектед чтобы не вызывался метод предыдущей версии
	protected void launch_trail_if_ready(String trail_key, String scenario_name,
			int odi_session_id) throws Exception {
		launch_trail_if_ready(trail_key, scenario_name,
				odi_session_id, false);
	}
			
	
	public void launch_trail_if_ready(String trail_key, String scenario_name,
			int odi_session_id, boolean debug) throws Exception {
			logger.debug("-- before read(trail_key) --");
			TrailBase trail = read(trail_key);
			trail.clearRetStrs();
			if(debug) {
				trail.setDebugOn();
			}
			logger.debug("-- after read(trail_key) --");
			trail.debug(getDebugInfo()+"-- after read(trail_key) --");
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
		trail.debug(getDebugInfo()+"-- do_trail_subro 1 -- ");
		trail.setStatusRunning();
		//logger.debug("-- do_trail_subro 2 -- ");
		trail.debug(getDebugInfo()+"-- do_trail_subro 2 -- ");
		//!!! Убрал в launch_trail_if_ready чтобы не чистились строки отладки. 
		//trail.clearRetStrs();
		//logger.debug("-- do_trail_subro 3 -- ");
		trail.debug(getDebugInfo()+"-- do_trail_subro 3 -- ");
		//trail.add_observable_object(getConnection(), scenario_name, odi_session_id);
		//logger.debug("-- do_trail_subro 4 -- ");
		trail.debug(getDebugInfo()+"-- do_trail_subro 4 -- ");
		//trail.overwrite(getConnection());
		//logger.debug("-- do_trail_subro 5 -- ");
		trail.debug(getDebugInfo()+"-- do_trail_subro 5 -- ");
		//getConnection().commit();
		try {
			//logger.debug("-- do_trail_subro 6 -- ");
			trail.debug(getDebugInfo()+"-- do_trail_subro 6 -- ");
			do_trail(trail);
			//logger.debug("-- do_trail_subro 7 -- ");
			trail.debug(getDebugInfo()+"-- do_trail_subro 7 -- ");
		} catch(Exception ex) {
			throw new Exception(ex.getMessage());
		} finally {
			//logger.debug("-- do_trail_subro 8 -- ");
			trail.debug(getDebugInfo()+"-- do_trail_subro 8 -- ");
			//trail.overwrite(getConnection());
			//logger.debug("-- do_trail_subro 9 -- ");
			trail.debug(getDebugInfo()+"-- do_trail_subro 9 -- ");
			//trail.add_observable_object(getConnection(), scenario_name, odi_session_id);
			//logger.debug("-- do_trail_subro 10 -- ");
			trail.debug(getDebugInfo()+"-- do_trail_subro 10 -- ");
			//getConnection().commit();
		}
	}

	public String  launchTrailFromJstring(String js) {
//    	String s =" {\"type\":\"TT_OS_SCRIPT\",\"script\":\"ping -n 3 http-proxy.srv.rdtex.ru\""
//    			+ ",\"description\":\"Тест выполнения скрипта ОС\",\"max_duration_in_sec\":\"5\""
//    			+ ",\"launch_period_in_sec\":\"5\"}";
    	try {
            JSONObject jo = (JSONObject) JSONValue.parseWithException(js);
            
            TrailBase trail = read((String)(jo.get("type")));
            trail.updateParamsFromJ(jo);
			System.out.println("------------- before  launch_trail_if_ready() ");
			launch_trail_if_ready(trail, "scenario_name", (int) (Math.random() * 1000.));
			System.out.println("------------- after  launch_trail_if_ready() ");
			return trail.getRetOutStr();
        } catch (Exception e) {
            e.printStackTrace();
        }
    	return "";
	}
	
	public static void main(String[] args) {
    	String s =" {\"type\":\"TT_OS_SCRIPT\",\"script\":\"ping -n 3 carlhost\""
    			+ ",\"description\":\"Тест выполнения скрипта ОС\",\"max_duration_in_sec\":\"5\""
    			+ ",\"launch_period_in_sec\":\"5\"}";
		
	 cb.dfs.trail.TrailManager trail = new cb.dfs.trail.TrailManager();
	trail.launchTrailFromJstring(s);
	System.out.println(trail.to_string());
	}
	
	public static void main2(String[] args) {
		if(args.length==0) {
			System.out.println(" Usage: java -jar cb_dfs_trail_ver_*** <trail_key> <url> <user> <pwd>");
			return;
		}
		TrailManager tm = null;
		try {
			//tm = new TrailManagerSubro(args[1], args[2], args[3]);
			tm = new TrailManager();
			logger.debug("-- tm -- :"+tm);
		} catch(Exception e) {
			System.err.println("Error:"+e.getMessage());
			return;
		}
		try {
			tm.launch_trail_if_ready(args[0], "scenario_name", (int)(Math.random() * 10000),true);
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
//		try {
//			tm.getConnection().close();
//		} catch(Exception e) {
//			System.err.println("Error:"+e.getMessage());
//			return;
//		}
	}
	
	
}
