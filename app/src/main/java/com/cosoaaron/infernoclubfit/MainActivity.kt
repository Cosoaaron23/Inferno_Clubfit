package com.cosoaaron.infernoclubfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cosoaaron.infernoclubfit.ui.theme.InfernoClubfitTheme
import com.cosoaaron.infernoclubfit.ui.theme.InfernoRed
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InfernoClubfitTheme {
                // Timer Global: Se mantiene activo en toda la app
                LaunchedEffect(GlobalWorkoutManager.isActive, GlobalWorkoutManager.isPaused) {
                    while (GlobalWorkoutManager.isActive && !GlobalWorkoutManager.isPaused) {
                        delay(1000L)
                        GlobalWorkoutManager.durationSeconds++
                    }
                }
                MainAppStructure()
            }
        }
    }
}

@Composable
fun MainAppStructure() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isInActiveSession = currentRoute?.startsWith("active_session") == true

    Scaffold(
        bottomBar = {
            Column {
                // Mini Player (Si hay entreno activo y no estamos en la pantalla de entreno)
                if (GlobalWorkoutManager.isActive && !isInActiveSession) {
                    WorkoutMiniPlayer(
                        name = GlobalWorkoutManager.currentRoutineName,
                        sec = GlobalWorkoutManager.durationSeconds,
                        onResume = {
                            GlobalWorkoutManager.resumeWorkout()
                            navController.navigate("active_session/${GlobalWorkoutManager.currentRoutineName}") { launchSingleTop = true }
                        },
                        onStop = { GlobalWorkoutManager.stopWorkout() }
                    )
                }

                // Barra de Navegación (Oculta en Splash, Login, Entreno, etc.)
                val showBottom = currentRoute != "splash" &&
                        currentRoute != "workout_summary" &&
                        !isInActiveSession &&
                        currentRoute != "create_routine" &&
                        currentRoute != "settings"

                if (showBottom) {
                    BottomNavigationBar(navController)
                }
            }
        },
        containerColor = Color.Black
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "splash", // Arrancamos en el Splash
            modifier = Modifier.padding(paddingValues)
        ) {
            // PANTALLA DE BIENVENIDA CON LOGO
            composable("splash") { SplashScreen(navController) }

            // RESTO DE PANTALLAS (Importadas de tus otros archivos)
            composable("home") { HomeScreen() }
            composable("workout_hub") { WorkoutHubScreen(navController) }
            composable("create_routine") { CreateRoutineScreen(navController) }

            composable("active_session/{routineName}") { backStackEntry ->
                val routineName = backStackEntry.arguments?.getString("routineName") ?: "Entreno"
                ActiveSessionScreen(navController, routineName)
            }

            composable("workout_summary") { SummaryScreen(navController) }
            composable("profile") { ProfileScreen(navController) }
            composable("settings") { SettingsScreen(navController) }
            composable("profile_stats") { ProfileStatsScreen(navController) }
            composable("profile_exercises") { ProfileExercisesScreen(navController) }
            composable("profile_measures") { ProfileMeasuresScreen(navController) }
            composable("profile_calendar") { ProfileCalendarScreen(navController) }
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(2000) // Espera 2 segundos
        navController.navigate("home") {
            popUpTo("splash") { inclusive = true } // Elimina el splash del historial
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // TU LOGO AQUÍ
            Image(
                painter = painterResource(id = R.drawable.logo_inferno),
                contentDescription = "Logo Inferno Clubfit",
                modifier = Modifier.size(250.dp).clip(CircleShape),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "INFERNO CLUBFIT",
                color = InfernoRed,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        }
    }
}