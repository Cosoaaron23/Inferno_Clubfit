package com.cosoaaron.infernoclubfit

import androidx.compose.runtime.*
import java.util.Calendar

// --- MODELOS DE DATOS ---
data class Routine(val id: String, var name: String, var description: String, var exerciseNames: List<String>)
data class WorkoutSet(val id: Int, var weight: String = "", var reps: String = "", var isCompleted: Boolean = false)
data class SocialPost(val id: Long, val user: String, val workoutName: String, var likes: Int, val timeAgo: String, var isLiked: Boolean = false)
data class ActiveExercise(val id: String, var name: String, val sets: MutableList<WorkoutSet>, var restTimeSeconds: Int = 60)
data class WorkoutHistoryItem(
    val id: Long = System.currentTimeMillis(),
    val routineName: String,
    val date: Long,
    val dateString: String,
    val duration: String,
    val volume: Int,
    val totalSets: Int,
    val notes: String,
    val completedExercises: List<ActiveExercise>
)
data class BodyMeasurement(val date: String, val weight: String, val height: String, val chest: String, val biceps: String, val quads: String)

// --- GESTORES GLOBALES (SINGLETONS) ---

object InternalExerciseRepo {
    val groupedExercises = mapOf(
        "PECHO" to listOf("Press de Banca", "Press Inclinado", "Aperturas", "Fondos", "Cruce de Poleas"),
        "ESPALDA" to listOf("Dominadas", "Jalón al Pecho", "Remo con Barra", "Remo en Polea", "Peso Muerto"),
        "PIERNA" to listOf("Sentadilla", "Prensa 45º", "Zancadas", "Extensiones", "Peso Muerto Rumano", "Curl Femoral"),
        "HOMBRO" to listOf("Press Militar", "Elevaciones Laterales", "Pájaros", "Face Pull"),
        "BRAZOS" to listOf("Curl con Barra", "Curl Martillo", "Tríceps Polea", "Press Francés"),
        "ABS/CARDIO" to listOf("Plancha", "Crunches", "Cinta de Correr", "Elíptica")
    )
    val allExercisesList = groupedExercises.values.flatten().sorted()
    fun getMuscleForExercise(name: String): String {
        groupedExercises.forEach { (m, l) -> if (l.any { it.equals(name, true) }) return m }
        return "OTROS"
    }
}

object GlobalUserManager {
    var name by mutableStateOf("Aaron")
    var username by mutableStateOf("cosoaaron16")
    var level by mutableStateOf("Nivel 5 - Círculo de la Ira")
    var followers by mutableIntStateOf(1)
    var following by mutableIntStateOf(1)
    var measurements = mutableStateListOf(BodyMeasurement("1 Ene 2026", "80.5", "180", "105", "40", "60"))
}

object GlobalFeedManager {
    val posts = mutableStateListOf(
        SocialPost(1, "Juan", "Pecho Legendario", 120, "2h"),
        SocialPost(2, "Ana", "Pierna Mortal", 85, "5h")
    )
    fun shareWorkout(w: WorkoutHistoryItem) {
        posts.add(0, SocialPost(System.currentTimeMillis(), GlobalUserManager.username, "${w.routineName} (${w.volume}kg)", 0, "Ahora"))
    }
    fun toggleLike(p: SocialPost) {
        val i = posts.indexOfFirst { it.id == p.id }
        if (i != -1) {
            val c = posts[i]
            posts[i] = c.copy(isLiked = !c.isLiked, likes = if (c.isLiked) c.likes - 1 else c.likes + 1)
        }
    }
}

object GlobalHistoryManager {
    val history = mutableStateListOf<WorkoutHistoryItem>()
    fun hasWorkoutOnDay(day: Int): Boolean {
        val c = Calendar.getInstance()
        return history.any {
            val cal = Calendar.getInstance()
            cal.timeInMillis = it.date
            cal.get(Calendar.DAY_OF_MONTH) == day && cal.get(Calendar.MONTH) == c.get(Calendar.MONTH)
        }
    }
    fun getExerciseStats(name: String): Triple<Int, Int, Int> {
        var max = 0; var vol = 0; var times = 0
        history.forEach { w ->
            w.completedExercises.filter { it.name == name }.forEach {
                times++
                it.sets.forEach { s ->
                    if (s.isCompleted) {
                        val wg = s.weight.toIntOrNull() ?: 0
                        if (wg > max) max = wg
                        vol += (wg * (s.reps.toIntOrNull() ?: 0))
                    }
                }
            }
        }
        return Triple(max, vol, times)
    }
    fun getMusclesWorkedInPeriod(days: Int): List<String> {
        val cutoff = System.currentTimeMillis() - (days * 86400000L)
        val m = mutableSetOf<String>()
        history.filter { it.date >= cutoff }.forEach { w ->
            w.completedExercises.forEach { m.add(InternalExerciseRepo.getMuscleForExercise(it.name)) }
        }
        return m.toList()
    }
}

object GlobalRoutineManager {
    val routines = mutableStateListOf(
        Routine("1", "Pierna (Leg Day)", "Sentadilla, Prensa...", listOf("Sentadilla", "Prensa 45º")),
        Routine("2", "Espalda", "Jalón, Remo...", listOf("Jalón al Pecho", "Remo en Polea")),
        Routine("3", "Pecho", "Press Banca...", listOf("Press de Banca", "Aperturas"))
    )
    var routineToEdit: Routine? = null
    fun addRoutine(n: String, e: List<String>) { routines.add(Routine(System.currentTimeMillis().toString(), n, e.take(3).joinToString(", "), e)) }
    fun updateRoutine(id: String, n: String, e: List<String>) {
        val i = routines.indexOfFirst { it.id == id }
        if (i != -1) routines[i] = routines[i].copy(name = n, exerciseNames = e, description = e.take(3).joinToString(", "))
    }
    fun deleteRoutine(r: Routine) { routines.remove(r) }
}

object GlobalWorkoutManager {
    var isActive by mutableStateOf(false); var isPaused by mutableStateOf(false); var currentRoutineName by mutableStateOf("")
    val exercises = mutableStateListOf<ActiveExercise>(); var durationSeconds by mutableLongStateOf(0L)
    fun startWorkout(n: String, e: List<ActiveExercise>) {
        if (isActive && currentRoutineName == n) return
        currentRoutineName = n; exercises.clear(); exercises.addAll(e); isActive = true; isPaused = false; durationSeconds = 0
    }
    fun stopWorkout() { isActive = false; isPaused = false; exercises.clear(); durationSeconds = 0 }
    fun pauseWorkout() { isPaused = true }; fun resumeWorkout() { isPaused = false }
}