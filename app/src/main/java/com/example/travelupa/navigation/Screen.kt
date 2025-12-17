package com.example.travelupa.navigation

sealed class Screen(val route: String) {
    object Greeting : Screen("greeting")
    object Login : Screen("login")
    object Register : Screen("register")
    object RekomendasiTempat : Screen("rekomendasi_tempat")
    object Gallery : Screen("gallery")
}
