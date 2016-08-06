  function doGet (url,returnMethod)
  {
    // send xml string
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange=function()
    {
      //alert ("onreadystatechange: state: " + xmlhttp.readyState + ", status: " + xmlhttp.status);
      if (xmlhttp.readyState==4)
      {
        var restultvalue = xmlhttp.responseText;
        returnMethod (restultvalue);
      }
    }

    xmlhttp.open("GET",url,true);
    xmlhttp.setRequestHeader("content-type","text/plain");
    xmlhttp.send();
  } 
  
  function doPost(url,postdata,returnMethod)
  {
    // send xml string
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange=function()
    {
      //alert ("onreadystatechange: state: " + xmlhttp.readyState + ", status: " + xmlhttp.status);
      if (xmlhttp.readyState==4)
      {
        var restultvalue = xmlhttp.responseText;
        returnMethod(restultvalue);
      }
    }

    xmlhttp.open("POST",url,true);
    xmlhttp.setRequestHeader("content-type","text/plain"); //application/x-www-form-urlencoded
    xmlhttp.send(postdata);
  }   