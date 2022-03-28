package tools.gnzlz.database.autocode.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import tools.gnzlz.database.autocode.ACFormat;
import tools.gnzlz.database.model.DBModel;
import tools.gnzlz.database.model.DBTable;
import tools.gnzlz.database.query.model.Select;

public class ACFileBaseModel {
	
	public static void createFile(ACDataBase dataBase) {
		try {
			for (ACTable table : dataBase.tables()) {
				System.out.println("creating model "+nameF(table)+" | path:" + path(dataBase,table)+ ACFileModel.nameF(table)+".java");
				File file = new File(path(dataBase, table)+nameF(table)+".java");
				if(!file.exists())
					Files.createFile(file.toPath());
				FileWriter fileWriter = new FileWriter(file.toString());
				fileWriter.write(packages(dataBase, table));
				fileWriter.write(line(2));
				fileWriter.write(imports(dataBase,table));
				fileWriter.write(line(1));
				fileWriter.write(dbModelName(table));
				fileWriter.write(line(2));
				fileWriter.write(tableName(table));
				fileWriter.write(line(1));
				fileWriter.write(columnsVars(table));
				fileWriter.write(line(2));
				fileWriter.write(dbTableConfig(table));
				fileWriter.write(line(2));
				fileWriter.write(constructor(table));
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

	static String modelPackage(ACDataBase dataBase, ACTable table) {
		if(dataBase.configuration.model().modelPackage()!=null)
			return dataBase.configuration.model().modelPackage().concat(table.packegeName()).concat(".base");
		else 
			return dataBase.configuration.getClass().getPackage().getName().concat(".model").concat(table.packegeName()).concat(".base");
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
		return "Base";
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
	
	private static String extraImports(ACTable table) {
		StringBuilder string = new StringBuilder();
		ArrayList<String> imports = new ArrayList<String>();
		for (ACColumn column : table.columns()) {
			String newImport = ACFormat.imports(column.getType());
			existsImport(imports,newImport,string);
		}
		return string.toString();
	}

	/********************************
	 * Imports
	 ********************************/

	private static String relationsImports(ACDataBase dataBase, ACTable table) {
		StringBuilder string = new StringBuilder();
		ArrayList<String> imports = new ArrayList<String>();
		for (ACColumn column : table.columns()) {
			for (ACRelation relation : column.hasOnes()) {
				String newImport = "import ".concat(ACFileModel.modelPackage(dataBase,dataBase.table(relation.relation()))).concat(".").concat(relation.relationCamelCase()).concat(";");
				existsImport(imports,newImport,string);
			}
			for (ACRelation relation : column.hasManys()) {
				String newImport = "import ".concat(ACFileModel.modelPackage(dataBase,dataBase.table(relation.relation()))).concat(".").concat(relation.relationCamelCase()).concat(";");
				existsImport(imports,newImport,string);
			}
			for (ACManyToMany relation : column.belongsToManys()) {
				String newImport1 = "import ".concat(ACFileModel.modelPackage(dataBase,dataBase.table(relation.relationForeign()))).concat(".").concat(relation.relationForeignCamelCase()).concat(";");
				String newImport2 = "import ".concat(ACFileModel.modelPackage(dataBase,dataBase.table(relation.relationInternal()))).concat(".").concat(relation.relationInternalCamelCase()).concat(";");
				existsImport(imports,newImport1,string);
				existsImport(imports,newImport2,string);
			}
		}
		String newImport1 = "import ".concat(ACFileModel.modelPackage(dataBase,dataBase.table(table.table()))).concat(".").concat(ACFileModel.nameF(table)).concat(";");
		existsImport(imports,newImport1,string);
		return string.toString();
	}
	
	private static String imports(ACDataBase dataBase,ACTable table) {
		return new StringBuilder()
				.append(extraImports(table))
				.append("import ").append(ArrayList.class.getPackage().getName()).append(".").append(ArrayList.class.getSimpleName()).append(";").append(line(2))
				.append("import ").append(dataBase.configuration.getClass().getPackage().getName()).append(".").append(dataBase.configuration.getClass().getSimpleName()).append(";").append(line(1))
				.append(relationsImports(dataBase,table))
				.append("import ").append(DBTable.class.getPackage().getName()).append(".").append(DBTable.class.getSimpleName()).append(";").append(line(1))
				.append("import ").append(DBModel.class.getPackage().getName()).append(".").append(DBModel.class.getSimpleName()).append(";").append(line(1))
				.append("import ").append(Select.class.getPackage().getName()).append(".").append(Select.class.getSimpleName()).append(";").append(line(1)).toString();
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
	
	private static String dbModelName(ACTable table) {
		return new StringBuilder().append("public class ").append(nameF(table)).append("<Type extends ").append(DBModel.class.getSimpleName()).append("<Type>> ")
				.append(" extends ").append(DBModel.class.getSimpleName()).append("<Type> {").toString();
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
		return ".addTable(TABLE)";
	}
	
	/********************************
	 * addPrimaryKey
	 ********************************/
	
	private static String addPrimaryKey(ACTable table) {
		ACColumn column = table.primaryKey();
		if(column == null)
			return "";
		return new StringBuilder().append(".addPrimaryKey(").append(column.nameUpperCase()).append(")").toString();
	}
	
	/********************************
	 * addColumns
	 ********************************/
	
	private static String addColumns(ACTable table) {
		ArrayList<ACColumn> columns = table.columns();
		if(!columns.isEmpty()){
			StringBuilder string = new StringBuilder();
			for (int i = 0; i < columns.size(); i++) {
				string.append(columns.get(i).nameUpperCase());
				if(i != columns.size() - 1) string.append(", ");
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
		return new StringBuilder().append(".addHasOne(")
				.append(column.nameUpperCase()).append(", ")
				.append(relation.relationCamelCaseClass()).append(", ")
				.append(relation.relationColumnUpperCase())
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
		return new StringBuilder().append(".addHasMany(")
				.append(column.nameUpperCase()).append(", ")
				.append(relation.relationCamelCaseClass()).append(", ")
				.append(relation.relationColumnUpperCase())
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
		return new StringBuilder().append(".addBelongsToMany(")
				.append(column.nameUpperCase()).append(", ")
				.append(relation.relationInternalKey1UpperCase()).append(", ")
				.append(relation.relationInternalCamelCaseClass()).append(", ")
				.append(relation.relationInternalKey2UpperCase()).append(", ")
				.append(relation.relationForeignCamelCaseClass()).append(", ")
				.append(relation.relationForeignKeyUpperCase())
				.append(")").toString();
	}

	/********************************
	 * tableName
	 ********************************/

	private static String tableName(ACTable table) {
		return new StringBuilder().append(tab(1)).append("public static final String TABLE = \"").append(table.table()).append("\";").toString();
	}
	
	/********************************
	 * columnsVar
	 ********************************/
	
	private static String columnsVars(ACTable table) {
		ArrayList<ACColumn> columns = table.columns();
		if(!columns.isEmpty()){
			StringBuilder string = new StringBuilder();
			for (int i = 0; i < columns.size(); i++) {
				string.append(tab(1)).append("public static final String ").append(columns.get(i).nameUpperCase()).append(" = ")
					.append("\"").append(columns.get(i).name()).append("\";");
				if(i != columns.size() - 1) string.append(line(1));
			}
			
			return string.toString();
		}
		return "";
	}

	/********************************
	 * constructor
	 ********************************/
	
	private static String constructor(ACTable table) {
		return new StringBuilder().append(tab(1)).append("public ").append(nameF(table)).append("() {").append(line(1))
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
	
	private static String method(ACTable table,ACColumn column) {
		String type = ACFormat.typeData(column.type());
		String value = ACFormat.typeValue(column.type());

		StringBuilder str = new StringBuilder();
		if(ACFormat.dateFormat(column.type())){
			str.append(tab(1)).append("public Type ").append(column.nameCamelCase()).append("(String  ").append(column.nameCamelCase()).append(") {").append(line(1))
				.append(tab(2)).append("set(").append(column.nameUpperCase()).append(", this.dateParse(").append(column.nameCamelCase()).append("));").append(line(1))
				.append(tab(2)).append("return (Type) this;").append(line(1)).append(tab(1)).append("}").append(line(2));
		}
		
		return str
				.append(tab(1)).append("public ").append(type).append(" ").append(column.nameCamelCase()).append("() {").append(line(1))
				.append(tab(2)).append("return get(").append(column.nameUpperCase()).append(").").append(value).append(line(1))
				.append(tab(1)).append("}").append(line(2))
				.append(tab(1)).append("public Type ").append(column.nameCamelCase()).append("(").append(type).append(" ").append(column.nameCamelCase()).append(") {").append(line(1))
				.append(tab(2)).append("set(").append(column.nameUpperCase()).append(", ").append(column.nameCamelCase()).append(");").append(line(1))
				.append(tab(2)).append("return (Type) this;").append(line(1)).append(tab(1)).append("}").toString();
	}
	
	/********************************
	 * statics
	 ********************************/
	
	private static String statics(ACTable table) {
		return new StringBuilder()
				.append(tab(1)).append("public static ").append(ACFileModel.nameF(table)).append(" find(Object primaryKey) {").append(line(1))
				.append(tab(2)).append("return ").append(ACFileModel.nameF(table)).append(".find(").append(ACFileModel.nameF(table)).append(".class,DBTABLE,primaryKey);").append(line(1))
				.append(tab(1)).append("}").append(line(2))
				.append(tab(1)).append("public static ").append(ACFileModel.nameF(table)).append(" find(String column, Object value) {").append(line(1))
				.append(tab(2)).append("return ").append(ACFileModel.nameF(table)).append(".find(").append(ACFileModel.nameF(table)).append(".class,DBTABLE,column,value);").append(line(1))
				.append(tab(1)).append("}").append(line(2))
				.append(tab(1)).append("public static ").append(ArrayList.class.getSimpleName()).append("<").append(ACFileModel.nameF(table)).append("> find(Object ... primaryKeys) {").append(line(1))
				.append(tab(2)).append("return ").append(ACFileModel.nameF(table)).append(".findIn(").append(ACFileModel.nameF(table)).append(".class,DBTABLE,primaryKeys);").append(line(1))
				.append(tab(1)).append("}").append(line(2))
				.append(tab(1)).append("public static ").append(ArrayList.class.getSimpleName()).append("<").append(ACFileModel.nameF(table)).append("> find(String column,Object ... values) {").append(line(1))
				.append(tab(2)).append("return ").append(ACFileModel.nameF(table)).append(".findIn(").append(ACFileModel.nameF(table)).append(".class,DBTABLE,column,values);").append(line(1))
				.append(tab(1)).append("}").append(line(2))
				.append(tab(1)).append("public static ").append(ArrayList.class.getSimpleName()).append("<").append(ACFileModel.nameF(table)).append("> findAll(String column,Object value) {").append(line(1))
				.append(tab(2)).append("return ").append(ACFileModel.nameF(table)).append(".findAll(").append(ACFileModel.nameF(table)).append(".class,DBTABLE,column,value);").append(line(1))
				.append(tab(1)).append("}").append(line(2))
				.append(tab(1)).append("public static ").append(ArrayList.class.getSimpleName()).append("<").append(ACFileModel.nameF(table)).append("> list() {").append(line(1))
				.append(tab(2)).append("return ").append(ACFileModel.nameF(table)).append(".all(").append(ACFileModel.nameF(table)).append(".class,DBTABLE);").append(line(1))
				.append(tab(1)).append("}").append(line(2))
				.append(tab(1)).append("public static ").append(Select.class.getSimpleName()).append("<").append(ACFileModel.nameF(table)).append("> selects() {").append(line(1))
				.append(tab(2)).append("return ").append(ACFileModel.nameF(table)).append(".select(").append(ACFileModel.nameF(table)).append(".class,DBTABLE);").append(line(1))
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
