package com.nex3z.examples.changedpi;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

public class DpiContextWrapper extends ContextWrapper {

    private DpiContextWrapper(Context base) {
        super(base);
    }

    public static DpiContextWrapper wrap(Context context) {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.densityDpi = DisplayMetrics.DENSITY_MEDIUM;
            context = context.createConfigurationContext(config);
        } else {
            resources.updateConfiguration(config, resources.getDisplayMetrics());
        }

        return new DpiContextWrapper(context);
    }

}
