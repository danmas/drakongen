package cb.dfs.trail.JuTests;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cb.dfs.trail.TrailBase;
import cb.dfs.trail.TrailHttp;
import cb.dfs.trail.TrailManagerSubro;
import cb.dfs.trail.common.Constants;

public class JuHttpTrailSubro {
	TrailManagerSubro trailSubroManager = null;
	
	// String urlString = "http://www.proza.ru/cgi-bin/login/intro.pl";
	//String log_str = "<input type=\"password\"";
	//String url_authString = "http://www.proza.ru/cgi-bin/login/intro.pl?&login=danmastest&password=2241401281";
	//String login_strings = "<form method=\"POST\" action=\"/cgi-bin/login/intro.pl\""
	//		+Constants.HTTP_BADSTRINGS_DELIMITER+"Bad string";

	
	String urlString = "http://vm-ocdm-demo.srv.rdtex.ru:9704/analytics/saw.dll?Dashboard&PortalPath=%2Fshared%2FOCDM%203.2-Dashboards%2FProduct%20Management%2FProduct%20Performance&page=Product%20Performance";
	String log_str = "<input id=\"idlogon\" type=\"submit\" class=\"button\"";
	String req_props = "User-agent=Mozilla/5.0 (Windows NT 6.1; WOW64;rv:26.0) Gecko/20100101 Firefox/26.0";
	String bad_str = "Your browser is not supported by Oracle BI Presentation Services.";
	
	/*
	@Test
	public void test_do_trail_subro_http() throws Exception {
		assert(trailSubroManager != null);

		TrailBase trail = null;
		//String urlString = "http://www.proza.ru/cgi-bin/login/intro.pl";
		//String urlString = "http://www.proza.ru/cgi-bin/login/intro.pl?&login=bestelesny&password=uiaait_5";
		//-- создаем новую ОП
		String trail_key = "G_HTTP_AUTOTEST_1";
		String login_strings = log_str;
		
		try {
			trail = new TrailHttp("RDAgent", trail_key, "Внутренний тест"
					, urlString, null
					//, "http-proxy.srv.rdtex.ru", "3128", "roman_eremeev", "xxx"
					//, "192.168.11.2", "3128", null, null
					, null, null, null, null
					, null, req_props, bad_str, login_strings, true, "120", "100", "0");
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
		assert(trailSubroManager.getConnection() != null);
		assert(trail != null);
		// -- сохраняем в БД
		try {
			trail.overwrite(trailSubroManager.getConnection());
			trailSubroManager.getConnection().commit();
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
		
		//-- выполняем проверку
		try {
			//trailSubroManager.do_trail_subro(trail, "scenario_name", (int)(Math.random() * 10000));
			trailSubroManager.launch_trail_if_ready(trail, "scenario_name", (int)(Math.random() * 10000));
			trail.overwrite(trailSubroManager.getConnection());
			trailSubroManager.getConnection().commit();
		} catch (Exception ex) {
			  try{ trailSubroManager.getConnection().rollback(); } catch(Exception ex2) {}
			  fail("Выполнение операции проверки прервано." + "\n" + ex.getMessage());
		} finally {
			  //! try{ trailSubroManager.getConnection().close();} catch(Exception ex2) {}  
		}
	}
	 */
	/*
	@Test
	public void test_do_trail_subro_http_3() throws Exception {
		assert(trailSubroManager != null);

		TrailBase trail = null;
		String urlString = "http://www.proza.ru/cgi-bin/login/intro.pl";
		String urlString_auth = "http://www.proza.ru/cgi-bin/login/intro.pl?&login=bestelesny&password=uiaait_5";
		String log_str = "<input type=\"password\"";
		
		//-- создаем новую ОП
		String trail_key = "G_HTTP_AUTOTEST_2";
		String login_strings = log_str;
		
		try {
			trail = new TrailHttp("RDAgent", trail_key, "Внутренний тест"
					, urlString, urlString_auth
					//, "http-proxy.srv.rdtex.ru", "3128", "roman_eremeev", "xxx"
					, "192.168.11.2", "3128", null, null
					//, null, null, null, null
					, null, req_props, bad_str, login_strings, true, "120", "100", "0");
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
		assert(trailSubroManager.getConnection() != null);
		assert(trail != null);
		// -- сохраняем в БД
		try {
			trail.overwrite(trailSubroManager.getConnection());
			trailSubroManager.getConnection().commit();
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
		
		//-- выполняем проверку
		try {
			//trailSubroManager.do_trail_subro(trail, "scenario_name", (int)(Math.random() * 10000));
			trailSubroManager.launch_trail_if_ready(trail, "scenario_name", (int)(Math.random() * 10000));
			trail.overwrite(trailSubroManager.getConnection());
			trailSubroManager.getConnection().commit();
		} catch (Exception ex) {
			  try{ trailSubroManager.getConnection().rollback(); } catch(Exception ex2) {}
			  fail("Выполнение операции проверки прервано." + "\n" + ex.getMessage());
		} finally {
			  //! try{ trailSubroManager.getConnection().close();} catch(Exception ex2) {}  
		}
	}
	
	
	@Test
	public void test_do_trail_subro_http_2()  throws Exception  {
		assert(trailSubroManager != null);

		TrailBase trail = null;
		
		//-- создаем новую ОП
		String trail_key = "(G) Проверка HTTP на http://softlib-p.srv.rdtex.ru/ время отклика 5 сек";
		try {
			trail = new TrailHttp("erv_local", trail_key, "Авто тест "+trail_key
					, "http://softlib-p.srv.rdtex.ru/", null, null, null, null, null, true
					, "1", "100","0");
			//"http-proxy.srv.rdtex.ru", "3128", 
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		assert(trailSubroManager.getConnection() != null);
		assert(trail != null);

		// -- сохраняем в БД
		try {
			trail.overwrite(trailSubroManager.getConnection());
			trailSubroManager.getConnection().commit();
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
		
		//-- выполняем проверку
		try {
			//trailSubroManager.do_trail_subro(trail, "scenario_name", (int)(Math.random() * 10000));
			trailSubroManager.launch_trail_if_ready(trail, "scenario_name", (int)(Math.random() * 10000));
			trail.overwrite(trailSubroManager.getConnection());
			trailSubroManager.getConnection().commit();
		} catch (Exception ex) {
			  try{ trailSubroManager.getConnection().rollback(); } catch(Exception ex2) {}
			  fail("Выполнение операции проверки прервано." + "\n" + ex.getMessage());
		} finally {
			  //! try{ trailSubroManager.getConnection().close();} catch(Exception ex2) {}  
		}
	}
	*/
	
	
	@Before
	public void prepareTests() {
		try {
		  trailSubroManager = new TrailManagerSubro(Constants.test_url
				  , Constants.test_user, Constants.test_pwd);
		  assert(trailSubroManager!=null);
		} catch(Exception ex) {
		  String str = "Не удалось создать TrailSubroManager и установить коннекцию к БД";
		  fail(str + "\n" + ex.getMessage()); 
		}
		assert(trailSubroManager != null);
	}

	@After
	public void afterTests() {
		assert(trailSubroManager != null);

		try {
			trailSubroManager.getConnection().commit();
			trailSubroManager.getConnection().close();
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}
	
}
