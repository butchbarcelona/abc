package proj.abc.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.bunny.game.framework.GraphicObject;

/**
 * Created by mbarcelona on 4/13/16.
 */
public class AlphabetPiece extends GraphicObject {

  boolean areYouSelected;
  int index;
  Rect initialBox;
  int padding = 60;
  boolean locked;
  int wordIndex;
  int wordCount;

  public boolean hasLetter = false;

  public AlphabetPiece(int x, int y, Bitmap bmp, int index, int wordIndex, int wordCount) {
    super(x, y, bmp, 53, 120, SpellingGame.percentWidth, SpellingGame.percentHeight);
    setPiece(index);
    this.index = index;
    areYouSelected = false;
    this.wordIndex = wordIndex;
    this.wordCount = wordCount;
    initialBox = getInitialPos();
    locked = false;

  }

  public Rect getInitialPos() {

    int x = (1020/2) - ((wordCount* (width+50))/2);
    int y = 220;

    int left = ((wordIndex) % wordCount)* width + ((wordIndex-1)*50);
      //* width ;
    int right = left + width;

    int top = 0;//((wordIndex) % wordCount)*height;
    int bottom = top + height;


    Rect rect = new Rect(left, top, right, bottom);
    rect.offset(x,y);

    return rect;
  }


  @Override
  public void onUpdate(long t) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onDraw(Canvas c) {
    // TODO Auto-generated method stub
  }

  public void onTouch(MotionEvent event, Rect ptTouch) {

  }

  public void onMove(Rect ptTouch) {

    x = ptTouch.centerX() - SpellingGame.setX(50 / 2);
    y = ptTouch.centerY() - SpellingGame.setY(50 / 2);

    if (initialBox.intersect(ptTouch)) {
      //rightBox
      x = ptTouch.centerX() - SpellingGame.setX(50 / 2);
      y = ptTouch.centerY() - SpellingGame.setY(50 / 2);
    }
  }


  public void setPiece(int index) {
    //0-8
    // 0 1 2
    // 3 4 5
    // 6 7 8

    boundingBox.top = (index / 13) * height;
    boundingBox.bottom = boundingBox.top + height;

    boundingBox.left = (index % 13) * width;
    boundingBox.right = boundingBox.left + width;


  }
}

