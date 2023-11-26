package tools.gnzlz.database.autocode;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.Struct;
import java.util.Date;

public class ACFormat {

	public static String imports(String type){
		type = type.split(" ")[0];
		if (type.equalsIgnoreCase("NUMERIC")) {
			return "import " + BigDecimal.class.getPackage().getName() + "." + BigDecimal.class.getSimpleName() + ";" + System.lineSeparator();
		} else if(type.equalsIgnoreCase("DATETIME") || type.equalsIgnoreCase("DATE") || type.equalsIgnoreCase("TIME") || type.equalsIgnoreCase("TIMESTAMP")) {
			return "import " + Date.class.getPackage().getName() + "." + Date.class.getSimpleName() + ";" + System.lineSeparator();
		} else if(type.equalsIgnoreCase("CLOB")) {
			return "import " + Clob.class.getPackage().getName() + "." + Clob.class.getSimpleName() + ";" + System.lineSeparator();
		} else if(type.equalsIgnoreCase("BLOB")) {
			return "import " + Blob.class.getPackage().getName() + "." + Blob.class.getSimpleName() + ";" + System.lineSeparator();
		} else if(type.equalsIgnoreCase("ARRAY")) {
			return "import " + Array.class.getPackage().getName() + "." + Array.class.getSimpleName() + ";" + System.lineSeparator();
		} else if(type.equalsIgnoreCase("REF")) {
			return "import " + Ref.class.getPackage().getName() + "." + Ref.class.getSimpleName() + ";" + System.lineSeparator();
		} else if(type.equalsIgnoreCase("STRUCT")){
			return"import "+Struct.class.getPackage().getName()+"."+Struct.class.getSimpleName()+";"+System.lineSeparator();
		}
		return "";
	}
}
