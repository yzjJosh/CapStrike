package josh.game.capstrike;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsoluteLayout.LayoutParams;

@SuppressWarnings("deprecation")
public class GamePlayActivity extends Activity{
	  GameControler gameControler;
	  XiaoQiangControler xiaoQiangControler;
	  boolean challenge;
	  XQ_challenge xq_challenge=new XQ_challenge(this);
		  
	  float shootPos;   //子弹射出位置的横坐标	  	  			
	  int x1,x2,y1,y2; 	 //碰撞检测的两个图片的左上角坐标
	  float lastPosX;   //记录瓶子上一次的横坐标 	 		 
 	  int footSize;      //子弹步进的倍数调整 	 
 	  int endPos;    //子弹射到的最远处
 	  int score = 0;         //游戏得分
 	  int fireTimes=0;      //开火次数
 	  int shootTimes=0;     //击中次数
 	  int sound_shoot;
 	  int normalXQKill=0;
 	  int magicXQKill=0;
 	  int rareXQKill=0;
 	  int unniqueXQKill=0;
 	  int[] xiaoQiangMatrix;
 	  long timeSpent = 0;    //游戏经过的时间，以秒为单位	 
 	  long slowdownTime=-10;
 	  long frozenTime=-10;
	  boolean isShooting = false;  //判断子弹是否正在发射
	  boolean isFirstTime = true;   //判断是否第一次检测重力 
	  boolean canShoot = true;
	  boolean isPaused=false;
	 
	  ImageView bottleView;   //瓶子图片控件
	  ImageView bulletView;    //子弹图片控件
	
	  TextView markView;   //记录分数的控件
	  TextView timeView;    //记录时间的控件
	  TextView StageLevel;
	 
	  ProgressBar shakeLevelBar;   //记录摇晃程度的进度条
	 
	  SoundPool sound;
	 	 		 
	  Handler timeHandler = new Handler();   //更新时间的Handler
	  Handler shootHandler = new Handler();  //子弹射击的Handler
	  Handler loseCheckHandler=new Handler();
	 
	  Runnable timeRun = new TimeRun(this);   //更新时间的接口
	  ShootRun shootRun = new ShootRun(this);  //更新子弹位置的接口
	  Runnable loseCheckRun=new LoseCheckRun(this);
	  
	  MultiKillDetector multiKillDetector=new MultiKillDetector(this);
	 
	  LayoutParams bulletParam;
	  LayoutParams bottleParam;
	 	 
	  AbsoluteLayout layout;
	  XiaoQiang[] xiaoqiang=new XiaoQiang[54];
	  int stageLevel;
	 
	  PauseWindow pauseWindow;
	  winWindow WinWindow;
	  loseWindow LoseWindow;
	  ChallengeEndWindow ChallengeWindow;
	 
	  SensorManager sensorMgr;
	  SensorEventListener lsn;
	  Sensor sensor;
	  Prop[] prop=new Prop[5];
	  Tag TAG=new Tag(2,this);
	  KillWindow killWindow=new KillWindow(2,this);
	  BottleAnimation bottleAnim=new BottleAnimation(this);
	 		 	 
 		 	 
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {  
		 	super.onCreate(savedInstanceState); 		 
	        setContentView(R.layout.game_play);	 
	    	Size.sizeInit(this);	
	    	stageLevel=getIntent().getIntExtra("stageLevel", 1);
	    	challenge=getIntent().getBooleanExtra("challenge", false);
	        gameControler=new GameControler(this);
	        xiaoQiangControler=new XiaoQiangControler(this);
	        gameControler.initViews();	
	        gameControler.initProps();
	        if(!challenge)
	        xiaoQiangControler.XiaoQiangInit(GameData.getStage(stageLevel).getRandomMatrix(stageLevel));
	        else
	        	this.xq_challenge.init();
	        gameControler.SoundInit();
	        if(!challenge)
	        Database.setLastStage(stageLevel, this);
	        new Handler().postDelayed(new Runnable(){

				@Override
				public void run() {					
				    Size.posInit(GamePlayActivity.this);				    
					gameControler.initBottle();						
				}
	        	
	        }, 500);
	        new Handler().postDelayed(new Runnable(){

				@Override
				public void run() {					
					gameControler.initBullet();			
				}
	        	
	        }, 800);
	 }
	 	 
	  @Override
	 protected void onResume() {
		  	super.onResume();			  	
		  		          	        
			sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE); 
	        sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	        lsn = new SensorEventListener() {
	                    public void onSensorChanged(SensorEvent e) {
	                    	gameControler.sensorDetect(e);
	                    }
	                    public void onAccuracyChanged(Sensor s, int accuracy) {
	                    }
	                };
	                
	                new Handler().postDelayed(new Runnable(){
	    	    		@Override
	    	    		public void run() {		    	    			
	    	    			if((pauseWindow==null||!pauseWindow.isShowing())
	    	    				&&(WinWindow==null||!WinWindow.isShowing())
	    	    				&&(LoseWindow==null||!LoseWindow.isShowing())
	    	    				&&(ChallengeWindow==null||!ChallengeWindow.isShowing()))
	    	    				gameControler.Continue(); 	    				
	    	    			}}, 1000);	        
	                  	       
	    }
	    
	 @Override
	 protected void onPause(){
		 super.onPause();
		 gameControler.pause();
		
	 }
	 	 
	 @Override
	 protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();			
	}

	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) 
	    //按下返回键的响应
	    {
	            if(keyCode == KeyEvent.KEYCODE_BACK ){   
	            	gameControler.pauseGame();      
	            }
	            return super.onKeyDown(keyCode, event);
	        }
	 
	
	  	 
     ShootOut bombShoot=new ShootOut(this,R.drawable.bomb){

		@Override
		public void onShoot(int[] shootedIndex, ImageView bullet) {
			 final ImageView explode=new ImageView(GamePlayActivity.this);
	    	 int Xspace=getWindowManager().getDefaultDisplay().getWidth()/9;
	    	 Bitmap bit=BitmapFactory.decodeResource(getResources(),R.drawable.bomb_explode);    	 
	    	 Bitmap xq=BitmapFactory.decodeResource(getResources(),R.drawable.xiaoqiang_magic_1);
	    	 int radius=(xq.getWidth()+Xspace*2)/2;
	    	 Bitmap BIT = Bitmap.createScaledBitmap(bit, radius*2, radius*2, true);
	    	 explode.setImageResource(R.drawable.bomb_explode);
	    	 layout.addView(explode);  
	    	 LayoutParams param=(LayoutParams) explode.getLayoutParams();
			 param.x=bullet.getLeft()-(BIT.getWidth()-bullet.getWidth())/2;
			 param.y=bullet.getTop()-(BIT.getHeight()-bullet.getHeight())/2;
			 explode.setLayoutParams(param);
			 if(!challenge)
			 for(int i=0;i<54;i++){
				 if(xiaoqiang[i]!=null&&xiaoqiang[i].isAlive()){
					 int d1=(int) (Math.pow(xiaoqiang[i].get_LEFT_TOP_Point().x-param.x-BIT.getWidth()/2,2)
					         +Math.pow(xiaoqiang[i].get_LEFT_TOP_Point().y-param.y-BIT.getHeight()/2,2));
					 d1=(int) Math.pow(d1, 0.5);
					 int d2=(int) (Math.pow(xiaoqiang[i].get_RIGHT_BOTTOM_Point().x-param.x-BIT.getWidth()/2,2)
							 +Math.pow(xiaoqiang[i].get_LEFT_TOP_Point().y-param.y-BIT.getHeight()/2,2));
					 d2=(int) Math.pow(d2, 0.5);
					 int d3=(int) (Math.pow(xiaoqiang[i].get_RIGHT_BOTTOM_Point().y-param.y-BIT.getHeight()/2,2)
							 +Math.pow(xiaoqiang[i].get_LEFT_TOP_Point().x-param.x-BIT.getWidth()/2,2));
					 d3=(int) Math.pow(d3, 0.5);
					 int d4=(int) (Math.pow(xiaoqiang[i].get_RIGHT_BOTTOM_Point().x-param.x-BIT.getWidth()/2,2)
							 +Math.pow(xiaoqiang[i].get_RIGHT_BOTTOM_Point().y-param.y-BIT.getHeight()/2,2));
					 d4=(int) Math.pow(d4, 0.5);
					 int d5=(int) (Math.pow(xiaoqiang[i].get_LEFT_TOP_Point().x+xiaoqiang[i].getWidth()/2-param.x-BIT.getWidth()/2,2)
							 +Math.pow(xiaoqiang[i].get_LEFT_TOP_Point().y-param.y-BIT.getHeight()/2,2));
					 d5=(int) Math.pow(d5, 0.5);
					 int d6=(int) (Math.pow(xiaoqiang[i].get_RIGHT_BOTTOM_Point().x-param.x-BIT.getWidth()/2,2)
							 +Math.pow(xiaoqiang[i].get_LEFT_TOP_Point().y+xiaoqiang[i].getHeight()/2-param.y-BIT.getHeight()/2,2));
					 d6=(int) Math.pow(d6, 0.5);
					 int d7=(int) (Math.pow(xiaoqiang[i].get_LEFT_TOP_Point().x-param.x-BIT.getWidth()/2,2)
							 +Math.pow(xiaoqiang[i].get_LEFT_TOP_Point().y+xiaoqiang[i].getHeight()/2-param.y-BIT.getHeight()/2,2));
					 d7=(int) Math.pow(d7, 0.5);
					 int d8=(int) (Math.pow(xiaoqiang[i].get_LEFT_TOP_Point().x+xiaoqiang[i].getWidth()/2-param.x-BIT.getWidth()/2,2)
							 +Math.pow(xiaoqiang[i].get_RIGHT_BOTTOM_Point().y-param.y-BIT.getHeight()/2,2));
					 d8=(int) Math.pow(d8, 0.5);
					 if(d1<radius||d2<radius||d3<radius||d4<radius||d5<radius||d6<radius||d7<radius||d8<radius){
						 xiaoqiang[i].reduceLife(1);
						 gameControler.addScore(10);
					 }
				 }				
			 }
			 else{
				 XQ_queue queue=xq_challenge.start;
				 for(int i=0;i<xq_challenge.QueueNum;i++){
					 if(queue!=null && queue.xq!=null)
						 for(int j=0;j<9;j++)
							 if(queue.xq[j]!=null && queue.xq[j].isAlive()){
								 int d1=(int) (Math.pow(queue.xq[j].get_LEFT_TOP_Point().x-param.x-BIT.getWidth()/2,2)
								         +Math.pow(queue.xq[j].get_LEFT_TOP_Point().y-param.y-BIT.getHeight()/2,2));
								 d1=(int) Math.pow(d1, 0.5);
								 int d2=(int) (Math.pow(queue.xq[j].get_RIGHT_BOTTOM_Point().x-param.x-BIT.getWidth()/2,2)
										 +Math.pow(queue.xq[j].get_LEFT_TOP_Point().y-param.y-BIT.getHeight()/2,2));
								 d2=(int) Math.pow(d2, 0.5);
								 int d3=(int) (Math.pow(queue.xq[j].get_RIGHT_BOTTOM_Point().y-param.y-BIT.getHeight()/2,2)
										 +Math.pow(queue.xq[j].get_LEFT_TOP_Point().x-param.x-BIT.getWidth()/2,2));
								 d3=(int) Math.pow(d3, 0.5);
								 int d4=(int) (Math.pow(queue.xq[j].get_RIGHT_BOTTOM_Point().x-param.x-BIT.getWidth()/2,2)
										 +Math.pow(queue.xq[j].get_RIGHT_BOTTOM_Point().y-param.y-BIT.getHeight()/2,2));
								 d4=(int) Math.pow(d4, 0.5);
								 int d5=(int) (Math.pow(queue.xq[j].get_LEFT_TOP_Point().x+Size.XIAOQIANG_WIDTH/2-param.x-BIT.getWidth()/2,2)
										 +Math.pow(queue.xq[j].get_LEFT_TOP_Point().y-param.y-BIT.getHeight()/2,2));
								 d5=(int) Math.pow(d5, 0.5);
								 int d6=(int) (Math.pow(queue.xq[j].get_RIGHT_BOTTOM_Point().x-param.x-BIT.getWidth()/2,2)
										 +Math.pow(queue.xq[j].get_LEFT_TOP_Point().y+Size.XIAOQIANG_HEIGHT/2-param.y-BIT.getHeight()/2,2));
								 d6=(int) Math.pow(d6, 0.5);
								 int d7=(int) (Math.pow(queue.xq[j].get_LEFT_TOP_Point().x-param.x-BIT.getWidth()/2,2)
										 +Math.pow(queue.xq[j].get_LEFT_TOP_Point().y+Size.XIAOQIANG_HEIGHT/2-param.y-BIT.getHeight()/2,2));
								 d7=(int) Math.pow(d7, 0.5);
								 int d8=(int) (Math.pow(queue.xq[j].get_LEFT_TOP_Point().x+Size.XIAOQIANG_WIDTH/2-param.x-BIT.getWidth()/2,2)
										 +Math.pow(queue.xq[j].get_RIGHT_BOTTOM_Point().y-param.y-BIT.getHeight()/2,2));
								 d8=(int) Math.pow(d8, 0.5);
								 if(d1<radius||d2<radius||d3<radius||d4<radius||d5<radius||d6<radius||d7<radius||d8<radius){
									 queue.xq[j].reduceLife(1);
									 gameControler.addScore(10);
								 }
							 }
					 queue=queue.next;
				 }
			 }
			 new Handler().postDelayed(new Runnable(){
 	    		@Override
 	    		public void run() {	
 	    				layout.removeView(explode);		
 	    			}}, 200);	
			 if(!challenge)
			 if(xiaoQiangControler.getXiaoQiangAmount()==0){
					gameControler.pause();
					gameControler.showWinWindow();
				}
		}
    	 
     };
	      
     ShootOut fireBall=new ShootOut(this,R.drawable.fire_ball){

		@Override
		public void onShoot(int[] shootedIndex, ImageView bullet) {
			if(!challenge){
			if(shootedIndex[0]!=-1){
				if(xiaoqiang[shootedIndex[0]]!=null&&xiaoqiang[shootedIndex[0]].isAlive())
					xiaoqiang[shootedIndex[0]].reduceLife(1);
				    gameControler.addScore(10);
			}
			if(shootedIndex[1]!=-1){
				if(xiaoqiang[shootedIndex[1]]!=null&&xiaoqiang[shootedIndex[1]].isAlive())
					xiaoqiang[shootedIndex[1]].reduceLife(1);
				    gameControler.addScore(10);
			}
			
			 if(xiaoQiangControler.getXiaoQiangAmount()==0){
				    gameControler.pause();
					gameControler.showWinWindow();
				}	}
			else{
				if(shootedIndex[0]!=-1 && shootedIndex[1]!=-1){
					XiaoQiang xq=xq_challenge.getXiaoQiang(shootedIndex[0], shootedIndex[1]);
					if(xq!=null&&xq.isAlive())
						xq.reduceLife(1);
					    gameControler.addScore(10);
				}
				if(shootedIndex[2]!=-1 && shootedIndex[3]!=-1){
					XiaoQiang xq=xq_challenge.getXiaoQiang(shootedIndex[2], shootedIndex[3]);
					if(xq!=null&&xq.isAlive())
						xq.reduceLife(1);
					    gameControler.addScore(10);
				}
			}
		}
    	 
     };
     
     ShootOut[] fires=new ShootOut[]{fireBall,fireBall.copy(),fireBall.copy(),
    		 fireBall.copy(),fireBall.copy(),fireBall.copy(),fireBall.copy(),
    		 fireBall.copy(),fireBall.copy()};
            
     WaveShoot pushBackWave=new WaveShoot(this,R.drawable.push_back_wave,150,false){

		@Override
		public void onShoot(XQ_queue Queue,int[] shootedIndex, ImageView wave,boolean isFirstShoot) {
			int distance=Size.XIAOQIANG_HEIGHT;
			if(!challenge)
			 for(int i=0;i<54;i++){
	    		 if(xiaoqiang[i]!=null&&xiaoqiang[i].isAlive()){
	    			 if(xiaoqiang[i].get_LEFT_TOP_Point().y-distance<xiaoqiang[i].getStartPosition()){
	    				 xiaoqiang[i].pushBack((int) (xiaoqiang[i].get_LEFT_TOP_Point().y-xiaoqiang[i].getStartPosition()));
	    			 }
	    			 else{
	    				 xiaoqiang[i].pushBack(distance);
	    			 }
	    			 gameControler.addScore(5);
	    		 }
	    	 }
			else{
				XQ_queue queue=Queue;
				for(int i=0;i<xq_challenge.QueueNum;i++){
					if(queue!=null && queue.xq!=null)
						for(int j=0;j<9;j++)
							if(queue.xq[j]!=null&&queue.xq[j].isAlive()){								
				    			 queue.xq[j].pushBack(distance);
				    			 gameControler.addScore(5);
							}
					queue=queue.next;
				}
			}
						
		}
    	 
     };
             
     WaveShoot slowDownWave=new WaveShoot(this,R.drawable.slow_down_wave,150,true){

		@Override
		public void onShoot(XQ_queue Queue,int[] shootedIndex, ImageView wave,
				boolean isFirstShoot) {
			float speedPercentage=0.4f;
            long  time=10;
            if(!challenge)
			for(int i=0;i<9;i++){
				if(shootedIndex[i]!=-1)
				 if(xiaoqiang[shootedIndex[i]]!=null&&xiaoqiang[shootedIndex[i]].isAlive())
	    			 xiaoqiang[shootedIndex[i]].slowDown( speedPercentage, time);
				     gameControler.addScore(5);
			}
            else{
            	if(Queue!=null && Queue.xq!=null)
            	for(int i=0;i<9;i++){
            		if(shootedIndex[i]==1){
            			if(Queue.xq[i]!=null&&Queue.xq[i].isAlive())
            				Queue.xq[i].slowDown( speedPercentage, time);
       				       gameControler.addScore(5);
            		}
            	}
            }
            
            if(challenge){
            	if(wave.getTop()<=Size.XIAOQIANG_HEIGHT && Queue.next!=null){
            		XQ_queue queue=Queue;
            		for(;;){
            			queue=queue.next;
            			if(queue==null) break;
            			for(int i=0;i<9;i++)
            				if(queue.xq[i]!=null&&queue.xq[i].isAlive())
            					queue.xq[i].slowDown(speedPercentage, time);
            		}
            	}
            }
			
		}
    	 
     };
    
     WaveShoot iceWave=new WaveShoot(this,R.drawable.ice_wave,WaveShoot.NO_ALPHA,true){

 		@Override
 		public void onShoot(XQ_queue Queue,int[] shootedIndex, ImageView wave,
 				boolean isFirstShoot) {
 			long FrozenTime=10;
 			if(!challenge)
 			for(int i=0;i<9;i++){
				if(shootedIndex[i]!=-1)
				 if(xiaoqiang[shootedIndex[i]]!=null&&xiaoqiang[shootedIndex[i]].isAlive())
	    			 xiaoqiang[shootedIndex[i]].freeze(FrozenTime);
				     gameControler.addScore(5);
			}
 			else{
 				if(Queue!=null && Queue.xq!=null)
 				for(int i=0;i<9;i++){
 					if(shootedIndex[i]==1)
 					 if(Queue.xq[i]!=null&&Queue.xq[i].isAlive())
 						Queue.xq[i].freeze(FrozenTime);
 					     gameControler.addScore(5);
 				}
 			}
 			
 			 if(challenge){
             	if(wave.getTop()<=Size.XIAOQIANG_HEIGHT && Queue.next!=null){
             		XQ_queue queue=Queue;
             		for(;;){
             			queue=queue.next;
             			if(queue==null) break;
             			for(int i=0;i<9;i++)
             				if(queue.xq[i]!=null&&queue.xq[i].isAlive())
             					queue.xq[i].freeze(FrozenTime);
             		}
             	}
             }
 				
 		}
 		 
 	 };
 	 
 	 Lightning lightning=new Lightning(this);
 	 
 	 CRun continueShoot=new CRun(this);
 	 
 	 
}

