package josh.game.capstrike;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.AbsoluteLayout.LayoutParams;



	  @SuppressWarnings({ "deprecation" })
	class Lightning implements Runnable{
 		 private ImageView lightning;		 
          private int times=0;
          private boolean isLightning=false;
          private boolean isPaused=false;
          private boolean hasPrepared=false;
          private Handler LightningHandler=new Handler();
          private GamePlayActivity game;
 		
       	public Lightning(Context context) {
       		   this.game=(GamePlayActivity) context;				
 		}
       	
       	public Lightning prepare(){
       		if(!isLightning){
       		int width=game.getWindowManager().getDefaultDisplay().getWidth();
 	    	 int height=game.layout.getHeight();
 	    	 lightning=new ImageView(game);
 	    	 Bitmap bit= BitmapFactory.decodeResource(game.getResources(), R.drawable.lightning);
 	    	 Bitmap bitmap=Bitmap.createScaledBitmap(bit, width, height, true);
 	    	 lightning.setImageBitmap(bitmap);
 	    	 lightning.setBackgroundColor(Color.argb(50, 0, 0, 0));
 	    	 times=0;
 	    	 hasPrepared=true;
 	    	 isPaused=false; 
       		}
 	    	 return this;
       	}
       	
       public void start(){
     	  if(!isLightning&&hasPrepared){
     	  isPaused=false;
     	  hasPrepared=false;
     	  isLightning=true;
     	 game.layout.addView(lightning);
     	 game.fireTimes++;     	
     	  LightningHandler.post(this);
     	  }
       }
       
       public void pause(){
     	  if(isLightning){
     	  LightningHandler.removeCallbacks(this);
     	  isPaused=true;
     	  }
       }
       
       public void Continue(){
     	  if(isLightning&&isPaused){
     		  isPaused=false;
     		  LightningHandler.post(this);
     	  }
       }
       
       public void cancle(){
     	  isLightning=false;
     	  isPaused=false;
     	  LightningHandler.removeCallbacks(this);
     	 game.layout.removeView(lightning);
       }
       
       public boolean isLightning(){
    	   return isLightning;
       }
       
       	
 			@Override
 			public void run() {
 				LightningHandler.postDelayed(this, 100);
 				if(times%2==0)
 					game.layout.removeView(lightning);
 					else{
 						game.layout.addView(lightning);
 						if(times==3){
 							boolean shoot=false;
 							if(!game.challenge)
 							for(int i=0;i<54;i++){
 								if(game.xiaoqiang[i]!=null&&
 										game.xiaoqiang[i].isAlive()){
 									game.xiaoqiang[i].reduceLife(1);
 									game.shootTimes++;
 									game.gameControler.addScore(10);
 									game.shootTimes--;
 								    shoot=true;
 								}
 								}
 							else{
 								XQ_queue temp=game.xq_challenge.start;
 								for(int i=0;i<game.xq_challenge.QueueNum;i++){
 									if(temp!=null && temp.xq!=null)
 										for(int j=0;j<9;j++)
 											if(temp.xq[j]!=null && temp.xq[j].isAlive()){
 												temp.xq[j].reduceLife(1);
 			 									game.shootTimes++;
 			 									game.gameControler.addScore(10);
 			 									game.shootTimes--;
 			 								    shoot=true;
 											}
 									temp=temp.next;
 								}
 							}
 							if(shoot) game.shootTimes++;
 							if(!game.challenge)
 							 if(game.xiaoQiangControler.getXiaoQiangAmount()==0){
 									game.gameControler.pause();
 									game.gameControler.showWinWindow();
 							}
 						}
 					}				  				
 					if(times==4){					
 						LightningHandler.removeCallbacks(this);												
 						 isLightning=false;
 					}
 					 times++;	
 				}
  		 
  	 }
 	 
 	  abstract class ShootOut implements Runnable{
 		 protected ImageView image; 
 		 protected Handler handler=new Handler();
 		 protected GamePlayActivity game;
 		 protected boolean isCompleted=true;
 		 protected boolean isPaused=false;
		@SuppressWarnings("deprecation")
		 protected LayoutParams param;
 		 protected Bitmap bitmap;
 		 protected int space=18;
 		 protected final int spaceTime=25;
 		 protected int[] index;
 		 protected int imageId;
 		 
 		 ShootOut(Context context,int ImageId){						
 			this.game=(GamePlayActivity) context;
 			new Handler().postDelayed(new Runnable(){

 				@Override
 				public void run() {
 					space = 18*Size.screenHeight/480;				
 				}			
 			}, 500);	
 			imageId=ImageId;			
 		 }
 		 
 		 @SuppressWarnings("deprecation")
		public ShootOut prepare(){
 			 if(isCompleted){
 			    bitmap=getBitmap();
 			    image=new ImageView(game);
 				image.setImageBitmap(bitmap);
 				game.layout.addView(image);			   
 				int Pos = getStartXPosition();
 				param=(LayoutParams) image.getLayoutParams();
 				param.x=Pos;
 				param.y=game.layout.getBottom();
 				image.setLayoutParams(param);			
 				index=game.gameControler.getAim(bitmap.getWidth(),(int) (game.shootPos+Size.OFFSET/2)-bitmap.getWidth()/2);
 				isCompleted=false;
 				isPaused=false;}
 				return this;
 		 }
 		 
		@SuppressWarnings("deprecation")
		public ShootOut setStartPosition(int x){
 			 if(param!=null){
 			 param.x=x;
 			 if(image!=null)
 				 image.setLayoutParams(param);
 			 }	
 			 if(bitmap!=null)
 			 index=game.gameControler.getAim(bitmap.getWidth(),x);
 			 return this;
 		 }
 		 
 		 public void shoot(){
 			 if(!isCompleted){
 			 handler.post(this);
 			 isPaused=false;
 			game.sound.play(game.sound_shoot, 1, 1, 1, 0, 1);
 			game.fireTimes++; 		
 			 }
 		 }
 		 
 		 public void pause(){
 			 if(!isCompleted){
 			 handler.removeCallbacks(this);
 			 isPaused=true;
 			 }
 		 }
 		 
 		 public void Continue(){
 			 if(!isCompleted&&isPaused){
 				 handler.post(this);
 				 isPaused=false;
 			 }
 		 }
 		 
 		 public void cancle(){
 			 handler.removeCallbacks(this);
 			game.layout.removeView(image);
 			 isPaused=false;
 			 isCompleted=true;
 		 }
 		 
 		 public boolean isShooting(){
 			 return !isCompleted;
 		 }
 		 
 		 public abstract void onShoot(int[] shootedIndex,ImageView bullet);
 		 
 		 public ShootOut copy(){
 			 final ShootOut copy=this;
 			 return new ShootOut(game,imageId){

 				@Override
 				public void onShoot(int[] shootedIndex, ImageView bullet) {
 					copy.onShoot(shootedIndex,bullet);
 					
 				}
 				 
 			 };
 		 }
 		 
 		 protected int getStartXPosition(){
 			 return (game.bottleView.getLeft() 
 					 + game.bottleView.getRight())/2
 					 - bitmap.getWidth()/2;
 		 }
 		 
 		 protected Bitmap getBitmap(){
 			 return BitmapFactory.decodeResource(game.getResources(), imageId);
 		 }
 		 
 		 protected void onImageMove(){
 			 if(!game.challenge){
 			 int[] shooted=new int[]{-1,-1};
 				if(index[0]!=-1){
 					if(game.gameControler.isCrash(image,game.xiaoqiang[index[0]].getImage()))
 						shooted[0]=index[0];
 				}
 				if(index[1]!=-1){
 					if(game.gameControler.isCrash(image,game.xiaoqiang[index[1]].getImage())){
 						if(shooted[0]==-1)
 							shooted[0]=index[1];
 						else
 							shooted[1]=index[1];
 					}
 				}
 				if(shooted[0]!=-1||shooted[1]!=-1){
 					handler.removeCallbacks(ShootOut.this);				
 					isCompleted=true;
 					game.shootTimes++;
 					onShoot(shooted,image);
 					game.layout.removeView(image);
 				}}
 			 else{
 				index=game.gameControler.getAim(bitmap.getWidth(),param.x);
 				int[] shooted=new int[]{-1,-1,-1,-1};
 				if(index[0]!=-1 && index[1]!=-1){
 					XiaoQiang xq=game.xq_challenge.getXiaoQiang(index[0], index[1]);
 					if(game.gameControler.isCrash(image,xq.getImage())){
 						shooted[0]=index[0];
 					    shooted[1]=index[1];
 					}
 				}
 				if(index[2]!=-1 && index[3]!=-1){
 					XiaoQiang xq=game.xq_challenge.getXiaoQiang(index[2], index[3]);
 					if(game.gameControler.isCrash(image,xq.getImage())){
 						if(shooted[0]==-1 && shooted[1]==-1){
 							shooted[0]=index[2];
 							shooted[1]=index[3];
 						}
 						else{
 							shooted[2]=index[2];
 							shooted[3]=index[3];
 						}
 					}
 				}
 				if((shooted[0]!=-1 && shooted[1]!=-1)||(shooted[2]!=-1&&shooted[3]!=-1)){
 					handler.removeCallbacks(ShootOut.this);				
 					isCompleted=true;
 					game.shootTimes++;
 					onShoot(shooted,image);
 					game.layout.removeView(image);
 				}
 			 }
 		 }
 		 
 		@SuppressWarnings("deprecation")
		@Override
 		public void run() {
 			handler.postDelayed(ShootOut.this, spaceTime);
 			if(param.y+bitmap.getHeight() <= 0){				
 				handler.removeCallbacks(ShootOut.this);
 				game.layout.removeView(image);
 				isCompleted=true;
 			}else{
 				LayoutParams cache=param;
 				param.y=cache.y-space;
 				image.setLayoutParams(param);
 				onImageMove();
 			}
 			
 		}
 	 }
 	  
 	  abstract class WaveShoot extends ShootOut{
 		 
 		 private int[] shoot=new int[]{-1,-1,-1,-1,-1,-1,-1,-1,-1};
 		 private int alpha=-1;
 		 public static final int NO_ALPHA=-1;
 		 private XiaoQiang xq;
 		 private int row=-1;
 		 private boolean canPassThrough=false;
 		 private boolean isFirstShoot=true;
 		 private XQ_queue queue=null;

 		WaveShoot(Context context, int ImageId,int alpha,boolean canPassThrough) {
 			super(context, ImageId);
 			this.alpha=alpha;
 			this.canPassThrough=canPassThrough;
 		}
 		
 		@Override
 		public ShootOut prepare(){
 			super.prepare();
 			if(!game.challenge){
 			for(int i=53;i>=0;i--){
 				if(game.xiaoqiang[i]!=null
 						&&game.xiaoqiang[i].isAlive()){
 					row=i/9;
 					break;
 				}
 			}
 			int j=0;
 			if(row!=-1)
 			for(int i=9*row;i<9*(row+1);i++){
 				if(game.xiaoqiang[i]!=null
 						&&game.xiaoqiang[i].isAlive()){
 					shoot[j++]=i;
 				    xq=game.xiaoqiang[i];
 				}
 			}}
 			else{
 				XQ_queue temp=game.xq_challenge.start;
 				queue=temp;
 				if(temp!=null && temp.xq!=null){
 					for(int i=0;i<9;i++)
 						if(temp.xq[i]!=null && temp.xq[i].isAlive()){
 							shoot[i]=1;
 							xq=temp.xq[i];
 					}
 				}
 			}
 			if(alpha!=NO_ALPHA)
 			image.setAlpha(alpha);
 			isFirstShoot=true;
 			return this;
 		}
 		 
 		@Override
 		protected Bitmap getBitmap(){
 			Bitmap bit=BitmapFactory.decodeResource(game.getResources(), imageId);
 			Bitmap BIT=Bitmap.createScaledBitmap(bit, game.layout.getWidth(), bit.getHeight(), true);
 			return BIT;
 		}
 		
 		@Override
 		protected int getStartXPosition(){
 			return game.layout.getWidth()/2-bitmap.getWidth()/2;
 		}
 		
 		@Override
 		public void onShoot(int[] shootedIndex,ImageView wave){
 			
 		}
 		
 		@Override
 		public WaveShoot copy(){
 			final WaveShoot copy=this;
 			return new WaveShoot(game,imageId,alpha,canPassThrough){

 				@Override
 				public void onShoot(XQ_queue Queue,int[] shootedIndex, ImageView wave,
 						boolean isFirstShoot) {
 					copy.onShoot(Queue,shootedIndex,wave,isFirstShoot);					
 				}
 				
 			};
 		}
 		
 		abstract public void onShoot(XQ_queue Queue,int[] shootedIndex,ImageView wave,boolean isFirstShoot);
 		
 		@Override
 		protected void onImageMove(){						
 			if(xq!=null)
 			if(image.getTop()<xq.get_RIGHT_BOTTOM_Point().y){
 				if(isFirstShoot)
 					game.shootTimes++;
 				if(!game.challenge)
 				onShoot(null,shoot,image,isFirstShoot);
 				else
 					onShoot(queue,shoot,image,isFirstShoot);
 				isFirstShoot=false;
 				if(!canPassThrough){
 					handler.removeCallbacks(WaveShoot.this);				
 					isCompleted=true;
 					game.layout.removeView(image);
 					}
 				else{					
 					for(int i=0;i<9;i++)
 						shoot[i]=-1;
 					if(!game.challenge){
 					if(row>0){
 						row--;
 						int j=0;
 						for(int i=9*row;i<9*(row+1);i++){
 							if(game.xiaoqiang[i]!=null
 									&&game.xiaoqiang[i].isAlive()){
 								shoot[j++]=i;
 							    xq=game.xiaoqiang[i];
 							}
 						}
 					}
 					else
 						xq=null;
 					}
 					else{
 						if(queue!=null && queue.next!=null)
 						{
 							queue=queue.next;
 						XQ_queue temp=queue;
 						if(temp.xq!=null)
 							for(int i=0;i<9;i++)
 								if(temp.xq[i]!=null && temp.xq[i].isAlive()){
 									shoot[i]=1;
 									xq=temp.xq[i];
 						}
 						}else
 							xq=null;
 							
 					}
 				}
 			}
 			
 		}
 	 }
 	  
 	 class BottleAnimation implements Runnable{
 		 private boolean isPlaying=false;
 		 private Handler handler=new Handler();
 		 private int i=0;
 		 private GamePlayActivity game;
 		 
 		BottleAnimation(Context context){
 			game=(GamePlayActivity)context;
 		}
 		
 		public void start(){
 			if(!isPlaying){
 				i=0;
 				handler.post(this);
 				isPlaying=true;
 			}
 		}
 		
 		public void pause(){
 			if(isPlaying){
 				handler.removeCallbacks(this);
 			}
 		}
 		
 		public void Continue(){
 			if(isPlaying){
 				handler.post(this);
 			}
 		}
 		
 		public void stop(){
 			if(isPlaying){
 				isPlaying=false;
 				handler.removeCallbacks(this);
 				game.bottleView.setImageResource(R.drawable.bottle);
 			}
 		}

		@Override
		public void run() {
			handler.postDelayed(this, 200);
			switch(i){			
			case 0:
				game.bottleView.setImageResource(R.drawable.bottle_s1);
				break;
			case 1:
				game.bottleView.setImageResource(R.drawable.bottle_s2);
				break;
			case 2:
				game.bottleView.setImageResource(R.drawable.bottle_s3);
				break;
			case 3:
				game.bottleView.setImageResource(R.drawable.bottle_s4);
				break;
			case 4:
				game.bottleView.setImageResource(R.drawable.bottle_s5);
				break;
			}
			i++;			
			if(i>4){
				i=0;
				stop();
			}
				
		}
 		 
 		 
 	 }

