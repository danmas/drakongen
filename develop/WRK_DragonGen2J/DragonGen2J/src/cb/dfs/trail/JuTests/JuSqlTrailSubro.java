package cb.dfs.trail.JuTests;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cb.dfs.trail.TrailBase;
import cb.dfs.trail.TrailSql;
import cb.dfs.trail.TrailManagerSubro;
import cb.dfs.trail.common.Constants;

public class JuSqlTrailSubro {
	TrailManagerSubro trailSubroManager = null;
	/*
	@Test
	public void test_do_trail_sql_1()  throws Exception  {
		assert(trailSubroManager != null);

		TrailBase trail = null;
		
		//-- создаем новую ОП
		String trail_key = "(G) RDAgent Проверка SQL. время отклика 5 сек";
		try {
			trail = new TrailSql("RDAgent", trail_key, "Авто тест "+trail_key
					, "select '1' from dual", Constants.test_url, Constants.test_user, Constants.test_pwd
					, "5", "10","0");
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
	public void test_do_trail_sql_2() {
		assert(trailSubroManager != null);

		TrailBase trail = null;
		
		//-- создаем новую ОП
		String trail_key = "(G) Проверка SQL. select sysdate from dual время отклика 5 сек";
		try {
			trail = new TrailSql("erv_local", trail_key, "Авто тест "+trail_key
					, "select sysdate from dual", Constants.test_url, Constants.test_user, Constants.test_pwd
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
			//trailSubroManager.do_trail_subro(trail, "scenario_name", (int)(Math.random() * 10000));
			trailSubroManager.launch_trail_if_ready(trail, "scenario_name", (int)(Math.random() * 10000));
		} catch (Exception ex) {
			  try{ trailSubroManager.getConnection().rollback(); } catch(Exception ex2) {}
			  fail("Выполнение операции проверки прервано." + "\n" + ex.getMessage());
		} finally {
			  //! try{ trailSubroManager.getConnection().close();} catch(Exception ex2) {}  
		}
	}
	

	@Test
	public void test_do_trail_sql_3() {
		assert(trailSubroManager != null);

		TrailBase trail = null;
		
		//-- создаем новую ОП
		String trail_key = "(B) Проверка SQL не возвращающего значения. время отклика 5 сек";
		try {
			trail = new TrailSql("erv_local", trail_key, "Авто тест "+trail_key
					, "select trail_key from d_trails where 1=2", Constants.test_url, Constants.test_user, Constants.test_pwd
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
			//trailSubroManager.do_trail_subro(trail, "scenario_name", (int)(Math.random() * 10000));
			trailSubroManager.launch_trail_if_ready(trail, "scenario_name", (int)(Math.random() * 10000));
   		    fail("Не возникло исключение для плохого теста!");
		} catch (Exception ex) {
			  try{ trailSubroManager.getConnection().commit(); } catch(Exception ex2) {}
		} finally {
			  //! try{ trailSubroManager.getConnection().close();} catch(Exception ex2) {}  
		}
	}
	
	@Test
	public void test_do_trail_sql_4() {
		assert(trailSubroManager != null);

		TrailBase trail = null;
		
		//-- создаем новую ОП
		String trail_key = "(G) Проверка SQL. Вывод кучи записей. время отклика 5 сек";
		try {
			trail = new TrailSql("RDAgent", trail_key, "Авто тест "+trail_key
					, "select * from d_trails", Constants.test_url, Constants.test_user, Constants.test_pwd
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
			//trailSubroManager.do_trail_subro(trail, "scenario_name", (int)(Math.random() * 10000));
			trailSubroManager.launch_trail_if_ready(trail, "scenario_name", (int)(Math.random() * 10000));
		} catch (Exception ex) {
			  try{ trailSubroManager.getConnection().rollback(); } catch(Exception ex2) {}
			  fail("Выполнение операции проверки прервано." + "\n" + ex.getMessage());
		} finally {
			  //! try{ trailSubroManager.getConnection().close();} catch(Exception ex2) {}  
		}
	}
	
	
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
	*/
}
