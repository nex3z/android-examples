package com.nex3z.examples.camera2api

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var previewSize : Size
    private lateinit var cameraId: String
    private var cameraDevice: CameraDevice? = null
    private lateinit var previewRequestBuilder: CaptureRequest.Builder
    private var captureSession: CameraCaptureSession? = null
    private lateinit var previewRequest: CaptureRequest
    private val surfaceTextureListener : SurfaceTextureListener = SurfaceTextureListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        if (tv_texture.isAvailable) {
            openCamera()
        } else {
            tv_texture.surfaceTextureListener = surfaceTextureListener
        }
    }

    override fun onPause() {
        super.onPause()
        closeCamera()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CAMERA_REQUEST_CODE && grantResults.size == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun setupCamera(): String? {
        val backCameraInfo = findBackCamera() ?: return null
        val (cameraId, characteristics) = backCameraInfo
        val configMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val sizes = configMap.getOutputSizes(ImageFormat.JPEG)
        previewSize = sizes.first { it.width > 1000 }
        Log.v(LOG_TAG, "setupCamera(): previewSize = $previewSize")
        return cameraId
    }

    private fun openCamera() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
            return
        }
        Log.v(LOG_TAG, "openCamera(): tv_texture.isAvailable = ${tv_texture.isAvailable}")
        cameraId = setupCamera() ?: return
        val manager = this.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        manager.openCamera(cameraId, StateCallback(), null)
    }

    private fun findBackCamera(): Pair<String, CameraCharacteristics>? {
        val manager = this.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        return manager.cameraIdList
                .map { Pair(it, manager.getCameraCharacteristics(it)) }
                .firstOrNull { it.second.get(CameraCharacteristics.LENS_FACING) ==
                        CameraCharacteristics.LENS_FACING_FRONT }
    }

    private fun createPreviewSession() {
        val texture = tv_texture.surfaceTexture
        Log.v(LOG_TAG, "createPreviewSession(): texture = $texture, cameraDevice = $cameraDevice")
        texture.setDefaultBufferSize(previewSize.width, previewSize.height)
        val surface = Surface(texture)
        previewRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        previewRequestBuilder.addTarget(surface)
        cameraDevice?.createCaptureSession(Arrays.asList(surface),
                object:CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession?) {
                        if (cameraDevice == null) return
                        captureSession = session
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)

                        previewRequest = previewRequestBuilder.build()
                        captureSession?.setRepeatingRequest(previewRequest,
                                CaptureCallback(), null)
                    }
                    override fun onConfigureFailed(session: CameraCaptureSession?) {
                        Log.v(LOG_TAG, "onConfigureFailed()")
                    }
                }, null)
    }

    private fun closeCamera() {
        captureSession?.close()
        captureSession = null
        cameraDevice?.close()
        cameraDevice = null
    }

    private inner class SurfaceTextureListener : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
            Log.v(LOG_TAG, "onSurfaceTextureSizeChanged():")
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
            Log.v(LOG_TAG, "onSurfaceTextureUpdated():")
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
            Log.v(LOG_TAG, "onSurfaceTextureDestroyed():")
            return true
        }

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
            Log.v(LOG_TAG, "onSurfaceTextureAvailable(): width = $width, height = $height")
            openCamera()
        }
    }

    private inner class StateCallback : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice?) {
            Log.v(LOG_TAG, "onOpened(): camera = $camera")
            cameraDevice = camera
            createPreviewSession()
        }

        override fun onDisconnected(camera: CameraDevice?) {
            Log.v(LOG_TAG, "onDisconnected(): camera = $camera")
            camera?.close()
            cameraDevice = null
        }

        override fun onError(camera: CameraDevice?, error: Int) {
            Log.v(LOG_TAG, "onError(): camera = $camera, error = $error")
            onDisconnected(camera)
            finish()
        }
    }

    private inner class CaptureCallback : CameraCaptureSession.CaptureCallback() {
        override fun onCaptureCompleted(session: CameraCaptureSession?, request: CaptureRequest?, result: TotalCaptureResult?) {
            Log.v(LOG_TAG, "onCaptureCompleted()")
        }

        override fun onCaptureFailed(session: CameraCaptureSession?, request: CaptureRequest?, failure: CaptureFailure?) {
            Log.v(LOG_TAG, "onCaptureFailed()")
        }

        override fun onCaptureSequenceCompleted(session: CameraCaptureSession?, sequenceId: Int, frameNumber: Long) {
            Log.v(LOG_TAG, "onCaptureSequenceCompleted()")
        }

        override fun onCaptureStarted(session: CameraCaptureSession?, request: CaptureRequest?, timestamp: Long, frameNumber: Long) {
            Log.v(LOG_TAG, "onCaptureStarted()")
        }

        override fun onCaptureProgressed(session: CameraCaptureSession?, request: CaptureRequest?, partialResult: CaptureResult?) {
            Log.v(LOG_TAG, "onCaptureProgressed()")
        }
    }

    companion object {
        @JvmStatic
        private val LOG_TAG: String = MainActivity::class.java.simpleName
        private const val CAMERA_REQUEST_CODE: Int = 1
    }
}
