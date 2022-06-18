package tools.gnzlz.database.autocode.model;

import tools.gnzlz.database.autocode.ACFormat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class ACFileCustomModel {
	
	public static void createFile(ACDataBase dataBase) {
		try {
			for (ACCatalog catalog: dataBase.catalogs) {
				for (ACScheme scheme: catalog.schemes) {
					for (ACTable table : scheme.tables) {
						File file = new File(path(dataBase, table) + nameF(table) + ".java");
						if (!file.exists()) {
							Files.createFile(file.toPath());
							FileWriter fileWriter = new FileWriter(file.toString());
							fileWriter.write(packages(dataBase, table));
							fileWriter.write(line(2));
							fileWriter.write(imports(dataBase, table));
							fileWriter.write(line(1));
							fileWriter.write(dbModelName(table));
							fileWriter.write(line(2));
							fileWriter.write(method(table));
							fileWriter.write(line(2));
							fileWriter.write(example(table));
							fileWriter.write(line(2));
							fileWriter.write(end(0));
							fileWriter.close();
						}
					}
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
			return dataBase.configuration.model().modelPackage().concat(table.packegeName()).concat(".custom");
		else 
			return dataBase.configuration.getClass().getPackage().getName().concat(".model").concat(table.packegeName()).concat(".custom");
	}
	
	private static String path(ACDataBase dataBase, ACTable table) {
		File file = new File("src/"+(modelPackage(dataBase, table).replaceAll("[.]", "/")));
		if(!file.exists())
			file.mkdirs();
		return file.getPath()+"/";
	}
	
	/********************************
	 * nameF
	 ********************************/

	static String prefix() {
		return "IC";
	}

	static String nameF(ACTable table) {
		return prefix()+table.nameCamelCase();
	}
	
	/********************************
	 * Imports
	 ********************************/
	
	private static String imports(ACDataBase dataBase,ACTable table) {
		return new StringBuilder()
				.append("import ").append(ACFileModel.modelPackage(dataBase, table)).append(".").append(ACFileModel.nameF(table)).append(";").append(line(1)).toString();
	}
	
	/********************************
	 * packages
	 ********************************/
	
	private static String packages(ACDataBase dataBase, ACTable table) {
		return new StringBuilder().append("package ").append(modelPackage(dataBase, table)).append(";").toString();
	}
	
	/********************************
	 * modelClass
	 ********************************/
	
	private static String dbModelName(ACTable table) {
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
				.append(tab(1)).append(" * \t\tmodel = ").append(ACFormat.camelCaseMethod(table.nameCamelCase())).append("();").append(line(1))
				.append(tab(1)).append(" * \t\tcode ... ").append(line(1))
				.append(tab(1)).append(" * }").append(line(1))
				.append(tab(1)).append(" *******************************************/").toString();
	}
	
	/********************************
	 * Method
	 ********************************/
	
	private static String method(ACTable table) {
		return new StringBuilder()
				.append(tab(1)).append("public ").append(table.nameCamelCase()).append(" ").append("modelDB();").toString();
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
