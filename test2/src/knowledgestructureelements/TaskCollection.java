package knowledgestructureelements;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class TaskCollection {
	public List<Task> tasks = new ArrayList<Task>();
	
	public TaskCollection(){}
	
	public TaskCollection(NodeList nodes, CompetenceStructure competenceStructure){
		for(int i=0;i<nodes.getLength();i++){
			addTask(new Task(nodes.item(i),competenceStructure));
		}
	}
	
	public void addTask(Task task){
		tasks.add(task);
	}
	
	public String getDiagnosticString(){
		String str = "Print out TaskCollection:\n";
		for(Task task : tasks)
			str += task.getDiagnosticString();
		return str;
	}
	
	public int isDataValid(List<Competence> competences){
		int retVal=1;
		
		if(tasks.size()==0)
			return 13;
		
		for(Task task : tasks){
			Double sum =0.0;
			for(Competence competence : task.weights.keySet()){
				if(!competences.contains(competence)){
					retVal = retVal * 2;
				}
				sum += task.weights.get(competence).doubleValue();
			}
			if(sum !=1)
				retVal = retVal *7;
		}
		return retVal;
	}

	public String toXML(){
		String xml="";
		for(Task task : tasks){
			xml+="<task>"+task.toXMLLong()+"</task>";
		}
		return xml;
	}
}
