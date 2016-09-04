package dbentities;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.w3c.dom.Document;

import knowledgestructureelements.Clazz;
import test2.DBConnector;
import updateelements.UpdateProcedure;

@Root(name="class")
public class DBclass extends DBentity{
	public int creator;
	

	@Element(name="updateprocedure")
	public UpdateProcedure updateProcedure = UpdateProcedure.CCU;
	@Element(name="visibility")
	public Visibility visibility;
	@Element(name="name")
	public String name;
	@Element(name="description")
	public String description;
	@Element(name="id")
	public int classid;
	
	public DBclass(){}
	
	public DBclass(String name, String description, Visibility visibility, int creatorId, UpdateProcedure updateProcedure){
		this.name = name;
		this.description= description;
		this.visibility = visibility;
		this.creator = creatorId;
		this.updateProcedure = updateProcedure;
	}
	
	public DBclass(Document doc){
		if(doc.getElementsByTagName("name").getLength()>0)
			this.name = doc.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
		if(doc.getElementsByTagName("description").getLength()>0)
			this.description = doc.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();
		if(doc.getElementsByTagName("visibility").getLength()>0)
			this.visibility =  (doc.getElementsByTagName("visibility").item(0).getFirstChild().getNodeValue().equals("ALL")) ? Visibility.ALL : Visibility.NOTALL;
		if(doc.getElementsByTagName("updateprocedure").getLength()>0)
			this.updateProcedure =  (doc.getElementsByTagName("updateprocedure").item(0).getFirstChild().getNodeValue().equals("SUR")) ? UpdateProcedure.SUR : UpdateProcedure.CCU;
	}
	
	public String toXMLWithCstructure(){
		String xml ="<class>";
		xml+="<visibility>"+this.visibility+"</visibility>";
		xml+="<name>"+this.name+"</name>";
		xml+="<description>"+this.description+"</description>";
		xml+="<id>"+this.classid+"</id>";
		xml+="<updateprocedure>"+this.updateProcedure+"</updateprocedure>";
		if(DBConnector.getActiveClassIdByName(this.name)==0){
			xml+="<active>false</active>";
		}
		else{
			xml+="<active>true</active>";
			xml+="<date>"+DBConnector.getActiveClassByName(this.name).date+"</date>";
		}
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
