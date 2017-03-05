package josh.game.capstrike;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

public class loseWindow extends Dialog{
	
	private ImageButton to_menu;
	private ImageButton restart;
	private ImageButton next_stage;
	private ImageButton exit;
	private View.OnClickListener  listener_click_to_menu;
	private View.OnClickListener  listener_click_restart;
	private View.OnClickListener  listener_click_next_stage;
	private View.OnClickListener  listener_click_exit;
	private static SoundPool sound;
	private static int sound_lost;
	private boolean hasCompleted;
	
	public loseWindow(Context context) {
		
	   super(context,R.style.Dialog_Fullscreen);
	   setContentView(R.layout.layout_lose);
	   to_menu=(ImageButton)findViewById(R.id.lose_to_menu);
	   restart=(ImageButton)findViewById(R.id.lose_restart);
	   next_stage=(ImageButton)findViewById(R.id.lose_next_stage);
	   exit=(ImageButton)findViewById(R.id.lose_exit);
	   to_menu.setOnTouchListener(listener_touch);
	   restart.setOnTouchListener(listener_touch);
	   next_stage.setOnTouchListener(listener_touch);
	   exit.setOnTouchListener(listener_touch);
	   if(Database.readScore(((GamePlayActivity)context).stageLevel, context)==-1)
	   hasCompleted=false;
	   else
		  hasCompleted=true; 
	   if(hasCompleted&&((GamePlayActivity)context).stageLevel!=GameData.getStageNum()) 
		   next_stage.setVisibility(View.VISIBLE);
	}
	
	public static void SoundPoolInit(Context context){
		sound=new SoundPool(10, AudioManager.STREAM_MUSIC,0);
		sound_lost=sound.load(context, R.raw.sound_lost, 0);
	}
	
	public loseWindow set_to_menu_OnClickListener(View.OnClickListener listener){
		this.listener_click_to_menu=listener;
		return this;
	}
		
	public loseWindow set_exit_OnClickListener(View.OnClickListener listener){
		this.listener_click_exit=listener;
		return this;
	}
	
	public loseWindow set_Restart_OnClickListener(View.OnClickListener listener){
		this.listener_click_restart=listener;
		return this;
	}
	
	public loseWindow set_next_stage_OnClickListener(View.OnClickListener listener){
		this.listener_click_next_stage=listener;
		return this;
	}
	
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
					loseWindow.this.dismiss();
				}
			});
			
              restart.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(listener_click_restart!=null)
					listener_click_restart.onClick(restart);
					loseWindow.this.dismiss();
				}
			});
              
              next_stage.setOnClickListener(new View.OnClickListener() {
  				
  				@Override
  				public void onClick(View v) {
  					if(listener_click_next_stage!=null)
  						listener_click_next_stage.onClick(next_stage);
  					loseWindow.this.dismiss();
  				}
  			});
                           
              exit.setOnClickListener(new View.OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					if(listener_click_exit!=null)
    					listener_click_exit.onClick(exit);
    					loseWindow.this.dismiss();
    				}
    			});                 
              sound.play(sound_lost, 1, 1, 1, 0, 1);
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

}
