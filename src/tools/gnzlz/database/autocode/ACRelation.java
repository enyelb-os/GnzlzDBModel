package tools.gnzlz.database.autocode;

public class ACRelation {
	
	private String nameColumn;
	private String relations;
	
	public ACRelation(String column, String relations) {
		this.relations = relations;
		this.nameColumn = column;
	}
	
	public ACRelation(String relations) {
		this.relations = relations;
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
		return ACFormat.camelCaseClass(relations).concat(".class");
	}
}
