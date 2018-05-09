package com.nex3z.examples.camera2api;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.widget.FrameLayout;

import com.nex3z.examples.camera2api.util.CameraUtil;

import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CameraView extends FrameLayout {
    private static final String LOG_TAG = CameraView.class.getSimpleName();

    private final AutoFitTextureView mTextureView;

    private final CameraManager mCameraManager;
    private String mCameraId;
    private Size mDesiredSize;
    private Size mPreviewSize;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCaptureSession;
    private ImageReader mPreviewReader;

    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    private SurfaceTextureListener mSurfaceTextureListener = new SurfaceTextureListener();
    private CameraStateCallback mStateCallback = new CameraStateCallback();
    private CaptureCallback mCaptureCallback = new CaptureCallback();

    private ConnectionCallback mConnectionCallback;
    private ImageReader.OnImageAvailableListener mImageListener;

    public CameraView(final Context context) {
        this(context, null);
    }

    public CameraView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.view_camera, this, true);
        } else {
            throw new IllegalStateException("inflater is null");
        }
        mTextureView = findViewById(R.id.view_texture);
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);

        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    }

    public void onResume() {
        boolean isReadyForOpenCamera = isCameraIdReady() && mTextureView.isAvailable();
        Log.v(LOG_TAG, "onResume(): isReadyForOpenCamera = " + isReadyForOpenCamera);
        if (isReadyForOpenCamera) {
            openCamera();
        }
    }

    public void onPause() {
        Log.v(LOG_TAG, "onPause()");
        closeCamera();
    }

    public void setCameraId(String cameraId) {
        mCameraId = cameraId;
    }

    public void setDesiredSize(Size desiredSize) {
        mDesiredSize = desiredSize;
    }

    public void setConnectionCallback(ConnectionCallback connectionCallback) {
        mConnectionCallback = connectionCallback;
    }

    public void setImageListener(ImageReader.OnImageAvailableListener imageListener) {
        mImageListener = imageListener;
    }

    public void openCamera() {
        Log.v(LOG_TAG, "openCamera(): mCameraId = " + mCameraId + ", mDesiredSize = "
                + mDesiredSize + ", mTextureView size = (" + mTextureView.getWidth() + ", "
                + mTextureView.getHeight() + ")");
        if (!mTextureView.isAvailable()) {
            Log.v(LOG_TAG, "openCamera(): TextureView not ready, skip");
            return;
        }

        setUpCameraOutputs();
        configureTransform();

        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening");
            }
            mCameraManager.openCamera(mCameraId, mStateCallback, null);
        } catch (InterruptedException e) {
            Log.e(LOG_TAG, "openCamera(): InterruptedException", e);
        } catch (CameraAccessException e) {
            Log.e(LOG_TAG, "openCamera(): CameraAccessException", e);
        } catch (SecurityException e) {
            Log.e(LOG_TAG, "openCamera(): SecurityException", e);
        }
    }

    private void setUpCameraOutputs() {
        final CameraCharacteristics characteristics;
        try {
            characteristics = mCameraManager.getCameraCharacteristics(mCameraId);
        } catch (CameraAccessException e) {
            Log.e(LOG_TAG, "setUpCameraOutputs(): CameraAccessException", e);
            return;
        }
        final StreamConfigurationMap map =
                characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (map == null) {
            Log.e(LOG_TAG, "setUpCameraOutputs(): map is null");
            return;
        }

        mPreviewSize = CameraUtil.chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                mDesiredSize.getWidth(), mDesiredSize.getHeight());
        Log.v(LOG_TAG, "setUpCameraOutputs(): mPreviewSize = " + mPreviewSize);

        final int orientation = getResources().getConfiguration().orientation;
        Log.v(LOG_TAG, "setUpCameraOutputs(): orientation = " + orientation);
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mTextureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        } else {
            mTextureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
        }

        if (mConnectionCallback != null) {
            final Integer sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            Log.v(LOG_TAG, "setUpCameraOutputs(): sensorOrientation = " + sensorOrientation);
            mConnectionCallback.onPreviewSizeChosen(mPreviewSize,
                    sensorOrientation == null ? 0 : sensorOrientation);
        }
    }

    private void configureTransform() {
        final int rotation = ((Activity) getContext()).getWindowManager().getDefaultDisplay()
                .getRotation();
        Log.v(LOG_TAG, "configureTransform(): mTextureView size = (" + mTextureView.getWidth()
                + ", " + mTextureView.getHeight() + "), mPreviewSize = " + mPreviewSize
                + ", rotation = " + rotation);
        final Matrix matrix = new Matrix();
        final RectF viewRect = new RectF(0, 0, mTextureView.getWidth(), mTextureView.getHeight());
        final float centerX = viewRect.centerX();
        final float centerY = viewRect.centerY();
        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            final RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) mTextureView.getHeight() / mPreviewSize.getHeight(),
                    (float) mTextureView.getWidth() / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            float rotateDegree = 90 * (rotation - 2);
            Log.v(LOG_TAG, "configureTransform(): scale = " + scale + ", rotateDegree = "
                    + rotateDegree);
            matrix.postRotate(rotateDegree, centerX, centerY);
        } else if (rotation == Surface.ROTATION_180) {
            matrix.postRotate(180, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    private void createCameraPreviewSession() {
        Log.v(LOG_TAG, "createCameraPreviewSession(): mPreviewSize = " + mPreviewSize);

        final SurfaceTexture texture = mTextureView.getSurfaceTexture();
        texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

        final Surface surface = new Surface(texture);
        final CaptureRequest.Builder previewRequestBuilder;
        try {
            previewRequestBuilder = mCameraDevice.createCaptureRequest(
                    CameraDevice.TEMPLATE_PREVIEW);
        } catch (CameraAccessException e) {
            Log.v(LOG_TAG, "createCameraPreviewSession(): failed to create capture request", e);
            return;
        }

        previewRequestBuilder.addTarget(surface);

        mPreviewReader = ImageReader.newInstance(mPreviewSize.getWidth(), mPreviewSize.getHeight(),
                ImageFormat.YUV_420_888, 2);
        mPreviewReader.setOnImageAvailableListener(mImageListener, null);
        previewRequestBuilder.addTarget(mPreviewReader.getSurface());

        previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
        final CaptureRequest previewRequest = previewRequestBuilder.build();

        try {
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mPreviewReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            mCaptureSession = session;
                            try {
                                mCaptureSession.setRepeatingRequest(previewRequest,
                                        mCaptureCallback, null);
                            } catch (CameraAccessException e) {
                                Log.e(LOG_TAG, "createCameraPreviewSession(): failed to set " +
                                        "repeating request");
                            }
                        }
                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                            Log.v(LOG_TAG, "onConfigureFailed()");
                        }
                    }, null);
        } catch (CameraAccessException e) {
            Log.v(LOG_TAG, "createCameraPreviewSession(): failed to create capture session", e);
        }
    }

    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (mCaptureSession != null) {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (mCameraDevice != null) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (mPreviewReader != null) {
                mPreviewReader.close();
                mPreviewReader = null;
            }
        } catch (InterruptedException e) {
            Log.e(LOG_TAG, "closeCamera(): InterruptedException", e);
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    private boolean isCameraIdReady() {
        return !TextUtils.isEmpty(mCameraId);
    }


    private class SurfaceTextureListener implements TextureView.SurfaceTextureListener {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.v(LOG_TAG, "onSurfaceTextureAvailable(): width = " + width  +", height = " + height);
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            Log.v(LOG_TAG, "onSurfaceTextureSizeChanged()：width = " + width + "， height = " + height);
            configureTransform();
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            // Log.v(LOG_TAG, "onSurfaceTextureUpdated()");
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Log.v(LOG_TAG, "onSurfaceTextureDestroyed()");
            return true;
        }
    }

    private class CameraStateCallback extends CameraDevice.StateCallback {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            Log.v(LOG_TAG, "onOpened()");
            mCameraOpenCloseLock.release();
            mCameraDevice = camera;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            Log.v(LOG_TAG, "onDisconnected()");
            mCameraOpenCloseLock.release();
            camera.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            Log.v(LOG_TAG, "onError()");
            mCameraOpenCloseLock.release();
            camera.close();
            mCameraDevice = null;
        }
    }

    private class CaptureCallback extends CameraCaptureSession.CaptureCallback {
        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
            Log.v(LOG_TAG, "onCaptureProgressed()");
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
            // Log.v(LOG_TAG, "onCaptureCompleted()");
        }
    }

    public interface ConnectionCallback {
        void onPreviewSizeChosen(Size size, int cameraRotation);
        void onScaleSet(float scale);
    }

}
