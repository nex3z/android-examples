package com.nex3z.examples.cameraxdemo

import android.content.Context
import android.graphics.Matrix
import android.hardware.display.DisplayManager
import android.util.Size
import android.view.*
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import java.lang.ref.WeakReference
import java.util.*

class AutoFitPreviewBuilder(
    config: PreviewConfig,
    textureViewRef: WeakReference<TextureView>
) {

    private var viewFinderDisplay: Int = -1
    private var viewFinderRotation: Int = 0
    private var bufferRotation: Int = 0
    private var viewFinderDimens: Size = Size(0, 0)
    private var bufferDimens: Size = Size(0, 0)

    val useCase: Preview = Preview(config).apply {
        onPreviewOutputUpdateListener = Preview.OnPreviewOutputUpdateListener {
            val textureView = textureViewRef.get() ?: return@OnPreviewOutputUpdateListener
            val parent = textureView.parent as ViewGroup
            parent.removeView(textureView)
            parent.addView(textureView, 0)

            textureView.surfaceTexture = it.surfaceTexture
            bufferRotation = it.rotationDegrees
            val rotation = getDisplaySurfaceRotation(textureView.display)
            updateTransform(textureView, rotation, it.textureSize, viewFinderDimens)
        }
    }

    init {
        val textureView = textureViewRef.get() ?: throw IllegalArgumentException()
        viewFinderDisplay = textureView.display.displayId
        viewFinderRotation = getDisplaySurfaceRotation(textureView.display)

        textureView.addOnLayoutChangeListener { view, left, top, right, bottom, _, _, _, _ ->
            val internalTextureView = view as TextureView
            val newViewFinderDimens = Size(right - left, bottom - top)
            val rotation = getDisplaySurfaceRotation(internalTextureView.display)
            updateTransform(internalTextureView, rotation, bufferDimens, newViewFinderDimens)
        }

        val displayManager = textureView.context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val displayListener = TextureDisplayListener(displayManager, textureViewRef)
        displayManager.registerDisplayListener(displayListener, null)

        textureView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View?) = Unit
            override fun onViewDetachedFromWindow(view: View?) {
                displayManager.unregisterDisplayListener(displayListener)
            }
        })
    }

    private fun updateTransform(textureView: TextureView, rotation: Int, newBufferDimens: Size,
                                newViewFinderDimens: Size) {
        if (rotation == viewFinderRotation && Objects.equals(newBufferDimens, bufferDimens)
            && Objects.equals(newViewFinderDimens, viewFinderDimens)) {
            return
        }

        viewFinderRotation = rotation

        if (newBufferDimens.width == 0 || newBufferDimens.height == 0) {
            // Invalid buffer dimens - wait for valid inputs before setting matrix
            return
        } else {
            // Update internal field with new inputs
            bufferDimens = newBufferDimens
        }

        if (newViewFinderDimens.width == 0 || newViewFinderDimens.height == 0) {
            // Invalid view finder dimens - wait for valid inputs before setting matrix
            return
        } else {
            // Update internal field with new inputs
            viewFinderDimens = newViewFinderDimens
        }

        val matrix = Matrix()

        // Compute the center of the view finder
        val centerX = viewFinderDimens.width / 2f
        val centerY = viewFinderDimens.height / 2f

        // Correct preview output to account for display rotation
        matrix.postRotate(-viewFinderRotation.toFloat(), centerX, centerY)

        // Buffers are rotated relative to the device's 'natural' orientation: swap width and height
        val bufferRatio = bufferDimens.height / bufferDimens.width.toFloat()

        val scaledWidth: Int
        val scaledHeight: Int
        // Match longest sides together -- i.e. apply center-crop transformation
        if (viewFinderDimens.width > viewFinderDimens.height) {
            scaledHeight = viewFinderDimens.width
            scaledWidth = Math.round(viewFinderDimens.width * bufferRatio)
        } else {
            scaledHeight = viewFinderDimens.height
            scaledWidth = Math.round(viewFinderDimens.height * bufferRatio)
        }

        // Compute the relative scale value
        val xScale = scaledWidth / viewFinderDimens.width.toFloat()
        val yScale = scaledHeight / viewFinderDimens.height.toFloat()

        // Scale input buffers to fill the view finder
        matrix.preScale(xScale, yScale, centerX, centerY)

        // Finally, apply transformations to our TextureView
        textureView.setTransform(matrix)
    }

    private inner class TextureDisplayListener(
        private val displayManager: DisplayManager,
        private val viewFinderRef: WeakReference<TextureView>
    ) :  DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit

        override fun onDisplayRemoved(displayId: Int) = Unit

        override fun onDisplayChanged(displayId: Int) {
            val viewFinder = viewFinderRef.get() ?: return
            if (displayId != viewFinderDisplay) {
                val display = displayManager.getDisplay(displayId)
                val rotation = getDisplaySurfaceRotation(display)
                updateTransform(viewFinder, rotation, bufferDimens, viewFinderDimens)
            }
        }
    }

    companion object {
        private val TAG: String = AutoFitPreviewBuilder::class.java.simpleName

        fun getDisplaySurfaceRotation(display: Display) = when (display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> 0
        }

        fun build(config: PreviewConfig, viewFinder: TextureView) =
            AutoFitPreviewBuilder(config, WeakReference(viewFinder)).useCase
    }
}