package proj.abc;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import proj.abc.game.GameMap;

/**
 * Created by mbarcelona on 4/12/16.
 */
public class CountingNurseryGameActivity extends Activity {


  TextView tvColor;
  TextView tvQuestNum;

  TextView tvChoice1, tvChoice2, tvChoice3;

  private MediaPlayer mp;
  public static int[] questions;
  public static int currQuestion;

  ArrayList<ImageView> imgObjects;

  //public static int bitmapBefore = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_counting_nursery);

    tvChoice1 = (TextView) findViewById(R.id.tv_choice1);
    tvChoice2 = (TextView) findViewById(R.id.tv_choice2);
    tvChoice3 = (TextView) findViewById(R.id.tv_choice3);

    tvColor = (TextView) findViewById(R.id.tv_color);
    tvQuestNum = (TextView) findViewById(R.id.tv_score);

    HashMap<String, Integer> letterData = GameMap.getInstance().getLetterData();

    imgObjects = new ArrayList<ImageView>();
    imgObjects.add((ImageView)findViewById(R.id.choice1));
    imgObjects.add((ImageView)findViewById(R.id.choice2));
    imgObjects.add((ImageView)findViewById(R.id.choice3));
    imgObjects.add((ImageView)findViewById(R.id.choice4));
    imgObjects.add((ImageView)findViewById(R.id.choice5));
    imgObjects.add((ImageView)findViewById(R.id.choice6));
    imgObjects.add((ImageView)findViewById(R.id.choice7));

    ArrayList<Integer> letters = new ArrayList<Integer>();
    for (int i = 0; i < letterData.size(); i++) {
      letters.add(i, i);
    }

    if (questions == null) {
      //get list of questioned letters
      questions = new int[10];
      for (int j = 0; j < 10; j++) {
        int random = (int) (Math.random() * letters.size());
        questions[j] = letters.get(random);
        letters.remove(random);
      }
      currQuestion = 0;
    } else {
      currQuestion++;
    }

    tvQuestNum.setText((currQuestion + 1) + "/10");

    ArrayList<TextView> tvChoices = new ArrayList<TextView>();
    tvChoices.add(tvChoice1);
    tvChoices.add(tvChoice2);
    tvChoices.add(tvChoice3);

    //get the correct answer

    int correctNumber = (int) ((Math.random() * 7) + 1);
    Log.d("tons","correctNumber:"+correctNumber);

    TextView tvChoice = tvChoices.get((int) (Math.random() * tvChoices.size()));
    tvChoice.setText(correctNumber + "");
    tvChoice.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        playCorrect();
      }
    });
    tvChoices.remove(tvChoice);


    View.OnClickListener listener = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        playWrong();
      }
    };

    tvChoices.get(0).setOnClickListener(listener);
    tvChoices.get(1).setOnClickListener(listener);

    switch ((int) (Math.random() * 3)) {
      case 0:
        tvChoices.get(0).setText((correctNumber - 1) + "");
        tvChoices.get(1).setText((correctNumber + 1) + "");
        break;
      case 1:
        tvChoices.get(1).setText((correctNumber - 2) + "");
        tvChoices.get(0).setText((correctNumber - 1) + "");
        break;
      case 2:
        tvChoices.get(0).setText((correctNumber + 1) + "");
        tvChoices.get(1).setText((correctNumber + 2) + "");
        break;

    }


    int bitmap = GameMap.getInstance().getRandomBitmap();

/*    while(bitmap == bitmapBefore){
      bitmap = GameMap.getInstance().getRandomBitmap();
    };

    bitmapBefore = bitmap;*/

    for(int i = 0; i < imgObjects.size(); i++){
      if(i >= correctNumber){
        imgObjects.get(i).setVisibility(View.INVISIBLE);
      }else{
        imgObjects.get(i).setVisibility(View.VISIBLE);
        imgObjects.get(i).setImageResource(bitmap);
      }

    }

  }


  @Override
  public void onBackPressed() {
    //super.onBackPressed();
    return;
  }


  public void playCorrect() {
    mp = (MediaPlayer.create(this, R.raw.right));
    if (mp != null)
      mp.start();


    stopGame(true);
  }

  public void playWrong() {
    mp = (MediaPlayer.create(this, R.raw.wrong));
    if (mp != null)
      mp.start();

    stopGame(false);
  }

  private void stopGame(boolean isCorrect) {
    Activity a = (Activity) this;
    Intent i = new Intent(a, ThankYouActivity.class);
    i.putExtra("gameType", "counting");
    i.putExtra("goodJob", isCorrect);

    if (this.currQuestion > MainActivity.numQuestions-2) {
      i.putExtra("lastQuestion", true);
    }

    a.startActivity(i);
    a.finish();
  }
}
