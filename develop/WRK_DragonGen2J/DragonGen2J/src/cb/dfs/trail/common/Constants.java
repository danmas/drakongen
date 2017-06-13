package cb.dfs.trail.common;
/*
DB=DB Файл                                                                                     
*/                                                                                               

public class Constants {
	
	public final static String HTTP_PROPERTY_DELIMITER ="{;}";
	public final static String HTTP_BADSTRINGS_DELIMITER ="{;}";
	public final static String HTTP_LOGIN_STRINGS_DELIMITER ="{;}";
	public final static String HTTP_COOKIE_DELIMITER =";";
	
	public final static String test_url = "jdbc:oracle:thin:@vm-cb3-bi.vm-p.rdtex.ru:1521:orcl";
	public final static String test_user = "ODI_TECH";
	public final static String test_pwd = "ODI_TECH";

	
	public final static int CUR_VER_MAJOR = 0;
	public final static int CUR_VER_MINOR = 1;
	public final static int CUR_VER_REVIS = 2;
	
	public final static String TT_OS_SCRIPT = "TT_OS_SCRIPT";
	public final static String TT_SSH_KEY = "TT_SSH_KEY";
	public final static String TT_SSH_PWD = "TT_SSH_PWD";
	public final static String TT_HTTP = "TT_HTTP";
	public final static String TT_TCP_PORT = "TT_TCP_PORT";
	public final static String TT_SQL = "TT_SQL";
	public final static String TT_OBIEE = "TT_OBIEE";
	
	public final static String ST_ERROR =   "ERROR";
	public final static String ST_RUNNING = "RUNNING";
	public final static String ST_SUCCESS = "SUCCESS";
	public final static String ST_UNKNOWN = "UNKNOWN";
	
	public final static String MSG_ERR_CANT_SETUP_CONNECTION = "Не могу установить JDBC коннекцию!";
	public final static String MSG_ERR_ON_EXECUTE_SQL="Ошибка при выполнении SQL:";                                              
	public final static String MSG_DONE="Выполнено";                                                                             
	                                                                                               
	public final static String LABEL_SQL="Sql:";                                                                                 
	                                                                                               
	public final static String ABOUT_HTPSQLTREEOBJECT="HTML таблица построенная на основе SQL запроса.";                         
	public final static String ABOUT_SQLTREEOBJECT="SQL запрос через JDBC коннекцию.";                                           
	public final static String ABOUT_ORAJDBCCONNECTIONNODE="JDBC коннекция к базе данных Oracle.";                               
	public final static String ABOUT_MYSQLJDBCCONNECTIONNODE="JDBC коннекция к базе данных MySql.";                              
	                                                                                               
	public final static String MSG_CANT_READ_SQL_FOR="Не могу прочитать SQL для";                                                
	public final static String MSG_CANT_FIND_AND_SETUP_JDBC="Не могу найти и установить JDBC коннекцию.";                         
	public final static String MSG_EMPTY_HTML="Пусто";                                                                           
	public final static String MSG_ERR_CANT_CREATE_JDBC_CONNECTION="Не могу создать JDBC коннекцию.";                             
	public final static String MSG_ERR_CONNECTIONSTRING_NOT_SETUP="Не задана строка подключения";
	
	public final static int BODY_MAX_LENGTH_IN_HTML_READ = 2000;
	
	
	public final static String BI_SERVER_SESSION_ID_REQUEST="<?xml version='1.0'  encoding='UTF-8' ?>\r\n"
	+		"<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:v7='urn://oracle.bi.webservices/v7'>\r\n"
	+"  <soapenv:Body>\r\n"
	+"    	<v7:logon>\r\n"
	+"  		<v7:name>%WS_LOGIN%</v7:name>\r\n"
	+"          <v7:password>%WS_PASSWORD%</v7:password>\r\n"
	+"    	</v7:logon>\r\n"
	+"  </soapenv:Body>\r\n"
	+"</soapenv:Envelope>\r\n";
	
	public final static String READ_XML_REPORT_BI_SERVER="<?xml version='1.0'  encoding='UTF-8' ?>\r\n"
	+		"<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:v7='urn://oracle.bi.webservices/v7'>\r\n"
	+		"<soapenv:Header/>\r\n"
	+		"<soapenv:Body>\r\n"
  		+		"<v7:executeXMLQuery>\r\n"
     		+		"<v7:report>\r\n"
        		+		"<v7:reportPath>%WS_REPORT_PATH%</v7:reportPath>\r\n"
        		+		"<v7:reportXml></v7:reportXml>\r\n"
     		+		"</v7:report>\r\n"
     		+		"<v7:outputFormat></v7:outputFormat>\r\n"
     		+		"<v7:executionOptions>\r\n"
        		+		"<v7:async></v7:async>\r\n"
        		+		"<v7:maxRowsPerPage></v7:maxRowsPerPage>\r\n"
        		+		"<v7:refresh></v7:refresh>\r\n"
        		+		"<v7:presentationInfo></v7:presentationInfo>\r\n"
        		+		"<v7:type></v7:type>\r\n"
     		+		"</v7:executionOptions>\r\n"
     		+		"<v7:reportParams>\r\n"
     		+		"</v7:reportParams>\r\n"
     		+		"<v7:sessionID>%WS_SESSIONID%</v7:sessionID>\r\n"
  		+		"</v7:executeXMLQuery>\r\n"
		+		"</soapenv:Body>\r\n"
	+		"</soapenv:Envelope>\r\n";

}
