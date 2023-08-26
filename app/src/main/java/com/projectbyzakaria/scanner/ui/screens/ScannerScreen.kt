package com.projectbyzakaria.scanner.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.util.Size
import android.view.ViewGroup
import android.widget.ImageView
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.projectbyzakaria.scanner.R
import com.projectbyzakaria.scanner.analyzer.scanner.QrCodeAnalyzer

typealias previewForCamera = androidx.camera.core.Preview.Builder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit,
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


        val context = LocalContext.current
        val composeLifeCycle = LocalLifecycleOwner.current

        val cameraProvide = remember {
            ProcessCameraProvider.getInstance(context)
        }

        val isAllow = remember {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        }

        if (isAllow) {

            var offset: Offset by remember {
                mutableStateOf(Offset(0f, 0f))
            }
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    AndroidView(
                        factory = { context ->
                            val previewView = PreviewView(context)

                            previewView.layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )

                            val preview = previewForCamera().build()

                            val selectore = CameraSelector.Builder()
                                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                .build()

                            preview.setSurfaceProvider(previewView.surfaceProvider)

                            val imageAnalysis = ImageAnalysis.Builder()
                                .setTargetResolution(Size(1200, 1600))

                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()

                            val qrCode = QrCodeAnalyzer(
                                { Log.d("ssssssssssssssssssss", it) }
                            ) { x, y ->
                                if (offset.x == 0f && x == 0f) {

                                }else{
                                    offset = Offset(x, y)
                                }
                            }

                            imageAnalysis.setAnalyzer(
                                ContextCompat.getMainExecutor(context),
                                qrCode
                            )

                            try {
                                cameraProvide.get().bindToLifecycle(
                                    composeLifeCycle,
                                    selectore,
                                    preview,
                                    imageAnalysis
                                )
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }

                            previewView
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {

                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .drawBehind {
//                                drawCircle(Color.Blue,200f,offset)
                                if (offset.x != 0f){
                                    drawRoundRect(
                                        Color.Black,
                                        offset,
                                        androidx.compose.ui.geometry.Size(700f, 700f),
                                        CornerRadius(50f, 50f),
                                        style = Stroke(width = 20f, cap = StrokeCap.Round)
                                    )
                                }

                            }
                    )

                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Permissions Dented")
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