package dbentities;

public class DBlinkageclasscstructure extends DBentity{
	public int id;
	public int cstructureid;
	public int classid;
	
	public DBlinkageclasscstructure(){}
	
	public DBlinkageclasscstructure(int cstructureid, int classid){
		this.classid = classid;
		this.cstructureid = cstructureid;
	}
}
