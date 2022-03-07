package com.adslinfosoft.softberry.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.adslinfosoft.softberry.app.Softberry;
import com.adslinfosoft.softberry.model.NotificationVO;
import com.adslinfosoft.softberry.model.QucikComment;
import com.adslinfosoft.softberry.model.Notification;

import java.util.ArrayList;

/**
 * Created by shreshtha on 15/06/16.
 */
public class FetchData {
    private static final String TAG = "FetchData";
    private final DataBaseHandler handler;

    public FetchData() {
        handler = Softberry.getDBHandler();
    }

    public long insertNotification(NotificationVO user) {
        long i = -1;
        try {
            SQLiteDatabase db = handler.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("Message", user.getMessage());
            values.put("Date", user.getDate());
            values.put("ImagePath", user.getPath());
            values.put("ImageURL", user.getImageURL());
            values.put("JobNo", user.getJobNo());
            values.put("CorrectionCount", user.getCorrectionCount());
            values.put("CoordinatorEmail", user.getCoordinatorEmail());
            values.put("IsRead", user.getIsRead());
            values.put("Title", user.getTitle());
            i = db.insert("Notification", null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public long insertQuickComment(QucikComment cmt) {
        long i = -1;
        try {
            SQLiteDatabase db = handler.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("Comment", cmt.getQuickComment());
            values.put("Date", cmt.getDate());
            values.put("JobNo", cmt.getJobno());
            values.put("JobDescription", cmt.getJobDescription());
            values.put("IsAttchment", cmt.getIsAttchment());
            i = db.insert("QucikComment", null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public long insertImageUrl(QucikComment cmt) {
        long i = -1;
        try {
            SQLiteDatabase db = handler.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("JobNo", cmt.getQuickComment());
            values.put("JsonUrl", cmt.getDate());
            i = db.insert("ImageUrl", null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    /*
     * this method will return all cities
     */
    public ArrayList<Notification> getAllNotification() {
        ArrayList<Notification> results = new ArrayList();
        try {
            SQLiteDatabase db = handler.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM Notification order by ID desc", null);
            Log.d("getAllCity", "cursor size = " + c.getCount());
            while (c.moveToNext()) {
                String name = c.getString(c.getColumnIndex("Message"));
                String date = c.getString(c.getColumnIndex("Date"));
                String path = c.getString(c.getColumnIndex("ImagePath"));
                String imageurl = c.getString(c.getColumnIndex("ImageURL"));
                String JobNo = c.getString(c.getColumnIndex("JobNo"));
                String CorrectionCount = c.getString(c.getColumnIndex("CorrectionCount"));
                String CoordinatorEmail = c.getString(c.getColumnIndex("CoordinatorEmail"));
                int id = c.getInt(c.getColumnIndex("ID"));
                int isRead = c.getInt(c.getColumnIndex("IsRead"));
                String title = c.getString(c.getColumnIndex("Title"));
                Notification dao = new Notification();
                dao.setId(id);
                dao.setMessage(name);
                dao.setDate(date);
                dao.setPath(path);
                dao.setImageURL(imageurl);
                dao.setCorrectionCount(CorrectionCount);
                dao.setCoordinatorEmail(CoordinatorEmail);
                dao.setJobNo(JobNo);
                dao.setIsRead(isRead);
                dao.setTitle(title);
                results.add(dao);
            }
            c.close();

        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(),
                    "Could not create or Open the database");
        }
        return results;
    }

    public ArrayList<QucikComment> getAllComments(String jobNo) {
        ArrayList<QucikComment> results = new ArrayList();
        try {
            SQLiteDatabase db = handler.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM QucikComment where JobNo LIKE '" + jobNo + "' order by ID desc LIMIT 30", null);
            Log.d("getAllCity", "cursor size = " + c.getCount());
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndex("ID"));
                String cmt = c.getString(c.getColumnIndex("Comment"));
                String date = c.getString(c.getColumnIndex("Date"));
                String jobno = c.getString(c.getColumnIndex("JobNo"));
                String descr = c.getString(c.getColumnIndex("JobDescription"));
                int attchment = c.getInt(c.getColumnIndex("IsAttchment"));
                QucikComment dao = new QucikComment();
                dao.setId(id);
                dao.setDate(date);
                dao.setQuickComment(cmt);
                dao.setJobno(jobno);
                dao.setJobDescription(descr);
                dao.setIsAttchment(attchment);
                results.add(dao);
            }
            c.close();

        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(),
                    "Could not create or Open the database");
        }
        return results;
    }

    public void deleteNotifications() {
        try {
            SQLiteDatabase db = handler.getWritableDatabase();
            db.delete("Notification",
                    "ROWID NOT IN (SELECT ROWID FROM Notification ORDER BY ID desc LIMIT 10)",
                    null);
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(),
                    "Could not create or Open the database");
        }
    }

    public String getImageUrl(String jobNo) {
        String results = "";
        try {
            SQLiteDatabase db = handler.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM ImageUrl where JobNo LIKE '" + jobNo + "'", null);
            Log.d("getAllCity", "cursor size = " + c.getCount());
            while (c.moveToNext()) {
                results = c.getString(c.getColumnIndex("JsonUrl"));
            }
            c.close();

        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(),
                    "Could not create or Open the database");
        }
        return results;
    }

    public void deleteImageUrl(SQLiteDatabase db, String id) {
        try {
            db.delete("ImageUrl", "JobNo LIKE '" + id + "'", null);
        } catch (Exception se) {
            Log.e(getClass().getSimpleName(),
                    "Could not create or Open the database");
        }
    }


//    /**
//     * call this to update user profile
//     */
//    public int updateProfile(UserDao user) {
//        int updateStatus = 0;
//        try {
//            Log.d("updateProfile", "update profile detail");
//            SQLiteDatabase db = handler.getWritableDatabase();
//            ContentValues values = new ContentValues();
//            values.put("UserName", user.getUserName());
//            values.put("Password", user.getPassword());
//            values.put("Name", user.getName());
//            values.put("Email", user.getEmail());
//            values.put("Mobile", user.getMobile());
//            values.put("Image", user.getImage());
//            updateStatus = db.update("Login", values,
//                    "UserID=" + user.getUserID(), null);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.d("updateProfile", "data not undated");
//        }
//        return updateStatus;
//    }

    /**
     * call this to update user profile
     */
    public int updateNotification(Notification notify) {
        int updateStatus = 0;
        try {
            Log.e("updateNotification", "update Notification detail");
            SQLiteDatabase db = handler.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("ID", notify.getId());
            values.put("IsRead", notify.getIsRead());
            values.put("JobNo", notify.getJobNo());
            values.put("ImageURL", notify.getImageURL());
            values.put("Date", notify.getDate());
            values.put("ImagePath", notify.getPath());
            values.put("Message", notify.getMessage());
            values.put("Title", notify.getTitle());
            values.put("CorrectionCount", notify.getCorrectionCount());
            values.put("CoordinatorEmail", notify.getCoordinatorEmail());
            updateStatus = db.update("Notification", values,
                    "ID=" + notify.getId(), null);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("updateNotification", "Notification not undated");
        }
        return updateStatus;
    }
}
