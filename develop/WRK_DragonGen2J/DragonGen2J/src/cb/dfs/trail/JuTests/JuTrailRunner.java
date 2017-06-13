package cb.dfs.trail.JuTests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

import cb.dfs.trail.TrailBase;
import cb.dfs.trail.TrailRunner;
import cb.dfs.trail.common.Constants;

public class JuTrailRunner {

	// //@Ignore("временно")
	// @Test
	// public void testGetDuration() {
	// TrailRunner trailRunner = new TrailRunner();
	// try {
	// assertTrue(1 == trailRunner.getDuration("1"));
	// } catch (Exception ex) {
	// fail(ex.getMessage());
	// }
	// // fail("Not yet implemented");
	// }

	// @Ignore("временно")
	@Test
	public void testRunOsScript() {
		TrailRunner trailRunner = new TrailRunner();
		TrailBase trail = null;

		assertTrue(trailRunner!=null);
		
		// -- проверка выполнения OS_SCRIPT и проверка ограничения на время
		// выполнения
		// -- GOOD
		try {
			trail = trailRunner.new_trail_os_script("AGENT_0","INT_TEST_OS_SCRIPT_0",
					"Авто тест _OS_SCRIPT_0",
					"ping -n 3 http-proxy.srv.rdtex.ru", "10","10000","0");
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		assertTrue(trail != null);

		try {
			trailRunner.do_trail(trail);
		} catch (Exception ex) {
			System.err.println(trail.to_string());
			fail(ex.getMessage());
		}
		// -- проверка стутуса
		assertTrue(trail.getStatus() == Constants.ST_SUCCESS);
		System.out.println(trail.to_string());

		// -- BAD
		try {
			trail = trailRunner.new_trail_os_script("AGENT_0","INT_TEST_OS_SCRIPT_1",
					"Авто тест _OS_SCRIPT_1",
					"ping -n 10 http-proxy.srv.rdtex.ru", "1","1000","0");
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		assertTrue(trail != null);
		try {
			trailRunner.do_trail(trail);
		} catch (Exception ex) {
			// -- проверка стутуса
			assertTrue(trail.getStatus() == Constants.ST_ERROR);
			System.out.println(trail.to_string());
			return;
		}
		fail("Не отловлена ошибка на превышение времени выполнения.");
	}

//	@Ignore
	@Test
	public void testRunSshPwd_1() {
		TrailRunner trailRunner = new TrailRunner();
		TrailBase trail = null;
		// -- проверка выполнения SSH_PWD и проверка ограничения на время
		// выполнения
		// -- GOOD
		try {
			trail = trailRunner.new_trail_ssh_pwd("AGENT_0","INT_TEST_SSH_PWD_0",
					"Авто тест _SSH_PWD_0", "vm-cb2-bi.vm-p.rdtex.ru", "22",
					"oracle", "or_dbms", "ping -c 3 http-proxy.srv.rdtex.ru",
					"100","10000","0");
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		try {
			assertTrue(trail != null);
			trailRunner.do_trail(trail);
		} catch (Exception ex) {
			System.err.println(trail.to_string());
			fail(ex.getMessage());
		}
		// -- проверка стутуса
		assertTrue(trail.getStatus() == Constants.ST_SUCCESS);
		System.out.println(trail.to_string());
	}

//	@Ignore
	@Test
	public void testRunSshPwd_2() {
		TrailRunner trailRunner = new TrailRunner();
		TrailBase trail = null;

		// -- BAD
		try {
			trail = trailRunner.new_trail_ssh_pwd("AGENT_0","INT_TEST_SSH_PWD_1",
					"Авто тест _SSH_PWD_1", "vm-cb2-bi.vm-p.rdtex.ru", "22",
					"oracle", "or_dbms", "ping -c 30 http-proxy.srv.rdtex.ru",
					"10","1000","0");
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		assertTrue(trail != null);
		try {
			trailRunner.do_trail(trail);
		} catch (Exception ex) {
			// -- проверка стутуса
			assertTrue(trail.getStatus() == Constants.ST_ERROR);
			System.out.println(trail.to_string());
			return;
		}
		fail("Не отловлена ошибка на превышение времени выполнения.");
	}

//	@Ignore
	@Test
	public void testRunSshPwd_3() {
		TrailRunner trailRunner = new TrailRunner();
		TrailBase trail = null;

		// -- BAD
		try {
			trail = trailRunner.new_trail_ssh_pwd("AGENT_0","INT_TEST_SSH_PWD_2",
					"Авто тест _SSH_PWD_2", "XXX", "22", "oracle", "or_dbms",
					"ping -c 3 http-proxy.srv.rdtex.ru", "10","10000","0");
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		assertTrue(trail != null);
		try {
			trailRunner.do_trail(trail);
			if(trail.getStatus() != Constants.ST_ERROR) {
				throw new Exception("Не отловлена ошибка на плохой SSH-server.");
			}
			return;
		} catch (Exception ex) {
			// -- проверка стутуса
			assertTrue(trail.getStatus() == Constants.ST_ERROR);
			System.out.println(trail.to_string());
			return;
		}
		//fail("Не отловлена ошибка на плохой SSH-server.");
	}

//	@Ignore
	@Test
	public void testRunSshPwd_4() {
		TrailRunner trailRunner = new TrailRunner();
		TrailBase trail = null;

		// -- BAD
		try {
			trail = trailRunner.new_trail_ssh_pwd("AGENT_0","INT_TEST_SSH_PWD_3",
					"Авто тест _SSH_PWD_3", "vm-cb2-bi.vm-p.rdtex.ru", "22",
					"oracle", "or_dbms", "ping -c 3 XXX", "10","10000","0");
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		assertTrue(trail != null);
		try {
			trailRunner.do_trail(trail);
			if(trail.getStatus() != Constants.ST_ERROR) {
				fail("Не отловлена ошибка на плохой скрипт.");
			}
		} catch (Exception ex) {
			// -- проверка стутуса
			assertTrue(trail.getStatus() == Constants.ST_ERROR);
			System.out.println(trail.to_string());
			return;
		}

	}

}
