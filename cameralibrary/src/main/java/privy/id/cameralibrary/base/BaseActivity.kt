package privy.id.cameralibrary.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import privy.id.cameralibrary.R

abstract class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_LivenessVideoCapturePrivyID)
        setContentView(setLayout())
        setUpVariable()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    abstract fun setLayout(): Int
    abstract fun setUpVariable()

    //  internet callbacks
    abstract fun internetAvailable()

    abstract fun internetUnAvailable()

    fun setNavBarColor(colorAccent: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setStatusBarColor(Color.parseColor(colorAccent))
        }
    }

}
