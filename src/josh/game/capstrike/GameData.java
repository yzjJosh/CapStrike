package josh.game.capstrike;

public class GameData 
//游戏数据，包含关卡信息等
{
	//关卡信息
	private final static  GameStage[] stage={
		 new GameStage(
		 new int[]   //1
		 {1,1,1,1,1,1,1,1,1,
		  0,0,0,0,1,0,0,0,0,
		  0,0,0,0,1,0,0,0,0,
		  0,0,0,0,1,0,0,0,0,
		  0,0,0,0,1,0,0,0,0,
		  0,0,0,0,1,0,0,0,0
		 },60000),
		 new GameStage(
		 new int[]  //2
		 {0,0,0,1,1,1,0,0,0,
		  0,0,1,0,1,0,1,0,0,
		  0,1,0,0,1,0,0,1,0,
		  0,1,0,0,1,0,0,1,0,
		  0,0,1,0,1,0,1,0,0,
		  0,0,0,1,1,1,0,0,0
		 },58000),
		 new GameStage(
		 new int[]  //3
		 {1,0,1,0,1,0,1,0,1,
		  0,2,0,2,0,2,0,2,0,
		  0,0,1,0,1,0,1,0,0,
		  0,0,0,2,0,2,0,0,0,
		  0,0,0,0,1,0,0,0,0,
		  0,0,0,0,0,0,0,0,0
		 },56000),
		 new GameStage(
		 new int[]   //4
		 {1,2,1,2,1,2,1,2,1,
		  1,1,1,1,1,1,1,1,1,
		  2,2,2,2,2,2,2,2,2,
		  0,0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,0
		 },54000),
		 new GameStage(
		 new int[]   //5
		 {0,1,1,1,1,1,1,1,0,
		  0,1,2,2,2,2,2,1,0,
		  0,1,2,0,0,0,2,1,0,
		  0,1,2,0,0,0,2,1,0,
		  0,1,2,2,2,2,2,1,0,
		  0,1,1,1,1,1,1,1,0
		 },52000),
		 new GameStage(
		 new int[]   //6
		 {0,0,0,2,2,2,0,0,0,
		  0,0,0,2,2,2,0,0,0,
		  1,1,1,1,3,2,1,1,1,
		  1,1,1,2,3,1,1,1,1,
		  0,0,0,2,2,2,0,0,0,
		  0,0,0,2,2,2,0,0,0
		 },50000),
		 new GameStage(
		 new int[]   //7
		 {0,1,1,0,0,0,2,2,0,
		  0,0,1,1,0,2,2,0,0,
		  0,0,0,3,3,3,0,0,0,
		  0,0,0,3,3,3,0,0,0,
		  0,0,2,2,0,1,1,0,0,
		  0,2,2,0,0,0,1,1,0
		 },48000),
		 new GameStage(
		 new int[]   //8
		 {0,0,3,0,0,0,3,0,0,
		  0,0,3,2,0,2,3,0,0,
		  0,0,3,2,3,2,3,0,0,
		  0,0,3,2,3,2,3,0,0,
		  0,0,3,2,0,2,3,0,0,
		  0,0,3,0,0,0,3,0,0
		 },46000),
		 new GameStage(
		 new int[]   //9
		 {0,0,0,0,2,0,0,0,0,
		  0,0,0,3,3,3,0,0,0,
		  0,0,1,1,1,1,1,0,0,
		  0,2,2,2,2,2,2,2,0,
		  3,3,3,3,3,3,3,3,3,
		  0,0,0,0,0,0,0,0,0
		 },44000),
		 new GameStage(
		 new int[]   //10
		 {3,3,3,0,1,0,3,3,3,
		  3,3,0,1,2,1,0,3,3,
		  3,0,1,2,3,2,1,0,3,
		  3,0,1,2,3,2,1,0,3,
		  3,3,0,1,2,1,0,3,3,
		  3,3,3,0,1,0,3,3,3
		 },42000),
		 new GameStage(
		 new int[]   //11
		 {0,0,3,3,1,3,3,0,0,
		  0,0,3,2,4,2,3,0,0,
		  0,0,1,3,4,3,1,0,0,
		  0,0,3,2,4,2,3,0,0,
		  0,0,3,3,1,3,3,0,0,
		  0,0,0,0,0,0,0,0,0
		 },39000),
		 new GameStage(
		 new int[]   //12
		 {0,0,0,1,1,1,0,0,0,
		  0,4,2,2,2,2,2,4,0,
		  4,4,1,1,1,1,1,4,4,
		  0,4,2,2,2,2,2,4,0,
		  0,0,0,3,3,3,0,0,0,
		  0,0,0,0,3,0,0,0,0
		 },36000),
		 new GameStage(
		 new int[]   //13
		 {0,0,1,0,0,0,1,0,0,
		  4,2,2,2,2,2,2,2,4,
		  0,4,0,0,0,0,0,4,0,
		  3,0,4,0,1,0,4,0,3,
		  3,3,0,4,0,4,0,3,3,
		  3,3,3,0,4,0,3,3,3
		 },33000),
		 new GameStage(
		 new int[]   //14
		 {1,2,3,4,4,4,3,2,1,
		  4,2,0,0,3,0,0,2,4,
		  4,0,2,3,3,3,2,0,4,
		  4,1,3,3,3,3,3,1,4,
		  4,1,1,2,1,2,1,1,4,
		  4,1,1,1,2,1,1,1,4
		 },30000),
		 new GameStage(
		 new int[]   //15
		 {1,1,1,1,1,1,1,1,1,
		  2,2,2,2,2,2,2,2,2,
		  3,3,3,3,3,3,3,3,3,
		  4,4,4,4,4,4,4,4,4,
		  0,0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,0
		 },27000),
		 new GameStage(
		 new int[]   //16
		 {0,0,4,4,0,4,4,0,0,
		  0,4,4,4,4,4,4,4,0,
		  0,4,4,4,4,4,4,4,0,
		  0,0,4,4,4,4,4,0,0,
		  0,0,0,4,4,4,0,0,0,
		  0,0,0,0,4,0,0,0,0
		 },24000)
		 };
	
	
	public static GameStage getStage(int stageLevel)
	//获得关卡信息，没有则返还null
	{
		if(stageLevel-1>=0&&stageLevel-1<stage.length)
		return stage[stageLevel-1];
		else
			return null;
	}
	
	
	public static int getStageNum(){
		return stage.length;
	}
	
   
	
	static class GameStage
	//关卡类
	{
	    int[] matrix;//小强矩阵
	    long duration;//小强跑动的时间（该参数影响其速度）
		
		GameStage(int[] matrix,long duration){
			this.matrix=matrix;
			this.duration=duration;
		}			
		
		public int[] getRandomMatrix(int level){
			int[] result=matrix.clone();
			int amount=(int) (Math.random()*(level+1));
			if(amount>10) amount=10;
			int[] index=new int[amount];
			int rate0=600+level*5;
			int rate1=rate0+100+250/level;
			int rate2=rate1+400+5*level;
			int rate3=rate2+10*level;
			int rate4=rate3+20*level;
			int rate5=rate4+30*level;
			for(int i=0;i<index.length;i++){
				index[i]=(int) (Math.random()*54);
				int random=(int) (Math.random()*rate5);
				if(random>=rate0&&random<rate1)
					result[index[i]]=0;
				if(random>=rate1&&random<rate2)
					result[index[i]]=1;
				if(random>=rate2&&random<rate3)
					result[index[i]]=2;
				if(random>=rate3&&random<rate4)
					result[index[i]]=3;
				if(random>=rate4&&random<rate5)
					result[index[i]]=4;					
			}
			
			return result;
		}
	}
}

