package com.nex3z.examples.cameraxdemo

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            RC_PERMISSION_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    naviCameraFragment()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun init() {
        val hasCamera = checkCamera()
        Log.v(TAG, "init(): hasCamera = $hasCamera")
        if (!hasCamera) {
            return
        }

        val rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        Log.v(TAG, "init(): rc = $rc")
        if (rc == PackageManager.PERMISSION_GRANTED) {
            naviCameraFragment()
        } else {
            requestCameraPermission()
        }
    }

    private fun checkCamera(): Boolean {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    private fun requestCameraPermission() {
        val permissions = arrayOf(Manifest.permission.CAMERA)
        ActivityCompat.requestPermissions(this, permissions, RC_PERMISSION_CAMERA)
    }

    private fun naviCameraFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CameraFragment.newInstance())
            .commit()
    }

    companion object {
        private val TAG: String = MainActivity::class.java.simpleName
        private const val RC_PERMISSION_CAMERA: Int = 2
    }
}
