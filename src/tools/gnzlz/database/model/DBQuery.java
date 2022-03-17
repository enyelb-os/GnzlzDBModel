package tools.gnzlz.database.model;

import tools.gnzlz.database.query.model.builder.Query;

import java.util.ArrayList;

public class DBQuery<M extends DBModel<M>> {

    private M model;
    private DBExecuteQuery executeQuery;

    DBQuery(DBConnection connection, M model, Query<?,M> query) {
        this.executeQuery = connection.query(query);
        this.model = model;
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
        executeQuery.executeID(model);
    }

    /**********************
     * ExecuteQuery
     **********************/

    public ArrayList<M> executeQuery() {
        return (ArrayList<M>) executeQuery.executeQuery(model.getClass());
    }

    /**********************
     * ExecuteSingel
     **********************/

    public M executeSingle() {
        return executeQuery.executeSingle(model);
    }
}