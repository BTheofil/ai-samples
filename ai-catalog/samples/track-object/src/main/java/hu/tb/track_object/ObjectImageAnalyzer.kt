package hu.tb.track_object

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

class ObjectImageAnalyzer(
    private val detectable: (List<DetectedObject>) -> Unit
) : ImageAnalysis.Analyzer {

    private val options = ObjectDetectorOptions.Builder()
        .enableClassification()
        //.enableMultipleObjects()
        .build()

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            ObjectDetection.getClient(options).process(image)
                .addOnSuccessListener { detectableObjects ->
                    //Log.d("MYTAG", "success")
                    detectable(detectableObjects)
                }
                .addOnCanceledListener {
                    //Log.d("MYTAG", "failed")
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }
}