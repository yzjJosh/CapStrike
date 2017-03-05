package josh.game.capstrike;
//CRun.java
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Handler;

class CRun implements Runnable{
	 private Handler cHandler = new Handler();
	 private int count = 0;	 
	 private GamePlayActivity game;
	 public boolean isShooting=false;	
	 
	 CRun(Context context){
		 this.game = (GamePlayActivity)context;
	 }
	 public void continuousShoot(){
		 if(!isShooting){
		 game.sensorMgr.unregisterListener(game.lsn , game.sensor);   
		 game.shootRun.setFootprint(Size.layoutHeight/20);
		 cHandler.post(CRun.this);  
		 game.sound.play(game.sound_shoot, 1, 1, 1, 0, 1);	 
		 game.fireTimes++; 
		 isShooting=true;		
		 }
    }
	 
	 @SuppressWarnings("deprecation")
	@Override
	 public void run() {		 
		if(isShooting){
		cHandler.postDelayed(CRun.this,35); 
		int shakeLevel = 100;
		game.shakeLevelBar.setProgress(shakeLevel);				 	    	    	    	
   	    game.gameControler.shakeDetect(shakeLevel);
   	    game.isShooting = true; 	    	    			
			if(game.bulletParam.y - Size.BULLET_HEIGHT <= game.endPos){				
				cHandler.removeCallbacks(CRun.this);
				game.isShooting = false;
				game.gameControler.setBullet();	
				count ++;
				if(count >= 10){
					stopShoot();					
					return;
				}else{
				cHandler.post(CRun.this);
				game.sound.play(game.sound_shoot, 1, 1, 1, 0, 1);
				game.fireTimes++;  	 				
				return;
				}
			}
			game.bulletParam.y  = Size.bulletTop;			
			game.bulletView.setLayoutParams(game.bulletParam);				
			Size.bulletTop = Size.bulletTop - game.shootRun.footprint/game.footSize;		
			int[] temp = game.gameControler.getAim(Size.BULLET_WIDTH, game.bulletView.getLeft());		
			boolean isShooted = false;	
			if(!game.challenge){
			if(temp[0] != -1){
				if(game.gameControler.isCrash(game.bulletView,game.xiaoqiang[temp[0]].getImage())){
					game.xiaoqiang[temp[0]].reduceLife(1);					
					cHandler.removeCallbacks(CRun.this);						
					isShooted = true;	
					game.shootTimes++;
					game.gameControler.addScore(10);
					game.shootTimes--;
				}
			}
			
			if(temp[1] != -1){
				if(game.gameControler.isCrash(game.bulletView,game.xiaoqiang[temp[1]].getImage())){
					game.xiaoqiang[temp[1]].reduceLife(1);					
					cHandler.removeCallbacks(CRun.this);					
					isShooted = true;		
					game.shootTimes++;
					game.gameControler.addScore(10);
					game.shootTimes--;
				}
			}}
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
				game.shootTimes++;
				cHandler.removeCallbacks(CRun.this);
				game.gameControler.setBullet();		
				count ++;
				if(count >= 10){
					stopShoot();
				}
				else{
				cHandler.post(CRun.this);  				
				game.sound.play(game.sound_shoot, 1, 1, 1, 0, 1);	 
				game.fireTimes++; 				
				}
				if(!game.challenge)
				if(game.xiaoQiangControler.getXiaoQiangAmount()==0){
					game.gameControler.pause();
					game.gameControler.showWinWindow();						
				}		
		}   	      
		 
	 }
}
	 
	 public void stopShoot(){
	 if(isShooting){
		 isShooting=false;
		 cHandler.removeCallbacks(CRun.this);						
		 count = 0;
		 game.shootRun.setFootprint(18*Size.screenHeight/480);	
		 if(!game.isPaused)
	     game.sensorMgr.registerListener(game.lsn, game.sensor, SensorManager.SENSOR_DELAY_GAME);	    
	     game.gameControler.setBullet();
	     game.isShooting = false;
	   }
	 }
	 
	 public void pauseShoot(){		
		 if(isShooting){
		 cHandler.removeCallbacks(CRun.this);	
		 }
	 }
	 
	 public void resumeShoot(){
		 if(isShooting){
		 if(isShooting){
		 cHandler.post(CRun.this);	
		 }
		 else
		 {
		 if(count <10)
			game.gameControler.setBullet();				  		
		 }
	 }
	}
}
