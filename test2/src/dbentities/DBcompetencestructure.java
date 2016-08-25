package dbentities;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.w3c.dom.Document;

import test2.DBConnector;

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
		if(doc.getElementsByTagName("name").getLength()>0)
			this.name = doc.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
		if(doc.getElementsByTagName("description").getLength()>0)
			this.description = doc.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();
		if(doc.getElementsByTagName("visibility").getLength()>0)
			this.visibility =  (doc.getElementsByTagName("visibility").item(0).getFirstChild().getNodeValue().equals("ALL")) ? Visibility.ALL : Visibility.NOTALL;
	}
	
	public String toXMLWithStructure(){
		String xml ="<competencestructure>";
		xml+="<name>"+this.name+"</name>";
		xml+="<description>"+this.description+"</description>";
		List<DBentity> entities = DBConnector.getCompetenceWeightByCStructureId(DBConnector.getCstructureIdByName(this.name));
		if(!entities.isEmpty()){
			xml+="<competenceweights>";
			for(int i=0;i<entities.size();i++){
				DBcompetenceweight weight = (DBcompetenceweight) entities.get(i);
				xml+="<competenceweight>";
				xml+="<from>"+DBConnector.getCompetenceNameById(weight.fromcompetenceid)+"</from>";
				xml+="<to>"+DBConnector.getCompetenceNameById(weight.tocompetenceid)+"</to>";
				xml+="<weight>"+weight.weight+"</weight>";
				xml+="</competenceweight>";
			}
			xml+="</competenceweights>";
		}
		xml+="</competencestructure>";
		
		return xml;
	};
}
