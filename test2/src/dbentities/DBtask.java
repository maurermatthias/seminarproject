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
		this.name = doc.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
		this.description = doc.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();
		this.visibility =  (doc.getElementsByTagName("visibility").item(0).getFirstChild().getNodeValue().equals("ALL")) ? Visibility.ALL : Visibility.NOTALL;
		this.answer = doc.getElementsByTagName("answer").item(0).getFirstChild().getNodeValue();
		this.text = doc.getElementsByTagName("text").item(0).getFirstChild().getNodeValue();
	}
	
	
}
