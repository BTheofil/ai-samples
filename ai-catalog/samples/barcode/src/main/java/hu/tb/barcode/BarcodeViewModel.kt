package hu.tb.barcode

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BarcodeViewModel : ViewModel() {

    private val options = BarcodeScannerOptions.Builder()
        .enableAllPotentialBarcodes()
        .build()

    private val _inputImage = MutableStateFlow<InputImage?>(null)
    val inputImage = _inputImage.asStateFlow()

    private val _channel = Channel<String>()
    val channel = _channel.receiveAsFlow()

    init {
        barcodeProcessImage()
    }

    fun takePicture(
        applicationContext: Context,
        controller: LifecycleCameraController
    ) {
        controller.takePicture(
            ContextCompat.getMainExecutor(applicationContext),
            object : OnImageCapturedCallback() {
                @OptIn(ExperimentalGetImage::class)
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val mediaImage = image.image
                    if (mediaImage != null) {
                        val processImage =
                            InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)

                        /*_inputImage.update {
                            processImage
                        }*/

                        image.close()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Camera", "Couldn't take photo: ", exception)
                }
            }
        )
    }

    private fun barcodeProcessImage() {
        viewModelScope.launch {
            inputImage.collect { image ->
                image?.let {
                    val result = BarcodeScanning.getClient(options).process(image)
                    _channel.send("Sending: " + result.isSuccessful)
                }
            }
        }
    }
}