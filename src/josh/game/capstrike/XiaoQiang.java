package josh.game.capstrike;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;

@SuppressWarnings("deprecation")
public class XiaoQiang{
	public static final int NORMAL=0;
	public static final int MAGIC=1;
	public static final int RARE=2;
	public static final int UNIQUE=3;	
	private final int LEVEL;
	private ImageView image;
	private ImageView ice;
	private ImageView slow_down;
	private ImageView propIcon;
	private LinearLayout propHolder;
	private int life;
	private AnimationDrawable anim;
	private MyAnimation run;
	private AbsoluteLayout layout;
	private static SoundPool sound;
	private static int sound_shooted;
	private static int sound_dead;
	private Handler FreezeHandler=new Handler();
	private Handler SlowDownHandler=new Handler();
	private Handler propHandler=new Handler();
	private propRunnable propRun;
	private FreezeRunnable FreezeRun;
	private SlowDownRunnable SlowDownRun;
	private boolean isFrozen=false;
	public boolean isSlowDown=false;
	private boolean propOn=false;
	private Context context;
	
	public XiaoQiang(Context context,ImageView v,int level){
		this.context=context;
		image=v;
		LEVEL=level;
		layout=((GamePlayActivity)context).layout;		
		switch(level){
		case NORMAL: 
			life=1;
			image.setBackgroundResource(R.drawable.anim_xiaoqiang_normal);
			anim=(AnimationDrawable)image.getBackground();
			break;
		case MAGIC: 
			life=2;
			image.setBackgroundResource(R.drawable.anim_xiaoqiang_magic);
			anim=(AnimationDrawable)image.getBackground();
			break;
		case RARE: 
			life=3;
			image.setBackgroundResource(R.drawable.anim_xiaoqiang_real);
			anim=(AnimationDrawable)image.getBackground();
			break;
		case UNIQUE: 
			life=4;
			image.setBackgroundResource(R.drawable.anim_xiaoqiang_unique);
			anim=(AnimationDrawable)image.getBackground();
			break;
		default: break;
		}		
	}
	 
	public static void SoundPoolInit(Context context)
	//初始化音效
	{
		sound=new SoundPool(10, AudioManager.STREAM_MUSIC,0);
		sound_shooted=sound.load(context, R.raw.sond_shooted, 0);
		sound_dead=sound.load(context, R.raw.sound_dead, 0);
	}

	public void run(long duration,int distance,int command,boolean hasLimit)
	//让小强开始跑
	{
     if(!hasLimit){
		switch(command){
		case RUNCOMMAND.CONTINUE_IF_ALREADY_SET:
			if(run!=null){				
				anim.start();
				run.start();				
			}
			else{
				run=new MyAnimation(image,duration,distance);
				anim.start();
				run.start();
			}
			break;
		case RUNCOMMAND.RESET:
			stop();
			run=new MyAnimation(image,duration,distance);
			anim.start();
			run.start();
			break;
		default:break;
		}} else{
    	 if(!isFrozen){
    	 switch(command){
 		case RUNCOMMAND.CONTINUE_IF_ALREADY_SET:
 			if(run!=null){				
 				anim.start();
 				run.start();				
 			}
 			else{
 				run=new MyAnimation(image,duration,distance);
 				anim.start();
 				run.start();
 			}
 			break;
 		case RUNCOMMAND.RESET:
 			stop();
 			run=new MyAnimation(image,duration,distance);
 			anim.start();
 			run.start();
 			break;
 		default:break;
 		}}  	 
     }		
	}
	
	public void stop()
	//让小强停止
	{
		if(run!=null) run.stop();
		anim.stop();
	}
	
	public void reduceLife(int damage)
	//扣血，播放动画和声音
	{
		if(damage>0){
		life=life-damage;	
		if(!isAlive()) kill();
		else{
			image.setImageResource(R.drawable.shooted);	
			if(sound!=null)
			sound.play(sound_shooted, 1, 1, 1, 0, 1);
		 	new Handler().postDelayed(new Runnable(){
	    		@Override  
	    		public void run() {
	    			image.setImageBitmap(null);
	    		}}, 100);		
		}
		}
	}
	
	public int getProp(){
		final GamePlayActivity game=(GamePlayActivity)context;
		boolean[] permit=new boolean[8];
		int level=game.stageLevel;
		for(int i=0;i<8;i++){
			if(level>i||game.challenge) 
				permit[i]=true;
			else
				permit[i]=false;
		}
		int prop=Prop.NONE;
		int random=(int) (Math.random()*1000+1);
		switch(LEVEL){
		case NORMAL:
			if(permit[0]&&random>=1&&random<=25) prop=Prop.MISSLE;
			if(permit[1]&&random>=26&&random<=40) prop=Prop.CONTINUOUS;
			if(permit[2]&&random>=41&&random<=55) prop=Prop.SLOWDOWN;
			if(permit[3]&&random>=56&&random<=70) prop=Prop.LASER;
			if(permit[4]&&random>=71&&random<=80) prop=Prop.PUSHBACK;
			if(permit[5]&&random>=81&&random<=90) prop=Prop.FIREBALL;
			if(permit[6]&&random>=91&&random<=96) prop=Prop.FROST_NOVA;
			if(permit[7]&&random>=97&&random<=100) prop=Prop.LIGHTENING;
			break;
		case MAGIC:
			if(permit[0]&&random>=1&&random<=50) prop=Prop.MISSLE;
			if(permit[1]&&random>=51&&random<=80) prop=Prop.CONTINUOUS;
			if(permit[2]&&random>=81&&random<=110) prop=Prop.SLOWDOWN;
			if(permit[3]&&random>=111&&random<=140) prop=Prop.LASER;
			if(permit[4]&&random>=141&&random<=160) prop=Prop.PUSHBACK;
			if(permit[5]&&random>=161&&random<=180) prop=Prop.FIREBALL;
			if(permit[6]&&random>=181&&random<=192) prop=Prop.FROST_NOVA;
			if(permit[7]&&random>=193&&random<=200) prop=Prop.LIGHTENING;
			break;
		case RARE:
			if(permit[0]&&random>=1&&random<=75) prop=Prop.MISSLE;
			if(permit[1]&&random>=76&&random<=120) prop=Prop.CONTINUOUS;
			if(permit[2]&&random>=121&&random<=165) prop=Prop.SLOWDOWN;
			if(permit[3]&&random>=166&&random<=210) prop=Prop.LASER;
			if(permit[4]&&random>=211&&random<=240) prop=Prop.PUSHBACK;
			if(permit[5]&&random>=241&&random<=270) prop=Prop.FIREBALL;
			if(permit[6]&&random>=271&&random<=288) prop=Prop.FROST_NOVA;
			if(permit[7]&&random>=289&&random<=300) prop=Prop.LIGHTENING;
			break;
		case UNIQUE:
			if(permit[0]&&random>=1&&random<=100) prop=Prop.MISSLE;
			if(permit[1]&&random>=101&&random<=160) prop=Prop.CONTINUOUS;
			if(permit[2]&&random>=161&&random<=220) prop=Prop.SLOWDOWN;
			if(permit[3]&&random>=221&&random<=280) prop=Prop.LASER;
			if(permit[4]&&random>=281&&random<=320) prop=Prop.PUSHBACK;
			if(permit[5]&&random>=321&&random<=360) prop=Prop.FIREBALL;
			if(permit[6]&&random>=361&&random<=384) prop=Prop.FROST_NOVA;
			if(permit[7]&&random>=385&&random<=400) prop=Prop.LIGHTENING;
			break;
		default:
			break;
		}
		if(prop!=Prop.NONE){		
		propOn=true;
		game.gameControler.addScore(20);
		propIcon=new ImageView(game);
	//	game.layout.addView(propIcon);
	/*	LayoutParams param=(LayoutParams) propIcon.getLayoutParams();
		param.width=Size.Prop_Width*2;
		param.x=image.getLeft()-Size.Prop_Width/2;
		param.y=image.getTop()-Size.Prop_Height/2;
		propIcon.setLayoutParams(param);*/
		propHolder=new LinearLayout(game);
		propHolder.setOrientation(LinearLayout.VERTICAL);
		propHolder.setGravity(Gravity.CENTER);
		game.layout.addView(propHolder);
		LayoutParams param=(LayoutParams) propHolder.getLayoutParams();
		param.width=Size.Prop_Width*2;
		param.height=Size.Prop_Height*2;
		param.x=image.getLeft()-Size.Prop_Width/2;
		param.y=image.getTop()-Size.Prop_Height/2;
		propHolder.setLayoutParams(param);
		propHolder.addView(propIcon);
		switch(prop){
		case Prop.MISSLE:
			propIcon.setImageResource(R.drawable.missle);
			break;
		case Prop.CONTINUOUS:
			propIcon.setImageResource(R.drawable.continuous);
			break;
		case Prop.SLOWDOWN:
			propIcon.setImageResource(R.drawable.slowdown);
			break;
		case Prop.LASER:
			propIcon.setImageResource(R.drawable.laserprop);
			break;
		case Prop.PUSHBACK:
			propIcon.setImageResource(R.drawable.pushback);			
			break;
		case Prop.FIREBALL:
			propIcon.setImageResource(R.drawable.fireball);
			break;
		case Prop.FROST_NOVA:
			propIcon.setImageResource(R.drawable.frostnova);
			break;
		case Prop.LIGHTENING:
			propIcon.setImageResource(R.drawable.lightning_strike);
			break;
		default:break;
		}		
		propHolder.setClickable(true);
		propRun=new propRunnable(game);
		propHandler.post(propRun);
		final int type=prop;
		propHolder.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {				
				Prop p=game.gameControler.getEmptyProp();
				if(p!=null)
				{
					p.setPropType(type);
					clearProp();  
				}				 
			}
			
		});
		}
		return prop;
	}
	
	public boolean isAlive()
	//是否是活的
	{
		if(life>0) return true;
		else
			return false;
	}
	
	public ImageView getImage(){
		return image;
	}
	
	public int getLife(){
		return life;
	}
	
	public void kill()
	//杀死小强，播放动画和声音
	{	
		final GamePlayActivity game=(GamePlayActivity)context;
		switch(LEVEL){
		case NORMAL: game.normalXQKill++;break;
		case MAGIC: game.magicXQKill++;break;
		case RARE: game.rareXQKill++;break;
		case UNIQUE: game.unniqueXQKill++;break;
		default:break;
		}
		layout.removeView(ice);
		layout.removeView(slow_down);
		image.setBackgroundResource(R.drawable.explode);
		if(sound!=null)
		sound.play(sound_dead, 1, 1, 1, 0, 1);
		stop();			
		new Handler().postDelayed(new Runnable(){
    		@Override
    		public void run() {
    			clear();
    			getProp();	
    		}}, 400);			
	}
	
	public void clear()
	//从屏幕上清除该小强
	{
		stop();
		layout.removeView(image);
		layout.removeView(ice);
		layout.removeView(slow_down);
		life=0;
	}
	
	public void clearProp(){
		if(propOn){
		((GamePlayActivity)context).layout.removeView(propHolder);
		propHandler.removeCallbacks(propRun);
		propOn=false;
		}
	}
	
	public void pauseProp(){
		if(propOn){
			propHandler.removeCallbacks(propRun);
		}
	}
	
	public void continueProp(){
		if(propOn){
			propHandler.post(propRun);
		}
		
	}
		
	public void freeze(long FrozenTime){	
		stop();
		if(!isFrozen){
		ice=new ImageView(context);		
		layout.addView(ice);
		LayoutParams param=(LayoutParams) ice.getLayoutParams();
		param.x=image.getLeft();
		param.y=image.getTop();
		ice.setLayoutParams(param);
		ice.setImageResource(R.drawable.ice_small);
		ice.setAlpha(150);
		}
		if(isFrozen)
			FreezeHandler.removeCallbacks(FreezeRun);
		FreezeRun=new FreezeRunnable(context,FrozenTime);
		FreezeHandler.post(FreezeRun);
		isFrozen=true;
	}
	
	public void slowDown(float speedPercentage,long time){
		if(!isSlowDown){ 
			slow_down=new ImageView(context);			
			layout.addView(slow_down);
			LayoutParams param=(LayoutParams) slow_down.getLayoutParams();
			param.x=image.getLeft();
			param.y=image.getTop();
			slow_down.setLayoutParams(param);
			new Handler().postDelayed(new Runnable(){
				@Override
				public void run() {					
					slow_down.setImageResource(R.drawable.slow_down);
					slow_down.setAlpha(50);			
				}	        	
	        }, 100);			
			}
		if(isSlowDown){
			SlowDownHandler.removeCallbacks(SlowDownRun);
			run.recoverSpeed();
		}
		run.changeSpeed((long) (run.getDuration()/speedPercentage));
		SlowDownRun=new SlowDownRunnable(context,time);
		SlowDownHandler.post(SlowDownRun);
		isSlowDown=true;
	}
	
	public void changeSpeed(long duration){
		if(run!=null)
		run.changeSpeed(duration);
	}
	
	public void pushBack(int distance){
		if(ice!=null){
			LayoutParams param=(LayoutParams) ice.getLayoutParams();
			if(param!=null){
				param.y-=distance;
				ice.setLayoutParams(param);
			}
		}
		
		if(slow_down!=null){
			LayoutParams param=(LayoutParams) slow_down.getLayoutParams();
			if(param!=null){
				param.y-=distance;
				slow_down.setLayoutParams(param);
			}
		}
		
		LayoutParams param=(LayoutParams) image.getLayoutParams();
		param.y-=distance;
		image.setLayoutParams(param);
		
	}
	
	public float getWidth(){
		return image.getWidth();
	}
	
	public float getHeight(){
		return image.getHeight();
	}
	
	public PointF get_RIGHT_BOTTOM_Point(){
		LayoutParams param=(LayoutParams) image.getLayoutParams();
		return new PointF(param.x+getWidth(),param.y+getHeight());
	}
	
	public PointF get_LEFT_TOP_Point(){
		LayoutParams param=(LayoutParams) image.getLayoutParams();
		return new PointF(param.x,param.y);
	}
	
	public int getStartPosition(){
		if(run!=null)
			return run.getStartPosition();
		else
			return image.getTop();
	}
	
	private class FreezeRunnable implements Runnable{
		private long time;
		private Context context;
		private long StartTime;	
		
		FreezeRunnable(Context context,long time){
			this.time=time;
			this.context=context;
			StartTime=((GamePlayActivity)context).timeSpent;
		}		
	  		@Override
	  		public void run() {	  				  				  			
	  			FreezeHandler.postDelayed(FreezeRun, 200);
	  			if(((GamePlayActivity)context).timeSpent>=StartTime+time){
	  				FreezeHandler.removeCallbacks(FreezeRun);
	  				layout.removeView(ice);
	  				if(run!=null) run.start();
	  				anim.start();
	  				isFrozen=false;
	  			}
	  		}
	  		  
	  	  };
	
	private class SlowDownRunnable implements Runnable{
		private long time;
		private Context context;
		private long StartTime;
		
		SlowDownRunnable(Context context,long time){
			this.time=time;
			this.context=context;
			StartTime=((GamePlayActivity)context).timeSpent;
		}

		@Override
		public void run() {
			LayoutParams param=(LayoutParams) slow_down.getLayoutParams();
			param.x=image.getLeft();
			param.y=image.getTop();
			slow_down.setLayoutParams(param);
			SlowDownHandler.postDelayed(SlowDownRun, 100);
			if(((GamePlayActivity)context).timeSpent>=StartTime+time){
				SlowDownHandler.removeCallbacks(SlowDownRun);
				layout.removeView(slow_down);
				run.recoverSpeed();
				isSlowDown=false;
			}
		}
		
	}
	
	private class propRunnable implements Runnable{
		private GamePlayActivity game;
		private long startTime;
		private final long duration=5;
		private int alpha=255;
		private int i=0;
		
		propRunnable(Context context){
			game=(GamePlayActivity)context;
			startTime=game.timeSpent;
		}

		@Override
		public void run() {
			propHandler.postDelayed(this, 100);			
			if(game.timeSpent-startTime>i){
				i++;
				propIcon.setAlpha(alpha-i*50);
			}
			if(game.timeSpent>=startTime+duration){
				clearProp();
			}
			
		}

	}
	  	  
	private class MyAnimation{	  
		  private int startPosition;
		  private int Distance;
		  private int spaceTime;
	      private int space=1;
	      private long Duration;
	      private long DurationCache;
	      private LayoutParams param;
	      private ImageView image;
	      private Handler handler=new Handler();
	      
	      MyAnimation(ImageView v,long duration,int distance){
	      Distance=distance;
	      DurationCache=Duration;
	      Duration=duration;
	      image=v;	
		  spaceTime=(int) (duration/(distance/space));
		  for(;;)
		  		 if(spaceTime<20) {
		  		  space++;
		  		  spaceTime=(int) (duration/(Distance/space));
		  		  }
		  		 else
		  			 break;
		  param=(LayoutParams) v.getLayoutParams();
		  startPosition=param.y;	  
	      }
	      
	      private Runnable r = new Runnable(){
	  		@Override
	  		public void run() {	 
	  			param=(LayoutParams) image.getLayoutParams();
	  			param.y+=space;
	  			image.setLayoutParams(param);	  			
	  			handler.postDelayed(r, spaceTime);
	  			if(param.y-startPosition>=Distance)
	  				stop();
	  		}
	  		  
	  	  };
	  	  
	  	  public void changeSpeed(long duration){
	  		 DurationCache=Duration;
	  		 Duration=duration;
	  		 space=1;
	  		 spaceTime=(int) (duration/(Distance/space));
	  		 for(;;)
	  		 if(spaceTime<20) {
	  		  space++;
	  		  spaceTime=(int) (duration/(Distance/space));
	  		  }
	  		 else
	  			 break;
	  	  }
	  	  
	  	  public void recoverSpeed(){
	  		 long cache=Duration;
	  		 Duration=DurationCache;	  		 
	  		 DurationCache=cache;	
	  		 space=1;
	  		 spaceTime=(int) (Duration/(Distance/space));
	  		for(;;)
		  		 if(spaceTime<20) {
		  		  space++;
		  		  spaceTime=(int) (Duration/(Distance/space));
		  		  }
		  		 else
		  			 break;
	  	  }
	  	  
	      public void start(){	  
		  handler.post(r);
	  }
	      
	      public void stop(){
	    	  handler.removeCallbacks(r);
	      }
	      
	      
	      public long getDuration(){
	    	  return Duration;
	      }
	      
	      public int getStartPosition(){
	    	  return startPosition;
	      }
	   
	  }
	
	class RUNCOMMAND{
		public static final int CONTINUE_IF_ALREADY_SET=4;//继续动画，如果动画未开始则开始动画
		public static final int RESET=5;//重新设置动画参数并开启动画
	}
	
}