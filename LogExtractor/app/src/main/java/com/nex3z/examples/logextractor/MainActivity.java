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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private SimpleDateFormat mFormatter = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");

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
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, -10);
                Date from = calendar.getTime();

                Process process = Runtime.getRuntime().exec(new String[]
                        {"logcat", "-d", "-t", mFormatter.format(from)});
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                StringBuilder sb = new StringBuilder();
                int lineCount = 0;
                String line = reader.readLine();
                while (line != null && !isCancelled()) {
                    sb.append(line);
                    sb.append(LINE_SEPARATOR);
                    lineCount++;
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
}
