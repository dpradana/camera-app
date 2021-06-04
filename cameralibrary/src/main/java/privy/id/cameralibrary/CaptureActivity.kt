package privy.id.cameralibrary

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_camera.*
import privy.id.cameralibrary.base.BaseActivity
import privy.id.cameralibrary.utils.gone
import privy.id.cameralibrary.utils.visible
import java.io.File

class CaptureActivity : BaseActivity() {
    private var imageCapture: ImageCapture? = null

    private lateinit var viewFinder: PreviewView
    private var tvInfoRecord: TextView? = null
    private var tvTitle: TextView? = null
    private var btnTakeRecorder: ConstraintLayout? = null
    private var cvBack: CardView? = null
    private var viewFirst: View? = null
    private var viewBackground: View? = null
    private var viewBackground1: View? = null
    private var viewBackground2: View? = null
    private var clInfo: ConstraintLayout? = null
    private var cvCapture: CardView? = null
    private var viewCapture: View? = null

    private val photos = ArrayList<String>()

    companion object {
        var dataPhoto: ((ArrayList<String>) -> Unit)? = null
        var onFinish:(() -> Unit)? = null
        fun toActivity(
            activity: AppCompatActivity,
            bundle: Bundle? = null,
            callback: ((ArrayList<String>) -> Unit),
            onFinish: (() -> Unit)
        ){
            dataPhoto = callback
            this.onFinish = onFinish
            val intent = Intent(activity, CaptureActivity::class.java)
            bundle?.let {
                intent.putExtras(bundle)
            }
            activity.startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        viewFinder.post { startCamera() }
        onTakePhoto(false)
    }

    override fun setLayout(): Int = R.layout.activity_camera

    override fun setUpVariable() {
        initView()
    }

    override fun internetAvailable() {

    }

    override fun internetUnAvailable() {

    }

    private fun initView() {
        bottom_sheet.gone()

        tvInfoRecord = findViewById(R.id.tv_info_record)
        tvTitle = findViewById(R.id.tv_title_said)
        btnTakeRecorder = findViewById(R.id.button)
        cvBack = findViewById(R.id.cv_back)
        cvCapture = findViewById(R.id.cv_capture)
        viewFinder = findViewById(R.id.view_finder)
        clInfo = findViewById(R.id.cl_info)
        viewFirst = findViewById(R.id.view_first)
        viewCapture = findViewById(R.id.view_capture)
        viewBackground = findViewById(R.id.view_background)
        viewBackground1 = findViewById(R.id.view_background1)
        viewBackground2 = findViewById(R.id.view_background2)
        val clCamera: ConstraintLayout = findViewById(R.id.cl_camera)
        val tvButton: TextView = findViewById(R.id.tv_button)
        tvButton.text = getString(R.string.start_process)

        viewFinder.post { startCamera() }

        onTakePhoto(false)

        btnTakeRecorder?.setOnClickListener {
            btnTakeRecorder?.isEnabled = false
            onTakePhoto()
        }

        cvCapture?.setOnClickListener {
            takePhoto()
        }

        cvBack?.setOnClickListener {
            finish()
            onFinish?.invoke()
        }

    }

    private fun setInfo(title: String, text: String) {
        tvTitle?.text = title
        tvInfoRecord?.text = text
    }

    private fun onTakePhoto(status: Boolean = true) {
        when (status) {
            true -> {
                clInfo?.visible()
                cvBack?.gone()
                viewFirst?.gone()
                bottom_sheet.gone()
                cvCapture?.visible()
                viewBackground?.visible()
                viewBackground1?.visible()
                viewBackground2?.visible()
            }
            else -> {
                clInfo?.gone()
                cvBack?.visible()
                viewFirst?.visible()
                bottom_sheet.visible()
                cvCapture?.gone()
                viewBackground?.gone()
                viewBackground1?.gone()
                viewBackground2?.gone()
                photos.clear()
                setInfo(getString(R.string.title_capture_1), getString(R.string.info_capture_1))
                btnTakeRecorder?.isEnabled = true
            }
        }
    }

    private fun takePhoto() {
        viewCapture?.visible()
        // Get a stable reference of the
        // modifiable image capture use case
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            externalMediaDirs.first(),
            "${System.currentTimeMillis()}.jpeg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener,
        // which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    viewCapture?.gone()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    viewCapture?.gone()
                    photos.add(photoFile.absolutePath)
                    if (photos.size > 1) {
                        dataPhoto?.invoke(photos)
                        finish()
                    } else {
                        setInfo(getString(R.string.title_capture_2), getString(R.string.info_capture_2))
                    }
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {

            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            // Preview
            viewFinder.preferredImplementationMode = PreviewView.ImplementationMode.TEXTURE_VIEW
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.createSurfaceProvider())
                }

            imageCapture = ImageCapture.Builder().build()

            // Select front camera as a default
            val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                exc.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    override fun onBackPressed() {
        if (photos.isEmpty()) {
            onFinish?.invoke()
            super.onBackPressed()
        }
    }
}