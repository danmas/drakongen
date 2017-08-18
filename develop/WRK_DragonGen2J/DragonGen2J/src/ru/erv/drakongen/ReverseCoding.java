 
	package ru.erv.drakongen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import ru.erv.drakongen.utils.FileUtils;
 
	public class ReverseCoding { 
	static class Wrapper<T> {
	T value;
	Wrapper(T value){
	this.value = value;
	}

	public T getValue() {
	return value;
	}

	public void setValue(T value) {
	this.value = value;
	}
} 
	public static final boolean RET_OK  = true;
public static final boolean RET_ERROR  = false;
public static final String PREF_NODE_ID = "<node id=\"";
public static final String POST_NODE_ID = "\"";
 
		List<String> files = new ArrayList<String>();
	String gml_src_file;
	String gml_tgt_file;
	
	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	Map<String, String> insert_codes = new HashMap<String, String>();
 
	public ReverseCoding() { 
		}


	public String reverseCode() { 
		String out_text = "";
 
		Iterator<String> itf = files.iterator();
while (itf.hasNext()) {
						String full_file_name = itf.next();
			System.out.println("-- Сформирована карта подстановок для файла:"+full_file_name);
 
			boolean bret = 
			geReleaseCode(full_file_name); 
			//--dg-- вернулась ошибка?
			if(bret == RET_ERROR) {
				System.err.println("Ошибка при реверскодинге файла " + full_file_name); 
				return null; 
			} else {
			}
			}
		String gml_text = FileUtils.fileRead(gml_src_file); 
		  System.out.println("-- Выполняются замены...");
  out_text = replaseMarkedCode(gml_text);
 
		return out_text;
}
	public String replaseMarkedCode(String src_text) { 
		int pos1 = 0;
int pos2 = 0;
Wrapper<Integer> i = new Wrapper<Integer>(0);
Wrapper<Integer> i2 = new Wrapper<Integer>(0);
String out_text = ""; 
		while(true) {
			i.setValue(pos1);
String mark_code = getTextBetween(src_text,DrakonUtils.PREF_MARKER_CDATA,"]",i); 
			//--dg-- нашли маркер?
			if(mark_code != null) {
				int pos_m = src_text.indexOf(DrakonUtils.PREF_MARKER_CDATA,pos1); 
				i2.setValue(pos_m-45);
String node_id = getTextBetween(src_text,PREF_NODE_ID,POST_NODE_ID,i2); 
			} else {
				out_text += src_text.substring(pos2,src_text.length()); 
				break; 
			}
			pos2 = src_text.indexOf(DrakonUtils.PREF_CODE_CDATA,pos1) + DrakonUtils.PREF_CODE_CDATA.length(); 
			out_text += src_text.substring(pos1,pos2);  
			String repl_text = insert_codes.get(mark_code); 
			//--dg-- есть новый код?
			if(repl_text != null) {
			} else {
				i.setValue(pos1);
String old_code = getTextBetween(src_text,DrakonUtils.PREF_CODE_CDATA,"]",i); 
				repl_text = old_code; 
			}
			out_text += repl_text; 
			pos1 = src_text.indexOf(DrakonUtils.POST_CODE_CDATA,pos2);
pos2 = pos1;
 
			}
		return out_text;
}
	protected boolean geReleaseCode(String full_file_name) { 
		try { 
		String text = FileUtils.fileRead(full_file_name,"UTF-8");
 
		Wrapper<Integer> i = new Wrapper<Integer>(0);
while(true) {
			String mark_code = getTextBetween(text,DrakonUtils.PREF_MARKER_DG2J,"\"",i); 
			//--dg-- нашли маркер?
			if(mark_code != null) {
			} else {
				break; 
			}
			//--dg-- уже есть такой маркер в карте подстановок?
			if(insert_codes.get(mark_code) != null) {
				System.err.println("Ошибка! Дублирование маркера кода ..."); 
				return RET_ERROR; 
			} else {
			}
			String out_text = getTextBetween(text,">","</DG2J>",i);
 
			//--dg-- нашли текста подстановки?
			if(out_text != null) {
			} else {
				break; 
			}
			insert_codes.put(mark_code, out_text); 
			}
		} catch(Exception e) {
	System.err.println(e.getMessage() );
} 
		return RET_OK;
}
	public static String getTextBetween(String text, String prefix, String postfix,  Wrapper<Integer>  position) { 
		int i1 = text.indexOf(prefix,position.getValue()); 
		//--dg-- нашли префикс?
		if(i1 >= 0) {
			int i2 = text.indexOf(postfix,i1+prefix.length()+1); 
			//--dg-- нашли постфикс?
			if(i2>=0) {
				position.setValue(i2+postfix.length());
return text.substring(i1+prefix.length(),i2); 
			} else {
				return null; 
			}
		} else {
			return null; 
		}
		}

	public void addCodeFile(String file_name) {
 
		  files.add(file_name); 
		}
	public void run() { 
				// выполняем подстановки
		String out_text  = reverseCode();
		// записываем новый файл gml
		FileUtils.fileWrite(gml_tgt_file, out_text);
		System.out.println("-- Записали файл: "+gml_tgt_file);
 
		}
	public static void main(String[] args) { 
				// устанавливаем BASE_DIR
		// Settings.setProperty("BASE_DIR", "..\\..\\..\\WRK\\DG2J\\DragonGen2J\\");
		
		// переменная rc
		ReverseCoding rc = new ReverseCoding();
		
		rc.gml_src_file = "C:/RDTEX/CB-NRD/work/schemes/DG_Algoritms_ACM.graphml";
		rc.gml_tgt_file = "C:/RDTEX/CB-NRD/work/schemes/DG_Algoritms_ACM_2.graphml";
//		rc.gml_src_file = "C:/RDTEX/CB-NRD/work/schemes/REPL_src.graphml";
//		rc.gml_tgt_file = "C:/RDTEX/CB-NRD/work/schemes/DG_Algoritms_ACM_2.graphml";
		
		rc.addCodeFile("C:/RDTEX/CB-NRD/work/base/CRC_TESTS_2_gen.sql");
		
		rc.run();
 
		}
	} //-- конец класса
 
