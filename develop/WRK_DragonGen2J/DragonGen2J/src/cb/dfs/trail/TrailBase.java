package cb.dfs.trail;

import java.util.Date;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import cb.dfs.trail.common.Constants;
import cb.dfs.trail.common.Version;
import cb.dfs.trail.common.Versioned;
import cb.dfs.trail.utils.Strings;


@Version(major = Constants.CUR_VER_MAJOR, minor = Constants.CUR_VER_MINOR, revision = Constants.CUR_VER_REVIS)
public abstract class TrailBase extends Versioned implements Runnable {

	final static Logger logger = Logger.getLogger(TrailBase.class);

	protected volatile String retOutStr = "";
	protected volatile String retErrStr = "";
	protected volatile String status = Constants.ST_UNKNOWN;
	protected volatile int ob_object_id = 0; // -- Если != 0 то id контролируемого объекта в СУБРО

	protected String run_agent = null;
	protected String trail_key = null;
	protected String trail_type = null;
	protected String description = null;
	protected String max_duration_in_sec = null;
	protected String launch_period_in_sec = null;
	protected String control_time_delay_in_sec = null;
	protected Date last_start_date = null;

	int duration = 0;
	int launch_period = 0;
	int control_time_delay = 0;
	
	public static void main(String[] args) {
		System.out.println(" --- Main по-русски ");
	}
	
	boolean debug = false;
	
	
	public boolean isDebug() {
		return debug;
	}
	

	public void setDebugOn() {
		this.debug = true;
	}
	public void setDebugOff() {
		this.debug = false;
	}
	

	public void debug(String msg) {
		if(debug) {
			addRetOutStr("DEBUG - " + msg);
		}
	}
	 
	public void error(String msg) {
		//if(debug) {
			addRetOutStr("ERROR - " + msg);
		//}
	}
	 
	protected String getDebugInfo() {
		return this.getClass().getName()+":"+Thread.currentThread().getStackTrace()[2].getMethodName()+":"+
				Thread.currentThread().getStackTrace()[2].getLineNumber();
	}
	
	public TrailBase(String _trail_key) {
		trail_key = _trail_key;
	}
	
	public TrailBase(String _run_agent, String _trail_key, String _trail_type, String _description
			, String _max_duration_in_sec, String _launch_period_in_sec
			, String  _control_time_delay_in_sec) throws Exception {
		run_agent = _run_agent;
		trail_key = _trail_key;
		trail_type = _trail_type;
		max_duration_in_sec = _max_duration_in_sec;
		duration = getDuration(_max_duration_in_sec);
		description = _description;
		launch_period_in_sec = _launch_period_in_sec;
		launch_period = getLaunchPeriod(_launch_period_in_sec);
		control_time_delay_in_sec = _control_time_delay_in_sec;
		control_time_delay= getControlTimeDelay(control_time_delay_in_sec);
	}

	/*
	 * Получение параметров из JSON
	 * если в json не указан параметр то старый не переписыавется!
	 */
	public void updateParamsFromJ(JSONObject jo) throws Exception {
		if(jo.get("run_agent")!=null) {
			run_agent = (String)(jo.get("run_agent")); 
		}
		if(jo.get("trail_key")!=null) {
			trail_key = (String)(jo.get("trail_key")); 
		}
//		if(jo.get("trail_type")!=null) {
//			run_agent = (String)(jo.get("trail_type")); 
//		}
		if(jo.get("max_duration_in_sec")!=null) {
			max_duration_in_sec = (String)(jo.get("max_duration_in_sec")); 
			duration = getDuration(max_duration_in_sec);
		}
		if(jo.get("description")!=null) {
			description = (String)(jo.get("description")); 
		}
		if(jo.get("launch_period_in_sec")!=null) {
			launch_period_in_sec = (String)(jo.get("launch_period_in_sec")); 
			launch_period = getLaunchPeriod(launch_period_in_sec);
		}
		if(jo.get("control_time_delay_in_sec")!=null) {
			control_time_delay_in_sec = (String)(jo.get("control_time_delay_in_sec")); 
			control_time_delay= getControlTimeDelay(control_time_delay_in_sec);
		}
	}
	
	boolean is_ready_to_start(Date cur_time) {
		logger.debug("-- is_ready_to_start("+cur_time+") "); 
		if(last_start_date==null)
			return true;
		if(launch_period==0) {
			return false;
		}
		//System.out.println(last_start_date.getTime());
		boolean ret = cur_time.getTime() > last_start_date.getTime() + launch_period*1000;
		logger.debug("-- is_ready_to_start("+cur_time+") returns "+ret); 
		return ret;
	}
	
	public void setParam(String param, String value) throws Exception {
		switch(param.trim().toLowerCase()) {
		case "run_agent": run_agent = value; break;
		case "max_duration_in_sec": max_duration_in_sec = value;
			duration = getDuration(max_duration_in_sec);
			break;
		case "description": description = value; break;
		case "launch_period_in_sec": launch_period_in_sec = value;
			launch_period = getLaunchPeriod(launch_period_in_sec);
			break;
		case "control_time_delay_in_sec": control_time_delay_in_sec = value;
			control_time_delay = getControlTimeDelay(control_time_delay_in_sec);
			break;
				
		default:
			throw new Exception("Неправильно задано имя параметра <"+param+"> или значение <"+value+">"
					+ "\nИсключение в  cb.dfs.trail.TrailBase.setParam()");
		}
	}
	
	@Override
	public String toString() {
		return "TrailBase [retOutStr=" + retOutStr + ", retErrStr=" + retErrStr + ", status=" + status
				+ ", ob_object_id=" + ob_object_id + ", run_agent=" + run_agent + ", trail_key=" + trail_key
				+ ", trail_type=" + trail_type + ", description=" + description + ", max_duration_in_sec="
				+ max_duration_in_sec + ", launch_period_in_sec=" + launch_period_in_sec
				+ ", control_time_delay_in_sec=" + control_time_delay_in_sec + ", last_start_date=" + last_start_date
				+ "]";
	}
	
	public void setLastStartDate(Date dt) throws Exception {
		last_start_date = dt;
	}
	
	public abstract void run(); 

	/*
	 * Запуск в персональном TrailManager
	 */
	public void launch_single() {
		 try { 
			TrailManager trailManager = null;
			trailManager = new TrailManager();
			
			//trail.overwrite(trailSubroManager.getConnection());
			//trailSubroManager.getConnection().commit();
			trailManager.launch_trail_if_ready(this, "scenario_name", (int) (Math.random() * 1000.));
		} catch (Exception ex) {
			error("---" + ex.getMessage());
		}
	}

	public void mergeIntoDb() {

	}

	protected void setStatusError() {
		this.status = Constants.ST_ERROR;
	}

	protected void setStatusRunning() {
		this.status = Constants.ST_RUNNING;
	}

	protected void setStatusSuccess() {
		this.status = Constants.ST_SUCCESS;
	}

	public int getDuration() {
		return duration;
	}

	protected int getDuration(String max_duration_in_sec) throws Exception {
		int duration;
		try {
			duration = Strings.stringToPositivInt(max_duration_in_sec);
		} catch (Exception ex) {
			String str = "Неверно задано время выполнения в секундах <" + max_duration_in_sec + ">.";
			addRetErrStr(str);
			throw new Exception(str);
		}
		return duration;
	}

	protected int getLaunchPeriod(String launch_period_in_sec) throws Exception {
		int ival;
		try {
			ival = Strings.stringToPositivInt(launch_period_in_sec);
		} catch (Exception ex) {
			String str = "Неверно задан период повторного исполнения в секундах <" + launch_period_in_sec + ">.";
			addRetErrStr(str);
			throw new Exception(str);
		}
		return ival;
	}

	protected int getControlTimeDelay(String _control_time_delay_in_sec) throws Exception {
		int ival;
		try {
			ival = Strings.stringToPositivInt(_control_time_delay_in_sec);
		} catch (Exception ex) {
			String str = "Неверно задана задержка контроля запуска проверочной операции в секундах <" + _control_time_delay_in_sec + ">.";
			addRetErrStr(str);
			throw new Exception(str);
		}
		return ival;
	}
	
	protected int getPort(String sport) throws Exception {
		int iport;
		if(sport==null || sport.equals("")) {
			return 0;
		}
		try {
			iport = Strings.stringToPositivInt(sport);
		} catch (Exception ex) {
			String str = "Неверно задан номер порта <" + sport + ">.";
			addRetErrStr(str);
			throw new Exception(str);
		}
		return iport;
	}

	public String to_string() {
		return super.to_string() + " status: " + getStatus() + "\nerr:" + retErrStr + "\nout:\n" + retOutStr;
	}

	public String getRetOutStr() {
		return retOutStr;
	}

	// public void setRetOutStr(String retOutStr) {
	// retOutStr = retOutStr;
	// }

	public String getRetErrStr() {
		return retErrStr;
	}

	// public void setRetErrStr(String retErrStr) {
	// retErrStr = retErrStr;
	// }

	public void clearRetStrs() {
		retOutStr = ""; retErrStr = ""; 
	}
	
	public void addRetErrStr(String str) {
		retErrStr += str + "\n";
	}

	public void addRetOutStr(String str) {
		retOutStr += str + "\n";
	}

	public String getStatus() {
		return status;
	}

}
