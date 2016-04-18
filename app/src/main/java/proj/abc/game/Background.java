package proj.abc.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.bunny.game.framework.GraphicObject;

public class Background extends GraphicObject {

	public Background(int x, int y, Bitmap bmp) {
		super(x, y, bmp,PuzzleGame.percentWidth,PuzzleGame.percentHeight);
	}

	@Override
	public void onUpdate(long t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDraw(Canvas c) {
		// TODO Auto-generated method stub

	}

}
