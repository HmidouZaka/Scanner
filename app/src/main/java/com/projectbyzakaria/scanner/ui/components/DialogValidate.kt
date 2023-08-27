package com.projectbyzakaria.scanner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun DialogValidate(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    cancelButtonText: String,
    deleteButtonText: String,
    onClickDelete: () -> Unit,
    onClickCancel: () -> Unit,
    onDismissRequest: () -> Unit,
) {

    Dialog(onDismissRequest = onDismissRequest) {
        DialogValidateContent(
            modifier,
            title,
            content,
            cancelButtonText,
            deleteButtonText,
            onClickDelete,
            onClickCancel
        )
    }

}


@Composable
fun DialogValidateContent(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    cancelButtonText: String,
    deleteButtonText: String,
    onClickDelete: () -> Unit,
    onClickCancel: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(8.dp),
            fontSize = 23.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Text(
            text = content,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                onClick = onClickCancel
            ) {
                Text(text = cancelButtonText)
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Button(
                onClick = onClickDelete
            ) {
                Text(text = deleteButtonText)
            }

        }


    }
}
