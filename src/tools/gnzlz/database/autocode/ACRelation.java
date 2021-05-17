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
	
	String relation() {
		return relations;
	}
	
	String relationF() {
		return ACFormat.camelCaseClass(relations);
	}
}
