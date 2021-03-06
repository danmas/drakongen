
//-- Класс DrakonAct
	//-- упоминание о DrakonGen2
	/**
  * Этот текст сгенерирован программой DrakonGen2
  * @author Erv +
*/ 
	//-- package//-- imports
	package ru.erv.drakongen.reserv;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;

import ru.erv.drakongen.DrakonAct;
import ru.erv.drakongen.DrakonGen2;
import ru.erv.drakongen.DrakonUtils;
import ru.erv.drakongen.Settings;
import ru.erv.drakongen.DrakonAct.IcTypes;
import ru.erv.drakongen.utils.*;
import ru.erv.drakongen.parser.GraphMLReader;

import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;

import japa.parser.ast.CompilationUnit;

import java.io.File;

import japa.parser.JavaParser;
 
	//-- class DrakonAct
	 public class DrakonActGood { 
	//-- константы
	public final static String DI_OS_ACTION = "OS_ACTION";
public final static String DI_DG_LIFT_DOWN = "DG_LIFT_DOWN";
public final static String DI_DG_LIFT_UP = "DG_LIFT_UP";
public final static String DI_FILE = "FILE";
 
	//-- переменные
	protected String CURRENT_RELEASE = DrakonUtils.RELEASE_TYPE_CODE_AS;

//private static final String BASE_DIR = "..\\..\\..\\WRK\\DG2J\\DragonGen2J\\";
private static final String MAIN_DG_FILE = "../../../WRK/DG2J/Schemes/Main.graphml";
Vertex in;

 
	//-- Конструктор
	public DrakonActGood() { 
		//-- //--             
	} 

	//-- Запускаем graph на выполнение
	protected void activate_drakon(Graph graph) {
 
		//-- Проходим по всем узлам
		for (Vertex v : graph.getVertices()) {
			//-- узел НАЧАЛО?
			if(DrakonUtils.getIconType(v).equals(DrakonUtils.DI_DG_BEG)) {
				//-- извлекаем из Начало тип реальности
				CURRENT_RELEASE = (String) DrakonUtils.getCode(v); 
				//-- ---
				DrakonUtils.message("--->Текущая реальность: " + CURRENT_RELEASE); 
				//-- у текущего узла один выход?
				if(DrakonUtils.getOutDegree(v) == 1) {
					//-- теперь текущий узел тот на который указывает выход
					v = DrakonUtils.getOutNode(v,0);
 
					//-- ---
					DrakonUtils.message("---> Разбираем силуэт "); 
					//-- Разбираем силуэт
					parseSiluet(v); 
				} else {
					//-- у текущего узла нет выходов?
					if(DrakonUtils.getOutDegree(v) == 0) {
					} else {
						//-- ОШИБКА! У иконы Начало ...//--  выходов!
						DrakonUtils.error("ОШИБКА! У иконы Начало \"" + DrakonUtils.getOutDegree(v) + "\" выходов!"); 
						//-- //--         
						return; 
					}
				}
			} else {
			}
		}
		//-- -- Схема построена
		DrakonUtils.message("--- Конец активности "); 
		//-- //--             
	} 

	//-- Парсер одного силуэта
	public void parseSiluet(Vertex node) {
/**
 * Парсер одного силуэта ДРАКОНА 
 * @param	var graph_data
 */ 
		//-- переменные
		String file_nm = ""; 
		//-- -psi- n: 
		DrakonUtils.debug("-psi- n: "+DrakonUtils.getComment(node)); 
		//-- тип узла
		IcTypes ic_type = IcTypes.valueOf(DrakonUtils.getIconType(node));
		switch(ic_type) {
			//-- неизвестный тип
			default:
				//-- Неизвестный тип активности ...
				DrakonUtils.error("Неизвестный тип активности \"" + DrakonUtils.getComment(node) + " \".\n"); 
				//-- //--         
				return; 
			//-- DG_LIFT_UP
			case DI_DG_LIFT_UP:
				//-- получаем вход который не НАЧАЛО
				in = DrakonUtils.getInNode(node,0);
if(DrakonUtils.getIconType(in).equals(DrakonUtils.DI_DG_BEG)) {
in = DrakonUtils.getInNode(node,1);
}
 
				//-- на входе икона ФАЙЛ?
				if(DrakonUtils.getIconType(in).equals(DI_FILE)) {
				} else {
					//-- На входе DG_LIFT_UP должна быть икона FILE
					DrakonUtils.error("На входе DG_LIFT_UP \"" + DrakonUtils.getComment(in) + "\" должна быть икона типа FILE  !"); 
					//-- //--         
					return; 
				}
				//-- получаем выход 
				Vertex out = DrakonUtils.getOutNode(node,0); 
				//-- на выходе икона ФАЙЛ?
				if(DrakonUtils.getIconType(out).equals(DI_FILE)) {
				} else {
					//-- На выходе DG_LIFT_UP должна быть икона FILE
					DrakonUtils.error("На выходе DG_LIFT_UP \"" + DrakonUtils.getComment(in) + "\" должна быть икона типа FILE  !"); 
					//-- //--         
					return; 
				}
				//-- try
				try { 
				//-- получаем имя входного файла со схемой
				file_nm = DrakonUtils.getCode(in); 
				//-- считаем что имя файла относительно BASE_DIR
				file_nm = Settings.getProperty("BASE_DIR") + file_nm; 
				//-- производим лексический разбор
				CompilationUnit cu = JavaParser.parse(new File(file_nm));
	
 
				//-- получаем текст GML
				String res = cu.toGraphml();
	
 
				//-- получаем имя выходного файла кодограммы
				file_nm = DrakonUtils.getCode(out); 
				//-- считаем что имя файла относительно BASE_DIR
				file_nm = Settings.getProperty("BASE_DIR") + file_nm; 
				//-- записываем текст в выходной файл кодограммы
				FileUtils.fileWrite(file_nm, res);
 
				//-- ---
				System.out.println(" ---> Записали файл "+ file_nm);

 
				//-- catch
				} catch(Exception e) {
	System.err.println("Error on file: "+ file_nm + " " +e.getMessage());
	e.printStackTrace();
	return;
} 
 
				//-- break
				break; 
			//-- DG_LIFT_DOWN
			case DI_DG_LIFT_DOWN:
				//-- получаем вход который не НАЧАЛО
				in = DrakonUtils.getInNode(node,0);
if(DrakonUtils.getIconType(in).equals(DrakonUtils.DI_DG_BEG)) {
in = DrakonUtils.getInNode(node,1);
}
 
				//-- на входе икона ФАЙЛ?
				if(DrakonUtils.getIconType(in).equals(DI_FILE)) {
				} else {
					//-- На входе DG_LIFT_DOWN должна быть икона FILE
					DrakonUtils.error("На входе DG_LIFT_DOWN \"" + DrakonUtils.getComment(in) + "\" должна быть икона типа FILE  !"); 
					//-- //--         
					return; 
				}
				//-- try
				try { 
				//-- получаем имя входного файла со схемой
				file_nm = DrakonUtils.getCode(in); 
				//-- считаем что имя файла относительно BASE_DIR
				file_nm = Settings.getProperty("BASE_DIR") + file_nm; 
				//-- читаем файл GML ридером
				Graph graph = new TinkerGraph();
GraphMLReader reader = new GraphMLReader(file_nm, graph);
reader.read();
	
 
				//-- ---
				System.out.println(" ---> Прочитали файл "+ file_nm);

 
				//-- производим генерацию кода из кодограммы
				DrakonGen2 dg = new DrakonGen2();
dg.parse_drakon(graph);
 
				//-- catch
				} catch(Exception e) {
	System.err.println("Error on file: "+ file_nm + " " +e.getMessage());
	e.printStackTrace();
	return;
} 
 
				//-- break
				break; 
		}
		//-- //--             
	} 

	//-- main
	public static void main(String[] args) { 
		//-- устанавливаем BASE_DIR
		Settings.setProperty("BASE_DIR", "..\\..\\..\\WRK\\DG2J\\DragonGen2J\\"); 
		//-- переменная da
		DrakonAct da 
		//-- экземпляр класса DrakonAct
		= new DrakonAct(); 
		//-- строим Graph из файла Маin
		Graph graph = new TinkerGraph();

GraphMLReader reader = new GraphMLReader(MAIN_DG_FILE, graph);

try {
	reader.read();
	System.out.println(" <--- Прочитали файл "+ MAIN_DG_FILE);
} catch(Exception e) {
	System.err.println(" err " +e.getMessage());
	e.printStackTrace();
} 
		//-- выполнение активностей
		//da.activate_drakon(graph); 
		//-- //--             
	} 

	//-- 
            
	} //-- конец класса
 
