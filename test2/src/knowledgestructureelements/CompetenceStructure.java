package knowledgestructureelements;

import java.util.ArrayList;
import java.util.List;

public class CompetenceStructure {
	public List<Competence> competences = new ArrayList<Competence>();
	
	public boolean addEdge(String fromName, String toName, double weight){
		Competence from = getCompetenceByName(fromName);
		Competence to = getCompetenceByName(toName);
		if(from == null || to == null)
			System.out.print("ERROR - competence not found!('"+fromName+"' OR '"+toName+"')");
		Edge edge = new Edge(from, to, weight);
		if(from.addSuccessor(edge)){
			if(to.addPrerequisite(edge)){
				return true;
			}
			else{
				from.removeSuccessor(edge);
				return false;
			}
		}
		else
			return false;
	}
	
	public boolean addCompetence(Competence competence){
		if(getCompetenceByName(competence.name)==null){
			competences.add(competence);
			return true;
		}
		return false;
	}
	
	public Competence getCompetenceByName(String name){
		for(Competence competence: competences)
			if(competence.name.equals(name))
				return competence;
		return null;
	}

	public String getDiagnosticString(){
		String str = "Print out competence structure:\n";
		for(Competence competence : competences)
			str += competence.getDiagnosticString();
		return str;
	}
	
	public List<Competence> getAllIndirectPrerequisites(Competence competence){
		if(competence.prerequisites.size()==0)
			return new ArrayList<Competence>();
		List<Competence> prerequisites = new ArrayList<Competence>();
		for(Edge edge : competence.prerequisites){
			if(!prerequisites.contains(edge.from)){
				prerequisites.add(edge.from);
				for(Competence prereq : getAllIndirectPrerequisites(edge.from)){
					if(!prerequisites.contains(prereq)){
						prerequisites.add(prereq);
					}
				}
			}
		}
		return prerequisites;
	}
	
	public int getTotalNumberOfPrerequisites(Competence competence){
		return getAllIndirectPrerequisites(competence).size();
	}
}
