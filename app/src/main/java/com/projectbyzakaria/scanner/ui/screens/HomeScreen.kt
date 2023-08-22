package com.projectbyzakaria.scanner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.projectbyzakaria.scanner.ui.components.SelectDialog
import com.projectbyzakaria.scanner.ui.theme.ScannerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {

    var isAddDialogVisible by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Home") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isAddDialogVisible = true }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add")
            }
        }
    ) {

        Column(
            modifier = Modifier.padding(it)
        ) {

        }

    }


    SelectDialog(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background)


            ,
        isVisible = { isAddDialogVisible }
    ) {
        isAddDialogVisible = false
    }

}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ScannerTheme {
        HomeScreen(Modifier.fillMaxSize())
    }
}