package hu.tb.track_object

import android.util.Log
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.objects.DetectedObject

@Composable
fun ObjectDetectionScreen() {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val controller = remember {
        LifecycleCameraController(context)
    }

    val detectedObjects = remember {
        mutableStateOf<List<DetectedObject>>(emptyList())
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(450.dp)
            ) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize(),
                    factory = {
                        PreviewView(it).apply {
                            controller.setImageAnalysisAnalyzer(
                                ContextCompat.getMainExecutor(context),
                                ObjectImageAnalyzer(
                                    detectable = { detectedObject ->
                                        detectedObjects.value = detectedObject
                                    }
                                )
                            )
                            controller.bindToLifecycle(lifecycleOwner)
                            controller.imageAnalysisBackpressureStrategy
                            this.controller = controller
                        }
                    }
                )
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    for (obj in detectedObjects.value) {
                        drawRect(
                            color = Color.Red,
                            topLeft = obj.boundingBox.toComposeRect().topLeft,
                            size = obj.boundingBox.toComposeRect().size,
                            style = Stroke(30f)
                        )
                    }
                }
            }
            Spacer(Modifier.height(64.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                for (objects in detectedObjects.value) {
                    Log.d("MYTAG", "Other objects: " + detectedObjects.value.size.toString())
                    ElevatedCard {
                        Column {
                            for (single in objects.labels) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = single.text
                                    )
                                    Spacer(Modifier.width(32.dp))
                                    Text(
                                        text = single.confidence.toString()
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}