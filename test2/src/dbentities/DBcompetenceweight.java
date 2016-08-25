package dbentities;

import org.w3c.dom.Document;

import test2.DBConnector;

public class DBcompetenceweight extends DBentity{
	public int id;
	public int fromcompetenceid;
	public int tocompetenceid;
	public int cstructureid;
	public double weight;
	
	public DBcompetenceweight(){}
	
	public DBcompetenceweight(int cstructureId,int fromId, int toId, double weight){
		this.fromcompetenceid = fromId;
		this.tocompetenceid = toId;
		this.cstructureid = cstructureId;
		this.weight = weight;
	}
	
	public DBcompetenceweight(Document doc){
		if(doc.getElementsByTagName("fromname").getLength()>0)
			this.fromcompetenceid = DBConnector.getCompetenceIdByName(doc.getElementsByTagName("fromname").item(0).getFirstChild().getNodeValue());
		if(doc.getElementsByTagName("toname").getLength()>0)
			this.tocompetenceid = DBConnector.getCompetenceIdByName(doc.getElementsByTagName("toname").item(0).getFirstChild().getNodeValue());
		if(doc.getElementsByTagName("weight").getLength()>0)
			this.weight = Double.parseDouble(doc.getElementsByTagName("weight").item(0).getFirstChild().getNodeValue());
		if(doc.getElementsByTagName("cstructurename").getLength()>0)
			this.cstructureid = DBConnector.getCstructureIdByName(doc.getElementsByTagName("cstructurename").item(0).getFirstChild().getNodeValue());
	}
}
