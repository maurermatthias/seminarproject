package updateelements;

import java.util.HashMap;
import java.util.Map;

import org.jblas.DoubleMatrix;

import knowledgestructureelements.Competence;
import knowledgestructureelements.CompetenceState;
import knowledgestructureelements.CompetenceStructure;
import knowledgestructureelements.Task;

public class CompetenceUpdaterSimplifiedUpdateRule extends CompetenceUpdater{
	public double xi0=1.5;
	public double xi1=1.5;
	public double epsilon = 0.0000001;

	@Override
	public void updateCompetenceState(CompetenceStructure competenceStructure, Task task,
			CompetenceState currentCompetenecstate, boolean success) {
		if(competenceStructure.containsCircles())
			return;
		
		//create structure to sum up calculated competence states
		Map<Competence,Double> values = new HashMap<Competence,Double>(); 
		for(Competence competence : competenceStructure.competences){
			values.put(competence, 0.0);
		}
		
		//update each competence for its own
		for(Competence competence : task.weights.keySet()){
			Map<Competence,Double> updateValues = updateOneCompetence(competenceStructure,currentCompetenecstate,competence,success);
			for(Competence com : competenceStructure.competences){
				values.put(com, values.get(com)+updateValues.get(com));
			}
		}
		
		//calculate the mean and store in the competence state
		for(Competence com : competenceStructure.competences){
			currentCompetenecstate.competencevalues.put(com, values.get(com)/((double) competenceStructure.competences.size()));
		}
	}
	
	private Map<Competence,Double> updateOneCompetence(CompetenceStructure competenceStructure,CompetenceState cs,
			Competence competence,boolean success){
		
		Map<Competence,Double> values = new HashMap<Competence,Double>();
		
		Double N = success ? xi0*cs.getValueByName(competence.name)+(1.0-cs.getValueByName(competence.name)) : 
			cs.getValueByName(competence.name)+xi1*(1.0-cs.getValueByName(competence.name));
		
		for(Competence com : competenceStructure.competences){
			if(competence.isSmallerOrEqual(com) && !competence.name.equals(com.name)){
				if(success){
					values.put(com, (xi0*cs.getValueByName(com.name))/N);
				}else{
					values.put(com, (cs.getValueByName(com.name))/N);
				}
			}else if(com.isSmallerOrEqual(competence)){
				if(success){
					values.put(com, (xi0*cs.getValueByName(competence.name)+(cs.getValueByName(com.name)-cs.getValueByName(competence.name)))/N);
				}else{
					values.put(com, (cs.getValueByName(competence.name)+xi1*(cs.getValueByName(com.name)-cs.getValueByName(competence.name)))/N);
				}
			}else{
				values.put(com, cs.getValueByName(com.name));
			}
		}
		
		//consistency check
		boolean changes = true;
		while(changes){
			for(Competence c1 : competenceStructure.competences){
				for(Competence c2 : competenceStructure.competences){
					if(c1.name.equals(c2.name))
						break;
					if(c1.isSmallerOrEqual(c2)  && values.get(c1) <= values.get(c2)){
						if(success){
							values.put(c1,Math.min(1.0-epsilon, values.get(c2)-epsilon));
						}else{
							values.put(c2,Math.max(epsilon, values.get(c1)-epsilon));
						}
						changes=true;
					}
				}
			}
		}
		
		return values;
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
