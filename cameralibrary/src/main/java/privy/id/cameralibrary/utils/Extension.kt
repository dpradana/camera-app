package privy.id.cameralibrary.utils

import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast


/** Check if this device has a camera */
fun Context.checkCameraHardware(): Boolean {
    return this.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible(){
    visibility = View.INVISIBLE
}
