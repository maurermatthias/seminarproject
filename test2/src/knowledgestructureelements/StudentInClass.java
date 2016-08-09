package knowledgestructureelements;

public class StudentInClass {
	private Clazz clazz;
	public CompetenceState competecneState;
	
	public StudentInClass(int classId, int userId){
		this.clazz = new Clazz(classId);
		this.competecneState = new CompetenceState(userId, clazz);
	}
	
	public String getDiagnosticString(){
		String str = "Student in Class:\n";
		str += clazz.getDiagnosticString() + "\n" + competecneState.getDiagnosticString();
		return str;
	}
}
