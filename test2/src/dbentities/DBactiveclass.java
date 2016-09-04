package dbentities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import updateelements.UpdateProcedure;

@Root(name="class")
public class DBactiveclass extends DBentity{
	public int creator;
	public String data;
	public String date;
	
	@Element(name="updateprocedure")
	public UpdateProcedure updateProcedure;
	@Element(name="visibility")
	public Visibility visibility;
	@Element(name="name")
	public String name;
	@Element(name="description")
	public String description;
	@Element(name="id")
	public int classid;
	
	@SuppressWarnings("deprecation")
	public DBactiveclass(){
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
		try {
			this.date = dateFormatLocal.parse( dateFormatGmt.format(new Date()) ).toGMTString();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	public DBactiveclass(String name, String description, Visibility visibility, int creatorId){
		this.name = name;
		this.description= description;
		this.visibility = visibility;
		this.creator = creatorId;
	}
	
	public DBactiveclass(Document doc){
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
	*/
}
