package josh.game.capstrike;


import java.io.IOException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

@SuppressWarnings("deprecation")      
public class CapStrikeMenu extends Activity {
	private ImageButton start;
	private ImageButton choose_stage;
	private ImageButton challengeMode;
	private ImageButton about;
	private ImageButton exit;	
	private ImageView bottle;
	private ImageView cap;
	private ImageView xiaoqiang;	
	private MediaPlayer player;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_strike);
        init();
    }
    
    @Override
    public void onResume(){
    	super.onResume();
        playBackgroundMusic();
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	player.stop();
    }
    
   @Override
   public void onStop(){
	   super.onStop();
	   this.finish();
   }
    
    
    private void init(){
    	start=(ImageButton)findViewById(R.id.start);
    	choose_stage=(ImageButton)findViewById(R.id.choose_stage);
    	challengeMode=(ImageButton)findViewById(R.id.challenge_mode);
    	about=(ImageButton)findViewById(R.id.about);
    	exit=(ImageButton)findViewById(R.id.exit);
    	bottle=(ImageView)findViewById(R.id.bottle_menu); 
    	cap=(ImageView)findViewById(R.id.cap_menu);
    	xiaoqiang=(ImageView)findViewById(R.id.running_xiaoqiang_menu);
    	setPosition();
    	bottle.setBackgroundResource(R.drawable.anim_bottle);
    	xiaoqiang.setBackgroundResource(R.drawable.anim_xiaoqiang0);
    	final AnimationDrawable anim_bottle=(AnimationDrawable)bottle.getBackground();
    	final AnimationDrawable anim_xiaoqiang0=(AnimationDrawable)xiaoqiang.getBackground();
    	final Animation anim_cap=AnimationUtils.loadAnimation(this,R.drawable.anim_cap);
    	final RotateAnimation anim_xiaoqiang1=new RotateAnimation(0,-360, Animation.RELATIVE_TO_PARENT, -0.3f, Animation.RELATIVE_TO_PARENT, 0);
    	anim_xiaoqiang1.setDuration(10000);
    	anim_xiaoqiang1.setInterpolator(new LinearInterpolator());  
    	anim_xiaoqiang1.setRepeatCount(Animation.INFINITE);
    	new Handler().postDelayed(new Runnable(){
    		@Override
    		public void run() {    			
    			anim_bottle.start();
    			anim_xiaoqiang0.start();
    			cap.startAnimation(anim_cap);
    			xiaoqiang.startAnimation(anim_xiaoqiang1);
    		}}, 400);	
    	start.setOnClickListener(listener_click_start);
    	start.setOnTouchListener(listener_touch_start);
    	choose_stage.setOnClickListener(listener_click_choose_stage);
    	choose_stage.setOnTouchListener(listener_touch_choose_stage);
    	challengeMode.setOnClickListener(listener_click_challenge_mode);
    	challengeMode.setOnTouchListener(listener_touch_challenge_mode);
    	about.setOnClickListener(listener_click_about);
    	about.setOnTouchListener(listener_touch_about);
    	exit.setOnClickListener(listener_click_exit);
    	exit.setOnTouchListener(listener_touch_exit);
    }
    
    private OnClickListener listener_click_start=new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			Intent intent=new Intent(CapStrikeMenu.this,GamePlayActivity.class);
			intent.putExtra("stageLevel",Database.getLastStage(CapStrikeMenu.this));
			startActivity(intent);
		}
    	
    };
    
    
   private OnClickListener listener_click_choose_stage=new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			Intent intent=new Intent(CapStrikeMenu.this,chooseStage.class);
			startActivity(intent);			
		}
    	
    };
    
    private OnClickListener listener_click_challenge_mode=new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			Intent intent=new Intent(CapStrikeMenu.this,GamePlayActivity.class);
			intent.putExtra("challenge", true);
			startActivity(intent);			
		}
    	
    };
    
    private OnClickListener listener_click_about=new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			Intent intent=new Intent(CapStrikeMenu.this,aboutGame.class);
			startActivity(intent);		
			
		}
    	
    };
    
    private OnClickListener listener_click_exit=new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			CapStrikeMenu.this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());
		}
    	
    };
    
    private OnTouchListener listener_touch_start=new OnTouchListener(){

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction()==MotionEvent.ACTION_DOWN){
			start.setImageResource(R.drawable.start_press);
			start.setBackgroundResource(R.drawable.background1);
			}
			else{
				start.setImageResource(R.drawable.start);
				start.setBackgroundResource(R.drawable.background0);
			}
			return false;
		}
    	
    };
    
    private OnTouchListener listener_touch_choose_stage=new OnTouchListener(){

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN){
		choose_stage.setImageResource(R.drawable.choose_stage_press);
		choose_stage.setBackgroundResource(R.drawable.background1);
		}
		else{
			choose_stage.setImageResource(R.drawable.choose_stage);
			choose_stage.setBackgroundResource(R.drawable.background0);
		}
		return false;
	}
    	
    };
    
    private OnTouchListener listener_touch_challenge_mode=new OnTouchListener(){

    	@Override
    	public boolean onTouch(View v, MotionEvent event) {
    		if(event.getAction()==MotionEvent.ACTION_DOWN){
    		challengeMode.setImageResource(R.drawable.challengemode_press);
    		challengeMode.setBackgroundResource(R.drawable.background1);
    		}
    		else{
    			challengeMode.setImageResource(R.drawable.challengemode);
    			challengeMode.setBackgroundResource(R.drawable.background0);
    		}
    		return false;
    	}
        	
        };
    
   private OnTouchListener listener_touch_about=new OnTouchListener(){

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN){
		about.setImageResource(R.drawable.about_press);
		about.setBackgroundResource(R.drawable.background1);
		}
		else{
			about.setImageResource(R.drawable.about);
			about.setBackgroundResource(R.drawable.background0);
		}
		return false;
	}
    	
    };
    
   private OnTouchListener listener_touch_exit=new OnTouchListener(){

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN){
		exit.setImageResource(R.drawable.exit_press);
		exit.setBackgroundResource(R.drawable.background1);
		}
		else{
			exit.setImageResource(R.drawable.exit);
			exit.setBackgroundResource(R.drawable.background0);
		}
		return false;
	}
    	
    };
    
   private void playBackgroundMusic(){
	   player = MediaPlayer.create(this, R.raw.music_background);  
	   player.setLooping(true);
	   try {
		player.prepare();
	} catch (IllegalStateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   player.start();
   }
   

   private void setPosition(){
	   LinearLayout menu=(LinearLayout)findViewById(R.id.main_menu);
	   ImageView xiaoqiang1=(ImageView)findViewById(R.id.xiaoqiang_menu);
	   ImageView xiaoqiang2=(ImageView)findViewById(R.id.running_xiaoqiang_menu);
	   Bitmap bitmap_menu=BitmapFactory.decodeResource(getResources(), R.drawable.background0);
	   Bitmap bitmap_xiaoqiang1=BitmapFactory.decodeResource(getResources(), R.drawable.xiaoqiang);
	   Bitmap bitmap_xiaoqiang2=BitmapFactory.decodeResource(getResources(), R.drawable.xiaoqiang_normal_1);
	   int menu_width=bitmap_menu.getWidth();
	   int menu_height=bitmap_menu.getHeight()*4;
	   int xiaoqiang1_width=bitmap_xiaoqiang1.getWidth();
	   int xiaoqiang1_height=bitmap_xiaoqiang1.getHeight();
	   int xiaoqiang2_width=bitmap_xiaoqiang2.getWidth();
	   int xiaoqiang2_height=bitmap_xiaoqiang2.getHeight();
	   LayoutParams menu_param=(LayoutParams) menu.getLayoutParams();
	   LayoutParams xiaoqiang1_param=(LayoutParams) xiaoqiang1.getLayoutParams();
	   LayoutParams xiaoqiang2_param=(LayoutParams) xiaoqiang2.getLayoutParams();
	   menu_param.x=(getWindowManager().getDefaultDisplay().getWidth()-menu_width)/2;
	   menu_param.y=getWindowManager().getDefaultDisplay().getHeight()*45/100-menu_height/2;
	   menu.setLayoutParams(menu_param);
	   xiaoqiang1_param.x=menu_param.x+menu_width+(menu_param.x-xiaoqiang1_width)/2;
	   xiaoqiang1_param.height=menu_param.y+menu_height/2-xiaoqiang1_height;
	   xiaoqiang1.setLayoutParams(xiaoqiang1_param);
	   xiaoqiang2_param.x=xiaoqiang1_param.x+(xiaoqiang1_width-xiaoqiang2_width)/2;
	   xiaoqiang2_param.y=xiaoqiang1_param.y+xiaoqiang2_height;
	   xiaoqiang2.setLayoutParams(xiaoqiang2_param);
   }
   
 


}
