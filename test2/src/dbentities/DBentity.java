package dbentities;

import java.io.StringWriter;
import java.io.Writer;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class DBentity {
	public String toXML(){
		Serializer serializer = new Persister();
		Writer writer = new StringWriter();
		try {
			serializer.write(this, writer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error when serializing DBtask.");
			e.printStackTrace();
		}
		return writer.toString();
	};
}
