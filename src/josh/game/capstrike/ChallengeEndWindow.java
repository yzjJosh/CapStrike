package josh.game.capstrike;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChallengeEndWindow extends Dialog{
	private TextView xq_normal_num;
	private TextView xq_magic_num;
	private TextView xq_rare_num;
	private TextView xq_unique_num;
	private TextView hit_rate;
	private TextView timeText;
	private TextView KillNum;
	private TextView scoreText;
	private TextView[] score;
	private TextView[] name;	
	private ImageView new_record;
	private ImageButton to_menu;
	private ImageButton restart;
	private ImageButton exit;
	private View.OnClickListener  listener_click_to_menu;
	private View.OnClickListener  listener_click_restart;
	private View.OnClickListener  listener_click_exit;
	private boolean isNewRecord=false;
	private Animation anim_record;
	private int bestScore;
	private static SoundPool sound;
	private static int sound_win;
	private GamePlayActivity game;

	public ChallengeEndWindow(Context context) {
		super(context,R.style.Dialog_Fullscreen);
		game=(GamePlayActivity)context;
		setContentView(R.layout.challenge_end);
		xq_normal_num=(TextView)findViewById(R.id.challenge_xq_normal_num);
		xq_magic_num=(TextView)findViewById(R.id.challenge_xq_magic_num);
		xq_rare_num=(TextView)findViewById(R.id.challenge_xq_rare_num);
		xq_unique_num=(TextView)findViewById(R.id.challenge_xq_unique_num);
		hit_rate=(TextView)findViewById(R.id.challenge_hit_rate);
		timeText=(TextView)findViewById(R.id.challenge_time);		
		scoreText=(TextView)findViewById(R.id.challenge_score);
		KillNum=(TextView)findViewById(R.id.challenge_kill_num);
		name=new TextView[3];
		score=new TextView[3];
		name[0]=(TextView)findViewById(R.id.NO_1_name);
		score[0]=(TextView)findViewById(R.id.NO_1_score);
		name[1]=(TextView)findViewById(R.id.NO_2_name);
		score[1]=(TextView)findViewById(R.id.NO_2_score);
		name[2]=(TextView)findViewById(R.id.NO_3_name);
		score[2]=(TextView)findViewById(R.id.NO_3_score);
		new_record=(ImageView)findViewById(R.id.challenge_new_record);
		to_menu=(ImageButton)findViewById(R.id.challenge_to_menu);
		restart=(ImageButton)findViewById(R.id.challenge_restart);
		exit=(ImageButton)findViewById(R.id.challenge_exit);
		to_menu.setOnTouchListener(listener_touch);
		restart.setOnTouchListener(listener_touch);
		exit.setOnTouchListener(listener_touch);
		KillNum.setText(game.normalXQKill+game.magicXQKill+game.rareXQKill+game.unniqueXQKill+"");
		xq_normal_num.setText("×"+game.normalXQKill);
		xq_magic_num.setText("×"+game.magicXQKill);
		xq_rare_num.setText("×"+game.rareXQKill);
		xq_unique_num.setText("×"+game.unniqueXQKill);
		timeText.setText("用时:"+timeFormat(((GamePlayActivity)context).timeSpent));			
		anim_record=AnimationUtils.loadAnimation(context,R.drawable.anim_new_record);
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				scoreText.setText("得分:"+game.score);
				
			}
			
		}, 500);		
		bestScore=Database.readChallengeScore(3, context);		
		if(bestScore==-1)
			bestScore=0;
		if(game.score>bestScore)
			isNewRecord=true;
		
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
					ChallengeEndWindow.this.dismiss();
				}
			});
			
              restart.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(listener_click_restart!=null)
					listener_click_restart.onClick(restart);
					ChallengeEndWindow.this.dismiss();
				}
			});
                          
              
              exit.setOnClickListener(new View.OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					if(listener_click_exit!=null)
    					listener_click_exit.onClick(exit);
    					ChallengeEndWindow.this.dismiss();
    				}
    			});
              
              if(isNewRecord){
            	  final EditText text=new EditText(game);
            	  new AlertDialog.Builder(game)
         		 .setTitle("新纪录！")
         		 .setIcon(android.R.drawable.ic_dialog_info)
         		 .setMessage("恭喜你打破记录，请输入姓名。")
         		 .setView(text)
         		 .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                 		public void onClick(DialogInterface dialog, int whichButton) {
                 			String n=text.getText().toString();
                 			if(n.equals(new String()))
                 				n="匿名玩家";
                 			Database.saveChallengeScore(n, game.score, game); 
                 			 for(int i=0;i<3;i++){
                       			int Score=Database.readChallengeScore(i+1, game);
                       			String Name=Database.readChallengeName(i+1, game);
                       			if(Score==-1)
                       				score[i].setText("---");
                       			else
                       				score[i].setText(""+Score);
                       			if(Name==null)
                       				name[i].setText("---");
                       			else
                       				name[i].setText(Name);
                       		}
                 		}})
         		 .show();           	             		
					new_record.setVisibility(View.VISIBLE);
					new_record.startAnimation(anim_record);
			  }
              for(int i=0;i<3;i++){
      			int Score=Database.readChallengeScore(i+1, game);
      			String Name=Database.readChallengeName(i+1, game);
      			if(Score==-1)
      				score[i].setText("---");
      			else
      				score[i].setText(""+Score);
      			if(Name==null)
      				name[i].setText("---");
      			else
      				name[i].setText(Name);
      		}
              sound.play(sound_win, 1, 1, 1, 0, 1);
	}
	
	  	  
	public static void SoundPoolInit(Context context){
		sound=new SoundPool(10, AudioManager.STREAM_MUSIC,0);
		sound_win=sound.load(context, R.raw.sound_win, 0);
	}
	
	public ChallengeEndWindow set_Restart_OnClickListener(View.OnClickListener listener){
		this.listener_click_restart=listener;
		return this;
	}
	
	public ChallengeEndWindow set_to_menu_OnClickListener(View.OnClickListener listener){
		this.listener_click_to_menu=listener;
		return this;
	}
		
	public ChallengeEndWindow set_exit_OnClickListener(View.OnClickListener listener){
		this.listener_click_exit=listener;
		return this;
	}
	
	public ChallengeEndWindow setHitRate(float hitRate){
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

}
