package com.cosoaaron.infernoclubfit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cosoaaron.infernoclubfit.ui.theme.InfernoOrange
import com.cosoaaron.infernoclubfit.ui.theme.InfernoRed

// --- BARRA DE NAVEGACIÓN ---
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(Triple("home", "Home", Icons.Default.Home), Triple("workout_hub", "Entreno", Icons.Default.FitnessCenter), Triple("profile", "Perfil", Icons.Default.Person))
    NavigationBar(containerColor = Color(0xFF121212)) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { (route, label, icon) ->
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = label) }, label = { Text(label) },
                selected = currentRoute == route, onClick = { navController.navigate(route) },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = InfernoOrange, selectedTextColor = InfernoOrange, indicatorColor = Color(0xFF3E2723), unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray)
            )
        }
    }
}

// --- MINI REPRODUCTOR DE ENTRENO ---
@Composable
fun WorkoutMiniPlayer(name: String, sec: Long, onResume: () -> Unit, onStop: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)), shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp), modifier = Modifier.fillMaxWidth().height(60.dp).border(BorderStroke(1.dp, InfernoRed))) {
        Row(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Column { Text("EN PAUSA: $name", color = InfernoOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold); Text("${sec/60}m ${sec%60}s", color = Color.White, fontWeight = FontWeight.Bold) }
            Row {
                TextButton(onClick = onStop) { Text("DETENER", color = Color.Gray, fontSize = 12.sp) }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onResume, colors = ButtonDefaults.buttonColors(containerColor = InfernoRed), shape = RoundedCornerShape(50), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp), modifier = Modifier.height(32.dp)) { Text("SEGUIR", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

// --- TARJETA DE RUTINA ---
@Composable
fun RoutineCardItem(routine: Routine, navController: NavController) {
    var showMenu by remember { mutableStateOf(false) }
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)), border = BorderStroke(1.dp, Color(0xFF333333))) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(routine.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Box {
                    IconButton(onClick = { showMenu = true }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.MoreHoriz, null, tint = Color.Gray) }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }, containerColor = Color(0xFF2C2C2C)) {
                        DropdownMenuItem(text = { Text("Editar", color = Color.White) }, onClick = { showMenu = false; GlobalRoutineManager.routineToEdit = routine; navController.navigate("create_routine") })
                        DropdownMenuItem(text = { Text("Borrar", color = InfernoRed) }, onClick = { showMenu = false; GlobalRoutineManager.deleteRoutine(routine) })
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp)); Text(routine.description, color = Color.Gray, fontSize = 12.sp, lineHeight = 16.sp, maxLines = 2); Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("active_session/${routine.name}") }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = InfernoRed), shape = RoundedCornerShape(6.dp)) { Text("Empezar Rutina", fontWeight = FontWeight.Bold, color = Color.White) }
        }
    }
}

// --- ITEM DE EJERCICIO ACTIVO (CON CAMBIO DE DESCANSO) ---
@Composable
fun ActiveExerciseItem(exercise: ActiveExercise, onDelete: () -> Unit, onReplace: () -> Unit, onTimerStart: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }
    var showTimeDialog by remember { mutableStateOf(false) }
    // Variable temporal para editar el tiempo
    var tempTime by remember { mutableStateOf(exercise.restTimeSeconds.toString()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Cabecera Ejercicio
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Text(exercise.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Box {
                    IconButton(onClick = { showMenu = true }) { Icon(Icons.Default.MoreVert, null, tint = Color.Gray) }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }, containerColor = Color(0xFF1E1E1E)) {
                        // NUEVA OPCIÓN: CAMBIAR DESCANSO
                        DropdownMenuItem(
                            text = { Text("⏱️ Cambiar Descanso", color = Color.White) },
                            onClick = {
                                showMenu = false
                                showTimeDialog = true
                            }
                        )
                        DropdownMenuItem(text = { Text("Reemplazar", color = Color.White) }, onClick = { showMenu = false; onReplace() })
                        Divider(color = Color.Gray, thickness = 0.5.dp)
                        DropdownMenuItem(text = { Text("Borrar", color = InfernoRed) }, onClick = { showMenu = false; onDelete() })
                    }
                }
            }
            Text("Descanso objetivo: ${exercise.restTimeSeconds}s", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(bottom = 16.dp))

            // Cabecera Tabla
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                Text("SET", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
                Text("PREV", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Text("KG", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f), textAlign = TextAlign.Center)
                Text("REPS", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.width(48.dp))
            }

            // Filas
            exercise.sets.forEachIndexed { i, set ->
                var w by remember { mutableStateOf(set.weight) }
                var r by remember { mutableStateOf(set.reps) }
                var c by remember { mutableStateOf(set.isCompleted) }

                val rowBgColor = if (c) InfernoOrange.copy(alpha = 0.15f) else Color.Transparent
                val rowBorder = if (c) BorderStroke(1.dp, InfernoOrange.copy(alpha = 0.5f)) else null

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).background(rowBgColor, RoundedCornerShape(8.dp)).border(rowBorder ?: BorderStroke(0.dp, Color.Transparent), RoundedCornerShape(8.dp)).padding(vertical = 8.dp, horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${i + 1}", color = if(c) InfernoOrange else Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
                    Text("-", color = Color.Gray, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    // Inputs
                    Box(modifier = Modifier.weight(1.2f), contentAlignment = Alignment.Center) { CompactInput(w, c) { w = it; set.weight = it } }
                    Box(modifier = Modifier.weight(1.2f), contentAlignment = Alignment.Center) { CompactInput(r, c) { r = it; set.reps = it } }
                    // Check
                    Box(modifier = Modifier.width(48.dp), contentAlignment = Alignment.Center) {
                        Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(if (c) InfernoOrange else Color.Transparent).border(2.dp, if (c) InfernoOrange else Color.Gray, CircleShape).clickable { if (!c) onTimerStart(); c = !c; set.isCompleted = c }, contentAlignment = Alignment.Center) {
                            if (c) Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
            TextButton(onClick = { exercise.sets.add(WorkoutSet(exercise.sets.size + 1)) }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Text("+ Añadir Serie", color = InfernoOrange, fontWeight = FontWeight.Bold)
            }
        }
        Divider(color = Color(0xFF222222), thickness = 1.dp, modifier = Modifier.padding(top = 16.dp))
    }

    // DIÁLOGO PARA CAMBIAR TIEMPO DE DESCANSO
    if (showTimeDialog) {
        AlertDialog(
            onDismissRequest = { showTimeDialog = false },
            containerColor = Color(0xFF1E1E1E),
            title = { Text("Tiempo de Descanso (segundos)", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = tempTime,
                    onValueChange = { if(it.all { char -> char.isDigit() }) tempTime = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = InfernoOrange,
                        cursorColor = InfernoOrange
                    ),
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        exercise.restTimeSeconds = tempTime.toIntOrNull() ?: 60
                        showTimeDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = InfernoRed)
                ) { Text("Guardar", color = Color.White, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showTimeDialog = false }) { Text("Cancelar", color = Color.Gray) }
            }
        )
    }
}

// --- INPUTS Y UTILIDADES ---
@Composable
fun CompactInput(value: String, isCompleted: Boolean, onChange: (String) -> Unit) {
    Box(
        modifier = Modifier.width(70.dp).height(35.dp).padding(horizontal = 2.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (isCompleted) Color.Transparent else Color(0xFF2C2C2C)),
        contentAlignment = Alignment.Center
    ) {
        if (isCompleted) {
            Text(value, color = Color.White, fontWeight = FontWeight.Bold)
        } else {
            BasicTextField(
                value = value,
                onValueChange = onChange,
                textStyle = TextStyle(color = Color.White, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 16.sp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable fun ExerciseSelectorDialog(onDismiss: () -> Unit, onSelect: (String) -> Unit) { var q by remember { mutableStateOf("") }; AlertDialog(onDismissRequest = onDismiss, containerColor = Color(0xFF1E1E1E), title = { Text("Seleccionar", color = Color.White) }, text = { Column(Modifier.height(400.dp)) { OutlinedTextField(value = q, onValueChange = { q = it }, placeholder = { Text("Buscar...") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, focusedBorderColor = InfernoOrange)); LazyColumn { items(InternalExerciseRepo.allExercisesList.filter { it.contains(q, true) }) { name -> TextButton(onClick = { onSelect(name) }) { Text(name, color = Color.White) } } } } }, confirmButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }) }
@Composable fun StatBox(label: String, value: String) { Column { Text(label, color = Color.Gray, fontSize = 12.sp); Text(value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) } }
@Composable fun StatRow(label: String, value: String) { Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text(label, color = Color.Gray); Text(value, color = InfernoOrange, fontWeight = FontWeight.Bold) } }
@Composable fun MeasureItem(label: String, value: String) { Column { Text(label, color = Color.Gray, fontSize = 12.sp); Text(value, color = Color.White, fontWeight = FontWeight.Bold) } }
@Composable fun ProfileStat(value: String, label: String) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold); Text(label, color = Color.Gray, fontSize = 12.sp) } }
@Composable fun ProfileMenuButton(text: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) { Button(onClick = onClick, modifier = modifier.height(60.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E))) { Row(verticalAlignment = Alignment.CenterVertically) { Icon(icon, null, tint = Color.White); Spacer(modifier = Modifier.width(8.dp)); Text(text, color = Color.White) } } }
@Composable fun ExerciseItemRow(name: String, onClick: (String) -> Unit) { TextButton(onClick = { onClick(name) }, modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(vertical = 4.dp)) { Text(name, color = Color.White, fontSize = 16.sp, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth()) }; Divider(color = Color(0xFF333333), thickness = 0.5.dp) }