package dbentities;

import org.w3c.dom.Document;

import test2.DBConnector;

public class DBlinkageclasstask extends DBentity{
	public int id;
	public int taskid;
	public int classid;
	
	public DBlinkageclasstask(){}
	
	public DBlinkageclasstask(int taskid, int classid){
		this.taskid = taskid;
		this.classid = classid;
	}
	
	public DBlinkageclasstask(Document doc){
		if(doc.getElementsByTagName("classname").getLength()>0)
			this.classid = DBConnector.getClassIdByName(doc.getElementsByTagName("classname").item(0).getFirstChild().getNodeValue());
		if(doc.getElementsByTagName("taskname").getLength()>0)
			this.taskid = DBConnector.getTaskIdByName(doc.getElementsByTagName("taskname").item(0).getFirstChild().getNodeValue());
	}
	
}
