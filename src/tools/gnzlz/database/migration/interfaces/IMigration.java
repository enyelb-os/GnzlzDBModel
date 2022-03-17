package tools.gnzlz.database.migration.interfaces;

import tools.gnzlz.database.properties.PropertiesTable;

@FunctionalInterface
public interface IMigration {

    void initTable(PropertiesTable table);
}
