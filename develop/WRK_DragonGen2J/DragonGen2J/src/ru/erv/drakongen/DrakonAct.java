
//--dg-- Класс DrakonAct
 
	//--dg-- упоминание о DrakonGen2
	/**
  * Этот текст сгенерирован программой DrakonGen2
  * @author Erv +
*/ 
	//--dg-- package//--dg-- imports
	package ru.erv.drakongen;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import ru.erv.drakongen.utils.*;
import ru.erv.drakongen.parser.GraphMLReader;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;
import japa.parser.ast.CompilationUnit;
import java.io.File;
import japa.parser.JavaParser;
 
	//--dg-- class DrakonAct
	 public class DrakonAct { 
	//--dg-- константы
	public final static String DI_OS_ACTION = "OS_ACTION";
public final static String DI_DG_LIFT_DOWN = "DG_LIFT_DOWN";
public final static String DI_DG_LIFT_UP = "DG_LIFT_UP";
public final static String DI_FILE = "FILE";
	public static enum IcTypes {
		DI_OS_ACTION, DI_DG_LIFT_UP, DI_DG_LIFT_DOWN, DI_FILE;
	};
 
	//--dg-- переменные
	protected String CURRENT_RELEASE = DrakonUtils.RELEASE_TYPE_CODE_AS;

//private static final String BASE_DIR = "..\\..\\..\\WRK\\DG2J\\DragonGen2J\\";
private static final String MAIN_DG_FILE = "../../../WRK/DG2J/Schemes/Main.graphml";
Vertex in;

 
	//--dg-- Конструктор
	public DrakonAct() { 
		//--dg-- //--dg--             
		}
	//--dg-- main
	public static void main(String[] args) { 
		//--dg-- устанавливаем BASE_DIR
		Settings.setProperty("BASE_DIR", "..\\..\\..\\WRK\\DG2J\\DragonGen2J\\"); 
		//--dg-- переменная da
		DrakonAct da 
		//--dg-- экземпляр класса DrakonAct
		= new DrakonAct(); 
		//--dg-- строим Graph из файла Маin
		Graph graph = new TinkerGraph();

GraphMLReader reader = new GraphMLReader(MAIN_DG_FILE, graph);

try {
	reader.read();
	System.out.println(" <--- Прочитали файл "+ MAIN_DG_FILE);
} catch(Exception e) {
	System.err.println(" err " +e.getMessage());
	e.printStackTrace();
} 
		//--dg-- выполнение активностей
		da.activate_drakon(graph); 
		//--dg-- //--dg--             
		}
	//--dg-- Запускаем graph на выполнение
	protected void activate_drakon(Graph graph) {
 
		//--dg-- теперь текущий узел null
		Vertex cur_node = null;
 
		//--dg-- Проходим по всем узлам
		for (Vertex v : graph.getVertices()) {
			//--dg-- узел ЗАПУСК АКТИВНОСТЕЙ?
			if(DrakonUtils.getIconType(v).equals(DrakonUtils.DI_START_ACTS)) {
				//--dg-- теперь текущий узел ЗАПУСК
				cur_node = v;
 
				//--dg-- break
				break; 
			} else {
			}
			}
		//--dg-- нашли узел ЗАПУСКА?
		if(cur_node != null) {
			//--dg-- Проходим по всем узлам ЗАПУСКА
			while(DrakonUtils.getOutDegree(cur_node) == 1) {
				//--dg-- теперь текущий узел тот на который указывает выход
				cur_node = DrakonUtils.getOutNode(cur_node,0);
 
				//--dg-- узел НАЧАЛО?
				if(DrakonUtils.getIconType(cur_node).equals(DrakonUtils.DI_DG_BEG)) {
				} else {
					//--dg-- ОШИБКА! У иконы ЗАПУСК АКТИВНОСТЕЙ неправильный тип выхода.
					DrakonUtils.error("ОШИБКА! У иконы ЗАПУСК АКТИВНОСТЕЙ неправильный тип выхода \"" + DrakonUtils.getIconType(cur_node) + "\""); 
					//--dg-- //--dg--         
					return; 
				}
				//--dg-- ищем в кодограмме НАЧАЛО с таким же именем
				Vertex act_node = null; 
String name = DrakonUtils.getComment(cur_node);				
for (Vertex v : graph.getVertices()) {
	//-- узел НАЧАЛО?
	if(v!= cur_node && DrakonUtils.getIconType(v).equals(DrakonUtils.DI_DG_BEG)) {
		if(DrakonUtils.getComment(v).equals(name)) {
			act_node = v;
			break;
		}
	} else {
	}
} 
				//--dg-- нашли?
				if(act_node != null) {
					//--dg-- Выполняем силуэт от ТС
					activate_siluet(act_node); 
				} else {
					//--dg-- ОШИБКА! Не нашли узла с именем ... как у стартовой последовательности. 
					DrakonUtils.error("ОШИБКА! У иконы Начало \"" + DrakonUtils.getOutDegree(cur_node) + "\" выходов!"); 
					//--dg-- //--dg--         
					return; 
				}
				}
		} else {
			//--dg-- Запускаем graph на //--dg-- выполнение по одному
			activate_drakon_single(graph);  
		}
		//--dg-- //--dg--             
		}
	//--dg-- Запускаем graph на выполнение по одному
	protected void activate_drakon_single(Graph graph) {
 
		//--dg-- Проходим по всем узлам
		for (Vertex v : graph.getVertices()) {
			//--dg-- узел НАЧАЛО?
			if(DrakonUtils.getIconType(v).equals(DrakonUtils.DI_DG_BEG)) {
				//--dg-- Выполняем силуэт от ТС
				activate_siluet(v); 
			} else {
			}
			}
		//--dg-- -- Схема построена
		DrakonUtils.message("--- Конец активности "); 
		//--dg-- //--dg--             
		}
	//--dg-- Выполняем силуэт от ТС
	protected void activate_siluet(Vertex v) {
 
		//--dg-- извлекаем из Начало тип реальности
		CURRENT_RELEASE = (String) DrakonUtils.getCode(v); 
		//--dg-- ---
		DrakonUtils.message("--->Текущая реальность из Акт: " + CURRENT_RELEASE); 
		//--dg-- у текущего узла один выход?
		if(DrakonUtils.getOutDegree(v) == 1) {
			//--dg-- теперь текущий узел тот на который указывает выход
			v = DrakonUtils.getOutNode(v,0);
 
			//--dg-- ---
			DrakonUtils.message("---> Разбираем силуэт "); 
			//--dg-- Разбираем силуэт
			parseSiluet(v); 
		} else {
			//--dg-- у текущего узла нет выходов?
			if(DrakonUtils.getOutDegree(v) == 0) {
			} else {
				//--dg-- ОШИБКА! У иконы Начало ...//--dg--  выходов!
				DrakonUtils.error("ОШИБКА! У иконы Начало \"" + DrakonUtils.getOutDegree(v) + "\" выходов!"); 
				//--dg-- //--dg--         
				return; 
			}
		}
		//--dg-- -- Схема построена
		DrakonUtils.message("--- Конец активности "); 
		//--dg-- //--dg--             
		}
	//--dg-- Парсер одного силуэта
	public void parseSiluet(Vertex node) {
/**
 * Парсер одного силуэта ДРАКОНА 
 * @param	var graph_data
 */ 
		//--dg-- переменные
		String file_nm = "";
String it = DrakonUtils.getIconType(node);
DrakonGen2 dg = null;

 
		//--dg-- -psi- n: 
		DrakonUtils.debug("-psi- n: "+DrakonUtils.getComment(node)); 
		//--dg-- тип узла
		switch(it) {
			//--dg-- неизвестный тип
			default:
				//--dg-- Неизвестный тип активности ...
				DrakonUtils.error("Неизвестный тип активности  \"" + DrakonUtils.getIconType(node) + "\" узла  \"" + DrakonUtils.getComment(node) + " \".\n"); 
				//--dg-- //--dg--         
				return; 
			//--dg--  DI_SI_BEG
			case DrakonUtils.DI_SI_BEG:
				//--dg-- создаем новый DrakonGen
				dg = new DrakonGen2();
 
				//--dg-- устанавливаем текущую реальность
				dg.setCurRelease(CURRENT_RELEASE);	
 
				//--dg-- ---
				DrakonUtils.message("--->Текущая реальность из Начало: " + CURRENT_RELEASE); 
				//--dg-- производим разбор силуэта
				dg.parseSiluet(node);

 
				//--dg-- break 
				break; 
			//--dg--  DG_LIFT_UP
			case DI_DG_LIFT_UP:
				//--dg-- получаем вход который не НАЧАЛО
				in = DrakonUtils.getInNode(node,0);
if(DrakonUtils.getIconType(in).equals(DrakonUtils.DI_DG_BEG)) {
in = DrakonUtils.getInNode(node,1);
}
 
				//--dg-- на входе икона ФАЙЛ?
				if(DrakonUtils.getIconType(in).equals(DI_FILE)) {
				} else {
					//--dg-- На входе DG_LIFT_UP должна быть икона FILE
					DrakonUtils.error("На входе DG_LIFT_UP \"" + DrakonUtils.getComment(in) + "\" должна быть икона типа FILE  !"); 
					//--dg-- //--dg--         
					return; 
				}
				//--dg-- получаем выход 
				Vertex out = DrakonUtils.getOutNode(node,0); 
				//--dg-- на выходе икона ФАЙЛ?
				if(DrakonUtils.getIconType(out).equals(DI_FILE)) {
				} else {
					//--dg-- На выходе DG_LIFT_UP должна быть икона FILE
					DrakonUtils.error("На выходе DG_LIFT_UP \"" + DrakonUtils.getComment(in) + "\" должна быть икона типа FILE  !"); 
					//--dg-- //--dg--         
					return; 
				}
				//--dg-- try
				try { 
				//--dg-- получаем имя входного файла со схемой
				file_nm = DrakonUtils.getCode(in); 
				//--dg-- считаем что имя файла относительно BASE_DIR
				file_nm = Settings.getProperty("BASE_DIR") + file_nm; 
				//--dg-- производим лексический разбор
				CompilationUnit cu = JavaParser.parse(new File(file_nm));
	
 
				//--dg-- получаем текст GML
				String res = cu.toGraphml();
	
 
				//--dg-- получаем имя выходного файла кодограммы
				file_nm = DrakonUtils.getCode(out); 
				//--dg-- считаем что имя файла относительно BASE_DIR
				file_nm = Settings.getProperty("BASE_DIR") + file_nm; 
				//--dg-- записываем текст в выходной файл кодограммы
				FileUtils.fileWrite(file_nm, res);
 
				//--dg-- ---
				System.out.println(" ---> Записали файл "+ file_nm);

 
				//--dg-- catch
				} catch(Exception e) {
	System.err.println("Error on file: "+ file_nm + " " +e.getMessage());
	e.printStackTrace();
	return;
} 
 
				//--dg-- break
				break; 
			//--dg--  DG_LIFT_DOWN
			case DI_DG_LIFT_DOWN:
				//--dg-- получаем вход который не НАЧАЛО
				in = DrakonUtils.getInNode(node,0);
if(DrakonUtils.getIconType(in).equals(DrakonUtils.DI_DG_BEG)) {
in = DrakonUtils.getInNode(node,1);
}
 
				//--dg-- на входе икона ФАЙЛ?
				if(DrakonUtils.getIconType(in).equals(DI_FILE)) {
				} else {
					//--dg-- На входе DG_LIFT_DOWN должна быть икона FILE
					DrakonUtils.error("На входе DG_LIFT_DOWN \"" + DrakonUtils.getComment(in) + "\" должна быть икона типа FILE  !"); 
					//--dg-- //--dg--         
					return; 
				}
				//--dg-- try
				try { 
				//--dg-- получаем имя входного файла со схемой
				file_nm = DrakonUtils.getCode(in); 
				//--dg-- считаем что имя файла относительно BASE_DIR
				file_nm = Settings.getProperty("BASE_DIR") + file_nm; 
				//--dg-- читаем файл GML ридером
				Graph graph = new TinkerGraph();
GraphMLReader reader = new GraphMLReader(file_nm, graph);
reader.read();
	
 
				//--dg-- ---
				System.out.println(" ---> Прочитали файл "+ file_nm);

 
				//--dg-- создаем новый DrakonGen
				dg = new DrakonGen2();
 
				//--dg-- устанавливаем текущую реальность
				dg.setCurRelease(CURRENT_RELEASE);	
 
				//--dg-- производим генерацию кода из кодограммы
				dg.parse_drakon(graph);
 
				//--dg-- catch
				} catch(Exception e) {
	System.err.println("Error on file: "+ file_nm + " " +e.getMessage());
	e.printStackTrace();
	return;
} 
 
				//--dg-- break
				break; 
		}
		//--dg-- //--dg--             
		}
	//--dg-- 
            
	} //-- конец класса
 
