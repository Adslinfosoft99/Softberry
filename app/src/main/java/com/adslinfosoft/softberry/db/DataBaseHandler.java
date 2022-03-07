package com.adslinfosoft.softberry.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {

	public DataBaseHandler(Context context) {
		super(context, name, null, version);
	}

	public static String name = "softberry.db";
	private static final int version = 3;

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table Notification(ID INTEGER PRIMARY KEY autoincrement, Message String, ImagePath String, Date DATETIME, ImageURL String, JobNo String, CorrectionCount String, CoordinatorEmail String, IsRead Int, Title String)");

		db.execSQL("create table QucikComment(ID INTEGER PRIMARY KEY autoincrement, Comment String, Date DATETIME, JobNo String, JobDescription String, IsAttchment Int)");

		db.execSQL("create table ImageUrl(ID INTEGER PRIMARY KEY autoincrement, JobNo String, JsonUrl String)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			db.execSQL("ALTER TABLE Notification RENAME TO temp_Notification");

		} catch (Exception e) {
		}
		try {
			db.execSQL("ALTER TABLE QucikComment RENAME TO temp_QucikComment");

		} catch (Exception e) {
		}
		try {
			db.execSQL("ALTER TABLE ImageUrl RENAME TO temp_ImageUrl");

		} catch (Exception e) {
		}
		try {
			db.execSQL("DROP TABLE IF EXISTS Notification");
			db.execSQL("DROP TABLE IF EXISTS QucikComment");
			db.execSQL("DROP TABLE IF EXISTS ImageUrl");
		} catch (Exception e) {
		}
		onCreate(db);
		try {
			db.execSQL("insert into Notification (ID, Message, ImagePath, Date, ImageURL, JobNo, CorrectionCount, CoordinatorEmail, IsRead, Title) select ID, Message, ImagePath, Date, ImageURL, JobNo, CorrectionCount, CoordinatorEmail, IsRead, Title from temp_Notification");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			db.execSQL("insert into QucikComment (ID, Comment, Date, JobNo, JobDescription, IsAttchment) select ID, Comment, Date, JobNo, JobDescription, IsAttchment from temp_QucikComment");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			db.execSQL("insert into ImageUrl (ID, JobNo, JsonUrl) select ID, JobNo, JsonUrl from temp_ImageUrl");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			db.execSQL("DROP TABLE IF EXISTS temp_Notification");
			db.execSQL("DROP TABLE IF EXISTS temp_QucikComment");
			db.execSQL("DROP TABLE IF EXISTS temp_ImageUrl");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
