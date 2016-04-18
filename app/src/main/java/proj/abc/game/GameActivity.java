package proj.abc.game;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.bunny.game.framework.GameView;

/**
 * Created by mbarcelona on 4/8/16.
 */
public class GameActivity extends Activity {

  private GameView game;
  private Thread thread;

  private MediaPlayer mp;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN);
    //setContentView(R.layout.view_instruct);
    startGame();
    //startMusic();

    //TODO: get from intent


/*    game = new Choice3Game(this);*/




  }

  public void startMusic(){

/*    mp = MediaPlayer.create(this, R.raw.bg_music);
    mp.setLooping(true);
    mp.start();*/
  }
  @Override
  protected void onStop() {
    // TODO Auto-generated method stub
    super.onStop();

    stopMusic();
  }

  public void stopMusic(){
    if(mp != null)
    {
      try{
        if(mp.isPlaying())
        {
          mp.stop();
        }

        mp.reset();
        mp.release();
      }catch(Exception e){

      }
    }
  }

  private void startGame(){


  createThread();


   /* game = new Choice3Game(this);
    createThread();*/
  }

  @Override
  public boolean onTouchEvent(MotionEvent evt)
  {
    if(evt.getAction() == MotionEvent.ACTION_DOWN)
    {
      synchronized(thread){
        thread.notifyAll();
      }
    }
    return true;
  }
  @Override
  protected void onDestroy() {
    super.onDestroy();
    if(game != null)
      if(game.thread != null)
        this.game.thread.setRunning(false); //sets thread to stop
  }


  @Override
  protected void onPause() {
    super.onPause();
    if(game != null)
      if(game.thread != null)
        this.game.thread.setRunning(false); //sets thread to stop
  }

  public void createThread()
  {
    new InitializeGame().execute();
  }

  private class InitializeGame extends AsyncTask<Void, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Void... arg0) {
      //TODO: not sure why we need to move this?
/*      int width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
      int height = getWindow().getWindowManager().getDefaultDisplay().getHeight();
      ((Choice3Game)game).init(width, height);*/
      return true;
    }

    @Override
    protected void onPostExecute(Boolean b) {
      Log.d("butch", "sets game as view");
      int width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
      int height = getWindow().getWindowManager().getDefaultDisplay().getHeight();

      switch(GameMap.studentLevel){

        case NURSERY:
          switch(GameMap.gameType){

            case LETTER:default:
              game = new LetterGame(GameActivity.this);
              ((GameView)game).init(width, height);
              setContentView(game);
              GameData gameData = GameMap.getInstance().getRandomFromGameData();

              ((LetterGame) game).setQuestionAndChoices(gameData);

              break;
            case COLOR:
              break;
          case SHAPE:
            break;
          case COUNTING:
            game = new CountingGame(GameActivity.this);
            ((GameView)game).init(width, height);
            setContentView(game);
            ((CountingGame) game).setQuestionAndChoices();
            break;
          }
          break;
        case KINDER:
          switch(GameMap.gameType){

            case LETTER:default:

              break;
            case COLOR:
              break;
            case SHAPE:
              break;
            case COUNTING:
              break;
          }
          break;
        case PREP:
          switch(GameMap.gameType){

          case LETTER:default:

              game = new SpellingGame(GameActivity.this);
              ((GameView)game).init(width, height);
              setContentView(game);

            break;
          case COLOR:
            case PUZZLE:
            game = new PuzzleGame(GameActivity.this);
            ((GameView)game).init(width, height);
            setContentView(game);
            break;
          case SHAPE:
            break;
          case COUNTING:
            break;
        }
          break;
      }


    }
  }

  @Override
  public void onBackPressed() {
    //super.onBackPressed();
    return;
  }
}
