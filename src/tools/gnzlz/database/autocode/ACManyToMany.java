package tools.gnzlz.database.autocode;

public class ACManyToMany {

    /**
     * relation1
     */
    public final ACRelation relation1;

    /**
     * relation2
     */
    public final ACRelation relation2;

    /**
     * ACManyToMany
     * @param relation1 r
     * @param relation2 r
     */
    ACManyToMany(ACRelation relation1, ACRelation relation2){
        this.relation1 = relation1;
        this.relation2 = relation2;
    }

}
