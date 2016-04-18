package proj.abc.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.bunny.game.framework.GameView;

import java.util.ArrayList;

import proj.abc.R;
import proj.abc.ThankYouActivity;

/**
 * Created by mbarcelona on 4/8/16.
 */
public class Choice3Game extends GameView {


  public static float percentWidth, percentHeight;
  private BitmapFactory.Options ops;
  private Context ctx;
  private MediaPlayer mp;
  private MotionEvent touchEvent;
  private Rect ptTouch;

  ArrayList<Choice3Object> choices;
  Choice3Object question;

  int correctObject = 0;

  public Choice3Game(Context context){
    super(context, null);
    this.ctx = context;

    ops = new BitmapFactory.Options();
    ops.inPurgeable = true;
    choices = new ArrayList<Choice3Object>();
  }

  public Choice3Game(Context context, AttributeSet s) {
    super(context, s);
  }

  public Choice3Game(Context context, int w, int h) {
    super(context, w, h);
  }

  public void setQuestionAndChoices(GameData gameData) {
    //TODO: diff layout horizontal, vertical, random

    question = new Choice3Object(10,10, getBitmap(gameData.getQuestion()));
    this.correctObject = gameData.getCorrectChoice();
    for(Integer resId: gameData.getChoices()) {
      choices.add(new Choice3Object(10,10, getBitmap(resId)));
    }
  }


  @Override
  public void init(int winWidth, int winHeight){
    this.percentHeight = (780-winHeight)/780.0f;
    this.percentWidth = (1280-winWidth)/1280.0f;
  }

  @Override
  public void Destroy() {

  }

  @Override
  public void Touch(MotionEvent event) {

    if(ptTouch == null)
      ptTouch = new Rect();
    ptTouch.bottom = (int) (event.getY()+1);
    ptTouch.top = (int) (event.getY()-1);
    ptTouch.left = (int) (event.getX()-1);
    ptTouch.right = (int) (event.getX()+1);

    switch(event.getAction()) {

      case MotionEvent.ACTION_MOVE:
        break;
      case MotionEvent.ACTION_DOWN:
        break;
      case MotionEvent.ACTION_UP:

        for(int i = 0; i < choices.size(); i++){
          if(ptTouch.intersect(choices.get(i).dest) && i == correctObject) {
            playCorrect();
          }else if(ptTouch.intersect(choices.get(i).dest)){
            playWrong();
          }
        }



        break;
    }

  }

  @Override
  public void Draw(Canvas c) {
    c.drawColor(Color.WHITE);

    question.onDraw(c);

    for(Choice3Object obj: choices){
      obj.onDraw(c);
    }

  }

  @Override
  public void Update(long t) {

  }

  public Bitmap getBitmap(int id){
    return BitmapFactory.decodeResource(ctx.getResources(), id, ops);
  }


  public void playCorrect (){
    mp = (MediaPlayer.create(ctx, R.raw.right));
    if(mp!= null)
      mp.start();


    stopGame(true);
  }
  public void playWrong(){
    mp = (MediaPlayer.create(ctx, R.raw.wrong));
    if(mp!= null)
      mp.start();

    stopGame(false);
  }

  private void stopGame(boolean isCorrect) {
    Activity a = (Activity)ctx;
    Intent i = new Intent(a,ThankYouActivity.class);
    i.putExtra("goodJob",isCorrect);
    a.startActivity(i);
    a.finish();
  }
}
