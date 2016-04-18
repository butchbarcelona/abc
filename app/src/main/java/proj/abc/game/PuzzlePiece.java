package proj.abc.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.bunny.game.framework.GraphicObject;

public class PuzzlePiece extends GraphicObject {

	boolean areYouSelected;
	int index;
	Rect initialBox;
	int padding = 60;
	boolean locked;
	
	public PuzzlePiece(int x, int y, Bitmap bmp, int index) {
		super(x, y, bmp,233,233,PuzzleGame.percentWidth,PuzzleGame.percentHeight);
		setPiece(index);
		this.index = index;
		areYouSelected = false;
		initialBox = getInitialPos();
		locked = false;
		
	}
	
	public Rect getInitialPos(){
		

		
		int top =  ((index/2) *  destHeight) + PuzzleGame.setY(170) + 2 + padding;
		int bottom =  (boundingBox.top +  destHeight) + PuzzleGame.setY(170) + 2 - padding;
		
		int left =  ((index%2) *  destWidth) + PuzzleGame.setX(170) + padding;
		int right =  (boundingBox.left +  destWidth) + PuzzleGame.setX(170) - padding;

		
		return new Rect(left, top, right, bottom);
	}
	

	@Override
	public void onUpdate(long t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDraw(Canvas c) {
		// TODO Auto-generated method stub

	}
	
	public void onTouch(MotionEvent event, Rect ptTouch){
		
	}
	
	public void onMove(Rect ptTouch){
		
		x = ptTouch.centerX()-PuzzleGame.setX(50/2);
		y = ptTouch.centerY()-PuzzleGame.setY(50/2);
		
		if(initialBox.intersect(ptTouch)){
			//rightBox
			x = ptTouch.centerX()-PuzzleGame.setX(50/2);
			y = ptTouch.centerY()-PuzzleGame.setY(50/2);
		}
	}

	
	
	
	public void setPiece(int index){
		//0-8
		// 0 1 2
		// 3 4 5
		// 6 7 8
		
		boundingBox.top =  (index/2) *  height;
		boundingBox.bottom =  boundingBox.top +  height;		
		
		boundingBox.left =  (index%2) *  width;
		boundingBox.right =  boundingBox.left +  width;
	}
	
	
}
