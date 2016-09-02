package knowledgestructureelements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbentities.DBcompetencevalue;
import test2.DBConnector;

public class CompetenceState {
	HashMap<Competence, Double> map = new HashMap<Competence, Double>();
	public int cstructureId;
	public int studentId;
	public int classId;
	public CompetenceStructure compStr;
	
	public CompetenceState(int  userId, int classId){
		this.studentId = userId;
		this.classId = classId;
		this.cstructureId = DBConnector.getCstructureIdByClassId(classId);
		compStr = DBConnector.getCompetenceStructure(cstructureId);
		loadCompetenceState();
	}
	
	public CompetenceState(int  userId, Clazz clazz){
		this.studentId = userId;
		this.classId = clazz.classId;
		this.cstructureId = DBConnector.getCstructureIdByClassId(classId);
		compStr = clazz.competenceStructure;
		loadCompetenceState();
	}
	
	private void loadCompetenceState(){
		Boolean initialiseCompetenceState = false;
		for(Competence competence : compStr.competences){
			Double value = DBConnector.getCompetenceValue(studentId, classId, DBConnector.getCompetenceIdByName(competence.name));
			if(value == -1){
				initialiseCompetenceState = true;
				break;
			}
			map.put(competence,value);
		}
		if(initialiseCompetenceState)
			setInitialCompetenceState();
			
	}
	
	private void setInitialCompetenceState(){
		//create initial competence state -> watch out when having circles!
		for(Competence competence : compStr.competences){
			Double value = 0.0;
			map.put(competence,value);
			DBConnector.addNewCompetenceValue(new DBcompetencevalue(studentId, classId, DBConnector.getCompetenceIdByName(competence.name), value,0,0,0));
		}
	}
	
	public void store(){
		for(Competence competence : compStr.competences){
			Double value = map.get(competence);
			DBConnector.updateCompetenceValue(new DBcompetencevalue(studentId, classId, DBConnector.getCompetenceIdByName(competence.name),value,0,0,0));
		}
	}
	
	public String toXML(){
		String xml = "<competencestate>";
		for (Map.Entry<Competence,Double> entry : map.entrySet()) {
		    xml+= "<competencevalue>";
		    xml += "<competence>"+entry.getKey().name+"</competence>";
		    xml += "<value>"+entry.getValue().doubleValue()+"</value>";
		    xml+= "</competencevalue>";
		}
		xml += "</competencestate>";
		return xml;
	}
	
	public String getDiagnosticString(){
		String str = "Competence state:\n";
		for (Map.Entry<Competence,Double> entry : map.entrySet()) {
		    str+="    -"+entry.getKey().name +"("+entry.getValue().doubleValue()+")\n";
		}
		return str;
	}
}
