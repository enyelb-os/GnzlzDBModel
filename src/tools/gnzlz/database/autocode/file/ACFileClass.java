package tools.gnzlz.database.autocode.file;

import tools.gnzlz.database.autocode.ACFormat;

import java.util.ArrayList;

public class ACFileClass {

    private String className;
    private String extend;
    private String packageName;
    private ArrayList<String> imposts;
    private ArrayList<String> implemensts;
    private ArrayList<ACConstructor> constructors;

    public static ACFileClass create() {
        return new ACFileClass();
    }

    public ACFileClass() {
        imposts = new ArrayList<String>();
        implemensts = new ArrayList<String>();
    }

    /****************************
     * className
     ***************************/

    public ACFileClass className(String className) {
        this.className = className;
        return this;
    }

    public StringBuilder className(StringBuilder content) {
        return new StringBuilder("public class ").append(className)
                .append(extend()).append(implemensts()).append("{").append(ACFormat.line(2))
                .append(content).append(ACFormat.line(1))
                .append("}");
    }

    /****************************
     * extend
     ***************************/

    public ACFileClass extend(String extend) {
        this.extend = extend;
        return this;
    }

    public StringBuilder extend() {
        if(extend != null && !extend.isEmpty())
            return new StringBuilder(" extends ").append(extend);
        else
            return new StringBuilder();
    }

    /****************************
     * implements
     ***************************/

    public ACFileClass implemensts(String implemensts) {
        this.implemensts.add(implemensts);
        return this;
    }

    public StringBuilder implemensts() {
        StringBuilder builder = new StringBuilder();
        if(!implemensts.isEmpty()){
            builder.append(" implements ");
            for (int i = 0; i < implemensts.size(); i++) {
                builder.append(implemensts.get(i));
                if(i != implemensts.size() - 1) builder.append(", ");
            }
        }

        return builder;

    }

    /****************************
     * packageName
     ****************************/

    public ACFileClass packageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public StringBuilder packageName() {
        return new StringBuilder("package ").append(packageName).append(";");
    }

    /****************************
     * imposts
     ****************************/

    public ACFileClass imposts(String imposts) {
        this.imposts.add(imposts);
        return this;
    }

    public StringBuilder imposts() {
        StringBuilder sb = new StringBuilder();
        for (String s : imposts) {
            sb.append("import ").append(s).append(";").append(ACFormat.line(1));
        }
        return sb;
    }

    /****************************
     * implements
     ****************************/
}
