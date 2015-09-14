package com.carlsaldanha.expertsexchange;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Carl Saldanha on 9/9/2015.
 */
public class Transfer {

    public Step[] transferDB(Context context,String task_id,String user_id){

        ExpertDB myDbHelper = new ExpertDB(context);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            ExpertStep expertStep=new ExpertStep();
            myDbHelper.openDataBase();
            SQLiteDatabase db=myDbHelper.getReadableDatabase();
            String[] projection =expertStep.columns;


// How you want the results sorted in the resulting Cursor
            String sortOrder =  "id_order ASC";

            Cursor c = db.query(
                    "instruction",  // The table to query
                    projection,                               // The columns to return
                    "user_id= ? AND task_id = ?",                                // The columns for the WHERE clause
                    new String[]{String.valueOf(user_id),String.valueOf(task_id)},                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
            Step s[]=new Step[c.getCount()];
            int i=0;
            c.moveToFirst();
            while (c.isAfterLast() == false) {
             //   view.append("n" + cur.getString(1));
                String text=c.getString(c.getColumnIndexOrThrow("instruction"));
                String t=c.getString(c.getColumnIndexOrThrow("media_type"));
                String type="";

                if(t==null){
                    type="None";
                }
                else{
                    Log.d("uuhiu", t+"");
                    if(t.equals("1"))type="Video";
                    else type="Photo";

                }
                String path=c.getString(c.getColumnIndexOrThrow("media_path"));
                long id=c.getLong(c.getColumnIndexOrThrow("_id"));
                if(path==null)
                    path="";
                String link=path.replaceFirst(".*/(\\w+)","$1");
                s[i]=new Step(link, type, text, id);
                i++;
                c.moveToNext();
            }
            c.close();
            return s;
        }catch(SQLException sqle) {
            throw sqle;

        }

    }

    public int getLastUserID(Context context){
        ProcedureDB procedureDB=new ProcedureDB(context);
        return procedureDB.getLastUser();
    }
}
