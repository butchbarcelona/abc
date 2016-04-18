package proj.abc;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import proj.abc.game.GameMap;

/**
 * Created by mbarcelona on 4/12/16.
 */
public class LetterKinderGameActivity extends Activity {

  ImageView imgColor;
  TextView tvColor;
  TextView tvQuestNum;

  TextView tvChoice1, tvChoice2;

  private MediaPlayer mp;
  public static int[] questions;
  public static int currQuestion;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_letter_kinder);

    tvChoice1 = (TextView) findViewById(R.id.tv_choice1);
    tvChoice2 = (TextView) findViewById(R.id.tv_choice2);

    tvColor = (TextView) findViewById(R.id.tv_color);
    tvQuestNum = (TextView) findViewById(R.id.tv_score);
    imgColor = (ImageView) findViewById(R.id.img_color);

    HashMap<String, Integer> letterData = GameMap.getInstance().getLetterData();

    ArrayList<Integer> letters = new ArrayList<Integer>();
    for(int i = 0; i < letterData.size(); i++){
      letters.add(i, i);
    }

    if(questions == null) {
      //get list of questioned letters
      questions = new int[10];
      for (int j = 0; j < 10; j++) {
        int random = (int) (Math.random() * letters.size());
        questions[j] =  letters.get(random);
        letters.remove(random);
      }
      currQuestion = 0;
    }else{
      currQuestion++;
    }

    tvQuestNum.setText((currQuestion+1)+"/10");

    ArrayList<TextView> tvChoices = new ArrayList<TextView>();
    tvChoices.add(tvChoice1);
    tvChoices.add(tvChoice2);

    //get the correct answer

    int count = 0;
    String currColor = "";


    for(Map.Entry<String, Integer> entry : letterData.entrySet()){
      System.out.printf("Key : %s and Value: %s %n", entry.getKey(), entry.getValue());
      if(count == questions[currQuestion]){
        //String strItem = (String) pair.getKey();
        currColor = entry.getKey();
        imgColor.setImageResource(entry.getValue());
      }
      count++;
    }

    int randLetterIndex = (int) (Math.random()*currColor.length());

    char currChar = currColor.charAt(randLetterIndex);
    currColor = currColor.replaceFirst(String.valueOf(currChar), "_");
    tvColor.setText(currColor.toUpperCase());
    TextView tvChoice = tvChoices.get((int) (Math.random()*tvChoices.size()));
    tvChoice.setText(String.valueOf(currChar).toUpperCase() + "");
    tvChoice.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        playCorrect();
      }
    });

    tvChoices.remove(tvChoice);



    char randomLetter ;

    do{
      randomLetter = ((char) (Math.random()*26 + 64));
    }while(randomLetter == currChar);
    tvChoices.get(0).setText(Character.toString(randomLetter));

    tvChoices.get(0).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        playWrong();
      }
    });

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
    i.putExtra("gameType", "letter");
    i.putExtra("goodJob", isCorrect);

    if(this.currQuestion > MainActivity.numQuestions-2) {
      i.putExtra("lastQuestion", true);
    }

    a.startActivity(i);
    a.finish();
  }
}
