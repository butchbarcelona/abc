package proj.abc;

/**
 * Created by mbarcelona on 4/8/16.
 */
public enum StudentLevel {
  NURSERY,
  KINDER,
  PREP;

  public static StudentLevel getGrade(String grade){
    switch(grade.toLowerCase()){
      case "nursery": default:  return NURSERY;
      case "kinder": return KINDER;
      case "prep": return PREP;
    }
  }
}
