
//--dg-- Сборка класса ReverseCoding
 
	//--dg-- package//--dg-- imports
	package ru.erv.drakongen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import ru.erv.drakongen.utils.FileUtils;
 
	//--dg-- сlass ReverseCoding
	public class ReverseCoding { 
	//--dg-- вспомогательный class Wrapper T 
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
	//--dg-- константы
	public static final boolean RET_OK  = true;
public static final boolean RET_ERROR  = false;
public static final String PREF_NODE_ID = "<node id=\"";
public static final String POST_NODE_ID = "\"";
 
	//--dg-- перменные
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
 
	//--dg-- Конструктор
	public ReverseCoding() { 
		//--dg-- //--dg--             
		}


	//--dg-- reverseCode()
	public String reverseCode() { 
		//--dg-- задаем переменные
		String out_text = "";
 
		//--dg-- для всех файлов из списка
		Iterator<String> itf = files.iterator();
while (itf.hasNext()) {
			//--dg-- получаем полное имя файла
						String full_file_name = itf.next();
			System.out.println("-- Сформирована карта подстановок для файла:"+full_file_name);
 
			//--dg-- bret=
			boolean bret = 
			//--dg-- Формирование карты подстановок для одного файла
			geReleaseCode(full_file_name); 
			//--dg-- вернулась ошибка?
			if(bret == RET_ERROR) {
				//--dg-- Ошибка при реверскодинге файла ...
				System.err.println("Ошибка при реверскодинге файла " + full_file_name); 
				//--dg-- null
				return null; 
			} else {
			}
			}
		//--dg-- читаем входной graphml файл в переменную-буфер
		String gml_text = FileUtils.fileRead(gml_src_file); 
		//--dg-- Выполняем маркерные замены
		  System.out.println("-- Выполняются замены...");
  out_text = replaseMarkedCode(gml_text);
 
		//--dg-- новый текст gml
		return out_text;
}
	//--dg-- перенос кода с заменой по маркерам
	public String replaseMarkedCode(String src_text) { 
		//--dg-- инициируем позиции поиска в начало pos1=0 pos2=0
		int pos1 = 0;
int pos2 = 0;
Wrapper<Integer> i = new Wrapper<Integer>(0);
Wrapper<Integer> i2 = new Wrapper<Integer>(0);
String out_text = ""; 
		//--dg-- бесконечный поиск маркеров
		while(true) {
			//--dg-- выделяем маркер начиная с текущей позиции pos1
			i.setValue(pos1);
String mark_code = getTextBetween(src_text,DrakonUtils.PREF_MARKER_CDATA,"]",i); 
			//--dg-- нашли маркер? 
			if(mark_code != null) {
				//--dg-- ищем позицию маркера с позиции pos1
				int pos_m = src_text.indexOf(DrakonUtils.PREF_MARKER_CDATA,pos1); 
				//--dg-- получаем имя узла с позиции маркера - 45
				i2.setValue(pos_m-45);
String node_id = getTextBetween(src_text,PREF_NODE_ID,POST_NODE_ID,i2); 
			} else {
				//--dg-- дописываем остаток в интервале pos2 - до конца
				out_text += src_text.substring(pos2,src_text.length()); 
				//--dg-- выходим из цикла
				break; 
			}
			//--dg-- находим позицию pos2 = конец BegD от текущей позиции pos1
			pos2 = src_text.indexOf(DrakonUtils.PREF_CODE_CDATA,pos1) + DrakonUtils.PREF_CODE_CDATA.length(); 
			//--dg-- переносим в результат код в интервале pos1-pos2
			out_text += src_text.substring(pos1,pos2);  
			//--dg-- получаем новый код по текущему маркеру Mi
			String repl_text = insert_codes.get(mark_code); 
			//--dg-- есть новый код?
			if(repl_text != null) {
			} else {
				//--dg-- Достаем старый код
				i.setValue(pos1);
String old_code = getTextBetween(src_text,DrakonUtils.PREF_CODE_CDATA,"]",i); 
				//--dg-- новый код = старому 
				repl_text = old_code; 
			}
			//--dg-- добавляем в результат новый код
			out_text += repl_text; 
			//--dg-- устанавливаем pos1 и pos2 за концом данных
			pos1 = src_text.indexOf(DrakonUtils.POST_CODE_CDATA,pos2);
pos2 = pos1;
 
			}
		//--dg-- результат
		return out_text;
}
	//--dg-- Формирование карты подстановок для одного файла
	protected boolean geReleaseCode(String full_file_name) { 
		//--dg-- try
		try { 
		//--dg-- открываем файл и читаем текст в UTF-8
		String text = FileUtils.fileRead(full_file_name,"UTF-8");
 
		//--dg-- бесконечный поиск PREF_MARKER_DG2J
		Wrapper<Integer> i = new Wrapper<Integer>(0);
while(true) {
			//--dg-- ищем маркер 
			String mark_code = getTextBetween(text,DrakonUtils.PREF_MARKER_DG2J,"\"",i); 
			//--dg-- нашли маркер? 
			if(mark_code != null) {
			} else {
				//--dg-- break
				break; 
			}
			//--dg-- уже есть такой маркер в карте подстановок? 
			if(insert_codes.get(mark_code) != null) {
				//--dg-- Ошибка! Дублирование маркера кода ...
				System.err.println("Ошибка! Дублирование маркера кода ..."); 
				//--dg-- Error
				return RET_ERROR; 
			} else {
			}
			//--dg-- ищем текста подстановки
			String out_text = getTextBetween(text,">","</DG2J>",i);
 
			//--dg-- нашли текста подстановки? 
			if(out_text != null) {
			} else {
				//--dg-- break
				break; 
			}
			//--dg-- заносим текст подстановки в карту замен
			insert_codes.put(mark_code, out_text); 
			}
		//--dg-- catch
		} catch(Exception e) {
	System.err.println(e.getMessage() );
} 
		//--dg-- Ok
		return RET_OK;
}
	//--dg-- Выделение текста между префиксом и постфиксом начиная с позиции pos
	public static String getTextBetween(String text, String prefix, String postfix,  Wrapper<Integer>  position) { 
		//--dg-- ищем фразу префикса
		int i1 = text.indexOf(prefix,position.getValue()); 
		//--dg-- нашли префикс? 
		if(i1 >= 0) {
			//--dg-- ищем постфикс от конца префикса 
			int i2 = text.indexOf(postfix,i1+prefix.length()+1); 
			//--dg-- нашли постфикс? 
			if(i2>=0) {
				//--dg-- результат
				position.setValue(i2+postfix.length());
return text.substring(i1+prefix.length(),i2); 
			} else {
				//--dg-- null
				return null; 
			}
		} else {
			//--dg-- null
			return null; 
		}
		//--dg-- //--dg--         
		}

	//--dg-- addCodeFile
	public void addCodeFile(String file_name) {
 
		//--dg-- files.add(file_name);
		  files.add(file_name); 
		//--dg-- //--dg--             
		}
	//--dg-- run()
	public void run() { 
		//--dg-- выполняем подстановки записываем новый файл gml
				// выполняем подстановки
		String out_text  = reverseCode();
		// записываем новый файл gml
		FileUtils.fileWrite(gml_tgt_file, out_text);
		System.out.println("-- Записали файл: "+gml_tgt_file);
 
		//--dg-- //--dg--             
		}
	//--dg-- main
	public static void main(String[] args) { 
		//--dg-- экземпляр класса ReverseCoding
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
 
		//--dg-- //--dg--             
		}
	//--dg-- 
            
	} //-- конец класса
 
