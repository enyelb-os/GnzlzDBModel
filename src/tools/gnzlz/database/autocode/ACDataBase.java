package tools.gnzlz.database.autocode;

import tools.gnzlz.database.model.DBConfiguration;
import tools.gnzlz.database.model.DBModel;

import java.util.ArrayList;

public class ACDataBase {

	/**
	 * configuration
	 */
	public final DBConfiguration configuration;

	/**
	 * catalogs
	 */
	public final ArrayList<ACCatalog> catalogs;

	/**
	 * configuration
	 * @param <T> t
	 * @param configuration c
	 * @param catalogName name
	 */
	<T extends DBConfiguration> ACDataBase(DBConfiguration configuration, String catalogName) {
		this.configuration = configuration;
		this.catalogs = new ArrayList<>();

		ArrayList<DBModel<?>> catalogsModel = this.configuration.connection().catalogs();
		for (DBModel<?> cat: catalogsModel) {
			ACCatalog catalog = new ACCatalog(cat, this);
			if (catalogName.isEmpty() || catalogName.equals(catalog.name)) {
				System.out.println(catalog.name);
				ArrayList<DBModel<?>> schemesModel = this.configuration.connection().schemes(catalog.name);
				for (DBModel<?> schemeModel: schemesModel) {
					ACScheme scheme = new ACScheme(schemeModel, catalog);
					ArrayList<DBModel<?>> tablesModel = this.configuration.connection().tables(catalog.name, scheme.name);
					for (DBModel<?> tableModel : tablesModel) {
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
		}

		// Relations
		for (ACCatalog catalog: catalogs) {
			for (ACScheme scheme: catalog.schemes) {
				for (ACTable table: scheme.tables) {
					table.addRelation(this.configuration.connection().importedKeys(catalog.name, scheme.name, table.name));
				}
			}
		}
	}

	/**
	 * dataBase
	 * @param <T> t
	 * @param c c
	 */
	public static <T extends DBConfiguration> ACDataBase dataBase(Class<T> c) {
		return new ACDataBase(DBConfiguration.configuration(c),"");
	}

	/**
	 * dataBase
	 * @param <T> t
	 * @param c c
	 * @param catalog c
	 */
	public static <T extends DBConfiguration> ACDataBase dataBase(Class<T> c, String catalog) {
		return new ACDataBase(DBConfiguration.configuration(c), catalog);
	}

}
