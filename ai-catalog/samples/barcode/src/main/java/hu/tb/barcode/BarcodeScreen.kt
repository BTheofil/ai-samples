package hu.tb.barcode

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BarcodeScreen(
    vm: BarcodeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = LocalActivity.current

    LaunchedEffect(Unit) {
        ActivityCompat.requestPermissions(
            activity!!, arrayOf(Manifest.permission.CAMERA), 0
        )
    }

    val event = vm.channel.collectAsStateWithLifecycle(initialValue = "")
    LaunchedEffect(event) {
        vm.channel.collect { event ->
            Toast.makeText(
                context,
                event,
                Toast.LENGTH_LONG,
            ).show()
        }
    }

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }
    controller.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    val bitmap = vm.bitmap.collectAsStateWithLifecycle().value

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .size(450.dp)
            ) {
                CameraPreview(
                    modifier = Modifier
                        .fillMaxSize(),
                    controller = controller
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    vm.takePicture(context, controller)
                },
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Camera")
            }
        }
    }
}
