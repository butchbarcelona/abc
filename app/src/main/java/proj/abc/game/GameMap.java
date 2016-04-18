package proj.abc.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import proj.abc.GameType;
import proj.abc.R;
import proj.abc.StudentLevel;
import proj.abc.models.User;

/**
 * Created by mbarcelona on 4/9/16.
 */
public class GameMap {

  private static GameMap instance = new GameMap();


  public static StudentLevel studentLevel;
  public static GameType gameType;
  public User user;

  HashMap<String, Integer> letterData;
  HashMap<Integer, String> colorData, shapeData;

  private GameMap() {
    setUpLetterGameData();

  }

  public static GameMap getInstance() {
    return instance;
  }


  public void setUser(User user){
    this.user = user;
    this.studentLevel = StudentLevel.getGrade(user.getLevel());
  }

  public User getUser(){
    return this.user;
  }


  ArrayList<GameData> gameDatas;

  public GameData getRandomFromGameData() {
    if (gameDatas != null && !gameDatas.isEmpty()) {
      int randNum = (int) (Math.random() * gameDatas.size());

      GameData gameData = gameDatas.get(randNum);
      gameDatas.remove(randNum);

      return gameData;
    }
    return null;
  }



  public int getRandomBitmap() {

    if (letterData == null || letterData.isEmpty()) {
      setUpLetterGameData();
    }

    int randPicIndex = (int) (Math.random() * letterData.size());
    int count = 0;

    Iterator it = letterData.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry pair = (Map.Entry) it.next();
      if (count == randPicIndex) {
        return (int) pair.getValue();

      }

      it.remove();
      count++;
    }

    return R.drawable.rabbit_letter;
  }


  public HashMap<Integer, String> getShapeGameData() {
    setUpShapeGameData();
    return shapeData;
  }

  public HashMap<Integer, String> getColorOutlineData() {
    setUpColorOutlineGameData();
    return colorData;
  }

  public HashMap<Integer, String> getColorData() {
    setUpColorGameData();
    return colorData;
  }

  public int getRandomShapeBitmap(ShapeType shape) {

    switch (shape) {

      case TRIANGLE:
        ArrayList<Integer> triangles = new ArrayList<Integer>();
        triangles.add(R.drawable.t_berry);
        triangles.add(R.drawable.t_cake);
        triangles.add(R.drawable.t_carrot);
        triangles.add(R.drawable.t_triangle);
        triangles.add(R.drawable.t_mountain);
        triangles.add(R.drawable.t_hanger);
        triangles.add(R.drawable.t_cone);
        triangles.add(R.drawable.t_hat);
        triangles.add(R.drawable.t_icecream);
        triangles.add(R.drawable.t_cheese);

        //TODO: add more
        return triangles.get((int) (Math.random()*triangles.size()));
      case CIRCLE:
        ArrayList<Integer> circles = new ArrayList<Integer>();
        circles.add(R.drawable.c_ball);
        circles.add(R.drawable.c_ballon);
        circles.add(R.drawable.c_baseball);
        circles.add(R.drawable.c_billiards);
        circles.add(R.drawable.c_burger);
        circles.add(R.drawable.c_button);
        circles.add(R.drawable.c_cake);
        circles.add(R.drawable.c_cherry);
        circles.add(R.drawable.c_clock);
        circles.add(R.drawable.c_cookie);

        //TODO: add more

        return circles.get((int) (Math.random()*circles.size()));
      case SQUARE:
        ArrayList<Integer> square = new ArrayList<Integer>();
        square.add(R.drawable.s_book);
        square.add(R.drawable.s_box);
        square.add(R.drawable.s_chocolate);
        square.add(R.drawable.s_drink);
        square.add(R.drawable.s_dice);
        square.add(R.drawable.s_ipod);
        square.add(R.drawable.s_paper);
        square.add(R.drawable.s_chest);
        square.add(R.drawable.s_gift);
        //TODO: add more

        return square.get((int) (Math.random()*square.size()));
    }

    return 0;

  }

  public ArrayList<Integer> setUpPuzzleGameData() {
    ArrayList<Integer> puzzleData = new ArrayList<Integer>();
    puzzleData.add(R.drawable.puzzle1);
    puzzleData.add(R.drawable.puzzle2);
    puzzleData.add(R.drawable.puzzle3);
    puzzleData.add(R.drawable.puzzle4);
    puzzleData.add(R.drawable.puzzle5);
    puzzleData.add(R.drawable.puzzle6);
    puzzleData.add(R.drawable.puzzle7);
    puzzleData.add(R.drawable.puzzle8);
    puzzleData.add(R.drawable.puzzle9);
    puzzleData.add(R.drawable.puzzle10);
    puzzleData.add(R.drawable.puzzle11);
    puzzleData.add(R.drawable.puzzle12);
    puzzleData.add(R.drawable.puzzle13);
    puzzleData.add(R.drawable.puzzle14);
    puzzleData.add(R.drawable.puzzle15);
    puzzleData.add(R.drawable.puzzle16);
    puzzleData.add(R.drawable.puzzle17);
    puzzleData.add(R.drawable.puzzle18);
    puzzleData.add(R.drawable.puzzle19);
    puzzleData.add(R.drawable.puzzle20);
    puzzleData.add(R.drawable.puzzle21);
    puzzleData.add(R.drawable.puzzle21);
    puzzleData.add(R.drawable.puzzle21);
    return puzzleData;

    //TODO: add more
  }

  public void setUpShapeGameData() {
    shapeData = new HashMap<Integer, String>();

    shapeData.put(R.drawable.circle_blue, "circle");
    shapeData.put(R.drawable.circle_orange, "circle");
    shapeData.put(R.drawable.diamond_green, "diamond");
    shapeData.put(R.drawable.diamond_pink, "diamond");
    shapeData.put(R.drawable.heart_purple, "heart");
    shapeData.put(R.drawable.heart_red, "heart");
    shapeData.put(R.drawable.oval_yellow, "oval");
    shapeData.put(R.drawable.oval_blue, "oval");
    shapeData.put(R.drawable.cross_green, "cross");
    shapeData.put(R.drawable.cross_orange, "cross");
    shapeData.put(R.drawable.rect_purple, "rect");
    shapeData.put(R.drawable.rect_red, "rect");
    shapeData.put(R.drawable.squareyellow, "square");
    shapeData.put(R.drawable.square_pink, "square");
    shapeData.put(R.drawable.staar_red, "star");
    shapeData.put(R.drawable.star_yellow, "star");
    shapeData.put(R.drawable.triangle_blue, "triangle");
    shapeData.put(R.drawable.triangle_green, "triangle");
    shapeData.put(R.drawable.arrow_purple, "arrow");
    shapeData.put(R.drawable.arrow_blue, "arrow");

    //TODO: add more
  }


  public void setUpColorGameData() {
    colorData = new HashMap<Integer, String>();

    colorData.put(R.drawable.bag_letter, "blue");
    colorData.put(R.drawable.gift_letter, "blue");
    colorData.put(R.drawable.apple_letter, "red");
    colorData.put(R.drawable.jeepney_letter, "red");
    colorData.put(R.drawable.candle_letter, "red");
    colorData.put(R.drawable.ball_letter, "orange");
    colorData.put(R.drawable.orange_letter, "orange");
    colorData.put(R.drawable.flower_letter, "purple");
    colorData.put(R.drawable.eggplant_letter, "purple");
    colorData.put(R.drawable.grapes_letter, "purple");
    colorData.put(R.drawable.frog_letter, "green");
    colorData.put(R.drawable.book_letter, "green");
    colorData.put(R.drawable.leaf_letter, "green");
    colorData.put(R.drawable.moon_letter, "yellow");
    colorData.put(R.drawable.star_letter, "yellow");

    //TODO: add more only these colors
  }

  public void setUpColorOutlineGameData() {

    colorData = new HashMap<Integer, String>();

    colorData.put(R.drawable.aligator_color, "green");
    colorData.put(R.drawable.apple_color, "red");
    colorData.put(R.drawable.avocado_color, "green");
    colorData.put(R.drawable.ball_color, "orange");
    colorData.put(R.drawable.banana_color, "yellow");
    colorData.put(R.drawable.broccoli_color, "green");
    colorData.put(R.drawable.cactus_color, "green");
    colorData.put(R.drawable.carrot_color, "orange");
    colorData.put(R.drawable.cheese_color, "yellow");
    colorData.put(R.drawable.cucumber_color, "green");
    //colorData.put(R.drawable.dove_color, "white");
    colorData.put(R.drawable.eggplant_color, "purple");
    colorData.put(R.drawable.grapes_color, "purple");
    colorData.put(R.drawable.heart_color, "red");
    colorData.put(R.drawable.lemon_color, "yellow");


    //TODO: add more only these colors

  }


  public void setUpLetterGameData() {
    //65+26
    letterData = new HashMap<String, Integer>();

    letterData.put("angel", R.drawable.angel_letter);
    letterData.put("bag", R.drawable.bag_letter);
    letterData.put("chicken", R.drawable.chicken_letter);
    letterData.put("doll", R.drawable.doll_letter);
    letterData.put("egg", R.drawable.egg_letter);
    letterData.put("flower", R.drawable.flower_letter);
    letterData.put("glass", R.drawable.glass_letter);
    letterData.put("hat", R.drawable.hat_letter);
    letterData.put("ink", R.drawable.ink_letter);
    letterData.put("jacket", R.drawable.jacket_letter);
    letterData.put("kite", R.drawable.kite_letter);
    letterData.put("lion", R.drawable.lion_letter);
    letterData.put("moon", R.drawable.moon_letter);
    letterData.put("nail", R.drawable.nail_letter);
    letterData.put("owl", R.drawable.owl_letter);
    letterData.put("pig", R.drawable.pig_letter);
    letterData.put("queen", R.drawable.queen_letter);
    letterData.put("rabbit", R.drawable.rabbit_letter);
    letterData.put("sun", R.drawable.sun_letter);
    letterData.put("turtle", R.drawable.turtle_letter);
    letterData.put("unicycle", R.drawable.unicycle_letter);
    letterData.put("vase", R.drawable.vase_letter);
    letterData.put("watch", R.drawable.watch_letter);
    letterData.put("xray", R.drawable.xray_letter);
    letterData.put("yarn", R.drawable.yarn_letter);
    letterData.put("zebra", R.drawable.zebra_letter);

  }


  public HashMap<String, Integer> getLetterData() {
    if (letterData.isEmpty()) {
      setUpLetterGameData();
    }
    return letterData;
  }


  public void setUpGameData(StudentLevel studentLevel, GameType gameType) {

    this.studentLevel = studentLevel;
    this.gameType = gameType;

    gameDatas = new ArrayList<GameData>();
    ArrayList<Integer> choices = new ArrayList<Integer>();
    switch (studentLevel) {

      case NURSERY:
        switch (gameType) {
          case LETTER:


            break;
          case COLOR:
            break;
          case SHAPE:
            break;
          case COUNTING:
            break;
          case PATTERN:
            break;
          case PUZZLE:
            break;
        }
        break;
      case KINDER:
        break;
      case PREP:
        break;
    }

  }

}
