package daniel.testapplication.cameraapplication

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_camera_test.*
import privy.id.cameralibrary.CaptureActivity

class CameraTestActivity : AppCompatActivity() {
    companion object {
        private val PERMISSIONS_REQUIRED = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

        /** Convenience method used to check if all permissions required by this app are granted */
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_test)
        setTheme(privy.id.cameralibrary.R.style.Theme_LivenessVideoCapturePrivyID)

        button.setOnClickListener {
            if (hasPermissions(this)) {
                CaptureActivity.toActivity(this, null, {
                    Log.i("data foto", it[0])
                }, {

                })
            }
        }

    }
}