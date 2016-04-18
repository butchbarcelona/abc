package proj.abc.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;

import com.bunny.game.framework.GameView;
import com.bunny.game.framework.Random;

import java.util.ArrayList;

import proj.abc.MainActivity;
import proj.abc.R;
import proj.abc.ThankYouActivity;

public class PuzzleGame extends GameView {

	public static int[] questions;
	public static int currQuestion;
	
	public static float percentWidth, percentHeight;

	private MediaPlayer mp;
	//1380x780

	private static final int MAX_TIMER = 20; //-1 the usual time
	
	private static final int BEFORE_GAME = 0;
	private static final int START_GAME = 1;
	private static final int END_GAME = 2;
	
	private int state = BEFORE_GAME;
	
	private long startTime = -1, currTime, millis; 
	private int seconds;
	
	
	private BitmapFactory.Options ops;
	private Context ctx;

	private Paint txtSecondsPaint, paintBorder, paintTime, paintTxt;

	private PuzzlePiece piece;

	private ArrayList<PuzzlePiece> pieces;
	private Background bgPuzzle, bgPuzzleCured;//, btnPlay, bgMsg, btnPlayAgain, txtAllBetter, logo;

	private Rect ptTouch, borderPuzzle, borderTime;
	private boolean selected = false;
	private boolean showCured = false;
	private boolean startGame = false;

	private int[] correct;
	
	
	private int idBitSick, idBitCured;

	private int imgPicked;

	String strQuestion;
	
	public PuzzleGame(Context context) {
		super(context, null);
		
		this.ctx = context;

		ops = new BitmapFactory.Options();
		ops.inPurgeable = true;
		txtSecondsPaint = new Paint();
		txtSecondsPaint.setColor(Color.BLUE);
		txtSecondsPaint.setTextSize(30.0f);


		paintTxt = new Paint();
		paintTxt.setTextSize(40);
		paintTxt.setColor(Color.BLACK);

		paintBorder = new Paint();
		paintBorder.setColor(Color.argb(255, 0,168,233));
		
		paintTime = new Paint();
		paintTime.setShader(new LinearGradient(0, 0, 0, getHeight(), Color.BLACK, Color.argb(255, 0,168,233), Shader.TileMode.MIRROR));

		strQuestion = "PUZZLE";
		
	}

	public Bitmap getBitmap(int id) {
		return BitmapFactory.decodeResource(ctx.getResources(), id, ops);
	}
	


	public static int setX(int x1)
	{	
		return (int)Math.round(x1-(x1*( PuzzleGame.percentWidth*1.0f)));
	}

	public static int setY(int y1)
	{	 
		return (int)Math.round(y1-(y1*( PuzzleGame.percentHeight*1.0f)));
	}
	
	

	public void init(int img, int winWidth, int winHeight) {

		this.percentWidth = (1280-winWidth)/1280.0f;
		this.percentHeight = (780-winHeight)/780.0f;
		
		borderPuzzle = new Rect(0,0,setX(800),setY(800));
		borderTime = new Rect(0,setY(700),setX(800),setY(1400));
		
		imgPicked = img;




		Log.d("butch", "imgPicked:"+img);


		ArrayList<Integer> puzzleData = GameMap.getInstance().setUpPuzzleGameData();

		ArrayList<Integer> letters = new ArrayList<Integer>();
		for(int i = 0; i < puzzleData.size(); i++){
			letters.add(i, i);
		}

		if(questions == null) {
			//get list of questioned letters
			questions = new int[10];
			for (int j = 0; j < 10; j++) {
				int random = (int) (Math.random() * letters.size());
				questions[j] = letters.get(random);
				letters.remove(random);
			}
			currQuestion = 0;
		}else{
			currQuestion++;
		}


		idBitCured = puzzleData.get(questions[currQuestion]);
		idBitSick = puzzleData.get(questions[currQuestion]);



		/*switch(imgPicked){
		case 2:

			idBitCured = R.drawable.puzzle02_cured;
			idBitSick = R.drawable.puzzle02;
			idBitMsg = R.drawable.text_ed_headache;
			break;
		case 3:

			idBitCured = R.drawable.puzzle03_cured;
			idBitSick = R.drawable.puzzle03;
			idBitMsg = R.drawable.text_tootsie_toothache;
			break;
		case 4:

			idBitCured = R.drawable.puzzle04_cured;
			idBitSick = R.drawable.puzzle04;
			idBitMsg = R.drawable.text_dennis_dengue;
			break;

		case 5:

			idBitCured = R.drawable.puzzle05_cured;
			idBitSick = R.drawable.puzzle05;
			idBitMsg = R.drawable.text_missy_measles;
			break;
		case 6:

			idBitCured = R.drawable.puzzle06_cured;
			idBitSick = R.drawable.puzzle06;
			idBitMsg = R.drawable.text_inno_immu;
			break;
		default:

			idBitCured = R.drawable.puzzle01_cured;
			idBitSick = R.drawable.puzzle01;
			idBitMsg = R.drawable.text_fifi_fever;
			break;


		}*/



		correct = new int[4];

		bgPuzzle = new Background(170, 170, getBitmap(idBitSick));
		bgPuzzleCured = new Background(170, 170,
				getBitmap(idBitSick));

		bgPuzzle.changeAlpha(50);
		
/*		bgMsg = new Background(50, 50, getBitmap(idBitMsg));
		bgMsg.x = (setX(400)-(bgMsg.width/2));
		bgMsg.y = setY(1000);*/

		// piece = new PuzzlePiece(50, 50, getBitmap(R.drawable.puzzle01));
		// piece.nextFrame();setX
		// piece.setDest(new Rect(0,0,233,233));
		// piece.setDest(new Rect(0,0,233,233));

/*		btnPlay = new Background(50,850,getBitmap(R.drawable.button_start));
		btnPlayAgain = new Background(50,850,getBitmap(R.drawable.button_playagain));
		btnPlayAgain.x = setX(400-(btnPlayAgain.destWidth/2));
		btnPlayAgain.y = setY(900);	
		
		logo = new Background(50,850,getBitmap(R.drawable.paracetamol_logo));
		logo.x = (setX(400)-(logo.destWidth/2));   
		logo.y = setY(1150);
		
		txtAllBetter = new Background(50, 50, getBitmap(R.drawable.text_all_better));
		txtAllBetter.x = (setX(400)-((txtAllBetter.destWidth/2)));
		txtAllBetter.y = setY(1050);*/

	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Touch(MotionEvent event) {

		if (ptTouch == null)
			ptTouch = new Rect();

		ptTouch.bottom = (int) (event.getY() + 1);
		ptTouch.top = (int) (event.getY() - 1);
		ptTouch.left = (int) (event.getX() - 1);
		ptTouch.right = (int) (event.getX() + 1);

		
		switch(state){
		case BEFORE_GAME: 
			//if (btnPlay.dest.intersect(ptTouch))
			{


				state = START_GAME;
				pieces = new ArrayList<PuzzlePiece>();
				

				ArrayList<Integer> arrRandIndex = new ArrayList<Integer>();

				for (int i = 0; i < 4; i++)
					arrRandIndex.add(i);
				
				
				
				
				int randInd = 0;
				for (int i = 0; i < 4; i++)
				{
					randInd = Random.Next(0, arrRandIndex.size());
					
					pieces.add(new PuzzlePiece(Random.Next(750, 900), Random.Next(200,
							400)
							, getBitmap(idBitSick)
							,arrRandIndex.get(randInd) ));
					
					arrRandIndex.remove(randInd);

				}
				

				bgPuzzleCured.bitmap = getBitmap(idBitCured);
				bgPuzzleCured.changeAlpha(0);
				startGame = true;
			}
			
			break;
		case START_GAME:
			
			if (!showCured && startGame) {

				if (event.getAction() == MotionEvent.ACTION_DOWN
						&& event.getAction() == MotionEvent.ACTION_UP) {
					selected = false;
				}

				for (int i = pieces.size() - 1; i >= 0; i--) {

					piece = pieces.get(i);

					if (piece.dest.intersect(ptTouch)) {
						if (event.getAction() == MotionEvent.ACTION_DOWN
								&& !selected) {
							piece.areYouSelected = true;
							selected = true;

							piece.x = ptTouch.centerX() - (setX(233) / 2);
							piece.y = ptTouch.centerY() - (setY(233) / 2);
						}

						if (event.getAction() == MotionEvent.ACTION_MOVE
								&& piece.areYouSelected) {

							piece.x = ptTouch.centerX() - (setX(233) / 2);
							piece.y = ptTouch.centerY() - (setY(233) / 2);

							for (PuzzlePiece p : pieces) {
								if (ptTouch.intersect(p.initialBox)) {
									piece.x = (p.initialBox.left - p.padding);
									piece.y = (p.initialBox.top - p.padding);

									if (p.index == piece.index) {
										correct[p.index] = 1;
										checkIfAllCorrect();
									}
								}
							}
						}

						if (event.getAction() == MotionEvent.ACTION_UP
								&& piece.areYouSelected) {
							selected = false;
							piece.areYouSelected = false;
						}
					}

				}
			}
			
			break;
		case END_GAME:

			//goToMainMenu();

			
			break;
		}
		
		
		

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
		i.putExtra("gameType", "color");
		i.putExtra("goodJob", isCorrect);

		if((PuzzleGame.currQuestion > MainActivity.numQuestions-2)) {
			i.putExtra("lastQuestion", true);

		}

		a.startActivity(i);
		a.finish();
	}


	private void checkIfAllCorrect() {
		// show cured!
		boolean cured = true;
		for(int i: correct){
			if(i == 0){
				cured = false;
			}
		}
		showCured = cured;
	}

	@Override
	public void Draw(Canvas c) {

		c.drawColor(Color.WHITE);
		//c.drawARGB(255, 94,97,221);
		//c.drawPaint(paintTime);
		//c.drawRect(borderTime, paintTime);
		//c.drawRect(borderPuzzle, paintBorder);
		//c.drawRect(bgPuzzle.dest, txtSecondsPaint);

		c.drawText((currQuestion+1)+"/10",750,35,txtSecondsPaint);
		c.drawText(strQuestion,380,70,paintTxt);
		
		
		bgPuzzle.draw(c);


		int time = MAX_TIMER-seconds;
		
		if(time < 0){
			time = 0;
		}
		
		
		
		switch(state){
		case BEFORE_GAME: 

			bgPuzzleCured.draw(c);


			c.drawText(":00", setX(300), setY(720), txtSecondsPaint);
			break;
		case START_GAME: 

			c.drawText(":"+(time<10?"0":"")+time, setX(300), setY(720), txtSecondsPaint);
			
			if(startGame)
			{
				for (PuzzlePiece piece : pieces) {
					piece.draw(c);
					//c.drawRect(piece.dest,txtSecondsPaint);
				}
	
				if (showCured) {
					bgPuzzleCured.draw(c);
				}
			}
			break;
		case END_GAME: 
			

			bgPuzzleCured.draw(c);
			break;
		}
		
		//c.drawRect(0, setY(1150), setX(800), setY(1400), txtSecondsPaint);
	}


	@Override
	public void Update(long t) {
	
		switch(state){
		case BEFORE_GAME: break;
		case START_GAME: 
			if(seconds >= MAX_TIMER){	
				//stop game
				playWrong();
			}
			
			if(startTime < 0 ){
				startTime = t;
			}
			currTime = t;

			millis = currTime - startTime;
			seconds = (int) (millis / 1000);
			seconds = seconds % 60;

			
			
			for (PuzzlePiece piece : pieces) {
				piece.update(t);
			}
			
			break;
		case END_GAME:


			playCorrect();
			
			break;
		
		}
		
		
		if (showCured) {
			
			if(bgPuzzleCured.alpha+10 > 255){
				bgPuzzleCured.changeAlpha(255);
				playCorrect();
			}else
				bgPuzzleCured.changeAlpha(bgPuzzleCured.alpha+10);
			
		}
		
	}

	@Override
	public void init(int winWidth, int winHeight) {
		init(0, winWidth, winHeight);
	}

}
