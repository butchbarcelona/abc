package proj.abc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by mbarcelona on 4/10/16.
 */
public class ScoreboardActivity extends Activity {

  TextView tvScore;
  Button btnBack;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_scoreboard);

    int score = this.getIntent().getIntExtra("score", 0);

    btnBack = (Button) findViewById(R.id.btn_back);
    btnBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    tvScore = (TextView) findViewById(R.id.tv_score);
    tvScore.setText(score+"/10");

  }


  @Override
  public void onBackPressed() {
    return;
  }


}
