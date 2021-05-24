package tools.gnzlz.database.query.migration.builder;

import tools.gnzlz.database.model.interfaces.Dialect;
import tools.gnzlz.database.model.interfaces.IDialects;

import java.util.ArrayList;

public abstract class Query<Q extends Query<?>>{

	private Dialect dialect = IDialects.dialectDefault();

	protected Query(){}
	
	public abstract String query();

	/*************************
	 * dialect
	 *************************/

	public Dialect dialect() {
		return dialect;
	}

	public Q dialect(Dialect dialect) {
		this.dialect = dialect;
		return (Q) this;
	}
}
