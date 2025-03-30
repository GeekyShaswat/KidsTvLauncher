package com.shaswat.Navigation

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shaswat.Screens.HomeScreen
import com.shaswat.Screens.SettingsScreen
import com.shaswat.Screens.ExitProtectionDialog
import com.shaswat.ViewModel.MainViewModel

@Composable
fun Navigation(viewModel: MainViewModel, navController: NavHostController, context: Context) {
    val packageManager = context.packageManager

    // Navigation Graph
    NavHost(navController, startDestination = "home") {

        // Home Screen
        composable("home") {
            HomeScreen(
                navController = navController,
                approvedApps = viewModel.getApprovedApps(packageManager),
                onAppClick = { packageName ->
                    launchApp(packageName, packageManager, context) // Launch app through function
                },
                onSettingsRequest = {
                    viewModel.showSettingsProtection()
                    navController.navigate("exit_protection_to_settings")
                }
            )
        }

        // Settings Screen
        composable("settings") {
            SettingsScreen(
                navController = navController,
                onBack = {
                    viewModel.navigateToHome()
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                //currently not implemented
                onChangePinClick = { /* Handle change PIN click */ },
                onManageAppsClick = { /* Handle manage apps click */ }
            )
        }

        // Exit Protection Screen for Settings
        composable("exit_protection_to_settings") {
            ExitProtectionDialog(
                onDismiss = {
                    viewModel.dismissExitProtection()
                    viewModel.pinVerified.value = false
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onPinSubmit = { pin ->
                    viewModel.validatePin(pin, context)
                    if (viewModel.pinVerified.value) {
                        viewModel.settingScreen()
                        navController.navigate("settings") {
                            popUpTo("home") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                title = "Go To Settings",
                description = "Enter your 4-digit PIN to go to settings"
            )
        }

        // Exit Protection Screen for Exiting the App
        composable("exit_protection_to_exitApp") {
            // Observe the exit verification flag (assumed to be a StateFlow in your ViewModel)
            val exitVerified = viewModel.exitPinVerified.collectAsState().value
            // When exit PIN is verified, finish the app
            LaunchedEffect(exitVerified) {
                if (exitVerified) {
                    FinishApp(context)
                }
            }
            ExitProtectionDialog(
                onDismiss = {
                    viewModel.dismissExitProtection()
                    // Reset exit verification flag
                    viewModel.exitPinVerified.value = false
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onPinSubmit = { pin ->
                    viewModel.validatePin(pin, context)
                    viewModel.exitPinVerified.value = true
                    // Validation should update viewModel.exitPinVerified if appropriate
                    // The LaunchedEffect above will trigger finish when true
                },
                title = "Exit the App",
                description = "Enter your 4-digit PIN to exit the App"
            )
        }
    }
}

/**
 * Helper function to launch apps.
 */
private fun launchApp(packageName: String, packageManager: PackageManager, context: Context) {
    try {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.let { context.startActivity(it) }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Finishes the app by casting the context to Activity and calling finish().
 */
fun FinishApp(context: Context) {
    (context as? Activity)?.finish()
}
