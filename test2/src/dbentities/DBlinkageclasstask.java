package dbentities;

public class DBlinkageclasstask extends DBentity{
	public int id;
	public int taskid;
	public int classid;
	
	public DBlinkageclasstask(){}
	
	public DBlinkageclasstask(int taskid, int classid){
		this.taskid = taskid;
		this.classid = classid;
	}
	
}
