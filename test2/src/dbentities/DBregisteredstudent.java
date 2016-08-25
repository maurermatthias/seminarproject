package dbentities;

import org.w3c.dom.Document;

import test2.DBConnector;

public class DBregisteredstudent extends DBentity{
	public int id;
	public int classid;
	public int studentid;
	
	public DBregisteredstudent(){}
	
	public DBregisteredstudent(int studentId, int classId){
		this.studentid = studentId;
		this.classid = classId;
	}
	
	public DBregisteredstudent(Document doc){
		if(doc.getElementsByTagName("classname").getLength()>0)
			this.classid = DBConnector.getClassIdByName(doc.getElementsByTagName("classname").item(0).getFirstChild().getNodeValue());
		if(doc.getElementsByTagName("studentname").getLength()>0)
			this.studentid = DBConnector.getUserId(doc.getElementsByTagName("studentname").item(0).getFirstChild().getNodeValue());
	}
}
