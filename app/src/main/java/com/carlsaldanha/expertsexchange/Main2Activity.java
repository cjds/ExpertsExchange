package com.carlsaldanha.expertsexchange;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class Main2Activity extends AppCompatActivity {

    EditText user_id;
    EditText task_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        user_id=(EditText)findViewById(R.id.user_id);
        task_id=(EditText)findViewById(R.id.task_id);
        Button button=(Button) findViewById(R.id.button);

        ProcedureDB pd=new ProcedureDB(this);
        Log.d("uno",pd.getLastUser()+"");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                intent.putExtra("user_id", user_id.getText().toString());
                intent.putExtra("task_id", task_id.getText().toString());
                startActivity(intent);
            }
        });

        findViewById(R.id.export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("uno","lick");
                backupDatabase();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    public final void backupDatabase() {
            File f = this.getDatabasePath(ProcedureDB.DATABASE_NAME);
            OutputStream myOutput=null;
        Log.d("uno",f.getAbsolutePath());
            InputStream myInput=null;
            if (f.exists()) {
                try {
                    File directory = new File(Environment.getExternalStorageDirectory()+"/proceduredb.db");
                    Log.d("uno","i");
                    FileChannel src = new FileInputStream(f).getChannel();
                    FileChannel dst = new FileOutputStream(directory).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (myOutput != null) {
                            myOutput.close();
                            myOutput = null;
                        }
                        if (myInput != null) {
                            myInput.close();
                            myInput = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
