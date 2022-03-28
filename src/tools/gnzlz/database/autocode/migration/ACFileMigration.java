package tools.gnzlz.database.autocode.migration;

import tools.gnzlz.database.autocode.ACFormat;
import tools.gnzlz.database.model.DBMigration;
import tools.gnzlz.database.properties.PropertiesTable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class ACFileMigration {
	
	public static void createFile(ACMigration migration) {
		try {
			for (ACTable table : migration.tables()) {
				System.out.println("creating migration "+nameF(table)+" | path:" + path(migration,table)+ACFileMigration.nameF(table)+".java");
				File file = new File(path(migration, table)+nameF(table)+".java");
				if(!file.exists()) {
					Files.createFile(file.toPath());
					FileWriter fileWriter = new FileWriter(file.toString());
					fileWriter.write(packages(migration, table));
					fileWriter.write(line(2));
					fileWriter.write(imports(migration, table));
					fileWriter.write(line(1));
					fileWriter.write(dbMigrationName(table));
					fileWriter.write(line(1));
					fileWriter.write(constructor(table));
					fileWriter.write(line(1));
					fileWriter.write(tableName(table));
					fileWriter.write(line(1));
					fileWriter.write(packageName(table));
					fileWriter.write(line(1));
					fileWriter.write(initTable(migration, table));
					fileWriter.write(line(1));
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

	static String migrationPackage(ACMigration migration, ACTable table) {
		if(migration.configuration.migration().migrationPackage()!=null)
			return migration.configuration.migration().migrationPackage().concat(table.packegeName());
		else 
			return migration.configuration.getClass().getPackage().getName().concat(".migration").concat(table.packegeName());
	}
	
//	private static String getPackageName(Class<?> c) {
//        while (c.isArray()) {
//            c = c.getComponentType();
//        }
//        if (c.isPrimitive()) {
//            return "java.lang";
//        } else {
//            String cn = c.getName();
//            int dot = cn.lastIndexOf('.');
//            return (dot != -1) ? cn.substring(0, dot).intern() : "";
//        }
//	}
	
	private static String path(ACMigration migration, ACTable table) {
		File file = new File("src/"+(migrationPackage(migration,table).replaceAll("[.]", "/")));
		if(!file.exists())
			file.mkdirs();
		return file.getPath()+"\\";
	}

	/********************************
	 * nameF
	 ********************************/

	static String prefix() {
		return "";
	}

	static String nameF(ACTable table) {
		return prefix()+table.tableCamelCase();
	}

	/********************************
	 * exists Imports
	 ********************************/

	private static boolean existsImport(ArrayList<String> imports, String newImport, StringBuilder string){
		for (String imported : imports) {
			if(imported.equalsIgnoreCase(newImport)){
				return true;
			}
		}
		string.append(newImport).append(line(1));
		imports.add(newImport);
		return false;
	}

	/********************************
	 * Imports
	 ********************************/

	private static String relationsImports(ACMigration migration, ACTable table) {
		StringBuilder string = new StringBuilder();
		ArrayList<String> imports = new ArrayList<String>();
		for (ACColumn column : table.columns()) {
			for (ACRelation relation : column.hasOnes()) {
				String imp = migration.migrationPackage(relation.relation());
				String newImport = null;
				if(!imp.isEmpty())
					newImport = "import ".concat(imp).concat(";");
				else
					newImport = "import ".concat(ACFileMigration.migrationPackage(migration,migration.table(relation.relation()))).concat(".").concat(relation.relationCamelCase()).concat(";");

				existsImport(imports,newImport,string);
			}
		}
		return string.toString();
	}
	
	private static String imports(ACMigration migration, ACTable table) {
		return new StringBuilder()
				.append(relationsImports(migration,table))
				.append("import ").append(DBMigration.class.getPackage().getName()).append(".").append(DBMigration.class.getSimpleName()).append(";").append(line(1))
				.append("import ").append(PropertiesTable.class.getPackage().getName()).append(".").append(PropertiesTable.class.getSimpleName()).append(";").append(line(1)).toString();
	}
	
	/********************************
	 * packages
	 ********************************/
	
	private static String packages(ACMigration migration, ACTable table) {
		return new StringBuilder().append("package ").append(migrationPackage(migration,table)).append(";").toString();
	}
	
	/********************************
	 * modelClass
	 ********************************/
	
	private static String dbMigrationName(ACTable table) {
		return new StringBuilder().append("public class ").append(nameF(table))
				.append(" extends ").append(DBMigration.class.getSimpleName()).append(" {").toString();
	}

	/********************************
	 * constructor
	 ********************************/
	
	private static String constructor(ACTable table) {
		return "";
	}
	
	/********************************
	 * constructor
	 ********************************/
	
	private static String end(int n) {
		return new StringBuilder().append(tab(n)).append("}").toString();
	}


	/********************************
	 * tableName
	 ********************************/

	private static String tableName(ACTable table) {
		StringBuilder str = new StringBuilder();
		str.append(tab(1)).append("@Override").append(line(1))
				.append(tab(1)).append("public String tableName() {").append(line(1))
				.append(tab(2)).append("return \"").append(table.table()).append("\";").append(line(1))
				.append(tab(1)).append("}").append(line(1));
		return str.toString();
	}

	/********************************
	 * packageName
	 ********************************/

	private static String packageName(ACTable table) {
		StringBuilder str = new StringBuilder();
		str.append(tab(1)).append("@Override").append(line(1))
				.append(tab(1)).append("public String packageName() {").append(line(1))
				.append(tab(2)).append("return \"").append(table.packegeName()).append("\";").append(line(1))
				.append(tab(1)).append("}").append(line(1));
		return str.toString();
	}

	/********************************
	 * initTable
	 ********************************/

	private static String initTable(ACMigration migration,ACTable table) {
		StringBuilder str = new StringBuilder();
		str.append(tab(1)).append("@Override").append(line(1))
				.append(tab(1)).append("protected void initTable(").append(PropertiesTable.class.getSimpleName()).append(" table)").append(" {").append(line(1))
				.append(methods(migration, table)).append(line(1))
				.append(tab(1)).append("}");
		return str.toString();
	}

	/********************************
	 * method
	 ********************************/
	
	private static String methods(ACMigration migration,ACTable table) {
		ArrayList<ACColumn> columns = table.columns();
		if(!columns.isEmpty()){
			StringBuilder s = new StringBuilder();
			for (int i = 0; i < columns.size(); i++) {
				if(i != 0) s.append(line(1));
				s.append(method(migration, table, columns.get(i)));
			}
			return s.toString();
		}
		return "";
	}
	
	private static String method(ACMigration migration,ACTable table, ACColumn column) {
		String type = ACFormat.typeData(column.type());
		String value = ACFormat.typeValue(column.type());

		StringBuilder str = new StringBuilder();
		str.append(tab(2)).append("table");
		if(column.primaryKey()){
			str.append(".primaryKey(\"").append(column.name).append("\",").append(column.type).append(",\"").append(column.size).append("\")");
		}else{
			str.append(".column(\"").append(column.name).append("\",").append(column.type).append(",\"").append(column.size).append("\")");
		}

		if(!column.nullable()){
			str.append(".notNull()");
		}

		if(!column.isDefault().isEmpty()){
			str.append(".isDefault(\"").append(column.isDefault).append("\")");
		}

		str.append(foreignKey(migration,column));
		
		return str.append(";").toString();
	}

	/********************************
	 * .foreignKey
	 ********************************/

	private static String foreignKey(ACMigration migration, ACColumn column) {
		ArrayList<ACRelation> relations = column.hasOnes();
		StringBuilder str = new StringBuilder();
		for (ACRelation relation: relations) {
			String imp = migration.migrationClass(relation.relation());
			String name = null;
			if(!imp.isEmpty())
				name = imp;
			else
				name = relation.relationCamelCase();

			str.append(".foreignKey(").append(name).append(".class,\"").append(relation.column()).append("\")");
		}
		return str.toString();
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
