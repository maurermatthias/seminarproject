package knowledgestructureelements;

public class StudentInClass {
	private Clazz clazz;
	public CompetenceState competenceState;
	
	public StudentInClass(int classId, int userId){
		this.clazz = new Clazz(classId);
		this.competenceState = new CompetenceState(userId, clazz);
	}
	
	public String getDiagnosticString(){
		String str = "Student in Class:\n";
		str += clazz.getDiagnosticString() + "\n" + competenceState.getDiagnosticString();
		return str;
	}

	public Task getNextTask(){
		return null;
	}
	
	public void updateCompetenceState(int taskId, Boolean success){
		competenceState = clazz.competenceStructure.updateCompetenceState(competenceState, success);
	}
}
