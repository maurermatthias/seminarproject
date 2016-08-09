package demo;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.stream.Node;
import org.xml.sax.InputSource;

import dbentities.DBtask;
import dbentities.DBuser;
import knowledgestructureelements.Clazz;
import knowledgestructureelements.CompetenceStructure;
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
        //*/
        
        ///*
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
