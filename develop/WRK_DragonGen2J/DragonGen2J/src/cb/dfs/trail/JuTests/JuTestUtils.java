package cb.dfs.trail.JuTests;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cb.dfs.trail.common.Constants;
import cb.dfs.trail.db.OraConnector;
import cb.dfs.trail.utils.Rs2Json;

public class JuTestUtils {

	OraConnector connector = null;
	
	@Before
	public void prepareTests()  throws Exception {
		try {
			connector = OraConnector.getOraConnector(Constants.test_url,
					Constants.test_user, Constants.test_pwd);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
		assert (connector.getConnection() != null);
	}

	@After
	public void afterTests()  throws Exception {
		try {
			connector.getConnection().commit();
			//connector.getConnection().close();
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}
/*	
	try {
		
		trailManager = new TrailManager(Constants.test_url,
				Constants.test_user_dfs_tech, Constants.test_pwd_dfs_tech);
		// -- удаляем все предыдущие ОПы
		trailManager.deleteAll();
	} catch (Exception ex) {
		fail(ex.getMessage());
	}
	assert (trailManager != null);
*/
	
	@Test
	public void testRs2Json() {
		try {
			String str = Rs2Json.select2json(connector.getConnection()
					, "select * from D_OBSERVABLE_OBJECTS");
			System.out.println(str);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
		
	}
}
