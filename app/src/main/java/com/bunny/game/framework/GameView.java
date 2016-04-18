package com.bunny.game.framework;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public abstract class GameView extends SurfaceView implements
SurfaceHolder.Callback 
{
	public int outFPS = 0;
	public MainThread thread;

	public GameView(Context context, AttributeSet s) {
		super(context);
		// TODO Auto-generated constructor stub

		getHolder().addCallback(this);		
		setFocusable(true);		
	}

	public GameView(Context context, int w, int h) {
		super(context);
		// TODO Auto-generated constructor stub

		getHolder().addCallback(this);		
		setFocusable(true);		
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		createThread();
	}


	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		boolean retry = true;
		
		while (retry) 
		{
			thread.interrupt();
			thread.setRunning(false);
			retry = false;
		}
	}	

	
	public void createThread()
	{
		thread = new MainThread(getHolder(), this);
		thread.setRunning(true);
		thread.start();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(thread != null){
			if(thread.isAlive() &&  !thread.isInterrupted())
				this.Touch(event);
			if(event.getAction() == MotionEvent.ACTION_MOVE 
					|| event.getAction() == MotionEvent.ACTION_UP 
					|| event.getAction() == MotionEvent.ACTION_POINTER_DOWN
					|| event.getAction() == MotionEvent.ACTION_POINTER_UP
					|| event.getAction() == MotionEvent.ACTION_DOWN)
			{	
				return true;
			}
		}
		return false;
	} 

	@Override
	public void onDraw(Canvas c) {
		if(c != null)
			this.Draw(c);
	}
	
	abstract public void Destroy();
	abstract public void Touch(MotionEvent event);
	abstract public void Draw(Canvas c);
	abstract public void Update(long t);
	abstract public void init(int winWidth, int winHeight);

}


