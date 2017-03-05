package josh.game.capstrike;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.TextView;

 @SuppressWarnings("deprecation")
class ShootRun implements Runnable {
	  private GamePlayActivity game;
	  int footprint=18;	//线程中子弹的步进
	  int time=25;
	  
	  public ShootRun(Context context){
		game=(GamePlayActivity) context;
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				footprint = 18*Size.screenHeight/480;				
			}			
		}, 500);		
	 }
	  
	  public void setFootprint(int footprint){
		  this.footprint = footprint;
	  }
	  
	  public void setTime(int time){
		  this.time = time;
	  }

	
		@Override
		public void run() {		
			if(game.bulletParam.y - Size.BULLET_HEIGHT <= game.endPos){				
				game.shootHandler.removeCallbacks(game.shootRun);
				game.isShooting = false;
				game.gameControler.setBullet();				
				return;
			}
			game.bulletParam.y  = Size.bulletTop;			
			game.bulletView.setLayoutParams(game.bulletParam);				
			Size.bulletTop = Size.bulletTop - footprint/game.footSize;		
			int[] temp = game.gameControler.getAim(Size.BULLET_WIDTH, game.bulletView.getLeft());		
			boolean isShooted = false;	
			if(!game.challenge){
			if(temp[0] != -1){
				if(game.gameControler.isCrash(game.bulletView,game.xiaoqiang[temp[0]].getImage())){
					game.xiaoqiang[temp[0]].reduceLife(1);					
					game.shootHandler.removeCallbacks(game.shootRun);						
					isShooted = true;	
					game.shootTimes++;
					game.gameControler.addScore(10);
					game.shootTimes--;
				}
			}
			if(temp[1] != -1){
				if(game.gameControler.isCrash(game.bulletView,game.xiaoqiang[temp[1]].getImage())){
					game.xiaoqiang[temp[1]].reduceLife(1);					
					game.shootHandler.removeCallbacks(game.shootRun);						
					isShooted = true;		
					game.shootTimes++;
					game.gameControler.addScore(10);
					game.shootTimes--;
				}
			}	}
			else{
				if(temp[0] != -1 && temp[1]!=-1){
					XiaoQiang xq=game.xq_challenge.getXiaoQiang(temp[0], temp[1]);
					if(game.gameControler.isCrash(game.bulletView,xq.getImage())){
						xq.reduceLife(1);					
						game.shootHandler.removeCallbacks(game.shootRun);						
						isShooted = true;	
						game.shootTimes++;
						game.gameControler.addScore(10);
						game.shootTimes--;
					}
				}
				if(temp[2] != -1 && temp[3]!=-1){
					XiaoQiang xq=game.xq_challenge.getXiaoQiang(temp[2], temp[3]);
					if(game.gameControler.isCrash(game.bulletView,xq.getImage())){
						xq.reduceLife(1);					
						game.shootHandler.removeCallbacks(game.shootRun);						
						isShooted = true;		
						game.shootTimes++;
						game.gameControler.addScore(10);
						game.shootTimes--;
					}
				}	
			}
			
			if(isShooted){
				game.isShooting = false;	
				game.gameControler.setBullet();
				game.shootTimes++;
			if(!game.challenge)
			if(game.xiaoQiangControler.getXiaoQiangAmount()==0){
				game.gameControler.pause();
				game.gameControler.showWinWindow();
			}
			return;
			}
			game.shootHandler.postDelayed(game.shootRun, time);			
		}
	 }
	 	
	  class LoseCheckRun implements Runnable{
		  private GamePlayActivity game;
		 
		  public LoseCheckRun(Context context){
			 game=(GamePlayActivity) context;
		 }

		@Override
		public void run() {			
			game.loseCheckHandler.postDelayed(game.loseCheckRun, 300);
			if(game.gameControler.isLost()){
				game.loseCheckHandler.removeCallbacks(game.loseCheckRun);
				if(!game.challenge)
				game.gameControler.showLoseWindow();
				else
					game.gameControler.showChallengeEndWindow();
				game.gameControler.pause();
			}
		}
		 
	 }
	 


	 class BottleOnTouchListener implements OnTouchListener
    {
		 private GamePlayActivity game;
		 
		 public BottleOnTouchListener(Context context){
			 game=(GamePlayActivity) context;
		 }

		@SuppressWarnings("deprecation")
		public boolean onTouch(View v, MotionEvent event) {
			
			game.bottleAnim.start();
			int action = event.getAction();			
			switch(action){
			case MotionEvent.ACTION_DOWN:{ //获取控件一开始的位置
				game.lastPosX = event.getRawX();	
				break;
			}
			case MotionEvent.ACTION_MOVE:{
				float dx = event.getRawX() - game.lastPosX;
				float dy = 0;					
				  
				//获取相对坐标
				float left = v.getLeft() + dx;
				float top  = v.getTop() + dy;
				float right = v.getRight() + dx;
				float bottom = v.getBottom()+ dy;
				//以下判断的目的：达到边界后不能再移动
				if(left + Size.BOTTLE_WIDTH < 0){
					left = -Size.BOTTLE_WIDTH;
					right = left + v.getWidth();
				}
				if(right - Size.BOTTLE_WIDTH > Size.screenWidth){
					right = Size.screenWidth+Size.BOTTLE_WIDTH;
					left = right - v.getWidth();
				}
				if(top < 0){
					top = 0;
					bottom = top + v.getHeight();
				}
				if(bottom > Size.screenHeight){
					bottom = Size.screenHeight;
					top = bottom - v.getHeight();
				}
														
					game.bottleParam.x = (int)left;
					game.bottleParam.y = (int)top;
					game.bottleView.setLayoutParams(game.bottleParam);
					game.shootPos = (game.bottleView.getLeft() + game.bottleView.getRight())/2 - Size.OFFSET;
					if(!game.isShooting){
					game.bulletParam.x= (int)(left + right)/2 - Size.OFFSET;
					game.bulletParam.y= Size.BOTTLE_TOP - Size.BULLET_HEIGHT;					
					game.bulletView.setLayoutParams(game.bulletParam);			
					}
				game.lastPosX = event.getRawX();					
				break;
			}
			case MotionEvent.ACTION_UP:{								
				break;
			}
			default:
				break;
			}
			return false;
		}
    	
    }
	 
	 class TimeRun implements Runnable{
		 
		 private GamePlayActivity game;
		 public TimeRun(Context context){
			 game=(GamePlayActivity) context;
		 }

	 		@Override
	 		public void run() {
	 			game.timeHandler.postDelayed(game.timeRun, 1000);	
	 			int minute = (int)(game.timeSpent/60);
	 			int second = (int)(game.timeSpent%60);
	 			if(minute < 10 && second < 10)
	 				game.timeView.setText("用时:" + "0" + minute + ":" + "0" + second);
	 			if(minute < 10 && second >= 10)
	 				game.timeView.setText("用时:" + "0" + minute + ":" + second);
	 			if(minute >= 10 && second <10)
	 				game.timeView.setText("用时:" +  minute + ":" + "0" + second);	
	 			if(minute >= 10 && second >= 10)
	 				game.timeView.setText("用时:" + minute + ":" + second);
	 			game.timeSpent++;				 																	
	 		}
	 		  
	 	  }
	 
	 class MultiKillDetector implements Runnable{
		 private GamePlayActivity game;
		 private Handler handler=new Handler();
		 int xqAmount;
		 
		 public MultiKillDetector(Context context){
			 game=(GamePlayActivity) context;
		 }
		 
		 public void start(){
			 if(!game.challenge)
			 xqAmount=game.xiaoQiangControler.getXiaoQiangAmount();
			 else
			 xqAmount=game.xq_challenge.getXQNum();
			 handler.post(this);
		 }
		 
		 public void stop(){
			 new Handler().postDelayed(new Runnable(){

				@Override
				public void run() {
					handler.removeCallbacks(MultiKillDetector.this);					
				}
				 
			 }, 600);			 
		 }

		@Override
		public void run() {
			handler.postDelayed(this, 500);
			int cache;
			if(!game.challenge)
			cache=game.xiaoQiangControler.getXiaoQiangAmount();
			else
				cache=game.xq_challenge.getXQNum();
			int killed=xqAmount-cache;
			if(killed>1){
				game.gameControler.addScore(killed*10);
				game.killWindow.show(killed);
			}
			xqAmount=cache;
			
			if(game.challenge){
				boolean add=true;
				if(game.xq_challenge.start!=null && game.xq_challenge.start.xq!=null)
					for(int i=0;i<9;i++){
						XiaoQiang xq=game.xq_challenge.start.xq[i];
						if(xq!=null && xq.isAlive()&&xq.getImage().getTop()>0)
						{
							add=false;
							break;
						}
					}
				if(add)
					game.gameControler.addScore(300);
			}
		}
	 }
	 
	 class KillWindow implements Runnable{
		 
		 private GamePlayActivity game;
		 private long startTime;
		 private long duration;
		 private Handler handler=new Handler();
		 private boolean isShowing=false;
		 private TextView text;
		 private static SoundPool sound;
		 private static int sound_doubleKill;
		 private static int sound_tripleKill;
		 private static int sound_ultraKill;
		 private static int sound_megaKill;
		 private static int sound_killingSpree;
		 private static int sound_whickedSick;
		 private static int sound_monsterKill;
		 private static int sound_godLike;
		 private static int sound_holyShit;
		 
		 public KillWindow(long duration,GamePlayActivity game){
			 this.duration=duration;
			 this.game=game;
		 }
		 
		 static void SoundPoolInit(Context context){
			 sound=new SoundPool(10, AudioManager.STREAM_MUSIC,0);
			 sound_doubleKill=sound.load(context, R.raw.sound_double_kill, 0);
			 sound_tripleKill=sound.load(context, R.raw.sound_triple_kill, 0);
			 sound_ultraKill=sound.load(context, R.raw.sound_ultra_kill, 0);
			 sound_killingSpree=sound.load(context, R.raw.sound_killing_spree, 0);
			 sound_megaKill=sound.load(context, R.raw.sound_mega_kill, 0);	 
			 sound_whickedSick=sound.load(context, R.raw.sound_whicked_sick, 0);
			 sound_monsterKill=sound.load(context, R.raw.sound_monster_kill, 0);
			 sound_godLike=sound.load(context, R.raw.sound_god_like, 0);
			 sound_holyShit=sound.load(context, R.raw.sound_holy_shit, 0);
		 }
		 
		 @SuppressWarnings("deprecation")
		public void show(int killNum){
			 stop();
			 isShowing=true;
			 text=new TextView(game);
			 text.setTextSize(20);
			 text.setTextColor(Color.RED);
			 switch (killNum){
			 case 1:
				 break;
			 case 2:
				 text.setText("双杀！！！！！");
				 if(sound!=null)
					    sound.play(sound_doubleKill, 1, 1, 1, 0, 1);
				 break;
			 case 3:
				 text.setText("三杀！！！！！");
				 if(sound!=null)
						sound.play(sound_tripleKill, 1, 1, 1, 0, 1);
				 break;
			 case 4:
				 text.setText("疯狂杀戮！！！");
				 if(sound!=null)
						sound.play(sound_ultraKill, 1, 1, 1, 0, 1);
				 break;
			 case 5:
				 text.setText("大杀特杀！！！");
				 if(sound!=null)
						sound.play(sound_killingSpree, 1, 1, 1, 0, 1);
				 break;
			 case 6:
				 text.setText("杀人如麻！！！");
				 if(sound!=null)
						sound.play(sound_megaKill, 1, 1, 1, 0, 1);
				 break;
			 case 7:
				 text.setText("变态杀戮！！！");
				 if(sound!=null)
						sound.play(sound_whickedSick, 1, 1, 1, 0, 1);
				 break;
			 case 8:
				 text.setText("妖怪般的杀戮！");
				 if(sound!=null)
						sound.play(sound_monsterKill, 1, 1, 1, 0, 1);
				 break;
			 case 9:
				 text.setText("如同神一般！！");
				 if(sound!=null)
						sound.play(sound_godLike, 1, 1, 1, 0, 1);
				 break;
			 default:
				 text.setText("超越神的杀戮！");
				 if(sound!=null)
						sound.play(sound_holyShit, 1, 1, 1, 0, 1);
				 break;			 			 
			 }
			 game.layout.addView(text);
			 text.setBackgroundColor(Color.argb(50, 0, 0, 0));
			 LayoutParams param=(LayoutParams) text.getLayoutParams();
			 param.y=-Size.screenHeight;
			 text.setLayoutParams(param);
			  new Handler().postDelayed(new Runnable(){
				@Override
				public void run() {
					LayoutParams param=(LayoutParams) text.getLayoutParams();
					 param.x=Size.screenWidth/2-text.getWidth()/2;
					 param.y=Size.layoutHeight/5*3;					 
					 text.setLayoutParams(param);		
				}
				  
			  }, 20);
			 
			 startTime=game.timeSpent;
			 handler.post(this);		 
		 }
		 
		 public void stop(){
			 if(isShowing){
				 game.layout.removeView(text);
				 handler.removeCallbacks(this);
				 isShowing=false;
			 }
		 }
		 
		 public void pause(){
			 if(isShowing){
				 handler.removeCallbacks(this);
			 }
		 }
		 
		 public void Continue(){
			 if(isShowing)
				  handler.post(this);
		 }

		@Override
		public void run() {
			handler.postDelayed(KillWindow.this, 200);
			if(game.timeSpent>=startTime+duration){
				stop();
			}
			
		}
		 
	 }
