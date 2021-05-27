package tools.gnzlz.database.model.interfaces;

public interface IDialects {

    public static final Dialect SQLite = Dialect.SQLite;
    public static final Dialect MySQL = Dialect.MySQL;
    public static final Dialect PostgreSQL = Dialect.PostgreSQL;

    public static Dialect dialectDefault(){
        return MySQL;
    }
}
