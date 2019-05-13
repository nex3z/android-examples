package com.nex3z.examples.cameraxdemo

import android.graphics.Matrix
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.DisplayMetrics
import android.util.Log
import android.util.Rational
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import kotlinx.android.synthetic.main.fragment_camera.*

class CameraFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        view_finder.post { startCamera() }
    }

    private fun startCamera() {
        CameraX.unbindAll()

        val metrics = DisplayMetrics().also { view_finder.display.getRealMetrics(it) }
        val screenSize = Size(metrics.widthPixels / 2, metrics.heightPixels / 2)
        val screenAspectRatio = Rational(metrics.widthPixels, metrics.heightPixels)
        Log.v(TAG, "startCamera(): screenSize = $screenSize, screenAspectRatio = $screenAspectRatio")

        val previewConfig = PreviewConfig.Builder().apply {
            setTargetResolution(screenSize)
            setTargetAspectRatio(screenAspectRatio)
            setTargetRotation(view_finder.display.rotation)
        }.build()

        val preview = AutoFitPreviewBuilder.build(previewConfig, view_finder)

        val analyserConfig = ImageAnalysisConfig.Builder().apply {
            val analyzerThread = HandlerThread("CameraImageAnalyzer").apply { start() }
            setCallbackHandler(Handler(analyzerThread.looper))
            setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
        }.build()

        val analyzerUseCase = ImageAnalysis(analyserConfig).apply {
            analyzer = CameraImageAnalyzer()
        }

        CameraX.bindToLifecycle(this, preview, analyzerUseCase)
    }

    companion object {
        private val TAG: String = CameraFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = CameraFragment()
    }
}
