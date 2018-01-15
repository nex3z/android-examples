package com.rnnativemodule;

import android.os.Build;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

public class RnDeviceInfoModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext mReactContext;

    public RnDeviceInfoModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RnDeviceInfo";
    }

    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        Map<String, Object> constants = new HashMap<>();
        constants.put("system", "Android");
        constants.put("systemVersion", Build.VERSION.RELEASE);
        constants.put("language", Locale.getDefault().getDisplayLanguage());
        constants.put("appVersion", BuildConfig.VERSION_NAME);
        return constants;
    }
}
