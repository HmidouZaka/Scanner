package com.projectbyzakaria.scanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.projectbyzakaria.scanner.ui.presentassions.MainViewModel
import com.projectbyzakaria.scanner.ui.screens.HomeScreen
import com.projectbyzakaria.scanner.ui.theme.ScannerTheme
import com.projectbyzakaria.scanner.utils.Screens

class MainActivity : ComponentActivity() {

    val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScannerTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screens.Home.name
                ) {
                    composable(route = Screens.Home.name){
                        HomeScreen(Modifier.fillMaxSize())
                    }
                    composable(route = Screens.Details.name){

                    }
                    composable(route = Screens.Scanner.name){

                    }
                    composable(route = Screens.ImageInfo.name){

                    }
                    composable(route = Screens.ImageScanner.name){

                    }
                }
            }
        }
    }
}
