package dbentities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@Root(name="user")
public class DBuser extends DBentity {

	public int userid;
	public String password;
	public int creator;
	
	@Element(name="usergroup")
	public int usergroup;
	
	@Element(name="name")
	public String name;
	
	public DBuser(){}
	
	public DBuser(String name, String password, int usergroup, int creatorId){
		this.name = name;
		this.password=password;
		this.creator = creatorId;
		this.usergroup = usergroup;
	}
	
	public DBuser(Document doc){
		if(doc.getElementsByTagName("name").getLength()>0)
			this.name = doc.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
		if(doc.getElementsByTagName("password").getLength()>0)
			this.password = doc.getElementsByTagName("password").item(0).getFirstChild().getNodeValue();
		if(doc.getElementsByTagName("usergroup").getLength()>0)
			this.usergroup = Integer.parseInt(doc.getElementsByTagName("usergroup").item(0).getFirstChild().getNodeValue());
	}
}

