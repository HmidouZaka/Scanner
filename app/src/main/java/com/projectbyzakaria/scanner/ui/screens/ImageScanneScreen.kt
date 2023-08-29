package com.projectbyzakaria.scanner.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.projectbyzakaria.scanner.analyzer.image_qr.ImageScanner
import com.projectbyzakaria.scanner.analyzer.scanner.Generator
import com.projectbyzakaria.scanner.model.ScanningResult
import com.projectbyzakaria.scanner.ui.presentassions.MainViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ImageScannerScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onClickBack: () -> Unit,
) {

    var result: String? by rememberSaveable {
        mutableStateOf(null)
    }

    val context = LocalContext.current
    var imageUrlString: String? by rememberSaveable {
        mutableStateOf(null)
    }

    val activityResult = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            val data = it.data?.data
            data?.let { uri: Uri ->
                imageUrlString = uri.toString()
            }

        }
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Scan QR Image") },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) {

        if (result == null) {


            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedContent(targetState = imageUrlString, label = "s") {
                    if (it == null) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_PICK)
                                intent.type = "image/*"
                                activityResult.launch(intent)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF03A9F4),
                                contentColor = Color(0xFFFFFFFF)
                            )
                        ) {
                            Text(
                                text = "Select Image",
                                fontSize = 20.sp
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxSize(), verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(context).data(imageUrlString).build()
                                ),
                                contentDescription = "image ",
                                modifier = Modifier
                                    .size(300.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        val intent = Intent(Intent.ACTION_PICK)
                                        intent.type = "image/*"
                                        activityResult.launch(intent)
                                    }
                            )

                            Spacer(modifier = Modifier.height(30.dp))

                            ElevatedButton(
                                onClick = {
                                    imageUrlString?.toUri()?.let { uri: Uri ->
                                        val imageScanner = ImageScanner(context = context,uri = uri)
                                        result = imageScanner.scan()
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4CAF50),
                                    contentColor = Color(0xFFFFFFFF)
                                )
                            ) {
                                Text(text = "Start Scanning")
                            }
                        }
                    }
                }
            }
        } else {

            var image = remember {
                val generator = Generator(result!!)
                generator.generate()
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (image != null) {
                    Image(
                        bitmap = image.asImageBitmap(),
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
                            text = result ?: "No Result",
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
                                    result.let {
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
                                result?.let { it1 ->
                                    ScanningResult(it1, image, 0).let { it1 ->
                                        viewModel.insertScanner(
                                            it1
                                        )
                                    }
                                }
                                imageUrlString = null
                                result = null
                                onClickBack()
                            },
                            modifier = Modifier.align(Alignment.BottomEnd)
                        ) {
                            Text(text = "Save")
                        }
                        Button(onClick = {
                            imageUrlString = null
                            result = null
                        }, modifier = Modifier.align(Alignment.BottomStart)) {
                            Text(text = "Reset")
                        }
                    }
                } else {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(onClick = {
                            imageUrlString = null
                            result = null
                        }) {
                            Text(text = "Retry")
                        }
                    }
                }
            }
        }
    }
}


