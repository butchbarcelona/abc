package proj.abc.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.bunny.game.framework.GameView;

import java.util.ArrayList;
import java.util.HashMap;

import proj.abc.R;
import proj.abc.ThankYouActivity;

/**
 * Created by mbarcelona on 4/11/16.
 */
public class ColorGame extends GameView {

  public static int[] questions;
  public static int currQuestion;
  public static int bitmapBefore = 0;

  public static float percentWidth, percentHeight;
  private BitmapFactory.Options ops;
  private Context ctx;
  private MediaPlayer mp;
  private MotionEvent touchEvent;
  private Rect ptTouch, choiceDest;
  Paint paintTxt,paintTxtSmall, paintTxtChoice;
  ArrayList<Choice3Object> questionObjects;
  ArrayList<String> strChoices;
  Choice3Object question;
  ArrayList<Point> choicesPoints;

  int correctObject = 0;

  public ColorGame(Context context){
    super(context, null);
    this.ctx = context;

    ops = new BitmapFactory.Options();
    ops.inPurgeable = true;
    questionObjects = new ArrayList<Choice3Object>();
  }

  public ColorGame(Context context, AttributeSet s) {
    super(context, s);
  }

  public ColorGame(Context context, int w, int h) {
    super(context, w, h);
  }

  public void setQuestionAndChoices() {


    strChoices = new ArrayList<String>();
    ArrayList<Point> points = new ArrayList<Point>();
    points.add(new Point(230, 420));
    points.add(new Point(430, 420));
    points.add(new Point(630, 420));


    HashMap<String, Integer> letterMap = GameMap.getInstance().getLetterData();

    ArrayList<Integer> letters = new ArrayList<Integer>();
    for(int i = 0; i < 26; i++){
      letters.add(i, i);
    }

    if(questions == null) {
      //get list of questioned letters
      questions = new int[10];
      for (int j = 0; j < 10; j++) {
        int random = (int) (Math.random() * letters.size());
        questions[j] = (int) ((Math.random()*6)+1);//letters.get(random);
        letters.remove(random);
      }
      currQuestion = 0;
    }else{
      currQuestion++;
    }


    int correctCount = questions[currQuestion];

    strChoices.add(correctCount+"");
    //map question objects

    int paddingY = 100;
    int paddingX = 200;
    int maxRowCount = 3;

    if(correctCount <= maxRowCount){
      paddingY = 180;
    }



    int bitmap = GameMap.getInstance().getRandomBitmap();

    while(bitmap == bitmapBefore){
      bitmap = GameMap.getInstance().getRandomBitmap();
    };

    bitmapBefore = bitmap;
    for(int j = 0; j < correctCount; j++) {


      if(j > correctCount-(correctCount%maxRowCount)){
        switch((correctCount%maxRowCount)){
          case 1:
            paddingX = 400;
            break;
          case 2:
            paddingX = 300;
            break;
        }
      }

      questionObjects.add(new Choice3Object((paddingX+(j%(maxRowCount)* 200))
        , (paddingY + (150 * (j / (maxRowCount))))
        , getBitmap(bitmap)));
    }



/*
    switch(GameMap.studentLevel){

      case NURSERY:
        strQuestion = "Starts with letter "+ Character.toUpperCase(currChar);
        break;
      case KINDER:
        strQuestion = "Tap the correct letter ";
        break;
      case PREP:
        strQuestion = "Spell out";
        break;
    }
*/

    strQuestion = "Counting".toUpperCase();

    //get randomized remaining questionObjects
    int[] random = getRandomNot(questions[currQuestion],6,2);

    for(int k = 0; k < random.length; k++){
      strChoices.add((random[k])+"");
    }


    correctObject = 0;
    choicesPoints = new ArrayList<Point>();

    for(Choice3Object obj: questionObjects){
      obj.scaleDest(90 ,90);
    }

    for(String str: strChoices) {
      Point point = points.get(((int) (Math.random() * points.size())));
      points.remove(point);
      choicesPoints.add(point);
    }

  }


  public int[] getRandomNot(int notThis, int max, int numRand){

    int random[] = new int[numRand];

    Log.d("butch", "notThis: " + notThis);
    ArrayList<Integer> letters = new ArrayList<Integer>();
    for(int i = 1; i <= max; i++){
      if(i != notThis) {
        letters.add(i);
        Log.d("butch", "letters[i]: " + i);
      }
    }
    for (int j = 0; j < numRand; j++) {
      int rand = (int) (Math.random() * letters.size());
      random[j] = letters.get(rand);
      letters.remove(rand);

      Log.d("butch", "getting: " + random[j]);
    }



    return random;
  }


  String strQuestion;

  @Override
  public void init(int winWidth, int winHeight){
    this.percentHeight = (780-winHeight)/780.0f;
    this.percentWidth = (1280-winWidth)/1280.0f;

    paintTxt = new Paint();
    paintTxt.setTextSize(40);
    paintTxt.setColor(Color.BLACK);

    paintTxtSmall = new Paint();
    paintTxtSmall.setTextSize(30);
    paintTxtSmall.setColor(Color.BLUE);

    paintTxtChoice = new Paint();
    paintTxtChoice.setTextSize(100);
    paintTxtChoice.setColor(Color.BLACK);
  }

  @Override
  public void Destroy() {

  }

  @Override
  public void Touch(MotionEvent event) {



    if(ptTouch == null)
      ptTouch = new Rect();
    ptTouch.bottom = (int) (event.getY()+1);
    ptTouch.top = (int) (event.getY()-1);
    ptTouch.left = (int) (event.getX()-1);
    ptTouch.right = (int) (event.getX()+1);

    switch(event.getAction()) {

      case MotionEvent.ACTION_MOVE:
        break;
      case MotionEvent.ACTION_DOWN:
        break;
      case MotionEvent.ACTION_UP:

        for(int i = 0; i < choicesPoints.size(); i++){
          choiceDest = new Rect(choicesPoints.get(i).x-50,choicesPoints.get(i).y-50,choicesPoints.get(i).x+50,choicesPoints.get(i).y+50);
          if(ptTouch.intersect(choiceDest) && i == correctObject) {
            playCorrect();
          }else if(ptTouch.intersect(choiceDest)){
            playWrong();
          }
        }
        break;
    }

  }

  @Override
  public void Draw(Canvas c) {
    c.drawColor(Color.WHITE);
    c.drawText((currQuestion + 1) + "/10", 750, 35, paintTxtSmall);
    c.drawText(strQuestion, 200, 70, paintTxt);
    for(Choice3Object obj: questionObjects){
      obj.draw(c);

    }
    for(int i = 0; i < strChoices.size(); i++){
      c.drawText(strChoices.get(i).toUpperCase()
        , choicesPoints.get(i).x, choicesPoints.get(i).y,paintTxt);
      choiceDest = new Rect(choicesPoints.get(i).x-50,choicesPoints.get(i).y-50,choicesPoints.get(i).x+50,choicesPoints.get(i).y+50);
      //c.drawRect(choiceDest,paintTxtSmall);
    }

  }

  @Override
  public void Update(long t) {

  }

  public Bitmap getBitmap(int id){
    return BitmapFactory.decodeResource(ctx.getResources(), id, ops);
  }


  public void playCorrect (){
    mp = (MediaPlayer.create(ctx, R.raw.right));
    if(mp!= null)
      mp.start();


    stopGame(true);
  }
  public void playWrong(){
    mp = (MediaPlayer.create(ctx, R.raw.wrong));
    if(mp!= null)
      mp.start();

    stopGame(false);
  }

  private void stopGame(boolean isCorrect) {

    Activity a = (Activity)ctx;
    Intent i = new Intent(a,ThankYouActivity.class);
    i.putExtra("gameType", "counting");
    i.putExtra("goodJob", isCorrect);

    if(!(LetterGame.currQuestion < 9)) {
      i.putExtra("lastQuestion", true);

    }

    a.startActivity(i);
    a.finish();
  }
}