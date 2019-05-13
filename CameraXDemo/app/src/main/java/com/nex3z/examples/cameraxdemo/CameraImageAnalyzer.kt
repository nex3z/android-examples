package com.nex3z.examples.cameraxdemo

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer

class CameraImageAnalyzer : ImageAnalysis.Analyzer {
    private var lastAnalyzedTimestamp: Long = 0L

    override fun analyze(image: ImageProxy?, rotationDegrees: Int) {
        val currentTimestamp = System.currentTimeMillis()
        if (currentTimestamp - lastAnalyzedTimestamp <= INTERVAL_MS || image == null) {
            return
        }

        val buffer = image.planes[0].buffer
        val data = buffer.toByteArray()
        val pixels = data.map { it.toInt() and 0xFF }
        Log.v(TAG, "pixels = $pixels")

        lastAnalyzedTimestamp = currentTimestamp
    }

    companion object {
        private val TAG: String = CameraImageAnalyzer::class.java.simpleName
        private val INTERVAL_MS: Long = 100

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()
            val data = ByteArray(remaining())
            get(data)
            return data
        }
    }
}