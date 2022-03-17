package tools.gnzlz.database.query.model.builder;

import tools.gnzlz.database.model.DBModel;
import tools.gnzlz.database.model.DBQuery;

@FunctionalInterface
public interface IExecute<Q extends Query<Q,M>,M extends DBModel<M>>{

    DBQuery<M> execute();
}
