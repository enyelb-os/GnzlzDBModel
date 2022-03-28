package tools.gnzlz.database.autocode;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.Struct;
import java.util.Date;

public class ACFormat {

	public static String beginValidNumber(String s) {
		boolean digit = false;
		for (int i = 0 ; i < s.length(); i++){
			if(Character.isDigit(s.charAt(i))){
				digit = true;
				if(i == s.length()-1)
					return "n"+s;
			}else{
				if(digit){
					return s.substring(i) + s.substring(0,i);
				}
				break;
			}
		}
		return s;
	}

	public static String camelCaseClass(String s) {
		s = s.toLowerCase();
		String temp[] = s.split("_");
		String newString = "";
		for (String string : temp) {
			newString = newString + Character.toUpperCase(string.charAt(0)) + string.substring(1, string.length());
		}
		
		return newString;
	}
	
	public static String camelCaseMethod(String s) {
		s = s.toLowerCase();
		String temp[] = s.split("_");
		String newString = "";
		for (int i = 0; i < temp.length; i++) {
			if(i==0) newString = newString + Character.toLowerCase(temp[i].charAt(0)) + temp[i].substring(1, temp[i].length());
			else newString = newString + Character.toUpperCase(temp[i].charAt(0)) + temp[i].substring(1, temp[i].length());
		}
		
		return newString;
	}
	
	public static String typeData(String type){
		String split[] = type.split(" ");
		type = split == null ? type : split[0];
		if(type.equalsIgnoreCase("INTEGER") || type.equalsIgnoreCase("INT"))
			return "int";
		if(type.equalsIgnoreCase("TINYINT")) 
			return "byte";
		if(type.equalsIgnoreCase("BOOLEAN") || type.equalsIgnoreCase("BIT"))
			return "boolean";
		if(type.equalsIgnoreCase("SMALLINTEGER") || type.equalsIgnoreCase("SMALLINT"))
			return "short";
		if(type.equalsIgnoreCase("BIGINT") || type.equalsIgnoreCase("BIGINTEGER") || type.equalsIgnoreCase("LONG"))
			return "long";
		if(type.equalsIgnoreCase("FLOAT") || type.equalsIgnoreCase("REAL"))
			return "float";
		if(type.equalsIgnoreCase("DECIMAL") || type.equalsIgnoreCase("DOUBLE"))
			return "double";
		if(type.equalsIgnoreCase("NUMERIC"))
			return "BigDecimal";
		if(type.equalsIgnoreCase("VARBINARY") || type.equalsIgnoreCase("BINARY"))
			return "byte[]";
		if(type.equalsIgnoreCase("DATETIME") || type.equalsIgnoreCase("DATE") || type.equalsIgnoreCase("TIME") || type.equalsIgnoreCase("TIMESTAMP"))
			return "Date";
		if(type.equalsIgnoreCase("VARCHAR") || type.equalsIgnoreCase("CHAR") || type.equalsIgnoreCase("LONGVARCHAR") || type.equalsIgnoreCase("TEXT"))
			return "String";
		if(type.equalsIgnoreCase("CLOB"))
			return "Clob";
		if(type.equalsIgnoreCase("BLOB"))
			return "Blob";
		if(type.equalsIgnoreCase("ARRAY"))
			return "Array";
		if(type.equalsIgnoreCase("REF"))
			return "Ref";
		if(type.equalsIgnoreCase("STRUCT"))
			return "Struct";
		
		return "String";
	}
	
	public static String typeValue(String type){
		String split[] = type.split(" ");
		type = split == null ? type : split[0];
		if(type.equalsIgnoreCase("INTEGER") || type.equalsIgnoreCase("INT"))
			return "intValue();";
		if(type.equalsIgnoreCase("TINYINT")) 
			return "byteValue();";
		if(type.equalsIgnoreCase("BOOLEAN") || type.equalsIgnoreCase("BIT"))
			return "booleanValue();";
		if(type.equalsIgnoreCase("SMALLINTEGER") || type.equalsIgnoreCase("SMALLINT"))
			return "shortValue();";
		if(type.equalsIgnoreCase("BIGINT") || type.equalsIgnoreCase("BIGINTEGER") || type.equalsIgnoreCase("LONG"))
			return "longValue();";
		if(type.equalsIgnoreCase("FLOAT") || type.equalsIgnoreCase("REAL"))
			return "floatValue();";
		if(type.equalsIgnoreCase("DECIMAL") || type.equalsIgnoreCase("DOUBLE"))
			return "doubleValue();";
		if(type.equalsIgnoreCase("NUMERIC"))
			return "decimalValue();";
		if(type.equalsIgnoreCase("VARBINARY") || type.equalsIgnoreCase("BINARY"))
			return "byteArrayValue();";
		if(type.equalsIgnoreCase("DATETIME") || type.equalsIgnoreCase("DATE") || type.equalsIgnoreCase("TIME") || type.equalsIgnoreCase("TIMESTAMP"))
			return "dateValue();";
		if(type.equalsIgnoreCase("VARCHAR") || type.equalsIgnoreCase("CHAR") || type.equalsIgnoreCase("LONGVARCHAR") || type.equalsIgnoreCase("TEXT"))
			return "stringValue();";
		if(type.equalsIgnoreCase("CLOB"))
			return "clobValue();";
		if(type.equalsIgnoreCase("BLOB"))
			return "blobValue();";
		if(type.equalsIgnoreCase("ARRAY"))
			return "arrayValue();";
		if(type.equalsIgnoreCase("REF"))
			return "refValue();";
		if(type.equalsIgnoreCase("STRUCT"))
			return "structValue();";
		
		return "stringValue();";
	}
	
	public static String imports(String type){
		String split[] = type.split(" ");
		type = split == null ? type : split[0];
		
		if(type.equalsIgnoreCase("NUMERIC"))
			return "import " + BigDecimal.class.getPackage().getName() + "." + BigDecimal.class.getSimpleName() + ";" + System.lineSeparator();
		if(type.equalsIgnoreCase("DATETIME") || type.equalsIgnoreCase("DATE") || type.equalsIgnoreCase("TIME") || type.equalsIgnoreCase("TIMESTAMP"))
			return "import " + Date.class.getPackage().getName() + "." + Date.class.getSimpleName() + ";" + System.lineSeparator();
		if(type.equalsIgnoreCase("CLOB"))
			return "import " + Clob.class.getPackage().getName() + "." + Clob.class.getSimpleName() + ";" + System.lineSeparator();
		if(type.equalsIgnoreCase("BLOB"))
			return "import " + Blob.class.getPackage().getName() + "." + Blob.class.getSimpleName() + ";" + System.lineSeparator();
		if(type.equalsIgnoreCase("ARRAY"))
			return "import " + Array.class.getPackage().getName() + "." + Array.class.getSimpleName() + ";" + System.lineSeparator();
		if(type.equalsIgnoreCase("REF"))
			return "import " + Ref.class.getPackage().getName() + "." + Ref.class.getSimpleName() + ";" + System.lineSeparator();
		if(type.equalsIgnoreCase("STRUCT"))
			return "import " + Struct.class.getPackage().getName() + "." + Struct.class.getSimpleName() + ";" + System.lineSeparator();
			
		return "";
	}

	public static boolean dateFormat(String type){
		String split[] = type.split(" ");
		type = split == null ? type : split[0];

		if(type.equalsIgnoreCase("DATETIME") || type.equalsIgnoreCase("DATE") || type.equalsIgnoreCase("TIME") || type.equalsIgnoreCase("TIMESTAMP"))
			return true;
		return false;
	}

	/********************************
	 * tab
	 ********************************/

	public static StringBuilder tab(int n) {
		StringBuilder tab = new StringBuilder();
		for (int i = 0; i < n; i++) {
			tab.append("\t");
		}
		return tab;
	}

	/********************************
	 * separator
	 ********************************/

	public static StringBuilder line(int n) {
		StringBuilder line = new StringBuilder();
		for (int i = 0; i < n; i++) {
			line.append(System.lineSeparator());
		}
		return line;
	}

}
