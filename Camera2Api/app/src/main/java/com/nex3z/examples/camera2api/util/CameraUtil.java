package com.nex3z.examples.camera2api.util;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CameraUtil {
    private static final String LOG_TAG = CameraUtil.class.getSimpleName();
    private static final int MINIMUM_PREVIEW_SIZE = 320;

    private CameraUtil() {}

    public static String chooseCamera(Activity activity) {
        final CameraManager manager = (CameraManager) activity
                .getSystemService(Context.CAMERA_SERVICE);
        if (manager == null) {
            return null;
        }
        try {
            for (final String cameraId : manager.getCameraIdList()) {
                final CameraCharacteristics characteristics = manager.
                        getCameraCharacteristics(cameraId);
                final Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                Log.v(LOG_TAG, "chooseCamera(): cameraId = " + cameraId + ", facing = " + facing);
                if (facing == null || facing != CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }
                final StreamConfigurationMap map = characteristics
                        .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }
                Log.v(LOG_TAG, "chooseCamera(): choose cameraId = " + cameraId);
                return cameraId;
            }
        } catch (CameraAccessException e) {
            Log.e(LOG_TAG, "chooseCamera(): CameraAccessException", e);
        }
        return null;
    }

    public static Size chooseOptimalSize(final Size[] choices, final int width, final int height) {
        final int minSize = Math.max(Math.min(width, height), MINIMUM_PREVIEW_SIZE);
        final Size desiredSize = new Size(width, height);

        boolean exactSizeFound = false;
        final List<Size> bigEnough = new ArrayList<>();
        final List<Size> tooSmall = new ArrayList<>();
        for (final Size option : choices) {
            if (option.equals(desiredSize)) {
                exactSizeFound = true;
            }
            if (option.getHeight() >= minSize && option.getWidth() >= minSize) {
                bigEnough.add(option);
            } else {
                tooSmall.add(option);
            }
        }

        Log.v(LOG_TAG, "Desired size: " + desiredSize + ", min size: " + minSize + "x" + minSize);
        Log.v(LOG_TAG, "Valid preview sizes: [" + TextUtils.join(", ", bigEnough) + "]");
        Log.v(LOG_TAG, "Rejected preview sizes: [" + TextUtils.join(", ", tooSmall) + "]");

        if (exactSizeFound) {
            Log.v(LOG_TAG, "Exact size match found.");
            return desiredSize;
        }

        // Pick the smallest of those, assuming we found any
        if (bigEnough.size() > 0) {
            final Size chosenSize = Collections.min(bigEnough, new CompareSizesByArea());
            Log.v(LOG_TAG, "Chosen size: " + chosenSize.getWidth() + "x" + chosenSize.getHeight());
            return chosenSize;
        } else {
            Log.e(LOG_TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

}
