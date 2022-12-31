package tools.gnzlz.database.autocode.model;

public class ACManyToMany {

    /****************************
     * vars
     ****************************/

    public final ACRelation relation1;
    public final ACRelation relation2;

    /****************************
     * Constructor
     ****************************/

    ACManyToMany(ACRelation relation1, ACRelation relation2){
        this.relation1 = relation1;
        this.relation2 = relation2;
    }

}
