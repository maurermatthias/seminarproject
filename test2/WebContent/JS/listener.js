$( document ).ready(function() {
	loadLoginScreen(false);
	sessionInformation.doTeacherLogin();
});


login = function(){
	sessionInformation.user.pwd=$("#pwd")[0].value;
	sessionInformation.user.name=$("#name")[0].value;
	doGet(sessionInformation.getAccessLevelUrlIdentificated(),loginAction);
}

loginAction=function(text){
	sessionInformation.user.accessLevel=parseInt(text);
	reloadPage();
}

reloadPage=function(){
	if(sessionInformation.user.accessLevel==0){
		reloadNoAccess();
	}else if(sessionInformation.user.accessLevel==1){
		reloadStudent();
	}else if(sessionInformation.user.accessLevel==2){
		reloadTeacher();
	}else if(sessionInformation.user.accessLevel==3){
		reloadAdmin();
	}else{
		$("#bodydiv")[0].innerHTML="<p class='whiteText'>ERROR: unknown access level</p>";
	}
	printLogInOutMenu();
	printMenu('menu1-2');
}

printMenu = function(div){
	if(sessionInformation.user.accessLevel==1){
		
	}else if(sessionInformation.user.accessLevel==2){
		teacher.drawMenu(div);
	}else if(sessionInformation.user.accessLevel==3){
		
	}
}

printLogInOutMenu = function(){
	if(sessionInformation.user.accessLevel==0){
		document.getElementById("menu1-3").innerHTML = '<font size="1">not logged in</font>';
		document.getElementById("menu2-3").innerHTML = '<input id="register" type="button" value="register">';
		
		document.getElementById("register").onclick = function(){
			alert("Registration not implemented yet!");
		}
		
	}else{
		document.getElementById("menu1-3").innerHTML = '<font size="1">logged in as:   </font>'+'<font>'+ sessionInformation.user.name +'</font>';
		document.getElementById("menu2-3").innerHTML = '<input id="logout" type="button" value="logout">';
		
		document.getElementById("logout").onclick = function(){
			sessionInformation.user.accessLevel = 0;
			sessionInformation.user.password = "";
			sessionInformation.user.name = "";
			loadLoginScreen(false);
		}
	}
	
}

reloadNoAccess = function(){
	loadLoginScreen(true);
}

reloadAdmin=function(){
	$("#bodydiv")[0].innerHTML="<p class='whiteText'>access admin</p>";
}

reloadTeacher=function(){
	//teacher.loadExistingStructureNames();
	teacher.drawTeacherScreen();
}

reloadStudent=function(){
	$("#bodydiv")[0].innerHTML="<p class='whiteText'>access student</p>";
}

loadLoginScreen = function(showLoginFailMessage){
	var html="";
	html+='<div id="box">';
	html+='<form action="javascript:login();"><table><tr><td align="right"><p id="text">Username:</p></td>';
	html+='<td align="left"><input type="text" name="name" id="name"></td></tr><tr><td align="right"><p id="text">Password:</p></td>';
	html+='<td align="left"><input type="password" name="pwd" id="pwd"></td></tr><tr><td align="right"><input type="submit" value="Log In"></td>';
	html+='<td align="left"><input type="reset" value="Reset">';
	html+='</td></tr></table></form>';
	html+='</div>';
	$("#bodydiv")[0].innerHTML=html;
	if(showLoginFailMessage)
		sessionInformation.printAlert("Login Failed! - Wrong Password or Username.",5000);
}