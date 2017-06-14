/*
*/
import ru.erv.drakongen.*;
import ru.erv.drakongen.parser.*;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;


public class JavaTest {

	

	public void run() {
		System.err.println(" !!! run() ");
	}

	public static void main(String[] args) {
		String MAIN_DG_FILE = "c:/YandexDisk/FLASH/WRK/Schemes/Main.graphml";
		System.out.println(" <--- ??? ??? ???  Main drakon file: " + MAIN_DG_FILE);
		// -- ????????????? BASE_DIR
		Settings.setProperty("BASE_DIR", "c:/YandexDisk/FLASH/WRK/DG2J/DragonGen2J/");
		System.out.println(" <--- BASE_DIR: " + MAIN_DG_FILE);
		// -- ?????????? da
		DrakonAct da
		// -- ????????? ?????? DrakonAct
		= new DrakonAct();
		// -- ?????? Graph ?? ????? ??in
		Graph graph = new TinkerGraph();

		GraphMLReader reader = new GraphMLReader(MAIN_DG_FILE, graph);
 
		try {
			reader.read();
			System.out.println(" <--- ????????? ???? " + MAIN_DG_FILE);
		} catch (Exception e) {
			System.err.println(" err " + e.getMessage());
			e.printStackTrace();
		}
		// -- ?????????? ???????????
		da.activate_drakon(graph);
	}
}