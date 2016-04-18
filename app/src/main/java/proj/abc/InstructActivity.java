package proj.abc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import proj.abc.game.CountingGame;
import proj.abc.game.GameActivity;
import proj.abc.game.GameMap;
import proj.abc.game.LetterGame;

/**
 * Created by mbarcelona on 4/15/16.
 */
public class InstructActivity extends AppCompatActivity implements View.OnClickListener {


  Button btnBack, btnPlay;

  GameType gameType;
  StudentLevel studentLevel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN);


    String strGame = this.getIntent().getStringExtra("gameType");
    if(strGame == null || strGame.isEmpty())
      strGame = "letters";
    gameType = GameType.getGameKind(strGame);

    studentLevel = StudentLevel.getGrade(GameMap.getInstance().getUser().getLevel());

    setContentView(R.layout.activity_instruct);

    btnBack = (Button) findViewById(R.id.btn_back);
    btnPlay = (Button) findViewById(R.id.btn_play);

    btnBack.setOnClickListener(this);
    btnPlay.setOnClickListener(this);

    TextView tvInstruct = (TextView) findViewById(R.id.tv_instruct);

    String strInstruct = "";

    switch(gameType){

      case LETTER:
        switch(studentLevel){
          case NURSERY:
            strInstruct = "Choose the picture that begins with the given letter. ";
            break;
          case KINDER:
            strInstruct = "Choose the letter that will complete the given word.";
            break;
          case PREP:
            strInstruct = "Choose the letters that would identify the given object.";
            break;
        }
        break;
      case COLOR:
        switch(studentLevel){
          case NURSERY:
            strInstruct = "Choose the picture of the given color. ";
            break;
          case KINDER:
            strInstruct = "Choose the color of the given picture. ";
            break;
        }
        break;
      case SHAPE:
        switch(studentLevel){
          case NURSERY:
            strInstruct = "Choose the shape of the given name of the shape. ";
            break;
          case KINDER:
            strInstruct = "Tap the shape that is similar to the picture.";
            break;
        }
        break;
      case COUNTING:
        switch(studentLevel){
          case NURSERY:
            strInstruct = "Count the number of the images and choose how many are there. ";
            break;
          case KINDER:
            strInstruct = "Choose the missing number from the sequence.";
            break;
          case PREP:
            strInstruct = "Tap the correct answer on the given equation.";
            break;
        }
        break;
      case PATTERN:
        strInstruct = "Tap the picture that comes next on the given pattern.";
        break;
      case PUZZLE:
        strInstruct = "Drag and drop the images to form the given image.";
        break;
    }

    tvInstruct.setText(strInstruct);

  }

  @Override
  public void onClick(View v) {

    switch(v.getId()){
      case R.id.btn_back:
        finish();
        break;
      case R.id.btn_play:
        gotoGame(gameType);
        break;
    }
  }

  public void gotoGame(GameType gameType){


    SharedPreferences prefs = this.getSharedPreferences(MainActivity.APP_CODE
      , Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();


    CountingGame.currQuestion = 0;
    LetterGame.currQuestion = 0;
    ColorNurseryGameActivity.currQuestion = 0;

    switch (gameType) {

      case LETTER:
        editor.putInt("letters", 0);

        editor.commit();
        GameMap.getInstance().setUpGameData(studentLevel, gameType);

        switch (studentLevel) {
          case NURSERY:
            startActivity(new Intent(InstructActivity.this, GameActivity.class)); break;
          case KINDER:
            startActivity(new Intent(InstructActivity.this, LetterKinderGameActivity.class));
            break;
          case PREP:
            gameType = GameType.LETTER;
            GameMap.getInstance().setUpGameData(studentLevel, gameType);
            startActivity(new Intent(InstructActivity.this, GameActivity.class));
            break;
        }
        break;
      case COUNTING:
        editor.putInt("counting", 0);
        editor.commit();
        GameMap.getInstance().setUpGameData(studentLevel, gameType);
        switch (studentLevel) {
          case NURSERY:
            startActivity(new Intent(InstructActivity.this, CountingNurseryGameActivity.class));
            break;
          case KINDER:
            startActivity(new Intent(InstructActivity.this, CountingKinderGameActivity.class));
            break;
          case PREP:
            startActivity(new Intent(InstructActivity.this, CountingPrepGameActivity.class));
            break;
        }
        break;
      case COLOR: case PUZZLE:
        GameMap.getInstance().setUpGameData(studentLevel, gameType);
        editor.putInt("colors", 0);
        editor.putInt("puzzles", 0);
        editor.commit();
        switch (studentLevel) {
          case NURSERY:
            startActivity(new Intent(InstructActivity.this, ColorNurseryGameActivity.class));
            break;
          case KINDER:
            startActivity(new Intent(InstructActivity.this, ColorKinderGameActivity.class));
            break;
          case PREP:
            gameType = GameType.PUZZLE;
            GameMap.getInstance().setUpGameData(studentLevel, gameType);
            startActivity(new Intent(InstructActivity.this, GameActivity.class));
            break;
        }
        break;
      case SHAPE: case PATTERN:
        GameMap.getInstance().setUpGameData(studentLevel, gameType);
        editor.putInt("shapes", 0);
        editor.putInt("patterns", 0);
        editor.commit();
        switch (studentLevel) {
          case NURSERY:
            startActivity(new Intent(InstructActivity.this, ShapeNurseryGameActivity.class));
            break;
          case KINDER:
            startActivity(new Intent(InstructActivity.this, ShapeKinderGameActivity.class));
            break;
          case PREP:
            gameType = GameType.PATTERN;
            GameMap.getInstance().setUpGameData(studentLevel, gameType);
            startActivity(new Intent(InstructActivity.this, PatternPrepGameActivity.class));
            break;
        }
        break;
    }
    finish();



  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    return;
  }
}