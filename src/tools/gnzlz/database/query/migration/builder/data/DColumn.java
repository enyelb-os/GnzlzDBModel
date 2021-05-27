package tools.gnzlz.database.query.migration.builder.data;

import tools.gnzlz.database.model.interfaces.Dialect;
import tools.gnzlz.database.model.interfaces.IDialects;
import tools.gnzlz.database.query.migration.builder.Query;

public class DColumn {

	private Query<?> query;

	public String column;
	public String type;
	public boolean primaryKey;
	public boolean unique;
	public boolean autoincrement;
	public boolean notNull;
	public String length;
	public Number max;
	public Number min;
	public String isDefault;
	public DForeignKey foreignKey;
	
	public DColumn(String column,Query<?> query) {
		this.column = column;
		this.query = query;
	}

	public String isPrimaryKey() {
		return primaryKey ? " PRIMARY KEY":"";
	}

	public String isAutoincrement() {
		return autoincrement ? (
				query.dialect() == Dialect.PostgreSQL ? "" :
				query.dialect() == Dialect.SQLite ? " AUTOINCREMENT" :
											  " AUTO_INCREMENT"
		):"";
	}

	public String isNotNull() {
		return notNull ? " NOT NULL":"";
	}

	public String isLength() {
		return length.isEmpty() ? "" : " (".concat(length).concat(")");
	}

	public String isUnique() {
		return unique ? " UNIQUE":"";
	}

	public String isDefault() {
		return isDefault == null ? "" : " DEFAULT ".concat(isDefault);
	}

	public String type(){
		return primaryKey && query.dialect() == Dialect.PostgreSQL ? "SERIAL" : type;
	}

	public String column() {
		return column.concat(" ").concat(type()).concat(isLength()).concat(isPrimaryKey()).concat(isAutoincrement()).concat(isNotNull()).concat(isUnique()).concat(isDefault());
	}

	public String foreignKey() {
		if(foreignKey == null)
			return "";
		else
			return "FOREIGN KEY(".concat(column).concat(") REFERENCES ").concat(foreignKey.table).concat("(").concat(foreignKey.column).concat(")");
	}

	@Override
	public String toString() {
		return column();
	}
}