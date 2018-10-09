import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

import java.util.HashMap;
import java.util.Map;

public class DOMParserReader {

    private File file;
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private Document document;
    private NodeList nodeList;
    public DOMParserReader(String xmlFile) throws Exception{
        file = new File(xmlFile);
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
        document = documentBuilder.parse(file);
        document.getDocumentElement().normalize();
    }
    
   public String getNodeName(){
       return document.getDocumentElement().getNodeName();
   }

   public void readElementByTagName(String name){
       nodeList = document.getElementsByTagName(name);
   }
   
    public Map<String, String> getData(String key, String value) {
        Map<String, String> map = new HashMap<>();
        
        for (int index = 0; index < nodeList.getLength(); index++) {
            
            Node node = nodeList.item(index);
            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) node;
                String tag_1 = eElement.getElementsByTagName(key).item(0).getTextContent();
                String tag_2 = eElement.getElementsByTagName(value).item(0).getTextContent();
                map.put(tag_1, tag_2);
                
            }
        }
        return map;
    }
  public static void main(String argv[]) throws Exception{
      DOMParserReader reader = new DOMParserReader(System.getProperty("user.dir")+"\\src\\MalaysiaState.xsd");
      //System.out.println(reader.getNodeName());
      reader.readElementByTagName("state");
      Map<String, String> map = reader.getData("name", "priority");
     
//      for (String name : map.keySet()) {
//
//          String key = name.toString();
//          String value = map.get(name).toString();
//          System.out.println(key + " " + value);
//
//      }
      
//    
//    try {
//
//	File fXmlFile = new File("A:\\FYP Project\\Student Side\\Java\\LodgingServiceSystem\\src\\MalaysiaState.xsd");
//	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//	Document doc = dBuilder.parse(fXmlFile);
//			
//	//optional, but recommended
//	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
//	doc.getDocumentElement().normalize();
//
//	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//			
//	NodeList nList = doc.getElementsByTagName("staff");
//			
//	System.out.println("----------------------------");
//
//	for (int temp = 0; temp < nList.getLength(); temp++) {
//
//		Node nNode = nList.item(temp);
//				
//		System.out.println("\nCurrent Element :" + nNode.getNodeName());
//				
//		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//
//			Element eElement = (Element) nNode;
//
//			System.out.println("Staff id : " + eElement.getAttribute("id"));
//			System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
//			System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
//			System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
//			System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
//
//		}
//	}
//    } catch (Exception e) {
//	e.printStackTrace();
//    }
  }

}
