package josh.game.capstrike;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class chooseStage extends Activity {
	private LinearLayout[] stage=new LinearLayout[16];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_stage);
        StageInit();
        SetAllStages();
    }  
    
    @Override
    public void onStop(){
 	   super.onStop();
 	   this.finish();
    }
    
    @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) 
	    //按下返回键的响应
	    {
	            if(keyCode == KeyEvent.KEYCODE_BACK ){   
	            	Intent intent=new Intent(this,CapStrikeMenu.class);
	            	startActivity(intent);
	            }
	            return super.onKeyDown(keyCode, event);
	        }
    
    private void StageInit(){
    	stage[0]=(LinearLayout)findViewById(R.id.stage1);
    	stage[1]=(LinearLayout)findViewById(R.id.stage2);
    	stage[2]=(LinearLayout)findViewById(R.id.stage3);
    	stage[3]=(LinearLayout)findViewById(R.id.stage4);
    	stage[4]=(LinearLayout)findViewById(R.id.stage5);
    	stage[5]=(LinearLayout)findViewById(R.id.stage6);
    	stage[6]=(LinearLayout)findViewById(R.id.stage7);
    	stage[7]=(LinearLayout)findViewById(R.id.stage8);
    	stage[8]=(LinearLayout)findViewById(R.id.stage9);
    	stage[9]=(LinearLayout)findViewById(R.id.stage10);
    	stage[10]=(LinearLayout)findViewById(R.id.stage11);
    	stage[11]=(LinearLayout)findViewById(R.id.stage12);
    	stage[12]=(LinearLayout)findViewById(R.id.stage13);
    	stage[13]=(LinearLayout)findViewById(R.id.stage14);
    	stage[14]=(LinearLayout)findViewById(R.id.stage15);
    	stage[15]=(LinearLayout)findViewById(R.id.stage16);
    }
    
    private void setStage(final int stageLevel,int score,boolean isLatest){
    	if(score!=-1||isLatest){
    	TextView StageLevel=new TextView(this);
    	TextView Score=new TextView(this);
    	StageLevel.setText(stageLevel+"");
    	StageLevel.setTextSize(25);
    	StageLevel.setTextColor(Color.WHITE);
    	if(!isLatest)
    	Score.setText("最高分:"+score);
    	else
    	   Score.setText("未完成");
    	Score.setTextSize(10);
    	Score.setTextColor(Color.WHITE);    
    	LinearLayout holder1=new LinearLayout(this);
    	holder1.setOrientation(LinearLayout.HORIZONTAL);
    	holder1.setGravity(Gravity.CENTER);
    	LinearLayout holder2=new LinearLayout(this);
    	holder2.setOrientation(LinearLayout.HORIZONTAL);
    	holder2.setGravity(Gravity.CENTER);   	
    	stage[stageLevel-1].addView(holder1);
    	holder1.addView(StageLevel);
    	stage[stageLevel-1].addView(holder2);
    	holder2.addView(Score);    	
		stage[stageLevel-1].setBackgroundResource(R.drawable.stage_button);
    	stage[stageLevel-1].setClickable(true);
    	stage[stageLevel-1].setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(chooseStage.this,GamePlayActivity.class);
				intent.putExtra("stageLevel", stageLevel);
				startActivity(intent);
			}
    		
    	});
    }else{
    	ImageView lock=new ImageView(this);
    	lock.setImageResource(R.drawable.lock);
    	stage[stageLevel-1].addView(lock);
    }
    }
    
   
    private void SetAllStages(){
    	int i=0;
    	for(i=0;i<16;i++){
    		int score=Database.readScore(i+1, this);
    		if(score!=-1)
    			setStage(i+1,score,false);
    		else
    		{
    			setStage(i+1,score,true);
    			i++;
    			break;
    		}
    	}
    	for(;i<16;i++){
    		setStage(i+1,Database.readScore(i+1, this),false);
    	}
    }
    
}
