package josh.game.capstrike;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class winWindow extends Dialog{
	private TextView xq_normal_num;
	private TextView xq_magic_num;
	private TextView xq_rare_num;
	private TextView xq_unique_num;
	private TextView hit_rate;
	private TextView timeText;
	private TextView scoreText;
	private TextView localHighest;
	private ImageView new_record;
	private ImageButton to_menu;
	private ImageButton restart;
	private ImageButton next_stage;
	private ImageButton exit;
	private View.OnClickListener  listener_click_to_menu;
	private View.OnClickListener  listener_click_next_stage;
	private View.OnClickListener  listener_click_restart;
	private View.OnClickListener  listener_click_exit;
	private boolean isNewRecord=false;
	private Animation anim_record;
	private int score;
	private int scoreCount;
	private int bestScore;
	private static SoundPool sound;
	private static int sound_win;
	private Handler handler=new Handler();
	private GamePlayActivity game;

	public winWindow(Context context) {
		super(context,R.style.Dialog_Fullscreen);
		game=(GamePlayActivity)context;
		setContentView(R.layout.layout_win);
		xq_normal_num=(TextView)findViewById(R.id.win_xq_normal_num);
		xq_magic_num=(TextView)findViewById(R.id.win_xq_magic_num);
		xq_rare_num=(TextView)findViewById(R.id.win_xq_rare_num);
		xq_unique_num=(TextView)findViewById(R.id.win_xq_unique_num);
		hit_rate=(TextView)findViewById(R.id.win_hit_rate);
		timeText=(TextView)findViewById(R.id.win_time);
		localHighest=(TextView)findViewById(R.id.local_highest);
		scoreText=(TextView)findViewById(R.id.win_score);
		new_record=(ImageView)findViewById(R.id.win_new_record);
		to_menu=(ImageButton)findViewById(R.id.win_to_menu);
		restart=(ImageButton)findViewById(R.id.win_restart);
		next_stage=(ImageButton)findViewById(R.id.win_next_stage);
		exit=(ImageButton)findViewById(R.id.win_exit);
		to_menu.setOnTouchListener(listener_touch);
		restart.setOnTouchListener(listener_touch);
		next_stage.setOnTouchListener(listener_touch);
		exit.setOnTouchListener(listener_touch);
		int CurrentLevel=((GamePlayActivity)context).stageLevel;
		if(CurrentLevel==GameData.getStageNum())
			next_stage.setVisibility(View.GONE);
		xq_normal_num.setText("×"+getXiaoQiangNum(((GamePlayActivity)context).xiaoQiangMatrix,XiaoQiang.NORMAL));
		xq_magic_num.setText("×"+getXiaoQiangNum(((GamePlayActivity)context).xiaoQiangMatrix,XiaoQiang.MAGIC));
		xq_rare_num.setText("×"+getXiaoQiangNum(((GamePlayActivity)context).xiaoQiangMatrix,XiaoQiang.RARE));
		xq_unique_num.setText("×"+getXiaoQiangNum(((GamePlayActivity)context).xiaoQiangMatrix,XiaoQiang.UNIQUE));
		timeText.setText("用时:"+timeFormat(((GamePlayActivity)context).timeSpent));			
		anim_record=AnimationUtils.loadAnimation(context,R.drawable.anim_new_record);	
		bestScore=Database.readScore(CurrentLevel, context);		
		if(bestScore==-1)
			bestScore=0;
		localHighest.setText("最高分:"+bestScore);
	}
	
	@Override
	public void onBackPressed(){
	}
	
	@Override
	public void show(){
		super.show();			
			to_menu.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(listener_click_to_menu!=null)
						listener_click_to_menu.onClick(to_menu);	
					winWindow.this.dismiss();
				}
			});
			
              restart.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(listener_click_restart!=null)
					listener_click_restart.onClick(restart);
					winWindow.this.dismiss();
				}
			});
              
              next_stage.setOnClickListener(new View.OnClickListener() {
  				
  				@Override
  				public void onClick(View v) {
  					if(listener_click_next_stage!=null)
  					listener_click_next_stage.onClick(next_stage);
  					winWindow.this.dismiss();
  				}
  			});    
              
              exit.setOnClickListener(new View.OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					if(listener_click_exit!=null)
    					listener_click_exit.onClick(exit);
    					winWindow.this.dismiss();
    				}
    			});    
              scoreCount=0;  
              handler.post(r);
              sound.play(sound_win, 1, 1, 1, 0, 1);
	}
	
	private Runnable r = new Runnable(){
	  		@Override
	  		public void run() {
	  			score=game.score;
	  			if(score>bestScore) isNewRecord=true;
	  			scoreText.setText("得分:"+scoreCount);
	  			scoreCount+=score/39;
	  			handler.postDelayed(r, 100);
	  			if(scoreCount>score){
	  				scoreText.setText("得分:"+score);
	  				if(isNewRecord){	  					
	  					new_record.setVisibility(View.VISIBLE);
	  					new_record.startAnimation(anim_record);
	  				}
	  				handler.removeCallbacks(r);
	  			}
	  		}
	  		  
	  	  };
	  	  
	public static void SoundPoolInit(Context context){
		sound=new SoundPool(10, AudioManager.STREAM_MUSIC,0);
		sound_win=sound.load(context, R.raw.sound_win, 0);
	}
	
	public winWindow set_Restart_OnClickListener(View.OnClickListener listener){
		this.listener_click_restart=listener;
		return this;
	}
	
	public winWindow set_to_menu_OnClickListener(View.OnClickListener listener){
		this.listener_click_to_menu=listener;
		return this;
	}
	
	public winWindow set_next_stage_OnClickListener(View.OnClickListener listener){
		this.listener_click_next_stage=listener;
		return this;
	}
	
	public winWindow set_exit_OnClickListener(View.OnClickListener listener){
		this.listener_click_exit=listener;
		return this;
	}
	
	public winWindow setHitRate(float hitRate){
		hit_rate.setText("命中率:"+((float)(int)(hitRate*10000))/100+"%");
		return this;
	}
	
	
	private OnTouchListener listener_touch=new OnTouchListener(){

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction()==MotionEvent.ACTION_DOWN){			
			v.setBackgroundResource(R.drawable.small_background1);
			}
			else{
				v.setBackgroundResource(R.drawable.small_background0);
			}
			return false;
		}
    	
    };
    
    private String timeFormat(long time){
    	String M,S;
    	long m;
		long s;
    	s=time;
    	m=s/60;
    	s=s-m*60;
    	if(m<10) M="0"+m;
    	else M=""+m;
    	if(s<10) S="0"+s;
    	else S=""+s;
    	return M+":"+S;
    }

    private int getXiaoQiangNum(int[] matrix,int xiaoQiangLevel){
		int result=0;
		switch(xiaoQiangLevel){
		case XiaoQiang.NORMAL:
			for(int i=0;i<matrix.length;i++)
				if(matrix[i]==1) result++;
			return result;
		case XiaoQiang.MAGIC:
			for(int i=0;i<matrix.length;i++)
				if(matrix[i]==2) result++;
			return result;
		case XiaoQiang.RARE:
			for(int i=0;i<matrix.length;i++)
				if(matrix[i]==3) result++;
			return result;
		case XiaoQiang.UNIQUE:
			for(int i=0;i<matrix.length;i++)
				if(matrix[i]==4) result++;
			return result;
		default:return result;
		}
	}
}
