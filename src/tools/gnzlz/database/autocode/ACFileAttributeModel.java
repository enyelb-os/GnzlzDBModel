package tools.gnzlz.database.autocode;

import tools.gnzlz.database.form.DBFormColumn;
import tools.gnzlz.database.form.DBFormModel;
import tools.gnzlz.validation.Validation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class ACFileAttributeModel {
	
	public static void createFile(ACDataBase dataBase) {
		try {
			for (ACTable table : dataBase.tables()) {
				File file = new File(path(dataBase, table)+nameF(table)+".java");
				if(!file.exists()){
					Files.createFile(file.toPath());
					FileWriter fileWriter = new FileWriter(file.toString());
					fileWriter.write(packages(dataBase, table));
					fileWriter.write(line(2));
					fileWriter.write(imports(dataBase,table));
					fileWriter.write(line(2));
					fileWriter.write(atModelName(table));
					//fileWriter.write(line(2));
					//fileWriter.write(constructor(table));
					fileWriter.write(line(2));
					fileWriter.write(methods(table));
					fileWriter.write(line(2));
					fileWriter.write(end(0));
					fileWriter.close();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/********************************
	 * path
	 ********************************/

	static String modelPackage(ACDataBase dataBase, ACTable table) {
		if(dataBase.configuration.model().modelPackage()!=null)
			return dataBase.configuration.model().modelPackage().concat(table.packegeName()).concat(".attribute");
		else 
			return dataBase.configuration.getClass().getPackage().getName().concat(".model").concat(table.packegeName()).concat(".attribute");
	}
	
	private static String path(ACDataBase dataBase, ACTable table) {
		File file = new File("src/"+(modelPackage(dataBase,table).replaceAll("[.]", "/")));
		if(!file.exists())
			file.mkdirs();
		return file.getPath()+"\\";
	}

	/********************************
	 * nameF
	 ********************************/

	static String prefix() {
		return "Attribute";
	}

	static String nameF(ACTable table) {
		return prefix()+table.tableCamelCase();
	}

	/********************************
	 * exists Imports
	 ********************************/
	
	private static String imports(ACDataBase dataBase,ACTable table) {
		return new StringBuilder()
				.append("import ").append(ACFileModel.modelPackage(dataBase, table)).append(".").append(ACFileModel.nameF(table)).append(";").append(line(1))
				.append("import ").append(Validation.class.getPackage().getName()).append(".").append(Validation.class.getSimpleName()).append(";").append(line(1))
				.append("import ").append(DBFormColumn.class.getPackage().getName()).append(".").append(DBFormColumn.class.getSimpleName()).append(";").append(line(1))
				.append("import ").append(DBFormModel.class.getPackage().getName()).append(".").append(DBFormModel.class.getSimpleName()).append(";").append(line(1)).toString();
	}
	
	/********************************
	 * packages
	 ********************************/
	
	private static String packages(ACDataBase dataBase, ACTable table) {
		return new StringBuilder().append("package ").append(modelPackage(dataBase,table)).append(";").toString();
	}
	
	/********************************
	 * modelClass
	 ********************************/
	
	private static String atModelName(ACTable table) {
		return new StringBuilder().append("public class ").append(nameF(table))
				.append(" extends ").append(DBFormModel.class.getSimpleName()).append("<").append(ACFileModel.nameF(table)).append("> {").toString();
	}

	/********************************
	 * constructor
	 ********************************/
	
	private static String constructor(ACTable table) {
		//return new StringBuilder().append(tab(1)).append("public ").append(nameF(table)).append("() {").append(line(1))
		//		.append(tab(1)).append("}").toString();
		return "";
	}
	
	/********************************
	 * constructor
	 ********************************/
	
	private static String end(int n) {
		return new StringBuilder().append(tab(n)).append("}").toString();
	}
	
	/********************************
	 * method
	 ********************************/
	
	private static String methods(ACTable table) {
		ArrayList<ACColumn> columns = table.columns();
		if(!columns.isEmpty()){
			StringBuilder s = new StringBuilder();
			for (int i = 0; i < columns.size(); i++) {
				if(i != 0) s.append(line(2));
				s.append(method(table, columns.get(i)));
			}
			return s.toString();
		}
		return "";
	}
	
	private static String method(ACTable table,ACColumn column) {
		String type = ACFormat.typeData(column.type());
		String value = ACFormat.typeValue(column.type());

		StringBuilder str = new StringBuilder();

		return str
				.append(tab(1)).append("public ").append(DBFormColumn.class.getSimpleName()).append(" ").append(column.nameCamelCase()).append("() {").append(line(1))
				.append(tab(2)).append("return ").append(DBFormColumn.class.getSimpleName()).append(".create(").append(ACFileModel.nameF(table)).append(".").append(column.nameUpperCase()).append(")").append(line(1))
				.append(tab(3)).append(".validation(").append(validation(type)).append(");").append(line(1))
				.append(tab(1)).append("}").toString();
	}

	private static String validation(String type) {
		if(type.equalsIgnoreCase("int"))
			return Validation.class.getSimpleName() + ".isInt()";
		if(type.equalsIgnoreCase("short"))
			return Validation.class.getSimpleName() + ".isShort()";
		if(type.equalsIgnoreCase("byte"))
			return Validation.class.getSimpleName() + ".isByte()";
		if(type.equalsIgnoreCase("boolean"))
			return Validation.class.getSimpleName() + ".isBoolean()";
		if(type.equalsIgnoreCase("long"))
			return Validation.class.getSimpleName() + ".isLong()";
		if(type.equalsIgnoreCase("float"))
			return Validation.class.getSimpleName() + ".isFloat()";
		if(type.equalsIgnoreCase("double"))
			return Validation.class.getSimpleName() + ".isDouble()";
		if(type.equalsIgnoreCase("String"))
			return Validation.class.getSimpleName() + ".alphanumeric()";

		return "";
	}
	
	/********************************
	 * tab
	 ********************************/
	
	private static String tab(int n) {
		StringBuilder tab = new StringBuilder();
		for (int i = 0; i < n; i++) {
			tab.append("\t");
		}
		return tab.toString();
	}
	
	/********************************
	 * separator
	 ********************************/
	
	private static String line(int n) {
		StringBuilder line = new StringBuilder();
		for (int i = 0; i < n; i++) {
			line.append(System.lineSeparator());
		}
		return line.toString();
	}
}
