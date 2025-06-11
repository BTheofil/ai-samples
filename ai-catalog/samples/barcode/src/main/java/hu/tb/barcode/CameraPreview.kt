package hu.tb.barcode

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    var barcodeData by remember { mutableStateOf("") }
    LaunchedEffect(barcodeData) {
        Toast.makeText(
            context,
            barcodeData,
            Toast.LENGTH_SHORT
        ).show()
    }

    AndroidView(
        modifier = Modifier
            .fillMaxSize(),
        factory = {
            PreviewView(it).apply {
                controller.imageAnalysisTargetSize = CameraController.OutputSize(AspectRatio.RATIO_16_9)
                controller.setImageAnalysisAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    BarcodeAnalyzer(
                        barcodeData = { data ->
                            barcodeData = data
                        }
                    )
                )
                controller.bindToLifecycle(lifecycleOwner)
                this.controller = controller
            }
        },
    )
}