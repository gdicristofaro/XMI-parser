package cscie97.xmlparser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

public class HtmlContent {

	// replace @CLASSDATA
	private static String document = "<!DOCTYPE html><html><head><title>design template</title><meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\"><style type=\"text/css\">ol{margin:0;padding:0}.c3{border-bottom-width:1pt;border-top-style:solid;width:129.8pt;background-color:#ffffff;border-right-style:solid;padding:5pt 5pt 5pt 5pt;border-bottom-color:#000000;border-top-width:1pt;border-bottom-style:solid;vertical-align:top;border-top-color:#000000;border-left-color:#000000;border-right-color:#000000;border-left-style:solid;border-right-width:1pt;border-left-width:1pt}.c13{border-bottom-width:1pt;border-top-style:solid;width:240.8pt;background-color:#ffffff;border-right-style:solid;padding:5pt 5pt 5pt 5pt;border-bottom-color:#000000;border-top-width:1pt;border-bottom-style:solid;vertical-align:top;border-top-color:#000000;border-left-color:#000000;border-right-color:#000000;border-left-style:solid;border-right-width:1pt;border-left-width:1pt}.c11{border-bottom-width:1pt;border-top-style:solid;width:129.8pt;border-right-style:solid;padding:5pt 5pt 5pt 5pt;border-bottom-color:#000000;border-top-width:1pt;border-bottom-style:solid;vertical-align:top;border-top-color:#000000;border-left-color:#000000;border-right-color:#000000;border-left-style:solid;border-right-width:1pt;border-left-width:1pt}.c14{border-bottom-width:1pt;border-top-style:solid;width:240.8pt;border-right-style:solid;padding:5pt 5pt 5pt 5pt;border-bottom-color:#000000;border-top-width:1pt;border-bottom-style:solid;vertical-align:top;border-top-color:#000000;border-left-color:#000000;border-right-color:#000000;border-left-style:solid;border-right-width:1pt;border-left-width:1pt}.c8{border-bottom-width:1pt;border-top-style:solid;width:97.5pt;border-right-style:solid;padding:5pt 5pt 5pt 5pt;border-bottom-color:#000000;border-top-width:1pt;border-bottom-style:solid;vertical-align:top;border-top-color:#000000;border-left-color:#000000;border-right-color:#000000;border-left-style:solid;border-right-width:1pt;border-left-width:1pt}.c1{vertical-align:baseline;color:#000000;font-size:11pt;font-style:normal;background-color:#ffffff;font-family:\"Arial\";text-decoration:none;font-weight:normal}.c5{vertical-align:baseline;color:#000000;font-size:11pt;font-style:normal;font-family:\"Arial\";text-decoration:none;font-weight:bold}.c2{line-height:1.0;padding-top:0pt;widows:2;orphans:2;text-align:left;direction:ltr;padding-bottom:0pt}.c7{widows:2;orphans:2;direction:ltr}.c12{margin-right:auto;border-collapse:collapse}.c16{max-width:468pt;padding:72pt 72pt 72pt 72pt}.c0{font-size:14pt;font-weight:bold}.c6{font-style:italic}.c4{font-size:14pt}.c15{background-color:#ffffff}.c10{height:0pt}.c9{height:11pt}.title{widows:2;padding-top:0pt;line-height:1.15;orphans:2;text-align:left;color:#000000;font-size:21pt;font-family:\"Trebuchet MS\";padding-bottom:0pt;page-break-after:avoid}.subtitle{widows:2;padding-top:0pt;line-height:1.15;orphans:2;text-align:left;color:#666666;font-style:italic;font-size:13pt;font-family:\"Trebuchet MS\";padding-bottom:10pt;page-break-after:avoid}li{color:#000000;font-size:11pt;font-family:\"Arial\"}p{color:#000000;font-size:11pt;margin:0;font-family:\"Arial\"}h1{widows:2;padding-top:10pt;line-height:1.15;orphans:2;text-align:left;color:#000000;font-size:16pt;font-family:\"Trebuchet MS\";padding-bottom:0pt;page-break-after:avoid}h2{widows:2;padding-top:10pt;line-height:1.15;orphans:2;text-align:left;color:#000000;font-size:13pt;font-family:\"Trebuchet MS\";font-weight:bold;padding-bottom:0pt;page-break-after:avoid}h3{widows:2;padding-top:8pt;line-height:1.15;orphans:2;text-align:left;color:#666666;font-size:12pt;font-family:\"Trebuchet MS\";font-weight:bold;padding-bottom:0pt;page-break-after:avoid}h4{widows:2;padding-top:8pt;line-height:1.15;orphans:2;text-align:left;color:#666666;font-size:11pt;text-decoration:underline;font-family:\"Trebuchet MS\";padding-bottom:0pt;page-break-after:avoid}h5{widows:2;padding-top:8pt;line-height:1.15;orphans:2;text-align:left;color:#666666;font-size:11pt;font-family:\"Trebuchet MS\";padding-bottom:0pt;page-break-after:avoid}h6{widows:2;padding-top:8pt;line-height:1.15;orphans:2;text-align:left;color:#666666;font-style:italic;font-size:11pt;font-family:\"Trebuchet MS\";padding-bottom:0pt;page-break-after:avoid}</style></head><body class=\"c15 c16\">@CLASSDATA</body></html>";
	// replace @TITLE @DESCRIPTION @TABLES
	private static String classEntry = "<p class=\"c7\"><span class=\"c0\">@TITLE</span></p><p class=\"c7\"><span class=\"c6\">@DESCRIPTION</span></p><p>&nbsp;</p>@TABLES<p>&nbsp;</p><p>&nbsp;</p>";
	// replace @TITLE, @HEADER1, @HEADER2, @HEADER3, @ROWDATA
	private static String classTable = "<p><span class=\"c6\">@TITLE</span></p><a href=\"#\" name=\"16f89fa4a674e4a0d54d0109a92a8452653c7966\"></a><a href=\"#\" name=\"0\"></a><table cellpadding=\"0\" cellspacing=\"0\" class=\"c12\"><tbody><tr class=\"c10\"><td class=\"c8\" colspan=\"1\" rowspan=\"1\"><p class=\"c2\"><span class=\"c5\">@HEADER1</span></p></td><td class=\"c11\" colspan=\"1\" rowspan=\"1\"><p class=\"c2\"><span class=\"c5\">@HEADER2</span></p></td><td class=\"c14\" colspan=\"1\" rowspan=\"1\"><p class=\"c2\"><span class=\"c5\">@HEADER3</span></p></td></tr>@ROWDATA</tbody></table><p class=\"c7 c9\"><span class=\"c6\"></span></p><p class=\"c7 c9\"><span></span></p><p>&nbsp;</p>";
	// replace @ITEM1, @ITEM2, @ITEM3
	private static String row = "<tr class=\"c10\"><td class=\"c8 c15\" colspan=\"1\" rowspan=\"1\"><p class=\"c2\"><span class=\"c1\">@ITEM1</span></p></td><td class=\"c3\" colspan=\"1\" rowspan=\"1\"><p class=\"c2\"><span class=\"c1\">@ITEM2</span></p></td><td class=\"c13\" colspan=\"1\" rowspan=\"1\"><p class=\"c2\"><span class=\"c1\">@ITEM3</span></p></td></tr>";

	
	private static Comparator<Descriptor> comp = new Comparator<Descriptor>() {
		public int compare(Descriptor o1, Descriptor o2) {
			return o1.name.compareTo(o2.name);
		}
	};
	
	private static <T extends Descriptor> List<T> sort(Collection<T> objs) {
		List<T> sortedList = new ArrayList<T>();
		sortedList.addAll(objs);
		Collections.sort(sortedList, comp);
		return sortedList;
	}
	
	public static String newDoc(Collection<ClassObject> cl) {
		List<ClassObject> classes = sort(cl);
		
		String body = "";
		
		for (ClassObject c : classes) {
			
			String methods = "";
			if (c.methods.size() > 0)
				methods = generateTable("Methods", "Method Name", "Signature", "Description", c.methods,
					new HtmlContent.Processor<MethodObject>() {
						@Override public String get(MethodObject item) {
							return (item.signature == null) ? "" : item.signature;
						}
					});
			
			String associations = "";
			if (c.associations.size() > 0)
				associations = generateTable("Associations", "Association Name", "Type", "Description", c.associations,
					new HtmlContent.Processor<AssociationObject>() {
						@Override public String get(AssociationObject item) {
							return (item.type == null) ? "" : item.type;
						}
					});
			
			String properties = "";
			if (c.properties.size() > 0)
				properties = generateTable("Properties", "Property Name", "Type", "Description", c.properties,
					new HtmlContent.Processor<PropertyObject>() {
						@Override public String get(PropertyObject item) {
							return (item.type == null) ? "" : item.type;
						}
					});
			
			// replace @TITLE @DESCRIPTION @TABLES
			body += classEntry
					.replace((CharSequence) "@TITLE", escapeHtml4(c.name))
					.replace((CharSequence) "@DESCRIPTION", escapeHtml4(c.description))
					.replace((CharSequence) "@TABLES", methods + properties + associations);
		}
		
		// replace @CLASSDATA
		return document.replace((CharSequence) "@CLASSDATA", body);
		
	}
	
	private static interface Processor<T> {
		public String get(T item);
	}
	
	
	private static <T extends Descriptor> String generateTable(String title, String header1, String header2, String header3, 
			Collection<T> descriptors, HtmlContent.Processor<T> processor) {
		String rows = "";
		for (T d : sort(descriptors)) {
			rows += row
					.replace((CharSequence) "@ITEM1", escapeHtml4(d.name)) 
					.replace((CharSequence) "@ITEM2", escapeHtml4(processor.get(d)))
					.replace((CharSequence) "@ITEM3", escapeHtml4(d.description));
		}
		
		// replace @TITLE, @HEADER1, @HEADER2, @HEADER3, @ROWDATA
		return classTable
				.replace((CharSequence) "@TITLE", escapeHtml4(title)) 
				.replace((CharSequence) "@HEADER1", escapeHtml4(header1)) 
				.replace((CharSequence) "@HEADER2", escapeHtml4(header2)) 
				.replace((CharSequence) "@HEADER3", escapeHtml4(header3)) 
				.replace((CharSequence) "@ROWDATA", rows);
	}

}