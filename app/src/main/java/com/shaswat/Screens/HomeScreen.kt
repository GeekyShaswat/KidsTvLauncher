package com.shaswat.Screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.shaswat.data.AppInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(
    navController: NavHostController,
    approvedApps: List<AppInfo>,
    onAppClick: (String) -> Unit,
    onSettingsRequest: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Top app bar with title and settings button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Kids TV",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            var isFocused by remember { mutableStateOf(false) }
            val scale by animateFloatAsState(targetValue = if (isFocused) 1.1f else 1.0f, label = "")
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                modifier = Modifier
                    .size(32.dp)
                    .scale(scale)
                    .onFocusChanged { isFocused = it.isFocused }
                    .focusable()
                    .border(
                        if (isFocused) {
                            BorderStroke(4.dp, Color.Yellow)
                        } else BorderStroke(0.dp, Color.Transparent),
                    )
                    .clickable{
                        onSettingsRequest()
                    },
                tint = Color.White
            )
        }

        // Grid of apps
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(approvedApps) { app ->
                AppItem(app = app, onClick = { onAppClick(app.packageName) })
            }
        }
    }
}
@Composable
fun AppItem(app: AppInfo, onClick: () -> Unit) {
    val context = LocalContext.current
    val bitmap = app.icon.toBitmap()
    val imageBitmap = bitmap.asImageBitmap()

    // Track focus state
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (isFocused) 1.1f else 1.0f, label = "")

    Card(
        onClick = onClick,
        modifier = Modifier
            .size(160.dp)
            .scale(scale)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable(),
        shape = RoundedCornerShape(16.dp),
        border = if (isFocused) BorderStroke(4.dp, Color.Yellow) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isFocused) 8.dp else 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Image(
                bitmap = imageBitmap,
                contentDescription = app.appName,
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 8.dp)
            )

            Text(
                text = app.appName,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}
