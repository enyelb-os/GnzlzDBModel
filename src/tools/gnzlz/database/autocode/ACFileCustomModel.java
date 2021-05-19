package tools.gnzlz.database.autocode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class ACFileCustomModel {
	
	public static void createFile(ACDataBase dataBase) {
		try {
			for (ACTable table : dataBase.tables()) {
				File file = new File(path(dataBase)+nameF(table)+".java");
				if(!file.exists()) {
					Files.createFile(file.toPath());
					FileWriter fileWriter = new FileWriter(file.toString());
					fileWriter.write(packages(dataBase));
					fileWriter.write(line(2));
					fileWriter.write(imports(dataBase,table));
					fileWriter.write(line(1));
					fileWriter.write(customModelName(table));
					fileWriter.write(line(2));
					fileWriter.write(method(table));
					fileWriter.write(line(2));
					fileWriter.write(example(table));
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

	private static String modelPackage(ACDataBase dataBase) {
		if(dataBase.configuration.model().modelPackage()!=null)
			return dataBase.configuration.model().modelPackage();
		else 
			return dataBase.configuration.getClass().getPackage().getName() + ".model"; 
	}
	
	static String icModelPackage(ACDataBase dataBase) {
		if(dataBase.configuration.model().modelPackage()!=null)
			return dataBase.configuration.model().modelPackage()+".custom";
		else 
			return dataBase.configuration.getClass().getPackage().getName() + ".model.custom"; 
	}
	
	private static String path(ACDataBase dataBase) {
		File file = new File("src/"+(icModelPackage(dataBase).replaceAll("[.]", "/")));
		if(!file.exists())
			file.mkdirs();
		return file.getPath()+"/";
	}
	
	/********************************
	 * nameF
	 ********************************/

	static String nameF(ACTable table) {
		return "IC"+table.tableCamelCase(); 
	}
	
	/********************************
	 * Imports
	 ********************************/
	
	private static String imports(ACDataBase dataBase,ACTable table) {
		return new StringBuilder()
				.append("import ").append(modelPackage(dataBase)).append(".").append(table.tableCamelCase()).append(";").append(line(1)).toString();
	}
	
	/********************************
	 * packages
	 ********************************/
	
	private static String packages(ACDataBase dataBase) {
		return new StringBuilder().append("package ").append(icModelPackage(dataBase)).append(";").toString();
	}
	
	/********************************
	 * modelClass
	 ********************************/
	
	private static String customModelName(ACTable table) {
		return new StringBuilder().append("public interface ").append(nameF(table)).append(" {").toString();
	}
	
	/********************************
	 * addConfiguration
	 ********************************/
	
	private static String example(ACTable table) {
		return new StringBuilder()
				.append(tab(1)).append("/*******************************************").append(line(1))
				.append(tab(1)).append(" * @Example").append(line(1))
				.append(tab(1)).append(" * public default <TypeData> nameMethod() {").append(line(1))
				.append(tab(1)).append(" * \t\tmodel = ").append(ACFormat.camelCaseMethod(table.tableCamelCase())).append("();").append(line(1))
				.append(tab(1)).append(" * \t\tcode ... ").append(line(1))
				.append(tab(1)).append(" * }").append(line(1))
				.append(tab(1)).append(" *******************************************/").toString();
	}
	
	/********************************
	 * Method
	 ********************************/
	
	private static String method(ACTable table) {
		return new StringBuilder()
				.append(tab(1)).append("public ").append(table.tableCamelCase()).append(" ").append("modelDB();").toString();
	}
	
	/********************************
	 * end
	 ********************************/
	
	private static String end(int n) {
		return new StringBuilder().append(tab(n)).append("}").toString();
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
