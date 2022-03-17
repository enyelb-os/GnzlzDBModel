package tools.gnzlz.database.autocode;

public class ACRelation {
	
	private String nameColumn;
	private String relations;
	
	ACRelation(String column, String relations) {
		this.relations = relations;
		this.nameColumn = column;
	}
	
	String column() {
		return nameColumn;
	}
	
	String columnUpperCase() {
		return nameColumn.toUpperCase();
	}
	
	String relationColumnUpperCase() {
		return relationCamelCase().concat(".").concat(columnUpperCase());
	}
	
	String relation() {
		return relations;
	}
	
	String relationCamelCase() {
		return ACFormat.camelCaseClass(relations);
	}
	
	String relationCamelCaseClass() {
		return relationCamelCase().concat(".class");
	}
}
