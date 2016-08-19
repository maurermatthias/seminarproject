package dbentities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.w3c.dom.Document;

@Root(name="competencestructure")
public class DBcompetencestructure extends DBentity{
	public int cstructureid;
	public int creator;
	public Visibility visibility;
	
	@Element(name="name")
	public String name;
	@Element(name="description")
	public String description;
	
	public DBcompetencestructure(){}

	public DBcompetencestructure(String name, String description, int creator, Visibility visibility){
		this.name = name;
		this.description = description;
		this.creator = creator;
		this.visibility = visibility;
	}
	

	public DBcompetencestructure(Document doc){
		this.name = doc.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
		this.description = doc.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();
		this.visibility =  (doc.getElementsByTagName("visibility").item(0).getFirstChild().getNodeValue().equals("ALL")) ? Visibility.ALL : Visibility.NOTALL;
	}
}
