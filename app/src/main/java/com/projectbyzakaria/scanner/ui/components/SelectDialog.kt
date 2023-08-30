package com.projectbyzakaria.scanner.ui.components

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.ImageLoader
import coil.compose.rememberImagePainter

import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.projectbyzakaria.scanner.R
import com.projectbyzakaria.scanner.ui.theme.ScannerTheme

@Composable
fun SelectDialog(
    modifier: Modifier = Modifier,
    isVisible: () -> Boolean,
    onDismiss: () -> Unit,
    onClickSelectImage: () -> Unit,
    onClickScanner: () -> Unit,
    onClickGenerate: () -> Unit,
) {

    if (isVisible()) {
        Dialog(onDismissRequest = onDismiss) {
            DialogContent(
                modifier = modifier.padding(top = 14.dp),
                onClickSelectImage = onClickSelectImage,
                onClickScanner = onClickScanner,
                onClickGenerate = onClickGenerate
            )
        }
    }

}

@Composable
fun DialogContent(
    modifier: Modifier, onClickSelectImage: () -> Unit,
    onClickScanner: () -> Unit,
    onClickGenerate: () -> Unit,
) {

    val context = LocalContext.current
    Column(
        modifier = modifier
    ) {

        val imageLoader = ImageLoader.Builder(context)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()


        Image(
            painter = rememberImagePainter(
                data = R.drawable.animation_llmobdls_small,
                imageLoader = imageLoader
            ),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp),
            contentScale = ContentScale.Crop
        )



        ElevatedButton(
            onClick = { onClickSelectImage()},
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.add_image_icon),
                contentDescription = "add image",
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Select Image",
                fontSize = 20.sp,
                fontWeight = FontWeight.W500
            )
        }



        ElevatedButton(
            onClick = { onClickScanner() },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),

            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.get_image_info_icon),
                contentDescription = "Scan QR Code",
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Scan QR Code",
                fontSize = 20.sp,
                fontWeight = FontWeight.W500
            )
        }



        ElevatedButton(
            onClick = { onClickGenerate ()},
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.scan_text),
                contentDescription = "Generate QR Code",
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Generate QR Code",
                fontSize = 20.sp,
                fontWeight = FontWeight.W500
            )
        }
    }
}


