package com.projectbyzakaria.scanner.ui.screens

import android.Manifest
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.core.content.ContextCompat
import com.projectbyzakaria.scanner.R
import com.projectbyzakaria.scanner.ui.components.QrCard
import com.projectbyzakaria.scanner.ui.components.SelectDialog
import com.projectbyzakaria.scanner.ui.presentassions.MainViewModel
import com.projectbyzakaria.scanner.ui.theme.ScannerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onClickSelectImage: () -> Unit,
    onClickSelectItem: (Int) -> Unit,
    onClickScanner: () -> Unit,
    onclickAnalise: () -> Unit,
    checkCameraPermissionIsGranted: () -> Boolean = { false },
) {

    val context = LocalContext.current
    var isAddDialogVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val scanners by viewModel.scanners.collectAsState()

    val activityResult = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            if (it) {
                isAddDialogVisible = true
            }
        }
    )





    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Home") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (checkCameraPermissionIsGranted()) {
                        isAddDialogVisible = true
                    } else {
                        activityResult.launch(Manifest.permission.CAMERA)
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add")
            }
        }
    ) {

        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {

            if (scanners.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.nodata),
                        contentDescription = "NoData",
                        modifier = Modifier.size(160.dp)
                    )
                    Spacer(modifier = Modifier.padding(8.dp))

                    Text(text = "No Scanner Saved")
                }
            } else {


                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ){
                    itemsIndexed(scanners){index,item->
                        QrCard(
                            item = item,
                            modifier = Modifier.fillMaxWidth(),
                            context = context
                            ) {
                            onClickSelectItem(index)
                        }
                    }
                }
            }
        }

    }


    SelectDialog(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background),
        isVisible = { isAddDialogVisible },
        onclickAnalise = onclickAnalise,
        onClickScanner = {
            isAddDialogVisible = false
            onClickScanner()
        },
        onClickSelectImage = onClickSelectImage,
        onDismiss = {
            isAddDialogVisible = false
        }
    )

}


