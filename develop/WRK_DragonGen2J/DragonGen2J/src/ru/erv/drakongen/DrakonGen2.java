
	/**
  * Этот текст сгенерирован программой DrakonGen2
  * @author Roman Eremeev
*/ 
	package ru.erv.drakongen;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import ru.erv.drakongen.utils.*;
import ru.erv.drakongen.*; 
 
	public class DrakonGen2 { 
		public final static String DI_EXT_NEXT = "next";
	public final static String DI_DG_BEG = "DG_BEG";
	public final static String DI_SI_BEG = "SI_BEG";
	public final static String DI_SI_END = "SI_END";
	public final static String DI_CLASS_END = "CLASS_END";
	public final static String DI_COMPIL_BEG = "COMPIL_BEG";
	public final static String DI_COMPIL_END = "COMPIL_END";
	public final static String DI_SH_BEG = "SH_BEG";
	public final static String DI_SH_END = "SH_END"; 
	public final static String DI_PROC_BEG = "PROC_BEG";
	public final static String DI_PROC_END = "PROC_END"; 
	public final static String DI_WR_RES_FILE = "WR_RES_FILE";
	public final static String DI_AC = "AC";		
	public final static String DI_ACTION = "ACTION";		
	public final static String DI_SUB_COMPIL = "SUB_COMPIL";		
	//public final static String DI_IR = "IR";		
	//TODO: удалить DI_ID и DI_IR
	//public final static String DI_EQL = "EQL";		
	//public final static String DI_EQR = "EQR";		
	public final static String DI_IF = "IF";		
	public final static String DI_RY = "RY";		
	public final static String DI_DN = "DN";		
	public final static String DI_EI = "EI";		
	public final static String DI_UK = "UK";		
	public final static String DI_FOR_BEG = "FOR_BEG";		
	public final static String FOR_EACH_BEG = "FOR_EACH_BEG";		
	public final static String DI_FOR_END = "FOR_END";		
	public final static String DI_REF = "REF";
	public final static String DI_BREAK = "BREAK";
	
	public final static String DI_CASE = "CASE";
	public final static String DI_DEFAULT = "DEFAULT";
	public final static String DI_SW = "SWITCH";
	public final static String DI_RETURN = "RETURN";
	public final static String DI_INSERT = "INSERT";
	public final static String DI_OUTPUT = "OUTPUT";
	public final static String DI_NATIVE_CODE = "NATIVE_CODE";
	
	public final static String RELEASE_TYPE_CODE_JAVA = "CODE_JAVA";
	public final static String RELEASE_TYPE_CODE_AS = "CODE_AS";
 
	protected boolean is_write_comment = false; //-- добавлять или нет коментарий в код

protected String res_str = "";
protected boolean load_finish = false;
protected String CURRENT_RELEASE = null;
protected String commentPrefix = "//-- ";
 // RELEASE_TYPE_CODE_AS; 
	public  DrakonGen2() { 
		}
	public String parse_drakon(Graph graph) {
	Object data;
	String descr;
	String di_type;
	
	res_str = ""; 
		for (Vertex v : graph.getVertices()) {
			di_type = DrakonUtils.getIconType(v); ;
 
			//--dg-- узел НАЧАЛО?
			if(di_type != null && di_type.contains(DI_DG_BEG)) {
				setCurReleaseFromNode(v); 
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
						return null; 
					}
				}
			} else {
			}
			}
		DrakonUtils.message("//-- Схема построена\n\n "); 
		return res_str;
}


	public void parseSiluet(Vertex node) {
/**
 * Парсер одного силуэта ДРАКОНА 
 * @param	var graph_data
 */ 
		Vertex cur_node = node;
String comment = DrakonUtils.getComment(cur_node);
String di_type;
String code = "";
Vertex next_node;
Vertex next_node2 = null;
int level = 0;
String str = "";  
		DrakonUtils.debug("-psi- n: "+DrakonUtils.getComment(cur_node)); 
		cur_node = node;
code = geReleaseCode(cur_node);
di_type = DrakonUtils.getIconType(cur_node);
comment = DrakonUtils.getComment(cur_node); 
		//--dg-- узел НАЧАЛО СИЛУЭТА?
		if(di_type.contains(DI_SI_BEG) || di_type.contains(DI_COMPIL_BEG)) {
			//--dg-- Проверка выходов проходит?
			if(isCheckOutputs(cur_node)) {
			} else {
				str = "ОШИБКА! НЕ ПРОШЛА ПРОВЕРКА ВЫХОДОВ для иконы " + comment +" (тип " + di_type+")";
res_str += str; 
				DrakonUtils.error(str); 
				return; 
			}
		} else {
			str = "ОШИБКА! Первый узел шампура должен быть \"" + DI_SI_BEG + "\"!\n"
+ "А узел " + comment +" имеет тип " + di_type;
res_str += str; 
			DrakonUtils.error(str); 
			return; 
		}
		if(comment != null)	
	comment = comment.replace("\n",commentPrefix);
if(comment != null && is_write_comment) 
	res_str += "\n"+commentPrefix + comment + "\n";
if(code != null)	
	res_str += code + "\n"; 
		//--dg-- выходных ребер не 1 и не 2?
		if(DrakonUtils.getOutDegree(cur_node) != 1 && DrakonUtils.getOutDegree(cur_node) != 2) {
			str = "ОШИБКА! У иконы Начало Силуэта \"" + DrakonUtils.getOutDegree(cur_node) + "\" выходов!\n"
+ " Должно быть 1 или 2.\n"; 
			DrakonUtils.error(str); 
			return; 
		} else {
		}
		//--dg-- выходных ребер 2?
		if(DrakonUtils.getOutDegree(cur_node) == 2) {
			Vertex v = DrakonUtils.getOutNode(cur_node,0);
di_type = DrakonUtils.getIconType(v);
 
			//--dg-- на первом выходе ЗАПИСЬ В ФАЙЛ?
			if(di_type.contains(DI_WR_RES_FILE)) {
				next_node = DrakonUtils.getOutNode(cur_node,0);
cur_node = DrakonUtils.getOutNode(cur_node,1); 
			} else {
				next_node = DrakonUtils.getOutNode(cur_node,1);
cur_node = DrakonUtils.getOutNode(cur_node,0); 
			}
		} else {
			DrakonUtils.error("ОШИБКА! У иконы Начало Силуэта  Должно быть 2 выхода"); 
			return; 
		}
		cur_node = 
		parceBegGroup(cur_node, level + 1); 
		//--dg-- тек.узел ЧАСТЬ СБОРКИ?
		if(DrakonUtils.isIconType(cur_node,DI_SUB_COMPIL)) {
			while(cur_node!=null && DrakonUtils.isIconType(cur_node,DI_SUB_COMPIL)) {
				code = geReleaseCode(cur_node);
di_type = DrakonUtils.getIconType(cur_node);
comment = DrakonUtils.getComment(cur_node); 
				if(comment != null && is_write_comment)
	res_str +=  commentPrefix + comment + "\n";
if(code != null) 
	res_str +=  code + " \n";
 
				//--dg-- у тек.узела 2 выхода?
				if(DrakonUtils.getOutDegree(cur_node) == 2
) {
					//--dg-- на первом выходе ЧАСТЬ СБОРКИ?
					if(di_type.contains(DI_SUB_COMPIL)) {
						next_node2 = DrakonUtils.getOutNode(cur_node,1);
cur_node = DrakonUtils.getOutNode(cur_node,0); 
					} else {
						next_node2 = DrakonUtils.getOutNode(cur_node,0);
cur_node = DrakonUtils.getOutNode(cur_node,1); 
					}
				} else {
					//--dg-- у тек.узела 1 выход?
					if(DrakonUtils.getOutDegree(cur_node) == 1
) {
						next_node2 = null;
cur_node = DrakonUtils.getOutNode(cur_node,0); 
					} else {
						str = "Ошибка! Ошибка! У иконы ЧАСТЬ СБОРКИ \"" + comment + "\" ("+ DrakonUtils.getIconType(cur_node) + ") должен быть один или два выхода.\n";
DrakonUtils.error(str); 
						return; 
					}
				}
				//--dg-- тип тек.узла НАЧАЛО(ШАМПУРАбПРОЦЕДУРЫ)?
				if(DrakonUtils.isIconType(cur_node,DI_SH_BEG) || DrakonUtils.isIconType(cur_node,DI_PROC_BEG)) {
				} else {
					cur_node = 
					parceBegGroup(cur_node, level + 1); 
				}
				parceShampur(cur_node, level + 1);
 
				cur_node = 
				next_node2; 
				}
		} else {
			//--dg-- тип тек.узла НАЧАЛО (ШАМПУРА,ПРОЦЕДУРЫ)?
			if(DrakonUtils.isIconType(cur_node,DI_SH_BEG) || DrakonUtils.isIconType(cur_node,DI_PROC_BEG)) {
				parceShampur(cur_node, level + 1);
 
			} else {
				//--dg-- тек узел БЛОК КОДА?
				if(DrakonUtils.getIconType(cur_node).equals(DI_NATIVE_CODE) ) {
					parceSheet(cur_node,level); 
				} else {
					//--dg-- тек. узел. КОНЕЦ СИЛУЭТА?
					if(DrakonUtils.isIconType(cur_node,DI_SI_END)) {
						parceShampur(cur_node, level + 1);
 
					} else {
						str = "Ошибка! НЕИЗВЕСТНЫЙ ТИП ИКОНЫ \"" + comment + "\" ("+ DrakonUtils.getIconType(cur_node) + ") должен быть ЧАСТЬ СБОРКИ или НАЧАЛО ПРОЦЕДУРЫ. !n";
DrakonUtils.error(str); 
					}
				}
			}
		}
		//--dg-- есть след.узел?
		if(next_node != null) {
			//--dg-- узел ЗАПИСЬ В ФАЙЛ?
			if(DrakonUtils.isIconType(next_node,DI_WR_RES_FILE)) {
				String file_name 
				= Settings.getProperty("BASE_DIR") + "\\" +  getCleanReleaseCode(next_node);
 
				//--dg-- Имя файла задано?
				if(file_name != null && file_name.length() > 0) {
				} else {
					str = "ОШИБКА. В иконе  ЗАПИСЬ ФАЙЛА не задано имя выходного файла.\n";
res_str += str;
 
					DrakonUtils.error(str); 
					file_name
 
					= "tmp.java";
 
				}
				DrakonUtils.message("----> Записываем файл " + file_name + "\n"); 
				FileUtils.fileWriteUTF8(file_name, res_str);
 
			} else {
			}
		} else {
			DrakonUtils.message("----> ПРЕДУПРЕЖДЕНИЕ. Результат не сохранен в файл.\n"); 
		}
		res_str = "";
 
		}


	protected Vertex parceBegGroup(Vertex cur_node, int _level) { 
		String comment = DrakonUtils.getComment(cur_node);
String di_type = DrakonUtils.getIconType(cur_node);
String code = geReleaseCode(cur_node);
String spaces = "";
Vertex term_yes;
String str;
Vertex node;
Vertex cur_node_d;  
		//--dg-- текущий узел null?
		if(cur_node == null) {
			return null; 
		} else {
		}
		for (int i = 0; i < _level; i++)  
	spaces += "\t";
 
		if(comment != null)	
	comment = comment.replace("\n",commentPrefix);
 
		//--dg-- тип текущего узла не задан?
		if(di_type == null || di_type.length() == 0) {
			DrakonUtils.error("ОШИБКА! Не задан тип иконы"); 
			return null; 
		} else {
		}
		switch(di_type) {
			default:
				str = "Ошибка! НЕИЗВЕСТНЫЙ ТИП ИКОНЫ \"" + comment + "\" ("+ di_type + ") В НАЧАЛЬНОЙ ГРУППЕ. !n";
DrakonUtils.error(str); 
				break; 
			case DI_PROC_BEG:
				case DI_SH_BEG:
					case DI_SUB_COMPIL:
						return cur_node; 
			case DI_ACTION:
				case DI_AC:
					case DI_NATIVE_CODE:
						if(comment != null && is_write_comment)
	res_str += spaces + commentPrefix + comment + "\n";
if(code != null) 
	res_str += spaces + code + " \n";
 
						//--dg-- есть выходы?
						if(DrakonUtils.getOutDegree(cur_node) >= 1) {
							for(int i2 = 0; i2 < DrakonUtils.getOutDegree(cur_node); i2++) {
								Edge e = DrakonUtils.getOutEdge(cur_node, i2);
 
								//--dg-- ребро ссылка-указатель?
								if(DrakonUtils.isReferenceEdge(e)) {
								} else {
									node 
									 = parceBegGroup(DrakonUtils.getOutNode(cur_node,i2), _level); 
									return node; 
								}
								}
						} else {
						}
						str = "ОШИБКА! У Действия \"" + comment + "\" должено быть выход!\n";
 
						res_str += spaces + str; 
						DrakonUtils.error(str); 
						return cur_node; 
			case DI_COMPIL_END:
				case DI_SI_END:
					if (comment != null && is_write_comment)
	res_str += spaces +commentPrefix + comment + "\n";
if (code != null)
res_str += spaces +code + "\n"; 
					return cur_node; 
		}
		return null;
}

	protected void parceShampur(Vertex node, int  _level) { 
		Vertex cur_node;
		String comment;
		String di_type;
		String code;
		String this_comment;
		String spaces = "";
		Vertex next_node; 
 
		for (int i = 0; i < _level; i++)  
    spaces += "\t";

 
		cur_node = node;
		code= geReleaseCode(cur_node);
		di_type = DrakonUtils.getIconType(cur_node);
		comment = DrakonUtils.getComment(cur_node);
this_comment = comment; 
 
		//--dg-- узел НАЧАЛО (ШАМПУРА,ПРОЦЕДУРЫ)?
		if(di_type.contains(DI_SH_BEG) || di_type.contains(DI_PROC_BEG)) {
		} else {
			//--dg-- узел КОНЕЦ (СИЛУЭТА,СБОРКИ,КЛАССА)?
			if(di_type.contains(DI_SI_END) ||
di_type.contains(DI_COMPIL_END) ||
di_type.contains(DI_CLASS_END)) {
				if(comment != null && is_write_comment)
	res_str += spaces + commentPrefix + comment + "\n";
if(code != null) 
	res_str += spaces + code + " \n";
 
				return; 
			} else {
				//--dg-- узел БЛОК КОДА?
				if(di_type.contains(DI_NATIVE_CODE)) {
				} else {
					String str = "ОШИБКА! Первый узел должен быть " + DI_SH_BEG + " а не "+ di_type + " икона "+comment+" !";
res_str += spaces + str;
 
					DrakonUtils.error(str); 
					return; 
				}
			}
		}
		if(comment != null && is_write_comment)
	res_str += spaces + commentPrefix + comment + "\n";
if(code != null) 
	res_str += spaces + code + " \n";
 
		//--dg-- выходных ребер 2?
		if(DrakonUtils.getOutDegree(cur_node) == 2) {
			Vertex v = DrakonUtils.getOutNode(cur_node,0);
di_type = DrakonUtils.getIconType(v);
 
			//--dg-- на первом выходе НАЧАЛО(ШАМПУРА,ПРОЦЕДУРЫ) или КОНЕЦ (СИЛУЭТА,СБОРКИ)//--dg-- или БЛОК КОДА?
			if(di_type.contains(DI_SH_BEG) || di_type.contains(DI_PROC_BEG) || di_type.contains(DI_SI_END) || di_type.equals(DI_COMPIL_END) ||
di_type.contains(DI_NATIVE_CODE)) {
				next_node = DrakonUtils.getOutNode(cur_node,0);
cur_node = DrakonUtils.getOutNode(cur_node,1); 
			} else {
				next_node = DrakonUtils.getOutNode(cur_node,1);
cur_node = DrakonUtils.getOutNode(cur_node,0); 
			}
		} else {
			//--dg-- выходных ребер 1?
			if(DrakonUtils.getOutDegree(cur_node) == 1) {
				cur_node = DrakonUtils.getOutNode(cur_node,0);
next_node = null; 
			} else {
				String str = "ОШИБКА! Число дочерних узлов у начала шампура не равно 1 или 2\n";
 
				DrakonUtils.error(str); 
				return; 
			}
		}
		Vertex term = parceNext(cur_node, _level + 1); 
		//--dg-- вернулся пустой терминатор?
		if(term == null) {
			String str = "ОШИБКА! в шампуре \"" + this_comment + "\" parceNext() вернул пусой терминатор\n";
res_str += str; 
			DrakonUtils.error(str); 
		} else {
		}
		//--dg-- есть след.узел?
		if(next_node != null) {
			//--dg-- тип след.узла НАЧАЛО(ШАМПУРА,ПРОЦЕДУРЫ)?
			if(DrakonUtils.isIconType(next_node,DI_SH_BEG) || DrakonUtils.isIconType(next_node,DI_PROC_BEG)) {
				parceShampur(next_node,_level); 
			} else {
				//--dg-- след узел узел БЛОК КОДА?
				if(DrakonUtils.isIconType(next_node,DI_NATIVE_CODE) ) {
					parceSheet(next_node,_level); 
				} else {
					//--dg-- след узел КОНЕЦ (СИЛУЭТА,СБОРКИ,КЛАССА)?
					if(DrakonUtils.isIconType(next_node,DI_SI_END) ||
DrakonUtils.isIconType(next_node,DI_COMPIL_END) ||
DrakonUtils.isIconType(next_node,DI_CLASS_END)
) {
						parceSheet(next_node,_level); 
					} else {
						DrakonUtils.error("ЧТО ЭТО ЗА УЗЕЛ В ПРОЦЕДУРЕ?"); 
					}
				}
			}
		} else {
		}
		}


	protected void parceSheet(Vertex node, int  _level) { 
		Vertex cur_node;
		String comment;
		String di_type;
		String code;
		String this_comment;
		String spaces = "";
		Vertex next_node; 
 
		for (int i = 0; i < _level; i++)  
			spaces += "\t"; 
		cur_node = node;
		code= geReleaseCode(cur_node);
		di_type = DrakonUtils.getIconType(cur_node);
		comment = DrakonUtils.getComment(cur_node);
this_comment = comment; 
 
		DrakonUtils.debug("-psh- n: "+DrakonUtils.getComment(cur_node)); 
		//--dg-- узел БЛОК КОДА?
		if(di_type.contains(DI_NATIVE_CODE)) {
		} else {
			//--dg-- узел КОНЕЦ (СИЛУЭТА,СБОРКИ,КЛАССА)?
			if(di_type.contains(DI_SI_END) ||
di_type.contains(DI_COMPIL_END) ||
di_type.contains(DI_CLASS_END)) {
				if(comment != null && is_write_comment)
	res_str += spaces + commentPrefix + comment + "\n";
if(code != null) 
	res_str += spaces + code + " \n";
 
				return; 
			} else {
				String str = "ОШИБКА! Первый узел должен быть " + DI_SH_BEG + " а не "+ di_type + " икона "+comment+" !";
res_str += spaces + str;
 
				DrakonUtils.error(str); 
				return; 
			}
		}
		if(comment != null && is_write_comment)
	res_str += spaces + commentPrefix + comment + "\n";
if(code != null) 
	res_str += spaces + code + " \n";
 
		//--dg-- выходных ребер 1?
		if(DrakonUtils.getOutDegree(cur_node) == 1) {
			next_node= DrakonUtils.getOutNode(cur_node,0);
cur_node = null; 
		} else {
			String str = "ОШИБКА! Число дочерних узлов у начала шампура не равно 1\n";
 
			DrakonUtils.error(str); 
			return; 
		}
		//--dg-- есть след.узел?
		if(next_node != null) {
			//--dg-- тип след.узла НАЧАЛО ШАМПУРА?
			if(DrakonUtils.isIconType(next_node,DI_SH_BEG) || DrakonUtils.isIconType(next_node,DI_PROC_BEG)) {
				parceShampur(next_node,_level); 
			} else {
				//--dg-- след узел ПРОСТЫНЯ ?
				if(DrakonUtils.isIconType(next_node,DI_NATIVE_CODE) ) {
					parceSheet(next_node,_level); 
				} else {
					//--dg-- след узел КОНЕЦ (СИЛУЭТА,СБОРКИ,КЛАССА)?
					if(DrakonUtils.isIconType(next_node,DI_SI_END) ||
DrakonUtils.isIconType(next_node,DI_COMPIL_END) ||
DrakonUtils.isIconType(next_node,DI_CLASS_END)
) {
						parceSheet(next_node,_level); 
					} else {
						DrakonUtils.error("ЧТО ЭТО ЗА УЗЕЛ В ПРОСТЫНЕ?"); 
					}
				}
			}
		} else {
		}
		}


	protected Vertex parceNext(Vertex cur_node, int _level) {
/**
 * @param	cur_node
 * @param	res_str
 * @return terminator - последний узел на котором закончилось движение
 */ 
		String comment = DrakonUtils.getComment(cur_node);
String di_type = DrakonUtils.getIconType(cur_node);
String code = geReleaseCode(cur_node);
String spaces = "";
Vertex term_yes;
String str;
Vertex node;
Vertex cur_node_d;  
		DrakonUtils.debug("-pnx- n: "+DrakonUtils.getComment(cur_node)); 
		//--dg-- текущий узел null?
		if(cur_node == null) {
			str = "\nОШИБКА! Следующий за узлом \"" + comment + "\" узел отсутствует."; 
			DrakonUtils.error(str); 
			return null; 
		} else {
		}
		for (int i = 0; i < _level; i++)  
	spaces += "\t";
 
		if(comment != null)	
	comment = comment.replace("\n",commentPrefix);
 
		//--dg-- тип текущего узла не задан?
		if(di_type == null || di_type.length() == 0) {
			res_str += spaces +commentPrefix+" ПРЕДУПРЕЖДЕНИЕ! не установлен тип узла \"" + comment + "\" \n"; 
			if(comment != null && is_write_comment) 
	res_str += "\n"+commentPrefix + comment + "\n";
if(code != null)	
	res_str += code + "\n"; 
			node 
			 = parceNext(DrakonUtils.getOutNode(cur_node,0), _level); 
			return node; 
		} else {
		}
		//--dg-- Проверка выходов проходит?
		if(isCheckOutputs(cur_node)) {
		} else {
			str = "ОШИБКА! НЕ ПРОШЛА ПРОВЕРКА ВЫХОДОВ для иконы " + comment +" (тип " + di_type+")";
res_str += str; 
			DrakonUtils.error(str); 
		}
		switch(di_type) {
			case DI_SW:
				if (comment != null && is_write_comment) 
res_str += spaces + commentPrefix + comment + "\n";
res_str += spaces +"switch(" + code + ") {\n"; 
				term_yes = null; 
				//--dg-- у узла switch один выход и это Точка?
				if(DrakonUtils.getOutDegree(cur_node)==1 && DrakonUtils.getIconType(DrakonUtils.getOutNode(cur_node, 0)).equals(DrakonUtils.DI_EI)) {
					cur_node = DrakonUtils.getOutNode(cur_node, 0); 
				} else {
				}
				for(int is2 = 0; is2 < DrakonUtils.getOutDegree(cur_node); is2++) {
					cur_node_d = DrakonUtils.getOutNode(cur_node,is2);

code = geReleaseCode(cur_node_d);
di_type = DrakonUtils.getIconType(cur_node_d); 
					//--dg-- это  CASE или DEFAULT?
					if(di_type.contains(DI_CASE) 
|| di_type.contains(DI_DEFAULT)) {
						term_yes 
						= parceNext(cur_node_d, _level + 1); 
					} else {
						str = "Ошибка! У Иконы ВЫБОР \"" + comment + "\" все ребра должны вести к Case иконам!"; 
						DrakonUtils.error(str); 
						res_str += spaces + str; 
						return null; 
					}
					}
				res_str += spaces +"}\n"; 
				//--dg-- терминатор пуст?
				if(term_yes == null) {
					return null; 
				} else {
				}
				//--dg-- У терминатора 1 выход?
				if(DrakonUtils.getOutDegree(term_yes) == 1) {
					cur_node = DrakonUtils.getOutNode(term_yes,0); 
					node 
					 = parceNext(cur_node, _level); 
					return node; 
				} else {
					//--dg-- терминатор КОНЕЦ ШАМПУРА?
					if(di_type.contains(DI_SH_END) || di_type.contains(DI_PROC_END)) {
					} else {
						str = commentPrefix + "ПРЕДУПРЕЖДЕНИЕ!   Терминатор развилки \"" + comment +"\" имеет "+ DrakonUtils.getOutDegree(term_yes) +" выходов. Должен быть один. \n";
 
						DrakonUtils.message(str); 
					}
					return term_yes; 
				}
			case DI_CASE:
				if (comment != null && is_write_comment)
res_str += spaces +commentPrefix+" " + comment + "\n";
res_str += spaces +"case " + code + ":\n"; 
				cur_node = DrakonUtils.getOutNode(cur_node,0);  
				term_yes 
				= parceNext(cur_node, _level + 1); 
				return term_yes; 
			case DI_DEFAULT:
				if (comment != null && is_write_comment)
res_str += spaces +commentPrefix + comment + "\n";
res_str += spaces +"default:\n"; 
				cur_node = DrakonUtils.getOutNode(cur_node,0);  
				term_yes 
				= parceNext(cur_node, _level + 1); 
				return term_yes; 
			case DI_IF:
				//{
//String di_type_edge;
int i = 0;
Vertex term_no = null; 
				//--dg-- реальность CODE_PLSQL или CODE_PGSQL ?
				if(CURRENT_RELEASE != null && (CURRENT_RELEASE.equals("CODE_PLSQL") || CURRENT_RELEASE.equals("CODE_PGSQL"))) {
					res_str += spaces +commentPrefix + comment + "\n";
res_str += spaces +"if(" + code + ") then\n"; 
				} else {
					res_str += spaces +commentPrefix + comment + "\n";
res_str += spaces +"if(" + code + ") {\n"; 
				}
				//--dg-- два выхода?
				if(DrakonUtils.getOutDegree(cur_node) == 2) {
				} else {
					str = "ОШИБКА! У Развилки \"" + comment + "\" должено быть два выхода!\n";
res_str += spaces + str; 
					DrakonUtils.error(str); 
				}
				Edge edge = DrakonUtils.getOutEdge(cur_node,0);
i = 0; 
				//--dg-- тип не Да?
				if(!DrakonUtils.isEdgeYes(edge)) {
					edge = DrakonUtils.getOutEdge(cur_node,1);
i = 1; 
				} else {
				}
				//--dg-- тип не Да?
				if(!DrakonUtils.isEdgeYes(edge)) {
					str = "Ошибка! У Развилки \"" + comment + "\" должен быть Да конец!"; 
					DrakonUtils.error(str); 
					return null; 
				} else {
				}
				cur_node_d = DrakonUtils.getOutNode(cur_node,i);
term_yes = parceNext(cur_node_d, _level + 1);
 
				//--dg-- реальность CODE_PLSQL ?
				if(CURRENT_RELEASE != null && (CURRENT_RELEASE.equals("CODE_PLSQL") || CURRENT_RELEASE.equals("CODE_PGSQL"))) {
					if (i == 0) {
	cur_node_d = DrakonUtils.getOutNode(cur_node,1);
} else {
	cur_node_d = DrakonUtils.getOutNode(cur_node,0);
}
 
					//--dg-- выход НЕ КОНЕЦ_ЕСЛИ ?
					if(!DrakonUtils.isIconType(cur_node_d,DI_EI)) {
						res_str += spaces +"else \n";
 
					} else {
					}
					term_no = parceNext(cur_node_d, _level + 1);
 
					res_str += spaces +"end if;\n";
 
				} else {
					res_str += spaces +"} else {\n";
 
					if (i == 0) {
	cur_node_d = DrakonUtils.getOutNode(cur_node,1);
} else {
	cur_node_d = DrakonUtils.getOutNode(cur_node,0);
}
term_no = parceNext(cur_node_d, _level+1);
 
					res_str += spaces +"}\n";
 
				}
				if (DrakonUtils.isIconType(term_yes,DI_SH_END) &&
!DrakonUtils.isIconType(term_no,DI_SH_END)) 
term_yes = term_no; 
				//--dg-- первый выход КОНЕЦ ШАМПУРА а второй нет?
				if(DrakonUtils.isIconType(term_yes,DI_SH_END) &&
!DrakonUtils.isIconType(term_no,DI_SH_END)) {
					term_yes = term_no; 
				} else {
				}
				//--dg-- выбранный терминатор null?
				if(term_yes == null) {
					return null; 
				} else {
				}
				//--dg-- выбранный терминатор КОНЕЦ ЦИКЛА?
				if(DrakonUtils.isIconType(term_yes,DI_FOR_END)) {
					return term_yes; 
				} else {
				}
				//--dg-- У терминатора 1 выход?
				if(DrakonUtils.getOutDegree(term_yes) == 1) {
					cur_node = DrakonUtils.getOutNode(term_yes,0); 
					node 
					 = parceNext(cur_node, _level); 
					return node; 
				} else {
					//--dg-- терминатор КОНЕЦ ШАМПУРА?
					if(di_type.contains(DI_SH_END) || di_type.contains(DI_PROC_END)) {
					} else {
						str = commentPrefix + " ПРЕДУПРЕЖДЕНИЕ!   Терминатор развилки \"" + comment +"\" имеет "+ DrakonUtils.getOutDegree(term_yes) +" выходов. Должен быть один. \n";
 
						DrakonUtils.message(str); 
					}
					return term_yes; 
				}
			case DI_FOR_BEG:
				if (comment != null && is_write_comment)
	res_str += spaces +commentPrefix + comment + "\n";
res_str += spaces + code +"\n"; 
				//--dg-- один выход?
				if(DrakonUtils.getOutDegree(cur_node) == 1) {
				} else {
					str = "ОШИБКА! У Начала цикла \"" + comment + "\" должен быть один выход!\n";
 
					DrakonUtils.error(str); 
					res_str += str; 
					return cur_node; 
				}
				cur_node = DrakonUtils.getOutNode(cur_node,0); 
				Vertex term 
				= parceNext(cur_node, _level + 1); 
				//--dg-- вернулся не КОНЕЦ ЦИКЛА?
				if(DrakonUtils.isIconType(term,DI_FOR_END)) {
				} else {
					str = "ОШИБКА! У Цикла \"" + comment + "\" нет конца!\n"; 
					DrakonUtils.error(str); 
					res_str += str; 
					return term; 
				}
				//--dg-- один выход у терминатора?
				if(DrakonUtils.getOutDegree(term) == 1) {
				} else {
					str = "ОШИБКА! У Цикла \"" + comment + "\" нет продолжения пути!\n"; 
					DrakonUtils.error(str); 
					res_str += str;
 
					return term; 
				}
				cur_node = DrakonUtils.getOutNode(term,0);  
				node 
				 = parceNext(cur_node, _level); 
				return node; 
			case DI_FOR_END:
				//--dg-- код есть?
				if(code != null && !code.equals("")) {
					if (comment != null && is_write_comment)
	res_str += spaces + commentPrefix + comment + "\n";
if (code != null)
res_str += spaces +code + "\n"; 
				} else {
					res_str += spaces + "}" + "\n"; 
				}
				return cur_node; 
			case DI_COMPIL_END:
				case DI_SI_END:
					if (comment != null && is_write_comment)
	res_str += spaces + commentPrefix + comment + "\n";
if (code != null)
res_str += spaces +code + "\n"; 
					return cur_node; 
			default:
				str = "Ошибка! НЕИЗВЕСТНЫЙ ТИП ИКОНЫ \"" + comment + "\" ("+ di_type + ")!n"; 
				DrakonUtils.error(str); 
				break; 
			case DI_ACTION:
				case DI_AC:
					case DI_RETURN:
						case DI_BREAK:
							case DI_INSERT:
								case DI_OUTPUT:
									case DI_NATIVE_CODE:
										if(comment != null && is_write_comment)
	res_str += spaces + commentPrefix + comment + "\n";
if(code != null) 
	res_str += spaces + code + " \n";
 
										//--dg-- есть выходы?
										if(DrakonUtils.getOutDegree(cur_node) >= 1) {
											for(int i2 = 0; i2 < DrakonUtils.getOutDegree(cur_node); i2++) {
												Edge e = DrakonUtils.getOutEdge(cur_node, i2);
 
												//--dg-- ребро ссылка-указатель?
												if(DrakonUtils.isReferenceEdge(e)) {
												} else {
													node 
													 = parceNext(DrakonUtils.getOutNode(cur_node,i2), _level); 
													return node; 
												}
												}
										} else {
										}
										str = "ОШИБКА! У Действия \"" + comment + "\" должено быть выход!\n";
 
										res_str += spaces + str; 
										DrakonUtils.error(str); 
										return cur_node; 
			case DI_PROC_END:
				case DI_SH_END:
					if (comment != null && is_write_comment)
	res_str += spaces + commentPrefix + comment + "\n";
if (code != null)
res_str += spaces +code + "\n"; 
					return cur_node; 
			case DI_EI:
				return cur_node; 
			case DI_PROC_BEG:
				case DI_SH_BEG:
					return cur_node; 
		}
		return null;
}



	public String geReleaseCode(Vertex node) { 
		//--dg-- икона из тех что без кода?
		if(DrakonUtils.isIconType(node,DI_EI) /*||
DrakonUtils.isIconType(node,DI_FOR_END)*/) {
			return ""; 
		} else {
		}
		String code = ""; 
		//--dg-- реализация установлена?
		if(CURRENT_RELEASE != null && CURRENT_RELEASE.length() != 0) {
			for(int i = 0; i < DrakonUtils.getInDegree(node); i++) {
				Vertex in_node = DrakonUtils.getInNode(node,i); 
				//--dg-- это узел текущей реализации?
				if(DrakonUtils.isIconType(in_node,CURRENT_RELEASE)) {
					code = DrakonUtils.getCode(in_node); 
					//--dg-- есть маркер?
					if(DrakonUtils.getCodeMark(in_node).length() > 0) {
						//--dg-- is_write_comment?
						if(is_write_comment) {
							code = "\n"+commentPrefix+"<DG2J code_mark=\""+/*n"+ (String) in_node.getId() + ":" +*/ DrakonUtils.getCodeMark(in_node) + "\" >\n"
+ code + "\n"
+ ""+commentPrefix+"</DG2J>\n";
 
						} else {
						}
					} else {
					}
					return code; 
				} else {
				}
				}
		} else {
		}
		code = DrakonUtils.getCode(node); 
		//--dg-- есть маркер?
		if(DrakonUtils.getCodeMark(node).length() > 0) {
			//--dg-- is_write_comment?
			if(is_write_comment) {
				code = "\n"+commentPrefix+"<DG2J code_mark=\"" + /*n"+ (String) node.getId() + ":" +*/ DrakonUtils.getCodeMark(node) + "\" >\n"
+ code + ""+commentPrefix+"</DG2J>\n";
 
			} else {
			}
		} else {
		}
		return code;
}

	public String getCleanReleaseCode(Vertex node) { 
		//--dg-- икона из тех что без кода?
		if(DrakonUtils.isIconType(node,DI_EI)  /*|| DrakonUtils.isIconType(node,DI_FOR_END)*/) {
			return ""; 
		} else {
		}
		String code = ""; 
		//--dg-- реализация установлена?
		if(CURRENT_RELEASE != null && CURRENT_RELEASE.length() != 0) {
			for(int i = 0; i < DrakonUtils.getInDegree(node); i++) {
				Vertex in_node = DrakonUtils.getInNode(node,i); 
				//--dg-- это узел текущей реализации?
				if(DrakonUtils.isIconType(in_node,CURRENT_RELEASE)) {
					code = DrakonUtils.getCode(in_node); 
					return code; 
				} else {
				}
				}
			code = DrakonUtils.getCode(node); 
		} else {
			code = DrakonUtils.getCode(node); 
		}
		return code;
}



	public boolean isCheckOutputs(Vertex node) { 
		Vertex cur_node;
Object data;
String comment;
String di_type;
String code;
int type;
//Edge edge;
Vertex next_node;
int level = 0;
String str = "";
Vertex out_1 = null;
Vertex out_2 = null; 
		cur_node = node;
code = geReleaseCode(cur_node);
di_type = DrakonUtils.getIconType(cur_node);
comment = DrakonUtils.getComment(cur_node); 
		//--dg-- икона из тех что без выхода?
		if(di_type.contains(DI_SH_END) 
|| di_type.contains(DI_PROC_END) 
|| di_type.contains(DI_SI_END) 
|| di_type.contains(DI_CLASS_END)) {
			//--dg-- выходных узлов 0?
			if(DrakonUtils.getOutDegree(cur_node) == 0) {
			} else {
				str = "НАРУШЕНИЕ ПРАВИЛА! У иконы \"" + comment + "\"  не один выход.\n";
DrakonUtils.error(str); 
				return false; 
			}
		} else {
			//--dg-- икона из тех что с одним выходом?
			if(di_type.contains(DI_ACTION) 
|| di_type.contains(DI_AC) 
|| di_type.contains(DI_EI) 
|| di_type.contains(DI_DG_BEG)
|| di_type.contains(DI_CASE)
|| di_type.contains(DI_DEFAULT)
|| di_type.contains(DI_FOR_BEG)
|| di_type.contains(DI_FOR_END)
|| di_type.contains(DI_BREAK)
|| di_type.contains(DI_OUTPUT)
|| di_type.contains(DI_INSERT)) {
				//--dg-- выходных узлов 1?
				if(DrakonUtils.getOutDegree(cur_node) == 1) {
				} else {
					str = "НАРУШЕНИЕ ПРАВИЛА! У иконы \"" + comment + "\"  не один выход.\n";
DrakonUtils.error(str); 
					return false; 
				}
				out_1 = DrakonUtils.getOutNode(cur_node,0); 
				switch(di_type) {
					case DI_ACTION:
						case DI_AC:
							//--dg-- тип ЧАСТЬ СБОРКИ?
							if(DrakonUtils.isIconType(out_1,DI_SUB_COMPIL)) {
								return true; 
							} else {
							}
							case DI_EI:
								case DI_BREAK:
									case DI_INSERT:
										case DI_NATIVE_CODE:
											case DI_OUTPUT:
												//--dg-- тип ВАРИАНТ?
												if(DrakonUtils.isIconType(out_1,DI_CASE)) {
													return true; 
												} else {
												}
												//--dg-- тип из группы RG_B или ТЕРМИНАТОР?
												if(DrakonUtils.isIconType(out_1,DI_ACTION) 
|| DrakonUtils.isIconType(out_1,DI_SW) 
|| DrakonUtils.isIconType(out_1,DI_IF) 
|| DrakonUtils.isIconType(out_1,DI_EI) 
|| DrakonUtils.isIconType(out_1,DI_BREAK)
|| DrakonUtils.isIconType(out_1,DI_OUTPUT)
|| DrakonUtils.isIconType(out_1,DI_INSERT)) {
												} else {
													//--dg-- тип из группы RG_С?
													if(DrakonUtils.isIconType(out_1,DI_PROC_END) 
|| DrakonUtils.isIconType(out_1,DI_SH_END) 
|| DrakonUtils.isIconType(out_1,DI_RETURN) ) {
													} else {
														//--dg-- тип из группы RG_D?
														if(DrakonUtils.isIconType(out_1,DI_FOR_BEG) 
|| DrakonUtils.isIconType(out_1,DI_FOR_END)) {
														} else {
															//--dg-- тип НАЧАЛО ПРОЦЕДУРЫ?
															if(DrakonUtils.isIconType(out_1,DI_PROC_BEG) 
|| DrakonUtils.isIconType(out_1,DI_SH_BEG)) {
															} else {
																str = "НАРУШЕНИЕ ПРАВИЛА Действия! У иконы \"" + comment + "\"  неверный тип выхода ("+ DrakonUtils.getIconType(out_1)+").\n";
DrakonUtils.error(str); 
															}
														}
													}
												}
												break; 
					default:
						DrakonUtils.message("Проверка правил выходов не выполняется  "+DrakonUtils.getIconType(cur_node) + "икона:" + DrakonUtils.getComment(cur_node) + "\n"); 
						break; 
					case DI_CASE:
						case DI_DEFAULT:
							//--dg-- тип из группы RG_B  или ВОЗВРАТ или НАЧАЛО ЦИКЛА или ВАРИАНТ?
							if(DrakonUtils.isIconType(out_1,DI_ACTION) 
|| DrakonUtils.isIconType(out_1,DI_SW) 
|| DrakonUtils.isIconType(out_1,DI_IF) 
|| DrakonUtils.isIconType(out_1,DI_RETURN)
|| DrakonUtils.isIconType(out_1,DI_FOR_BEG)
|| DrakonUtils.isIconType(out_1,DI_CASE)
|| DrakonUtils.isIconType(out_1,DI_BREAK)
|| DrakonUtils.isIconType(out_1,DI_OUTPUT)
|| DrakonUtils.isIconType(out_1,DI_INSERT)) {
							} else {
								str = "НАРУШЕНИЕ ПРАВИЛА Вариант! У иконы \"" + comment + "\"  неверный тип выхода ("+ DrakonUtils.getIconType(out_1) +").\n";
DrakonUtils.error(str); 
							}
							break; 
				}
			} else {
				switch(di_type) {
					default:
						break; 
					case DI_SUB_COMPIL:
						//--dg-- выходных узлов 2?
						if(DrakonUtils.getOutDegree(cur_node) == 2) {
							out_1 = DrakonUtils.getOutNode(cur_node,0);
out_2 = DrakonUtils.getOutNode(cur_node,1); 
							//--dg-- первый выход ЧАСТЬ СБОРКИ или КОНЕЦ СБОРКИ?
							if(DrakonUtils.isIconType(out_1,DI_SUB_COMPIL) || DrakonUtils.isIconType(out_1,DI_SI_END)) {
							} else {
								out_1 = DrakonUtils.getOutNode(cur_node,1);
out_2 = DrakonUtils.getOutNode(cur_node,0); 
								//--dg-- первый выход ЧАСТЬ СБОРКИ или КОНЕЦ СБОРКИ?
								if(DrakonUtils.isIconType(out_1,DI_SUB_COMPIL) || DrakonUtils.isIconType(out_1,DI_SI_END)) {
								} else {
									str = "НАРУШЕНИЕ ПРАВИЛА Часть сборки!  У иконы \"" + comment + "\" неправильный тип выхода!\n";
DrakonUtils.error(str); 
									return false; 
								}
							}
							//--dg-- второй выход НАЧАЛО ПРОЦЕДУРЫ или ДЕЙСТВИЕ или БЛОК КОДА?
							if(DrakonUtils.isIconType(out_2,DI_SH_BEG)
|| DrakonUtils.isIconType(out_2,DI_PROC_BEG) 
|| DrakonUtils.isIconType(out_2,DI_ACTION) 
|| DrakonUtils.isIconType(out_2,DI_NATIVE_CODE) ) {
							} else {
								str = "НАРУШЕНИЕ ПРАВИЛА Часть сборки!  У иконы \"" + comment + "\" неправильный тип выхода!\n";
DrakonUtils.error(str); 
								return false; 
							}
						} else {
							str = "НАРУШЕНИЕ ПРАВИЛА Часть сборки! У иконы \"" + comment + "\" должно быть два выхода!\n";
DrakonUtils.error(str); 
							return false; 
						}
						break; 
					case DI_SI_BEG:
						case DI_COMPIL_BEG:
							//--dg-- выходных узлов 1?
							if(DrakonUtils.getOutDegree(cur_node) == 1) {
								out_1 = DrakonUtils.getOutNode(cur_node,0); 
								//--dg-- тип вых.узла НАЧАЛО ПРОЦЕДУРЫ или ДЕЙСТВИЕ или БЛОК КОДА?
								if(DrakonUtils.isIconType(out_1,DI_SH_BEG)
|| DrakonUtils.isIconType(out_1,DI_PROC_BEG) 
|| DrakonUtils.isIconType(out_1,DI_ACTION) 
|| DrakonUtils.isIconType(out_1,DI_NATIVE_CODE) ) {
								} else {
									str = "НАРУШЕНИЕ ПРАВИЛА Сборка-1! У иконы \"" + comment + "\" неправильный тип выхода!\n";
DrakonUtils.error(str); 
									return false; 
								}
								return true; 
							} else {
								//--dg-- выходных узлов 2?
								if(DrakonUtils.getOutDegree(cur_node) == 2) {
									out_1 = DrakonUtils.getOutNode(cur_node,0);
out_2 = DrakonUtils.getOutNode(cur_node,1); 
									//--dg-- первый выход ЗАПИСЬ В ФАЙЛ?
									if(DrakonUtils.isIconType(out_1,DI_WR_RES_FILE)) {
										//--dg-- первый выход НАЧАЛО ПРОЦЕДУРЫ или ДЕЙСТВИЕ или БЛОК КОДА?
										if(DrakonUtils.isIconType(out_2,DI_SH_BEG)
|| DrakonUtils.isIconType(out_2,DI_PROC_BEG) 
|| DrakonUtils.isIconType(out_2,DI_ACTION) 
|| DrakonUtils.isIconType(out_2,DI_NATIVE_CODE) ) {
										} else {
											str = "НАРУШЕНИЕ ПРАВИЛА Сборка-2! У иконы \"" + comment + "\" неправильный тип выхода!\n";
DrakonUtils.error(str); 
											return false; 
										}
									} else {
										//--dg-- второй выход ЗАПИСЬ В ФАЙЛ?
										if(DrakonUtils.isIconType(out_2,DI_WR_RES_FILE)) {
											//--dg-- первый выход НАЧАЛО ПРОЦЕДУРЫ или ДЕЙСТВИЕ или БЛОК КОДА?
											if(DrakonUtils.isIconType(out_1,DI_SH_BEG)
|| DrakonUtils.isIconType(out_1,DI_PROC_BEG) 
|| DrakonUtils.isIconType(out_1,DI_ACTION) 
|| DrakonUtils.isIconType(out_1,DI_NATIVE_CODE) ) {
											} else {
												str = "НАРУШЕНИЕ ПРАВИЛА Сборка-2! У иконы \"" + comment + "\" неправильный тип выхода!\n";
DrakonUtils.error(str); 
												return false; 
											}
										} else {
											str = "НАРУШЕНИЕ ПРАВИЛА Сборка-2! У иконы НАЧАЛО СБОРКИ \"" + comment + "\" один из двух выходов должен быть ЗАПИСЬ В ФАЙЛ\n";
DrakonUtils.error(str); 
											return false; 
										}
									}
								} else {
									str = "ОШИБКА! У иконы \"" + comment + "\" не оди и не два выхода!\n";
DrakonUtils.error(str); 
									return false; 
								}
							}
							break; 
				}
			}
		}
		return true;
}



	public void parseDrakon() { 
		 
		}


	protected void build_vis(Object data) { 
		 
		}


	public void zoomAll() { 
		 
		}


	public void loadData(String query) { 
		 
		}


	private void configListeners() { 
		 
		}


	private void onLoadingFinesh() { 
		 
		}


	public void buildVis() { 
		 
		}


	public void setCurReleaseFromNode(Vertex v) { 
		CURRENT_RELEASE = (String) DrakonUtils.getCode(v);
setCurRelease(CURRENT_RELEASE); 
		}


	public String getCurRelease() { 
		return CURRENT_RELEASE; 
		}


	public  void setCurRelease(String cur_rel) { 
		CURRENT_RELEASE = cur_rel; 
		DrakonUtils.message("--->Текущая реальность: " + CURRENT_RELEASE); 
		//--dg-- реальность CODE_PLSQL ?
		if(CURRENT_RELEASE != null && (CURRENT_RELEASE.equals("CODE_PLSQL") || CURRENT_RELEASE.equals("CODE_PGSQL"))) {
			commentPrefix = "-- "; 
		} else {
			commentPrefix = "//--dg-- "; 
		}
		DrakonUtils.message("--->префикс коментария: " + commentPrefix); 
		}


	} //-- конец класса 
