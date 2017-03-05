package josh.game.capstrike;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database {
	
	public static final String KEY_ROWID = "_id";//id列的关键字
	public static final String KEY_SCORE = "score";	
	public static final String KEY_LAST_STAGE = "laststage";
	public static final String KEY_CHALLENGE_NAME = "name";
	public static final String KEY_CHALLENGE_SCORE = "score";
	public static final int INDEX_ROWID = 0;	
	public static final int INDEX_SCORE = 1;
	public static final int INDEX_LASTSTAGE = 1;
	public static final int INDEX_CHALLENGE_SCORE = 2;
	public static final int INDEX_CHALLENGE_NAME = 1;
	private static final String TAG = "database";
	private static final String DATABASE_NAME = "gameData.db";
	private static final String DATABASE_TABLE = "gamedata";
	private static final String DATABASE_TABLE_LAST_STAGE = "laststage";
	private static final String DATABASE_TABLE_CHALLENGE_SCORE = "challengescore";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = 
		"CREATE TABLE gamedata("
			+" _id INTEGER PRIMARY KEY AUTOINCREMENT,"			
			+" score INTEGER"	
			+" );";
	private static final String DATABASE_CREATE_LAST_STAGE_TABLE = 
		"CREATE TABLE laststage("
			+" _id INTEGER PRIMARY KEY AUTOINCREMENT,"			
			+" laststage INTEGER"	
			+" );";
	private static final String DATABASE_CREATE_LAST_CHALLENGE_SCORE = 
		"CREATE TABLE challengescore("
			+" _id INTEGER PRIMARY KEY AUTOINCREMENT,"		
			+" name TEXT,"
			+" score INTEGER"	
			+" );";
	
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public Database(Context ctx){
		this.context = ctx;
		DBHelper=new DatabaseHelper(context);
		}
		
	private class DatabaseHelper extends SQLiteOpenHelper{
		
		DatabaseHelper(Context ctx){
			super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
			}
		
		@Override
		public void onCreate(SQLiteDatabase db){
			db.execSQL(DATABASE_CREATE);
			db.execSQL(DATABASE_CREATE_LAST_STAGE_TABLE);
			db.execSQL(DATABASE_CREATE_LAST_CHALLENGE_SCORE);
			}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){
			Log.w(TAG, "Upgrading database from version " + oldVersion+ " to "+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS titles");
			onCreate(db);
			}
		}
	
	public Database open() throws SQLException
	/**打开数据库*/
	{
		db = DBHelper.getWritableDatabase();
		return this;
		}
	
	public void close()
	/**关闭数据库*/
	{
		DBHelper.close();
	}
	
	public long insertGamedata(int score)
	{
		ContentValues initialValues = new ContentValues();		
		initialValues.put(KEY_SCORE, score);
		return db.insert(DATABASE_TABLE, null, initialValues);
		}
	
	public boolean deleteGamedata(int rowId)
	{
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
		}
	
	public Cursor getAllGamedata()
	{
		return db.query(DATABASE_TABLE,
				new String[] {KEY_ROWID,KEY_SCORE},
				null,null,null,null,null);
		}
	
	public Cursor getGamedata(int rowId) throws SQLException
	{
		Cursor mCursor =db.query(true, DATABASE_TABLE,
				new String[] {KEY_ROWID,KEY_SCORE,},
				KEY_ROWID + "=" + rowId,
				null,null,null,null,null);
		if (mCursor != null) {
			mCursor.moveToFirst();
			}
		return mCursor;
		}
	
	public boolean updateGamedata(int rowId, int score)
	{
		ContentValues args = new ContentValues();	
		args.put(KEY_SCORE, score);	
		return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
		}	
	
	public boolean updateChallengeScore(int rowId, String name,int score){
		ContentValues args = new ContentValues();	
		args.put(KEY_CHALLENGE_NAME, name);
		args.put(KEY_CHALLENGE_SCORE, score);
		return db.update(DATABASE_TABLE_CHALLENGE_SCORE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public long insertChallengeScore(String name,int score)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_CHALLENGE_NAME, name);
		initialValues.put(KEY_CHALLENGE_SCORE, score);
		return db.insert(DATABASE_TABLE_CHALLENGE_SCORE, null, initialValues);
		}
	
	public Cursor getChallengeScore(int rowId) throws SQLException
	{
		Cursor mCursor =db.query(true, DATABASE_TABLE_CHALLENGE_SCORE,
				new String[] {KEY_ROWID,KEY_CHALLENGE_NAME,KEY_CHALLENGE_SCORE,},
				KEY_ROWID + "=" + rowId,
				null,null,null,null,null);
		if (mCursor != null) {
			mCursor.moveToFirst();
			}
		return mCursor;
		}
	
	public boolean updateLastStageLevel(int lastStageLevel){
		ContentValues args = new ContentValues();	
		args.put(KEY_LAST_STAGE, lastStageLevel);	
		return db.update(DATABASE_TABLE_LAST_STAGE, args, KEY_ROWID + "=" + 1, null) > 0;
	}
	
	public Cursor getLastStageLevel() throws SQLException
	{
		Cursor mCursor =db.query(true, DATABASE_TABLE_LAST_STAGE,
				new String[] {KEY_ROWID,KEY_LAST_STAGE},
				KEY_ROWID + "=" + 1,
				null,null,null,null,null);
		if (mCursor != null) {
			mCursor.moveToFirst();
			}
	return mCursor;
	}
	
	public long insertLastStage(int lastStage)
	{
		ContentValues initialValues = new ContentValues();		
		initialValues.put(KEY_LAST_STAGE, lastStage);
		return db.insert(DATABASE_TABLE_LAST_STAGE, null, initialValues);
		}
	
	public static void setLastStage(int lastStage,Context context){
		Database db = new Database(context);
		db.open();
		Cursor cursor = db.getLastStageLevel();
		if(!cursor.moveToFirst())
			db.insertLastStage(lastStage);
		else
		db.updateLastStageLevel(lastStage);
		db.close();		
	}
	
	public static int getLastStage(Context context){
		int lastStage=1;
		Database db = new Database(context);
		db.open();
		Cursor cursor = db.getLastStageLevel();
		 if(cursor.moveToFirst() == true){		 						
			 String scoreStr = cursor.getString(Database.INDEX_SCORE);	
			 lastStage = Integer.parseInt(scoreStr);
		 }		 
		 db.close();
	   return lastStage;
	}
	
	public static  int readScore(int satgeId,Context context)	 
	 {		
  	 int tempScore = -1;
		 Database db = new Database(context);
		 db.open();
		 Cursor cursor = db.getGamedata(satgeId);			
		 if(cursor.moveToFirst() == true){		 						
			 String scoreStr = cursor.getString(Database.INDEX_SCORE);	
			 tempScore = Integer.parseInt(scoreStr);
		 }		 
		db.close();
		return tempScore;
	 }
	
	
   
   public static void saveScore(int stageId,int score,Context context)     
   {       
		 Database db = new Database(context);
		 db.open();		 		 
		 Cursor cursor = db.getGamedata(stageId);
		 if(cursor.moveToFirst() == false)		 
			db.insertGamedata(score);						 		 
		 if(cursor.moveToFirst() == true){			
			String scoreStr = cursor.getString(Database.INDEX_SCORE);		
			if(score > Integer.parseInt(scoreStr))
			db.updateGamedata(stageId,score);
		 }
		 db.close();
		 }
   
   public static  int readChallengeScore(int position,Context context)	 
	 {		
	 int tempScore = -1;
		 Database db = new Database(context);
		 db.open();
		 Cursor cursor = db.getChallengeScore(position);			
		 if(cursor.moveToFirst() == true){		 						
			 String scoreStr = cursor.getString(Database.INDEX_CHALLENGE_SCORE);	
			 tempScore = Integer.parseInt(scoreStr);
		 }		 
		db.close();
		return tempScore;
	 }
   
   public static  String readChallengeName(int position,Context context)	 
	 {		
	 String tempName = null;
		 Database db = new Database(context);
		 db.open();
		 Cursor cursor = db.getChallengeScore(position);			
		 if(cursor.moveToFirst() == true){		 						
			 tempName= cursor.getString(Database.INDEX_CHALLENGE_NAME);	
		 }		 
		db.close();
		return tempName;
	 }
   
   public static void saveChallengeScore(String name,int score,Context context)     
   {       
	     int[] NO=new int[3];
	     String[] Name=new String[3];
	     NO[0]=readChallengeScore(1,context);
	     NO[1]=readChallengeScore(2,context);
	     NO[2]=readChallengeScore(3,context);
	     Name[0]=readChallengeName(1,context);
	     Name[1]=readChallengeName(2,context);
	     Name[2]=readChallengeName(3,context);
		 Database db = new Database(context);
		 db.open();	
		 int i=0;
		 for(;i<3;i++)
			 if(score>NO[i])break;
		 if(i<3){
		for(int j=i+1;j<3;j++){
		 Cursor cursor = db.getChallengeScore(j+1);
		 if(cursor.moveToFirst() == false)		 
			db.insertChallengeScore(Name[j-1],NO[j-1]);						 		 
		 else
			db.updateChallengeScore(j+1,Name[j-1],NO[j-1]);
		 }
		 Cursor cursor = db.getChallengeScore(i+1);
		 if(cursor.moveToFirst() == false)		 
			db.insertChallengeScore(name,score);						 		 
		 else
			db.updateChallengeScore(i+1,name,score);
		 db.close();
		 }
	}
}

