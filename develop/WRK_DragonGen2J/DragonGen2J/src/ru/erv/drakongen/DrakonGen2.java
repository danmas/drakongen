
//--dg-- Класс DrakonGen2
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
import ru.erv.drakongen.*; 
 
	//--dg-- class DrakonGen2
	public class DrakonGen2 { 
	//--dg-- константы
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
 
	//--dg-- переменные
	protected String res_str = "";
protected boolean load_finish = false;
protected String CURRENT_RELEASE = null;
protected String commentPrefix = "//-- ";
 // RELEASE_TYPE_CODE_AS; 
	//--dg-- Конструктор
	public  DrakonGen2() { 
		//--dg-- //--dg--             
		}


	//--dg-- Парсер схемы
	public String parse_drakon(Graph graph) {
	Object data;
	String descr;
	String di_type;
	
	res_str = ""; 
		//--dg-- Проходим по всем узлам
		for (Vertex v : graph.getVertices()) {
			//--dg-- получаем тип узла
			di_type = DrakonUtils.getIconType(v); ; 
			//--dg-- узел НАЧАЛО?
			if(di_type != null && di_type.equals(DI_DG_BEG)) {
				//--dg-- извлекаем из Начало тип реальности
				setCurReleaseFromNode(v); 
				//--dg-- у текущего узла один выход?
				if(DrakonUtils.getOutDegree(v) == 1) {
					//--dg-- теперь текущий узел тот на который указывает выход
					v = DrakonUtils.getOutNode(v,0); 
					//--dg-- ---
					DrakonUtils.message("---> Разбираем силуэт "); 
					//--dg-- Разбираем диаграмму
					parseSiluet(v); 
				} else {
					//--dg-- у текущего узла нет выходов?
					if(DrakonUtils.getOutDegree(v) == 0) {
					} else {
						//--dg-- ОШИБКА! У иконы Начало ...//--dg--  выходов!
						DrakonUtils.error("ОШИБКА! У иконы Начало \"" + DrakonUtils.getOutDegree(v) + "\" выходов!"); 
						//--dg-- null
						return null; 
					}
				}
			} else {
			}
			}
		//--dg-- -- Схема построена
		DrakonUtils.message("//-- Схема построена\n\n "); 
		//--dg-- результирующая строка
		return res_str;
}


	//--dg-- Парсер одного силуэта
	public void parseSiluet(Vertex node) {
/**
 * Парсер одного силуэта ДРАКОНА 
 * @param	var graph_data
 */ 
		//--dg-- переменные
		Vertex cur_node = node;
String comment = DrakonUtils.getComment(cur_node);
String di_type;
String code = "";
Vertex next_node;
Vertex next_node2 = null;
int level = 0;
String str = "";  
		//--dg-- -psi- n: 
		DrakonUtils.debug("-psi- n: "+DrakonUtils.getComment(cur_node)); 
		//--dg-- получаем параметры текущего узла
		cur_node = node;
code = geReleaseCode(cur_node);
di_type = DrakonUtils.getIconType(cur_node);
comment = DrakonUtils.getComment(cur_node); 
		//--dg-- узел НАЧАЛО СИЛУЭТА?
		if(di_type.equals(DI_SI_BEG) || di_type.equals(DI_COMPIL_BEG)) {
			//--dg-- Проверка выходов проходит?
			if(isCheckOutputs(cur_node)) {
			} else {
				//--dg-- формируем сообщение о ошибке
				str = "ОШИБКА! НЕ ПРОШЛА ПРОВЕРКА ВЫХОДОВ для иконы " + comment +" (тип " + di_type+")";
res_str += str; 
				//--dg-- ОШИБКА! НЕ ПРОШЛА ПРОВЕРКА ВЫХОДОВ для иконы 
				DrakonUtils.error(str); 
				//--dg-- //--dg--         
				return; 
			}
		} else {
			//--dg-- формируем сообщение о ошибке
			str = "ОШИБКА! Первый узел шампура должен быть \"" + DI_SI_BEG + "\"!\n"
+ "А узел " + comment +" имеет тип " + di_type;
res_str += str; 
			//--dg-- ОШИБКА! Первый узел шампура должен быть НАЧАЛО СИЛУЭТА
			DrakonUtils.error(str); 
			//--dg-- //--dg--         
			return; 
		}
		//--dg-- меняем в комментарии перевод сроки на //-- и добавляем его в результат
		if(comment != null)	
	comment = comment.replace("\n",commentPrefix);
if(comment != null) 
	res_str += "\n"+commentPrefix + comment + "\n";
if(code != null)	
	res_str += code + "\n"; 
		//--dg-- выходных ребер не 1 и не 2?
		if(DrakonUtils.getOutDegree(cur_node) != 1 && DrakonUtils.getOutDegree(cur_node) != 2) {
			//--dg-- формируем сообщение о ошибке
			str = "ОШИБКА! У иконы Начало Силуэта \"" + DrakonUtils.getOutDegree(cur_node) + "\" выходов!\n"
+ " Должно быть 1 или 2.\n"; 
			//--dg-- ОШИБКА! У иконы Начало Силуэта  Должно быть 1 или 2 выхода
			DrakonUtils.error(str); 
			//--dg-- //--dg--         
			return; 
		} else {
		}
		//--dg-- выходных ребер 2?
		if(DrakonUtils.getOutDegree(cur_node) == 2) {
			//--dg-- получаем тип первго выхода
			Vertex v = DrakonUtils.getOutNode(cur_node,0);
di_type = DrakonUtils.getIconType(v);
 
			//--dg-- на первом выходе ЗАПИСЬ В ФАЙЛ?
			if(di_type.equals(DI_WR_RES_FILE)) {
				//--dg-- след.узлом будет тот что на первом выходе, а текущим станет тот что на втором
				next_node = DrakonUtils.getOutNode(cur_node,0);
cur_node = DrakonUtils.getOutNode(cur_node,1); 
			} else {
				//--dg-- след.узлом будет тот что на втором выходе, а текущим станет тот что на первом
				next_node = DrakonUtils.getOutNode(cur_node,1);
cur_node = DrakonUtils.getOutNode(cur_node,0); 
			}
		} else {
			//--dg-- ОШИБКА! У иконы Начало Силуэта  Должно быть 2 выхода
			DrakonUtils.error("ОШИБКА! У иконы Начало Силуэта  Должно быть 2 выхода"); 
			//--dg-- //--dg--         
			return; 
		}
		//--dg-- тек.узел
		cur_node = 
		//--dg-- Разбираем начальную группу
		parceBegGroup(cur_node, level + 1); 
		//--dg-- тек.узел ЧАСТЬ СБОРКИ?
		if(DrakonUtils.getIconType(cur_node).equals(DI_SUB_COMPIL)) {
			//--dg-- по всем ЧАСТЯМ СБОРКИ
			while(cur_node!=null && DrakonUtils.getIconType(cur_node).equals(DI_SUB_COMPIL)) {
				//--dg-- получаем параметры текущего узла
				code = geReleaseCode(cur_node);
di_type = DrakonUtils.getIconType(cur_node);
comment = DrakonUtils.getComment(cur_node); 
				//--dg-- добавляем коментарий и код в результат
				if(comment != null)
	res_str +=  commentPrefix + comment + "\n";
if(code != null) 
	res_str +=  code + " \n";
 
				//--dg-- у тек.узела 2 выхода?
				if(DrakonUtils.getOutDegree(cur_node) == 2
) {
					//--dg-- на первом выходе ЧАСТЬ СБОРКИ?
					if(di_type.equals(DI_SUB_COMPIL)) {
						//--dg-- след.узлом будет тот что на втором выходе, а текущим станет тот что на первом
						next_node2 = DrakonUtils.getOutNode(cur_node,1);
cur_node = DrakonUtils.getOutNode(cur_node,0); 
					} else {
						//--dg-- след.узлом будет тот что на первом выходе, а текущим станет тот что на втором
						next_node2 = DrakonUtils.getOutNode(cur_node,0);
cur_node = DrakonUtils.getOutNode(cur_node,1); 
					}
				} else {
					//--dg-- у тек.узела 1 выход?
					if(DrakonUtils.getOutDegree(cur_node) == 1
) {
						//--dg-- след.узлом будет null, а текущим тот что на выходе
						next_node2 = null;
cur_node = DrakonUtils.getOutNode(cur_node,0); 
					} else {
						//--dg-- Ошибка! У иконы ЧАСТЬ СБОРКИ должен быть один или два выхода.
						str = "Ошибка! Ошибка! У иконы ЧАСТЬ СБОРКИ \"" + comment + "\" ("+ DrakonUtils.getIconType(cur_node) + ") должен быть один или два выхода.\n";
DrakonUtils.error(str); 
						//--dg-- //--dg--         
						return; 
					}
				}
				//--dg-- тип тек.узла НАЧАЛО ШАМПУРА?
				if(DrakonUtils.getIconType(cur_node).equals(DI_SH_BEG) || DrakonUtils.getIconType(cur_node).equals(DI_PROC_BEG)) {
				} else {
					//--dg-- тек.узел
					cur_node = 
					//--dg-- Разбираем начальную группу
					parceBegGroup(cur_node, level + 1); 
				}
				//--dg-- Разбираем шампур
				parceShampur(cur_node, level + 1); 
				//--dg-- тек.узел
				cur_node =
 
				//--dg-- след.узел
				next_node2;
 
				}
		} else {
			//--dg-- тип тек.узла НАЧАЛО ШАМПУРА?
			if(DrakonUtils.getIconType(cur_node).equals(DI_SH_BEG) || DrakonUtils.getIconType(cur_node).equals(DI_PROC_BEG)) {
				//--dg-- Разбираем шампур
				parceShampur(cur_node, level + 1); 
			} else {
				//--dg-- тек узел БЛОК КОДА?
				if(DrakonUtils.getIconType(cur_node).equals(DI_NATIVE_CODE) ) {
					//--dg-- Разбираем блок кода
					parceSheet(cur_node,level); 
				} else {
					//--dg-- КОНЕЦ СИЛУЭТА?
					if(DrakonUtils.getIconType(cur_node).equals(DI_SI_END)) {
						//--dg-- Разбираем шампур
						parceShampur(cur_node, level + 1); 
					} else {
						//--dg-- Ошибка! НЕИЗВЕСТНЫЙ ТИП ИКОНЫ Должен быть ЧАСТЬ СБОРКИ или НАЧАЛО ПРОЦЕДУРЫ.
						str = "Ошибка! НЕИЗВЕСТНЫЙ ТИП ИКОНЫ \"" + comment + "\" ("+ DrakonUtils.getIconType(cur_node) + ") должен быть ЧАСТЬ СБОРКИ или НАЧАЛО ПРОЦЕДУРЫ. !n";
DrakonUtils.error(str); 
					}
				}
			}
		}
		//--dg-- есть след.узел?
		if(next_node != null) {
			//--dg-- узел ЗАПИСЬ В ФАЙЛ?
			if(DrakonUtils.getIconType(next_node).equals(DI_WR_RES_FILE)) {
				//--dg-- имя файла
				String file_name
 
				//--dg-- из иконы ЗАПИСЬ В ФАЙЛ
				= Settings.getProperty("BASE_DIR") + "\\" +  getCleanReleaseCode(next_node);
 
				//--dg-- Имя файла задано?
				if(file_name != null && file_name.length() > 0) {
				} else {
					//--dg-- формируем сообщение о ошибке
					str = "ОШИБКА. В иконе  ЗАПИСЬ ФАЙЛА не задано имя выходного файла.\n";
res_str += str;
 
					//--dg-- ОШИБКА. В иконе  ЗАПИСЬ ФАЙЛА не задано имя выходного файла.
					DrakonUtils.error(str); 
					//--dg-- имя файла
					file_name
 
					//--dg-- временный файл
					= "tmp.java";
 
				}
				//--dg-- ----
				DrakonUtils.message("----> Записываем файл " + file_name + "\n"); 
				//--dg-- Запись в файл//--dg-- 
				FileUtils.fileWriteUTF8(file_name, res_str);
 
			} else {
			}
		} else {
			//--dg-- ----
			DrakonUtils.message("----> ПРЕДУПРЕЖДЕНИЕ. Результат не сохранен в файл.\n"); 
		}
		//--dg-- очищаем результат
		res_str = ""; 
		//--dg-- //--dg--             
		}


	//--dg-- Разбираем начальную группу
	protected Vertex parceBegGroup(Vertex cur_node, int _level) { 
		//--dg-- переменные
		String comment = DrakonUtils.getComment(cur_node);
String di_type = DrakonUtils.getIconType(cur_node);
String code = geReleaseCode(cur_node);
String spaces = "";
Vertex term_yes;
String str;
Vertex node;
Vertex cur_node_d;  
		//--dg-- -pnx- n: 
		DrakonUtils.debug("-pnx- n: "+DrakonUtils.getComment(cur_node)); 
		//--dg-- текущий узел null?
		if(cur_node == null) {
			//--dg-- null
			return null; 
		} else {
		}
		//--dg-- в строку пробелов добавляем табуляторы по глубине уровня
		for (int i = 0; i < _level; i++)  
	spaces += "\t";
 
		//--dg-- обрабатываем перевод строки в комментариях и коде
		if(comment != null)	
	comment = comment.replace("\n",commentPrefix);
//if(code != null)	
//	code = code.replace("\n",commentPrefix);
 
		//--dg-- тип текущего узла не задан?
		if(di_type == null || di_type.length() == 0) {
			//--dg-- ОШИБКА! Не задан тип иконы
			DrakonUtils.error("ОШИБКА! Не задан тип иконы"); 
			//--dg-- null
			return null; 
		} else {
		}
		//--dg-- тип узла
		switch(di_type) {
			//--dg-- неизвестный тип
			default:
				//--dg-- Ошибка! НЕИЗВЕСТНЫЙ ТИП ИКОНЫ В НАЧАЛЬНОЙ ГРУППЕ 
				str = "Ошибка! НЕИЗВЕСТНЫЙ ТИП ИКОНЫ \"" + comment + "\" ("+ di_type + ") В НАЧАЛЬНОЙ ГРУППЕ. !n";
DrakonUtils.error(str); 
				//--dg-- break
				break; 
			//--dg--  НАЧАЛО ПРОЦЕДУРЫ
			case DI_PROC_BEG:
				//--dg--  НАЧАЛО ШАМПУРА(SH_BEG)
				case DI_SH_BEG:
					//--dg--  ЧАСТЬ СБОРКИ
					case DI_SUB_COMPIL:
						//--dg-- тек. узел
						return cur_node; 
			//--dg--  ДЕСТВИЕ(ACTION)
			case DI_ACTION:
				//--dg--  ДЕСТВИЕ(AC)
				case DI_AC:
					//--dg--  БЛОК КОДА
					case DI_NATIVE_CODE:
						//--dg-- добавляем коментарий и код в результат
						if(comment != null)
	res_str += spaces + commentPrefix + comment + "\n";
if(code != null) 
	res_str += spaces + code + " \n";
 
						//--dg-- есть выходы?
						if(DrakonUtils.getOutDegree(cur_node) >= 1) {
							//--dg-- для всех выходов
							for(int i2 = 0; i2 < DrakonUtils.getOutDegree(cur_node); i2++) {
								//--dg-- получаем тип выхода
								Edge e = DrakonUtils.getOutEdge(cur_node, i2);
 
								//--dg-- ребро ссылка-указатель?
								if(DrakonUtils.isReferenceEdge(e)) {
								} else {
									//--dg-- node
									node 
									//--dg-- Разбираем начальную группу
									 = parceBegGroup(DrakonUtils.getOutNode(cur_node,i2), _level); 
									//--dg-- node
									return node; 
								}
								}
						} else {
						}
						//--dg-- формируем сообщение о ошибке
						str = "ОШИБКА! У Действия \"" + comment + "\" должено быть выход!\n";
 
						//--dg-- добавляем в результат
						res_str += spaces + str; 
						//--dg-- "ОШИБКА! У Действия ... должен быть выход.
						DrakonUtils.error(str); 
						//--dg-- тек. узел
						return cur_node; 
			//--dg--  КОНЕЦ СБОРКИ
			case DI_COMPIL_END:
				//--dg--  КОНЕЦ СИЛУЭТА(SI_END)
				case DI_SI_END:
					//--dg-- добавляем комент и код в результат
					if (comment != null)
	res_str += spaces +commentPrefix + comment + "\n";
if (code != null)
res_str += spaces +code + "\n"; 
					//--dg-- тек. узел
					return cur_node; 
		}
		//--dg-- null
		return null;
}


	//--dg-- Парсер одного шампура
	protected void parceShampur(Vertex node, int  _level) { 
		//--dg-- переменные
		Vertex cur_node;
		String comment;
		String di_type;
		String code;
		String this_comment;
		String spaces = "";
		Vertex next_node; 
 
		//--dg-- в строку пробелов добавляем табуляторы по глубине уровня
		for (int i = 0; i < _level; i++)  
			spaces += "\t";
 
		//--dg-- получаем параметры текущего узла
		cur_node = node;
		code= geReleaseCode(cur_node);
		di_type = DrakonUtils.getIconType(cur_node);
		comment = DrakonUtils.getComment(cur_node);
this_comment = comment; 
 
		//--dg-- -psh- n: 
		DrakonUtils.debug("-psh- n: "+DrakonUtils.getComment(cur_node)); 
		//--dg-- узел НАЧАЛО ШАМПУРА?
		if(di_type.equals(DI_SH_BEG) || di_type.equals(DI_PROC_BEG)) {
		} else {
			//--dg-- узел КОНЕЦ (СИЛУЭТА,СБОРКИ,КЛАССА)?
			if(di_type.equals(DI_SI_END) ||
di_type.equals(DI_COMPIL_END) ||
di_type.equals(DI_CLASS_END)) {
				//--dg-- добавляем в результат комментарий и код если они есть
				if(comment != null)
	res_str += spaces + commentPrefix + comment + "\n";
if(code != null) 
	res_str += spaces + code + " \n";
 
				//--dg-- //--dg--         
				return; 
			} else {
				//--dg-- узел БЛОК КОДА?
				if(di_type.equals(DI_NATIVE_CODE)) {
				} else {
					//--dg-- формируем сообщение о ошибке
					String str = "ОШИБКА! Первый узел должен быть " + DI_SH_BEG + " а не "+ di_type + " икона "+comment+" !";
res_str += spaces + str;
 
					//--dg-- ОШИБКА! Первый узел должен быть  НАЧАЛО ШАМПУРА
					DrakonUtils.error(str); 
					//--dg-- //--dg--         
					return; 
				}
			}
		}
		//--dg-- добавляем в результат комментарий и код если они есть
		if(comment != null)
	res_str += spaces + commentPrefix + comment + "\n";
if(code != null) 
	res_str += spaces + code + " \n";
 
		//--dg-- выходных ребер 2?
		if(DrakonUtils.getOutDegree(cur_node) == 2) {
			//--dg-- получаем тип первго выхода
			Vertex v = DrakonUtils.getOutNode(cur_node,0);
di_type = DrakonUtils.getIconType(v);
 
			//--dg-- на первом выходе НАЧАЛО ШАМПУРА или КОНЕЦ СИЛУЭТА//--dg-- или БЛОК КОДА?
			if(di_type.equals(DI_SH_BEG) || di_type.equals(DI_PROC_BEG) || di_type.equals(DI_SI_END) || di_type.equals(DI_COMPIL_END) ||
di_type.equals(DI_NATIVE_CODE)) {
				//--dg-- след.узлом будет тот что на втором выходе, а текущим станет тот что на первом
				next_node = DrakonUtils.getOutNode(cur_node,0);
cur_node = DrakonUtils.getOutNode(cur_node,1); 
			} else {
				//--dg-- след.узлом будет тот что на первом  выходе, а текущим станет тот что на втром
				next_node = DrakonUtils.getOutNode(cur_node,1);
cur_node = DrakonUtils.getOutNode(cur_node,0); 
			}
		} else {
			//--dg-- выходных ребер 1?
			if(DrakonUtils.getOutDegree(cur_node) == 1) {
				//--dg-- след.узла не будет, а текущим станет тот что на первом
				cur_node = DrakonUtils.getOutNode(cur_node,0);
next_node = null; 
			} else {
				//--dg-- формируем сообщение о ошибке
				String str = "ОШИБКА! Число дочерних узлов у начала шампура не равно 1 или 2\n";
 
				//--dg-- ОШИБКА! Число дочерних узлов у начала шампура не равно 1 или 2
				DrakonUtils.error(str); 
				//--dg-- //--dg--         
				return; 
			}
		}
		//--dg-- Разбираем ветку
		Vertex term = parceNext(cur_node, _level + 1); 
		//--dg-- вернулся пустой терминатор?
		if(term == null) {
			//--dg-- формируем сообщение о ошибке
			String str = "ОШИБКА! в шампуре \"" + this_comment + "\" parceNext() вернул пусой терминатор\n";
res_str += str; 
			//--dg-- ОШИБКА! в шампуре ... вернул пусой терминатор
			DrakonUtils.error(str); 
		} else {
		}
		//--dg-- есть след.узел?
		if(next_node != null) {
			//--dg-- тип след.узла НАЧАЛО ШАМПУРА?
			if(DrakonUtils.getIconType(next_node).equals(DI_SH_BEG) || DrakonUtils.getIconType(next_node).equals(DI_PROC_BEG)) {
				//--dg-- Разбираем след. шамапур
				parceShampur(next_node,_level); 
			} else {
				//--dg-- след узел узел БЛОК КОДА?
				if(DrakonUtils.getIconType(next_node).equals(DI_NATIVE_CODE) ) {
					//--dg-- Разбираем след. простыню
					parceSheet(next_node,_level); 
				} else {
					//--dg-- след узел КОНЕЦ (СИЛУЭТА,СБОРКИ,КЛАССА)?
					if(DrakonUtils.getIconType(next_node).equals(DI_SI_END) ||
DrakonUtils.getIconType(next_node).equals(DI_COMPIL_END) ||
DrakonUtils.getIconType(next_node).equals(DI_CLASS_END)
) {
						//--dg-- Разбираем след. блок кода
						parceSheet(next_node,_level); 
					} else {
						//--dg-- ЧТО ЭТО ЗА УЗЕЛ В ПРОЦЕДУРЕ?
						DrakonUtils.error("ЧТО ЭТО ЗА УЗЕЛ В ПРОЦЕДУРЕ?"); 
					}
				}
			}
		} else {
		}
		//--dg-- //--dg--             
		}


	//--dg-- Парсер одной простыни
	protected void parceSheet(Vertex node, int  _level) { 
		//--dg-- переменные
		Vertex cur_node;
		String comment;
		String di_type;
		String code;
		String this_comment;
		String spaces = "";
		Vertex next_node; 
 
		//--dg-- в строку пробелов добавляем табуляторы по глубине уровня
		for (int i = 0; i < _level; i++)  
			spaces += "\t";
 
		//--dg-- получаем параметры текущего узла
		cur_node = node;
		code= geReleaseCode(cur_node);
		di_type = DrakonUtils.getIconType(cur_node);
		comment = DrakonUtils.getComment(cur_node);
this_comment = comment; 
 
		//--dg-- -psh- n: 
		DrakonUtils.debug("-psh- n: "+DrakonUtils.getComment(cur_node)); 
		//--dg-- узел БЛОК КОДА?
		if(di_type.equals(DI_NATIVE_CODE)) {
		} else {
			//--dg-- узел КОНЕЦ (СИЛУЭТА,СБОРКИ,КЛАССА)?
			if(di_type.equals(DI_SI_END) ||
di_type.equals(DI_COMPIL_END) ||
di_type.equals(DI_CLASS_END)) {
				//--dg-- добавляем в результат комментарий и код если они есть
				if(comment != null)
	res_str += spaces + commentPrefix + comment + "\n";
if(code != null) 
	res_str += spaces + code + " \n";
 
				//--dg-- //--dg--         
				return; 
			} else {
				//--dg-- формируем сообщение о ошибке
				String str = "ОШИБКА! Первый узел должен быть " + DI_SH_BEG + " а не "+ di_type + " икона "+comment+" !";
res_str += spaces + str;
 
				//--dg-- ОШИБКА! Первый узел должен быть  НАЧАЛО ШАМПУРА
				DrakonUtils.error(str); 
				//--dg-- //--dg--         
				return; 
			}
		}
		//--dg-- добавляем в результат комментарий и код если они есть
		if(comment != null)
	res_str += spaces + commentPrefix + comment + "\n";
if(code != null) 
	res_str += spaces + code + " \n";
 
		//--dg-- выходных ребер 1?
		if(DrakonUtils.getOutDegree(cur_node) == 1) {
			//--dg-- получаем след.узел
			next_node= DrakonUtils.getOutNode(cur_node,0);
cur_node = null; 
		} else {
			//--dg-- формируем сообщение о ошибке
			String str = "ОШИБКА! Число дочерних узлов у начала шампура не равно 1\n";
 
			//--dg-- ОШИБКА! Число дочерних узлов у начала шампура не равно 1
			DrakonUtils.error(str); 
			//--dg-- //--dg--         
			return; 
		}
		//--dg-- есть след.узел?
		if(next_node != null) {
			//--dg-- тип след.узла НАЧАЛО ШАМПУРА?
			if(DrakonUtils.getIconType(next_node).equals(DI_SH_BEG) || DrakonUtils.getIconType(next_node).equals(DI_PROC_BEG)) {
				//--dg-- Разбираем след. шамапур
				parceShampur(next_node,_level); 
			} else {
				//--dg-- след узел ПРОСТЫНЯ ?
				if(DrakonUtils.getIconType(next_node).equals(DI_NATIVE_CODE) ) {
					//--dg-- Разбираем след. блок кода
					parceSheet(next_node,_level); 
				} else {
					//--dg-- след узел КОНЕЦ (СИЛУЭТА,СБОРКИ,КЛАССА)?
					if(DrakonUtils.getIconType(next_node).equals(DI_SI_END) ||
DrakonUtils.getIconType(next_node).equals(DI_COMPIL_END) ||
DrakonUtils.getIconType(next_node).equals(DI_CLASS_END)
) {
						//--dg-- Разбираем след. блок кода
						parceSheet(next_node,_level); 
					} else {
						//--dg-- ЧТО ЭТО ЗА УЗЕЛ В ПРОСТЫНЕ?
						DrakonUtils.error("ЧТО ЭТО ЗА УЗЕЛ В ПРОСТЫНЕ?"); 
					}
				}
			}
		} else {
		}
		//--dg-- //--dg--             
		}


	//--dg-- Разбор ветки
	protected Vertex parceNext(Vertex cur_node, int _level) {
/**
 * @param	cur_node
 * @param	res_str
 * @return terminator - последний узел на котором закончилось движение
 */ 
		//--dg-- переменные
		String comment = DrakonUtils.getComment(cur_node);
String di_type = DrakonUtils.getIconType(cur_node);
String code = geReleaseCode(cur_node);
String spaces = "";
Vertex term_yes;
String str;
Vertex node;
Vertex cur_node_d;  
		//--dg-- -pnx- n: 
		DrakonUtils.debug("-pnx- n: "+DrakonUtils.getComment(cur_node)); 
		//--dg-- текущий узел null?
		if(cur_node == null) {
			//--dg-- формируем сообщение о ошибке
			str = "\nОШИБКА! Следующий за узлом \"" + comment + "\" узел отсутствует."; 
			//--dg-- ОШИБКА! Первый узел должен быть  НАЧАЛО ШАМПУРА
			DrakonUtils.error(str); 
			//--dg-- null
			return null; 
		} else {
		}
		//--dg-- в строку пробелов добавляем табуляторы по глубине уровня
		for (int i = 0; i < _level; i++)  
	spaces += "\t";
 
		//--dg-- обрабатываем перевод строки в комментариях и коде
		if(comment != null)	
	comment = comment.replace("\n",commentPrefix);
//if(code != null)	
//	code = code.replace("\n",commentPrefix);
 
		//--dg-- тип текущего узла не задан?
		if(di_type == null || di_type.length() == 0) {
			//--dg-- в результат добавляем  ПРЕДУПРЕЖДЕНИЕ! не установлен тип узла
			res_str += spaces +commentPrefix+" ПРЕДУПРЕЖДЕНИЕ! не установлен тип узла \"" + comment + "\" \n"; 
			//--dg-- добавляем коментарий и код в результат
			if(comment != null) 
	res_str += "\n"+commentPrefix + comment + "\n";
if(code != null)	
	res_str += code + "\n"; 
			//--dg-- node
			node 
			//--dg-- Разбираем ветку
			 = parceNext(DrakonUtils.getOutNode(cur_node,0), _level); 
			//--dg-- node
			return node; 
		} else {
		}
		//--dg-- -pnx- n -2-: 
		DrakonUtils.debug("-pnx- -2- n: "+DrakonUtils.getComment(cur_node)); 
		//--dg-- Проверка выходов проходит?
		if(isCheckOutputs(cur_node)) {
		} else {
			//--dg-- формируем сообщение о ошибке
			str = "ОШИБКА! НЕ ПРОШЛА ПРОВЕРКА ВЫХОДОВ для иконы " + comment +" (тип " + di_type+")";
res_str += str; 
			//--dg-- ОШИБКА! НЕ ПРОШЛА ПРОВЕРКА ВЫХОДОВ для иконы 
			DrakonUtils.error(str); 
		}
		//--dg-- тип узла
		switch(di_type) {
			//--dg--  КОНЕЦ ЦИКЛА(FOR_END)
			case DI_FOR_END:
				//--dg-- код есть?
				if(code != null) {
					//--dg-- добавляем комент и код в результат
					if (comment != null)
	res_str += spaces + commentPrefix + comment + "\n";
if (code != null)
res_str += spaces +code + "\n"; 
				} else {
					//--dg-- добавляем "}"
					res_str += spaces + "}" + "\n"; 
				}
				//--dg-- тек. узел
				return cur_node; 
			//--dg--  КОНЕЦ СБОРКИ
			case DI_COMPIL_END:
				//--dg--  КОНЕЦ СИЛУЭТА(SI_END)
				case DI_SI_END:
					//--dg-- добавляем комент и код в результат
					if (comment != null)
	res_str += spaces + commentPrefix + comment + "\n";
if (code != null)
res_str += spaces +code + "\n"; 
					//--dg-- тек. узел
					return cur_node; 
			//--dg--  УСЛОВИЕ(IF)
			case DI_IF:
				//--dg-- переменные
				//{
//String di_type_edge;
int i = 0;
Vertex term_no = null; 
				//--dg-- реальность CODE_PLSQL ?
				if(CURRENT_RELEASE != null && (CURRENT_RELEASE.equals("CODE_PLSQL") || CURRENT_RELEASE.equals("CODE_PGSQL"))) {
					//--dg-- записываем if выражение в результат then
					res_str += spaces +commentPrefix + comment + "\n";
res_str += spaces +"if(" + code + ") then\n"; 
				} else {
					//--dg-- записываем if выражение в результат {
					res_str += spaces +commentPrefix + comment + "\n";
res_str += spaces +"if(" + code + ") {\n"; 
				}
				//--dg-- два выхода?
				if(DrakonUtils.getOutDegree(cur_node) == 2) {
				} else {
					//--dg-- формируем сообщение о ошибке
					str = "ОШИБКА! У Развилки \"" + comment + "\" должено быть два выхода!\n";
res_str += spaces + str; 
					//--dg-- ОШИБКА! У Развилки ... должено быть два выхода!
					DrakonUtils.error(str); 
				}
				//--dg-- берем первый выход, определяем его тип
				Edge edge = DrakonUtils.getOutEdge(cur_node,0);
i = 0; 
				//--dg-- тип не Да?
				if(!DrakonUtils.isEdgeYes(edge)) {
					//--dg-- не тот конец, берем следующий
					edge = DrakonUtils.getOutEdge(cur_node,1);
i = 1; 
				} else {
				}
				//--dg-- тип не Да?
				if(!DrakonUtils.isEdgeYes(edge)) {
					//--dg-- формируем сообщение о ошибке
					str = "Ошибка! У Развилки \"" + comment + "\" должен быть Да конец!"; 
					//--dg-- Ошибка! У Развилки ... должен быть Да конец!
					DrakonUtils.error(str); 
					//--dg-- null
					return null; 
				} else {
				}
				//--dg-- обрабатываем ветку Да
				cur_node_d = DrakonUtils.getOutNode(cur_node,i);
term_yes = parceNext(cur_node_d, _level + 1);
 
				//--dg-- реальность CODE_PLSQL ?
				if(CURRENT_RELEASE != null && (CURRENT_RELEASE.equals("CODE_PLSQL") || CURRENT_RELEASE.equals("CODE_PGSQL"))) {
					//--dg-- получаем выход НЕТ
					if (i == 0) {
	cur_node_d = DrakonUtils.getOutNode(cur_node,1);
} else {
	cur_node_d = DrakonUtils.getOutNode(cur_node,0);
}
 
					//--dg-- выход НЕ КОНЕЦ_ЕСЛИ ?
					if(!DrakonUtils.getIconType(cur_node_d).equals(DI_EI)) {
						//--dg-- записываем в результат "else"
						res_str += spaces +"else \n";
 
					} else {
					}
					//--dg-- обрабатываем ветку, получаем терминатор
					term_no = parceNext(cur_node_d, _level + 1);
 
					//--dg-- записываем в результат end if
					res_str += spaces +"end if;\n";
 
				} else {
					//--dg-- записываем в результат "} else {"
					res_str += spaces +"} else {\n";
 
					//--dg-- обрабатываем ветку Нет
					if (i == 0) {
	cur_node_d = DrakonUtils.getOutNode(cur_node,1);
} else {
	cur_node_d = DrakonUtils.getOutNode(cur_node,0);
}
term_no = parceNext(cur_node_d, _level+1);
 
					//--dg-- записываем в результат "}"
					res_str += spaces +"}\n";
 
				}
				//--dg-- выбираем куда дальше идти 
				if (DrakonUtils.getIconType(term_yes).equals(DI_SH_END) &&
!DrakonUtils.getIconType(term_no).equals(DI_SH_END)) 
term_yes = term_no; 
				//--dg-- первый выход КОНЕЦ ШАМПУРА а второй нет?
				if(DrakonUtils.getIconType(term_yes).equals(DI_SH_END) &&
!DrakonUtils.getIconType(term_no).equals(DI_SH_END)) {
					//--dg-- делаем текущим второй выход
					term_yes = term_no; 
				} else {
				}
				//--dg-- выбранный терминатор null?
				if(term_yes == null) {
					//--dg-- null
					return null; 
				} else {
				}
				//--dg-- выбранный терминатор КОНЕЦ ЦИКЛА?
				if(DrakonUtils.getIconType(term_yes).equals(DI_FOR_END)) {
					//--dg-- терминатор
					return term_yes; 
				} else {
				}
				//--dg-- У терминатора 1 выход?
				if(DrakonUtils.getOutDegree(term_yes) == 1) {
					//--dg-- делаем текущим узел выхода из терминатора
					cur_node = DrakonUtils.getOutNode(term_yes,0); 
					//--dg-- node
					node 
					//--dg-- Разбираем ветку
					 = parceNext(cur_node, _level); 
					//--dg-- node
					return node; 
				} else {
					//--dg-- терминатор КОНЕЦ ШАМПУРА?
					if(di_type.equals(DI_SH_END) || di_type.equals(DI_PROC_END)) {
					} else {
						//--dg-- формируем предупреждение
						str = commentPrefix + " ПРЕДУПРЕЖДЕНИЕ!   Терминатор развилки \"" + comment +"\" имеет "+ DrakonUtils.getOutDegree(term_yes) +" выходов. Должен быть один. \n";
 
						//--dg-- ПРЕДУПРЕЖДЕНИЕ!   Терминатор развилки ... имеет ... выходов. Должен быть один.
						DrakonUtils.message(str); 
					}
					//--dg-- терминатор
					return term_yes; 
				}
			//--dg--  КОНЕЦ ПРОЦЕДУРЫ
			case DI_PROC_END:
				//--dg--  КОНЕЦ ШАМПУРА(SH_END)
				case DI_SH_END:
					//--dg-- добавляем комент и код в результат
					if (comment != null)
	res_str += spaces + commentPrefix + comment + "\n";
if (code != null)
res_str += spaces +code + "\n"; 
					//--dg-- тек. узел
					return cur_node; 
			//--dg--  ДЕСТВИЕ(ACTION)
			case DI_ACTION:
				//--dg--  ДЕСТВИЕ(AC)
				case DI_AC:
					//--dg--  ВОЗВРАТ
					case DI_RETURN:
						//--dg--  ПРЕКРАЩЕНИЕ
						case DI_BREAK:
							//--dg--  ВСТАВКА
							case DI_INSERT:
								//--dg--  ВЫВОД
								case DI_OUTPUT:
									//--dg--  БЛОК КОДА
									case DI_NATIVE_CODE:
										//--dg-- добавляем коментарий и код в результат
										if(comment != null)
	res_str += spaces + commentPrefix + comment + "\n";
if(code != null) 
	res_str += spaces + code + " \n";
 
										//--dg-- есть выходы?
										if(DrakonUtils.getOutDegree(cur_node) >= 1) {
											//--dg-- для всех выходов
											for(int i2 = 0; i2 < DrakonUtils.getOutDegree(cur_node); i2++) {
												//--dg-- получаем тип выхода
												Edge e = DrakonUtils.getOutEdge(cur_node, i2);
 
												//--dg-- ребро ссылка-указатель?
												if(DrakonUtils.isReferenceEdge(e)) {
												} else {
													//--dg-- node
													node 
													//--dg-- Разбираем ветку
													 = parceNext(DrakonUtils.getOutNode(cur_node,i2), _level); 
													//--dg-- node
													return node; 
												}
												}
										} else {
										}
										//--dg-- формируем сообщение о ошибке
										str = "ОШИБКА! У Действия \"" + comment + "\" должено быть выход!\n";
 
										//--dg-- добавляем в результат
										res_str += spaces + str; 
										//--dg-- "ОШИБКА! У Действия ... должен быть выход.
										DrakonUtils.error(str); 
										//--dg-- тек. узел
										return cur_node; 
			//--dg--  НАЧАЛО ЦИКЛА(FOR_BEG)
			case DI_FOR_BEG:
				//--dg-- записываем комментарии  и код в результат
				if (comment != null)
	res_str += spaces +commentPrefix + comment + "\n";
res_str += spaces + code +"\n"; 
				//--dg-- один выход?
				if(DrakonUtils.getOutDegree(cur_node) == 1) {
				} else {
					//--dg-- формируем сообщение о ошибке
					str = "ОШИБКА! У Начала цикла \"" + comment + "\" должен быть один выход!\n";
 
					//--dg-- ОШИБКА! У Начала цикла ... должен быть один выход!
					DrakonUtils.error(str); 
					//--dg-- записываем сообщение о ошибке в результат
					res_str += str; 
					//--dg-- тек. узел
					return cur_node; 
				}
				//--dg-- делаем текущим выход.узел 
				cur_node = DrakonUtils.getOutNode(cur_node,0); 
				//--dg-- терминатор
				Vertex term 
				//--dg-- Разбираем ветку
				= parceNext(cur_node, _level + 1); 
				//--dg-- вернулся не КОНЕЦ ЦИКЛА?
				if(DrakonUtils.getIconType(term).equals(DI_FOR_END)) {
				} else {
					//--dg-- формируем сообщение о ошибке
					str = "ОШИБКА! У Цикла \"" + comment + "\" нет конца!\n"; 
					//--dg-- ОШИБКА! У Цикла ... нет конца!
					DrakonUtils.error(str); 
					//--dg-- записываем сообщение о ошибке в результат
					res_str += str; 
					//--dg-- терминатор
					return term; 
				}
				//--dg-- один выход у терминатора?
				if(DrakonUtils.getOutDegree(term) == 1) {
				} else {
					//--dg-- формируем сообщение о ошибке
					str = "ОШИБКА! У Цикла \"" + comment + "\" нет продолжения пути!\n"; 
					//--dg-- ОШИБКА: У Цикла ... нет продолжения пути.
					DrakonUtils.error(str); 
					//--dg-- записываем сообщение о ошибке в результат
					res_str += str;
 
					//--dg-- терминатор
					return term; 
				}
				//--dg-- делаем текущим выход терминатора
				cur_node = DrakonUtils.getOutNode(term,0);  
				//--dg-- node
				node 
				//--dg-- Разбираем ветку
				 = parceNext(cur_node, _level); 
				//--dg-- node
				return node; 
			//--dg--  КОНЕЦ УСЛОВИЯ(EI)
			case DI_EI:
				//--dg-- тек. узел
				return cur_node; 
			//--dg--  DEFAULT
			case DI_DEFAULT:
				//--dg-- записываем default выражение в результат
				res_str += spaces +commentPrefix + comment + "\n";
res_str += spaces +"default:\n"; 
				//--dg-- делаем текущим выход.узел 
				cur_node = DrakonUtils.getOutNode(cur_node,0);  
				//--dg-- term_yes
				term_yes 
				//--dg-- Разбираем ветку
				= parceNext(cur_node, _level + 1); 
				//--dg-- терминатор
				return term_yes; 
			//--dg--  НАЧАЛО ПРОЦЕДУРЫ
			case DI_PROC_BEG:
				//--dg--  НАЧАЛО ШАМПУРА(SH_BEG)
				case DI_SH_BEG:
					//--dg-- тек. узел
					return cur_node; 
			//--dg--  ВЫБОР(SWITCH)
			case DI_SW:
				//--dg-- записываем switch выражение в результат
				res_str += spaces + commentPrefix + comment + "\n";
res_str += spaces +"switch(" + code + ") {\n"; 
				//--dg-- сбрасываем терминатор
				term_yes = null; 
				//--dg-- у узла switch один выход и это Точка?
				if(DrakonUtils.getOutDegree(cur_node)==1 && DrakonUtils.getIconType(DrakonUtils.getOutNode(cur_node, 0)).equals(DrakonUtils.DI_EI)) {
					//--dg-- делаем текущим узел Точка
					cur_node = DrakonUtils.getOutNode(cur_node, 0); 
				} else {
				}
				//--dg-- Обрабатываем все case ветки
				for(int is2 = 0; is2 < DrakonUtils.getOutDegree(cur_node); is2++) {
					//--dg-- получаем данные очередного рыходного узла
					cur_node_d = DrakonUtils.getOutNode(cur_node,is2);

code = geReleaseCode(cur_node_d);
di_type = DrakonUtils.getIconType(cur_node_d); 
					//--dg-- это  CASE или DEFAULT?
					if(di_type.equals(DI_CASE) 
|| di_type.equals(DI_DEFAULT)) {
						//--dg-- term_yes
						term_yes 
						//--dg-- Разбираем ветку
						= parceNext(cur_node_d, _level + 1); 
					} else {
						//--dg-- формируем сообщение о ошибке
						str = "Ошибка! У Иконы ВЫБОР \"" + comment + "\" все ребра должны вести к Case иконам!"; 
						//--dg-- Ошибка! У Иконы ВЫБОР ... все ребра должны вести к Case иконам!
						DrakonUtils.error(str); 
						//--dg-- записываем в результат сообщение
						res_str += spaces + str; 
						//--dg-- null
						return null; 
					}
					}
				//--dg-- записываем в результат конец "}"
				res_str += spaces +"}\n"; 
				//--dg-- терминатор пуст?
				if(term_yes == null) {
					//--dg-- null
					return null; 
				} else {
				}
				//--dg-- У терминатора 1 выход?
				if(DrakonUtils.getOutDegree(term_yes) == 1) {
					//--dg-- текущий узел выход из терминатора
					cur_node = DrakonUtils.getOutNode(term_yes,0); 
					//--dg-- node
					node 
					//--dg-- Разбираем ветку
					 = parceNext(cur_node, _level); 
					//--dg-- node
					return node; 
				} else {
					//--dg-- терминатор КОНЕЦ ШАМПУРА?
					if(di_type.equals(DI_SH_END) || di_type.equals(DI_PROC_END)) {
					} else {
						//--dg-- формируем предупреждение
						str = commentPrefix + "ПРЕДУПРЕЖДЕНИЕ!   Терминатор развилки \"" + comment +"\" имеет "+ DrakonUtils.getOutDegree(term_yes) +" выходов. Должен быть один. \n";
 
						//--dg-- ПРЕДУПРЕЖДЕНИЕ!   Терминатор развилки ... имеет ... выходов. Должен быть один.
						DrakonUtils.message(str); 
					}
					//--dg-- терминатор
					return term_yes; 
				}
			//--dg-- неизвестный тип
			default:
				//--dg-- формируем сообщение о ошибке
				str = "Ошибка! НЕИЗВЕСТНЫЙ ТИП ИКОНЫ \"" + comment + "\" ("+ di_type + ")!n"; 
				//--dg-- Ошибка! НЕИЗВЕСТНЫЙ ТИП ИКОНЫ ...
				DrakonUtils.error(str); 
				//--dg-- break
				break; 
			//--dg--  CASE
			case DI_CASE:
				//--dg-- записываем case выражение в результат
				res_str += spaces +commentPrefix+" " + comment + "\n";
res_str += spaces +"case " + code + ":\n"; 
				//--dg-- делаем текущим выход.узел 
				cur_node = DrakonUtils.getOutNode(cur_node,0);  
				//--dg-- term_yes
				term_yes 
				//--dg-- Разбираем ветку
				= parceNext(cur_node, _level + 1); 
				//--dg-- терминатор
				return term_yes; 
		}
		//--dg-- null
		return null;
}



	//--dg-- Получение рабочего кода узла с маркером
	public String geReleaseCode(Vertex node) { 
		//--dg-- икона из тех что без кода?
		if(DrakonUtils.getIconType(node).equals(DI_EI) /*||
DrakonUtils.getIconType(node).equals(DI_FOR_END)*/) {
			//--dg-- ""
			return ""; 
		} else {
		}
		//--dg-- У иконы нет Коментария?
		if(DrakonUtils.getComment(node).equals("")) {
			//--dg-- ""
			return ""; 
		} else {
		}
		//--dg-- переменная
		String code = ""; 
		//--dg-- реализация установлена?
		if(CURRENT_RELEASE != null && CURRENT_RELEASE.length() != 0) {
			//--dg-- для всех входов
			for(int i = 0; i < DrakonUtils.getInDegree(node); i++) {
				//--dg-- получаем входной узел
				Vertex in_node = DrakonUtils.getInNode(node,i); 
				//--dg-- это узел текущей реализации?
				if(DrakonUtils.getIconType(in_node).equals(CURRENT_RELEASE)) {
					//--dg-- получаем код из вход. узла
					code = DrakonUtils.getCode(in_node); 
					//--dg-- есть маркер?
					if(DrakonUtils.getCodeMark(in_node).length() > 0) {
						//--dg-- добавляем к коду маркер
						
//--dg-- <DG2J code_mark="SPECIAL_DG2J_MARK" >
code = "\n"+commentPrefix+"<DG2J code_mark=\""+/*n"+ (String) in_node.getId() + ":" +*/ DrakonUtils.getCodeMark(in_node) + "\" >\n"
+ code + "\n"
+ ""+commentPrefix+"</DG2J>\n";

//--dg-- </DG2J>
 
					} else {
					}
					//--dg-- код
					return code; 
				} else {
				}
				}
		} else {
		}
		//--dg-- получаем код из текущего узла
		code = DrakonUtils.getCode(node); 
		//--dg-- есть маркер?
		if(DrakonUtils.getCodeMark(node).length() > 0) {
			//--dg-- добавляем к коду маркер
			
//--dg-- <DG2J code_mark="SPECIAL_DG2J_MARK" >
code = "\n"+commentPrefix+"<DG2J code_mark=\"" + /*n"+ (String) node.getId() + ":" +*/ DrakonUtils.getCodeMark(node) + "\" >\n"
+ code + "\n"
+ ""+commentPrefix+"</DG2J>\n";

//--dg-- </DG2J>
 
		} else {
		}
		//--dg-- код
		return code;
}


	//--dg-- Получение рабочего кода узла без маркера
	public String getCleanReleaseCode(Vertex node) { 
		//--dg-- икона из тех что без кода?
		if(DrakonUtils.getIconType(node).equals(DI_EI)  /*|| DrakonUtils.getIconType(node).equals(DI_FOR_END)*/) {
			//--dg-- ""
			return ""; 
		} else {
		}
		//--dg-- У иконы нет Коментария?
		if(DrakonUtils.getComment(node).equals("")) {
			//--dg-- ""
			return ""; 
		} else {
		}
		//--dg-- переменная
		String code = ""; 
		//--dg-- реализация установлена?
		if(CURRENT_RELEASE != null && CURRENT_RELEASE.length() != 0) {
			//--dg-- для всех входов
			for(int i = 0; i < DrakonUtils.getInDegree(node); i++) {
				//--dg-- получаем входной узел
				Vertex in_node = DrakonUtils.getInNode(node,i); 
				//--dg-- это узел текущей реализации?
				if(DrakonUtils.getIconType(in_node).equals(CURRENT_RELEASE)) {
					//--dg-- получаем код из вход. узла
					code = DrakonUtils.getCode(in_node); 
					//--dg-- код
					return code; 
				} else {
				}
				}
			//--dg-- получаем код из вход. узла
			code = DrakonUtils.getCode(node); 
		} else {
			//--dg-- получаем код из текущего узла
			code = DrakonUtils.getCode(node); 
		}
		//--dg-- код
		return code;
}



	//--dg-- Проверка выходов иконы
	public boolean isCheckOutputs(Vertex node) { 
		//--dg-- переменные
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
		//--dg-- получаем параметры текущего узла
		cur_node = node;
code = geReleaseCode(cur_node);
di_type = DrakonUtils.getIconType(cur_node);
comment = DrakonUtils.getComment(cur_node); 
		//--dg-- икона из тех что без выхода?
		if(di_type.equals(DI_SH_END) 
|| di_type.equals(DI_PROC_END) 
|| di_type.equals(DI_SI_END) 
|| di_type.equals(DI_CLASS_END)) {
			//--dg-- выходных узлов 0?
			if(DrakonUtils.getOutDegree(cur_node) == 0) {
			} else {
				//--dg-- НАРУШЕНИЕ ПРАВИЛА! У иконы ... не один выход.
				str = "НАРУШЕНИЕ ПРАВИЛА! У иконы \"" + comment + "\"  не один выход.\n";
DrakonUtils.error(str); 
				//--dg-- фальшь
				return false; 
			}
		} else {
			//--dg-- икона из тех что с одним выходом?
			if(di_type.equals(DI_ACTION) 
|| di_type.equals(DI_AC) 
|| di_type.equals(DI_EI) 
|| di_type.equals(DI_DG_BEG)
|| di_type.equals(DI_CASE)
|| di_type.equals(DI_DEFAULT)
|| di_type.equals(DI_FOR_BEG)
|| di_type.equals(DI_FOR_END)
|| di_type.equals(DI_BREAK)
|| di_type.equals(DI_OUTPUT)
|| di_type.equals(DI_INSERT)) {
				//--dg-- выходных узлов 1?
				if(DrakonUtils.getOutDegree(cur_node) == 1) {
				} else {
					//--dg-- НАРУШЕНИЕ ПРАВИЛА! У иконы ... не один выход.
					str = "НАРУШЕНИЕ ПРАВИЛА! У иконы \"" + comment + "\"  не один выход.\n";
DrakonUtils.error(str); 
					//--dg-- фальшь
					return false; 
				}
				//--dg-- получам первый выход
				out_1 = DrakonUtils.getOutNode(cur_node,0); 
				//--dg-- тип узла
				switch(di_type) {
					//--dg-- другой тип
					default:
						//--dg-- -1- D- Проверка правил выходов не выполняется  
						DrakonUtils.message("Проверка правил выходов не выполняется  "+DrakonUtils.getIconType(cur_node) + "икона:" + DrakonUtils.getComment(cur_node) + "\n"); 
						//--dg-- break
						break; 
					//--dg--  CASE
					case DI_CASE:
						//--dg--  DEFAULT
						case DI_DEFAULT:
							//--dg-- тип из группы RG_B  или ВОЗВРАТ или НАЧАЛО ЦИКЛА или ВАРИАНТ?
							if(DrakonUtils.getIconType(out_1).equals(DI_ACTION) 
|| DrakonUtils.getIconType(out_1).equals(DI_SW) 
|| DrakonUtils.getIconType(out_1).equals(DI_IF) 
|| DrakonUtils.getIconType(out_1).equals(DI_RETURN)
|| DrakonUtils.getIconType(out_1).equals(DI_FOR_BEG)
|| DrakonUtils.getIconType(out_1).equals(DI_CASE)
|| DrakonUtils.getIconType(out_1).equals(DI_BREAK)
|| DrakonUtils.getIconType(out_1).equals(DI_OUTPUT)
|| DrakonUtils.getIconType(out_1).equals(DI_INSERT)) {
							} else {
								//--dg-- НАРУШЕНИЕ ПРАВИЛА Вариант! У иконы ... неверный тип выхода.
								str = "НАРУШЕНИЕ ПРАВИЛА Вариант! У иконы \"" + comment + "\"  неверный тип выхода ("+ DrakonUtils.getIconType(out_1) +").\n";
DrakonUtils.error(str); 
							}
							//--dg-- break
							break; 
					//--dg--  ДЕСТВИЕ(ACTION)
					case DI_ACTION:
						//--dg--  ДЕСТВИЕ(AC)
						case DI_AC:
							//--dg-- тип ЧАСТЬ СБОРКИ?
							if(DrakonUtils.getIconType(out_1).equals(DI_SUB_COMPIL)) {
								//--dg-- истина
								return true; 
							} else {
							}
							//--dg--  КОНЕЦ УСЛОВИЯ(EI)
							case DI_EI:
								//--dg--  ПРЕКРАЩЕНИЕ
								case DI_BREAK:
									//--dg--  ВСТАВКА
									case DI_INSERT:
										//--dg--  БЛОК КОДА
										case DI_NATIVE_CODE:
											//--dg--  ВЫВОД
											case DI_OUTPUT:
												//--dg-- тип ВАРИАНТ?
												if(DrakonUtils.getIconType(out_1).equals(DI_CASE)) {
													//--dg-- истина
													return true; 
												} else {
												}
												//--dg-- тип из группы RG_B или ТЕРМИНАТОР?
												if(DrakonUtils.getIconType(out_1).equals(DI_ACTION) 
|| DrakonUtils.getIconType(out_1).equals(DI_SW) 
|| DrakonUtils.getIconType(out_1).equals(DI_IF) 
|| DrakonUtils.getIconType(out_1).equals(DI_EI) 
|| DrakonUtils.getIconType(out_1).equals(DI_BREAK)
|| DrakonUtils.getIconType(out_1).equals(DI_OUTPUT)
|| DrakonUtils.getIconType(out_1).equals(DI_INSERT)) {
												} else {
													//--dg-- тип из группы RG_С?
													if(DrakonUtils.getIconType(out_1).equals(DI_PROC_END) 
|| DrakonUtils.getIconType(out_1).equals(DI_SH_END) 
|| DrakonUtils.getIconType(out_1).equals(DI_RETURN) ) {
													} else {
														//--dg-- тип из группы RG_D?
														if(DrakonUtils.getIconType(out_1).equals(DI_FOR_BEG) 
|| DrakonUtils.getIconType(out_1).equals(DI_FOR_END)) {
														} else {
															//--dg-- тип НАЧАЛО ПРОЦЕДУРЫ?
															if(DrakonUtils.getIconType(out_1).equals(DI_PROC_BEG) 
|| DrakonUtils.getIconType(out_1).equals(DI_SH_BEG)) {
															} else {
																//--dg-- НАРУШЕНИЕ ПРАВИЛА Действия! У иконы ... неверный тип выхода.
																str = "НАРУШЕНИЕ ПРАВИЛА Действия! У иконы \"" + comment + "\"  неверный тип выхода ("+ DrakonUtils.getIconType(out_1)+").\n";
DrakonUtils.error(str); 
															}
														}
													}
												}
												//--dg-- break
												break; 
				}
			} else {
				//--dg-- тип узла
				switch(di_type) {
					//--dg--  ЧАСТЬ СБОРКИ
					case DI_SUB_COMPIL:
						//--dg-- выходных узлов 2?
						if(DrakonUtils.getOutDegree(cur_node) == 2) {
							//--dg-- берем текущие выходы по порядку
							out_1 = DrakonUtils.getOutNode(cur_node,0);
out_2 = DrakonUtils.getOutNode(cur_node,1); 
							//--dg-- первый выход ЧАСТЬ СБОРКИ или КОНЕЦ СБОРКИ?
							if(DrakonUtils.getIconType(out_1).equals(DI_SUB_COMPIL) || DrakonUtils.getIconType(out_1).equals(DI_SI_END)) {
							} else {
								//--dg-- меняем местами выходы
								out_1 = DrakonUtils.getOutNode(cur_node,1);
out_2 = DrakonUtils.getOutNode(cur_node,0); 
								//--dg-- первый выход ЧАСТЬ СБОРКИ или КОНЕЦ СБОРКИ?
								if(DrakonUtils.getIconType(out_1).equals(DI_SUB_COMPIL) || DrakonUtils.getIconType(out_1).equals(DI_SI_END)) {
								} else {
									//--dg-- НАРУШЕНИЕ ПРАВИЛА Часть сборки! У иконы ЧАСТЬ СБОРКИ ... неправильный тип выхода!
									str = "НАРУШЕНИЕ ПРАВИЛА Часть сборки!  У иконы \"" + comment + "\" неправильный тип выхода!\n";
DrakonUtils.error(str); 
									//--dg-- фальшь
									return false; 
								}
							}
							//--dg-- второй выход НАЧАЛО ПРОЦЕДУРЫ или ДЕЙСТВИЕ или БЛОК КОДА?
							if(DrakonUtils.getIconType(out_2).equals(DI_SH_BEG)
|| DrakonUtils.getIconType(out_2).equals(DI_PROC_BEG) 
|| DrakonUtils.getIconType(out_2).equals(DI_ACTION) 
|| DrakonUtils.getIconType(out_2).equals(DI_NATIVE_CODE) ) {
							} else {
								//--dg-- НАРУШЕНИЕ ПРАВИЛА Часть сборки! У иконы ЧАСТЬ СБОРКИ ... неправильный тип выхода!
								str = "НАРУШЕНИЕ ПРАВИЛА Часть сборки!  У иконы \"" + comment + "\" неправильный тип выхода!\n";
DrakonUtils.error(str); 
								//--dg-- фальшь
								return false; 
							}
						} else {
							//--dg-- НАРУШЕНИЕ ПРАВИЛА Часть сборки! У иконы ЧАСТЬ СБОРКИ ... должно быть 2 выхода
							str = "НАРУШЕНИЕ ПРАВИЛА Часть сборки! У иконы \"" + comment + "\" должно быть два выхода!\n";
DrakonUtils.error(str); 
							//--dg-- фальшь
							return false; 
						}
						//--dg-- break
						break; 
					//--dg-- другой тип
					default:
						//--dg-- break
						break; 
					//--dg--  НАЧАЛО СИЛУЭТА
					case DI_SI_BEG:
						//--dg--  СБОРКА
						case DI_COMPIL_BEG:
							//--dg-- выходных узлов 1?
							if(DrakonUtils.getOutDegree(cur_node) == 1) {
								//--dg-- получам первый выход
								out_1 = DrakonUtils.getOutNode(cur_node,0); 
								//--dg-- тип вых.узла НАЧАЛО ПРОЦЕДУРЫ или ДЕЙСТВИЕ или БЛОК КОДА?
								if(DrakonUtils.getIconType(out_1).equals(DI_SH_BEG)
|| DrakonUtils.getIconType(out_1).equals(DI_PROC_BEG) 
|| DrakonUtils.getIconType(out_1).equals(DI_ACTION) 
|| DrakonUtils.getIconType(out_1).equals(DI_NATIVE_CODE) ) {
								} else {
									//--dg-- НАРУШЕНИЕ ПРАВИЛА Сборка-1! У иконы СБОРКА ... неправильный тип выхода!
									str = "НАРУШЕНИЕ ПРАВИЛА Сборка-1! У иконы \"" + comment + "\" неправильный тип выхода!\n";
DrakonUtils.error(str); 
									//--dg-- фальшь
									return false; 
								}
								//--dg-- истина
								return true; 
							} else {
								//--dg-- выходных узлов 2?
								if(DrakonUtils.getOutDegree(cur_node) == 2) {
									//--dg-- текущие выходы по порядку
									out_1 = DrakonUtils.getOutNode(cur_node,0);
out_2 = DrakonUtils.getOutNode(cur_node,1); 
									//--dg-- первый выход ЗАПИСЬ В ФАЙЛ?
									if(DrakonUtils.getIconType(out_1).equals(DI_WR_RES_FILE)) {
										//--dg-- первый выход НАЧАЛО ПРОЦЕДУРЫ или ДЕЙСТВИЕ или БЛОК КОДА?
										if(DrakonUtils.getIconType(out_2).equals(DI_SH_BEG)
|| DrakonUtils.getIconType(out_2).equals(DI_PROC_BEG) 
|| DrakonUtils.getIconType(out_2).equals(DI_ACTION) 
|| DrakonUtils.getIconType(out_2).equals(DI_NATIVE_CODE) ) {
										} else {
											//--dg-- НАРУШЕНИЕ ПРАВИЛА Сборка-2! У иконы СБОРКА ... неправильный тип выхода!
											str = "НАРУШЕНИЕ ПРАВИЛА Сборка-2! У иконы \"" + comment + "\" неправильный тип выхода!\n";
DrakonUtils.error(str); 
											//--dg-- фальшь
											return false; 
										}
									} else {
										//--dg-- второй выход ЗАПИСЬ В ФАЙЛ?
										if(DrakonUtils.getIconType(out_2).equals(DI_WR_RES_FILE)) {
											//--dg-- первый выход НАЧАЛО ПРОЦЕДУРЫ или ДЕЙСТВИЕ или БЛОК КОДА?
											if(DrakonUtils.getIconType(out_1).equals(DI_SH_BEG)
|| DrakonUtils.getIconType(out_1).equals(DI_PROC_BEG) 
|| DrakonUtils.getIconType(out_1).equals(DI_ACTION) 
|| DrakonUtils.getIconType(out_1).equals(DI_NATIVE_CODE) ) {
											} else {
												//--dg-- НАРУШЕНИЕ ПРАВИЛА Сборка-1! У иконы СБОРКА ... неправильный тип выхода!
												str = "НАРУШЕНИЕ ПРАВИЛА Сборка-2! У иконы \"" + comment + "\" неправильный тип выхода!\n";
DrakonUtils.error(str); 
												//--dg-- фальшь
												return false; 
											}
										} else {
											//--dg-- НАРУШЕНИЕ ПРАВИЛА Сборка-2! У иконы СБОРКА ... один из двух выходов должен быть ЗАПИСЬ В ФАЙЛ
											str = "НАРУШЕНИЕ ПРАВИЛА Сборка-2! У иконы НАЧАЛО СБОРКИ \"" + comment + "\" один из двух выходов должен быть ЗАПИСЬ В ФАЙЛ\n";
DrakonUtils.error(str); 
											//--dg-- фальшь
											return false; 
										}
									}
								} else {
									//--dg-- ОШИБКА! У иконы СБОРКА ... не оди и не два выхода!
									str = "ОШИБКА! У иконы \"" + comment + "\" не оди и не два выхода!\n";
DrakonUtils.error(str); 
									//--dg-- фальшь
									return false; 
								}
							}
							//--dg-- break
							break; 
				}
			}
		}
		//--dg-- истина
		return true;
}



	//--dg-- Парсер схемы ДРАКОНА полученной из графа yEd
	public void parseDrakon() { 
		//--dg-- строим код
		//--dg-- //--dg--             
		}


	//--dg-- Отрисовка графа
	protected void build_vis(Object data) { 
		//--dg-- строим код
		//--dg-- //--dg--             
		}


	//--dg-- Масштабирование картинки
	public void zoomAll() { 
		//--dg-- строим код
		//--dg-- //--dg--             
		}


	//--dg-- Загрузка данных
	public void loadData(String query) { 
		//--dg-- строим код
		//--dg-- //--dg--             
		}


	//--dg-- Конфигурация листнеров
	private void configListeners() { 
		//--dg-- строим код
		//--dg-- //--dg--             
		}


	//--dg-- Обработка события "загрузка завершена"
	private void onLoadingFinesh() { 
		//--dg-- строим код
		//--dg-- //--dg--             
		}


	//--dg-- Отрисовка графа загруженного функцией loadData()
	public void buildVis() { 
		//--dg-- строим код
		//--dg-- //--dg--             
		}


	//--dg-- setCurReleaseFromNode()
	public void setCurReleaseFromNode(Vertex v) { 
		//--dg-- строим код
		CURRENT_RELEASE = (String) DrakonUtils.getCode(v);
setCurRelease(CURRENT_RELEASE); 
		//--dg-- //--dg--             
		}


	//--dg-- getCurRelease()
	public String getCurRelease() { 
		//--dg-- строим код
		return CURRENT_RELEASE; 
		//--dg-- //--dg--             
		}


	//--dg-- setCurRelease()
	public  void setCurRelease(String cur_rel) { 
		//--dg-- строим код
		CURRENT_RELEASE = cur_rel; 
		//--dg-- ---
		DrakonUtils.message("--->Текущая реальность: " + CURRENT_RELEASE); 
		//--dg-- реальность CODE_PLSQL ?
		if(CURRENT_RELEASE != null && (CURRENT_RELEASE.equals("CODE_PLSQL") || CURRENT_RELEASE.equals("CODE_PGSQL"))) {
			//--dg-- префикс коментария "--dg--"
			commentPrefix = "-- "; 
		} else {
			//--dg-- префикс коментария "//--dg-- "
			commentPrefix = "//--dg-- "; 
		}
		//--dg-- ---
		DrakonUtils.message("--->префикс коментария: " + commentPrefix); 
		//--dg-- //--dg--             
		}


	//--dg-- 
            
	} //-- конец класса 
