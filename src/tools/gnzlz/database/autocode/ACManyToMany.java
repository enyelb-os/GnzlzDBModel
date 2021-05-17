package tools.gnzlz.database.autocode;

public class ACManyToMany {
	
	private String internalKey1;
	private String relationInternal;
	private String internalKey2;
	private String relationForeign;
	private String foreignKey;
	
	public ACManyToMany(String internalKey1, String relationInternal, String internalKey2, String relationForeign, String foreignKey) {
		this(internalKey1, relationInternal, internalKey2, relationForeign);
		this.foreignKey = foreignKey;
	}
	
	public ACManyToMany(String internalKey1, String relationInternal, String internalKey2, String relationForeign) {
		this.internalKey1 = internalKey1;
		this.relationInternal = relationInternal;
		this.internalKey2 = internalKey2;
		this.relationForeign = relationForeign;
	}
	
	String internalKey1() {
		return internalKey1;
	}
	
	String internalKey2() {
		return internalKey2;
	}
	
	String foreignKey() {
		return foreignKey;
	}
	
	String relationInternal() {
		return relationInternal;
	}
	
	String relationForeign() {
		return relationForeign;
	}
	
	String relationInternalF() {
		return ACFormat.camelCaseClass(relationInternal);
	}
	
	String relationForeignF() {
		return ACFormat.camelCaseClass(relationForeign);
	}
}
