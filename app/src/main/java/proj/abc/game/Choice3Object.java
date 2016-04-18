package proj.abc.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.bunny.game.framework.GraphicObject;

/**
 * Created by mbarcelona on 4/8/16.
 */
public class Choice3Object extends GraphicObject {

  Paint paint;

  public Choice3Object(String n, int x, int y, Bitmap bmp, int nFrames, int w, int h) {
    super(n, x, y, bmp, nFrames, w, h,Choice3Game.percentWidth, Choice3Game.percentHeight);
  }

  public Choice3Object(int x, int y, Bitmap bmp, int w, int h) {
    super(x, y, bmp, w, h,Choice3Game.percentWidth, Choice3Game.percentHeight);
  }


  public Choice3Object(int x, int y, Bitmap bmp) {
    super(x, y, bmp, Choice3Game.percentWidth, Choice3Game.percentHeight);
    paint = new Paint();
    paint.setColor(Color.RED);
  }


  @Override
  public void onUpdate(long t) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onDraw(Canvas c) {
    // TODO Auto-generated method stub
   // c.drawRect(dest,paint);
  }

}