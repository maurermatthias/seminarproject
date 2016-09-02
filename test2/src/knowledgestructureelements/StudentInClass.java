package knowledgestructureelements;

import java.util.Random;

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

	//@return null -> no further task useful
	public Task getNextTask(){
		if(clazz.taskCollection.tasks.size()>0){
			Random rand = new Random();
			int randomNum = rand.nextInt(clazz.taskCollection.tasks.size());
			Task task = clazz.taskCollection.tasks.get(randomNum);
			return task;
		}else{
			return null;
		}
	}
	
	public void updateCompetenceState(int taskId, Boolean success){
		Task task = clazz.taskCollection.getTaskById(taskId);
		clazz.competenceStructure.updateCompetenceState(task, competenceState, success);
	}
	
	public boolean isDataValid(){
		return clazz.isDataValid();
	}
	
}
