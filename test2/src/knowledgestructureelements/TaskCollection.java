package knowledgestructureelements;

import java.util.ArrayList;
import java.util.List;

public class TaskCollection {
	public List<Task> tasks = new ArrayList<Task>();
	
	public void addTask(Task task){
		tasks.add(task);
	}
	
	public String getDiagnosticString(){
		String str = "Print out TaskCollection:\n";
		for(Task task : tasks)
			str += task.getDiagnosticString();
		return str;
	}
}
