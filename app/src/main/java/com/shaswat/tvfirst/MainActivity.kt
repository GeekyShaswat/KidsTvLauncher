package com.shaswat.tvfirst

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.shaswat.tvfirst.ui.theme.TVFirstTheme
import com.shaswat.ViewModel.MainViewModel
import com.shaswat.Navigation.Navigation
import com.shaswat.ViewModel.Screen

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TVFirstTheme {
                // Create a single navController instance here
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Pass the navController to your Navigation composable
                    Navigation(mainViewModel, navController,context)
                }
                // Back press handling using the same navController instance
                onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (mainViewModel.uiState.value.currentScreen == Screen.HOME) {
                            mainViewModel.showExitProtection(navController)
                        }
                        else if (mainViewModel.uiState.value.currentScreen == Screen.SETTINGS) {
                            mainViewModel.navigateToHome()
                        }
                        else if (mainViewModel.uiState.value.currentScreen == Screen.EXIT_PROTECTION) {
                            mainViewModel.navigateToHome()
                        }
                        else {
                            navController.popBackStack()
                        }
                    }
                })
            }
        }
    }


}
