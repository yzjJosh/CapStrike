package josh.game.capstrike;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.AbsoluteLayout.LayoutParams;

@SuppressWarnings("deprecation")
public class XiaoQiangControler {
	private GamePlayActivity game;
	
	public XiaoQiangControler(Context context){
		this.game=(GamePlayActivity) context;
	}
	
	 void XiaoQiangInit(int[] matrix)
	    //用小强矩阵初始化小强
	    {
		        game.xiaoQiangMatrix=matrix;		      	    	    	    	  	    	    
	    	    int Xspace=Size.screenWidth/9;
	    	    int Yspace=Size.XIAOQIANG_HEIGHT;
	    	    int width= Size.XIAOQIANG_WIDTH;		
	    		for(int i=0;i<6;i++){
	    			for(int j=0;j<9;j++){
	    			int index=i*9+j;
	    			if(matrix[index]!=0){
	    			ImageView image=new ImageView(game);   			
	    			game.layout.addView(image); 		
	    			LayoutParams param=(LayoutParams) image.getLayoutParams();
	    			param.x=Xspace*(j)+(Xspace-width)/2;
	    			param.y=Yspace*(i);
	    			image.setLayoutParams(param);
	    			switch(matrix[index]){
	    			case 1:game.xiaoqiang[index]=new XiaoQiang(game,image,XiaoQiang.NORMAL);break;
	    			case 2:game.xiaoqiang[index]=new XiaoQiang(game,image,XiaoQiang.MAGIC);break;
	    			case 3:game.xiaoqiang[index]=new XiaoQiang(game,image,XiaoQiang.RARE);break;
	    			case 4:game.xiaoqiang[index]=new XiaoQiang(game,image,XiaoQiang.UNIQUE);break;
	    			default:break;
	    			}
	    			}
	    		}
	    		}
	     				
	    }
	 
	 void XiaoQiangRun(long duration,int distance,int command,boolean hasLimit)
	    //启动所有小强
	    {
	    	for(int i=0;i<54;i++)
	    		if(game.xiaoqiang[i]!=null)
	    			game.xiaoqiang[i].run(duration,distance,command,hasLimit);	    	
	    }
	 
	  void XiaoQiangStop()
	    //所有小强停止前进
	    {
	    	for(int i=0;i<54;i++)
	    		if(game.xiaoqiang[i]!=null)
	    			game.xiaoqiang[i].stop();
	    }
	 
	  void XiaoQiangClear()
	    //清除所有小强
	    {
	    	for(int i=0;i<54;i++)
	    		if(game.xiaoqiang[i]!=null){
	    			game.xiaoqiang[i].clear();
	    			game.xiaoqiang[i].clearProp();
	    		}
	    }
	  
	  void pauseProps(){
		  for(int i=0;i<54;i++){
			  if(game.xiaoqiang[i]!=null)
				  game.xiaoqiang[i].pauseProp();
		  }
	  }
	  
	  void continueProps(){
		  for(int i=0;i<54;i++){
			  if(game.xiaoqiang[i]!=null)
				  game.xiaoqiang[i].continueProp();
		  }
		  
	  }
	  	
	 
	 int getXiaoQiangAmount(){
    	 int amount=0;
    	 for(int i=0;i<54;i++){
    		 if(game.xiaoqiang[i]!=null&&game.xiaoqiang[i].isAlive())
    			 amount++;
    	 }
    	 return amount;
     }

}


class XQ_challenge implements Runnable{
	public XQ_queue  start;
	public XQ_queue  end;
	public int QueueNum=0;
	private Handler handler=new Handler();
	private GamePlayActivity game;
	private int Duration=120000;
	private int DurationChange=2500;
	private int runTimes=0;
	private long refreshTime=0;
	
	public XQ_challenge(Context context){
		this.game=(GamePlayActivity) context;	
	}
	
	public int getXQNum(){
		int r=0;
		XQ_queue queue=start;
		if(System.currentTimeMillis()-refreshTime<600)
		for(int i=0;i<QueueNum-1;i++){
			if(queue!=null&&queue.xq!=null)
				for(int j=0;j<9;j++)
					if(queue.xq[j]!=null&&queue.xq[j].isAlive())
						r++;
			queue=queue.next;
		}
		else{
			for(int i=0;i<QueueNum;i++){
				if(queue!=null&&queue.xq!=null)
					for(int j=0;j<9;j++)
						if(queue.xq[j]!=null&&queue.xq[j].isAlive())
							r++;
				queue=queue.next;
			}
		}
		return r;
	}
	
	public void stop(){
		handler.removeCallbacks(this);
		XQ_queue queue=start;
		for(int i=0;i<QueueNum;i++){
			if(queue.xq!=null)
				for(int j=0;j<9;j++)
					if(queue.xq[j]!=null)
					queue.xq[j].stop();
			queue=queue.next;
		}
	}
	
	public void clear(){
		stop();
		XQ_queue queue=start;
		for(int i=0;i<QueueNum;i++){
			queue.clear();
			queue=queue.next;
		}
		start=null;
		end=null;
		QueueNum=0;
		Duration=120000;
		runTimes=0;
	}
	
	public void pauseProps(){
		XQ_queue queue=start;
		for(int i=0;i<QueueNum;i++){
			queue.pauseProps();
			queue=queue.next;
		}
		
	}
	
	public void continueProps(){
		XQ_queue queue=start;
		for(int i=0;i<QueueNum;i++){
			queue.continueProps();
			queue=queue.next;
		}
	}
	
	public XQ_queue getQueue(int QueueNum){
		if(QueueNum>this.QueueNum) return null;
		else{
			XQ_queue result=start;
		    for(int i=0;i<QueueNum-1;i++)
				result=result.next;
		    return result;
		}				
	}
	
	public XiaoQiang getXiaoQiang(int Queue,int num){
		XQ_queue queue=getQueue(Queue);
		if(queue==null) return null;
		else{
			if(queue.xq==null) return null;
			else
				return queue.xq[num];
		}
	}
	
	@SuppressWarnings("deprecation")
	private void refresh(){
		int[] n=new int[9];
		XiaoQiang[] xq=new XiaoQiang[9];
		for(int i=0;i<9;i++){
			int random=(int) (Math.random()*100);
			if(random>=0 && random<30) n[i]=0;
			if(random>=30 && random<55) n[i]=1;
			if(random>=55 && random<75) n[i]=2;
			if(random>=75 && random<90) n[i]=3;
			if(random>=90 && random<100) n[i]=4;
		}
		int Xspace=Size.screenWidth/9;
 	    int Yspace=Size.XIAOQIANG_HEIGHT;
 	    int width= Size.XIAOQIANG_WIDTH;
 	   for(int j=0;j<9;j++){
			if(n[j]!=0){			
			ImageView image=new ImageView(game);   			
			game.layout.addView(image); 		
			LayoutParams param=(LayoutParams) image.getLayoutParams();
			param.x=Xspace*(j)+(Xspace-width)/2;
			param.y=-Yspace;
			image.setLayoutParams(param);
			switch(n[j]){
			case 1:xq[j]=new XiaoQiang(game,image,XiaoQiang.NORMAL);break;
			case 2:xq[j]=new XiaoQiang(game,image,XiaoQiang.MAGIC);break;
			case 3:xq[j]=new XiaoQiang(game,image,XiaoQiang.RARE);break;
			case 4:xq[j]=new XiaoQiang(game,image,XiaoQiang.UNIQUE);break;
			default:break;
			}}
			}
 	   if(start==null){
 		   start=new XQ_queue();
 		   start.xq=xq;
 		   end=start;
 	   }
 	   else{ 		   
 		 end.next=new XQ_queue();
 		 end=end.next;
 		 end.xq=xq;
 	   }
 	  QueueNum++;
 	 for(int j=0;j<9;j++){
 		 if(xq[j]!=null)
			end.xq[j].run(Duration,Size.layoutHeight-Size.BOTTLE_HEIGHT,
	    			XiaoQiang.RUNCOMMAND.CONTINUE_IF_ALREADY_SET,
	    			true);
 		 long time=game.timeSpent-game.slowdownTime;
 		 if(time<10 && xq[j]!=null)
 			 end.xq[j].slowDown(0.4f,10- time);
 	 }
 	 refreshTime=System.currentTimeMillis();
 //	 Log.d("QueueNum", QueueNum+"");
	}
	
	private void eraseEmptyStart(){
		while(start!=null){
		boolean erase=true; 
		for(int i=0;i<9;i++){
			if(start.xq[i]!=null && start.xq[i].isAlive()){	
				erase=false;
				break;
			}
		}
		if(erase)
		{
			start=start.next;
			QueueNum--;
			if(start==null) end=null;
		}
		else
			break;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void init(){
		for(int h=0;h<6;h++){
		if(h==0) {
			end=new XQ_queue();
			start=end;
		}
		else{
			end.next=new XQ_queue();
			end=end.next;
		}
		int[] n=new int[9];
		XiaoQiang[] xq=new XiaoQiang[9];
		for(int i=0;i<9;i++){
			int random=(int) (Math.random()*100);
			if(random>=0 && random<30) n[i]=0;
			if(random>=30 && random<55) n[i]=1;
			if(random>=55 && random<75) n[i]=2;
			if(random>=75 && random<90) n[i]=3;
			if(random>=90 && random<100) n[i]=4;
		}
		int Xspace=Size.screenWidth/9;
 	    int Yspace=Size.XIAOQIANG_HEIGHT;
 	    int width= Size.XIAOQIANG_WIDTH;
 	   for(int j=0;j<9;j++){
			if(n[j]!=0){
			ImageView image=new ImageView(game);   			
			game.layout.addView(image); 		
			LayoutParams param=(LayoutParams) image.getLayoutParams();
			param.x=Xspace*(j)+(Xspace-width)/2;
			param.y=Yspace*(5-h);
			image.setLayoutParams(param);
			switch(n[j]){
			case 1:xq[j]=new XiaoQiang(game,image,XiaoQiang.NORMAL);break;
			case 2:xq[j]=new XiaoQiang(game,image,XiaoQiang.MAGIC);break;
			case 3:xq[j]=new XiaoQiang(game,image,XiaoQiang.RARE);break;
			case 4:xq[j]=new XiaoQiang(game,image,XiaoQiang.UNIQUE);break;
			default:break;
			}}			
			}
 	     end.xq=xq;
		}
		QueueNum=6;
	}
	
	public void start(){
		XQ_queue queue=start;
		for(int i=0;i<QueueNum;i++){
			if(queue.xq!=null)
				for(int j=0;j<9;j++)
					if(queue.xq[j]!=null)
					queue.xq[j].run(Duration,Size.layoutHeight-Size.BOTTLE_HEIGHT,
			    			XiaoQiang.RUNCOMMAND.CONTINUE_IF_ALREADY_SET,
			    			true);
			queue=queue.next;
		}
		handler.post(this);
	}
	
	@Override
	public void run() {
		runTimes++;
		handler.postDelayed(this, 20);
		eraseEmptyStart();
		if(start==null)
			refresh();
		else{
		int startplace=0; 
		for(int i=0;i<9;i++){
			if(start.xq[i]!=null && start.xq[i].isAlive()){
				startplace=(int) start.xq[i].get_LEFT_TOP_Point().y;
				break;
			}
		}
		int place=0;
		for(int i=0;i<9;i++)
			if(end.xq[i]!=null&&end.xq[i].isAlive()){
				place=(int) end.xq[i].get_LEFT_TOP_Point().y;
				break;
			}
		if(startplace-(QueueNum-1)*Size.XIAOQIANG_HEIGHT>=0 && place>=0
				&& game.timeSpent-game.frozenTime>10)
			refresh();
		}
		if(runTimes>=501){
			runTimes=0;
			if(game.timeSpent-game.slowdownTime>11 && Duration>5000){
			Duration=Duration-DurationChange;
			XQ_queue queue=start;
			for(int i=0;i<QueueNum;i++){				
				if(queue.xq!=null)
					for(int j=0;j<9;j++)
						if(queue.xq[j]!=null)
							queue.xq[j].changeSpeed(Duration);
				queue=queue.next;
			}}
		}
	}
}

class XQ_queue{
	XiaoQiang[] xq;
	XQ_queue next;
	public XQ_queue(){
		next=null;
	}
	public void clear(){
		if(xq!=null)
			for(int i=0;i<9;i++)
				if(xq[i]!=null){
					xq[i].clear();
					xq[i].clearProp();
				}
	}
	void pauseProps(){
		if(xq!=null)
		  for(int i=0;i<9;i++){
			  if(xq[i]!=null)
				  xq[i].pauseProp();
		  }
	  }
	  
	  void continueProps(){
		  if(xq!=null)
		  for(int i=0;i<9;i++){
			  if(xq[i]!=null)
				  xq[i].continueProp();
		  }
		  
	  }
}
