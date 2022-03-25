# GnzlzDBModel
Simple orm for handling database connections

### Example Configuration
```*.java

public class ExampleMySQL extends DBConfiguration{

  @Override
  protected void initConnection(PropertiesConnection connection) {
    connection
      .dialect(MySQL).prefix("jdbc:mysql:").path("//localhost:3306/")
      .user("user").password("pass").dbname("name")
      .property("useSSL","false").property("serverTimezone","UTC");
  }

  @Override
  protected void initMigration(PropertiesMigration migration) {
    migration
      .add(MyMigration.class).add(MyMigration2.class);
  }

  @Override
  protected void initModel(PropertiesModel model) {
     model.modelPackage("db.mysql.modelo").refresh(false);
  }
}
```

### Example Migration
```*.java
public class MyTable extends DBMigration {

  @Override
  public String tableName() {
    return "TableName";
  }
  
  @Override
  public String packageName() {
    return "Module o Packege model";
  }

  @Override
  protected void initTable(PropertiesTable table) {
    table.primaryKey("id",INTEGER).notNull();
    table.column("column1",VARCHAR,100).notNull();
    table.column("column2",DECIMAL,"10,2").notNull();
  }
}
```

### Example Generate Models DB or Migration
```*.java
public static void main(String[] args) {
  // Method 1
  ACDataBase.autocode(ExampleMySQL.class);
  // Method 2
  DBConfiguration.configuration(ExampleMySQL.class).migrate();
}
```

### Example Using Models
```*.java
  //Find
  MyTable table = MyTable.find(1);  // find primary key
  
  MyTable table = MyTable.find("id",1);
  
  //Find In
  ArrayList<MyTable> tables = MyTable.find(1,2,3); // find primary key
  
  ArrayList<MyTable> tables = MyTable.find("column2",1,2,3);
  
  //Find All
  ArrayList<MyTable> tables = MyTable.all();
  
  // SET Method 1
  table.set("column1","string");
  // SET Method 2 AND Save
  table.column2(2.5).column1("string").save(); 
  
  // Delete
  table.delete(); 
  
  //Create AND Save
  new MyTable().column2(2.5).column1("string").save();
```

### Example Relations
```*.java
  // One To One - if a relationship exists, return Object MyTable2
  MyTable2 table = MyTable.hasOne(MyTable2.class); 
  
  // One To Many - if a relationship exists, return ArrayList MyTable
  ArrayList<MyTable> tables = MyTable2.hasMany(MyTable.class); 
  
  // Many To Many - if a relationship exists, return ArrayList MyTable2
  ArrayList<MyTable2> tables = MyTable.belongsToMany(MyTableIntermedial.class, MyTable2.class); 
  
```
