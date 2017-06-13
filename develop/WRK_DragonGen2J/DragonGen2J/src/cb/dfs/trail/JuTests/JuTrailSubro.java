package cb.dfs.trail.JuTests;

import static org.junit.Assert.fail;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cb.dfs.trail.TrailBase;
import cb.dfs.trail.TrailHttp;
import cb.dfs.trail.TrailManagerSubro;
import cb.dfs.trail.TrailOsScript;
import cb.dfs.trail.TrailSshKey;
import cb.dfs.trail.TrailTcpPort;
import cb.dfs.trail.common.Constants;

/**
 * Тесты проверяют выполнение как в ODI mon_trail_subro 
 * 
 * 
 * @author roman_eremeev
 *
 */
public class JuTrailSubro {

	TrailManagerSubro trailSubroManager = null;
	
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
	
	
	@Test
	public void test_do_trail_subro_tcp_port_B()  throws Exception {
		assert(trailSubroManager != null);

		Connection conn = trailSubroManager.getConnection();
		TrailBase trail = null;
		
		//-- создаем новую ОП
		String trail_key = "(B) RDAgent Проверка TCP_PORT на xxxxxxx:3128 ";
		try {
			trail = new TrailTcpPort(trail_key);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		assert(conn != null);
		assert(trail != null);

		// -- сохраняем в БД
		try {
			trail.read(conn);
			conn.commit();
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
		
		//-- выполняем проверку
		try {
			trailSubroManager.do_trail_subro(trail, "scenario_name", (int)(Math.random() * 10000));
		} catch (Exception ex) {
			  try{ trailSubroManager.getConnection().rollback(); } catch(Exception ex2) {}
			  fail("Выполнение операции проверки прервано." + "\n" + ex.getMessage());
		} finally {
			  //! try{ trailSubroManager.getConnection().close();} catch(Exception ex2) {}  
		}
	}
	
	/*
	//
	// Проверка do_trail_subro
	//
	@Test
	public void test_do_trail_subro_os_script()  throws Exception {
		assert(trailSubroManager != null);

		TrailOsScript trail = null;
		
		//-- создаем новую ОП
		String trail_key = "(G) Проверка OS_SCRIPT ping -n 3 http-proxy.srv.rdtex.ru";
		try {
			trail = new TrailOsScript("RDAgent", trail_key, "Авто тест "+ trail_key,
					"ping -n 3 http-proxy.srv.rdtex.ru", "100", "11","0");
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
			trailSubroManager.do_trail_subro(trail, "scenario_name", (int)(Math.random() * 10000));
		} catch (Exception ex) {
			  try{ trailSubroManager.getConnection().rollback(); } catch(Exception ex2) {}
			  fail("Выполнение операции проверки прервано." + "\n" + ex.getMessage());
		} finally {
			  try{ trailSubroManager.getConnection().close();} catch(Exception ex2) {}  
		}
	}
	
	//
	// Проверка do_trail_subro TCP_PORT
	//
	
	@Test
	public void test_do_trail_subro_tcp_port()  throws Exception {
		assert(trailSubroManager != null);

		TrailBase trail = null;
		
		//-- создаем новую ОП
		String trail_key = "(G) Проверка TCP_PORT на http-proxy.srv.rdtex.ru:3128 ";
		try {
			trail = new TrailTcpPort("erv_local", trail_key, "Авто тест "+trail_key
					,"http-proxy.srv.rdtex.ru", "3128", "500", "100","0");
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
			trailSubroManager.do_trail_subro(trail, "scenario_name", (int)(Math.random() * 10000));
		} catch (Exception ex) {
			  try{ trailSubroManager.getConnection().rollback(); } catch(Exception ex2) {}
			  fail("Выполнение операции проверки прервано." + "\n" + ex.getMessage());
		} finally {
			  //! try{ trailSubroManager.getConnection().close();} catch(Exception ex2) {}  
		}
	}
	
	
	//
	// Проверка do_trail_subro TCP_PORT
	//
	
	@Test
	public void test_do_trail_subro_tcp_port_3()  throws Exception {
		assert(trailSubroManager != null);

		TrailBase trail = null;
		
		//-- создаем новую ОП
		String trail_key = "(G) RDAgent Проверка TCP_PORT на http-proxy.srv.rdtex.ru:3128 ";
		try {
			trail = new TrailTcpPort("RDAgent", trail_key, "Авто тест "+trail_key
					,"http-proxy.srv.rdtex.ru", "3128", "500", "100","0");
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
			trailSubroManager.do_trail_subro(trail, "scenario_name", (int)(Math.random() * 10000));
		} catch (Exception ex) {
			  try{ trailSubroManager.getConnection().rollback(); } catch(Exception ex2) {}
			  fail("Выполнение операции проверки прервано." + "\n" + ex.getMessage());
		} finally {
			  //! try{ trailSubroManager.getConnection().close();} catch(Exception ex2) {}  
		}
	}
	
	
	@Test
	public void test_do_trail_subro_http()  throws Exception  {
		assert(trailSubroManager != null);

		TrailBase trail = null;
		
		//-- создаем новую ОП
		String trail_key = "(G) Проверка HTTP на http://softlib-p.srv.rdtex.ru/ время отклика 5 сек";
		try {
			trail = new TrailHttp("erv_local", trail_key, "Авто тест "+trail_key
					, "http://softlib-p.srv.rdtex.ru/", null, null, null, null, true
					, "5", "100","0");
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
			trailSubroManager.do_trail_subro(trail, "scenario_name", (int)(Math.random() * 10000));
		} catch (Exception ex) {
			  try{ trailSubroManager.getConnection().rollback(); } catch(Exception ex2) {}
			  fail("Выполнение операции проверки прервано." + "\n" + ex.getMessage());
		} finally {
			  //! try{ trailSubroManager.getConnection().close();} catch(Exception ex2) {}  
		}
	}

	
	@Test
	public void test_launch_trail_if_ready_subro_http()  throws Exception {
		assert(trailSubroManager != null);

		TrailBase trail = null;
		
		//-- создаем новую ОП
		String trail_key = "(G) Проверка launch_trail_if_ready() HTTP на http://softlib-p.srv.rdtex.ru/ время отклика 5 сек, период 10 сек.";
		try {
			trail = new TrailHttp("erv_local", trail_key, "Авто тест "+trail_key
					, "http://softlib-p.srv.rdtex.ru/", null, null, null, null, true
					, "5", "10","0");
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
		
		//-- выполняем проверку (должна стартануть т.к. last_start_date = null)
		try {
			boolean ret = trailSubroManager.launch_trail_if_ready(trail, "scenario_name", (int)(Math.random() * 10000));
			if(!ret) {
				fail("Не произошел запуск выполнения операции проверки! А должно бы т.к. last_start_date = null.");
			}
		} catch (Exception ex) {
			  try{ trailSubroManager.getConnection().rollback(); } catch(Exception ex2) {}
			  fail("Выполнение операции проверки прервано." + "\n" + ex.getMessage());
		}
		//-- спим 14 сек
		try { Thread.sleep(14000); } catch(Exception ex) {}
		
		//-- выполняем проверку (должна стартануть т.к. прошло больше 10 сек.)
		try {
			boolean ret = trailSubroManager.launch_trail_if_ready(trail, "scenario_name", (int)(Math.random() * 10000));
			if(!ret) {
				fail("Не произошел запуск выполнения операции проверки! А должно бы т.к. прошло больше 10 сек.");
			}
		} catch (Exception ex) {
			  try{ trailSubroManager.getConnection().rollback(); } catch(Exception ex2) {}
			  fail("Выполнение операции проверки прервано." + "\n" + ex.getMessage());
		}
	}


	
	@Test
	public void test_do_trail_subro_tcp_port_2()  throws Exception {
		assert(trailSubroManager != null);

		TrailBase trail = null;
		
		//-- создаем новую ОП
		String trail_key = "(W) Проверка связи с МСТПРО прокси сервером TCP_PORT на 192.168.11.2:3128 ";
		try {
			trail = new TrailTcpPort("RDAgent", trail_key, "Авто тест "+trail_key
					,"192.168.11.2", "3128", "5", "10","0");
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
			trailSubroManager.do_trail_subro(trail, "scenario_name", (int)(Math.random() * 10000));
		} catch (Exception ex) {
			  try{ trailSubroManager.getConnection().rollback(); } catch(Exception ex2) {}
			  fail("Выполнение операции проверки прервано." + "\n" + ex.getMessage());
		} finally {
			  //! try{ trailSubroManager.getConnection().close();} catch(Exception ex2) {}  
		}
	}
	
	
	@Test
	public void test_do_trail_subro_ssh_key()  throws Exception {
		assert(trailSubroManager != null);

		TrailBase trail = null;
		
		//-- создаем новую ОП
		String trail_key = "(G) Проверка SSH KEY ssh oracle@vm-cb2-bi.vm-p.rdtex.ru ls";
		try {
			trail = new TrailSshKey("RDAgentVM3", trail_key, "Авто тест "+trail_key
					, "vm-cb2-bi.vm-p.rdtex.ru", "22", "oracle", "hostname\nls\n" 
					, "5", "100","0");
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
			trailSubroManager.do_trail_subro(trail, "scenario_name", (int)(Math.random() * 10000));
		} catch (Exception ex) {
			  try{ trailSubroManager.getConnection().rollback(); } catch(Exception ex2) {}
			  fail("Выполнение операции проверки прервано." + "\n" + ex.getMessage());
		} finally {
			  //! try{ trailSubroManager.getConnection().close();} catch(Exception ex2) {}  
		}
	}
	*/
	
}
