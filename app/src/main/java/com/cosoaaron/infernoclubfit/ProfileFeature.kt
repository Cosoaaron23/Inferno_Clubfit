package com.cosoaaron.infernoclubfit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cosoaaron.infernoclubfit.ui.theme.InfernoOrange
import com.cosoaaron.infernoclubfit.ui.theme.InfernoRed
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(Color.Black).padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text(GlobalUserManager.username, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp); IconButton(onClick = { navController.navigate("settings") }) { Icon(Icons.Default.Settings, "Configuración", tint = Color.White) } }
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Color.Gray), contentAlignment = Alignment.Center) { Text("🐱", fontSize = 40.sp) }
            Spacer(modifier = Modifier.width(20.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                // FIXED: Llama a ProfileStat con dos argumentos
                ProfileStat("${GlobalHistoryManager.history.size}", "Entrenos")
                ProfileStat("${GlobalUserManager.followers}", "Seguidores")
                ProfileStat("${GlobalUserManager.following}", "Siguiendo")
            }
        }
        Spacer(modifier = Modifier.height(8.dp)); Text(GlobalUserManager.level, color = InfernoOrange, fontSize = 14.sp); Spacer(modifier = Modifier.height(24.dp)); Text("Volumen últimos entrenos", color = Color.White, fontWeight = FontWeight.Bold); Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth().height(100.dp), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceBetween) { val rw = GlobalHistoryManager.history.take(7).reversed(); val max = if (rw.isNotEmpty()) rw.maxOf { it.volume }.toFloat() else 1f; if (rw.isEmpty()) Text("Sin datos", color = Color.Gray, fontSize = 12.sp) else rw.forEach { w -> Box(modifier = Modifier.width(20.dp).fillMaxHeight((w.volume / max).coerceAtLeast(0.1f)).background(InfernoOrange, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))) } }
        Spacer(modifier = Modifier.height(24.dp)); Text("Información", color = Color.Gray); Spacer(modifier = Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) { ProfileMenuButton("Estadísticas", Icons.Default.ShowChart, Modifier.weight(1f)) { navController.navigate("profile_stats") }; ProfileMenuButton("Ejercicios", Icons.Default.FitnessCenter, Modifier.weight(1f)) { navController.navigate("profile_exercises") } }
        Spacer(modifier = Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) { ProfileMenuButton("Medidas", Icons.Default.Accessibility, Modifier.weight(1f)) { navController.navigate("profile_measures") }; ProfileMenuButton("Calendario", Icons.Default.CalendarMonth, Modifier.weight(1f)) { navController.navigate("profile_calendar") } }
        Spacer(modifier = Modifier.height(24.dp)); Text("Historial Reciente", color = Color.Gray); Spacer(modifier = Modifier.height(8.dp))
        if (GlobalHistoryManager.history.isNotEmpty()) { GlobalHistoryManager.history.take(3).forEach { item -> Card(modifier = Modifier.padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))) { Row(modifier = Modifier.padding(16.dp)) { Column { Text(item.routineName, color = Color.White, fontWeight = FontWeight.Bold); Text("${item.dateString} • ${item.volume}kg", color = Color.Gray, fontSize = 12.sp) } } } } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    var tName by remember { mutableStateOf(GlobalUserManager.name) }; var tUser by remember { mutableStateOf(GlobalUserManager.username) }; var tLvl by remember { mutableStateOf(GlobalUserManager.level) }
    Scaffold(topBar = { TopAppBar(title = { Text("Configuración", color = Color.White) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) } }, actions = { TextButton(onClick = { GlobalUserManager.name = tName; GlobalUserManager.username = tUser; GlobalUserManager.level = tLvl; navController.popBackStack() }) { Text("Guardar", color = InfernoOrange) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)) }, containerColor = Color.Black) { p ->
        Column(Modifier.padding(p).padding(16.dp)) {
            Text("PERFIL", color = InfernoOrange, fontWeight = FontWeight.Bold); Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = tName, onValueChange = { tName = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = Color.Gray))
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = tUser, onValueChange = { tUser = it }, label = { Text("Usuario") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = Color.Gray))
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = tLvl, onValueChange = { tLvl = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = Color.Gray))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileStatsScreen(navController: NavController) {
    var period by remember { mutableStateOf("Semana") }; val workedMuscles = remember(period) { val days = when (period) { "Semana" -> 7; "Mes" -> 30; else -> 365 }; GlobalHistoryManager.getMusclesWorkedInPeriod(days) }
    Scaffold(topBar = { TopAppBar(title = { Text("Estadísticas", color = Color.White) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)) }, containerColor = Color.Black) { p ->
        Column(Modifier.padding(p).padding(16.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.background(Color(0xFF1E1E1E), RoundedCornerShape(8.dp)).padding(4.dp)) { listOf("Semana", "Mes", "Año").forEach { p -> val selected = period == p; Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(if (selected) Color(0xFF333333) else Color.Transparent).clickable { period = p }.padding(horizontal = 16.dp, vertical = 8.dp)) { Text(p, color = if (selected) Color.White else Color.Gray) } } }
            Spacer(modifier = Modifier.height(32.dp)); Text("Músculos trabajados ($period)", color = Color.White, fontWeight = FontWeight.Bold); Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.size(300.dp, 400.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val path = Path().apply { addOval(androidx.compose.ui.geometry.Rect(center = Offset(size.width / 2, 40f), radius = 30f)); moveTo(size.width / 2 - 40, 70f); lineTo(size.width / 2 + 40, 70f); lineTo(size.width / 2 + 30, 200f); lineTo(size.width / 2 - 30, 200f); close(); moveTo(size.width / 2 - 40, 80f); lineTo(size.width / 2 - 90, 150f); lineTo(size.width / 2 - 70, 160f); moveTo(size.width / 2 + 40, 80f); lineTo(size.width / 2 + 90, 150f); lineTo(size.width / 2 + 70, 160f); moveTo(size.width / 2 - 30, 200f); lineTo(size.width / 2 - 40, 350f); lineTo(size.width / 2 - 10, 350f); lineTo(size.width / 2 - 5, 200f); moveTo(size.width / 2 + 30, 200f); lineTo(size.width / 2 + 40, 350f); lineTo(size.width / 2 + 10, 350f); lineTo(size.width / 2 + 5, 200f) }
                    drawPath(path, color = Color.DarkGray, style = Stroke(width = 3f)); val highlight = InfernoOrange.copy(alpha = 0.7f)
                    if (workedMuscles.contains("PECHO") || workedMuscles.contains("ABS/CARDIO")) drawCircle(highlight, radius = 25f, center = Offset(size.width / 2, 110f))
                    if (workedMuscles.contains("HOMBRO")) { drawCircle(highlight, radius = 15f, center = Offset(size.width / 2 - 45, 80f)); drawCircle(highlight, radius = 15f, center = Offset(size.width / 2 + 45, 80f)) }
                    if (workedMuscles.contains("BRAZOS")) { drawCircle(highlight, radius = 12f, center = Offset(size.width / 2 - 70, 120f)); drawCircle(highlight, radius = 12f, center = Offset(size.width / 2 + 70, 120f)) }
                    if (workedMuscles.contains("PIERNA")) { drawCircle(highlight, radius = 25f, center = Offset(size.width / 2 - 25, 260f)); drawCircle(highlight, radius = 25f, center = Offset(size.width / 2 + 25, 260f)) }
                }
            }
            if (workedMuscles.isEmpty()) Text("No hay datos en este periodo", color = Color.Gray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCalendarScreen(navController: NavController) {
    val calendar = Calendar.getInstance(); val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); val monthName = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)
    Scaffold(topBar = { TopAppBar(title = { Text("Calendario", color = Color.White) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)) }, containerColor = Color.Black) { padding -> Column(modifier = Modifier.padding(padding).padding(16.dp)) { Text(monthName.capitalize(Locale.ROOT), color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold); Spacer(modifier = Modifier.height(24.dp)); LazyVerticalGrid(columns = GridCells.Fixed(7)) { items(listOf("L", "M", "M", "J", "V", "S", "D")) { day -> Text(day, color = Color.Gray, textAlign = TextAlign.Center) }; items((1..daysInMonth).toList()) { day -> val hasWorkout = GlobalHistoryManager.hasWorkoutOnDay(day); Box(modifier = Modifier.padding(4.dp).aspectRatio(1f), contentAlignment = Alignment.Center) { if (hasWorkout) Box(modifier = Modifier.fillMaxSize().clip(CircleShape).background(InfernoRed)); Text("$day", color = if (hasWorkout) Color.White else Color.LightGray) } } } } }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileExercisesScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }; var selectedExercise by remember { mutableStateOf<String?>(null) }
    Scaffold(topBar = { TopAppBar(title = { Text("Ejercicios", color = Color.White) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)) }, containerColor = Color.Black) { padding -> Column(modifier = Modifier.padding(padding).padding(16.dp)) { OutlinedTextField(value = searchQuery, onValueChange = { searchQuery = it }, placeholder = { Text("Buscar ejercicio...", color = Color.Gray) }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = InfernoOrange), leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) }); Spacer(modifier = Modifier.height(16.dp)); LazyColumn { val filtered = InternalExerciseRepo.allExercisesList.filter { it.contains(searchQuery, ignoreCase = true) }; items(filtered) { exercise -> Row(modifier = Modifier.fillMaxWidth().clickable { selectedExercise = exercise }.padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(50.dp).clip(CircleShape).background(Color(0xFF1E1E1E)), contentAlignment = Alignment.Center) { Icon(Icons.Default.FitnessCenter, null, tint = InfernoOrange) }; Spacer(modifier = Modifier.width(16.dp)); Text(exercise, color = Color.White, fontSize = 16.sp); Spacer(modifier = Modifier.weight(1f)); Icon(Icons.Default.ChevronRight, null, tint = Color.Gray) }; Divider(color = Color(0xFF222222)) } } }; if (selectedExercise != null) { val stats = GlobalHistoryManager.getExerciseStats(selectedExercise!!); AlertDialog(onDismissRequest = { selectedExercise = null }, containerColor = Color(0xFF1E1E1E), title = { Text(selectedExercise!!, color = Color.White, fontWeight = FontWeight.Bold) }, text = { Column { if (stats.third == 0) Text("No has realizado este ejercicio aún.", color = Color.Gray) else { StatRow("Récord Peso (1RM)", "${stats.first} kg"); StatRow("Volumen Total Histórico", "${stats.second} kg"); StatRow("Veces Realizado", "${stats.third}") } } }, confirmButton = { Button(onClick = { selectedExercise = null }, colors = ButtonDefaults.buttonColors(containerColor = InfernoRed)) { Text("Cerrar") } }) } }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMeasuresScreen(navController: NavController) {
    var showAddDialog by remember { mutableStateOf(false) }
    Scaffold(topBar = { TopAppBar(title = { Text("Medidas", color = Color.White) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) } }, actions = { IconButton(onClick = { showAddDialog = true }) { Icon(Icons.Default.Add, null, tint = InfernoOrange) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)) }, containerColor = Color.Black) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) { items(GlobalUserManager.measurements) { m -> Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))) { Column(modifier = Modifier.padding(16.dp)) { Text(m.date, color = InfernoOrange, fontWeight = FontWeight.Bold); Spacer(modifier = Modifier.height(8.dp)); Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { MeasureItem("Peso", "${m.weight}kg"); MeasureItem("Altura", "${m.height}cm"); MeasureItem("Pecho", "${m.chest}cm") }; Spacer(modifier = Modifier.height(8.dp)); Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { MeasureItem("Bíceps", "${m.biceps}cm"); MeasureItem("Cuádriceps", "${m.quads}cm"); Spacer(Modifier.weight(1f)) } } } } }
        if (showAddDialog) { var w by remember { mutableStateOf("") }; var h by remember { mutableStateOf("") }; var c by remember { mutableStateOf("") }; var b by remember { mutableStateOf("") }; var q by remember { mutableStateOf("") }; AlertDialog(onDismissRequest = { showAddDialog = false }, containerColor = Color(0xFF1E1E1E), title = { Text("Añadir Registro", color = Color.White) }, text = { Column { OutlinedTextField(value = w, onValueChange = { w = it }, label = { Text("Peso") }, colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, focusedBorderColor = InfernoOrange)); OutlinedTextField(value = h, onValueChange = { h = it }, label = { Text("Altura") }, colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, focusedBorderColor = InfernoOrange)); OutlinedTextField(value = c, onValueChange = { c = it }, label = { Text("Pecho") }, colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, focusedBorderColor = InfernoOrange)); OutlinedTextField(value = b, onValueChange = { b = it }, label = { Text("Bíceps") }, colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, focusedBorderColor = InfernoOrange)); OutlinedTextField(value = q, onValueChange = { q = it }, label = { Text("Cuádriceps") }, colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, focusedBorderColor = InfernoOrange)) } }, confirmButton = { Button(onClick = { val date = SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(Date()); GlobalUserManager.measurements.add(0, BodyMeasurement(date, w, h, c, b, q)); showAddDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = InfernoRed)) { Text("Guardar") } }) }
    }
}