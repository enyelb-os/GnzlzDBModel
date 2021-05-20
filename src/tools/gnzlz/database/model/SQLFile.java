package tools.gnzlz.database.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SQLFile {
	
	private String[] script;
	
	public static SQLFile file(String name) {
		return new SQLFile(name,false);		
	}
	
	public static SQLFile internal(String name) {
		return new SQLFile(name,true);		
	}
	
	private SQLFile(String name, boolean internal) {
		script = inputStreamToStrings(name+".sql", internal);
	}
	
	private String[] inputStreamToStrings(String url, boolean internal) {
		
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			InputStream is = internal ? getClass().getResourceAsStream(url) : new FileInputStream(new File(url));
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line).append(System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString().split(";"); 
	}
	
	public String[] script() {
		return script;
	}
}
