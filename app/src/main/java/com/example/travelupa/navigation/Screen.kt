package com.example.travelupa.navigation

sealed class Screen(val route: String) {
    object Greeting : Screen("greeting")
    object Login : Screen("login")
    object Register : Screen("register")
    object RekomendasiTempat : Screen("rekomendasi_tempat")
    object Gallery : Screen("gallery")
    object Home : Screen("home")
    object Maps : Screen("maps")
    object Profile : Screen("profile")
    object Detail : Screen("detail/{tempatId}") {
        fun createRoute(tempatId: String) = "detail/$tempatId"
    }
    object AddWisata : Screen("add_wisata")
    object Favorite : Screen("favorite")
}

