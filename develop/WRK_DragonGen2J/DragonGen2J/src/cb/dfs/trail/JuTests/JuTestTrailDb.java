package cb.dfs.trail.JuTests;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import cb.dfs.trail.TrailBase;
import cb.dfs.trail.TrailManager;
import cb.dfs.trail.TrailOsScript;
import cb.dfs.trail.TrailSshKey;
import cb.dfs.trail.TrailSshPwd;
import cb.dfs.trail.common.Constants;

public class JuTestTrailDb {
	TrailManager trailManager = null;
 
	@Rule
	public final Timeout timeout = new Timeout(10000000);

	/*
	 * Удаляем все ОПы!
	 */
	@Before
	public void prepareTests() {
		try {
			trailManager = new TrailManager(Constants.test_url, Constants.test_user,
					Constants.test_pwd);
			// -- удаляем все предыдущие ОПы
			//trailManager.deleteAll();
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
		assert(trailManager != null);
	}

	@After
	public void afterTests() {
		assert(trailManager != null);

		try {
			trailManager.getConnection().commit();
			//trailManager.getConnection().close();
			
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	// @Ignore
	@Test
	public void testRWDbSshPwd()  throws Exception  {
		assert(trailManager != null);

		TrailSshPwd trail = null;

		try {
			trail = new TrailSshPwd("AGENT_0", "JU_TEST_SSH_PWD_0", "Авто тест JU_TEST_SSH_PWD_0",
					"vm-cb2-bi.vm-p.rdtex.ru", "22", "oracle", "or_dbms", "ping -c 3 http-proxy.srv.rdtex.ru", "100",
					"0","0");
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		assert(trailManager.getConnection() != null);
		assert(trail != null);

		String old = trail.toString(); // -- сохраняем
		// в БД
		try {
			trail.overwrite(trailManager.getConnection());
			trailManager.getConnection().commit();
		} catch (Exception ex) {
			try {
				trailManager.getConnection().commit();
			} catch (Exception ex2) {
			}
			fail(ex.getMessage());
		}

		// -- читаем из БД
		try {
			trail.read(trailManager.getConnection());
		} catch (Exception ex) {
			System.err.println(trail.to_string());
			fail(ex.getMessage());
		} // -- сравниваем результаты
		if (!old.equals(trail.toString())) {
			System.out.println("---old != new --------");
			System.out.println(old);
			System.out.println("---new----------------");
			System.out.println(trail.toString());
		}
	}

	// @Ignore

	@Test
	public void testRWDbOsScript_2()  throws Exception  {
		assert(trailManager != null);

		TrailBase trail = null;

		try {
			trail = trailManager.new_trail_os_script("AGENT_0", "JU_TEST_OS_SCRIPT_0", "Авто тест JU_TEST_OS_SCRIPT_0",
					"ping -c 3 http-proxy.srv.rdtex.ru", "10", "0","0");
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		assert(trailManager.getConnection() != null);
		assert(trail != null);

		String old = trail.toString(); // -- сохраняем // в БД
		try {
			trail.overwrite(trailManager.getConnection());
			trailManager.getConnection().commit();
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		// -- читаем из БД
		try {
			trail.read(trailManager.getConnection());
		} catch (Exception ex) {
			System.err.println(trail.to_string());
			fail(ex.getMessage());
		} // -- сравниваем результаты
		if (!old.equals(trail.toString())) {
			System.out.println("---old != new --------");
			System.out.println(old);
			System.out.println("---new----------------");
			System.out.println(trail.toString());
		}
	}

	// @Ignore

	@Test
	public void testRWDbOsScript()  throws Exception {
		assert(trailManager != null);

		TrailOsScript trail = null;

		try {
			trail = new TrailOsScript("AGENT_0", "JU_TEST_OS_SCRIPT_0", "Авто тест JU_TEST_OS_SCRIPT_0",
					"ping -c 3 http-proxy.srv.rdtex.ru", "10", "0","0");
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		assert(trailManager.getConnection() != null);
		assert(trail != null);

		String old = trail.toString();
		// -- сохраняем в БД
		try {
			trail.overwrite(trailManager.getConnection());
			trailManager.getConnection().commit();
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		// -- читаем из БД
		try {
			trail.read(trailManager.getConnection());
		} catch (Exception ex) {
			System.err.println(trail.to_string());
			fail(ex.getMessage());
		} // -- сравниваем результаты
		if (!old.equals(trail.toString())) {
			System.out.println("---old != new --------");
			System.out.println(old);
			System.out.println("---new----------------");
			System.out.println(trail.toString());
		}
	}

	// @Ignore

	@Test
	public void testRWDbSshKey()  throws Exception {
		assert(trailManager != null);

		TrailSshKey trail = null;

		try {
			trail = new TrailSshKey("AGENT_0", "JU_TEST_SSH_KEY_0", "Авто тест JU_TEST_SSH_KEY_0",
					"vm-cb2-bi.vm-p.rdtex.ru", "22", "oracle", "ping -c 3 http-proxy.srv.rdtex.ru"
					, "100", "0","0");
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		assert(trailManager.getConnection() != null);
		assert(trail != null);

		String old = trail.toString();
		// -- сохраняем в БД
		try {
			trail.overwrite(trailManager.getConnection());
			trailManager.getConnection().commit();
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		// -- читаем из БД
		try {
			trail.read(trailManager.getConnection());
		} catch (Exception ex) {
			System.err.println(trail.to_string());
			fail(ex.getMessage());
		} // -- сравниваем результаты
		if (!old.equals(trail.toString())) {
			System.out.println("---old != new --------");
			System.out.println(old);
			System.out.println("---new----------------");
			System.out.println(trail.toString());
		}
	}

	@Test
	public void testRWDbOsScript_3()  throws Exception {
		assert(trailManager != null);

		TrailOsScript trail = null;

		try {
			trail = new TrailOsScript("AGENT_0", "JU_TEST_OS_SCRIPT_0", "Авто тест JU_TEST_OS_SCRIPT_0",
					"ping -c 3 http-proxy.srv.rdtex.ru", "10", "11","0");
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		assert(trailManager.getConnection() != null);
		assert(trail != null);

		String old = trail.toString(); // -- сохраняем в БД
		try {
			trail.overwrite(trailManager.getConnection());
			trailManager.getConnection().commit();
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		TrailBase trail2 = null;

		// -- читаем из БД
		try {
			trail2 = trailManager.read("JU_TEST_OS_SCRIPT_0");
		} catch (Exception ex) {
			System.err.println(trail.to_string());
			fail(ex.getMessage());
		} // -- сравниваем результаты
		if (!old.equals(trail2.toString())) {
			System.out.println("---old != new --------");
			System.out.println(old);
			System.out.println("---new----------------");
			System.out.println(trail2.toString());
		} else {
			System.out.println("---new----------------");
			System.out.println(trail2.toString());
		}
	} //

	// Проверка test_add_observable_object //
	@Test
	public void test_add_observable_object() throws Exception {
		assert(trailManager != null);

		TrailOsScript trail = null;

		try {
			trail = new TrailOsScript("AGENT_0", "JU_TEST_OS_SCRIPT_0", "Авто тест JU_TEST_OS_SCRIPT_0",
					"ping -c 3 http-proxy.srv.rdtex.ru", "10", "11","0");
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		assert(trailManager.getConnection() != null);
		assert(trail != null);

		// -- сохраняем в БД
		try {
			trail.overwrite(trailManager.getConnection());
			trailManager.getConnection().commit();
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		try { // -- здесь должен быть insert
			trail.add_observable_object(trailManager.getConnection(), "scanario", 11111);
			trailManager.getConnection().commit();
		} catch (Exception ex) {
			try {
				trailManager.getConnection().rollback();
			} catch (Exception ex2) {
			}
			System.err.println(trail.to_string());
			fail(ex.getMessage());
		}

		try { // -- здесь должен быть update
			trail.add_observable_object(trailManager.getConnection(), "scanario", 11111);
			trailManager.getConnection().commit();
		} catch (Exception ex) {
			try {
				trailManager.getConnection().rollback();
			} catch (Exception ex2) {
			}
			System.err.println(trail.to_string());
			fail(ex.getMessage());
		}
	}


}
