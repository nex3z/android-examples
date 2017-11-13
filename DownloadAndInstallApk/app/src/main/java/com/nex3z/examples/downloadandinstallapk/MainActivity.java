package com.nex3z.examples.downloadandinstallapk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int MAX_PROGRESS = 100;

    private EditText mEtUrl;
    private ProgressBar mPbProgress;
    private Button mBtnDownload;
    private DownloadTask mDownloadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEtUrl = findViewById(R.id.et_url);
        mPbProgress = findViewById(R.id.pb_progress);
        mBtnDownload = findViewById(R.id.btn_download);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelRunningTask();
    }

    private void init() {
        mPbProgress.setMax(MAX_PROGRESS);
        mBtnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelRunningTask();
                mDownloadTask = new DownloadTask(MainActivity.this);
                mDownloadTask.execute(mEtUrl.getText().toString());
                mBtnDownload.setEnabled(false);
            }
        });
    }

    private void cancelRunningTask() {
        if (mDownloadTask != null && mDownloadTask.getStatus() == AsyncTask.Status.RUNNING) {
            mDownloadTask.cancel(true);
        }
    }

    private static class DownloadTask extends AsyncTask<String, Integer, File> {
        private final WeakReference<MainActivity> mRef;
        private final String mDownloadDir;

        DownloadTask(MainActivity activity) {
            mRef = new WeakReference<>(activity);

            File file = activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            if (file != null) {
                mDownloadDir = file.getPath();
            } else {
                mDownloadDir = Environment.getExternalStorageDirectory().getPath();
            }

            Log.v(LOG_TAG, "DownloadTask(): mDownloadDir = " + mDownloadDir);
        }

        @Override
        protected File doInBackground(String... args) {
            HttpURLConnection connection = null;
            InputStream in = null;
            OutputStream out = null;

            try {
                URL url = new URL(args[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int length = connection.getContentLength();
                in = connection.getInputStream();
                String file = mDownloadDir + "/temp.apk";
                out = new FileOutputStream(file);
                byte data[] = new byte[1024];
                int count = 0;
                long downloaded = 0;

                while((count = in.read(data)) != -1) {
                    downloaded += count;
                    int progress = (int) (1.0 * downloaded / length * MAX_PROGRESS);
                    Log.v(LOG_TAG, "doInBackground(): progress = " + progress);

                    onProgressUpdate(progress);
                    out.write(data, 0, count);

                    if (isCancelled()) {
                        Log.v(LOG_TAG, "doInBackground(): Cancelled");
                        return null;
                    }
                }
                out.flush();
                return new File(file);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "MalformedURLException", e);
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOException", e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                close(in);
                close(out);
            }

            return null;
        }

        private void close(Closeable closeable) {
            if (closeable == null) {
                return;
            }
            try {
                closeable.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "close(): ", e);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            MainActivity activity = mRef.get();
            if (activity != null) {
                activity.mPbProgress.setProgress(values[0]);
            }
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            MainActivity activity = mRef.get();
            if (activity != null) {
                activity.mBtnDownload.setEnabled(true);
                install(activity, file);
            }
        }

        private void install(Context context, File file) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (file == null) {
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uri = FileProvider.getUriForFile(context,
                        BuildConfig.APPLICATION_ID + ".fileProvider", file);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                intent.setDataAndType(Uri.fromFile(file),
                        "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
        }

    }
}
