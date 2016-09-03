package updateelements;

import java.util.HashMap;
import java.util.Map;

import org.jblas.DoubleMatrix;

import knowledgestructureelements.Competence;
import knowledgestructureelements.CompetenceState;
import knowledgestructureelements.CompetenceStructure;
import knowledgestructureelements.Task;

public class CompetenceUpdaterSimplifiedUpdateRule extends CompetenceUpdater{

	@Override
	public void updateCompetenceState(CompetenceStructure competenceStructure, Task task,
			CompetenceState currentCompetenecstate, boolean success) {
		if(competenceStructure.containsCircles())
			return;
		
	}

	@Override
	public void setInitialCompetenceState(CompetenceStructure competenceStructure, CompetenceState competenceState) {
		if(competenceStructure.containsCircles())
			return;
		Map<Competence,Integer> up = new HashMap<Competence,Integer>();
		Map<Competence,Integer> down = new HashMap<Competence,Integer>();
		for(Competence competence : competenceStructure.competences){
			up.put(competence, 0);
			down.put(competence, 0);
		}
		for(Competence c1 : competenceStructure.competences){
			for(Competence c2 : competenceStructure.competences){
				if(c1.isSmallerOrEqual(c2)){
					up.put(c1, up.get(c1)+1);
					down.put(c2, down.get(c2)+1);
				}
			}
		}
		Double value;
		for(Competence competence : competenceStructure.competences){
			value = ((double)(competenceStructure.competences.size()+up.get(competence)-down.get(competence)+1))/
					((double)(2*competenceStructure.competences.size()+2));
			competenceState.competencevalues.put(competence, value);
			competenceState.denominatorvalues.put(competence, -1.0);
			competenceState.numeratorvalues.put(competence, -1.0);
			competenceState.nvalues.put(competence, -1);
		}
	}

	@Override
	public int isDataValid() {
		// TODO Auto-generated method stub
		return 0;
	}


	
}
