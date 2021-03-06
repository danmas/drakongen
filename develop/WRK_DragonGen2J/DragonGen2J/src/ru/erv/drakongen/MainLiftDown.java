package ru.erv.drakongen;


import ru.erv.drakongen.parser.GraphMLReader;
import ru.erv.drakongen.utils.FileUtils;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;

public class MainLiftDown {

	/**
	 * @param args
	 */
	  private static String base_dir = "C:/RDTEX/CB-NRD/work/";  
	  private static String xml_file = "C:/RDTEX/CB-NRD/work/schemes/DG_Algoritms_ACM.graphml"; 
	  
	  public static void main(String[] args) throws Exception {
	  
		if(args.length == 2) {
			base_dir = args[0];
			xml_file = args[1];
			
		} else {
//			System.err.println("Должно быть два аргумента! Базовый каталог и файл со схемой.");
//			System.err.println("Error. Must be two arguments!");
//			System.err.println("java -jar drakongen.jar <base dir> <schema file>");
//			
			base_dir = "C:/RDTEX/CB-NRD/work/";  
			xml_file = "C:/RDTEX/CB-NRD/work/schemes/DG_Algoritms_ACM.graphml"; 
			
//			return;
		}
		System.out.println("Базовый каталог:"+base_dir+" Файл со схемой:"+xml_file);
		  
		DrakonAct da = new DrakonAct();
		Graph graph = new TinkerGraph();

		Settings.setDebug(false);
		Settings.setProperty("BASE_DIR", base_dir); //BASE_DIR);
		
		GraphMLReader reader = new GraphMLReader(xml_file, graph);

		try {
			reader.read();
			System.out.println(" <--- Was read:" + xml_file);
		} catch (Exception e) {
			System.err.println(" err " + e.getMessage());
			e.printStackTrace();
			return;
		}
		da.activate_drakon(graph);
	  }
	  
	  
//	  public static void main2(String[] args) throws Exception {
//			base_dir = args[0];
//			xml_file = args[1];
//			
//			System.out.println("Базовый каталог:"+base_dir+" Файл со схемой:"+xml_file);
//		
//		Settings.setDebug(false);
//		Settings.setProperty("BASE_DIR", base_dir); //BASE_DIR);
//		  
//		//String text = FileUtils.fileRead(full_file_name);
//	    Graph graph = new TinkerGraph();
//	    
//	    
//		GraphMLReader reader = new GraphMLReader(xml_file, graph);
//		
//		
//		try {
//			reader.read();
//			System.out.println(" <--- Прочитали файл "+ xml_file);
////			System.out.println(graph.toString());
////			
////			for (Vertex v : graph.getVertices()) {
////				String str = "node code:"+ v.getProperty("code")+" com:"+v.getProperty("comment")
////						+" type:"+v.getProperty("type");
////				
////				System.out.println(str); 
////			}
////
////			for (Edge e : graph.getEdges()) {
////				String str = "edge dglabel:"+ e.getProperty("dglabel") + " type:"+e.getProperty("type");
////				
////				System.out.println(str); 
////			}
//		} catch(Exception e) {
//			System.err.println(" err " +e.getMessage());
//			e.printStackTrace();
//		}
//		
//	    DrakonGen2 dg = new DrakonGen2();
//	    dg.parse_drakon(graph);
//	    
//		
//		/*
//	    DgVertex cv1 = new DgVertex("1", (TinkerGraph)graph);
//	    DgVertex cv2 = new DgVertex("2",(TinkerGraph)graph);
//	    
//	    cv1.setProperty("code","cod 1");
//
//	    graph.addEdge("", cv1, cv2, "arg3");
//	    
//	    Vertex v1 = graph.addVertex(cv1);
//	    Vertex v2 = graph.addVertex(cv2);
//	    v1.setProperty("code","cod 11");
//	    
//	    graph.addEdge("ie1", v1, v2, "sdfs");
//	    graph.addEdge("ie2", v2, v1, "sdfs");
//	    */
//	    /*
//		for (Vertex v : graph.getVertices()) {
//			
//			Object o = v.getId();
//			//DgVertex dgv = (DgVertex) v;
//			System.out.println("v.getProperty(code) " + v.getProperty("code")
//					+ "v.getProperty(comment) " + v.getProperty("comment")
//					); 
//			
//			int i = 0;
//			for (Edge e : v.getOutEdges()) {
//				i++;
//			}
//			System.out.println(i);
//			i = 0;
//			for (Edge e : v.getInEdges()) {
//				i++;
//			}
//			System.out.println(i);
//			
//		}*/
//	  }

	  
	  /*
	  public static void main(String[] args) throws Exception {
		    Graph graph = new TinkerGraph();
		    
		    ru.erv.drakongen.Vertex cv1 = new ru.erv.drakongen.Vertex("1");
		    ru.erv.drakongen.Vertex cv2 = new ru.erv.drakongen.Vertex("2");
		    
		    cv1.setCode("cod 1");

		    //Vertex v1 = graph.addVertex("v1");
		    //Vertex v2 = graph.addVertex("v2");
		    Vertex v1 = graph.addVertex(cv1);
		    Vertex v2 = graph.addVertex(cv2);
		    
		    Edge e1 = graph.addEdge("e1", v1, v2, "sdfs");
		    Edge e2 = graph.addEdge("e2", v2, v1, "sdfs");
		    
		    for (Vertex v : graph.getVertices()) {
		        System.out.println("------");
		        System.out.println(v);

		        //java.lang.Iterable<Edge> ie = v.getInEdges(); 
			    for (Edge e : v.getInEdges()) {
			    	System.out.println("++++++" + e);
			    }
		        
		        
		        for (String key : v.getPropertyKeys()) {
		            System.out.println(key + "=" + v.getProperty(key));
		        }
		    }
		    
		  }
	  */
	  /*
	  public static void main(String[] args) throws Exception {
	    Graph graph = new TinkerGraph();
	    GraphMLReader reader = new GraphMLReader(graph);
	 
	    InputStream is = new BufferedInputStream(new FileInputStream(XML_FILE));
	    reader.inputGraph(is);
	 
	    Iterable<Vertex> vertices = graph.getVertices();
	    Iterator<Vertex> verticesIterator = vertices.iterator();
	 

	    System.out.println();
	    for (Vertex v : graph.getVertices()) {
	        System.out.println("------");
	        System.out.println(v);

	        //java.lang.Iterable<Edge> ie = v.getInEdges(); 
		    for (Edge e : v.getInEdges()) {
		    	System.out.println("++++++" + e);
		    }
	        
	        
	        for (String key : v.getPropertyKeys()) {
	            System.out.println(key + "=" + v.getProperty(key));
	        }
	    }
	    for (Edge e : graph.getEdges()) {
	        System.out.println("------");
	        System.out.println(e);
	        for (String key : e.getPropertyKeys()) {
	            System.out.println(key + "=" + e.getProperty(key));
	        }
	    }    
	    
	    
	    while (verticesIterator.hasNext()) {
	 
	      Vertex vertex = verticesIterator.next();
	      Iterable<Edge> edges = vertex.getInEdges();
	      Iterator<Edge> edgesIterator = edges.iterator();
	 
	      
	      
	      while (edgesIterator.hasNext()) {
	 
	        Edge edge = edgesIterator.next();
	        Vertex outVertex = edge.getOutVertex();
	        Vertex inVertex = edge.getInVertex();
	        

	        String label = (String) outVertex.getProperty("description") ;
	        String code  = (String) inVertex.getProperty("url");
	        
	        System.out.println(" ---> "+ label + " " + code);        
	        
//	        String person = (String) outVertex.getProperty("name");
//	        String knownPerson = (String) inVertex.getProperty("name");
//	        int since = (Integer) edge.getProperty("since");
	// 
//	        String sentence = person + " " + edge.getLabel() + " " + knownPerson
//	                + " since " + since + ".";
//	        System.out.println(sentence);
	      }
	    }
	  }*/

	  /*
	  Vertex vertex = v;
      Iterable<Edge> edges = vertex.getInEdges();
      Iterator<Edge> edgesIterator = edges.iterator();
 
      while (edgesIterator.hasNext()) {
 
        Edge edge = edgesIterator.next();
        Vertex outVertex = edge.getOutVertex();
        Vertex inVertex = edge.getInVertex();
        
        String label = (String) inVertex.getProperty("description") ;
		String code  = (String) inVertex.getProperty("url");
        String com  = (String) inVertex.getProperty("NodeLabel");
		System.out.println(" edge.getInVertex ---> "+ label + " " + code +  " com= " + com);
		
		label = (String) outVertex.getProperty("description") ;
		code  = (String) outVertex.getProperty("url");
        com  = (String) inVertex.getProperty("NodeLabel");
		System.out.println(" edge.getOutVertex ---> "+ label + " " + code +  " com= " + com + "\n");
      }				
  
  
      System.out.println(" -------- Выходные ребра ---- ");
      
      edges = vertex.getOutEdges();
      edgesIterator = edges.iterator();
 
      while (edgesIterator.hasNext()) {
 
        Edge edge = edgesIterator.next();
        Vertex outVertex = edge.getOutVertex();
        Vertex inVertex = edge.getInVertex();
        
        String label = (String) inVertex.getProperty("description") ;
        String code  = (String) inVertex.getProperty("url");
        String com  = (String) inVertex.getProperty("NodeLabel");
        
        System.out.println(" edge.getInVertex ---> "+ label + " " + code + " com= " + com);

        label = (String) outVertex.getProperty("description") ;
        code  = (String) outVertex.getProperty("url");
        com  = (String) inVertex.getProperty("NodeLabel");
        System.out.println(" edge.getOutVertex ---> "+ label + " " + code + " com= " + com + "\n");
      }				
  
  
		Vertex in_1 = getInNode(v,0);
		Vertex in_2 = getInNode(v,1);
		Vertex out_1 = getOutNode(v,0);
		Vertex out_2 = getOutNode(v,1);
		Vertex out_3 = getOutNode(v,2);
		
		System.out.println(" -- in_1 "+ getCode(in_1) + " | " + getComment(in_1) + " | " + getIconType(in_1));
		System.out.println(" -- in_2 "+ getCode(in_2) + " | " + getComment(in_2) + " | " + getIconType(in_2));
		System.out.println(" -- out_1 "+ getCode(out_1) + " | " + getComment(out_1) + " | " + getIconType(out_1));
		System.out.println(" -- out_2 "+ getCode(out_2) + " | " + getComment(out_2) + " | " + getIconType(out_2));
		System.out.println(" -- out_3 "+ getCode(out_3) + " | " + getComment(out_3) + " | " + getIconType(out_3));
 */
	  /*  
      String text = "<DG2J code_mark=\"mark1:1564562\">1 ddd ddd ddd 2</DG2J> sd fs fsd"
      		+ "s fsfsdf s sd sd<DG2J code_mark=\"mark1:3564564\">3 ddd ddd ddd 4</DG2J>";
      
          int i = 0; 
          while(true) {
	        	int i1 = text.indexOf("<DG2J code_mark=",i);
	        	if(i1 >= 0 ) {
	        		
	        		
	        		//-- выделяем маркер и код 
	        		
	        		int i2 = text.indexOf('"',i1+"<DG2J code_mark=".length()+1);
	        		if(i2<0)
	        			break;
	        		String mark_code = text.substring(i1+"<DG2J code_mark=".length()+1,i2);
	        		System.out.println("-->"+i1 + " " + i2 + " " + mark_code);
	        		
	        		int i3 = text.indexOf('>',i2);
	        		if(i3<0)
	        			break;
	        		int i4 = text.indexOf("</DG2J>",i3+1);
	        		if(i4<0)
	        			break;
	        		String body = text.substring(i3+1,i4);
	        		
	        		
	        		System.out.println("-->"+i3 + " " + i4 + "--" + body + "--");
	        		i = i4+"</DG2J>".length();
	        	} else {
	        		break;
	        	}
          }
*/
	  
	  
}
