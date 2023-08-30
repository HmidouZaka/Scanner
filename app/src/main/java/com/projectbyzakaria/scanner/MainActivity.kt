package com.projectbyzakaria.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.projectbyzakaria.scanner.ui.presentassions.MainViewModel
import com.projectbyzakaria.scanner.ui.screens.Details
import com.projectbyzakaria.scanner.ui.screens.HomeScreen
import com.projectbyzakaria.scanner.ui.screens.ImageScannerScreen
import com.projectbyzakaria.scanner.ui.screens.ScannerScreen
import com.projectbyzakaria.scanner.ui.screens.TextToQRCodeScreen
import com.projectbyzakaria.scanner.ui.theme.ScannerTheme
import com.projectbyzakaria.scanner.utils.ScanResult
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
                            checkCameraPermissionIsGranted = ::checkCameraPermissionIsGranted,
                            viewModel = viewModel,
                            onClickSelectItem = { index ->
                                navController.navigate(Screens.Details.name + "/$index")
                            },
                            onClickGenerate = {
                                navController.navigate(Screens.TextToQr.name)
                            }
                        )
                    }
                    composable(
                        route = Screens.Details.name + "/{index}",
                        arguments = listOf(
                            navArgument("index") {
                                this.type = NavType.IntType
                                this.defaultValue = -1
                            }
                        )
                    ) {
                        val index = it.arguments?.getInt("index") ?: -1
                        if (index != -1) {
                            val state by viewModel.itemScanner.collectAsState()
                            LaunchedEffect(key1 = true) {
                                viewModel.findItem(index)
                            }

                            if (state is ScanResult.Success) {
                                state.data?.let { it1 ->
                                    Details(item = it1, viewModel = viewModel) {
                                        navController.popBackStack()
                                    }
                                }
                            } else {
                                Box(
                                    Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }


                        }
                    }
                    composable(route = Screens.Scanner.name) {
                        ScannerScreen(modifier = Modifier.fillMaxSize(), viewModel = viewModel) {
                            navController.popBackStack()
                        }
                    }
                    composable(route = Screens.TextToQr.name) {
                        TextToQRCodeScreen(
                            modifier = Modifier.fillMaxSize(),
                            onClickBack = { navController.popBackStack() },
                            viewModel = viewModel,
                        )
                    }
                    composable(route = Screens.ImageScanner.name) {
                        ImageScannerScreen(
                            modifier = Modifier.fillMaxSize(),
                            viewModel = viewModel
                        ) {
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }


    private fun checkCameraPermissionIsGranted(): Boolean {
        if (isCameraPermissionAllowed) {
            return true
        } else {
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
