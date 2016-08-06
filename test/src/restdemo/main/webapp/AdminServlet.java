package restdemo.main.webapp;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class AdminServlet extends HttpServlet 
{

  protected static String propertyfilelocation_ = "webapps/restdemo/WEB-INF/data/restdemo.properties";
  
  protected static Properties properties_;
  
  public void init (ServletConfig servletconfig) throws ServletException 
  {
    // do some initialisation
    
    try
    {
      properties_ = new Properties ();

      File propsfile = new File (propertyfilelocation_);    
      FileReader freader = new FileReader (propsfile);
      properties_.load (freader);
      freader.close ();
    }
    catch (Exception exception)
    {
      // ...
    }
    
  }
  
  public void destroy()
  {
    // do destroy stuff
  }
  
  
  public void doGet (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
  {
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    // general information 

    // compod version
    String propkey = req.getParameter ("propkey");
    
    String propvalue = properties_.getProperty (propkey);
    
    out.println ("propvalue: " + propvalue);
  }  


}