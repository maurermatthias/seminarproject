package knowledgestructureelements;

import dbentities.DBclass;
import test2.DBConnector;

public class StudentInClass {
	public Clazz clazz;
	public CompetenceState competenceState;
	
	public StudentInClass(int classId, int userId){
		String classname = ((DBclass)DBConnector.getClassById(classId)).name;
		this.clazz = DBConnector.getActiveClazzByName(classname);
		this.competenceState = new CompetenceState(userId, clazz);
	}
	
	public String getDiagnosticString(){
		String str = "Student in Class:\n";
		str += clazz.getDiagnosticString() + "\n" + competenceState.getDiagnosticString();
		return str;
	}

	public Task getNextTask(){
		if(clazz.taskCollection.tasks.size()>0){
			Task task = clazz.taskCollection.tasks.get(0);
			return task;
		}else{
			return null;
		}
	}
	
	public void updateCompetenceState(int taskId, Boolean success){
		competenceState = clazz.competenceStructure.updateCompetenceState(competenceState, success);
	}
	
	public boolean isDataValid(){
		return clazz.isDataValid();
	}
}
