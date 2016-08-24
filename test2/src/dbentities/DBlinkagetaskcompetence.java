package dbentities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.w3c.dom.Document;

import test2.DBConnector;

@Root(name="linkagetaskcompetence")
public class DBlinkagetaskcompetence extends DBentity{
	public int id;
	@Element(name="taskid")
	public int taskid;
	@Element(name="competenceid")
	public int competenceid;
	@Element(name="weight")
	public double weight;
	
	
	public DBlinkagetaskcompetence(){}
	
	public DBlinkagetaskcompetence(int taskid, int competenceid, double weight){
		this.taskid = taskid;
		this.competenceid = competenceid;
		this.weight = weight;
	}
	
	public DBlinkagetaskcompetence(Document doc){
		if(doc.getElementsByTagName("competencename").getLength()>0)
			this.competenceid =  DBConnector.getCompetenceIdByName(doc.getElementsByTagName("competencename").item(0).getFirstChild().getNodeValue());
		if(doc.getElementsByTagName("taskname").getLength()>0)
			this.taskid = DBConnector.getTaskIdByName(doc.getElementsByTagName("taskname").item(0).getFirstChild().getNodeValue());
		if(doc.getElementsByTagName("weight").getLength()>0)
			this.weight =  Double.parseDouble(doc.getElementsByTagName("weight").item(0).getFirstChild().getNodeValue());
	}
}
