package cb.dfs.trail.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class FibonacciSOAPClient {

	public final static String DEFAULT_SERVER = "http://172.28.64.2:9704/analytics/saw.dll?SoapImpl=nQSessionService";
	public final static String SOAP_ACTION = "http://www.example.com/fibonacci";

	/*
	 * <soapenv:Envelope
	 * xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
	 * xmlns:v7="urn://oracle.bi.webservices/v7"> <soapenv:Header/>
	 * <soapenv:Body> <v7:logon> <v7:name>weblogic</v7:name>
	 * <v7:password>welcome1</v7:password> </v7:logon> </soapenv:Body>
	 * </soapenv:Envelope>
	 */

//	public static String readSID() { //throws Exception {
//		
//	}
	
	public static void readReport(String sId) {
		
	}
	
	static String server1 = "http://172.28.64.2:9704/analytics/saw.dll?SoapImpl=nQSessionService";
	static String server2 = "http://172.28.64.2:9704/analytics/saw.dll?SoapImpl=xmlViewService";
//-- пустой отчет	
//	<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/" xmlns:sawsoap="urn://oracle.bi.webservices/v7">
//	   <soap:Body>
//	      <sawsoap:executeXMLQueryResult>
//	         <sawsoap:return xsi:type="sawsoap:QueryResults">
//	            <sawsoap:rowset xsi:type="xsd:string">&lt;rowset xmlns="urn:schemas-microsoft-com:xml-analysis:rowset">&lt;/rowset></sawsoap:rowset>
//	            <sawsoap:queryID xsi:type="xsd:string">RSXS2_1</sawsoap:queryID>
//	            <sawsoap:finished xsi:type="xsd:boolean">true</sawsoap:finished>
//	         </sawsoap:return>
//	      </sawsoap:executeXMLQueryResult>
//	   </soap:Body>
//	</soap:Envelope>
	
//-- ошибка авторизации	
//	<sawsoape:Message>Authentication error. Invalid session ID or Session Expired</sawsoape:Message>
	
//-- путь не найден	
//	<sawsoape:Message>Path not found (/shared/TEST_STEPANOV/++EREMEEV_TEST)</sawsoape:Message>
	
	public static void main(String[] args) {
		String s1="<?xml version='1.0'?>\r\n"
		+		"<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:v7='urn://oracle.bi.webservices/v7'>\r\n"
		+"  <soapenv:Body>\r\n"
		+"    	<v7:logon>\r\n"
		+"  		<v7:name>%WS_LOGIN%</v7:name>\r\n"
		+"          <v7:password>%WS_PASSWORD%</v7:password>\r\n"
		+"    	</v7:logon>\r\n"
		+"  </soapenv:Body>\r\n"
		+"</soapenv:Envelope>\r\n";
		
		s1 = Strings.replace(s1, "%WS_LOGIN%", "weblogic");
		s1 = Strings.replace(s1, "%WS_PASSWORD%", "welcome1");
		
		String sId = null;
		try {
			String resp = read(server1,s1);
			System.out.println(resp.toString());

			sId = Strings.getStrBetween(resp,"<sawsoap:sessionID xsi:type=\"xsd:string\">","</sawsoap:sessionID>");
			System.out.println("sId:"+sId);
		} catch(Exception ex) {
			System.err.println(ex.getMessage());
			return;
		}
		
		String s2="<?xml version='1.0'?>\r\n"
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
            		+		"<!--Zero or more repetitions:-->\r\n"
            		+		"<v7:filterExpressions></v7:filterExpressions>\r\n"
            		+		"<!--Zero or more repetitions:-->\r\n"
            		+		"<v7:variables>\r\n"
               		+		"<v7:name></v7:name>\r\n"
               		+		"<v7:value></v7:value>\r\n"
            		+		"</v7:variables>\r\n"
            		+		"<!--Zero or more repetitions:-->\r\n"
            		+		"<v7:nameValues>\r\n"
               		+		"<v7:name></v7:name>\r\n"
               		+		"<v7:value></v7:value>\r\n"
            		+		"</v7:nameValues>\r\n"
            		+		"<!--Zero or more repetitions:-->\r\n"
            		+		"<v7:templateInfos>\r\n"
               		+		"<v7:templateForEach></v7:templateForEach>\r\n"
               		+		"<v7:templateIterator></v7:templateIterator>\r\n"
               		+		"<!--Zero or more repetitions:-->\r\n"
               		+		"<v7:instance>\r\n"
                  		+		"<v7:instanceName></v7:instanceName>\r\n"
                  		+		"<!--Zero or more repetitions:-->\r\n"
                  		+		"<v7:nameValues>\r\n"
                     		+		"<v7:name></v7:name>\r\n"
                     		+		"<v7:value></v7:value>\r\n"
                  		+		"</v7:nameValues>\r\n"
               		+		"</v7:instance>\r\n"
            		+		"</v7:templateInfos>\r\n"
            		+		"<!--Optional:-->\r\n"
            		+		"<v7:viewName></v7:viewName>\r\n"
         		+		"</v7:reportParams>\r\n"
         		+		"<v7:sessionID>%WS_SESSIONID%</v7:sessionID>\r\n"
      		+		"</v7:executeXMLQuery>\r\n"
   		+		"</soapenv:Body>\r\n"
		+		"</soapenv:Envelope>\r\n";
		
		s2 = Strings.replace(s2, "%WS_SESSIONID%", sId);
		s2 = Strings.replace(s2, "%WS_REPORT_PATH%", "/shared/TEST_STEPANOV/EREMEEV_TEST");
		try {
			String resp = read(server2,s2);
			System.out.println(resp.toString());
			if(resp.indexOf("<sawsoap:rowset xsi:type=\"xsd:string\">&lt;rowset xmlns=&quot;urn:schemas-microsoft-com:xml-analysis:rowset&quot;&gt;&lt;/rowset&gt;</sawsoap:rowset>")
					!=-1) {
				System.out.println("Отчет не содержит данных.");
			}
			if(resp.indexOf("<sawsoape:Message>Authentication error. Invalid session ID or Session Expired</sawsoape:Message>")
					!=-1) {
				System.out.println("Ошибка авторизации или закрыта сессия.");
			}
			if(resp.indexOf("<sawsoape:Message>Path not found")
					!=-1) {
				System.out.println("Не найден путь к отчёту.");
			}
			
		} catch(Exception ex) {
			System.err.println(ex.getMessage());
		}
		
	}
	
	public static String read(String server, String s1) throws Exception {
			URL u = new URL(server);
			URLConnection uc = u.openConnection();
			HttpURLConnection connection = (HttpURLConnection) uc;

			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");

			OutputStream out = connection.getOutputStream();
			Writer wout = new OutputStreamWriter(out);

			String[] tok = Strings.tokenize(s1, "\n");
		    for (int i = 0; i < tok.length; i++) {
		    	wout.write(tok[i]+"\n");
		      }
			wout.flush();
			wout.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			String resp = new String(response);
			return resp;
			
			//-- находим номер сессии
			//<sawsoap:sessionID xsi:type="xsd:string"></sawsoap:sessionID>
			//int i1 = response.indexOf(str)
			//"Не удалось подключиться к серверу."
	} // end main

	
	public static void main2(String[] args) {
		String responce = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:sawsoap=\"urn://oracle.bi.webservices/v7\"> <soap:Body> <sawsoap:logonResult><sawsoap:sessionID xsi:type=\"xsd:string\">4t181sf8bg9uj1qs3c695gga99a0nl9cvib4bii</sawsoap:sessionID></sawsoap:logonResult></soap:Body> </soap:Envelope>"; 
		System.out.println("sId:"+Strings.getStrBetween(responce,"<sawsoap:sessionID xsi:type=\"xsd:string\">","</sawsoap:sessionID>"));
		System.out.println("sId:"+"4t181sf8bg9uj1qs3c695gga99a0nl9cvib4bii");
	}
	
	
} // end FibonacciSOAPClient