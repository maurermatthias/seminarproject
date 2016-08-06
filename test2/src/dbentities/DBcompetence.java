package dbentities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="competence")
public class DBcompetence extends DBentity{
	public int competenceid;
	public int creator;
	public Visibility visibility;
	
	@Element(name="name")
	public String name;
	@Element(name="describtion")
	public String description;
	
	public DBcompetence(){}
	
	public DBcompetence(String name, String description, int creator, Visibility visibility){
		this.name = name;
		this.description = description;
		this.creator = creator;
		this.visibility = visibility;
	}
}