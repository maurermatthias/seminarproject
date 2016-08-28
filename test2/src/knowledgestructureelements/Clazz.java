package knowledgestructureelements;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dbentities.DBactiveclass;
import dbentities.DBclass;
import test2.DBConnector;

public class Clazz {
	public CompetenceStructure competenceStructure;
	public TaskCollection taskCollection;
	public int classId=0;
	public int cstructureId;
	private int validationCode = 0;
	
	///*
	//C-tor for loading clazz from classes
	public Clazz(int classId){
		this.classId = classId;
		this.cstructureId = DBConnector.getCstructureIdByClassId(classId);
		this.competenceStructure = DBConnector.getCompetenceStructure(cstructureId);
		this.taskCollection = DBConnector.getTaskCollectionByClassId(classId, competenceStructure);
	}
	//*/
	
	//C-tor for loading clazz from activeclasses
	public Clazz(String xml) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc = null;
		builder = factory.newDocumentBuilder();		
		StringBuilder xmlStringBuilder = new StringBuilder();
		xmlStringBuilder.append(xml);
		ByteArrayInputStream input =  new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
		doc = builder.parse(input);
		//doc.getElementsByTagName("competencestructure").item(0).getFirstChild().getNodeValue()
		NodeList compstructure = doc.getElementsByTagName("competences").item(0).getChildNodes();
		NodeList taskcol = doc.getElementsByTagName("taskcollection").item(0).getChildNodes();
		
		competenceStructure = new CompetenceStructure(compstructure);
		taskCollection= new TaskCollection(taskcol,competenceStructure);
		
	}
	
	public String getDiagnosticString(){
		String str = "***********************************************\nPrint Class:";
		str += this.competenceStructure.getDiagnosticString();
		if(this.taskCollection!= null)
			str += this.taskCollection.getDiagnosticString();
		return str+"***********************************************";
	}
	
	public String toXML(){
		String xml ="<class>";
		xml+="<competencestructure>"+this.competenceStructure.toXML()+"</competencestructure>";
		xml+="<taskcollection>"+this.taskCollection.toXML()+"</taskcollection>";
		xml+="</class>";
		return xml;
	}

	public boolean setActive(){
		if(classId==0 || !this.isDataValid())
			return false;
		DBactiveclass clazz = new DBactiveclass();
		clazz.data = this.toXML();
		DBclass clazz2 = DBConnector.getClassById(classId);
		clazz.creator= clazz2.creator;
		clazz.description=clazz2.description;
		clazz.name=clazz2.name;
		clazz.visibility = clazz2.visibility;
		if(DBConnector.getActiveClassIdByName(clazz.name)==0){
			return DBConnector.addActiveClass(clazz);
		}else{
			return DBConnector.updateActiveClass(clazz);
		}
	}

	//1, if data is fine
	//%2==0, if task contains 
	//%3==0, if competence weight sum for one competence is > 1
	//%5==0, if competence has neither prerequisites nor successors
	//%7==0, if taskweights do not sum up to one
	public boolean isDataValid(){
		if(validationCode == 0)
			validationCode =  competenceStructure.isDataValid() * taskCollection.isDataValid(competenceStructure.competences);
		return validationCode==1 ? true : false;
	}
	
	public String getErrorXML(){
		String xml = "<structurvalidation>";
		if(validationCode ==0)
			return null;
		if(validationCode ==1)
			xml+="<status>valid</status>";
		else
			xml+="<status>unvalid</status>";
		if(validationCode%2==0){
			xml+="<errorcode>There is an competence assigned to a task, which doesn't exist in the competence structure.<errorcode>";
		}
		if(validationCode%7==0){
			xml+="<errorcode>Competence-Weights assigned to task do not sum up to 1.<errorcode>";
		}
		if(validationCode%3==0){
			xml+="<errorcode>There is an competence for which competence weights sum up > 1.<errorcode>";
		}
		if(validationCode%5==0){
			xml+="<errorcode>There is an isolated competence.<errorcode>";
		}
		xml +="</structurvalidation>";
		return (xml);
	}

}
