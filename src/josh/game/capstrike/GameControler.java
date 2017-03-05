package josh.game.capstrike;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsoluteLayout.LayoutParams;

@SuppressWarnings("deprecation")
public class GameControler {
	private GamePlayActivity game;
	final  int GATE_SPEED = 1200;	//触发操作的临界速度
	long startTime = 0;	//记录上一次检测重力加速度的时间				 
	double accX, accY, accZ; 	//记录x，y，z三个方向的加速度
	double lastAccX,lastAccY,lastAccZ;	  //记录上一个检测点x，y，z三个方向的加速度
	private int cache1,cache2,cache3,cache4,shakeLevel;  
	
	public GameControler(Context context){
		this.game=(GamePlayActivity) context;
	}
	
	 
	 void pause()
	    //暂停游戏
	    {
		     game.isPaused=true;
		     if(!game.challenge){
		     game.xiaoQiangControler.XiaoQiangStop();
		     game.xiaoQiangControler.pauseProps();
		     }
		     else{
		    	 game.xq_challenge.stop();
		    	 game.xq_challenge.pauseProps();
		     }
	    	 game.timeHandler.removeCallbacks(game.timeRun);
	    	 game.loseCheckHandler.removeCallbacks(game.loseCheckRun);
	    	 game.multiKillDetector.stop();
	    	 game.TAG.pause();
	    	 game.killWindow.pause();
	    	 game.bottleAnim.pause();
	    	 game.continueShoot.pauseShoot();
	    	 game.bombShoot.pause();
	    	 game.pushBackWave.pause();
	    	 game.slowDownWave.pause();
	    	 game.iceWave.pause();
	    	 game.lightning.pause();
	    	 for(int i=0;i<9;i++)
	    		 game.fires[i].pause();
			 game.shootHandler.removeCallbacks(game.shootRun);			 	
			 game.sensorMgr.unregisterListener(game.lsn , game.sensor);
	    }
	 	 
	 public void Continue()
	    //继续游戏，如果游戏未开始则开始游戏
	    {
		 game.isPaused=false;
		 if(!game.challenge){
		 game.xiaoQiangControler.XiaoQiangRun
		 (GameData.getStage(game.stageLevel).duration*(Size.layoutHeight-Size.BOTTLE_HEIGHT)/
				 (Size.layoutHeight-Size.BOTTLE_HEIGHT-Size.XIAOQIANG_HEIGHT*6),
	    			Size.layoutHeight-Size.BOTTLE_HEIGHT,
	    			XiaoQiang.RUNCOMMAND.CONTINUE_IF_ALREADY_SET,
	    			true);	
		 game.xiaoQiangControler.continueProps();
		 }
		 else{
			 game.xq_challenge.start();
			 game.xq_challenge.continueProps();
		 }
		 if(!game.continueShoot.isShooting)
	    	game.sensorMgr.registerListener(game.lsn,game. sensor,
	    			SensorManager.SENSOR_DELAY_GAME);
	    	game.timeHandler.post(game.timeRun);
	    	game.loseCheckHandler.post(game.loseCheckRun);
	    	if(game.isShooting)
	    	game.shootHandler.post(game.shootRun);
	    	game.multiKillDetector.start();
	    	game.TAG.Continue();
	    	game.killWindow.Continue();
	    	game.bottleAnim.Continue();
	    	game.continueShoot.resumeShoot();
	    	game.bombShoot.Continue();
	    	game.pushBackWave.Continue();
	    	game.slowDownWave.Continue();
	    	game.iceWave.Continue();
	    	game.lightning.Continue();
	    	for(int i=0;i<9;i++)
	    		game.fires[i].Continue();
	    }
	 
	 void pauseGame()
	    //暂停游戏，弹出菜单
	    {
		 game.pauseWindow=new PauseWindow(game);
		 game.pauseWindow.set_Continue_OnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					//按下“继续游戏”的响应
					Continue();
				}
				
			})
			.set_Restart_OnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// 按下“重新开始”的响应
					restartGame(game.stageLevel);				
				}
				
			})
			.set_Return_main_menu_OnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// 按下“返回主菜单”的响应
					Intent intent=new Intent(game,CapStrikeMenu.class);
					game.startActivity(intent);
					game.finish();
				}
				
			})
			.set_Exit_OnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// 按下“退出游戏”的响应
					 game.finish();
					android.os.Process.killProcess(android.os.Process.myPid());
				}
				
			})
			.show();
	    	pause();
	    	
	    }

    private void restartGame(int GameLevel)
    //重新开始游戏，并设置关卡为GameLevel
    {  
    	game.score=0;
    	game.timeSpent=0;
    	game.fireTimes=0;
    	game.shootTimes=0;
    	game.normalXQKill=0;
    	game.magicXQKill=0;
    	game.rareXQKill=0;
    	game.unniqueXQKill=0;
    	game.slowdownTime=-10;
    	game.frozenTime=-10;
    	game.markView.setText("得分:"+game.score);
    	game.timeView.setText("用时:00:00");
    	if(!game.challenge)
    	game.StageLevel.setText("第"+GameLevel+"关");
    	else
    		game.StageLevel.setText("挑战模式");
    	game.continueShoot.stopShoot();
    	game.bombShoot.cancle();
    	game.pushBackWave.cancle();
    	game.slowDownWave.cancle();
    	game.iceWave.cancle();
    	game.lightning.cancle();
    	game.shootHandler.removeCallbacks(game.shootRun);
    	game.TAG.stop();
    	game.killWindow.stop();
    	game.bottleAnim.stop();
    	setBullet();
    	game.isShooting=false;
   	    for(int i=0;i<9;i++)
   	    	game.fires[i].cancle();
   	    for(int i=0;i<5;i++)
   	    	game.prop[i].clearProp();
   	    if(!game.challenge){
   	    game.xiaoQiangControler.XiaoQiangClear();
   	    game.xiaoQiangControler.XiaoQiangInit(GameData.getStage(GameLevel).getRandomMatrix(GameLevel));
   	    }
   	    else{
   	    	game.xq_challenge.clear();
   	    	game.xq_challenge.init();
   	    }
   	    initBottle(); 
   	    game.bulletView.setVisibility(View.GONE);
   	    if(!game.challenge)
   	    Database.setLastStage(GameLevel, game);
			new Handler().postDelayed(new Runnable(){
	    		@Override
	    		public void run() {				    			
	    			initBullet();	
	    			Continue();
	    			game.bulletView.setVisibility(View.VISIBLE);
	    			}}, 300);	  
    }
    

    void showWinWindow(){
   	 new Handler().postDelayed(new Runnable(){
			@Override
			public void run() {				
    	    if(game.pauseWindow!=null&&game.pauseWindow.isShowing())
    	    	game.pauseWindow.dismiss();
			}},200);
         	game.WinWindow=new winWindow(game);
   	        game.WinWindow.set_to_menu_OnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent intent=new Intent(game,CapStrikeMenu.class);
					game.startActivity(intent);
					game.finish();
				}
				
			})
   	 .set_Restart_OnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {			
					restartGame(game.stageLevel);				
				}
				
			})
   	 .set_next_stage_OnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					game.stageLevel++;
					restartGame(game.stageLevel);				
				}
				
			})
   	 .set_exit_OnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					game.finish();
					android.os.Process.killProcess(android.os.Process.myPid());
				}
				
			})
		 .setHitRate((float)game.shootTimes
				 /(float)game.fireTimes)
   	 .show();
   	        Database.saveScore(game.stageLevel, game.score, game);
    }
    

    void showLoseWindow(){
   	 new Handler().postDelayed(new Runnable(){
			@Override
			public void run() {				
   	    if(game.pauseWindow!=null&&game.pauseWindow.isShowing())
   	    	game.pauseWindow.dismiss();
			}},200);
   	    game.LoseWindow=new loseWindow(game);
   	    game.LoseWindow.set_to_menu_OnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent intent=new Intent(game,CapStrikeMenu.class);
					game.startActivity(intent);
					game.finish();
				}
				
			})
		 .set_Restart_OnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {			
					restartGame(game.stageLevel);				
				}
				
			})
			.set_next_stage_OnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					game.stageLevel++;
					restartGame(game.stageLevel);
					
				}
				
			})
		 .set_exit_OnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					game.finish();
					android.os.Process.killProcess(android.os.Process.myPid());
				}
				
			})
		 .show();
   	 
    }
    
    void showChallengeEndWindow(){
      	 new Handler().postDelayed(new Runnable(){
   			@Override
   			public void run() {				
      	    if(game.pauseWindow!=null&&game.pauseWindow.isShowing())
      	    	game.pauseWindow.dismiss();
   			}},200);
      	    game.ChallengeWindow=new ChallengeEndWindow(game);
      	    game.ChallengeWindow.set_to_menu_OnClickListener(new OnClickListener(){

   				@Override
   				public void onClick(View v) {
   					Intent intent=new Intent(game,CapStrikeMenu.class);
   					game.startActivity(intent);
   					game.finish();
   				}
   				
   			})
   		 .set_Restart_OnClickListener(new OnClickListener(){

   				@Override
   				public void onClick(View v) {			
   					restartGame(game.stageLevel);				
   				}
   				
   			})
   		 .set_exit_OnClickListener(new OnClickListener(){

   				@Override
   				public void onClick(View v) {
   					game.finish();
   					android.os.Process.killProcess(android.os.Process.myPid());
   				}
   				
   			})
   			.setHitRate((float)game.shootTimes
				 /(float)game.fireTimes)
   		 .show();
      	 
       }
    
    boolean isCrash(ImageView bullet, ImageView xiaoqiang){
		 
		 LayoutParams param=(LayoutParams) bullet.getLayoutParams();
		 game.x1 = param.x;
		 game.y1 = param.y;		 
		 game.x2 = xiaoqiang.getLeft();
		 game.y2 = xiaoqiang.getTop();	
		 
		 if(game.x1 < game.x2 + xiaoqiang.getWidth()){
			 if(game.x2 < game.x1 + bullet.getWidth()){
				 if(game.y1 < game.y2 + xiaoqiang.getHeight()){
					 if(game.y2 < game.y1 + bullet.getWidth()){
						 return true;						 
					 }
				 }
			 }			 
		 }
		 return false;		 
	 }
	  		 
	 void shakeDetect(int shakeLevel){
		 game.endPos = (int)((1 - (float)(shakeLevel -40)/60) * Size.BOTTLE_TOP);
		 game.footSize = (5 - (shakeLevel - 41)/10)/2 + 1;		 
	 }
	 
	 void initViews(){
		    game.layout=(AbsoluteLayout)game.findViewById(R.id.Layout_stage);
		    initLayout();	
		    game.bulletView = new ImageView(game);
		    game.layout.addView(game.bulletView); 
		    game.bottleView = new ImageView(game);
		    game.bottleView.setClickable(true);		 
		    game.layout.addView(game.bottleView); 
			game.markView.setText("得分:"+game.score);
			game.timeView.setText("用时:00:00");
			if(!game.challenge)
			game.StageLevel.setText("第"+game.stageLevel+"关");
			else
				game.StageLevel.setText("挑战模式");
	 }
	 
	 void initBullet(){	
		 	
		 game.bulletParam=(LayoutParams)game.bulletView.getLayoutParams();
		 game.shootPos = (game.bottleView.getLeft() + game.bottleView.getRight())/2 - Size.OFFSET;			
		 game.bulletParam.x= (int)game.shootPos;
		 game.bulletParam.y= Size.BOTTLE_TOP - Size.BULLET_HEIGHT;
		 game.bulletView.setLayoutParams(game.bulletParam);		 
		 game.bulletView.setImageResource(R.drawable.capmod);		 
	 }
	 
	 void initBottle(){
		 	
		 game.bottleParam =(LayoutParams)game.bottleView.getLayoutParams();	
		 game.bottleParam.x = Size.screenWidth/2 - Size.BOTTLE_WIDTH*3/2;
		 game.bottleParam.y = Size.BOTTLE_TOP;
		 game.bottleParam.width = Size.BOTTLE_WIDTH * 3;
		 game.bottleView.setLayoutParams(game.bottleParam);			
		 game.bottleView.setImageResource(R.drawable.bottle);
		 game.bottleView.setOnTouchListener(new BottleOnTouchListener(game));
	 }
	 	 
	 void setBullet(){		
		 game.shootPos = (game.bottleView.getLeft() + game.bottleView.getRight())/2 - Size.OFFSET;		
		 game.bulletParam.x = (int)game.shootPos;
		 game.bulletParam.y = Size.BOTTLE_TOP - Size.BULLET_HEIGHT;
		 game.bulletView.setLayoutParams(game.bulletParam);
		 Size.bulletTop = Size.BOTTLE_TOP - Size.BULLET_HEIGHT;
	 }
	 
	 int[] getAim(int bulletWidth,int position){
		
		 int unitWidth = Size.screenWidth/9;
		 int columnNum = (int)position/unitWidth;
		 int xqX = unitWidth*(columnNum)+(unitWidth - Size.XIAOQIANG_WIDTH)/2;
		 int[] index= new int[]{-1,-1};
        if(mayShoot(xqX,position,bulletWidth)){
       	 index[0] = columnNum;        	 
        }
        if(mayShoot(xqX + unitWidth,position,bulletWidth)){
       	 if(columnNum + 1<9)
       	 index[1] = columnNum + 1; 
        }
        int[] targetIndex=null;
        if(!game.challenge){
        targetIndex = new int[]{-1,-1};
        if(index[0]!=-1)
		 for(int i=index[0]+9*5;i>=index[0];i=i-9){
			 if(game.xiaoqiang[i]!=null && game.xiaoqiang[i].isAlive()){
				 targetIndex[0]=i;
				 break;
			 }
		 }
        if(index[1]!=-1)
   		 for(int i=index[1]+9*5;i>=index[1];i=i-9){
   			 if(game.xiaoqiang[i]!=null && game.xiaoqiang[i].isAlive()){
   				 targetIndex[1]=i;
   				 break;
   			 }
   		 }}
        else{
        	targetIndex = new int[]{-1,-1,-1,-1};
        	if(index[0]!=-1){
        		XQ_queue temp=game.xq_challenge.start;   
        		for(int i=0;i<game.xq_challenge.QueueNum;i++){
        			if(temp!=null && temp.xq!=null)
        					if(temp.xq[index[0]]!=null && temp.xq[index[0]].isAlive()){
        						targetIndex[0]=i+1;
        						targetIndex[1]=index[0];
        						break;
        					}
        			temp=temp.next;
        		}}
        	if(index[1]!=-1){
        		XQ_queue temp=game.xq_challenge.start;   
        		for(int i=0;i<game.xq_challenge.QueueNum;i++){
        			if(temp!=null && temp.xq!=null)
        					if(temp.xq[index[1]]!=null && temp.xq[index[1]].isAlive()){
        						targetIndex[2]=i+1;
        						targetIndex[3]=index[1];
        						break;
        					}
        			temp=temp.next;
        		}}
        }
        return targetIndex;
	 }
	 
	  boolean mayShoot(int xqX, int bulletX,int bulletWidth){
		 if(bulletWidth<Size.XIAOQIANG_WIDTH){
		 if(bulletX < xqX && xqX < bulletX + bulletWidth)return true;
		 if(bulletX >= xqX && bulletX + bulletWidth <= xqX + Size.XIAOQIANG_WIDTH)return true;
		 if(bulletX < xqX + Size.XIAOQIANG_WIDTH && xqX + Size.XIAOQIANG_WIDTH <bulletX + bulletWidth)return true;
		 }
		 return false;		 
	 }
	  
	  void SoundInit(){
	 		XiaoQiang.SoundPoolInit(game);
	 		winWindow.SoundPoolInit(game);
	 		loseWindow.SoundPoolInit(game);
	 		KillWindow.SoundPoolInit(game);
	 		ChallengeEndWindow.SoundPoolInit(game);
	 		game.sound=new SoundPool(10, AudioManager.STREAM_MUSIC,0);
	 		game.sound_shoot=game.sound.load(game, R.raw.sound_shoot, 0);
	 	 }
	 	 
		
		
	     boolean isLost(){
	    	 if(!game.challenge)
	    	 for(int i=53;i>=0;i--){
	    		 if(game.xiaoqiang[i]!=null&&game.xiaoqiang[i].isAlive()
	    		&&game.xiaoqiang[i].get_LEFT_TOP_Point().y+game.xiaoqiang[i].getHeight()>Size.layoutHeight-Size.BOTTLE_HEIGHT-Size.BULLET_HEIGHT/2)
	    			 return true;
	    	 }
	    	 else{
	    		 XQ_queue temp=game.xq_challenge.start;
	    		 if(temp!=null && temp.xq!=null){
	    			 for(int i=0;i<9;i++)
	    				 if(temp.xq[i]!=null&&temp.xq[i].isAlive()
	    				    		&&temp.xq[i].get_LEFT_TOP_Point().y+Size.XIAOQIANG_HEIGHT>Size.layoutHeight-Size.BOTTLE_HEIGHT-Size.BULLET_HEIGHT/2)
	    				    			 return true;
	    		 }
	    	 }
	    	 return false;
	     }
	     
	      void sensorDetect(SensorEvent e){
			 if(game.isFirstTime){
				 accX = lastAccX = e.values[SensorManager.DATA_X];  
				 accY = lastAccY = e.values[SensorManager.DATA_Y];  
				 accZ = lastAccZ = e.values[SensorManager.DATA_Z];
				 game.isFirstTime = false;
	         	}
	         	if(!game.isFirstTime){
	         		accX = e.values[SensorManager.DATA_X];  
	         		accY = e.values[SensorManager.DATA_Y];  
	         		accZ = e.values[SensorManager.DATA_Z];
	         	}
	         	game.setTitle("x=" + (int)accX + "," + "y=" + (int)accY + "," + "z=" + (int)accZ);	                            
	                 long currentTime = System.currentTimeMillis();
	                 if((currentTime - startTime) > 20){  
	                	long dividedTime = currentTime - startTime;                 	
	                	startTime = currentTime;     	                	
	                 	float speed = (float) (Math.abs(accX + accY + accZ - lastAccX - lastAccY - lastAccZ) / dividedTime * 10000);  
	                 	shakeLevel = (int)((speed/GATE_SPEED) * 100);	
	                 	cache1=cache2;
	                 	cache2=cache3;
	                 	cache3=cache4;
	                 	cache4=shakeLevel;
	    				if(shakeLevel > 100)
	    				shakeLevel = 100;
	    				game.shakeLevelBar.setProgress((cache1+cache2+cache3+cache4+shakeLevel)/5);
	    				if(cache3>cache1&&cache3>shakeLevel&&cache3>=cache2&&cache3>=cache4)
	                 	shoot(shakeLevel);                    	
	                 	lastAccX = accX;
	                 	lastAccY = accY;
	                 	lastAccZ = accZ;   
	                                 
	                 }
	         }
		 		 
		  void initLayout()
		    {
			  game.lastPosX = Size.screenWidth/2;  		    		    			 	 		       		    	
			  game.shakeLevelBar = (ProgressBar)game.findViewById(R.id.shakeLevelBar); 		    		    		    	
			  game.markView = (TextView)game.findViewById(R.id.markView);
			  game.timeView = (TextView)game.findViewById(R.id.timeView);
			  game.StageLevel=(TextView)game.findViewById(R.id.stageText);
			  game.shakeLevelBar.setMax(100);	    		 	    		    		   	    		    		    		    		    		    	
		    }
		    	 	 	 	 	 	 	  
		  void shoot(int shakeLevel)
		  /**发射子弹函数*/
		  {			
				 if(shakeLevel>40)
					 game.bottleAnim.start();
		    if(!game.isShooting){
		    	if(game.canShoot && shakeLevel > 40){	
		    		game.canShoot = false;
		    		game.gameControler.shakeDetect(shakeLevel);
		    		game.shootHandler.post(game.shootRun);
		    	new Handler().postDelayed(new Runnable(){
		    		@Override
		    		public void run() {	
		    			game.canShoot = true;    				    				
		    			}}, 300);	   
		    	game.isShooting = true;
		    	game.fireTimes++;
		    	addScore(shakeLevel/10);
		    	game.sound.play(game.sound_shoot, 1, 1, 1, 0, 1);
		}	  
	 }
	}
		  
		  public void addScore(int score){
			  float r1=1;
			  if(game.fireTimes!=0)
			  r1=game.shootTimes/game.fireTimes;				  
			  float r2;
			  if(!game.challenge){
			  r2=1-((float)game.timeSpent)/(GameData.getStage(game.stageLevel).duration/1000*1.5f);
			  if(r2<0) r2=0;
			  }
			  else{
				  r2= 1+((float)game.timeSpent)/60;
			  }
			  float r=(float) (r1*0.4+r2*0.4+0.2);
			  game.score+=score*r;
			  game.markView.setText("得分:"+game.score);
		  }
		  
		  public void initProps(){
			 game.prop[0]=new Prop(R.id.prop1,game);
			 game.prop[1]=new Prop(R.id.prop2,game);
			 game.prop[2]=new Prop(R.id.prop3,game);
			 game.prop[3]=new Prop(R.id.prop4,game);
			 game.prop[4]=new Prop(R.id.prop5,game);
		  }
		  
		  public Prop getEmptyProp(){
			  Prop prop=null;
			  for(int i=0;i<5;i++){
				  if(game.prop[i].isEmpty()){
					  prop=game.prop[i];
					  break;
				  }
			  }
			  return prop;
		  }


}
