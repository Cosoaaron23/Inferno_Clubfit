package com.cosoaaron.infernoclubfit

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cosoaaron.infernoclubfit.ui.theme.InfernoOrange
import com.cosoaaron.infernoclubfit.ui.theme.InfernoRed
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WorkoutHubScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(Color.Black).padding(16.dp)) {
        Text("ENTRENAMIENTO", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("active_session/Entrenamiento Libre") }, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E)), shape = RoundedCornerShape(8.dp)) { Icon(Icons.Default.Add, null, tint = Color.White); Spacer(modifier = Modifier.width(8.dp)); Text("Empezar Entrenamiento Vacío", color = Color.White) }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { GlobalRoutineManager.routineToEdit = null; navController.navigate("create_routine") }, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E)), shape = RoundedCornerShape(8.dp)) { Icon(Icons.Default.Edit, null, tint = InfernoOrange, modifier = Modifier.size(18.dp)); Spacer(modifier = Modifier.width(8.dp)); Text("Nueva Rutina", color = Color.White, fontSize = 14.sp) }
        Spacer(modifier = Modifier.height(24.dp)); Text("Mis Rutinas (${GlobalRoutineManager.routines.size})", color = Color.Gray, fontSize = 14.sp); Spacer(modifier = Modifier.height(8.dp))
        LazyColumn { items(GlobalRoutineManager.routines) { routine -> RoutineCardItem(routine, navController) } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoutineScreen(navController: NavController) {
    val edit = GlobalRoutineManager.routineToEdit; var name by remember { mutableStateOf(edit?.name ?: "") }; val exs = remember { mutableStateListOf<String>().apply { addAll(edit?.exerciseNames ?: emptyList()) } }; var showSel by remember { mutableStateOf(false) }
    Scaffold(topBar = { TopAppBar(title = { Text(if (edit != null) "Editar" else "Crear", color = Color.White) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.Close, null, tint = Color.White) } }, actions = { TextButton(onClick = { if (name.isNotEmpty() && exs.isNotEmpty()) { if (edit != null) GlobalRoutineManager.updateRoutine(edit.id, name, exs.toList()) else GlobalRoutineManager.addRoutine(name, exs.toList()); GlobalRoutineManager.routineToEdit = null; navController.popBackStack() } }) { Text("Guardar", color = if (name.isNotEmpty()) InfernoOrange else Color.Gray, fontWeight = FontWeight.Bold) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)) }, containerColor = Color.Black) { p ->
        Column(Modifier.padding(p).padding(16.dp)) {
            OutlinedTextField(value = name, onValueChange = { name = it }, placeholder = { Text("Nombre Rutina (ej. Pecho)") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, focusedBorderColor = InfernoOrange)); Spacer(Modifier.height(24.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Ejercicios", color = Color.Gray); TextButton(onClick = { showSel = true }) { Text("+ Añadir", color = InfernoOrange) } }
            LazyColumn { items(exs) { e -> Card(modifier = Modifier.padding(4.dp).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))) { Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text(e, color = Color.White); Icon(Icons.Default.Close, null, tint = Color.Gray, modifier = Modifier.clickable { exs.remove(e) }) } } } }
        }
        if (showSel) ExerciseSelectorDialog(onDismiss = { showSel = false }, onSelect = { exs.add(it); showSel = false })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveSessionScreen(navController: NavController, routineTitle: String) {
    BackHandler { GlobalWorkoutManager.pauseWorkout(); navController.popBackStack() }
    var showSel by remember { mutableStateOf(false) }; var idxRep by remember { mutableStateOf(-1) }; var timer by remember { mutableStateOf(0) }; var timerOn by remember { mutableStateOf(false) }
    LaunchedEffect(timerOn, timer) { if (timerOn && timer > 0) { delay(1000L); timer-- } else if (timer == 0) timerOn = false }
    LaunchedEffect(routineTitle) { if (!GlobalWorkoutManager.isActive || GlobalWorkoutManager.currentRoutineName != routineTitle) { val r = GlobalRoutineManager.routines.find { it.name == routineTitle }; val e = mutableListOf<ActiveExercise>(); r?.exerciseNames?.forEachIndexed { i, n -> e.add(ActiveExercise((i + 1).toString(), n, mutableStateListOf(WorkoutSet(1, "-", "-")))) }; GlobalWorkoutManager.startWorkout(routineTitle, e) } else { GlobalWorkoutManager.resumeWorkout() } }
    Scaffold(topBar = { TopAppBar(title = { Column { Text(routineTitle, color = Color.White, fontWeight = FontWeight.Bold); Text("${GlobalWorkoutManager.durationSeconds / 60}m ${GlobalWorkoutManager.durationSeconds % 60}s", color = InfernoOrange, fontSize = 12.sp) } }, actions = { Button(onClick = { navController.navigate("workout_summary") }, colors = ButtonDefaults.buttonColors(containerColor = InfernoRed)) { Text("Terminar", fontWeight = FontWeight.Bold) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)) }, floatingActionButton = { ExtendedFloatingActionButton(onClick = { idxRep = -1; showSel = true }, containerColor = InfernoRed, contentColor = Color.White) { Icon(Icons.Default.Add, null); Text("AÑADIR") } }, bottomBar = { AnimatedVisibility(timerOn || timer > 0) { Card(colors = CardDefaults.cardColors(containerColor = InfernoOrange), modifier = Modifier.fillMaxWidth().height(50.dp)) { Row(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) { Text("DESCANSO", fontWeight = FontWeight.Bold); Text("$timer s", fontSize = 20.sp, fontWeight = FontWeight.Bold); IconButton(onClick = { timerOn = false; timer = 0 }) { Icon(Icons.Default.Close, null) } } } } }) { p ->
        Box(Modifier.fillMaxSize().background(Color.Black).padding(p)) {
            LazyColumn(Modifier.padding(horizontal = 8.dp, vertical = 16.dp)) { itemsIndexed(GlobalWorkoutManager.exercises) { i, ex -> ActiveExerciseItem(exercise = ex, onDelete = { GlobalWorkoutManager.exercises.removeAt(i) }, onReplace = { idxRep = i; showSel = true }, onTimerStart = { timer = ex.restTimeSeconds; timerOn = true }); Spacer(Modifier.height(24.dp)) }; item { Spacer(Modifier.height(80.dp)) } }
        }
        if (showSel) { ExerciseSelectorDialog(onDismiss = { showSel = false }, onSelect = { if (idxRep >= 0) GlobalWorkoutManager.exercises[idxRep] = GlobalWorkoutManager.exercises[idxRep].copy(name = it) else GlobalWorkoutManager.exercises.add(ActiveExercise(System.currentTimeMillis().toString(), it, mutableStateListOf(WorkoutSet(1, "-", "-")))); showSel = false }) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(navController: NavController) {
    var notes by remember { mutableStateOf("") }; val date = SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(Date())
    val vol = GlobalWorkoutManager.exercises.sumOf { it.sets.filter { s -> s.isCompleted }.sumOf { s -> (s.weight.toIntOrNull() ?: 0) * (s.reps.toIntOrNull() ?: 0) } }; val sets = GlobalWorkoutManager.exercises.sumOf { it.sets.count { s -> s.isCompleted } }
    Scaffold(topBar = { TopAppBar(title = { Text("Guardar", color = Color.White) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) } }, actions = { Button(onClick = { val ex = GlobalWorkoutManager.exercises.map { activeEx -> activeEx.copy(sets = activeEx.sets.map { it.copy() }.toMutableList()) }; GlobalHistoryManager.history.add(0, WorkoutHistoryItem(routineName = GlobalWorkoutManager.currentRoutineName, date = System.currentTimeMillis(), dateString = date, duration = "${GlobalWorkoutManager.durationSeconds / 60}m", volume = vol, totalSets = sets, notes = notes, completedExercises = ex)); GlobalWorkoutManager.stopWorkout(); navController.navigate("profile") { popUpTo("home") { inclusive = false } } }, colors = ButtonDefaults.buttonColors(containerColor = InfernoRed)) { Text("Guardar") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)) }) { p ->
        Column(Modifier.padding(p).padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState())) {
            Text(GlobalWorkoutManager.currentRoutineName, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold); Spacer(Modifier.height(24.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { StatBox("Duración", "${GlobalWorkoutManager.durationSeconds / 60}m"); StatBox("Volumen", "$vol kg"); StatBox("Series", "$sets") }
            Spacer(Modifier.height(24.dp)); OutlinedTextField(value = notes, onValueChange = { notes = it }, placeholder = { Text("Notas...", color = Color.Gray) }, modifier = Modifier.fillMaxWidth().height(150.dp), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = InfernoOrange)); Spacer(Modifier.height(40.dp))
            TextButton(onClick = { GlobalWorkoutManager.stopWorkout(); navController.popBackStack() }, modifier = Modifier.align(Alignment.CenterHorizontally)) { Text("Descartar", color = InfernoRed) }
        }
    }
}