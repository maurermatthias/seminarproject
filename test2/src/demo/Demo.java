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

import dbentities.DBactiveclass;
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
        
        ///*
        DBConnector.resetDB();
        DBConnector.createTestData();
        //*/
        
        StudentInClass sic = new StudentInClass(DBConnector.getClassIdByName("class1"),DBConnector.getUserId("student1"));
        CompetenceStructure cs =  sic.clazz.competenceStructure;
        cs.changeCompetencePosition(1, 2);
        cs.changeCompetencePosition(3, 4);
        //System.out.println(cs.getMatrixString(cs.getCompetenceAdjacencyMatrix(), cs.competences));
        //System.out.println(cs.getVectorString(cs.getCoreCompetenceWeightVector(), cs.competences));
        //System.out.println(cs.getMatrixString(cs.getResolvedCompetenceAdjacencyMatrix(), cs.competences));
        
        System.out.println(sic.competenceState.getDiagnosticString(0.8));
        sic.updateCompetenceState(3, true);
        System.out.println(sic.competenceState.getDiagnosticString(0.8));
        sic.updateCompetenceState(1, true);
        System.out.println(sic.competenceState.getDiagnosticString(0.8));
        sic.updateCompetenceState(1, false);
        System.out.println(sic.competenceState.getDiagnosticString(0.8));
        System.out.println(sic.competenceState.getDiagnosticStringFringe(0.8));
        sic.updateCompetenceState(5, false);
        System.out.println(sic.competenceState.getDiagnosticString(0.8));

        System.out.println("END");
    }
}
