package proj.abc.models;

/**
 * Created by mbarcelona on 4/14/16.
 */
public class User {

  public String name, fName, lName,  level;
  public int id;



  public User(String name, String firstName, String lastName, String level, int id){
    this.name = name;
    this.level = level;
    this.id = id;
    this.fName = firstName;
    this.lName = lastName;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }






}
