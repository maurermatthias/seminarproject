package test2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dbentities.DBclass;
import dbentities.DBcompetence;
import dbentities.DBcompetencestructure;
import dbentities.DBcompetencevalue;
import dbentities.DBcompetenceweight;
import dbentities.DBentity;
import dbentities.DBlinkageclasscstructure;
import dbentities.DBlinkageclasstask;
import dbentities.DBlinkagetaskcompetence;
import dbentities.DBregisteredstudent;
import dbentities.DBtask;
import dbentities.DBuser;
import dbentities.Usergroup;
import dbentities.Visibility;
import knowledgestructureelements.Competence;
import knowledgestructureelements.CompetenceStructure;
import knowledgestructureelements.Task;
import knowledgestructureelements.TaskCollection;

import java.util.Arrays;




public class DBConnector {
	//admin-login-data
	static private String adminName = "admin";
	static private String adminPwd = "admin";
	static private String teacherName = "teacher";
	static private String teacherPwd = "teacher";
	static private String studentName = "student";
	static private String studentPwd = "student";

	//static private String dbName = "db1";
	private static String userName = "java2";
	private static String password = "java3";
	private static String dbUrl = "jdbc:mysql://localhost/db2";
	
	//singelton instance
	//private static DBConnector instance;
	
	//table identifier
	private static String[] tableIdentifier = {"users","competences","tasks","competencestructures",
			"classes","competencevalues","linkagetaskcompetence","competenceweights",
			"linkageclasstask","linkageclasscstructure","registeredstudents"};
	
	//private constructor
	private DBConnector() {
	}
	
	private static void print(String msg){
		System.out.println(msg);
	}
	
	//get connection to DB
	public static Connection getConnection(){

		Connection conn = null;
		try{
		    Class.forName("com.mysql.jdbc.Driver");
		    conn = DriverManager.getConnection(dbUrl,userName,password);
		}catch (SQLException | ClassNotFoundException ex) {
		    print("Not connected to database");
		    print(ex.getMessage());
		} 
	    return conn;
	}
	
	//singelton getter
	public static void resetDB() {
		print("Reset DB - deleting all tables/content, recreating tables and inserting admin/teacher");
		createEmptyTable();
		if(usernameFree(adminName)){
			DBuser user = new DBuser();
			user.name=adminName;
			user.usergroup = 3;
			user.password = adminPwd;
			user.creator = -1;
			addNewUser(user);
		}
		if(usernameFree(teacherName)){
			DBuser user = new DBuser();
			user.name=teacherName;
			user.usergroup = 2;
			user.password = teacherPwd;
			user.creator = -1;
			addNewUser(user);
		}
		if(usernameFree(studentName)){
			DBuser user = new DBuser();
			user.name=studentName;
			user.usergroup = 1;
			user.password = studentPwd;
			user.creator = -1;
			addNewUser(user);
		}
	}
	
	///Methods for User-DB
	//check if username already exists
	public static boolean usernameFree(String name){

        List<DBentity> rs = select("users","name='"+name+"'");
        
        if (rs.isEmpty())   
        	return true;
		
		return false;
	}
	

	///++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//                    Basic table operations
	///++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	//returns usergroup for supplied login data (1..student/2...teacher/3...admin/0...unknown)
	public static Usergroup getUsergroup(String name, String password){
		List<DBentity> user = select("users","name='"+name+"' AND password='"+password+"'");
		
        if (user.isEmpty())   
        	return Usergroup.fromInteger(0);
        
        return Usergroup.fromInteger(((DBuser)user.get(0)).usergroup);
	}
	
	//insert an database entity into a table
	
	//Executes an SQL command, returns the result-set, if command was SELECT
 	public static void execute(String cmd){
 		print("Executing command: "+cmd);
		Statement stmt = null;

		try {
			Connection con = getConnection();
		    stmt = con.createStatement();
		    stmt.execute(cmd);
		    con.close();
		  
		}
		catch (SQLException ex){
		    // handle any errors
		    print("SQLException: " + ex.getMessage());
		    print("SQLState: " + ex.getSQLState());
		    print("VendorError: " + ex.getErrorCode());
		}
		finally {
		    if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException sqlEx) { } // ignore

		        stmt = null;
		    }
		}
	}
	
	public static void createTable(String type){
        String usertable = "";
        
        switch(type){
	        case "users":
	            usertable += "CREATE TABLE IF NOT EXISTS users (" 
	                    + "userid INT(64) NOT NULL AUTO_INCREMENT,"  
	                    + "creator INT(64)," 
	                    + "name TEXT,"  
	                    + "usergroup INT(2),"  
	                    + "password TEXT,"
	                    + "PRIMARY KEY (userid))"; 
	            break;
	        case "competences":
	            usertable += "CREATE TABLE IF NOT EXISTS competences (" 
	                    + "competenceid INT(64) NOT NULL AUTO_INCREMENT,"  
	                    + "creator INT(64)," 
	                    + "name TEXT,"  
	                    + "description TEXT,"  
	                    + "visibility Int(4),"
	                    //+ "FOREIGN KEY (creator) REFERENCES users(userid) ON DELETE CASCADE," 
	                    + "PRIMARY KEY (competenceid))"; 
	            break;
	        case "tasks":
	            usertable += "CREATE TABLE IF NOT EXISTS tasks (" 
	                    + "taskid INT(64) NOT NULL AUTO_INCREMENT,"  
	                    + "creator INT(64)," 
	                    + "name TEXT,"  
	                    + "description TEXT,"  
	                    + "visibility Int(4),"
	                    + "text TEXT,"  
	                    + "answer TEXT,"
	                    //+ "FOREIGN KEY (creator) REFERENCES users(userid) ON DELETE CASCADE," 
	                    + "PRIMARY KEY (taskid))"; 
	            break;
	        case "competencestructures":
	            usertable += "CREATE TABLE IF NOT EXISTS competencestructures (" 
	                    + "cstructureid INT(64) NOT NULL AUTO_INCREMENT,"  
	                    + "creator INT(64)," 
	                    + "name TEXT,"  
	                    + "description TEXT,"  
	                    + "visibility Int(4),"
	                    //+ "FOREIGN KEY (creator) REFERENCES users(userid) ON DELETE CASCADE," 
	                    + "PRIMARY KEY (cstructureid))"; 
	            break;
	        case "classes":
	            usertable += "CREATE TABLE IF NOT EXISTS classes (" 
	                    + "classid INT(64) NOT NULL AUTO_INCREMENT,"  
	                    + "creator INT(64)," 
	                    + "name TEXT,"  
	                    + "description TEXT,"  
	                    + "visibility Int(4),"
	                    //+ "FOREIGN KEY (creator) REFERENCES users(userid) ON DELETE CASCADE," 
	                    + "PRIMARY KEY (classid))"; 
	            break;
	        case "competencevalues":
	            usertable += "CREATE TABLE IF NOT EXISTS competencevalues (" 
	                    + "id INT(64) NOT NULL AUTO_INCREMENT,"  
	                    + "studentid INT(64) NOT NULL," 
	                    + "classid INT(64) NOT NULL,"  
	                    + "competenceid INT(64) NOT NULL,"  
	                    + "value DOUBLE,"
	                    + "PRIMARY KEY (id),"
	                    + "FOREIGN KEY (studentid) REFERENCES users(userid) ON DELETE CASCADE," 
	                    + "FOREIGN KEY (classid) REFERENCES classes(classid) ON DELETE CASCADE," 
	                    + "FOREIGN KEY (competenceid) REFERENCES competences(competenceid) ON DELETE CASCADE)";
	            break;
	        case "linkagetaskcompetence":
	            usertable += "CREATE TABLE IF NOT EXISTS linkagetaskcompetence (" 
	                    + "id INT(64) NOT NULL AUTO_INCREMENT,"  
	                    + "taskid INT(64) NOT NULL,"  
	                    + "competenceid INT(64) NOT NULL,"  
	                    + "weight DOUBLE,"
	                    + "PRIMARY KEY (id),"
	                    + "FOREIGN KEY (taskid) REFERENCES tasks(taskid) ON DELETE CASCADE," 
	                    + "FOREIGN KEY (competenceid) REFERENCES competences(competenceid) ON DELETE CASCADE)";
	            break;
	        case "linkageclasstask":
	            usertable += "CREATE TABLE IF NOT EXISTS linkageclasstask (" 
	                    + "id INT(64) NOT NULL AUTO_INCREMENT,"  
	                    + "taskid INT(64) NOT NULL,"  
	                    + "classid INT(64) NOT NULL,"  
	                    + "PRIMARY KEY (id),"
	                    + "FOREIGN KEY (taskid) REFERENCES tasks(taskid) ON DELETE CASCADE," 
	                    + "FOREIGN KEY (classid) REFERENCES classes(classid) ON DELETE CASCADE)";
	            break;
	        case "linkageclasscstructure":
	            usertable += "CREATE TABLE IF NOT EXISTS linkageclasscstructure (" 
	                    + "id INT(64) NOT NULL AUTO_INCREMENT,"  
	                    + "classid INT(64) NOT NULL,"  
	                    + "cstructureid INT(64) NOT NULL,"  
	                    + "PRIMARY KEY (id),"
	                    + "FOREIGN KEY (classid) REFERENCES tasks(taskid) ON DELETE CASCADE," 
	                    + "FOREIGN KEY (cstructureid) REFERENCES competencestructures(cstructureid) ON DELETE CASCADE)";
	            break;
	        case "competenceweights":
	            usertable += "CREATE TABLE IF NOT EXISTS competenceweights (" 
	                    + "id INT(64) NOT NULL AUTO_INCREMENT,"  
	                    + "fromcompetenceid INT(64) NOT NULL,"  
	                    + "tocompetenceid INT(64) NOT NULL,"  
	                    + "cstructureid INT(64) NOT NULL,"  
	                    + "weight DOUBLE,"
	                    + "PRIMARY KEY (id),"
	                    + "FOREIGN KEY (fromcompetenceid) REFERENCES competences(competenceid) ON DELETE CASCADE," 
	                    + "FOREIGN KEY (tocompetenceid) REFERENCES competences(competenceid) ON DELETE CASCADE," 
	                    + "FOREIGN KEY (cstructureid) REFERENCES competencestructures(cstructureid) ON DELETE CASCADE)";
	            break;
	        case "registeredstudents":
	            usertable += "CREATE TABLE IF NOT EXISTS registeredstudents (" 
	                    + "id INT(64) NOT NULL AUTO_INCREMENT,"  
	                    + "classid INT(64) NOT NULL,"  
	                    + "studentid INT(64) NOT NULL,"  
	                    + "PRIMARY KEY (id),"
	                    + "FOREIGN KEY (classid) REFERENCES classes(classid) ON DELETE CASCADE," 
	                    + "FOREIGN KEY (studentid) REFERENCES users(userid) ON DELETE CASCADE)";
	            break;
	        default:
	        	print("Table type "+type+" unknown!");
	        	return;
        }
        
        try {
            Connection con = DBConnector.getConnection();
            Statement statement = (Statement) con.createStatement();
			statement.executeUpdate(usertable);
			con.close();
		} catch (SQLException e) {
			print("Error when creating table "+type+"!");
			e.printStackTrace();
		}
	}
	public static void createTable(){
		for(int i=0;i<DBConnector.tableIdentifier.length;i++)
			createTable(DBConnector.tableIdentifier[i]);
	}
	public static void truncateTable(String type){
		String usertable = "";
        
        switch(type){
	        case "users":
	            usertable += "TRUNCATE TABLE users"; 
	            break;
	        case "competences":
	            usertable += "TRUNCATE TABLE competences"; 
	            break;
	        case "tasks":
	            usertable += "TRUNCATE TABLE tasks"; 
	            break;
	        case "competencestructures":
	            usertable += "TRUNCATE TABLE competencestructure"; 
	            break;
	        case "classes":
	            usertable += "TRUNCATE TABLE classes"; 
	            break;
	        case "competencevalues":
	            usertable += "TRUNCATE TABLE competencevalues"; 
	            break;
	        case "linkagetaskcompetence":
	            usertable += "TRUNCATE TABLE linkagetaskcompetence"; 
	            break;
	        case "competenceweights":
	            usertable += "TRUNCATE TABLE competenceweights"; 
	            break;
	        case "linkageclasstask":
	            usertable += "TRUNCATE TABLE linkageclasstask"; 
	            break;
	        case "linkageclasscstructure":
	            usertable += "TRUNCATE TABLE linkageclasscstructure"; 
	            break;
	        case "registeredstudents":
	            usertable += "TRUNCATE TABLE registeredstudents"; 
	            break;
	        default:
	        	print("Table type "+type+" unknown!");
	        	return;
        }
        
        try {
            Connection con = DBConnector.getConnection();
            Statement statement = (Statement) con.createStatement();
			statement.executeUpdate(usertable);
			con.close();
		} catch (SQLException e) {
			print("Error when truncating table "+type+"!");
			e.printStackTrace();
		}
	}
	public static void truncateTable(){
		for(int i=DBConnector.tableIdentifier.length-1;i>=0;i--)
			truncateTable(DBConnector.tableIdentifier[i]);
	}
	public static void createEmptyTable(String type){
		dropTable(type);
		createTable(type);
	}
	public static void createEmptyTable(){
		for(int i=DBConnector.tableIdentifier.length-1;i>=0;i--)
			dropTable(DBConnector.tableIdentifier[i]);
		for(int i=0;i<DBConnector.tableIdentifier.length;i++)
			createTable(DBConnector.tableIdentifier[i]);
	}
	public static void dropTable(String type){
		String usertable = "";
        
        switch(type){
	        case "users":
	            usertable += "DROP TABLE IF EXISTS users"; 
	            break;
	        case "competences":
	            usertable += "DROP TABLE IF EXISTS competences"; 
	            break;
	        case "tasks":
	            usertable += "DROP TABLE IF EXISTS tasks"; 
	            break;
	        case "competencestructures":
	            usertable += "DROP TABLE IF EXISTS competencestructure"; 
	            break;
	        case "classes":
	            usertable += "DROP TABLE IF EXISTS classes"; 
	            break;
	        case "competencevalues":
	            usertable += "DROP TABLE IF EXISTS competencevalues"; 
	            break;
	        case "linkagetaskcompetence":
	            usertable += "DROP TABLE IF EXISTS linkagetaskcompetence"; 
	            break;
	        case "competenceweights":
	            usertable += "DROP TABLE IF EXISTS competenceweights"; 
	            break;
	        case "linkageclasstask":
	            usertable += "DROP TABLE IF EXISTS linkageclasstask"; 
	            break;
	        case "linkageclasscstructure":
	            usertable += "DROP TABLE IF EXISTS linkageclasscstructure"; 
	            break;
	        case "registeredstudents":
	            usertable += "DROP TABLE IF EXISTS registeredstudents"; 
	            break;
	        default:
	        	print("Table type "+type+" unknown!");
	        	return;
        }
        
        try {
            Connection con = DBConnector.getConnection();
            Statement statement = (Statement) con.createStatement();
			statement.executeUpdate(usertable);
			con.close();
		} catch (SQLException e) {
			print("Error when dropping table "+type+"!");
			e.printStackTrace();
		}
	}
	public static void dropTable(){
		for(int i=DBConnector.tableIdentifier.length-1;i>=0;i--)
			dropTable(DBConnector.tableIdentifier[i]);
	}

	///++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//                    GETTER
	///++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	public static List<DBentity> select(String table, String where){
		
	    
		if(!Arrays.asList(DBConnector.tableIdentifier).contains(table))
			print("table identifier unknown!");
		
		Statement stmt = null;
		ResultSet rs = null;
		List<DBentity> results = new ArrayList<DBentity>();
		
		String cmd;
		if(where == null)
			cmd = "SELECT * FROM "+table+";";
		else
			cmd = "SELECT * FROM "+table+" WHERE "+where+";";
		
		print("Executing command: "+cmd);
		
		try{
			Connection con = getConnection();
		    stmt = con.createStatement();
		    stmt.execute(cmd);
		    rs = stmt.getResultSet();
		    
		    switch(table){
			    case "users":
		        	while(rs.next()){
		        		DBuser ue = new DBuser();
		        		ue.userid = rs.getInt("userid");
		        		ue.usergroup = rs.getInt("usergroup");
		        		ue.name = rs.getString("name");
		        		ue.creator = rs.getInt("creator");
		        		ue.password = rs.getString("password");
		        		results.add(ue);
		        	}
			    	break;
			    case "competencevalues":
		        	while(rs.next()){
		        		DBcompetencevalue cv = new DBcompetencevalue();
		        		cv.classid = rs.getInt("classid");
		        		cv.competenceid = rs.getInt("competenceid");
		        		cv.id = rs.getInt("id");
		        		cv.studentid = rs.getInt("studentid");
		        		cv.value = rs.getDouble("value");
		        		results.add(cv);
		        	}
			    	break;
			    case "competences":
		        	while(rs.next()){
		        		DBcompetence c = new DBcompetence();
		        		c.competenceid = rs.getInt("competenceid");
		        		c.creator = rs.getInt("creator");
		        		c.name = rs.getString("name");
		        		c.description = rs.getString("description");
		        		c.visibility = Visibility.fromInteger(rs.getInt("visibility"));
		        		results.add(c);
		        	}
			    	break;
			    case "tasks":
		        	while(rs.next()){
		        		DBtask t = new DBtask();
		        		t.taskid = rs.getInt("taskid");
		        		t.creator = rs.getInt("creator");
		        		t.name = rs.getString("name");
		        		t.description = rs.getString("description");
		        		t.visibility = Visibility.fromInteger(rs.getInt("visibility"));
		        		t.text = rs.getString("text");
		        		t.answer = rs.getString("answer");
		        		results.add(t);
		        	}
			    	break;
			    case "linkagetaskcompetence":
		        	while(rs.next()){
		        		DBlinkagetaskcompetence ltc = new DBlinkagetaskcompetence();
		        		ltc.id = rs.getInt("id");
		        		ltc.taskid = rs.getInt("taskid");
		        		ltc.competenceid = rs.getInt("competenceid");
		        		ltc.weight = rs.getDouble("weight");
		        		results.add(ltc);
		        	}
			    	break;
			    case "competencestructures":
		        	while(rs.next()){
		        		DBcompetencestructure cs = new DBcompetencestructure();
		        		cs.cstructureid = rs.getInt("cstructureid");
		        		cs.creator = rs.getInt("creator");
		        		cs.name = rs.getString("name");
		        		cs.description = rs.getString("description");
		        		cs.visibility = Visibility.fromInteger(rs.getInt("visibility"));
		        		results.add(cs);
		        	}
			    	break;
			    case "competenceweights":
		        	while(rs.next()){
		        		DBcompetenceweight cw = new DBcompetenceweight();
		        		cw.id = rs.getInt("id");
		        		cw.fromcompetenceid = rs.getInt("fromcompetenceid");
		        		cw.tocompetenceid = rs.getInt("tocompetenceid");
		        		cw.cstructureid = rs.getInt("cstructureid");
		        		cw.weight = rs.getDouble("weight");
		        		results.add(cw);
		        	}
			    	break;
			    case "classes":
		        	while(rs.next()){
		        		DBclass cl = new DBclass();
		        		cl.classid = rs.getInt("classid");
		        		cl.creator = rs.getInt("creator");
		        		cl.name = rs.getString("name");
		        		cl.description = rs.getString("description");
		        		cl.visibility = Visibility.fromInteger(rs.getInt("visibility"));
		        		results.add(cl);
		        	}
			    	break;
			    case "linkageclasstask":
		        	while(rs.next()){
		        		DBlinkageclasstask lct = new DBlinkageclasstask();
		        		lct.id = rs.getInt("id");
		        		lct.taskid = rs.getInt("taskid");
		        		lct.classid = rs.getInt("classid");
		        		results.add(lct);
		        	}
			    	break;
			    case "linkageclasscstructure":
		        	while(rs.next()){
		        		DBlinkageclasscstructure lcc = new DBlinkageclasscstructure();
		        		lcc.id = rs.getInt("id");
		        		lcc.cstructureid = rs.getInt("cstructureid");
		        		lcc.classid = rs.getInt("classid");
		        		results.add(lcc);
		        	}
			    	break;
			    case "registeredstudents":
		        	while(rs.next()){
		        		DBregisteredstudent res = new DBregisteredstudent();
		        		res.id = rs.getInt("id");
		        		res.studentid = rs.getInt("studentid");
		        		res.classid = rs.getInt("classid");
		        		results.add(res);
		        	}
			    	break;
		    }
		    con.close();
		    
		}catch(SQLException ex){
		    // handle any errors
		    print("SQLException: " + ex.getMessage());
		    print("SQLState: " + ex.getSQLState());
		    print("VendorError: " + ex.getErrorCode());
		}
		finally {

		    if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException sqlEx) { } // ignore

		        rs = null;
		    }
		    if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException sqlEx) { } // ignore

		        stmt = null;
		    }
		}
		
		return results;
	}
	public static List<DBentity> getRegisteredClasses(int studentid){
		List<DBentity> regClasses = select("registeredstudents","studentid='"+studentid+"'");
		List<DBentity> entities = new ArrayList<DBentity>();
		for(DBentity entity : regClasses){
			entities.add((DBentity) getClassById(((DBregisteredstudent)entity).classid));
		}
		return entities;
	}
	public static List<DBentity> getAvailableClasses(int creatorid){
		List<DBentity> entities = select("classes","creator='"+creatorid+"' OR visibility=0");
		return entities;
	}
	public static int getUserId(String name){
		List<DBentity> entities = select("users","name='"+name+"'");
		if(entities.isEmpty())
			return 0;
		else 
			return ((DBuser)entities.get(0)).userid;
	}
	public static DBuser getUserById(int id){
		List<DBentity> entity = select("users","userid="+id);
		if(entity.isEmpty())
			return null;
		else
			return ((DBuser) entity.get(0));
	}
	public static DBclass getClassById(int id){
		List<DBentity> entity = select("classes","classid="+id);
		if(entity.isEmpty())
			return null;
		else
			return ((DBclass) entity.get(0));
	}
	public static DBcompetencestructure getCStructureById(int id){
		List<DBentity> entity = select("competencestructures","cstructureid="+id);
		if(entity.isEmpty())
			return null;
		else
			return ((DBcompetencestructure) entity.get(0));
	}
	public static DBcompetence getCompetenceById(int id){
		List<DBentity> entity = select("competences","competenceid="+id);
		if(entity.isEmpty())
			return null;
		else
			return ((DBcompetence) entity.get(0));
	}
	public static int getClassIdByName(String name){
		List<DBentity> entities = select("classes","name='"+name+"'");
		if(entities.isEmpty())
			return 0;
		else 
			return ((DBclass)entities.get(0)).classid;
	}
	public static List<DBentity> getAllUsers(){
		List<DBentity> entities = select("users","usergroup<6 ORDER BY usergroup DESC");
		return entities;
	}
	public static List<DBentity> getAllStudents(){
		List<DBentity> entities = select("users","usergroup=1");
		return entities;
	}
	public static List<DBentity> getAllTeacher(){
		List<DBentity> entities = select("users","usergroup=2");
		return entities;
	}
	public static List<DBentity> getAllAdministartors(){
		List<DBentity> entities = select("users","usergroup=3");
		return entities;
	}
	public static List<DBentity> getClassesByTeacherId(int teacherId){
		List<DBentity> entities = select("classes","creator="+teacherId);
		return entities;
	}
	public static List<DBentity> getCompetencesByTeacherId(int teacherId){
		List<DBentity> entities = select("competences","creator="+teacherId);
		return entities;
	}
	public static List<DBentity> getCompetenceLinksToTaskById(int taskId){
		List<DBentity> entities = select("linkagetaskcompetence","taskid="+taskId);
		return entities;
	}
	public static List<DBentity> getCstructureByTeacherId(int teacherId){
		List<DBentity> entities = select("competencestructures","creator="+teacherId);
		return entities;
	}
	public static List<DBentity> getTasksByTeacherId(int teacherId){
		List<DBentity> entities = select("tasks","creator="+teacherId);
		return entities;
	}
	public static List<DBentity> getClassTaskLinkageByClassId(int classId){
		List<DBentity> entities = select("linkageclasstask","classid="+classId);
		return entities;
	}
	public static DBtask getTaskById(int taskId){
		List<DBentity> entities = select("tasks","taskid="+taskId);
		if(entities.isEmpty())
			return null;
		return (DBtask) entities.get(0);
	}
	public static List<DBentity> getVisibleCompetencesByTeacherId(int teacherId){
		List<DBentity> entities = select("competences","creator!="+teacherId+" AND visibility=0");
		return entities;
	}
	public static List<DBentity> getVisibleCstructureByTeacherId(int teacherId){
		List<DBentity> entities = select("competencestructures","creator!="+teacherId+" AND visibility=0");
		return entities;
	}
	public static List<DBentity> getVisibleTasksByTeacherId(int teacherId){
		List<DBentity> entities = select("tasks","creator!="+teacherId+" AND visibility=0");
		return entities;
	}
	public static List<DBentity> getRegisteredStudentsByClassId(int classId){
		List<DBentity> entities = select("registeredstudents","classid="+classId);
		List<DBentity> users = new ArrayList<DBentity>();
		for(DBentity entity : entities){
			users.add(select("users","userid="+((DBregisteredstudent)entity).studentid).get(0));
		}
		return users;
	}
	public static List<DBentity> getCreatedStudentsByCreatorId(int creatorId){
		List<DBentity> entities = select("users","creator="+creatorId);
		return entities;
	}
	public static List<DBentity> getCompetenceWeightByCStructureId(int cstructureid){
		List<DBentity> entities = select("competenceweights","cstructureid="+cstructureid);
		return entities;
	}
	public static int getCstructureIdByName(String name){
		List<DBentity> entities = select("competencestructures","name='"+name+"'");
		if(entities.isEmpty())
			return 0;
		else 
			return ((DBcompetencestructure)entities.get(0)).cstructureid;
	}
	public static int getLinkageTaskCompetenceIdByTaskIdCompetenceId(int taskid,int competenceid){
		List<DBentity> entities = select("linkagetaskcompetence","taskid="+taskid+" AND competenceid="+competenceid);
		if(entities.isEmpty())
			return 0;
		else 
			return ((DBlinkagetaskcompetence)entities.get(0)).id;
	}
	public static int getLinkageClassTaskIdByClassIdTaskId(int classid,int taskid){
		List<DBentity> entities = select("linkageclasstask","taskid="+taskid+" AND classid="+classid);
		if(entities.isEmpty())
			return 0;
		else 
			return ((DBlinkageclasstask)entities.get(0)).id;
	}
	public static int getRegisteredStudentIdByClassIdStudentId(int classid,int studentid){
		List<DBentity> entities = select("registeredstudents","studentid="+studentid+" AND classid="+classid);
		if(entities.isEmpty())
			return 0;
		else 
			return ((DBregisteredstudent)entities.get(0)).id;
	}
	public static int getCompetenceWeightByFromToCstructureIds(int fromid, int toid, int cstructureid){
		List<DBentity> entities = select("competenceweights","fromcompetenceid="+fromid+
				" AND tocompetenceid="+toid+" AND cstructureid="+cstructureid);
		if(entities.isEmpty())
			return 0;
		else 
			return ((DBcompetenceweight)entities.get(0)).id;
	}
	public static int getLinkageClassCstructureIdByClassId(int classid){
		List<DBentity> entities = select("linkageclasscstructure","classid="+classid);
		if(entities.isEmpty())
			return 0;
		else 
			return ((DBlinkageclasscstructure)entities.get(0)).id;
	}
	public static int getCompetenceIdByName(String name){
		List<DBentity> entities = select("competences","name='"+name+"'");
		if(entities.isEmpty())
			return 0;
		else 
			return ((DBcompetence)entities.get(0)).competenceid;
	}
	public static String getCompetenceNameById(int id){
		List<DBentity> entities = select("competences","competenceid='"+id+"'");
		if(entities.isEmpty())
			return null;
		else 
			return ((DBcompetence)entities.get(0)).name;
	}
	public static int getTaskIdByName(String name){
		List<DBentity> entities = select("tasks","name='"+name+"'");
		if(entities.isEmpty())
			return 0;
		else 
			return ((DBtask)entities.get(0)).taskid;
	}
	public static CompetenceStructure getCompetenceStructure(int cstructureId){
		List<DBentity> competenceWeights = select("competenceweights","cstructureid="+cstructureId);
		List<Integer> competenceIds = new ArrayList<Integer>();
		for(DBentity competenceWeight : competenceWeights){
			DBcompetenceweight edge = (DBcompetenceweight) competenceWeight;
			if(!competenceWeights.contains(edge.fromcompetenceid))
				competenceIds.add(edge.fromcompetenceid);
			if(!competenceWeights.contains(edge.tocompetenceid))
				competenceIds.add(edge.tocompetenceid);
		}
		
		List<DBcompetence> competences = new ArrayList<DBcompetence>();
		for(int compId : competenceIds){
			List<DBentity> ent = select("competences","competenceid="+compId);
			competences.add((DBcompetence) ent.get(0));
		}
		
		CompetenceStructure compStr = new CompetenceStructure();
		for(DBcompetence comp : competences){
			compStr.addCompetence(new Competence(comp.name));
		}
		
		for(DBentity competenceWeight : competenceWeights){
			DBcompetenceweight edge = (DBcompetenceweight) competenceWeight;
			String nameFrom = null;
			for(DBcompetence from : competences){
				if(from.competenceid == edge.fromcompetenceid){
					nameFrom = from.name;
					break;
				}
			}
			String nameTo = null;
			for(DBcompetence to : competences){
				if(to.competenceid == edge.tocompetenceid){
					nameTo = to.name;
					break;
				}
			}
			compStr.addEdge(nameFrom, nameTo, edge.weight);
		}
		
		return compStr;
	}
	public static TaskCollection getTaskCollectionByClassId(int classId, CompetenceStructure competenceStructure){
		List<DBentity> tasklinks = select("linkageclasstask","classid="+classId);
		TaskCollection tc = new TaskCollection();
		for(DBentity task : tasklinks){
			tc.addTask(new Task(((DBlinkageclasstask) task).taskid,competenceStructure));
		}
		return tc;
	}
	public static int getCstructureIdByClassId(int id){
		List<DBentity> entities = select("linkageclasscstructure","classid="+id);
		if(entities.isEmpty())
			return 0;
		else 
			return ((DBlinkageclasscstructure)entities.get(0)).cstructureid;
	}
	public static double getCompetenceValue(int studentid,int classid,int competenceid){
		List<DBentity> entities = select("competencevalues","classid="+classid+" AND studentid="+studentid+" AND competenceid="+competenceid);
		if(entities.isEmpty())
			return -1.0;
		else 
			return ((DBcompetencevalue)entities.get(0)).value;
	}
	
	///++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//                    Insert data
	///++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	public static void insert(String table, DBentity entity){
	    
		if(!Arrays.asList(DBConnector.tableIdentifier).contains(table))
			print("table identifier unknown!");
		
		String cmd = "INSERT INTO "+ table +" ";
		
	    switch(table){
		    case "users":
		    	DBuser u = (DBuser) entity;
		    	cmd += "(usergroup,name,creator,password) VALUES ";
		    	cmd += "("+u.usergroup+",'"+u.name+"',"+u.creator+",'"+u.password+"');";
		    	execute(cmd);
		    	break;
		    case "competencevalues":
		    	DBcompetencevalue cv = (DBcompetencevalue) entity;
		    	cmd += "(competenceid,classid,studentid,value) VALUES ";
		    	cmd += "("+cv.competenceid+","+cv.classid+","+cv.studentid+","+cv.value+");";
		    	execute(cmd);
		    	break;
		    case "competences":
		    	DBcompetence c = (DBcompetence) entity;
		    	cmd += "(creator,name,description,visibility) VALUES ";
		    	cmd += "('"+c.creator+"','"+c.name+"','"+c.description+"',"+Visibility.toInteger(c.visibility)+");";
		    	execute(cmd);
		    	break;
		    case "tasks":
		    	DBtask t = (DBtask) entity;
		    	cmd += "(creator,name,description,visibility,text,answer) VALUES ";
		    	cmd += "("+t.creator+",'"+t.name+"','"+t.description+"',"+Visibility.toInteger(t.visibility)+",'"+t.text+"','"+t.answer+"');";
		    	execute(cmd);
		    	break;
		    case "linkagetaskcompetence":
		    	DBlinkagetaskcompetence ltc = (DBlinkagetaskcompetence) entity;
		    	cmd += "(taskid,competenceid,weight) VALUES ";
		    	cmd += "("+ltc.taskid+","+ltc.competenceid+","+ltc.weight+");";
		    	execute(cmd);
		    	break;
		    case "competencestructures":
		    	DBcompetencestructure cs = (DBcompetencestructure) entity;
		    	cmd += "(creator,name,description,visibility) VALUES ";
		    	cmd += "("+cs.creator+",'"+cs.name+"','"+cs.description+"',"+Visibility.toInteger(cs.visibility)+");";
		    	execute(cmd);
		    	break;
		    case "competenceweights":
		    	DBcompetenceweight cw = (DBcompetenceweight) entity;
		    	cmd += "(fromcompetenceid,tocompetenceid,cstructureid,weight) VALUES ";
		    	cmd += "("+cw.fromcompetenceid+","+cw.tocompetenceid+","+cw.cstructureid+","+cw.weight+");";
		    	execute(cmd);
		    	break;
		    case "classes":
		    	DBclass cl = (DBclass) entity;
		    	cmd += "(creator,name,description,visibility) VALUES ";
		    	cmd += "("+cl.creator+",'"+cl.name+"','"+cl.description+"',"+Visibility.toInteger(cl.visibility)+");";
		    	execute(cmd);
		    	break;
		    case "linkageclasstask":
		    	DBlinkageclasstask lct = (DBlinkageclasstask) entity;
		    	cmd += "(taskid,classid) VALUES ";
		    	cmd += "("+lct.taskid+","+lct.classid+");";
		    	execute(cmd);
		    	break;
		    case "linkageclasscstructure":
		    	DBlinkageclasscstructure lcc = (DBlinkageclasscstructure) entity;
		    	cmd += "(cstructureid,classid) VALUES ";
		    	cmd += "("+lcc.cstructureid+","+lcc.classid+");";
		    	execute(cmd);
		    	break;
		    case "registeredstudents":
		    	DBregisteredstudent rs = (DBregisteredstudent) entity;
		    	cmd += "(studentid,classid) VALUES ";
		    	cmd += "("+rs.studentid+","+rs.classid+");";
		    	execute(cmd);
		    	break;
	    }
	}
	public static boolean addNewUser(DBuser user) {
		if(usernameFree(user.name)){
			//String cmd = "INSERT INTO users (creator,name,usergroup,password) VALUES (\""+creator
			//		+"\",\""+name+"\","+usergroup+",\""+pwd+"\");";
			//update(cmd);
			insert("users",user);
			print("New user '"+user.name+"' inserted.");
			return true;
		}
		else{
			print("Adding new user not possible - username '"+user.name+"' already exists!");
			return false;
		}
	}
	public static boolean addNewClass(DBclass clazz){
		List<DBentity> entity = select("classes","name='"+clazz.name+"'");
		if(entity.isEmpty()){
			insert("classes",clazz);
			print("Class '"+clazz.name+"' added.");
			return true;
		}
		else{
			print("Can not add class '"+clazz.name+"' - this class already exists!");
			return false;
		}
	}
	public static boolean registerStudentToClass(DBregisteredstudent regstud){
		List<DBentity> entity = select("registeredstudents","studentid="+regstud.studentid+" AND classid="+regstud.classid);
		if(entity.isEmpty()){
			DBclass clazz = getClassById(regstud.classid);
			if(getUserById(regstud.studentid).creator == clazz.creator || clazz.visibility == Visibility.ALL){
				insert("registeredstudents",regstud);
				print("Registered student with id '"+regstud.studentid+"' for class with id '"+regstud.classid+"'!");
				return true;
			}else{
				print("Can not register student with id '"+regstud.studentid+"' for class with id '"+regstud.classid+"' - Student is not allowed to register!");
				return false;
			}
		}
		else{
			print("Can not register student with id '"+regstud.studentid+"' for class with id '"+regstud.classid+"' - this combination already exists!");
			return false;
		}
	}
	public static boolean addNewCompetence(DBcompetence competence){
		List<DBentity> entity = select("competences","name='"+competence.name+"'");
		if(entity.isEmpty()){
			insert("competences",competence);
			print("Competence '"+competence.name+"' added.");
			return true;
		}
		else{
			print("Can not add competence '"+competence.name+"' - this competence already exists!");
			return false;
		}
	}
	public static boolean addNewCompetenceStructure(DBcompetencestructure competencestructure){
		List<DBentity> entity = select("competencestructures","name='"+competencestructure.name+"'");
		if(entity.isEmpty()){
			insert("competencestructures",competencestructure);
			print("Competencestructure '"+competencestructure.name+"' added.");
			return true;
		}
		else{
			print("Can not add competencestructure '"+competencestructure.name+"' - this competencestructure already exists!");
			return false;
		}
	}
	public static boolean addNewCompetenceWeight(DBcompetenceweight competenceweight){
		List<DBentity> entity = select("competenceweights","fromcompetenceid="+competenceweight.fromcompetenceid+
				" AND tocompetenceid="+competenceweight.tocompetenceid+" AND cstructureid="+competenceweight.cstructureid);
		if(entity.isEmpty()){
			insert("competenceweights",competenceweight);
			print("Competenceweight added.");
			return true;
		}
		else{
			print("Can not add competenceweight - this competenceweight already exists!");
			return false;
		}
	}
	public static boolean addNewTask(DBtask task){
		List<DBentity> entity = select("tasks","name='"+task.name+"'");
		if(entity.isEmpty()){
			insert("tasks",task);
			print("Task '"+task.name+"' added.");
			return true;
		}
		else{
			print("Can not add task '"+task.name+"' - this taskname already exists!");
			return false;
		}
	}
	public static boolean addNewTaskCompetenceLink(DBlinkagetaskcompetence ltc){
		List<DBentity> entity = select("linkagetaskcompetence","taskid="+ltc.taskid+" AND competenceid="+ltc.competenceid);
		if(entity.isEmpty()){
			insert("linkagetaskcompetence",ltc);
			print("Task and Competence linked together.");
			return true;
		}
		else{
			print("Task and Competence already linked together.");
			return false;
		}
	}
	public static boolean addNewClassCstructureLink(DBlinkageclasscstructure lcc){
		List<DBentity> entity = select("linkageclasscstructure","classid="+lcc.classid);
		if(entity.isEmpty()){
			insert("linkageclasscstructure",lcc);
			print("Class and Competencestructure linked together.");
			return true;
		}
		else{
			print("Class  already linked to a Competencestructure.");
			return false;
		}
	}
	public static boolean addNewClassTaskLink(DBlinkageclasstask lct){
		List<DBentity> entity = select("linkageclasstask","classid="+lct.classid+" AND taskid="+lct.taskid);
		if(entity.isEmpty()){
			insert("linkageclasstask",lct);
			print("Class and task linked together.");
			return true;
		}
		else{
			print("Class and task already linked together.");
			return false;
		}
	}
	public static boolean addNewCompetenceValue(DBcompetencevalue cvalue){
		List<DBentity> entity = select("competencevalues","studentid="+cvalue.studentid+" AND classid="+cvalue.classid+" AND competenceid="+cvalue.competenceid);
		if(entity.isEmpty()){
			insert("competencevalues",cvalue);
			print("Competencevalue inserted.");
			return true;
		}
		else{
			print("Competencevalue already exists!");
			return false;
		}
	}
	///++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//                    Change data
	///++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	public static boolean update(String table, String set, String where){
		if(!Arrays.asList(DBConnector.tableIdentifier).contains(table))
			print("table identifier unknown!");
		
		List<DBentity> entities = select(table, where);
		if(entities.isEmpty())
			return false;
		
		String cmd = "UPDATE "+table+" SET "+ set + " WHERE " + where +";";
		execute(cmd);
		
		return true;
	}
	public static boolean updateCompetenceValue(DBcompetencevalue cvalue){
		return update("competencevalues","value="+cvalue.value,"studentid="+cvalue.studentid+
				" AND classid="+cvalue.classid+" AND competenceid="+cvalue.competenceid);
	}
	public static boolean updateLinkageClassCstructure(DBlinkageclasscstructure entity){
		return update("linkageclasscstructure","cstructureid="+entity.cstructureid,
				"classid="+entity.classid);
	}
	
	///++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//                    DELETE data
	///++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	public static boolean delete(String table, String where){
		if(!Arrays.asList(DBConnector.tableIdentifier).contains(table))
			print("table identifier unknown!");
		
		String cmd = "DELETE FROM "+table+" WHERE " + where +";";
		execute(cmd);
		
		return true;
	}
	public static boolean deleteUserByName(String name){
		if(DBConnector.getUserId(name)==0)
			return false;
		return delete("users","name='"+name+"'");
	}
	public static boolean deleteClassByName(String name){
		if(DBConnector.getClassIdByName(name)==0)
			return false;
		return delete("classes","name='"+name+"'");
	}
	public static boolean deleteTaskByName(String name){
		if(DBConnector.getTaskIdByName(name)==0)
			return false;
		return delete("tasks","name='"+name+"'");
	}
	public static boolean deleteCompetenceByName(String name){
		if(DBConnector.getCompetenceIdByName(name)==0)
			return false;
		return delete("competences","name='"+name+"'");
	}
	public static boolean deleteCstructureByName(String name){
		if(DBConnector.getCstructureIdByName(name)==0)
			return false;
		return delete("competencestructures","name='"+name+"'");
	}
	public static boolean deleteLinkageTaskCompetence(int taskid, int competenceid){
		if(DBConnector.getLinkageTaskCompetenceIdByTaskIdCompetenceId(taskid,competenceid)==0)
			return false;
		return delete("linkagetaskcompetence","taskid="+taskid+" AND competenceid="+competenceid);
	}
	public static boolean deleteLinkageClassCstructure(int classid){
		if(DBConnector.getLinkageClassCstructureIdByClassId(classid)==0)
			return false;
		return delete("linkageclasscstructure","classid="+classid);
	}
	public static boolean deleteLinkageClassTask(int classid,int taskid){
		if(DBConnector.getLinkageClassTaskIdByClassIdTaskId(classid,taskid)==0)
			return false;
		return delete("linkageclasstask","classid="+classid+" AND taskid="+taskid);
	}
	public static boolean deleteRegisteredStudentByClassIdStudentId(int classid,int studentid){
		if(DBConnector.getRegisteredStudentIdByClassIdStudentId(classid,studentid)==0)
			return false;
		return delete("registeredstudents","studentid="+studentid+" AND classid="+classid);
	}
	public static boolean deleteCompetenceWeightByFromToCstructureIds(int fromid, int toid, int cstructureid){
		if(DBConnector.getCompetenceWeightByFromToCstructureIds(fromid,toid,cstructureid)==0)
			return false;
		return delete("competenceweights","fromcompetenceid="+fromid+
				" AND tocompetenceid="+toid+" AND cstructureid="+cstructureid);
	}
	///++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//                   Test Data
	///++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
 	public static void createTestData(){
		//create User
		DBuser teacher1 = new DBuser("teacher1","teacher1",2,getUserId(adminName));
		addNewUser(teacher1);
		DBuser teacher2 = new DBuser("teacher2","teacher2",2,getUserId(adminName));
		addNewUser(teacher2);
		DBuser student1 = new DBuser("student1","student1",1,getUserId(teacher1.name));
		addNewUser(student1);
		DBuser student2 = new DBuser("student2","student2",1,getUserId(teacher1.name));
		addNewUser(student2);
		DBuser student3 = new DBuser("student3","student3",1,getUserId(teacher1.name));
		addNewUser(student3);
		DBuser student4 = new DBuser("student4","student4",1,getUserId(teacher2.name));
		addNewUser(student4);
		DBuser student5 = new DBuser("student5","student5",1,getUserId(teacher2.name));
		addNewUser(student5);
		DBuser student6 = new DBuser("student6","student6",1,getUserId(teacher2.name));
		addNewUser(student6);
		
		//create classes
		DBclass class1 = new DBclass("class1","desc. class1",Visibility.ALL,getUserId("teacher1"));
		addNewClass(class1);
		DBclass class2 = new DBclass("class2","desc. class2",Visibility.NOTALL,getUserId("teacher1"));
		addNewClass(class2);
		DBclass class3 = new DBclass("class3","desc. class3",Visibility.ALL,getUserId("teacher2"));
		addNewClass(class3);
		DBclass class4 = new DBclass("class4","desc. class4",Visibility.NOTALL,getUserId("teacher2"));
		addNewClass(class4);
		
		//register students
		DBregisteredstudent regstud1 = new DBregisteredstudent(getUserId("student1"),getClassIdByName("class1"));
		registerStudentToClass(regstud1);
		DBregisteredstudent regstud2 = new DBregisteredstudent(getUserId("student1"),getClassIdByName("class2"));
		registerStudentToClass(regstud2);
		DBregisteredstudent regstud3 = new DBregisteredstudent(getUserId("student1"),getClassIdByName("class4"));
		registerStudentToClass(regstud3);
		DBregisteredstudent regstud4 = new DBregisteredstudent(getUserId("student4"),getClassIdByName("class2"));
		registerStudentToClass(regstud4);
		DBregisteredstudent regstud5 = new DBregisteredstudent(getUserId("student4"),getClassIdByName("class3"));
		registerStudentToClass(regstud5);
		DBregisteredstudent regstud6 = new DBregisteredstudent(getUserId("student4"),getClassIdByName("class4"));
		registerStudentToClass(regstud6);
		DBregisteredstudent regstud7 = new DBregisteredstudent(getUserId("student2"),getClassIdByName("class1"));
		registerStudentToClass(regstud7);
		DBregisteredstudent regstud8 = new DBregisteredstudent(getUserId("student2"),getClassIdByName("class2"));
		registerStudentToClass(regstud8);
		DBregisteredstudent regstud9 = new DBregisteredstudent(getUserId("student3"),getClassIdByName("class4"));
		registerStudentToClass(regstud9);
		DBregisteredstudent regstud10 = new DBregisteredstudent(getUserId("student5"),getClassIdByName("class3"));
		registerStudentToClass(regstud10);
		DBregisteredstudent regstud11 = new DBregisteredstudent(getUserId("student5"),getClassIdByName("class4"));
		registerStudentToClass(regstud11);
		DBregisteredstudent regstud12 = new DBregisteredstudent(getUserId("student6"),getClassIdByName("class2"));
		registerStudentToClass(regstud12);
		
		
		//create competence structure for teacher1
		DBcompetence com1 = new DBcompetence("C1","desc. C1",getUserId("teacher1"),Visibility.ALL);
		addNewCompetence(com1);
		DBcompetence com2 = new DBcompetence("C2","desc. C2",getUserId("teacher1"),Visibility.ALL);
		addNewCompetence(com2);
		DBcompetence com3 = new DBcompetence("C3","desc. C3",getUserId("teacher1"),Visibility.ALL);
		addNewCompetence(com3);
		DBcompetence com4 = new DBcompetence("C4","desc. C4",getUserId("teacher1"),Visibility.NOTALL);
		addNewCompetence(com4);
		DBcompetence com5 = new DBcompetence("C5","desc. C5",getUserId("teacher1"),Visibility.NOTALL);
		addNewCompetence(com5);
		DBcompetence com6 = new DBcompetence("C6","desc. C6",getUserId("teacher1"),Visibility.NOTALL);
		addNewCompetence(com6);
		
		DBcompetencestructure competencestructure = new DBcompetencestructure("CS1","desc. CS1",getUserId("teacher1"),Visibility.ALL);
		addNewCompetenceStructure(competencestructure);
		
		DBcompetenceweight wei1 = new DBcompetenceweight(getCstructureIdByName("CS1"),getCompetenceIdByName("C1"),getCompetenceIdByName("C3"),0.3);
		addNewCompetenceWeight(wei1);
		DBcompetenceweight wei2 = new DBcompetenceweight(getCstructureIdByName("CS1"),getCompetenceIdByName("C2"),getCompetenceIdByName("C3"),0.4);
		addNewCompetenceWeight(wei2);
		DBcompetenceweight wei3 = new DBcompetenceweight(getCstructureIdByName("CS1"),getCompetenceIdByName("C3"),getCompetenceIdByName("C5"),0.35);
		addNewCompetenceWeight(wei3);
		DBcompetenceweight wei4 = new DBcompetenceweight(getCstructureIdByName("CS1"),getCompetenceIdByName("C4"),getCompetenceIdByName("C5"),0.45);
		addNewCompetenceWeight(wei4);
		
		//create tasks for teacher1
		DBtask task1 = new DBtask("task1","desc1","frage1","antw1",getUserId("teacher1"),Visibility.ALL);
		addNewTask(task1);
		DBtask task2 = new DBtask("task2","desc2","frage2","antw2",getUserId("teacher1"),Visibility.ALL);
		addNewTask(task2);
		DBtask task3 = new DBtask("task3","desc3","frage3","antw3",getUserId("teacher1"),Visibility.ALL);
		addNewTask(task3);
		DBtask task4 = new DBtask("task4","desc4","frage4","antw4",getUserId("teacher1"),Visibility.ALL);
		addNewTask(task4);
		DBtask task5 = new DBtask("task5","desc5","frage5","antw5",getUserId("teacher1"),Visibility.ALL);
		addNewTask(task5);
		
		//link tasks and competences
		DBlinkagetaskcompetence ltc1 = new DBlinkagetaskcompetence(getTaskIdByName("task1"),getCompetenceIdByName("C1"),1);
		addNewTaskCompetenceLink(ltc1);
		DBlinkagetaskcompetence ltc2 = new DBlinkagetaskcompetence(getTaskIdByName("task2"),getCompetenceIdByName("C2"),1);
		addNewTaskCompetenceLink(ltc2);
		DBlinkagetaskcompetence ltc3 = new DBlinkagetaskcompetence(getTaskIdByName("task3"),getCompetenceIdByName("C3"),1);
		addNewTaskCompetenceLink(ltc3);
		DBlinkagetaskcompetence ltc4 = new DBlinkagetaskcompetence(getTaskIdByName("task4"),getCompetenceIdByName("C4"),1);
		addNewTaskCompetenceLink(ltc4);
		DBlinkagetaskcompetence ltc5 = new DBlinkagetaskcompetence(getTaskIdByName("task5"),getCompetenceIdByName("C5"),1);
		addNewTaskCompetenceLink(ltc5);
		
		//link class with cstructure
		DBlinkageclasscstructure lcc = new DBlinkageclasscstructure(getCstructureIdByName("CS1"),getClassIdByName("class1"));
		addNewClassCstructureLink(lcc);
		 
		//link task with class
		DBlinkageclasstask lct1 = new DBlinkageclasstask(getTaskIdByName("task1"),getClassIdByName("class1"));
		addNewClassTaskLink(lct1);
		DBlinkageclasstask lct2 = new DBlinkageclasstask(getTaskIdByName("task2"),getClassIdByName("class1"));
		addNewClassTaskLink(lct2);
		DBlinkageclasstask lct3 = new DBlinkageclasstask(getTaskIdByName("task3"),getClassIdByName("class1"));
		addNewClassTaskLink(lct3);
		DBlinkageclasstask lct4 = new DBlinkageclasstask(getTaskIdByName("task4"),getClassIdByName("class1"));
		addNewClassTaskLink(lct4);
		DBlinkageclasstask lct5 = new DBlinkageclasstask(getTaskIdByName("task5"),getClassIdByName("class1"));
		addNewClassTaskLink(lct5);
	}
	
}
