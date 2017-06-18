package ru.erv.drakongen.test;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonTest {

    public static void main(String[] args) {
    	String s =" {\"type\":\"OS_SCRIPT\",\"script\":\"ping -n 3 http-proxy.srv.rdtex.ru\""
    			+ ",\"description\":\"Тест выполнения скрипта ОС\",\"max_duration_in_sec\":\"5\""
    			+ ",\"launch_period_in_sec\":\"5\"}";
    	try {
            JSONObject jo = (JSONObject) JSONValue.parseWithException(s);
            // get the title
            System.out.println(jo.get("type"));
            System.out.println(jo.get("script"));
            System.out.println(jo.get("description"));
            System.out.println(jo.get("max_duration_in_sec"));
            System.out.println(jo.get("launch_period_in_sec"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    public static void main2(String[] args) { 
        /*
         * {"title":"Free Music Archive - Genres","message":"","errors":[],"total" : "161","total_pages":81,"page":1,"limit":"2",
         * "dataset":
         * [{"genre_id": "1","genre_parent_id":"38","genre_title":"Avant-Garde" ,"genre_handle": "Avant-Garde","genre_color":"#006666"},
         * {"genre_id":"2","genre_parent_id" :null,"genre_title":"International","genre_handle":"International","genre_color":"#CC3300"}]}
         */
        String s =  "{\"title\":\"Free Music Archive - Genres\",\"message\":\"\",\"errors\":[],\"total\" : \"161\",\"total_pages\":81,\"page\":1,\"limit\":\"2\"," +
                 "\"dataset\":" +
                    "[{\"genre_id\": \"1\",\"genre_parent_id\":\"38\",\"genre_title\":\"Avant-Garde\" ,\"genre_handle\": \"Avant-Garde\",\"genre_color\":\"#006666\"}," +
                     "{\"genre_id\":\"2\",\"genre_parent_id\" :null,\"genre_title\":\"International\",\"genre_handle\":\"International\",\"genre_color\":\"#CC3300\"}]}";
        
        try {
            JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(s);
            // get the title
            System.out.println(genreJsonObject.get("title"));
            // get the data
            JSONArray genreArray = (JSONArray) genreJsonObject.get("dataset");
            // get the first genre
            JSONObject firstGenre = (JSONObject) genreArray.get(0);
            System.out.println(firstGenre.get("genre_title"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
