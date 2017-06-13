package cb.dfs.trail.db;



import java.sql.Connection;
import java.sql.DriverManager;


//import net.confex.db.JdbcConnection;
import oracle.jdbc.driver.OracleDriver;



//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
// Класс коннекции
// !!! с коннекцией нужно работать только через getConnection.
// Если коннекции еще нет или закрыта то она будет создана.
//
// История изменений:
// ФИО         Дата     Комментарии
// ---------   ------   -------------------------------------------
// Еремеев     25.06.04 Создана
//
// Версия 0.1
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


public class OraConnector extends JdbcConnector {

	protected static OraConnector oraConnector = null;
	
	protected OraConnector(String url, String user, String password) {
		super(url, user, password);
		if(oraConnector==null) {
			oraConnector = this;
		}
	}
	
	public static OraConnector getOraConnector(String url, String user, String password) {
		if(oraConnector==null) {
			oraConnector = new OraConnector(url, user, password);
		}
		return oraConnector;
	}
	
	public Connection cereate_connection_on_server() throws Exception {
        return DriverManager.getConnection("jdbc:default:connection:");
	}
 
	public Connection cereate_connection(String url) throws Exception {
		DriverManager.registerDriver(new OracleDriver());
		return DriverManager.getConnection(url); 
	}
	
	public Connection cereate_connection(String url, String user, String password) throws Exception {
        DriverManager.registerDriver(new OracleDriver());
        return DriverManager.getConnection(url,user,password);
	}
	
}
