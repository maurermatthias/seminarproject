package dbentities;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.w3c.dom.Document;

import test2.DBConnector;

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
	
	public String toXMLWithCstructure(){
		String xml ="<class>";
		xml+="<visibility>"+this.visibility+"</visibility>";
		xml+="<name>"+this.name+"</name>";
		xml+="<description>"+this.description+"</description>";
		xml+="<id>"+this.classid+"</id>";
		if(DBConnector.getActiveClassIdByName(this.name)==0)
			xml+="<active>false</active>";
		else
			xml+="<active>true</active>";
		DBcompetencestructure cstruct =  DBConnector.getCStructureById(DBConnector.getCstructureIdByClassId(this.classid));
		if(cstruct != null)
			xml +="<competencestructure>"+cstruct.name+"</competencestructure>";
		this.classid=DBConnector.getClassIdByName(this.name);
		List<DBentity> entities = DBConnector.getClassTaskLinkageByClassId(this.classid);
		if(!entities.isEmpty()){
			xml+="<tasks>";
			for(int i=0;i<entities.size();i++){
				xml+="<task>"+DBConnector.getTaskById(((DBlinkageclasstask)entities.get(i)).taskid).name+"</task>";
			}
			xml+="</tasks>";
		}
		xml+="</class>";
		
		return xml;
	}
}
