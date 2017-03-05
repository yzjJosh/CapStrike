package josh.game.capstrike;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RelativeLayout;
import android.widget.AbsoluteLayout.LayoutParams;

@SuppressWarnings("deprecation")
class Size{
    static int BULLET_HEIGHT ;   //�ӵ�ͼƬ�ĸ߶� 
    static int BULLET_WIDTH ;	//�ӵ�ͼƬ�Ŀ��
    static int BOTTLE_TOP;   //ƿ���϶˵�����
    static int XIAOQIANG_WIDTH;   //СǿͼƬ�ĸ߶� 
    static int XIAOQIANG_HEIGHT;  //СǿͼƬ�Ŀ��
    static int OFFSET ;   //�ӵ����λ�õ�ƫ����	 	 
    static int BOTTLE_HEIGHT ;   
    static int BOTTLE_WIDTH ;
    static int screenWidth;    //��Ļ�Ŀ��
    static int screenHeight;    //��Ļ�ĸ߶�
    static int bulletTop;	//�ӵ��ĳ�ʼ�϶�����
    static int layoutHeight;
    static int Prop_Height;
    static int Prop_Width;

    static void sizeInit(Context context){
    	GamePlayActivity game=(GamePlayActivity)context;
    	screenWidth=game.getWindowManager().getDefaultDisplay().getWidth();
    	screenHeight=game.getWindowManager().getDefaultDisplay().getHeight();
    	Bitmap xqBit=BitmapFactory.decodeResource(game.getResources(), R.drawable.xiaoqiang_normal_1);
    	XIAOQIANG_WIDTH=xqBit.getWidth();
    	XIAOQIANG_HEIGHT=xqBit.getHeight();
    	Bitmap btBit=BitmapFactory.decodeResource(game.getResources(), R.drawable.bottle);
    	BOTTLE_HEIGHT=btBit.getHeight();
    	BOTTLE_WIDTH=btBit.getWidth();
    	Bitmap bullet=BitmapFactory.decodeResource(game.getResources(), R.drawable.cap3);
    	BULLET_HEIGHT=bullet.getHeight();
    	BULLET_WIDTH=bullet.getWidth();
    	OFFSET=BULLET_WIDTH;
    	Bitmap prop=BitmapFactory.decodeResource(game.getResources(), R.drawable.slow_down);
    	Prop_Width=prop.getWidth();
    	Prop_Height=prop.getHeight();
    }
    

	static void posInit(Context context){
    	GamePlayActivity game=(GamePlayActivity)context;
    	RelativeLayout relativeLayout = (RelativeLayout)game.findViewById(R.id.relativeLayout2); 
    	int bottomHeight = relativeLayout.getHeight();
    	LayoutParams layoutParam =(LayoutParams) game.layout.getLayoutParams();
		layoutParam.height = screenHeight - bottomHeight;
		game.layout.setLayoutParams(layoutParam);
		layoutHeight=screenHeight - bottomHeight;
    	BOTTLE_TOP=screenHeight - bottomHeight-BOTTLE_HEIGHT;
    	bulletTop=BOTTLE_TOP+BULLET_HEIGHT;
    }

}


