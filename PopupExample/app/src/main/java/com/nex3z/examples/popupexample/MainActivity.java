package com.nex3z.examples.popupexample;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private final String[] data = {"A", "B", "C", "D"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnShowPopupWindow = (Button) findViewById(R.id.btn_show_popup_window);
        btnShowPopupWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupWindow(MainActivity.this, view, 0, 0);
            }
        });

        Button btnShowAlertDialog = (Button) findViewById(R.id.btn_show_alert_dialog);
        btnShowAlertDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog(MainActivity.this);
            }
        });
    }

    void showPopupWindow(Context context, View view, int offsetX, int offsetY) {
        PopupWindow popup = new PopupWindow(context);
        popup.setFocusable(true);
        popup.setOutsideTouchable(true);
        popup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setWidth(300);
        popup.setContentView(createViewForPopupWindow(popup));
        popup.showAsDropDown(view, offsetX, offsetY);
    }

    private View createViewForPopupWindow(final PopupWindow popup) {
        View view = getLayoutInflater().inflate(R.layout.popupwindow, null);
        ListView list = (ListView) view.findViewById(R.id.list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item, R.id.tv_title, data);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Selected " + data[position],
                        Toast.LENGTH_SHORT).show();
                popup.dismiss();
            }
        });

        return view;
    }

    private void showAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(data, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Selected " + data[which],
                        Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
