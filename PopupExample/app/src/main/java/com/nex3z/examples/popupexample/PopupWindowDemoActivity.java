package com.nex3z.examples.popupexample;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class PopupWindowDemoActivity extends AppCompatActivity {

    private final String[] data = {"A", "B", "C", "D"};

    FrameLayout mContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_window_demo);

        mContainer = (FrameLayout) findViewById(R.id.container);
        mContainer.getForeground().setAlpha(0);

        Button btnDimByWindowManager = (Button) findViewById(R.id.btn_dim_by_window_manager);
        btnDimByWindowManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindow popup = buildPopupWindow(PopupWindowDemoActivity.this);
                popup.showAsDropDown(view);
                dimBackground(popup, 0.5f);
            }
        });

        Button btnDismissByViewGroupOverlay =
                (Button) findViewById(R.id.btn_dim_by_view_group_overlay);
        btnDismissByViewGroupOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindow popup = buildPopupWindow(PopupWindowDemoActivity.this);
                popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        clearDim();
                    }
                });

                popup.showAsDropDown(view);
                applyDim(0.5f);
            }
        });

        Button btnDismissByDrawable = (Button) findViewById(R.id.btn_dim_by_drawable);
        btnDismissByDrawable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindow popup = buildPopupWindow(PopupWindowDemoActivity.this);
                popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mContainer.getForeground().setAlpha(0);
                    }
                });

                popup.showAsDropDown(view);
                mContainer.getForeground().setAlpha(127);
            }
        });
    }

    private PopupWindow buildPopupWindow(Context context) {
        PopupWindow popup = new PopupWindow(context);
        popup.setFocusable(true);
        popup.setOutsideTouchable(true);
        popup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setWidth(300);
        popup.setContentView(createViewForPopupWindow(popup));
        popup.setBackgroundDrawable(
                new ColorDrawable(ContextCompat.getColor(this, R.color.popupBackground)));
        return popup;
    }

    private View createViewForPopupWindow(final PopupWindow popup) {
        View view = getLayoutInflater().inflate(R.layout.popupwindow, null);
        ListView list = (ListView) view.findViewById(R.id.list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item, R.id.tv_title, data);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(PopupWindowDemoActivity.this, "Selected " + data[position],
                        Toast.LENGTH_SHORT).show();
                popup.dismiss();
            }
        });

        return view;
    }


    private void dimBackground(PopupWindow popup, float dimAmount) {
        ViewParent vp = popup.getContentView().getParent();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (vp instanceof View) {
            // PopupWindow has background drawable set
            View container = (View) popup.getContentView().getParent();
            WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
            p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            p.dimAmount = dimAmount;
            wm.updateViewLayout(container, p);
        } else {
            // PopupWindow has no background drawable
            WindowManager.LayoutParams p =
                    (WindowManager.LayoutParams) popup.getContentView().getLayoutParams();
            p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            p.dimAmount = dimAmount;
            wm.updateViewLayout(popup.getContentView()  , p);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void applyDim(float dimAmount){
        ViewGroup parent = (ViewGroup) getWindow().getDecorView().getRootView();
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void clearDim() {
        ViewGroup parent = (ViewGroup) getWindow().getDecorView().getRootView();
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }
}
