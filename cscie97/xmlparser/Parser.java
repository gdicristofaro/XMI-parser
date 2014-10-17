package cscie97.xmlparser;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Parser {
	public static String UNKNOWN_CONSTANT = "@UNKNOWN";
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		
		if (args.length != 2) {
			p("parameters should be as follows: Parser <path to xmi file> <path to output html file>");
			return;
		}
		
		
		Collection<ClassObject> classes = getClasses(loadDocument(new File(args[0])));
		// for debugging
		/*for (ClassObject c : classes) {
			p("");
			p(c.name + " - " + c.description);
			
			for (AssociationObject a : c.associations)
				p("    " + a.toString());
			
			for (PropertyObject a : c.properties)
				p("    " + a.toString());
			
			for (MethodObject a : c.methods)
				p("    " + a.toString());
			

		}*/
		
		PrintStream out = new PrintStream(args[1]);
		out.append(HtmlContent.newDoc(classes));
		out.close();
	}
	
	public static void p(String s) {
		System.out.println(s);
	}
	
	public static String decode(String s) {
		try {
			return java.net.URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Document loadDocument(File xml) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xml);
		doc.getDocumentElement().normalize();
		return doc;
	}
	
	public static Collection<ClassObject> getClasses(Document d) {
		IDInf ids = new IDInf();
		ids.setup(d);
		
		NodeList nl = d.getElementsByTagName("UML:Class");		
		Collection<ClassObject> classes = new ArrayList<ClassObject>();
		
		for (int i=0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			
			Element element = null;
			try {
				element = (Element) node;
				
				if (!ids.inClassDiagram(element))
					continue;
				
				classes.add(getClass(element, ids));
			}
			catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}

		return classes;
	}
	
	
	private static class DescInf {
		public final String name;
		public final String description;
		
		public DescInf(String name, String description) {
			this.name = name;
			this.description = description;
		}
	}
	
	private static class IDInf {
		private final Map<String, String> ids = new HashMap<String, String>();
		private Map<String, Set<AssociationObject>> associations = null;
		private final Set<String> classDiagramIDs = new HashSet<String>();
		
		public void setup(Document d) {
			// get all class and primitive items
			placeClasses(d, "UML:Primitive");
			Set<Element> missingClasses = placeClasses(d, "UML:Class");
			missingClasses.addAll(placeClasses(d, "UML:Interface"));
			getTemplateClasses(d, missingClasses);

			// find out which classes to add
			NodeList classDiagrams = d.getElementsByTagName("JUDE:Diagram");
			for (int i = 0; i < classDiagrams.getLength(); i++) {
				Element e = (Element) classDiagrams.item(i);
				if (!e.getAttribute("typeInfo").equals("Class Diagram"))
					continue;
				
				NodeList classes = e.getElementsByTagName("JUDE:UPresentation.semanticModel");
				for (int a = 0; a < classes.getLength(); a++) {
					NodeList semanticModelElement = ((Element) classes.item(a)).getElementsByTagName("UML:Class");
					if (semanticModelElement == null || semanticModelElement.getLength() == 0)
						continue;
					
					Element internal = (Element) semanticModelElement.item(0);
					String id = internal.getAttribute("xmi.idref");
					classDiagramIDs.add(id);
				}
			}
						
			// setup the associations
			associations = Parser.getAssociations(d, ids);
			
		}
		
		private static Element ele(Element parent, String...descendents) {
			for (String d : descendents) {
				parent = (Element) parent.getElementsByTagName(d).item(0);
			}
			
			return parent;
		}
		
		private void getTemplateClasses(Document body, Set<Element> missingClasses) {
			// get parameter types (id -> class)
			Map<String, String> parameterTypes = new HashMap<String, String>();
			NodeList paramTypes = body.getElementsByTagName("UML:TemplateParameterSubstition");
			for (int i = 0; i < paramTypes.getLength(); i++) {
				try {
					Element e = (Element) paramTypes.item(i);
					String classType = ids.get(ele(e, "UML:TemplateParameterSubstition.actual", "UML:Class")
							.getAttribute("xmi.idref"));
					parameterTypes.put(e.getAttribute("xmi.id"), classType);
				}
				catch (Exception e) {}
			}
			
			// get template  types (id -> class)
			Map<String, String> templateSigTypes = new HashMap<String, String>();
			NodeList genTypes = body.getElementsByTagName("UML:TemplateSignature");
			for (int i = 0; i < genTypes.getLength(); i++) {
				try {
					Element e = (Element) genTypes.item(i);
					String classType = ids.get(ele(e, "UML:TemplateSignature.template", "UML:TemplateableElement")
							.getAttribute("xmi.idref"));
					templateSigTypes.put(e.getAttribute("xmi.id"), classType); 
				}
				catch (Exception e) {}
			}
			
			// get generic types (id -> class signature)
			Map<String, String> genericItems = new HashMap<String, String>();
			NodeList genItems = body.getElementsByTagName("UML:TemplateBinding");
			for (int i = 0; i < genItems.getLength(); i++) {
				Element e = (Element) genItems.item(i);
				
				String genericClassType = null;
				try {
					String genericID = 
						ele(e, "UML:TemplateBinding.signature", "UML:TemplateSignature").getAttribute("xmi.idref");
					genericClassType = templateSigTypes.get(genericID);
				}
				catch (Exception exc) { 
					continue; 
				}
				
				if (genericClassType == null)
					continue;
				
				List<String> genericParams = new ArrayList<String>();
				NodeList thisParamTypes = ((Element) e.getElementsByTagName("UML:TemplateBinding.parameterSubstition")
						.item(0)).getElementsByTagName("UML:TemplateParameterSubstition");
				for (int a = 0; a < thisParamTypes.getLength(); a++)
					genericParams.add(
						parameterTypes.get(
							((Element) thisParamTypes.item(a)).getAttribute("xmi.idref")));
				
				
				
				if (genericParams.size() > 0)
					genericClassType += "<" + genericParams.get(0);
				
				for (int a = 1; a < genericParams.size(); a++)
					genericClassType += ", " + genericParams.get(a);
				
				if (genericParams.size() > 0)
					genericClassType += ">";
				
				genericItems.put(e.getAttribute("xmi.id"), genericClassType);
			}

			
			for (Element e : missingClasses) {
				try {
					String genericID = ele(e, "UML:Classifier.templateBinding", "UML:TemplateBinding")
					.getAttribute("xmi.idref");
					
					String classGeneric = genericItems.get(genericID);
					ids.put(e.getAttribute("xmi.id"), classGeneric);					
				}
				catch (Exception exc) {}
			}
		}
		
		// returns unnamed classes
		private Set<Element> placeClasses(Document body, String tagname) {
			NodeList items = body.getElementsByTagName(tagname);
			Set<Element> missingNames = new HashSet<Element>();
			
			for (int i = 0; i < items.getLength(); i++) {
				Element e = (Element) items.item(i);
				
				// try to see if this is some template kind of stuff
				if (e.getAttribute("name") == null || e.getAttribute("name").equals(""))
					missingNames.add(e);
				else
					ids.put(e.getAttribute("xmi.id"), decode(e.getAttribute("name")));
			}
			
			return missingNames;
		}
		
		
		public boolean inClassDiagram(Element classNode) {
			return classDiagramIDs.contains(classNode.getAttribute("xmi.id"));
		}

		
		public Set<AssociationObject> getAssociations(String id) {
			Set<AssociationObject> toRet = associations.get(id);
			return (toRet == null) ? new HashSet<AssociationObject>() : toRet;
		}
		
		public String getType(String typeID) {
			return ids.get(typeID);
		}
	}
	
	
	private static DescInf getDescriptor(Element e) {
		String name = decode(e.getAttributes().getNamedItem("name").getNodeValue());
		String description = "";
		NodeList descriptions = e.getElementsByTagName("UML:ModelElement.definition");
		
		if (descriptions.getLength() > 0)
			description = decode(descriptions.item(0).getAttributes().getNamedItem("xmi.value").getNodeValue());
		return new DescInf(name, description);
	}
	
	
	public static ClassObject getClass(Element classNode, IDInf ids) {
		DescInf results = getDescriptor(classNode); 
		
		NodeList methodNodes = classNode.getElementsByTagName("UML:Operation");

		Collection<MethodObject> methods = getNodeColl(methodNodes, ids,
			new Transformer<MethodObject>() {
				public MethodObject transform(Node n, IDInf ids) {
					return getMethod(n, ids);
				}
			});
		
		NodeList propertyNodes = classNode.getElementsByTagName("UML:Attribute");
		
		Collection<PropertyObject> properties = getNodeColl(propertyNodes, ids,
			new Transformer<PropertyObject>() {
				public PropertyObject transform(Node n, IDInf ids) {
					return getProperty(n, ids);
				}
			});
			
		
		return new ClassObject(results.name, results.description, methods, properties, 
				ids.getAssociations(classNode.getAttribute("xmi.id")));
	}
	
	private static interface Transformer<T> {
		public T transform(Node n, IDInf ids);
	}
	
	public static <T> Collection<T> getNodeColl(NodeList nodes, IDInf ids, Transformer<T> transformer) {
		Collection<T> collection = new ArrayList<T>();
	
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			try {
				collection.add(transformer.transform(node, ids));
			}
			catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return collection;
	}
	
	public static MethodObject getMethod(Node methodNode, IDInf ids) {
		Element e = (Element) methodNode;
		
		DescInf res = getDescriptor(e);
		
		// return signature of form like (identifier:String):Node
		
		String returnType = "";
		List<String> params = new ArrayList<String>();
		
		NodeList parameterNodes = e.getElementsByTagName("UML:Parameter");
		for (int i = 0; i < parameterNodes.getLength(); i++) {
			Element param = (Element) parameterNodes.item(i);
			
			String typeID = ((Element) param.getElementsByTagName("UML:Parameter.type").item(0))
					.getElementsByTagName("UML:Classifier").item(0)
					.getAttributes().getNamedItem("xmi.idref").getNodeValue();
			
			String classType = ids.getType(typeID);
			if (classType == null)
				classType = UNKNOWN_CONSTANT;
			
			String name = param.getAttribute("name");
			
			if (param.getAttribute("kind").equals("return"))
				returnType = classType;
			else
				params.add(name + ":" + classType);
		}
		
		String formattedParams = (params.size() > 0) ? params.get(0) : "";
		for (int i = 1; i < params.size(); i++)
			formattedParams += ", " + params.get(i);

		return new MethodObject(res.name, res.description, "(" + formattedParams + "):" + returnType);
	}
	
	public static PropertyObject getProperty(Node propertyNode, IDInf ids) {
		Element e = (Element) propertyNode;
		DescInf res = getDescriptor(e);
		
		String typeID = ((Element) ((Element) e.getElementsByTagName("UML:StructuralFeature.type").item(0))
			.getElementsByTagName("UML:Classifier").item(0)).getAttribute("xmi.idref");
				
		String type = ids.getType(typeID);
		if (type == null)
			type = UNKNOWN_CONSTANT;
		
		return new PropertyObject(res.name, res.description, type);
	}
	
	
	public static Map<String, Set<AssociationObject>> getAssociations(Document d, Map<String, String> ids) {
		Map<String, Set<AssociationObject>> returnMap = new HashMap<String, Set<AssociationObject>>();
		
		NodeList associations = d.getElementsByTagName("UML:AssociationEnd");
		for (int i = 0; i < associations.getLength(); i++) {
			try {
				Element e = (Element) associations.item(i);
				
				if (!e.getAttribute("navigableType").equals("navigable"))
					continue;

				String ownerID = ((Element) ((Element) e.getElementsByTagName("UML:Feature.owner").item(0))
					.getElementsByTagName("UML:Classifier").item(0)).getAttribute("xmi.idref");
				
				String endID = ((Element) ((Element) e.getElementsByTagName("UML:AssociationEnd.participant").item(0))
						.getElementsByTagName("UML:Classifier").item(0)).getAttribute("xmi.idref");
				
				String end = ids.get(endID);
				
				DescInf res = getDescriptor(e);
				
				if (!returnMap.containsKey(ownerID))
					returnMap.put(ownerID, new HashSet<AssociationObject>());
				
				returnMap.get(ownerID).add(new AssociationObject(res.name, res.description, end));
			}
			catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}
		
		return returnMap;
	}
}
