package proj.abc;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import proj.abc.game.GameMap;

/**
 * Created by mbarcelona on 4/12/16.
 */
public class CountingKinderGameActivity extends Activity {


  TextView tvColor;
  TextView tvQuestNum;

  TextView tvChoice1, tvChoice2, tvChoice3;

  private MediaPlayer mp;
  public static int[] questions;
  public static int currQuestion;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_counting_kinder);

    tvChoice1 = (TextView) findViewById(R.id.tv_choice1);
    tvChoice2 = (TextView) findViewById(R.id.tv_choice2);
    tvChoice3 = (TextView) findViewById(R.id.tv_choice3);

    tvColor = (TextView) findViewById(R.id.tv_color);
    tvQuestNum = (TextView) findViewById(R.id.tv_score);

    HashMap<String, Integer> letterData = GameMap.getInstance().getLetterData();

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

    int count = 0;
    String currColor = "";


    int correctNumber = (int) ((Math.random() * 9) + 1);

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
      case 0: //before
        tvColor.setText(" __ " + (correctNumber + 1));
        tvChoices.get(0).setText((correctNumber - 1) + "");
        tvChoices.get(1).setText(((int) (Math.random() * (10)) + (correctNumber + 1)) + "");
        break;
      case 1: //after
        tvColor.setText((correctNumber - 1) + " __");
        tvChoices.get(0).setText((correctNumber + 1) + "");
        tvChoices.get(1).setText(((int) (Math.random() * (correctNumber - 1))) + "");
        break;
      case 2: //between
        tvColor.setText((correctNumber - 1) + " __ " + (correctNumber + 1));
        tvChoices.get(0).setText(((int) (Math.random() * (correctNumber - 1))) + "");
        tvChoices.get(1).setText(((int) (Math.random() * (10)) + (correctNumber + 1)) + "");
        break;
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
