package com.bunny.game.framework;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

@SuppressLint("WrongCall")
abstract public class GraphicObject {

	/*	
	 * 	Include " _notresize" in name if object is not to be resized	 
	 */

	public String  name;
	public Bitmap  bitmap;
	public Rect  boundingBox //src rect of bitmap. bounds of current view in image
				, dest; //dest rect of bitmap. 
	public Paint  paint;
	
	public Matrix  transform; //used for rotation and scaling
	
	public long  frameTimer  = 0;
	
	public int  x,  y, 
				fps = 1000/5,  
				alpha = 255,  
				numFrames,  //number of frames
				currFrame  = 0, //current frame
				destWidth,  destHeight,  //used for the visual width and height
				height,  width; // used for animating of sprites. width and height of bounding box when animating.
	
    
	public boolean animate = false, //should be true when you want your object to animate frames 0 to numFrames-1. automatically true when numFrames > 1.
					stop = false, //should be true if you want to animate only once
					flippedY = false, //used for recomputation of spritesheet for flipped assets in the y axis
					flippedX = false, 
					rotated = false, 
					doNotResize = false; 
	
	public float pWidth, pHeight, //difference of current window size to 480x320 in percent
				 scaleX = 1, scaleY = 1, 
				 rotDeg = 0.0f;
	
	public int oldWidth, oldHeight, oldX, oldY; //initial x and y that are not yet computed for screen size.
	
	public boolean isUp = false, isDown = false, isRight = false, isLeft = false; //used for shake function

	public GraphicObject(String n, int x, int y, Bitmap bmp, int nFrames, int w, int h, float pW,float pH ) {
		name = n;
		bitmap = bmp;
		boundingBox = new Rect(0,0,w,h);
		pWidth = pW;
		pHeight = pH;    
		if( name.contains("notresize"))
		{
			destHeight = h;
			destWidth = w;
			doNotResize = true;
			name = name.split("_notresize")[0];
		}else{
			destHeight = (int)Math.round(h-(h* pHeight));
			destWidth = (int)Math.round(w-(w* pWidth));
			

			boundingBox = new Rect(0,0,destWidth,destHeight);

		
			height = destHeight;
			width = destWidth;
			
		}

		dest = new Rect();
		setX(x);
		setY(y);
		height = h;
		width = w;
		numFrames = nFrames;

		height = destHeight;
		width = destWidth;

		oldWidth = destWidth;
		oldHeight = destHeight;
		oldX = x;
		oldY = y;
		
	}
	public GraphicObject(int x, int y, Bitmap bmp,int w, int h, float pW,float pH ) {

		bitmap = bmp;
		boundingBox = new Rect(0,0,w,h);
		pWidth = pW;
		pHeight = pH;
		
		destHeight = (int)Math.round(h-(h* pHeight));
		destWidth = (int)Math.round(w-(w* pWidth));

		boundingBox = new Rect(0,0,destWidth,destHeight);

		dest = new Rect();
		setX(x);
		setY(y);
		height = destHeight;
		width = destWidth;
		

		oldWidth = destWidth;
		oldHeight = destHeight;
		oldX = x;
		oldY = y;
	}

	public GraphicObject(int x, int y, Bitmap bmp, float pW,float pH ) {

		width = bmp.getWidth(); 
		height = bmp.getHeight(); 
		
		
		bitmap = bmp;
		boundingBox = new Rect(0,0,width,height);
		pWidth = pW;
		pHeight = pH;
		

		destHeight = (int)Math.round(height-(height* pHeight));
		destWidth = (int)Math.round(width-(width* pWidth));
		
		

		dest = new Rect();
		setX(x);
		setY(y);
		numFrames = 1;
		
		if(bitmap != null)
			setBitmapWidthHeight();

		oldWidth = width;
		oldHeight = height;
		oldX = x;
		oldY = y;
		
	}
	


	abstract public void onUpdate(long t);
	abstract public void onDraw(Canvas c);

	//reset width and height to the bitmaps width and height
	public void setBitmapWidthHeight()
	{
		if(bitmap != null)
		{
			int h =  bitmap.getHeight();
			int w =  bitmap.getWidth();
			height = h;
			width = w;
			boundingBox.right = w;
			boundingBox.bottom = h;
	/*
			if(doNotResize)
			{
				destHeight = h;
				destWidth = w;
			}else
			*/{
	
				destHeight = (int)Math.round(h-(h* 0));
				destWidth = (int)Math.round(w-(w* 0));
			}
	
			dest.left =  x;
			dest.top =  y;
			dest.right =  x +  destWidth;
			dest.bottom =  y +  destHeight;
		}

	} 

	public void shake(int nShake)
	{
		int choice = Random.Next(4);

		switch (choice)
		{
		case 0:
		{
			if (!isUp)
			{
				isUp = true;
				goUp(nShake);
			}
			else
			{
				isUp = false;
				goDown(nShake);
			}
		} break;
		case 1:
		{
			if (!isDown)
			{
				isDown = true;
				goDown(nShake);
			}
			else
			{
				isDown = false;
				goUp(nShake);
			}
		} break;
		case 2:
		{
			if (!isRight)
			{
				isRight = true;
				goRight(nShake);
			}
			else
			{
				isRight = false;
				goLeft(nShake);
			}
		} break;
		case 3:
		{
			if (!isLeft)
			{
				isLeft = true;
				goLeft(nShake);
			}
			else
			{
				isLeft = false;
				goRight(nShake);
			}
		} break;
		}
	}

	public void setBoundingBox(Rect r)
	{
		boundingBox = r;
		width = r.width();
		height = r.height();
	}

	public void setDest(Rect r)
	{
		dest = r;
		width = r.width();
		height = r.height();
		
		destHeight = height;
		destWidth = width;
	}


	public void goDown(int dist)
	{
		translate(0, dist);
	}

	public void goLeft(int dist)
	{
		translate(-dist, 0);
	}

	public void goUp(int dist)
	{
		translate(0, -dist);
	}

	public void goRight(int dist)
	{
		translate(dist, 0);
	}

	public void setStop(boolean s)
	{
		stop = s;
	}




	public void shakeVertical(int nShake)
	{
		int choice = Random.Next(2);

		switch (choice)
		{
		case 0:
		{
			if (!isUp)
			{
				isUp = true;
				goUp(nShake);
			}
			else
			{
				isUp = false;
				goDown(nShake);
			}
		} break;
		case 1:
		{
			if (!isDown)
			{
				isDown = true;
				goDown(nShake);
			}
			else
			{
				isDown = false;
				goUp(nShake);
			}
		} break;

		}
	}

	public void shakeHorizontal(int nShake)
	{
		int choice = Random.Next(2);

		switch (choice)
		{
		case 0:
		{
			if (!isLeft)
			{
				isLeft = true;
				goLeft(nShake);
			}
			else
			{
				isLeft = false;
				goRight(nShake);
			}
		} break;
		case 1:
		{
			if (!isRight)
			{
				isRight = true;
				goRight(nShake);
			}
			else
			{
				isRight = false;
				goLeft(nShake);
			}
		} break;

		}
	}

	public void skew(float degree, int pX, int pY)
	{
		transform = new Matrix();
		Matrix  t = new Matrix();
		t.setSkew(degree/100, 0, pX,  pY);
		//		t.preRotate(degree);
		t.postTranslate(this.x,this.y);
		transform.setConcat( transform,  t);
	}
	public void skew2(float degree, int pX, int pY, int x1)
	{
		transform = new Matrix();
		Matrix  t = new Matrix();
		t.setSkew(degree/100, 0, pX,  pY);
		t.postTranslate(this.x-80,this.y);
		transform.setConcat( transform,  t);
	}

	public void skewRotate(float degree, int pX, int pY)
	{
		transform = new Matrix();
		Matrix  t = new Matrix();
		t.postSkew(degree/100, 0, pX,  pY);
		t.preRotate(-degree, pX,  pY);
		t.postTranslate(this.x,this.y);
		transform.setConcat( transform,  t);

		rotated = true;
	}

	//rotates the object clockwise by a given degree
	public void rotate(float degree)
	{
		rotDeg = degree;
		if(transform == null)
			transform = new Matrix();

		transform.setTranslate(dest.left,dest.top);
		transform.preRotate(degree,  width/2,  height/2);
		
	}

	public void rotate(float degree, int pX, int pY)
	{
		rotDeg = degree;
		if(transform == null)
			transform = new Matrix();

		transform.setTranslate(dest.left,dest.top);
		transform.preRotate(degree,  pX, pY);
	}

	public void rotateBitmap(float deg, int pX, int pY)
	{
		transform = new Matrix();

		transform.preRotate(deg,bitmap.getWidth()/2,bitmap.getHeight()/2);
		transform.setTranslate(0,0);
		bitmap = Bitmap.createBitmap( bitmap, 0, 0,  bitmap.getWidth(),  bitmap.getHeight(),  transform, false);
		transform = null;
		rotated = true;
	}
	
	public void scale(float s)
	{
		float pX = 1.0f - pWidth, pY = 1.0f - pHeight;
		scaleX = s; scaleY = s;
		pX *= scaleX; pY *= scaleY;
		
		if(transform == null)
		{
			destWidth = (int) (oldWidth*pX);
			destHeight = (int) (oldHeight*pY);
			
		}else 
		{
			transform.preScale(pX, pY);
		}
	}

	public void scale(float sX, float sY)
	{
		float pX = 1.0f - pWidth, pY = 1.0f - pHeight;
		scaleX = sX; scaleY = sY;
		pX *= scaleX; pY *= scaleY;
		
		if(transform == null)
		{
			destWidth = (int) (oldWidth*pX);
			destHeight = (int) (oldHeight*pY);
			
		}else 
		{
			transform.preScale(sX, sY);
		}
		
		/*transform = new Matrix();
		transform.postScale (sDeg, sDeg);
		transform.setTranslate(this.x, this.y);

		scaleValue = sDeg;*/
	}

	public static Bitmap scale(Bitmap bm, float sW, float sH) {

		int width = bm.getWidth();

		int height = bm.getHeight();

		// create a matrix for the manipulation

		Matrix matrix = new Matrix();

		// resize the bit map

		matrix.postScale(sW,sH);

		// recreate the new Bitmap
		Bitmap resizedBitmap = null;
		
		
		if(width > 0 && height > 0)
			resizedBitmap = Bitmap.createBitmap(  bm, 0, 0, width, height, matrix, false);

		return resizedBitmap;

		}
	
	public static Bitmap scale(Bitmap bm, float sDeg) {

		int width = bm.getWidth();

		int height = bm.getHeight();

		// create a matrix for the manipulation

		Matrix matrix = new Matrix();

		// resize the bit map

		matrix.postScale(sDeg,sDeg);

		// recreate the new Bitmap

		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

		return resizedBitmap;

		}

	public void scaleDest(int w, int h)
	{
		dest.right = w;
		dest.bottom = h;
		destHeight = h;
		destWidth = w;

		//		destHeight = (int)(h-(h* pHeight));
		//		destWidth = (int)(w-(w* pWidth));
	}

	public void scaleBoundingBox(int x, int y, int w, int h)
	{
		width = w;
		height = h;
		boundingBox.right = w;
		boundingBox.bottom = h;

		boundingBox.left = x;
		boundingBox.top = y;
	}

	public void scaleBitmap(float sDeg)
	{

		transform = new Matrix();
		transform.postScale (sDeg, sDeg);
		transform.setTranslate(this.x, this.y);
		bitmap = Bitmap.createBitmap( bitmap, 0, 0,  bitmap.getWidth(),  bitmap.getHeight(),  transform, false);
		transform = null;
	}
	
	public static Bitmap flip(Bitmap bmp, boolean x, boolean y)
	{
		Matrix t = new Matrix();
		t.setScale(-1.0f, 1.0f);
		t.postTranslate(0,0);

		bmp = Bitmap.createBitmap( bmp, 0, 0,  bmp.getWidth(),  bmp.getHeight(),  t, false);
		return bmp;
	}
	
	public static Bitmap shadow(Bitmap bmp)
	{
		Bitmap sourceBitmap = bmp;

	    ColorMatrix colorMatrix = new ColorMatrix();
	    colorMatrix.setSaturation(0f); //Remove Colour 
	    //colorMatrix.set(colorTransform); //Apply the Red
	    

	    ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
	    Paint paint = new Paint();
	    paint.setColorFilter(colorFilter);   

	    //return paint;
	    Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, bmp.getWidth(), bmp.getHeight()); 
	   //return resultBitmap;
	    for(int x = 0; x < bmp.getWidth(); ++x)
	    	for(int y = 0; y < bmp.getHeight(); ++y)
	    	{
	    		int c = resultBitmap.getPixel(x, y);
	    		
	    		if(Color.alpha(c) > 0)
	    			resultBitmap.setPixel(x, y, Color.argb(255, 0,0,0));
	    	}
	    return resultBitmap;
	}
	

	public void flipY()
	{
		transform = new Matrix();
		transform.setScale(-1.0f, 1.0f);
		transform.postTranslate(this. x,this. y);

		bitmap = Bitmap.createBitmap( bitmap, 0, 0,  bitmap.getWidth(),  bitmap.getHeight(),  transform, false);
		transform = null;
		flippedY = ! flippedY;
	}

	public void flipX()
	{
		transform = new Matrix();

		transform.setScale(1.0f, -1.0f);
		transform.postTranslate(dest.centerX(),dest.centerY());

		bitmap = Bitmap.createBitmap( bitmap, 0, 0,  bitmap.getWidth(),  bitmap.getHeight(),  transform, false);
		transform = null;
		flippedX = ! flippedX;
	}

	public void flipXY()
	{
		transform = new Matrix();

		transform.setScale(-1.0f, -1.0f);
		transform.postTranslate(dest.centerX(),dest.centerY());

		bitmap = Bitmap.createBitmap( bitmap, 0, 0,  bitmap.getWidth(),  bitmap.getHeight(),  transform, false);
		transform = null;
		flippedX = ! flippedX;
		flippedY = ! flippedY;
	}

	public void setWidth(int  w) {
		this. width =  w;
		boundingBox.left =  currFrame *  w;
		boundingBox.right =  boundingBox.left +  w;
		if( doNotResize)
		{
			destWidth =  w;
		}else{
			destWidth = (int)( w-( w* pWidth));
		}
		dest.right =  x +  destWidth;
	}

	public void setHeight(int  h) {
		this. height =  h;
		if(!doNotResize)
			destHeight =  h;
	
		dest.bottom =  y +  destHeight;
	}
	public void setFPS(int f)
	{
		fps = 1000/f;
	}

	
	public void setPaintColor(int c)
	{
		if(paint == null)
			paint = new Paint();
		
		
		paint.setColor(c);
	}
	
	public void setCurrFram(int curr)
	{
		currFrame = curr;

		if( flippedY){
			currFrame = ( numFrames-1)- currFrame;
		}
		boundingBox.left =  currFrame *  width;
		boundingBox.right =  boundingBox.left +  width;
	}

	public void nextFrame()
	{
		++ currFrame;
		setCurrFram( currFrame);
	}

	public void previousFrame()
	{
		-- currFrame;
		setCurrFram( currFrame);
	}

	public void enableAnimation(int f)
	{
		fps = 1000/f;
		animate = true;
	}

	public void enableAnimation()
	{
		animate = true;
	}

	public void disableAnimation(int numFrame)
	{	
		animate = false;
		stop = false;
		setCurrFram(numFrame);
	}

	public void disableAnimation()
	{	
		animate = false;
	}

	public boolean isAnimating()
	{
		return  animate;
	}

	public void translate(int x1,int y1)
	{
		x += x1;
		y += y1;
		
		if(transform != null)
		{
			transform.postTranslate(x1, y1);
		}
	}

	public void setX(int x1)
	{	
	
		
		x = (int)Math.round(x1-(x1*( pWidth*1.0f)));
		dest.left =  x;
		dest.right =  x +  destWidth;      
		
		
		
	}

	public void setY(int y1)
	{	 
		
		y = (int)Math.round(y1-(y1*( pHeight*1.0f)));
		//Log.d("", name+" y: "+y+" pHeightS: "+ pHeight+"  y: "+ y);
		dest.top =  y;
		dest.bottom =  y+  destHeight;
	
	}
	public static float setOutX(float x1, float p)
	{	return (int)(x1-(x1*(p*1.0f)));
	//Log.d("", name+" x: "+x+" pWidth: "+ pWidth+"  x: "+ x);

	}

	public static float setOutY(float y1, float p)
	{	 return (int)(y1-(y1*(p*1.0f)));
	//Log.d("", name+" y: "+y+" pHeightS: "+ pHeight+"  y: "+ y);
	}
	
	public static int setOutX(int x1, float p)
	{	return (int)(x1-(x1*(p*1.0f)));
	//Log.d("", name+" x: "+x+" pWidth: "+ pWidth+"  x: "+ x);

	}

	public static int setOutY(int y1, float p)
	{	 return (int)(y1-(y1*(p*1.0f)));
	//Log.d("", name+" y: "+y+" pHeightS: "+ pHeight+"  y: "+ y);
	}

	public void setXY(int x, int y)
	{
		
		setX(x);
		setY(y);
	}

	public void copyXY(int x1, int y1){
		x = x1;
		y = y1;
		dest.left =  x;
		dest.right =  x +  destWidth;
		dest.top =  y;
		dest.bottom =  y +  destHeight;
	}

	public void setDestRect(Rect destR)
	{
		dest = destR;   
	}


	public void changeAlpha(int i)
	{

		alpha = i;
		
		if(alpha > 255)
			alpha = 255;
		else if(alpha < 0 )
			alpha = 0;
		
		if(paint == null)
			paint = new Paint();
		paint.setAlpha( alpha);
	}

	//set width and height
	public void setWidthHeight(int w, int h)
	{
		height = h;
		width = w;
		boundingBox.left =  currFrame *  width;
		boundingBox.right =  boundingBox.left +  width;
		boundingBox.bottom = height;
		if( doNotResize)
		{
			destHeight = h;
			destWidth = w;
		}else{
			destHeight = (int)(h-(h* pHeight));
			destWidth = (int)(w-(w* pWidth));
		}
		dest.left =  x;
		dest.top =  y;
		dest.right =  x +  destWidth;
		dest.bottom =  y +  destHeight;
	}

	public void update(long GameTime) {
		onUpdate(GameTime);

		if( animate){ //disable animate if you don't want to animate the object
			if(GameTime >  frameTimer +  fps ) {
				frameTimer = GameTime;
				currFrame +=1;

				if( currFrame >=  numFrames) {
					currFrame = 0;
					if(stop) //enable stop if you want to animate only once
						animate = false;
				}
			}

			if(!rotated){
				boundingBox.left =  currFrame *  width;
				boundingBox.right =  boundingBox.left +  width;
			}else
			{

				boundingBox.top =  currFrame *  height;
				boundingBox.bottom =  boundingBox.top +  height;

			}

		}



	}

	public void draw(Canvas canvas) {

		onDrawBefore(canvas);
		
		if( bitmap != null)
		{
			dest.left =  x;
			dest.top =  y;
			dest.right =  x +  destWidth;
			dest.bottom =  y +  destHeight;

			if( transform == null)
				canvas.drawBitmap( bitmap,  boundingBox,  dest,((paint != null)? paint:null));
			else{
				canvas.drawBitmap( bitmap,  transform,  ((paint != null)? paint:null));
			}
			
			
		}
		onDraw(canvas);

	}
	
	public void onDrawBefore(Canvas c)
	{  
		
	}

	public static Bitmap doShadow(Bitmap src) {
		// create new bitmap with the same settings as source bitmap
		Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
		int A, R, G, B;
		int pixelColor;
		int height = src.getHeight();
		int width = src.getWidth();

	    for (int y = 0; y < height; y++)
	    {
	        for (int x = 0; x < width; x++)
	        {
	            pixelColor = src.getPixel(x, y);
	            A = Color.alpha(pixelColor);
	            R = 0;
	            G = 0;
	            B = 0;
	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	        }
	    }

	    return bmOut;
	}
	
	public static Bitmap doLight(Bitmap src) {
		// create new bitmap with the same settings as source bitmap
		Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
		int A, R, G, B;
		int pixelColor;
		int height = src.getHeight();
		int width = src.getWidth();

	    for (int y = 0; y < height; y++)
	    {
	        for (int x = 0; x < width; x++)
	        {
	            pixelColor = src.getPixel(x, y);
	            A = Color.alpha(pixelColor);
	            R = 255;
	            G = 255;
	            B = 255;
	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	        }
	    }

	    return bmOut;
	}
	
	//taken from http://xjaphx.wordpress.com/2011/06/20/image-processing-invert-image-on-the-fly/
	public static Bitmap doInvert(Bitmap src) {
		//bitmap must have rgb
		// create new bitmap with the same settings as source bitmap
		Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
		// color info
		int A, R, G, B;
		int pixelColor;
		// image size
		int height = src.getHeight();
		int width = src.getWidth();

		// scan through every pixel
	    for (int y = 0; y < height; y++)
	    {
	        for (int x = 0; x < width; x++)
	        {
	        	// get one pixel
	            pixelColor = src.getPixel(x, y);
	            // saving alpha channel
	            A = Color.alpha(pixelColor);
	            // inverting byte for each R/G/B channel
	            R = 255 - Color.red(pixelColor);
	            G = 255 - Color.green(pixelColor);
	            B = 255 - Color.blue(pixelColor);
	            // set newly-inverted pixel to output image
	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	        }
	    }

	    // return final bitmap
	    return bmOut;
	}
}