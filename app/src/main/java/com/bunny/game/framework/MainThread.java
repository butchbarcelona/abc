package com.bunny.game.framework;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {


	private SurfaceHolder surfaceHolder;
	private GameView gameView;
	private boolean running;
	private long timer;
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public MainThread(SurfaceHolder holder, GameView gameView2) {
		// TODO Auto-generated constructor stub
		super();
		this.surfaceHolder = holder;
		this.gameView = gameView2;
	}

	@Override
	public void run() {
		Canvas c;
		int nFrames = 0;
		long sleepTime, delay = 30, beforeTime;
		
		//start = System.currentTimeMillis();
		
		while (running) {

			beforeTime = System.nanoTime();
			
			timer = System.currentTimeMillis();
			++nFrames;

			//Game
            c = null;
            try {
            	c = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
                	gameView.Update(timer);
                	gameView.onDraw(c);
                	
                }
            } finally {
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
            //end Game
			
            //for consistency of speed on various devices
		    sleepTime = delay-((System.nanoTime()-beforeTime)/1000000L);
		    
		    try {
                if(sleepTime>0){
                	Thread.sleep(sleepTime);
                }
            } catch (InterruptedException ex) {
            	//Log.e(TAG,"Could not sleep for delay . "+ex.getMessage());
            }
		}

		this.interrupt();
		
		//end = System.currentTimeMillis();	
		
	}
}