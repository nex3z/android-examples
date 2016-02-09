package com.nex3z.examples.preventscreenshot;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_main);

        Button btnTakeScreenshot = (Button) findViewById(R.id.btn_take_screenshot);
        btnTakeScreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenshot();
            }
        });

    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
            View view = getWindow().getDecorView().getRootView();
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);

            File imageFile = new File(path);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();

            outputStream.close();

            Toast.makeText(this, "Saved to " + path, Toast.LENGTH_LONG).show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
