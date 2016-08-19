package dbentities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.w3c.dom.Document;

@Root(name="class")
public class DBclass extends DBentity{
	public int creator;
	
	@Element(name="visibility")
	public Visibility visibility;
	@Element(name="name")
	public String name;
	@Element(name="description")
	public String description;
	@Element(name="id")
	public int classid;
	
	public DBclass(){}
	
	public DBclass(String name, String description, Visibility visibility, int creatorId){
		this.name = name;
		this.description= description;
		this.visibility = visibility;
		this.creator = creatorId;
	}
	
	public DBclass(Document doc){
		if(doc.getElementsByTagName("name").getLength()>0)
			this.name = doc.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
		if(doc.getElementsByTagName("description").getLength()>0)
			this.description = doc.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();
		if(doc.getElementsByTagName("visibility").getLength()>0)
			this.visibility =  (doc.getElementsByTagName("visibility").item(0).getFirstChild().getNodeValue().equals("ALL")) ? Visibility.ALL : Visibility.NOTALL;
	}
	
}
