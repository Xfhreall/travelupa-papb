package com.example.travelupa.ui.screen

import androidx.compose.runtime.Composable

@Composable
fun RekomendasiTempatScreen(
    onBackToLogin: () -> Unit,
    onNavigateToGallery: () -> Unit
) {
    HomeScreen(
        onNavigateToDetail = { _ ->
        }
    )
}
