package com.shaswat.ViewModel
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.shaswat.data.AppInfo
import com.shaswat.data.PinRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class Screen {
    HOME,
    EXIT_PROTECTION,
    SETTINGS
}

data class MainUiState(
    val currentScreen: Screen = Screen.HOME,
    val shouldExitApp: Boolean = false,  // Flag to indicate if app should exit
    val showExitDialog: Boolean = true   // Flag to determine if EXIT_PROTECTION is for exit or settings
)

class MainViewModel(
    private val pinRepository: PinRepository = PinRepository()
) : ViewModel() {

    val pinVerified = MutableStateFlow(false)

    val exitPinVerified = MutableStateFlow(false)

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    // List of approved app package names
    private val approvedPackages = listOf(
        "com.vooks",
        "com.sonyliv",
        "com.rumble.battles",
        "com.movile.playkids",
        "com.jio.media.jiobeats",
        "in.startv.hotstar",
        "com.jio.media.stb.ondemand",
        "com.fancode.tv",
        "ott.mysportlive",
        "gg.rooter.androidtv",
        "net.colorcity.loolookids",
        "com.apptebo.math",
        "com.soundcloud.android",
        "com.magikbee.google_kidsbeetv_learning"
    )

    fun getApprovedApps(packageManager: PackageManager): List<AppInfo> {
        val installedApps = mutableListOf<AppInfo>()

        for (packageName in approvedPackages) {
            try {
                val appInfo = packageManager.getApplicationInfo(packageName, 0)
                val appName = packageManager.getApplicationLabel(appInfo).toString()
                val icon = packageManager.getApplicationIcon(appInfo)

                installedApps.add(AppInfo(packageName, appName, icon))
            } catch (e: PackageManager.NameNotFoundException) {
                // App not installed, skip
            }
        }

        return installedApps
    }

    fun showExitProtection(navController: NavController) {
        _uiState.update { it.copy(currentScreen = Screen.EXIT_PROTECTION, showExitDialog = true) }
        navController.navigate("exit_protection_to_exitApp")
    }
    // For settings protection
    fun showSettingsProtection() {
        _uiState.update { it.copy(currentScreen = Screen.EXIT_PROTECTION, showExitDialog = false) }
    }
    fun settingScreen(){
        _uiState.update { it.copy(currentScreen = Screen.SETTINGS) }
    }

    fun dismissExitProtection() {
        _uiState.update { it.copy(currentScreen = Screen.HOME) }
    }

    fun validatePin(enteredPin: String, context: Context) {
        viewModelScope.launch {
            if (pinRepository.validatePin(enteredPin)) {
                if (uiState.value.showExitDialog) {
                    // Exit protection is active, so mark the exit PIN as verified
                    exitPinVerified.value = true
                    _uiState.update { it.copy(shouldExitApp = true) }
                } else {
                    // Settings protection is active, so mark the settings PIN as verified
                    pinVerified.value = true
                    _uiState.update { it.copy(currentScreen = Screen.SETTINGS) }
                }
            } else {
                // Inform the user of an incorrect PIN
                Toast.makeText(context, "Incorrect PIN", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun navigateToHome() {
        _uiState.update { it.copy(currentScreen = Screen.HOME) }
    }

}