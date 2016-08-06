package loading;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//Class for reading html file and returning string
public class FileLoader {

	//singelton instance
	private static FileLoader instance;
	
	//private constructor
	private FileLoader() {
	}
	
	//singelton getter
	public static FileLoader getInstance() {
		if (instance == null) {
			instance = new FileLoader();
		}
		return instance;
	}
	
	//loading a html file
	public static String getHTMLFile(String filename) throws IOException
	{
	    String content = null;
	    File file = new File(filename); //for ex foo.txt
	    FileReader reader = null;
	    try {
	        reader = new FileReader(file);
	        char[] chars = new char[(int) file.length()];
	        reader.read(chars);
	        content = new String(chars);
	        reader.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if(reader !=null){reader.close();}
	    }
	    return content;
	}
	 
	//save to desktop
	public static void writeFile(String canonicalFilename, String text) throws IOException
	{
	  File file = new File (canonicalFilename);
	  BufferedWriter out = new BufferedWriter(new FileWriter(file)); 
	  out.write(text);
	  out.close();
	}
	
}
