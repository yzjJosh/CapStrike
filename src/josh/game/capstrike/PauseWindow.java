package josh.game.capstrike;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

public class PauseWindow extends Dialog{
	private ImageButton Continue;
	private ImageButton restart;
	private ImageButton return_main_menu;
	private ImageButton exit;
	private View.OnClickListener  listener_click_continue;
	private View.OnClickListener  listener_click_return_main_menu;
	private View.OnClickListener  listener_click_restart;
	private View.OnClickListener  listener_click_exit;
	private Context context;

	public PauseWindow(Context context) {
		super(context,R.style.theme_pause);
		this.context=context;
		setContentView(R.layout.layout_pause);
		Continue=(ImageButton)findViewById(R.id.pause_continue);
		restart=(ImageButton)findViewById(R.id.pause_restart);
		return_main_menu=(ImageButton)findViewById(R.id.pause_return_main_menu);
		exit=(ImageButton)findViewById(R.id.pause_exit);
		Continue.setOnTouchListener(listener_touch_continue);
		restart.setOnTouchListener(listener_touch_restart);
		return_main_menu.setOnTouchListener(listener_return_main_menu);
		exit.setOnTouchListener(listener_touch_exit);
	}
	
	public PauseWindow set_Restart_OnClickListener(View.OnClickListener listener){
		this.listener_click_restart=listener;
		return this;
	}
	
	public PauseWindow set_Continue_OnClickListener(View.OnClickListener listener){
		this.listener_click_continue=listener;
		return this;
	}
	
	public PauseWindow set_Return_main_menu_OnClickListener(View.OnClickListener listener){
		this.listener_click_return_main_menu=listener;
		return this;
	}
	
	public PauseWindow set_Exit_OnClickListener(View.OnClickListener listener){
		this.listener_click_exit=listener;
		return this;
	}
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		((GamePlayActivity)context).gameControler.Continue();
	}
	
	@Override
	public void show(){
		super.show();		
			Continue.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(listener_click_continue!=null)
					listener_click_continue.onClick(Continue);	
					PauseWindow.this.dismiss();	   
				}
			});
			
              restart.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(listener_click_restart!=null)
					listener_click_restart.onClick(restart);
					PauseWindow.this.dismiss();
				}
			});
              
              return_main_menu.setOnClickListener(new View.OnClickListener() {
  				
  				@Override
  				public void onClick(View v) {
  					if(listener_click_return_main_menu!=null)
  					listener_click_return_main_menu.onClick(return_main_menu);
  					PauseWindow.this.dismiss();
  				}
  			});
              
              exit.setOnClickListener(new View.OnClickListener() {
  				
  				@Override
  				public void onClick(View v) {
  					if(listener_click_exit!=null)
  					listener_click_exit.onClick(exit);
  					PauseWindow.this.dismiss();
  				}
  			});
              
              
	}
	
	  private OnTouchListener listener_touch_continue=new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN){
				Continue.setImageResource(R.drawable.pausemenu_continue_press);
				Continue.setBackgroundResource(R.drawable.background1);
				}
				else{
					Continue.setImageResource(R.drawable.pausemenu_continue);
					Continue.setBackgroundResource(R.drawable.background0);
				}
				return false;
			}
	    	
	    };
	    
	    private OnTouchListener listener_touch_restart=new OnTouchListener(){

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction()==MotionEvent.ACTION_DOWN){
				restart.setImageResource(R.drawable.pausemenu_restart_press);
				restart.setBackgroundResource(R.drawable.background1);
			}
			else{
				restart.setImageResource(R.drawable.pausemenu_restart);
				restart.setBackgroundResource(R.drawable.background0);
			}
			return false;
		}
	    	
	    };
	    
	   private OnTouchListener listener_return_main_menu=new OnTouchListener(){

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction()==MotionEvent.ACTION_DOWN){
				return_main_menu.setImageResource(R.drawable.pausemenu_return_main_menu_press);
				return_main_menu.setBackgroundResource(R.drawable.background1);
			}
			else{
				return_main_menu.setImageResource(R.drawable.pausemenu_return_main_menu);
				return_main_menu.setBackgroundResource(R.drawable.background0);
			}
			return false;
		}
	    	
	    };
	    
	   private OnTouchListener listener_touch_exit=new OnTouchListener(){

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction()==MotionEvent.ACTION_DOWN){
			exit.setImageResource(R.drawable.pausemenu_exit_press);
			exit.setBackgroundResource(R.drawable.background1);
			}
			else{
				exit.setImageResource(R.drawable.pausemenu_exit);
				exit.setBackgroundResource(R.drawable.background0);
			}
			return false;
		}
	    	
	    };

}
