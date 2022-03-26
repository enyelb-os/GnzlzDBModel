package tools.gnzlz.database.form;

import tools.gnzlz.validation.Validation;

import java.util.ArrayList;

public class DBFormColumn {

    protected final String name;
    protected ArrayList<Validation> validations;
    protected String label;

    public DBFormColumn(String name) {
        this.name = name;
    }

    public static DBFormColumn create(String name){
        return new DBFormColumn(name);
    }

    public String name() {
        return name;
    }

    public ArrayList<Validation> validations(){
        if (validations == null) validations = new ArrayList<Validation>();
        return validations;
    }

    public DBFormColumn validation(Validation ... validations) {
        if(validations != null){
            for (Validation validation: validations) {
                validations().add(validation);
            }
        }
        return this;
    }

    public DBFormColumn label(String label) {
        this.label = label;
        return this;
    }

    public String label() {
        return label;
    }
}
