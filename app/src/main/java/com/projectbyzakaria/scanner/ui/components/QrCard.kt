package com.projectbyzakaria.scanner.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectbyzakaria.scanner.model.ScanningResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrCard(
    item:ScanningResult,
    modifier : Modifier = Modifier,
    context:Context,
    onClick:()->Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Image(
            bitmap = item.image.asImageBitmap(),
            contentDescription = item.text,
            modifier = Modifier.fillMaxWidth().padding(8.dp).clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.padding(8.dp))

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
                        item.text.let {
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

    }
}