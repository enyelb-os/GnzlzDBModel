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
		if(s.equals("package") || s.equals("query")){
			return s + "1";
		}
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
		String[] temp = s.split("_");
		StringBuilder newString = new StringBuilder();
		for (String string : temp) {
			if (!string.isEmpty()) {
				newString.append(Character.toUpperCase(string.charAt(0))).append(string.substring(1));
			}
		}

		if(!s.isEmpty() && s.substring(s.length()-1).equalsIgnoreCase("_")){
			newString.append("_");
		}
		
		return newString.toString();
	}
	
	public static String camelCaseMethod(String s) {
		s = s.toLowerCase();
		String[] temp = s.split("_");
		StringBuilder newString = new StringBuilder();
		for (int i = 0; i < temp.length; i++) {
			if(i==0){
				newString.append(Character.toLowerCase(temp[i].charAt(0))).append(temp[i].substring(1));
			} else {
				newString.append(Character.toUpperCase(temp[i].charAt(0))).append(temp[i].substring(1));
			}
		}

		if(s.substring(s.length()-1).equalsIgnoreCase("_")){
			newString.append("_");
		}
		
		return newString.toString();
	}
	
	public static String typeData(String type) {
		type = type.split(" ")[0];
		if (type.equalsIgnoreCase("INTEGER") || type.equalsIgnoreCase("INT")) {
			return "int";
		} else if (type.equalsIgnoreCase("TINYINT")) {
			return "byte";
		} else if (type.equalsIgnoreCase("BOOLEAN") || type.equalsIgnoreCase("BIT")) {
			return "boolean";
		} else if (type.equalsIgnoreCase("SMALLINTEGER") || type.equalsIgnoreCase("SMALLINT")) {
			return "short";
		} else if (type.equalsIgnoreCase("BIGINT") || type.equalsIgnoreCase("BIGINTEGER") || type.equalsIgnoreCase("LONG")) {
			return "long";
		} else if (type.equalsIgnoreCase("FLOAT") || type.equalsIgnoreCase("REAL")) {
			return "float";
		} else if (type.equalsIgnoreCase("DECIMAL") || type.equalsIgnoreCase("DOUBLE")) {
			return "double";
		} else if (type.equalsIgnoreCase("NUMERIC")) {
			return "BigDecimal";
		} else if (type.equalsIgnoreCase("VARBINARY") || type.equalsIgnoreCase("BINARY")) {
			return "byte[]";
	    } else if(type.equalsIgnoreCase("DATETIME") || type.equalsIgnoreCase("DATE") || type.equalsIgnoreCase("TIME") || type.equalsIgnoreCase("TIMESTAMP")) {
			return "Date";
		} else if(type.equalsIgnoreCase("VARCHAR") || type.equalsIgnoreCase("CHAR") || type.equalsIgnoreCase("LONGVARCHAR") || type.equalsIgnoreCase("TEXT")) {
			return "String";
		} else if(type.equalsIgnoreCase("CLOB")) {
			return "Clob";
		} else if(type.equalsIgnoreCase("BLOB")) {
			return "Blob";
		} else if(type.equalsIgnoreCase("ARRAY")) {
			return "Array";
		} else if(type.equalsIgnoreCase("REF")) {
			return "Ref";
		} else if(type.equalsIgnoreCase("STRUCT")) {
			return "Struct";
		}
		return "String";
	}
	
	public static String typeValue(String type){
		type= type.split(" ")[0];
		if(type.equalsIgnoreCase("INTEGER") || type.equalsIgnoreCase("INT")) {
			return "intValue();";
		} else if(type.equalsIgnoreCase("TINYINT")) {
			return "byteValue();";
		} else if(type.equalsIgnoreCase("BOOLEAN") || type.equalsIgnoreCase("BIT")) {
			return "booleanValue();";
		} else if(type.equalsIgnoreCase("SMALLINTEGER") || type.equalsIgnoreCase("SMALLINT")) {
			return "shortValue();";
		} else if(type.equalsIgnoreCase("BIGINT") || type.equalsIgnoreCase("BIGINTEGER") || type.equalsIgnoreCase("LONG")) {
			return "longValue();";
		} else if(type.equalsIgnoreCase("FLOAT") || type.equalsIgnoreCase("REAL")) {
			return "floatValue();";
		} else if(type.equalsIgnoreCase("DECIMAL") || type.equalsIgnoreCase("DOUBLE")) {
			return "doubleValue();";
		} else if(type.equalsIgnoreCase("NUMERIC")) {
			return "decimalValue();";
		} else if(type.equalsIgnoreCase("VARBINARY") || type.equalsIgnoreCase("BINARY")) {
			return "byteArrayValue();";
		} else if(type.equalsIgnoreCase("DATETIME") || type.equalsIgnoreCase("DATE") || type.equalsIgnoreCase("TIME") || type.equalsIgnoreCase("TIMESTAMP")) {
			return "dateValue();";
		} else if(type.equalsIgnoreCase("VARCHAR") || type.equalsIgnoreCase("CHAR") || type.equalsIgnoreCase("LONGVARCHAR") || type.equalsIgnoreCase("TEXT")) {
			return "stringValue();";
		} else if(type.equalsIgnoreCase("CLOB")) {
			return "clobValue();";
		} else if(type.equalsIgnoreCase("BLOB")) {
			return "blobValue();";
		} else if(type.equalsIgnoreCase("ARRAY")) {
			return "arrayValue();";
		} else if(type.equalsIgnoreCase("REF")) {
			return "refValue();";
		} else if(type.equalsIgnoreCase("STRUCT")) {
			return "structValue();";
		}
		return "stringValue();";
	}
	
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

	public static boolean isDateFormat(String type){
		type = type.split(" ")[0];
        return type.equalsIgnoreCase("DATETIME") || type.equalsIgnoreCase("DATE") || type.equalsIgnoreCase("TIME") || type.equalsIgnoreCase("TIMESTAMP");
    }

}
