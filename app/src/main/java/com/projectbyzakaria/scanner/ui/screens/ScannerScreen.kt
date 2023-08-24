package com.projectbyzakaria.scanner.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.projectbyzakaria.scanner.analyzer.scanner.QrCodeAnalyzer

typealias previewForCamera = androidx.camera.core.Preview.Builder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    modifier: Modifier = Modifier,
    onClickBack :()->Unit
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Scan QR Code") },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) {
        var result by rememberSaveable {
            mutableStateOf("kjkj")
        }

        val context = LocalContext.current
        val composeLifeCycle = LocalLifecycleOwner.current
        val cameraProvide = remember {
            ProcessCameraProvider.getInstance(context)
        }

        val isAllow = remember {
            ContextCompat.checkSelfPermission(context,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        }

        if (isAllow){
            Column(
                modifier = Modifier.padding(it).fillMaxSize()
            ) {
                AndroidView(factory = {context->
                    val previewView  = PreviewView(context)
                    val preview = previewForCamera().build()
                    val selectore = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()

                    preview.setSurfaceProvider(previewView.surfaceProvider)
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setTargetResolution(Size(previewView.width,previewView.height))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                    val qrCode = QrCodeAnalyzer{
                        result = it
                    }
                    imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context),qrCode)

                    try {
                        cameraProvide.get().bindToLifecycle(
                            composeLifeCycle,
                            selectore,
                            preview,
                            imageAnalysis
                        )
                    }catch (ex:Exception){
                        ex.printStackTrace()
                    }
                    previewView
                }, modifier = Modifier.weight(1f)){

                }

                Text(text = result, fontSize = 30.sp, modifier = Modifier.padding(20.dp))
            }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun ScannerScreenPreview() {
    ScannerScreen(
        Modifier.fillMaxSize()
    ) {}
}