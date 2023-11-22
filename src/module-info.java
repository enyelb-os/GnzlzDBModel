module tools.gnzlz.database {
    requires java.sql;

    exports tools.gnzlz.database.autocode;
    exports tools.gnzlz.database.model;
    exports tools.gnzlz.database.model.interfaces;
    exports tools.gnzlz.database.query.model;
    exports tools.gnzlz.database.properties;
    exports tools.gnzlz.database.migration;
}