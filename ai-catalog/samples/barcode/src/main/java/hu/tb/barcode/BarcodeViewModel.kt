package hu.tb.barcode

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
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
import kotlinx.coroutines.launch

class BarcodeViewModel : ViewModel() {

    private val options = BarcodeScannerOptions.Builder()
        .enableAllPotentialBarcodes()
        .build()

    private val _bitmap = MutableStateFlow<Bitmap?>(null)
    val bitmap = _bitmap.asStateFlow()

    private val _channel = Channel<String>()
    val channel = _channel.receiveAsFlow()

    fun takePicture(
        applicationContext: Context,
        controller: LifecycleCameraController
    ) {
        controller.takePicture(
            ContextCompat.getMainExecutor(applicationContext),
            object : OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val imageBitmap = image.toBitmap()
                    val image = InputImage.fromBitmap(imageBitmap, 0)
                    val scanner = BarcodeScanning.getClient(options)
                    scanner.process(image)
                        .addOnSuccessListener { barcodes ->
                            for (barcode in barcodes) {
                                val valueType = barcode.valueType
                                when (valueType) {
                                    Barcode.TYPE_WIFI -> {
                                        val ssid = barcode.wifi!!.ssid
                                        val password = barcode.wifi!!.password
                                        val type = barcode.wifi!!.encryptionType
                                        viewModelScope.launch {
                                            _channel.send("$ssid $password $type")
                                        }
                                    }
                                    Barcode.TYPE_URL -> {
                                        val title = barcode.url!!.title
                                        val url = barcode.url!!.url
                                        viewModelScope.launch {
                                            _channel.send("$title $url")
                                        }
                                    }
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Camera", exception.message?: "error when scanning")
                        }

                    _bitmap.value = imageBitmap
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Camera", "Couldn't take photo: ", exception)
                }
            }
        )
    }
}