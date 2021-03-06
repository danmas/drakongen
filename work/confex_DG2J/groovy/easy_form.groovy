import groovy.jface.JFaceBuilder
import org.eclipse.swt.widgets.*
import groovy.swt.SwtBuilder
import groovy.swt.CustomSwingBuilder
import groovy.sql.Sql
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.*

import net.confex.tree.ITreeNode
import net.confex.tree.ICompositeProvider
import org.eclipse.ui.part.ViewPart

import ru.erv.drakongen.DrakonAct;
import ru.erv.drakongen.Settings;
import ru.erv.drakongen.parser.*;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;

// \groovy\easy_form.groovy


FormTest form = new FormTest(parent,thisGroovyNode,runViewPart);
form.run();


class FormTest {

	def Composite parent
	def ITreeNode thisGroovyNode
	def ViewPart  runViewPart
	def swt = new SwtBuilder() 

	def combo_values = []
	
	FormTest(Composite p_parent, ITreeNode p_thisGroovyNode, ViewPart p_runViewPart) {
		parent = p_parent
		thisGroovyNode = p_thisGroovyNode
		runViewPart = p_runViewPart
		println "Работа с DragonAct"
	}
	
	public static void main(String[] args) {
	}
	
	public void run() {
		swt.setCurrent(parent);
		
		def compTraceLog0 = swt.composite {	
      	gridLayout(numColumns:1)
		//gridData( style:"fill_horizontal" )
		gridData( style:"fill_both" )
		def comp0 = composite {
			gridData( style:"fill_both" )
	      	gridLayout(numColumns:1) 
			def compTraceLog1 = composite {
			gridData( style:"fill_both" )
	      	gridLayout(numColumns:1)
	      	
	      	//def MAIN_DG_FILE = "Main.graphml";
			def MAIN_DG_FILE = "DrakonGen2.graphml";
			
	 		def tab = tabFolder( style:"none" ) {
				gridData( style:"fill_horizontal" )
				tabItem( style:"none", text:"#{TAB_RUN_DRAKON}" ) {
					 composite {
						gridData( style:"fill_both" )
			         	gridLayout(numColumns:3) 
 			        	label( style:"none", text:"#{TEXT_ENTER_DRAKON_FILE_NAME}")
						combo_values = ["#{DRAKON_FILE1}","#{DRAKON_FILE2}","#{DRAKON_FILE3}","#{DRAKON_FILE4}","#{DRAKON_FILE5}"];
						def garphml_file = combo(text:"DrakonGen2.graphml", items:combo_values );
						garphml_file.text = "#{DRAKON_FILE1}";
						
						button( style:"push",text:"#{BTN_EXECUTE}", background:[0, 255, 255] )  {
							onEvent(type:"Selection", closure:{
								
								//System.out.println(" garphml_file = " + garphml_file.text);
								
								// -- устанавливаем BASE_DIR
								Settings.setProperty("BASE_DIR", "#{BASE_DIR}");
								 
								System.out.println(" --- WORK_DIR: #{WORK_DIR}"); 
								System.out.println(" --- BASE_DIR: " + Settings.getProperty("BASE_DIR")); 
								
								MAIN_DG_FILE = "#{SCHEMES_DIR}/" + garphml_file.text;
								System.out.println(" --- Main drakon file: '" + MAIN_DG_FILE+"'");
								
								// -- переменная da
								DrakonAct da = new DrakonAct();
								// -- строим Graph из файла Маin
								Graph graph = new TinkerGraph();

								GraphMLReader reader = new GraphMLReader(MAIN_DG_FILE, graph);

								try {
									reader.read();
									System.out.println(" <--- Прочитали файл " + MAIN_DG_FILE);
								} catch (Exception e) {
									System.err.println(" err " + e.getMessage());
									e.printStackTrace();
								}
								// -- выполнение активностей
								da.activate_drakon(graph);
								dq = null;
								})  
						}
					} //-- composite
				} //-- tabItem
				tabItem( style:"none", text:"#{TAB_RESULT}" ) {
					 composite {
						gridData( style:"fill_both" )
			         	gridLayout(numColumns:3) 
			        	label( style:"none", text:"Field 21" )
			        	def v_testVar21 = text( style:"border" ) {  
				        	gridData( style:"fill_horizontal", grabExcessHorizontalSpace:true )
			        	}
						text( editable:false, enabled:true, style:"none", text:"some texts" )
			        	
			        	label( style:"none", text:"Поле 22" )
			        	def v_testVar22 = text( style:"border" ) {  
				        	gridData( style:"fill_horizontal", grabExcessHorizontalSpace:true )
			        	}
						text( editable:false, enabled:true, style:"none", text:"произвольный текст 22" )		        	
						button( style:"push",text:"Кнопка 21", background:[0, 255, 255] )  {
							onEvent(type:"Selection", closure:{
								println "Вы нажали на кнопку 21"
								println "Значение поля 21="+v_testVar21.text
								println "Значение поля 22="+v_testVar22.text
								})  
						}
					} //-- composite
				} //-- tabItem
				} //-- tab
			} //-- compTraceLog1
			} //-- comp0 
		}
	} //-- run()
	
} //-- class