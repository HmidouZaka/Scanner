package com.projectbyzakaria.scanner.ui.screens

import android.Manifest
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.projectbyzakaria.scanner.R
import com.projectbyzakaria.scanner.analyzer.image_qr.SaveBitmap
import com.projectbyzakaria.scanner.model.ScanningResult
import com.projectbyzakaria.scanner.ui.components.QrCard
import com.projectbyzakaria.scanner.ui.components.SelectDialog
import com.projectbyzakaria.scanner.ui.presentassions.MainViewModel
import com.projectbyzakaria.scanner.ui.theme.ScannerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextToQRCodeScreen(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit,
    viewModel: MainViewModel,
) {

    val context = LocalContext.current
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Text to Qr Code") },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(8.dp)
                .fillMaxSize()
        ) {

            Text(
                text = "Enter you Target Text",
                fontSize = 25.sp
            )

            Spacer(modifier = Modifier.padding(8.dp))

            val text by viewModel.textGenerator.collectAsState()
            val image by viewModel.resultImage.collectAsState()


            AnimatedVisibility(visible = image != null) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    image?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        ElevatedButton(
                            onClick = {
                                image?.let { it1 ->
                                    viewModel.insertScanner(
                                        ScanningResult(
                                            text = text,
                                            image = it1,
                                            id =0
                                        )
                                    )

                                    onClickBack()
                                }

                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Add,contentDescription = null)
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(text = "Save")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedButton(
                            onClick = {
                                image?.let { bitmap->
                                    val imageDownLoader = SaveBitmap(bitmap,context)
                                    imageDownLoader.save()
                                    Toast.makeText(context, "Qr code saved as a image", Toast.LENGTH_SHORT).show()
                                } ?: Toast.makeText(context, "no image", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle,contentDescription = null)
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(text = "Download")
                        }
                    }
                }
            }



            OutlinedTextField(
                value = text,
                onValueChange = viewModel::setTextGenerator,
                modifier = Modifier.fillMaxWidth()
            )


            AnimatedVisibility(visible = image != null) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth()){
                    OutlinedButton(
                        onClick = {
                            viewModel.reset()
                        },
                        modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
                    ) {
                        Icon(imageVector = Icons.Default.Clear,contentDescription = null)
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = "Reset")
                    }
                }
            }
        }
    }
}


