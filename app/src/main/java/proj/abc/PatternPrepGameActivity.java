package proj.abc;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import proj.abc.game.GameMap;

/**
 * Created by mbarcelona on 4/13/16.
 */
public class PatternPrepGameActivity extends Activity {

  ImageView imgColor;
  TextView tvColor;
  TextView tvQuestNum;

  ImageButton imgBtn1, imgBtn2, imgBtn3,imgBtn4,imgBtn5;

  ArrayList<ImageView> imgViews;

  private MediaPlayer mp;
  public static int[] questions;
  public static int currQuestion;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_pattern_prep);

    imgBtn1 = (ImageButton) findViewById(R.id.choice2);
    imgBtn2 = (ImageButton) findViewById(R.id.choice4);
    imgBtn3 = (ImageButton) findViewById(R.id.choice3);

    tvColor = (TextView) findViewById(R.id.tv_color);
    tvQuestNum = (TextView) findViewById(R.id.tv_score);
    imgColor = (ImageView) findViewById(R.id.img_color);


    HashMap<Integer,String> colorData = GameMap.getInstance().getShapeGameData();

    ArrayList<Integer> colors = new ArrayList<Integer>();
    for(int i = 0; i < colorData.size(); i++){
      colors.add(i, i);
    }

    if(questions == null) {
      //get list of questioned letters
      questions = new int[10];
      for (int j = 0; j < 10; j++) {
        int random = (int) (Math.random() * colors.size());
        questions[j] =  colors.get(random);
        colors.remove(random);
      }
      currQuestion = 0;
    }else{
      currQuestion++;
    }

    tvQuestNum.setText((currQuestion + 1) + "/10");


    imgViews = new ArrayList<ImageView>();
    imgViews.add((ImageView) findViewById(R.id.img1));
    imgViews.add((ImageView) findViewById(R.id.img2));
    imgViews.add((ImageView) findViewById(R.id.img3));
    imgViews.add((ImageView) findViewById(R.id.img4));
    imgViews.add((ImageView) findViewById(R.id.img5));

    //createPattern

    String[] pattern = new String[3];
    for(int i = 0; i < 3; i++){
      pattern[i] = getRandomShape();

      imgViews.get(i).setImageResource(getShape(pattern[i]));
      if(i + 3 <= 4) {
        imgViews.get(i + 3).setImageResource(getShape(pattern[i]));
      }
    }

    View.OnClickListener clickListener =  null;
    switch(pattern[2]){
        case "circle":
          clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              switch(v.getId()){
                case R.id.choice2:
                  playCorrect();
                  break;
                case R.id.choice4:
                case R.id.choice3:
                  playWrong();
                  break;
              }
            }
          };
          break;
        case "triangle":
          clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              switch(v.getId()){
                case R.id.choice4:
                  playCorrect();
                  break;
                case R.id.choice2:
                case R.id.choice3:
                  playWrong();
                  break;
              }
            }
          };
          break;
        case "square":
          clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              switch(v.getId()){
                case R.id.choice3:
                  playCorrect();
                  break;
                case R.id.choice2:
                case R.id.choice4:
                  playWrong();
                  break;
              }
            }
          };
          break;
    }


    imgBtn1.setOnClickListener(clickListener);
    imgBtn2.setOnClickListener(clickListener);
    imgBtn3.setOnClickListener(clickListener);
  }
  public String getRandomShape(){
    String shape = "";


    switch((int) (Math.random()*3)){
      case 0: shape = "circle";
        break;
      case 1: shape = "triangle";
        break;
      case 2: shape = "square";
        break;
    }
    return shape;
  }

  public int getShape(String shape){
    int bitmapColor = 0;
    switch(shape){
      case "circle":
        bitmapColor = R.drawable.circle;
        break;
      case "triangle":
        bitmapColor = R.drawable.triangle;
        break;
      case "square":
        bitmapColor = R.drawable.square;
        break;
    }
    return bitmapColor;
  }


  public int[] getRandomNot(int notThis, int max, int numRand){

    int random[] = new int[numRand];

    Log.d("butch", "notThis: " + notThis);
    ArrayList<Integer> letters = new ArrayList<Integer>();
    for(int i = 0; i < max; i++){
      letters.add(i);
    }
    for (int j = 0; j < numRand; j++) {
      int rand = (int) (Math.random() * letters.size());
      random[j] = letters.get(rand);
      letters.remove(rand);

    }



    return random;
  }



  @Override
  public void onBackPressed() {
    //super.onBackPressed();
    return;
  }


  public void playCorrect (){
    mp = (MediaPlayer.create(this, R.raw.right));
    if(mp!= null)
      mp.start();


    stopGame(true);
  }
  public void playWrong(){
    mp = (MediaPlayer.create(this, R.raw.wrong));
    if(mp!= null)
      mp.start();

    stopGame(false);
  }

  private void stopGame(boolean isCorrect) {
    Activity a = (Activity)this;
    Intent i = new Intent(a,ThankYouActivity.class);
    i.putExtra("gameType", "shape"); //pattern
    i.putExtra("goodJob", isCorrect);

    if(this.currQuestion > MainActivity.numQuestions-2) {
      i.putExtra("lastQuestion", true);
    }

    a.startActivity(i);
    a.finish();
  }
}
