package proj.abc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import proj.abc.game.GameActivity;
import proj.abc.game.GameMap;
import proj.abc.services.RestCallServices;
import proj.abc.services.RestCalls;
import proj.abc.services.Util;


public class ThankYouActivity extends Activity{

	private MediaPlayer mp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_end);
		
		RelativeLayout layout = (RelativeLayout) this.findViewById(R.id.end_screen);


		SharedPreferences prefs = this.getSharedPreferences(MainActivity.APP_CODE
			, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();

		if(this.getIntent().getBooleanExtra("goodJob", false)){
			layout.setBackgroundResource(R.drawable.bg_right);

			switch (GameType.getGameKind(this.getIntent().getStringExtra("gameType"))) {
				case LETTER:
					editor.putInt("letters",prefs.getInt("letters",0)+1);
					break;
				case COLOR:
					editor.putInt("colors",prefs.getInt("colors",0)+1);
					break;
				case SHAPE:
					editor.putInt("shapes",prefs.getInt("shapes",0)+1);
					break;
				case COUNTING:
					editor.putInt("counting",prefs.getInt("counting",0)+1);
					break;
				case PATTERN:
					editor.putInt("patterns",prefs.getInt("patterns",0)+1);
					break;
				case PUZZLE:
					editor.putInt("puzzles",prefs.getInt("puzzles",0)+1);
					break;
			}

			editor.commit();
		}else{
			layout.setBackgroundResource(R.drawable.bg_wrong);
		}


		Thread sThread =  new Thread(){
			@Override
			public void run(){

				try
				{
					int waited = 0;
					while (waited < 5000) {
						sleep(100);
						waited += 100;
					}
				}
				catch(InterruptedException ex){
				}  finally {
					nextActivity();

					this.interrupt();
				}
			}
		};
		sThread.start();

	}

	private void nextActivity() {
		if(!this.getIntent().getBooleanExtra("lastQuestion",false)) {

			switch (GameType.getGameKind(this.getIntent().getStringExtra("gameType"))) {
				case LETTER:
					switch(GameMap.studentLevel){
						case NURSERY: startActivity(new Intent(ThankYouActivity.this
							, GameActivity.class)); break;
						case KINDER: startActivity(new Intent(ThankYouActivity.this
							, LetterKinderGameActivity.class)); break;
						case PREP:
							break;
					}
					break;
				case COLOR: case PUZZLE:
					switch(GameMap.studentLevel){
						case NURSERY: startActivity(new Intent(ThankYouActivity.this
							, ColorNurseryGameActivity.class)); break;
						case KINDER: startActivity(new Intent(ThankYouActivity.this
								, ColorKinderGameActivity.class)); break;
						case PREP:startActivity(new Intent(ThankYouActivity.this
							, GameActivity.class)); break;
					}
					break;
				case SHAPE: case PATTERN:
					switch(GameMap.studentLevel){
						case NURSERY: startActivity(new Intent(ThankYouActivity.this
							, ShapeNurseryGameActivity.class)); break;
						case KINDER:startActivity(new Intent(ThankYouActivity.this
							, ShapeKinderGameActivity.class)); break;
						case PREP:startActivity(new Intent(ThankYouActivity.this
							, PatternPrepGameActivity.class)); break;
					}
					break;
				case COUNTING:
					switch(GameMap.studentLevel){
						case NURSERY: startActivity(new Intent(ThankYouActivity.this
							, CountingNurseryGameActivity.class)); break;
						case KINDER:startActivity(new Intent(ThankYouActivity.this
							, CountingKinderGameActivity.class));
							break;
						case PREP:startActivity(new Intent(ThankYouActivity.this
							, CountingPrepGameActivity.class));
							break;
					}
					break;
			}




		}else{

			int score = 0;
			SharedPreferences prefs = this.getSharedPreferences(MainActivity.APP_CODE
				, Context.MODE_PRIVATE);

			switch (GameType.getGameKind(this.getIntent().getStringExtra("gameType"))) {
				case LETTER:
					score = prefs.getInt("letters", 0);
					break;
				case COLOR:
					score = prefs.getInt("colors", 0);
					break;
				case SHAPE:
					score = prefs.getInt("shapes", 0);
					break;
				case COUNTING:
					score = prefs.getInt("counting", 0);
					break;
				case PATTERN:
					score = prefs.getInt("patterns", 0);
					break;
				case PUZZLE:
					score = prefs.getInt("puzzles", 0);
					break;
			}

			sendScore(this.getIntent().getStringExtra("gameType"), score);
		}
		finish();
	}

	public void sendScore(final String gameType, final int score){
		RestCallServices restService = new RestCallServices();

		restService.submitScore(new RestCallServices.RestServiceListener() {
			@Override
			public void onSuccess(RestCalls callType, final String string) {
				Intent intent = new Intent(ThankYouActivity.this, ScoreboardActivity.class);
				intent.putExtra("gameType", gameType);
				intent.putExtra("score", score);
				startActivity(intent);
			}

			@Override
			public void onFailure(RestCalls callType, String string) {

				Util.getInstance().showSnackBarToast(ThankYouActivity.this, "Failed to send score to server.");

				Intent intent = new Intent(ThankYouActivity.this, ScoreboardActivity.class);
				intent.putExtra("gameType", gameType);
				intent.putExtra("score", score);
				startActivity(intent);
/*				Util.getInstance().showDialog(ThankYouActivity.this, "Failed to send score to server.", "OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(ThankYouActivity.this, ScoreboardActivity.class);
						intent.putExtra("gameType" ,gameType);
						intent.putExtra("score",score);
						startActivity(intent);
					}
				});
				*/


			}
		}, GameMap.getInstance().getUser().getId(), gameType, score);
	}


	@Override
	public void onBackPressed() {
		return;
	}
}
