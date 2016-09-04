package knowledgestructureelements;

import java.util.Random;

import dbentities.DBclass;
import test2.DBConnector;
import updateelements.CompetenceUpdater;
import updateelements.CompetenceUpdaterCoreCompetences;
import updateelements.CompetenceUpdaterSimplifiedUpdateRule;

public class StudentInClass {
	public Clazz clazz;
	public CompetenceState competenceState;
	
	public StudentInClass(int classId, int userId){
		String classname = ((DBclass)DBConnector.getClassById(classId)).name;
		this.clazz = DBConnector.getActiveClazzByName(classname);
		this.competenceState = new CompetenceState(userId, clazz, clazz.updater);
	}
	
	public String getDiagnosticString(){
		String str = "Student in Class:\n";
		str += clazz.getDiagnosticString() + "\n" + competenceState.getDiagnosticString();
		return str;
	}

	//@return null -> no further task useful
	public Task getNextTask(){
		return clazz.updater.getNextTask(competenceState, clazz);
	}
	
	public void updateCompetenceState(int taskId, Boolean success){
		Task task = clazz.taskCollection.getTaskById(taskId);
		clazz.updater.updateCompetenceState(clazz.competenceStructure, task, competenceState, success);
		competenceState.store();
	}
	
	public boolean isDataValid(){
		return clazz.isDataValid();
	}
	
}
