package com.carlsaldanha.expertsexchange;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Carl Saldanha on 9/6/2015.
 */
public class ProcedureDB extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Procedures.db";
    public QueryManager qm=new QueryManager();

    public ProcedureDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public  long  addStep(String text,String type, String link){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Step.COLUMN__NAME_STEP, text);
        values.put(Step.COLUMN__NAME_LINK,link);
        values.put(Step.COLUMN__NAME_TYPE,type);

// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(Step.TABLE_NAME,null,values);
        return newRowId;
    }

    public int getLastUser(){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection={"max(user_id)"};
        String selection="id > ?";
        Cursor c = db.query(
                "StepRecorder",  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        c.moveToFirst();
        if(c.isAfterLast())
            return -1;
        int itemId = c.getInt(c.getColumnIndexOrThrow("max(user_id)"));
        return itemId+1;
    }

    public Step getNextStep(long rowId){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection={"id",Step.COLUMN__NAME_STEP,Step.COLUMN__NAME_TYPE,Step.COLUMN__NAME_LINK};
        String selection="id > ?";
        String[] selectionArgs={String.valueOf(rowId)};
        Cursor c = db.query(
                Step.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        c.moveToFirst();
        if(c.isAfterLast())
            return null;
        long itemId = c.getLong(c.getColumnIndexOrThrow("id"));
        String type = c.getString(c.getColumnIndexOrThrow(Step.COLUMN__NAME_TYPE));
        String link = c.getString(c.getColumnIndexOrThrow(Step.COLUMN__NAME_LINK));
        String text = c.getString(c.getColumnIndexOrThrow(Step.COLUMN__NAME_STEP));

        return new Step(link,type,text,itemId);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(qm.SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(qm.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private class QueryManager{
        String SQL_CREATE_ENTRIES="";
        String SQL_DELETE_ENTRIES="";
        String[] tables={"StepRecorder"};
        String [][] columns={{"user_id","task_id","expert_user_id", "current_step_id", "start_time","end_time"}};


        QueryManager(){
            for (int i=0;i<tables.length;i+=1){
                SQL_CREATE_ENTRIES+=_create(tables[i],columns[i]);
                SQL_DELETE_ENTRIES+=_delete(tables[i]);
            }

        }

        private String _create(String table_name,String[] columns){
            String create_string= "CREATE TABLE "+ table_name+" (";
            create_string+="id INT NOT NULL,";
            for (String column:columns) {
                create_string+=column+" VARCHAR(500),";
            }

            create_string+="PRIMARY KEY ( id ));";
            return create_string;
        }

        private String  _delete(String table){
            String delete_string="DROP TABLE "+table+";";

            return delete_string;
        }

    }
}


