# Kids TV Launcher

A kid-friendly Android TV launcher app developed with Kotlin and Jetpack Compose that provides a safe environment for children by restricting access to approved apps only and implementing exit protection.

## Features

- Kid-friendly UI with large, colorful app icons
- Grid-based home screen showing only pre-approved apps
- PIN-protected exit to prevent children from leaving the launcher
- D-pad navigation support for TV remote controls
- Smooth animations and transitions

## Setup Instructions

1. Clone the repository:
   ```
   git clone https://github.com/GeekyShaswat/KidsTvLauncher.git
   ```

2. Open the project in Android Studio (Arctic Fox or later recommended)

3. Build and run the app on an Android TV device or emulator

## Implementation Details

- **Architecture**: MVVM (Model-View-ViewModel)
- **UI Framework**: Jetpack Compose with TV-specific components
- **Default PIN**: 1234 (can be changed in settings)

## App Restriction
The launcher only displays pre-approved apps that are suitable for children. This is implemented by maintaining a whitelist of package names in the `MainViewModel`.

## Exit Protection
To access settings or exit the launcher, a long-press on any empty area of the screen for 5 seconds will trigger the PIN entry dialog. This prevents children from accidentally or intentionally exiting the protected environment.

## Customization
Parents can customize the launcher through the settings screen (after PIN authentication):
- Change the PIN code
- Manage which apps are visible to children

## Requirements
- Android Studio Electric Eel or later
- Android TV device or emulator running Android 5.0 (API level 21) or higher
