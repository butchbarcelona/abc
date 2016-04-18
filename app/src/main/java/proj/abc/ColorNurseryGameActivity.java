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
import java.util.Map;

import proj.abc.game.GameMap;

/**
 * Created by mbarcelona on 4/11/16.
 */
public class ColorNurseryGameActivity extends Activity{

  ImageView imgColor;
  TextView tvColor;
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
    setContentView(R.layout.activity_color_nursery);

    imgBtn1 = (ImageButton) findViewById(R.id.choice2);
    imgBtn2 = (ImageButton) findViewById(R.id.choice4);
    imgBtn3 = (ImageButton) findViewById(R.id.choice3);

    tvColor = (TextView) findViewById(R.id.tv_color);
    tvQuestNum = (TextView) findViewById(R.id.tv_score);
    imgColor = (ImageView) findViewById(R.id.img_color);


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

    tvQuestNum.setText((currQuestion+1)+"/10");

    ArrayList<ImageButton> imgBtns = new ArrayList<ImageButton>();
    imgBtns.add(imgBtn1);
    imgBtns.add(imgBtn2);
    imgBtns.add(imgBtn3);

    //get the correct answer

    int count = 0;
    String currColor = "";


    for(Map.Entry<Integer, String> entry : colorData.entrySet()){
      System.out.printf("Key : %s and Value: %s %n", entry.getKey(), entry.getValue());
      if(count == questions[currQuestion]){
        //String strItem = (String) pair.getKey();
        currColor = (String) entry.getValue();

        ImageButton imgButton = imgBtns.get((int) (Math.random()*imgBtns.size()));
        imgButton.setImageResource((Integer) entry.getKey());

        imgButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            playCorrect();
          }
        });
        imgBtns.remove(imgButton);
      }
      count++;
    }


    tvColor.setText(currColor);
    imgColor.setImageResource(getColor(currColor));

    ArrayList<Integer> colorBitmaps = new ArrayList<Integer>();

     for(Map.Entry<Integer, String> entry : colorData.entrySet()){
        System.out.printf("Key : %s and Value: %s %n", entry.getKey(), entry.getValue());
        if(!currColor.equals(entry.getValue())){
          colorBitmaps.add(entry.getKey());
        }
     }

    int[] rand = getRandomNot(0,colorBitmaps.size(),2);

    int j = 0;
    for(int r: rand){
      imgBtns.get(j).setImageResource(colorBitmaps.get(r));
      imgBtns.get(j).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          playWrong();
        }
      });
      j++;
    }


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
    i.putExtra("gameType", "color");
    i.putExtra("goodJob", isCorrect);

    if(this.currQuestion > MainActivity.numQuestions-2) {
      i.putExtra("lastQuestion", true);
    }

    a.startActivity(i);
    a.finish();
  }
}
