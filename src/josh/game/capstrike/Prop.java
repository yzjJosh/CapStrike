package josh.game.capstrike;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class Prop {
	static final int NONE=0;
	static final int MISSLE = 1;
	static final int FROST_NOVA = 2;
	static final int LASER = 3;
	static final int PUSHBACK = 4;
	static final int FIREBALL = 5;
	static final int SLOWDOWN = 6;
	static final int LIGHTENING = 7;
	static final int CONTINUOUS = 8;	
	private boolean isEmpty=true;
	
	GamePlayActivity game;
	
	ImageView propView;
	
	Prop(int propId,Context context){		
		game = (GamePlayActivity)context;
		propView = (ImageView)game.findViewById(propId);
	}
			
	public void clearProp(){
		propView.setImageResource(R.drawable.prop);
		propView.setClickable(false);
		isEmpty=true;
	}
	
	public boolean isEmpty(){
		return isEmpty;
	}
	
	public void setPropType(int propType){	
		if(propType!=NONE){
		isEmpty=false;
		game.TAG.stop();
		propView.setClickable(true);
		switch(propType){
		case MISSLE:propView.setImageResource(R.drawable.missle);
					propView.setOnClickListener(new MissleListener());
					break;
		case FROST_NOVA:propView.setImageResource(R.drawable.frostnova);
					    propView.setOnClickListener(new NovaListener());
					    break;		
		case LASER:propView.setImageResource(R.drawable.laserprop);
				   propView.setOnClickListener(new LaserListener());
				   break;	
		case PUSHBACK:propView.setImageResource(R.drawable.pushback);
					  propView.setOnClickListener(new PushbackListener());
					  break;	
		case FIREBALL:propView.setImageResource(R.drawable.fireball);
					  propView.setOnClickListener(new FireballListener());
					  break;	
		case SLOWDOWN:propView.setImageResource(R.drawable.slowdown);
					  propView.setOnClickListener(new SlowdownListener());
					  break;	
		case LIGHTENING:propView.setImageResource(R.drawable.lightning_strike);
						propView.setOnClickListener(new LightningListener());
					    break;	
		case CONTINUOUS:propView.setImageResource(R.drawable.continuous);
						propView.setOnClickListener(new ContinuousListener());
					    break;	
		default:break;
		}	
		game.TAG.show(propType);
		}
	}
								
	class MissleListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(!game.bombShoot.isShooting()){		
			((ImageView)v).setImageResource(R.drawable.prop);
			((ImageView)v).setClickable(false);
			BombExplode();
			isEmpty=true;
			}
		}
	}
	
	class NovaListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(!game.iceWave.isShooting()){
			((ImageView)v).setImageResource(R.drawable.prop);
			((ImageView)v).setClickable(false);
			FrostNova();
			isEmpty=true;
			}
		}
	}
	
	class LaserListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub		
			((ImageView)v).setImageResource(R.drawable.prop);
			((ImageView)v).setClickable(false);
			LaserStrike();
			isEmpty=true;
		}
	}
	
	class PushbackListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(!game.pushBackWave.isShooting()){		
			((ImageView)v).setImageResource(R.drawable.prop);
			((ImageView)v).setClickable(false);
			PushBack();
			isEmpty=true;
			}
		}
	}
	
	class FireballListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			boolean bool=true;
			for(int i=0;i<9;i++){
				if(game.fires[i].isShooting())
					bool=false;
			}
			if(bool){
			((ImageView)v).setImageResource(R.drawable.prop);
			((ImageView)v).setClickable(false);
			FireStrike();
			isEmpty=true;
			}
		}
	}
	
	class SlowdownListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(!game.slowDownWave.isShooting()){		
			((ImageView)v).setImageResource(R.drawable.prop);
			((ImageView)v).setClickable(false);
			SlowDown();
			isEmpty=true;
			}
		}
	}
	
	class LightningListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(!game.lightning.isLightning()){		
			((ImageView)v).setImageResource(R.drawable.prop);
			((ImageView)v).setClickable(false);
			LightningStrike();
			isEmpty=true;
			}
		}
	}
	
	class ContinuousListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(!game.continueShoot.isShooting){		
			((ImageView)v).setImageResource(R.drawable.prop);
			((ImageView)v).setClickable(false);
			ContinueShoot();
			isEmpty=true;
			}
		}
	}
	
	  private void ContinueShoot(){
	 		game.gameControler.addScore(40);
	 		game.continueShoot.continuousShoot();
	 	  }
	      	    
	  private  void LaserStrike(){
	    	  game.gameControler.addScore(60);
	    	 final ImageView laser=new ImageView(game);
	    	 Bitmap bit=BitmapFactory.decodeResource(game.getResources(),R.drawable.laser);
	    	 Bitmap BIT = Bitmap.createScaledBitmap(bit, bit.getWidth(), Size.BOTTLE_TOP, true);
	    	 laser.setImageBitmap(BIT);
	    	 game.layout.addView(laser);
	    	 LayoutParams laserParam=(LayoutParams) laser.getLayoutParams();
	    	 int laserX = (game.bottleView.getLeft() + game.bottleView.getRight())/2 - BIT.getWidth()/2;
	    	 laserParam.x = laserX;
	    	 laserParam.y=0;  
	    	 laser.setLayoutParams(laserParam);
	    	 game.sound.play(game.sound_shoot, 1, 1, 1, 0, 1);
	    	 game.fireTimes++;
	    	 int[] index=game.gameControler.getAim(BIT.getWidth(),(int) game.shootPos);
	    	 boolean shoot=false;
	    	 if(!game.challenge){
	    	 if(index[0]!=-1)
	    	 for(int i=index[0];i>=0;i=i-9){   		 
	    		 if(game.xiaoqiang[i]!=null&&game.xiaoqiang[i].isAlive()){
	    			 game.xiaoqiang[i].reduceLife(1);    			 
	    			 game.shootTimes++;
	    			 game.gameControler.addScore(10);
	    			 game.shootTimes--;
	    			 shoot=true;
	    		 }
	    	 }
	    	 if(index[1]!=-1)
	    	 for(int i=index[1];i>=0;i=i-9){    		
	    		 if(game.xiaoqiang[i]!=null&&game.xiaoqiang[i].isAlive()){
	    			 game.xiaoqiang[i].reduceLife(1);
	    			 game.shootTimes++;
	    			 game.gameControler.addScore(10);
	    			 game.shootTimes--;
	    			 shoot=true;
	    		 }
	    	 }}
	    	 else{
	    		 if(index[0]!=-1 && index[1]!=-1){
	    			 XQ_queue queue=game.xq_challenge.getQueue(index[0]);
	    	    	 for(;;){  
	    	    		 if(queue==null)break;
	    	    		 if(queue!=null&&queue.xq!=null)
	    	    		 if(queue.xq[index[1]]!=null&&queue.xq[index[1]].isAlive()){
	    	    			 queue.xq[index[1]].reduceLife(1);    			 
	    	    			 game.shootTimes++;
	    	    			 game.gameControler.addScore(10);
	    	    			 game.shootTimes--;
	    	    			 shoot=true;
	    	    		 }
	    	    		 queue=queue.next;
	    	    	 }}
	    		 if(index[2]!=-1 && index[3]!=-1){
	    			 XQ_queue queue=game.xq_challenge.getQueue(index[2]);
	    	    	 for(;;){  
	    	    		 if(queue==null)break;
	    	    		 if(queue!=null&&queue.xq!=null)
	    	    		 if(queue.xq[index[3]]!=null&&queue.xq[index[3]].isAlive()){
	    	    			 queue.xq[index[3]].reduceLife(1);    			 
	    	    			 game.shootTimes++;
	    	    			 game.gameControler.addScore(10);
	    	    			 game.shootTimes--;
	    	    			 shoot=true;
	    	    		 }
	    	    		 queue=queue.next;
	    	    	 }}
	    	 }
	    	 if(shoot) game.shootTimes++;
	    	 if(!game.challenge)
	    	 if(game.xiaoQiangControler.getXiaoQiangAmount()==0){
	    		 game.gameControler.pause();
	    		 game.gameControler.showWinWindow();
				}
			 new Handler().postDelayed(new Runnable(){
		    		@Override
		    		public void run() {	
		    			game.layout.removeView(laser);				
		    			}}, 400);	     
	     }
	     

	  private void FrostNova(){
	    	  game.gameControler.addScore(80);
	    	  game.iceWave.prepare().shoot();
	    	  
	    	  if(game.challenge){
	    		  boolean freeze=false;
	    		  XQ_queue queue=game.xq_challenge.start;
	    		  if(queue!=null && queue.xq!=null){
	    			  for(int i=0;i<9;i++){
	    				  if(queue.xq[i]!=null&&queue.xq[i].isAlive()&&queue.xq[i].getImage().getTop()<-Size.XIAOQIANG_HEIGHT){
	    					  freeze=true;
	    					  break;
	    				  }
	    			  }
	    			  
	    			  if(freeze){
	    				  for(;;){
	    					  for(int j=0;j<9;j++)
	    						  if(queue.xq[j]!=null&&queue.xq[j].isAlive())
	    							  queue.xq[j].freeze(10);
	    					  queue=queue.next;
	    					  if(queue==null)break;
	    				  }
	    			  }
	    		  }
	    	  }
	    	  game.frozenTime=game.timeSpent;
	    	
	     }
	     
	  private  void BombExplode(){
	    	  game.gameControler.addScore(40);
	    	  game.bombShoot.prepare().shoot();			
	     }
	     

	  private void SlowDown(){
	    	  game.gameControler.addScore(60);
	    	  game.slowDownWave.prepare().shoot();
	    	  if(game.challenge){
	    		  boolean slow=false;
	    		  XQ_queue queue=game.xq_challenge.start;
	    		  if(queue!=null && queue.xq!=null){
	    			  for(int i=0;i<9;i++){
	    				  if(queue.xq[i]!=null&&queue.xq[i].isAlive()&&queue.xq[i].getImage().getTop()<-Size.XIAOQIANG_HEIGHT){
	    					  slow=true;
	    					  break;
	    				  }
	    			  }
	    			  
	    			  if(slow){
	    				  for(;;){
	    					  for(int j=0;j<9;j++)
	    						  if(queue.xq[j]!=null&&queue.xq[j].isAlive())
	    							  queue.xq[j].slowDown(0.4f, 10);
	    					  queue=queue.next;
	    					  if(queue==null)break;
	    				  }
	    			  }
	    		  }
	    	  }
	    	  game.slowdownTime=game.timeSpent;
	     } 
	     
	     
	  private void PushBack(){
	    	  game.gameControler.addScore(70);
	    	  game.pushBackWave.prepare().shoot();
	     }
	     
	    
	  private void FireStrike(){
	    	  game.gameControler.addScore(80);
	    	 int Xspace=game.getWindowManager().getDefaultDisplay().getWidth()/9;
	    	 for(int i=0;i<9;i++){
	    		 game.fires[i].prepare()
	    		 .setStartPosition(Xspace*(i)+(Xspace-Size.XIAOQIANG_WIDTH)/2)
	    		 .shoot();
	    	 }
	     }
	     
	     
	  private void LightningStrike(){
	    	 game.gameControler.addScore(100);
	    	 game.lightning.prepare().start();
	     }		  	
}

class Tag implements Runnable{
	  private long startTime;
	  private long duration;
	  private Handler handler=new Handler();
	  private LinearLayout tag;
	  private GamePlayActivity game;
	  private boolean isShowing=false;
	  
	  Tag(long duration,Context context){
		  this.duration=duration;
		  game=(GamePlayActivity)context;
	  }
	  
	  public void stop(){
		  if(isShowing){
		  handler.removeCallbacks(this);
		  game.layout.removeView(tag);
		  isShowing=false;
		  }
	  }
	  
	  public void pause(){
		  if(isShowing)
			  handler.removeCallbacks(this);
	  }
	  
	  public void Continue(){
		  if(isShowing)
			  handler.post(this);
	  }
	  
	  @SuppressWarnings("deprecation")
	public void show(int propType){
		  if(!isShowing){
		  isShowing=true;
		  tag=new LinearLayout(game);
		  tag.setOrientation(LinearLayout.VERTICAL);
		  LinearLayout row1=new LinearLayout(game);
		  row1.setOrientation(LinearLayout.HORIZONTAL);
		  row1.setGravity(Gravity.CENTER);
		  TextView text1=new TextView(game);
		  TextView text2=new TextView(game);
		  text1.setText("»ñµÃµÀ¾ß");
		  text1.setTextSize(20);
		  text1.setTextColor(Color.WHITE);
		  text2.setTextSize(20);
		  LinearLayout row2=new LinearLayout(game);
		  row2.setOrientation(LinearLayout.HORIZONTAL);
		  row2.setGravity(Gravity.CENTER);
		  ImageView image=new ImageView(game);
		  switch(propType){
		  case Prop.MISSLE:
			  text2.setText("¾ÞÐÍÕ¨µ¯");
			  text2.setTextColor(Color.rgb(0xff, 0x61, 0));
			  image.setImageResource(R.drawable.missle);
			  break;
		  case Prop.FROST_NOVA:
			  text2.setText("Ëª¶³ÐÂÐÇ");
			  text2.setTextColor(Color.BLUE);
			  image.setImageResource(R.drawable.frostnova);
		      break;		
        case Prop.LASER:
      	  text2.setText("¼¤¹âÉä»÷");
			  text2.setTextColor(Color.rgb(0xe3, 0xcf, 0x57));
			  image.setImageResource(R.drawable.laserprop);
	          break;	
        case Prop.PUSHBACK:
      	  text2.setText(" ³å»÷²¨");
			  text2.setTextColor(Color.GREEN);
			  image.setImageResource(R.drawable.pushback);
		      break;	
        case Prop.FIREBALL:
      	  text2.setText("»ðÑæ´ò»÷");
			  text2.setTextColor(Color.RED);
			  image.setImageResource(R.drawable.fireball);
		      break;	
        case Prop.SLOWDOWN:
      	  text2.setText(" ¼õËÙ ");
			  text2.setTextColor(Color.rgb(0, 0xff, 0x7f));
			  image.setImageResource(R.drawable.slowdown);
		      break;	
        case Prop.LIGHTENING:
      	  text2.setText("ÎåÀ×ºä¶¥");
			  text2.setTextColor(Color.YELLOW);
			  image.setImageResource(R.drawable.lightning_strike);
		      break;	
        case Prop.CONTINUOUS:
      	  text2.setText("Á¬ÐøÉä»÷");
			  text2.setTextColor(Color.rgb(0xff, 0x7d, 0x40));
			  image.setImageResource(R.drawable.continuous);
		      break;	
        default:break;
		  }
		  row1.addView(text1);
		  row1.addView(text2);
		  row2.addView(image);
		  tag.addView(row1);
		  tag.addView(row2);
		  tag.setBackgroundColor(Color.argb(50, 0, 0, 0));
		  game.layout.addView(tag);		
		  LayoutParams param=(LayoutParams) tag.getLayoutParams();
		  param.y=-Size.screenHeight;
		  tag.setLayoutParams(param);
		  new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				LayoutParams param=(LayoutParams) tag.getLayoutParams();
				param.x=Size.screenWidth/2-tag.getWidth()/2;
				param.y=Size.layoutHeight/5;
				tag.setLayoutParams(param);				
			}
			  
		  }, 20);
		  			
		  startTime=game.timeSpent;
		  handler.post(this);
		  }
	  }
	  
	@Override
	public void run() {
		handler.postDelayed(Tag.this, 200);
		if(game.timeSpent>=startTime+duration){
			stop();
		}
		
	}		  
}
