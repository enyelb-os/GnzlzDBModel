package tools.gnzlz.database.autocode.migration;

import tools.gnzlz.database.autocode.ACFormat;

public class ACManyToMany {
	
	private String internalKey1;
	private String relationInternal;
	private String internalKey2;
	private String relationForeign;
	private String foreignKey;
	
	ACManyToMany(String internalKey1, String relationInternal, String internalKey2, String relationForeign, String foreignKey) {
		this.internalKey1 = internalKey1;
		this.relationInternal = relationInternal;
		this.internalKey2 = internalKey2;
		this.relationForeign = relationForeign;
		this.foreignKey = foreignKey;
	}
	
	String internalKey1() {
		return internalKey1;
	}
	
	String internalKey1UpperCase() {
		return internalKey1.toUpperCase();
	}
	
	String relationInternalKey1UpperCase() {
		return relationInternalCamelCase().concat(".").concat(internalKey1UpperCase());
	}
	
	String internalKey2() {
		return internalKey2;
	}
	
	String internalKey2UpperCase() {
		return internalKey2.toUpperCase();
	}
	
	String relationInternalKey2UpperCase() {
		return relationInternalCamelCase().concat(".").concat(internalKey2UpperCase());
	}
	
	String foreignKey() {
		return foreignKey;
	}
	
	String foreignKeyUpperCase() {
		return foreignKey.toUpperCase();
	}
	
	String relationForeignKeyUpperCase() {
		return relationForeignCamelCase().concat(".").concat(foreignKeyUpperCase());
	}
	
	String relationInternal() {
		return relationInternal;
	}
	
	String relationForeign() {
		return relationForeign;
	}
	
	String relationInternalCamelCase() {
		return ACFormat.camelCaseClass(relationInternal);
	}
	
	String relationInternalCamelCaseClass() {
		return ACFormat.camelCaseClass(relationInternal).concat(".class");
	}
	
	String relationForeignCamelCase() {
		return ACFormat.camelCaseClass(relationForeign);
	}
	
	String relationForeignCamelCaseClass() {
		return ACFormat.camelCaseClass(relationForeign).concat(".class");
	}
}
