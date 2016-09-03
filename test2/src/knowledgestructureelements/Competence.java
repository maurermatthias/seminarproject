package knowledgestructureelements;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class Competence {
	
	public String name;
	public List<Edge> prerequisites = new ArrayList<Edge>();
	public List<Edge> successors =  new ArrayList<Edge>();
		
	public Competence(){}
	
	public Competence(String name){
		this.name = name;
	}
	
	public Competence(Node comp){
		name = comp.getFirstChild().getFirstChild().getNodeValue();
	}
	
	public Competence getPrerequisiteCompetence(String name){
		for(Edge edge:prerequisites)
			if(edge.from.name.equals(name))
				return edge.from;
		return null;
	}
	
	public Competence getSuccessorCompetence(String name){
		for(Edge edge:successors)
			if(edge.to.name.equals(name))
				return edge.to;
		return null;
	}
	
	public Edge getSuccessorEdgeByCompetenceName(String name){
		for(Edge edge:successors)
			if(edge.to.name.equals(name))
				return edge;
		return null;
	}
	
	public boolean addPrerequisite(Edge edge){
		if(getPrerequisiteCompetence(edge.from.name) == null){
			prerequisites.add(edge);
			return true;
		}
		return false;
	}
	
	public boolean addSuccessor(Edge edge){
		if(getSuccessorCompetence(edge.to.name) == null){
			successors.add(edge);
			return true;
		}
		return false;
	}

	public boolean removeSuccessor(Edge edge){
		Competence com = getSuccessorCompetence(edge.to.name);
		if(com == null){
			return false;
		}
		return successors.remove(com);
	}

	public boolean isSmallerOrEqual(Competence competence){
		List<Competence> indirectSuccessors = new ArrayList<Competence>();
		for(Edge edge : successors)
			indirectSuccessors.add(edge.to);
		Competence com;
		while(indirectSuccessors.size()>0){
			com = indirectSuccessors.remove(0);
			if(com.name.equals(competence.name))
				return true;
			else{
				for(Edge edge : com.successors)
					indirectSuccessors.add(edge.to);
			}
		}
		return false;
	}
	
	public String getDiagnosticString(){
		String str = "Competence '"+name+"':\n";
		str+="Prerequisites:\n";
		for(Edge edge : prerequisites)
			str+="-"+edge.from.name +"("+edge.weight+")\n";
		str+="Successors:\n";
		for(Edge edge : successors)
			str+="-"+edge.to.name +"("+edge.weight+")\n";
		return str;
	}
	
	public String toXML(){
		String xml = "<competence>";
		xml+="<name>"+name+"</name>";
		xml+="<prerequisites>";
		for(Edge edge : prerequisites){
			xml+="<prerequisite>";
			xml+="<weight>"+edge.weight+"</weight>";
			xml+="<prerequisitescompetence>"+edge.from.name+"</prerequisitescompetence>";
			xml+="</prerequisite>";
		}
		xml += "</prerequisites>";
		xml+="</competence>";
		return xml;
	}
}
