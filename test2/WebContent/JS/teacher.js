function Teacher() {
	this.tableDiv;
	this.structureName="";
	this.structureDescribtion='Enter describtion of the competence structure here.';
	this.nodeDiv;
	this.addStudent = function(name, pwd){
		doPost(sessionInformation.postnewStudentUrlIdentificated()+"&studentName="+name+"&studentPwd="+pwd,"",function(msg){
			alert(msg);
		});
	}
	this.selectedMenu=1;
	this.registeredStudents=[];
	this.existingStructureNames = [];
	this.competenceList=[];
	this.createdTasks=[];
	this.loadExistingStructureNames = function(){
		doGet(sessionInformation.getStoredCompetenceStructureNamesUrlIdentificated(), function(msg){
			teacher.existingStructureNames=msg.split("&");
			if(document.getElementById('selectCompStruct')){
				var html ='<option value="load">Load Competence Structure</option>';
				for(var i=0; i<teacher.existingStructureNames.length;i++){
					html+='<option value="'+teacher.existingStructureNames[i]+'">'+teacher.existingStructureNames[i]+"</option>";
				}
				document.getElementById('selectCompStruct').innerHTML = html;
			}
		});
	};
	this.addCompetence = function(name){
		if(name != null){
			com = new Competence(name);
			this.competenceList.push(com);
		}else{
			var newName='new Competence';
			var addOn = 1;
			while(this.getCompetenceByName(newName + addOn)!=null){
				addOn +=1;
			};
			com = new Competence(newName+addOn);
			this.competenceList.push(com);
		}
	};
	this.getCompetenceByName = function(name){
		for(var i=0;i<this.competenceList.length;i++){
			if(this.competenceList[i].name==name){
				return(this.competenceList[i]);
			};
		};
		return(null);
	};
	this.deleteCompetenceByName = function(name){
		for(var i =0;i<this.competenceList.length; i++){
			if(this.competenceList[i].name==name){
				this.competenceList.splice(i,1);
				break;
			};
		};
	};
	this.drawCompetenceTableHTML = function(divId){
		this.tableDiv = divId;
		var html = '<div class="teacher-information">';
		html += '<table>';
		html += '<tr><th class="whiteText">Competences</th></tr>';
		for(var i = 0; i<this.competenceList.length;i++){
			html += '<tr><td><input id="teacher-changeCompetence-'+i+'" type="text" value="' + this.competenceList[i].name + '"></td></tr>';
		};
		html += '<tr><td><input class="centered" id="teacher-addCompetence" type="text" value="+" readonly></td></tr>';
		html += '</table>';
		document.getElementById(divId).innerHTML = html+'</div>';
		
		//set listeners
		for(var i = 0; i<this.competenceList.length;i++){
			document.getElementById('teacher-changeCompetence-'+i).addEventListener("change", function(){
				if(teacher.getCompetenceByName(this.value)==null){
					if(teacher.checkName(this.value)){
						teacher.changeCompetenceName(this.id.substring(25,this.id.length),this.value)
					}else{
						sessionInformation.printAlert("Competence names are only allowed to contain letters a-z,A-Z; numbers 0-9 and the sign -!",3000);
					}
				}
				teacher.redrawCompetenceTable();
				this.click();
			});
			document.getElementById('teacher-changeCompetence-'+i).addEventListener("click", function(){
				if(teacher.getCompetenceByName(this.value)!=null){
					teacher.drawCompetenceInformationHTML(this.value,'teacher-competenceInformation')
				}
			});
		};
		document.getElementById('teacher-addCompetence').addEventListener("click", function(){
			teacher.addCompetence();
			teacher.redrawCompetenceTable();
		});
		
	};
	this.changeCompetenceName = function(line,newName){
		this.competenceList[parseInt(line)].name=newName;
	};
	this.redrawCompetenceTable = function(){
		this.drawCompetenceTableHTML(this.tableDiv);
	};
	this.drawCompetenceInformationHTML = function(nodeName,divId){
		this.nodeDiv = divId;
		this.getCompetenceByName(nodeName).drawInformation(divId);
	};
	this.drawTeacherScreen = function(){
		if(this.selectedMenu==1){
			//overview
			document.getElementById(sessionInformation.bodyDiv).innerHTML = '<div class="whiteText"> How to use this application... </div>';
		}else if(this.selectedMenu==2){
			//competence structures
			teacher.loadExistingStructureNames();
			document.getElementById(sessionInformation.bodyDiv).innerHTML = '<div class="teacher-div" id="teacher-save"></div><div class="teacher-div" id="teacher-competenceList"></div><div class="teacher-div" id="teacher-competenceInformation"></div>';
			this.drawCompetenceTableHTML('teacher-competenceList');
			this.drawSaveMenu('teacher-save');
		}else if(this.selectedMenu==3){
			//classes
			document.getElementById(sessionInformation.bodyDiv).innerHTML = '<div class="whiteText"> Assign tasks and students to classes. </div>';
		}else if(this.selectedMenu==4){
			//students
			teacher.loadRegisteredStudents();
			document.getElementById(sessionInformation.bodyDiv).innerHTML = '<div id="teacherStudentOverview" class="teacher-div" > Work on students. </div><div  class="teacher-div"  id="teacherStudentLevelTwoDiv"></div><div  class="teacher-div" id="teacherStudentLevelThreeDiv"></div>';
			this.drawStundentOverviewTable('teacherStudentOverview');
		}else if(this.selectedMenu=5){
			//tasks
			teacher.loadCreatedTasks('teacherTaskOverview');
			document.getElementById(sessionInformation.bodyDiv).innerHTML = '<div id="teacherTaskOverview" class="teacher-div" > Work on students. </div><div  class="teacher-div"  id="teacherTaskLevelTwoDiv"></div>';
		}else{
			alert("not implementd yet!");
		}
	};
	this.loadRegisteredStudents = function(){
		doGet(sessionInformation.getRegisteredStudentsForTeacherUrlIdentificated(),function(msg){
			teacher.registeredStudents=msg.split("&");
			if(document.getElementById('teacherStudentOverview')){
				teacher.drawStundentOverviewTable('teacherStudentOverview');
			}
		});
	};
	this.loadCreatedTasks = function(){
		doGet(sessionInformation.getCreatedTasksForTeacherUrlIdentificated(),function(msg){
			teacher.createdTasks=msg.split("&");
			if(document.getElementById('teacherTaskOverview')){
				teacher.drawTaskOverviewTable('teacherTaskOverview');
			}
		});
	}
	this.drawTaskOverviewTable = function(div){
		html='<div class="teacher-information"><table>';
		html+='<tr><th><h3>created tasks</h3></th></tr>';
		for(var i=0;i<this.createdTasks.length;i++){
			html+='<tr><td id="teacher-createdTasks-'+this.createdTasks[i]+'">'+this.createdTasks[i]+'</td></tr>';
		}
		html += '<tr><td id="teacher-creatTask">+</td></tr>';
		html += "</table></div>";
		document.getElementById(div).innerHTML = html;

		for(var i=0;i<this.createdTasks.length;i++){
			document.getElementById('teacher-createdTasks-'+this.createdTasks[i]).onclick = function(){
				var taskSelected = this.id.substring(21,this.id.length);
				teacher.drawteacherTaskLevelTwo(taskSelected);
			};
		}
		
		document.getElementById("teacher-creatTask").onclick = function(){
			teacher.drawteacherTaskLevelTwo();
		};
	};
	this.drawteacherTaskLevelTwo = function(taskSelected){
		if(taskSelected==null){
			var html ='<div class="teacher-information">';
			html += '<p>name:</p><input type="text" id="teacherTaskNewName">';
			html += '<p>task message:</p><textarea id="teacherTaskNewMessage" rows="4" cols="40"></textarea>';
			html += '<p><input type="button" id="teacherTaskNewTaskButton" value="add task"></p>';
			html += '</div>';
			document.getElementById('teacherTaskLevelTwoDiv').innerHTML = html;
			
			document.getElementById("teacherTaskNewTaskButton").onclick = function(){
				var idstring = document.getElementById("teacherTaskNewName").value;
				var text = document.getElementById("teacherTaskNewMessage").value;
				if(teacher.createdTasks.indexOf(idstring)==-1){
					doPost(sessionInformation.postNewTaskUrlIdentificated()+"&idstring="+idstring,text,function(msg){
						if(msg=="OK"){
							sessionInformation.printInformation("Task stored.",3000);
							teacher.drawTeacherScreen();
						}
						else
							sessionInformation.printAlert("Task couldn't be stored.",3000);
					});
				}else{
					sessionInformation.printAlert("Task-name already in use!",3000);
				}
			};
			
		}else{
			var html ='<div class="teacher-information">';
			html += '<h3>'+taskSelected+'</h3>';
			html += '<p>task message:</p><textarea id="teacher-task-message-'+taskSelected+'" rows="4" cols="40">still to fill</textarea>'
			html += '<p><input type="button" value="delete task" id="teacherDeleteTaskButton-'+taskSelected+'"></p>';
			html += '</div>';
			document.getElementById('teacherTaskLevelTwoDiv').innerHTML = html;
			
			document.getElementById('teacherDeleteTaskButton-'+taskSelected).onclick = function(){
				var taskid = this.id.substring(24,this.id.length);
				doPost(sessionInformation.postDeleteTaskUrlIdentificated()+"&taskid="+taskid,"",function(msg){
					if(msg=="OK"){
						sessionInformation.printInformation("Task deleted.",3000);
						teacher.drawTeacherScreen();
					}
					else
						sessionInformation.printAlert("Task couldn't be deleted.",3000);
				});
				teacher.drawTeacherScreen();
			};
		}
	};
	this.dropdownFunction = function(){
		document.getElementById("myDropdown").classList.toggle("show");
	};
	this.drawStundentOverviewTable = function(div){
		html='<div class="teacher-information"><table>';
		html+='<tr><th><h3>registered students</h3></th></tr>';
		for(var i=0;i<this.registeredStudents.length;i++){
			html+='<tr><td id="teacher-registeredStudents-'+this.registeredStudents[i]+'">'+this.registeredStudents[i]+'</td></tr>';
		}
		html += '<tr><td id="teacher-registerStudent">+</td></tr>';
		html += "</table></div>";
		document.getElementById(div).innerHTML = html;
		
		for(var i=0;i<this.registeredStudents.length;i++){
			document.getElementById('teacher-registeredStudents-'+this.registeredStudents[i]).onclick = function(){
				var studentSelected = this.id.substring(27,this.id.length);
				teacher.drawStudentLevelTwoDiv(studentSelected);
			};
		}
		
		document.getElementById("teacher-registerStudent").onclick = function(){
			teacher.drawStudentLevelTwoDiv();
		};

	};
	this.drawStudentLevelTwoDiv = function(name){
		if(name==null){
			var html='<div class="teacher-information">';
			html += '<h3> add a new student </h3>';
			html += '<p> name: </p><input type="text" value="" id="teacherStudentAddnewStudentName">';
			html += '<p> password: </p><input type="text" value="" id="teacherStudentAddnewStudentPassword">';
			html += '<p><input type="button" value="add student" id="teacherstudentaddstudentbutton"></p>'
			document.getElementById("teacherStudentLevelTwoDiv").innerHTML = html+'</div>';
			
			document.getElementById("teacherstudentaddstudentbutton").onclick = function(){
				var name= document.getElementById('teacherStudentAddnewStudentName').value;
				var pwd= document.getElementById('teacherStudentAddnewStudentPassword').value;
				//alert("new "+name+"/"+pwd);
				if(teacher.registeredStudents.indexOf(name)==-1){
					doPost(sessionInformation.postnewStudentUrlIdentificated()+"&studentName="+name+"&studentPwd="+pwd,"",function(msg){
						if(msg=="OK"){
							sessionInformation.printInformation("New student added!",3000);
							teacher.registeredStudents.push(document.getElementById('teacherStudentAddnewStudentName').value);
							teacher.drawTeacherScreen();
						}else if(msg=="FAIL"){
							sessionInformation.printAlert("Student couldn't be added!",3000);
						}
				});
				}else{
					sessionInformation.printAlert("Student already exists.",3000);
				}

			};
		}else{
			var html='<div class="teacher-information">';
			html += '<h3 id="teacherstudentname">'+name+'</h3>';
			html += '<input type="button" value="delete student" id="teacherstudentdeletestudent">';
			document.getElementById("teacherStudentLevelTwoDiv").innerHTML = html+'</div>';
			
			document.getElementById("teacherstudentdeletestudent").onclick = function(){
				var name= document.getElementById('teacherstudentname').innerHTML;
				doPost(sessionInformation.postDeleteStudentUrlIdentificated()+"&studentName="+name,"",function(msg){
					if(msg=="OK"){
						sessionInformation.printInformation("Student deleted!",3000);
						teacher.registeredStudents.splice(teacher.registeredStudents.indexOf(name),1);
						teacher.drawTeacherScreen();
					}else if(msg=="FAIL"){
						sessionInformation.printAlert("Student couldn't be deleted!",3000);
					}
				});
			};
		}
	};
	this.exampleFill = function(){
		this.addCompetence("C1");
		this.addCompetence("C2");
		this.addCompetence("C3");
		this.addCompetence("C4");
		this.addCompetence("C5");
		this.addCompetence("C6");
		this.addPrerequisite("C6","C5",0.3); 
		this.addPrerequisite("C6","C4",0.3); 
		this.addPrerequisite("C4","C3",0.3); 
		this.addPrerequisite("C5","C2",0.3); 
		this.addPrerequisite("C5","C1",0.3); 
	};
	this.addPrerequisite = function(from,to,weight){
		pre = new Prerequisite();
		pre.weight = weight;
		pre.competence = this.getCompetenceByName(to);
		this.getCompetenceByName(from).prerequisites.push(pre);
	}
	this.toXML = function(){
		var xml = '<competenceStructure>';
		xml += '<name>'+this.structureName+'</name>';
		xml += '<description>'+this.structureDescribtion+'</description>';
		xml += '<competenceList>';
		for(var i=0; i<this.competenceList.length;i++){
			xml += '<competence>';
			xml += this.competenceList[i].toXML();
			xml += '</competence>';
		}
		return(xml+'</competenceList></competenceStructure>');
	};
	this.fromXML = function(xmlString){
		var parseXml;
		
		if (typeof window.DOMParser != "undefined") {
		    parseXml = function(xmlStr) {
		        return ( new window.DOMParser() ).parseFromString(xmlStr, "text/xml");
		    };
		} else if (typeof window.ActiveXObject != "undefined" &&
		       new window.ActiveXObject("Microsoft.XMLDOM")) {
		    parseXml = function(xmlStr) {
		        var xmlDoc = new window.ActiveXObject("Microsoft.XMLDOM");
		        xmlDoc.async = "false";
		        xmlDoc.loadXML(xmlStr);
		        return xmlDoc;
		    };
		} else {
		    throw new Error("No XML parser found");
		}
		
		var xml = parseXml(xmlString);
		for(var i=0; i<xml.documentElement.childNodes.length;i++){
			if(xml.documentElement.childNodes[i].nodeName=="name"){
				teacher.structureName=xml.documentElement.childNodes[i].childNodes[0].nodeValue;
			}else if(xml.documentElement.childNodes[i].nodeName=="description"){
				teacher.structureDescribtion=xml.documentElement.childNodes[i].childNodes[0].nodeValue;
			}else if(xml.documentElement.childNodes[i].nodeName=="competenceList"){
				teacher.competenceList = [];
				//alert(xml.documentElement.childNodes[i].childNodes[0].childNodes[0].nodeName);
				var competences = xml.documentElement.childNodes[i].childNodes;
				for(var k=0; k< competences.length;k++){
					var competenceName ="";
					var competenceDescribtion ="";
					for(var j=0; j<competences[k].childNodes.length; j++){
						if(competences[k].childNodes[j].nodeName=="name"){
							competenceName = competences[k].childNodes[j].childNodes[0].nodeValue;
						}else if(competences[k].childNodes[j].nodeName=="description"){
							competenceDescribtion = competences[k].childNodes[j].childNodes[0].nodeValue;
						}
					}
					com = new Competence(competenceName);
					com.describtion = competenceDescribtion;
					teacher.competenceList.push(com);
				}
				for(var k=0; k< competences.length;k++){
					var competenceName ="";
					var prerequisiteList = [];
					for(var j=0; j<competences[k].childNodes.length; j++){
						if(competences[k].childNodes[j].nodeName=="name"){
							competenceName = competences[k].childNodes[j].childNodes[0].nodeValue;
						}else if(competences[k].childNodes[j].nodeName=="prerequisiteList"){
							var prerequisites = competences[k].childNodes[j].childNodes;
							for(var c=0; c<prerequisites.length;c++){
								var prerequisiteName = "";
								var prerequisiteWeight = 0;
								for(var t=0; t<prerequisites[c].childNodes.length;t++){
									if(prerequisites[c].childNodes[t].nodeName=="name"){
										prerequisiteName=prerequisites[c].childNodes[t].childNodes[0].nodeValue;
									}else if(prerequisites[c].childNodes[t].nodeName=="weight"){
										prerequisiteWeight= prerequisites[c].childNodes[t].childNodes[0].nodeValue;
									}
								}
								teacher.addPrerequisite(competenceName,prerequisiteName,prerequisiteWeight);
							}
						}
					}
				}
			}
		}

		reloadPage();
	};
	this.saveXML = function(){
		doPost(sessionInformation.postCompetenceStructureUrlIdentificated(),this.toXML(),function(returnValue){
			if(returnValue=="OK"){
				teacher.existingStructureNames.push(teacher.structureName);
				document.getElementById('selectCompStruct').innerHTML += '<option value="'+teacher.structureName+'">'+teacher.structureName+'</option>';
				sessionInformation.printInformation("Data successfully stored!",3000);
			}else{
				sessionInformation.printAlert("Data could not be stored!",3000);
			}
		});
	};
	this.loadXML = function(name){
		document.getElementById('selectCompStruct').value='load';
		doGet(sessionInformation.getCompetenceStructureUrlIdentificated(name),function(msg){
			teacher.fromXML(msg);
			});
	}
	this.drawSaveMenu = function(div){
		var html ='<div class="teacher-information">';
		html += '<p class="whiteText">Save Competence Structure as:</p> <input id="teacher-saveName" size="14" type="text" value="'+this.structureName+'"><input id="teacher-saveComp" type="button" value="save"><input id="teacher-deleteComp" type="button" value="delete">';
		html += '<p class="whiteText">Load Competence Structure:</p><select id="selectCompStruct" onchange="teacher.loadXML(this.value)"><option value="load">Load Competence Structure</option>';
		for(var i=0; i<teacher.existingStructureNames.length;i++){
			html+='<option value="'+teacher.existingStructureNames[i]+'">'+teacher.existingStructureNames[i]+"</option>";
		}
		html += '</select>';
		html += '<p class="whiteText">Description:</p><textarea id="teacher-description" rows="4" cols="40">'+this.structureDescribtion+'</textarea>';
		document.getElementById(div).innerHTML = html+'</div>';
		
		document.getElementById("teacher-deleteComp").onclick = function(){
			if(teacher.structureName!=null && teacher.structureName!=""){
				if(teacher.existingStructureNames.indexOf(teacher.structureName) != -1){
					doPost(sessionInformation.postDeleteCompetenceStructureUrlIdentificated(),null,function(msg){
						if(msg=="OK"){
							teacher.structureName="";
							teacher.structureDescribtion="";
							teacher.competenceList = [];
							reloadPage();
							sessionInformation.printInformation("Competence Structure deleted!",3000);
						}else{
							sessionInformation.printAlert("It was not possible to delete the competence structure.",3000);
						}
						
					})
				}else{
					sessionInformation.printAlert("This structure can not be deleted - it is not stored on the server!",3000);
				}
			}
		}
		
		document.getElementById("teacher-saveComp").onclick = function(){
			if(teacher.structureName!=null && teacher.structureName!=""){
				if(teacher.checkName(teacher.structureName)){
					var override = true;
					if(teacher.existingStructureNames.indexOf(teacher.structureName) != -1){
						override = confirm("Structure already exists!\n Override?");
					}
					if(override){
						teacher.saveXML();
					}
				}else{
					sessionInformation.printAlert('Names are only allowed to contain letters a-z,A-Z; numbers 0-9 and the sign -!',3000);
				}
			}else{
				sessionInformation.printAlert('Enter a valid name to store the Structure!',3000);
			}
		};
		document.getElementById('teacher-saveName').addEventListener("change", function(){
			teacher.structureName = this.value;
		});
		document.getElementById('teacher-description').addEventListener("change", function(){
			this.structureDescribtion = this.value;
		});
	};
	this.checkName = function(name){
		for(var i=0; i<name.length;i++){
			if("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ- ".indexOf(name[i])==-1){
				return(false);
			}
		}
		return(true);
	};
	this.drawMenu = function(div){
		html='<div class="teacher-menu-items" id="teacher-menu-overview">Overview</div>';
		html += '<div class="teacher-menu-items" id="teacher-menu-addCS">competence structures</div>';
		html += '<div class="teacher-menu-items" id="teacher-menu-classes">classes</div>';
		html += '<div class="teacher-menu-items" id="teacher-menu-students">students</div>';
		html += '<div class="teacher-menu-items" id="teacher-menu-tasks">tasks</div>';
		document.getElementById(div).innerHTML = html;
		
		document.getElementById("teacher-menu-overview").addEventListener("click",function(){
			if(teacher.selectedMenu!=1){
				teacher.selectedMenu=1;
				reloadPage();
			}
		});
		document.getElementById("teacher-menu-addCS").addEventListener("click",function(){
			if(teacher.selectedMenu!=2){
				teacher.selectedMenu=2;
				reloadPage();
			}
		});
		document.getElementById("teacher-menu-classes").addEventListener("click",function(){
			if(teacher.selectedMenu!=3){
				teacher.selectedMenu=3;
				reloadPage();
			}
		});
		document.getElementById("teacher-menu-students").addEventListener("click",function(){
			if(teacher.selectedMenu!=4){
				teacher.selectedMenu=4;
				reloadPage();
			}
		});
		document.getElementById("teacher-menu-tasks").addEventListener("click",function(){
			if(teacher.selectedMenu!=5){
				teacher.selectedMenu=5;
				reloadPage();
			}
		});
	}
}

function Competence(name){
	this.menuDiv;
	this.name = name;
	this.prerequisites=[];
	this.description='Enter the description to this competence here.';
	this.getPrerequisiteByName = function(name){
		for(var i = 0; i < this.prerequisites.length; i++){
			if(this.prerequisites[i].competence.name == name){
				return(this.prerequisites[i]);
			}
		}
		return(null);
	};
	this.drawInformation = function(div){
		this.menuDiv = div;
		var html = '<div class="teacher-information"><h1 class="whiteText" id="teacherCompetenceHeading">'+this.name+'</h1>';
		html+='<p class="whiteText">Description:</p>';
		html += '<textarea id="teacher-description-'+this.name+'" rows="4" cols="50">'+this.description+'</textarea>';
		html += '<table>';
		html += '<tr><th class="whiteText">Prerequisite</th><th class="whiteText">weight</th></tr>';
		for(var i =0; i<this.prerequisites.length;i++){
			//<input id="teacher-changeCompetence-'+i+'" type="text" value="' + this.competenceList[i].name + '">
			html += '<tr><td class="whiteText">'+this.prerequisites[i].competence.name+'</td><td><input size="15" class="centered" id="teacher-changePreWeight-'+this.prerequisites[i].competence.name+'" type="text" value="'+ this.prerequisites[i].weight +'"></td>';
		}
		html += '<tr><td>';
		//dropdown start
		html += '<div class="dropdown"><button onclick="teacher.dropdownFunction()" class="dropbtn">add a new prerequisite</button><div id="myDropdown" class="dropdown-content">';
		var unusedPrerequisites = this.getUnusedPrerequisites();
		for(var i = 0; i<unusedPrerequisites.length; i++ ){
			html+='<a id="teacher-addprerequisite-'+unusedPrerequisites[i].name+'" href="#">'+unusedPrerequisites[i].name+'</a>';
		}
		html += '</div></div>';
		//dropdown end
		html += '</td><td></td></tr>';
		html += '</table></div>';
		document.getElementById(div).innerHTML = html;
		
		//set listeners
		for(var i = 0; i < unusedPrerequisites.length; i++){
			document.getElementById('teacher-addprerequisite-'+unusedPrerequisites[i].name).addEventListener("click", function(){
				var activeCompName = document.getElementById("teacherCompetenceHeading").innerHTML;
				pre = new Prerequisite();
				pre.competence = teacher.getCompetenceByName(this.id.substring(24,this.id.length));
				pre.weight = 0;
				teacher.getCompetenceByName(activeCompName).prerequisites.push(pre);
				teacher.getCompetenceByName(activeCompName).redrawInformation();
			});
		}
		
		for(var i =0; i< this.prerequisites.length;i++){
			document.getElementById('teacher-changePreWeight-'+this.prerequisites[i].competence.name).addEventListener("change", function(){
				var val = parseFloat(this.value);
				var activeCompName = document.getElementById("teacherCompetenceHeading").innerHTML;
				if(!isNaN(val) && val==this.value){
					if(val<0){
						sessionInformation.printAlert("Prerequisite weights need to be >= 0!",2500);
					}else{
						teacher.getCompetenceByName(activeCompName).getPrerequisiteByName(this.id.substring(24,this.id.length)).weight = this.value;
					}
					if(!teacher.getCompetenceByName(activeCompName).prerequisitesValid()){
						teacher.getCompetenceByName(activeCompName).getPrerequisiteByName(this.id.substring(24,this.id.length)).weight = 0;
						sessionInformation.printAlert("Sum of prerequisite weights need to be <= 1!",2500);
					}
				}
				teacher.getCompetenceByName(activeCompName).redrawInformation();			
			});
		}
		
		document.getElementById('teacher-description-'+this.name).addEventListener("change", function(){
			var activeCompName = document.getElementById("teacherCompetenceHeading").innerHTML;
			teacher.getCompetenceByName(activeCompName).description = this.value;
			//teacher.getCompetenceByName(activeCompName).redrawInformation();
		});
	};
	this.redrawInformation = function(){
		this.drawInformation(this.menuDiv);
	};
	this.isPrerequisite = function(name){
		for(var i = 0; i<this.prerequisites.length;i++){
			if(this.prerequisites[i].competence.name == name){
				return(true);
			}
		}
		return(false);
	};
	this.getUnusedPrerequisites = function(){
		unusedPrerequisites = [];
		for(var i =0; i < teacher.competenceList.length; i++){
			if(!this.isPrerequisite(teacher.competenceList[i].name) && teacher.competenceList[i].name != this.name){
				unusedPrerequisites.push(teacher.competenceList[i]);
			}
		}
		return(unusedPrerequisites);
	};
	this.prerequisitesValid = function(){
		var sum =0;
		for(var i=0; i < this.prerequisites.length; i++){
			sum += parseFloat(this.prerequisites[i].weight);
		}
		if(sum<=1){
			return(true);
		}
		return(false);
	}
	this.toXML = function(){
		var xml='<name>'+this.name+'</name>';
		xml += '<description>'+this.description+'</description>';
		xml += '<prerequisiteList>';
		for(var i=0; i<this.prerequisites.length;i++){
			xml += '<prerequisite>';
			xml += this.prerequisites[i].toXML();
			xml+= '</prerequisite>';
		}
		xml += '</prerequisiteList>';
		return(xml);
	}
}


// Close the dropdown menu if the user clicks outside of it
window.onclick = function(event) {
  if (!event.target.matches('.dropbtn')) {

    var dropdowns = document.getElementsByClassName("dropdown-content");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains('show')) {
        openDropdown.classList.remove('show');
      }
    }
  }
}


function Prerequisite(){
	this.competence;
	this.weight;
	this.toXML = function(){
		var xml='<weight>'+this.weight+'</weight>';
		xml += '<name>'+this.competence.name+'</name>';
		return(xml);
	};
}