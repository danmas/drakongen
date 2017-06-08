package ru.erv.drakongen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MakeAutoCodeMark {

	public static void main(String argv[]) {

		try {

			Document doc;
			// String in_xml = argv[0]; //--
			// "C:/ERV/PROJECTS/WRK_TestXML/TextXML/src/data/DG_Algoritms_NEW.graphml"
			// String out_xml = argv[1]; //--
			// "C:/ERV/PROJECTS/WRK_TestXML/TextXML/src/data/DG_Algoritms_NEW_2.graphml";

			String in_xml  = "C:/RDTEX/CB-NRD/work/schemes/DG_Algoritms_ACM_1.graphml";
			String out_xml = "C:/RDTEX/CB-NRD/work/schemes/DG_Algoritms_ACM_NEW.graphml";

			System.out
					.println("--- Выставление auto_code_mark для всех"
							+ "\n драконовских иконок(для которых не было введен code_mark)\n"
							+ "\n из файла: " + in_xml 
							+ "\n в  файл : " + out_xml);

			File fXmlFile = new File(in_xml);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			System.out.println("Root element :"
					+ doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("node");

			int cnt = 0;
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					String id = eElement.getAttribute("id");
					String node_type = null;
					String code_mark = null;
					Element e_data_d6 = null;
					Element e_node_type = null;
					Element e_code_mark = null;

					NodeList nList2 = eElement.getElementsByTagName("data");

					
					for (int temp2 = 0; temp2 < nList2.getLength(); temp2++) {

						Node nNode2 = nList2.item(temp2);
						Element eElement2 = (Element) nNode2;

						String key = eElement2.getAttribute("key");

						if (key.equals("d5")) {
							e_node_type = eElement2;
							node_type = eElement2.getTextContent();
						}
						if (key.equals("d6")) {
							e_data_d6 = eElement2;
							//data_d6 = eElement2.getTextContent();
						}
						if (key.equals("d4")) {
							e_code_mark = eElement2;
							code_mark = eElement2.getTextContent();
//							if(code_mark == null || code_mark.equals("")) {
//								CDATASection cdataNode = doc.createCDATASection("auto_code_mark_" + id);
//								e_code_mark.appendChild(cdataNode);
//							}
						}
					}
					
					if(code_mark == null || code_mark.equals("")
							&& e_data_d6 != null ) {
						CDATASection cdataNode = doc.createCDATASection("auto_code_mark_" + id);
						e_code_mark.appendChild(cdataNode);
						System.out.println("id: " + id + "  node_type: "
								+ node_type + " code_mark:" + code_mark);
					}

//					System.out.println("id: " + id + "  node_type: "
//							+ node_type + " code_mark:" + code_mark);

				}


			}
			
			try {
				Transformer tr = TransformerFactory.newInstance()
						.newTransformer();
				tr.setOutputProperty(OutputKeys.INDENT, "yes");
				tr.setOutputProperty(OutputKeys.METHOD, "xml");
				tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				// tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
				// "roles.dtd");
				// tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
				// "4");

				// send DOM to file
				tr.transform(new DOMSource(doc), new StreamResult(
						new FileOutputStream(out_xml)));
				System.out.println("Записан файл: "+out_xml);	
			} catch (TransformerException te) {
				System.out.println(te.getMessage());
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
