package dbentities;

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
	
}
