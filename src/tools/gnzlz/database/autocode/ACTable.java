package tools.gnzlz.database.autocode;

import java.util.ArrayList;

import tools.gnzlz.database.autocode.definition.Definition;
import tools.gnzlz.database.model.DBModel;

public class ACTable {

	/**
	 * name
	 */
	public final String name;

	/**
	 * type
	 */
	public final String type;

	/**
	 * remarks
	 */
	public final String remarks;

	/**
	 * typeCatalog
	 */
	public final String typeCatalog;

	/**
	 * typeScheme
	 */
	public final String typeScheme;

	/**
	 * typeName
	 */
	public final String typeName;

	/**
	 * columns
	 */
	public final ArrayList<ACColumn> columns;

	/**
	 * scheme
	 */
	public final ACScheme scheme;

	/**
	 * ACTable
	 * @param model model
	 * @param scheme scheme
	 */
	ACTable(DBModel<?> model, ACScheme scheme) {
		this.scheme = scheme;

		this.name = model.get(Definition.TABLE_NAME).stringValue();
		this.type = model.get(Definition.TABLE_TYPE).stringValue();
		this.remarks = model.get(Definition.REMARKS).stringValue();
		this.typeCatalog = model.get(Definition.TYPE_CAT).stringValue();
		this.typeScheme = model.get(Definition.TYPE_SCHEM).stringValue();
		this.typeName = model.get(Definition.TYPE_NAME).stringValue();

		columns = new ArrayList<>();
	}


	/**
	 * column
	 * @param name name
	 */
	 ACColumn column(DBModel<?> name) {
		 if (name != null) {
			 for (ACColumn column : columns) {
				 if (column.equals(name)) {
					 return column;
				 }
			 }
		 }
		 return null;
	 }

	/**
	 * column
	 * @param name name
	 */
	ACColumn column(String name) {
		if(name != null) {
			for (ACColumn column : columns) {
				if (column.name.equals(name)) {
					return column;
				}
			}
		}
		return null;
	}

	/**
	 * addColumns
	 * @param columns columns
	 * @param primaryKeys primaryKeys
	 */
	 void addColumns(ArrayList<DBModel<?>> columns, ArrayList<DBModel<?>> primaryKeys) {
		for (DBModel<?> columnModel : columns) {
			String columnName = columnModel.get(Definition.COLUMN_NAME).stringValue();
			DBModel<?> primaryKeyModel = null;
			for (DBModel<?> primary: primaryKeys) {
				if(primary.get(Definition.COLUMN_NAME).stringValue().equals(columnName)){
					primaryKeyModel = primary;
				}
			}

			ACColumn column = column(columnModel);
			if(column == null) {
				column = new ACColumn(columnModel, primaryKeyModel, this);
				this.columns.add(column);
			}
		}
	}

	/**
	 * pkColumn
	 * @param model model
	 */
	ACColumn pkColumn(DBModel<?> model){
		for (ACCatalog catalog : this.scheme.catalog.dataBase.catalogs) {
			if(catalog.name.equals(model.get(Definition.PKTABLE_CAT).stringValue())) {
				for (ACScheme scheme : this.scheme.catalog.schemes) {
					if(scheme.name.equals(model.get(Definition.PKTABLE_SCHEM).stringValue())) {
						for (ACTable table : scheme.tables) {
							if(table.name.equals(model.get(Definition.PKTABLE_NAME).stringValue())) {
								for (ACColumn column : table.columns) {
									if(column.name.equals(model.get(Definition.PKCOLUMN_NAME).stringValue())) {
										return column;
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * addRelation
	 * @param list list
	 */
	 void addRelation(ArrayList<DBModel<?>> list) {
		for (DBModel<?> model : list) {
			String columnName = model.get(Definition.FKCOLUMN_NAME).stringValue();
			ACColumn fkColumn = column(columnName);
			ACColumn pkColumn = pkColumn(model);
			if(pkColumn != null && fkColumn != null) {
				ACRelation relation = new ACRelation(model, pkColumn, fkColumn);

				pkColumn.addRelation(relation);
				fkColumn.addRelation(relation);
			} else {
				if(pkColumn == null) {
					System.out.println(columnName);
				}

				if(fkColumn == null) {
					System.out.println(columnName);
				}
			}
		}
	}

	/**
	 * primaryKey
	 */
	public ACColumn primaryKey() {
		for (ACColumn acColumn : columns) {
			if (acColumn.isPrimaryKey()) {
				return acColumn;
			}
		}
		return null;
	}

	/**
	 * isPrimaryKey
	 */
	public boolean isPrimaryKey() {
		for (ACColumn acColumn : columns) {
			if (acColumn.isPrimaryKey()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * hasOneImports
	 */
	public ArrayList<ACRelation> oneToOne() {
		ArrayList<ACRelation> relations = new ArrayList<>();
		for (ACColumn column : this.columns) {
			for (ACRelation relation : column.oneToOne()) {
				if (hasOneToOne(relations, relation))
					relations.add(relation);
			}
		}
		return relations;
	}

	/**
	 * hasOneExists
	 * @param relations relations
	 * @param relation relation
	 */
	boolean hasOneToOne(ArrayList<ACRelation> relations, ACRelation relation) {
		for (ACRelation relation2 : relations) {
			if (relation.pkColumn.table.name.equals(relation2.pkColumn.table.name))
				return false;
		}
		return true;
	}

	/**
	 * oneToMany
	 */
	public ArrayList<ACRelation> oneToMany() {
		ArrayList<ACRelation> relations = new ArrayList<>();
		for (ACColumn column : this.columns) {
			for (ACRelation relation : column.oneToMany()) {
				if (hasOneToMany(relations, relation))
					relations.add(relation);
			}
		}
		return relations;
	}

	/**
	 * hasOneToMany
	 * @param relations relations
	 * @param relation relation
	 */
	boolean hasOneToMany(ArrayList<ACRelation> relations, ACRelation relation) {
		for (ACRelation relation2 : relations) {
			if (relation.fkColumn.table.name.equals(relation2.fkColumn.table.name))
				return false;
		}
		return true;
	}

	/**
	 * allColumns
	 */
	public ArrayList<ACColumn> allColumns() {
		ArrayList<ACColumn> columns = new ArrayList<>();
		for (ACColumn column : this.columns) {
			for (ACRelation relation : column.oneToOne()) {
				if (existsColumn(columns, relation.pkColumn)) {
					columns.add(relation.pkColumn);
				}
				if (existsColumn(columns, relation.fkColumn)) {
					columns.add(relation.fkColumn);
				}
			}
			for (ACRelation relation : column.oneToMany()) {
				if (existsColumn(columns, relation.fkColumn)) {
					columns.add(relation.fkColumn);
				}
				if (existsColumn(columns, relation.pkColumn)) {
					columns.add(relation.pkColumn);
				}
			}
			for (ACManyToMany manyToMany : column.belongsToMany()) {
				if (existsColumn(columns, manyToMany.relation1.pkColumn)) {
					columns.add(manyToMany.relation1.pkColumn);
				}
				if (existsColumn(columns, manyToMany.relation1.fkColumn)) {
					columns.add(manyToMany.relation1.fkColumn);
				}
				if (existsColumn(columns, manyToMany.relation2.pkColumn)) {
					columns.add(manyToMany.relation2.pkColumn);
				}
				if (existsColumn(columns, manyToMany.relation2.fkColumn)) {
					columns.add(manyToMany.relation2.fkColumn);
				}
			}

			if (existsColumn(columns, column)) {
				columns.add(column);
			}
		}
		return columns;
	}

	/**
	 * existsColumn
	 * @param columns columns
	 * @param column column
	 */
	boolean existsColumn(ArrayList<ACColumn> columns, ACColumn column) {
		for (ACColumn column2 : columns) {
			if (column2.table.name.equals(column.table.name))
				return false;
		}
		return true;
	}
}
