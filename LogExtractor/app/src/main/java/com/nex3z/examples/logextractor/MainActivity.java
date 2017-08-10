package com.nex3z.examples.logextractor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private SimpleDateFormat mFoormatsdf = new SimpleDateFormat("MM-dd HH:mm:ss");
    private static final String COMMAND = "logcat -d";


    private Button mBtnDump;
    private TextView mTvLog;
    private DumpTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvLog = (TextView) findViewById(R.id.tv_log);
        mBtnDump = (Button) findViewById(R.id.btn_dump);
        mBtnDump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTask != null && mTask.getStatus() == AsyncTask.Status.RUNNING) {
                    mTask.cancel(true);
                }
                mTvLog.setText("");
                mTask = new DumpTask();
                mTask.execute();
                Log.v(LOG_TAG, "Dump started");
            }
        });
        fillLog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTask != null && mTask.getStatus() == AsyncTask.Status.RUNNING) {
            mTask.cancel(true);
        }
    }

    private void fillLog() {
        for (int i = 0; i < 10; i++) {
            Log.v(LOG_TAG, "fillLog(): Filling some log, count = " + i);
        }
    }

    private class DumpTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            BufferedReader reader = null;
            try {
                Process process = Runtime.getRuntime().exec(COMMAND);
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, -10);
                calendar.set(Calendar.YEAR, 1970);
                Date from = calendar.getTime();

                StringBuilder sb = new StringBuilder();
                int lineCount = 0;
                String line = reader.readLine();
                boolean startSaving = false;

                while (line != null && !isCancelled()) {
                    if (!startSaving) {
                        Date date = getDate(line);
                        if (date != null && date.after(from)) {
                            startSaving = true;
                        }
                    }
                    if (startSaving) {
                        sb.append(line);
                        sb.append(System.getProperty("line.separator"));
                        lineCount++;
                    }
                    line = reader.readLine();
                }

                Log.v(LOG_TAG, "doInBackground(): Dump complete, lineCount = " + lineCount);
                return sb.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "doInBackground()", e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "doInBackground(): Error closing BufferedReader", e);
                    }
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            mTvLog.setText(s);
            Log.v(LOG_TAG, "Dump finished");
        }
    }

    private Date getDate(String line) {
        int pointPos = line.indexOf('.');
        if (pointPos != -1) {
            String dateStr = line.substring(0, pointPos);
            try {
                return mFoormatsdf.parse(dateStr);
            } catch (ParseException e) {
                return null;
            }
        } else {
            return null;
        }
    }
}
