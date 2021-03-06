function SessionInformation(){
	this.menuDiv = 'menudiv';
	this.bodyDiv = 'bodydiv';
	this.user = new User();
	this.basePath = "http://localhost:8080/test2/rest/";
	this.getAccessLevelUrl = this.basePath + "getAccessLevel";
	this.postCompetenceStructureUrl = this.basePath + "saveCompetenceStructure";
	this.getStoredCompetenceStructureNamesUrl =this.basePath + "getStoredCompetenceStructureNames";
	this.getCompetenceStructureUrl = this.basePath + "loadCompetenceStructure";
	this.postDeleteCompetenceStructureUrl = this.basePath + "deleteCompetenceStructure";
	this.postNewStudentUrl = this.basePath + "addStudent";
	this.postNewTaskUrl = this.basePath + "addTask";
	this.postDeleteTaskUrl = this.basePath + "deleteTask";
	this.postDeleteStudentUrl = this.basePath + "deleteStudent";
	this.getRegisteredStudentsForTeacherUrl = this.basePath +"getRegisteredStudentsForTeacher";
	this.getCreatedTasksForTeacherUrl = this.basePath +"getCreatedTasksForTeacher";
	this.getRequestUrlAdding = function(){
		return("?name="+this.user.name+"&pwd="+this.user.pwd);
	};
	this.getAccessLevelUrlIdentificated = function(){
		return(this.getAccessLevelUrl+this.getRequestUrlAdding());
	};
	this.postCompetenceStructureUrlIdentificated = function(){
		return(this.postCompetenceStructureUrl+this.getRequestUrlAdding()+"&structureName="+teacher.structureName);
	};
	this.postDeleteCompetenceStructureUrlIdentificated = function(){
		return(this.postDeleteCompetenceStructureUrl+this.getRequestUrlAdding()+"&structureName="+teacher.structureName);
	};
	this.postnewStudentUrlIdentificated = function(){
		return(this.postNewStudentUrl+this.getRequestUrlAdding());
	};
	this.postDeleteStudentUrlIdentificated = function(){
		return(this.postDeleteStudentUrl+this.getRequestUrlAdding());
	};
	this.postNewTaskUrlIdentificated = function(){
		return(this.postNewTaskUrl+this.getRequestUrlAdding());
	};
	this.postDeleteTaskUrlIdentificated = function(){
		return(this.postDeleteTaskUrl+this.getRequestUrlAdding());
	};
	this.getStoredCompetenceStructureNamesUrlIdentificated = function(){
		return(this.getStoredCompetenceStructureNamesUrl+this.getRequestUrlAdding());
	};
	this.getCompetenceStructureUrlIdentificated = function(structureName){
		return(this.getCompetenceStructureUrl+this.getRequestUrlAdding()+"&structureName="+structureName);
	};
	this.getRegisteredStudentsForTeacherUrlIdentificated = function(){
		return(this.getRegisteredStudentsForTeacherUrl+this.getRequestUrlAdding());
	};
	this.getCreatedTasksForTeacherUrlIdentificated = function(){
		return(this.getCreatedTasksForTeacherUrl+this.getRequestUrlAdding());
	};
	this.doTeacherLogin = function(){
		this.user.name='teacher';
		this.user.pwd='teacher';
		this.user.accessLevel=2;
		//teacher.exampleFill();
		reloadPage();
	};
	this.printAlert = function(msg,time){
		if(time==null)
			time=3000;
		document.getElementById('menu2-1').style.color = "red";
		document.getElementById('menu2-1').innerHTML ='<font size="1">'+msg+'</font>';
		setTimeout(function() { $("#menu2-1")[0].innerHTML=""; }, time);
	}
	this.printInformation = function(msg,time){
		if(time==null)
			time=3000;
		document.getElementById('menu2-1').style.color = "green";
		document.getElementById('menu2-1').innerHTML = '<font size="1">'+msg+'</font>';
		setTimeout(function() { $("#menu2-1")[0].innerHTML=""; }, time);
	}
}

function User(name,pwd) {
	this.name=name;
	this.pwd=pwd;
	this.accessLevel=0;
}