package knowledgestructureelements;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dbentities.DBentity;
import dbentities.DBlinkagetaskcompetence;
import dbentities.DBtask;
import test2.DBConnector;

public class Task {

	public Map<Competence,Double> weights;
	public String question;
	public String answer;
	public int taskid;
	
	public Task(int taskid, CompetenceStructure competenceStructure){
		this.taskid = taskid;
		DBtask dbtask = DBConnector.getTaskById(taskid);
		question = dbtask.text;
		answer = dbtask.answer;
		
		weights = new HashMap<Competence,Double>();
		List<DBentity> entities = DBConnector.getCompetenceLinksToTaskById(taskid);
		for(DBentity entity : entities){
			DBlinkagetaskcompetence link = ((DBlinkagetaskcompetence) entity);
			weights.put(competenceStructure.getCompetenceByName(DBConnector.getCompetenceNameById(link.competenceid)), link.weight);
		}
	}
	
	public String getDiagnosticString(){
		String str = "";
		str += "-> "+taskid+","+question+":"+answer;
		for (Map.Entry<Competence,Double> entry : weights.entrySet()) {
		    str+="\n    -"+entry.getKey().name +"("+entry.getValue().doubleValue()+")\n";
		}
		return str;
	}
}
