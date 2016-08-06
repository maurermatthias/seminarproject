package restdemo.main.webapp;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/")
public class RestInterface
{
  
  protected static int counter_ = 0;
  
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("sayhello")
  public String sayHello () 
  {
    return "Hello Universe. (" + counter_++ + ")";
  }


  @GET
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Path("echoget")
  public String echoGet (@QueryParam("data") String data)
  {
    return data;
  }
  

  
  @POST
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Path("echopost")
  public String echoPost (@FormParam("data") String data)
  {
    return data;
  }
  


  
}