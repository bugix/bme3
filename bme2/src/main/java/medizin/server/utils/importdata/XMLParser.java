package medizin.server.utils.importdata;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class XMLParser {

	private final SAXBuilder builder;
	private final Document doc;
	private final Element root;
	private HashMap<String, Integer> fields;
	private List<Element> dataSet;
	
	public XMLParser(File xmlFile) throws JDOMException, IOException {
		builder = new SAXBuilder();
		doc = builder.build(xmlFile);
		root = doc.getRootElement();
		// read header fields
		parseHeader();
	}
	
	/**
	 * Parse <METADATA> in export-file an save it for latter usage
	 */
	@SuppressWarnings("unchecked")
	private void parseHeader() {
		Iterator<Element> i;
		Element e;
		
		fields = new HashMap<String, Integer>();
		
		// get header
		Element meta = (Element) root.getChildren().get(3); // matches "METADATA"
		List<Element> metaList = meta.getChildren();
		i = metaList.iterator();
		int c = 0;
		while(i.hasNext()) {
			e = i.next();
			String attName = e.getAttributeValue("NAME");
			fields.put(attName, c);
			
			c++;
		}
	}
	
	public HashMap<String, Integer> getHeader() {
		if(fields == null) {
			parseHeader();
		}
		return fields;
	}
	
	/**
	 * Get whole data set (RESULTSET in xml file)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Element> getData() {
		// get records
		Element resultset = (Element) root.getChildren().get(4); // matches "RESULTSET"
		dataSet = resultset.getChildren();
		return dataSet;
	}
	
	/**
	 * Get a field value
	 * scaffold is the same for each field (<col><data>[...]</data></col>)
	 * 
	 * @param r	List of <cols> in <row...>
	 * @param fieldName Name of the field to get value (see <METADATA>)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Element> getField(Element e, String fieldName) {
		List<Element> children = e.getChildren();
		
		return children.get(fields.get(fieldName)).getChildren();
	}
}
