package dbentities;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.w3c.dom.Document;

@Root(name="task")
public class DBtask extends DBentity{
	
	public int taskid;
	public int creator;
	public Visibility visibility;
	
	@Element(name="name")
	public String name;
	@Element(name="description")
	public String description;
	@Element(name="text")
	public String text;
	@Element(name="answer")
	public String answer;
	
	public DBtask(){}
			
	public DBtask(String name, String description, String text, String answer, int creator, Visibility visibility){
		this.name = name;
		this.description = description;
		this.text = text;
		this.answer = answer;
		this.creator = creator;
		this.visibility = visibility;
	}
	

	public DBtask(Document doc){
		if(doc.getElementsByTagName("name").getLength()>0)
			this.name = doc.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
		if(doc.getElementsByTagName("description").getLength()>0)
			this.description = doc.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();
		if(doc.getElementsByTagName("visibility").getLength()>0)
			this.visibility =  (doc.getElementsByTagName("visibility").item(0).getFirstChild().getNodeValue().equals("ALL")) ? Visibility.ALL : Visibility.NOTALL;
		if(doc.getElementsByTagName("answer").getLength()>0)
			this.answer = doc.getElementsByTagName("answer").item(0).getFirstChild().getNodeValue();
		if(doc.getElementsByTagName("text").getLength()>0)
			this.text = doc.getElementsByTagName("text").item(0).getFirstChild().getNodeValue();
	}
	
	
}
