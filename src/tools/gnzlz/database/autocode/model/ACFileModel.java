package tools.gnzlz.database.autocode.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class ACFileModel {

	public static void createFile(ACDataBase dataBase) {
		try {
			for (ACCatalog catalog: dataBase.catalogs) {
				for (ACScheme scheme: catalog.schemes) {
					for (ACTable table: scheme.tables) {
						File file = new File(path(dataBase, table)+nameF(table)+".java");
						if(!file.exists()) {
							Files.createFile(file.toPath());
							FileWriter fileWriter = new FileWriter(file.toString());
							fileWriter.write(packages(dataBase,table));
							fileWriter.write(line(2));
							fileWriter.write(imports(dataBase,table));
							fileWriter.write(line(1));
							fileWriter.write(dbModelName(table));
							fileWriter.write(line(2));
							fileWriter.write(method(table));
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

	static String modelPackage(ACDataBase dataBase,ACTable table) {
		if(dataBase.configuration.model().modelPackage()!=null)
			return dataBase.configuration.model().modelPackage().concat(table.packegeName());
		else
			return dataBase.configuration.getClass().getPackage().getName().concat(".model").concat(table.packegeName());
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
		return "";
	}

	static String nameF(ACTable table) {
		return prefix() + table.nameCamelCase();
	}

	/********************************
	 * Imports
	 ********************************/

	private static String imports(ACDataBase dataBase,ACTable table) {
		return new StringBuilder()
				.append("import ").append(ACFileCustomModel.modelPackage(dataBase, table)).append(".").append(ACFileCustomModel.nameF(table)).append(";").append(line(1))
				.append("import ").append(ACFileBaseModel.modelPackage(dataBase, table)).append(".").append(ACFileBaseModel.nameF(table)).append(";").append(line(1)).toString();
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
		return new StringBuilder().append("public class ").append(nameF(table)).append(" extends ")
				.append(ACFileBaseModel.nameF(table)).append("<").append(nameF(table)).append("> implements ")
				.append(ACFileCustomModel.nameF(table)).append(" {").toString();
	}

	/********************************
	 * method
	 ********************************/

	private static String method(ACTable table) {
		return new StringBuilder()
				.append(tab(1)).append("@Override").append(line(1))
				.append(tab(1)).append("public ").append(nameF(table)).append(" modelDB() {").append(line(1))
				.append(tab(2)).append("return this;").append(line(1))
				.append(tab(1)).append("}").toString();
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
