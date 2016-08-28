package demo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.stream.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import dbentities.DBtask;
import dbentities.DBuser;
import knowledgestructureelements.Clazz;
import knowledgestructureelements.CompetenceStructure;
import knowledgestructureelements.StudentInClass;
import test2.DBConnector;
import test2.XMLCreator;

public class Demo{  
  
    public void demo() {  
  
        System.out.println("ewferz"); 
  
    }  
    
    public static void main(String[] args) {
        // Prints "Hello, World" to the terminal window.
        System.out.println("START");
        
        DBConnector.resetDB();
        
        DBConnector.createTestData();
        
        /*
        CompetenceStructure cstruct = DBConnector.getCompetenceStructure(DBConnector.getCstructureIdByName("CS1"));
        System.out.println(cstruct.getDiagnosticString());
        */
        
        
        ///*
        StudentInClass sic = new StudentInClass(DBConnector.getClassIdByName("class1"),DBConnector.getUserId("student1"));
        sic.isDataValid();
        System.out.println(XMLCreator.prettyFormat(sic.clazz.toXML()));
        
        Clazz cs;
        try {
        	String xml1=XMLCreator.prettyFormat(sic.clazz.toXML());
			cs = new Clazz(sic.clazz.toXML());
			System.out.println(cs.getDiagnosticString());
			String xml2= XMLCreator.prettyFormat(cs.toXML());
			if(xml1.equals(xml2))
				System.out.println("WORKED");
			else{
				System.out.println("FAIL");
			}
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERROR");
		}
        
        
        //*/
        
        
        /*
        Clazz clazz = new Clazz(DBConnector.getClassIdByName("class1"));
        System.out.println(clazz.getDiagnosticString());
        //*/
        
        /*
        XMLCreator xmlcreator = new XMLCreator("n","pwd");
        String xml = XMLCreator.prettyFormat(xmlcreator.getLoginXML());
        System.out.println(xml);
        */
        
        /*
        DBuser user = new DBuser();
        user.creator=23;
        user.name="testname";
        user.password="pwd";
        user.usergroup=1;
        
		DBConnector.addNewUser(user);
		*/
        

        System.out.println("END");
    }
}
