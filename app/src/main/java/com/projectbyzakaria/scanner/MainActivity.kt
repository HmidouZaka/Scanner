package com.projectbyzakaria.scanner

import android.Manifest
import android.content.pm.PackageManager
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
import com.projectbyzakaria.scanner.ui.screens.ImageScannerScreen
import com.projectbyzakaria.scanner.ui.screens.ScannerScreen
import com.projectbyzakaria.scanner.ui.theme.ScannerTheme
import com.projectbyzakaria.scanner.utils.Screens
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel by viewModels<MainViewModel>()

    var isCameraPermissionAllowed = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScannerTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screens.Home.name
                ) {
                    composable(route = Screens.Home.name) {
                        HomeScreen(
                            modifier = Modifier.fillMaxSize(),
                            onClickSelectImage = {
                                navController.navigate(Screens.ImageScanner.name)
                            },
                            onClickScanner = {
                                navController.navigate(Screens.Scanner.name)
                            },
                            onclickAnalise = {
                                navController.navigate(Screens.ImageInfo.name)
                            },
                            checkCameraPermissionIsGranted = ::checkCameraPermissionIsGranted
                        )
                    }
                    composable(route = Screens.Details.name) {

                    }
                    composable(route = Screens.Scanner.name) {
                        ScannerScreen(modifier = Modifier.fillMaxSize()) {
                            navController.popBackStack()
                        }
                    }
                    composable(route = Screens.ImageInfo.name) {

                    }
                    composable(route = Screens.ImageScanner.name) {
                        ImageScannerScreen(
                            modifier = Modifier.fillMaxSize()
                        ){
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }


    private fun checkCameraPermissionIsGranted(): Boolean {
        if (isCameraPermissionAllowed){
            return true
        }else{
            val checkCameraPermissionIsGranted =
                    androidx.core.content.ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
            isCameraPermissionAllowed = checkCameraPermissionIsGranted
            return checkCameraPermissionIsGranted
        }
    }
}
