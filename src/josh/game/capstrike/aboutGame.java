package josh.game.capstrike;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
  

public class aboutGame extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_game);
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

}
