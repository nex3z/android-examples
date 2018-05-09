package com.nex3z.examples.camera2api;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.widget.ImageView;

import com.nex3z.examples.camera2api.util.CameraUtil;
import com.nex3z.examples.camera2api.util.ImageUtil;
import com.nex3z.examples.camera2api.util.PermissionUtil;

public class MainActivity extends AppCompatActivity implements
        ImageReader.OnImageAvailableListener, CameraView.ConnectionCallback{
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int RC_PERMISSIONS = 1;
    private static final String[] PERMISSIONS = { Manifest.permission.CAMERA };
    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);

    private CameraView mCameraView;
    private ImageView mIvImage;

    private Size mPreviewSize;
    private byte[][] mYuvBytes = new byte[3][];
    private int[] mRgbBytes = null;
    private boolean mIsProcessingFrame = false;
    private Bitmap mRgbBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_main);

        mCameraView = findViewById(R.id.cv_camera);
        mIvImage = findViewById(R.id.iv_image);

        init();

        if (PermissionUtil.hasPermission(this, PERMISSIONS)) {
            openCamera();
        } else {
            PermissionUtil.requestPermission(this, RC_PERMISSIONS, PERMISSIONS);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraView.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
        }
    }

    @Override
    public void onImageAvailable(ImageReader reader) {
        Log.v(LOG_TAG, "onImageAvailable(): mIsProcessingFrame = " + mIsProcessingFrame);
        if (mPreviewSize == null) {
            return;
        }
        final Image image = reader.acquireLatestImage();
        if (image == null) {
            return;
        } else if (mIsProcessingFrame) {
            image.close();
            return;
        }

        mIsProcessingFrame = true;

        final Image.Plane[] planes = image.getPlanes();
        ImageUtil.fillBytes(planes, mYuvBytes);

        int yRowStride = planes[0].getRowStride();
        final int uvRowStride = planes[1].getRowStride();
        final int uvPixelStride = planes[1].getPixelStride();

        ImageUtil.convertYUV420ToARGB8888(mYuvBytes[0], mYuvBytes[1], mYuvBytes[2],
                mPreviewSize.getWidth(), mPreviewSize.getHeight(),
                yRowStride, uvRowStride, uvPixelStride, mRgbBytes);
        mRgbBitmap.setPixels(mRgbBytes, 0, mPreviewSize.getWidth(), 0, 0, mPreviewSize.getWidth(),
                mPreviewSize.getHeight());

        mIvImage.setImageBitmap(mRgbBitmap);

        image.close();
        mIsProcessingFrame = false;
    }

    @Override
    public void onPreviewSizeChosen(Size size, int cameraRotation) {
        Log.v(LOG_TAG, "onPreviewSizeChosen(): size = " + size);
        mPreviewSize = size;
        mRgbBitmap = Bitmap.createBitmap(mPreviewSize.getWidth(), mPreviewSize.getHeight(),
                Bitmap.Config.ARGB_8888);
        mRgbBytes = new int[mPreviewSize.getWidth() * mPreviewSize.getHeight()];
    }

    @Override
    public void onScaleSet(float scale) {

    }

    private void init() {
        mCameraView.setImageListener(this);
        mCameraView.setConnectionCallback(this);
    }

    private void openCamera() {
        String cameraId = CameraUtil.chooseCamera(this);
        Log.v(LOG_TAG, "showFragment(): cameraId = " + cameraId);
        mCameraView.setDesiredSize(DESIRED_PREVIEW_SIZE);
        mCameraView.setCameraId(cameraId);
        mCameraView.openCamera();
    }
}
