package proj.abc;

import proj.abc.game.GameMap;

/**
 * Created by mbarcelona on 4/8/16.
 */
public enum GameType {

  LETTER,
  COLOR,
  SHAPE,
  COUNTING,
  PATTERN,
  PUZZLE;


  public static GameType getGameKind(String gameKind){
    switch(gameKind.toLowerCase()){
      case "letter": case "letters": default:  return LETTER;
      case "color": case "colors":

        if(GameMap.getInstance().getUser().getLevel().toUpperCase().equals("PREP")){
          return PUZZLE;
        }else{
          return COLOR;
        }

      case "shape": case "shapes":

        if(GameMap.getInstance().getUser().getLevel().toUpperCase().equals("PREP")){
          return PATTERN;
        }else{
          return SHAPE;
        }
      case "counting": return COUNTING;
      case "pattern": case "patterns": return PATTERN;
      case "puzzle": case "puzzles":return PUZZLE;
    }
  }

  public static String getGameKindString(String gameKind){
    switch(gameKind.toLowerCase()){
      case "letter": default:  return "Letters";
      case "color":
        if(GameMap.getInstance().getUser().getLevel().toUpperCase().equals("PREP")){
          return "Puzzles";
        }else{
          return "Colors";
        }
      case "shape":
        if(GameMap.getInstance().getUser().getLevel().toUpperCase().equals("PREP")){
          return "Patterns";
        }else{
          return "Shapes";
        }
      case "counting": return "Counting";
      case "pattern": return "Patterns";
      case "puzzle":return "Puzzles";
    }
  }
}
