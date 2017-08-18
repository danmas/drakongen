 
	/**
  * Этот текст сгенерирован программой DrakonGen2
  * @author Erv +
*/ 
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
import org.apache.log4j.Logger;
 
	public class DrakonAct {
	final static Logger logger = Logger.getLogger(DrakonAct.class); 
	public final static String DI_OS_ACTION = "OS_ACTION";
public final static String DI_DG_LIFT_DOWN = "DG_LIFT_DOWN";
public final static String DI_DG_LIFT_UP = "DG_LIFT_UP";
public final static String DI_FILE = "FILE";
public final static String DI_TRAIL_ACTION = "TRAIL_ACTION";
public final static String DI_INPIT_J = "TRAIL_ACTION";
public final static String DI_TRAIL_INPUT_J = "TRAIL_INPUT_J";

	public static enum IcTypes {
		DI_OS_ACTION, DI_DG_LIFT_UP, DI_DG_LIFT_DOWN, DI_FILE;
	};
 
	protected String CURRENT_RELEASE = DrakonUtils.RELEASE_TYPE_CODE_AS;

//private static final String BASE_DIR = "..\\..\\..\\WRK\\DG2J\\DragonGen2J\\";
private static final String MAIN_DG_FILE = "../../../WRK/DG2J/Schemes/Main.graphml";
Vertex in;
String acts_result = "";
 
	public DrakonAct() { 
		}
	public static void main(String[] args) { 
		Settings.setProperty("BASE_DIR", "..\\..\\..\\WRK\\DG2J\\DragonGen2J\\"); 
		DrakonAct da 
		= new DrakonAct(); 
		Graph graph = new TinkerGraph();

GraphMLReader reader = new GraphMLReader(MAIN_DG_FILE, graph);

try {
	reader.read();
	System.out.println(" <--- Прочитали файл "+ MAIN_DG_FILE);
} catch(Exception e) {
	System.err.println(" err " +e.getMessage());
	e.printStackTrace();
} 
		da.activate_drakon(graph); 
		}
	protected void activate_drakon(Graph graph) {
 
		Vertex cur_node = null;
Vertex node = null;
 
		for (Vertex v : graph.getVertices()) {
			//--dg-- узел ЗАПУСК АКТИВНОСТЕЙ?
			if(DrakonUtils.isIconType(v,DrakonUtils.DI_START_ACTS)) {
				cur_node = v;
 
				break; 
			} else {
			}
			}
		//--dg-- нашли узел ЗАПУСКА?
		if(cur_node != null) {
			for(int i=0;i<DrakonUtils.getInDegree(cur_node);i++) {
				node = DrakonUtils.getInNode(cur_node,i);
 
				//--dg-- узел ЗАМЕНЫ?
				if(DrakonUtils.isIconType(node,DrakonUtils.DI_SUBSTITUTES)) {
					make_substitutions(node); 
				} else {
				}
				}
			while(DrakonUtils.getOutDegree(cur_node) == 1) {
				cur_node = DrakonUtils.getOutNode(cur_node,0);
 
				//--dg-- узел НАЧАЛО?
				if(DrakonUtils.isIconType(cur_node,DrakonUtils.DI_DG_BEG)) {
				} else {
					DrakonUtils.error("ОШИБКА! У иконы ЗАПУСК АКТИВНОСТЕЙ неправильный тип выхода \"" + DrakonUtils.getIconType(cur_node) + "\""); 
					return; 
				}
				Vertex act_node = null; 
String name = DrakonUtils.getComment(cur_node);				
for (Vertex v : graph.getVertices()) {
	//-- узел НАЧАЛО?
	if(v!= cur_node && DrakonUtils.isIconType(v,DrakonUtils.DI_DG_BEG)) {
		if(DrakonUtils.getComment(v).equals(name)) {
			act_node = v;
			break;
		}
	} else {
	}
} 
				//--dg-- нашли?
				if(act_node != null) {
					activate_siluet(act_node); 
				} else {
					DrakonUtils.error("ОШИБКА! У иконы Начало \"" + DrakonUtils.getOutDegree(cur_node) + "\" выходов!"); 
					return; 
				}
				}
		} else {
			activate_drakon_single(graph);  
		}
		}
	protected void make_substitutions(Vertex node) {
 
		Vertex cur_node = null;
String file_nm;
 
		DrakonUtils.fillSubst(node);
 
		for(int i=0;i<DrakonUtils.getInDegree(node);i++) {
			cur_node = DrakonUtils.getInNode(node,i);
 
			//--dg-- узел ЗАМЕНЫ?
			if(DrakonUtils.isIconType(cur_node,DrakonUtils.DI_SUBSTITUTES)) {
				DrakonUtils.fillSubst(cur_node);
 
			} else {
				//--dg-- узел ФАЙЛ?
				if(DrakonUtils.isIconType(cur_node,DI_FILE)) {
					file_nm = DrakonUtils.getCode(cur_node); 
					file_nm = Settings.getProperty("BASE_DIR") + file_nm; 
					DrakonUtils.fillSubstFromNode(file_nm);
 
				} else {
					DrakonUtils.error("Предупреждение: узел не может быть входящим для ПОДСТАНОВКИ \"" + DrakonUtils.getIconType(cur_node) + "\"   \"" + DrakonUtils.getComment(cur_node) + " \".\n"); 
				}
			}
			}
		}
	protected void activate_drakon_single(Graph graph) {
 
		for (Vertex v : graph.getVertices()) {
			//--dg-- узел НАЧАЛО?
			if(DrakonUtils.isIconType(v,DrakonUtils.DI_DG_BEG)) {
				activate_siluet(v); 
			} else {
			}
			}
		DrakonUtils.message("--- Конец активности "); 
		}
	protected void activate_siluet(Vertex v) {
 
		CURRENT_RELEASE = (String) DrakonUtils.getCode(v); 
		DrakonUtils.message("--->Текущая реальность из Акт: " + CURRENT_RELEASE); 
		//--dg-- у текущего узла один выход?
		if(DrakonUtils.getOutDegree(v) == 1) {
			v = DrakonUtils.getOutNode(v,0);
 
			DrakonUtils.message("---> Разбираем силуэт "); 
			parseSiluet(v); 
		} else {
			//--dg-- у текущего узла нет выходов?
			if(DrakonUtils.getOutDegree(v) == 0) {
			} else {
				DrakonUtils.error("ОШИБКА! У иконы Начало \"" + DrakonUtils.getOutDegree(v) + "\" выходов!"); 
				return; 
			}
		}
		DrakonUtils.message("--- Конец активности "); 
		}
	public void parseSiluet(Vertex node) {
/**
 * Парсер одного силуэта ДРАКОНА 
 * @param	var graph_data
 */ 
		String file_nm = "";
String it = DrakonUtils.getIconType(node);
DrakonGen2 dg = null;
Vertex out = null;
String result = "";
boolean out_to_file = false; 
		//--dg-- узел DG_LIFT_DOWN?
		if(DrakonUtils.isIconType(node,DI_DG_LIFT_DOWN)) {
			in = DrakonUtils.getInNode(node,0);
if(DrakonUtils.isIconType(in,DrakonUtils.DI_DG_BEG)) {
in = DrakonUtils.getInNode(node,1);
}
 
			//--dg-- на входе икона ФАЙЛ?
			if(DrakonUtils.isIconType(in,DI_FILE)) {
			} else {
				DrakonUtils.error("На входе DG_LIFT_DOWN \"" + DrakonUtils.getComment(in) + "\" должна быть икона типа FILE  !"); 
				return; 
			}
			try { 
			file_nm = DrakonUtils.getCode(in); 
			file_nm = Settings.getProperty("BASE_DIR") + file_nm; 
			Graph graph = new TinkerGraph();
GraphMLReader reader = new GraphMLReader(file_nm, graph);
reader.read();
	
 
			System.out.println(" ---> Прочитали файл "+ file_nm);

 
			dg = new DrakonGen2();
 
			dg.setCurRelease(CURRENT_RELEASE);	
 
			dg.parse_drakon(graph);
 
			} catch(Exception e) {
	System.err.println("Error on file: "+ file_nm + " " +e.getMessage());
	e.printStackTrace();
	return;
} 
 
		} else {
			//--dg-- узел DG_LIFT_UP?
			if(DrakonUtils.isIconType(node,DI_DG_LIFT_UP)) {
				in = DrakonUtils.getInNode(node,0);
if(DrakonUtils.isIconType(in,DrakonUtils.DI_DG_BEG)) {
in = DrakonUtils.getInNode(node,1);
}
 
				//--dg-- на входе икона ФАЙЛ?
				if(DrakonUtils.isIconType(in,DI_FILE)) {
				} else {
					DrakonUtils.error("На входе DG_LIFT_UP \"" + DrakonUtils.getComment(in) + "\" должна быть икона типа FILE  !"); 
					return; 
				}
				out = DrakonUtils.getOutNode(node,0); 
				//--dg-- на выходе икона ФАЙЛ?
				if(DrakonUtils.isIconType(out,DI_FILE)) {
				} else {
					DrakonUtils.error("На выходе DG_LIFT_UP \"" + DrakonUtils.getComment(in) + "\" должна быть икона типа FILE  !"); 
					return; 
				}
				try { 
				file_nm = DrakonUtils.getCode(in); 
				file_nm = Settings.getProperty("BASE_DIR") + file_nm; 
				CompilationUnit cu = JavaParser.parse(new File(file_nm));
	
 
				String res = cu.toGraphml();
	
 
				file_nm = DrakonUtils.getCode(out); 
				file_nm = Settings.getProperty("BASE_DIR") + file_nm; 
				FileUtils.fileWrite(file_nm, res);
 
				System.out.println(" ---> Записали файл "+ file_nm);

 
				} catch(Exception e) {
	System.err.println("Error on file: "+ file_nm + " " +e.getMessage());
	e.printStackTrace();
	return;
} 
 
			} else {
				//--dg-- узел DI_TRAIL_ACTION?
				if(DrakonUtils.isIconType(node,DI_TRAIL_ACTION)) {
					in = DrakonUtils.getInNode(node,0);
if(DrakonUtils.isIconType(in,DrakonUtils.DI_DG_BEG)) {
in = DrakonUtils.getInNode(node,1);
}
 
					//--dg-- на входе икона TRAIL_INPUT_J?
					if(DrakonUtils.isIconType(in,DI_TRAIL_INPUT_J)) {
					} else {
						DrakonUtils.error("На входе TRAIL_ACTION \"" + DrakonUtils.getComment(in) + "\" должна быть икона типа TRAIL_INPUT_J  !"); 
						return; 
					}
					out = DrakonUtils.getOutNode(node,0); 
					//--dg-- на выходе икона ФАЙЛ?
					if(DrakonUtils.isIconType(out,DI_FILE)) {
						out_to_file 
						= true; 
						file_nm = DrakonUtils.getCode(out); 
						file_nm = Settings.getProperty("BASE_DIR") + file_nm; 
					} else {
						out_to_file 
						= false; 
					}
					try { 
					String input_j = DrakonUtils.getCode(in); 
					 try {
	 cb.dfs.trail.TrailManager trail = new cb.dfs.trail.TrailManager();
	result = trail.launchTrailFromJstring(input_j);
} catch (Exception ex) {
	result = ex.getMessage();
	System.err.println("---" + ex.getMessage());
} 
 
					//--dg-- выводить результат в файл?
					if(out_to_file) {
						FileUtils.fileWrite(file_nm, result);
 
					} else {
						acts_result += result;
 
					}
					} catch(Exception e) {
	System.err.println("Error on file: "+ file_nm + " " +e.getMessage());
	e.printStackTrace();
	return;
} 
 
				} else {
					//--dg-- узел DI_FILE?
					if(DrakonUtils.isIconType(node,DI_FILE)) {
						file_nm = DrakonUtils.getCode(node); 
						file_nm = Settings.getProperty("BASE_DIR") + file_nm; 
						FileUtils.fileWrite(file_nm, acts_result);
 
					} else {
						//--dg-- узел DI_SI_BEG?
						if(DrakonUtils.isIconType(node,DrakonUtils.DI_SI_BEG)) {
							dg = new DrakonGen2();
 
							dg.setCurRelease(CURRENT_RELEASE);	
 
							DrakonUtils.message("--->Текущая реальность из Начало: " + CURRENT_RELEASE); 
							dg.parseSiluet(node);
 
						} else {
							DrakonUtils.error("Неизвестный тип активности  \"" + DrakonUtils.getIconType(node) + "\" узла  \"" + DrakonUtils.getComment(node) + " \".\n"); 
							return; 
						}
					}
				}
			}
		}
		}
	} //-- конец класса
 
