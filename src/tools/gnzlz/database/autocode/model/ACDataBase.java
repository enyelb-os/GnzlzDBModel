package tools.gnzlz.database.autocode.model;

import java.util.ArrayList;

import tools.gnzlz.database.model.DBConfiguration;
import tools.gnzlz.database.model.DBModel;

public class ACDataBase {

	/****************************
	 * vars
	 ****************************/

	public final DBConfiguration configuration;
	public final ArrayList<ACCatalog> catalogs;
	
	public static <T extends DBConfiguration> ACDataBase dataBase(Class<T> c) {
		return new ACDataBase(DBConfiguration.configuration(c),"");
	}

	public static <T extends DBConfiguration> ACDataBase dataBase(Class<T> c, String catalog) {
		return new ACDataBase(DBConfiguration.configuration(c), catalog);
	}

	/****************************
	 * constructor
	 ****************************/

	<T extends DBConfiguration> ACDataBase(DBConfiguration configuration, String catalogName) {
		this.configuration = configuration;
		this.catalogs = new ArrayList<ACCatalog>();

		ArrayList<DBModel<?>> catalosModel = this.configuration.connection().catalogs();
		for (DBModel cat: catalosModel) {
			ACCatalog catalog = new ACCatalog(cat, this);
			if (!catalogName.isEmpty() && !catalogName.equals(catalog.name))
				continue;
			ArrayList<DBModel<?>> schemesModel = this.configuration.connection().schemes(catalog.name);
			for (DBModel schemeModel: schemesModel) {
				ACScheme scheme = new ACScheme(schemeModel, catalog);
				ArrayList<DBModel<?>> tablesModel = this.configuration.connection().tables(catalog.name, scheme.name);
				for (DBModel tableModel : tablesModel) {
					ACTable table = new ACTable(tableModel, scheme);
					table.addColumns(
						this.configuration.connection().columns(catalog.name, scheme.name, table.name),
						this.configuration.connection().primaryKeys(catalog.name, scheme.name, table.name)
					);
					scheme.tables.add(table);
				}
				catalog.schemes.add(scheme);
			}
			catalogs.add(catalog);
		}

		// Relations
		for (ACCatalog catalog: catalogs) {
			for (ACScheme scheme: catalog.schemes) {
				for (ACTable table: scheme.tables) {
					table.addRelation(this.configuration.connection().importedKeys(catalog.name, scheme.name, table.name));
				}
			}
		}

		//ACFileBaseModel.createFile(this);
		//ACFileCustomModel.createFile(this);
		//ACFileModel.createFile(this);
		//ACFileAttributeModel.createFile(this);
	}
}
