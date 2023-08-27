package com.projectbyzakaria.scanner.ui.screens

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.util.Size
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.projectbyzakaria.scanner.R
import com.projectbyzakaria.scanner.analyzer.scanner.QrCodeAnalyzer
import com.projectbyzakaria.scanner.ui.presentassions.MainViewModel
import com.projectbyzakaria.scanner.utils.ScanResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

typealias previewForCamera = androidx.camera.core.Preview.Builder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
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

            val coroutineScope = rememberCoroutineScope()

            var offset: Offset by remember {
                mutableStateOf(Offset(0f, 0f))
            }
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                val scannerUiStates by viewModel.scanningState.collectAsState()
                if (scannerUiStates is ScanResult.Scanning) {
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


                                val qrCode = QrCodeAnalyzer({ text ->
                                    coroutineScope.launch(Dispatchers.Main) {
                                        delay(500)
                                        viewModel.showResult(text)
                                    }
                                }
                                ) { x, y ->
                                    if (offset.x == 0f && x == 0f) {

                                    } else {
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
                                    if (offset.x != 0f) {
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
                } else if (scannerUiStates is ScanResult.Loading) {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                } else if (scannerUiStates is ScanResult.Success) {
                    val image = scannerUiStates.data?.image?.asImageBitmap()
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (image != null) {
                            Image(
                                bitmap = image,
                                contentDescription = "QR Code",
                                modifier = Modifier
                                    .size(400.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                var showAllText by rememberSaveable {
                                    mutableStateOf(false)
                                }
                                Text(
                                    text = scannerUiStates.data?.text ?: "No Result",
                                    textAlign = TextAlign.Center,
                                    maxLines = if (showAllText) 6 else 1,
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable {
                                            showAllText = !showAllText
                                        }
                                        .padding(vertical = 8.dp, horizontal = 4.dp)
                                        .animateContentSize(),
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.padding(8.dp))
                                Box(

                                ) {
                                    Button(
                                        onClick = {
                                            scannerUiStates.data?.text?.let {
                                                val clipboardManager =
                                                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                val clipData =
                                                    ClipData.newPlainText("Copied Text", it)
                                                clipboardManager.setPrimaryClip(clipData)
                                                Toast.makeText(
                                                    context,
                                                    "Text copied to clipboard",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        },
                                        modifier = Modifier.align(Alignment.TopCenter)
                                    ) {
                                        Text(
                                            text = "Copy"
                                        )
                                    }
                                }

                            }

                            Box(
                                Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        scannerUiStates.data?.let { it1 ->
                                            viewModel.insertScanner(
                                                it1
                                            )
                                        }
                                        onClickBack()
                                    },
                                    modifier = Modifier.align(Alignment.BottomEnd)
                                ) {
                                    Text(text = "Save")
                                }
                                Button(onClick = {
                                    viewModel.retry()
                                }, modifier = Modifier.align(Alignment.BottomStart)) {
                                    Text(text = "Reset")
                                }
                            }
                        } else {
                            Box(
                                Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Button(onClick = { viewModel.retry() }) {
                                    Text(text = "Retry")
                                }
                            }
                        }
                    }
                } else {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(onClick = { viewModel.retry() }) {
                            Text(text = "Retry")
                        }
                    }

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


