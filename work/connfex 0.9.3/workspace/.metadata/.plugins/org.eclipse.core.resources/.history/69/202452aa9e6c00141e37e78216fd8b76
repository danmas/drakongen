//import ru.erv.grakongen.DrakonAct;
import ru.erv.drakongen.DrakonAct;
import ru.erv.drakongen.Settings;
import ru.erv.drakongen.parser.*;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;


class GroovyTest {

	public void run() {
		String MAIN_DG_FILE = "Main.graphml";
		
		// -- устанавливаем BASE_DIR
		//Settings.setProperty("BASE_DIR", "#{CONFEX_DIR}/../../develop/DG2J/DragonGen2J/");
		Settings.setProperty("BASE_DIR", "c:/ERV/WRK/drakongen/develop/DG2J/DragonGen2J/");
		
		System.out.println(" <--- BASE_DIR: " + Settings.getProperty("BASE_DIR")); 
		
		MAIN_DG_FILE = Settings.getProperty("BASE_DIR") + "../../../work/Schemes/" + MAIN_DG_FILE;
		System.out.println(" <--- Main drakon file: " + MAIN_DG_FILE);
		
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
	}

	public static void main(String[] args) {
		System.err.println(" !!! Working main! ");
	}
}
