package proj.abc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import proj.abc.game.ShapeType;

/**
 * Created by mbarcelona on 4/12/16.
 */
public class ShapeKinderGameActivity extends Activity {


  ImageView imgMain;
  TextView tvQuestNum;

  ImageButton imgBtn1, imgBtn2, imgBtn3;

  private MediaPlayer mp;
  public static int[] questions;
  public static int currQuestion;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_shape_kinder);

    imgBtn1 = (ImageButton) findViewById(R.id.choice2);
    imgBtn2 = (ImageButton) findViewById(R.id.choice4);
    imgBtn3 = (ImageButton) findViewById(R.id.choice3);

    tvQuestNum = (TextView) findViewById(R.id.tv_score);
    imgMain = (ImageView) findViewById(R.id.img_color);


    HashMap<Integer,String> colorData = GameMap.getInstance().getColorData();

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

    View.OnClickListener clickListener = null;
    int random = (int) (Math.random()*3);
    switch(random) {
      case 0: //circle
        imgMain.setImageResource(GameMap.getInstance().getRandomShapeBitmap(ShapeType.CIRCLE));
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
      case 1: //triangle
        imgMain.setImageResource(GameMap.getInstance().getRandomShapeBitmap(ShapeType.TRIANGLE));
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
      case 2: //square
        imgMain.setImageResource(GameMap.getInstance().getRandomShapeBitmap(ShapeType.SQUARE));
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



  public int getColor(String color){
    int bitmapColor = Color.BLACK;
    switch(color){
      case "red":
        bitmapColor = R.color.red;
        break;
      case "blue":
        bitmapColor = R.color.blue;
        break;
      case "purple":
        bitmapColor = R.color.purple;
        break;
      case "yellow":
        bitmapColor = R.color.yellow;
        break;
      case "orange":
        bitmapColor = R.color.orange;
        break;
      case "green":
        bitmapColor = R.color.green;
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
    i.putExtra("gameType", "shape");
    i.putExtra("goodJob", isCorrect);

    if(this.currQuestion > MainActivity.numQuestions-2) {
      i.putExtra("lastQuestion", true);
    }

    a.startActivity(i);
    a.finish();
  }
}

