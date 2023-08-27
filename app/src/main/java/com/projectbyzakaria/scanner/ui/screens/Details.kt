package com.projectbyzakaria.scanner.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectbyzakaria.scanner.R
import com.projectbyzakaria.scanner.model.ScanningResult
import com.projectbyzakaria.scanner.ui.components.DialogValidate
import com.projectbyzakaria.scanner.ui.presentassions.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Details(
    modifier: Modifier = Modifier,
    item: ScanningResult,
    viewModel: MainViewModel,
    onClickBack: () -> Unit,
) {
    val context = LocalContext.current
    var isShowDeleteDialog by rememberSaveable {
        mutableStateOf(false)
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Details") },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) {


        Column(
            modifier = modifier.padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                bitmap = item.image.asImageBitmap(),
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
                    text = item.text,
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
                            item.text?.let {
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
                        isShowDeleteDialog = true
                    },
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Text(text = "Delete")
                }
            }

        }
    }

    if (isShowDeleteDialog) {
        DialogValidate(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(8.dp)
                ).background(MaterialTheme.colorScheme.onPrimary),
            title = "Delete Scanner",
            content = "Are you sure to delete the scanner",
            cancelButtonText = "Cancel",
            deleteButtonText = "Delete",
            onClickDelete = {
                isShowDeleteDialog = false
                viewModel.deleteScanner(item)

                onClickBack()

            },
            onClickCancel = { isShowDeleteDialog = false })
        {
            isShowDeleteDialog = false
        }
    }
}





