package updateelements;

import knowledgestructureelements.Clazz;
import knowledgestructureelements.CompetenceState;
import knowledgestructureelements.CompetenceStructure;
import knowledgestructureelements.Task;

public abstract class CompetenceUpdater {
	public boolean isSUR;
	public boolean isCCU;
	
	public abstract void updateCompetenceState(CompetenceStructure competenceStructure,Task task, CompetenceState currentCompetenecstate, boolean success);
	
	public abstract void setInitialCompetenceState(CompetenceStructure competenceStructure, CompetenceState competenceState);
	
	public abstract int isDataValid(Clazz clazz);
	
	public abstract Task getNextTask(CompetenceState competenceState,Clazz clazz);
}
