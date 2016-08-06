package dbentities;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

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
	
	
}