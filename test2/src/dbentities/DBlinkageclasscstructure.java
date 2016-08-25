package dbentities;

import org.w3c.dom.Document;

import test2.DBConnector;

public class DBlinkageclasscstructure extends DBentity{
	public int id;
	public int cstructureid;
	public int classid;
	
	public DBlinkageclasscstructure(){}
	
	public DBlinkageclasscstructure(int cstructureid, int classid){
		this.classid = classid;
		this.cstructureid = cstructureid;
	}
	
	public DBlinkageclasscstructure(Document doc){
		if(doc.getElementsByTagName("cstructurename").getLength()>0)
			this.cstructureid = DBConnector.getCstructureIdByName(doc.getElementsByTagName("cstructurename").item(0).getFirstChild().getNodeValue());
		if(doc.getElementsByTagName("classname").getLength()>0)
			this.classid = DBConnector.getClassIdByName(doc.getElementsByTagName("classname").item(0).getFirstChild().getNodeValue());
	}
}
