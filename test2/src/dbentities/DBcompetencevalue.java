package dbentities;

public class DBcompetencevalue extends DBentity {

	public int id;
	public int studentid;
	public int classid;
	public int competenceid;
	public double value; 
	
	public DBcompetencevalue() {}
	
	public DBcompetencevalue(int studentid, int classid, int competenceid, double value){
		this.classid = classid;
		this.studentid = studentid;
		this.competenceid = competenceid;
		this.value = value;
	}
	
}
