package com.example.travelupa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.travelupa.navigation.Screen
import androidx.room.Room
import com.example.travelupa.data.database.AppDatabase
import com.example.travelupa.data.seeder.WisataSeeder
import com.example.travelupa.ui.components.TravelupaBottomBar
import com.example.travelupa.ui.screen.*
import com.example.travelupa.ui.theme.TravelupaTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        
        // Run seeder on first launch (use forceReseed once to update data with lat/long)
        CoroutineScope(Dispatchers.IO).launch {
            WisataSeeder.forceReseed(this@MainActivity)
        }
        
        val currentUser = FirebaseAuth.getInstance().currentUser
        setContent {
            TravelupaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(currentUser)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(currentUser: FirebaseUser?) {
    val navController = rememberNavController()
    val context = LocalContext.current
    
    // Initialize Room database
    val db = remember {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "travelupa-database"
        ).build()
    }
    val imageDao = db.imageDao()
    
    Scaffold(
        bottomBar = {
            TravelupaBottomBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (currentUser != null) Screen.Home.route else Screen.Greeting.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Greeting.route) {
                GreetingScreen(
                    onStart = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Greeting.route) { inclusive = true }
                        }
                    }
                )
            }
            
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) { 
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }
            
            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onBackToLogin = {
                        navController.navigateUp()
                    }
                )
            }
            
            // Main screens with bottom navigation
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToDetail = { tempat ->
                        navController.navigate(Screen.Detail.createRoute(tempat.id))
                    }
                )
            }
            
            composable(Screen.Maps.route) {
                MapsScreen()
            }
            
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToGallery = {
                        navController.navigate(Screen.Gallery.route)
                    },
                    onNavigateToAddWisata = {
                        navController.navigate(Screen.AddWisata.route)
                    },
                    onNavigateToFavorite = {
                        navController.navigate(Screen.Favorite.route)
                    },
                    onLogout = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate(Screen.Greeting.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }
            
            // Detail screen
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("tempatId") { type = NavType.StringType })
            ) { backStackEntry ->
                val tempatId = backStackEntry.arguments?.getString("tempatId") ?: ""
                DetailScreen(
                    tempatId = tempatId,
                    onBack = { navController.navigateUp() }
                )
            }
            
            // Favorite screen
            composable(Screen.Favorite.route) {
                FavoriteScreen(
                    onNavigateToDetail = { tempatId ->
                        navController.navigate(Screen.Detail.createRoute(tempatId))
                    },
                    onBack = { navController.navigateUp() }
                )
            }
            
            // Secondary screens
            composable(Screen.Gallery.route) {
                GalleryScreen(
                    imageDao = imageDao,
                    onImageSelected = { _ ->
                        // Handle image selection if needed
                    },
                    onBack = {
                        navController.navigateUp()
                    }
                )
            }
            
            composable(Screen.AddWisata.route) {
                AddWisataScreen(
                    onBack = {
                        navController.navigateUp()
                    },
                    onSuccess = {
                        navController.navigateUp()
                    }
                )
            }
            
            // Legacy route for backward compatibility
            composable(Screen.RekomendasiTempat.route) {
                HomeScreen(
                    onNavigateToDetail = { tempat ->
                        navController.navigate(Screen.Detail.createRoute(tempat.id))
                    }
                )
            }
        }
    }
}

