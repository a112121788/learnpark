package net.learnpark.app.learnpark;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 课程的sqlite实现类
 * @author peng
 * @version 1 2014年6月5日 15:41:11
 * 修改：2014年6月30日 13:17:45 根据晓午的代码再次修改
 */
public class CourseSQLiteOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_CREATE = "create table courses(_id integer primary key, day text,time integer,coursename text,timebegin text,timeend text,cite text ,teacher text);";
	private static final String DATABASE_INIT="insert into courses (day,time,coursename ,timebegin,timeend,cite ,teacher) values(?,?,?,?,?,?,?);";
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "learnpark";

	public CourseSQLiteOpenHelper(Context context, String sqliteDatabaseName,
			CursorFactory factory, int version) {
		super(context, sqliteDatabaseName, factory, version);
	}

	public CourseSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
		init(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL("DROP TABLE IF EXISTS MyEmployees");//执行更新函数‘
		db.execSQL(DATABASE_CREATE);
	}

	public void init(SQLiteDatabase db){
		for (int i = 1; i < 8; i++) {
			for (int j = 1; j < 9; j++) {
				db.execSQL(DATABASE_INIT,new String[]{i+"",j+"","","","",""});
			}
			
		}
		
	}
}