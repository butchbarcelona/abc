package proj.abc.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;

import com.bunny.game.framework.GameView;
import com.bunny.game.framework.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import proj.abc.MainActivity;
import proj.abc.R;
import proj.abc.ThankYouActivity;

/**
 * Created by mbarcelona on 4/13/16.
 */
public class SpellingGame  extends GameView {

  public static int[] questions;
  public static int currQuestion;

  public static float percentWidth, percentHeight;

  private MediaPlayer mp;
  //1380x780

  private static final int MAX_TIMER = 20; //-1 the usual time

  private static final int BEFORE_GAME = 0;
  private static final int START_GAME = 1;
  private static final int END_GAME = 2;

  private int state = BEFORE_GAME;

  private long startTime = -1, currTime, millis;
  private int seconds;


  private BitmapFactory.Options ops;
  private Context ctx;

  private Paint txtSecondsPaint, paintBorder, paintTime, paintTxt, paintTxtUnderscore;

  private AlphabetPiece piece;

  private ArrayList<AlphabetPiece> pieces;
  private Background bgPuzzle, bgPuzzleCured, bgChosenWord;//, btnPlay, bgMsg, btnPlayAgain, txtAllBetter, logo;

  private Rect ptTouch;
  private boolean selected = false;
  private boolean showCured = false;
  private boolean startGame = false;

  private int[] correct, hasLetter;

  private ArrayList<Rect> initPosRects;
  private String chosenWord;
  private int idBitSick, idBitCured, idChosenBitmap;

  private int imgPicked;

  String strQuestion;

  public SpellingGame(Context context) {
    super(context, null);

    this.ctx = context;

    ops = new BitmapFactory.Options();
    ops.inPurgeable = true;
    txtSecondsPaint = new Paint();
    txtSecondsPaint.setColor(Color.BLUE);
    txtSecondsPaint.setTextSize(30.0f);


    paintTxt = new Paint();
    paintTxt.setTextSize(40);
    paintTxt.setColor(Color.BLACK);


    paintTxtUnderscore = new Paint();
    paintTxtUnderscore.setTextSize(140);
    paintTxtUnderscore.setColor(Color.BLACK);

    paintBorder = new Paint();
    paintBorder.setColor(Color.argb(255, 0, 168, 233));

    paintTime = new Paint();
    paintTime.setShader(new LinearGradient(0, 0, 0, getHeight(), Color.BLACK, Color.argb(255, 0, 168, 233), Shader.TileMode.MIRROR));

    initPosRects = new ArrayList<Rect>();

    strQuestion = "SPELL OUT";

  }

  public Bitmap getBitmap(int id) {
    return BitmapFactory.decodeResource(ctx.getResources(), id, ops);
  }


  public static int setX(int x1) {
    return (int) Math.round(x1 - (x1 * (PuzzleGame.percentWidth * 1.0f)));
  }

  public static int setY(int y1) {
    return (int) Math.round(y1 - (y1 * (PuzzleGame.percentHeight * 1.0f)));
  }


  public void init(int img, int winWidth, int winHeight) {

    Log.d("butch", "winWidth:" + winWidth);
    Log.d("butch", "winHeight:" + winHeight);

    this.percentWidth =0; //(1280 - winWidth) / 1280.0f;
    this.percentHeight = 0;//(780 - winHeight) / 780.0f;


    imgPicked = img;

    HashMap<String, Integer> letterMap = GameMap.getInstance().getLetterData();
   /* Iterator it = letterMap.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry pair = (Map.Entry) it.next();
    }*/

    ArrayList<Integer> letters = new ArrayList<Integer>();
    for(int i = 0; i < letterMap.size(); i++){
      letters.add(i, i);
    }

    if(questions == null) {
      //get list of questioned letters
      questions = new int[10];
      for (int j = 0; j < 10; j++) {
        int random = (int) (Math.random() * letters.size());
        questions[j] = letters.get(random);
        letters.remove(random);
      }
      currQuestion = 0;
    }else{
      currQuestion++;
    }

    int count = 0;
    Log.d("butch","start lettermap");
    Iterator it = letterMap.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry pair = (Map.Entry)it.next();
      if(count == questions[currQuestion]){
        chosenWord = (String) pair.getKey();
        idChosenBitmap = (int) pair.getValue();
      }

      it.remove();
      count++;
    }



/*    idBitCured = puzzleData.get(questions[currQuestion]);
    idBitSick = puzzleData.get(questions[currQuestion]);*/

    idBitCured = R.drawable.alphabet;
    idBitSick = R.drawable.alphabet;

    correct = new int[chosenWord.length()];
    hasLetter = new int[chosenWord.length()];

    bgPuzzle = new Background(170, 170, getBitmap(idBitSick));
    bgPuzzleCured = new Background(170, 170,
      getBitmap(idBitSick));
    bgChosenWord =  new Background(350, 100,
      getBitmap(idChosenBitmap));
    bgChosenWord.scaleDest(150,150);
    bgPuzzle.changeAlpha(50);

    state = START_GAME;
    pieces = new ArrayList<AlphabetPiece>();


    char[] charWord = chosenWord.toUpperCase().toCharArray();


    ArrayList<Integer> arrRandIndex = new ArrayList<Integer>();
    Rect rect;

    //not randin
    for (int i = 0; i < charWord.length; i++) {
      pieces.add(new AlphabetPiece(Random.Next(200, 700), Random.Next(350,
        400)
        , getBitmap(idBitSick)
        , ((int) charWord[i]) - 65, i, chosenWord.length()));

      rect = new Rect(pieces.get(i).initialBox.left
        , pieces.get(i).initialBox.top+110
        , pieces.get(i).initialBox.right
        , pieces.get(i).initialBox.bottom);
      initPosRects.add(rect);
    }


    bgPuzzleCured.bitmap = getBitmap(idBitCured);
    bgPuzzleCured.changeAlpha(0);
    bgPuzzle.changeAlpha(0);
    startGame = true;
  }

  @Override
  public void Destroy() {
    // TODO Auto-generated method stub

  }

  @Override
  public void Touch(MotionEvent event) {

    if (ptTouch == null)
      ptTouch = new Rect();

    ptTouch.bottom = (int) (event.getY() + 1);
    ptTouch.top = (int) (event.getY() - 1);
    ptTouch.left = (int) (event.getX() - 1);
    ptTouch.right = (int) (event.getX() + 1);


    switch (state) {
      case BEFORE_GAME:
        //if (btnPlay.dest.intersect(ptTouch))
      {


      }

      break;
      case START_GAME:

        if (!showCured && startGame) {

          if (event.getAction() == MotionEvent.ACTION_DOWN
            || event.getAction() == MotionEvent.ACTION_UP) {
            selected = false;
          }

          Log.d("butch"," selected "+selected);
          for (int i = pieces.size() - 1; i >= 0; i--) {

            piece = pieces.get(i);

            if (piece.dest.intersect(ptTouch)) {
              if (event.getAction() == MotionEvent.ACTION_DOWN
                && !selected) {
                piece.areYouSelected = true;
                selected = true;

                piece.x = ptTouch.centerX() - (setX(54) / 2);
                piece.y = ptTouch.centerY() - (setY(120) / 2);
              }

              if (event.getAction() == MotionEvent.ACTION_MOVE
                && piece.areYouSelected) {

                piece.x = ptTouch.centerX() - (setX(54) / 2);
                piece.y = ptTouch.centerY() - (setY(120) / 2);

                for (AlphabetPiece p : pieces) {
                  if (ptTouch.intersect(p.initialBox) ) {
                    piece.x = (p.initialBox.left);
                    piece.y = (p.initialBox.top);

                    if (p.wordIndex == piece.wordIndex) {
                      correct[p.wordIndex] = 1;
                      checkIfAllCorrect();
                    }
                  }
                }
              }

              if (event.getAction() == MotionEvent.ACTION_UP
                && piece.areYouSelected
                ){
                selected = false;
                //if(piece.areYouSelected)
                  piece.areYouSelected = false;
              }
            }


          }
        }

        break;
      case END_GAME:

        //goToMainMenu();


        break;
    }


  }

  public void playCorrect() {
    mp = (MediaPlayer.create(ctx, R.raw.right));
    if (mp != null)
      mp.start();


    stopGame(true);
  }

  public void playWrong() {
    mp = (MediaPlayer.create(ctx, R.raw.wrong));
    if (mp != null)
      mp.start();

    stopGame(false);
  }

  private void stopGame(boolean isCorrect) {
    Activity a = (Activity) ctx;
    Intent i = new Intent(a, ThankYouActivity.class);
    i.putExtra("gameType", "letter");
    i.putExtra("goodJob", isCorrect);

    if ((SpellingGame.currQuestion  > MainActivity.numQuestions-2)) {
      i.putExtra("lastQuestion", true);

    }

    a.startActivity(i);
    a.finish();
  }


  private void checkIfAllCorrect() {
    // show cured!
    boolean cured = true;
    for (int i : correct) {
      if (i == 0) {
        cured = false;
      }
    }
    showCured = cured;
  }

  @Override
  public void Draw(Canvas c) {

    c.drawColor(Color.WHITE);

    c.drawText((currQuestion + 1) + "/10", 750, 35, txtSecondsPaint);
    c.drawText(strQuestion, 320, 70, paintTxt);
    bgChosenWord.draw(c);

    //bgPuzzle.draw(c);


    int time = MAX_TIMER - seconds;

    if (time < 0) {
      time = 0;
    }


    switch (state) {
      case BEFORE_GAME:

        c.drawText(":00", setX(30), setY(40), txtSecondsPaint);
        break;
      case START_GAME:

        c.drawText(":" + (time < 10 ? "0" : "") + time, setX(30), setY(40), txtSecondsPaint);

        if (startGame) {
          for (int i = 0; i < pieces.size(); i++) {
            c.drawRect(initPosRects.get(i), paintTxtUnderscore);

            pieces.get(i).draw(c);
          }

          if (showCured) {
          }
        }
        break;
      case END_GAME:


        bgPuzzleCured.draw(c);
        break;
    }

  }


  @Override
  public void Update(long t) {

    switch (state) {
      case BEFORE_GAME:
        break;
      case START_GAME:
        if (seconds >= MAX_TIMER) {
          //stop game
          playWrong();
        }

        if (startTime < 0) {
          startTime = t;
        }
        currTime = t;

        millis = currTime - startTime;
        seconds = (int) (millis / 1000);
        seconds = seconds % 60;


        for (AlphabetPiece piece : pieces) {
          piece.update(t);
        }

        break;
      case END_GAME:


        playCorrect();

        break;

    }


    if (showCured) {

      if (bgPuzzleCured.alpha + 10 > 255) {
        bgPuzzleCured.changeAlpha(255);
        playCorrect();
      } else
        bgPuzzleCured.changeAlpha(bgPuzzleCured.alpha + 10);

    }

  }

  @Override
  public void init(int winWidth, int winHeight) {
    init(0, winWidth, winHeight);
  }
}
