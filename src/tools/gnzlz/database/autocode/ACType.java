package tools.gnzlz.database.autocode;

import tools.gnzlz.database.autocode.definition.Definition;
import tools.gnzlz.database.model.DBModel;

public class ACType {

    /**
     * type
     */
    public final String type;

    /**
     * args
     */
    public final String args;

    /**
     * ACColumn
     * @param model model
     */
    ACType(DBModel<?> model) {
        String type = model.get(Definition.TYPE_NAME).stringValue();

        String[] split = type.split(" ");
        this.type = split[0];
        this.args = split.length > 1 && !split[1].equals("()") ? (split[1].equals("UNSIGNED") ? "" : split[1]) : "";
    }

    /**
     * toString
     */
    @Override
    public String toString() {
        return this.type;
    }
}
