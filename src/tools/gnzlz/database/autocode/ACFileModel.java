package tools.gnzlz.database.autocode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import tools.gnzlz.database.data.DBModel;
import tools.gnzlz.database.data.DBTable;

public class ACFileModel {
	
	public static void createFile(ACDataBase dataBase) {
		try {
			for (ACTable table : dataBase.tables()) {
				System.out.println("creating model "+table.tableF()+" | path:" + path(dataBase)+table.tableF()+".java");
				File file = new File(path(dataBase)+table.tableF()+".java");
				if(!file.exists())
					Files.createFile(file.toPath());
				FileWriter fileWriter = new FileWriter(file.toString());
				fileWriter.write(packages(dataBase));
				fileWriter.write(line(2));
				fileWriter.write(imports(dataBase,table));
				fileWriter.write(line(1));
				fileWriter.write(dbModelName(table));
				fileWriter.write(line(2));
				fileWriter.write(dbTableConfig(table));
				fileWriter.write(line(2));
				fileWriter.write(constructor(table));
				fileWriter.write(line(2));
				fileWriter.write(method(table));
				fileWriter.write(line(2));
				fileWriter.write(methods(table));
				fileWriter.write(line(2));
				fileWriter.write(statics(table));
				fileWriter.write(line(1));
				fileWriter.write(end(0));
				fileWriter.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/********************************
	 * path
	 ********************************/

	private static String modelPackage(ACDataBase dataBase) {
		if(dataBase.configuration.modelPackage()!=null)
			return dataBase.configuration.modelPackage();
		else 
			return dataBase.configuration.getClass().getPackage().getName() + ".model"; 
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
	
	private static String path(ACDataBase dataBase) {
		File file = new File("src/"+(modelPackage(dataBase).replaceAll("[.]", "/")));
		if(!file.exists())
			file.mkdirs();
		return file.getPath()+"\\";
	}
	
	/********************************
	 * Imports
	 ********************************/
	
	private static String extraImports(ACTable table) {
		StringBuilder string = new StringBuilder();
		ArrayList<String> imports = new ArrayList<String>();
		for (ACColumn column : table.columns()) {
			String newImport = ACFormat.imports(column.getType());
			boolean isImported = false;
			for (String imported : imports) {
				if(imported.equalsIgnoreCase(newImport)){
					isImported = true;
					break;
				}
			}
			if(!isImported) {
				string.append(newImport);
				imports.add(newImport);
			}
		}
		
		return string.toString();
	}
	
	private static String imports(ACDataBase dataBase,ACTable table) {
		return new StringBuilder()
				.append(extraImports(table))
				.append("import java.util.ArrayList;").append(line(2))
				.append("import ").append(dataBase.configuration.getClass().getPackage().getName()).append(".").append(dataBase.configuration.getClass().getSimpleName()).append(";").append(line(1))
				.append("import ").append(DBModel.class.getPackage().getName()).append(".").append(DBModel.class.getSimpleName()).append(";").append(line(1))
				.append("import ").append(DBTable.class.getPackage().getName()).append(".").append(DBTable.class.getSimpleName()).append(";").append(line(1))
				.append("import ").append(ACFileCustomModel.icModelPackage(dataBase)).append(".").append(ACFileCustomModel.nameF(table)).append(";").append(line(1)).toString();
	}
	
	/********************************
	 * packages
	 ********************************/
	
	private static String packages(ACDataBase dataBase) {
		return new StringBuilder().append("package ").append(modelPackage(dataBase)).append(";").toString();
	}
	
	/********************************
	 * modelClass
	 ********************************/
	
	private static String dbModelName(ACTable table) {
		return new StringBuilder().append("public class ").append(table.tableF()).append(" extends DBModel<").append(table.tableF()).append("> implements ").append(ACFileCustomModel.nameF(table)).append(" {").toString();
	}
	
	/********************************
	 * tableConfig
	 ********************************/
	
	private static String dbTableConfig(ACTable table) {
		return new StringBuilder().append(tab(1)).append("private static final ").append(DBTable.class.getSimpleName()).append(" DBTABLE = ").append(DBTable.class.getSimpleName()).append(".create()").append(line(1))
				.append(tab(3)).append(addConfiguration(table)).append(line(1))
				.append(tab(3)).append(addTable(table)).append(addPrimaryKey(table)).append(line(1))
				.append(tab(3)).append(addColumns(table)).append(line(1))
				.append(addRelations(table)).append(";").toString();
	}
	
	/********************************
	 * addConfiguration
	 ********************************/
	
	private static String addConfiguration(ACTable table) {
		return new StringBuilder().append(".addConfiguration(").append(table.dataBase.configuration.getClass().getSimpleName()).append(".class)").toString();
	}
	
	/********************************
	 * addTable
	 ********************************/
	
	private static String addTable(ACTable table) {
		return new StringBuilder().append(".addTable(\"").append(table.table()).append("\")").toString();
	}
	
	/********************************
	 * addPrimaryKey
	 ********************************/
	
	private static String addPrimaryKey(ACTable table) {
		ACColumn column = table.primaryKey();
		if(column == null)
			return "";
		return new StringBuilder().append(".addPrimaryKey(\"").append(column.name()).append("\")").toString();
	}
	
	/********************************
	 * addColumns
	 ********************************/
	
	private static String addColumns(ACTable table) {
		ArrayList<ACColumn> columns = table.columns();
		if(!columns.isEmpty()){
			StringBuilder string = new StringBuilder();
			for (int i = 0; i < columns.size(); i++) {
				string.append("\"").append(columns.get(i).name).append("\"");
				if(i != columns.size() - 1) string.append(",");
			}
			
			return new StringBuilder().append(".addColumns(").append(string).append(")").toString();
		}
		return "";
	}
	
	/********************************
	 * .addRelations
	 ********************************/
	
	private static String addRelations(ACTable table) {
		String one = addHasOne(table);
		String many = addHasMany(table);
		String belongs = addBelongsToMany(table);
		StringBuilder str = new StringBuilder();
		
		if(one.isEmpty() && many.isEmpty() && belongs.isEmpty())
			return ""; 
			
		if(!one.isEmpty())
			str.append(one);
		if(!many.isEmpty()) {
			if(str.length() != 0)
				str.append(line(1));
			str.append(many);
		}
		if(!belongs.isEmpty()) {
			if(str.length() != 0)
				str.append(line(1));
			str.append(belongs);
		}
		return str.toString();
	}
	
	/********************************
	 * .addHasOne
	 ********************************/
	
	private static String addHasOne(ACTable table) {
		ArrayList<ACColumn> columns = table.columns();
		if(!columns.isEmpty()){
			StringBuilder s = new StringBuilder();
			boolean line = false;
			for (int i = 0; i < columns.size(); i++) {
				ACColumn column = columns.get(i);
				ArrayList<ACRelation> relations = column.hasOnes();
				for (int j = 0; j < relations.size(); j++) {
					if(line) s.append(line(1));
					s.append(tab(3)).append(addHasOne(column, relations.get(j)));
					line = true;
				}
			}
			return s.toString();
		}
		return "";
	}
	
	private static String addHasOne(ACColumn column,ACRelation relation) {
		return new StringBuilder().append(".addHasOne(\"")
				.append(column.name()).append("\",")
				.append(relation.relationF()).append(".class,")
				.append("\"").append(relation.column()).append("\"")
				.append(")").toString();
	}

	/********************************
	 * addHasMany
	 ********************************/
	
	private static String addHasMany(ACTable table) {
		ArrayList<ACColumn> columns = table.columns();
		if(!columns.isEmpty()){
			StringBuilder s = new StringBuilder();
			boolean line = false;
			for (int i = 0; i < columns.size(); i++) {
				ACColumn column = columns.get(i);
				ArrayList<ACRelation> relations = column.hasManys();
				for (int j = 0; j < relations.size(); j++) {
					if(line) s.append(line(1));
					s.append(tab(3)).append(addHasMany(column, relations.get(j)));
					line = true;
				}
			}
			return s.toString();
		}
		return "";
	}
	
	private static String addHasMany(ACColumn column, ACRelation relation) {
		return new StringBuilder().append(".addHasMany(\"")
				.append(column.name()).append("\",")
				.append(relation.relationF()).append(".class,")
				.append("\"").append(relation.column()).append("\"")
				.append(")").toString();
	}
	
	/********************************
	 * addHasMany
	 ********************************/
	
	private static String addBelongsToMany(ACTable table) {
		ArrayList<ACColumn> columns = table.columns();
		if(!columns.isEmpty()){
			StringBuilder s = new StringBuilder();
			boolean line = false;
			for (int i = 0; i < columns.size(); i++) {
				ACColumn column = columns.get(i);
				ArrayList<ACManyToMany> relations = column.belongsToManys();
				for (int j = 0; j < relations.size(); j++) {
					if(line) s.append(line(1));
					s.append(tab(3)).append(addBelongsToMany(column, relations.get(j)));
					line = true;
				}
			}
			return s.toString();
		}
		return "";
	}
	
	private static String addBelongsToMany(ACColumn column, ACManyToMany relation) {
		return new StringBuilder().append(".addBelongsToMany(\"")
				.append(column.name()).append("\",\"")
				.append(relation.internalKey1()).append("\",")
				.append(relation.relationInternalF()).append(".class,\"")
				.append(relation.internalKey2()).append("\",")
				.append(relation.relationForeignF()).append(".class,\"")
				.append(relation.foreignKey()).append("\"")
				.append(")").toString();
	}

	/********************************
	 * constructor
	 ********************************/
	
	private static String constructor(ACTable table) {
		return new StringBuilder().append(tab(1)).append("public ").append(table.tableF()).append("() {").append(line(1))
				.append(tab(2)).append("super(DBTABLE);").append(line(1))
				.append(tab(1)).append("}").toString();
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
	
	private static String method(ACTable table) {
		return new StringBuilder()
				.append(tab(1)).append("@Override").append(line(1))
				.append(tab(1)).append("public ").append(table.tableF()).append(" modelDB() {").append(line(1))
				.append(tab(2)).append("return this;").append(line(1))
				.append(tab(1)).append("}").toString();
	}
	
	private static String method(ACTable table,ACColumn column) {
		String type = ACFormat.typeData(column.type());
		String value = ACFormat.typeValue(column.type());
		
		return new StringBuilder()
				.append(tab(1)).append("public ").append(type).append(" ").append(column.nameF()).append("() {").append(line(1))
				.append(tab(2)).append("return get(").append("\"").append(column.name()).append("\").").append(value).append(line(1))
				.append(tab(1)).append("}").append(line(2))
				.append(tab(1)).append("public ").append(table.tableF()).append(" ").append(column.nameF()).append("(").append(type).append(" ").append(column.nameF()).append(") {").append(line(1))
				.append(tab(2)).append("set(\"").append(column.name()).append("\", ").append(column.nameF()).append(");").append(line(1))
				.append(tab(2)).append("return this;").append(line(1)).append(tab(1)).append("}").toString();
	}
	
	/********************************
	 * statics
	 ********************************/
	
	private static String statics(ACTable table) {
		return new StringBuilder()
				.append(tab(1)).append("public static ").append(table.tableF()).append(" find(Object primaryKey) {").append(line(1))
				.append(tab(2)).append("return ").append(table.tableF()).append(".find(").append(table.tableF()).append(".class,primaryKey);").append(line(1))
				.append(tab(1)).append("}").append(line(2))
				.append(tab(1)).append("public static ").append(table.tableF()).append(" find(String column, Object value) {").append(line(1))
				.append(tab(2)).append("return ").append(table.tableF()).append(".find(").append(table.tableF()).append(".class,column,value);").append(line(1))
				.append(tab(1)).append("}").append(line(2))
				.append(tab(1)).append("public static ArrayList<").append(table.tableF()).append("> find(Object ... primaryKeys) {").append(line(1))
				.append(tab(2)).append("return ").append(table.tableF()).append(".findIn(").append(table.tableF()).append(".class,primaryKeys);").append(line(1))
				.append(tab(1)).append("}").append(line(2))
				.append(tab(1)).append("public static ArrayList<").append(table.tableF()).append("> find(String column,Object ... values) {").append(line(1))
				.append(tab(2)).append("return ").append(table.tableF()).append(".findIn(").append(table.tableF()).append(".class,column,values);").append(line(1))
				.append(tab(1)).append("}").append(line(2))
				.append(tab(1)).append("public static ArrayList<").append(table.tableF()).append("> findAll(String column,Object value) {").append(line(1))
				.append(tab(2)).append("return ").append(table.tableF()).append(".findAll(").append(table.tableF()).append(".class,column,value);").append(line(1))
				.append(tab(1)).append("}").append(line(2))
				.append(tab(1)).append("public static ArrayList<").append(table.tableF()).append("> list() {").append(line(1))
				.append(tab(2)).append("return ").append(table.tableF()).append(".all(").append(table.tableF()).append(".class);").append(line(1))
				.append(tab(1)).append("}").toString();
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
