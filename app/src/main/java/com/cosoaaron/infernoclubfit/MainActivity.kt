package com.cosoaaron.infernoclubfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.cosoaaron.infernoclubfit.ui.theme.InfernoClubfitTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InfernoClubfitTheme {
                // Timer Global
                LaunchedEffect(GlobalWorkoutManager.isActive, GlobalWorkoutManager.isPaused) {
                    while (GlobalWorkoutManager.isActive && !GlobalWorkoutManager.isPaused) {
                        delay(1000L); GlobalWorkoutManager.durationSeconds++
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
                if (GlobalWorkoutManager.isActive && !isInActiveSession) {
                    WorkoutMiniPlayer(
                        name = GlobalWorkoutManager.currentRoutineName, // FIXED PARAMETER NAME
                        sec = GlobalWorkoutManager.durationSeconds,     // FIXED PARAMETER NAME
                        onResume = { GlobalWorkoutManager.resumeWorkout(); navController.navigate("active_session/${GlobalWorkoutManager.currentRoutineName}") { launchSingleTop = true } },
                        onStop = { GlobalWorkoutManager.stopWorkout() }
                    )
                }
                val showBottom = currentRoute != "workout_summary" && !isInActiveSession && currentRoute != "create_routine" && currentRoute != "settings"
                if (showBottom) BottomNavigationBar(navController)
            }
        },
        containerColor = Color.Black
    ) { paddingValues ->
        NavHost(navController = navController, startDestination = "home", modifier = Modifier.padding(paddingValues)) {
            composable("home") { HomeScreen() }
            composable("workout_hub") { WorkoutHubScreen(navController) }
            composable("create_routine") { CreateRoutineScreen(navController) }
            composable("active_session/{routineName}") { ActiveSessionScreen(navController, it.arguments?.getString("routineName") ?: "Entreno") }
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