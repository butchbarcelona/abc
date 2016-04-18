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
import proj.abc.game.GameMap;
import proj.abc.game.LetterGame;
import proj.abc.models.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  public static final String APP_CODE = "ABC";

  Button btnLetter, btnColor, btnShape, btnCounting, btnLogout;

  GameType gameType;
  StudentLevel studentLevel;
  public static final int numQuestions = 10;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN);

    //TODO: for testing purposes
    if(GameMap.getInstance().getUser() == null) {
      //GameMap.getInstance().setUser(new User("Test","Test","Name","Nursery",19));
      GameMap.getInstance().setUser(new User("Test","Test","Name","Kinder",19));
      //GameMap.getInstance().setUser(new User("Test", "Test", "Name", "Prep", 19));
    }

    studentLevel = StudentLevel.getGrade(GameMap.getInstance().getUser().getLevel());
    switch (studentLevel){

      case NURSERY:
      case KINDER:
        setContentView(R.layout.activity_main);
        break;
      case PREP:
        setContentView(R.layout.activity_main_prep);
        break;
    }

    if(GameMap.getInstance().getUser() != null) {
      TextView tvUser = (TextView) findViewById(R.id.tv_user);
      tvUser.setText("Welcome, " + GameMap.getInstance().getUser().fName + "!");
    }

    btnLetter = (Button) findViewById(R.id.btn_letter);
    btnColor = (Button) findViewById(R.id.btn_color); //puzzle
    btnShape = (Button) findViewById(R.id.btn_shape); //pattern
    btnCounting = (Button) findViewById(R.id.btn_counting);


    btnLogout = (Button) findViewById(R.id.btn_logout);

    btnLetter.setOnClickListener(this);
    btnColor.setOnClickListener(this);
    btnShape.setOnClickListener(this);
    btnCounting.setOnClickListener(this);
    btnLogout.setOnClickListener(this);

  }

  @Override
  public void onClick(View v) {


    SharedPreferences prefs = this.getSharedPreferences(MainActivity.APP_CODE
      , Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();


    CountingGame.currQuestion = 0;
    LetterGame.currQuestion = 0;
    ColorNurseryGameActivity.currQuestion = 0;

    switch (v.getId()) {
      case R.id.btn_logout:
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
        break;
      case R.id.btn_letter:
        startActivity(new Intent(MainActivity.this, InstructActivity.class).putExtra("gameType","letters"));
        break;
      case R.id.btn_counting:
        startActivity(new Intent(MainActivity.this, InstructActivity.class).putExtra("gameType", "counting"));
        break;
      case R.id.btn_color:
        startActivity(new Intent(MainActivity.this, InstructActivity.class).putExtra("gameType", "colors"));
        break;
      case R.id.btn_shape:
        startActivity(new Intent(MainActivity.this, InstructActivity.class).putExtra("gameType", "shapes"));
        break;
    }


  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    return;
  }
}

