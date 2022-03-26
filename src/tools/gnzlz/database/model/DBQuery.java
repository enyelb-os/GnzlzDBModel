package tools.gnzlz.database.model;

import tools.gnzlz.database.query.model.builder.Query;

import java.util.ArrayList;

public class DBQuery<M extends DBModel<M>> {

    private M model;
    private Class<M> c;
    private DBExecuteQuery executeQuery;

    DBQuery(DBConnection connection, M model, Query<?,M> query) {
        this.executeQuery = connection.query(query);
        this.model = model;
        this.c = (Class<M>) model.getClass();
    }
    DBQuery(DBConnection connection, Class<M> c, Query<?,M> query) {
        this.executeQuery = connection.query(query);
        this.model = null;
        this.c = c;
    }

    /**********************
     * Execute
     **********************/

    public boolean execute() {
        return executeQuery.execute();
    }

    /**********************
     * Execute
     **********************/

    public void executeID() {
        if(model == null) executeQuery.executeID();
        else executeQuery.executeID(model);
    }

    /**********************
     * ExecuteQuery
     **********************/

    public ArrayList<M> executeQuery() {
        return (ArrayList<M>) executeQuery.executeQuery(c);
    }

    /**********************
     * ExecuteSingel
     **********************/

    public M executeSingle() {
        return model == null ? executeQuery.executeSingle(c) : executeQuery.executeSingle(model);
    }
}